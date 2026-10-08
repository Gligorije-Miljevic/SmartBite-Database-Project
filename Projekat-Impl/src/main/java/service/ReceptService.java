package service;

import model.Korisnik;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static dao.KomentarDAO.*;
import static service.KomentarService.prijavaKomentara;

public class ReceptService {
    public static Scanner sc = new Scanner(System.in);
    /*public static void upravljanjeReceptom(Connection conn, Korisnik klijent, String unos)
    {
        System.out.println("Sacuvaj [S], ocijeni [O], komentarisi [K], prikaz svih komentara [P] izlaz [X]");
        sc = new Scanner(System.in);
        String opcija = sc.nextLine();
        if (opcija.equals("S")) {
            try{
                int a = sacuvajRecept(conn, Integer.parseInt(unos), klijent.id);
                if (a != 0) {
                    System.out.println("Uspjesno sacuvan recept");
                }else {
                    System.out.println("Neuspjesno sacuvan recept");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        } else if (opcija.equals("O")) {
            System.out.println("Izaberite ocjenu: [1-5]");
            opcija = sc.nextLine();
            if (Integer.parseInt(opcija) >= 1 && Integer.parseInt(opcija) <= 5) {
                try {
                    int a = ocijeniRecept(conn, Integer.parseInt(unos), klijent.id, Integer.parseInt(opcija));
                    if (a == 1) {
                        System.out.println("Recept uspjesno ocjenjen");
                    } else if(a==0) {
                        System.out.println("Recept neuspjesno ocjenjen");
                    }else{
                        System.out.println("Promjena ocjene? [DA]/[NE] ");
                        String dane = sc.nextLine();
                        if (dane.equals("DA")) {
                            updateOcjena(conn, Integer.parseInt(unos), klijent.id, Integer.parseInt(opcija));
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            } else {
                System.out.println("Ocjena mora biti izmedju 1 i 5");
            }
        }else if(opcija.equals("K")) {
            System.out.println("Unesite vas komentar:");
            String komentar = sc.nextLine();
            try {
                unesiKomentar(conn, komentar, klijent.id, Integer.parseInt(unos),-1);
                System.out.println("Uspjesno dodan komentar");
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(opcija.equals("P")) {
            try {
                boolean ugnj = true;
                int roditeljskiKomentar = -1;
                int imaKom = prikaziSveKomentare(conn, Integer.parseInt(unos));
                while(ugnj) {
                    if (imaKom > 0) {
                        System.out.println("Pregledaj detalje komentara [P], komentarisi [K], prijavi komentar [R] izlaz [X]");
                        String izbor = sc.nextLine();
                        if (izbor.equals("P")) {
                            System.out.println("Unesi id komentara: [ID]");
                            String id = sc.nextLine();
                            prikaziSvePodkomentare(conn, Integer.parseInt(id));
                            roditeljskiKomentar = Integer.parseInt(id);
                        }else if (izbor.equals("K")) {
                            System.out.println("Unesi komentar:");
                            String komentar = sc.nextLine();
                            try {
                                unesiKomentar(conn, komentar, klijent.id, Integer.parseInt(unos), roditeljskiKomentar);
                                System.out.println("Uspjesno dodan komentar");
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }else if (izbor.equals("X")) {
                            ugnj = false;
                        }else if (izbor.equals("R")) {
                            System.out.println("Unesi id komentara: [ID]");
                            String id = sc.nextLine();
                            System.out.println("Unesi razlog prijave:");
                            String razlog = sc.nextLine();
                            prijavaKomentara(conn, klijent.id, Integer.parseInt(id), razlog);
                        }
                    }else{
                        System.out.println("Komentarisi [K]");
                        String izbor =  sc.nextLine();
                        if(izbor.equals("K")) {
                            String komentar = sc.nextLine();
                            try {
                                unesiKomentar(conn, komentar, klijent.id, Integer.parseInt(unos),roditeljskiKomentar);
                                System.out.println("Uspjesno dodan komentar");
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            ugnj = false;
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }*/
    public static int sacuvajRecept(Connection conn, int ID, int ID_Klijent)throws SQLException
    {
        String sql = "select count(*) from korisnik_cuva_recept where Recept_ID_Recepta = ? and Klijent_Korisnik_ID_Korisnika = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, ID);
        ps.setInt(2, ID_Klijent);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            int count = rs.getInt(1);
            if(count == 0)
            {
                String sql2 = "insert into korisnik_cuva_recept "+
                        "(recept_id_recepta, Klijent_Korisnik_ID_Korisnika)"+
                        "values (?, ?)";
                ps = conn.prepareStatement(sql2);
                ps.setInt(1, ID);
                ps.setInt(2, ID_Klijent);
                ps.executeUpdate();
                return 1;
            }else{
                System.out.println("Recept je vec sacuvan");
            }
        }else{
            System.out.println("Greska");
        }
        return 0;
    }
    public static int ocijeniRecept(Connection conn, int ID, int ID_Klijent, int ocjena)throws SQLException
    {
        String sql = "select count(*) from korisnik_ocjenjuje_recept where Recept_ID_Recepta = ? and Klijent_Korisnik_ID_Korisnika = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, ID);
        ps.setInt(2, ID_Klijent);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            int count = rs.getInt(1);
            if(count == 0)
            {
                String sql2 = "insert into korisnik_ocjenjuje_recept "+
                        "(recept_id_recepta, Klijent_Korisnik_ID_Korisnika, ocjena)"+
                        "values (?, ?, ?)";
                ps = conn.prepareStatement(sql2);
                ps.setInt(1, ID);
                ps.setInt(2, ID_Klijent);
                ps.setInt(3, ocjena);
                ps.executeUpdate();
                return 1;
            }else{
                System.out.println("Recept je vec ocjenjen!");
                return 2;
            }
        }else{
            System.out.println("Greska");
        }
        return 0;
    }
    public static void updateOcjena(Connection conn, int ID, int ID_Klijent, int ocjena)throws SQLException
    {
        String sql = "update korisnik_ocjenjuje_recept "+
                "set ocjena = ? "+
                "where Recept_ID_Recepta = ? and Klijent_Korisnik_ID_Korisnika = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, ocjena);
        ps.setInt(2, ID);
        ps.setInt(3, ID_Klijent);
        ps.executeUpdate();
        System.out.println("Ocjena recepta uspjesno azurirana!");
    }

}
