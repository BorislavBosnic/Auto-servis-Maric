/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.TerminDAO;
import data.dto.TerminDTO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

/**
 *
 * @author nikol
 */
public class MySQLTerminDAO implements TerminDAO
{
    @Override
    public ArrayList<TerminDTO> sviTermini()
    {
        ArrayList<TerminDTO> retVal = new ArrayList<TerminDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdTermin, Datum, Vrijeme, Marka, Model, Ime, Prezime, BrojTelefona, DatumZakazivanja "
                        + "FROM termin "
                        + "ORDER BY IdTermin ASC ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                while (rs.next())
                        retVal.add(new TerminDTO(
                                rs.getInt("IdTermin"),
                                rs.getDate("Datum"),
                                rs.getTime("Vrijeme"),
                                rs.getString("Marka"),
                                rs.getString("Model"),
                                rs.getString("Ime"),
                                rs.getString("Prezime"),
                                rs.getString("BrojTelefona"),
                                rs.getDate("DatumZakazivanja")));
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
    public ArrayList<TerminDTO> sviTerminiDatumZakazivanja(Date datumZakazivanja)
    {
        ArrayList<TerminDTO> retVal = new ArrayList<TerminDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdTermin, Datum, Vrijeme, Marka, Model, Ime, Prezime, BrojTelefona, DatumZakazivanja "
                        + "FROM termin "
                        + "WHERE DatumZakazivanja=?"
                        + "ORDER BY IdTermin ASC ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setDate(1, datumZakazivanja);
                rs = ps.executeQuery();

                while (rs.next())
                        retVal.add(new TerminDTO(
                                rs.getInt("IdTermin"),
                                rs.getDate("Datum"),
                                rs.getTime("Vrijeme"),
                                rs.getString("Marka"),
                                rs.getString("Model"),
                                rs.getString("Ime"),
                                rs.getString("Prezime"),
                                rs.getString("BrojTelefona"),
                                rs.getDate("DatumZakazivanja")));
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
    public TerminDTO termin(int idTermin)
    {
        TerminDTO retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdTermin, Datum, Vrijeme, Marka, Model, Ime, Prezime, BrojTelefona, DatumZakazivanja "
                        + "FROM termin "
                        + "WHERE IdTermin=?"
                        + "ORDER BY IdTermin ASC ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, idTermin);
                rs = ps.executeQuery();

                if (rs.next())
                        retVal = new TerminDTO(
                                        rs.getInt("IdTermin"),
                                        rs.getDate("Datum"),
                                        rs.getTime("Vrijeme"),
                                        rs.getString("Marka"),
                                        rs.getString("Model"),
                                        rs.getString("Ime"),
                                        rs.getString("Prezime"),
                                        rs.getString("BrojTelefona"),
                                        rs.getDate("DatumZakazivanja"));
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
    public TerminDTO terminDatum(Date datum)
    {
        TerminDTO retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdTermin, Datum, Vrijeme, Marka, Model, Ime, Prezime, BrojTelefona, DatumZakazivanja "
                        + "FROM termin "
                        + "WHERE Datum=?"
                        + "ORDER BY IdTermin ASC ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setDate(1, datum);
                rs = ps.executeQuery();

                if (rs.next())
                        retVal = new TerminDTO(
                                        rs.getInt("IdTermin"),
                                        rs.getDate("Datum"),
                                        rs.getTime("Vrijeme"),
                                        rs.getString("Marka"),
                                        rs.getString("Model"),
                                        rs.getString("Ime"),
                                        rs.getString("Prezime"),
                                        rs.getString("BrojTelefona"),
                                        rs.getDate("DatumZakazivanja"));
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
    public TerminDTO terminVrijeme(Time vrijeme)
    {
        TerminDTO retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdTermin, Datum, Vrijeme, Marka, Model, Ime, Prezime, BrojTelefona, DatumZakazivanja "
                        + "FROM termin "
                        + "WHERE Vrijeme=?"
                        + "ORDER BY IdTermin ASC ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setTime(1, vrijeme);
                rs = ps.executeQuery();

                if (rs.next())
                        retVal = new TerminDTO(
                                        rs.getInt("IdTermin"),
                                        rs.getDate("Datum"),
                                        rs.getTime("Vrijeme"),
                                        rs.getString("Marka"),
                                        rs.getString("Model"),
                                        rs.getString("Ime"),
                                        rs.getString("Prezime"),
                                        rs.getString("BrojTelefona"),
                                        rs.getDate("DatumZakazivanja"));
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
    public TerminDTO terminDatumVrijeme(Date datum, Time vrijeme)
    {
        TerminDTO retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdTermin, Datum, Vrijeme, Marka, Model, Ime, Prezime, BrojTelefona, DatumZakazivanja "
                        + "FROM termin "
                        + "WHERE Datum=? AND Vrijeme=?"
                        + "ORDER BY IdTermin ASC ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setDate(1, datum);
                ps.setTime(2, vrijeme);
                rs = ps.executeQuery();

                if (rs.next())
                        retVal = new TerminDTO(
                                        rs.getInt("IdTermin"),
                                        rs.getDate("Datum"),
                                        rs.getTime("Vrijeme"),
                                        rs.getString("Marka"),
                                        rs.getString("Model"),
                                        rs.getString("Ime"),
                                        rs.getString("Prezime"),
                                        rs.getString("BrojTelefona"),
                                        rs.getDate("DatumZakazivanja"));
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
    public boolean dodajTermin(TerminDTO termin)
    {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO termin(IdTermin, Datum, Vrijeme, Marka, Model, Ime, Prezime, BrojTelefona, DatumZakazivanja) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, termin.getIdTermin());
                ps.setDate(2, termin.getDatum());
                ps.setTime(3, termin.getVrijeme());
                ps.setString(4, termin.getMarka());
                ps.setString(5, termin.getModel());
                ps.setString(6, termin.getIme());
                ps.setString(7, termin.getPrezime());
                ps.setString(8, termin.getBrojTelefona());
                ps.setDate(9, termin.getDatumZakazivanja());

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
    public boolean azurirajTerminId(TerminDTO termin)
    {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "UPDATE termin SET"
                        + "IdTermin=?, "
                        + "Datum=?, "
                        + "Vrijeme=?, "
                        + "Marka=?, "
                        + "Model=?, "
                        + "Ime=?, "
                        + "Prezime=?, "
                        + "BrojTelefona=?,"
                        + "DatumZakazivanja=?";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, termin.getIdTermin());
                ps.setDate(2, termin.getDatum());
                ps.setTime(3, termin.getVrijeme());
                ps.setString(4, termin.getMarka());
                ps.setString(5, termin.getModel());
                ps.setString(6, termin.getIme());
                ps.setString(7, termin.getPrezime());
                ps.setString(8, termin.getBrojTelefona());
                ps.setDate(9, termin.getDatumZakazivanja());

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
    public boolean obrisiTermin(int idTermina)
    {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "DELETE FROM termin "
                        + "WHERE IdTermin=?";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setInt(1, idTermina);

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
    public boolean obrisiTerminDatumVrijeme(Date datum, Time vrijeme)
    {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "DELETE FROM termin "
                        + "WHERE Datum=? AND Vrijeme=?";
        try {
                conn = ConnectionPool.getInstance().checkOut();
                ps = conn.prepareStatement(query);
                ps.setDate(1, datum);
                ps.setTime(2, vrijeme);

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
