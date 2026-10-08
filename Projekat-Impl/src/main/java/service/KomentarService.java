package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class KomentarService {
    public static void prijavaKomentara(Connection conn, int klijentID, int komentar_id, String razlog)
    {
        try
        {
            String provjeraSql = "select * from klijent_prijavljuje_komentar " +
                            "where klijent_korisnik_id_korisnika = ? " +
                            "and komentar_id_komentara = ?";
            PreparedStatement provjeraPs = conn.prepareStatement(provjeraSql);
            provjeraPs.setInt(1, klijentID);
            provjeraPs.setInt(2, komentar_id);
            java.sql.ResultSet rs = provjeraPs.executeQuery();
            if(rs.next()) {
                System.out.println("Vec ste prijavili ovaj komentar!");
                return;
            }
            String sql = "insert into klijent_prijavljuje_komentar " +
                            "(klijent_korisnik_id_korisnika, komentar_id_komentara, datum, razlog) " +
                            "values (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, klijentID);
            pstmt.setInt(2, komentar_id);
            LocalDateTime datum = LocalDateTime.now();
            pstmt.setTimestamp(3, Timestamp.valueOf(datum));
            pstmt.setString(4, razlog);
            pstmt.executeUpdate();
            System.out.println("Komentar je zabiljezen");
        } catch(SQLException ex)
        {
            System.out.println("Doslo je do greske!");
            ex.printStackTrace();
        }
    }
}
