/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.teste.telas;

import java.sql.*;
import br.com.teste.dal.Conexao;
import br.com.teste.uteis.ComponentPopup;
import br.com.teste.uteis.PopupCategoria;
import br.com.teste.uteis.PopupSair;
import br.com.teste.uteis.PopupTabelasVM;
import com.k33ptoo.components.KButton;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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

        // atualizar as tabelas e comboboxes
        atualizarTabelas();
        atualizarComboBoxes();

        // Adicionando o listener para o botão de logout
        btnSairApp.addActionListener(e -> logout());

        // Adicionado o listener para o botão do Painel adm
        btnPainelAdmin.addActionListener(e -> {
            painelAdmin.setVisible(true); // Somente aqui o painel é exibido
        });

        // =====================================
        // FUNÇÕES DE ESTILO:
        //outros etilos
        definirIconeJanela();
        btnEdit();

        // Estiliza os botões de entrada e saída na tela movimentações
        estilizarBotaoPequeno(btnEntrada, "Entrada", "/br/com/teste/icones/iconeEntrada.png");
        estilizarBotaoPequeno(btnSaida, "Saída", "/br/com/teste/icones/iconeSaida.png");

        // Estiliza os botões do menu lateral com icones
        estilizarBotaoLateral(btnMateriais, "Materiais", "/br/com/teste/icones/iconMenuMateriais.png");
        estilizarBotaoLateral(btnFornecedores, "Fornecedores", "/br/com/teste/icones/iconMenuFornecedores.png");
        estilizarBotaoLateral(btnCategorias, "Categorias", "/br/com/teste/icones/iconMenuCategorias.png");
        estilizarBotaoLateral(btnMovimentacoes, "Movimentações", "/br/com/teste/icones/iconMenuMovimentacoes.png");
        estilizarBotaoLateral(btnPainelAdmin, "Painel Admin", "/br/com/teste/icones/iconMenuPainelAdmin.png");
        estilizarBotaoLateral(btnSairApp, "Sair", "/br/com/teste/icones/iconeSair.png");

        // Estiliza uma tabela
        estilizarTabela(tblMateriaisEmMat);
        estilizarTabela(tblFornecedores);
        estilizarTabela(tblFornecedoresEmForn);
        estilizarTabela(tblMaterial);
        estilizarTabela(tblPainelAdmin);
        estilizarTabela(tblNovaCad);
        estilizarTabela(tblMovimentacoes);
        estilizarTabela(tabelaSaidas);
        estilizarTabela(tblEntrada);
        estilizarTabela(tabelaMenuCategorias);

        // Estiliza as comboBox
        estilizarComboBox(cBoxIdCat);
        estilizarComboBox(cBoxTipoAlmox);
        estilizarComboBox(cBoxTipoMov);
        estilizarComboBox(cBoxMatEntrada);
        estilizarComboBox(cBoxMatSaida);

        //DESATIVANDO OS BOTÕES DAS TELAS DE CADASTRO:
        //cadastro fornecedor
        configurarEstadoKButton(btnAlterar, false);
        configurarEstadoKButton(btnExcluir, false);
        configurarEstadoKButton(btnAlterarMat, false);
        configurarEstadoKButton(btnExcluirMat, false);
        configurarEstadoKButton(btnNovaCatAlterar, false);
        configurarEstadoKButton(btnNovaCatExcluir, false);
        configurarEstadoKButton(btnAlterarPainel, false);
        configurarEstadoKButton(btnExcluirPainel, false);

        aplicarMargin(); //aplica uma margem nos campos de texto de todo o programa

        //remover tooltiptext ao colocar o mouse sobre algum item no menu lateral
        removerTooltips(btnMateriais, btnFornecedores, btnCategorias, btnMovimentacoes, btnPainelAdmin, btnSairApp);

        //função que configura os atalhos
        configurarAtalhos();
    }

    public void setIdUsuarioLogado(int idUsuario) {
        this.idUsuarioLogado = idUsuario;
    }

    private int idUsuarioLogado; // Declara a variável para armazenar o ID do usuário logado

    public void atualizarLabelUsuario(String nomeUsuario) {
        lblUsuario.setText("Olá, " + nomeUsuario);
    }

    // Classe para esconder/mostrar botão de acordo com a hierarquia
    public void configurarVisibilidade(String tipoAlmoxarife) {
        if (tipoAlmoxarife.equals("adm")) {
            // Exibe apenas o botão para abrir o painel de administração, não o painel em si
            btnPainelAdmin.setVisible(true);
            painelAdmin.setVisible(false); // painel escondido por padrão
        } else {
            // Oculta o botão e o painel para usuários que não são administradores
            btnPainelAdmin.setVisible(false);
            painelAdmin.setVisible(false);
        }
    }

    // Função sair do programa
    public void logout() {
        // Passa o comportamento de resposta para o PopupSair usando um listener
        PopupSair popupSair = new PopupSair(sairSelecionado -> {
            if (sairSelecionado) {
                // Se o usuário escolheu "Sair", exibe a tela de login
                TelaLogin telaLogin = new TelaLogin();
                telaLogin.setVisible(true);

                // Fecha o menu principal
                if (this instanceof JFrame) {
                    ((JFrame) this).dispose();
                }
            } else {
                System.out.println("Usuário escolheu 'Voltar', permanece na aplicação.");
            }
        });

        // Configura o popup e exibe
        popupSair.setLocationRelativeTo(null);
        popupSair.setVisible(true);
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

            if (txtFornNome.getText().isEmpty() || txtFornEmail.getText().isEmpty() || txtFornFone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Fornecedor Cadastrado com Sucesso.");

                    limpar(); // chamando a função de limpar os campos
                    atualizarTabelas();
                    jTabbedPane2.setSelectedComponent(telaFornecedores);
                }
            }
        } catch (MysqlDataTruncation e) {
            JOptionPane.showMessageDialog(null, "Um dos campos excedeu o tamanho permitido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro no banco de dados.");
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado na interface gráfica.");
        }
    }

    //metodo para buscar fornecedores
    private void pesquisar_fornecedor() {
        conn = Conexao.getConexao();
        String sql = "select id_fornecedor AS 'COD Fornecedor', nome_fornecedor AS Fornecedor, cnpj AS CNPJ, email AS Email, numero_telefone AS Telefone, endereco AS Endereço,"
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

    // Método para setar os campos do formulário com o conteúdo da tabela fornecedor
    public void setar_campos() {
        int setar = tblFornecedores.getSelectedRow();

        // Armazena o ID do fornecedor selecionado sem exibi-lo na interface
        idFornecedorSelecionado = Integer.parseInt(tblFornecedores.getModel().getValueAt(setar, 0).toString());

        // Preenche os demais campos do formulário
        txtFornNome.setText(tblFornecedores.getModel().getValueAt(setar, 1).toString());
        txtFornCnpj.setText(tblFornecedores.getModel().getValueAt(setar, 2).toString());
        txtFornEmail.setText(tblFornecedores.getModel().getValueAt(setar, 3).toString());
        txtFornFone.setText(tblFornecedores.getModel().getValueAt(setar, 4).toString());
        txtFornEndereco.setText(tblFornecedores.getModel().getValueAt(setar, 5).toString());
        txtFornSite.setText(tblFornecedores.getModel().getValueAt(setar, 6).toString());

        // Desabilita o botão de adicionar para evitar duplicidade
        btnAdicionar.setEnabled(false);
        btnAdicionar.setkBackGroundColor(new Color(128, 128, 128));
        btnAdicionar.setkHoverColor(new Color(128, 128, 128));

        configurarEstadoKButton(btnAlterar, true);
        configurarEstadoKButton(btnExcluir, true);
    }

    // Variável para armazenar o ID do fornecedor selecionado
    private int idFornecedorSelecionado;

    // Método para alterar dados dos fornecedores
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
            pst.setInt(7, idFornecedorSelecionado); // Utilizando a variável idFornecedorSelecionado

            if (txtFornNome.getText().isEmpty() || txtFornEmail.getText().isEmpty() || txtFornFone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do Fornecedor Alterados com Sucesso.");
                    limpar(); // Limpando os campos
                    atualizarTabelas(); // Atualizando as tabelas

                    // Reativando o botão de adicionar
                    btnAdicionar.setEnabled(true);
                    btnAdicionar.setkBackGroundColor(new Color(26, 131, 43));
                    btnAdicionar.setkHoverColor(new Color(52, 153, 68));
                }
            }
        } catch (MysqlDataTruncation e) {
            JOptionPane.showMessageDialog(null, "Um dos campos excedeu o tamanho permitido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro no banco de dados.");
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado na interface gráfica.");
        }
    }

    // Método para excluir cadastro dos fornecedores
    private void remover() {
        conn = Conexao.getConexao();

        // Verifica se os campos obrigatórios estão vazios
        if (txtFornNome.getText().isEmpty() || txtFornEmail.getText().isEmpty() || txtFornFone.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios.");
            return; // Encerra a execução da função se algum campo estiver vazio
        }

        // Verifica se um fornecedor foi selecionado
        if (idFornecedorSelecionado == 0) {
            JOptionPane.showMessageDialog(null, "Nenhum fornecedor selecionado para exclusão.");
            return; // Encerra a execução da função se nenhum fornecedor foi selecionado
        }

        // Cria a instância do ComponentPopup para exibir o pop-up de confirmação
        ComponentPopup popup = new ComponentPopup();
        popup.setLabelText("Tem certeza que deseja excluir o Fornecedor?"); // Define o texto da label
        popup.setButton1Text("Sim");
        popup.setButton2Text("Não");

        // Adiciona o ActionListener aos botões antes de exibir o pop-up
        popup.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comando = e.getActionCommand();

                if ("Sim".equals(comando)) {
                    String sql = "DELETE FROM fornecedor WHERE id_fornecedor=?";
                    try {
                        pst = conn.prepareStatement(sql);
                        pst.setInt(1, idFornecedorSelecionado);  // Utilizando idFornecedorSelecionado
                        int apagado = pst.executeUpdate();

                        if (apagado > 0) {
                            // Exibe a mensagem após a remoção
                            JOptionPane.showMessageDialog(null, "Fornecedor Removido com Sucesso!");
                            limpar(); // Chamando a função de limpar os campos
                            atualizarTabelas();
                            btnAdicionar.setEnabled(true);
                            btnAdicionar.setkBackGroundColor(new Color(26, 131, 43));
                            btnAdicionar.setkHoverColor(new Color(52, 153, 68));
                        }
                    } catch (HeadlessException | SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao remover fornecedor: " + ex.getMessage());
                    }
                }
                // Fecha o pop-up ao concluir a ação
                popup.dispose();
            }
        });

        // Exibe o pop-up após definir o ActionListener
        popup.setVisible(true);
    }

    //=============================================================================================
    //metodos para materiais
    //metodo para adicionar um material
    private void cadastrarMaterial() {
        conn = Conexao.getConexao();
        String sql = "INSERT INTO material(id_categoria, nome_material, descricao) VALUES(?, ?, ?)";

        try {
            // Verificar se o usuário manteve "Selecione uma categoria."
            String nomeCategoriaSelecionada = (String) cBoxIdCat.getSelectedItem();
            if (nomeCategoriaSelecionada == null || nomeCategoriaSelecionada.equals("Selecione uma categoria")) {
                JOptionPane.showMessageDialog(null, "Por favor, selecione uma categoria válida.");
                return;
            }

            // Obter o ID da categoria correspondente ao nome selecionado
            String sqlId = "SELECT id_categoria FROM categoria WHERE nome_categoria = ?";
            pst = conn.prepareStatement(sqlId);
            pst.setString(1, nomeCategoriaSelecionada);
            rs = pst.executeQuery();

            int idCategoria = 0;
            if (rs.next()) {
                idCategoria = rs.getInt("id_categoria");
            }

            // Inserir os dados do material no banco com o idCategoria encontrado
            String sqlInsert = "INSERT INTO material(id_categoria, nome_material, descricao) VALUES(?, ?, ?)";
            pst = conn.prepareStatement(sqlInsert);
            pst.setInt(1, idCategoria);  // Agora usamos o id encontrado
            pst.setString(2, txtNomeMat.getText());
            pst.setString(3, txtDescMat.getText());

            // Verifica se os campos obrigatórios estão preenchidos
            if (txtNomeMat.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Material cadastrado com sucesso.");

                    limpar(); // Chamando a função de limpar os campos
                    atualizarTabelas(); // Atualizar as tabelas
                    atualizarComboBoxes(); //Atualizando os Combo box
                    jTabbedPane2.setSelectedComponent(telaMateriais);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro no banco de dados: " + e.getMessage());
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado na interface gráfica: " + e.getMessage());
        }
    }

    //metodo para buscar um material
    private void pesquisar_material() {
        conn = Conexao.getConexao();
        //String sql = "select * from fornecedor where nome_fornecedor like ?";
        String sql = "SELECT material.id_material AS 'COD Material', categoria.nome_categoria AS 'Categoria', material.nome_material AS 'Material', material.descricao AS 'Descrição' FROM material INNER JOIN categoria ON material.id_categoria = categoria.id_categoria WHERE material.nome_material LIKE ?";
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

    // Método para setar os campos do formulário com o conteúdo da tabela material
    public void setar_camposMaterial() {
        int setar = tblMaterial.getSelectedRow();

        // Definindo o id do material na variável em vez de exibi-lo no campo de texto
        idMaterialSelecionado = Integer.parseInt(tblMaterial.getModel().getValueAt(setar, 0).toString());

        // Recuperando o nome da categoria da coluna 'Categoria' (índice 1)
        String nomeCategoria = tblMaterial.getModel().getValueAt(setar, 1).toString();

        // Definindo o nome da categoria na ComboBox
        cBoxIdCat.setSelectedItem(nomeCategoria);

        // Preenchendo os outros campos
        txtNomeMat.setText(tblMaterial.getModel().getValueAt(setar, 2).toString()); // Nome do material
        txtDescMat.setText(tblMaterial.getModel().getValueAt(setar, 3).toString()); // Descrição

        // Desabilitar o botão de adicionar para evitar dados duplicados
        btnCadastrarMat.setEnabled(false);
        btnCadastrarMat.setkBackGroundColor(new Color(128, 128, 128));
        btnCadastrarMat.setkHoverColor(new Color(128, 128, 128));

        configurarEstadoKButton(btnAlterarMat, true);
        configurarEstadoKButton(btnExcluirMat, true);
    }

    // Variável para armazenar o ID do material selecionado
    private int idMaterialSelecionado;

    // Método para alterar dados dos materiais
    private void alterar_material() {
        conn = Conexao.getConexao();
        String sql = "UPDATE material SET id_categoria=?, nome_material=?, descricao=? WHERE id_material=?";

        try {
            pst = conn.prepareStatement(sql);

            // Obter o nome da categoria selecionada na combobox
            String nomeCategoriaSelecionada = (String) cBoxIdCat.getSelectedItem();

            // Verificar se a categoria está selecionada
            if (nomeCategoriaSelecionada == null || nomeCategoriaSelecionada.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione uma Categoria.");
                return;
            }

            // Consultar o id correspondente ao nome da categoria
            String sqlCategoria = "SELECT id_categoria FROM categoria WHERE nome_categoria = ?";
            PreparedStatement pstCategoria = conn.prepareStatement(sqlCategoria);
            pstCategoria.setString(1, nomeCategoriaSelecionada);
            ResultSet rsCategoria = pstCategoria.executeQuery();

            if (rsCategoria.next()) {
                int idCategoriaSelecionada = rsCategoria.getInt("id_categoria");
                pst.setInt(1, idCategoriaSelecionada);
            } else {
                JOptionPane.showMessageDialog(null, "Categoria selecionada não encontrada.");
                return;
            }

            // Verifica se o campo nome do material está vazio
            if (txtNomeMat.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os Campos Obrigatórios.");
                return;
            }

            // Define o nome e a descrição do material
            pst.setString(2, txtNomeMat.getText());
            pst.setString(3, txtDescMat.getText());
            pst.setInt(4, idMaterialSelecionado); // Usa o ID armazenado na variável

            int adicionado = pst.executeUpdate();

            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Cadastro do Material Alterado com Sucesso.");
                limpar();
                atualizarTabelas();
                atualizarComboBoxes();
                btnCadastrarMat.setEnabled(true);
                btnCadastrarMat.setkBackGroundColor(new Color(26, 131, 43));
                btnCadastrarMat.setkHoverColor(new Color(52, 153, 68));
            }

            rsCategoria.close();
            pstCategoria.close();

        } catch (MysqlDataTruncation e) {
            JOptionPane.showMessageDialog(null, "Um dos campos excedeu o tamanho permitido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro no banco de dados.");
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado na interface gráfica.");
        }
    }

    // Método para excluir cadastro dos materiais
    private void remover_material() {
        conn = Conexao.getConexao();

        // Verifica se os campos obrigatórios estão vazios
        if (txtNomeMat.getText().isEmpty() || cBoxIdCat.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios.");
            return; // Encerra a execução da função se algum campo obrigatório estiver vazio
        }

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este Material?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "CALL delete_material(?)";
            try {
                pst = conn.prepareStatement(sql);

                // Define o ID do material diretamente pela variável idMaterialSelecionado
                pst.setInt(1, idMaterialSelecionado);
                int apagado = pst.executeUpdate();

                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Material Removido com Sucesso!");
                    limpar(); // Chamando a função de limpar os campos
                    atualizarTabelas(); // Atualizando as tabelas
                    atualizarComboBoxes(); // Atualizando os ComboBox

                    // Habilitar o botão de adicionar novamente
                    btnCadastrarMat.setEnabled(true);
                    btnCadastrarMat.setkBackGroundColor(new Color(26, 131, 43));
                    btnCadastrarMat.setkHoverColor(new Color(52, 153, 68));
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao remover o material: " + e.getMessage());
            } finally {
                try {
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
    }

    private void preencherComboBoxCategorias() {
        conn = Conexao.getConexao();
        String sql = "SELECT nome_categoria FROM categoria"; // Consulta para buscar todos os nomes das categorias

        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            // Cria um DefaultComboBoxModel para adicionar os nomes das categorias
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            // Adiciona um item padrão (opcional), como "Selecione uma categoria"
            model.addElement("Selecione uma categoria");

            while (rs.next()) {
                // Adiciona cada nome de categoria ao modelo
                model.addElement(rs.getString("nome_categoria"));
            }

            // Define o modelo na ComboBox
            cBoxIdCat.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher a ComboBox de categorias: " + e.getMessage());
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

    //=============================================================================================
    //metodos para Categorias | Vincular Materiais
    //metodo para adicionar uma Categoria
    private void cadastrarCategoria() {
        conn = Conexao.getConexao();
        String sql = "INSERT INTO categoria(nome_categoria) VALUES(?)";

        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtNovaCatNome.getText());

            if ((txtNovaCatNome.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha o Campos Nome.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Categoria Criada com Sucesso.");

                    limpar(); //chamando a função de limpar os campos
                    atualizarTabelas();
                    jTabbedPane2.setSelectedComponent(telaCategorias);
                }
            }
        } catch (MysqlDataTruncation e) {
            JOptionPane.showMessageDialog(null, "Um dos campos excedeu o tamanho permitido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro no banco de dados.");
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado na interface gráfica.");
        }
    }

    // Variável para armazenar o ID da categoria selecionada
    private int idCategoriaSelecionada;

// Método para alterar dados das categorias
    private void alterarCategoria() {
        conn = Conexao.getConexao();
        String sql = "UPDATE categoria SET nome_categoria=? WHERE id_categoria=?";

        try {
            pst = conn.prepareStatement(sql);

            // Verifica se o campo de nome da categoria está preenchido
            String nomeCategoria = txtNovaCatNome.getText();
            if (nomeCategoria == null || nomeCategoria.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha o Campo com o Nome da Categoria.");
                return;
            }

            // Define o nome da categoria no PreparedStatement
            pst.setString(1, nomeCategoria);

            // Verifica se há um ID da categoria selecionado
            if (idCategoriaSelecionada == 0) {
                JOptionPane.showMessageDialog(null, "Selecione uma categoria válida.");
                return;
            }

            // Define o ID da categoria no PreparedStatement
            pst.setInt(2, idCategoriaSelecionada);

            int adicionado = pst.executeUpdate();

            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Categoria Alterada com Sucesso.");

                limpar(); // Chamando a função de limpar os campos
                atualizarTabelas(); // Atualizar a tabela

                // Habilitar e redefinir o botão de salvar
                btnNovaCatSalvar.setEnabled(true);
                btnNovaCatSalvar.setkBackGroundColor(new Color(26, 131, 43));
                btnNovaCatSalvar.setkHoverColor(new Color(52, 153, 68));
            }

        } catch (MysqlDataTruncation e) {
            JOptionPane.showMessageDialog(null, "Um dos campos excedeu o tamanho permitido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro no banco de dados.");
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado na interface gráfica.");
        }
    }

    // Método para excluir cadastro de categoria
    private void remover_categoria() {
        conn = Conexao.getConexao();

        // Verifica se a categoria foi selecionada
        if (idCategoriaSelecionada == 0) {
            JOptionPane.showMessageDialog(null, "Selecione a categoria na Tabela.");
            return; // Encerra a execução da função se nenhuma categoria foi selecionada
        }

        // Cria a instância do ComponentPopup para exibir o pop-up de confirmação
        ComponentPopup popup = new ComponentPopup();
        popup.setLabelText("Tem certeza que deseja excluir esta Categoria?"); // Define o texto da label
        popup.setButton1Text("Sim");
        popup.setButton2Text("Não");

        // Adiciona o ActionListener aos botões antes de exibir o pop-up
        popup.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comando = e.getActionCommand();

                if ("Sim".equals(comando)) {
                    String sql = "CALL delete_categoria(?)";
                    try {
                        pst = conn.prepareStatement(sql);
                        pst.setInt(1, idCategoriaSelecionada);  // Usando idCategoriaSelecionada
                        pst.execute();
                        JOptionPane.showMessageDialog(null, "Categoria Removida com Sucesso!");
                        limpar(); // Chamando a função de limpar os campos
                        // Atualizar as tabelas
                        atualizarTabelas();
                        btnNovaCatSalvar.setEnabled(true);
                        btnNovaCatSalvar.setkBackGroundColor(new Color(26, 131, 43));
                        btnNovaCatSalvar.setkHoverColor(new Color(52, 153, 68));
                    } catch (SQLException ex) {
                        // Verificar a mensagem de erro específica e exibir uma mensagem apropriada
                        if (ex.getMessage().contains("Existem materiais vinculados a esta categoria. Exclusão não permitida.")) {
                            JOptionPane.showMessageDialog(null, "Existem materiais vinculados a esta categoria. Exclusão não permitida.");
                            substituir_categoria(); // Chama a função de substituição de categoria
                        } else {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Erro ao excluir a categoria.");
                        }
                    }
                }
                // Fecha o pop-up ao concluir a ação
                popup.dispose();
            }
        });

        // Exibe o pop-up após definir o ActionListener
        popup.setVisible(true);
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
            JOptionPane.showMessageDialog(null, "Erro ao verificar a existência da categoria.");
        }
        return false;
    }

    private void substituir_categoria() {
        // Solicita ao usuário o ID da nova categoria para substituição
        String novaCategoriaId = JOptionPane.showInputDialog(null, "Digite o ID da nova categoria para substituir:");

        // Verifica se o usuário cancelou ou deixou o campo vazio
        if (novaCategoriaId == null || novaCategoriaId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Substituição cancelada.");
            idCategoriaSelecionada = 0; // Reset do ID
            return;
        }

        // Verifica se a nova categoria existe
        if (!categoriaExiste(novaCategoriaId)) {
            JOptionPane.showMessageDialog(null, "A nova categoria não existe!");
            idCategoriaSelecionada = 0; // Reset do ID
            return;
        }

        String sql = "UPDATE material SET id_categoria = ? WHERE id_categoria = ?";
        try {
            pst = conn.prepareStatement(sql);

            // Define o ID da nova categoria no PreparedStatement
            pst.setString(1, novaCategoriaId);

            // Verifica se há uma categoria selecionada
            if (idCategoriaSelecionada == 0) {
                JOptionPane.showMessageDialog(null, "Selecione uma categoria na tabela.");
                return;
            }

            // Define o ID da categoria selecionada no PreparedStatement
            pst.setInt(2, idCategoriaSelecionada);

            int atualizados = pst.executeUpdate();
            if (atualizados > 0) {
                JOptionPane.showMessageDialog(null, "Categoria substituída com sucesso!");
                limpar(); // Chamando a função de limpar os campos
                atualizarTabelas(); // Atualizar as tabelas
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao substituir a categoria.");
        } finally {
            idCategoriaSelecionada = 0; // Reset do ID ao final da função
        }
    }

    // Método para setar os campos do formulário com o conteúdo da tabela categoria
    public void setar_camposCategoria() {
        int setar = tblNovaCad.getSelectedRow();

        // Definindo o id da categoria na variável em vez de exibi-lo no campo de texto
        idCategoriaSelecionada = Integer.parseInt(tblNovaCad.getModel().getValueAt(setar, 0).toString());

        // Recuperando o nome da categoria da coluna correspondente (índice 1)
        String nomeCategoria = tblNovaCad.getModel().getValueAt(setar, 1).toString();

        // Definindo o nome da categoria no campo txtNovaCatNome
        txtNovaCatNome.setText(nomeCategoria);

        // Desabilitar o botão de adicionar para evitar dados duplicados
        btnNovaCatSalvar.setEnabled(false);
        btnNovaCatSalvar.setkBackGroundColor(new Color(128, 128, 128));
        btnNovaCatSalvar.setkHoverColor(new Color(128, 128, 128));

        configurarEstadoKButton(btnNovaCatAlterar, true);
        configurarEstadoKButton(btnNovaCatExcluir, true);
    }

    //metodo para buscar uma categoria no MENU CATEGORIA
    private void pesquisarCategoria(JTextField campoPesquisa, JTable tabelaResultado) {
        conn = Conexao.getConexao();
        String sql = "select id_categoria AS 'COD Categoria', nome_categoria AS 'Nome da Categoria' FROM categoria where nome_categoria like ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + campoPesquisa.getText() + "%");
            rs = pst.executeQuery();
            tabelaResultado.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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

    //=============================================================================================
    //Metodos para Movimentações
    //Método para filtrar a busca(todas, entradas ou saídas)
    private void atualizarTabelaMovimentacoes(String filtro) {
        conn = Conexao.getConexao();
        String sql = "SELECT id_movimentacao AS 'COD Movimentação', m.nome_material AS 'Nome Material', m.id_material AS 'COD Material', tipo_movimentacao AS Tipo, quantidade AS Quantidade, DATE_FORMAT(data_movimentacao, '%d/%m/%Y %H:%i:%s') AS 'Data e Hora' "
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
        String sql = "SELECT id_movimentacao AS 'COD Movimentação', m.nome_material AS 'Nome Material', m.id_material AS 'COD Material', tipo_movimentacao AS Tipo, quantidade AS Quantidade, DATE_FORMAT(data_movimentacao, '%d/%m/%Y %H:%i:%s') AS 'Data e Hora' "
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

    //função para preencher a combobox da tela entradas
    private void preencherComboBoxMateriais() {
        conn = Conexao.getConexao();
        String sql = "SELECT nome_material FROM material"; // Consulta para buscar todos os nomes dos materiais

        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            // Cria um DefaultComboBoxModel para adicionar os nomes dos materiais
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            // Adiciona um item padrão (opcional), como "Selecione um material"
            model.addElement("Selecione um Material");

            while (rs.next()) {
                // Adiciona cada nome de material ao modelo
                model.addElement(rs.getString("nome_material"));
            }

            // Define o modelo na ComboBox
            cBoxMatEntrada.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher a ComboBox de materiais: " + e.getMessage());
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

    // Método para adicionar Movimentações de Entrada
    private void novaEntrada() {
        conn = Conexao.getConexao();
        String sql = "INSERT INTO movimentacao_estoque(id_material, tipo_movimentacao, quantidade, data_movimentacao) VALUES (?, 'entrada', ?, NOW())";

        try {
            // Verificar se o usuário selecionou um material válido
            String nomeMaterialSelecionado = (String) cBoxMatEntrada.getSelectedItem();
            if (nomeMaterialSelecionado == null || nomeMaterialSelecionado.equals("Selecione um Material")) {
                JOptionPane.showMessageDialog(null, "Por favor, selecione um material válido.");
                return;
            }

            // Obter o ID do material correspondente ao nome selecionado
            String sqlId = "SELECT id_material FROM material WHERE nome_material = ?";
            pst = conn.prepareStatement(sqlId);
            pst.setString(1, nomeMaterialSelecionado);
            rs = pst.executeQuery();

            int idMaterial = 0;
            if (rs.next()) {
                idMaterial = rs.getInt("id_material");
            } else {
                JOptionPane.showMessageDialog(null, "Material não encontrado.");
                return;
            }

            pst = conn.prepareStatement(sql);

            // Define o ID do material no PreparedStatement
            pst.setInt(1, idMaterial);

            // Verifica se o campo de quantidade está preenchido
            if (txtEntradaQnt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha o campo de quantidade.");
                return;
            }

            // Define a quantidade no PreparedStatement
            pst.setString(2, txtEntradaQnt.getText());

            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Movimentação de Entrada Realizada com Sucesso.");

                limpar(); // Chamando a função de limpar os campos
                atualizarTabelas(); // Atualizar as tabelas
                atualizarComboBoxes();
            }
        } catch (MysqlDataTruncation e) {
            JOptionPane.showMessageDialog(null, "Um dos campos excedeu o tamanho permitido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro no banco de dados.");
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado na interface gráfica.");
        }
    }

    // Método para buscar material na Tela de Nova Entrada
    private void pesquisar_MovEntradas() {
        conn = Conexao.getConexao();
        String sql = "SELECT m.nome_material AS 'Nome', c.nome_categoria AS 'Categoria', m.descricao AS 'Descrição', e.quantidade_atual AS 'Estoque' "
                + "FROM material AS m "
                + "INNER JOIN categoria AS c ON m.id_categoria = c.id_categoria "
                + "INNER JOIN estoque AS e ON m.id_material = e.id_material "
                + "WHERE m.nome_material LIKE ?";

        try {
            pst = conn.prepareStatement(sql);
            // Passa o texto digitado na caixa de pesquisa para o ?
            pst.setString(1, "%" + txtEntradaBuscar.getText() + "%");
            rs = pst.executeQuery();
            // Exibe os resultados na tabela usando a biblioteca rs2xml.jar
            tblEntrada.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //função para preencher a combobox da tela entradas
    private void preencherComboBoxMateriais2() {
        conn = Conexao.getConexao();
        String sql = "SELECT nome_material FROM material"; // Consulta para buscar todos os nomes dos materiais

        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            // Cria um DefaultComboBoxModel para adicionar os nomes dos materiais
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            // Adiciona um item padrão (opcional), como "Selecione um material"
            model.addElement("Selecione um Material");

            while (rs.next()) {
                // Adiciona cada nome de material ao modelo
                model.addElement(rs.getString("nome_material"));
            }

            // Define o modelo na ComboBox
            cBoxMatSaida.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao preencher a ComboBox de materiais: " + e.getMessage());
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

    private void novaSaida() {
        conn = Conexao.getConexao();
        String sql = "INSERT INTO movimentacao_estoque(id_material, tipo_movimentacao, quantidade, data_movimentacao) values(?,'saida',?,NOW())";

        try {
            // Obter o nome do material selecionado na ComboBox
            String nomeMaterialSelecionado = (String) cBoxMatSaida.getSelectedItem();
            if (nomeMaterialSelecionado == null || nomeMaterialSelecionado.equals("Selecione um Material")) {
                JOptionPane.showMessageDialog(null, "Por favor, selecione um material válido.");
                return;
            }

            // Obter o ID do material correspondente ao nome selecionado
            String sqlId = "SELECT id_material FROM material WHERE nome_material = ?";
            pst = conn.prepareStatement(sqlId);
            pst.setString(1, nomeMaterialSelecionado);
            rs = pst.executeQuery();

            int idMaterial = 0;
            if (rs.next()) {
                idMaterial = rs.getInt("id_material");
            }

            // Prepara o INSERT com o ID do material obtido
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idMaterial);  // Define o ID do material na consulta
            pst.setString(2, txtSaidaQnt.getText());  // Define a quantidade

            // Verifica se a quantidade está preenchida
            if (txtSaidaQnt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Campos.");
            } else {
                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Movimentação de Saída Realizada com Sucesso.");

                    limpar();  // Chamando a função de limpar os campos
                    atualizarTabelas();  // Atualizando as tabelas
                    atualizarComboBoxes();  // Atualizando os combo boxes
                }
            }
        } catch (SQLException e) {
            // Lida com o erro de quantidade insuficiente no estoque
            if ("45000".equals(e.getSQLState())) {
                JOptionPane.showMessageDialog(null, "Não pode ser realizada a saída, a quantidade excede o estoque disponível.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro no banco de dados: " + e.getMessage());
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado na interface gráfica.");
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

    //metodo para buscar material na Tela de Saída
    private void pesquisar_MovSaidas() {
        conn = Conexao.getConexao();
        String sql = "SELECT m.nome_material AS 'Nome', c.nome_categoria AS 'Categoria', m.descricao AS 'Descrição', e.quantidade_atual AS 'Estoque' "
                + "FROM material AS m "
                + "INNER JOIN categoria AS c ON m.id_categoria = c.id_categoria "
                + "INNER JOIN estoque AS e ON m.id_material = e.id_material "
                + "WHERE m.nome_material LIKE ?";

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
        String sql = "SELECT m.id_material AS 'COD Material', nome_material AS Material, c.nome_categoria AS Categoria, e.quantidade_atual AS 'Estoque Atual', descricao AS Descrição "
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
        String sql = "SELECT id_fornecedor AS 'COD Fornecedor', nome_fornecedor AS Nome, CNPJ, email AS 'E-Mail', numero_telefone AS Telefone, endereco AS Endereço, Site FROM fornecedor WHERE nome_fornecedor LIKE ?";

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

    //=============================================================================================
    //Métodos do Painel Admin
    //Método para buscar dados na tabela
    private void pesquisar_painelAdmin() {
        conn = Conexao.getConexao();
        String sql = "SELECT id_almoxarife AS 'COD', tipo_almoxarife AS 'Tipo', nome AS 'Nome', login AS 'Login', senha AS 'Senha' "
                + "FROM almoxarife "
                + "WHERE nome LIKE ?";

        try {
            pst = conn.prepareStatement(sql);
            // Passando o texto de busca digitado no txtPainelAdmin para o parâmetro de consulta
            pst.setString(1, "%" + txtPainelAdmin.getText() + "%");
            rs = pst.executeQuery();

            // Exibindo o resultado na tblPainelAdmin
            tblPainelAdmin.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar dados para o painel administrativo: " + e.getMessage());
        }
    }

    // Variável para armazenar o ID do almoxarife selecionado
    private int idAlmoxarifeSelecionado;

// Método para alterar dados dos almoxarifes
    private void alterar_almoxarife() {
        conn = Conexao.getConexao();
        String sql = "UPDATE almoxarife SET tipo_almoxarife=?, nome=?, login=?, senha=? WHERE id_almoxarife=?";

        try {
            pst = conn.prepareStatement(sql);

            // Obter o tipo de almoxarife selecionado na combobox
            String tipoSelecionado = (String) cBoxTipoAlmox.getSelectedItem();

            // Verificar se o tipo de almoxarife está selecionado
            if (tipoSelecionado == null || tipoSelecionado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione o Tipo de Almoxarife.");
                return; // Interrompe a função se o tipo não estiver selecionado
            }

            // Verificar se os campos obrigatórios estão preenchidos
            if (txtNomePainel.getText().isEmpty() || txtLoginPainel.getText().isEmpty() || txtSenhaPainel.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os Campos.");
                return; // Interrompe a função se algum campo obrigatório estiver vazio
            }

            // Define o tipo, nome, login e senha do almoxarife no PreparedStatement
            pst.setString(1, tipoSelecionado);
            pst.setString(2, txtNomePainel.getText());
            pst.setString(3, txtLoginPainel.getText());
            pst.setString(4, txtSenhaPainel.getText());

            // Verifica se há um ID de almoxarife selecionado
            if (idAlmoxarifeSelecionado == 0) {
                JOptionPane.showMessageDialog(null, "Selecione um almoxarife válido.");
                return;
            }

            // Define o ID do almoxarife no PreparedStatement
            pst.setInt(5, idAlmoxarifeSelecionado);

            int atualizado = pst.executeUpdate();

            if (atualizado > 0) {
                JOptionPane.showMessageDialog(null, "Cadastro do Almoxarife Alterado com Sucesso.");

                limpar(); // Chamando a função de limpar os campos
                atualizarTabelas(); // Atualizando a tabela de almoxarifes
                atualizarComboBoxes(); // Atualizando os ComboBoxes, se necessário
            }

        } catch (MysqlDataTruncation e) {
            JOptionPane.showMessageDialog(null, "Um dos campos excedeu o tamanho permitido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro no banco de dados.");
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado na interface gráfica.");
        }
    }

    // Método para setar os campos do formulário com o conteúdo da tabela almoxarife
    public void setar_camposAlmoxarife() {
        int setar = tblPainelAdmin.getSelectedRow();

        // Definindo o id do cadastro na variável de instância em vez de exibi-lo no campo de texto
        idAlmoxarifeSelecionado = Integer.parseInt(tblPainelAdmin.getModel().getValueAt(setar, 0).toString());

        // Recuperando o cargo da coluna 'tipo' (índice 1)
        String tipoAlmoxarife = tblPainelAdmin.getModel().getValueAt(setar, 1).toString();

        // Definindo o tipo de almoxarife na ComboBox
        cBoxTipoAlmox.setSelectedItem(tipoAlmoxarife);

        // Preenchendo os outros campos
        txtNomePainel.setText(tblPainelAdmin.getModel().getValueAt(setar, 2).toString()); // Nome do usuário
        txtLoginPainel.setText(tblPainelAdmin.getModel().getValueAt(setar, 3).toString()); // Login
        txtSenhaPainel.setText(tblPainelAdmin.getModel().getValueAt(setar, 4).toString()); // Senha

        configurarEstadoKButton(btnAlterarPainel, true);
        configurarEstadoKButton(btnExcluirPainel, true);
    }

    // Função para preencher o combobox trazendo do banco de dados
    private void preencherComboBoxTipos() {
        // Cria um DefaultComboBoxModel para adicionar os tipos de almoxarife
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Adiciona um item padrão
        model.addElement("Selecione");

        // Adiciona os tipos disponíveis
        model.addElement("adm");
        model.addElement("almoxarife");
        model.addElement("compras");

        // Define o modelo na ComboBox
        cBoxTipoAlmox.setModel(model);
    }

    private void remover_almoxarife() {
        conn = Conexao.getConexao();

        // Verifica se os campos obrigatórios estão vazios
        if (txtNomePainel.getText().isEmpty() || cBoxTipoAlmox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios.");
            return; // Encerra a execução da função se algum campo obrigatório estiver vazio
        }

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este usuário?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM almoxarife WHERE id_almoxarife = ?";

            try {
                pst = conn.prepareStatement(sql);

                // Verifica se há um ID do almoxarife selecionado
                if (idAlmoxarifeSelecionado == 0) {
                    JOptionPane.showMessageDialog(null, "Selecione um almoxarife válido.");
                    return; // Encerra a função se o ID não estiver definido
                }

                // Define o ID do almoxarife a ser removido no PreparedStatement
                pst.setInt(1, idAlmoxarifeSelecionado);

                int apagado = pst.executeUpdate();

                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário removido com sucesso!");

                    limpar(); // Chamando a função de limpar os campos
                    atualizarTabelas(); // Atualizando as tabelas
                    preencherComboBoxTipos(); // Atualizando a ComboBox, se necessário
                }

            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao remover o usuário: " + e.getMessage());
            } finally {
                try {
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
    }

    //=============================================================================================
    //método dos atalhos para navegação rápida
    private void configurarAtalhos() {
        adicionarAtalho(KeyEvent.VK_F, "abrirFornecedor", () -> abrirTela(telaCadFornecedor));
        adicionarAtalho(KeyEvent.VK_M, "abrirMaterial", () -> abrirTela(telaCadMaterial));
        adicionarAtalho(KeyEvent.VK_E, "abrirEntradaMov", () -> abrirTela(telaEntradaMov));
        adicionarAtalho(KeyEvent.VK_S, "abrirSaidaMov", () -> abrirTela(telaSaidaMov));
        adicionarAtalho(KeyEvent.VK_G, "abrirCategorias", () -> abrirTela(telaCadCategorias));
    }

    private void adicionarAtalho(int keyEvent, String actionKey, Runnable acao) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyEvent, KeyEvent.CTRL_DOWN_MASK);
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        inputMap.put(keyStroke, actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acao.run();
            }
        });
    }

    private void abrirTela(JPanel tela) {
        jTabbedPane2.setSelectedComponent(tela);
    }

    //=============================================================================================
    //=============================================================================================
    //outros métodos
    //Método para atualizar tabelas
    private void atualizarTabelas() {
        pesquisar_fornecedor();
        pesquisar_material();
        atualizarTabelaMovimentacoes("Todas");
        pesquisar_MovEntradas();
        pesquisar_MovSaidas();
        pesquisar_MateriaisEmMat();
        pesquisar_FornecedoresEmForn();
        pesquisar_painelAdmin();

        pesquisarCategoria(txtBuscarCategoria, tabelaMenuCategorias);
        pesquisarCategoria(txtNovaCatBuscar, tblNovaCad);
    }

    // Método para atualizar as comboboxes
    private void atualizarComboBoxes() {
        preencherComboBoxCategorias();
        preencherComboBoxTipos();
        preencherComboBoxMateriais();
        preencherComboBoxMateriais2();
    }

    //adicionar um margin nos campos de texto
    private void adicionarMargemEsquerda(JTextField campoTexto) {
        campoTexto.setBorder(BorderFactory.createCompoundBorder(
                campoTexto.getBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 0) // Margem interna de 10 pixels à esquerda
        ));
    }

    private void removerTooltips(KButton... botoes) {
        for (KButton botao : botoes) {
            botao.setToolTipText(null);
        }
    }

    public void aplicarMargin() {
        //material
        adicionarMargemEsquerda(txtBuscarEmMat);
        adicionarMargemEsquerda(txtNomeMat);
        adicionarMargemEsquerda(txtDescMat);
        adicionarMargemEsquerda(txtBuscarMat);

        //fornecedor
        adicionarMargemEsquerda(txtFornNome);
        adicionarMargemEsquerda(txtFornCnpj);
        adicionarMargemEsquerda(txtFornSite);
        adicionarMargemEsquerda(txtFornEndereco);
        adicionarMargemEsquerda(txtFornEmail);
        adicionarMargemEsquerda(txtFornFone);
        adicionarMargemEsquerda(txtFornPesquisar);
        adicionarMargemEsquerda(txtBuscarEmForn);

        //movimentações
        adicionarMargemEsquerda(txtBuscarMov);
        adicionarMargemEsquerda(txtEntradaBuscar);
        adicionarMargemEsquerda(txtEntradaQnt);
        adicionarMargemEsquerda(txtBuscarEmSaida);
        adicionarMargemEsquerda(txtSaidaQnt);

        //categoria
        adicionarMargemEsquerda(txtBuscarCategoria);
        adicionarMargemEsquerda(txtNovaCatBuscar);
        adicionarMargemEsquerda(txtNovaCatNome);

        //painel admin
        adicionarMargemEsquerda(txtPainelAdmin);
        adicionarMargemEsquerda(txtNomePainel);
        adicionarMargemEsquerda(txtLoginPainel);
        adicionarMargemEsquerda(txtSenhaPainel);
    }

    // Função para ativar ou desativar kButtons com cores personalizadas
    public void configurarEstadoKButton(com.k33ptoo.components.KButton botao, boolean ativo) {
        botao.setEnabled(ativo);
        // Define as cores de fundo e de hover com base no estado do botão
        if (ativo) {
            botao.setkBackGroundColor(new Color(26, 131, 43)); // Cor ativa (verde)
            botao.setkHoverColor(new Color(0, 180, 50));       // Cor de hover ativa (verde claro)
        } else {
            botao.setkBackGroundColor(new Color(128, 128, 128)); // Cor inativa (cinza)
            botao.setkHoverColor(new Color(128, 128, 128));      // Cor de hover inativa (cinza)
        }
    }

    // Nova função para estilizar o botão com ícone à esquerda e texto ao lado, alinhados à esquerda
    private void estilizarBotaoLateral(KButton btn, String nome, String caminhoImagem) {
        btn.setPreferredSize(new Dimension(200, 75)); // Ajusta o tamanho para o novo estilo
        btn.setLayout(new BorderLayout()); // Usar BorderLayout para posicionar o ícone e o texto

        // Carrega a imagem como recurso
        URL iconeURL = getClass().getResource(caminhoImagem);
        if (iconeURL != null) {
            ImageIcon icon = new ImageIcon(iconeURL);
            Image image = icon.getImage();
            Image newImage = image.getScaledInstance(25, 25, Image.SCALE_SMOOTH); // Redimensiona o ícone
            ImageIcon newIcon = new ImageIcon(newImage);

            // Define o ícone e o texto
            JLabel labelIcon = new JLabel(newIcon);
            JLabel textLabel = new JLabel(nome);
            textLabel.setForeground(Color.WHITE); // Define a cor do texto como branco

            // Adiciona o ícone e o texto ao botão
            JPanel panel = new JPanel(); // Usar um JPanel para alinhar o ícone e o texto
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS)); // Usar BoxLayout para alinhar horizontalmente
            panel.setOpaque(false); // Para manter a transparência

            // Ajusta o alinhamento vertical do ícone e do texto
            labelIcon.setAlignmentY(JLabel.CENTER_ALIGNMENT); // Centraliza verticalmente o ícone
            textLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT); // Centraliza verticalmente o texto

            panel.add(labelIcon);
            panel.add(Box.createRigidArea(new Dimension(5, 0))); // Espaço entre o ícone e o texto
            panel.add(textLabel);

            btn.add(panel, BorderLayout.WEST); // Adiciona o painel ao lado esquerdo do botão

            // Configurações de estilo
            btn.setBackground(new Color(24, 140, 91)); // Cor de fundo
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            textLabel.setFont(new Font("Roboto", Font.BOLD, 16));
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
        } else {
            System.err.println("Ícone não encontrado: " + caminhoImagem);
        }
    }

    // função de estilo das tabelas
    private void estilizarTabela(JTable tabela) {
        // Estilização do cabeçalho da tabela
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240)); // Fundo cinza claro para o cabeçalho
        header.setForeground(Color.BLACK); // Texto preto no cabeçalho
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(26, 131, 43))); // Linha inferior verde escuro

        // Estilização das células
        tabela.setFont(new Font("Arial", Font.PLAIN, 12));
        tabela.setBackground(Color.WHITE); // Fundo branco nas células
        tabela.setForeground(Color.BLACK); // Texto preto nas células
        tabela.setGridColor(new Color(220, 220, 220)); // Cor cinza claro para as grades da tabela
        tabela.setRowHeight(25); // Altura das linhas

        // Definir renderizador para as linhas
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(26, 131, 43)); // Fundo verde ao selecionar
                    c.setForeground(Color.WHITE); // Texto branco ao selecionar
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
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
                    c.setBackground(new Color(223, 223, 223)); // Cor de fundo normal
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

    //setar o icone mão nos botões
    private void btnEdit() {
        //MENU LATERAL
        btnMateriais.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFornecedores.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCategorias.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMovimentacoes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPainelAdmin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //TELA CADASTROS
        //FORNECEDOR
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnNovaCatSalvar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovaCatAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovaCatExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //MATERIAL
        btnCadastrarMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterarMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluirMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //TELA MOVIMENTAÇÕES
        //MOVIMENTAÇÕES
        btnEntrada.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSaida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cBoxTipoMov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //ENTRADA
        btnSalvarEntrada.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparEntradas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cBoxMatEntrada.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //SAÍDA
        btnSalvarSaida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparSaidas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cBoxMatSaida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //TELA MATERIAIS | FORNECEDORES | CATEGORIAS
        btnLimparEmMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovoMaterialEmMat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnLimparEmForn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovoFornecedorEmForn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNovaCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //PAINEL ADMIN
        cBoxTipoAlmox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparPainel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAlterarPainel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluirPainel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuarioPainel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //AJUDA
        ajudaCad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ajudaMov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ajudaVerCad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ajudaRelat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    }

    //método para limpar os campos
    private void limpar() {
        //tela fornecedor
        txtFornNome.setText(null);
        txtFornCnpj.setText(null);
        txtFornEmail.setText(null);
        txtFornFone.setText(null);
        txtFornEndereco.setText(null);
        txtFornSite.setText(null);
        txtFornPesquisar.setText(null);
        ((DefaultTableModel) tblFornecedores.getModel()).setRowCount(0);
        configurarEstadoKButton(btnAdicionar, true);
        configurarEstadoKButton(btnAlterar, false);
        configurarEstadoKButton(btnExcluir, false);

        //tela material
        cBoxIdCat.setSelectedIndex(1);
        txtNomeMat.setText(null);
        txtDescMat.setText(null);
        txtBuscarMat.setText(null);
        ((DefaultTableModel) tblMaterial.getModel()).setRowCount(0);
        configurarEstadoKButton(btnCadastrarMat, true);
        configurarEstadoKButton(btnAlterarMat, false);
        configurarEstadoKButton(btnExcluirMat, false);

        //tela Nova Categoria
        txtNovaCatNome.setText(null);
        txtNovaCatBuscar.setText(null);
        ((DefaultTableModel) tblNovaCad.getModel()).setRowCount(0);
        configurarEstadoKButton(btnNovaCatSalvar, true);
        configurarEstadoKButton(btnNovaCatAlterar, false);
        configurarEstadoKButton(btnNovaCatExcluir, false);

        //tela Movimentações
        txtBuscarMov.setText(null);
        cBoxTipoMov.setSelectedItem("Todas");
        ((DefaultTableModel) tblMovimentacoes.getModel()).setRowCount(0);

        //Tela Movimentações > Entradas
        cBoxMatEntrada.setSelectedIndex(0);
        txtEntradaQnt.setText(null);
        txtEntradaBuscar.setText(null);
        ((DefaultTableModel) tblEntrada.getModel()).setRowCount(0);

        //Tela Movimentações > Saídas
        cBoxMatSaida.setSelectedIndex(0);
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
        ((DefaultTableModel) tabelaMenuCategorias.getModel()).setRowCount(0);

        //Painel Admin
        txtPainelAdmin.setText(null);
        cBoxTipoAlmox.setSelectedIndex(1);
        txtNomePainel.setText(null);
        txtLoginPainel.setText(null);
        txtSenhaPainel.setText(null);
        ((DefaultTableModel) tblPainelAdmin.getModel()).setRowCount(0);
        configurarEstadoKButton(btnAlterarPainel, false);
        configurarEstadoKButton(btnExcluirPainel, false);
    }

    // Método para estilizar um botão com um nome e uma imagem específicos
    private void estilizarBotao(KButton btn, String nome, String caminhoImagem) {
        btn.setPreferredSize(new Dimension(200, 175));
        btn.setLayout(new BorderLayout());

        // Carrega a imagem como recurso
        URL iconeURL = getClass().getResource(caminhoImagem);
        if (iconeURL != null) {
            ImageIcon icon = new ImageIcon(iconeURL);
            Image image = icon.getImage();
            Image newImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(newImage);

            JLabel label = new JLabel("", newIcon, JLabel.CENTER);
            btn.add(label, BorderLayout.CENTER);

            JLabel textLabel = new JLabel(nome, JLabel.CENTER);
            textLabel.setForeground(new Color(26, 131, 43));
            textLabel.setVerticalAlignment(JLabel.TOP);
            textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            btn.add(textLabel, BorderLayout.SOUTH);

            btn.setBackground(new Color(24, 140, 91));
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            textLabel.setFont(new Font("Calibri", Font.BOLD, 28));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(7, 108, 65));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(24, 140, 91));
                }
            });
        } else {
            System.err.println("Ícone não encontrado: " + caminhoImagem);
        }
    }

    // Método para estilizar um botão pequeno com um nome e uma imagem específicos
    private void estilizarBotaoPequeno(KButton btn, String nome, String caminhoImagem) {
        btn.setPreferredSize(new Dimension(200, 50)); // Altura máxima definida para 55
        btn.setLayout(new BorderLayout());

        // Carrega a imagem como recurso
        URL iconeURL = getClass().getResource(caminhoImagem);
        if (iconeURL != null) {
            ImageIcon icon = new ImageIcon(iconeURL);
            Image image = icon.getImage();
            Image newImage = image.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(newImage);

            JLabel label = new JLabel("", newIcon, JLabel.CENTER);
            btn.add(label, BorderLayout.WEST);

            JLabel textLabel = new JLabel(nome, JLabel.CENTER);
            textLabel.setForeground(Color.WHITE); // Cor da letra em branco
            textLabel.setVerticalAlignment(JLabel.CENTER);
            textLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Espaçamento interno
            btn.add(textLabel, BorderLayout.CENTER);

            btn.setBackground(new Color(26, 131, 43)); // Verde do botão
            btn.setBorderPainted(false);
            textLabel.setFont(new Font("Calibri", Font.BOLD, 16));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(7, 108, 65)); // Verde mais escuro ao passar o mouse
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(26, 131, 43)); // Cor original ao sair
                }
            });
        } else {
            System.err.println("Ícone não encontrado: " + caminhoImagem);
        }
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

        jTabbedPane2 = new javax.swing.JTabbedPane();
        telaInicial = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
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
        btnLimpar = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        btnHome14 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        telaCadMaterial = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btnFechar3 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txtNomeMat = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDescMat = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        btnCadastrarMat = new com.k33ptoo.components.KButton();
        btnAlterarMat = new com.k33ptoo.components.KButton();
        btnExcluirMat = new com.k33ptoo.components.KButton();
        btnLimparMat = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        btnHome16 = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        cBoxIdCat = new javax.swing.JComboBox<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblMaterial = new javax.swing.JTable();
        txtBuscarMat = new javax.swing.JTextField();
        telaMenuMovimentacoes = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblMovimentacoes = new javax.swing.JTable();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        txtBuscarMov = new javax.swing.JTextField();
        cBoxTipoMov = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        btnHome21 = new javax.swing.JButton();
        btnEntrada = new com.k33ptoo.components.KButton();
        btnSaida = new com.k33ptoo.components.KButton();
        telaEntradaMov = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        btnSalvarEntrada = new com.k33ptoo.components.KButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblEntrada = new javax.swing.JTable();
        jLabel38 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        txtEntradaQnt = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtEntradaBuscar = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        btnLimparEntradas = new com.k33ptoo.components.KButton();
        btnHome22 = new javax.swing.JButton();
        btnFechar4 = new javax.swing.JButton();
        cBoxMatEntrada = new javax.swing.JComboBox<>();
        telaSaidaMov = new javax.swing.JPanel();
        btnSalvarSaida = new com.k33ptoo.components.KButton();
        jLabel41 = new javax.swing.JLabel();
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
        cBoxMatSaida = new javax.swing.JComboBox<>();
        telaMateriais = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblMateriaisEmMat = new javax.swing.JTable();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        txtBuscarEmMat = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        btnLimparEmMat = new com.k33ptoo.components.KButton();
        btnNovoMaterialEmMat = new com.k33ptoo.components.KButton();
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
        jLabel64 = new javax.swing.JLabel();
        btnHome25 = new javax.swing.JButton();
        telaCategorias = new javax.swing.JPanel();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        txtBuscarCategoria = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        btnLimparCategoria = new com.k33ptoo.components.KButton();
        btnNovaCategoria = new com.k33ptoo.components.KButton();
        jLabel65 = new javax.swing.JLabel();
        btnHome26 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaMenuCategorias = new javax.swing.JTable();
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
        painelAdmin = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblPainelAdmin = new javax.swing.JTable();
        jSeparator18 = new javax.swing.JSeparator();
        jSeparator19 = new javax.swing.JSeparator();
        txtPainelAdmin = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        btnAlterarPainel = new com.k33ptoo.components.KButton();
        jLabel67 = new javax.swing.JLabel();
        ops5 = new javax.swing.JButton();
        cBoxTipoAlmox = new javax.swing.JComboBox<>();
        txtNomePainel = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        txtLoginPainel = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        txtSenhaPainel = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        btnExcluirPainel = new com.k33ptoo.components.KButton();
        jLabel72 = new javax.swing.JLabel();
        btnLimparPainel = new javax.swing.JButton();
        btnUsuarioPainel = new com.k33ptoo.components.KButton();
        telaCadCategorias = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tblNovaCad = new javax.swing.JTable();
        jSeparator20 = new javax.swing.JSeparator();
        jSeparator21 = new javax.swing.JSeparator();
        txtNovaCatBuscar = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        btnNovaCatSalvar = new com.k33ptoo.components.KButton();
        jLabel73 = new javax.swing.JLabel();
        ops6 = new javax.swing.JButton();
        txtNovaCatNome = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        btnNovaCatAlterar = new com.k33ptoo.components.KButton();
        btnNovaCatExcluir = new com.k33ptoo.components.KButton();
        btnFechar1 = new javax.swing.JButton();
        btnLimparNovaCat = new javax.swing.JButton();
        menuLateral = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnMateriais = new com.k33ptoo.components.KButton();
        btnFornecedores = new com.k33ptoo.components.KButton();
        btnCategorias = new com.k33ptoo.components.KButton();
        btnMovimentacoes = new com.k33ptoo.components.KButton();
        btnPainelAdmin = new com.k33ptoo.components.KButton();
        btnSairApp = new com.k33ptoo.components.KButton();
        lblUsuario = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("StockSync");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(900, 550));
        setResizable(false);
        setSize(new java.awt.Dimension(1280, 720));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        telaInicial.setBackground(new java.awt.Color(217, 217, 217));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/STOCKSYNCIMG2.png"))); // NOI18N

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 212, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout telaInicialLayout = new javax.swing.GroupLayout(telaInicial);
        telaInicial.setLayout(telaInicialLayout);
        telaInicialLayout.setHorizontalGroup(
            telaInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaInicialLayout.createSequentialGroup()
                .addGroup(telaInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaInicialLayout.createSequentialGroup()
                        .addGap(282, 282, 282)
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaInicialLayout.createSequentialGroup()
                        .addGap(338, 338, 338)
                        .addComponent(jLabel4)))
                .addGap(372, 372, 372))
        );
        telaInicialLayout.setVerticalGroup(
            telaInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaInicialLayout.createSequentialGroup()
                .addContainerGap(149, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98)
                .addComponent(jLabel4)
                .addGap(328, 328, 328))
        );

        jTabbedPane2.addTab("tab1", telaInicial);

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

        btnAdicionar.setText("Salvar");
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
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addComponent(btnHome14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(txtFornEndereco, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtFornNome, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addComponent(jLabel12))
                                        .addGap(18, 18, 18)
                                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(txtFornEmail, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtFornCnpj, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                                            .addComponent(jLabel13))
                                        .addGap(18, 18, 18)
                                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel11)
                                            .addComponent(txtFornSite, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                            .addComponent(jLabel14)
                                            .addComponent(txtFornFone)))
                                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                        .addComponent(jLabel51)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel37)
                                        .addGap(33, 33, 33))))
                            .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnFechar)
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtFornPesquisar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, 0)
                                        .addComponent(btnLimpar))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(114, 114, 114)
                        .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(114, 114, 114)
                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        telaCadFornecedorLayout.setVerticalGroup(
            telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addContainerGap(49, Short.MAX_VALUE)
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel37))
                            .addComponent(btnHome14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(btnFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 36, Short.MAX_VALUE)))
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, 0)
                                .addComponent(txtFornNome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(0, 0, 0)
                                .addComponent(txtFornCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(0, 0, 0)
                                .addComponent(txtFornSite, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadFornecedorLayout.createSequentialGroup()
                                            .addComponent(jLabel12)
                                            .addGap(30, 30, 30))
                                        .addComponent(txtFornEndereco, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadFornecedorLayout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addGap(30, 30, 30))))
                            .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(txtFornFone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(telaCadFornecedorLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(0, 0, 0)
                        .addComponent(txtFornEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addGap(0, 2, Short.MAX_VALUE)
                .addGroup(telaCadFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFornPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab2", telaCadFornecedor);

        telaCadMaterial.setBackground(new java.awt.Color(217, 217, 217));
        telaCadMaterial.setPreferredSize(new java.awt.Dimension(1110, 695));

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

        jLabel18.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(26, 131, 43));
        jLabel18.setText("ID Categoria *");

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

        btnCadastrarMat.setText("Salvar");
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

        cBoxIdCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione uma categoria", "Item 2", "Item 3", "Item 4" }));

        tblMateriaisEmMat = new javax.swing.JTable(){
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
        jScrollPane12.setViewportView(tblMaterial);

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

        javax.swing.GroupLayout telaCadMaterialLayout = new javax.swing.GroupLayout(telaCadMaterial);
        telaCadMaterial.setLayout(telaCadMaterialLayout);
        telaCadMaterialLayout.setHorizontalGroup(
            telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadMaterialLayout.createSequentialGroup()
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnFechar3)
                            .addGroup(telaCadMaterialLayout.createSequentialGroup()
                                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                                        .addComponent(txtBuscarMat)
                                        .addGap(0, 0, 0)
                                        .addComponent(btnLimparMat))
                                    .addComponent(jLabel24)
                                    .addComponent(btnHome16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(768, 768, 768))
                            .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1043, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addGap(161, 161, 161)
                        .addComponent(btnCadastrarMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(114, 114, 114)
                        .addComponent(btnAlterarMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(119, 119, 119)
                        .addComponent(btnExcluirMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(57, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaCadMaterialLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel44))
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaCadMaterialLayout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtNomeMat))
                        .addGap(18, 18, 18)
                        .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(txtDescMat, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cBoxIdCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(84, 84, 84))
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
                .addGap(18, 18, 18)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(telaCadMaterialLayout.createSequentialGroup()
                            .addComponent(jLabel19)
                            .addGap(0, 0, 0)
                            .addComponent(txtNomeMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(telaCadMaterialLayout.createSequentialGroup()
                            .addComponent(jLabel20)
                            .addGap(0, 0, 0)
                            .addComponent(txtDescMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaCadMaterialLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(cBoxIdCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel18))
                .addGap(45, 45, 45)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCadastrarMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlterarMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcluirMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jLabel24)
                .addGap(0, 0, 0)
                .addGroup(telaCadMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimparMat)
                    .addComponent(txtBuscarMat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab2", telaCadMaterial);

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

        btnEntrada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEntrada.setkAllowGradient(false);
        btnEntrada.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnEntrada.setkBorderRadius(20);
        btnEntrada.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnEntrada.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntradaActionPerformed(evt);
            }
        });

        btnSaida.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaida.setkAllowGradient(false);
        btnSaida.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnSaida.setkBorderRadius(20);
        btnSaida.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnSaida.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnSaida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaidaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaMenuMovimentacoesLayout = new javax.swing.GroupLayout(telaMenuMovimentacoes);
        telaMenuMovimentacoes.setLayout(telaMenuMovimentacoesLayout);
        telaMenuMovimentacoesLayout.setHorizontalGroup(
            telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3)
            .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 1088, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                        .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(txtBuscarMov, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cBoxTipoMov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34))
                        .addGap(27, 27, 27)
                        .addComponent(btnEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(btnSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                        .addComponent(btnHome21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1043, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addGap(0, 0, 0)
                        .addComponent(cBoxTipoMov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(telaMenuMovimentacoesLayout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(0, 3, Short.MAX_VALUE)
                        .addComponent(txtBuscarMov, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaMenuMovimentacoesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(telaMenuMovimentacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSaida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEntrada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab7", telaMenuMovimentacoes);

        telaEntradaMov.setBackground(new java.awt.Color(217, 217, 217));

        jLabel36.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(26, 131, 43));
        jLabel36.setText("> NOVA ENTRADA");

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
        jLabel38.setText("Material");

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

        cBoxMatEntrada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione um Material", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout telaEntradaMovLayout = new javax.swing.GroupLayout(telaEntradaMov);
        telaEntradaMov.setLayout(telaEntradaMovLayout);
        telaEntradaMovLayout.setHorizontalGroup(
            telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaEntradaMovLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFechar4)
                .addGap(53, 53, 53))
            .addGroup(telaEntradaMovLayout.createSequentialGroup()
                .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                        .addComponent(jSeparator7)
                        .addGroup(telaEntradaMovLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1043, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaEntradaMovLayout.createSequentialGroup()
                                .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel39)
                                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                                        .addComponent(txtEntradaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnLimparEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(50, 50, 50)
                                .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cBoxMatEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel38))
                                .addGap(50, 50, 50)
                                .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel40)
                                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                                        .addComponent(txtEntradaQnt, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnSalvarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(telaEntradaMovLayout.createSequentialGroup()
                                .addComponent(btnHome22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel36)))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        telaEntradaMovLayout.setVerticalGroup(
            telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaEntradaMovLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btnFechar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHome22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                        .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEntradaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimparEntradas, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cBoxMatEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                        .addGap(27, 27, 27))
                    .addGroup(telaEntradaMovLayout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(0, 0, 0)
                        .addGroup(telaEntradaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEntradaQnt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSalvarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
        jLabel41.setText("Material");

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
        jLabel62.setText("> NOVA SAÍDA");

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

        cBoxMatSaida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione um Material", "Item 2", "Item 3", "Item 4" }));

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
                            .addGap(10, 10, 10)
                            .addComponent(btnHome23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel62)
                            .addGap(10, 10, 10)))
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaSaidaMovLayout.createSequentialGroup()
                                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel43)
                                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                                        .addComponent(txtBuscarEmSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnLimparSaidas, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(50, 50, 50)
                                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41)
                                    .addComponent(cBoxMatSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel42)
                                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                                        .addComponent(txtSaidaQnt, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnSalvarSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 1041, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 15, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, telaSaidaMovLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFechar5)
                .addGap(50, 50, 50))
        );
        telaSaidaMovLayout.setVerticalGroup(
            telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaSaidaMovLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btnFechar5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHome23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(telaSaidaMovLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnSalvarSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(telaSaidaMovLayout.createSequentialGroup()
                                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel43)
                                    .addComponent(jLabel41))
                                .addGap(0, 0, 0)
                                .addGroup(telaSaidaMovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtBuscarEmSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnLimparSaidas, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cBoxMatSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(telaSaidaMovLayout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(1, 1, 1)
                        .addComponent(txtSaidaQnt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
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
                            .addGap(10, 10, 10)
                            .addGroup(telaMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1042, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(telaMateriaisLayout.createSequentialGroup()
                                    .addComponent(btnHome24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel63)))))
                    .addGroup(telaMateriaisLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(telaMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47)
                            .addGroup(telaMateriaisLayout.createSequentialGroup()
                                .addComponent(txtBuscarEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimparEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(49, 49, 49)
                        .addComponent(btnNovoMaterialEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addComponent(btnNovoMaterialEmMat, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                            .addGap(10, 10, 10)
                            .addGroup(telaFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 1044, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(telaFornecedoresLayout.createSequentialGroup()
                                    .addComponent(btnHome25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel64)))))
                    .addGroup(telaFornecedoresLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(telaFornecedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel48)
                            .addGroup(telaFornecedoresLayout.createSequentialGroup()
                                .addComponent(txtBuscarEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLimparEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(49, 49, 49)
                        .addComponent(btnNovoFornecedorEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addComponent(btnNovoFornecedorEmForn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );

        jTabbedPane2.addTab("tab8", telaFornecedores);

        telaCategorias.setBackground(new java.awt.Color(217, 217, 217));

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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarCategoriaKeyPressed(evt);
            }
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

        tabelaMenuCategorias = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tabelaMenuCategorias.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tabelaMenuCategorias);

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
                            .addGap(10, 10, 10)
                            .addComponent(btnHome26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel65)
                            .addGap(909, 909, 909)))
                    .addGroup(telaCategoriasLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(telaCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1041, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(telaCategoriasLayout.createSequentialGroup()
                                .addGroup(telaCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel49)
                                    .addGroup(telaCategoriasLayout.createSequentialGroup()
                                        .addComponent(txtBuscarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnLimparCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(49, 49, 49)
                                .addComponent(btnNovaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab8", telaCategorias);

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

        painelAdmin.setBackground(new java.awt.Color(217, 217, 217));

        tblPainelAdmin = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblPainelAdmin.setModel(new javax.swing.table.DefaultTableModel(
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
        tblPainelAdmin.getTableHeader().setReorderingAllowed(false);
        tblPainelAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPainelAdminMouseClicked(evt);
            }
        });
        tblPainelAdmin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPainelAdminKeyReleased(evt);
            }
        });
        jScrollPane13.setViewportView(tblPainelAdmin);

        txtPainelAdmin.setBackground(new java.awt.Color(223, 223, 223));
        txtPainelAdmin.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtPainelAdmin.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtPainelAdmin.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtPainelAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPainelAdminActionPerformed(evt);
            }
        });
        txtPainelAdmin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPainelAdminKeyReleased(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(26, 131, 43));
        jLabel50.setText("Buscar");

        btnAlterarPainel.setText("Alterar");
        btnAlterarPainel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAlterarPainel.setkAllowGradient(false);
        btnAlterarPainel.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnAlterarPainel.setkBorderRadius(20);
        btnAlterarPainel.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnAlterarPainel.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnAlterarPainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarPainelActionPerformed(evt);
            }
        });

        jLabel67.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(26, 131, 43));
        jLabel67.setText("> PAINEL ADMIN");

        ops5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        ops5.setBorderPainted(false);
        ops5.setContentAreaFilled(false);
        ops5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ops5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ops5ActionPerformed(evt);
            }
        });

        cBoxTipoAlmox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tipo", "Item 2", "Item 3", "Item 4" }));

        txtNomePainel.setBackground(new java.awt.Color(223, 223, 223));
        txtNomePainel.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtNomePainel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtNomePainel.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtNomePainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomePainelActionPerformed(evt);
            }
        });
        txtNomePainel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomePainelKeyReleased(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(26, 131, 43));
        jLabel69.setText("Nome");

        txtLoginPainel.setBackground(new java.awt.Color(223, 223, 223));
        txtLoginPainel.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtLoginPainel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtLoginPainel.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtLoginPainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoginPainelActionPerformed(evt);
            }
        });
        txtLoginPainel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLoginPainelKeyReleased(evt);
            }
        });

        jLabel70.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(26, 131, 43));
        jLabel70.setText("Login");

        txtSenhaPainel.setBackground(new java.awt.Color(223, 223, 223));
        txtSenhaPainel.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtSenhaPainel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtSenhaPainel.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtSenhaPainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSenhaPainelActionPerformed(evt);
            }
        });
        txtSenhaPainel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSenhaPainelKeyReleased(evt);
            }
        });

        jLabel71.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(26, 131, 43));
        jLabel71.setText("Senha");

        btnExcluirPainel.setText("Excluir");
        btnExcluirPainel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnExcluirPainel.setkAllowGradient(false);
        btnExcluirPainel.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnExcluirPainel.setkBorderRadius(20);
        btnExcluirPainel.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnExcluirPainel.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnExcluirPainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirPainelActionPerformed(evt);
            }
        });

        jLabel72.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(26, 131, 43));
        jLabel72.setText("Tipo");

        btnLimparPainel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimparPainel.setForeground(new java.awt.Color(26, 131, 43));
        btnLimparPainel.setText("Limpar");
        btnLimparPainel.setContentAreaFilled(false);
        btnLimparPainel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparPainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparPainelActionPerformed(evt);
            }
        });

        btnUsuarioPainel.setText("Novo Usuário");
        btnUsuarioPainel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnUsuarioPainel.setkAllowGradient(false);
        btnUsuarioPainel.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnUsuarioPainel.setkBorderRadius(20);
        btnUsuarioPainel.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnUsuarioPainel.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnUsuarioPainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuarioPainelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painelAdminLayout = new javax.swing.GroupLayout(painelAdmin);
        painelAdmin.setLayout(painelAdminLayout);
        painelAdminLayout.setHorizontalGroup(
            painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelAdminLayout.createSequentialGroup()
                .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator18, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                        .addComponent(jSeparator19)
                        .addGroup(painelAdminLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(painelAdminLayout.createSequentialGroup()
                                    .addComponent(ops5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel67))
                                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 1047, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(painelAdminLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelAdminLayout.createSequentialGroup()
                                .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel69)
                                    .addComponent(txtNomePainel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel70)
                                    .addComponent(txtLoginPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel71)
                                    .addComponent(txtSenhaPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel72)
                                    .addComponent(cBoxTipoAlmox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addComponent(btnAlterarPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnExcluirPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnUsuarioPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelAdminLayout.createSequentialGroup()
                                .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel50)
                                    .addComponent(txtPainelAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addComponent(btnLimparPainel)))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        painelAdminLayout.setVerticalGroup(
            painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelAdminLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ops5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(painelAdminLayout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addGap(0, 0, 0)
                        .addComponent(txtNomePainel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(painelAdminLayout.createSequentialGroup()
                        .addComponent(jLabel70)
                        .addGap(0, 0, 0)
                        .addComponent(txtLoginPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(painelAdminLayout.createSequentialGroup()
                        .addComponent(jLabel71)
                        .addGap(0, 0, 0)
                        .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSenhaPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAlterarPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnExcluirPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnUsuarioPainel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(painelAdminLayout.createSequentialGroup()
                        .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtPainelAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimparPainel)
                            .addGroup(painelAdminLayout.createSequentialGroup()
                                .addComponent(jLabel50)
                                .addGap(30, 30, 30)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addGroup(painelAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cBoxTipoAlmox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(painelAdminLayout.createSequentialGroup()
                                .addComponent(jLabel72)
                                .addGap(30, 30, 30)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        jTabbedPane2.addTab("tab8", painelAdmin);

        telaCadCategorias.setBackground(new java.awt.Color(217, 217, 217));

        tblNovaCad = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblNovaCad.setModel(new javax.swing.table.DefaultTableModel(
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
        tblNovaCad.getTableHeader().setReorderingAllowed(false);
        tblNovaCad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNovaCadMouseClicked(evt);
            }
        });
        tblNovaCad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblNovaCadKeyReleased(evt);
            }
        });
        jScrollPane14.setViewportView(tblNovaCad);

        txtNovaCatBuscar.setBackground(new java.awt.Color(223, 223, 223));
        txtNovaCatBuscar.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtNovaCatBuscar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtNovaCatBuscar.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtNovaCatBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNovaCatBuscarActionPerformed(evt);
            }
        });
        txtNovaCatBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNovaCatBuscarKeyReleased(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(26, 131, 43));
        jLabel66.setText("Buscar");

        btnNovaCatSalvar.setText("Salvar");
        btnNovaCatSalvar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNovaCatSalvar.setkAllowGradient(false);
        btnNovaCatSalvar.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnNovaCatSalvar.setkBorderRadius(20);
        btnNovaCatSalvar.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnNovaCatSalvar.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnNovaCatSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovaCatSalvarActionPerformed(evt);
            }
        });

        jLabel73.setFont(new java.awt.Font("Calibri", 1, 20)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(26, 131, 43));
        jLabel73.setText("> NOVA CATEGORIA");

        ops6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/btnHome.png"))); // NOI18N
        ops6.setBorderPainted(false);
        ops6.setContentAreaFilled(false);
        ops6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ops6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ops6ActionPerformed(evt);
            }
        });

        txtNovaCatNome.setBackground(new java.awt.Color(223, 223, 223));
        txtNovaCatNome.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        txtNovaCatNome.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(176, 176, 176), 1, true));
        txtNovaCatNome.setSelectionColor(new java.awt.Color(26, 131, 43));
        txtNovaCatNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNovaCatNomeActionPerformed(evt);
            }
        });
        txtNovaCatNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNovaCatNomeKeyReleased(evt);
            }
        });

        jLabel75.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(26, 131, 43));
        jLabel75.setText("Nome da Categoria*");

        btnNovaCatAlterar.setText("Alterar");
        btnNovaCatAlterar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNovaCatAlterar.setkAllowGradient(false);
        btnNovaCatAlterar.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnNovaCatAlterar.setkBorderRadius(20);
        btnNovaCatAlterar.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnNovaCatAlterar.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnNovaCatAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovaCatAlterarActionPerformed(evt);
            }
        });

        btnNovaCatExcluir.setText("Excluir");
        btnNovaCatExcluir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNovaCatExcluir.setkAllowGradient(false);
        btnNovaCatExcluir.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnNovaCatExcluir.setkBorderRadius(20);
        btnNovaCatExcluir.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnNovaCatExcluir.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        btnNovaCatExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovaCatExcluirActionPerformed(evt);
            }
        });

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

        btnLimparNovaCat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimparNovaCat.setForeground(new java.awt.Color(26, 131, 43));
        btnLimparNovaCat.setText("Limpar");
        btnLimparNovaCat.setContentAreaFilled(false);
        btnLimparNovaCat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimparNovaCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparNovaCatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout telaCadCategoriasLayout = new javax.swing.GroupLayout(telaCadCategorias);
        telaCadCategorias.setLayout(telaCadCategoriasLayout);
        telaCadCategoriasLayout.setHorizontalGroup(
            telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadCategoriasLayout.createSequentialGroup()
                .addGroup(telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadCategoriasLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel66)
                            .addGroup(telaCadCategoriasLayout.createSequentialGroup()
                                .addComponent(txtNovaCatBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnLimparNovaCat))))
                    .addGroup(telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator20, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                        .addComponent(jSeparator21)
                        .addGroup(telaCadCategoriasLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(telaCadCategoriasLayout.createSequentialGroup()
                                    .addGroup(telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 1044, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(telaCadCategoriasLayout.createSequentialGroup()
                                            .addGroup(telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel75)
                                                .addComponent(txtNovaCatNome, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(80, 80, 80)
                                            .addComponent(btnNovaCatSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(30, 30, 30)
                                            .addComponent(btnNovaCatAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(30, 30, 30)
                                            .addComponent(btnNovaCatExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE))
                                .addGroup(telaCadCategoriasLayout.createSequentialGroup()
                                    .addComponent(ops6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel73)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnFechar1)
                                    .addGap(41, 41, 41))))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        telaCadCategoriasLayout.setVerticalGroup(
            telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(telaCadCategoriasLayout.createSequentialGroup()
                .addGroup(telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(telaCadCategoriasLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ops6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(telaCadCategoriasLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(btnFechar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel66)
                .addGap(0, 0, 0)
                .addGroup(telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNovaCatBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimparNovaCat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator21, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel75)
                .addGap(0, 0, 0)
                .addGroup(telaCadCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNovaCatNome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNovaCatSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNovaCatAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNovaCatExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        jTabbedPane2.addTab("tab8", telaCadCategorias);

        getContentPane().add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, -50, 1110, 750));

        menuLateral.setBackground(new java.awt.Color(26, 131, 43));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/teste/icones/STOCKSYNCIMG.png"))); // NOI18N

        btnMateriais.setForeground(new java.awt.Color(0, 0, 0));
        btnMateriais.setToolTipText("");
        btnMateriais.setAlignmentY(0.0F);
        btnMateriais.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnMateriais.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMateriais.setkAllowGradient(false);
        btnMateriais.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnMateriais.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnMateriais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMateriaisActionPerformed(evt);
            }
        });

        btnFornecedores.setForeground(new java.awt.Color(0, 0, 0));
        btnFornecedores.setToolTipText("");
        btnFornecedores.setAlignmentY(0.0F);
        btnFornecedores.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnFornecedores.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFornecedores.setkAllowGradient(false);
        btnFornecedores.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnFornecedores.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnFornecedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFornecedoresActionPerformed(evt);
            }
        });

        btnCategorias.setForeground(new java.awt.Color(0, 0, 0));
        btnCategorias.setToolTipText("");
        btnCategorias.setAlignmentY(0.0F);
        btnCategorias.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnCategorias.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCategorias.setkAllowGradient(false);
        btnCategorias.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnCategorias.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCategoriasActionPerformed(evt);
            }
        });

        btnMovimentacoes.setForeground(new java.awt.Color(0, 0, 0));
        btnMovimentacoes.setToolTipText("");
        btnMovimentacoes.setAlignmentY(0.0F);
        btnMovimentacoes.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnMovimentacoes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMovimentacoes.setkAllowGradient(false);
        btnMovimentacoes.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnMovimentacoes.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnMovimentacoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovimentacoesActionPerformed(evt);
            }
        });

        btnPainelAdmin.setForeground(new java.awt.Color(0, 0, 0));
        btnPainelAdmin.setToolTipText("");
        btnPainelAdmin.setAlignmentY(0.0F);
        btnPainelAdmin.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnPainelAdmin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPainelAdmin.setkAllowGradient(false);
        btnPainelAdmin.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnPainelAdmin.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnPainelAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPainelAdminActionPerformed(evt);
            }
        });

        btnSairApp.setForeground(new java.awt.Color(0, 0, 0));
        btnSairApp.setToolTipText("");
        btnSairApp.setAlignmentY(0.0F);
        btnSairApp.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        btnSairApp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSairApp.setkAllowGradient(false);
        btnSairApp.setkBackGroundColor(new java.awt.Color(26, 131, 43));
        btnSairApp.setkHoverColor(new java.awt.Color(52, 153, 68));
        btnSairApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairAppActionPerformed(evt);
            }
        });

        lblUsuario.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(255, 255, 255));
        lblUsuario.setText("jLabel5");

        javax.swing.GroupLayout menuLateralLayout = new javax.swing.GroupLayout(menuLateral);
        menuLateral.setLayout(menuLateralLayout);
        menuLateralLayout.setHorizontalGroup(
            menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnMateriais, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addComponent(btnFornecedores, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addComponent(btnCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addComponent(btnMovimentacoes, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addComponent(btnPainelAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addComponent(btnSairApp, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(menuLateralLayout.createSequentialGroup()
                .addGroup(menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuLateralLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(menuLateralLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUsuario))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        menuLateralLayout.setVerticalGroup(
            menuLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLateralLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(btnFornecedores, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(btnCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(btnMovimentacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(btnPainelAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 334, Short.MAX_VALUE)
                .addComponent(lblUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnSairApp, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        getContentPane().add(menuLateral, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 700));

        setSize(new java.awt.Dimension(1259, 712));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaFornecedores);
        limpar();
        atualizarTabelas();
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnFechar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar3ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaMateriais);
        btnCadastrarMat.setEnabled(true);
        btnCadastrarMat.setkBackGroundColor(new Color(26, 131, 43));
        btnCadastrarMat.setkHoverColor(new Color(52, 153, 68));
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
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

    private void txtNomeMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeMatActionPerformed

    private void txtDescMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescMatActionPerformed

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

    private void btnLimparMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparMatActionPerformed
        // TODO add your handling code here:
        limpar();
        btnCadastrarMat.setEnabled(true);
        btnCadastrarMat.setkBackGroundColor(new Color(26, 131, 43));
        btnCadastrarMat.setkHoverColor(new Color(52, 153, 68));
        //atualizar as tabelas
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnLimparMatActionPerformed

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

    private void btnSalvarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarEntradaActionPerformed
        // TODO add your handling code here:
        novaEntrada();
    }//GEN-LAST:event_btnSalvarEntradaActionPerformed

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
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnNovoMaterialEmMatActionPerformed

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
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnNovoFornecedorEmFornActionPerformed

    private void txtBuscarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarCategoriaActionPerformed

    private void txtBuscarCategoriaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCategoriaKeyReleased
        // TODO add your handling code here:
        // No Menu Categoria
        pesquisarCategoria(txtBuscarCategoria, tabelaMenuCategorias);
    }//GEN-LAST:event_txtBuscarCategoriaKeyReleased

    private void btnLimparCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCategoriaActionPerformed
        // TODO add your handling code here:
        limpar();
        atualizarTabelas();
    }//GEN-LAST:event_btnLimparCategoriaActionPerformed

    private void btnNovaCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovaCategoriaActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCadCategorias);
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnNovaCategoriaActionPerformed

    private void txtBuscarEmSaidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarEmSaidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarEmSaidaActionPerformed

    private void txtBuscarEmSaidaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmSaidaKeyReleased
        // TODO add your handling code here:
        pesquisar_MovSaidas();
    }//GEN-LAST:event_txtBuscarEmSaidaKeyReleased

    private void btnHome8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome8ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_btnHome8ActionPerformed

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

    private void btnHome14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome14ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
        limpar();
    }//GEN-LAST:event_btnHome14ActionPerformed

    private void btnHome16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome16ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
        limpar();
    }//GEN-LAST:event_btnHome16ActionPerformed

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

    private void btnFechar4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar4ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaMenuMovimentacoes);
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnFechar4ActionPerformed

    private void btnFechar5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar5ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaMenuMovimentacoes);
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnFechar5ActionPerformed

    private void btnMateriaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMateriaisActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaMateriais);
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnMateriaisActionPerformed

    private void btnFornecedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFornecedoresActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaFornecedores);
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnFornecedoresActionPerformed

    private void btnCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCategoriasActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCategorias);
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnCategoriasActionPerformed

    private void btnMovimentacoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovimentacoesActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaMenuMovimentacoes);
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnMovimentacoesActionPerformed

    private void btnPainelAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPainelAdminActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(painelAdmin);
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnPainelAdminActionPerformed

    private void btnSairAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairAppActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSairAppActionPerformed

    private void tblNovaCadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNovaCadMouseClicked
        // TODO add your handling code here:
        setar_camposCategoria();
    }//GEN-LAST:event_tblNovaCadMouseClicked

    private void tblNovaCadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblNovaCadKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblNovaCadKeyReleased

    private void txtNovaCatBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNovaCatBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNovaCatBuscarActionPerformed

    private void txtNovaCatBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNovaCatBuscarKeyReleased
        // TODO add your handling code here:
        // Na Tela Nova Categoria
        pesquisarCategoria(txtNovaCatBuscar, tblNovaCad);
    }//GEN-LAST:event_txtNovaCatBuscarKeyReleased

    private void btnNovaCatSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovaCatSalvarActionPerformed
        // TODO add your handling code here:
        cadastrarCategoria();
    }//GEN-LAST:event_btnNovaCatSalvarActionPerformed

    private void ops6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ops6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ops6ActionPerformed

    private void txtNovaCatNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNovaCatNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNovaCatNomeActionPerformed

    private void txtNovaCatNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNovaCatNomeKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtNovaCatNomeKeyReleased

    private void btnNovaCatAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovaCatAlterarActionPerformed
        // TODO add your handling code here:
        alterarCategoria();
    }//GEN-LAST:event_btnNovaCatAlterarActionPerformed

    private void btnLimparPainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparPainelActionPerformed
        // TODO add your handling code here:
        limpar();
        atualizarTabelas();
        atualizarComboBoxes();
    }//GEN-LAST:event_btnLimparPainelActionPerformed

    private void btnExcluirPainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirPainelActionPerformed
        // TODO add your handling code here:
        remover_almoxarife();
    }//GEN-LAST:event_btnExcluirPainelActionPerformed

    private void txtSenhaPainelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSenhaPainelKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSenhaPainelKeyReleased

    private void txtSenhaPainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSenhaPainelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSenhaPainelActionPerformed

    private void txtLoginPainelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLoginPainelKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoginPainelKeyReleased

    private void txtLoginPainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoginPainelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoginPainelActionPerformed

    private void txtNomePainelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomePainelKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomePainelKeyReleased

    private void txtNomePainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomePainelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomePainelActionPerformed

    private void ops5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ops5ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaInicial);
    }//GEN-LAST:event_ops5ActionPerformed

    private void btnAlterarPainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarPainelActionPerformed
        // TODO add your handling code here:
        alterar_almoxarife();
    }//GEN-LAST:event_btnAlterarPainelActionPerformed

    private void txtPainelAdminKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPainelAdminKeyReleased
        // TODO add your handling code here:
        pesquisar_painelAdmin();
    }//GEN-LAST:event_txtPainelAdminKeyReleased

    private void txtPainelAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPainelAdminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPainelAdminActionPerformed

    private void tblPainelAdminKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPainelAdminKeyReleased
        // TODO add your handling code here:
        pesquisar_painelAdmin();
    }//GEN-LAST:event_tblPainelAdminKeyReleased

    private void tblPainelAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPainelAdminMouseClicked
        // TODO add your handling code here:
        setar_camposAlmoxarife();
    }//GEN-LAST:event_tblPainelAdminMouseClicked

    private void btnNovaCatExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovaCatExcluirActionPerformed
        // TODO add your handling code here:
        remover_categoria();
    }//GEN-LAST:event_btnNovaCatExcluirActionPerformed

    private void btnFechar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFechar1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaCategorias);
        limpar();
        atualizarTabelas();
    }//GEN-LAST:event_btnFechar1ActionPerformed

    private void btnLimparNovaCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparNovaCatActionPerformed
        // TODO add your handling code here:
        btnNovaCatSalvar.setEnabled(true);
        btnNovaCatSalvar.setkBackGroundColor(new Color(26, 131, 43));
        btnNovaCatSalvar.setkHoverColor(new Color(52, 153, 68));
        limpar();
        atualizarTabelas();
    }//GEN-LAST:event_btnLimparNovaCatActionPerformed

    private void txtBuscarMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarMatActionPerformed

    private void txtBuscarMatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarMatKeyReleased
        // TODO add your handling code here:
        pesquisar_material();
    }//GEN-LAST:event_txtBuscarMatKeyReleased

    private void tblMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMaterialMouseClicked
        // TODO add your handling code here:
        setar_camposMaterial();
    }//GEN-LAST:event_tblMaterialMouseClicked

    private void txtBuscarCategoriaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCategoriaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarCategoriaKeyPressed

    private void btnUsuarioPainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuarioPainelActionPerformed
        // TODO add your handling code here:
        telaCadastro popUpCad = new telaCadastro();
        popUpCad.setVisible(true);
    }//GEN-LAST:event_btnUsuarioPainelActionPerformed

    private void btnEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntradaActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaEntradaMov);
    }//GEN-LAST:event_btnEntradaActionPerformed

    private void btnSaidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaidaActionPerformed
        // TODO add your handling code here:
        jTabbedPane2.setSelectedComponent(telaSaidaMov);
    }//GEN-LAST:event_btnSaidaActionPerformed

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
    private com.k33ptoo.components.KButton btnAlterar;
    private com.k33ptoo.components.KButton btnAlterarMat;
    private com.k33ptoo.components.KButton btnAlterarPainel;
    private com.k33ptoo.components.KButton btnCadastrarMat;
    private com.k33ptoo.components.KButton btnCategorias;
    private com.k33ptoo.components.KButton btnEntrada;
    private com.k33ptoo.components.KButton btnExcluir;
    private com.k33ptoo.components.KButton btnExcluirMat;
    private com.k33ptoo.components.KButton btnExcluirPainel;
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnFechar1;
    private javax.swing.JButton btnFechar3;
    private javax.swing.JButton btnFechar4;
    private javax.swing.JButton btnFechar5;
    private com.k33ptoo.components.KButton btnFornecedores;
    private javax.swing.JButton btnHome12;
    private javax.swing.JButton btnHome14;
    private javax.swing.JButton btnHome16;
    private javax.swing.JButton btnHome18;
    private javax.swing.JButton btnHome19;
    private javax.swing.JButton btnHome20;
    private javax.swing.JButton btnHome21;
    private javax.swing.JButton btnHome22;
    private javax.swing.JButton btnHome23;
    private javax.swing.JButton btnHome24;
    private javax.swing.JButton btnHome25;
    private javax.swing.JButton btnHome26;
    private javax.swing.JButton btnHome8;
    private javax.swing.JButton btnLimpar;
    private com.k33ptoo.components.KButton btnLimparCategoria;
    private com.k33ptoo.components.KButton btnLimparEmForn;
    private com.k33ptoo.components.KButton btnLimparEmMat;
    private com.k33ptoo.components.KButton btnLimparEntradas;
    private javax.swing.JButton btnLimparMat;
    private javax.swing.JButton btnLimparNovaCat;
    private javax.swing.JButton btnLimparPainel;
    private com.k33ptoo.components.KButton btnLimparSaidas;
    private com.k33ptoo.components.KButton btnMateriais;
    private com.k33ptoo.components.KButton btnMovimentacoes;
    private com.k33ptoo.components.KButton btnNovaCatAlterar;
    private com.k33ptoo.components.KButton btnNovaCatExcluir;
    private com.k33ptoo.components.KButton btnNovaCatSalvar;
    private com.k33ptoo.components.KButton btnNovaCategoria;
    private com.k33ptoo.components.KButton btnNovoFornecedorEmForn;
    private com.k33ptoo.components.KButton btnNovoMaterialEmMat;
    private com.k33ptoo.components.KButton btnPainelAdmin;
    private com.k33ptoo.components.KButton btnSaida;
    private com.k33ptoo.components.KButton btnSairApp;
    private com.k33ptoo.components.KButton btnSalvarEntrada;
    private com.k33ptoo.components.KButton btnSalvarSaida;
    private com.k33ptoo.components.KButton btnUsuarioPainel;
    private javax.swing.JComboBox<String> cBoxIdCat;
    private javax.swing.JComboBox<String> cBoxMatEntrada;
    private javax.swing.JComboBox<String> cBoxMatSaida;
    private javax.swing.JComboBox<String> cBoxTipoAlmox;
    private javax.swing.JComboBox<String> cBoxTipoMov;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
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
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane2;
    public static javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel menuLateral;
    private javax.swing.JButton ops5;
    private javax.swing.JButton ops6;
    private javax.swing.JPanel painelAdmin;
    private javax.swing.JTable tabelaMenuCategorias;
    private javax.swing.JTable tabelaSaidas;
    private javax.swing.JTable tblEntrada;
    private javax.swing.JTable tblFornecedores;
    private javax.swing.JTable tblFornecedoresEmForn;
    private javax.swing.JTable tblMateriaisEmMat;
    private javax.swing.JTable tblMaterial;
    private javax.swing.JTable tblMovimentacoes;
    private javax.swing.JTable tblNovaCad;
    private javax.swing.JTable tblPainelAdmin;
    private javax.swing.JPanel telaAjuda;
    private javax.swing.JPanel telaAjuda1;
    private javax.swing.JPanel telaAjuda2;
    private javax.swing.JPanel telaAjuda3;
    private javax.swing.JPanel telaAjuda4;
    private javax.swing.JPanel telaCadCategorias;
    private javax.swing.JPanel telaCadFornecedor;
    private javax.swing.JPanel telaCadMaterial;
    private javax.swing.JPanel telaCategorias;
    private javax.swing.JPanel telaEntradaMov;
    private javax.swing.JPanel telaFornecedores;
    private javax.swing.JPanel telaInicial;
    private javax.swing.JPanel telaMateriais;
    private javax.swing.JPanel telaMenuMovimentacoes;
    private javax.swing.JPanel telaSaidaMov;
    private javax.swing.JTextField txtBuscarCategoria;
    private javax.swing.JTextField txtBuscarEmForn;
    private javax.swing.JTextField txtBuscarEmMat;
    private javax.swing.JTextField txtBuscarEmSaida;
    private javax.swing.JTextField txtBuscarMat;
    private javax.swing.JTextField txtBuscarMov;
    private javax.swing.JTextField txtDescMat;
    private javax.swing.JTextField txtEntradaBuscar;
    private javax.swing.JTextField txtEntradaQnt;
    private javax.swing.JTextField txtFornCnpj;
    private javax.swing.JTextField txtFornEmail;
    private javax.swing.JTextField txtFornEndereco;
    private javax.swing.JTextField txtFornFone;
    private javax.swing.JTextField txtFornNome;
    private javax.swing.JTextField txtFornPesquisar;
    private javax.swing.JTextField txtFornSite;
    private javax.swing.JTextField txtLoginPainel;
    private javax.swing.JTextField txtNomeMat;
    private javax.swing.JTextField txtNomePainel;
    private javax.swing.JTextField txtNovaCatBuscar;
    private javax.swing.JTextField txtNovaCatNome;
    private javax.swing.JTextField txtPainelAdmin;
    private javax.swing.JTextField txtSaidaQnt;
    private javax.swing.JTextField txtSenhaPainel;
    // End of variables declaration//GEN-END:variables
}
