package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Recept {
    public int id;
    public String naziv;
    public String opis;
    public String detaljnoUputstvo;
    public LocalDateTime datumKreiranja;
    public int status;
    public Klijent klijent;
    public ArrayList<Sastojak> sastojci;
    public double prosjecnaOcjena;
    private String sastojciTekst;
    public Recept() {
        sastojci = new ArrayList<>();
    }
    public String getSastojciTekst() {
        return sastojciTekst;
    }
    public void setSastojciTekst(String sastojciTekst) {
        this.sastojciTekst = sastojciTekst;
    }
    public int getId() {
        return id;
    }
    public String getNaziv() {
        return naziv;
    }
    public String getOpis() {
        return opis;
    }
    public double getProsjecnaOcjena() {
        return prosjecnaOcjena;
    }
    public int getStatus() {
        return status;
    }
    public String getStatusTekst() {
        if(status == 1) {
            return "Aktivan";
        }
        return "Uklonjen";
    }
    @Override
    public String toString() {
        return "Recept{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                ", opis='" + opis + '\'' +
                ", detaljnoUputstvo='" + detaljnoUputstvo + '\'' +
                ", datumKreiranja=" + datumKreiranja +
                ", klijent=" + klijent +
                ", sastojci=" + sastojci +
                '}';
    }
}