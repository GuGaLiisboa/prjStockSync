/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.teste.uteis;

import br.com.teste.dal.Conexao;
import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Gustavo
 */
public class PopupCategoria extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form PopupCategoria
     */
    public PopupCategoria() {
        initComponents();
        
        btnVincularCatNova.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    }

    private void substituirCategoria() {
        conn = Conexao.getConexao();

        String sql = "UPDATE material SET id_categoria = ? WHERE id_categoria = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, IdCategoriaNova.getText());
            pst.setString(2, IdCategoriaAntiga.getText());

            int atualizados = pst.executeUpdate();
            if (atualizados > 0) {
                JOptionPane.showMessageDialog(null, "Categoria substituída com sucesso!");

            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao substituir a categoria: " + e.getMessage());
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

        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        IdCategoriaAntiga = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        IdCategoriaNova = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btnVincularCatNova = new com.k33ptoo.components.KButton();

        setTitle("StockSync - Alterar Categoria");
        setBackground(new java.awt.Color(217, 217, 217));
        setResizable(false);

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(26, 131, 43));
        jLabel8.setText("ALTERAR CATEGORIA ");

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(26, 131, 43));
        jLabel9.setText("AO EXCLUIR UMA CATEGORIA QUE JÁ ESTÁ VINCULADA A ALGUM ");

        jLabel10.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(26, 131, 43));
        jLabel10.setText("MATERIAL, VOCÊ DEVE INFORMAR UMA NOVA CATEGORIA ABAIXO.");

        IdCategoriaAntiga.setBackground(new java.awt.Color(223, 223, 223));
        IdCategoriaAntiga.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        IdCategoriaAntiga.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        IdCategoriaAntiga.setSelectionColor(new java.awt.Color(26, 131, 43));

        jLabel11.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(26, 131, 43));
        jLabel11.setText("ID CATEGORIA");

        IdCategoriaNova.setBackground(new java.awt.Color(223, 223, 223));
        IdCategoriaNova.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        IdCategoriaNova.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        IdCategoriaNova.setSelectionColor(new java.awt.Color(26, 131, 43));

        jLabel12.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(26, 131, 43));
        jLabel12.setText("NOVO ID CATEGORIA");

        btnVincularCatNova.setText("Vincular");
        btnVincularCatNova.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVincularCatNova.setkAllowGradient(false);
        btnVincularCatNova.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnVincularCatNova.setkBorderRadius(20);
        btnVincularCatNova.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnVincularCatNova.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnVincularCatNova.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVincularCatNovaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(82, 82, 82))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9)
                        .addComponent(jLabel10)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel12)
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(IdCategoriaNova)
                                .addComponent(IdCategoriaAntiga, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(27, 27, 27))
            .addGroup(layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(btnVincularCatNova, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addGap(0, 0, 0)
                .addComponent(jLabel10)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IdCategoriaAntiga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(IdCategoriaNova, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(27, 27, 27)
                .addComponent(btnVincularCatNova, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnVincularCatNovaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVincularCatNovaActionPerformed
        // TODO add your handling code here:
        substituirCategoria();
    }//GEN-LAST:event_btnVincularCatNovaActionPerformed

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
            java.util.logging.Logger.getLogger(PopupCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PopupCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PopupCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PopupCategoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PopupCategoria().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IdCategoriaAntiga;
    private javax.swing.JTextField IdCategoriaNova;
    private com.k33ptoo.components.KButton btnVincularCatNova;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    // End of variables declaration//GEN-END:variables
}
