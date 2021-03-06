/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.RadniNalogDAO;
import data.dao.RadniNalogRadnikDAO;
import data.dto.RadniNalogRadnikDTO;
import data.dto.VoziloDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author DulleX
 */
public class MySQLRadniNalogRadnikDAO implements RadniNalogRadnikDAO{

    @Override
    public boolean dodajRadniNalogRadnik(RadniNalogRadnikDTO rnr) {
                boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "INSERT INTO radni_nalog_radnik(IdRadniNalog, IdRadnik, Opis) VALUES "
				+ "(?, ?, ?) ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, rnr.getIdRadniNalog());
			ps.setInt(2, rnr.getIdRadnik());
                        ps.setString(3, rnr.getOpis());

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
    public ArrayList<RadniNalogRadnikDTO> radniciNaRadnomNalogu(int idRadnogNaloga) {
                ArrayList<RadniNalogRadnikDTO> retVal = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdRadniNalog, IdRadnik, Opis "
				+ "FROM radni_nalog_radnik "
				+ "WHERE IdRadniNalog=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, idRadnogNaloga);
			rs = ps.executeQuery();
                        
                        
			while (rs.next())
				retVal.add(new RadniNalogRadnikDTO(rs.getInt("IdRadniNalog"), rs.getInt("IdRadnik"), rs.getString("Opis")));
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
    public boolean postojiLiZapis(int idRadnogNaloga, int idRadnik) {
                boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdRadniNalog, IdRadnik, Opis "
				+ "FROM radni_nalog_radnik "
				+ "WHERE IdRadniNalog=? AND IdRadnik=?";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, idRadnogNaloga);
                        ps.setInt(2, idRadnik);
			rs = ps.executeQuery();
                        
			if (rs.next())
                        {
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

    @Override
    public boolean izbrisi(RadniNalogRadnikDTO rnr) {
                boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "DELETE from radni_nalog_radnik where IdRadniNalog=? AND IdRadnik=?";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setInt(1, rnr.getIdRadniNalog());
                        ps.setInt(2, rnr.getIdRadnik());
                        
                     
                        
			retVal = ps.executeUpdate() == 1;
                        
                        return retVal;
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
