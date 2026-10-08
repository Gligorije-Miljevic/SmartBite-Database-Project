package controller;

import database.DatabaseConn;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import session.UserSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NutritivniController {

    @FXML
    private Label kalorijeLabel;

    @FXML
    private Label proteiniLabel;

    @FXML
    private Label uhLabel;

    @FXML
    private Label mastiLabel;

    @FXML
    public void initialize() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            Connection conn = DriverManager.getConnection(url, "root", "K@nj4321");
            String sql = "select * from nutritivni_podaci " +
                            "where klijent_id_korisnika = ? " +
                            "order by datum desc limit 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, UserSession.korisnik.id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                kalorijeLabel.setText("Kalorije: " + rs.getDouble("kalorije"));
                proteiniLabel.setText("Proteini: " + rs.getDouble("proteini"));
                uhLabel.setText("Ugljeni hidrati: " + rs.getDouble("ugljeni_hidrati"));
                mastiLabel.setText("Masti: " + rs.getDouble("masti"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}