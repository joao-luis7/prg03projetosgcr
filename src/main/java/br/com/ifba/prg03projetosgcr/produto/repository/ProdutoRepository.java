/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.produto.repository;

import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
/**
 *
 * @author joaol
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
        List<Produto> findByNomeContainingIgnoreCase(String nome);
        
        List<Produto> findByAtivoTrue();
        
        @EntityGraph(attributePaths = {"categoria"})
        List<Produto> findAllComCategoriaByAtivoTrue();
        
        @EntityGraph(attributePaths = {"categoria"})
        List<Produto> findAll();
}
