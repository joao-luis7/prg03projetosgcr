/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.transacao.service;

import br.com.ifba.prg03projetosgcr.transacao.entity.Pagamento;
import br.com.ifba.prg03projetosgcr.transacao.repository.PagamentoRepository;
import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import br.com.ifba.prg03projetosgcr.cliente.service.ClienteService;
import br.com.ifba.prg03projetosgcr.transacao.entity.FormaPagamento;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 *
 * @author joaol
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class PagamentoServiceImpl implements PagamentoService{
    
    private final PagamentoRepository pagamentoRepository;
    
    // Injetamos o ClienteService para poder alterar o saldo devedor do cliente
    private final ClienteService clienteService;
    
    @Override
    @Transactional
    public Pagamento registrarPagamento(Pagamento pagamento) {
        log.info("Iniciando registro de pagamento no valor de R$ {}", pagamento.getValorTotal());

        // Validações de segurança para não quebrar o caixa
        if (pagamento.getValorTotal() <= 0) {
            throw new IllegalArgumentException("Operação recusada: O valor do pagamento deve ser maior que zero.");
        }
        
        if (pagamento.getCliente() == null || pagamento.getCliente().getId() == null) {
            throw new IllegalArgumentException("Operação recusada: O pagamento deve estar vinculado a um cliente válido.");
        }
        
        //Injeta o momento exato em que o pagamento está sendo feito
        pagamento.setDataHora(java.time.LocalDateTime.now());

        // busca o cliente novo direto do banco de dados
        Cliente clienteNoBanco = clienteService.findById(pagamento.getCliente().getId());
        double saldoAtual = clienteNoBanco.getSaldoDevedor();
        
        //O cliente já está quite?
        if(saldoAtual <= 0){
            log.warn("Tentativa de pagamento para cleinte sem divida. ID Cleitne: {}", clienteNoBanco.getId());
            throw new IllegalArgumentException("Operação recusada: o cliente não possui dívida");
        }
        
        //Está pagando mais do que deve e não é fiado?
        if (pagamento.getValorTotal() > saldoAtual && pagamento.getFormaPagamento() != FormaPagamento.FIADO) {
            log.warn("Tentativa de pagamento maior que o saldo devedor. Valor: {}, Saldo: {}", pagamento.getValorTotal(), saldoAtual);
            throw new IllegalArgumentException(String.format("Operação recusada: O valor do pagamento (R$ %.2f) não pode ser maior que o saldo devedor (R$ %.2f).", pagamento.getValorTotal(), saldoAtual));
        }
        
        double novoSaldo = saldoAtual;
        
        //So abate o valor pago se a forma nao for FIADO
        if(pagamento.getFormaPagamento() != FormaPagamento.FIADO){
            novoSaldo = saldoAtual - pagamento.getValorTotal();
            clienteNoBanco.setSaldoDevedor(novoSaldo);
            clienteService.update(clienteNoBanco);
            log.debug("Saldo do cliente abatido. Novo saldo devedor: R$ {}", novoSaldo);
        } else{
            log.info("Pagamento registrado como FIADO. Saldo não foi alterado");
        }
        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);
        log.info("Pagamento finalizado com sucesso! ID Recibo: {}. Saldo atual do Cliente: R$ {}", 
                pagamentoSalvo.getId(), novoSaldo);
        return pagamentoSalvo;
    }
    
    @Override
    public List<Pagamento> findAll() {
        log.debug("Buscando todos os pagamentos registrados");
        return pagamentoRepository.findAll();
    }

    @Override
    public Pagamento findById(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recibo de pagamento não encontrado com o ID: " + id));
    }

    @Override
    public List<Pagamento> findByClienteId(Long idCliente) {
        log.debug("Buscando histórico de pagamentos do cliente ID: {}", idCliente);
        return pagamentoRepository.findByClienteId(idCliente); // Esse é aquele método automático do Repository que criamos!
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Iniciando exclusão do pagamento ID: {}", id);
        
        Pagamento pagamento = findById(id);
        
        //Busca o cliente vinculado a esse pagamento
        Cliente cliente = clienteService.findById(pagamento.getCliente().getId());
        
        //Devolve a dívida para o cliente (soma o valor que havia sido abatido)
        double saldoRestaurado = cliente.getSaldoDevedor();
        
        // Devolve a dívida para o cliente APENAS se o pagamento excluído não for FIADO
        if (pagamento.getFormaPagamento() != FormaPagamento.FIADO) {
            saldoRestaurado = cliente.getSaldoDevedor() + pagamento.getValorTotal();
            cliente.setSaldoDevedor(saldoRestaurado);
            clienteService.update(cliente);
            log.info("Pagamento deletado. Saldo do cliente restaurado para R$ {}", saldoRestaurado);
        } else {
            log.info("Pagamento do tipo FIADO deletado. Saldo do cliente mantido intacto.");
        }
        
        pagamentoRepository.delete(pagamento);
    }

    @Override
    @Transactional
    public Pagamento update(Pagamento pagamentoAtualizado) {
        log.info("Atualizando pagamento ID: {}", pagamentoAtualizado.getId());
        
        // Busca como o pagamento estava ANTES de ser alterado
        Pagamento pagamentoAntigo = findById(pagamentoAtualizado.getId());
        
        //preserva a data e hora de quando o recibo foi emitido
        pagamentoAtualizado.setDataHora(pagamentoAntigo.getDataHora());
        
        // DESFAZ O PAGAMENTO ANTIGO (Devolve o saldo para o cliente antigo)
        if (pagamentoAntigo.getFormaPagamento() != FormaPagamento.FIADO) {
            Cliente clienteAntigo = clienteService.findById(pagamentoAntigo.getCliente().getId());
            clienteAntigo.setSaldoDevedor(clienteAntigo.getSaldoDevedor() + pagamentoAntigo.getValorTotal());
            clienteService.update(clienteAntigo);
            log.debug("Valores originais restaurados no saldo do cliente (ID: {}) antes de aplicar atualização.", clienteAntigo.getId());
        }
        
        // APLICA O NOVO PAGAMENTO (apenas se a nova forma não for FIADO)
        if(pagamentoAtualizado.getFormaPagamento() != FormaPagamento.FIADO){
            Cliente clienteNovo = clienteService.findById(pagamentoAtualizado.getCliente().getId());
            
            //valida pra nao abater mais do que o cliente deve 
            if(pagamentoAtualizado.getValorTotal() > clienteNovo.getSaldoDevedor()){
                log.warn("Atualização recusada. O valor excede o saldo devedor do cliente.");
                throw new IllegalArgumentException("O valor editado excede o saldo devedor");
            }
            
            clienteNovo.setSaldoDevedor(clienteNovo.getSaldoDevedor() - pagamentoAtualizado.getValorTotal());
            clienteService.update(clienteNovo);
            log.debug("Novos valores abatidos do saldo do cliente (ID: {}", clienteNovo.getId()); 
        }
        
        Pagamento salvo = pagamentoRepository.save(pagamentoAtualizado);
        log.info("Pagamento atualizado com sucesso");
        
        return salvo;
    }
    
    @Override
    public Double calcularFaturamentoUltimos30Dias() {
        // 1. Descobre a data e hora de exatos 30 dias atrás
        java.time.LocalDateTime trintaDiasAtras = java.time.LocalDateTime.now().minusDays(30);
        
        // 2. Manda o banco somar
        Double soma = pagamentoRepository.somarFaturamentoDesde(trintaDiasAtras);
        
        // 3. Se for null (zero vendas no período), devolve 0.0
        return soma != null ? soma : 0.0;
    }
}
