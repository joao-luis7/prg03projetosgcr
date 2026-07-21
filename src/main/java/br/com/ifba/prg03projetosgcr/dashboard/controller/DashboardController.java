/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.dashboard.controller;

import br.com.ifba.prg03projetosgcr.dashboard.service.DashboardService;
import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import java.util.List;
/**
 *
 * @author joaol
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final DashboardService dashboardService;

    public Double obterFaturamentoDoDia() {
        log.debug("Requisição para obter faturamento do dia recebida no controller");
        return dashboardService.obterFaturamentoDoDia();
    }

    public Long obterTotalVendasDoDia() {
        log.debug("Requisição para obter total de vendas do dia recebida no controller");
        return dashboardService.obterTotalVendasDoDia();
    }

    public List<Produto> obterProdutosComEstoqueBaixo() {
        log.debug("Requisição para obter produtos com estoque baixo recebida no controller");
        return dashboardService.obterProdutosComEstoqueBaixo();
    }
    
    public Long obterTotalClientesAtivos() {
        log.debug("Requisição para obter total de clientes ativos recebida no controller");
        return dashboardService.obterTotalClientesAtivos();
    }
}
