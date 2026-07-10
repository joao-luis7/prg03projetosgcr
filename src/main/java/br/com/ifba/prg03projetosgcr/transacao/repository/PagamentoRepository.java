/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.transacao.repository;

import br.com.ifba.prg03projetosgcr.transacao.entity.Pagamento;
import java.time.LocalDateTime;
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
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{
    @Override
    @EntityGraph(attributePaths = "cliente")
    List<Pagamento> findAll();
    
    //@EntityGraph resolve a query em um único JOIN antecipado com Cliente
    @EntityGraph(attributePaths = "cliente")
    List<Pagamento> findByClienteId(Long clienteId);
    
    //Metodo padrão para casos de uso que exigem o Pagamento detalhado com os dados do Cliente
    @EntityGraph(attributePaths = "cliente")
    @Query("SELECT p FROM Pagamento p WHERE p.id = :id")
    Optional<Pagamento> findByIdComCliente(Long id);
    
    // Faz a soma direto no banco de dados para máxima performance
    @Query("SELECT SUM(p.valorTotal) FROM Pagamento p WHERE p.dataHora >= :dataLimite")
    Double somarFaturamentoDesde(@Param("dataLimite") LocalDateTime dataLimite);
}
