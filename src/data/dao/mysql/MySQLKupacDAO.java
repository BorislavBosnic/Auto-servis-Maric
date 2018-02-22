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
                                + "AND Prezime LIKE ? WHERE Aktivan!=false "
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
                                + "WHERE Naziv LIKE ? WHERE Aktivan!=false "
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
				+ "FROM kupac WHERE Aktivan!=false "
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

		String query = "INSERT INTO kupac(Naziv, Telefon, Adresa, Grad, Ime, Prezime) VALUES "
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
				+ "Naziv=?, "
                                + "Telefon=?, "
                                + "Adresa=?, "
                                + "Grad=?, "
                                + "Ime=?, "
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
    public boolean obrisiKupca(KupacDTO kupac) {
         boolean retVal = false;
	Connection conn = null;
	PreparedStatement ps = null;
        
        if(kupac.getNaziv() == null){
            String query = "DELETE FROM kupac "
                     + "WHERE Ime=? "
                     + "AND Prezime=? ";
            
            try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, kupac.getIme());
                        ps.setString(2, kupac.getPrezime());

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
        else{
             String query = "DELETE FROM kupac "
                     + "WHERE Naziv=? AND Telefon=? AND Adresa=? AND Grad=? ";
             
             try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, kupac.getNaziv());
                        ps.setString(2, kupac.getTelefon());
                        ps.setString(3, kupac.getAdresa());
                        ps.setString(4, kupac.getGrad());

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
    }

    @Override
    public ArrayList<KupacDTO> kupciIme(String ime) {
         ArrayList<KupacDTO> retVal = new ArrayList<KupacDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdKupac, Naziv, Ime, Prezime, Telefon, Adresa, Grad "
				+ "FROM kupac "
                                + "WHERE Ime LIKE CONCAT(?,'%') AND Aktivan!=false "
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
                                + "WHERE Prezime LIKE CONCAT(?,'%') AND Aktivan!=false "
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
                                + "WHERE Naziv IS NULL AND Aktivan!=false "
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
                                + "WHERE Naziv IS NOT NULL AND Aktivan!=false "
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
    public KupacDTO kupac(int id) {
               KupacDTO retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdKupac, Naziv, Telefon, Adresa, Grad, Ime, Prezime "
				+ "FROM kupac "
				+ "WHERE IdKupac=? AND Aktivan!=false";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();

			if (rs.next())
				retVal = new KupacDTO(rs.getInt("IdKupac"), rs.getString("Ime"), rs.getString("Prezime"), rs.getString("Naziv"), rs.getString("Telefon"), rs.getString("Adresa"), rs.getString("Grad"));
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
    public boolean obrisiKupca(int id) {
        boolean retVal = false;
	Connection conn = null;
	PreparedStatement ps = null;
        
        
            String query = "UPDATE kupac "
                     + "SET Aktivan=false "
                     + "WHERE IdKupac=? ";
            
            try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, id);

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
        
    
}
