/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.funcionario.repository;

import br.com.ifba.prg03projetosgcr.funcionario.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 *
 * @author joaol
 */
@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{
    Optional<Funcionario> findByUsername(String username);
}
