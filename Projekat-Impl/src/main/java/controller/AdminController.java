package controller;

import database.DatabaseConn;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

public class AdminController {

    private Connection conn;
    public AdminController() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            conn = DriverManager.getConnection(url, "root", "K@nj4321");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void dodajSastojak() {
        otvoriProzor("add_ingredient.fxml");
    }

    @FXML
    public void komentari() {
        otvoriProzor("admin_comments.fxml");
    }

    @FXML
    public void sastojci() {
        otvoriProzor("manage_ingredient.fxml");
    }

    @FXML
    public void kreirajAdmina() {
        otvoriProzor("create_admin.fxml");
    }

    @FXML
    public void recepti() {
        otvoriProzor("remove_recipe.fxml");
    }

    @FXML
    public void korisnici() {
        otvoriProzor("remove_user.fxml");
    }

    private void otvoriProzor(String nazivFajla) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + nazivFajla));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}