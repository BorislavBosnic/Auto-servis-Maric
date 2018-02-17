/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.DioDAO;
import data.dto.DioDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Aco
 */
public class MySQLDioDAO implements DioDAO{
    
    @Override
    public DioDTO dio(DioDTO dio) {
                DioDTO retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve "
				+ "FROM dio "
				+ "WHERE Sifra=? ";
                /*String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Sifra=?";*/
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, dio.getSifra());
			rs = ps.executeQuery();
                        
			if (rs.next())
				retVal = new DioDTO(rs.getInt("IdDio"), rs.getString("Sifra"), rs.getString("Naziv"), 
                                        rs.getString("VrstaGoriva"), rs.getInt("GodisteVozila"), rs.getBoolean("Novo"), 
                                        rs.getDouble("TrenutnaCijena"), rs.getInt("Kolicina"), rs.getBoolean("ZaSve"));
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
    public DioDTO getDio(DioDTO dio) {
                DioDTO retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		/*String query = "SELECT IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve "
				+ "FROM dio "
				+ "WHERE Sifra=? AND Naziv=? ";*/
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Sifra=? AND d.Naziv=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, dio.getSifra());
                        ps.setString(2, dio.getNaziv());
			rs = ps.executeQuery();
                        
			if (rs.next())
				retVal = new DioDTO(rs.getInt("IdDio"), rs.getString("Sifra"), rs.getString("Naziv"), 
                                        rs.getString("VrstaGoriva"), rs.getInt("GodisteVozila"), rs.getBoolean("Novo"), 
                                        rs.getDouble("TrenutnaCijena"), rs.getInt("Kolicina"), rs.getBoolean("ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model"));
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
    public DioDTO getDio(String sifra) {
                DioDTO retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		/*String query = "SELECT IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve "
				+ "FROM dio "
				+ "WHERE Sifra=? ";*/
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Sifra=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, sifra);
			rs = ps.executeQuery();
                        
