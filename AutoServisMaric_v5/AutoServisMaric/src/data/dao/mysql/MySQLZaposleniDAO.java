/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.ZaposleniDAO;
import data.dto.ZaposleniDTO;
import java.sql.Connection;
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

		String query = "SELECT IdRadnik, Ime, Prezime, Telefon, Adresa, StrucnaSprema, ImeOca, BrojLicneKarte,DatumRodjenja,IdRadniOdnos "
				+ "FROM radnik "
				+ "ORDER BY IdRadnik ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new ZaposleniDTO( rs.getString("Ime"), rs.getString("Prezime"), rs.getString("Telefon"), rs.getString("Adresa"), rs.getString("StrucnaSprema"), rs.getString("ImeOca"),rs.getString("BrojLicneKarte"), rs.getDate("DatumRodjenja")));
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

		String query = "INSERT INTO vozilo( Ime, Prezime,Telefon,Adresa, StrucnaSprema,ImeOca, BrojLicneKarte,DatumRodjenja) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?) ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, zaposleni.getIme());
                        ps.setString(2, zaposleni.getPrezime());
                        ps.setString(3, zaposleni.getTelefon().toString());
                        ps.setString(4, zaposleni.getAdresa().toString());
                        ps.setString(5, zaposleni.getStrucnaSprema().toString());
                        ps.setString(6, zaposleni.getImeoca().toString());
                        ps.setString(7, zaposleni.getBrojlicnekarte().toString());
                        ps.setDate(8,zaposleni.getDatumrodjenja());

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

		String query = "UPDATE zaposleni SET "
				+ "Imee=? "
                                + "Prezime=? "
                                
                                + "Telefon=? "
                                + "Adresa=? "
                                + "StrunaSprema=? "
                                + "ImeOca=? "
                                + "BrojLicneKarte=? "
                                + "DatumRodjenja=?"
				+ "WHERE IdRadnik=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, zaposleni.getIme());
                        ps.setString(2, zaposleni.getPrezime());
                        ps.setString(3, zaposleni.getTelefon().toString());
                        ps.setString(4, zaposleni.getAdresa().toString());
                        ps.setString(5, zaposleni.getStrucnaSprema().toString());
                        ps.setString(6, zaposleni.getImeoca().toString());
                        ps.setString(7, zaposleni.getBrojlicnekarte().toString());
                        ps.setDate(8,zaposleni.getDatumrodjenja());
                        ps.setString(9,zaposleni.getIdRadnik());

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
}
