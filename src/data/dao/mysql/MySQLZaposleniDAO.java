/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.ZaposleniDAO;
import data.dto.ZaposleniDTO;
import data.dto.ZaposleniPomocniDTO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author haker
 */
public class MySQLZaposleniDAO implements ZaposleniDAO{
    @Override
    public List<ZaposleniDTO> sviZaposleni() {
                List<ZaposleniDTO> retVal = new ArrayList<ZaposleniDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM radnik r "
                        + "WHERE DatumDo is null "
                        + "ORDER BY IdRadnik ASC";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new ZaposleniDTO(rs.getInt("IdRadnik"),rs.getString("Ime"),
                                        rs.getString("Prezime"), rs.getString("Telefon"), rs.getString("Adresa"),
                                        rs.getString("StrucnaSprema"), rs.getString("ImeOca"),
                                        rs.getString("BrojLicneKarte"), rs.getDate("DatumRodjenja"),
                                        rs.getString("Funkcija"),rs.getDate("DatumOd"),rs.getDate("DatumDo")));
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps, rs);
		}
		return retVal;
    }

    @Override
    public List<ZaposleniDTO> sviBivsiZaposleni() {
        List<ZaposleniDTO> retVal = new ArrayList<ZaposleniDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM radnik r "
                        + "WHERE DatumDo is not null "
                        + "ORDER BY IdRadnik ASC";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()){
                            retVal.add(new ZaposleniDTO(rs.getInt("IdRadnik"),rs.getString("Ime"),
                                        rs.getString("Prezime"), rs.getString("Telefon"), rs.getString("Adresa"),
                                        rs.getString("StrucnaSprema"), rs.getString("ImeOca"),
                                        rs.getString("BrojLicneKarte"), rs.getDate("DatumRodjenja"),
                                        rs.getString("Funkcija"),rs.getDate("DatumOd"),rs.getDate("DatumDo")));
                        }
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps, rs);
		}
		return retVal;
    }
    @Override
    public boolean dodajZaposlenog(ZaposleniDTO zaposleni) {
         boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "INSERT INTO radnik(Ime,Prezime, Telefon , Adresa, StrucnaSprema,ImeOca, BrojLicneKarte,DatumRodjenja,Funkcija,DatumOd) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ? ,?) ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, zaposleni.getIme().toString());
                        ps.setString(2, zaposleni.getPrezime().toString());
                        ps.setString(3, zaposleni.getTelefon().toString());
                        ps.setString(4, zaposleni.getAdresa().toString());
                        ps.setString(5, zaposleni.getStrucnaSprema().toString());
                        ps.setString(6, zaposleni.getImeOca().toString());
                        ps.setString(7, zaposleni.getBrojLicneKarte().toString());
                        ps.setDate(8,zaposleni.getDatumRodjenja());
                        ps.setString(9,zaposleni.getFunkcija());
                        ps.setDate(10,zaposleni.getDatumOd());

			retVal = ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps);
		}
		return retVal;
    }

    @Override
    public boolean azurirajZaposlenog(ZaposleniDTO zaposleni) {
        boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "UPDATE radnik SET "
				+ "Ime=? "
                                + ",Prezime=? "                               
                                + ",Telefon=? "
                                + ",Adresa=? "
                                + ",StrucnaSprema=? "
                                + ",ImeOca=? "
                                + ",BrojLicneKarte=? "
                                + ",DatumRodjenja=? "
                                + ",Funkcija=? "
                                + ",DatumOd=? "
                                + ",DatumDo=? "
				+ "WHERE IdRadnik=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, zaposleni.getIme());
                        ps.setString(2, zaposleni.getPrezime());
                        ps.setString(3, zaposleni.getTelefon());
                        ps.setString(4, zaposleni.getAdresa());
                        ps.setString(5, zaposleni.getStrucnaSprema());
                        ps.setString(6, zaposleni.getImeOca());
                        ps.setString(7, zaposleni.getBrojLicneKarte());
                        ps.setDate(8,zaposleni.getDatumRodjenja());
                        ps.setString(9, zaposleni.getFunkcija());
                        ps.setDate(10,zaposleni.getDatumOd());
                        ps.setDate(11,zaposleni.getDatumDo());

                        ps.setInt(12,zaposleni.getIdRadnik());
			retVal = ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps);
		}
		return retVal;
    }

    @Override
    public boolean obrisiZaposlenog(ZaposleniDTO zaposleni) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean ponistiOtkaz(ZaposleniDTO zaposleni){
        boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "UPDATE radnik SET "
				+ "DatumOd=? ,"
                                + "DatumDo=NULL "
				+ "WHERE IdRadnik=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setDate(1, zaposleni.getDatumOd());
                        ps.setInt(2, zaposleni.getIdRadnik());

			retVal = ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps);
		}
		return retVal;
    }

    @Override
    public List<ZaposleniPomocniDTO> sviRadniNaloziZaposlenog(ZaposleniDTO zaposleni,java.sql.Date datumOd,java.sql.Date datumDo){
        List<ZaposleniPomocniDTO> retVal = new ArrayList<ZaposleniPomocniDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query ="SELECT DatumZatvaranjaNaloga,Troskovi,Opis,CijenaUsluge,Marka,Model,BrojRegistracije\n" +
"from radnik r  inner join radni_nalog_radnik rnr on r.IdRadnik=rnr.IdRadnik\n" +
"inner join radni_nalog rn on rnr.IdRadniNalog=rn.IdRadniNalog\n" +
"inner join vozilo v on rn.IdVozilo=v.IdVozilo\n" +
"inner join model_vozila mv on v.IdModelVozila=mv.IdModelVozila\n" +
"where r.IdRadnik=? AND rn.DatumZatvaranjaNaloga BETWEEN ? AND ?";
		try{
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setInt(1, zaposleni.getIdRadnik());
                        ps.setDate(2,datumOd);
                        ps.setDate(3,datumDo);
			rs = ps.executeQuery();

			while(rs.next()){
                            retVal.add(new ZaposleniPomocniDTO(rs.getString("Marka"),
                            rs.getString("Model"),rs.getString("BrojRegistracije"),rs.getDate("DatumZatvaranjaNaloga"),
                            rs.getString("Opis"),rs.getDouble("Troskovi"),rs.getDouble("CijenaUsluge")));
                        }
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps, rs);
		}
		return retVal;
    }
    @Override
    public ZaposleniDTO zaposleniPoImenu(String ime, String prezime, String imeOca) {
                ZaposleniDTO retVal = null;
                Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM radnik r "
                        + "WHERE DatumDo is null, "
                        + "Ime=?, "
                        + "Prezime=? "
                        + "AND ImeOca=? ";
               
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, ime);
                        ps.setString(2, prezime);
                        ps.setString(3, imeOca);
			rs = ps.executeQuery();

			while (rs.next())
				retVal = new ZaposleniDTO(rs.getInt("IdRadnik"),rs.getString("Ime"),
                                        rs.getString("Prezime"), rs.getString("Telefon"), rs.getString("Adresa"),
                                        rs.getString("StrucnaSprema"), rs.getString("ImeOca"),
                                        rs.getString("BrojLicneKarte"), rs.getDate("DatumRodjenja"),
                                        rs.getString("Funkcija"),rs.getDate("DatumOd"),rs.getDate("DatumDo"));
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps, rs);
		}
		return retVal;
    }

    @Override
    public ZaposleniDTO zaposleni(int id) {
                ZaposleniDTO retVal = null;
                Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT * FROM radnik "
                        + "WHERE IdRadnik=? ";
               
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setInt(1, id);
			rs = ps.executeQuery();

			while (rs.next())
				retVal = new ZaposleniDTO(rs.getInt("IdRadnik"),rs.getString("Ime"),
                                        rs.getString("Prezime"), rs.getString("Telefon"), rs.getString("Adresa"),
                                        rs.getString("StrucnaSprema"), rs.getString("ImeOca"),
                                        rs.getString("BrojLicneKarte"), rs.getDate("DatumRodjenja"),
                                        rs.getString("Funkcija"),rs.getDate("DatumOd"),rs.getDate("DatumDo"));
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps, rs);
		}
		return retVal;
    }
}
