/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.cliente.service;

import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import br.com.ifba.prg03projetosgcr.cliente.repository.ClienteRepository;
import br.com.ifba.prg03projetosgcr.util.ValidatorUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 *
 * @author joaol
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ClienteServiceImpl implements ClienteService{
     
    private final ClienteRepository clienteRepository;
    private final ValidatorUtil validatorUtil;

    
    private Cliente findById(Long id){
        return clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado. ID: " + id));
    }
    
    // MÉTODO AUXILIAR: Concentra todas as regras de validação do Cliente
    private void validarCliente(Cliente cliente){
        log.info("Iniciando validação do cliente");
        
        //verificar nome
        validatorUtil.validateStringNotNullOrEmpty(cliente.getNome(), "nome do cliente");
        //verifica telefone
        validatorUtil.validateStringNotNullOrEmpty(cliente.getTelefone(), "telefone do cliente");
        //validação do Telefone (deve conter 11 digitos DDD + numero)
        validatorUtil.validateNumericString(cliente.getTelefone(), "telefone", 11);

        //validação do Endereço
        if (cliente.getEndereco() != null) {
            log.debug("Cliente possui endereço, validando campos");
            validatorUtil.validateEndereco(cliente.getEndereco().getBairro(),
                    cliente.getEndereco().getRua(),
                    cliente.getEndereco().getNumero());      
        }
        log.info("Validação do cliente concluída com sucesso");
    }
    
    @Override
    public Cliente save(Cliente cliente) {
        log.info("Salvando novo cliente: {}", cliente.getNome());
        validarCliente(cliente);
            
        // Se passar por todas as regras, acessa o Repository para salvar
        Cliente salvo = clienteRepository.save(cliente);
        log.info("Cliente salvo com sucesso. ID: {}", salvo.getId());
        return salvo;        
    }

    @Override
    public List<Cliente> findAll() {
        log.debug("Buscando todos os clientes");
        List<Cliente> clientes = clienteRepository.findAll();
        log.debug("Encontrados {} clientes", clientes.size());
        return clientes;
    }

    @Override
    public Cliente update(Cliente cliente) {
        log.info("Atualizando cliente ID: {}", cliente.getId());
        findById(cliente.getId()); // verifica primeiro se o cliente realmente existe no banco
        validarCliente(cliente);
        
        Cliente atualizado = clienteRepository.save(cliente);
        log.info("Cliente atualizado com sucesso. ID: {}", atualizado.getId());
        return atualizado;
    }

    @Override
    public void delete(Long id) {
        log.info("Deletando cliente ID: {}", id);
        findById(id);
        clienteRepository.deleteById(id);
        log.info("Cliente deletado com sucesso. ID: {}", id);
    }

    @Override
    public List<Cliente> findByNome(String nome) {
        log.debug("Buscando clientes por nome: {}", nome);
        if (nome == null || nome.isEmpty() || nome.equals("Pesquise por nome...")) {
            log.debug("Nome vazio ou padrão, retornando todos os clientes");
            return clienteRepository.findAll();
        }
        List<Cliente> clientes = clienteRepository.findByNomeContainingIgnoreCase(nome);
        log.debug("Encontrados {} clientes para o nome: {}", clientes.size(), nome);
        return clientes;
    }
    
}
