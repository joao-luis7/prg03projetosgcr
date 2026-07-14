/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.categoria.service;

import br.com.ifba.prg03projetosgcr.categoria.entity.Categoria;
import br.com.ifba.prg03projetosgcr.categoria.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
/**
 *
 * @author joaol
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoriaServiceImpl implements CategoriaService{

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public Categoria save(Categoria categoria) {
        log.info("A salvar nova categoria: {}", categoria.getNome());
        
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            log.warn("Tentativa de salvar categoria sem nome");
            throw new IllegalArgumentException("O nome da categoria não pode estar vazio.");
        }
        
        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public Categoria update(Categoria categoria) {
        log.info("A atualizar a categoria com ID: {}", categoria.getId());
        
        if (categoria.getId() == null) {
            log.warn("Tentativa de atualizar categoria sem ID");
            throw new IllegalArgumentException("O ID da categoria não pode ser nulo na atualização.");
        }
        
        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("A processar a inativação/deleção da categoria com ID: {}", id);
        
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Falha na deleção: Categoria com ID {} não encontrada", id);
                    return new IllegalArgumentException("Categoria não encontrada para o ID: " + id);
                });
        
        // Soft delete: inativa a entidade via setter da própria Service
        categoria.setAtivo(false);
        categoriaRepository.save(categoria);
        
        log.info("Categoria com ID {} inativada com sucesso", id);
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        log.debug("A procurar categoria pelo ID: {}", id);
        return categoriaRepository.findById(id);
    }

    @Override
    public List<Categoria> findAll() {
        log.debug("A listar todas as categorias");
        return categoriaRepository.findAll();
    }

    @Override
    public List<Categoria> findAllAtivas() {
        log.debug("A listar todas as categorias ativas");
        return categoriaRepository.findByAtivoTrue(); 
    }
    
}
