/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.categoria.view;

import br.com.ifba.prg03projetosgcr.categoria.controller.CategoriaController;
import br.com.ifba.prg03projetosgcr.categoria.entity.Categoria;
import br.com.ifba.prg03projetosgcr.config.SpringContext;
import br.com.ifba.prg03projetosgcr.util.CampoPesquisaUtil;
import br.com.ifba.prg03projetosgcr.util.components.GenericBotaoCelulaEditor;
import br.com.ifba.prg03projetosgcr.util.components.GenericBotaoCelulaRenderer;
import br.com.ifba.prg03projetosgcr.util.components.TableActionEvent;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author joaol
 */
@Component
public class ListarCategorias extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ListarCategorias.class.getName());
    
    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private CategoriaController categoriaController;
    
    private List<Categoria> categoriasCadastradas = new ArrayList<>();

    /**
     * Creates new form ListarCategorias
     */
    public ListarCategorias() {
        initComponents();
        
        
        CampoPesquisaUtil.configurarPesquisaDinamica(
            txtPesquisar, 
            "Pesquisar...", 
            this::atualizarTabela
        );
        
        configurarColunaAcoes();
        
        tblCategorias.setRowHeight(45);
        
    }
    
    @PostConstruct
    public void init() {
        atualizarTabela();
    }
    
    public void atualizarTabela() {
        String nomeBusca = txtPesquisar.getText().trim();
        
        if (nomeBusca.equals("Pesquisar...")) {
            nomeBusca = "";
        }

        List<Categoria> lista = categoriaController.findAll();
        
        //  Limpa a lista global antes de adicionar os itens filtrados
        categoriasCadastradas.clear(); 
            
        DefaultTableModel modelo = (DefaultTableModel) tblCategorias.getModel();
        modelo.setRowCount(0); // Limpa as linhas atuais

        for (Categoria c : lista) {
            
            if (!nomeBusca.isEmpty() && !c.getNome().toLowerCase().contains(nomeBusca.toLowerCase())) {
                continue;
            }

            // Adiciona a categoria validada na lista global para manter as linhas da tabela sincronizadas com o código
            categoriasCadastradas.add(c);

            String textoAtivo = c.isAtivo() ? "ATIVO" : "INATIVO";
            
            modelo.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                c.getDescricao(), 
                textoAtivo,
                "" // Coluna de ações
            });
        }
    }
    
    public void editarCategoria(int linha) {
        Categoria categoriaSelecionada = categoriasCadastradas.get(linha);    
        
        EditarCategoria telaEditar = context.getBean(EditarCategoria.class);
        telaEditar.preencherCampos(categoriaSelecionada);
        telaEditar.setListarCategorias(this); 
        telaEditar.setVisible(true);
    }
    
    public void deletarCategoria(int linha) {
        Categoria categoriaSelecionada = categoriasCadastradas.get(linha);    
        
        // Corrigido a mensagem (produto -> categoria)
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja inativar a categoria: " + categoriaSelecionada.getNome() + "?", 
            "Atenção: Exclusão", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            categoriaController.delete(categoriaSelecionada.getId());
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Categoria removida com sucesso.");
        }
    }
    private void configurarColunaAcoes(){
        TableActionEvent evento = new TableActionEvent(){
            @Override
            public void onEdit(int row) {
                editarCategoria(row);
            }

            @Override
            public void onDelete(int row) {
                deletarCategoria(row);
            }

            @Override
            public void onView(int row) {
                //aqui nao tem visualizar
            }     
        };
        
        int colunaAcoesIdx = 4;
        
        tblCategorias.getColumnModel().getColumn(colunaAcoesIdx).setCellRenderer(new GenericBotaoCelulaRenderer());
        tblCategorias.getColumnModel().getColumn(colunaAcoesIdx).setCellEditor(new GenericBotaoCelulaEditor(evento, false));
  
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
        btnCadastrarCategoria = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCategorias = new javax.swing.JTable();
        txtPesquisar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTitulo.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblTitulo.setText("Gestão de Categorias");

        btnCadastrarCategoria.setBackground(new java.awt.Color(0, 102, 51));
        btnCadastrarCategoria.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCadastrarCategoria.setForeground(new java.awt.Color(255, 255, 255));
        btnCadastrarCategoria.setText("Nova Categoria");
        btnCadastrarCategoria.addActionListener(this::btnCadastrarCategoriaActionPerformed);

        tblCategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Descrição", "Disponibilidade", "Ações"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblCategorias);

        txtPesquisar.setText("Pesquisar...");
        txtPesquisar.addActionListener(this::txtPesquisarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTitulo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCadastrarCategoria)))
                        .addGap(46, 46, 46))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitulo)
                    .addComponent(btnCadastrarCategoria))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisarActionPerformed

    private void btnCadastrarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarCategoriaActionPerformed
        // TODO add your handling code here:
        CadastrarCategoria telaCadastro = context.getBean(CadastrarCategoria.class);
    
        telaCadastro.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                atualizarTabela(); 
            }
        });
        telaCadastro.setLocationRelativeTo(this);
        telaCadastro.setVisible(true);
    }//GEN-LAST:event_btnCadastrarCategoriaActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ListarCategorias().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrarCategoria;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblCategorias;
    private javax.swing.JTextField txtPesquisar;
    // End of variables declaration//GEN-END:variables
}
