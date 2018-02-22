package data.dao.mysql;

import data.dao.ModelVozilaDAO;
import data.dto.KupacDTO;
import data.dto.ModelVozilaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLModelVozilaDAO implements ModelVozilaDAO {

    @Override
    public ArrayList<ModelVozilaDTO> sviModeli() {
       ArrayList<ModelVozilaDTO> retVal = new ArrayList<ModelVozilaDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdModelVozila, Marka, Model "
				+  "FROM model_vozila "
                                + "WHERE Aktivan!= false "
				+ "ORDER BY IdModelVozila ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new ModelVozilaDTO(rs.getInt("IdModelVozila"), rs.getString("Marka"), rs.getString("Model")));
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
    public ArrayList<ModelVozilaDTO> getModeli(String marka){
        ArrayList<ModelVozilaDTO> retVal = new ArrayList<ModelVozilaDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdModelVozila, Marka, Model "
                                + "FROM model_vozila "
				+ "WHERE Marka=? AND Aktivan!=false "
				+ "ORDER BY IdModelVozila ASC ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, marka);
			rs = ps.executeQuery();

			while (rs.next())
				retVal.add(new ModelVozilaDTO(rs.getInt("IdModelVozila"), rs.getString("Marka"), rs.getString("Model")));
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
    public boolean dodajModel(ModelVozilaDTO model) {
        boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "INSERT INTO model_vozila(Marka, Model) VALUES "
				+ "(?, ?) ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, model.getMarka());
			ps.setString(2, model.getModel());
             
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
    public boolean azurirajModel(ModelVozilaDTO model) {
        boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "UPDATE model_vozila SET "
				+ "Marka=?, "
                                + "Model=? "
				+ "WHERE IdModelVozila=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, model.getMarka());
			ps.setString(2, model.getModel());
                        ps.setInt(3, model.getIdModelVozila());

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
    public boolean obrisiModel(ModelVozilaDTO model) {
        boolean retVal = false;
	Connection conn = null;
	PreparedStatement ps = null;

	String query = "UPDATE model_vozila "
                     + "SET Aktivan=false "
                     + "WHERE IdModelVozila=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, model.getIdModelVozila());

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
    public ModelVozilaDTO model(String marka, String model) {
                ModelVozilaDTO retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdModelVozila, Marka, Model "
				+ "FROM model_vozila "
				+ "WHERE Marka LIKE ? "
                                + "AND Model LIKE ? AND Aktivan!=false ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, marka);
                        ps.setString(2, model);
			rs = ps.executeQuery();

			if (rs.next())
				retVal = new ModelVozilaDTO(rs.getInt("IdModelVozila"), rs.getString("Marka"), rs.getString("Model"));
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
    public ModelVozilaDTO model(int id) {
        ModelVozilaDTO retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdModelVozila, Marka, Model "
				+ "FROM model_vozila "
				+ "WHERE IdModelVozila=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeQuery();

			if (rs.next())
				retVal = new ModelVozilaDTO(rs.getInt("IdModelVozila"), rs.getString("Marka"), rs.getString("Model"));
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
    public boolean postojiLiVoziloSaOvimModelom(ModelVozilaDTO model) {
                boolean retVal = true;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "select IdModelVozila\n" +
                               "from vozilo\n" +
                                "where IdModelVozila=? AND Izbrisano!=true\n" +
                                "group by IdModelVozila\n" +
                                "having count(IdModelVozila) > 0;";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, model.getIdModelVozila());
			rs = ps.executeQuery();

			if (rs.next()){
                            
                       
                            return true;
                        }
                        else return false;

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
