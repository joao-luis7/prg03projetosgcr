/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.controller;

import br.com.ifba.prg03projetosgcr.venda.entity.Venda;
import br.com.ifba.prg03projetosgcr.venda.service.VendaService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
/**
 *
 * @author joaol
 */
@Slf4j 
@RequiredArgsConstructor
@Controller
public class VendaController {
    
    private final VendaService vendaService;
    
    public Venda realizarVenda(Venda venda) {
        log.info("Requisição recebida para realizar nova venda");
        return vendaService.realizarVenda(venda);
    }
    
    public List<Venda> findAll() {
        log.debug("Requisição recebida para buscar todas as vendas"); 
        return vendaService.findAll();
    }
    
    public void cancelarVenda(Long id) {
        log.info("Requisição recebida para cancelar a venda ID: {}", id);
        vendaService.cancelarVenda(id);
    }
    
    public Venda findByIdComItens(Long id) {
        log.debug("Requisição recebida para buscar venda detalhada (com itens) ID: {}", id);
        return vendaService.findByIdComItens(id);
    }
}
