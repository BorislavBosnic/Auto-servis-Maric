/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.FakturaDAO;
import data.dto.FakturaDTO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author nikol
 */
public class MySQLFakturaDAO implements FakturaDAO
{
    @Override
    public ArrayList<FakturaDTO> sveFakture()
    {
        ArrayList<FakturaDTO> retVal = new ArrayList<FakturaDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdFaktura, DatumIzdavanja, IdRadniNalog, Iznos, VrijemeRada "
                        + "FROM faktura "
                        + "ORDER BY IdFaktura ASC ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next())
                        retVal.add(new FakturaDTO(
                                rs.getInt("IdFaktura"),
                                rs.getDate("DatumIzdavanja"),
                                rs.getInt("IdRadniNalog"),
                                rs.getDouble("Iznos"),
                                rs.getInt("VrijemeRada")));
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
    public FakturaDTO faktura(int idFaktura)
    {
        FakturaDTO retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdFaktura, DatumIzdavanja, IdRadniNalog, Iznos, VrijemeRada "
                            + "FROM faktura "
                            + "WHERE IdFaktura=? ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, idFaktura);
                rs = ps.executeQuery();

                if (rs.next())
                        retVal = new FakturaDTO(
                                        rs.getInt("IdFaktura"),
                                        rs.getDate("DatumIzdavanja"),
                                        rs.getInt("IdRadniNalog"),
                                        rs.getDouble("Iznos"),
                                        rs.getInt("VrijemeRada"));
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
    public FakturaDTO fakturaRadniNalog(int idRadnogNaloga)
    {
        FakturaDTO retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdFaktura, DatumIzdavanja, IdRadniNalog, Iznos, VrijemeRada "
                            + "FROM faktura "
                            + "WHERE IdRadniNalog=? ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, idRadnogNaloga);
                rs = ps.executeQuery();

                if (rs.next())
                        retVal = new FakturaDTO(
                                        rs.getInt("IdFaktura"),
                                        rs.getDate("DatumIzdavanja"),
                                        rs.getInt("IdRadniNalog"),
                                        rs.getDouble("Iznos"),
                                        rs.getInt("VrijemeRada"));
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
    public FakturaDTO fakturaDatum(Date datumFakturisanja)
    {
        FakturaDTO retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdFaktura, DatumIzdavanja, IdRadniNalog, Iznos, VrijemeRada "
                            + "FROM faktura "
                            + "WHERE DatumIzdavanja=? ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setDate(1, datumFakturisanja);
                rs = ps.executeQuery();

                if (rs.next())
                        retVal = new FakturaDTO(
                                        rs.getInt("IdFaktura"),
                                        rs.getDate("DatumIzdavanja"),
                                        rs.getInt("IdRadniNalog"),
                                        rs.getDouble("Iznos"),
                                        rs.getInt("VrijemeRada"));
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
    public boolean dodajFakturu(FakturaDTO faktura)
    {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO faktura(DatumIzdavanja, IdRadniNalog, Iznos, VrijemeRada)"
                        + "VALUES (?, ?, ?, ?) ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                //ps.setInt(1, faktura.getIdFaktura());
                ps.setDate(1, faktura.getDatumIzdavanja());
                ps.setInt(2, faktura.getIdRadniNalog());
                ps.setDouble(3, faktura.getIznos());
                ps.setInt(4, faktura.getVrijemeRada());

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
    public boolean azurirajFakturu(FakturaDTO faktura)
    {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "UPDATE faktura SET "
                        + "IdFaktura=?, "
                        + "DatumIzdavanja=?, "
                        + "IdRadniNalog=?, "
                        + "Iznos=?, "
                        + "VrijemeRada=? ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, faktura.getIdFaktura());
                ps.setDate(2, faktura.getDatumIzdavanja());
                ps.setInt(3, faktura.getIdRadniNalog());
                ps.setDouble(4, faktura.getIznos());
                ps.setInt(5, faktura.getVrijemeRada());

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
    public boolean obrisiFakturu(int idFaktura)
    {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "DELETE FROM faktura "
                        + "WHERE IdFaktura=?";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, idFaktura);

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
    public int getMaxID()
    {
        int retVal = -1;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT max(IdFaktura)"
                            + "FROM faktura ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                if (rs.next())retVal = rs.getInt(1);
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
