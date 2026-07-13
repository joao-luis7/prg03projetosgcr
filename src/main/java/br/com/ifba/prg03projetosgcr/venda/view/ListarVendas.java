/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.venda.view;

import br.com.ifba.prg03projetosgcr.cliente.view.components.BotaoCelulaRenderer;
import br.com.ifba.prg03projetosgcr.util.components.GenericBotaoCelulaEditor;
import br.com.ifba.prg03projetosgcr.util.components.GenericBotaoCelulaRenderer;
import br.com.ifba.prg03projetosgcr.util.components.TableActionEvent;
import br.com.ifba.prg03projetosgcr.venda.controller.VendaController;
import br.com.ifba.prg03projetosgcr.venda.entity.Venda;
import br.com.ifba.prg03projetosgcr.venda.view.components.VendaBotaoEditor;
import jakarta.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author joaol
 */
@Component
public class ListarVendas extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ListarVendas.class.getName());
    
    @Autowired
    private VendaController vendaController;
    @Autowired
    private FrenteDeCaixa frenteDeCaixa;
    
    /**
     * Creates new form ListarVendas
     */
    public ListarVendas() {
        initComponents();
        
        configurarColunaAcoes();
    }
    
    @PostConstruct
    public void inicializarDados() {
        carregarTabelaVendas();
    }
    
    private void configurarColunaAcoes() {
        // Cria os eventos para os botões desta tela (Visualizar e Deletar)
        TableActionEvent eventoVenda = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                // Fica vazio,  Vendas não tem botão de editar
            }

            @Override
            public void onDelete(int row) {
                cancelarVenda(row);
            }

            @Override
            public void onView(int row) {
                verDetalhesVenda(row);
            }
        };

        // Instancia o Renderer Genérico 
        GenericBotaoCelulaRenderer rendererPersonalizado = new GenericBotaoCelulaRenderer();
        rendererPersonalizado.setModoVisualizacao(true);
        
        
        tblVendas.getColumnModel().getColumn(6).setCellRenderer(rendererPersonalizado);
        // Passa 'true' no final do construtor do Editor para ele saber que está no modo visualização
        tblVendas.getColumnModel().getColumn(6).setCellEditor(new GenericBotaoCelulaEditor(eventoVenda, true));
        
        // Tranca a largura da coluna e altura das linhas
        tblVendas.getColumnModel().getColumn(6).setPreferredWidth(110); 
        tblVendas.getColumnModel().getColumn(6).setMinWidth(110);
        tblVendas.setRowHeight(35);
    }
    
    private void carregarTabelaVendas() {
        // Busca todas as vendas no banco de dados
        List<Venda> vendas = vendaController.findAll();
        
        DefaultTableModel modelo = (DefaultTableModel) tblVendas.getModel();
        modelo.setNumRows(0); // Limpa a tabela antes de carregar
        
        // Formatador para deixar a data no padrão brasileiro
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Venda v : vendas) {
            // Se a venda não tiver cliente vinculado, foi uma venda rápida de balcão
            String nomeCliente = (v.getCliente() != null) ? v.getCliente().getNome() : "Cliente Balcão";
            
            // Tratamento de segurança para dados que podem vir nulos do banco
            String dataFormatada = (v.getDataHora() != null) ? v.getDataHora().format(formatter) : "-";
            String formaPgto = (v.getFormaPagamento() != null) ? v.getFormaPagamento().name() : "-";
            String status = (v.getStatusVenda() != null) ? v.getStatusVenda().name() : "-";
            
            modelo.addRow(new Object[]{
                v.getId(),
                dataFormatada,
                nomeCliente,
                formaPgto,
                String.format("R$ %.2f", v.getValorTotal()),
                status,
                "" // Espaço vazio onde o Renderer vai desenhar os botões
            });
        }
    }
    
    public void verDetalhesVenda(int linha) {
        // Pega o ID da venda que está na primeira coluna da linha clicada
        Long idVenda = (Long) tblVendas.getValueAt(linha, 0);
        
        try {
            //Busca a venda completa no banco (agora trazendo os itens graças ao novo método)
            Venda vendaCompleta = vendaController.findByIdComItens(idVenda);
            
            // Instancia a janela de detalhes
            DetalhesVenda dialog = new DetalhesVenda(this, true);
                
            //Entrega a venda para a janela preencher os textos e a tabela
            dialog.preencherDados(vendaCompleta);
            
            //Centraliza a janea e mostra na tela
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar detalhes: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cancelarVenda(int linha) {
        Long idVenda = (Long) tblVendas.getValueAt(linha, 0);
        
        int resposta = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja estornar e CANCELAR a venda #" + idVenda + "?", 
            "Cancelar Venda", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (resposta == JOptionPane.YES_OPTION) {
            try {
                vendaController.cancelarVenda(idVenda);
                
                JOptionPane.showMessageDialog(this, "Venda cancelada com sucesso!");
                carregarTabelaVendas(); // Atualiza a tela para mostrar o novo status             
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cancelar: " + ex.getMessage());
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVendas = new javax.swing.JTable();
        btnNovaVenda = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTitulo.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblTitulo.setText("Gestão de Vendas");

        tblVendas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Data/Hora", "Cliente", "Forma Pgto.", "Total (R$)", "Status", "Ações"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblVendas);
        if (tblVendas.getColumnModel().getColumnCount() > 0) {
            tblVendas.getColumnModel().getColumn(6).setMinWidth(110);
            tblVendas.getColumnModel().getColumn(6).setPreferredWidth(110);
        }

        btnNovaVenda.setBackground(new java.awt.Color(0, 102, 51));
        btnNovaVenda.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnNovaVenda.setForeground(new java.awt.Color(255, 255, 255));
        btnNovaVenda.setText("Nova Venda");
        btnNovaVenda.addActionListener(this::btnNovaVendaActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNovaVenda))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(47, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnNovaVenda)
                    .addComponent(lblTitulo))
                .addGap(39, 39, 39)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNovaVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovaVendaActionPerformed
        frenteDeCaixa.setVisible(true);
        
        frenteDeCaixa.toFront();
        
        this.dispose();
    }//GEN-LAST:event_btnNovaVendaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ListarVendas().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNovaVenda;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblVendas;
    // End of variables declaration//GEN-END:variables
}
