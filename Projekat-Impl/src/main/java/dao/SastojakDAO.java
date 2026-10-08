package dao;

import model.Sastojak;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SastojakDAO {
    public static Scanner sc = new Scanner(System.in);
    public static void prikaziSveSastojke(Connection conn)
    {
        try
        {
            String sql = "select * from sastojak where status = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Sastojak s = new Sastojak();
                s.id = rs.getInt("id_sastojka");
                s.naziv = rs.getString("naziv");
                System.out.println(s);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static Map<Integer, String> pregledSastojaka(Connection conn, String unos)
    {
        Map<Integer, String> sastojci = new HashMap<>();
        try {
            String sql = "select * from sastojak " +
                            "where naziv like ? and status = 1 " +
                            "limit 10";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, unos + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                sastojci.put(rs.getInt("id_sastojka"), rs.getString("naziv"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sastojci;
    }
    public static Map<Integer,Double> dodajSastojke(Connection conn) {
        Map<Integer,Double> mapa = new HashMap<>();
        boolean inf= true;
        while(inf) {
            System.out.println("Izaberite neke od sljedecih sastojaka [ID], pretrazite sastojke [P], potvrdi [K], izlaz [X]");
            prikaziSveSastojke(conn);
            boolean ispravnost = true;
            String unos =  sc.nextLine();
            String regex = "[0-9]*";
            if(unos.matches(regex)) {
                int idSastojka = Integer.parseInt(unos);
                System.out.println("Unesite kolicinu: [xx.yy]");
                unos = sc.nextLine();
                regex = "[0-9]+\\.[0-9]+";
                if(unos.matches(regex)) {
                    double kol = Double.parseDouble(unos);
                    mapa.put(idSastojka, kol);
                } else {
                    System.out.println("Nije ispravan format kolicine.");
                }
            }else if(unos.equalsIgnoreCase("P")) {
                System.out.println("Unesite naziv sastojka:");
                String pretraga = sc.nextLine();
                try {
                    String sql = "select * from sastojak where naziv like ? limit 5";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, pretraga + "%");
                    ResultSet rs = ps.executeQuery();
                    while(rs.next())
                    {
                        Sastojak s = new Sastojak();
                        s.id = rs.getInt("id_sastojka");
                        s.naziv = rs.getString("naziv");
                        System.out.println(s);
                    }
                }catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }else if(unos.equalsIgnoreCase("X")) {
                return null;
            }else if(unos.equalsIgnoreCase("K")) {
                break;
            }
        }
        return mapa;
    }
}
