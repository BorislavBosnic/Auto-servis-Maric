package data.dao.mysql;

import data.dao.KupacDAO;
import data.dto.KupacDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLKupacDAO implements KupacDAO{

    
    
    @Override
    public ArrayList<KupacDTO> kupciPrivatni(String ime, String prezime) {
        ArrayList<KupacDTO> retVal = new ArrayList<KupacDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdKupac, Naziv, Ime, Prezime, Telefon, Adresa, Grad "
				+ "FROM kupac "
                                + "WHERE Ime LIKE ? "
                                + "AND Prezime LIKE ? "
				+ "ORDER BY IdKupac ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, ime);
                        ps.setString(2, prezime);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new KupacDTO(rs.getInt("IdKupac"), rs.getString("Ime"), rs.getString("Prezime"), rs.getString("Naziv"), rs.getString("Telefon"), rs.getString("Adresa"), rs.getString("Grad")));
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
    public ArrayList<KupacDTO> kupciPravni(String naziv) {
        ArrayList<KupacDTO> retVal = new ArrayList<KupacDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdKupac, Naziv, Ime, Prezime, Telefon, Adresa, Grad "
				+ "FROM kupac "
                                + "WHERE Naziv LIKE ? "
				+ "ORDER BY IdKupac ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, naziv);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new KupacDTO(rs.getInt("IdKupac"), rs.getString("Ime"), rs.getString("Prezime"), rs.getString("Naziv"), rs.getString("Telefon"), rs.getString("Adresa"), rs.getString("Grad")));
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
    public ArrayList<KupacDTO> sviKupci() {
        ArrayList<KupacDTO> retVal = new ArrayList<KupacDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdKupac, Naziv, Ime, Prezime, Telefon, Adresa, Grad "
				+ "FROM kupac "
				+ "ORDER BY IdKupac ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new KupacDTO(rs.getInt("IdKupac"), rs.getString("Ime"), rs.getString("Prezime"), rs.getString("Naziv"), rs.getString("Telefon"), rs.getString("Adresa"), rs.getString("Grad")));
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
    public boolean dodajKupca(KupacDTO kupac) {
                boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "INSERT INTO kupac VALUES "
				+ "(?, ?, ?, ?, ?, ?) ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, kupac.getNaziv());
			ps.setString(2, kupac.getTelefon());
                        ps.setString(3, kupac.getAdresa());
                        ps.setString(4, kupac.getGrad());
                        ps.setString(5, kupac.getIme());
                        ps.setString(6, kupac.getPrezime());

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
    public boolean azurirajKupca(KupacDTO kupac) {
        
		boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "UPDATE kupac SET "
				+ "Naziv=? "
                                + "Telefon=? "
                                + "Adresa=? "
                                + "Grad=? "
                                + "Ime=? "
                                + "Prezime=? "
				+ "WHERE IdKupac=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, kupac.getNaziv());
			ps.setString(2, kupac.getTelefon());
                        ps.setString(3, kupac.getAdresa());
                        ps.setString(4, kupac.getGrad());
                        ps.setString(5, kupac.getIme());
                        ps.setString(6, kupac.getPrezime());
                        ps.setInt(7, kupac.getIdKupac());

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
    public boolean obrisiKupca(String kupac) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<KupacDTO> kupciIme(String ime) {
         ArrayList<KupacDTO> retVal = new ArrayList<KupacDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdKupac, Naziv, Ime, Prezime, Telefon, Adresa, Grad "
				+ "FROM kupac "
                                + "WHERE Ime LIKE ? "
				+ "ORDER BY IdKupac ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, ime);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new KupacDTO(rs.getInt("IdKupac"), rs.getString("Ime"), rs.getString("Prezime"), rs.getString("Naziv"), rs.getString("Telefon"), rs.getString("Adresa"), rs.getString("Grad")));
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
    public ArrayList<KupacDTO> kupciPrezime(String prezime) {
         ArrayList<KupacDTO> retVal = new ArrayList<KupacDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdKupac, Naziv, Ime, Prezime, Telefon, Adresa, Grad "
				+ "FROM kupac "
                                + "WHERE Prezime LIKE ? "
				+ "ORDER BY IdKupac ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, prezime);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new KupacDTO(rs.getInt("IdKupac"), rs.getString("Ime"), rs.getString("Prezime"), rs.getString("Naziv"), rs.getString("Telefon"), rs.getString("Adresa"), rs.getString("Grad")));
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
    public ArrayList<KupacDTO> sviPrivatni() {
         ArrayList<KupacDTO> retVal = new ArrayList<KupacDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdKupac, Naziv, Ime, Prezime, Telefon, Adresa, Grad "
				+ "FROM kupac "
                                + "WHERE Naziv IS NULL "
				+ "ORDER BY IdKupac ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new KupacDTO(rs.getInt("IdKupac"), rs.getString("Ime"), rs.getString("Prezime"), rs.getString("Naziv"), rs.getString("Telefon"), rs.getString("Adresa"), rs.getString("Grad")));
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
    public ArrayList<KupacDTO> sviPravni() {
         ArrayList<KupacDTO> retVal = new ArrayList<KupacDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdKupac, Naziv, Ime, Prezime, Telefon, Adresa, Grad "
				+ "FROM kupac "
                                + "AND Naziv IS NOT NULL "
				+ "ORDER BY IdKupac ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new KupacDTO(rs.getInt("IdKupac"), rs.getString("Ime"), rs.getString("Prezime"), rs.getString("Naziv"), rs.getString("Telefon"), rs.getString("Adresa"), rs.getString("Grad")));
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
