/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.pagamento.view.components;

import br.com.ifba.prg03projetosgcr.pagamento.view.ListarPagamentos;
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
 */
@Slf4j
public class BotaoCelulaEditor extends AbstractCellEditor implements TableCellEditor, ActionListener{

    private JTable table;
    private ListarPagamentos listarPagamentosFrame;
    private BotaoCelulaRenderer renderer;
    private int currentRow;

    public BotaoCelulaEditor(JTable table, ListarPagamentos listarPagamentosFrame) {
        this.table = table;
        this.listarPagamentosFrame = listarPagamentosFrame;
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
        fireEditingStopped();
        
        JButton source = (JButton) e.getSource();

        if(source == renderer.getBtnEditar()){
            log.info("Pagamento editado na linha: {}", currentRow);
            listarPagamentosFrame.editarPagamento(currentRow);
        } else if (source == renderer.getBtnDeletar()) {
            log.info("Pagamento deletado na linha: {}", currentRow);
            listarPagamentosFrame.deletarPagamento(currentRow);
        }

        fireEditingStopped();
    }
}
