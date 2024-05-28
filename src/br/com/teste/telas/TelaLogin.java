/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.teste.telas;

import br.com.teste.uteis.BordaRedonda;
import java.sql.*;
import br.com.teste.dal.Conexao;
import br.com.teste.uteis.TelaAviso;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Gustavo
 */
public class TelaLogin extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void logar() {
        String sql = "select * from almoxarife where login =? and senha =?";
        try {
            //essas linhas preparam a consulta ao banco para verificar login e senha
            //onde a ? é substituido pelo conteúdo das variáveis.
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtUsuario.getText());
            pst.setString(2, new String(txtSenha.getPassword()));

            rs = pst.executeQuery();

            if (rs.next()) {
                testeMenuNovo principal = new testeMenuNovo();
                principal.setVisible(true);
                //MenuPrincipal.lblUsuario.setText(rs.getString(2));
                //MenuPrincipal.lblUsuario.setForeground(Color.blue);
                this.dispose();
                conn.close();
            } else {
            TelaAviso aviso = new TelaAviso(this, true); // 'this' se refere ao JFrame atual (TelaLogin)
            aviso.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    /**
     * Creates new form TelaLogin
     */
    public TelaLogin() {
        initComponents();
        conn = Conexao.getConexao();
        if (conn != null) {
            lblstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/bancoconectado.png")));
        } else {
            lblstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/bancodesconectado.png")));
        }

        // Adiciona os placeholders nos campos
        adicionarPlaceholders();
        // Estiliza o botão de acessar
        estilizarBotaoLogin();
        // Altera o icone
        definirIconeJanela();

    }

    private void adicionarPlaceholders() {
        // Placeholder para o campo de usuário
        txtUsuario.setForeground(new Color(24, 140, 91)); // Cor verde escuro
        txtUsuario.setText("Digite o login");
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                txtUsuario.getBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 0) // Margem interna de 10 pixels à esquerda
        ));
        txtUsuario.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtUsuario.getText().equals("Digite o login")) {
                    txtUsuario.setText("");
                    txtUsuario.setForeground(new Color(24, 140, 91)); // Define a cor do texto como verde
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtUsuario.getText().isEmpty()) {
                    txtUsuario.setForeground(new Color(24, 140, 91)); // Define a cor do texto como verde
                    txtUsuario.setText("Digite o login");
                }
            }
        });

        // Placeholder para o campo de senha
        txtSenha.setForeground(new Color(24, 140, 91)); // Cor verde escuro
        txtSenha.setEchoChar((char) 0); // Remove o caractere de senha inicialmente
        txtSenha.setText("Digite a senha");
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                txtSenha.getBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 0) // Margem interna de 10 pixels à esquerda
        ));
        txtSenha.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSenha.getText().equals("Digite a senha")) {
                    txtSenha.setText("");
                    txtSenha.setForeground(new Color(24, 140, 91)); // Define a cor do texto como verde
                    txtSenha.setEchoChar('*'); // Define o caractere de senha ao obter foco
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSenha.getText().isEmpty()) {
                    txtSenha.setForeground(new Color(24, 140, 91)); // Define a cor do texto como verde
                    txtSenha.setText("Digite a senha");
                    txtSenha.setEchoChar((char) 0); // Remove o caractere de senha ao perder foco
                }
            }
        });
    }

    // Método para estilizar o botão de acessar
    private void estilizarBotaoLogin() {
        btnLogin.setBackground(new Color(24, 140, 91)); // Define a cor de fundo padrão (verde)
        btnLogin.setForeground(Color.WHITE); // Define a cor do texto (branco)
        btnLogin.setBorder(new BordaRedonda(7)); // Aplica a borda com cantos arredondados (7 é o raio)
        btnLogin.setBorderPainted(false); // Remove a borda pintada
        btnLogin.setFont(new Font("Calibri", Font.BOLD, 14)); // Define a fonte (opcional)
        btnLogin.setPreferredSize(new Dimension(150, 25)); // Define o tamanho preferido do botão
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        // Adiciona um ouvinte de mouse ao botão de acessar
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Define a cor de fundo quando o mouse entra no botão
                btnLogin.setBackground(new Color(7, 108, 65)); // Cor verde mais escura
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Define a cor de fundo padrão quando o mouse sai do botão
                btnLogin.setBackground(new Color(24, 140, 91)); // Cor verde padrão
            }
        });
    }

    private void definirIconeJanela() {
        // Carrega o ícone da sua aplicação
        ImageIcon icon = new ImageIcon(getClass().getResource("/br/com/teste/icones/icone.png"));

        // Define o ícone da janela
        setIconImage(icon.getImage());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtUsuario = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        lblstatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("StockSync - Login");
        setBackground(new java.awt.Color(51, 51, 51));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(217, 217, 217));
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 455));

        txtUsuario.setBackground(new java.awt.Color(200, 200, 200));
        txtUsuario.setForeground(new java.awt.Color(26, 131, 43));
        txtUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(155, 155, 155)));
        txtUsuario.setName(""); // NOI18N

        txtSenha.setBackground(new java.awt.Color(200, 200, 200));
        txtSenha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(155, 155, 155)));
        txtSenha.setCaretColor(new java.awt.Color(143, 142, 142));

        btnLogin.setText("Acessar");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        lblstatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/bancoconectado.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(24, 140, 91));
        jLabel1.setText("Login");
        jLabel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel1FocusGained(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(24, 140, 91));
        jLabel5.setText("Preencha os campos com os dados de ");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(24, 140, 91));
        jLabel6.setText("acesso.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtSenha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                    .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addGap(32, 32, 32))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblstatus)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(1, 1, 1)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addComponent(lblstatus))
        );

        jPanel2.setBackground(new java.awt.Color(24, 140, 91));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 32)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mantendo seu");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 32)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("estoque em");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 32)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("sintonia.");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/STOCKSYNCIMG3.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(716, 494));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        logar();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void jLabel1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1FocusGained

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
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblstatus;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
