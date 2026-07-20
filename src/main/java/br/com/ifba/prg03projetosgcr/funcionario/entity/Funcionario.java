/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.funcionario.entity;

import br.com.ifba.prg03projetosgcr.funcionario.entity.enums.PerfilAcesso;
import br.com.ifba.prg03projetosgcr.infrastructure.entity.PersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 *
 * @author joaol
 */
@Entity
@Table(name = "funcionario")
@Getter
@Setter
@NoArgsConstructor
public class Funcionario extends PersistenceEntity{
    
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilAcesso perfil;

    @Column(nullable = false)
    private boolean ativo = true;
}
