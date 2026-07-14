/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.categoria.controller;

import br.com.ifba.prg03projetosgcr.categoria.entity.Categoria;
import br.com.ifba.prg03projetosgcr.categoria.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
/**
 *
 * @author joaol
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class CategoriaController {
    
    private final CategoriaService categoriaService;

    public Categoria save(Categoria categoria) {
        log.info("Iniciando salvamento da categoria: {}", categoria.getNome());
        return categoriaService.save(categoria);
    }

    public Categoria update(Categoria categoria) {
        log.info("Iniciando atualização da categoria com ID: {}", categoria.getId());
        return categoriaService.update(categoria);
    }

    public void delete(Long id) {
        log.info("Iniciando deleção da categoria com ID: {}", id);
        categoriaService.delete(id);
    }

    public Optional<Categoria> findById(Long id) {
        log.debug("Buscando categoria por ID: {}", id);
        return categoriaService.findById(id);
    }

    public List<Categoria> findAll() {
        log.debug("Buscando todas as categorias cadastradas");
        return categoriaService.findAll();
    }

    public List<Categoria> findAllAtivas() {
        log.debug("Buscando todas as categorias ativas para exibição");
        return categoriaService.findAllAtivas();
    }
}
