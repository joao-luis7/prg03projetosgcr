/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.cliente.service;

import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import br.com.ifba.prg03projetosgcr.cliente.repository.ClienteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 *
 * @author joaol
 */
@Service
public class ClienteServiceImpl implements ClienteService{
     
    @Autowired
    private ClienteRepository clienteRepository;
    
    private Cliente findById(Long id){
        return clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado. ID: " + id));
    }
    
    // MÉTODO AUXILIAR: Concentra todas as regras de validação do Cliente
    private void validarCliente(Cliente cliente){
        //verificar nome
        if(cliente.getNome() == null || cliente.getNome().trim().isEmpty()){
            throw new RuntimeException("O nome do cliente é obrigatorio");
        }
        //verifica telefone
        if(cliente.getTelefone() == null || cliente.getTelefone().trim().isEmpty()){
            throw new RuntimeException("O telefone do cliente é obrigatorio");
        }
    
        //validação do Telefone
        if (cliente.getTelefone() != null && !cliente.getTelefone().trim().isEmpty()) {
            String telefoneApenasNumeros = cliente.getTelefone().replaceAll("\\D", "");
            if (telefoneApenasNumeros.length() != 11) {
                throw new RuntimeException("O telefone deve conter o DDD (2 digitos) e o número.");
            }
        }

        //validação do Endereço
        if (cliente.getEndereco() != null) {
            if (cliente.getEndereco().getBairro() == null || cliente.getEndereco().getBairro().trim().isEmpty() ||
                cliente.getEndereco().getRua() == null || cliente.getEndereco().getRua().trim().isEmpty() ||
                cliente.getEndereco().getNumero() == null || cliente.getEndereco().getNumero().trim().isEmpty()) {
                
                throw new RuntimeException("Para salvar o endereço, é obrigatório informar o bairro, a rua e o número.");
            }
        }
    }
    
    @Override
    public Cliente save(Cliente cliente) {
        validarCliente(cliente);
            
        // Se passar por todas as regras, acessa o Repository para salvar
        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente update(Cliente cliente) {
        findById(cliente.getId());// vrifica primeiro se o cliente realmente existe no banco
        validarCliente(cliente);

        return clienteRepository.save(cliente);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        clienteRepository.deleteById(id);
    }

    @Override
    public List<Cliente> findByNome(String nome) {
        if (nome == null || nome.isEmpty() || nome.equals("Pesquise por nome...")){
            return clienteRepository.findAll();
        }
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
}
