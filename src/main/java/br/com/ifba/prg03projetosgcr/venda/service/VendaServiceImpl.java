/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.service;

import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import br.com.ifba.prg03projetosgcr.cliente.service.ClienteService;
import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import br.com.ifba.prg03projetosgcr.produto.service.ProdutoService;
import br.com.ifba.prg03projetosgcr.transacao.entity.FormaPagamento;
import br.com.ifba.prg03projetosgcr.venda.entity.ItemVenda;
import br.com.ifba.prg03projetosgcr.venda.entity.StatusVenda;
import br.com.ifba.prg03projetosgcr.venda.entity.Venda;
import br.com.ifba.prg03projetosgcr.venda.repository.VendaRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author joaol
 */
@Service
@RequiredArgsConstructor
public class VendaServiceImpl implements VendaService{
    
    private final VendaRepository vendaRepository;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Venda realizarVenda(Venda venda) throws Exception {
       
        if(venda.getItens() == null || venda.getItens().isEmpty()){
            throw new Exception("O carrinho está vazio, adicione produtos antes de fianlizar");
        }
        
        double totalVenda = 0.0;
        
        //Processa cada item do carrinho
        for(ItemVenda item : venda.getItens()){
            totalVenda += item.getSubtotal();
            
            //Abate do estoque
            Produto p = produtoService.findById(item.getProduto().getId());
            if(p.getQuantidadeEstoque() < item.getQuantidade()){
                throw new Exception("Estoque insuficiente para o produto: " + p.getNome());
            }
            
            p.setQuantidadeEstoque(p.getQuantidadeEstoque() - item.getQuantidade());
            produtoService.update(p); //atualiza o estoque no banco
        }
        
        //preenche os dados finais da transação
        venda.setValorTotal(totalVenda);
        venda.setDataHora(LocalDateTime.now());
        venda.setStatusVenda(StatusVenda.FINALIZADA);
        
       //Se for FIADO, atualiza a divida do cliente
       if(venda.getFormaPagamento() == FormaPagamento.FIADO){
           if(venda.getCliente() == null){
               throw new Exception("Para vender fiado, é obrigatório informar o cliente");
           }
           Cliente cliente = clienteService.findById(venda.getCliente().getId());
           cliente.setSaldoDevedor(cliente.getSaldoDevedor() + totalVenda);
           clienteService.update(cliente);
       }
       
       //salva a venda e todos os itens
       return vendaRepository.save(venda);
    }
    
    
}
