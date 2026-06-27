/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.view.components;

import br.com.ifba.prg03projetosgcr.cliente.view.components.BotaoCelulaRenderer;
import br.com.ifba.prg03projetosgcr.venda.view.ListarVendas;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author joaol
 */
public class VendaBotaoEditor extends AbstractCellEditor implements TableCellEditor, ActionListener{
    
    private JTable table;
    private ListarVendas listarVendasFrame;
    private BotaoCelulaRenderer renderer;
    private int currentRow;
    
    
    public VendaBotaoEditor(JTable table, ListarVendas listarVendasFrame) {
        this.table = table;
        this.listarVendasFrame = listarVendasFrame;
        this.renderer = new BotaoCelulaRenderer(table);
        
        this.renderer.setModoVisualizacao(true);
        
        renderer.getBtnVisualizar().addActionListener(this);
        renderer.getBtnDeletar().addActionListener(this);
    }
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.currentRow = row;
        return renderer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        
        if (source == renderer.getBtnVisualizar()) {
            listarVendasFrame.verDetalhesVenda(currentRow);
        } else if (source == renderer.getBtnDeletar()) {
            listarVendasFrame.cancelarVenda(currentRow);
        }
        
        fireEditingStopped();
    }
    
}
