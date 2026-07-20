package br.com.ifba.prg03projetosgcr;

import br.com.ifba.prg03projetosgcr.funcionario.view.TelaLogin; 
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Prg03projetosgcrApplication {
    
    
	public static void main(String[] args) {
            //configura o spring para suportar o swing
            ConfigurableApplicationContext context = new SpringApplicationBuilder(Prg03projetosgcrApplication.class)
                .headless(false)
                .run(args);
            
            //recupera a instancia da tela gerenciada pelo spring
            TelaLogin telaLogin = context.getBean(TelaLogin.class);
            
            //exibe a tela
            telaLogin.setVisible(true);
	}

}
