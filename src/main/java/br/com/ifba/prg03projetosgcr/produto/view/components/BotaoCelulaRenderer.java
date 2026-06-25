/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.produto.view.components;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


/**
 *
 * @author joaol
 * Renderer personalizado para exibir botões de Editar e Deletar na tabela
 */
public class BotaoCelulaRenderer extends JPanel implements TableCellRenderer{
    
    private JButton btnEditar;
    private JButton btnDeletar;
    
    public BotaoCelulaRenderer(JTable table){
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0)); //organizar os componentes no centro com espaço
                                                            //de 5px entre eles
        
        //carregar os ícones
        ImageIcon iconEditar = new ImageIcon(getClass().getResource("/images/icone-editar.png"));
        ImageIcon iconDeletar = new ImageIcon(getClass().getResource("/images/icone-deletar.png"));
        
        btnEditar = new JButton(iconEditar);
        btnDeletar = new JButton(iconDeletar);
        
        btnEditar.setToolTipText("Editar");
        btnDeletar.setToolTipText("Deletar");
        
        btnEditar.setFocusPainted(false); //remove o contorno pontilhado qnd o botao ta em foco
        btnDeletar.setFocusPainted(false);
        btnEditar.setBorderPainted(false); //remove a borda ao redor do botao
        btnDeletar.setBorderPainted(false);
        btnEditar.setContentAreaFilled(false); //remove o fundo do botao
        btnDeletar.setContentAreaFilled(false);
        
        add(btnEditar);
        add(btnDeletar);
    }
    
    public BotaoCelulaRenderer(){
        this(null);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //returna o painel com dois botoes
        return this;
    }
    
    public JButton getBtnEditar(){
        return btnEditar;
    }
    
    public JButton getBtnDeletar(){
        return btnDeletar;
    }
    
}
