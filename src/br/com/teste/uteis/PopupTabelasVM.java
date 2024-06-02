/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.teste.uteis;

import br.com.teste.dal.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Gustavo
 */
public class PopupTabelasVM extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form PopupTabelasVM
     */
    public PopupTabelasVM() {
        initComponents();
        pesquisar_fornecedorEmCat();
        pesquisar_materialEmCat();
    }

    //metodo para buscar uma fornecedor no menu Categorias
    private void pesquisar_fornecedorEmCat() {
        //String sql = "select * from fornecedor where nome_fornecedor like ?";
        conn = Conexao.getConexao();
        String sql = "select id_fornecedor AS ID, nome_fornecedor AS Fornecedor from fornecedor where nome_fornecedor like ?";
        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1,"%" + txtBuscarFornecedor.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblFornecedorVM.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para buscar um material no menu Categorias
    private void pesquisar_materialEmCat() {
        conn = Conexao.getConexao();
        String sql = "select id_material AS ID, id_categoria AS 'ID Categoria', nome_material AS 'Nome do Material' FROM material where nome_material like ?";
        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1,"%" + txtBuscarMaterial.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblMaterialVM.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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

        jScrollPane7 = new javax.swing.JScrollPane();
        tblMaterialVM = new javax.swing.JTable();
        txtBuscarFornecedor = new javax.swing.JTextField();
        txtBuscarMaterial = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFornecedorVM = new javax.swing.JTable();
        btnAtualizar = new com.k33ptoo.components.KButton();

        tblFornecedorVM = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblMaterialVM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Nome do Fornecedor"
            }
        ));
        tblMaterialVM.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(tblMaterialVM);

        txtBuscarFornecedor.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarFornecedor.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarFornecedor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarFornecedor.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarFornecedorKeyReleased(evt);
            }
        });

        txtBuscarMaterial.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarMaterial.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarMaterial.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarMaterial.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarMaterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarMaterialKeyReleased(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(26, 131, 43));
        jLabel30.setText("Buscar Fornecedor");

        jLabel31.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(26, 131, 43));
        jLabel31.setText("Buscar Material");

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(26, 131, 43));
        jLabel8.setText("VISUALIZAR FORNECEDORES E MATERIAIS");

        tblFornecedorVM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblFornecedorVM);

        btnAtualizar.setText("Atualizar");
        btnAtualizar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAtualizar.setkAllowGradient(false);
        btnAtualizar.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnAtualizar.setkBorderRadius(20);
        btnAtualizar.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnAtualizar.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(166, 166, 166)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBuscarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(txtBuscarMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel8)
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarFornecedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarFornecedorKeyReleased
        // TODO add your handling code here:
        pesquisar_fornecedorEmCat();
    }//GEN-LAST:event_txtBuscarFornecedorKeyReleased

    private void txtBuscarMaterialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarMaterialKeyReleased
        // TODO add your handling code here:
        pesquisar_materialEmCat();
    }//GEN-LAST:event_txtBuscarMaterialKeyReleased

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
        pesquisar_materialEmCat();
        pesquisar_fornecedorEmCat();
    }//GEN-LAST:event_btnAtualizarActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PopupTabelasVM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PopupTabelasVM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PopupTabelasVM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PopupTabelasVM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PopupTabelasVM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.k33ptoo.components.KButton btnAtualizar;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable tblFornecedorVM;
    private javax.swing.JTable tblMaterialVM;
    private javax.swing.JTextField txtBuscarFornecedor;
    private javax.swing.JTextField txtBuscarMaterial;
    // End of variables declaration//GEN-END:variables
}
