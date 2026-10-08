package model;

public class Administrator extends Korisnik {

    public Administrator() {}
    public Administrator(int id, String ime, String prezime, String email, String korisnickoIme) {
        super(id, ime, prezime, email, korisnickoIme);
    }
    @Override
    public String toString() {
        return "Administrator{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", email='" + email + '\'' +
                ", korisnickoIme='" + korisnickoIme + '\'' +
                '}';
    }
}