/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.ifba.prg03projetosgcr.cliente.view;

import br.com.ifba.prg03projetosgcr.cliente.controller.ClienteController;
import br.com.ifba.prg03projetosgcr.cliente.entity.Cliente;
import br.com.ifba.prg03projetosgcr.cliente.service.ClienteService;
import br.com.ifba.prg03projetosgcr.cliente.view.components.BotaoCelulaRenderer;
import br.com.ifba.prg03projetosgcr.cliente.view.components.BotaoCelulaEditor;
import jakarta.annotation.PostConstruct;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author joaol
 */
@Component
public class ListarClientes extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ListarClientes.class.getName());
    
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ClienteController clienteController;
    
    //lista para gaurdar a referencia dos clientes vindo do banco
    private List<Cliente> clientesCadastrados = new ArrayList<>();
    
    
    public ListarClientes() {
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
        DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel();
        modelo.setRowCount(0);
        
        //aumenta a altura das linhas da tablea
        tblClientes.setRowHeight(45);
        
        //Configurar a coluna de ações com botoes de editar e deletar
        configurarColunaAcoes();
        
        //apaga o "Pesquise por nome..." ao clicar e recoloca se sair sem digitar nada
        txtPesquisa.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt){
                if(txtPesquisa.getText().equals("Pesquise por nome...")){
                    txtPesquisa.setText(""); //limpa o campo
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt){
                if(txtPesquisa.getText().trim().isEmpty()){
                    txtPesquisa.setText("Pesquise por nome..."); //volta para o texto padrao
                    atualizarTabela(); //recarrega a tabela completa
                }
            }
        });
        
        //faz pesquisa dinamica atualiza a tabela cada vez que uma tecla é acionada
        txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt){
            //toda vez o usuario usa uma tecla, a tabela filta
            atualizarTabela();
            }
        });
    }
    
    // O @PostConstruct faz com que este método rode automaticamente 
    // APÓS o Spring injetar o ClienteService. Se tentar chamar o 
    // serviço direto no construtor, vai dar NullPointerException.
    @PostConstruct
    public void init(){
        atualizarTabela();
    }
    
    //agora ele pesquisa por nome, se nao houver nome na pesquisa ele lista todos
    public void atualizarTabela(){
        String nomeBusca = txtPesquisa.getText().trim();
        clientesCadastrados = clienteController.findByNome(nomeBusca);
            
        DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel();
        modelo.setRowCount(0); //limpa as linhas atuais
        
        for (Cliente c : clientesCadastrados){
            //adiciona as colunas: cliente, divida, contato e acoes
            modelo.addRow(new Object[]{
                c.getNome(),
                "R$ " + String.format("%.2f", c.getSaldoDevedor()),
                c.getTelefone(),
                "" //cooluna de ações é renderizada pelos botoes
            });
        }
    }
    
    private void configurarColunaAcoes(){
        //criar o renderer personalizado para a coluna acoes 
        BotaoCelulaRenderer renderer = new BotaoCelulaRenderer(tblClientes);
        
        //obter a coluna de acoes
        TableColumn colunaAcoes = tblClientes.getColumnModel().getColumn(3);
        colunaAcoes.setCellRenderer(renderer);
        colunaAcoes.setCellEditor(new BotaoCelulaEditor(tblClientes, this));
        
        
    }
    
    public void editarCliente(int linha){
        Cliente clienteSelecionado = clientesCadastrados.get(linha);    
        
        //criar e configurar a tela de edição
        EditarCliente telaEditar = context.getBean(EditarCliente.class);
        telaEditar.preencherCampos(clienteSelecionado);
        telaEditar.setListarClientes(this);
        telaEditar.setVisible(true);

    }
    
    public void deletarCliente(int linha){
        //pega o cliente exato da lista
        Cliente clienteSelecionado = clientesCadastrados.get(linha);
        
        
        int confirmacao = javax.swing.JOptionPane.showConfirmDialog(this, 
            "Tem ceteza que deseja deletar o cliente: " + clienteSelecionado.getNome() + "?", 
            "Confirmar Exclusão", 
            javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE);
        
        if(confirmacao == javax.swing.JOptionPane.YES_OPTION){
            //deleta do banco usando o id
            clienteController.delete(clienteSelecionado.getId());
            
            //atualiza a tabela
            atualizarTabela();
            
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Cliente deletado com sucesso",
                "Sucesso",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void abrirTelaCadastro(){
        // como a tela é prototype, o context.getBean cria uma tela zerada toda vez
        CadastrarClientes telaCadastro = context.getBean(CadastrarClientes.class);
        telaCadastro.setListarClientes(this);
        telaCadastro.setVisible(true);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        btnCadastrarCliente = new javax.swing.JButton();
        txtPesquisa = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(214, 214, 214));

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel1.setText("Gestão de Clientes");

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cliente", "Dívida Total", "Contato", "Ações"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblClientes);

        btnCadastrarCliente.setBackground(new java.awt.Color(0, 102, 51));
        btnCadastrarCliente.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btnCadastrarCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnCadastrarCliente.setText("Cadastrar Cliente");
        btnCadastrarCliente.addActionListener(this::btnCadastrarClienteActionPerformed);

        txtPesquisa.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txtPesquisa.setText("Pesquise por nome...");
        txtPesquisa.addActionListener(this::txtPesquisaActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(93, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCadastrarCliente)
                        .addGap(79, 79, 79))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnCadastrarCliente))
                .addGap(12, 12, 12)
                .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarClienteActionPerformed
        // TODO add your handling code here:
        abrirTelaCadastro();

    }//GEN-LAST:event_btnCadastrarClienteActionPerformed

    private void txtPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisaActionPerformed
        // TODO add your handling code here:
        atualizarTabela();
    }//GEN-LAST:event_txtPesquisaActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ListarClientes().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrarCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtPesquisa;
    // End of variables declaration//GEN-END:variables
}
