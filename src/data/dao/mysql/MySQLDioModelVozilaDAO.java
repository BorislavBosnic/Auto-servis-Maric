/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.DioModelVozilaDAO;
import data.dto.DioModelVozilaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Aco
 */
public class MySQLDioModelVozilaDAO implements DioModelVozilaDAO{
    @Override
    public boolean dodajDioModelVozila(int idDio, int idVozila) {
                boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "INSERT INTO Dio_Model_Vozila(IdDio, IdModelVozila) VALUES "
				+ "(?, ?) ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, idDio);
			ps.setInt(2, idVozila);
                        

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
    public DioModelVozilaDTO getDioModelVozila(int idDio, int idVozila) {
                DioModelVozilaDTO retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdDio, IdModelVozila "
				+ "FROM dio_model_vozila "
				+ "WHERE IdDio=? AND IdModelVozila=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setInt(1, idDio);
                        ps.setInt(2, idVozila);
			rs = ps.executeQuery();
                        
			if (rs.next())
				retVal = new DioModelVozilaDTO(rs.getInt("IdDio"), rs.getInt("IdModelVozila"));
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
    public DioModelVozilaDTO getDioModelVozila(int idDio) {
                DioModelVozilaDTO retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdDio, IdModelVozila "
				+ "FROM dio_model_vozila "
				+ "WHERE IdDio=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setInt(1, idDio);
			rs = ps.executeQuery();
                        
			if (rs.next())
				retVal = new DioModelVozilaDTO(rs.getInt("IdDio"), rs.getInt("IdModelVozila"));
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
    public boolean obrisiDioModelVozila(int idDio) {
        boolean retVal = false;
	Connection conn = null;
	PreparedStatement ps = null;

	String query = "DELETE FROM dio_model_vozila "
                     + "WHERE IdDio=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, idDio);

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
