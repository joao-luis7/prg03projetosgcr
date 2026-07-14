/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.produto.entity;

import br.com.ifba.prg03projetosgcr.categoria.entity.Categoria;
import br.com.ifba.prg03projetosgcr.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
/**
 *
 * @author joaol
 */
@Entity
@Table(name="produtos")
@Getter
@Setter
@NoArgsConstructor
public class Produto extends PersistenceEntity{
    
    @Column(nullable = false)
    private String nome;

    @Column
    private String descricao;

    @Column(name = "preco_unidade", nullable = false)
    private double precoUnidade;

    @Column(name = "preco_custo")
    private double precoCusto;

    @Column(name = "quantidade_estoque", nullable = false)
    private int quantidadeEstoque;

    @Column(nullable = false)
    private boolean ativo = true; // Por padrão, um produto novo já nasce ativo
    
    // Nova associação com Categoria mapeada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
