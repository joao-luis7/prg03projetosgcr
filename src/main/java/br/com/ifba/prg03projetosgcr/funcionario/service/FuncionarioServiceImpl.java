/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.funcionario.service;

import br.com.ifba.prg03projetosgcr.funcionario.entity.Funcionario;
import br.com.ifba.prg03projetosgcr.funcionario.entity.enums.PerfilAcesso;
import br.com.ifba.prg03projetosgcr.funcionario.repository.FuncionarioRepository;
import br.com.ifba.prg03projetosgcr.infrastructure.exception.RegraNegocioException;
import br.com.ifba.prg03projetosgcr.util.ValidatorUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
/**
 *
 * @author joaol
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class FuncionarioServiceImpl implements FuncionarioService{
    
    private final FuncionarioRepository funcionarioRepository;
    private final ValidatorUtil validatorUtil;
    
    /**
     * SEEDER - Inicialização do MVP
     * Executa automaticamente ao subir a aplicação. Cria um administrador padrão
     * se o banco estiver vazio, economizando a criação de uma tela de cadastro agora.
     */
    @PostConstruct
    @Transactional
    public void initDefaultAdmin() {
        if (funcionarioRepository.count() == 0) {
            log.info("Banco de dados vazio. Inicializando funcionário Admin padrão.");
            
            Funcionario admin = new Funcionario();
            admin.setNome("Administrador Sistema");
            admin.setUsername("admin");
            admin.setSenha("1234"); // MVP: Em produção futura, vou usar BCrypt ou outra coisa
            admin.setPerfil(PerfilAcesso.ADMIN);
            admin.setAtivo(true);
            
            funcionarioRepository.save(admin);
            log.info("Funcionário Admin padrão criado com sucesso (username: admin, senha: 1234).");
        }
    }
    
    @Override
    @Transactional(readOnly = true) // Consulta simples
    public Funcionario autenticar(String username, String senha) {
        log.debug("Iniciando processo de autenticação para o username: {}", username);

        // Fail-fast para dados vazios na tentativa de login
        validatorUtil.validateStringNotNullOrEmpty(username, "username do funcionário");
        validatorUtil.validateStringNotNullOrEmpty(senha, "senha do funcionário");

        Funcionario funcionario = funcionarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Tentativa de login falhou: Funcionário não encontrado. Username: {}", username);
                    // RegraNegocioException para violação de política
                    return new RegraNegocioException("Credenciais inválidas. Verifique o usuário e a senha.");
                });

        if (!funcionario.isAtivo()) {
            log.warn("Tentativa de login de funcionário inativo. Username: {}", username);
            throw new RegraNegocioException("Usuário inativo. Entre em contato com a administração.");
        }

        if (!funcionario.getSenha().equals(senha)) {
            log.warn("Tentativa de login falhou: Senha incorreta. Username: {}", username);
            // Mensagem genérica por segurança (não indicar exatamente o que errou)
            throw new RegraNegocioException("Credenciais inválidas. Verifique o usuário e a senha.");
        }

        log.info("Autenticação realizada com sucesso para o username: {}", username);
        return funcionario;
    }

    @Override
    @Transactional
    public void save(Funcionario funcionario) {
        log.info("Salvando novo funcionário: {}", funcionario.getUsername());
        
        validarFuncionario(funcionario);
        
        funcionarioRepository.save(funcionario);
        
        log.info("Funcionário {} salvo com sucesso!", funcionario.getUsername());
    }
    
    private void validarFuncionario(Funcionario funcionario) {
        log.debug("Validando dados do funcionário: {}", funcionario.getUsername());
        
        validatorUtil.validateStringNotNullOrEmpty(funcionario.getNome(), "nome do funcionário");
        validatorUtil.validateStringNotNullOrEmpty(funcionario.getUsername(), "username do funcionário");
        validatorUtil.validateStringNotNullOrEmpty(funcionario.getSenha(), "senha do funcionário");
        
        if (funcionario.getPerfil() == null) {
            log.warn("Tentativa de salvar funcionário sem perfil de acesso definido.");
            throw new RegraNegocioException("O perfil de acesso do funcionário é obrigatório.");
        }
    }
    
}
