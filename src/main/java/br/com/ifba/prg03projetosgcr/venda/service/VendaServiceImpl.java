/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.service;

import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import br.com.ifba.prg03projetosgcr.cliente.service.ClienteService;
import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import br.com.ifba.prg03projetosgcr.produto.service.ProdutoService;
import br.com.ifba.prg03projetosgcr.pagamento.entity.FormaPagamento;
import br.com.ifba.prg03projetosgcr.venda.entity.ItemVenda;
import br.com.ifba.prg03projetosgcr.venda.entity.enums.StatusVenda;
import br.com.ifba.prg03projetosgcr.venda.entity.Venda;
import br.com.ifba.prg03projetosgcr.venda.repository.VendaRepository;
import br.com.ifba.prg03projetosgcr.infrastructure.exception.RegraNegocioException;
import br.com.ifba.prg03projetosgcr.util.ValidatorUtil;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author joaol
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VendaServiceImpl implements VendaService{
    
    private final VendaRepository vendaRepository;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final ValidatorUtil validatorUtil;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Venda realizarVenda(Venda venda) {
        log.info("Iniciando o processamento de uma nova venda.");
       
        validatorUtil.validateListaNotEmpty(venda.getItens(), "O carrinho está vazio, adicione produtos antes de finalizar");
        
        //LOGICA AUTOMATICA: CLIENTE NÃO IDENTIFICADO
        if (venda.getCliente() == null) {
            log.debug("Venda sem cliente. Vinculando 'Não Identificado'...");
            
            // Busca se o cliente invisível já existe no banco
            List<Cliente> clientesPadrao = clienteService.findByNome("Não Identificado");
            Cliente clientePadrao;
            
            if (clientesPadrao.isEmpty()) {
                // Se não existir, o sistema cria ele sozinho silenciosamente
                clientePadrao = new Cliente();
                clientePadrao.setNome("Não Identificado");
                clientePadrao.setTelefone("00000000000");
                clientePadrao.setAtivo(true);
                
                clientePadrao = clienteService.save(clientePadrao);
                log.info("Cliente 'Não Identificado' criado no banco de dados automaticamente.");
            } else {
                clientePadrao = clientesPadrao.get(0);
            }
            
            // Vincula à venda atual
            venda.setCliente(clientePadrao);
        }
        
        double totalVenda = 0.0;
        
        //Processa cada item do carrinho
        for(ItemVenda item : venda.getItens()){
            totalVenda += item.getSubtotal();
            
            log.debug("Processando item. Verificando estoque do produto ID: {}", item.getProduto().getId()); 
            Produto p = produtoService.findById(item.getProduto().getId());
            if(p.getQuantidadeEstoque() < item.getQuantidade()){
                log.warn("Operação abortada por regra de negócio: Estoque insuficiente para o produto '{}'. Disponível: {}, Solicitado: {}", 
                        p.getNome(), p.getQuantidadeEstoque(), item.getQuantidade()); 
                throw new RegraNegocioException("Estoque insuficiente para o produto: " + p.getNome());
            }
            
            int estoqueAnterior = p.getQuantidadeEstoque();
            p.setQuantidadeEstoque(estoqueAnterior - item.getQuantidade());
            log.debug("Abatendo estoque de '{}'. Estoque anterior: {}, Novo estoque: {}", p.getNome(), estoqueAnterior, p.getQuantidadeEstoque()); 
            produtoService.update(p); 
        }
        
        //preenche os dados finais da transação
        venda.setValorTotal(totalVenda);
        venda.setDataHora(LocalDateTime.now());
        venda.setStatusVenda(StatusVenda.FINALIZADA);
        
       //Se for FIADO, atualiza a divida do cliente
       if(venda.getFormaPagamento() == FormaPagamento.FIADO){
           validatorUtil.validateListaNotEmpty(venda.getItens(), "O carrinho está vazio, adicione produtos antes de finalizar");
           
           log.debug("Venda configurada como FIADO. Ajustando limite e dívida do cliente ID: {}", venda.getCliente().getId()); 
           Cliente cliente = clienteService.findById(venda.getCliente().getId());
           double saldoAnterior = cliente.getSaldoDevedor();
           cliente.setSaldoDevedor(cliente.getSaldoDevedor() + totalVenda);
           log.debug("Saldo devedor do cliente ID {} modificado. Antigo: R$ {}, Novo: R$ {}", cliente.getId(), saldoAnterior, cliente.getSaldoDevedor());
           clienteService.update(cliente);
       }
       log.info("Venda processada com sucesso. Salvando no banco de dados. Total: R$ {}", totalVenda);
       //salva a venda e todos os itens
       return vendaRepository.save(venda);
    }
    
    @Override
    public List<Venda> findAll() {
        log.debug("Executando consulta geral de vendas cadastradas.");
        return vendaRepository.findAll();
    }
    
    @Override
    @Transactional // Garante que, se der erro no meio, o banco desfaz tudo (Rollback)
    public void cancelarVenda(Long id){
        log.info("Iniciando requisição de cancelamento para a venda ID: {}", id); 
        //Busca a venda no banco
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cancelamento cancelado: Venda ID {} inexistente no sistema.", id); 
                    return new IllegalArgumentException("Venda não encontrada no sistema.");
                        });

        //Proteção: Verifica se já foi cancelada antes
        if (venda.getStatusVenda() == StatusVenda.CANCELADA) {
            log.warn("Cancelamento rejeitado: Venda ID {} já está no status CANCELADA.", id); 
            throw new RegraNegocioException("Esta venda já consta como CANCELADA!");
        }

        // Se foi vendido fiado, precisamos devolver o limite/diminuir a dívida do cliente
        if (venda.getFormaPagamento() == FormaPagamento.FIADO && venda.getCliente() != null) {
            Cliente cliente = venda.getCliente();
            double saldoAnterior = cliente.getSaldoDevedor();
            double novoSaldo = cliente.getSaldoDevedor() - venda.getValorTotal();
            
            // Garante que o saldo não fique negativo por algum erro de arredondamento
            cliente.setSaldoDevedor(Math.max(novoSaldo, 0.0)); 
            log.debug("Estorno de FIADO efetuado no cliente ID {}. Saldo devedor reduzido de R$ {} para R$ {}",
                    cliente.getId(), saldoAnterior, cliente.getSaldoDevedor());
            
        }
        
        for (ItemVenda item : venda.getItens()) {
            
            // Pega o produto daquele item
            Produto produto = item.getProduto();
            
            // Soma a quantidade cancelada de volta ao estoque
            int estoqueAtual = produto.getQuantidadeEstoque();
            int quantidadeDevolvida = item.getQuantidade();
            
            produto.setQuantidadeEstoque(estoqueAtual + quantidadeDevolvida); // Ajuste o nome do 'set' se necessário
            log.debug("Estorno de estoque realizado. Produto: '{}', Quantidade devolvida: {}, Novo estoque: {}",
                    produto.getNome(), quantidadeDevolvida, produto.getQuantidadeEstoque()); 
            // Salva o produto atualizado no banco
            produtoService.update(produto);
        }
        
        venda.setStatusVenda(StatusVenda.CANCELADA);
        log.info("Cancelamento concluído com sucesso para a venda ID: {}.", id); 
        vendaRepository.save(venda);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Venda findByIdComItens(Long id) {
        log.debug("Executando busca explícita da venda ID: {} com carregamento de itens.", id);
        return vendaRepository.findByIdComItens(id)
                .orElseThrow(() -> {
                    log.warn("Busca com itens mal-sucedida: Venda ID {} não existe.", id);
                    return new IllegalArgumentException("Venda não encontrada!");
                });
    }
}
