package model;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Komentar {
    public int id;
    public String komentar;
    public LocalDateTime datumObjave;
    public int status;
    public int id_klijenta;
    public int id_recept;
    public int roditeljskiKomentar;
    public String korisnickoIme;
    public ArrayList<Komentar> odgovori;
    public Connection conn;
    public Komentar() {
        odgovori = new ArrayList<>();
    }
    public Komentar(int id, String komentar, LocalDateTime datumObjave, int brojLajkova,
                    int status, int id_klijenta, int id_recept, int roditeljskiKomentar, String korisnickoIme) {
        this.id = id;
        this.komentar = komentar;
        this.datumObjave = datumObjave;
        this.id_klijenta = id_klijenta;
        this.id_recept = id_recept;
        this.roditeljskiKomentar = roditeljskiKomentar;
        this.status = status;
        this.korisnickoIme = korisnickoIme;
        odgovori = new ArrayList<>();
    }
    @Override
    public String toString() {
        return  "id{" + id + "} " + korisnickoIme + " -> " + komentar;
    }
}