			if (rs.next())
				retVal = new DioDTO(rs.getInt("IdDio"), rs.getString("Sifra"), rs.getString("Naziv"), 
                                        rs.getString("VrstaGoriva"), rs.getInt("GodisteVozila"), rs.getBoolean("Novo"), 
                                        rs.getDouble("TrenutnaCijena"), rs.getInt("Kolicina"), rs.getBoolean("ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model"));
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
    public DioDTO getDio(int id) {
                DioDTO retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		/*String query = "SELECT IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve "
				+ "FROM dio "
				+ "WHERE IdDio=? ";*/
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "+
                                "mv.IdModelVozila, Marka, Model FROM dio d "+
                                "INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio " +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila " +
                                "where d.IdDio=? ";
		
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setInt(1, id);
			rs = ps.executeQuery();
                        
			if (rs.next())
				retVal = new DioDTO(rs.getInt("IdDio"), rs.getString("Sifra"), rs.getString("Naziv"), 
                                        rs.getString("VrstaGoriva"), rs.getInt("GodisteVozila"), rs.getBoolean("Novo"), 
                                        rs.getDouble("TrenutnaCijena"), rs.getInt("Kolicina"), rs.getBoolean("ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model"));
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
    public DioDTO getDioo(String sifra, String naziv) {
                DioDTO retVal = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		/*String query = "SELECT IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve "
				+ "FROM dio "
				+ "WHERE Sifra=? AND Naziv=? ";*/
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Sifra=? AND d.Naziv=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, sifra);
                        ps.setString(2, naziv);
			rs = ps.executeQuery();
                        
			if (rs.next())
				retVal = new DioDTO(rs.getInt("IdDio"), rs.getString("Sifra"), rs.getString("Naziv"), 
                                        rs.getString("VrstaGoriva"), rs.getInt("GodisteVozila"), rs.getBoolean("Novo"), 
                                        rs.getDouble("TrenutnaCijena"), rs.getInt("Kolicina"), rs.getBoolean("ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model"));
                        //System.out.println(retVal.getSifra());
                        //System.out.println(retVal.getId());
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
    public ArrayList<DioDTO> getSviDijelovi(){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> getDijelovi(String naziv, Integer godiste, String marka, String model,
            String gorivo, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where mv.Marka=? AND mv.Model=? AND d.Naziv=? AND d.GodisteVozila=? AND d.VrstaGoriva=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, marka);
                        ps.setString(2, model);
                        ps.setString(3, naziv);
                        ps.setInt(4, godiste);
                        ps.setString(5, gorivo);
                        ps.setBoolean(6, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> getDijelovi(String naziv, Integer godiste, String marka, String gorivo, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where mv.Marka=? AND d.Naziv=? AND d.GodisteVozila=? AND d.VrstaGoriva=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, marka);
                        ps.setString(2, naziv);
                        ps.setInt(3, godiste);
                        ps.setString(4, gorivo);
                        ps.setBoolean(5, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> dijelovi(String naziv, Integer godiste, String marka, String model, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where mv.Marka=? AND mv.Model AND d.Naziv=? AND d.GodisteVozila=? AND d.VrstaGoriva=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, marka);
                        ps.setString(2, model);
                        ps.setString(3, naziv);
                        ps.setInt(4, godiste);
                        ps.setBoolean(5, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> dijelovi(String naziv, Integer godiste, String gorivo, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Naziv=? AND d.GodisteVozila=? AND d.VrstaGoriva=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, naziv);
                        ps.setInt(2, godiste);
                        ps.setString(3, gorivo);
                        ps.setBoolean(4, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> dijelovi(String naziv, Integer godiste, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Naziv=? AND d.GodisteVozila=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, naziv);
                        ps.setInt(2, godiste);
                        ps.setBoolean(3, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> getDijelovi(String naziv, Integer godiste, String marka, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where mv.Marka=? AND d.Naziv=? AND d.GodisteVozila=?  AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, marka);
                        ps.setString(2, naziv);
                        ps.setInt(3, godiste);
                        ps.setBoolean(4, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> dijelovibg(String naziv, String marka, String model, String gorivo, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Naziv=? AND mv.Marka=? AND mv.Model=? AND d.VrstaGoriva=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, naziv);
                        ps.setString(2, marka);
                        ps.setString(3, model);
                        ps.setString(4, gorivo);
                        ps.setBoolean(5, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> dijelovibg(String naziv, String marka, String model, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Naziv=? AND mv.Marka=? AND mv.Model=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, naziv);
                        ps.setString(2, marka);
                        ps.setString(3, model);
                        ps.setBoolean(4, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> dijelovibg(String naziv, String marka, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Naziv=? AND mv.Marka=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, naziv);
                        ps.setString(2, marka);
                        ps.setBoolean(3, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> dijelovibg2(String naziv, String marka, String gorivo, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Naziv=? AND mv.Marka=? AND d.VrstaGoriva=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, naziv);
                        ps.setString(2, marka);
                        ps.setString(3, gorivo);
                        ps.setBoolean(4, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> dijelovibg2(String naziv, String gorivo, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Naziv=? AND d.VrstaGoriva=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, naziv);
                        ps.setString(2, gorivo);
                        ps.setBoolean(3, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> getDijelovi(String gorivo, String marka, String model, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.VrstaGoriva=? AND mv.Marka=? AND mv.Model=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, gorivo);
                        ps.setString(2, marka);
                        ps.setString(3, model);
                        ps.setBoolean(4, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> getDijelovi(String gorivo, String marka, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.VrstaGoriva=? AND mv.Marka=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, gorivo);
                        ps.setString(2, marka);
                        ps.setBoolean(3, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public ArrayList<DioDTO> getDijeloviNaziv(String naziv, Boolean stanje){
                ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT d.IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve, "
                        + "mv.IdModelVozila, Marka, Model FROM dio d INNER JOIN dio_model_vozila dmv ON d.IdDio=dmv.IdDio\n" +
                                "INNER JOIN model_vozila mv ON dmv.IdModelVozila=mv.IdModelVozila\n" +
                                "where d.Naziv=? AND d.Novo=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
                        ps.setString(1, naziv);
                        ps.setBoolean(2, stanje);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("d.IdDio"), rs.getString("d.Sifra"), rs.getString("d.Naziv"), 
                                        rs.getString("d.VrstaGoriva"), rs.getInt("d.GodisteVozila"), rs.getBoolean("d.Novo"), 
                                        rs.getDouble("d.TrenutnaCijena"), rs.getInt("d.Kolicina"), rs.getBoolean("d.ZaSve"),
                                        rs.getString("mv.Marka"), rs.getString("mv.Model")));
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
    public boolean dodajDio(DioDTO dio) {
                boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "INSERT INTO dio(Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, "
                        + "Kolicina, ZaSve) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?) ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, dio.getNaziv());
			ps.setString(2, dio.getSifra());
                        ps.setObject(3, dio.getGodisteVozila());//int
                        ps.setBoolean(4, dio.getNovo());
                        ps.setString(5, dio.getVrstaGoriva());
                        ps.setObject(6, dio.getTrenutnaCijena());
                        ps.setObject(7, dio.getKolicina());//int
                        ps.setBoolean(8, true);
                        

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
    public boolean azurirajDio(DioDTO dio) {
         boolean retVal = false;
		Connection conn = null;
		PreparedStatement ps = null;

		String query = "UPDATE dio SET "
				+ "Sifra=?, "
                                + "Naziv=?, "
                                + "VrstaGoriva=?, "
                                + "GodisteVozila=?, "
                                + "Novo=?, "
                                + "TrenutnaCijena=?, "
                                + "Kolicina=?, "
                                + "ZaSve=? "
                                + "WHERE IdDio=? ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			ps.setString(1, dio.getSifra());
			ps.setString(2, dio.getNaziv());
                        ps.setString(3, dio.getVrstaGoriva());
                        ps.setObject(4, dio.getGodisteVozila());
                        ps.setBoolean(5, dio.getNovo());
                        ps.setObject(6, dio.getTrenutnaCijena());
                        ps.setObject(7, dio.getKolicina());
                        ps.setBoolean(8, true);
                        ps.setInt(9, dio.getId());

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
    public boolean obrisiDio(int idDio) {
        boolean retVal = false;
	Connection conn = null;
	PreparedStatement ps = null;

	String query = "DELETE FROM dio "
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

    @Override
    public ArrayList<DioDTO> getDijeloviZaSvaVozila() {
        ArrayList<DioDTO> retVal = new ArrayList<DioDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
                
                String query = "SELECT IdDio, Naziv, Sifra, GodisteVozila, Novo, VrstaGoriva, TrenutnaCijena, Kolicina, ZaSve FROM dio WHERE ZaSve=true ";
		try {
			conn = ConnectionPool.getInstance().checkOut();
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
                        
			while (rs.next()){
				retVal.add(new DioDTO(rs.getInt("IdDio"), rs.getString("Sifra"), rs.getString("Naziv"), 
                                        rs.getString("VrstaGoriva"), rs.getInt("GodisteVozila"), rs.getBoolean("Novo"), 
                                        rs.getDouble("TrenutnaCijena"), rs.getInt("Kolicina"), rs.getBoolean("ZaSve")));
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
