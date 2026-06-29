/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.transacao.service;

import br.com.ifba.prg03projetosgcr.transacao.entity.Pagamento;
import java.util.List;
/**
 *
 * @author joaol
 */
public interface PagamentoService {
    
    Pagamento registrarPagamento(Pagamento pagamento);
    
    List<Pagamento> findAll();
    
    Pagamento findById(Long id);
    
    List<Pagamento> findByClienteId(Long idCliente);
    
    Pagamento update(Pagamento pagamento);
    
    void delete(Long id);
    
    public Double calcularFaturamentoUltimos30Dias();
}
