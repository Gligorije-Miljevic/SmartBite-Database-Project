package service;

import model.Administrator;
import model.Klijent;
import model.Korisnik;

import java.sql.*;
import java.util.Scanner;

import static dao.KorisnikDAO.kreirajTabeluNutrijenata;

public class AuthService {
    public static Scanner sc;
    public static Korisnik prijava(Connection conn, int uloga, String username, String password)
    {
            try {
                String sql;
                if(uloga == 1) {
                    sql = "select * from korisnik k " +
                            "join klijent k1 on k.id_korisnika = k1.id_korisnika " +
                            "where korisnickoime = ? and lozinka = ? and status = 1";
                }
                else {
                    sql = "select * from korisnik k " +
                            "join administrator a on k.id_korisnika = a.id_korisnika " +
                            "where korisnickoime = ? and lozinka = ? and status = 1";
                }
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Uspjesna prijava!");
                    if(uloga==1) {
                        Klijent korisnik = new Klijent();
                        korisnik.id = rs.getInt("ID_Korisnika");
                        korisnik.ime = rs.getString("Ime");
                        korisnik.prezime = rs.getString("Prezime");
                        korisnik.email = rs.getString("Email");
                        korisnik.brojTelefona=rs.getString("brojtelefona");
                        korisnik.korisnickoIme=rs.getString("korisnickoime");
                        korisnik.pol=rs.getString("pol");
                        korisnik.starost = rs.getInt("starost");
                        korisnik.visina = rs.getDouble("visina");
                        korisnik.tezina = rs.getDouble("tezina");
                        korisnik.nivoFizickeAktivnosti = rs.getString("nivoFizickeAktivnosti");
                        korisnik.ciljanaTezina=rs.getDouble("ciljanaTezina");
                        Date datum = rs.getDate("datumpostizanjaciljanetezine");
                        if(datum!=null) korisnik.datumPostizanjaCiljaneTezine=datum.toLocalDate();
                        return korisnik;
                    } else if(uloga==2) {
                        Administrator admin = new Administrator();
                        admin.id = rs.getInt("ID_Korisnika");
                        admin.ime = rs.getString("Ime");
                        admin.prezime = rs.getString("Prezime");
                        admin.email = rs.getString("Email");
                        admin.korisnickoIme = rs.getString("KorisnickoIme");
                        return admin;
                    }
                } else {
                    System.out.println("Neuspjesna prijava!");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        return null;
    }
    /*public static void registracija(Connection conn)
    {
        sc = new Scanner(System.in);
        System.out.println("Unesi ime: ");
        String ime = sc.nextLine();
        System.out.println("Unesi prezime: ");
        String prezime = sc.nextLine();
        System.out.println("Unesi email: ");
        String email = sc.nextLine();
        System.out.println("Unesi broj telefona: ");
        String brojtelefona = sc.nextLine();
        System.out.println("Unesi korisnicko ime: ");
        String korisnickoIme = sc.nextLine();
        System.out.println("Unesi lozinka: ");
        String lozinka = sc.nextLine();
        if(ime.trim().isEmpty() ||
                prezime.trim().isEmpty() ||
                email.trim().isEmpty() ||
                lozinka.trim().isEmpty())
        {
            System.out.println("Obavezna polja ne smiju biti prazna!");
            return;
        }
        System.out.println("Unesi broj godina: ");
        String brojGodina = sc.nextLine();
        System.out.println("Unesi pol: [M] [Z]");
        String pol = sc.nextLine();
        System.out.println("Unesi visinu: ");
        String visina = sc.nextLine();
        System.out.println("Unesi kilazu: ");
        String kilaza = sc.nextLine();
        System.out.println("Unesi nivo fizicke aktivnosti: [MALO] [SREDNJE] [PUNO]");
        String nivoFizicke = sc.nextLine();
        System.out.println("Unesi ciljanu tezinu: ");
        String ciljanaTezina = sc.nextLine();
        System.out.println("Unesi datum postizanja ciljane tezine: [yyyy-mm-dd]");
        String datumPostizanjeTezine = sc.nextLine();
        try {
            String provjera = "select * from korisnik where korisnickoime = ? or email = ?";
            PreparedStatement ps0 = conn.prepareStatement(provjera);
            ps0.setString(1, korisnickoIme);
            ps0.setString(2, email);
            ResultSet rs0 = ps0.executeQuery();
            if (rs0.next()) {
                System.out.println("Postoji vec korisnik sa unesenim korisnickim imenom ili emailom!");
                return;
            }
            String sql = "insert into korisnik (ime,prezime,email,brojtelefona,korisnickoime,lozinka) values (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ime);
            ps.setString(2, prezime);
            ps.setString(3, email);
            ps.setString(4, brojtelefona);
            ps.setString(5, korisnickoIme);
            ps.setString(6, lozinka);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int idKorisnika = 0;
            if (rs.next()) {
                idKorisnika = rs.getInt(1);
            }
            String sql2 = "insert into klijent (ID_Korisnika, starost, pol, visina, tezina, nivofizickeaktivnosti, ciljanatezina,datumpostizanjaciljanetezine) values (?,?,?,?,?,?,?,?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, idKorisnika);
            if (brojGodina.isEmpty())
                ps2.setNull(2, Types.INTEGER);
            else
                ps2.setInt(2, Integer.parseInt(brojGodina));
            if (pol.isEmpty())
                ps2.setNull(3, Types.INTEGER);
            else
                ps2.setString(3, pol);
            if (visina.isEmpty())
                ps2.setNull(4, Types.DECIMAL);
            else
                ps2.setDouble(4, Double.parseDouble(visina));
            if (kilaza.isEmpty())
                ps2.setNull(5, Types.DECIMAL);
            else
                ps2.setDouble(5, Double.parseDouble(kilaza));
            ps2.setString(6, nivoFizicke.isEmpty() ? null : nivoFizicke);
            if (ciljanaTezina.isEmpty())
                ps2.setNull(7, Types.DECIMAL);
            else
                ps2.setDouble(7, Double.parseDouble(ciljanaTezina));
            if (datumPostizanjeTezine.isEmpty())
                ps2.setNull(8, Types.DATE);
            else
                ps2.setDate(8, Date.valueOf(datumPostizanjeTezine));
            ps2.executeUpdate();
            System.out.println("Uspjesna registracija!");
            if (!brojGodina.isEmpty() && !visina.isEmpty() && !kilaza.isEmpty()
                    && !nivoFizicke.isEmpty() && nivoFizicke!=null && !ciljanaTezina.isEmpty() && !datumPostizanjeTezine.isEmpty())
            {
                double skalar;
                if(nivoFizicke.startsWith("V"))
                    skalar = 1.9;
                else if(nivoFizicke.startsWith("S"))
                    skalar = 1.55;
                else skalar = 1.2;
                int razlika = Integer.parseInt(ciljanaTezina)>Integer.parseInt(kilaza)?600:-600;
                double kalorije = ((10*(Double.parseDouble(kilaza))+6.25*(Double.parseDouble(visina))-5*Integer.parseInt(brojGodina)
                        +(pol.startsWith("M")?5:-161))*skalar)+razlika;
                double proteini = Double.parseDouble(kilaza)*2;
                double masti = Double.parseDouble(kilaza)*0.8;
                double ugljeni = (kalorije-(4*proteini+9*masti))/4;
                kreirajTabeluNutrijenata(conn,kalorije,proteini,masti,ugljeni,idKorisnika);
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }*/
    public static boolean registracija(
            Connection conn,
            String ime,
            String prezime,
            String email,
            String brojtelefona,
            String korisnickoIme,
            String lozinka,
            String brojGodina,
            String pol,
            String visina,
            String kilaza,
            String nivoFizicke,
            String ciljanaTezina,
            String datumPostizanjeTezine
    ) {
        if(ime.trim().isEmpty() ||
                prezime.trim().isEmpty() ||
                email.trim().isEmpty() ||
                lozinka.trim().isEmpty())
        {
            System.out.println("Obavezna polja ne smiju biti prazna!");
            return false;
        }
        try {
            conn.setAutoCommit(false);
            String provjera = "select * from korisnik where korisnickoime = ? or email = ?";
            PreparedStatement ps0 = conn.prepareStatement(provjera);
            ps0.setString(1, korisnickoIme);
            ps0.setString(2, email);
            ResultSet rs0 = ps0.executeQuery();
            if (rs0.next()) {
                System.out.println("Postoji vec korisnik sa unesenim korisnickim imenom ili emailom!");
                return false;
            }
            String sql = "insert into korisnik (ime,prezime,email,brojtelefona,korisnickoime,lozinka) values (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ime);
            ps.setString(2, prezime);
            ps.setString(3, email);
            ps.setString(4, brojtelefona);
            ps.setString(5, korisnickoIme);
            ps.setString(6, lozinka);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int idKorisnika = 0;
            if (rs.next()) {
                idKorisnika = rs.getInt(1);
            }
            String sql2 = "insert into klijent (ID_Korisnika, starost, pol, visina, tezina, nivofizickeaktivnosti, ciljanatezina,datumpostizanjaciljanetezine) values (?,?,?,?,?,?,?,?)";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, idKorisnika);
            if (brojGodina.isEmpty())
                ps2.setNull(2, Types.INTEGER);
            else
                ps2.setInt(2, Integer.parseInt(brojGodina));
            if (pol.isEmpty())
                ps2.setNull(3, Types.INTEGER);
            else
                ps2.setString(3, pol);
            if (visina.isEmpty())
                ps2.setNull(4, Types.DECIMAL);
            else
                ps2.setDouble(4, Double.parseDouble(visina));
            if (kilaza.isEmpty())
                ps2.setNull(5, Types.DECIMAL);
            else
                ps2.setDouble(5, Double.parseDouble(kilaza));
            ps2.setString(6, nivoFizicke.isEmpty() ? null : nivoFizicke);
            if (ciljanaTezina.isEmpty())
                ps2.setNull(7, Types.DECIMAL);
            else
                ps2.setDouble(7, Double.parseDouble(ciljanaTezina));
            if (datumPostizanjeTezine.isEmpty())
                ps2.setNull(8, Types.DATE);
            else
                ps2.setDate(8, Date.valueOf(datumPostizanjeTezine));
            ps2.executeUpdate();
            if (!brojGodina.isEmpty() && !visina.isEmpty() && !kilaza.isEmpty()
                    && !nivoFizicke.isEmpty() && nivoFizicke != null && !ciljanaTezina.isEmpty() && !datumPostizanjeTezine.isEmpty()) {
                double skalar;
                if (nivoFizicke.startsWith("V"))
                    skalar = 1.9;
                else if (nivoFizicke.startsWith("S"))
                    skalar = 1.55;
                else skalar = 1.2;
                int razlika = Integer.parseInt(ciljanaTezina) > Integer.parseInt(kilaza) ? 600 : -600;
                double kalorije = ((10 * (Double.parseDouble(kilaza)) + 6.25 * (Double.parseDouble(visina)) - 5 * Integer.parseInt(brojGodina)
                        + (pol.startsWith("M") ? 5 : -161)) * skalar) + razlika;
                double proteini = Double.parseDouble(kilaza) * 2;
                double masti = Double.parseDouble(kilaza) * 0.8;
                double ugljeni = (kalorije - (4 * proteini + 9 * masti)) / 4;
                kreirajTabeluNutrijenata(conn, kalorije, proteini, masti, ugljeni, idKorisnika);
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;
        }
        finally {
            try {
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
