/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.entity;

import br.com.ifba.prg03projetosgcr.venda.entity.enums.StatusVenda;
import br.com.ifba.prg03projetosgcr.transacao.entity.enums.FormaPagamento;
import br.com.ifba.prg03projetosgcr.transacao.entity.Transacao;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
/**
 *
 * @author joaol
 */
@Entity
@Table(name = "venda")
@Getter
@Setter
@NoArgsConstructor
public class Venda extends Transacao{
    // Salvamos o status como texto puro para proteger o banco de dados
    @Enumerated(EnumType.STRING)
    @Column(name = "status_venda", nullable = false)
    private StatusVenda statusVenda = StatusVenda.ABERTA;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    private FormaPagamento formaPagamento;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemVenda> itens = new ArrayList<>();
}
