/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.RadniNalogDioDAO;
import data.dto.RadniNalogDioDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author nikol
 */
public class MySQLRadniNalogDioDAO implements RadniNalogDioDAO
{
    public ArrayList<RadniNalogDioDTO> sviRadniNalogDijelovi()
    {
        ArrayList<RadniNalogDioDTO> retVal = new ArrayList<RadniNalogDioDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdRadniNalog, IdDio, Cijena, Kolicina "
                        + "FROM radni_nalog_dio "
                        + "ORDER BY IdRadniNalog ASC, IdDio ASC ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next())
                        retVal.add(new RadniNalogDioDTO(
                                rs.getInt("IdRadniNalog"),
                                rs.getInt("IdDio"),
                                rs.getDouble("Cijena"),
                                rs.getInt("Kolicina")));
        } catch (SQLException e) {
                e.printStackTrace();
                DBUtilities.getInstance().showSQLException(e);
        } finally {
                ConnectionPool.getInstance().checkIn(conn);
                DBUtilities.getInstance().close(ps, rs);
        }
        return retVal;
    }
    public ArrayList<RadniNalogDioDTO> radniNalogDioIdRadniNalog(int idRadniNalog)
    {
        ArrayList<RadniNalogDioDTO> retVal = new ArrayList<RadniNalogDioDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdRadniNalog, IdDio, Cijena, Kolicina "
                            + "FROM radni_nalog_dio "
                            + "WHERE IdRadniNalog=?";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, idRadniNalog);
                rs = ps.executeQuery();

                while (rs.next())
                        retVal.add(new RadniNalogDioDTO(
                                rs.getInt("IdRadniNalog"),
                                rs.getInt("IdDio"),
                                rs.getDouble("Cijena"),
                                rs.getInt("Kolicina")));
        } catch (SQLException e) {
                e.printStackTrace();
                DBUtilities.getInstance().showSQLException(e);
        } finally {
                ConnectionPool.getInstance().checkIn(conn);
                DBUtilities.getInstance().close(ps, rs);
        }
        return retVal;
    }
    public RadniNalogDioDTO radniNalogDio(int idRadniNalog, int idDio)
    {
        RadniNalogDioDTO retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdRadniNalog, IdDio, Cijena, Kolicina "
                            + "FROM radni_nalog_dio "
                            + "WHERE IdRadniNalog=? AND IdDio=?";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, idRadniNalog);
                ps.setInt(2, idDio);
                rs = ps.executeQuery();

                if (rs.next())
                        retVal = new RadniNalogDioDTO(
                                        rs.getInt("IdRadniNalog"),
                                        rs.getInt("IdDio"),
                                        rs.getDouble("Cijena"),
                                        rs.getInt("Kolicina"));
        } catch (SQLException e) {
                e.printStackTrace();
                DBUtilities.getInstance().showSQLException(e);
        } finally {
                ConnectionPool.getInstance().checkIn(conn);
                DBUtilities.getInstance().close(ps, rs);
        }
        return retVal;
    }
    public boolean dodajRadniNalogDio(RadniNalogDioDTO radniNalogDio)
    {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO radni_nalog_dio(IdRadniNalog, IdDio, Cijena, Kolicina)"
                        + "VALUES (?, ?, ?, ?) ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, radniNalogDio.getIdRadniNalog());
                ps.setInt(2, radniNalogDio.getIdDio());
                ps.setDouble(3, radniNalogDio.getCijena());
                ps.setInt(4, radniNalogDio.getKolicina());

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
    public boolean azurirajRadniNalogDio(RadniNalogDioDTO radniNalogDio)
    {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "UPDATE radni_nalog_dio SET "
                        + "IdRadniNalog=?, "
                        + "IdDio=?, "
                        + "Cijena=?, "
                        + "Kolicina=? "
                        + "WHERE IdRadniNalog=? "
                        + "AND IdDio=?";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, radniNalogDio.getIdRadniNalog());
                ps.setInt(2, radniNalogDio.getIdDio());
                ps.setDouble(3, radniNalogDio.getCijena());
                ps.setInt(4, radniNalogDio.getKolicina());
                ps.setInt(5, radniNalogDio.getIdRadniNalog());
                ps.setInt(6, radniNalogDio.getIdDio());

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
    public boolean obrisiRadniNalogDio(int idRadniNalog, int idDio)
    {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "DELETE FROM radni_nalog_dio "
                        + "WHERE IdRadniNalog=? AND IdDio=?";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, idRadniNalog);
                ps.setInt(2, idDio);

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
