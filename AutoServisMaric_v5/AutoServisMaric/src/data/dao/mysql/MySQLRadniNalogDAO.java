/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.RadniNalogDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author HP BOOK
 */
public class MySQLRadniNalogDAO implements RadniNalogDAO{
    
    public double getSumaCijenaUsluga(Date datumOd, Date datumDo){
          double sumaCijenaUsluga=0;
		Connection conn = null;
		PreparedStatement ps = null;
                ResultSet rs = null;

		String query = "select sum(CijenaUsluge) as suma from faktura inner join radni_nalog " +
                        "using (idRadniNalog)"+
                        "where DatumIzdavanja > ? and DatumIzdavanja <  ? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(datumOd.getTime()));
                        ps.setDate(2, new java.sql.Date(datumDo.getTime()));

			
			rs=ps.executeQuery();
                        
                        while(rs.next()){
                        sumaCijenaUsluga=rs.getDouble("suma");
                        }
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps, rs);
		}
	
        return sumaCijenaUsluga;
    }
    
     public double getSumaCijenaDijelova(Date datumOd, Date datumDo){
          double sumaCijenaDijelova=0;
		Connection conn = null;
		PreparedStatement ps = null;
                ResultSet rs = null;

		String query = "select * from prodan_dio inner join dio using (idDio)"+
                        "where Datum > ? and Datum < ?";
                       
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(datumOd.getTime()));
                        ps.setDate(2, new java.sql.Date(datumDo.getTime()));

			
			rs=ps.executeQuery();
                        
                        while(rs.next()){
                        sumaCijenaDijelova+=(rs.getDouble("CijenaProdaje")-rs.getDouble("TrenutnaCijena"))*rs.getDouble("Kolicina");
                        }
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps, rs);
		}
         return sumaCijenaDijelova;
         
     }
}
