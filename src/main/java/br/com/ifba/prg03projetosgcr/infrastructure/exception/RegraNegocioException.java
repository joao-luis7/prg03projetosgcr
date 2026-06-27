/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.infrastructure.exception;

/**
 *
 * @author joaol
 */
public class RegraNegocioException extends RuntimeException{
    public RegraNegocioException(String mensagem){
        super(mensagem);
    }
}
