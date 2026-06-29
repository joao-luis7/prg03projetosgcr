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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 *
 * @author joaol
 */
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{
    // Traz todo o histórico de pagamentos de um cliente específico!
    List<Pagamento> findByClienteId(Long clienteId);
    // Faz a soma direto no banco de dados para máxima performance
    @Query("SELECT SUM(p.valorTotal) FROM Pagamento p WHERE p.dataHora >= :dataLimite")
    Double somarFaturamentoDesde(@Param("dataLimite") LocalDateTime dataLimite);
}
