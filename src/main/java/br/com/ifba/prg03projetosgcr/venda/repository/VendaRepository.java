/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.repository;

import br.com.ifba.prg03projetosgcr.venda.entity.enums.StatusVenda;
import br.com.ifba.prg03projetosgcr.venda.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 *
 * @author joaol
 */
@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    
    @Override
    @EntityGraph(attributePaths = "cliente")
    List<Venda> findAll();
    
    // Otimizado para que a listagem de vendas na view já tenha os nomes dos clientes carregados
    @EntityGraph(attributePaths = "cliente")
    List<Venda> findByStatusVenda(StatusVenda statusVenda);
    
    //Fetch explícito do Cliente (herdado de Transacao)
    @EntityGraph(attributePaths = "cliente")
    @Query("SELECT v FROM Venda v WHERE v.id = :id")        
    Optional<Venda> findByIdComCliente(Long id);
    
    // NOVO MÉTODO: Faz o fetch (carregamento antecipado) dos itens na mesma consulta
    @Query("SELECT v FROM Venda v JOIN FETCH v.cliente JOIN FETCH v.itens i JOIN FETCH i.produto WHERE v.id = :id")
    Optional<Venda> findByIdComItens(@Param("id") Long id);
    
    // preventivo caso a View exija o relatório completo de Cliente + Itens da Venda
    @Query("SELECT v FROM Venda v JOIN FETCH v.cliente JOIN FETCH v.itens WHERE v.id = :id")
    Optional<Venda> findByIdComClienteEItens(@Param("id") Long id);
}
