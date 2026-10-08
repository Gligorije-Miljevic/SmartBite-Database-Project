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

public class RemoveRecipeController {

    @FXML
    private TextField idField;

    @FXML
    private TextArea obrazlozenjeField;

    private Connection conn;

    public RemoveRecipeController() {
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
            int receptID =Integer.parseInt(idField.getText());
            String obrazlozenje = obrazlozenjeField.getText();
            AdminService.ukloniRecept(conn, receptID, UserSession.korisnik.id, obrazlozenje);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Recept uklonjen!");
            alert.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void aktiviraj() {
        try {
            int receptID = Integer.parseInt(idField.getText());
            String obrazlozenje = obrazlozenjeField.getText();
            AdminService.aktivirajRecept(conn, receptID, UserSession.korisnik.id, obrazlozenje);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Recept aktiviran!");
            alert.show();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}