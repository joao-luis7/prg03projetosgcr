/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.produto.controller;

import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import br.com.ifba.prg03projetosgcr.produto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.List;
/**
 *
 * @author joaol
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProdutoController {
    
    private final ProdutoService produtoService;
    
    public Produto save(Produto produto) {
        log.info("Requisição recebida para salvar produto: {}", produto.getNome());
        return produtoService.save(produto);
    }

    public Produto update(Produto produto) {
        log.info("Requisição recebida para atualizar produto ID: {}", produto.getId());
        return produtoService.update(produto);
    }

    public void delete(Long id) {
        log.info("Requisição recebida para deletar produto ID: {}", id);
        produtoService.delete(id);
    }

    public List<Produto> findAll() {
        log.debug("Requisição recebida para buscar todos os produtos");
        return produtoService.findAll();
    }

    public Produto findById(Long id) {
        log.debug("Requisição recebida para buscar produto ID: {}", id);
        return produtoService.findById(id);
    }

    public List<Produto> findByNome(String nome) {
        log.debug("Requisição recebida para buscar produtos pelo nome: {}", nome);
        return produtoService.findByNome(nome);
    }
    
    public List<Produto> findAllAtivos() {
        log.debug("Requisição recebida para buscar todos os produtos ativos");
        return produtoService.findAllAtivos();
    }
}