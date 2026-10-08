package controller;

import dao.KorisnikDAO;
import dao.ReceptDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Klijent;
import model.Korisnik;
import session.UserSession;
import database.DatabaseConn;
import java.sql.Connection;
import java.sql.DriverManager;

public class ProfileController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField imeField;

    @FXML
    private TextField prezimeField;

    @FXML
    private TextField receptIDField;

    @FXML
    private TextField starostField;

    @FXML
    private TextField aktivnostField;

    @FXML
    private TextField ciljanaTezinaField;

    @FXML
    private TextField polField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField weightField;

    @FXML
    private Label messageLabel;
    private Connection conn;

    @FXML
    public void initialize() {
        try {
            String url = DatabaseConn.buildURL("mysql", "localhost", 3306, "prehrambeniciljeviirecepti");
            conn = DriverManager.getConnection(url, "root", "K@nj4321");
            Klijent korisnik = KorisnikDAO.ucitajKlijenta(conn, UserSession.korisnik.id);
            usernameField.setText(korisnik.korisnickoIme);
            emailField.setText(korisnik.email);
            heightField.setText(String.valueOf(korisnik.visina));
            weightField.setText(String.valueOf(korisnik.tezina));
            imeField.setText(korisnik.ime);
            prezimeField.setText(korisnik.prezime);
            starostField.setText(String.valueOf(korisnik.starost));
            aktivnostField.setText(korisnik.nivoFizickeAktivnosti);
            ciljanaTezinaField.setText(String.valueOf(korisnik.ciljanaTezina));
            polField.setText(korisnik.pol);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void omoguciIzmjenu() {
        usernameField.setEditable(true);
        emailField.setEditable(true);
        heightField.setEditable(true);
        weightField.setEditable(true);
        imeField.setEditable(true);
        prezimeField.setEditable(true);
        starostField.setEditable(true);
        aktivnostField.setEditable(true);
        ciljanaTezinaField.setEditable(true);
        polField.setEditable(true);
    }
    @FXML
    public void sacuvajPodatke() {
        try {
            Klijent klijent = KorisnikDAO.ucitajKlijenta(conn, UserSession.korisnik.id);
            boolean postoji = KorisnikDAO.postojiUsernameIliEmail(conn, usernameField.getText(), emailField.getText(), klijent.id);
            if(postoji) {
                messageLabel.setText("Username ili email vec postoje!");
                return;
            }
            klijent.korisnickoIme = usernameField.getText();
            klijent.email = emailField.getText();
            klijent.visina = Double.parseDouble(heightField.getText());
            klijent.tezina = Double.parseDouble(weightField.getText());
            klijent.ime = imeField.getText();
            klijent.prezime = prezimeField.getText();
            klijent.starost = Integer.parseInt(starostField.getText());
            klijent.nivoFizickeAktivnosti = aktivnostField.getText();
            klijent.ciljanaTezina = Double.parseDouble(ciljanaTezinaField.getText());
            klijent.pol = polField.getText();
            KorisnikDAO.azurirajKlijenta(conn, klijent);
            messageLabel.setText("Podaci sacuvani!");
        } catch(Exception e) {
            e.printStackTrace();
            messageLabel.setText("Greska!");
        }
    }
    @FXML
    public void ukloniRecept() {
        try {
            int receptID = Integer.parseInt(receptIDField.getText());
            ReceptDAO.ukloniSvojRecept(conn, receptID, UserSession.korisnik.id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Recept uklonjen!");
            alert.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void aktivirajRecept() {
        try {
            int receptID = Integer.parseInt(receptIDField.getText());
            ReceptDAO.aktivirajSvojRecept(conn, receptID, UserSession.korisnik.id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Recept aktiviran!");
            alert.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}