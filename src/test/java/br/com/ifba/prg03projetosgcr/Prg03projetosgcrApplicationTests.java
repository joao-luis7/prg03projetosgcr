package br.com.ifba.prg03projetosgcr;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Prg03projetosgcrApplicationTests {
    
    //bloco estatico para liberar uso do swing nos testes
    static {
        System.setProperty("java.awt.headless", "false");
    }
	@Test
	void contextLoads() {
	}

}
