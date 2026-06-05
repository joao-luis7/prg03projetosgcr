/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.cliente.entity;

import br.com.ifba.prg03projetosgcr.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author joaol
 */
@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends PersistenceEntity{
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String telefone;
    
    @Column(nullable = false)
    private boolean ativo;
    
    @Column(name="saldo_devedor", nullable = false)
    private double saldoDevedor;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) //qualquer operacao em Cliente reflete
                                                               //auto no Endereco
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;
    
    public void inativarCliente(){
        this.ativo = false;
    }
    
    public void atualizarSaldo(double valor){
        this.saldoDevedor = valor;
    }

}
