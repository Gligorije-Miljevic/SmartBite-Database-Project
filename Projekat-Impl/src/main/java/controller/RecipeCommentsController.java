package controller;

import dao.KomentarDAO;
import database.DatabaseConn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Komentar;
import model.Recept;
import service.ReceptService;
import session.UserSession;
import javafx.scene.control.TextInputDialog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import javafx.scene.control.TextInputDialog;
import service.ReceptService;
import service.KomentarService;

public class RecipeCommentsController {

    @FXML
    private Label recipeTitle;

    @FXML
    private ListView<Komentar> commentList;

    @FXML
    private TextField commentField;

    private Connection conn;

    private Recept recept;

    private int roditeljskiKomentar = -1;

    @FXML
    public void initialize() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            conn = DriverManager.getConnection(url, "root", "K@nj4321");
            commentList.setOnMouseClicked(e -> {
                if(e.getClickCount() == 2) {
                    Komentar komentar = commentList.getSelectionModel().getSelectedItem();
                    if(komentar != null) {
                        otvoriPodkomentare(komentar);
                    }
                }
            });

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setRecept(Recept recept) {
        this.recept = recept;
        recipeTitle.setText("Komentari - " + recept.naziv);
        ucitajKomentare();
    }
    public void setRoditeljskiKomentar(int roditeljskiKomentar) {
        this.roditeljskiKomentar = roditeljskiKomentar;
    }

    public void ucitajKomentare() {
        try {
            ArrayList<Komentar> listaKomentara = new ArrayList<>();
            if(roditeljskiKomentar == -1) {
                KomentarDAO.prikaziSveKomentare(conn, recept.id);
                listaKomentara.addAll(KomentarDAO.komentari);
            } else {
                String sql = "select k.*, kor.korisnickoIme from komentar k " +
                                "join korisnik kor on k.klijent_id_korisnika = kor.id_korisnika " +
                                "where komentar_id_komentara = ? limit 10";
                java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, roditeljskiKomentar);
                java.sql.ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    Komentar k = new Komentar();
                    k.id = rs.getInt("id_komentara");
                    k.komentar = rs.getString("komentar");
                    k.korisnickoIme = rs.getString("korisnickoIme");
                    listaKomentara.add(k);
                }
            }
            ObservableList<Komentar> observableList = FXCollections.observableArrayList(listaKomentara);
            commentList.setItems(observableList);
        }catch(Exception ex) {
            recipeTitle.setText(ex.getMessage());
        }
    }

    @FXML
    public void dodajKomentar() {
        try {
            String komentar = commentField.getText();
            if(komentar.isEmpty()) {
                return;
            }
            KomentarDAO.unesiKomentar(conn, komentar, UserSession.korisnik.id, recept.id, roditeljskiKomentar);
            commentField.clear();
            ucitajKomentare();
        } catch(Exception ex) {
            recipeTitle.setText(ex.getMessage());
        }
    }

    public void otvoriPodkomentare(Komentar komentar) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/recipe_comments.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            RecipeCommentsController controller = loader.getController();
            controller.setRecept(recept);
            controller.setRoditeljskiKomentar(komentar.id);
            controller.ucitajKomentare();
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Podkomentari");
            stage.show();
        } catch(Exception ex) {
            recipeTitle.setText(ex.getMessage());
        }
    }
    @FXML
    public void sacuvajRecept() {
        try {
            int rezultat = ReceptService.sacuvajRecept(conn, recept.id, UserSession.korisnik.id);
            if(rezultat == 1) {
                recipeTitle.setText("Recept sacuvan!");
            } else {
                recipeTitle.setText("Recept je vec sacuvan!");
            }
        } catch(Exception ex) {
            recipeTitle.setText(ex.getMessage());
        }
    }
    @FXML
    public void ocijeniRecept() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ocjena recepta");
            dialog.setHeaderText("Unesi ocjenu 1-5");
            dialog.showAndWait().ifPresent(value -> {
                try {
                    int ocjena = Integer.parseInt(value);
                    if(ocjena < 1 || ocjena > 5) {
                        return;
                    }
                    int rezultat = ReceptService.ocijeniRecept(conn, recept.id, UserSession.korisnik.id, ocjena);
                    if(rezultat == 1) {
                        recipeTitle.setText("Recept ocijenjen!");
                    } else if(rezultat == 2) {
                        ReceptService.updateOcjena(conn, recept.id, UserSession.korisnik.id, ocjena);
                        recipeTitle.setText("Ocjena azurirana!");
                    }
                } catch(Exception ex) {
                    recipeTitle.setText(ex.getMessage());
                }
            });
        } catch(Exception ex) {
            recipeTitle.setText(ex.getMessage());
        }
    }
    @FXML
    public void prijaviKomentar() {
        try {
            Komentar komentar = commentList.getSelectionModel().getSelectedItem();
            if(komentar == null) {
                return;
            }
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Prijava komentara");
            dialog.setHeaderText("Unesite razlog prijave");
            dialog.showAndWait().ifPresent(razlog -> {
                KomentarService.prijavaKomentara(conn, UserSession.korisnik.id, komentar.id, razlog);
                recipeTitle.setText("Komentar prijavljen!");
            });
        } catch(Exception ex) {
            recipeTitle.setText(ex.getMessage());
        }
    }
}