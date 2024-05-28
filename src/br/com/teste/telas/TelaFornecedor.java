/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.teste.telas;

/**
 *
 * @author Gustavo
 */
import java.sql.*;
import br.com.teste.dal.Conexao;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

public class TelaFornecedor extends javax.swing.JInternalFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaUsuario
     */
    public TelaFornecedor() {
        initComponents();
        conn = Conexao.getConexao();
        pesquisar_fornecedor();
    }

    //metodo para adicionar fornecedores
    private void adicionar() {
        String sql = "insert into fornecedor(nome_fornecedor,cnpj,email,numero_telefone,endereco,site) values(?,?,?,?,?,?)";

        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtFornNome.getText());
            pst.setString(2, txtFornCnpj.getText());
            pst.setString(3, txtFornEmail.getText());
            pst.setString(4, txtFornFone.getText());
            pst.setString(5, txtFornEndereco.getText());
            pst.setString(6, txtFornSite.getText());

            if ((txtFornNome.getText().isEmpty()) || (txtFornEmail.getText().isEmpty()) || (txtFornFone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Fornecedor Cadastrado com Sucesso.");

                    limpar(); //chamando a função de limpar os campos
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para buscar fornecedores
    private void pesquisar_fornecedor() {
        //String sql = "select * from fornecedor where nome_fornecedor like ?";
        String sql = "select id_fornecedor AS ID, nome_fornecedor AS Fornecedor, cnpj AS CNPJ, email AS Email, numero_telefone AS Telefone, endereco AS Endereço,"
                + "site AS Site from fornecedor where nome_fornecedor like ?";
        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, txtFornPesquisar.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblFornecedores.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para setar os campos do formulário com o conteúdo da tabela que foi clicada
    public void setar_campos() {
        int setar = tblFornecedores.getSelectedRow();
        txtFornId.setText(tblFornecedores.getModel().getValueAt(setar, 0).toString());
        txtFornNome.setText(tblFornecedores.getModel().getValueAt(setar, 1).toString());
        txtFornCnpj.setText(tblFornecedores.getModel().getValueAt(setar, 2).toString());
        txtFornEmail.setText(tblFornecedores.getModel().getValueAt(setar, 3).toString());
        txtFornFone.setText(tblFornecedores.getModel().getValueAt(setar, 4).toString());
        txtFornEndereco.setText(tblFornecedores.getModel().getValueAt(setar, 5).toString());
        txtFornSite.setText(tblFornecedores.getModel().getValueAt(setar, 6).toString());

        //a linha abaixo irá desabilitar o botão de adicionar, para evitar dados duplicados.
        btnAdicionar.setEnabled(false);
    }

    //metodo para alterar dados dos fornecedores
    private void alterar() {
        String sql = "UPDATE fornecedor SET nome_fornecedor=?, cnpj=?, email=?, numero_telefone=?, endereco=?, site=? WHERE id_fornecedor=?";

        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtFornNome.getText());
            pst.setString(2, txtFornCnpj.getText());
            pst.setString(3, txtFornEmail.getText());
            pst.setString(4, txtFornFone.getText());
            pst.setString(5, txtFornEndereco.getText());
            pst.setString(6, txtFornSite.getText());
            pst.setString(7, txtFornId.getText());

            if ((txtFornNome.getText().isEmpty()) || (txtFornEmail.getText().isEmpty()) || (txtFornFone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do Fornecedor Alterados com Sucesso.");

                    limpar(); //chamando a função de limpar os campos
                    btnAdicionar.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para excluir cadastro dos fornecedores
    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este Fornecedor?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM fornecedor WHERE id_fornecedor=?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtFornId.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Fornecedor Removido com Sucesso!");
                    limpar(); //chamando a função de limpar os campos
                    btnAdicionar.setEnabled(true);
                }
            } catch (Exception e) {
            }
        }
    }

    //método para limpar os campos do formulário
    private void limpar() {
        txtFornId.setText(null);
        txtFornNome.setText(null);
        txtFornCnpj.setText(null);
        txtFornEmail.setText(null);
        txtFornFone.setText(null);
        txtFornEndereco.setText(null);
        txtFornSite.setText(null);
        txtFornPesquisar.setText(null);
        ((DefaultTableModel) tblFornecedores.getModel()).setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtFornNome = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtFornEndereco = new javax.swing.JTextField();
        txtFornEmail = new javax.swing.JTextField();
        txtFornCnpj = new javax.swing.JTextField();
        txtFornFone = new javax.swing.JTextField();
        txtFornSite = new javax.swing.JTextField();
        txtFornPesquisar = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFornecedores = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtFornId = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Fornecedor");
        setPreferredSize(new java.awt.Dimension(700, 550));

        jLabel2.setText("* NOME");

        jLabel3.setText("CNPJ");

        jLabel4.setText("* TELEFONE");

        jLabel5.setText("* E-MAIL");

        jLabel6.setText("ENDEREÇO");

        txtFornNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFornNomeActionPerformed(evt);
            }
        });

        jLabel7.setText("SITE");

        txtFornPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFornPesquisarKeyReleased(evt);
            }
        });

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/adicionar.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/editar.png"))); // NOI18N
        btnAlterar.setToolTipText("Alterar");
        btnAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/excluir.png"))); // NOI18N
        btnExcluir.setToolTipText("Excluir");
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/pesquisar2.png"))); // NOI18N

        tblFornecedores = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblFornecedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "CNPJ", "E-Mail", "Telefone", "Endereço", "Site"
            }
        ));
        tblFornecedores.setFocusable(false);
        tblFornecedores.getTableHeader().setReorderingAllowed(false);
        tblFornecedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFornecedoresMouseClicked(evt);
            }
        });
        tblFornecedores.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblFornecedoresKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblFornecedores);

        jLabel9.setText("* Campos Obrigatórios");

        jLabel1.setText("ID Fornecedor");

        txtFornId.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(114, 114, 114)
                                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(114, 114, 114)
                                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtFornCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(35, 35, 35)
                                    .addComponent(jLabel4)
                                    .addGap(10, 10, 10)
                                    .addComponent(txtFornFone))
                                .addComponent(txtFornNome)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtFornEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(35, 35, 35)
                                    .addComponent(jLabel7)
                                    .addGap(10, 10, 10)
                                    .addComponent(txtFornSite))
                                .addComponent(txtFornEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtFornId, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(21, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addGap(38, 38, 38))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(txtFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtFornId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFornNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFornFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFornCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFornEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFornEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFornSite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExcluir))
                .addGap(101, 101, 101))
        );

        setBounds(0, 0, 700, 550);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // Método para adicionar Fornecedor
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void tblFornecedoresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblFornecedoresKeyReleased

    }//GEN-LAST:event_tblFornecedoresKeyReleased

    private void txtFornPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFornPesquisarKeyReleased
        //o evento abaixo é do tipo "enquanto for digitando"
        pesquisar_fornecedor();
    }//GEN-LAST:event_txtFornPesquisarKeyReleased

    private void tblFornecedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFornecedoresMouseClicked
        //EVENTO QUE SERÁ USADO PARA SETAR OS CAMPOS DA TABELA(CLICANDO COM O BOTÃO DO MOUSE)
        //e chamando o método para setar os campos.
        setar_campos();
    }//GEN-LAST:event_tblFornecedoresMouseClicked

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        //Chamando o método para alterar dados do Fornecedor.
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // Método para remover Fornecedor
        remover();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFornNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFornNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFornNomeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblFornecedores;
    private javax.swing.JTextField txtFornCnpj;
    private javax.swing.JTextField txtFornEmail;
    private javax.swing.JTextField txtFornEndereco;
    private javax.swing.JTextField txtFornFone;
    private javax.swing.JTextField txtFornId;
    private javax.swing.JTextField txtFornNome;
    private javax.swing.JTextField txtFornPesquisar;
    private javax.swing.JTextField txtFornSite;
    // End of variables declaration//GEN-END:variables

}
