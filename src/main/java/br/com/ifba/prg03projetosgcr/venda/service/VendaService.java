/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.service;

import br.com.ifba.prg03projetosgcr.venda.entity.Venda;
import java.util.List;

/**
 *
 * @author joaol
 */
public interface VendaService {
    Venda realizarVenda(Venda venda);
    List<Venda> findAll();
    void cancelarVenda(Long id);
    Venda findByIdComItens(Long id);
}
