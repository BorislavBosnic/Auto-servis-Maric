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
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author HP BOOK
 */
public class MySQLRadniNalogDAO implements RadniNalogDAO{
    
    
    //kod racunanja sume ne provjeravam flag iz radnog naloga da li je usluga placena jer u tom slucaju ne bi postojala faktura, to rjesava inner join
    //cekam Marica da javi da li postoje fakture sa odgodjenim placanjem, u kojem slucaju bi morali dodati novo polje - datum uplate
    public double getSumaCijenaUsluga(Date datumOd, Date datumDo){
          double sumaCijenaUsluga=0;
		Connection conn = null;
		PreparedStatement ps = null;
                ResultSet rs = null;

		String query = "select sum(CijenaUsluge) as suma from faktura inner join radni_nalog " +
                        "using (idRadniNalog)"+
                        "where DatumIzdavanja >= ? and DatumIzdavanja <=  ? ";
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
                        "where Datum >= ? and Datum <= ?";
                       
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
     
     
     public int getBrojPopravki(Date datumOd, Date datumDo){
          int brojPopravki=0;
		Connection conn = null;
		PreparedStatement ps = null;
                ResultSet rs = null;

		String query = "select count(*) as brojPopravki from radni_nalog where DatumZatvaranjaNaloga is not null and " +
                        " DatumZatvaranjaNaloga >= ? and DatumZatvaranjaNaloga <= ?";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setDate(1, new java.sql.Date(datumOd.getTime()));
                        ps.setDate(2, new java.sql.Date(datumDo.getTime()));

			
			rs=ps.executeQuery();
                        
                        while(rs.next()){
                        brojPopravki=rs.getInt("brojPopravki");
                        }
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps, rs);
		}
	
        return brojPopravki;
    }
     
      public int getBrojAutaNaStanju(){
          int brojAutaNaStanju=0;
		Connection conn = null;
		Statement s = null;
                ResultSet rs = null;

                
		String query = "select  count(*) as brojAuta from radni_nalog left outer join faktura using (idRadniNalog) where IdFaktura is null;";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			s = conn.createStatement();	
			rs=s.executeQuery(query);
                        
                        while(rs.next()){
                        brojAutaNaStanju=rs.getInt("brojAuta");
                        }
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(s, rs);
		}
	
        return brojAutaNaStanju;
    }
      
      
         public int getBrojAutaKojaCekajuPopravku(){
          int brojAutaKojaCekajuPopravku=0;
		Connection conn = null;
		Statement s = null;
                ResultSet rs = null;

                
		String query = "select count(*) as brojAuta from radni_nalog where DatumZatvaranjaNaloga is  null";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			s = conn.createStatement();	
			rs=s.executeQuery(query);
                        
                        while(rs.next()){
                        brojAutaKojaCekajuPopravku=rs.getInt("brojAuta");
                        }
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(s, rs);
		}
	
        return brojAutaKojaCekajuPopravku;
    }
}
