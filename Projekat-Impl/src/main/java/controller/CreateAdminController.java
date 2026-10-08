package controller;

import database.DatabaseConn;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreateAdminController {

    @FXML
    private TextField imeField;

    @FXML
    private TextField prezimeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telefonField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Connection conn;

    public CreateAdminController() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            conn = DriverManager.getConnection(url, "root", "K@nj4321");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void kreiraj() {
        try {
            String provjera = "select * from korisnik " +
                            "where korisnickoime = ? or email = ?";
            PreparedStatement ps0 = conn.prepareStatement(provjera);
            ps0.setString(1, usernameField.getText());
            ps0.setString(2, emailField.getText());
            ResultSet rs0 = ps0.executeQuery();
            if(rs0.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username ili email vec postoje!");
                alert.show();
                return;
            }
            String sql = "insert into korisnik " +
                            "(ime, prezime, email, brojtelefona, korisnickoime, lozinka) " +
                            "values (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, imeField.getText());
            ps.setString(2, prezimeField.getText());
            ps.setString(3, emailField.getText());
            ps.setString(4, telefonField.getText());
            ps.setString(5, usernameField.getText());
            ps.setString(6, passwordField.getText());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if(rs.next()) {
                id = rs.getInt(1);
            }
            String sql2 = "insert into administrator(id_korisnika) values (?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, id);
            ps2.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Administrator uspjesno kreiran!");
            alert.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}