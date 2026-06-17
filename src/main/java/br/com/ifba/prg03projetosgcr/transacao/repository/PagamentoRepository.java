/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.transacao.repository;

import br.com.ifba.prg03projetosgcr.transacao.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 *
 * @author joaol
 */
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{
    // Traz todo o histórico de pagamentos de um cliente específico!
    List<Pagamento> findByClienteId(Long clienteId);
}
