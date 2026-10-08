package controller;

import database.DatabaseConn;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import service.AuthService;

import java.sql.Connection;
import java.sql.DriverManager;

public class RegisterController {

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
    private TextField passwordField;

    @FXML
    private TextField starostField;

    @FXML
    private TextField polField;

    @FXML
    private TextField visinaField;

    @FXML
    private TextField tezinaField;

    @FXML
    private TextField aktivnostField;

    @FXML
    private TextField ciljField;

    @FXML
    private TextField datumField;

    @FXML
    private Label messageLabel;

    @FXML
    public void registracija() {

        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            Connection conn = DriverManager.getConnection(url, "root", "K@nj4321");
            boolean uspjeh = AuthService.registracija(
                            conn,
                            imeField.getText(),
                            prezimeField.getText(),
                            emailField.getText(),
                            telefonField.getText(),
                            usernameField.getText(),
                            passwordField.getText(),
                            starostField.getText(),
                            polField.getText(),
                            visinaField.getText(),
                            tezinaField.getText(),
                            aktivnostField.getText(),
                            ciljField.getText(),
                            datumField.getText()
                    );
            if(uspjeh) {
                messageLabel.setText("Uspjesna registracija!");
            } else {
                messageLabel.setText("Korisnicko ime ili email vec postoji!");
            }
        } catch(Exception e) {
            e.printStackTrace();
            messageLabel.setText("Greska!");
        }
    }
}