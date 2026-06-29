package br.com.ifba.prg03projetosgcr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import br.com.ifba.prg03projetosgcr.cliente.view.ListarClientes;
import br.com.ifba.prg03projetosgcr.produto.view.ListarProdutos;
import br.com.ifba.prg03projetosgcr.transacao.view.ListarPagamentos;
import br.com.ifba.prg03projetosgcr.venda.view.FrenteDeCaixa;
import br.com.ifba.prg03projetosgcr.venda.view.ListarVendas;

@SpringBootApplication
public class Prg03projetosgcrApplication {
    
    
	public static void main(String[] args) {
            //configura o spring para suportar o swing
            ConfigurableApplicationContext context = new SpringApplicationBuilder(Prg03projetosgcrApplication.class)
                .headless(false)
                .run(args);
            
            //recupera a instancia da tela gerenciada pelo spring
            ListarPagamentos telaPrincipal = context.getBean(ListarPagamentos.class);
            
            //exibe a tela
            telaPrincipal.setVisible(true);
	}

}
