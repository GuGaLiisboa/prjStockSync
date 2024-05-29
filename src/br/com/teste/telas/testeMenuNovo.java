/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.teste.telas;

import java.sql.*;
import br.com.teste.dal.Conexao;
import com.k33ptoo.components.KButton;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Gustavo
 */
public class testeMenuNovo extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form testeMenuNovo
     */
    public testeMenuNovo() {
        initComponents();
        conn = Conexao.getConexao();
        //Estiliza os botões com imagens do menu cadastro
        estilizarBotao(btnCadFornecedor, "FORNECEDOR", "..\\prjStockSync\\src\\br\\com\\teste\\icones\\iconeCaminhao.png");
        estilizarBotao(btnCadCategoria, "CATEGORIA", "..\\prjStockSync\\src\\br\\com\\teste\\icones\\iconeCategoria.png");
        estilizarBotao(btnCadMaterial, "MATERIAL", "..\\prjStockSync\\src\\br\\com\\teste\\icones\\iconeMaterial.png");

        pesquisar_fornecedor(); //Atualizar as tabela fornecedor
        pesquisar_material();
        definirIconeJanela();
        btnEdit();

    }

    //setar o icone mão nos botões
    private void btnEdit() {
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadastros.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadastrarMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterarMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluirMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    //metodo para adicionar fornecedores
    private void adicionar() {
        conn = Conexao.getConexao();
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
                    pesquisar_fornecedor();
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para buscar fornecedores
    private void pesquisar_fornecedor() {
        //String sql = "select * from fornecedor where nome_fornecedor like ?";
        conn = Conexao.getConexao();
        String sql = "select id_fornecedor AS ID, nome_fornecedor AS Fornecedor, cnpj AS CNPJ, email AS Email, numero_telefone AS Telefone, endereco AS Endereço,"
                + "site AS Site from fornecedor where nome_fornecedor like ?";
        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, txtFornPesquisar.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblFornecedores.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para setar os campos do formulário com o conteúdo da tabela fornecedor
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
        btnAdicionar.setkBackGroundColor(new Color(128, 128, 128));
        btnAdicionar.setkHoverColor(new Color(128, 128, 128));
    }

    //metodo para alterar dados dos fornecedores
    private void alterar() {
        conn = Conexao.getConexao();
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
                    pesquisar_fornecedor(); //atualizar a tabela
                    btnAdicionar.setEnabled(true);
                    btnAdicionar.setkBackGroundColor(new Color(26, 131, 43));
                    btnAdicionar.setkHoverColor(new Color(52, 153, 68));
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para excluir cadastro dos fornecedores
    private void remover() {
        conn = Conexao.getConexao();

        // Verifica se os campos obrigatórios estão vazios
        if (txtFornNome.getText().isEmpty() || txtFornEmail.getText().isEmpty() || txtFornFone.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios.");
            return; // Encerra a execução da função se algum campo estiver vazio
        }

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
                    pesquisar_fornecedor(); //atualizar a tabela
                    btnAdicionar.setEnabled(true);
                    btnAdicionar.setkBackGroundColor(new Color(26, 131, 43));
                    btnAdicionar.setkHoverColor(new Color(52, 153, 68));
                }
            } catch (HeadlessException | SQLException e) {
                // Lidar com a exceção aqui
            }
        }
    }
    
    //metodos para materiais
    //metodo para adicionar uma categoria
    private void cadastrarMaterial() {
        conn = Conexao.getConexao();
        String sql = "insert into material(id_categoria, nome_material, valor_compra, descricao) values(?, ?, ?, ?)";

        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtIdCat.getText());
            pst.setString(2, txtNomeMat.getText());
            pst.setString(3, txtValorMat.getText());
            pst.setString(4, txtDescMat.getText());

            if ((txtNomeMat.getText().isEmpty()) || (txtIdCat.getText().isEmpty()) || (txtValorMat.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha o campo com o nome do Material.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Material cadastrado com Sucesso.");

                    limpar(); //chamando a função de limpar os campos
                    pesquisar_material();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //metodo para buscar uma categoria
    private void pesquisar_material() {
        conn = Conexao.getConexao();
        //String sql = "select * from fornecedor where nome_fornecedor like ?";
        String sql = "select id_material AS ID, id_categoria AS 'ID Categoria', nome_material AS Material, valor_compra AS Valor, descricao AS Descriçao FROM material where nome_material like ?";
        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, txtBuscarMat.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblMaterial.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para setar os campos do formulário com o conteúdo da tabela fornecedor
    public void setar_camposMaterial() {
        int setar = tblMaterial.getSelectedRow();
        txtIdMat.setText(tblMaterial.getModel().getValueAt(setar, 0).toString());
        txtIdCat.setText(tblMaterial.getModel().getValueAt(setar, 1).toString());
        txtNomeMat.setText(tblMaterial.getModel().getValueAt(setar, 2).toString());
        txtValorMat.setText(tblMaterial.getModel().getValueAt(setar, 3).toString());
        txtDescMat.setText(tblMaterial.getModel().getValueAt(setar, 4).toString());

        //a linha abaixo irá desabilitar o botão de adicionar, para evitar dados duplicados.
        btnCadastrarMat.setEnabled(false);
        btnCadastrarMat.setkBackGroundColor(new Color(128, 128, 128));
        btnCadastrarMat.setkHoverColor(new Color(128, 128, 128));
    }
    
    
    
    //outros métodos
    //método para limpar os campos
    private void limpar() {
        //tela fornecedor
        txtFornId.setText(null);
        txtFornNome.setText(null);
        txtFornCnpj.setText(null);
        txtFornEmail.setText(null);
        txtFornFone.setText(null);
        txtFornEndereco.setText(null);
        txtFornSite.setText(null);
        txtFornPesquisar.setText(null);
        ((DefaultTableModel) tblFornecedores.getModel()).setRowCount(0);
        
        //tela material
        txtIdMat.setText(null);
        txtIdCat.setText(null);
        txtNomeMat.setText(null);
        txtValorMat.setText(null);
        txtDescMat.setText(null);
        ((DefaultTableModel) tblMaterial.getModel()).setRowCount(0);
    }
    
    

    // Método para estilizar um botão com um nome e uma imagem específicos
    private void estilizarBotao(KButton btn, String nome, String caminhoImagem) {
        // Define o tamanho preferido do botão
        btn.setPreferredSize(new Dimension(200, 175));

        // Adiciona um layout para o botão
        btn.setLayout(new BorderLayout());

        // Carrega a imagem
        ImageIcon icon = new ImageIcon(caminhoImagem);

        // Ajusta o tamanho da imagem
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Ajusta a largura e a altura conforme necessário
        ImageIcon newIcon = new ImageIcon(newImage);

        // Adiciona a imagem em cima do texto
        JLabel label = new JLabel("", newIcon, JLabel.CENTER);
        btn.add(label, BorderLayout.CENTER);

        // Adiciona o texto embaixo da imagem
        JLabel textLabel = new JLabel(nome, JLabel.CENTER);
        textLabel.setForeground(new Color(26, 131, 43)); // Define a cor do texto (verde)
        textLabel.setVerticalAlignment(JLabel.TOP); // Alinha o texto ao topo do JLabel
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // Adiciona uma margem inferior para separar o texto da imagem
        btn.add(textLabel, BorderLayout.SOUTH);

        // Define a cor de fundo padrão (verde)
        btn.setBackground(new Color(24, 140, 91));

        // Define a cor do texto (branco)
        btn.setForeground(Color.WHITE);

        // Remove a borda pintada
        btn.setBorderPainted(false);

        // Define a fonte para o texto (opcional)
        textLabel.setFont(new Font("Calibri", Font.BOLD, 28));

        // Define o cursor para a mãozinha quando passar por cima
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Adiciona um ouvinte de mouse ao botão
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(7, 108, 65)); // Cor verde mais escura
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(24, 140, 91)); // Cor verde padrão
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

        menuLateral = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnCadastros = new com.k33ptoo.components.KButton();
        btnMovimentacoes = new com.k33ptoo.components.KButton();
        kButton4 = new com.k33ptoo.components.KButton();
        kButton5 = new com.k33ptoo.components.KButton();
        kButton6 = new com.k33ptoo.components.KButton();
        kButton7 = new com.k33ptoo.components.KButton();
        kButton8 = new com.k33ptoo.components.KButton();
        kButton9 = new com.k33ptoo.components.KButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        telaInicial = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        telaCadastros = new javax.swing.JPanel();
        btnCadFornecedor = new com.k33ptoo.components.KButton();
        btnCadCategoria = new com.k33ptoo.components.KButton();
        btnCadMaterial = new com.k33ptoo.components.KButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btnFechar1 = new javax.swing.JButton();
        telaCadFornecedor = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnFechar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtFornNome = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtFornCnpj = new javax.swing.JTextField();
        txtFornSite = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtFornEndereco = new javax.swing.JTextField();
        txtFornEmail = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtFornFone = new javax.swing.JTextField();
        btnAdicionar = new com.k33ptoo.components.KButton();
        btnAlterar = new com.k33ptoo.components.KButton();
        btnExcluir = new com.k33ptoo.components.KButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFornecedores = new javax.swing.JTable();
        txtFornPesquisar = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtFornId = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        btnLimpar = new javax.swing.JButton();
        telaCadCategoria = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btnFechar2 = new javax.swing.JButton();
        telaCadMaterial = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btnFechar3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMaterial = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblCategoria = new javax.swing.JTable();
        txtIdMat = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtIdCat = new javax.swing.JTextField();
        txtNomeMat = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDescMat = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtValorMat = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        btnCadastrarMat = new com.k33ptoo.components.KButton();
        btnAlterarMat = new com.k33ptoo.components.KButton();
        btnExcluirMat = new com.k33ptoo.components.KButton();
        txtBuscarMat = new javax.swing.JTextField();
        txtBuscarCat = new javax.swing.JTextField();
        btnLimparMat = new javax.swing.JButton();
        telaMovimentacoes = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("StockSync");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(900, 550));
        setPreferredSize(new java.awt.Dimension(1280, 720));
        setResizable(false);
        setSize(new java.awt.Dimension(1280, 720));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuLateral.setBackground(new java.awt.Color(26, 131, 43));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/STOCKSYNCIMG.png"))); // NOI18N

        btnCadastros.setText("CADASTROS");
        btnCadastros.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnCadastros.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCadastros.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        btnCadastros.setkEndColor(new java.awt.Color(26, 131, 43));
        btnCadastros.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnCadastros.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        btnCadastros.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCadastros.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        btnCadastros.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        btnCadastros.setkPressedColor(new java.awt.Color(52, 153, 68));
        btnCadastros.setkSelectedColor(new java.awt.Color(52, 153, 68));
        btnCadastros.setkStartColor(new java.awt.Color(26, 131, 43));
        btnCadastros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrosActionPerformed(evt);
            }
        });

        btnMovimentacoes.setText("MOVIMENTAÇÕES");
        btnMovimentacoes.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnMovimentacoes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMovimentacoes.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        btnMovimentacoes.setkEndColor(new java.awt.Color(26, 131, 43));
        btnMovimentacoes.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnMovimentacoes.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        btnMovimentacoes.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnMovimentacoes.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        btnMovimentacoes.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        btnMovimentacoes.setkPressedColor(new java.awt.Color(52, 153, 68));
        btnMovimentacoes.setkSelectedColor(new java.awt.Color(52, 153, 68));
        btnMovimentacoes.setkStartColor(new java.awt.Color(26, 131, 43));
        btnMovimentacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovimentacoesActionPerformed(evt);
            }
        });

        kButton4.setText("MATERIAIS");
        kButton4.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        kButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kButton4.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        kButton4.setkEndColor(new java.awt.Color(26, 131, 43));
        kButton4.setkHoverColor(new java.awt.Color(52, 153, 68));
        kButton4.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        kButton4.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton4.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        kButton4.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        kButton4.setkPressedColor(new java.awt.Color(52, 153, 68));
        kButton4.setkSelectedColor(new java.awt.Color(52, 153, 68));
        kButton4.setkStartColor(new java.awt.Color(26, 131, 43));

        kButton5.setText("FORNECEDORES");
        kButton5.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        kButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kButton5.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        kButton5.setkEndColor(new java.awt.Color(26, 131, 43));
        kButton5.setkHoverColor(new java.awt.Color(52, 153, 68));
        kButton5.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        kButton5.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton5.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        kButton5.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        kButton5.setkPressedColor(new java.awt.Color(52, 153, 68));
        kButton5.setkSelectedColor(new java.awt.Color(52, 153, 68));
        kButton5.setkStartColor(new java.awt.Color(26, 131, 43));

        kButton6.setText("CATEGORIAS");
        kButton6.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        kButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kButton6.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        kButton6.setkEndColor(new java.awt.Color(26, 131, 43));
        kButton6.setkHoverColor(new java.awt.Color(52, 153, 68));
        kButton6.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        kButton6.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton6.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        kButton6.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        kButton6.setkPressedColor(new java.awt.Color(52, 153, 68));
        kButton6.setkSelectedColor(new java.awt.Color(52, 153, 68));
        kButton6.setkStartColor(new java.awt.Color(26, 131, 43));

        kButton7.setText("RELATÓRIOS");
        kButton7.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        kButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kButton7.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        kButton7.setkEndColor(new java.awt.Color(26, 131, 43));
        kButton7.setkHoverColor(new java.awt.Color(52, 153, 68));
        kButton7.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        kButton7.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton7.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        kButton7.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        kButton7.setkPressedColor(new java.awt.Color(52, 153, 68));
        kButton7.setkSelectedColor(new java.awt.Color(52, 153, 68));
        kButton7.setkStartColor(new java.awt.Color(26, 131, 43));

        kButton8.setText("AJUDA");
        kButton8.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        kButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kButton8.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        kButton8.setkEndColor(new java.awt.Color(26, 131, 43));
        kButton8.setkHoverColor(new java.awt.Color(52, 153, 68));
        kButton8.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        kButton8.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton8.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        kButton8.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        kButton8.setkPressedColor(new java.awt.Color(52, 153, 68));
        kButton8.setkSelectedColor(new java.awt.Color(52, 153, 68));
        kButton8.setkStartColor(new java.awt.Color(26, 131, 43));

        kButton9.setText("SOBRE");
        kButton9.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        kButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kButton9.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        kButton9.setkEndColor(new java.awt.Color(26, 131, 43));
        kButton9.setkHoverColor(new java.awt.Color(52, 153, 68));
        kButton9.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        kButton9.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        kButton9.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        kButton9.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        kButton9.setkPressedColor(new java.awt.Color(52, 153, 68));
        kButton9.setkSelectedColor(new java.awt.Color(52, 153, 68));
        kButton9.setkStartColor(new java.awt.Color(26, 131, 43));

        javax.swing.GroupLayout menuLateralLayout = new javax.swing.GroupLayout(menuLateral);
        menuLateral.setLayout(menuLateralLayout);
        menuLateralLayout.setHorizontalGroup(
            menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addGroup(menuLateralLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
            .addComponent(btnCadastros, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnMovimentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(kButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(kButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(kButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(kButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(kButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(kButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        menuLateralLayout.setVerticalGroup(
            menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCadastros, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnMovimentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(kButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(kButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(kButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(kButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(kButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(kButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(324, Short.MAX_VALUE))
        );

        getContentPane().add(menuLateral, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 700));

        telaInicial.setBackground(new java.awt.Color(217, 217, 217));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/STOCKSYNCIMG2.png"))); // NOI18N

        javax.swing.GroupLayout telaInicialLayout = new javax.swing.GroupLayout(telaInicial);
        telaInicial.setLayout(telaInicialLayout);
        telaInicialLayout.setHorizontalGroup(
            telaInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaInicialLayout.createSequentialGroup()
                .addContainerGap(348, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(357, 357, 357))
        );
        telaInicialLayout.setVerticalGroup(
            telaInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaInicialLayout.createSequentialGroup()
                .addGap(309, 309, 309)
                .addComponent(jLabel4)
                .addContainerGap(306, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab1", telaInicial);

        telaCadastros.setBackground(new java.awt.Color(217, 217, 217));

        btnCadFornecedor.setBackground(new java.awt.Color(222, 222, 222));
        btnCadFornecedor.setBorder(null);
        btnCadFornecedor.setForeground(new java.awt.Color(26, 131, 43));
        btnCadFornecedor.setToolTipText("");
        btnCadFornecedor.setAlignmentY(0.0F);
        btnCadFornecedor.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btnCadFornecedor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnCadFornecedor.setIconTextGap(65);
        btnCadFornecedor.setkBackGroundColor(new java.awt.Color(239, 239, 239));
        btnCadFornecedor.setkBorderRadius(50);
        btnCadFornecedor.setkEndColor(new java.awt.Color(239, 239, 239));
        btnCadFornecedor.setkForeGround(new java.awt.Color(26, 131, 43));
        btnCadFornecedor.setkHoverColor(new java.awt.Color(239, 239, 239));
        btnCadFornecedor.setkHoverEndColor(new java.awt.Color(245, 245, 245));
        btnCadFornecedor.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCadFornecedor.setkHoverStartColor(new java.awt.Color(245, 245, 245));
        btnCadFornecedor.setkIndicatorColor(new java.awt.Color(239, 239, 239));
        btnCadFornecedor.setkPressedColor(new java.awt.Color(175, 175, 175));
        btnCadFornecedor.setkSelectedColor(new java.awt.Color(0, 0, 0));
        btnCadFornecedor.setkStartColor(new java.awt.Color(239, 239, 239));
        btnCadFornecedor.setPreferredSize(new java.awt.Dimension(200, 175));
        btnCadFornecedor.setVerifyInputWhenFocusTarget(false);
        btnCadFornecedor.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnCadFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadFornecedorActionPerformed(evt);
            }
        });

        btnCadCategoria.setBorder(null);
        btnCadCategoria.setToolTipText("");
        btnCadCategoria.setAlignmentY(0.0F);
        btnCadCategoria.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnCadCategoria.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCadCategoria.setIconTextGap(70);
        btnCadCategoria.setkBackGroundColor(new java.awt.Color(239, 239, 239));
        btnCadCategoria.setkBorderRadius(50);
        btnCadCategoria.setkEndColor(new java.awt.Color(239, 239, 239));
        btnCadCategoria.setkHoverColor(new java.awt.Color(239, 239, 239));
        btnCadCategoria.setkHoverEndColor(new java.awt.Color(245, 245, 245));
        btnCadCategoria.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCadCategoria.setkHoverStartColor(new java.awt.Color(245, 245, 245));
        btnCadCategoria.setkIndicatorColor(new java.awt.Color(239, 239, 239));
        btnCadCategoria.setkPressedColor(new java.awt.Color(175, 175, 175));
        btnCadCategoria.setkSelectedColor(new java.awt.Color(0, 0, 0));
        btnCadCategoria.setkStartColor(new java.awt.Color(239, 239, 239));
        btnCadCategoria.setPreferredSize(new java.awt.Dimension(200, 175));
        btnCadCategoria.setVerifyInputWhenFocusTarget(false);
        btnCadCategoria.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnCadCategoria.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCadCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadCategoriaActionPerformed(evt);
            }
        });

        btnCadMaterial.setBorder(null);
        btnCadMaterial.setToolTipText("");
        btnCadMaterial.setAlignmentY(0.0F);
        btnCadMaterial.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnCadMaterial.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCadMaterial.setIconTextGap(70);
        btnCadMaterial.setkBackGroundColor(new java.awt.Color(239, 239, 239));
        btnCadMaterial.setkBorderRadius(50);
        btnCadMaterial.setkEndColor(new java.awt.Color(239, 239, 239));
        btnCadMaterial.setkHoverColor(new java.awt.Color(239, 239, 239));
        btnCadMaterial.setkHoverEndColor(new java.awt.Color(245, 245, 245));
        btnCadMaterial.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCadMaterial.setkHoverStartColor(new java.awt.Color(245, 245, 245));
        btnCadMaterial.setkIndicatorColor(new java.awt.Color(239, 239, 239));
        btnCadMaterial.setkPressedColor(new java.awt.Color(175, 175, 175));
        btnCadMaterial.setkSelectedColor(new java.awt.Color(0, 0, 0));
        btnCadMaterial.setkStartColor(new java.awt.Color(239, 239, 239));
        btnCadMaterial.setPreferredSize(new java.awt.Dimension(200, 175));
        btnCadMaterial.setVerifyInputWhenFocusTarget(false);
        btnCadMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadMaterialActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(26, 131, 43));
        jLabel5.setText("> MENU DE CADASTRO");

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(26, 131, 43));
        jLabel6.setText("NOVO CADASTRO");

        jSeparator2.setBackground(new java.awt.Color(26, 131, 43));
        jSeparator2.setForeground(new java.awt.Color(26, 131, 43));

        btnFechar1.setBackground(new java.awt.Color(217, 217, 217));
        btnFechar1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnFechar1.setForeground(new java.awt.Color(26, 131, 43));
        btnFechar1.setText("X");
        btnFechar1.setBorder(null);
        btnFechar1.setBorderPainted(false);
        btnFechar1.setContentAreaFilled(false);
        btnFechar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFechar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaCadastrosLayout = new javax.swing.GroupLayout(telaCadastros);
        telaCadastros.setLayout(telaCadastrosLayout);
        telaCadastrosLayout.setHorizontalGroup(
            telaCadastrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadastrosLayout.createSequentialGroup()
                .addGroup(telaCadastrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadastrosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5))
                    .addGroup(telaCadastrosLayout.createSequentialGroup()
                        .addGap(417, 417, 417)
                        .addComponent(jLabel6))
                    .addGroup(telaCadastrosLayout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(telaCadastrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 930, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(telaCadastrosLayout.createSequentialGroup()
                                .addComponent(btnCadFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(90, 90, 90)
                                .addComponent(btnCadCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(90, 90, 90)
                                .addComponent(btnCadMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(90, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadastrosLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnFechar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        telaCadastrosLayout.setVerticalGroup(
            telaCadastrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadastrosLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnFechar1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(60, 60, 60)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addGroup(telaCadastrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCadMaterial, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(btnCadCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCadFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(236, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab2", telaCadastros);

        telaCadFornecedor.setBackground(new java.awt.Color(217, 217, 217));

        jLabel7.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(26, 131, 43));
        jLabel7.setText("> NOVO FORNECEDOR");

        btnFechar.setBackground(new java.awt.Color(217, 217, 217));
        btnFechar.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnFechar.setForeground(new java.awt.Color(26, 131, 43));
        btnFechar.setText("X");
        btnFechar.setBorder(null);
        btnFechar.setBorderPainted(false);
        btnFechar.setContentAreaFilled(false);
        btnFechar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(26, 131, 43));
        jLabel2.setText("Nome do Fornecedor");

        txtFornNome.setBackground(new java.awt.Color(223, 223, 223));
        txtFornNome.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtFornNome.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtFornNome.setSelectionColor(new java.awt.Color(26, 131, 43));

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(26, 131, 43));
        jLabel10.setText("CNPJ");

        txtFornCnpj.setBackground(new java.awt.Color(223, 223, 223));
        txtFornCnpj.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtFornCnpj.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtFornCnpj.setSelectionColor(new java.awt.Color(26, 131, 43));

        txtFornSite.setBackground(new java.awt.Color(223, 223, 223));
        txtFornSite.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtFornSite.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtFornSite.setSelectionColor(new java.awt.Color(26, 131, 43));

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(26, 131, 43));
        jLabel11.setText("Site");

        jLabel12.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(26, 131, 43));
        jLabel12.setText("Endereço");

        txtFornEndereco.setBackground(new java.awt.Color(223, 223, 223));
        txtFornEndereco.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtFornEndereco.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtFornEndereco.setSelectionColor(new java.awt.Color(26, 131, 43));

        txtFornEmail.setBackground(new java.awt.Color(223, 223, 223));
        txtFornEmail.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtFornEmail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtFornEmail.setSelectionColor(new java.awt.Color(26, 131, 43));

        jLabel13.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(26, 131, 43));
        jLabel13.setText("E-mail");

        jLabel14.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(26, 131, 43));
        jLabel14.setText("Telefone");

        txtFornFone.setBackground(new java.awt.Color(223, 223, 223));
        txtFornFone.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtFornFone.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtFornFone.setSelectionColor(new java.awt.Color(26, 131, 43));

        btnAdicionar.setText("Cadastrar");
        btnAdicionar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAdicionar.setkAllowGradient(false);
        btnAdicionar.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnAdicionar.setkBorderRadius(20);
        btnAdicionar.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnAdicionar.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnAlterar.setText("Alterar");
        btnAlterar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAlterar.setkAllowGradient(false);
        btnAlterar.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnAlterar.setkBorderRadius(20);
        btnAlterar.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnAlterar.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnExcluir.setText("Excluir");
        btnExcluir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnExcluir.setkAllowGradient(false);
        btnExcluir.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnExcluir.setkBorderRadius(20);
        btnExcluir.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnExcluir.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        tblFornecedores = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblFornecedores.setModel(new javax.swing.table.DefaultTableModel(
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
        tblFornecedores.getTableHeader().setReorderingAllowed(false);
        tblFornecedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFornecedoresMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFornecedores);

        txtFornPesquisar.setBackground(new java.awt.Color(223, 223, 223));
        txtFornPesquisar.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtFornPesquisar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtFornPesquisar.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtFornPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFornPesquisarKeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(26, 131, 43));
        jLabel15.setText("Buscar");

        txtFornId.setBackground(new java.awt.Color(223, 223, 223));
        txtFornId.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtFornId.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtFornId.setEnabled(false);
        txtFornId.setSelectionColor(new java.awt.Color(26, 131, 43));

        jLabel16.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(26, 131, 43));
        jLabel16.setText("ID Fornecedor");

        btnLimpar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimpar.setForeground(new java.awt.Color(26, 131, 43));
        btnLimpar.setText("Limpar");
        btnLimpar.setContentAreaFilled(false);
        btnLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaCadFornecedorLayout = new javax.swing.GroupLayout(telaCadFornecedor);
        telaCadFornecedor.setLayout(telaCadFornecedorLayout);
        telaCadFornecedorLayout.setHorizontalGroup(
            telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadFornecedorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, telaCadFornecedorLayout.createSequentialGroup()
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtFornNome, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(txtFornCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(86, 86, 86))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, telaCadFornecedorLayout.createSequentialGroup()
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(txtFornSite, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(txtFornEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFornId, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(txtFornEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFornFone, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addGap(127, 127, 127))))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addComponent(txtFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLimpar))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addContainerGap(42, Short.MAX_VALUE))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(124, 124, 124)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(150, 150, 150))))
        );
        telaCadFornecedorLayout.setVerticalGroup(
            telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(35, 35, 35)
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12)
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(0, 0, 0)
                                .addComponent(txtFornCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)))
                        .addComponent(txtFornEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, 0)
                        .addComponent(txtFornNome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel11)
                        .addGap(0, 0, 0)
                        .addComponent(txtFornSite, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(0, 0, 0)
                        .addComponent(txtFornId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addGap(0, 4, Short.MAX_VALUE)
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14))
                                .addGap(0, 0, 0)
                                .addComponent(txtFornEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtFornFone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTabbedPane2.addTab("tab2", telaCadFornecedor);

        telaCadCategoria.setBackground(new java.awt.Color(217, 217, 217));

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(26, 131, 43));
        jLabel8.setText("> NOVA CATEGORIA");

        btnFechar2.setBackground(new java.awt.Color(217, 217, 217));
        btnFechar2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnFechar2.setForeground(new java.awt.Color(26, 131, 43));
        btnFechar2.setText("X");
        btnFechar2.setBorder(null);
        btnFechar2.setBorderPainted(false);
        btnFechar2.setContentAreaFilled(false);
        btnFechar2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFechar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechar2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaCadCategoriaLayout = new javax.swing.GroupLayout(telaCadCategoria);
        telaCadCategoria.setLayout(telaCadCategoriaLayout);
        telaCadCategoriaLayout.setHorizontalGroup(
            telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadCategoriaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFechar2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addContainerGap(918, Short.MAX_VALUE))
        );
        telaCadCategoriaLayout.setVerticalGroup(
            telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnFechar2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(636, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab2", telaCadCategoria);

        telaCadMaterial.setBackground(new java.awt.Color(217, 217, 217));

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(26, 131, 43));
        jLabel9.setText("> NOVO MATERIAL");

        btnFechar3.setBackground(new java.awt.Color(217, 217, 217));
        btnFechar3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnFechar3.setForeground(new java.awt.Color(26, 131, 43));
        btnFechar3.setText("X");
        btnFechar3.setBorder(null);
        btnFechar3.setBorderPainted(false);
        btnFechar3.setContentAreaFilled(false);
        btnFechar3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFechar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechar3ActionPerformed(evt);
            }
        });

        tblMaterial = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblMaterial.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMaterial.getTableHeader().setReorderingAllowed(false);
        tblMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMaterialMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblMaterial);

        tblCategoria = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblCategoria.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCategoria.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblCategoria);

        txtIdMat.setBackground(new java.awt.Color(223, 223, 223));
        txtIdMat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtIdMat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtIdMat.setEnabled(false);
        txtIdMat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtIdMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdMatActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(26, 131, 43));
        jLabel17.setText("ID Material");

        jLabel18.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(26, 131, 43));
        jLabel18.setText("ID Categoria");

        txtIdCat.setBackground(new java.awt.Color(223, 223, 223));
        txtIdCat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtIdCat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtIdCat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtIdCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdCatActionPerformed(evt);
            }
        });

        txtNomeMat.setBackground(new java.awt.Color(223, 223, 223));
        txtNomeMat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtNomeMat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtNomeMat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtNomeMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeMatActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(26, 131, 43));
        jLabel19.setText("Nome do Material");

        txtDescMat.setBackground(new java.awt.Color(223, 223, 223));
        txtDescMat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtDescMat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtDescMat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtDescMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescMatActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(26, 131, 43));
        jLabel20.setText("Descrição");

        txtValorMat.setBackground(new java.awt.Color(223, 223, 223));
        txtValorMat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtValorMat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtValorMat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtValorMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorMatActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(26, 131, 43));
        jLabel21.setText("Valor");

        btnCadastrarMat.setText("Cadastrar");
        btnCadastrarMat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCadastrarMat.setkAllowGradient(false);
        btnCadastrarMat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnCadastrarMat.setkBorderRadius(20);
        btnCadastrarMat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnCadastrarMat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCadastrarMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarMatActionPerformed(evt);
            }
        });

        btnAlterarMat.setText("Alterar");
        btnAlterarMat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAlterarMat.setkAllowGradient(false);
        btnAlterarMat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnAlterarMat.setkBorderRadius(20);
        btnAlterarMat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnAlterarMat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnAlterarMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarMatActionPerformed(evt);
            }
        });

        btnExcluirMat.setText("Excluir");
        btnExcluirMat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnExcluirMat.setkAllowGradient(false);
        btnExcluirMat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnExcluirMat.setkBorderRadius(20);
        btnExcluirMat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnExcluirMat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnExcluirMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirMatActionPerformed(evt);
            }
        });

        txtBuscarMat.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarMat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarMat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarMat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarMatActionPerformed(evt);
            }
        });
        txtBuscarMat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarMatKeyReleased(evt);
            }
        });

        txtBuscarCat.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarCat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarCat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarCat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarCatActionPerformed(evt);
            }
        });

        btnLimparMat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimparMat.setForeground(new java.awt.Color(26, 131, 43));
        btnLimparMat.setText("Limpar");
        btnLimparMat.setContentAreaFilled(false);
        btnLimparMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparMatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaCadMaterialLayout = new javax.swing.GroupLayout(telaCadMaterial);
        telaCadMaterial.setLayout(telaCadMaterialLayout);
        telaCadMaterialLayout.setHorizontalGroup(
            telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadMaterialLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFechar3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(telaCadMaterialLayout.createSequentialGroup()
                .addGap(185, 185, 185)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(txtDescMat, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(txtValorMat, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(201, 201, 201))
            .addGroup(telaCadMaterialLayout.createSequentialGroup()
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9))
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadMaterialLayout.createSequentialGroup()
                                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtIdMat, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))
                                .addGap(101, 101, 101)
                                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtIdCat, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addGap(106, 106, 106)
                                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(txtNomeMat, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(96, 96, 96))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadMaterialLayout.createSequentialGroup()
                                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                                        .addComponent(txtBuscarMat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnLimparMat)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtBuscarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(btnCadastrarMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(118, 118, 118)
                        .addComponent(btnAlterarMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(119, 119, 119)
                        .addComponent(btnExcluirMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        telaCadMaterialLayout.setVerticalGroup(
            telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadMaterialLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnFechar3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(43, 43, 43)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, telaCadMaterialLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, 0)
                        .addComponent(txtIdMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(0, 0, 0)
                        .addComponent(txtIdCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(0, 0, 0)
                        .addComponent(txtNomeMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(0, 0, 0)
                        .addComponent(txtDescMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadMaterialLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(0, 0, 0)
                        .addComponent(txtValorMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(47, 47, 47)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCadastrarMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlterarMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcluirMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimparMat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab2", telaCadMaterial);

        jLabel3.setText("jLabel3");

        javax.swing.GroupLayout telaMovimentacoesLayout = new javax.swing.GroupLayout(telaMovimentacoes);
        telaMovimentacoes.setLayout(telaMovimentacoesLayout);
        telaMovimentacoesLayout.setHorizontalGroup(
            telaMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaMovimentacoesLayout.createSequentialGroup()
                .addContainerGap(657, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(414, 414, 414))
        );
        telaMovimentacoesLayout.setVerticalGroup(
            telaMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaMovimentacoesLayout.createSequentialGroup()
                .addGap(210, 210, 210)
                .addComponent(jLabel3)
                .addContainerGap(489, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab3", telaMovimentacoes);

        getContentPane().add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, -50, 1110, 750));

        setSize(new java.awt.Dimension(1280, 720));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrosActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadastros);
    }//GEN-LAST:event_btnCadastrosActionPerformed

    private void btnMovimentacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovimentacoesActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaMovimentacoes);
    }//GEN-LAST:event_btnMovimentacoesActionPerformed

    private void btnCadCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadCategoriaActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadCategoria);
    }//GEN-LAST:event_btnCadCategoriaActionPerformed

    private void btnCadFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadFornecedorActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadFornecedor);
        pesquisar_fornecedor();
    }//GEN-LAST:event_btnCadFornecedorActionPerformed

    private void btnCadMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadMaterialActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadMaterial);
        pesquisar_material();
    }//GEN-LAST:event_btnCadMaterialActionPerformed

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadastros);
        limpar();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnFechar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnFechar1ActionPerformed

    private void btnFechar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadastros);
    }//GEN-LAST:event_btnFechar2ActionPerformed

    private void btnFechar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar3ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadastros);
        limpar();
    }//GEN-LAST:event_btnFechar3ActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        //Chamando o método para alterar dados do Fornecedor.
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // TODO add your handling code here:
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void txtFornPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFornPesquisarKeyReleased
        // TODO add your handling code here:
        pesquisar_fornecedor();
    }//GEN-LAST:event_txtFornPesquisarKeyReleased

    private void tblFornecedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFornecedoresMouseClicked
        //EVENTO QUE SERÁ USADO PARA SETAR OS CAMPOS DA TABELA(CLICANDO COM O BOTÃO DO MOUSE)
        //e chamando o método para setar os campos.
        setar_campos();
    }//GEN-LAST:event_tblFornecedoresMouseClicked

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        remover();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // TODO add your handling code here:
        limpar();
        btnAdicionar.setEnabled(true);
        btnAdicionar.setkBackGroundColor(new Color(26, 131, 43));
        btnAdicionar.setkHoverColor(new Color(52, 153, 68));
        pesquisar_fornecedor();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void txtIdMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdMatActionPerformed

    private void txtIdCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdCatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdCatActionPerformed

    private void txtNomeMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeMatActionPerformed

    private void txtDescMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescMatActionPerformed

    private void txtValorMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorMatActionPerformed

    private void btnCadastrarMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarMatActionPerformed
        // TODO add your handling code here:
        cadastrarMaterial();
    }//GEN-LAST:event_btnCadastrarMatActionPerformed

    private void btnAlterarMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarMatActionPerformed

    private void btnExcluirMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExcluirMatActionPerformed

    private void txtBuscarMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarMatActionPerformed

    private void txtBuscarCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarCatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarCatActionPerformed

    private void btnLimparMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparMatActionPerformed
        // TODO add your handling code here:
        limpar();
        btnCadastrarMat.setEnabled(true);
        btnCadastrarMat.setkBackGroundColor(new Color(26, 131, 43));
        btnCadastrarMat.setkHoverColor(new Color(52, 153, 68));
        pesquisar_material();
    }//GEN-LAST:event_btnLimparMatActionPerformed

    private void txtBuscarMatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarMatKeyReleased
        // TODO add your handling code here:
        pesquisar_material();
    }//GEN-LAST:event_txtBuscarMatKeyReleased

    private void tblMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMaterialMouseClicked
        // TODO add your handling code here:
        setar_camposMaterial();
    }//GEN-LAST:event_tblMaterialMouseClicked

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
            java.util.logging.Logger.getLogger(testeMenuNovo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(testeMenuNovo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(testeMenuNovo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(testeMenuNovo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new testeMenuNovo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.k33ptoo.components.KButton btnAdicionar;
    private com.k33ptoo.components.KButton btnAlterar;
    private com.k33ptoo.components.KButton btnAlterarMat;
    private com.k33ptoo.components.KButton btnCadCategoria;
    private com.k33ptoo.components.KButton btnCadFornecedor;
    private com.k33ptoo.components.KButton btnCadMaterial;
    private com.k33ptoo.components.KButton btnCadastrarMat;
    private com.k33ptoo.components.KButton btnCadastros;
    private com.k33ptoo.components.KButton btnExcluir;
    private com.k33ptoo.components.KButton btnExcluirMat;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnFechar1;
    private javax.swing.JButton btnFechar2;
    private javax.swing.JButton btnFechar3;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnLimparMat;
    private com.k33ptoo.components.KButton btnMovimentacoes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private com.k33ptoo.components.KButton kButton4;
    private com.k33ptoo.components.KButton kButton5;
    private com.k33ptoo.components.KButton kButton6;
    private com.k33ptoo.components.KButton kButton7;
    private com.k33ptoo.components.KButton kButton8;
    private com.k33ptoo.components.KButton kButton9;
    private javax.swing.JPanel menuLateral;
    private javax.swing.JTable tblCategoria;
    private javax.swing.JTable tblFornecedores;
    private javax.swing.JTable tblMaterial;
    private javax.swing.JPanel telaCadCategoria;
    private javax.swing.JPanel telaCadFornecedor;
    private javax.swing.JPanel telaCadMaterial;
    private javax.swing.JPanel telaCadastros;
    private javax.swing.JPanel telaInicial;
    private javax.swing.JPanel telaMovimentacoes;
    private javax.swing.JTextField txtBuscarCat;
    private javax.swing.JTextField txtBuscarMat;
    private javax.swing.JTextField txtDescMat;
    private javax.swing.JTextField txtFornCnpj;
    private javax.swing.JTextField txtFornEmail;
    private javax.swing.JTextField txtFornEndereco;
    private javax.swing.JTextField txtFornFone;
    private javax.swing.JTextField txtFornId;
    private javax.swing.JTextField txtFornNome;
    private javax.swing.JTextField txtFornPesquisar;
    private javax.swing.JTextField txtFornSite;
    private javax.swing.JTextField txtIdCat;
    private javax.swing.JTextField txtIdMat;
    private javax.swing.JTextField txtNomeMat;
    private javax.swing.JTextField txtValorMat;
    // End of variables declaration//GEN-END:variables
}
