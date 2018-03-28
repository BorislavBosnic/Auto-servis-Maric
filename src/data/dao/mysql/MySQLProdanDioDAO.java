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
    public ArrayList<ProdanDioDTO> getSviProdaniDijelovi() {
        ArrayList<ProdanDioDTO> retVal = new ArrayList<ProdanDioDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT pd.IdProdanDio,IdDio,Sifra,Naziv, CijenaProdaje, pd.Kolicina, Datum "
                + "FROM Prodan_Dio pd INNER JOIN Dio d USING(IdDio) ";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                retVal.add(new ProdanDioDTO(rs.getInt("pd.IdProdanDio"), rs.getInt("IdDio"), rs.getString("Sifra"), rs.getString("Naziv"),
                        rs.getDouble("CijenaProdaje"), rs.getInt("pd.Kolicina"), rs.getDate("Datum")));
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
    public ArrayList<ProdanDioDTO> traziProdanDio(ProdanDioDTO parametri) {
        ArrayList<ProdanDioDTO> retVal = new ArrayList<ProdanDioDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT pd.IdProdanDio,IdDio,Sifra,Naziv, CijenaProdaje, pd.Kolicina, Datum "
                + "FROM Prodan_Dio pd INNER JOIN Dio d USING(IdDio)";
        boolean flag = false;
        if (parametri.getId() != null) {
            query += " WHERE pd.IdProdanDio=?";
            flag = true;
        }
        if (parametri.getNaziv() != null) {
            if (flag) {
                query += " AND Naziv LIKE ?";
            } else {
                query += " WHERE Naziv LIKE ?";
                flag = true;
            }
        }
        if (parametri.getSifra() != null) {
            if (flag) {
                query += " AND Sifra LIKE ?";
            } else {
                query += " WHERE Sifra LIKE ?";
                flag = true;
            }
        }

        if (parametri.getKolicina() != null) {
            if (flag) {
                query += " AND pd.Kolicina=?";
            } else {
                query += " WHERE pd.Kolicina=?";
                flag = true;
            }
        }
        if (parametri.getDatum() != null) {
            if (flag) {
                query += " AND Datum=?";
            } else {
                query += " WHERE Datum=?";
                flag = true;
            }
        }
        if (parametri.getCijena() != null) {
            if (flag) {
                query += " AND CijenaProdaje=?";
            } else {
                query += " WHERE CijenaProdaje=?";
                flag = true;
            }
        }
        query+=";";
        System.out.println(query);
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            int brojac = 1;
            if (parametri.getId() != null) {
                ps.setInt(brojac, parametri.getId());
                brojac++;
            }
            if (parametri.getNaziv() != null) {
                ps.setString(brojac, "%" + parametri.getNaziv() + "%");
                brojac++;
            }
            if (parametri.getSifra() != null) {
                ps.setString(brojac, "%" + parametri.getSifra() + "%");
                brojac++;
            }
            if (parametri.getKolicina() != null) {
                ps.setInt(brojac, parametri.getKolicina());
                brojac++;
            }
            if (parametri.getDatum() != null) {
                ps.setDate(brojac, parametri.getDatum());
                brojac++;
            }
            if (parametri.getCijena() != null) {
                ps.setDouble(brojac, parametri.getCijena());
                brojac++;
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                retVal.add(new ProdanDioDTO(rs.getInt("pd.IdProdanDio"), rs.getInt("IdDio"), rs.getString("Sifra"), rs.getString("Naziv"),
                        rs.getDouble("CijenaProdaje"), rs.getInt("pd.Kolicina"), rs.getDate("Datum")));
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
