package dao;

import model.Komentar;

import java.sql.*;
import java.util.ArrayList;

public class KomentarDAO {
    public static ArrayList<Komentar> komentari = new ArrayList<>();
    public static void unesiKomentar(Connection conn, String komentar, int ID_Klijent,int ID_recepta, int rodit)throws SQLException
    {
        String sql = "insert into komentar "+
                "(komentar,klijent_id_korisnika,recept_id_recepta,komentar_id_komentara) "+
                "values (?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, komentar);
        ps.setInt(2, ID_Klijent);
        ps.setInt(3, ID_recepta);
        if(rodit==-1) ps.setNull(4, Types.INTEGER);
        else ps.setInt(4, rodit);
        ps.executeUpdate();
    }
    public static int prikaziSveKomentare(Connection conn, int id)throws SQLException{
        komentari.clear();
        String sql = "select k.*, kor.korisnickoIme from komentar k " +
                "join korisnik kor on k.klijent_id_korisnika = kor.id_korisnika " +
                "where k.komentar_id_komentara is null " +
                "and k.status = 1 " +
                "and k.recept_id_recepta = ? limit 10";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            Komentar k = new Komentar();
            k.id = rs.getInt("id_komentara");
            k.komentar = rs.getString("komentar");
            Timestamp date = rs.getTimestamp("datum_objave");
            if(date!=null) k.datumObjave = date.toLocalDateTime();
            k.id_klijenta = rs.getInt("klijent_id_korisnika");
            k.id_recept = rs.getInt("recept_id_recepta");
            k.roditeljskiKomentar = rs.getInt("komentar_id_komentara");
            k.korisnickoIme = rs.getString("korisnickoIme");
            komentari.add(k);
        }
        for(Komentar k : komentari)
        {
            System.out.println(k);
        }
        return komentari.size();
    }
    public static void prikaziSvePodkomentare(Connection conn, int id)throws SQLException{
        String sql = "select k.*, kor.korisnickoIme from komentar k " +
                "join korisnik kor on k.klijent_id_korisnika = kor.id_korisnika " +
                "where k.komentar_id_komentara = ? " +
                "and k.status = 1 limit 10";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            Komentar k = new Komentar();
            k.id = rs.getInt("id_komentara");
            k.komentar = rs.getString("komentar");
            Timestamp date = rs.getTimestamp("datum_objave");
            if(date!=null) k.datumObjave = date.toLocalDateTime();
            k.id_klijenta = rs.getInt("klijent_id_korisnika");
            k.id_recept = rs.getInt("recept_id_recepta");
            k.roditeljskiKomentar = rs.getInt("komentar_id_komentara");
            k.korisnickoIme = rs.getString("korisnickoIme");
            System.out.println(k);
        }
    }

}
