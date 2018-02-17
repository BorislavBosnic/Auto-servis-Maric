/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.RadniNalogDAO;
import data.dao.RadniNalogRadnikDAO;
import data.dto.RadniNalogRadnikDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    
}
