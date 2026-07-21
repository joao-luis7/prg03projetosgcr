/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.dashboard.service;

import br.com.ifba.prg03projetosgcr.cliente.service.ClienteService;
import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import br.com.ifba.prg03projetosgcr.produto.service.ProdutoService;
import br.com.ifba.prg03projetosgcr.venda.service.VendaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 *
 * @author joaol
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DashboardServiceImpl implements DashboardService{

    private final VendaService vendaService;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;

    @Override
    @Transactional(readOnly = true)
    public Double obterFaturamentoDoDia() {
        log.debug("Buscando faturamento do dia no dashboard");
        return vendaService.calcularFaturamentoDoDia();
    }

    @Override
    @Transactional(readOnly = true)
    public Long obterTotalVendasDoDia() {
        log.debug("Buscando total de vendas do dia no dashboard");
        return vendaService.contarVendasDoDia();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> obterProdutosComEstoqueBaixo() {
        log.debug("Buscando lista de produtos com estoque baixo no dashboard");
        return produtoService.listarProdutosComEstoqueBaixo();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long obterTotalClientesAtivos() {
        log.debug("Buscando total de clientes ativos para o dashboard");
        return clienteService.contarClientesAtivosIdentificados();
    }
}
