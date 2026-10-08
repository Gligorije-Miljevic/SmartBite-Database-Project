package controller;

import dao.ReceptDAO;
import database.DatabaseConn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Recept;
import dao.ReceptDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import session.UserSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import session.UserSession;
import javafx.scene.input.MouseButton;

public class ClientController {

    @FXML
    private TableView<Recept> recipeTable;

    @FXML
    private TableColumn<Recept, Integer> idCol;

    @FXML
    private TextField receptIDField;

    @FXML
    private TableColumn<Recept, String> nazivCol;

    @FXML
    private TableColumn<Recept, String> opisCol;

    @FXML
    private TextField searchField;

    private Connection conn;

    @FXML
    private TableColumn<Recept, String> statusCol;

    @FXML
    private TableColumn<Recept, Double> ocjenaCol;

    @FXML
    private TableColumn<Recept, String> sastojciCol;

    @FXML
    public void pocetna() {

        ucitajRandomRecepte();
    }

    @FXML
    public void initialize() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            conn = DriverManager.getConnection(url, "root", "K@nj4321");
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nazivCol.setCellValueFactory(new PropertyValueFactory<>("naziv"));
            opisCol.setCellValueFactory(new PropertyValueFactory<>("opis"));
            sastojciCol.setCellValueFactory(new PropertyValueFactory<>("sastojciTekst"));
            ocjenaCol.setCellValueFactory(new PropertyValueFactory<>("prosjecnaOcjena"));
            statusCol.setCellValueFactory(new PropertyValueFactory<>("statusTekst"));
            ucitajRandomRecepte();
            recipeTable.setOnMouseClicked(e -> {
                if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                    Recept recept = recipeTable.getSelectionModel().getSelectedItem();
                    if(recept != null) {
                        otvoriKomentare(recept);
                    }
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void ucitajSveRecepte() {
        try {
            ArrayList<Recept> recepti = ReceptDAO.pregled(conn, "");
            ObservableList<Recept> lista = FXCollections.observableArrayList(recepti);
            recipeTable.setItems(lista);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void ucitajRandomRecepte() {
        try {
            ArrayList<Recept> recepti = ReceptDAO.getRandomRecepte(conn);
            ObservableList<Recept> lista = FXCollections.observableArrayList(recepti);
            recipeTable.setItems(lista);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void mojiRecepti() {
        try {
            ArrayList<Recept> recepti = ReceptDAO.getMojiRecepti(conn, UserSession.korisnik.id);
            ObservableList<Recept> lista = FXCollections.observableArrayList(recepti);
            recipeTable.setItems(lista);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void sacuvaniRecepti() {
        try {
            ArrayList<Recept> recepti = ReceptDAO.getSacuvaniRecepti(conn, UserSession.korisnik.id);
            ObservableList<Recept> lista = FXCollections.observableArrayList(recepti);
            recipeTable.setItems(lista);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void pretragaRecepata() {
        try {
            ArrayList<Recept> recepti = ReceptDAO.pregled(conn, searchField.getText());
            ObservableList<Recept> lista = FXCollections.observableArrayList(recepti);
            recipeTable.setItems(lista);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void kreirajRecept() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/create_recipe.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setWidth(600);
            stage.setHeight(700);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void nutritivni() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/nutritivni.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Nutritivni podaci");
            stage.setWidth(400);
            stage.setHeight(300);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void otvoriKomentare(Recept recept) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/recipe_comments.fxml"));
            Scene scene = new Scene(loader.load());
            RecipeCommentsController controller = loader.getController();
            controller.setRecept(recept);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Komentari");
            stage.setWidth(600);
            stage.setHeight(500);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void mojProfil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/profil.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Moj profil");
            stage.setWidth(400);
            stage.setHeight(500);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void ukloniRecept() {
        try {
            int idRecepta = Integer.parseInt(receptIDField.getText());
            ReceptDAO.ukloniSvojRecept(conn, idRecepta, UserSession.korisnik.id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Recept uklonjen!");
            alert.show();
            mojiRecepti();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    public void aktivirajRecept() {
        try {
            int idRecepta = Integer.parseInt(receptIDField.getText());
            ReceptDAO.aktivirajSvojRecept(conn, idRecepta, UserSession.korisnik.id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Recept aktiviran!");
            alert.show();
            mojiRecepti();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
}