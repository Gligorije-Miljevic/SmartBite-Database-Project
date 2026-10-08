package model;

import java.time.LocalDate;

public class Klijent extends Korisnik {

    public int starost;
    public double visina;
    public double tezina;
    public String nivoFizickeAktivnosti;
    public double ciljanaTezina;
    public String pol;
    public LocalDate datumPostizanjaCiljaneTezine;

    public Klijent() {}
    public Klijent(int id, String ime, String prezime, String email, String korisnickoIme, int starost, double visina,String pol,
                   double tezina, String nivoFizickeAktivnosti, double ciljanaTezina, LocalDate datumPostizanjaCiljaneTezine) {
        super(id, ime, prezime, email, korisnickoIme);
        this.starost = starost;
        this.visina = visina;
        this.pol=pol;
        this.tezina = tezina;
        this.nivoFizickeAktivnosti = nivoFizickeAktivnosti;
        this.ciljanaTezina = ciljanaTezina;
        this.datumPostizanjaCiljaneTezine = datumPostizanjaCiljaneTezine;
    }
    @Override
    public String toString() {
        return "Klijent{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", email='" + email + '\'' +
                ", korisnickoIme='" + korisnickoIme + '\'' +
                ", starost=" + starost +
                ", visina=" + visina +
                ", tezina=" + tezina +
                ", nivoFizickeAktivnosti='" + nivoFizickeAktivnosti + '\'' +
                ", ciljanaTezina=" + ciljanaTezina +
                ", datumPostizanjaCiljaneTezine=" + datumPostizanjaCiljaneTezine +
                '}';
    }
}