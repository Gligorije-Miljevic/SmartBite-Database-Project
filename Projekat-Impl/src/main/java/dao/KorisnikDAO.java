package dao;

import model.Klijent;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class KorisnikDAO {
    public static void kreirajTabeluNutrijenata(Connection conn, double kalorije, double proteini, double masti, double ug, int id)
    {
        try{
            String sql = "insert into nutritivni_podaci " +
                    "(klijent_id_korisnika,datum,kalorije,proteini,ugljeni_hidrati,masti) " +
                    "values " +
                    "(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            LocalDateTime datum =  LocalDateTime.now();
            stmt.setTimestamp(2, Timestamp.valueOf(datum));
            stmt.setDouble(3, kalorije);
            stmt.setDouble(4, proteini);
            stmt.setDouble(5, ug);
            stmt.setDouble(6, masti);
            stmt.executeUpdate();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    /*public static void pregledajNutritivnePodatke(Connection conn, int id)
    {
        try
        {
            String sql = "select * from nutritivni_podaci where klijent_id_korisnika = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                System.out.printf("Potrebne dnevne kalorije: %.2f%n", rs.getDouble("Kalorije"));
                System.out.printf("Potrebni dnevni proteini: %.2f%n", rs.getDouble("proteini"));
                System.out.printf("Potrebni dnevni ugljeni hidrati: %.2f%n", rs.getDouble("ugljeni_hidrati"));
                System.out.printf("Potrebne dnevne masti: %.2f%n", rs.getDouble("masti"));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }*/
    /*public static void izmjenaPodataka(Connection conn, int id, Klijent klijent)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Godine [G], Visina [V], Tezina [T], Nivo fizicke aktivnost [N], " +
                "Ciljana tezina [C], Datum postizanja cilja [D]");
        String pp = sc.nextLine();
        Boolean potvrda = false;
        if(pp.equals("G")) {
            System.out.println("Unesite godine: ");
            String godine = sc.nextLine();
            String regex = "[1-9][0-9]?";
            if(godine.matches(regex)) {
                String sql = "update klijent set starost = ? where ID_korisnika = ?";
                try{
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, godine);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    klijent.starost = Integer.parseInt(godine);
                    potvrda=true;
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }else if(pp.equals("V")) {
            System.out.println("Unesite visinu: ");
            String visina = sc.nextLine();
            String regex = "[1-2][0-9][0-9]";
            if(visina.matches(regex)) {
                String sql = "update klijent set visina = ? where ID_korisnika = ?";
                try{
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, visina);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    klijent.visina = Double.parseDouble(visina);
                    potvrda=true;
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }else if(pp.equals("T")){
            System.out.println("Unesite tezinu: ");
            String tezina = sc.nextLine();
            String regex = "[0-9]+(\\.[0-9]+)?";
            if(tezina.matches(regex)) {
                String sql = "update klijent set tezina = ? where ID_korisnika = ?";
                try{
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, tezina);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    klijent.tezina = Double.parseDouble(tezina);
                    potvrda=true;
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }else if(pp.equals("N")) {
            System.out.println("Unesite fizicku aktivnost: [MALO] [SREDNJE] [PUNO]");
            String fizickaAktivnost = sc.nextLine();
            if(fizickaAktivnost.equals("MALO") || fizickaAktivnost.equals("SREDNJE") || fizickaAktivnost.equals("PUNO")) {
                String sql = "update klijent set nivofizickeaktivnosti = ? where ID_korisnika = ?";
                try{
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, fizickaAktivnost);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    klijent.nivoFizickeAktivnosti = fizickaAktivnost;
                    potvrda=true;
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }else if(pp.equals("C")) {
            System.out.println("Unesite ciljanu tezinu: ");
            String tezina = sc.nextLine();
            String regex = "[1-9][0-9]+";
            if(tezina.matches(regex)) {
                String sql = "update klijent set ciljanatezina = ? where ID_korisnika = ?";
                try{
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, tezina);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    klijent.ciljanaTezina = Double.parseDouble(tezina);
                    potvrda=true;
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }else if(pp.equals("D")) {
            System.out.println("Unesite datum postizanja cilja: [yyyy-mm-dd]");
            String datum = sc.nextLine();
            String regex = "\\d{4}-\\d{2}-\\d{2}";
            if(datum.matches(regex)) {
                String sql = "update klijent set datumpostizanjaciljanetezine = ? where ID_korisnika = ?";
                try{
                    PreparedStatement ps = conn.prepareStatement(sql);
                    LocalDate localDate = LocalDate.parse(datum);
                    klijent.datumPostizanjaCiljaneTezine = localDate;
                    ps.setDate(1, Date.valueOf(localDate));
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    potvrda=true;
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            } else {
                System.out.println("Neispravan format datuma!");
            }
        }
        if(potvrda)
        {
            if (klijent.starost>0 && klijent.visina>0 && klijent.tezina>0 && klijent.nivoFizickeAktivnosti!=null
                    && !klijent.nivoFizickeAktivnosti.isEmpty() && klijent.ciljanaTezina>0
                    && klijent.datumPostizanjaCiljaneTezine.isAfter(LocalDate.now()))
            {
                double skalar;
                if(klijent.nivoFizickeAktivnosti.startsWith("V"))
                    skalar = 1.9;
                else if(klijent.nivoFizickeAktivnosti.startsWith("S"))
                    skalar = 1.55;
                else skalar = 1.2;
                int razlika = klijent.ciljanaTezina>klijent.tezina?600:-600;
                double kalorije = (10*klijent.tezina)+6.25*(klijent.visina)-5*klijent.starost
                        +(klijent.pol.startsWith("M")?5:-161)+razlika*skalar;
                double proteini = klijent.tezina*2;
                double masti = klijent.tezina*0.8;
                double ugljeni = (kalorije-(4*proteini+9*masti))/4;
                kreirajTabeluNutrijenata(conn,kalorije,proteini,masti,ugljeni,klijent.id);
                System.out.println("Uspjesna izmjena podataka");
            }
        }
    }*/
    public static Klijent ucitajKlijenta(Connection conn, int id) {
        try {
            String sql = "select * from korisnik k " +
                            "join klijent kl " +
                            "on k.id_korisnika = kl.id_korisnika " +
                            "where k.id_korisnika = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Klijent k = new Klijent();
                k.id = rs.getInt("id_korisnika");
                k.ime = rs.getString("ime");
                k.prezime = rs.getString("prezime");
                k.email = rs.getString("email");
                k.korisnickoIme = rs.getString("korisnickoIme");
                k.starost = rs.getInt("starost");
                k.visina = rs.getDouble("visina");
                k.tezina = rs.getDouble("tezina");
                k.nivoFizickeAktivnosti = rs.getString("nivoFizickeAktivnosti");
                k.ciljanaTezina = rs.getDouble("ciljanaTezina");
                k.pol = rs.getString("pol");
                Date datum = rs.getDate("datumPostizanjaCiljaneTezine");
                if(datum != null) {
                    k.datumPostizanjaCiljaneTezine = datum.toLocalDate();
                }
                return k;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void azurirajKlijenta(
            Connection conn,
            Klijent klijent
    ) {
        try {
            if(postojiUsernameIliEmail(conn, klijent.korisnickoIme, klijent.email, klijent.id))
            {
                System.out.println("Username ili email vec postoje!");
                return;
            }
            String sql1 = "update korisnik set " +
                            "ime=?, " +
                            "prezime=?, " +
                            "email=?, " +
                            "korisnickoIme=? " +
                            "where id_korisnika=?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, klijent.ime);
            ps1.setString(2, klijent.prezime);
            ps1.setString(3, klijent.email);
            ps1.setString(4, klijent.korisnickoIme);
            ps1.setInt(5, klijent.id);
            Klijent stariKlijent = ucitajKlijenta(conn, klijent.id);
            boolean promjenaNutritivnih = false;
            if(stariKlijent.starost != klijent.starost)
                promjenaNutritivnih = true;
            if(stariKlijent.visina != klijent.visina)
                promjenaNutritivnih = true;
            if(stariKlijent.tezina != klijent.tezina)
                promjenaNutritivnih = true;
            if(!String.valueOf(stariKlijent.nivoFizickeAktivnosti).equals(String.valueOf(klijent.nivoFizickeAktivnosti)))
                promjenaNutritivnih = true;
            if(stariKlijent.ciljanaTezina != klijent.ciljanaTezina)
                promjenaNutritivnih = true;
            if(!String.valueOf(stariKlijent.pol).equals(String.valueOf(klijent.pol)))
                promjenaNutritivnih = true;
            if(!String.valueOf(stariKlijent.datumPostizanjaCiljaneTezine).equals(String.valueOf(klijent.datumPostizanjaCiljaneTezine)))
                promjenaNutritivnih = true;
            ps1.executeUpdate();
            String sql2 = "update klijent set " +
                            "starost=?, " +
                            "visina=?, " +
                            "tezina=?, " +
                            "nivoFizickeAktivnosti=?, " +
                            "ciljanaTezina=?, " +
                            "datumPostizanjaCiljaneTezine=?, " +
                            "pol=? " +
                            "where id_korisnika=?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, klijent.starost);
            ps2.setDouble(2, klijent.visina);
            ps2.setDouble(3, klijent.tezina);
            ps2.setString(4, klijent.nivoFizickeAktivnosti);
            ps2.setDouble(5, klijent.ciljanaTezina);
            ps2.setDate(6, Date.valueOf(klijent.datumPostizanjaCiljaneTezine));
            ps2.setString(7, klijent.pol);
            ps2.setInt(8, klijent.id);
            ps2.executeUpdate();
            if(promjenaNutritivnih)
            {
                if (klijent.starost > 0 && klijent.visina > 0 && klijent.tezina > 0 && klijent.nivoFizickeAktivnosti != null && !klijent.nivoFizickeAktivnosti.isEmpty()
                        && klijent.ciljanaTezina > 0 && klijent.datumPostizanjaCiljaneTezine != null && klijent.datumPostizanjaCiljaneTezine.isAfter(LocalDate.now()))
                {
                    double skalar;
                    if(klijent.nivoFizickeAktivnosti.startsWith("P"))
                        skalar = 1.9;
                    else if(klijent.nivoFizickeAktivnosti.startsWith("S"))
                        skalar = 1.55;
                    else
                        skalar = 1.2;
                    int razlika = klijent.ciljanaTezina > klijent.tezina ? 600 : -600;
                    double kalorije = ((10 * klijent.tezina) + 6.25 * klijent.visina - 5 * klijent.starost
                                    + (klijent.pol.startsWith("M") ? 5 : -161)) * skalar + razlika;
                    double proteini = klijent.tezina * 2;
                    double masti = klijent.tezina * 0.8;
                    double ugljeni = (kalorije - (4 * proteini + 9 * masti)) / 4;
                    kreirajTabeluNutrijenata(conn, kalorije, proteini, masti, ugljeni, klijent.id);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean postojiUsernameIliEmail(
            Connection conn,
            String username,
            String email,
            int trenutniId
    ) {
        try {
            String sql = "select * from korisnik " +
                            "where (korisnickoime = ? or email = ?) " +
                            "and id_korisnika != ?";
            PreparedStatement ps =
                    conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setInt(3, trenutniId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
