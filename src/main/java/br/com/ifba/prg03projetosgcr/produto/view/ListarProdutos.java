/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.produto.view;

import br.com.ifba.prg03projetosgcr.produto.controller.ProdutoController;
import br.com.ifba.prg03projetosgcr.produto.entity.Produto;
import br.com.ifba.prg03projetosgcr.produto.view.components.BotaoCelulaEditor;
import br.com.ifba.prg03projetosgcr.produto.view.components.BotaoCelulaRenderer;
import br.com.ifba.prg03projetosgcr.util.CampoPesquisaUtil;
import br.com.ifba.prg03projetosgcr.util.components.GenericBotaoCelulaEditor;
import br.com.ifba.prg03projetosgcr.util.components.GenericBotaoCelulaRenderer;
import br.com.ifba.prg03projetosgcr.util.components.TableActionEvent;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author joaol
 */
@Component
public class ListarProdutos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ListarProdutos.class.getName());
    
        
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ProdutoController produtoController;
    
    //lista para gaurdar a referencia dos clientes vindo do banco
    private List<Produto> produtosCadastrados = new ArrayList<>();
    /**
     * Creates new form ListarProdutos
     */
    public ListarProdutos() {
        initComponents();
        
        setLocationRelativeTo(null);
        
        //zera a qtnd de linhas da tabela para remover as linhas nulas
        DefaultTableModel modelo = (DefaultTableModel) tblProdutos.getModel();
        modelo.setRowCount(0);
        
        //aumenta a altura das linhas da tablea
        tblProdutos.setRowHeight(45);
        
        //Configurar a coluna de ações com botoes de editar e deletar
        configurarColunaAcoes();
        
        CampoPesquisaUtil.configurarPesquisaDinamica(
            txtPesquisar, 
            "Pesquisar...", 
            this::atualizarTabela
        );
    }
    
    
    // O @PostConstruct faz com que este método rode automaticamente 
    // APÓS o Spring injetar o ClienteService. Se tentar chamar o 
    // serviço direto no construtor, vai dar NullPointerException.
    @PostConstruct
    public void init(){
        atualizarTabela();
    }
    
    public void atualizarTabela() {
        String nomeBusca = txtPesquisar.getText().trim();
        
        if (nomeBusca.equals("Pesquisar...")) {
            nomeBusca = "";
        }

        produtosCadastrados = produtoController.findByNome(nomeBusca);
            
        DefaultTableModel modelo = (DefaultTableModel) tblProdutos.getModel();
        modelo.setRowCount(0); // Limpa as linhas atuais
        
        for (Produto p : produtosCadastrados) {
            // Se o produto estiver inativo, vamos dar um aviso visual no estoque
            String textoEstoque = p.isAtivo() ? String.valueOf(p.getQuantidadeEstoque()) : "INATIVO";
            
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                "R$ " + String.format("%.2f", p.getPrecoUnidade()),
                textoEstoque,
                "" // Coluna de ações
            });
        }
    }
    
    public void editarProduto(int linha) {
        Produto produtoSelecionado = produtosCadastrados.get(linha);    
        
        // Abre a tela de Edição (que terá os botões de rádio para Inativar)
        EditarProduto telaEditar = context.getBean(EditarProduto.class);
            telaEditar.preencherCampos(produtoSelecionado);
        telaEditar.setListarProdutos(this);
        telaEditar.setVisible(true);
    }
    
    public void deletarProduto(int linha) {
        Produto produtoSelecionado = produtosCadastrados.get(linha);
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem ceteza que deseja apagar DEFINITIVAMENTE o produto: " + produtoSelecionado.getNome() + "?", 
            "Atenção: Exclusão Permanente", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            produtoController.delete(produtoSelecionado.getId());
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Produto apagado com sucesso.");
        }
    }
    
    private void configurarColunaAcoes(){
        TableActionEvent evento = new TableActionEvent(){
            @Override
            public void onEdit(int row) {
                editarProduto(row);
            }

            @Override
            public void onDelete(int row) {
                deletarProduto(row);
            }

            @Override
            public void onView(int row) {
                //aqui nao tem visualizar
            }     
        };
        
        int colunaAcoesIdx = 4;
        
        tblProdutos.getColumnModel().getColumn(colunaAcoesIdx).setCellRenderer(new GenericBotaoCelulaRenderer());
        tblProdutos.getColumnModel().getColumn(colunaAcoesIdx).setCellEditor(new GenericBotaoCelulaEditor(evento, false));
    }
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTituloGestao = new javax.swing.JLabel();
        btnNovoProduto = new javax.swing.JButton();
        txtPesquisar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProdutos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTituloGestao.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        lblTituloGestao.setText("Gestão de Produtos");

        btnNovoProduto.setBackground(new java.awt.Color(0, 102, 51));
        btnNovoProduto.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnNovoProduto.setForeground(new java.awt.Color(255, 255, 255));
        btnNovoProduto.setText("Novo Produto");
        btnNovoProduto.addActionListener(this::btnNovoProdutoActionPerformed);

        txtPesquisar.setText("Pesquisar...");
        txtPesquisar.addActionListener(this::txtPesquisarActionPerformed);

        tblProdutos.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        tblProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Produto", "Preço Unit.", "Estoque", "Ações"
            }
        ));
        jScrollPane1.setViewportView(tblProdutos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTituloGestao)
                            .addComponent(txtPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNovoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(64, 64, 64))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTituloGestao)
                    .addComponent(btnNovoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(txtPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNovoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoProdutoActionPerformed
        // TODO add your handling code here:
        CadastrarProduto telaCadastro = context.getBean(CadastrarProduto.class);
        telaCadastro.setListarProdutos(this);
        telaCadastro.setVisible(true);
    }//GEN-LAST:event_btnNovoProdutoActionPerformed

    private void txtPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisarActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ListarProdutos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNovoProduto;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTituloGestao;
    private javax.swing.JTable tblProdutos;
    private javax.swing.JTextField txtPesquisar;
    // End of variables declaration//GEN-END:variables
}
