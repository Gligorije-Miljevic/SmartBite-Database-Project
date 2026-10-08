package model;

public class Sastojak {
    public int id;
    public String naziv;
    public int status;
    public int administrator;

    public Sastojak() {}
    public Sastojak(int id, String naziv, int status, int administrator) {
        this.id = id;
        this.naziv = naziv;
        this.status = status;
        this.administrator = administrator;
    }
    @Override
    public String toString() {
        return "Sastojak{" +
                id +
                "}, Naziv " + naziv ;
    }
}