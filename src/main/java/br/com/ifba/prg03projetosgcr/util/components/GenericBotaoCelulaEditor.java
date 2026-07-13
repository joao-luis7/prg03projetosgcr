/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.util.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import lombok.extern.slf4j.Slf4j;
/**
 *
 * @author joaol
 */
@Slf4j
public class GenericBotaoCelulaEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    
    private TableActionEvent event;
    private GenericBotaoCelulaRenderer renderer;
    private int currentRow;
    
    // Construtor recebe a interface em vez de uma View específica
    public GenericBotaoCelulaEditor(TableActionEvent event, boolean modoVisualizacao) {
        this.event = event;
        this.renderer = new GenericBotaoCelulaRenderer();
        this.renderer.setModoVisualizacao(modoVisualizacao);

        // Adicionar listeners
        renderer.getBtnEditar().addActionListener(this);
        renderer.getBtnDeletar().addActionListener(this);
        renderer.getBtnVisualizar().addActionListener(this);
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
        
        // Dispara o evento correspondente na interface
        if(source == renderer.getBtnEditar()){
            event.onEdit(currentRow);
        } else if (source == renderer.getBtnDeletar()) {
            event.onDelete(currentRow);
        } else if (source == renderer.getBtnVisualizar()) {
            event.onView(currentRow);
        }
        
        fireEditingStopped();
    }
}