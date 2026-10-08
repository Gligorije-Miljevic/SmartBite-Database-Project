package controller;

import database.DatabaseConn;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Korisnik;
import service.AuthService;

import java.sql.Connection;
import java.sql.DriverManager;
import session.UserSession;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private Connection conn;

    public LoginController() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            conn = DriverManager.getConnection(url, "root", "K@nj4321");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loginClient() {
        try {
            Korisnik korisnik = AuthService.prijava(conn, 1,usernameField.getText(), passwordField.getText());
            if(korisnik != null) {
                UserSession.korisnik = korisnik;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/client.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
            } else {
                messageLabel.setText("Neuspjesna prijava!");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loginAdmin() {
        try {
            Korisnik admin = AuthService.prijava(conn, 2,usernameField.getText(), passwordField.getText());
            if(admin != null) {
                UserSession.korisnik = admin;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
            } else {
                messageLabel.setText("Neuspjesna prijava!");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/register.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setWidth(400);
            stage.setHeight(600);
            stage.setTitle("Registracija");
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}