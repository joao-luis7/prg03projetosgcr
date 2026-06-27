/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.controller;

import br.com.ifba.prg03projetosgcr.venda.entity.Venda;
import br.com.ifba.prg03projetosgcr.venda.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
/**
 *
 * @author joaol
 */
@Controller
@RequiredArgsConstructor
public class VendaController {
    
    private final VendaService vendaService;

    public Venda realizarVenda(Venda venda) throws Exception {
        return vendaService.realizarVenda(venda);
    }
}
