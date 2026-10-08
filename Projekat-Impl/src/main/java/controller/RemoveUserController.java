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

public class RemoveUserController {

    @FXML
    private TextField idField;

    @FXML
    private TextArea obrazlozenjeField;

    private Connection conn;

    public RemoveUserController() {
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
            int korisnikID = Integer.parseInt(idField.getText());
            String obrazlozenje = obrazlozenjeField.getText();
            AdminService.ukloniKlijenta(conn, korisnikID, UserSession.korisnik.id, obrazlozenje);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Korisnik uklonjen!");
            alert.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void aktiviraj() {
        try {
            int korisnikID = Integer.parseInt(idField.getText());
            String obrazlozenje = obrazlozenjeField.getText();
            AdminService.aktivirajKlijenta(conn, korisnikID, UserSession.korisnik.id, obrazlozenje);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Korisnik aktiviran!");
            alert.show();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}