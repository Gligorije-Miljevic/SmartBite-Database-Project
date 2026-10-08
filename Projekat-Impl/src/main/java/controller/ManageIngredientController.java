package controller;

import database.DatabaseConn;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import service.AdminService;
import session.UserSession;

import java.sql.Connection;
import java.sql.DriverManager;

public class ManageIngredientController {

    @FXML
    private TextField idField;

    @FXML
    private TextArea obrazlozenjeField;

    private Connection conn;

    public ManageIngredientController() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            conn = DriverManager.getConnection(url, "root", "K@nj4321");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ukloni() {
        try {
            AdminService.ukloniSastojak(conn, Integer.parseInt(idField.getText()), UserSession.korisnik.id, obrazlozenjeField.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sastojak uklonjen!");
            alert.show();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    public void aktiviraj() {
        try {
            AdminService.aktivirajSastojak(conn, Integer.parseInt(idField.getText()), UserSession.korisnik.id, obrazlozenjeField.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sastojak aktiviran!");
            alert.show();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}