package controller;

import database.DatabaseConn;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Komentar;
import service.AdminService;
import session.UserSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class AdminCommentsController {

    @FXML
    private ListView<String> commentList;

    @FXML
    private TextField commentIdField;

    @FXML
    private TextField reasonField;

    private Connection conn;

    @FXML
    public void initialize() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            conn = DriverManager.getConnection(url, "root", "K@nj4321");
            ucitajKomentare();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void ucitajKomentare() {
        try {
            ArrayList<String> komentari = AdminService.topPrijavljeniKomentari(conn, 5);
            commentList.setItems(FXCollections.observableArrayList(komentari));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void ukloniKomentar() {
        try {
            AdminService.ukloniKomentar(conn, Integer.parseInt(commentIdField.getText()),
                    UserSession.korisnik.id, reasonField.getText());
            ucitajKomentare();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void aktivirajKomentar() {
        try {
            AdminService.aktivirajKomentar(conn, Integer.parseInt(commentIdField.getText()),
                    UserSession.korisnik.id, reasonField.getText());
            ucitajKomentare();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}