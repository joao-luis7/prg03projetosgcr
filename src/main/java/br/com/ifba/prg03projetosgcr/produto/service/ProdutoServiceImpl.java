/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.produto.service;

import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import br.com.ifba.prg03projetosgcr.produto.repository.ProdutoRepository;
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
public class ProdutoServiceImpl implements ProdutoService{
    
    private final ProdutoRepository produtoRepository;
    
    private void validarProduto(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (produto.getPrecoUnidade() <= 0) {
            throw new IllegalArgumentException("O preço de venda deve ser maior que zero.");
        }
        if (produto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }
    }
    
    @Override
    @Transactional
    public Produto save(Produto produto) {
        log.info("Salvando novo produto: {}", produto.getNome());
        validarProduto(produto);
        return produtoRepository.save(produto);
    }

    @Override
    @Transactional
    public Produto update(Produto produto) {
        log.info("Atualizando produto ID: {}", produto.getId());
        findById(produto.getId()); // Garante que o produto existe
        validarProduto(produto);
        return produtoRepository.save(produto);    
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deletando produto ID: {}", id);
        Produto produto = findById(id);
        produtoRepository.delete(produto);    
    
    }

    @Override
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    @Override
    public Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + id));    }

    @Override
    public List<Produto> findByNome(String nome) {
       if (nome == null || nome.trim().isEmpty() || nome.equals("Pesquisar...")) {
            return findAll();
        }
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }
    
}
