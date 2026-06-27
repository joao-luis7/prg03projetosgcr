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
        return produtoService.save(produto);
    }

    public Produto update(Produto produto) {
        return produtoService.update(produto);
    }

    public void delete(Long id) {
        produtoService.delete(id);
    }

    public List<Produto> findAll() {
        return produtoService.findAll();
    }

    public Produto findById(Long id) {
        return produtoService.findById(id);
    }

    public List<Produto> findByNome(String nome) {
        return produtoService.findByNome(nome);
    }

    public List<Produto> findAllAtivos() {
        return produtoService.findAllAtivos();
    }
}
