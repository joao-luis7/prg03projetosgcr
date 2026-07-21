    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.cliente.service;

import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import br.com.ifba.prg03projetosgcr.cliente.repository.ClienteRepository;
import br.com.ifba.prg03projetosgcr.util.ValidatorUtil;
import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
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
        List<Cliente> clientes = clienteRepository.findByAtivoTrue();
        log.debug("Encontrados {} clientes", clientes.size());
        return clientes;
    }

    @Override
    @Transactional
    public Cliente update(Cliente cliente) {
        log.info("Atualizando cliente ID: {}", cliente.getId());
        findById(cliente.getId()); // verifica primeiro se o cliente realmente existe no banco
        validarCliente(cliente);
        
        Cliente atualizado = clienteRepository.save(cliente);
        log.info("Cliente atualizado com sucesso. ID: {}", atualizado.getId());
        return atualizado;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Solicitação de desativação) para o cliente ID: {}", id);
        Cliente cliente = findById(id);
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
        log.info("Cliente desativado com sucesso. ID: {}", id);
    }

    @Override
    public List<Cliente> findByNome(String nome) {
        log.debug("Buscando clientes por nome: {}", nome);
        if (nome == null || nome.isEmpty()) {
            log.debug("Nome vazio ou padrão, retornando todos os clientes");
            return clienteRepository.findByAtivoTrue();
        }
        return clienteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
    }

    @Override
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Erro: Cliente não encontrado com o ID informado (" + id + ")."));
    }    

    @Override
    public void atualizarSaldo(Long id, double novoValor) {
        log.info("Solicitação para atualizar saldo do cliente ID: {} para o valor: {}", id, novoValor);
        
        Cliente cliente = findById(id);
        
        if (novoValor < 0) {
            throw new IllegalArgumentException("Erro: O saldo devedor não pode ser um valor negativo.");
        }
        
        cliente.setSaldoDevedor(novoValor);
        
        clienteRepository.save(cliente);
        
        log.info("Saldo do cliente ID: {} atualizado com sucesso.", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long contarClientesAtivosIdentificados() {
        log.debug("Contando o total de clientes ativos (excluindo balcão/não identificado)");
        // Define exatamente qual é o nome do cliente que não deve entrar na contagem
        String CLIENTE_NAO_IDENTIFICADO = "Não Identificado"; 
        
        Long total = clienteRepository.countByAtivoTrueAndNomeNot(CLIENTE_NAO_IDENTIFICADO);
        return total != null ? total : 0L;
    }
}
