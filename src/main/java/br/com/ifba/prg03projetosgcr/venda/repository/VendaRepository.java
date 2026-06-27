/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.repository;

import br.com.ifba.prg03projetosgcr.venda.entity.StatusVenda;
import br.com.ifba.prg03projetosgcr.venda.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 *
 * @author joaol
 */
@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    
    // Método para o sistema encontrar vendas em andamento ou finalizadas
    List<Venda> findByStatusVenda(StatusVenda statusVenda);
}
