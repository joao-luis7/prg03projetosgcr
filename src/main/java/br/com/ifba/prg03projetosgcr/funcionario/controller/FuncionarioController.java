/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.funcionario.controller;

import br.com.ifba.prg03projetosgcr.funcionario.entity.Funcionario;
import br.com.ifba.prg03projetosgcr.funcionario.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
/**
 *
 * @author joaol
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class FuncionarioController {
    
    private final FuncionarioService funcionarioService;
  
    public Funcionario autenticar(String username, String senha) {
        log.debug("Requisição de autenticação recebida no controller para o username: {}", username);
        return funcionarioService.autenticar(username, senha);
    }

    public void save(Funcionario funcionario) {
        log.info("Requisição para salvar funcionário recebida no controller: {}", funcionario.getUsername());
        funcionarioService.save(funcionario);
    }
}
