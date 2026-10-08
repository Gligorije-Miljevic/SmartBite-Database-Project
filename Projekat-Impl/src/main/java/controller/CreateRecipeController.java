package controller;

import dao.ReceptDAO;
import database.DatabaseConn;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Recept;
import session.UserSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import dao.SastojakDAO;
import javafx.scene.control.ListView;
import session.UserSession;

public class CreateRecipeController {

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextArea instructionArea;

    @FXML
    private TextArea ingredientsArea;

    @FXML
    private TextField searchIngredientField;

    @FXML
    private ListView<String> ingredientList;

    @FXML
    private TextField quantityField;

    @FXML
    private Label messageLabel;

    private Connection conn;

    public CreateRecipeController() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            conn = DriverManager.getConnection(url, "root", "K@nj4321");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveRecipe() {
        try {
            Recept recept = new Recept();
            recept.naziv = nameField.getText();
            recept.opis = descriptionArea.getText();
            recept.detaljnoUputstvo = instructionArea.getText();
            Map<Integer, Double> mapa = new HashMap<>();
            String[] lines = ingredientsArea.getText().split("\n");
            for(String line : lines) {
                String[] parts = line.split(":");
                int id = Integer.parseInt(parts[0]);
                double kol = Double.parseDouble(parts[1]);
                mapa.put(id, kol);
            }
            ReceptDAO.sacuvajRecept(recept, conn, mapa, UserSession.korisnik.id);
            messageLabel.setText("Recept uspjesno sacuvan!");
        } catch(Exception e) {
            e.printStackTrace();
            messageLabel.setText("Greska!");
        }
    }
    @FXML
    public void pretragaSastojaka() {
        try {
            ingredientList.getItems().clear();
            Map<Integer, String> sastojci = SastojakDAO.pregledSastojaka(conn, searchIngredientField.getText());
            for(Map.Entry<Integer, String> entry : sastojci.entrySet()) {
                ingredientList.getItems().add(entry.getKey() + " - " + entry.getValue());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void dodajSastojak() {
        try {
            String selected = ingredientList.getSelectionModel().getSelectedItem();
            if(selected == null) {
                return;
            }
            String id = selected.split("-")[0].trim();
            ingredientsArea.appendText(id + ":" + quantityField.getText() + "\n");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}