/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.dashboard.service;

import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import java.util.List;
/**
 *
 * @author joaol
 */
public interface DashboardService {
    Double obterFaturamentoDoDia();
    Long obterTotalVendasDoDia();
    List<Produto> obterProdutosComEstoqueBaixo();
    Long obterTotalClientesAtivos();
}
