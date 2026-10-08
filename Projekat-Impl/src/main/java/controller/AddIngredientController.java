package controller;

import database.DatabaseConn;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import service.AdminService;
import session.UserSession;
import java.sql.Connection;
import java.sql.DriverManager;

public class AddIngredientController {

    @FXML
    private TextField nazivField;

    @FXML
    private TextField kalorijeField;

    @FXML
    private TextField proteiniField;

    @FXML
    private TextField ugljeniField;

    @FXML
    private TextField mastiField;

    @FXML
    private Label messageLabel;

    private Connection conn;

    @FXML
    public void initialize() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            conn = DriverManager.getConnection(url, "root", "K@nj4321");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void dodaj() {
        try {
            AdminService.dodajSastojak(conn, nazivField.getText(), Double.parseDouble(kalorijeField.getText()),
                    Double.parseDouble(proteiniField.getText()), Double.parseDouble(ugljeniField.getText()),
                    Double.parseDouble(mastiField.getText()), UserSession.korisnik.id);
            messageLabel.setText("Sastojak uspjesno dodan!");
        } catch(Exception e) {
            messageLabel.setText("Greška!");
            e.printStackTrace();
        }
    }
}