package service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
public class AdminService {
    public static Scanner sc = new Scanner(System.in);
    public static void dodajSastojak(Connection conn, String naziv, double kalorije, double proteini, double ugljeni, double masti, int adminID) {
        try {
            String sql = "insert into sastojak " +
                            "(naziv, kalorije, proteini, ugljeni_hidrati, masti, administrator_id_korisnika) " +
                            "values (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, naziv);
            ps.setDouble(2, kalorije);
            ps.setDouble(3, proteini);
            ps.setDouble(4, ugljeni);
            ps.setDouble(5, masti);
            ps.setInt(6, adminID);
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String> topPrijavljeniKomentari(Connection conn, int limit) {
        ArrayList<String> lista = new ArrayList<>();
        try {
            String sql = "select k.id_komentara, " +
                            "k.komentar, " +
                            "kor.korisnickoime, " +
                            "count(pk.komentar_id_komentara) as broj_prijava " +
                            "from komentar k " +
                            "join korisnik kor " +
                            "on k.klijent_id_korisnika = kor.id_korisnika " +
                            "left join klijent_prijavljuje_komentar pk " +
                            "on k.id_komentara = pk.komentar_id_komentara " +
                            "group by k.id_komentara, k.komentar, kor.korisnickoime " +
                            "order by broj_prijava desc " +
                            "limit ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                lista.add("ID: " + rs.getInt("id_komentara") + " | " +
                                rs.getString("korisnickoime") + " | prijave: " +
                                rs.getInt("broj_prijava") + "\n" + rs.getString("komentar"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    public static void ukloniKomentar(Connection conn, int komentarID, int adminID, String obrazlozenje)
    {
        try {
            CallableStatement cs = conn.prepareCall("{call provjeri_admin_status(?)}");
            cs.setInt(1, adminID);
            cs.execute();
            String sql1 = "update komentar set status = 0 where id_komentara = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, komentarID);
            ps1.executeUpdate();
            String sql2 = "insert into administrator_uklanja_komentar " +
                            "(komentar_id_komentara, datum, obrazlozenje, administrator_korisnik_id_korisnika) " +
                            "values (?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, komentarID);
            ps2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps2.setString(3, obrazlozenje);
            ps2.setInt(4, adminID);
            ps2.executeUpdate();
            System.out.println("Komentar uklonjen!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void aktivirajKomentar(Connection conn, int komentarID, int adminID, String obrazlozenje)
    {
        try {
            CallableStatement cs = conn.prepareCall("{call provjeri_admin_status(?)}");
            cs.setInt(1, adminID);
            cs.execute();
            String provjera = "select status from komentar where id_komentara = ?";
            PreparedStatement p0 = conn.prepareStatement(provjera);
            p0.setInt(1, komentarID);
            ResultSet rs = p0.executeQuery();
            if(rs.next()) {
                if (rs.getInt("status") == 1) {
                    System.out.println("Komentar je vec aktivan!");
                    return;
                }
            } else {
                System.out.println("Komentar ne postoji!");
                return;
            }
            String sql1 = "update komentar set status = 1 where id_komentara = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, komentarID);
            ps1.executeUpdate();
            String sql2 = "insert into administrator_uklanja_komentar " +
                            "(komentar_id_komentara, datum, obrazlozenje, administrator_korisnik_id_korisnika) " +
                            "values (?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, komentarID);
            ps2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps2.setString(3, "AKTIVACIJA: " + obrazlozenje);
            ps2.setInt(4, adminID);
            ps2.executeUpdate();
            System.out.println("Komentar aktiviran!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void ukloniRecept(Connection conn, int receptID, int adminID, String obrazlozenje)
    {
        try {
            CallableStatement cs = conn.prepareCall("{call provjeri_admin_status(?)}");
            cs.setInt(1, adminID);
            cs.execute();
            String sql1 = "update recept set status = 0 where id_recepta = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, receptID);
            ps1.executeUpdate();
            String sql2 = "insert into administrator_uklanja_recept " +
                            "(recept_id_recepta, datum, obrazlozenje, administrator_korisnik_id_korisnika) " +
                            "values (?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, receptID);
            ps2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps2.setString(3, obrazlozenje);
            ps2.setInt(4, adminID);
            ps2.executeUpdate();
            System.out.println("Recept uklonjen!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void aktivirajRecept(Connection conn, int receptID, int adminID, String obrazlozenje)
    {
        try {
            CallableStatement cs = conn.prepareCall("{call provjeri_admin_status(?)}");
            cs.setInt(1, adminID);
            cs.execute();
            String provjera = "select status from recept where id_recepta = ?";
            PreparedStatement p0 = conn.prepareStatement(provjera);
            p0.setInt(1, receptID);
            ResultSet rs = p0.executeQuery();
            if(rs.next()) {
                if(rs.getInt("status") == 1) {
                    System.out.println("Recept je vec aktivan!");
                    return;
                }
            } else {
                System.out.println("Recept ne postoji!");
                return;
            }
            String sql1 = "update recept set status = 1 where id_recepta = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, receptID);
            ps1.executeUpdate();
            String sql2 = "insert into administrator_uklanja_recept " +
                            "(recept_id_recepta, datum, obrazlozenje, administrator_korisnik_id_korisnika) " +
                            "values (?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, receptID);
            ps2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps2.setString(3, "AKTIVACIJA: " + obrazlozenje);
            ps2.setInt(4, adminID);
            ps2.executeUpdate();
            System.out.println("Recept aktiviran!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void ukloniKlijenta(Connection conn, int klijentID, int adminID, String obrazlozenje)
    {
        try {
            CallableStatement cs = conn.prepareCall("{call provjeri_admin_status(?)}");
            cs.setInt(1, adminID);
            cs.execute();
            String sql1 = "update korisnik set status = 0 where id_korisnika = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, klijentID);
            ps1.executeUpdate();
            String sql2 = "insert into administrator_uklanja_klijenta " +
                            "(klijent_korisnik_id_korisnika, datum, obrazlozenje, administrator_korisnik_id_korisnika) " +
                            "values (?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, klijentID);
            ps2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps2.setString(3, obrazlozenje);
            ps2.setInt(4, adminID);
            ps2.executeUpdate();
            System.out.println("Klijent uklonjen!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void aktivirajKlijenta(Connection conn, int klijentID, int adminID, String obrazlozenje)
    {
        try {
            CallableStatement cs = conn.prepareCall("{call provjeri_admin_status(?)}");
            cs.setInt(1, adminID);
            cs.execute();
            String provjera = "select status from korisnik where id_korisnika = ?";
            PreparedStatement p0 = conn.prepareStatement(provjera);
            p0.setInt(1, klijentID);
            ResultSet rs = p0.executeQuery();
            if(rs.next()) {
                if(rs.getInt("status") == 1) {
                    System.out.println("Klijent je vec aktivan!");
                    return;
                }
            } else {
                System.out.println("Klijent ne postoji!");
                return;
            }
            String sql1 = "update korisnik set status = 1 where id_korisnika = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, klijentID);
            ps1.executeUpdate();
            String sql2 = "insert into administrator_uklanja_klijenta " +
                            "(klijent_korisnik_id_korisnika, datum, obrazlozenje, administrator_korisnik_id_korisnika) " +
                            "values (?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, klijentID);
            ps2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps2.setString(3, "AKTIVACIJA: " + obrazlozenje);
            ps2.setInt(4, adminID);
            ps2.executeUpdate();
            System.out.println("Klijent aktiviran!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void ukloniSastojak(Connection conn, int sastojakID, int adminID, String obrazlozenje)
    {
        try {
            CallableStatement cs = conn.prepareCall("{call provjeri_admin_status(?)}");
            cs.setInt(1, adminID);
            cs.execute();
            String sql1 = "update sastojak set status = 0 where id_sastojka = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, sastojakID);
            ps1.executeUpdate();
            String sql2 = "insert into administrator_uklanja_sastojak " +
                            "(sastojak_id_sastojka, datum, obrazlozenje, administrator_korisnik_id_korisnika) " +
                            "values (?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, sastojakID);
            ps2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps2.setString(3, obrazlozenje);
            ps2.setInt(4, adminID);
            ps2.executeUpdate();
            System.out.println("Sastojak uklonjen!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void aktivirajSastojak(Connection conn, int sastojakID, int adminID, String obrazlozenje)
    {
        try {
            CallableStatement cs = conn.prepareCall("{call provjeri_admin_status(?)}");
            cs.setInt(1, adminID);
            cs.execute();
            String provjera = "select status from sastojak where id_sastojka = ?";
            PreparedStatement p0 = conn.prepareStatement(provjera);
            p0.setInt(1, sastojakID);
            ResultSet rs = p0.executeQuery();
            if(rs.next()) {
                if(rs.getInt("status") == 1) {
                    System.out.println("Sastojak je vec aktivan!");
                    return;
                }
            } else {
                System.out.println("Sastojak ne postoji!");
                return;
            }
            String sql1 = "update sastojak set status = 1 where id_sastojka = ?";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.setInt(1, sastojakID);
            ps1.executeUpdate();
            String sql2 = "insert into administrator_uklanja_sastojak " +
                            "(sastojak_id_sastojka, datum, obrazlozenje, administrator_korisnik_id_korisnika) " +
                            "values (?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, sastojakID);
            ps2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps2.setString(3, "AKTIVACIJA: " + obrazlozenje);
            ps2.setInt(4, adminID);
            ps2.executeUpdate();
            System.out.println("Sastojak aktiviran!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    /*public static void kreirajAdministratora(Connection conn)
    {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Ime:");
            String ime = sc.nextLine();
            System.out.println("Prezime:");
            String prezime = sc.nextLine();
            System.out.println("Email:");
            String email = sc.nextLine();
            System.out.println("Broj telefona:");
            String telefon = sc.nextLine();
            System.out.println("Korisnicko ime:");
            String username = sc.nextLine();
            System.out.println("Lozinka:");
            String password = sc.nextLine();
            String provjera = "select * from korisnik where korisnickoime = ? or email = ?";
            PreparedStatement ps0 = conn.prepareStatement(provjera);
            ps0.setString(1, username);
            ps0.setString(2, email);
            ResultSet rs0 = ps0.executeQuery();
            if(rs0.next()) {
                System.out.println("Administrator vec postoji!");
                return;
            }
            String sql = "insert into korisnik " +
                            "(ime, prezime, email, brojtelefona, korisnickoime, lozinka) " +
                            "values (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ime);
            ps.setString(2, prezime);
            ps.setString(3, email);
            ps.setString(4, telefon);
            ps.setString(5, username);
            ps.setString(6, password);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = 0;
            if(rs.next()) {
                id = rs.getInt(1);
            }
            String sql2 = "insert into administrator (id_korisnika) values (?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, id);
            ps2.executeUpdate();
            System.out.println("Administrator uspjesno kreiran!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void promjenaStatusaKomentara(Connection conn, int adminID)
    {
        try {
            System.out.println("Najprijavljivaniji komentari:");
            String prikaz = "select k.id_komentara, " +
                            "k.komentar, " +
                            "kor.korisnickoime, " +
                            "count(pk.komentar_id_komentara) as broj_prijava " +
                            "from komentar k " +
                            "join korisnik kor " +
                            "on k.klijent_id_korisnika = kor.id_korisnika " +
                            "left join klijent_prijavljuje_komentar pk " +
                            "on k.id_komentara = pk.komentar_id_komentara " +
                            "group by k.id_komentara, k.komentar, kor.korisnickoime " +
                            "order by broj_prijava desc limit 5";
            PreparedStatement prikazPs = conn.prepareStatement(prikaz);
            ResultSet prikazRs = prikazPs.executeQuery();
            while(prikazRs.next())
            {
                System.out.println("ID: " + prikazRs.getInt("id_komentara") +
                                " | Korisnik: " + prikazRs.getString("korisnickoime") +
                                " | Broj prijava: " + prikazRs.getInt("broj_prijava")
                );
                System.out.println("Komentar:");
                System.out.println(prikazRs.getString("komentar"));
                System.out.println("-----------------------------------");
            }
            System.out.println("ID komentara:");
            int id = Integer.parseInt(sc.nextLine());
            String provjera = "select status from komentar where id_komentara = ?";
            PreparedStatement ps = conn.prepareStatement(provjera);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int status = rs.getInt("status");
                if(status == 1) {
                    System.out.println("Obrazlozenje:");
                    String obrazlozenje = sc.nextLine();
                    String sql = "update komentar set status = 0 where id_komentara = ?";
                    PreparedStatement ps2 = conn.prepareStatement(sql);
                    ps2.setInt(1, id);
                    ps2.executeUpdate();
                    String log = "insert into administrator_uklanja_komentar " +
                                    "(komentar_id_komentara, datum, obrazlozenje, administrator_korisnik_id_korisnika) " +
                                    "values (?, ?, ?, ?)";
                    PreparedStatement ps3 = conn.prepareStatement(log);
                    ps3.setInt(1, id);
                    ps3.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    ps3.setString(3, obrazlozenje);
                    ps3.setInt(4, adminID);
                    ps3.executeUpdate();
                    System.out.println("Komentar uklonjen!");
                } else {
                    String sql = "update komentar set status = 1 where id_komentara = ?";
                    PreparedStatement ps2 = conn.prepareStatement(sql);
                    ps2.setInt(1, id);
                    ps2.executeUpdate();
                    System.out.println("Komentar ponovo aktiviran!");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }*/
}