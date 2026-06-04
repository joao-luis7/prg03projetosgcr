package br.com.ifba.prg03projetosgcr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import br.com.ifba.prg03projetosgcr.cliente.view.ListarClientes;

@SpringBootApplication
public class Prg03projetosgcrApplication {

	public static void main(String[] args) {
            //configura o spring para suportar o swing
            ConfigurableApplicationContext context = new SpringApplicationBuilder(Prg03projetosgcrApplication.class)
                .headless(false)
                .run(args);
            
            //recupera a instancia da tela gerenciada pelo spring
            ListarClientes telaPrincipal = context.getBean(ListarClientes.class);
            
            //exibe a tela
            telaPrincipal.setVisible(true);
	}

}
