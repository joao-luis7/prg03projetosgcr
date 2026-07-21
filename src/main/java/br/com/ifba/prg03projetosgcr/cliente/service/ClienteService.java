/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.cliente.service;

import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import java.util.List;

/**
 *
 * @author joaol
 */
public interface ClienteService {
    
    Cliente save(Cliente cliente);
    
    List<Cliente> findByNome(String nome);
    
    List<Cliente> findAll();
    
    Cliente findById(Long id);

    Cliente update(Cliente cliente);
    
    void delete (Long id);
    
    void atualizarSaldo(Long id, double novoValor);
    
    Long contarClientesAtivosIdentificados();
}
