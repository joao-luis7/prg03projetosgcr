/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.util;

import br.com.ifba.prg03projetosgcr.infrastructure.exception.RegraNegocioException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
/**
 *
 * @author joaol
 */
@Slf4j
@Component
public class ValidatorUtil {
    //Valida se uma string é nula ou está vazia (apenas espaços em branco).
    public void validateStringNotNullOrEmpty(String valor, String campo){
        log.debug("Validando o campo: {}", campo);
        if(valor == null || valor.trim().isEmpty()){
            log.error("Campo '{}' é obrigatório", campo);
            throw new RegraNegocioException("o " + campo + " é obrigatório");
        }
        log.debug("Campo '{}' validado com sucesso", campo);
    }
    
    // Valida se uma string contém apenas números e tem o tamanho esperado.
    public void validateNumericString(String valor, String campo, int tamanhoEsperado){
        log.debug("Validando campo número: {} com tamanho esperado: {}", campo, tamanhoEsperado);
        
        if(valor != null && !valor.trim().isEmpty()){
            String apenasNumeros = valor.replaceAll("\\D", "");
            if(apenasNumeros.length() != tamanhoEsperado){
                log.error("Campo '{}' deve conter {} dígitos númericos. Encontrado: {}", campo, 
                        tamanhoEsperado, apenasNumeros.length());
                throw new RegraNegocioException("O " + campo + " deve conter " + tamanhoEsperado + " dígitos.");
            }
        }
        log.debug("Campo '{}' validado com sucesso", campo);
    }
    
    //Valida se o objeto é nulo
    public void validateNotNull(Object objeto, String mensagem) {
        log.debug("Validando objeto não nulo");
        if (objeto == null) {
            log.error("Validação falhou: {}", mensagem);
            throw new RegraNegocioException(mensagem);
        }
        log.debug("Objeto validado com sucesso");
    }
    
    //Valida múltiplos campos de endereço.
    public void validateEndereco(String bairro, String rua, String numero) {
        log.debug("Validando campos de endereço");

        validateStringNotNullOrEmpty(bairro, "bairro");
        validateStringNotNullOrEmpty(rua, "rua");
        validateStringNotNullOrEmpty(numero, "número");

        log.debug("Endereço validado com sucesso");
    }
    
    public void validateValorMaiorQueZero(double valor, String campo) {
        if (valor <= 0) {
            throw new RegraNegocioException("O " + campo + " deve ser maior que zero.");
        }
    }

    public void validateNaoNegativo(int valor, String campo) {
        if (valor < 0) {
            throw new RegraNegocioException("O " + campo + " não pode ser negativo.");
        }
    }

    // reutilizável para coleções vazias (Carrinho, Listas)
    public void validateListaNotEmpty(List<?> lista, String message) {
        if (lista == null || lista.isEmpty()) {
            throw new RegraNegocioException(message);
        }
    }
}
