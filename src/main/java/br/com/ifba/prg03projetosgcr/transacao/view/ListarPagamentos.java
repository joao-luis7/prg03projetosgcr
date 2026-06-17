/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.transacao.view;

import br.com.ifba.prg03projetosgcr.transacao.controller.PagamentoController;
import br.com.ifba.prg03projetosgcr.transacao.entity.Pagamento;
import br.com.ifba.prg03projetosgcr.transacao.view.components.BotaoCelulaEditor;
import br.com.ifba.prg03projetosgcr.transacao.view.components.BotaoCelulaRenderer;
import jakarta.annotation.PostConstruct;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
public class ListarPagamentos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ListarPagamentos.class.getName());
    
    @Autowired
    private ApplicationContext context;
    @Autowired
    private PagamentoController pagamentoController;

    //lista para guardar a referencia dos pagamentos vindo do banco
    private List<Pagamento> pagamentosCadastrados = new ArrayList<>();
    /**
     * Creates new form ListarPagamentos
     */
    public ListarPagamentos() {
        initComponents();
        
                JPanel painelPrincipal = new JPanel();
        // Define o tamanho ideal apenas do conteúdo
        painelPrincipal.setPreferredSize(new Dimension(1280,720));

        add(painelPrincipal);

        // O 'pack' empacota a janela para ela se ajustar ao tamanho do painel
        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //zera a qtnd de linhas da tabela para remover as linhas nulas
        DefaultTableModel modelo = (DefaultTableModel) tblPagamentos.getModel();
        modelo.setRowCount(0);

        //aumenta a altura das linhas da tablea
        tblPagamentos.setRowHeight(45);

        //Configurar a coluna de ações com botoes de editar e deletar
        configurarColunaAcoes();
    }
    
    // O @PostConstruct faz com que este método rode automaticamente
    // APÓS o Spring injetar o PagamentoController. Se tentar chamar o
    // controller direto no construtor, vai dar NullPointerException.
    @PostConstruct
    public void init(){
        atualizarTabela();
    }
    
    
    public void atualizarTabela(){
        pagamentosCadastrados = pagamentoController.findAll();

        DefaultTableModel modelo = (DefaultTableModel) tblPagamentos.getModel();
        modelo.setRowCount(0); //limpa as linhas atuais

        for (Pagamento p : pagamentosCadastrados){
            //adiciona as colunas: cliente, valor, data, metodo e acoes
            modelo.addRow(new Object[]{
                p.getCliente().getNome(),
                "R$ " + String.format("%.2f", p.getValorTotal()),
                p.getDataHora(),
                p.getFormaPagamento(),
                "" //coluna de ações é renderizada pelos botoes
            });
        }
    }
    
        private void configurarColunaAcoes(){
        //criar o renderer personalizado para a coluna acoes
        BotaoCelulaRenderer renderer = new BotaoCelulaRenderer(tblPagamentos);

        //obter a coluna de acoes
        TableColumn colunaAcoes = tblPagamentos.getColumnModel().getColumn(4);
        colunaAcoes.setCellRenderer(renderer);
        colunaAcoes.setCellEditor(new BotaoCelulaEditor(tblPagamentos, this));


    }

    public void editarPagamento(int linha){
        Pagamento pagamentoSelecionado = pagamentosCadastrados.get(linha);

        //criar e configurar a tela de edição
        RegistrarPagamento telaEditar = context.getBean(RegistrarPagamento.class);
        telaEditar.preencherCampos(pagamentoSelecionado);
        telaEditar.setListarPagamentos(this);
        telaEditar.setVisible(true);

    }
    
        public void deletarPagamento(int linha){
        //pega o pagamento exato da lista
        Pagamento pagamentoSelecionado = pagamentosCadastrados.get(linha);



        int confirmacao = javax.swing.JOptionPane.showConfirmDialog(this,
            "Tem ceteza que deseja deletar o pagamento de: " + pagamentoSelecionado.getCliente().getNome() + "?",
            "Confirmar Exclusão",
            javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE);

        if(confirmacao == javax.swing.JOptionPane.YES_OPTION){
            //deleta do banco usando o id
            pagamentoController.delete(pagamentoSelecionado.getId());

            //atualiza a tabela
            atualizarTabela();

            javax.swing.JOptionPane.showMessageDialog(this,
                "Pagamento deletado com sucesso",
                "Sucesso",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void abrirTelaRegistro(){
        // como a tela é prototype, o context.getBean cria uma tela zerada toda vez
        RegistrarPagamento telaRegistro = context.getBean(RegistrarPagamento.class);
        telaRegistro.setListarPagamentos(this);
        telaRegistro.setVisible(true);

    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblPagamentos = new javax.swing.JTable();
        lblTituloPagamentos = new javax.swing.JLabel();
        btnRegistrarPagamento = new javax.swing.JButton();
        lblTituloTotal = new javax.swing.JLabel();
        lblValorTotal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(214, 214, 214));

        tblPagamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "CLIENTE", "VALOR", "DATA", "MÉTODO", "AÇÕES"
            }
        ));
        jScrollPane1.setViewportView(tblPagamentos);

        lblTituloPagamentos.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        lblTituloPagamentos.setText("Controle de Pagamentos");

        btnRegistrarPagamento.setBackground(new java.awt.Color(0, 102, 51));
        btnRegistrarPagamento.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        btnRegistrarPagamento.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarPagamento.setText("Registrar Pagamento");
        btnRegistrarPagamento.addActionListener(this::btnRegistrarPagamentoActionPerformed);

        lblTituloTotal.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        lblTituloTotal.setText("Total Recebido:");

        lblValorTotal.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        lblValorTotal.setText("R$ ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(lblTituloPagamentos, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRegistrarPagamento))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 727, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTituloTotal)
                    .addComponent(lblValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTituloPagamentos)
                    .addComponent(btnRegistrarPagamento))
                .addGap(18, 18, 18)
                .addComponent(lblTituloTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblValorTotal)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarPagamentoActionPerformed
        // TODO add your handling code here:
        abrirTelaRegistro();
    }//GEN-LAST:event_btnRegistrarPagamentoActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ListarPagamentos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegistrarPagamento;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTituloPagamentos;
    private javax.swing.JLabel lblTituloTotal;
    private javax.swing.JLabel lblValorTotal;
    private javax.swing.JTable tblPagamentos;
    // End of variables declaration//GEN-END:variables
}
