/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.util;

import javax.swing.JTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/**
 *  @author joaol
 */
public class CampoPesquisaUtil {
    
    /**
     * @param campoPesquisa O JTextField que receberá a configuração.
     * @param textoPadrao O texto de placeholder (ex: "Pesquisar...").
     * @param acaoAtualizar O método que será chamado para atualizar a tabela (ex: this::atualizarTabela).
    */
    
    public static void configurarPesquisaDinamica(JTextField campoPesquisa, String textoPadrao, Runnable acaoAtualizar) {
        
        // Configura o FocusListener (comportamento de placeholder)
        campoPesquisa.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (campoPesquisa.getText().equals(textoPadrao)) {
                    campoPesquisa.setText(""); // limpa o campo
                }
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (campoPesquisa.getText().trim().isEmpty()) {
                    campoPesquisa.setText(textoPadrao); // volta para o texto padrão
                    acaoAtualizar.run(); // recarrega a tabela completa
                }
            }
        });

        // Configura o KeyListener para a pesquisa dinâmica
        campoPesquisa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                // Toda vez que o usuário usa uma tecla, a ação é disparada (a tabela filtra)
                acaoAtualizar.run();
            }
        });
    }
}
