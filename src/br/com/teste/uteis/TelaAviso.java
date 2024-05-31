/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.teste.uteis;

import br.com.teste.telas.TelaLogin;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Gustavo
 */
public class TelaAviso extends javax.swing.JFrame {

    public TelaAviso() throws UnsupportedLookAndFeelException {
        // Define a janela como undecorated (sem barra de título) antes de chamar initComponents()
        this.setUndecorated(true);
        initComponents();
        
        this.setBackground(new Color(0, 0, 0, 0)); // Cor transparente

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); 
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            // Trate a exceção se ocorrer
            System.err.println("Erro ao definir o Look and Feel: " + ex.getMessage());
        }
    }

    public TelaAviso(JFrame telaLogin, boolean modal) {
        // Define a janela como undecorated (sem barra de título) antes de chamar initComponents()
        this.setUndecorated(true);
        this.getRootPane().setBorder(new BordaRedonda(20)); // Define a borda arredondada
        this.setBackground(new Color(0, 0, 0, 0)); // Torna o fundo transparente
        initComponents();
        estilizarBotoes();
        
        
        // Remove qualquer outra borda que possa estar sendo aplicada à janela
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder());
        
        if (modal) {
            this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
            this.setAlwaysOnTop(true);
        }
        this.setLocationRelativeTo(telaLogin);
       

        // Adicionando um ouvinte de eventos ao botão Entendi
        btnEntendi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntendiActionPerformed(evt);
            }
        });

        // Adicionando um ouvinte de eventos ao botão Sair
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });
    }
    
    private void estilizarBotoes() {
        btnEntendi.setBackground(new Color(24, 140, 91)); // Define a cor de fundo padrão (verde)
        btnEntendi.setForeground(Color.WHITE); // Define a cor do texto (branco)
        btnEntendi.setBorder(new BordaRedonda(7)); // Aplica a borda com cantos arredondados (7 é o raio)
        btnEntendi.setBorderPainted(false); // Remove a borda pintada
        btnEntendi.setFont(new Font("Calibri", Font.BOLD, 14)); // Define a fonte (opcional)
        btnEntendi.setPreferredSize(new Dimension(150, 25)); // Define o tamanho preferido do botão
        btnEntendi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntendi.setOpaque(false); // Define o botão como transparente
        
        btnSair.setBackground(new Color(24, 140, 91)); // Define a cor de fundo padrão (verde)
        btnSair.setForeground(Color.WHITE); // Define a cor do texto (branco)
        btnSair.setBorder(new BordaRedonda(7)); // Aplica a borda com cantos arredondados (7 é o raio)
        btnSair.setBorderPainted(false); // Remove a borda pintada
        btnSair.setFont(new Font("Calibri", Font.BOLD, 14)); // Define a fonte (opcional)
        btnSair.setPreferredSize(new Dimension(150, 25)); // Define o tamanho preferido do botão
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSair.setOpaque(false); // Define o botão como transparente


        // Adiciona um ouvinte de mouse ao botão de acessar
        btnEntendi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Define a cor de fundo quando o mouse entra no botão
                btnEntendi.setBackground(new Color(7, 108, 65)); // Cor verde mais escura
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Define a cor de fundo padrão quando o mouse sai do botão
                btnEntendi.setBackground(new Color(24, 140, 91)); // Cor verde padrão
            }
        });
        
        btnSair.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Define a cor de fundo quando o mouse entra no botão
                btnSair.setBackground(new Color(7, 108, 65)); // Cor verde mais escura
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Define a cor de fundo padrão quando o mouse sai do botão
                btnSair.setBackground(new Color(24, 140, 91)); // Cor verde padrão
            }
        });
    }

    // Método para manipular o clique no botão Entendi
    private void btnEntendiActionPerformed(java.awt.event.ActionEvent evt) {
        // Fecha a janela de aviso
        this.dispose();
    }
    
    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {
        // Fecha o programa
        System.exit(0);

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
        btnEntendi = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnSair = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(400, 200));
        setMinimumSize(new java.awt.Dimension(200, 100));
        setPreferredSize(new java.awt.Dimension(360, 170));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(217, 217, 217));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(155, 155, 155), 2, true));
        jPanel1.setToolTipText("");
        jPanel1.setMaximumSize(new java.awt.Dimension(360, 143));
        jPanel1.setMinimumSize(new java.awt.Dimension(200, 100));

        btnEntendi.setText("Tentar Novamente");

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(24, 140, 91));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Erro ao logar");

        btnSair.setText("Sair do Programa");

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(24, 140, 91));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Os dados de login estão incorretos.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(btnEntendi, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEntendi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(TelaAviso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaAviso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaAviso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaAviso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TelaAviso().setVisible(true);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(TelaAviso.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntendi;
    private javax.swing.JButton btnSair;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    private void setModal(boolean modal) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
