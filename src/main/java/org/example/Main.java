package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

        public class Main {
            public static void main(String[] args) {
                System.out.println("Iniciando aplicação...");

                String url = "jdbc:mysql://localhost: <COLOCA A PORTA DO SEU BANCO DE DADOS> /  <NOME DO SEU DATA BASE>";
                String user = "??????????????";
                String password = "???????**";

                try (Connection mySqlConnect = DriverManager.getConnection(url, user, password)) {

                    // Criar a tabela "usuario" com AUTO_INCREMENT (se ainda não existir)
                    createTable(mySqlConnect);

                    // Inserir novos usuários
                    int[] usuarioIDs = {1,2,3,4,5};
                    String[] nomes = {"João", "Maria", "Lucas", "Tiago", "Lia "};
                    String[] emails = {"joao@example.com", "maria@example.com", "lucas@example.com", "tiago@example.com","lia@gmail.com "};
                    String[] enderecos = {"Rua A, 123", "Rua B, 456", "Rua D, 454", "Rua F, 457","RUA T,501 "};

                    for (int i = 0; i < usuarioIDs.length; i++) {
                        if (!usuarioExiste(mySqlConnect, usuarioIDs[i])) {
                            inserirUsuario(mySqlConnect, usuarioIDs[i], nomes[i], emails[i], enderecos[i]);
                            System.out.println("Usuário " + nomes[i] + " inserido com sucesso!");
                        } else {
                            System.out.println("Usuário com ID " + usuarioIDs[i] + " já existe. Não foi inserido.");
                        }
                    }

                    // Excluir usuários específicos
//                    int[] idParaDeletar = {2, 3, 5, 6, };
//                    for (int id : idParaDeletar) {
//                        excluirUsuario(mySqlConnect, id);
//                        System.out.println("Usuário com ID " + id + " excluído.");
//                    }

                    // Selecionar e exibir os dados da tabela "usuario"
                    selecionarUsuarios(mySqlConnect);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            private static void createTable(Connection connection) throws SQLException {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS usuario ("
                        + "usuarioID INT PRIMARY KEY AUTO_INCREMENT, "
                        + "nome VARCHAR(100), "
                        + "email VARCHAR(100), "
                        + "endereco VARCHAR(255))";
                try (PreparedStatement createTableStmt = connection.prepareStatement(createTableSQL)) {
                    createTableStmt.executeUpdate();
                }
            }

            private static boolean usuarioExiste(Connection connection, int usuarioID) throws SQLException {
                String checkSQL = "SELECT 1 FROM usuario WHERE usuarioID = ?";
                try (PreparedStatement checkStmt = connection.prepareStatement(checkSQL)) {
                    checkStmt.setInt(1, usuarioID);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        return rs.next();
                    }
                }
            }

            private static void inserirUsuario(Connection connection, int usuarioID, String nome, String email, String endereco) throws SQLException {
                String insertSQL = "INSERT INTO usuario (usuarioID, nome, email, endereco) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
                    insertStmt.setInt(1, usuarioID);
                    insertStmt.setString(2, nome);
                    insertStmt.setString(3, email);
                    insertStmt.setString(4, endereco);
                    insertStmt.executeUpdate();
                }
            }

            //EXCLUIR POR ID
            //private static void excluirUsuario(Connection connection, int usuarioID) throws SQLException {
                //String deleteSQL = "DELETE FROM usuario WHERE usuarioID = ?";
                //try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
                   // deleteStmt.setInt(1, usuarioID);
                    //deleteStmt.executeUpdate();
                //}
            //}

            //SELECIONA USUARIOS
            private static void selecionarUsuarios(Connection connection) throws SQLException {
                String selectSQL = "SELECT * FROM usuario";
                try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
                     ResultSet rs = selectStmt.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("usuarioID"));
                        System.out.println("Nome: " + rs.getString("nome"));
                        System.out.println("Email: " + rs.getString("email"));
                        System.out.println("Endereço: " + rs.getString("endereco"));
                        System.out.println("---------------");
                    }
                }
            }
        }


