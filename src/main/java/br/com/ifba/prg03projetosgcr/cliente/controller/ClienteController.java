/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.cliente.controller;

import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import br.com.ifba.prg03projetosgcr.cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 *
 * @author joaol
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class ClienteController {
    
    private final ClienteService clienteService;

    public Cliente save(Cliente cliente) {
        log.info("Requisição para salvar cliente recebida no controller");
        return clienteService.save(cliente);
    }

    public List<Cliente> findAll() {
        log.debug("Requisição para listar todos os clientes recebida no controller");
        return clienteService.findAll();
    }

    public Cliente update(Cliente cliente) {
        log.info("Requisição para atualizar cliente recebida no controller");
        return clienteService.update(cliente);
    }

    public void delete(Long id) {
        log.info("Requisição para deletar cliente ID: {} recebida no controller", id);
        clienteService.delete(id);
    }

    public List<Cliente> findByNome(String nome) {
        log.debug("Requisição para buscar clientes por nome: {} recebida no controller", nome);
        return clienteService.findByNome(nome);
    }
    
    public Cliente findById(Long id) {
        return clienteService.findById(id);
    }
}
