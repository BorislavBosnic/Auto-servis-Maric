/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.ProdanDioDAO;
import data.dto.DioDTO;
import data.dto.ProdanDioDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aco
 */
public class MySQLProdanDioDAO implements ProdanDioDAO {
    @Override
    public boolean dodajProdanDio(ProdanDioDTO dio) {
                boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "INSERT INTO Prodan_Dio(IdDio, CijenaProdaje, Kolicina, Datum) VALUES "
				+ "(?, ?, ?, ?) ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setObject(1, dio.getIdDio());
			ps.setObject(2, dio.getCijena());
                        ps.setObject(3, dio.getKolicina());//int
                        ps.setDate(4, dio.getDatum());
                        

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
    public ArrayList<ProdanDioDTO> getSviProdaniDijelovi(){
                ArrayList<ProdanDioDTO> retVal = new ArrayList<ProdanDioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdProdanDio, IdDio, CijenaProdaje, Kolicina, Datum "+
                                "FROM Prodan_Dio d ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new ProdanDioDTO(rs.getInt("d.IdProdanDio"), rs.getInt("d.IdDio"),
                                        rs.getDouble("CijenaProdaje"), rs.getInt("Kolicina"), rs.getDate("Datum")));
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
    public ArrayList<ProdanDioDTO> getProdaniDijelovi(Integer idDio){
                ArrayList<ProdanDioDTO> retVal = new ArrayList<ProdanDioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdProdanDio, IdDio, CijenaProdaje, Kolicina, Datum "
                               + "FROM prodan_dio d "
                               + "where d.IdDio=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setInt(1, idDio);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new ProdanDioDTO(rs.getInt("d.IdProdanDio"), rs.getInt("d.IdDio"), 
                                        rs.getDouble("CijenaProdaje"), rs.getInt("Kolicina"), rs.getDate("Datum")));
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
}
