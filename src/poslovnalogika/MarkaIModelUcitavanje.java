/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import data.dao.mysql.ConnectionPool;
import data.dao.mysql.DBUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author haker
 */
public class MarkaIModelUcitavanje {
    
    public static ArrayList<String> ucitajSveMarke(){
        ArrayList<String> retVal = new ArrayList<String>()
                ;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT DISTINCT Marka FROM model_vozila";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        rs=ps.executeQuery();
                        
			while(rs.next())
				retVal.add(rs.getString("Marka"));
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtilities.getInstance().showSQLException(e);
		} finally {
			ConnectionPool.getInstance().checkIn(conn);
			DBUtilities.getInstance().close(ps, rs);
		}
		return retVal;
    }
    
     public static ArrayList<String> ucitajSveModele(String marka){
        ArrayList<String> retVal = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT DISTINCT Model FROM model_vozila WHERE Marka=?";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1,marka );
                        rs=ps.executeQuery();
                        
			while(rs.next())
				retVal.add(rs.getString("Model"));
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
