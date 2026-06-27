/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.produto.service;

import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import java.util.List;
/**
 *
 * @author joaol
 */
public interface ProdutoService {
    Produto save(Produto produto);
    Produto update(Produto produto);
    void delete(Long id);
    List<Produto> findAll();
    Produto findById(Long id);
    List<Produto> findByNome(String nome);
    List<Produto> findAllAtivos();
}
