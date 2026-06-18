/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.transacao.service;

import br.com.ifba.prg03projetosgcr.transacao.entity.Pagamento;
import br.com.ifba.prg03projetosgcr.transacao.repository.PagamentoRepository;
import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import br.com.ifba.prg03projetosgcr.cliente.service.ClienteService;
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
        // (Evita usar dados defasados caso a tela esteja aberta há muito tempo)
        Cliente clienteNoBanco = clienteService.findById(pagamento.getCliente().getId());

        // Abate o valor pago do saldo devedor atual
        double saldoAtual = clienteNoBanco.getSaldoDevedor();
        double novoSaldo = saldoAtual - pagamento.getValorTotal();
        
        clienteNoBanco.setSaldoDevedor(novoSaldo);

        // Salva as duas informações no banco (Protegido pelo @Transactional)
        clienteService.update(clienteNoBanco);
        
        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        log.info("Pagamento finalizado com sucesso! ID Recibo: {}. Novo saldo do cliente: R$ {}", 
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
        double saldoRestaurado = cliente.getSaldoDevedor() + pagamento.getValorTotal();
        cliente.setSaldoDevedor(saldoRestaurado);
        clienteService.update(cliente);
        
        // Agora sim, exclui o recibo de pagamento
        pagamentoRepository.delete(pagamento);
        log.info("Pagamento deletado. Saldo do cliente restaurado para R$ {}", saldoRestaurado);
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
        // Isso resolve o caso do usuário ter selecionado o cliente errado!
        Cliente clienteAntigo = clienteService.findById(pagamentoAntigo.getCliente().getId());
        clienteAntigo.setSaldoDevedor(clienteAntigo.getSaldoDevedor() + pagamentoAntigo.getValorTotal());
        clienteService.update(clienteAntigo);
        
        // APLICA O NOVO PAGAMENTO (Abate o saldo do cliente novo com o valor novo)
        // Se o usuário não errou o cliente, o "clienteNovo" será o mesmo cara, 
        // e a matemática vai bater perfeitamente no final.
        Cliente clienteNovo = clienteService.findById(pagamentoAtualizado.getCliente().getId());
        clienteNovo.setSaldoDevedor(clienteNovo.getSaldoDevedor() - pagamentoAtualizado.getValorTotal());
        clienteService.update(clienteNovo);
        
        // Salva o recibo com os novos dados (valor, forma de pagamento, etc)
        Pagamento salvo = pagamentoRepository.save(pagamentoAtualizado);
        log.info("Pagamento atualizado com sucesso.");
        
        return salvo;
    }
}
