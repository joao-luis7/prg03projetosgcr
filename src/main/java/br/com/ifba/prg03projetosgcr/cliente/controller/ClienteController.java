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
/**
 *
 * @author joaol
 */
@Controller
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    public Cliente save(Cliente cliente) {
        return clienteService.save(cliente);
    }

    public List<Cliente> findAll() {
        return clienteService.findAll();
    }

    public Cliente update(Cliente cliente) {
        return clienteService.update(cliente);
    }

    public void delete(Long id) {
        clienteService.delete(id);
    }

    public List<Cliente> findByNome(String nome) {
        return clienteService.findByNome(nome);
    }
}
