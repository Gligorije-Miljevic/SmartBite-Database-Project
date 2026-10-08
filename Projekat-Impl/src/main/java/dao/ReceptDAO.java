package dao;

import model.Recept;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import static dao.SastojakDAO.dodajSastojke;

public class ReceptDAO {
    public static Scanner sc = new Scanner(System.in);
    /*public static void ispisiRandomRecepte(Connection conn)
    {
        try {

            String sql =
                    "select r.id_recepta, r.naziv, " +
                            "s.naziv as sastojak, " +
                            "rs.kolicina, " +
                            "s.kalorije, " +
                            "s.proteini, " +
                            "s.ugljeni_hidrati, " +
                            "s.masti " +
                            "from (select * from recept where status = 1 order by rand() limit 10) r " +
                            "join recept_sadrzi_sastojak rs " +
                            "on r.id_recepta = rs.recept_id_recepta " +
                            "join sastojak s " +
                            "on rs.sastojak_id_sastojka = s.id_sastojka " +
                            "order by r.id_recepta";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int trenutniRecept = -1;
            double ukupnoKalorija = 0;
            double ukupnoProteina = 0;
            double ukupnoUH = 0;
            double ukupnoMasti = 0;
            while(rs.next())
            {
                int idRecepta = rs.getInt("id_recepta");
                if(trenutniRecept != idRecepta)
                {
                    if(trenutniRecept != -1)
                    {
                        System.out.printf("Ukupne kalorije: %.2f",ukupnoKalorija, " Ukupno proteina: %.2f", ukupnoProteina, " Ukupno UH: %.2f", ukupnoUH, " Ukupno masti: %.2f", ukupnoMasti);
                        System.out.println("\n--------------------------------");
                    }
                    trenutniRecept = idRecepta;
                    ukupnoKalorija = 0;
                    ukupnoProteina = 0;
                    ukupnoUH = 0;
                    ukupnoMasti = 0;
                    System.out.println("RECEPT:");
                    System.out.println("ID: " + idRecepta + " Naziv: " + rs.getString("naziv")+ "| Sastojci:");
                }
                String nazivSastojka = rs.getString("sastojak");
                double kolicina = rs.getDouble("kolicina");
                double kalorije = rs.getDouble("kalorije") * kolicina;
                double proteini = rs.getDouble("proteini") * kolicina;
                double uh = rs.getDouble("ugljeni_hidrati") * kolicina;
                double masti = rs.getDouble("masti") * kolicina;
                ukupnoKalorija += kalorije;
                ukupnoProteina += proteini;
                ukupnoUH += uh;
                ukupnoMasti += masti;
                System.out.printf("- ", nazivSastojka, " | kolicina: %.2f", kolicina, " | kalorije: %.2f", kalorije);
            }
            if(trenutniRecept != -1)
            {
                System.out.printf("Ukupne kalorije: %.2f",ukupnoKalorija, " Ukupno proteina: %.2f", ukupnoProteina, " Ukupno UH: %.2f", ukupnoUH, " Ukupno masti: %.2f", ukupnoMasti);
                System.out.println();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }*/
    /*public static Recept ucitaj(Connection conn, int ID)
    {
        try {
            String sql = "select * from recept where id_recepta = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Recept recept = new Recept();
                recept.id = rs.getInt("ID_Recepta");
                recept.naziv = rs.getString("Naziv");
                recept.opis = rs.getString("Opis");
                recept.detaljnoUputstvo = rs.getString("Detaljno_uputstvo");
                Timestamp timestamp = rs.getTimestamp("Datum_kreiranja");
                if(timestamp != null) {
                    recept.datumKreiranja = timestamp.toLocalDateTime();
                }
                recept.status = rs.getInt("Status");
                return recept;
            }
        }catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }*/
    public static ArrayList<Recept> pregled(Connection conn, String unos)
    {
        ArrayList<Recept> recepti = new ArrayList<>();
        try {
            String sql = "select * from pregled_recepata " +
                            "where naziv like ? limit 5";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, unos + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Recept recept = new Recept();
                recept.id = rs.getInt("id_recepta");
                recept.naziv = rs.getString("naziv");
                recept.opis = rs.getString("opis");
                recept.detaljnoUputstvo = rs.getString("detaljno_uputstvo");
                recept.status = rs.getInt("status");
                recept.prosjecnaOcjena = rs.getDouble("prosjecna_ocjena");
                recept.setSastojciTekst(rs.getString("sastojci"));
                recepti.add(recept);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return recepti;
    }
    public static ArrayList<Recept> getMojiRecepti(Connection conn, int idKorisnika)
    {
        ArrayList<Recept> recepti = new ArrayList<>();
        try {
            String sql = "select * from pregled_recepata " +
                            "where klijent_id_korisnika = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idKorisnika);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Recept recept = new Recept();
                recept.id = rs.getInt("id_recepta");
                recept.naziv = rs.getString("naziv");
                recept.opis = rs.getString("opis");
                recept.detaljnoUputstvo = rs.getString("detaljno_uputstvo");
                recept.status = rs.getInt("status");
                recept.prosjecnaOcjena = rs.getDouble("prosjecna_ocjena");
                recept.setSastojciTekst(rs.getString("sastojci"));
                recepti.add(recept);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return recepti;
    }
    /*public static void ispisMojihRecepata(Connection conn, int ID)
    {
        try {
            String sql = "select * from recept where klijent_id_korisnika = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Recept recept = new Recept();
                recept.id = rs.getInt("ID_Recepta");
                recept.naziv = rs.getString("Naziv");
                recept.opis = rs.getString("Opis");
                recept.detaljnoUputstvo = rs.getString("Detaljno_uputstvo");
                Timestamp timestamp = rs.getTimestamp("Datum_kreiranja");
                if(timestamp != null) {
                    recept.datumKreiranja = timestamp.toLocalDateTime();
                }
                System.out.println(recept);
            }
        }catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }*/
    public static void sacuvajRecept(Recept recept, Connection conn, Map<Integer,Double> mapa,int ID)
    {
        try {
            CallableStatement ps = conn.prepareCall("{call dodaj_recept(?, ?, ?, ?)}");
            ps.setString(1, recept.naziv);
            ps.setString(2, recept.opis);
            ps.setString(3, recept.detaljnoUputstvo);
            ps.setInt(4, ID);
            ResultSet rs = ps.executeQuery();
            int idRecepta = 0;
            if(rs.next()) {
                idRecepta = rs.getInt(1);
            }
            for(Map.Entry<Integer, Double> entry : mapa.entrySet()) {
                int idSastojka = entry.getKey();
                double kolicina = entry.getValue();
                String sql2 = "insert into recept_sadrzi_sastojak " +
                                "(recept_id_recepta, sastojak_id_sastojka, kolicina) " +
                                "values (?, ?, ?)";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setInt(1, idRecepta);
                ps2.setInt(2, idSastojka);
                ps2.setDouble(3, kolicina);
                ps2.executeUpdate();
            }
            System.out.println("Recept je uspjesno sacuvan u bazu!");
        }catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    /*public static void kreiranjeRecepta(Connection conn, int ID)
    {
        Recept recept = new Recept();
        System.out.println("Unesi naziv recepta: ");
        String unos = sc.nextLine();
        recept.naziv = unos;
        System.out.println("Opis recepta: ");
        unos = sc.nextLine();
        recept.opis = unos;
        Map<Integer,Double> listaSastojaka = dodajSastojke(conn);
        System.out.println("Detaljno_uputstvo: ");
        unos = sc.nextLine();
        recept.detaljnoUputstvo = unos;
        System.out.println("Potvrdi kreiranje recepta: [DA] [NE]");
        unos = sc.nextLine();
        if(unos.equals("DA")) {
            sacuvajRecept(recept, conn, listaSastojaka,ID);
        }else if (unos.equals("NE")) {
            System.out.println("Uspjesno otkazano kreiranje recepta!");
        }
    }*/
    public static ArrayList<Recept> getRandomRecepte(Connection conn)
    {
        ArrayList<Recept> recepti = new ArrayList<>();
        try {
            String sql = "select * from pregled_recepata " +
                            "where status = 1 " +
                            "order by rand() limit 10";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Recept recept = new Recept();
                recept.id = rs.getInt("id_recepta");
                recept.naziv = rs.getString("naziv");
                recept.opis = rs.getString("opis");
                recept.detaljnoUputstvo = rs.getString("detaljno_uputstvo");
                recept.status = rs.getInt("status");
                recept.prosjecnaOcjena = rs.getDouble("prosjecna_ocjena");
                recept.setSastojciTekst(rs.getString("sastojci"));
                recepti.add(recept);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return recepti;
    }
    public static ArrayList<Recept> getSacuvaniRecepti(Connection conn, int idKorisnika)
    {
        ArrayList<Recept> recepti = new ArrayList<>();
        try {
            String sql = "select pr.* " +
                            "from pregled_recepata pr " +
                            "join korisnik_cuva_recept k " +
                            "on pr.id_recepta = k.recept_id_recepta " +
                            "where k.klijent_korisnik_id_korisnika = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idKorisnika);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Recept recept = new Recept();
                recept.id = rs.getInt("id_recepta");
                recept.naziv = rs.getString("naziv");
                recept.opis = rs.getString("opis");
                recept.detaljnoUputstvo = rs.getString("detaljno_uputstvo");
                recept.status = rs.getInt("status");
                recept.prosjecnaOcjena = rs.getDouble("prosjecna_ocjena");
                recept.setSastojciTekst(rs.getString("sastojci"));
                recepti.add(recept);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return recepti;
    }
    public static void ukloniSvojRecept(Connection conn, int receptID, int korisnikID) throws Exception {
        String sql = "update recept " +
                        "set status = 0 " +
                        "where ID_Recepta = ? " +
                        "and Klijent_ID_Korisnika = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, receptID);
        ps.setInt(2, korisnikID);
        int rezultat = ps.executeUpdate();
        if(rezultat == 0) {
            throw new Exception("Nemate pravo da uklonite ovaj recept.");
        }
    }

    public static void aktivirajSvojRecept(Connection conn, int receptID, int korisnikID) throws Exception {
        String sql = "update recept " +
                        "set status = 1 " +
                        "where ID_Recepta = ? " +
                        "and Klijent_ID_Korisnika = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, receptID);
        ps.setInt(2, korisnikID);
        int rezultat = ps.executeUpdate();
        if(rezultat == 0) {
            throw new Exception("Nemate pravo da aktivirate ovaj recept.");
        }
    }
}
