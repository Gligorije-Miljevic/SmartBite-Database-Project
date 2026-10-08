package model;

public abstract class Korisnik {

    public int id;
    public String ime;
    public String prezime;
    public String email;
    public String korisnickoIme;
    public String brojTelefona;

    public Korisnik() {}
    public Korisnik(int id, String ime, String prezime, String email, String korisnickoIme) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.korisnickoIme = korisnickoIme;
    }
    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", email='" + email + '\'' +
                ", korisnickoIme='" + korisnickoIme + '\'' +
                '}';
    }
}