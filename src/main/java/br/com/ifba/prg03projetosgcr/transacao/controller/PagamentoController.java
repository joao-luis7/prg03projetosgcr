/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.transacao.controller;

import br.com.ifba.prg03projetosgcr.transacao.entity.Pagamento;
import br.com.ifba.prg03projetosgcr.transacao.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
/**
 *
 * @author joaol
 */
@RequiredArgsConstructor
@Slf4j
@Controller 
public class PagamentoController {
    private final PagamentoService pagamentoService;

    public Pagamento registrarPagamento(Pagamento pagamento) {
        log.info("Requisição para registrar pagamento recebida no controller. Valor: R$ {}", pagamento.getValorTotal());
        return pagamentoService.registrarPagamento(pagamento);
    }

    public List<Pagamento> findAll() {
        log.debug("Requisição para listar todos os pagamentos recebida no controller");
        return pagamentoService.findAll();
    }

    public Pagamento findById(Long id) {
        log.debug("Requisição para buscar pagamento ID: {} recebida no controller", id);
        return pagamentoService.findById(id);
    }
    
    public List<Pagamento> findByClienteId(Long idCliente) {
        log.debug("Requisição para buscar pagamentos do cliente ID: {} recebida no controller", idCliente);
        return pagamentoService.findByClienteId(idCliente);
    }
    
    public Pagamento update(Pagamento pagamento) {
        log.info("Requisição para atualizar pagamento ID: {} recebida no controller", pagamento.getId());
        return pagamentoService.update(pagamento);
    }

    public void delete(Long id) {
        log.info("Requisição para deletar pagamento ID: {} recebida no controller", id);
        pagamentoService.delete(id);
    }
    
    public Double getFaturamentoUltimos30Dias() {
        return pagamentoService.calcularFaturamentoUltimos30Dias();
    }
}
