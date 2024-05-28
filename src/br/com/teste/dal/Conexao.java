package br.com.teste.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/bancosimples";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    private static Connection conn;

    public static Connection getConexao() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Registra a exceção para depuração
        }
        return conn;
    }
}
