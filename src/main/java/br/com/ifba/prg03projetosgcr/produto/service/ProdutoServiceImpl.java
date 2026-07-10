/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.produto.service;

import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import br.com.ifba.prg03projetosgcr.produto.repository.ProdutoRepository;
import br.com.ifba.prg03projetosgcr.util.ValidatorUtil;
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
    private final ValidatorUtil validatorUtil;
    
    private void validarProduto(Produto produto) {
        validatorUtil.validateStringNotNullOrEmpty(produto.getNome(), "nome do produto");
        validatorUtil.validateValorMaiorQueZero(produto.getPrecoUnidade(), "preço de venda");
        validatorUtil.validateNaoNegativo(produto.getQuantidadeEstoque(), "estoque");
        
        log.info("Validação do produto concluída com sucesso.");
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
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    @Override
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    @Override
    public Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + id));
    }

    @Override
    public List<Produto> findByNome(String nome) {
       if (nome == null || nome.trim().isEmpty()) {
            return findAll();
        }
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    @Override
    public List<Produto> findAllAtivos() {
        return produtoRepository.findByAtivoTrue(); 
    }
    
}
