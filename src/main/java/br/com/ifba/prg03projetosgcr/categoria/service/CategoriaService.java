/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.categoria.service;

import br.com.ifba.prg03projetosgcr.categoria.entity.Categoria;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author joaol
 */
public interface CategoriaService {
    
    Categoria save(Categoria categoria);
    
    Categoria update(Categoria categoria);
    
    void delete(Long id);
    
    Optional<Categoria> findById(Long id);
    
    List<Categoria> findAll();
    
    List<Categoria> findAllAtivas();
}
