/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.entity;

import br.com.ifba.prg03projetosgcr.infrastructure.entity.PersistenceEntity;
import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
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
@Table(name="item_venda")
@Getter
@Setter
@NoArgsConstructor
public class ItemVenda extends PersistenceEntity{
    
    //todo item pertence a uma venda especifica
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="venda_id", nullable = false)
    private Venda venda;
    
    //todo item aponta para um produto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(nullable = false)
    private int quantidade;
    
    @Column(name="preco_unitario", nullable = false)
    private double precoUnitario;
    
    @Column(nullable = false)
    private double subtotal;
}
