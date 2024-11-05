/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.teste.telas;

import br.com.teste.dal.Conexao;
import com.k33ptoo.components.KButton;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author NOTE
 */
public class telaCadastro extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form telaCadastro
     */
    public telaCadastro() {
        initComponents();
        conn = Conexao.getConexao();

        adicionarPlaceholders();

        preencherComboBoxCad();

        definirIconeJanela();

        estilizarBotaoCadastrar(btnCadastrar);

        estilizarComboBox(comboBoxCad);
    }

    private void adicionar() {
        Connection conn = null; // Não inicialize aqui
        PreparedStatement pst = null;
        ResultSet countRs = null;

        try {
            conn = Conexao.getConexao(); // Obtém a conexão
            String sql = "INSERT INTO almoxarife(nome, login, senha, tipo_almoxarife) VALUES (?, ?, ?, ?)";

            // Verifica quantos usuários já existem na tabela almoxarife
            String countSql = "SELECT COUNT(*) FROM almoxarife";
            pst = conn.prepareStatement(countSql);
            countRs = pst.executeQuery();

            // Determina o tipo de almoxarife baseado na quantidade de registros
            String tipoAlmoxarife;
            if (countRs.next() && countRs.getInt(1) == 0) {
                tipoAlmoxarife = "adm"; // Primeiro usuário será 'adm'
            } else {
                tipoAlmoxarife = "compras"; // Usuários subsequentes serão 'compras'
            }

            // Agora, insere os dados do novo usuário
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtCadNome.getText());
            pst.setString(2, txtCadUsuario.getText());
            pst.setString(3, new String(txtCadSenha.getPassword()));
            pst.setString(4, tipoAlmoxarife); // Define o tipo de almoxarife

            // Verifica campos obrigatórios
            if (txtCadNome.getText().isEmpty() || txtCadUsuario.getText().isEmpty() || txtCadSenha.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso.");
                    this.dispose(); // Fecha a tela de cadastro após o sucesso
                }
            }
        } catch (MysqlDataTruncation e) {
            JOptionPane.showMessageDialog(null, "Um dos campos excedeu o tamanho permitido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro no banco de dados: " + e.getMessage());
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado na interface gráfica.");
        } finally {
            // Fechar os recursos
            try {
                if (countRs != null) {
                    countRs.close(); // Fecha o ResultSet
                }
                if (pst != null) {
                    pst.close(); // Fecha o PreparedStatement
                }
                if (conn != null) {
                    conn.close(); // Fecha a conexão
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar os recursos: " + e.getMessage());
            }
        }
    }

    // função de estilo para a combobox
    public void estilizarComboBox(JComboBox<String> comboBox) {
        // Remove a borda
        comboBox.setBorder(BorderFactory.createEmptyBorder());

        // Altera a cor do popup da JComboBox
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground(new Color(26, 131, 43)); // Cor de fundo da seleção
                    c.setForeground(Color.WHITE); // Cor do texto da seleção
                } else {
                    c.setBackground(Color.WHITE); // Cor de fundo normal
                    c.setForeground(Color.BLACK); // Cor do texto normal
                }
                return c;
            }
        });

        comboBox.setFont(new Font("Arial", Font.PLAIN, 14)); // Fonte
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor ao passar o mouse

        // Personalizar as cores de fundo e texto
        comboBox.setBackground(new Color(240, 240, 240)); // Cor de fundo padrão
        comboBox.setForeground(Color.BLACK); // Cor do texto padrão
    }

    private void preencherComboBoxCad() {
        conn = Conexao.getConexao();
        String sql = "SHOW COLUMNS FROM almoxarife LIKE 'tipo_almoxarife'";
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                // Obtém o tipo de dados do campo tipo_almoxarife
                String columnType = rs.getString("Type");
                // Remove o prefixo "enum(" e o sufixo ")" para obter os valores
                String enumValues = columnType.substring(columnType.indexOf("(") + 1, columnType.lastIndexOf(")"));
                // Divide os valores separados por vírgula e remove as aspas
                String[] values = enumValues.replace("'", "").split(",");

                // Preenche a comboBoxCad com os valores do ENUM
                comboBoxCad.setModel(new DefaultComboBoxModel<>(values));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher a combobox: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão: " + ex.getMessage());
            }
        }
    }

    private void estilizarBotaoCadastrar(KButton btn) {
        String nome = "Cadastrar"; // Texto do botão

        btn.setPreferredSize(new Dimension(200, 75)); // Ajusta o tamanho do botão
        btn.setLayout(new GridBagLayout()); // Usar GridBagLayout para melhor controle de posicionamento

        // Define o texto do botão
        JLabel textLabel = new JLabel(nome);
        textLabel.setForeground(Color.WHITE); // Define a cor do texto como branco
        textLabel.setFont(new Font("Roboto", Font.BOLD, 16)); // Mantém a fonte Roboto

        // Configura o GridBagConstraints para centralizar o texto
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Coluna 0
        gbc.gridy = 0; // Linha 0
        gbc.anchor = GridBagConstraints.CENTER; // Centraliza
        gbc.fill = GridBagConstraints.NONE; // Não preenche todo o espaço
        btn.add(textLabel, gbc); // Adiciona o texto ao botão

        // Configurações de estilo
        btn.setBackground(new Color(24, 140, 91)); // Cor de fundo
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efeito de hover (muda a cor ao passar o mouse)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(7, 108, 65)); // Cor ao passar o mouse
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(24, 140, 91)); // Cor padrão ao sair
            }
        });
    }

    // placeholdrs para os campos de texto
    private void adicionarPlaceholders() {
        // Placeholder para o campo Nome
        txtCadNome.setForeground(new Color(26, 131, 43)); // Cor verde escuro
        txtCadNome.setText("Digite o seu Nome");
        txtCadNome.setBorder(BorderFactory.createCompoundBorder(
                txtCadNome.getBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 0) // Margem interna de 10 pixels à esquerda
        ));
        txtCadNome.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtCadNome.getText().equals("Digite o seu Nome")) {
                    txtCadNome.setText("");
                    txtCadNome.setForeground(new Color(26, 131, 43)); // Define a cor do texto como verde
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtCadNome.getText().isEmpty()) {
                    txtCadNome.setForeground(new Color(26, 131, 43)); // Define a cor do texto como verde
                    txtCadNome.setText("Digite o seu Nome");
                }
            }
        });

        // Placeholder para o campo de usuário
        txtCadUsuario.setForeground(new Color(26, 131, 43)); // Cor verde escuro
        txtCadUsuario.setText("Crie um Login");
        txtCadUsuario.setBorder(BorderFactory.createCompoundBorder(
                txtCadUsuario.getBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 0) // Margem interna de 10 pixels à esquerda
        ));
        txtCadUsuario.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtCadUsuario.getText().equals("Crie um Login")) {
                    txtCadUsuario.setText("");
                    txtCadUsuario.setForeground(new Color(26, 131, 43)); // Define a cor do texto como verde
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtCadUsuario.getText().isEmpty()) {
                    txtCadUsuario.setForeground(new Color(26, 131, 43)); // Define a cor do texto como verde
                    txtCadUsuario.setText("Crie um Login");
                }
            }
        });

        // Placeholder para o campo de senha
        txtCadSenha.setForeground(new Color(26, 131, 43)); // Cor verde escuro
        txtCadSenha.setEchoChar((char) 0); // Remove o caractere de senha inicialmente
        txtCadSenha.setText("Crie uma Senha");
        txtCadSenha.setBorder(BorderFactory.createCompoundBorder(
                txtCadSenha.getBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 0) // Margem interna de 10 pixels à esquerda
        ));
        txtCadSenha.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtCadSenha.getText().equals("Crie uma Senha")) {
                    txtCadSenha.setText("");
                    txtCadSenha.setForeground(new Color(26, 131, 43)); // Define a cor do texto como verde
                    txtCadSenha.setEchoChar('*'); // Define o caractere de senha ao obter foco
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtCadSenha.getText().isEmpty()) {
                    txtCadSenha.setForeground(new Color(26, 131, 43)); // Define a cor do texto como verde
                    txtCadSenha.setText("Crie uma Senha");
                    txtCadSenha.setEchoChar((char) 0); // Remove o caractere de senha ao perder foco
                }
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
        kGradientPanel1 = new com.k33ptoo.components.KGradientPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCadUsuario = new javax.swing.JTextField();
        txtCadSenha = new javax.swing.JPasswordField();
        txtCadNome = new javax.swing.JTextField();
        btnCadastrar = new com.k33ptoo.components.KButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        comboBoxCad = new javax.swing.JComboBox<>();

        setTitle("Cadastro - StockSync");

        jPanel1.setBackground(new java.awt.Color(217, 217, 217));

        kGradientPanel1.setkBorderRadius(0);
        kGradientPanel1.setkEndColor(new java.awt.Color(26, 131, 43));
        kGradientPanel1.setkStartColor(new java.awt.Color(26, 131, 43));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CADASTRO");

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        txtCadUsuario.setBackground(new java.awt.Color(200, 200, 200));
        txtCadUsuario.setForeground(new java.awt.Color(26, 131, 43));
        txtCadUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(155, 155, 155)));
        txtCadUsuario.setName(""); // NOI18N

        txtCadSenha.setBackground(new java.awt.Color(200, 200, 200));
        txtCadSenha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(155, 155, 155)));
        txtCadSenha.setCaretColor(new java.awt.Color(143, 142, 142));
        txtCadSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCadSenhaActionPerformed(evt);
            }
        });

        txtCadNome.setBackground(new java.awt.Color(200, 200, 200));
        txtCadNome.setForeground(new java.awt.Color(26, 131, 43));
        txtCadNome.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(155, 155, 155)));
        txtCadNome.setName(""); // NOI18N

        btnCadastrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCadastrar.setkAllowGradient(false);
        btnCadastrar.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnCadastrar.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnCadastrar.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(26, 131, 43));
        jLabel2.setText("Nome");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(26, 131, 43));
        jLabel3.setText("Usuário");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(26, 131, 43));
        jLabel4.setText("Senha");

        comboBoxCad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(txtCadNome, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtCadSenha)
                        .addComponent(txtCadUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(comboBoxCad, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCadNome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCadUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCadSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(comboBoxCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
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

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        // TODO add your handling code here:
        adicionar();
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void txtCadSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCadSenhaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCadSenhaActionPerformed

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
            java.util.logging.Logger.getLogger(telaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(telaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(telaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(telaCadastro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new telaCadastro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.k33ptoo.components.KButton btnCadastrar;
    private javax.swing.JComboBox<String> comboBoxCad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private com.k33ptoo.components.KGradientPanel kGradientPanel1;
    private javax.swing.JTextField txtCadNome;
    private javax.swing.JPasswordField txtCadSenha;
    private javax.swing.JTextField txtCadUsuario;
    // End of variables declaration//GEN-END:variables
}
