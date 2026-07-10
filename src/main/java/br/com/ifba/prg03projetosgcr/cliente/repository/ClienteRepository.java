/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.cliente.repository;

import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author joaol
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    // Busca TODOS (ativos e inativos)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    List<Cliente> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    
    List<Cliente> findByAtivoTrue();
    
}
