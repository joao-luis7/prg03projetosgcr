/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.cliente.view.components;

import br.com.ifba.prg03projetosgcr.cliente.view.ListarClientes;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author joaol
 * Editor personalizado para gerenciar os eventos dos botões de Editar e Deletar na tabela
 */
@Slf4j
public class BotaoCelulaEditor extends AbstractCellEditor implements TableCellEditor, ActionListener{
    
    private JTable table;
    private ListarClientes listarClientesFrame;
    private BotaoCelulaRenderer renderer;
    private int currentRow;
    
    public BotaoCelulaEditor(JTable table, ListarClientes listarClientesFrame) {
        this.table = table;
        this.listarClientesFrame = listarClientesFrame;
        this.renderer = new BotaoCelulaRenderer(table);

        // Adicionar listeners para os botões
        renderer.getBtnEditar().addActionListener(this);
        renderer.getBtnDeletar().addActionListener(this);
    }
    
    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.currentRow = row;
        log.debug("Editando célula da linha: {}", row);
        return renderer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        
        if(source == renderer.getBtnEditar()){
            log.info("Cliente editado na linha: {}", currentRow);
            listarClientesFrame.editarCliente(currentRow);
        } else if (source == renderer.getBtnDeletar()) {
            log.info("Cliente deletado na linha: {}", currentRow);
            listarClientesFrame.deletarCliente(currentRow);
        }
        
        fireEditingStopped();
    }
    
}
