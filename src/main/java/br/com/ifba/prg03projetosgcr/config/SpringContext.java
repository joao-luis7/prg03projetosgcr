/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContextAware;
/**
 *
 * @author joaol
 */
@Component
public class SpringContext implements ApplicationContextAware {
    
    // Variável estática que vai guardar a memória inteira do Spring Boot
    private static ApplicationContext context;
    
    /**
     * Este método é chamado automaticamente pelo Spring Boot assim que a 
     * Main termina de carregar. Ele injeta o 'applicationContext' 
     * aqui para dentro e nós o salvamos na nossa variável estática.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    
    /**
     * Um método estático e genérico que qualquer tela Swing 
     * pode chamar de qualquer lugar do código para buscar um Controller ou Service.
     */
    public static <T> T getBean(Class<T> beanClass) {
        if (context == null) {
            throw new IllegalStateException("O contexto do Spring ainda não foi inicializado!");
        }
        return context.getBean(beanClass);
    }
}
