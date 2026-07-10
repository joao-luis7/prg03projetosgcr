/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.repository;

import br.com.ifba.prg03projetosgcr.venda.entity.ItemVenda;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 *
 * @author joaol
 */
@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    
    //Buscar um item e carregar os dados do Produto antecipadamente
    @EntityGraph(attributePaths = "produto")
    @Query("SELECT i FROM ItemVenda i WHERE i.id = :id")
    Optional<ItemVenda> findByIdComProduto(Long id);
    
    // Buscar todos os itens de uma determinada venda
    @EntityGraph(attributePaths = "produto")
    @Query("SELECT i FROM ItemVenda i WHERE i.venda.id = :vendaId")
    List<ItemVenda> findByVendaIdComProduto(Long vendaId);
}
