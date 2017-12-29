package data.dao.mysql;

import data.dao.VoziloDAO;
import data.dto.KupacDTO;
import data.dto.VoziloDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLVoziloDAO implements VoziloDAO {

    @Override
    public ArrayList<VoziloDTO> svaVozila() {
        ArrayList<VoziloDTO> retVal = new ArrayList<VoziloDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdVozilo, BrojRegistracije, Kilovat, Kubikaza, Godiste, IdKupac, IdModelVozila, VrstaGoriva "
				+ "FROM vozilo "
				+ "ORDER BY IdVozilo ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new VoziloDTO(rs.getInt("IdVozilo"), rs.getString("BrojRegistracije"), rs.getInt("Kilovat"), rs.getDouble("Kubikaza"), rs.getInt("Godiste"), rs.getInt("IdKupac"), rs.getInt("IdModelVozila"), rs.getString("VrstaGoriva")));
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
    public boolean dodajVozilo(VoziloDTO vozilo) {
         boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "INSERT INTO vozilo(IdVozilo, BrojRegistracije, Kilovat, Kubikaza, Godiste, IdKupac, IdModelVozila, VrstaGoriva) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?) ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, vozilo.getIdVozilo().toString());
			ps.setString(2, vozilo.getBrojRegistracije());
                        ps.setString(3, vozilo.getKilovat().toString());
                        ps.setString(4, vozilo.getKubikaza().toString());
                        ps.setString(5, vozilo.getGodiste().toString());
                        ps.setString(6, vozilo.getIdKupac().toString());
                        ps.setString(7, vozilo.getIdModelVozila().toString());
                        ps.setString(8, vozilo.getVrstaGoriva().toString());

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
    public boolean azurirajVozilo(VoziloDTO vozilo) {
        boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "UPDATE vozilo SET "
				+ "BrojRegistracije=? "
                                + "Kilovat=? "
                                + "Kubikaza=? "
                                + "Godiste=? "
                                + "IdKupac=? "
                                + "IdModelVozila=? "
                                + "VrstaGoriva=? "
				+ "WHERE IdKupac=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, vozilo.getBrojRegistracije());
			ps.setInt(2, vozilo.getKilovat());
                        ps.setDouble(3, vozilo.getKubikaza());
                        ps.setInt(4, vozilo.getGodiste());
                        ps.setInt(5, vozilo.getIdKupac());
                        ps.setInt(6, vozilo.getIdModelVozila());
                        ps.setString(7, vozilo.getVrstaGoriva());
                        ps.setInt(8, vozilo.getIdKupac());

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
    public boolean obrisiVozilo(VoziloDTO vozilo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
