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
import br.com.ifba.prg03projetosgcr.infrastructure.exception.RegraNegocioException;
import java.time.LocalDateTime;
import java.util.List;
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
    public Venda realizarVenda(Venda venda) {
       
        if(venda.getItens() == null || venda.getItens().isEmpty()){
            throw new RegraNegocioException("O carrinho está vazio, adicione produtos antes de fianlizar");
        }
        
        double totalVenda = 0.0;
        
        //Processa cada item do carrinho
        for(ItemVenda item : venda.getItens()){
            totalVenda += item.getSubtotal();
            
            //Abate do estoque
            Produto p = produtoService.findById(item.getProduto().getId());
            if(p.getQuantidadeEstoque() < item.getQuantidade()){
                throw new RegraNegocioException("Estoque insuficiente para o produto: " + p.getNome());
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
               throw new RegraNegocioException("Para vender fiado, é obrigatório informar o cliente");
           }
           Cliente cliente = clienteService.findById(venda.getCliente().getId());
           cliente.setSaldoDevedor(cliente.getSaldoDevedor() + totalVenda);
           clienteService.update(cliente);
       }
       
       //salva a venda e todos os itens
       return vendaRepository.save(venda);
    }
    
    @Override
    public List<Venda> findAll() {
        return vendaRepository.findAll();
    }
    
    @Override
    @Transactional // Garante que, se der erro no meio, o banco desfaz tudo (Rollback)
    public void cancelarVenda(Long id){
        
        //Busca a venda no banco
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Venda não encontrada no sistema."));

        //Proteção: Verifica se já foi cancelada antes
        if (venda.getStatusVenda() == StatusVenda.CANCELADA) {
            throw new RegraNegocioException("Esta venda já consta como CANCELADA!");
        }

        // Se foi vendido fiado, precisamos devolver o limite/diminuir a dívida do cliente
        if (venda.getFormaPagamento() == FormaPagamento.FIADO && venda.getCliente() != null) {
            Cliente cliente = venda.getCliente();
            double novoSaldo = cliente.getSaldoDevedor() - venda.getValorTotal();
            
            // Garante que o saldo não fique negativo por algum erro de arredondamento
            cliente.setSaldoDevedor(Math.max(novoSaldo, 0.0)); 
            
        }
        
        for (ItemVenda item : venda.getItens()) {
            
            // Pega o produto daquele item
            Produto produto = item.getProduto();
            
            // Soma a quantidade cancelada de volta ao estoque
            int estoqueAtual = produto.getQuantidadeEstoque();
            int quantidadeDevolvida = item.getQuantidade();
            
            produto.setQuantidadeEstoque(estoqueAtual + quantidadeDevolvida); // Ajuste o nome do 'set' se necessário
            
            // Salva o produto atualizado no banco
            produtoService.update(produto);
        }
        
        venda.setStatusVenda(StatusVenda.CANCELADA);
        vendaRepository.save(venda);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Venda findByIdComItens(Long id) {
        // cerChama o novo método do repositório que já traz os itens
        return vendaRepository.findByIdComItens(id)
                .orElseThrow(() -> new RegraNegocioException("Venda não encontrada!"));
    }
}
