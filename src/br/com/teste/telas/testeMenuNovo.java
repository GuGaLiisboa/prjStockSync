/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.teste.telas;

import java.sql.*;
import br.com.teste.dal.Conexao;
import br.com.teste.uteis.PopupCategoria;
import br.com.teste.uteis.PopupTabelasVM;
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
        //estilizarBotao(btnCadFornecedor, "FORNECEDOR", "..\\prjStockSync\\src\\br\\com\\teste\\icones\\iconeCaminhao.png");
        //estilizarBotao(btnCadCategoria, "CATEGORIA", "..\\prjStockSync\\src\\br\\com\\teste\\icones\\iconeCategoria.png");
        //estilizarBotao(btnCadMaterial, "MATERIAL", "..\\prjStockSync\\src\\br\\com\\teste\\icones\\iconeMaterial.png");
        //estilizarBotao(btnMovEntrada, "ENTRADA", "..\\prjStockSync\\src\\br\\com\\teste\\icones\\iconeEntrada.png");
        //estilizarBotao(btnMovSaida, "SAÍDA", "..\\prjStockSync\\src\\br\\com\\teste\\icones\\iconeSaida.png");
        btnCadFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/iconeCaminhao.png")));
        btnCadCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/iconeCategoria.png")));
        btnCadMaterial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/iconeMaterial.png")));
        btnMovEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/iconeEntrada.png")));
        btnMovSaida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/iconeSaida.png")));

        //atualizar as tabelas
        atualizarTabelas();

        //outros etilos
        definirIconeJanela();
        btnEdit();

    }

    //=============================================================================================
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
                    atualizarTabelas();
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
            pst.setString(1, "%" + txtFornPesquisar.getText() + "%");
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
                    atualizarTabelas();
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
                    atualizarTabelas();
                    btnAdicionar.setEnabled(true);
                    btnAdicionar.setkBackGroundColor(new Color(26, 131, 43));
                    btnAdicionar.setkHoverColor(new Color(52, 153, 68));
                }
            } catch (HeadlessException | SQLException e) {
                // Lidar com a exceção aqui
            }
        }
    }

    //=============================================================================================
    //metodos para materiais
    //metodo para adicionar um material
    private void cadastrarMaterial() {
        conn = Conexao.getConexao();
        String sql = "INSERT INTO material(id_categoria, nome_material, valor_compra, descricao) VALUES(?, ?, ?, ?)";

        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtIdCat.getText());
            pst.setString(2, txtNomeMat.getText());
            pst.setString(3, txtValorMat.getText());
            pst.setString(4, txtDescMat.getText());

            if (txtIdCat.getText().isEmpty() || txtNomeMat.getText().isEmpty() || txtValorMat.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Material cadastrado com sucesso.");

                    limpar(); // chamando a função de limpar os campos
                    //atualizar as tabelas
                    atualizarTabelas();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para buscar um material
    private void pesquisar_material() {
        conn = Conexao.getConexao();
        //String sql = "select * from fornecedor where nome_fornecedor like ?";
        String sql = "select id_material AS ID, id_categoria AS 'ID Categoria', nome_material AS Material, valor_compra AS Valor, descricao AS Descriçao FROM material where nome_material like ?";
        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, "%" + txtBuscarMat.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblMaterial.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para buscar uma categoria na tela de cadastro de material
    private void pesquisar_categoriaEmMat() {
        conn = Conexao.getConexao();
        String sql = "select id_categoria AS 'ID Categoria', nome_categoria AS 'Nome da Categoria' FROM categoria where nome_categoria like ?";
        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, "%" + txtMatBuscarCat.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblCategoria.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para setar os campos do formulário com o conteúdo da tabela material
    public void setar_camposMaterial() {
        int setar = tblMaterial.getSelectedRow();
        txtIdMat.setText(tblMaterial.getModel().getValueAt(setar, 0).toString());
        txtIdCat.setText(tblMaterial.getModel().getValueAt(setar, 1).toString());
        txtNomeMat.setText(tblMaterial.getModel().getValueAt(setar, 2).toString());
        txtValorMat.setText(tblMaterial.getModel().getValueAt(setar, 3).toString());
        txtDescMat.setText(tblMaterial.getModel().getValueAt(setar, 4).toString());

        // Desabilitar o botão de adicionar para evitar dados duplicados
        btnCadastrarMat.setEnabled(false);
        btnCadastrarMat.setkBackGroundColor(new Color(128, 128, 128));
        btnCadastrarMat.setkHoverColor(new Color(128, 128, 128));
    }

    //metodo para alterar dados dos materiais
    private void alterar_material() {
        conn = Conexao.getConexao();
        String sql = "UPDATE material SET id_categoria=?, nome_material=?, valor_compra=?, descricao=? WHERE id_material=?";

        try {
            pst = conn.prepareStatement(sql);
            // Verificar se o campo id_categoria está vazio
            if (txtIdCat.getText().isEmpty()) {
                pst.setNull(1, java.sql.Types.INTEGER);
            } else {
                pst.setInt(1, Integer.parseInt(txtIdCat.getText()));
            }
            pst.setString(2, txtNomeMat.getText());
            pst.setString(3, txtValorMat.getText());
            pst.setString(4, txtDescMat.getText());
            pst.setString(5, txtIdMat.getText());

            if (txtNomeMat.getText().isEmpty() || txtValorMat.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cadastro do Material Alterado com Sucesso.");

                    limpar(); //chamando a função de limpar os campos
                    //atualizar as tabelas
                    atualizarTabelas();
                    btnCadastrarMat.setEnabled(true);
                    btnCadastrarMat.setkBackGroundColor(new Color(26, 131, 43));
                    btnCadastrarMat.setkHoverColor(new Color(52, 153, 68));
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para excluir cadastro dos materiais
    private void remover_material() {
        conn = Conexao.getConexao();

        // Verifica se os campos obrigatórios estão vazios
        if (txtNomeMat.getText().isEmpty() || txtValorMat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios.");
            return; // Encerra a execução da função se algum campo estiver vazio
        }

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este Material?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "CALL delete_material(?)";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtIdMat.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Material Removido com Sucesso!");
                    limpar(); //chamando a função de limpar os campos
                    //atualizar as tabelas
                    atualizarTabelas();
                    btnCadastrarMat.setEnabled(true);
                    btnCadastrarMat.setkBackGroundColor(new Color(26, 131, 43));
                    btnCadastrarMat.setkHoverColor(new Color(52, 153, 68));
                }
            } catch (HeadlessException | SQLException e) {
                // Lidar com a exceção aqui
            }
        }
    }

    //=============================================================================================
    //metodos para Categorias | Vincular Materiais
    //metodo para adicionar uma Categoria
    private void cadastrarCategoria() {
        conn = Conexao.getConexao();
        String sql = "INSERT INTO categoria(nome_categoria) VALUES(?)";

        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtNomeCat.getText());

            if ((txtNomeCat.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha o Campos Nome.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Categoria Criada com Sucesso.");

                    limpar(); //chamando a função de limpar os campos
                    atualizarTabelas();
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para alterar dados dos fornecedores
    private void alterarCategoria() {
        conn = Conexao.getConexao();
        String sql = "UPDATE categoria SET nome_categoria=? WHERE id_categoria=?";

        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtNomeCat.getText());
            pst.setString(2, txtIdCatEmCat.getText());

            if ((txtNomeCat.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha o Campo com o Nome da Categoria.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Categoria Alterada com Sucesso.");

                    limpar(); //chamando a função de limpar os campos
                    atualizarTabelas(); //atualizar a tabela
                    btnCadastrarCat.setEnabled(true);
                    btnCadastrarCat.setkBackGroundColor(new Color(26, 131, 43));
                    btnCadastrarCat.setkHoverColor(new Color(52, 153, 68));
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para excluir cadastro de categoria
    private void remover_categoria() {
        conn = Conexao.getConexao();

        // Verifica se os campos obrigatórios estão vazios
        if (txtNomeCat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione a categoria na Tabela.");
            return; // Encerra a execução da função se algum campo estiver vazio
        }

        // Verificar se a categoria existe
        if (!categoriaExiste(txtIdCatEmCat.getText())) {
            JOptionPane.showMessageDialog(null, "A categoria não existe!");
            return;
        }

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esta Categoria?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "CALL delete_categoria(?)";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtIdCatEmCat.getText());
                pst.execute();
                JOptionPane.showMessageDialog(null, "Categoria Removida com Sucesso!");
                limpar(); // Chamando a função de limpar os campos
                // Atualizar as tabelas
                atualizarTabelas();
                btnCadastrarCat.setEnabled(true);
                btnCadastrarCat.setkBackGroundColor(new Color(26, 131, 43));
                btnCadastrarCat.setkHoverColor(new Color(52, 153, 68));
            } catch (SQLException e) {
                // Verificar a mensagem de erro específica e exibir uma mensagem apropriada
                if (e.getMessage().contains("Existem materiais vinculados a esta categoria. Exclusão não permitida.")) {
                    JOptionPane.showMessageDialog(null, "Existem materiais vinculados a esta categoria. Exclusão não permitida.");
                    substituir_categoria(); // Chamar a função de substituição de categoria
                } else {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao excluir a categoria: " + e.getMessage());
                }
            }
        }
    }

    // Função para verificar se a categoria existe durante o processo de remover
    private boolean categoriaExiste(String categoriaId) {
        String sql = "SELECT COUNT(*) FROM categoria WHERE id_categoria = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, categoriaId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao verificar a existência da categoria: " + e.getMessage());
        }
        return false;
    }

    // Função para substituir a categoria após tentar excluir
    private void substituir_categoria() {
        String novaCategoriaId = JOptionPane.showInputDialog(null, "Digite o ID da nova categoria para substituir:");
        if (novaCategoriaId == null || novaCategoriaId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Substituição cancelada.");
            return;
        }

        if (!categoriaExiste(novaCategoriaId)) {
            JOptionPane.showMessageDialog(null, "A nova categoria não existe!");
            return;
        }

        String sql = "UPDATE material SET id_categoria = ? WHERE id_categoria = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, novaCategoriaId);
            pst.setString(2, txtIdCatEmCat.getText());
            int atualizados = pst.executeUpdate();
            if (atualizados > 0) {
                JOptionPane.showMessageDialog(null, "Categoria substituída com sucesso!");
                limpar(); // Chamando a função de limpar os campos
                // Atualizar as tabelas
                atualizarTabelas();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao substituir a categoria: " + e.getMessage());
        }
    }

    // função para vincular o fornecedor a um material
    private void vincularFornecedorMaterial() {
        conn = Conexao.getConexao();
        String sql = "INSERT INTO fornecedor_material(id_fornecedor, id_material) VALUES(?, ?)";

        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtIdFornVM.getText());
            pst.setString(2, txtIdMatVM.getText());

            if ((txtIdFornVM.getText().isEmpty()) || (txtIdMatVM.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os Campos com os Ids.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Fornecedor e Material vinculados com Sucesso.");

                    limpar(); //chamando a função de limpar os campos
                    atualizarTabelas();
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para desvincular o fornecedor de um material
    private void desvincularFornecedorMaterial() {
        conn = Conexao.getConexao();

        // Verifica se os campos obrigatórios estão vazios
        if ((txtIdFornVM.getText().isEmpty()) || (txtIdMatVM.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha os Campos com os Ids.");
            return; // Encerra a execução da função se algum campo estiver vazio
        }

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja desvincular?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM fornecedor_material WHERE id_fornecedor_material=?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, txtIdVincVM.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Desvinculado com Sucesso!");
                    limpar(); //chamando a função de limpar os campos
                    atualizarTabelas();
                    btnVincularFM.setEnabled(true);
                    btnVincularFM.setkBackGroundColor(new Color(26, 131, 43));
                    btnVincularFM.setkHoverColor(new Color(52, 153, 68));
                }
            } catch (HeadlessException | SQLException e) {
                // Lidar com a exceção aqui
            }
        }
    }

    //metodo para setar os campos do formulário com o conteúdo da tabela categoria
    public void setar_camposFornecedorMaterial() {
        int setar = tblVincularMaterial.getSelectedRow();
        txtIdVincVM.setText(tblVincularMaterial.getModel().getValueAt(setar, 0).toString());
        txtIdFornVM.setText(tblVincularMaterial.getModel().getValueAt(setar, 1).toString());
        txtIdMatVM.setText(tblVincularMaterial.getModel().getValueAt(setar, 3).toString());

        //a linha abaixo irá desabilitar o botão de adicionar, para evitar dados duplicados.
        btnVincularFM.setEnabled(false);
        btnVincularFM.setkBackGroundColor(new Color(128, 128, 128));
        btnVincularFM.setkHoverColor(new Color(128, 128, 128));
    }

    //metodo para setar os campos do formulário com o conteúdo da tabela categoria
    public void setar_camposCategoria() {
        int setar = tblCategoria2.getSelectedRow();
        txtIdCatEmCat.setText(tblCategoria2.getModel().getValueAt(setar, 0).toString());
        txtNomeCat.setText(tblCategoria2.getModel().getValueAt(setar, 1).toString());

        //a linha abaixo irá desabilitar o botão de adicionar, para evitar dados duplicados.
        btnCadastrarCat.setEnabled(false);
        btnCadastrarCat.setkBackGroundColor(new Color(128, 128, 128));
        btnCadastrarCat.setkHoverColor(new Color(128, 128, 128));
    }

    //metodo para buscar uma categoria no menu Categorias
    private void pesquisar_categoriaEmCat() {
        conn = Conexao.getConexao();
        String sql = "select id_categoria AS 'ID Categoria', nome_categoria AS 'Nome da Categoria' FROM categoria where nome_categoria like ?";
        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, "%" + txtBuscarCatEmCat.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblCategoria2.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para buscar uma categoria no menu Categorias
    private void pesquisar_FornecedorMaterial() {
        conn = Conexao.getConexao();
        String sql = "SELECT id_fornecedor_material AS ID, f.id_fornecedor AS 'ID Fornecedor', f.nome_fornecedor AS Nome, m.id_material AS 'ID Material', m.nome_material AS Nome "
                + "FROM fornecedor_material AS fm INNER JOIN material AS m ON m.id_material = fm.id_material INNER JOIN fornecedor AS f ON f.id_fornecedor = fm.id_fornecedor WHERE f.nome_fornecedor LIKE ?";
        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, "%" + txtBuscarVM.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblVincularMaterial.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //=============================================================================================
    //Metodos para Movimentações
    //Método para filtrar a busca(todas, entradas ou saídas)
    private void atualizarTabelaMovimentacoes(String filtro) {
        conn = Conexao.getConexao();
        String sql = "SELECT id_movimentacao AS 'ID Movimentação', m.nome_material AS 'Nome Material', m.id_material AS 'ID Material', tipo_movimentacao AS Tipo, quantidade AS Quantidade, DATE_FORMAT(data_movimentacao, '%d/%m/%Y %H:%i:%s') AS 'Data e Hora' "
                + "FROM movimentacao_estoque AS me INNER JOIN material AS m ON me.id_material = m.id_material ORDER BY id_movimentacao DESC";

        // Adiciona condição ao SQL com base no filtro
        if ("Entradas".equals(filtro)) {
            sql += "WHERE tipo_movimentacao = 'Entrada'";
        } else if ("Saídas".equals(filtro)) {
            sql += "WHERE tipo_movimentacao = 'Saida'";
        }

        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // Atualiza a tabela com o resultado da consulta
            tblMovimentacoes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para buscar por movimentações pelo campo de texto
    private void pesquisar_Movimentacao(String termoPesquisa, String filtro) {
        conn = Conexao.getConexao();
        String sql = "SELECT id_movimentacao AS 'ID Movimentação', m.nome_material AS 'Nome Material', m.id_material AS 'ID Material', tipo_movimentacao AS Tipo, quantidade AS Quantidade, DATE_FORMAT(data_movimentacao, '%d/%m/%Y %H:%i:%s') AS 'Data e Hora' "
                + "FROM movimentacao_estoque AS me INNER JOIN material AS m ON me.id_material = m.id_material ";

        // Adiciona a condição de pesquisa se houver um termo de pesquisa
        if (!termoPesquisa.isEmpty()) {
            sql += "WHERE m.nome_material LIKE ? ";

            // Se houver um filtro aplicado, adiciona à condição WHERE
            if ("Entradas".equals(filtro)) {
                sql += "AND tipo_movimentacao = 'Entrada'";
            } else if ("Saídas".equals(filtro)) {
                sql += "AND tipo_movimentacao = 'Saida'";
            }
        } else {
            // Se não houver termo de pesquisa, apenas aplicar o filtro se houver
            if ("Entradas".equals(filtro)) {
                sql += "WHERE tipo_movimentacao = 'Entrada'";
            } else if ("Saídas".equals(filtro)) {
                sql += "WHERE tipo_movimentacao = 'Saida'";
            }
        }

        //adicionar no final do select para deixar da mais recenta para a mais antiga
        sql += " ORDER BY id_movimentacao DESC";

        try {
            pst = conn.prepareStatement(sql);

            // Define o parâmetro de pesquisa se houver um termo de pesquisa
            if (!termoPesquisa.isEmpty()) {
                String searchTerm = "%" + termoPesquisa + "%";
                pst.setString(1, searchTerm);
            }

            rs = pst.executeQuery();
            // Atualiza a tabela com o resultado da consulta
            tblMovimentacoes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para adicionar Movimentações de Entrada
    private void novaEntrada() {
        conn = Conexao.getConexao();
        String sql = "INSERT INTO movimentacao_estoque(id_material, tipo_movimentacao, quantidade, data_movimentacao) values(?,'entrada',?,NOW())";

        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtEntradaIdMat.getText());
            pst.setString(2, txtEntradaQnt.getText());

            if ((txtEntradaIdMat.getText().isEmpty()) || (txtEntradaQnt.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Campos.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Movimentação de Entrada Realizada com Sucesso.");

                    limpar(); //chamando a função de limpar os campos
                    atualizarTabelas();
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para buscar material na Tela de Nova Entrada
    private void pesquisar_MovEntradas() {
        conn = Conexao.getConexao();
        String sql = "SELECT id_material AS 'ID Material', nome_material AS 'Nome', nome_categoria AS 'Categoria', valor_compra AS 'Valor', descricao AS 'Descrição' FROM material AS m "
                + "INNER JOIN categoria AS c ON m.id_categoria = c.id_categoria WHERE nome_material LIKE ?";

        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, "%" + txtEntradaBuscar.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblEntrada.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para adicionar Movimentações de Saída
    private void novaSaida() {
        conn = Conexao.getConexao();
        String sql = "INSERT INTO movimentacao_estoque(id_material, tipo_movimentacao, quantidade, data_movimentacao) values(?,'saida',?,NOW())";

        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtSaidaIdMat.getText());
            pst.setString(2, txtSaidaQnt.getText());

            if ((txtSaidaIdMat.getText().isEmpty()) || (txtSaidaQnt.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Campos.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Movimentação de Saída Realizada com Sucesso.");

                    limpar(); //chamando a função de limpar os campos
                    atualizarTabelas();
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para buscar material na Tela de Saída
    private void pesquisar_MovSaidas() {
        conn = Conexao.getConexao();
        String sql = "SELECT id_material AS 'ID Material', nome_material AS 'Nome', nome_categoria AS 'Categoria', valor_compra AS 'Valor', descricao AS 'Descrição' FROM material AS m "
                + "INNER JOIN categoria AS c ON m.id_categoria = c.id_categoria WHERE nome_material LIKE ?";

        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, "%" + txtBuscarEmSaida.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tabelaSaidas.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //=============================================================================================
    //Métodos da Tela Materiais | Fornecedores | Categorias
    //Método para exibir os dados na tabela Materiais
    private void pesquisar_MateriaisEmMat() {
        conn = Conexao.getConexao();
        String sql = "SELECT m.id_material AS 'ID Material', nome_material AS Material, c.nome_categoria AS Categoria, valor_compra AS Valor, e.quantidade_atual AS 'Estoque Atual', descricao AS Descrição "
                + "FROM material AS m INNER JOIN estoque AS e ON m.id_material = e.id_material INNER JOIN categoria AS c ON m.id_categoria = c.id_categoria WHERE m.nome_material LIKE ?";

        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, "%" + txtBuscarEmMat.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblMateriaisEmMat.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Método para exibir os dados na tabela Fornecedores
    private void pesquisar_FornecedoresEmForn() {
        conn = Conexao.getConexao();
        String sql = "SELECT id_fornecedor AS ID, nome_fornecedor AS Nome, CNPJ, email AS 'E-Mail', numero_telefone AS Telefone, endereco AS Endereço, Site FROM fornecedor WHERE nome_fornecedor LIKE ?";

        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, "%" + txtBuscarEmForn.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblFornecedoresEmForn.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Método para exibir os dados na tabela Fornecedores
    private void pesquisar_CategoriaEmCat() {
        conn = Conexao.getConexao();
        String sql = "SELECT id_categoria AS 'ID Categoria', nome_categoria AS 'Nome da Categoria' FROM categoria WHERE nome_categoria LIKE ?";

        try {
            pst = conn.prepareStatement(sql);
            //aqui, iremos passar o que foi digitado na caixa de pesquisa para o ?
            pst.setString(1, "%" + txtBuscarCategoria.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar
            tblCategoriasEmCat.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //=============================================================================================
    //Métodos da Tela Relatórios
    //Método para exibir o relatório na tabela, com base na combobox
    private void gerarRelatorio(String relatorio) {
        conn = Conexao.getConexao();
        String sql = "";

        switch (relatorio) {
            case "Relatório de Movimentação de Estoque por Fornecedor":
                sql = "SELECT f.nome_fornecedor AS Fornecedor, m.nome_material AS Material, SUM(CASE WHEN me.tipo_movimentacao = 'entrada' THEN me.quantidade ELSE 0 END) AS Entradas, "
                        + "SUM(CASE WHEN me.tipo_movimentacao = 'saida' THEN me.quantidade ELSE 0 END) AS Saídas, (SUM(CASE WHEN me.tipo_movimentacao = 'entrada' THEN me.quantidade ELSE 0 END) - "
                        + "SUM(CASE WHEN me.tipo_movimentacao = 'saida' THEN me.quantidade ELSE 0 END)) AS Saldo FROM movimentacao_estoque me INNER JOIN material m ON me.id_material = m.id_material "
                        + "INNER JOIN fornecedor_material fm ON fm.id_material = m.id_material INNER JOIN fornecedor f ON f.id_fornecedor = fm.id_fornecedor "
                        + "GROUP BY f.nome_fornecedor, m.nome_material ORDER BY f.nome_fornecedor, m.nome_material;";
                break;
            case "Relatório de Estoque por Categoria":
                sql = "SELECT c.nome_categoria AS Categoria, SUM(e.quantidade_atual) AS 'Estoque Total', COUNT(m.id_material) AS 'Quantidade de Materiais' FROM estoque e "
                        + "INNER JOIN material m ON e.id_material = m.id_material INNER JOIN categoria c ON m.id_categoria = c.id_categoria GROUP BY c.nome_categoria "
                        + "ORDER BY c.nome_categoria";
                break;
            case "Relatório de Movimentação de Estoque por Mês":
                sql = "SELECT YEAR(data_movimentacao) AS Ano, MONTH(data_movimentacao) AS Mês, SUM(CASE WHEN tipo_movimentacao = 'entrada' THEN quantidade ELSE 0 END) AS Entradas, "
                        + "SUM(CASE WHEN tipo_movimentacao = 'saida' THEN quantidade ELSE 0 END) AS Saídas FROM movimentacao_estoque GROUP BY YEAR(data_movimentacao), MONTH(data_movimentacao) "
                        + "ORDER BY YEAR(data_movimentacao), MONTH(data_movimentacao);";
                break;
            case "Relatório de Estoques Mínimos": //o parâmetro é 20 unidades
                sql = "SELECT m.nome_material AS Material, e.quantidade_atual AS 'Estoque Atual' FROM estoque e INNER JOIN material m ON e.id_material = m.id_material WHERE e.quantidade_atual < 20;";
                break;
            case "Relatório de Fornecedores e Materiais":
                sql = "SELECT f.nome_fornecedor AS Fornecedor, m.nome_material AS Material, c.nome_categoria AS Categoria, f.email AS Email, f.numero_telefone AS Telefone, "
                        + "f.endereco AS Endereco, f.site AS Site FROM fornecedor f INNER JOIN fornecedor_material fm ON f.id_fornecedor = fm.id_fornecedor INNER JOIN "
                        + "material m ON fm.id_material = m.id_material INNER JOIN categoria c ON m.id_categoria = c.id_categoria ORDER BY f.nome_fornecedor, c.nome_categoria, m.nome_material";
                break;
            case "Relatórios":
            default:
                resetarTabela();
                return;
        }

        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // Atualiza a tabela com o resultado da consulta
            tblRelatorios.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    //método para retornar a tabela de relatórios
    private void resetarTabela() {
        String[] colunas = {"Selecione um Relatório"};
        DefaultTableModel modelo = new DefaultTableModel(null, colunas);
        tblRelatorios.setModel(modelo);
    }

    //=============================================================================================
    //outros métodos
    //Método para atualizar tabelas
    private void atualizarTabelas() {
        pesquisar_fornecedor();
        pesquisar_material();
        pesquisar_categoriaEmMat();
        pesquisar_categoriaEmCat();
        pesquisar_FornecedorMaterial();
        atualizarTabelaMovimentacoes("Todas");
        pesquisar_MovEntradas();
        pesquisar_MovSaidas();
        pesquisar_MateriaisEmMat();
        pesquisar_FornecedoresEmForn();
        pesquisar_CategoriaEmCat();
    }

    //setar o icone mão nos botões
    private void btnEdit() {
        //MENU LATERAL
        btnCadastros.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMovimentacoes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMateriais.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFornecedores.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCategorias.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRelatorios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAjuda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSobre.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //TELA CADASTROS
        //FORNECEDOR
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //CATEGORIA
        btnCadastrarCat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterarCat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluirCat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubstituirCat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVincularFM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDesvincularFM.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVerTabelas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparEmCat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //MATERIAL
        btnCadastrarMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterarMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluirMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //TELA MOVIMENTAÇÕES
        //MOVIMENTAÇÕES
        btnLimparMovimentacoes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovaMovimentacao.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cBoxTipoMov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //ENTRADA
        btnSalvarEntrada.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparEntradas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //SAÍDA
        btnSalvarSaida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparSaidas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //TELA MATERIAIS | FORNECEDORES | CATEGORIAS
        btnLimparEmMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovoMaterialEmMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovaMovEmMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVincEmMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnLimparEmForn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovoFornecedorEmForn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVincEmForn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnLimparCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovaCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        //RELATÓRIOS
        cBoxRelatorios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        //AJUDA
        ajudaCad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ajudaMov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ajudaVerCad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ajudaRelat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    }

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
        txtMatBuscarCat.setText(null);
        txtBuscarMat.setText(null);
        ((DefaultTableModel) tblMaterial.getModel()).setRowCount(0);
        ((DefaultTableModel) tblCategoria.getModel()).setRowCount(0);

        //tela categoria
        txtIdCatEmCat.setText(null);
        txtNomeCat.setText(null);
        txtBuscarCatEmCat.setText(null);
        txtBuscarVM.setText(null);
        txtIdVincVM.setText(null);
        txtIdFornVM.setText(null);
        txtIdMatVM.setText(null);
        ((DefaultTableModel) tblCategoria2.getModel()).setRowCount(0);
        ((DefaultTableModel) tblVincularMaterial.getModel()).setRowCount(0);

        //tela Movimentações
        txtBuscarMov.setText(null);
        cBoxTipoMov.setSelectedItem("Todas");
        ((DefaultTableModel) tblMovimentacoes.getModel()).setRowCount(0);

        //Tela Movimentações > Entradas
        txtEntradaIdMat.setText(null);
        txtEntradaQnt.setText(null);
        txtEntradaBuscar.setText(null);
        ((DefaultTableModel) tblEntrada.getModel()).setRowCount(0);

        //Tela Movimentações > Saídas
        txtSaidaIdMat.setText(null);
        txtSaidaQnt.setText(null);
        txtBuscarEmSaida.setText(null);
        ((DefaultTableModel) tabelaSaidas.getModel()).setRowCount(0);

        //Tela Materiais
        txtBuscarEmMat.setText(null);
        ((DefaultTableModel) tblMateriaisEmMat.getModel()).setRowCount(0);

        //Tela Fornecedores
        txtBuscarEmForn.setText(null);
        ((DefaultTableModel) tblFornecedoresEmForn.getModel()).setRowCount(0);

        //Tela Categorias
        txtBuscarCategoria.setText(null);
        ((DefaultTableModel) tblCategoriasEmCat.getModel()).setRowCount(0);
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
        btnMateriais = new com.k33ptoo.components.KButton();
        btnFornecedores = new com.k33ptoo.components.KButton();
        btnCategorias = new com.k33ptoo.components.KButton();
        btnRelatorios = new com.k33ptoo.components.KButton();
        btnAjuda = new com.k33ptoo.components.KButton();
        btnSobre = new com.k33ptoo.components.KButton();
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
        btnHome13 = new javax.swing.JButton();
        telaCadFornecedor = new javax.swing.JPanel();
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
        jLabel51 = new javax.swing.JLabel();
        btnHome14 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        telaCadCategoria = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btnFechar2 = new javax.swing.JButton();
        txtIdCatEmCat = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtNomeCat = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblVincularMaterial = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblCategoria2 = new javax.swing.JTable();
        btnCadastrarCat = new com.k33ptoo.components.KButton();
        btnAlterarCat = new com.k33ptoo.components.KButton();
        btnExcluirCat = new com.k33ptoo.components.KButton();
        jSeparator5 = new javax.swing.JSeparator();
        txtIdVincVM = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtIdFornVM = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtIdMatVM = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        btnVincularFM = new com.k33ptoo.components.KButton();
        btnDesvincularFM = new com.k33ptoo.components.KButton();
        jLabel29 = new javax.swing.JLabel();
        txtBuscarCatEmCat = new javax.swing.JTextField();
        txtBuscarVM = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        btnLimparEmCat = new com.k33ptoo.components.KButton();
        btnSubstituirCat = new com.k33ptoo.components.KButton();
        btnVerTabelas = new com.k33ptoo.components.KButton();
        btnHome15 = new javax.swing.JButton();
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
        txtMatBuscarCat = new javax.swing.JTextField();
        btnLimparMat = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        btnHome16 = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        telaCadMovimentacoes = new javax.swing.JPanel();
        btnMovSaida = new com.k33ptoo.components.KButton();
        btnMovEntrada = new com.k33ptoo.components.KButton();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        btnHome17 = new javax.swing.JButton();
        telaMenuMovimentacoes = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblMovimentacoes = new javax.swing.JTable();
        btnNovaMovimentacao = new com.k33ptoo.components.KButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        txtBuscarMov = new javax.swing.JTextField();
        cBoxTipoMov = new javax.swing.JComboBox<>();
        btnLimparMovimentacoes = new com.k33ptoo.components.KButton();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        btnHome21 = new javax.swing.JButton();
        telaEntradaMov = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        btnSalvarEntrada = new com.k33ptoo.components.KButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblEntrada = new javax.swing.JTable();
        jLabel38 = new javax.swing.JLabel();
        txtEntradaIdMat = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        txtEntradaQnt = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtEntradaBuscar = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        btnLimparEntradas = new com.k33ptoo.components.KButton();
        btnHome22 = new javax.swing.JButton();
        btnFechar4 = new javax.swing.JButton();
        telaSaidaMov = new javax.swing.JPanel();
        btnSalvarSaida = new com.k33ptoo.components.KButton();
        jLabel41 = new javax.swing.JLabel();
        txtSaidaIdMat = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        txtSaidaQnt = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        btnLimparSaidas = new com.k33ptoo.components.KButton();
        txtBuscarEmSaida = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        tabelaSaidas = new javax.swing.JTable();
        jLabel62 = new javax.swing.JLabel();
        btnHome23 = new javax.swing.JButton();
        btnFechar5 = new javax.swing.JButton();
        telaMateriais = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblMateriaisEmMat = new javax.swing.JTable();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        txtBuscarEmMat = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        btnLimparEmMat = new com.k33ptoo.components.KButton();
        btnNovoMaterialEmMat = new com.k33ptoo.components.KButton();
        btnNovaMovEmMat = new com.k33ptoo.components.KButton();
        btnVincEmMat = new com.k33ptoo.components.KButton();
        jLabel63 = new javax.swing.JLabel();
        btnHome24 = new javax.swing.JButton();
        telaFornecedores = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblFornecedoresEmForn = new javax.swing.JTable();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        txtBuscarEmForn = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        btnLimparEmForn = new com.k33ptoo.components.KButton();
        btnNovoFornecedorEmForn = new com.k33ptoo.components.KButton();
        btnVincEmForn = new com.k33ptoo.components.KButton();
        jLabel64 = new javax.swing.JLabel();
        btnHome25 = new javax.swing.JButton();
        telaCategorias = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblCategoriasEmCat = new javax.swing.JTable();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        txtBuscarCategoria = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        btnLimparCategoria = new com.k33ptoo.components.KButton();
        btnNovaCategoria = new com.k33ptoo.components.KButton();
        jLabel65 = new javax.swing.JLabel();
        btnHome26 = new javax.swing.JButton();
        telaRelatorios = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblRelatorios = new javax.swing.JTable();
        jSeparator16 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        cBoxRelatorios = new javax.swing.JComboBox<>();
        jLabel66 = new javax.swing.JLabel();
        btnHome27 = new javax.swing.JButton();
        telaSobre = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        btnHome9 = new javax.swing.JButton();
        telaAjuda = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        btnHome8 = new javax.swing.JButton();
        ajudaCad = new com.k33ptoo.components.KButton();
        ajudaMov = new com.k33ptoo.components.KButton();
        ajudaVerCad = new com.k33ptoo.components.KButton();
        ajudaRelat = new com.k33ptoo.components.KButton();
        jLabel61 = new javax.swing.JLabel();
        telaAjuda1 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        btnHome12 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        telaAjuda2 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        btnHome18 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        telaAjuda3 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        btnHome19 = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        telaAjuda4 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        btnHome20 = new javax.swing.JButton();
        jLabel56 = new javax.swing.JLabel();

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
        btnCadastros.setMargin(new java.awt.Insets(8, 50, 3, 14));
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

        btnMateriais.setText("MATERIAIS");
        btnMateriais.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnMateriais.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMateriais.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        btnMateriais.setkEndColor(new java.awt.Color(26, 131, 43));
        btnMateriais.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnMateriais.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        btnMateriais.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnMateriais.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        btnMateriais.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        btnMateriais.setkPressedColor(new java.awt.Color(52, 153, 68));
        btnMateriais.setkSelectedColor(new java.awt.Color(52, 153, 68));
        btnMateriais.setkStartColor(new java.awt.Color(26, 131, 43));
        btnMateriais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMateriaisActionPerformed(evt);
            }
        });

        btnFornecedores.setText("FORNECEDORES");
        btnFornecedores.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnFornecedores.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFornecedores.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        btnFornecedores.setkEndColor(new java.awt.Color(26, 131, 43));
        btnFornecedores.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnFornecedores.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        btnFornecedores.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnFornecedores.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        btnFornecedores.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        btnFornecedores.setkPressedColor(new java.awt.Color(52, 153, 68));
        btnFornecedores.setkSelectedColor(new java.awt.Color(52, 153, 68));
        btnFornecedores.setkStartColor(new java.awt.Color(26, 131, 43));
        btnFornecedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFornecedoresActionPerformed(evt);
            }
        });

        btnCategorias.setText("CATEGORIAS");
        btnCategorias.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnCategorias.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCategorias.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        btnCategorias.setkEndColor(new java.awt.Color(26, 131, 43));
        btnCategorias.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnCategorias.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        btnCategorias.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCategorias.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        btnCategorias.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        btnCategorias.setkPressedColor(new java.awt.Color(52, 153, 68));
        btnCategorias.setkSelectedColor(new java.awt.Color(52, 153, 68));
        btnCategorias.setkStartColor(new java.awt.Color(26, 131, 43));
        btnCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoriasActionPerformed(evt);
            }
        });

        btnRelatorios.setText("RELATÓRIOS");
        btnRelatorios.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnRelatorios.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelatorios.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        btnRelatorios.setkEndColor(new java.awt.Color(26, 131, 43));
        btnRelatorios.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnRelatorios.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        btnRelatorios.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnRelatorios.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        btnRelatorios.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        btnRelatorios.setkPressedColor(new java.awt.Color(52, 153, 68));
        btnRelatorios.setkSelectedColor(new java.awt.Color(52, 153, 68));
        btnRelatorios.setkStartColor(new java.awt.Color(26, 131, 43));
        btnRelatorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelatoriosActionPerformed(evt);
            }
        });

        btnAjuda.setText("AJUDA");
        btnAjuda.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnAjuda.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAjuda.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        btnAjuda.setkEndColor(new java.awt.Color(26, 131, 43));
        btnAjuda.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnAjuda.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        btnAjuda.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnAjuda.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        btnAjuda.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        btnAjuda.setkPressedColor(new java.awt.Color(52, 153, 68));
        btnAjuda.setkSelectedColor(new java.awt.Color(52, 153, 68));
        btnAjuda.setkStartColor(new java.awt.Color(26, 131, 43));
        btnAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjudaActionPerformed(evt);
            }
        });

        btnSobre.setText("SOBRE");
        btnSobre.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnSobre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSobre.setkBackGroundColor(new java.awt.Color(52, 153, 68));
        btnSobre.setkEndColor(new java.awt.Color(26, 131, 43));
        btnSobre.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnSobre.setkHoverEndColor(new java.awt.Color(52, 153, 68));
        btnSobre.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnSobre.setkHoverStartColor(new java.awt.Color(52, 153, 68));
        btnSobre.setkIndicatorColor(new java.awt.Color(52, 153, 68));
        btnSobre.setkPressedColor(new java.awt.Color(52, 153, 68));
        btnSobre.setkSelectedColor(new java.awt.Color(52, 153, 68));
        btnSobre.setkStartColor(new java.awt.Color(26, 131, 43));
        btnSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSobreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuLateralLayout = new javax.swing.GroupLayout(menuLateral);
        menuLateral.setLayout(menuLateralLayout);
        menuLateralLayout.setHorizontalGroup(
            menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnCadastros, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnMovimentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnFornecedores, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnRelatorios, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnAjuda, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(btnSobre, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(menuLateralLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        menuLateralLayout.setVerticalGroup(
            menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLateralLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCadastros, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnMovimentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnFornecedores, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnRelatorios, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnAjuda, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnSobre, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(322, Short.MAX_VALUE))
        );

        getContentPane().add(menuLateral, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 700));

        telaInicial.setBackground(new java.awt.Color(217, 217, 217));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/STOCKSYNCIMG2.png"))); // NOI18N

        javax.swing.GroupLayout telaInicialLayout = new javax.swing.GroupLayout(telaInicial);
        telaInicial.setLayout(telaInicialLayout);
        telaInicialLayout.setHorizontalGroup(
            telaInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaInicialLayout.createSequentialGroup()
                .addContainerGap(353, Short.MAX_VALUE)
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

        btnHome13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome13.setBorderPainted(false);
        btnHome13.setContentAreaFilled(false);
        btnHome13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaCadastrosLayout = new javax.swing.GroupLayout(telaCadastros);
        telaCadastros.setLayout(telaCadastrosLayout);
        telaCadastrosLayout.setHorizontalGroup(
            telaCadastrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadastrosLayout.createSequentialGroup()
                .addGroup(telaCadastrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                .addComponent(btnCadMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(telaCadastrosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnHome13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)))
                .addContainerGap(95, Short.MAX_VALUE))
        );
        telaCadastrosLayout.setVerticalGroup(
            telaCadastrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadastrosLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaCadastrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnHome13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addGroup(telaCadastrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCadMaterial, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(btnCadCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCadFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(238, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab2", telaCadastros);

        telaCadFornecedor.setBackground(new java.awt.Color(217, 217, 217));

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
        jLabel2.setText("Nome do Fornecedor *");

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
        jLabel13.setText("E-mail *");

        jLabel14.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(26, 131, 43));
        jLabel14.setText("Telefone *");

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

        jLabel51.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(26, 131, 43));
        jLabel51.setText("> NOVO FORNECEDOR");

        btnHome14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome14.setBorderPainted(false);
        btnHome14.setContentAreaFilled(false);
        btnHome14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome14ActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(26, 131, 43));
        jLabel37.setText("* Campos Obrigatórios");

        javax.swing.GroupLayout telaCadFornecedorLayout = new javax.swing.GroupLayout(telaCadFornecedor);
        telaCadFornecedor.setLayout(telaCadFornecedorLayout);
        telaCadFornecedorLayout.setHorizontalGroup(
            telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
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
                        .addGap(127, 127, 127))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel37))
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtFornNome, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(txtFornCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(86, 86, 86))))
            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124)
                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(150, 150, 150))
            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btnHome14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel51)
                                .addGap(833, 833, 833))
                            .addComponent(jScrollPane1)
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnFechar)))
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, telaCadFornecedorLayout.createSequentialGroup()
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, telaCadFornecedorLayout.createSequentialGroup()
                                .addComponent(txtFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLimpar)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        telaCadFornecedorLayout.setVerticalGroup(
            telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel37))
                            .addComponent(btnHome14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addGap(55, 55, 55)
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(0, 0, 0)
                        .addComponent(txtFornEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtFornFone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addComponent(jLabel15)
                .addGap(0, 2, Short.MAX_VALUE)
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab2", telaCadFornecedor);

        telaCadCategoria.setBackground(new java.awt.Color(217, 217, 217));

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(26, 131, 43));
        jLabel8.setText("> NOVA CATEGORIA > VINCULAR ");

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

        txtIdCatEmCat.setBackground(new java.awt.Color(223, 223, 223));
        txtIdCatEmCat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtIdCatEmCat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtIdCatEmCat.setEnabled(false);
        txtIdCatEmCat.setSelectionColor(new java.awt.Color(26, 131, 43));

        jLabel22.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(26, 131, 43));
        jLabel22.setText("ID Categoria");

        txtNomeCat.setBackground(new java.awt.Color(223, 223, 223));
        txtNomeCat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtNomeCat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtNomeCat.setSelectionColor(new java.awt.Color(26, 131, 43));

        jLabel23.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(26, 131, 43));
        jLabel23.setText("Nome da Categoria");

        tblVincularMaterial = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblVincularMaterial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Nome do Material"
            }
        ));
        tblVincularMaterial.getTableHeader().setReorderingAllowed(false);
        tblVincularMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVincularMaterialMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblVincularMaterial);

        tblCategoria2 = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblCategoria2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Nome da Categoria"
            }
        ));
        tblCategoria2.getTableHeader().setReorderingAllowed(false);
        tblCategoria2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCategoria2MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblCategoria2);

        btnCadastrarCat.setText("Cadastrar");
        btnCadastrarCat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCadastrarCat.setkAllowGradient(false);
        btnCadastrarCat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnCadastrarCat.setkBorderRadius(20);
        btnCadastrarCat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnCadastrarCat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnCadastrarCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarCatActionPerformed(evt);
            }
        });

        btnAlterarCat.setText("Alterar");
        btnAlterarCat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAlterarCat.setkAllowGradient(false);
        btnAlterarCat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnAlterarCat.setkBorderRadius(20);
        btnAlterarCat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnAlterarCat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnAlterarCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarCatActionPerformed(evt);
            }
        });

        btnExcluirCat.setText("Excluir");
        btnExcluirCat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnExcluirCat.setkAllowGradient(false);
        btnExcluirCat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnExcluirCat.setkBorderRadius(20);
        btnExcluirCat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnExcluirCat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnExcluirCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirCatActionPerformed(evt);
            }
        });

        jSeparator5.setBackground(new java.awt.Color(26, 131, 43));
        jSeparator5.setForeground(new java.awt.Color(26, 131, 43));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 131, 43), 2));

        txtIdVincVM.setBackground(new java.awt.Color(223, 223, 223));
        txtIdVincVM.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtIdVincVM.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtIdVincVM.setEnabled(false);
        txtIdVincVM.setSelectionColor(new java.awt.Color(26, 131, 43));

        jLabel26.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(26, 131, 43));
        jLabel26.setText("ID Vinculação");

        txtIdFornVM.setBackground(new java.awt.Color(223, 223, 223));
        txtIdFornVM.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtIdFornVM.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtIdFornVM.setSelectionColor(new java.awt.Color(26, 131, 43));

        jLabel27.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(26, 131, 43));
        jLabel27.setText("ID Fornecedor");

        txtIdMatVM.setBackground(new java.awt.Color(223, 223, 223));
        txtIdMatVM.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtIdMatVM.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtIdMatVM.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtIdMatVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdMatVMActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(26, 131, 43));
        jLabel28.setText("ID Material");

        btnVincularFM.setText("Vincular");
        btnVincularFM.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVincularFM.setkAllowGradient(false);
        btnVincularFM.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnVincularFM.setkBorderRadius(20);
        btnVincularFM.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnVincularFM.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnVincularFM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVincularFMActionPerformed(evt);
            }
        });

        btnDesvincularFM.setText("Desvincular");
        btnDesvincularFM.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDesvincularFM.setkAllowGradient(false);
        btnDesvincularFM.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnDesvincularFM.setkBorderRadius(20);
        btnDesvincularFM.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnDesvincularFM.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnDesvincularFM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesvincularFMActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(26, 131, 43));
        jLabel29.setText("Buscar Categoria");

        txtBuscarCatEmCat.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarCatEmCat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarCatEmCat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarCatEmCat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarCatEmCat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarCatEmCatKeyReleased(evt);
            }
        });

        txtBuscarVM.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarVM.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarVM.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarVM.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarVM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarVMKeyReleased(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(26, 131, 43));
        jLabel31.setText("Buscar Vinculação");

        btnLimparEmCat.setText("Limpar");
        btnLimparEmCat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimparEmCat.setkAllowGradient(false);
        btnLimparEmCat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnLimparEmCat.setkBorderRadius(20);
        btnLimparEmCat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnLimparEmCat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnLimparEmCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparEmCatActionPerformed(evt);
            }
        });

        btnSubstituirCat.setText("Substituir");
        btnSubstituirCat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSubstituirCat.setkAllowGradient(false);
        btnSubstituirCat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnSubstituirCat.setkBorderRadius(20);
        btnSubstituirCat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnSubstituirCat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnSubstituirCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubstituirCatActionPerformed(evt);
            }
        });

        btnVerTabelas.setText("Ver Tabelas F/M");
        btnVerTabelas.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVerTabelas.setkAllowGradient(false);
        btnVerTabelas.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnVerTabelas.setkBorderRadius(20);
        btnVerTabelas.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnVerTabelas.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnVerTabelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerTabelasActionPerformed(evt);
            }
        });

        btnHome15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome15.setBorderPainted(false);
        btnHome15.setContentAreaFilled(false);
        btnHome15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaCadCategoriaLayout = new javax.swing.GroupLayout(telaCadCategoria);
        telaCadCategoria.setLayout(telaCadCategoriaLayout);
        telaCadCategoriaLayout.setHorizontalGroup(
            telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                        .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadCategoriaLayout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addComponent(txtIdCatEmCat, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(txtNomeCat, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(70, 70, 70))
                            .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(btnCadastrarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnAlterarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnExcluirCat, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSubstituirCat, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)))
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                .addComponent(btnVincularFM, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(btnDesvincularFM, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(btnVerTabelas, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26)
                                    .addComponent(txtIdVincVM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel27)
                                    .addComponent(txtIdFornVM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel28)
                                    .addComponent(txtIdMatVM, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 51, Short.MAX_VALUE))
                    .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                        .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel29)
                                    .addComponent(txtBuscarCatEmCat, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane5))
                                .addGap(12, 12, 12))
                            .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btnHome15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, telaCadCategoriaLayout.createSequentialGroup()
                                .addComponent(txtBuscarVM, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnLimparEmCat, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnFechar2))))
                .addGap(34, 34, 34))
        );
        telaCadCategoriaLayout.setVerticalGroup(
            telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadCategoriaLayout.createSequentialGroup()
                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                        .addGap(48, 48, 48)
                                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                        .addGap(61, 61, 61)
                                        .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                                    .addComponent(jLabel26)
                                                    .addGap(0, 0, 0)
                                                    .addComponent(txtIdVincVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, telaCadCategoriaLayout.createSequentialGroup()
                                                    .addComponent(jLabel27)
                                                    .addGap(0, 0, 0)
                                                    .addComponent(txtIdFornVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                                .addComponent(jLabel28)
                                                .addGap(0, 0, 0)
                                                .addComponent(txtIdMatVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(28, 28, 28)
                                        .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(btnVincularFM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnDesvincularFM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnVerTabelas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                    .addComponent(btnHome15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(173, 173, 173)))
                            .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtIdCatEmCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addGap(1, 1, 1)
                                        .addComponent(txtNomeCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(29, 29, 29)
                                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnCadastrarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAlterarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnExcluirCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSubstituirCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(86, 86, 86))
                    .addGroup(telaCadCategoriaLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnFechar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(jLabel31))
                        .addGap(1, 1, 1)
                        .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscarCatEmCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBuscarVM, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimparEmCat, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(telaCadCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jLabel18.setText("ID Categoria *");

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
        jLabel19.setText("Nome do Material *");

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
        jLabel21.setText("Valor *");

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

        txtMatBuscarCat.setBackground(new java.awt.Color(223, 223, 223));
        txtMatBuscarCat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtMatBuscarCat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtMatBuscarCat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtMatBuscarCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMatBuscarCatActionPerformed(evt);
            }
        });
        txtMatBuscarCat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMatBuscarCatKeyReleased(evt);
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

        jLabel24.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(26, 131, 43));
        jLabel24.setText("Buscar Material");

        jLabel25.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(26, 131, 43));
        jLabel25.setText("Buscar Categoria");

        btnHome16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome16.setBorderPainted(false);
        btnHome16.setContentAreaFilled(false);
        btnHome16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome16ActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(26, 131, 43));
        jLabel44.setText("* Campos Obrigatórios");

        javax.swing.GroupLayout telaCadMaterialLayout = new javax.swing.GroupLayout(telaCadMaterial);
        telaCadMaterial.setLayout(telaCadMaterialLayout);
        telaCadMaterialLayout.setHorizontalGroup(
            telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadMaterialLayout.createSequentialGroup()
                                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(telaCadMaterialLayout.createSequentialGroup()
                                            .addComponent(txtBuscarMat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnLimparMat)))
                                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMatBuscarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel25)))
                            .addComponent(btnFechar3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadMaterialLayout.createSequentialGroup()
                                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(btnHome16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel44))
                                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
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
                                            .addComponent(txtNomeMat, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(96, 96, 96))))
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
                .addGap(24, 24, 24)
                .addComponent(btnFechar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                        .addComponent(jLabel44))
                    .addComponent(btnHome16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25))
                .addGap(0, 0, 0)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMatBuscarCat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimparMat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab2", telaCadMaterial);

        telaCadMovimentacoes.setBackground(new java.awt.Color(217, 217, 217));

        btnMovSaida.setBackground(new java.awt.Color(222, 222, 222));
        btnMovSaida.setBorder(null);
        btnMovSaida.setForeground(new java.awt.Color(26, 131, 43));
        btnMovSaida.setToolTipText("");
        btnMovSaida.setAlignmentY(0.0F);
        btnMovSaida.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btnMovSaida.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnMovSaida.setIconTextGap(65);
        btnMovSaida.setkBackGroundColor(new java.awt.Color(239, 239, 239));
        btnMovSaida.setkBorderRadius(50);
        btnMovSaida.setkEndColor(new java.awt.Color(239, 239, 239));
        btnMovSaida.setkForeGround(new java.awt.Color(26, 131, 43));
        btnMovSaida.setkHoverColor(new java.awt.Color(239, 239, 239));
        btnMovSaida.setkHoverEndColor(new java.awt.Color(245, 245, 245));
        btnMovSaida.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnMovSaida.setkHoverStartColor(new java.awt.Color(245, 245, 245));
        btnMovSaida.setkIndicatorColor(new java.awt.Color(239, 239, 239));
        btnMovSaida.setkPressedColor(new java.awt.Color(175, 175, 175));
        btnMovSaida.setkSelectedColor(new java.awt.Color(0, 0, 0));
        btnMovSaida.setkStartColor(new java.awt.Color(239, 239, 239));
        btnMovSaida.setPreferredSize(new java.awt.Dimension(200, 175));
        btnMovSaida.setVerifyInputWhenFocusTarget(false);
        btnMovSaida.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnMovSaida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovSaidaActionPerformed(evt);
            }
        });

        btnMovEntrada.setBackground(new java.awt.Color(222, 222, 222));
        btnMovEntrada.setBorder(null);
        btnMovEntrada.setForeground(new java.awt.Color(26, 131, 43));
        btnMovEntrada.setToolTipText("");
        btnMovEntrada.setAlignmentY(0.0F);
        btnMovEntrada.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        btnMovEntrada.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnMovEntrada.setIconTextGap(65);
        btnMovEntrada.setkBackGroundColor(new java.awt.Color(239, 239, 239));
        btnMovEntrada.setkBorderRadius(50);
        btnMovEntrada.setkEndColor(new java.awt.Color(239, 239, 239));
        btnMovEntrada.setkForeGround(new java.awt.Color(26, 131, 43));
        btnMovEntrada.setkHoverColor(new java.awt.Color(239, 239, 239));
        btnMovEntrada.setkHoverEndColor(new java.awt.Color(245, 245, 245));
        btnMovEntrada.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnMovEntrada.setkHoverStartColor(new java.awt.Color(245, 245, 245));
        btnMovEntrada.setkIndicatorColor(new java.awt.Color(239, 239, 239));
        btnMovEntrada.setkPressedColor(new java.awt.Color(175, 175, 175));
        btnMovEntrada.setkSelectedColor(new java.awt.Color(0, 0, 0));
        btnMovEntrada.setkStartColor(new java.awt.Color(239, 239, 239));
        btnMovEntrada.setPreferredSize(new java.awt.Dimension(200, 175));
        btnMovEntrada.setVerifyInputWhenFocusTarget(false);
        btnMovEntrada.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btnMovEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovEntradaActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(26, 131, 43));
        jLabel30.setText("> NOVA MOVIMENTAÇÃO");

        jLabel32.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(26, 131, 43));
        jLabel32.setText("SELECIONE O TIPO DE MOVIMENTAÇÃO");

        btnHome17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome17.setBorderPainted(false);
        btnHome17.setContentAreaFilled(false);
        btnHome17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome17ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaCadMovimentacoesLayout = new javax.swing.GroupLayout(telaCadMovimentacoes);
        telaCadMovimentacoes.setLayout(telaCadMovimentacoesLayout);
        telaCadMovimentacoesLayout.setHorizontalGroup(
            telaCadMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadMovimentacoesLayout.createSequentialGroup()
                .addGroup(telaCadMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadMovimentacoesLayout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(btnMovEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(151, 151, 151)
                        .addComponent(btnMovSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaCadMovimentacoesLayout.createSequentialGroup()
                        .addGap(386, 386, 386)
                        .addComponent(jLabel32))
                    .addGroup(telaCadMovimentacoesLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnHome17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30)))
                .addContainerGap(230, Short.MAX_VALUE))
        );
        telaCadMovimentacoesLayout.setVerticalGroup(
            telaCadMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadMovimentacoesLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaCadMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(telaCadMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMovSaida, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(btnMovEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(244, 244, 244))
        );

        jTabbedPane2.addTab("tab3", telaCadMovimentacoes);

        telaMenuMovimentacoes.setBackground(new java.awt.Color(217, 217, 217));

        jLabel33.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(26, 131, 43));
        jLabel33.setText("> MOVIMENTAÇÕES");

        tblMovimentacoes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblMovimentacoes.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMovimentacoes.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(tblMovimentacoes);

        btnNovaMovimentacao.setText("Nova Movimentação");
        btnNovaMovimentacao.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNovaMovimentacao.setkAllowGradient(false);
        btnNovaMovimentacao.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnNovaMovimentacao.setkBorderRadius(20);
        btnNovaMovimentacao.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnNovaMovimentacao.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnNovaMovimentacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovaMovimentacaoActionPerformed(evt);
            }
        });

        txtBuscarMov.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarMov.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarMov.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarMov.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarMov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarMovActionPerformed(evt);
            }
        });
        txtBuscarMov.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarMovKeyReleased(evt);
            }
        });

        cBoxTipoMov.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Entradas", "Saídas" }));
        cBoxTipoMov.setToolTipText("");
        cBoxTipoMov.setName("Mov"); // NOI18N
        cBoxTipoMov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxTipoMovActionPerformed(evt);
            }
        });

        btnLimparMovimentacoes.setText("Limpar");
        btnLimparMovimentacoes.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimparMovimentacoes.setkAllowGradient(false);
        btnLimparMovimentacoes.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnLimparMovimentacoes.setkBorderRadius(20);
        btnLimparMovimentacoes.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnLimparMovimentacoes.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnLimparMovimentacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparMovimentacoesActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(26, 131, 43));
        jLabel34.setText("Filtrar");

        jLabel35.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(26, 131, 43));
        jLabel35.setText("Buscar Material");

        btnHome21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome21.setBorderPainted(false);
        btnHome21.setContentAreaFilled(false);
        btnHome21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome21ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaMenuMovimentacoesLayout = new javax.swing.GroupLayout(telaMenuMovimentacoes);
        telaMenuMovimentacoes.setLayout(telaMenuMovimentacoesLayout);
        telaMenuMovimentacoesLayout.setHorizontalGroup(
            telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3)
            .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 1088, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaMenuMovimentacoesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cBoxTipoMov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addGap(50, 50, 50)
                .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                        .addComponent(txtBuscarMov, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLimparMovimentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel35))
                .addGap(197, 197, 197))
            .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnNovaMovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1065, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnHome21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        telaMenuMovimentacoesLayout.setVerticalGroup(
            telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                        .addComponent(btnHome21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35))
                .addGap(0, 1, Short.MAX_VALUE)
                .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cBoxTipoMov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarMov, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimparMovimentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNovaMovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab7", telaMenuMovimentacoes);

        telaEntradaMov.setBackground(new java.awt.Color(217, 217, 217));

        jLabel36.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(26, 131, 43));
        jLabel36.setText("> NOVA MOVIMENTAÇÃO > ENTRADA");

        btnSalvarEntrada.setText("Salvar");
        btnSalvarEntrada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSalvarEntrada.setkAllowGradient(false);
        btnSalvarEntrada.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnSalvarEntrada.setkBorderRadius(20);
        btnSalvarEntrada.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnSalvarEntrada.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnSalvarEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarEntradaActionPerformed(evt);
            }
        });

        tblEntrada = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblEntrada.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEntrada.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(tblEntrada);

        jLabel38.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(26, 131, 43));
        jLabel38.setText("ID Material");

        txtEntradaIdMat.setBackground(new java.awt.Color(223, 223, 223));
        txtEntradaIdMat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtEntradaIdMat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtEntradaIdMat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtEntradaIdMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntradaIdMatActionPerformed(evt);
            }
        });
        txtEntradaIdMat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEntradaIdMatKeyReleased(evt);
            }
        });

        txtEntradaQnt.setBackground(new java.awt.Color(223, 223, 223));
        txtEntradaQnt.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtEntradaQnt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtEntradaQnt.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtEntradaQnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntradaQntActionPerformed(evt);
            }
        });
        txtEntradaQnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEntradaQntKeyReleased(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(26, 131, 43));
        jLabel40.setText("Quantidade");

        txtEntradaBuscar.setBackground(new java.awt.Color(223, 223, 223));
        txtEntradaBuscar.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtEntradaBuscar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtEntradaBuscar.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtEntradaBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntradaBuscarActionPerformed(evt);
            }
        });
        txtEntradaBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEntradaBuscarKeyReleased(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(26, 131, 43));
        jLabel39.setText("Buscar");

        btnLimparEntradas.setText("Limpar");
        btnLimparEntradas.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimparEntradas.setkAllowGradient(false);
        btnLimparEntradas.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnLimparEntradas.setkBorderRadius(20);
        btnLimparEntradas.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnLimparEntradas.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnLimparEntradas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparEntradasActionPerformed(evt);
            }
        });

        btnHome22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome22.setBorderPainted(false);
        btnHome22.setContentAreaFilled(false);
        btnHome22.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome22ActionPerformed(evt);
            }
        });

        btnFechar4.setBackground(new java.awt.Color(217, 217, 217));
        btnFechar4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnFechar4.setForeground(new java.awt.Color(26, 131, 43));
        btnFechar4.setText("X");
        btnFechar4.setBorder(null);
        btnFechar4.setBorderPainted(false);
        btnFechar4.setContentAreaFilled(false);
        btnFechar4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFechar4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechar4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaEntradaMovLayout = new javax.swing.GroupLayout(telaEntradaMov);
        telaEntradaMov.setLayout(telaEntradaMovLayout);
        telaEntradaMovLayout.setHorizontalGroup(
            telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaEntradaMovLayout.createSequentialGroup()
                .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                        .addComponent(jSeparator7)
                        .addGroup(telaEntradaMovLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                        .addGap(330, 330, 330)
                        .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEntradaIdMat, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38))
                        .addGap(39, 39, 39)
                        .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40)
                            .addGroup(telaEntradaMovLayout.createSequentialGroup()
                                .addComponent(txtEntradaQnt, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(btnSalvarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39)
                            .addGroup(telaEntradaMovLayout.createSequentialGroup()
                                .addComponent(txtEntradaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimparEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(telaEntradaMovLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnHome22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFechar4)
                .addGap(34, 34, 34))
        );
        telaEntradaMovLayout.setVerticalGroup(
            telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaEntradaMovLayout.createSequentialGroup()
                .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHome22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnFechar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel38)
                        .addGap(0, 0, 0)
                        .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEntradaIdMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSalvarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(0, 0, 0)
                        .addComponent(txtEntradaQnt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jLabel39)
                .addGap(0, 0, 0)
                .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtEntradaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimparEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab8", telaEntradaMov);

        telaSaidaMov.setBackground(new java.awt.Color(217, 217, 217));

        btnSalvarSaida.setText("Salvar");
        btnSalvarSaida.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSalvarSaida.setkAllowGradient(false);
        btnSalvarSaida.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnSalvarSaida.setkBorderRadius(20);
        btnSalvarSaida.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnSalvarSaida.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnSalvarSaida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarSaidaActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(26, 131, 43));
        jLabel41.setText("ID Material");

        txtSaidaIdMat.setBackground(new java.awt.Color(223, 223, 223));
        txtSaidaIdMat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtSaidaIdMat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtSaidaIdMat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtSaidaIdMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaidaIdMatActionPerformed(evt);
            }
        });
        txtSaidaIdMat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSaidaIdMatKeyReleased(evt);
            }
        });

        txtSaidaQnt.setBackground(new java.awt.Color(223, 223, 223));
        txtSaidaQnt.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtSaidaQnt.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtSaidaQnt.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtSaidaQnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaidaQntActionPerformed(evt);
            }
        });
        txtSaidaQnt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSaidaQntKeyReleased(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(26, 131, 43));
        jLabel42.setText("Quantidade");

        jLabel43.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(26, 131, 43));
        jLabel43.setText("Buscar");

        btnLimparSaidas.setText("Limpar");
        btnLimparSaidas.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimparSaidas.setkAllowGradient(false);
        btnLimparSaidas.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnLimparSaidas.setkBorderRadius(20);
        btnLimparSaidas.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnLimparSaidas.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnLimparSaidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparSaidasActionPerformed(evt);
            }
        });

        txtBuscarEmSaida.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarEmSaida.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarEmSaida.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarEmSaida.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarEmSaida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarEmSaidaActionPerformed(evt);
            }
        });
        txtBuscarEmSaida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarEmSaidaKeyReleased(evt);
            }
        });

        tabelaSaidas = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tabelaSaidas.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelaSaidas.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(tabelaSaidas);

        jLabel62.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(26, 131, 43));
        jLabel62.setText("> NOVA MOVIMENTAÇÃO > SAÍDA");

        btnHome23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome23.setBorderPainted(false);
        btnHome23.setContentAreaFilled(false);
        btnHome23.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome23ActionPerformed(evt);
            }
        });

        btnFechar5.setBackground(new java.awt.Color(217, 217, 217));
        btnFechar5.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        btnFechar5.setForeground(new java.awt.Color(26, 131, 43));
        btnFechar5.setText("X");
        btnFechar5.setBorder(null);
        btnFechar5.setBorderPainted(false);
        btnFechar5.setContentAreaFilled(false);
        btnFechar5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFechar5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFechar5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaSaidaMovLayout = new javax.swing.GroupLayout(telaSaidaMov);
        telaSaidaMov.setLayout(telaSaidaMovLayout);
        telaSaidaMovLayout.setHorizontalGroup(
            telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaSaidaMovLayout.createSequentialGroup()
                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                        .addComponent(jSeparator9)
                        .addGroup(telaSaidaMovLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(btnHome23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel62)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnFechar5)
                            .addGap(19, 19, 19)))
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addGap(330, 330, 330)
                        .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSaidaIdMat, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41))
                        .addGap(39, 39, 39)
                        .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42)
                            .addGroup(telaSaidaMovLayout.createSequentialGroup()
                                .addComponent(txtSaidaQnt, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(btnSalvarSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel43))
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtBuscarEmSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLimparSaidas, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        telaSaidaMovLayout.setVerticalGroup(
            telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaSaidaMovLayout.createSequentialGroup()
                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHome23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnFechar5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel41)
                        .addGap(0, 0, 0)
                        .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSaidaIdMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSalvarSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(0, 0, 0)
                        .addComponent(txtSaidaQnt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jLabel43)
                .addGap(0, 0, 0)
                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLimparSaidas, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscarEmSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab8", telaSaidaMov);

        telaMateriais.setBackground(new java.awt.Color(217, 217, 217));

        tblMateriaisEmMat = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblMateriaisEmMat.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMateriaisEmMat.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(tblMateriaisEmMat);

        txtBuscarEmMat.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarEmMat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarEmMat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarEmMat.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarEmMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarEmMatActionPerformed(evt);
            }
        });
        txtBuscarEmMat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarEmMatKeyReleased(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(26, 131, 43));
        jLabel47.setText("Buscar");

        btnLimparEmMat.setText("Limpar");
        btnLimparEmMat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimparEmMat.setkAllowGradient(false);
        btnLimparEmMat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnLimparEmMat.setkBorderRadius(20);
        btnLimparEmMat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnLimparEmMat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnLimparEmMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparEmMatActionPerformed(evt);
            }
        });

        btnNovoMaterialEmMat.setText("Novo Material");
        btnNovoMaterialEmMat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNovoMaterialEmMat.setkAllowGradient(false);
        btnNovoMaterialEmMat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnNovoMaterialEmMat.setkBorderRadius(20);
        btnNovoMaterialEmMat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnNovoMaterialEmMat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnNovoMaterialEmMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoMaterialEmMatActionPerformed(evt);
            }
        });

        btnNovaMovEmMat.setText("Nova Movimentação");
        btnNovaMovEmMat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNovaMovEmMat.setkAllowGradient(false);
        btnNovaMovEmMat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnNovaMovEmMat.setkBorderRadius(20);
        btnNovaMovEmMat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnNovaMovEmMat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnNovaMovEmMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovaMovEmMatActionPerformed(evt);
            }
        });

        btnVincEmMat.setText("Vincular");
        btnVincEmMat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVincEmMat.setkAllowGradient(false);
        btnVincEmMat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnVincEmMat.setkBorderRadius(20);
        btnVincEmMat.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnVincEmMat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnVincEmMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVincEmMatActionPerformed(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(26, 131, 43));
        jLabel63.setText("> MATERIAIS");

        btnHome24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome24.setBorderPainted(false);
        btnHome24.setContentAreaFilled(false);
        btnHome24.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaMateriaisLayout = new javax.swing.GroupLayout(telaMateriais);
        telaMateriais.setLayout(telaMateriaisLayout);
        telaMateriaisLayout.setHorizontalGroup(
            telaMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaMateriaisLayout.createSequentialGroup()
                .addGroup(telaMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator10, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                        .addComponent(jSeparator11)
                        .addGroup(telaMateriaisLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1067, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(telaMateriaisLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(btnHome24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel63)))
                    .addGroup(telaMateriaisLayout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addGroup(telaMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47)
                            .addGroup(telaMateriaisLayout.createSequentialGroup()
                                .addComponent(txtBuscarEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimparEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(49, 49, 49)
                        .addComponent(btnNovoMaterialEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnNovaMovEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnVincEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        telaMateriaisLayout.setVerticalGroup(
            telaMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaMateriaisLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHome24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addGap(0, 0, 0)
                .addGroup(telaMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtBuscarEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(telaMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLimparEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNovoMaterialEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNovaMovEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnVincEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab8", telaMateriais);

        telaFornecedores.setBackground(new java.awt.Color(217, 217, 217));

        tblFornecedoresEmForn = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblFornecedoresEmForn.setModel(new javax.swing.table.DefaultTableModel(
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
        tblFornecedoresEmForn.getTableHeader().setReorderingAllowed(false);
        jScrollPane10.setViewportView(tblFornecedoresEmForn);

        txtBuscarEmForn.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarEmForn.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarEmForn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarEmForn.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarEmForn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarEmFornActionPerformed(evt);
            }
        });
        txtBuscarEmForn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarEmFornKeyReleased(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(26, 131, 43));
        jLabel48.setText("Buscar");

        btnLimparEmForn.setText("Limpar");
        btnLimparEmForn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimparEmForn.setkAllowGradient(false);
        btnLimparEmForn.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnLimparEmForn.setkBorderRadius(20);
        btnLimparEmForn.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnLimparEmForn.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnLimparEmForn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparEmFornActionPerformed(evt);
            }
        });

        btnNovoFornecedorEmForn.setText("Novo Fornecedor");
        btnNovoFornecedorEmForn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNovoFornecedorEmForn.setkAllowGradient(false);
        btnNovoFornecedorEmForn.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnNovoFornecedorEmForn.setkBorderRadius(20);
        btnNovoFornecedorEmForn.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnNovoFornecedorEmForn.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnNovoFornecedorEmForn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoFornecedorEmFornActionPerformed(evt);
            }
        });

        btnVincEmForn.setText("Vincular");
        btnVincEmForn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnVincEmForn.setkAllowGradient(false);
        btnVincEmForn.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnVincEmForn.setkBorderRadius(20);
        btnVincEmForn.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnVincEmForn.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnVincEmForn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVincEmFornActionPerformed(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(26, 131, 43));
        jLabel64.setText("> FORNECEDORES");

        btnHome25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome25.setBorderPainted(false);
        btnHome25.setContentAreaFilled(false);
        btnHome25.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaFornecedoresLayout = new javax.swing.GroupLayout(telaFornecedores);
        telaFornecedores.setLayout(telaFornecedoresLayout);
        telaFornecedoresLayout.setHorizontalGroup(
            telaFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaFornecedoresLayout.createSequentialGroup()
                .addGroup(telaFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator12, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                        .addComponent(jSeparator13)
                        .addGroup(telaFornecedoresLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 1067, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(telaFornecedoresLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(btnHome25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel64)))
                    .addGroup(telaFornecedoresLayout.createSequentialGroup()
                        .addGap(237, 237, 237)
                        .addGroup(telaFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel48)
                            .addGroup(telaFornecedoresLayout.createSequentialGroup()
                                .addComponent(txtBuscarEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimparEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(49, 49, 49)
                        .addComponent(btnNovoFornecedorEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnVincEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        telaFornecedoresLayout.setVerticalGroup(
            telaFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaFornecedoresLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHome25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel48)
                .addGap(0, 0, 0)
                .addGroup(telaFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtBuscarEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(telaFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLimparEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNovoFornecedorEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnVincEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab8", telaFornecedores);

        telaCategorias.setBackground(new java.awt.Color(217, 217, 217));

        tblCategoriasEmCat = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblCategoriasEmCat.setModel(new javax.swing.table.DefaultTableModel(
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
        tblCategoriasEmCat.getTableHeader().setReorderingAllowed(false);
        jScrollPane11.setViewportView(tblCategoriasEmCat);

        txtBuscarCategoria.setBackground(new java.awt.Color(223, 223, 223));
        txtBuscarCategoria.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtBuscarCategoria.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtBuscarCategoria.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtBuscarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarCategoriaActionPerformed(evt);
            }
        });
        txtBuscarCategoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarCategoriaKeyReleased(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(26, 131, 43));
        jLabel49.setText("Buscar");

        btnLimparCategoria.setText("Limpar");
        btnLimparCategoria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimparCategoria.setkAllowGradient(false);
        btnLimparCategoria.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnLimparCategoria.setkBorderRadius(20);
        btnLimparCategoria.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnLimparCategoria.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnLimparCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparCategoriaActionPerformed(evt);
            }
        });

        btnNovaCategoria.setText("Nova Categoria");
        btnNovaCategoria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNovaCategoria.setkAllowGradient(false);
        btnNovaCategoria.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnNovaCategoria.setkBorderRadius(20);
        btnNovaCategoria.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnNovaCategoria.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnNovaCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovaCategoriaActionPerformed(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(26, 131, 43));
        jLabel65.setText("> CATEGORIAS");

        btnHome26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome26.setBorderPainted(false);
        btnHome26.setContentAreaFilled(false);
        btnHome26.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome26ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaCategoriasLayout = new javax.swing.GroupLayout(telaCategorias);
        telaCategorias.setLayout(telaCategoriasLayout);
        telaCategoriasLayout.setHorizontalGroup(
            telaCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCategoriasLayout.createSequentialGroup()
                .addGroup(telaCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator14, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                        .addComponent(jSeparator15)
                        .addGroup(telaCategoriasLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 1067, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(telaCategoriasLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(btnHome26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel65)))
                    .addGroup(telaCategoriasLayout.createSequentialGroup()
                        .addGap(301, 301, 301)
                        .addGroup(telaCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel49)
                            .addGroup(telaCategoriasLayout.createSequentialGroup()
                                .addComponent(txtBuscarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimparCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(49, 49, 49)
                        .addComponent(btnNovaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        telaCategoriasLayout.setVerticalGroup(
            telaCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCategoriasLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHome26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator14, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel49)
                .addGap(0, 0, 0)
                .addGroup(telaCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtBuscarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(telaCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLimparCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNovaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab8", telaCategorias);

        telaRelatorios.setBackground(new java.awt.Color(217, 217, 217));

        tblRelatorios = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblRelatorios.setModel(new javax.swing.table.DefaultTableModel(
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
        tblRelatorios.getTableHeader().setReorderingAllowed(false);
        jScrollPane12.setViewportView(tblRelatorios);

        cBoxRelatorios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Relatórios", "Relatório de Movimentação de Estoque por Fornecedor", "Relatório de Estoque por Categoria", "Relatório de Movimentação de Estoque por Mês", "Relatório de Estoques Mínimos" }));
        cBoxRelatorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBoxRelatoriosActionPerformed(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(26, 131, 43));
        jLabel66.setText("> RELATÓRIO");

        btnHome27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome27.setBorderPainted(false);
        btnHome27.setContentAreaFilled(false);
        btnHome27.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome27ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaRelatoriosLayout = new javax.swing.GroupLayout(telaRelatorios);
        telaRelatorios.setLayout(telaRelatoriosLayout);
        telaRelatoriosLayout.setHorizontalGroup(
            telaRelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaRelatoriosLayout.createSequentialGroup()
                .addGroup(telaRelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaRelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator16, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                        .addComponent(jSeparator17)
                        .addGroup(telaRelatoriosLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 1067, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(telaRelatoriosLayout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(btnHome27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel66)))
                    .addGroup(telaRelatoriosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cBoxRelatorios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        telaRelatoriosLayout.setVerticalGroup(
            telaRelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaRelatoriosLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaRelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHome27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(cBoxRelatorios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator17, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab8", telaRelatorios);

        telaSobre.setBackground(new java.awt.Color(217, 217, 217));

        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/txtStockSync.png"))); // NOI18N

        jLabel55.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(26, 131, 43));
        jLabel55.setText("> SOBRE");

        btnHome9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome9.setBorderPainted(false);
        btnHome9.setContentAreaFilled(false);
        btnHome9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaSobreLayout = new javax.swing.GroupLayout(telaSobre);
        telaSobre.setLayout(telaSobreLayout);
        telaSobreLayout.setHorizontalGroup(
            telaSobreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaSobreLayout.createSequentialGroup()
                .addGroup(telaSobreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaSobreLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnHome9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel55))
                    .addGroup(telaSobreLayout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(jLabel54)))
                .addContainerGap(112, Short.MAX_VALUE))
        );
        telaSobreLayout.setVerticalGroup(
            telaSobreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaSobreLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaSobreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab15", telaSobre);

        telaAjuda.setBackground(new java.awt.Color(217, 217, 217));

        jLabel52.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(26, 131, 43));
        jLabel52.setText("> AJUDA");

        btnHome8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome8.setBorderPainted(false);
        btnHome8.setContentAreaFilled(false);
        btnHome8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome8ActionPerformed(evt);
            }
        });

        ajudaCad.setText("Cadastros");
        ajudaCad.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ajudaCad.setkAllowGradient(false);
        ajudaCad.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        ajudaCad.setkBorderRadius(20);
        ajudaCad.setkHoverColor(new java.awt.Color(52, 153, 68));
        ajudaCad.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        ajudaCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajudaCadActionPerformed(evt);
            }
        });

        ajudaMov.setText("Movimentações");
        ajudaMov.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ajudaMov.setkAllowGradient(false);
        ajudaMov.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        ajudaMov.setkBorderRadius(20);
        ajudaMov.setkHoverColor(new java.awt.Color(52, 153, 68));
        ajudaMov.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        ajudaMov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajudaMovActionPerformed(evt);
            }
        });

        ajudaVerCad.setText("Visualizar Cadastros");
        ajudaVerCad.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ajudaVerCad.setkAllowGradient(false);
        ajudaVerCad.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        ajudaVerCad.setkBorderRadius(20);
        ajudaVerCad.setkHoverColor(new java.awt.Color(52, 153, 68));
        ajudaVerCad.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        ajudaVerCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajudaVerCadActionPerformed(evt);
            }
        });

        ajudaRelat.setText("Relatórios");
        ajudaRelat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ajudaRelat.setkAllowGradient(false);
        ajudaRelat.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        ajudaRelat.setkBorderRadius(20);
        ajudaRelat.setkHoverColor(new java.awt.Color(52, 153, 68));
        ajudaRelat.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        ajudaRelat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ajudaRelatActionPerformed(evt);
            }
        });

        jLabel61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/txtTelaAjuda.png"))); // NOI18N

        javax.swing.GroupLayout telaAjudaLayout = new javax.swing.GroupLayout(telaAjuda);
        telaAjuda.setLayout(telaAjudaLayout);
        telaAjudaLayout.setHorizontalGroup(
            telaAjudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaAjudaLayout.createSequentialGroup()
                .addGroup(telaAjudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaAjudaLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnHome8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel52))
                    .addGroup(telaAjudaLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel61))
                    .addGroup(telaAjudaLayout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(ajudaCad, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(ajudaMov, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(ajudaVerCad, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(ajudaRelat, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        telaAjudaLayout.setVerticalGroup(
            telaAjudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaAjudaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaAjudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel61)
                .addGap(18, 18, 18)
                .addGroup(telaAjudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ajudaCad, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ajudaMov, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ajudaVerCad, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ajudaRelat, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(343, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab14", telaAjuda);

        telaAjuda1.setBackground(new java.awt.Color(217, 217, 217));
        telaAjuda1.setAutoscrolls(true);

        jLabel57.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(26, 131, 43));
        jLabel57.setText("> AJUDA > CADASTROS");

        btnHome12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome12.setBorderPainted(false);
        btnHome12.setContentAreaFilled(false);
        btnHome12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome12ActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/ajudaCadastros.png"))); // NOI18N

        javax.swing.GroupLayout telaAjuda1Layout = new javax.swing.GroupLayout(telaAjuda1);
        telaAjuda1.setLayout(telaAjuda1Layout);
        telaAjuda1Layout.setHorizontalGroup(
            telaAjuda1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaAjuda1Layout.createSequentialGroup()
                .addGroup(telaAjuda1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaAjuda1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnHome12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel57))
                    .addGroup(telaAjuda1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel3)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        telaAjuda1Layout.setVerticalGroup(
            telaAjuda1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaAjuda1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaAjuda1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab14", telaAjuda1);

        telaAjuda2.setBackground(new java.awt.Color(217, 217, 217));
        telaAjuda2.setAutoscrolls(true);

        jLabel58.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(26, 131, 43));
        jLabel58.setText("> AJUDA > MOVIMENTAÇÕES");

        btnHome18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome18.setBorderPainted(false);
        btnHome18.setContentAreaFilled(false);
        btnHome18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome18ActionPerformed(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/ajudaMovimentacoes.png"))); // NOI18N

        javax.swing.GroupLayout telaAjuda2Layout = new javax.swing.GroupLayout(telaAjuda2);
        telaAjuda2.setLayout(telaAjuda2Layout);
        telaAjuda2Layout.setHorizontalGroup(
            telaAjuda2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaAjuda2Layout.createSequentialGroup()
                .addGroup(telaAjuda2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaAjuda2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnHome18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel58))
                    .addGroup(telaAjuda2Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel7)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        telaAjuda2Layout.setVerticalGroup(
            telaAjuda2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaAjuda2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaAjuda2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab14", telaAjuda2);

        telaAjuda3.setBackground(new java.awt.Color(217, 217, 217));
        telaAjuda3.setAutoscrolls(true);

        jLabel59.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(26, 131, 43));
        jLabel59.setText("> AJUDA > VISUALIZAR CADASTROS");

        btnHome19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome19.setBorderPainted(false);
        btnHome19.setContentAreaFilled(false);
        btnHome19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome19ActionPerformed(evt);
            }
        });

        jLabel53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/ajudaMenus.png"))); // NOI18N

        javax.swing.GroupLayout telaAjuda3Layout = new javax.swing.GroupLayout(telaAjuda3);
        telaAjuda3.setLayout(telaAjuda3Layout);
        telaAjuda3Layout.setHorizontalGroup(
            telaAjuda3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaAjuda3Layout.createSequentialGroup()
                .addGroup(telaAjuda3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaAjuda3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnHome19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel59))
                    .addGroup(telaAjuda3Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel53)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        telaAjuda3Layout.setVerticalGroup(
            telaAjuda3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaAjuda3Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaAjuda3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel53)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab14", telaAjuda3);

        telaAjuda4.setBackground(new java.awt.Color(217, 217, 217));
        telaAjuda4.setAutoscrolls(true);

        jLabel60.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(26, 131, 43));
        jLabel60.setText("> AJUDA > RELATÓRIOS");

        btnHome20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        btnHome20.setBorderPainted(false);
        btnHome20.setContentAreaFilled(false);
        btnHome20.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome20ActionPerformed(evt);
            }
        });

        jLabel56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/ajudaRelatorios.png"))); // NOI18N

        javax.swing.GroupLayout telaAjuda4Layout = new javax.swing.GroupLayout(telaAjuda4);
        telaAjuda4.setLayout(telaAjuda4Layout);
        telaAjuda4Layout.setHorizontalGroup(
            telaAjuda4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaAjuda4Layout.createSequentialGroup()
                .addGroup(telaAjuda4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaAjuda4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnHome20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel60))
                    .addGroup(telaAjuda4Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel56)))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        telaAjuda4Layout.setVerticalGroup(
            telaAjuda4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaAjuda4Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(telaAjuda4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel56)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab14", telaAjuda4);

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
        jTabbedPane2.setSelectedComponent(telaMenuMovimentacoes);
        atualizarTabelas();
    }//GEN-LAST:event_btnMovimentacoesActionPerformed

    private void btnCadCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadCategoriaActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadCategoria);
        //atualizartabelas
        atualizarTabelas();
    }//GEN-LAST:event_btnCadCategoriaActionPerformed

    private void btnCadFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadFornecedorActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadFornecedor);
        //atualizartabelas
        atualizarTabelas();
    }//GEN-LAST:event_btnCadFornecedorActionPerformed

    private void btnCadMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadMaterialActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadMaterial);
        //atualizar as tabelas
        atualizarTabelas();
    }//GEN-LAST:event_btnCadMaterialActionPerformed

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadastros);
        limpar();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnFechar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadastros);
        limpar();
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
        alterar_material();
    }//GEN-LAST:event_btnAlterarMatActionPerformed

    private void btnExcluirMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirMatActionPerformed
        // TODO add your handling code here:
        remover_material();
    }//GEN-LAST:event_btnExcluirMatActionPerformed

    private void txtBuscarMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarMatActionPerformed

    private void txtMatBuscarCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMatBuscarCatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatBuscarCatActionPerformed

    private void btnLimparMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparMatActionPerformed
        // TODO add your handling code here:
        limpar();
        btnCadastrarMat.setEnabled(true);
        btnCadastrarMat.setkBackGroundColor(new Color(26, 131, 43));
        btnCadastrarMat.setkHoverColor(new Color(52, 153, 68));
        //atualizar as tabelas
        atualizarTabelas();
    }//GEN-LAST:event_btnLimparMatActionPerformed

    private void txtBuscarMatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarMatKeyReleased
        // TODO add your handling code here:
        pesquisar_material();
    }//GEN-LAST:event_txtBuscarMatKeyReleased

    private void tblMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMaterialMouseClicked
        // TODO add your handling code here:
        setar_camposMaterial();
    }//GEN-LAST:event_tblMaterialMouseClicked

    private void txtMatBuscarCatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMatBuscarCatKeyReleased
        // TODO add your handling code here:
        pesquisar_categoriaEmMat();
    }//GEN-LAST:event_txtMatBuscarCatKeyReleased

    private void btnCadastrarCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarCatActionPerformed
        // TODO add your handling code here:
        cadastrarCategoria();
    }//GEN-LAST:event_btnCadastrarCatActionPerformed

    private void btnAlterarCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarCatActionPerformed
        // TODO add your handling code here:
        alterarCategoria();
    }//GEN-LAST:event_btnAlterarCatActionPerformed

    private void btnExcluirCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirCatActionPerformed
        // TODO add your handling code here:
        remover_categoria();
    }//GEN-LAST:event_btnExcluirCatActionPerformed

    private void btnVincularFMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVincularFMActionPerformed
        // TODO add your handling code here:
        vincularFornecedorMaterial();
    }//GEN-LAST:event_btnVincularFMActionPerformed

    private void btnDesvincularFMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesvincularFMActionPerformed
        // TODO add your handling code here:
        desvincularFornecedorMaterial();
    }//GEN-LAST:event_btnDesvincularFMActionPerformed

    private void txtIdMatVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdMatVMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdMatVMActionPerformed

    private void txtBuscarCatEmCatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCatEmCatKeyReleased
        // TODO add your handling code here:
        pesquisar_categoriaEmCat();
    }//GEN-LAST:event_txtBuscarCatEmCatKeyReleased

    private void txtBuscarVMKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarVMKeyReleased
        // TODO add your handling code here:
        pesquisar_FornecedorMaterial();
    }//GEN-LAST:event_txtBuscarVMKeyReleased

    private void tblCategoria2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCategoria2MouseClicked
        // TODO add your handling code here:
        setar_camposCategoria();
    }//GEN-LAST:event_tblCategoria2MouseClicked

    private void btnLimparEmCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparEmCatActionPerformed
        // TODO add your handling code here:
        limpar();
        //reativar o botao cadastrar
        btnCadastrarCat.setEnabled(true);
        btnCadastrarCat.setkBackGroundColor(new Color(26, 131, 43));
        btnCadastrarCat.setkHoverColor(new Color(52, 153, 68));

        //reativar o botao vincular
        btnVincularFM.setEnabled(true);
        btnVincularFM.setkBackGroundColor(new Color(26, 131, 43));
        btnVincularFM.setkHoverColor(new Color(52, 153, 68));
        //atualizar as tabelas
        atualizarTabelas();
    }//GEN-LAST:event_btnLimparEmCatActionPerformed

    private void btnSubstituirCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubstituirCatActionPerformed
        // TODO add your handling code here:
        PopupCategoria popup = new PopupCategoria();
        popup.setVisible(true);
    }//GEN-LAST:event_btnSubstituirCatActionPerformed

    private void btnVerTabelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerTabelasActionPerformed
        // TODO add your handling code here:
        PopupTabelasVM popup2 = new PopupTabelasVM();
        popup2.setVisible(true);
    }//GEN-LAST:event_btnVerTabelasActionPerformed

    private void tblVincularMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVincularMaterialMouseClicked
        // TODO add your handling code here:
        setar_camposFornecedorMaterial();
    }//GEN-LAST:event_tblVincularMaterialMouseClicked

    private void btnMovSaidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovSaidaActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaSaidaMov);
        atualizarTabelas();
    }//GEN-LAST:event_btnMovSaidaActionPerformed

    private void btnMovEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovEntradaActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaEntradaMov);
        atualizarTabelas();
    }//GEN-LAST:event_btnMovEntradaActionPerformed

    private void txtBuscarMovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarMovActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarMovActionPerformed

    private void txtBuscarMovKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarMovKeyReleased
        String termoPesquisa = txtBuscarMov.getText();
        String filtro = (String) cBoxTipoMov.getSelectedItem(); // Supondo que você tenha um JComboBox para selecionar o filtro
        pesquisar_Movimentacao(termoPesquisa, filtro);
    }//GEN-LAST:event_txtBuscarMovKeyReleased

    private void cBoxTipoMovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxTipoMovActionPerformed
        // Obtém o filtro selecionado no JComboBox
        String filtro = (String) cBoxTipoMov.getSelectedItem();
        // Chama a função para atualizar a tabela com o filtro selecionado
        //atualizarTabelaMovimentacoes(filtro);
        pesquisar_Movimentacao(txtBuscarMov.getText(), filtro);
    }//GEN-LAST:event_cBoxTipoMovActionPerformed

    private void btnLimparMovimentacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparMovimentacoesActionPerformed
        // TODO add your handling code here:
        limpar();
        atualizarTabelas();
    }//GEN-LAST:event_btnLimparMovimentacoesActionPerformed

    private void btnNovaMovimentacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovaMovimentacaoActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadMovimentacoes);
    }//GEN-LAST:event_btnNovaMovimentacaoActionPerformed

    private void btnSalvarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarEntradaActionPerformed
        // TODO add your handling code here:
        novaEntrada();
    }//GEN-LAST:event_btnSalvarEntradaActionPerformed

    private void txtEntradaIdMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntradaIdMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntradaIdMatActionPerformed

    private void txtEntradaIdMatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntradaIdMatKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntradaIdMatKeyReleased

    private void txtEntradaQntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntradaQntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntradaQntActionPerformed

    private void txtEntradaQntKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntradaQntKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntradaQntKeyReleased

    private void txtEntradaBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntradaBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEntradaBuscarActionPerformed

    private void txtEntradaBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEntradaBuscarKeyReleased
        // TODO add your handling code here:
        pesquisar_MovEntradas();
    }//GEN-LAST:event_txtEntradaBuscarKeyReleased

    private void btnSalvarSaidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarSaidaActionPerformed
        // TODO add your handling code here:
        novaSaida();
    }//GEN-LAST:event_btnSalvarSaidaActionPerformed

    private void txtSaidaIdMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaidaIdMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaidaIdMatActionPerformed

    private void txtSaidaIdMatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSaidaIdMatKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaidaIdMatKeyReleased

    private void txtSaidaQntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaidaQntActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaidaQntActionPerformed

    private void txtSaidaQntKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSaidaQntKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaidaQntKeyReleased

    private void btnLimparEntradasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparEntradasActionPerformed
        // TODO add your handling code here:
        limpar();
        atualizarTabelas();
    }//GEN-LAST:event_btnLimparEntradasActionPerformed

    private void btnLimparSaidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparSaidasActionPerformed
        // TODO add your handling code here:
        limpar();
        atualizarTabelas();
    }//GEN-LAST:event_btnLimparSaidasActionPerformed

    private void txtBuscarEmMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarEmMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarEmMatActionPerformed

    private void txtBuscarEmMatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmMatKeyReleased
        // TODO add your handling code here:
        pesquisar_MateriaisEmMat();
    }//GEN-LAST:event_txtBuscarEmMatKeyReleased

    private void btnLimparEmMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparEmMatActionPerformed
        // TODO add your handling code here:
        limpar();
        atualizarTabelas();
    }//GEN-LAST:event_btnLimparEmMatActionPerformed

    private void btnNovoMaterialEmMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoMaterialEmMatActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadMaterial);
    }//GEN-LAST:event_btnNovoMaterialEmMatActionPerformed

    private void btnNovaMovEmMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovaMovEmMatActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadMovimentacoes);
    }//GEN-LAST:event_btnNovaMovEmMatActionPerformed

    private void btnVincEmMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVincEmMatActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadCategoria);
    }//GEN-LAST:event_btnVincEmMatActionPerformed

    private void txtBuscarEmFornActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarEmFornActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarEmFornActionPerformed

    private void txtBuscarEmFornKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmFornKeyReleased
        // TODO add your handling code here:
        pesquisar_FornecedoresEmForn();
    }//GEN-LAST:event_txtBuscarEmFornKeyReleased

    private void btnLimparEmFornActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparEmFornActionPerformed
        // TODO add your handling code here:
        limpar();
        atualizarTabelas();
    }//GEN-LAST:event_btnLimparEmFornActionPerformed

    private void btnNovoFornecedorEmFornActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoFornecedorEmFornActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadFornecedor);
    }//GEN-LAST:event_btnNovoFornecedorEmFornActionPerformed

    private void btnVincEmFornActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVincEmFornActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadCategoria);
    }//GEN-LAST:event_btnVincEmFornActionPerformed

    private void txtBuscarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarCategoriaActionPerformed

    private void txtBuscarCategoriaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCategoriaKeyReleased
        // TODO add your handling code here:
        pesquisar_CategoriaEmCat();
    }//GEN-LAST:event_txtBuscarCategoriaKeyReleased

    private void btnLimparCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCategoriaActionPerformed
        // TODO add your handling code here:
        limpar();
        atualizarTabelas();
    }//GEN-LAST:event_btnLimparCategoriaActionPerformed

    private void btnNovaCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovaCategoriaActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadCategoria);
    }//GEN-LAST:event_btnNovaCategoriaActionPerformed

    private void btnMateriaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMateriaisActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaMateriais);
        atualizarTabelas();
    }//GEN-LAST:event_btnMateriaisActionPerformed

    private void btnFornecedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFornecedoresActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaFornecedores);
        atualizarTabelas();
    }//GEN-LAST:event_btnFornecedoresActionPerformed

    private void txtBuscarEmSaidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarEmSaidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarEmSaidaActionPerformed

    private void txtBuscarEmSaidaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmSaidaKeyReleased
        // TODO add your handling code here:
        pesquisar_MovSaidas();
    }//GEN-LAST:event_txtBuscarEmSaidaKeyReleased

    private void cBoxRelatoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBoxRelatoriosActionPerformed
        // TODO add your handling code here:
        String relatorio = (String) cBoxRelatorios.getSelectedItem();
        gerarRelatorio(relatorio);
    }//GEN-LAST:event_cBoxRelatoriosActionPerformed

    private void btnRelatoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatoriosActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaRelatorios);
        resetarTabela();
        cBoxRelatorios.setSelectedItem("Relatórios");
    }//GEN-LAST:event_btnRelatoriosActionPerformed

    private void btnCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoriasActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCategorias);
        atualizarTabelas();
    }//GEN-LAST:event_btnCategoriasActionPerformed

    private void btnAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjudaActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaAjuda);
    }//GEN-LAST:event_btnAjudaActionPerformed

    private void btnSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSobreActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaSobre);
    }//GEN-LAST:event_btnSobreActionPerformed

    private void btnHome8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome8ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome8ActionPerformed

    private void btnHome9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome9ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome9ActionPerformed

    private void btnHome12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome12ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome12ActionPerformed

    private void ajudaCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajudaCadActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaAjuda1);
    }//GEN-LAST:event_ajudaCadActionPerformed

    private void btnHome18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome18ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome18ActionPerformed

    private void btnHome19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome19ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome19ActionPerformed

    private void btnHome20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome20ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome20ActionPerformed

    private void ajudaMovActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajudaMovActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaAjuda2);
    }//GEN-LAST:event_ajudaMovActionPerformed

    private void ajudaVerCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajudaVerCadActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaAjuda3);
    }//GEN-LAST:event_ajudaVerCadActionPerformed

    private void ajudaRelatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ajudaRelatActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaAjuda4);
    }//GEN-LAST:event_ajudaRelatActionPerformed

    private void btnHome13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome13ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome13ActionPerformed

    private void btnHome14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome14ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
        limpar();
    }//GEN-LAST:event_btnHome14ActionPerformed

    private void btnHome15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome15ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
        limpar();
    }//GEN-LAST:event_btnHome15ActionPerformed

    private void btnHome16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome16ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
        limpar();
    }//GEN-LAST:event_btnHome16ActionPerformed

    private void btnHome17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome17ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome17ActionPerformed

    private void btnHome21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome21ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
        limpar();
    }//GEN-LAST:event_btnHome21ActionPerformed

    private void btnHome22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome22ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome22ActionPerformed

    private void btnHome23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome23ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome23ActionPerformed

    private void btnHome24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome24ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome24ActionPerformed

    private void btnHome25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome25ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome25ActionPerformed

    private void btnHome26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome26ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome26ActionPerformed

    private void btnHome27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome27ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome27ActionPerformed

    private void btnFechar4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar4ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadMovimentacoes);
        limpar();
    }//GEN-LAST:event_btnFechar4ActionPerformed

    private void btnFechar5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar5ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadMovimentacoes);
        limpar();
    }//GEN-LAST:event_btnFechar5ActionPerformed

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
    private com.k33ptoo.components.KButton ajudaCad;
    private com.k33ptoo.components.KButton ajudaMov;
    private com.k33ptoo.components.KButton ajudaRelat;
    private com.k33ptoo.components.KButton ajudaVerCad;
    private com.k33ptoo.components.KButton btnAdicionar;
    private com.k33ptoo.components.KButton btnAjuda;
    private com.k33ptoo.components.KButton btnAlterar;
    private com.k33ptoo.components.KButton btnAlterarCat;
    private com.k33ptoo.components.KButton btnAlterarMat;
    private com.k33ptoo.components.KButton btnCadCategoria;
    private com.k33ptoo.components.KButton btnCadFornecedor;
    private com.k33ptoo.components.KButton btnCadMaterial;
    private com.k33ptoo.components.KButton btnCadastrarCat;
    private com.k33ptoo.components.KButton btnCadastrarMat;
    private com.k33ptoo.components.KButton btnCadastros;
    private com.k33ptoo.components.KButton btnCategorias;
    private com.k33ptoo.components.KButton btnDesvincularFM;
    private com.k33ptoo.components.KButton btnExcluir;
    private com.k33ptoo.components.KButton btnExcluirCat;
    private com.k33ptoo.components.KButton btnExcluirMat;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnFechar2;
    private javax.swing.JButton btnFechar3;
    private javax.swing.JButton btnFechar4;
    private javax.swing.JButton btnFechar5;
    private com.k33ptoo.components.KButton btnFornecedores;
    private javax.swing.JButton btnHome12;
    private javax.swing.JButton btnHome13;
    private javax.swing.JButton btnHome14;
    private javax.swing.JButton btnHome15;
    private javax.swing.JButton btnHome16;
    private javax.swing.JButton btnHome17;
    private javax.swing.JButton btnHome18;
    private javax.swing.JButton btnHome19;
    private javax.swing.JButton btnHome20;
    private javax.swing.JButton btnHome21;
    private javax.swing.JButton btnHome22;
    private javax.swing.JButton btnHome23;
    private javax.swing.JButton btnHome24;
    private javax.swing.JButton btnHome25;
    private javax.swing.JButton btnHome26;
    private javax.swing.JButton btnHome27;
    private javax.swing.JButton btnHome8;
    private javax.swing.JButton btnHome9;
    private javax.swing.JButton btnLimpar;
    private com.k33ptoo.components.KButton btnLimparCategoria;
    private com.k33ptoo.components.KButton btnLimparEmCat;
    private com.k33ptoo.components.KButton btnLimparEmForn;
    private com.k33ptoo.components.KButton btnLimparEmMat;
    private com.k33ptoo.components.KButton btnLimparEntradas;
    private javax.swing.JButton btnLimparMat;
    private com.k33ptoo.components.KButton btnLimparMovimentacoes;
    private com.k33ptoo.components.KButton btnLimparSaidas;
    private com.k33ptoo.components.KButton btnMateriais;
    private com.k33ptoo.components.KButton btnMovEntrada;
    private com.k33ptoo.components.KButton btnMovSaida;
    private com.k33ptoo.components.KButton btnMovimentacoes;
    private com.k33ptoo.components.KButton btnNovaCategoria;
    private com.k33ptoo.components.KButton btnNovaMovEmMat;
    private com.k33ptoo.components.KButton btnNovaMovimentacao;
    private com.k33ptoo.components.KButton btnNovoFornecedorEmForn;
    private com.k33ptoo.components.KButton btnNovoMaterialEmMat;
    private com.k33ptoo.components.KButton btnRelatorios;
    private com.k33ptoo.components.KButton btnSalvarEntrada;
    private com.k33ptoo.components.KButton btnSalvarSaida;
    private com.k33ptoo.components.KButton btnSobre;
    private com.k33ptoo.components.KButton btnSubstituirCat;
    private com.k33ptoo.components.KButton btnVerTabelas;
    private com.k33ptoo.components.KButton btnVincEmForn;
    private com.k33ptoo.components.KButton btnVincEmMat;
    private com.k33ptoo.components.KButton btnVincularFM;
    private javax.swing.JComboBox<String> cBoxRelatorios;
    private javax.swing.JComboBox<String> cBoxTipoMov;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JPanel menuLateral;
    private javax.swing.JTable tabelaSaidas;
    private javax.swing.JTable tblCategoria;
    private javax.swing.JTable tblCategoria2;
    private javax.swing.JTable tblCategoriasEmCat;
    private javax.swing.JTable tblEntrada;
    private javax.swing.JTable tblFornecedores;
    private javax.swing.JTable tblFornecedoresEmForn;
    private javax.swing.JTable tblMateriaisEmMat;
    private javax.swing.JTable tblMaterial;
    private javax.swing.JTable tblMovimentacoes;
    private javax.swing.JTable tblRelatorios;
    private javax.swing.JTable tblVincularMaterial;
    private javax.swing.JPanel telaAjuda;
    private javax.swing.JPanel telaAjuda1;
    private javax.swing.JPanel telaAjuda2;
    private javax.swing.JPanel telaAjuda3;
    private javax.swing.JPanel telaAjuda4;
    private javax.swing.JPanel telaCadCategoria;
    private javax.swing.JPanel telaCadFornecedor;
    private javax.swing.JPanel telaCadMaterial;
    private javax.swing.JPanel telaCadMovimentacoes;
    private javax.swing.JPanel telaCadastros;
    private javax.swing.JPanel telaCategorias;
    private javax.swing.JPanel telaEntradaMov;
    private javax.swing.JPanel telaFornecedores;
    private javax.swing.JPanel telaInicial;
    private javax.swing.JPanel telaMateriais;
    private javax.swing.JPanel telaMenuMovimentacoes;
    private javax.swing.JPanel telaRelatorios;
    private javax.swing.JPanel telaSaidaMov;
    private javax.swing.JPanel telaSobre;
    private javax.swing.JTextField txtBuscarCatEmCat;
    private javax.swing.JTextField txtBuscarCategoria;
    private javax.swing.JTextField txtBuscarEmForn;
    private javax.swing.JTextField txtBuscarEmMat;
    private javax.swing.JTextField txtBuscarEmSaida;
    private javax.swing.JTextField txtBuscarMat;
    private javax.swing.JTextField txtBuscarMov;
    private javax.swing.JTextField txtBuscarVM;
    private javax.swing.JTextField txtDescMat;
    private javax.swing.JTextField txtEntradaBuscar;
    private javax.swing.JTextField txtEntradaIdMat;
    private javax.swing.JTextField txtEntradaQnt;
    private javax.swing.JTextField txtFornCnpj;
    private javax.swing.JTextField txtFornEmail;
    private javax.swing.JTextField txtFornEndereco;
    private javax.swing.JTextField txtFornFone;
    private javax.swing.JTextField txtFornId;
    private javax.swing.JTextField txtFornNome;
    private javax.swing.JTextField txtFornPesquisar;
    private javax.swing.JTextField txtFornSite;
    private javax.swing.JTextField txtIdCat;
    private javax.swing.JTextField txtIdCatEmCat;
    private javax.swing.JTextField txtIdFornVM;
    private javax.swing.JTextField txtIdMat;
    private javax.swing.JTextField txtIdMatVM;
    private javax.swing.JTextField txtIdVincVM;
    private javax.swing.JTextField txtMatBuscarCat;
    private javax.swing.JTextField txtNomeCat;
    private javax.swing.JTextField txtNomeMat;
    private javax.swing.JTextField txtSaidaIdMat;
    private javax.swing.JTextField txtSaidaQnt;
    private javax.swing.JTextField txtValorMat;
    // End of variables declaration//GEN-END:variables
}
