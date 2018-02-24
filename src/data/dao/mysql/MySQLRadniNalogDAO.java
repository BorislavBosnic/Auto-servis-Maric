/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao.mysql;

import data.dao.RadniNalogDAO;
import data.dto.ModelVozilaDTO;
import data.dto.RadniNalogDTO;
import data.dto.RadniNalogParametri;
import data.dto.RezultatRNPretrazivanje;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author HP BOOK
 */
public class MySQLRadniNalogDAO implements RadniNalogDAO {

    //kod racunanja sume ne provjeravam flag iz radnog naloga da li je usluga placena jer u tom slucaju ne bi postojala faktura, to rjesava inner join
    //cekam Marica da javi da li postoje fakture sa odgodjenim placanjem, u kojem slucaju bi morali dodati novo polje - datum uplate
    public double getSumaCijenaUsluga(Date datumOd, Date datumDo) {
        double sumaCijenaUsluga = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "select sum(CijenaUsluge) as suma from faktura inner join radni_nalog "
                + "using (idRadniNalog)"
                + "where Placeno=1 && DatumIzdavanja >= ? and DatumIzdavanja <=  ? ";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(datumOd.getTime()));
            ps.setDate(2, new java.sql.Date(datumDo.getTime()));

            rs = ps.executeQuery();

            while (rs.next()) {
                sumaCijenaUsluga = rs.getDouble("suma");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtilities.getInstance().showSQLException(e);
        } finally {
            ConnectionPool.getInstance().checkIn(conn);
            DBUtilities.getInstance().close(ps, rs);
        }

        return sumaCijenaUsluga;
    }

    public double getSumaCijenaDijelova(Date datumOd, Date datumDo) {
        double sumaCijenaDijelova = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "select * from prodan_dio inner join dio using (idDio)"
                + "where Datum >= ? and Datum <= ?";

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(datumOd.getTime()));
            ps.setDate(2, new java.sql.Date(datumDo.getTime()));

            rs = ps.executeQuery();

            while (rs.next()) {
                sumaCijenaDijelova += (rs.getDouble("CijenaProdaje") - rs.getDouble("TrenutnaCijena")) * rs.getDouble("Kolicina");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtilities.getInstance().showSQLException(e);
        } finally {
            ConnectionPool.getInstance().checkIn(conn);
            DBUtilities.getInstance().close(ps, rs);
        }
        return sumaCijenaDijelova;

    }

    public int getBrojPopravki(Date datumOd, Date datumDo) {
        int brojPopravki = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "select count(*) as brojPopravki from radni_nalog where DatumZatvaranjaNaloga is not null and "
                + " DatumZatvaranjaNaloga >= ? and DatumZatvaranjaNaloga <= ?";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(datumOd.getTime()));
            ps.setDate(2, new java.sql.Date(datumDo.getTime()));

            rs = ps.executeQuery();

            while (rs.next()) {
                brojPopravki = rs.getInt("brojPopravki");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtilities.getInstance().showSQLException(e);
        } finally {
            ConnectionPool.getInstance().checkIn(conn);
            DBUtilities.getInstance().close(ps, rs);
        }

        return brojPopravki;
    }

    public int getBrojAutaNaStanju() {
        int brojAutaNaStanju = 0;
        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;

        String query = "select  count(*) as brojAuta from radni_nalog left outer join faktura using (idRadniNalog) where IdFaktura is null;";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            s = conn.createStatement();
            rs = s.executeQuery(query);

            while (rs.next()) {
                brojAutaNaStanju = rs.getInt("brojAuta");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtilities.getInstance().showSQLException(e);
        } finally {
            ConnectionPool.getInstance().checkIn(conn);
            DBUtilities.getInstance().close(s, rs);
        }

        return brojAutaNaStanju;
    }

    public int getBrojAutaKojaCekajuPopravku() {
        int brojAutaKojaCekajuPopravku = 0;
        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;

        String query = "select count(*) as brojAuta from radni_nalog where DatumZatvaranjaNaloga is  null";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            s = conn.createStatement();
            rs = s.executeQuery(query);

            while (rs.next()) {
                brojAutaKojaCekajuPopravku = rs.getInt("brojAuta");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtilities.getInstance().showSQLException(e);
        } finally {
            ConnectionPool.getInstance().checkIn(conn);
            DBUtilities.getInstance().close(s, rs);
        }

        return brojAutaKojaCekajuPopravku;
    }

      public int getBrojPlacenihFaktura() {
        int brojPlacenihFaktura = 0;
        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;

        String query = "select count(*) as brojPlacenihFaktura from faktura inner join radni_nalog using (IdRadniNalog) where Placeno=1";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            s = conn.createStatement();
            rs = s.executeQuery(query);

            while (rs.next()) {
                brojPlacenihFaktura = rs.getInt("brojPlacenihFaktura");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtilities.getInstance().showSQLException(e);
        } finally {
            ConnectionPool.getInstance().checkIn(conn);
            DBUtilities.getInstance().close(s, rs);
        }

        return brojPlacenihFaktura;
    }
      
      
      public int getBrojFaktura() {
        int brojFaktura = 0;
        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;

        String query = "select count(*) as brojFaktura from faktura";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            s = conn.createStatement();
            rs = s.executeQuery(query);

            while (rs.next()) {
                brojFaktura = rs.getInt("brojFaktura");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtilities.getInstance().showSQLException(e);
        } finally {
            ConnectionPool.getInstance().checkIn(conn);
            DBUtilities.getInstance().close(s, rs);
        }

        return brojFaktura;
    }
      
       public boolean zatvoriRadniNalog(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "update radni_nalog set DatumZatvaranjaNaloga = ? where IdRadniNalog = ? and DatumZatvaranjaNaloga is null";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            ps.setInt(2, id);

             if(ps.executeUpdate() == 1)
                 return true;

        } catch (SQLException e) {
            e.printStackTrace();
            DBUtilities.getInstance().showSQLException(e);
        } finally {
            ConnectionPool.getInstance().checkIn(conn);
            DBUtilities.getInstance().close(ps, rs);
        }
          return false;

    }
      
      
    @Override
    public ArrayList<RadniNalogDTO> getRadniNalozi() {
        ArrayList<RadniNalogDTO> retVal = new ArrayList<RadniNalogDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdRadniNalog, Placeno, DatumOtvaranjaNaloga, DatumZatvaranjaNaloga, IdVozilo, Troskovi, Kilometraza, OpisProblema, PredvidjenoVrijemeZavrsetka, CijenaUsluge, Izbrisano "
                + "FROM radni_nalog WHERE Izbrisano!=true "
                + "ORDER BY DatumOtvaranjaNaloga ASC ";

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                retVal.add(new RadniNalogDTO(rs.getBoolean("Placeno"), rs.getDate("DatumOtvaranjaNaloga"), rs.getDate("DatumZatvaranjaNaloga"), rs.getInt("IdVozilo"), rs.getDouble("Troskovi"), rs.getInt("Kilometraza"), rs.getDate("PredvidjenoVrijemeZavrsetka"), rs.getDouble("CijenaUsluge"), rs.getString("OpisProblema"), rs.getInt("IdRadniNalog"), rs.getBoolean("Izbrisano")));
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
    public ArrayList<RadniNalogDTO> radniNaloziVozila(int idVozila){
        ArrayList<RadniNalogDTO> retVal = new ArrayList<RadniNalogDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdRadniNalog, Placeno, DatumOtvaranjaNaloga, DatumZatvaranjaNaloga, IdVozilo, Troskovi, Kilometraza, OpisProblema, PredvidjenoVrijemeZavrsetka, CijenaUsluge, Izbrisano "
                + "FROM radni_nalog "
                + "WHERE IdVozilo=? AND Izbrisano!=true "
                + "ORDER BY DatumOtvaranjaNaloga ASC ";

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setObject(1, idVozila);
            rs = ps.executeQuery();

            while (rs.next()) {
                retVal.add(new RadniNalogDTO(rs.getBoolean("Placeno"), rs.getDate("DatumOtvaranjaNaloga"), rs.getDate("DatumZatvaranjaNaloga"), rs.getInt("IdVozilo"), rs.getDouble("Troskovi"), rs.getInt("Kilometraza"), rs.getDate("PredvidjenoVrijemeZavrsetka"), rs.getDouble("CijenaUsluge"), rs.getString("OpisProblema"), rs.getInt("IdRadniNalog"), rs.getBoolean("Izbrisano")));
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
    public ArrayList<RadniNalogDTO> getRadniNalozi(int id) {
        ArrayList<RadniNalogDTO> retVal = new ArrayList<RadniNalogDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdRadniNalog, Placeno, DatumOtvaranjaNaloga, DatumZatvaranjaNaloga, IdVozilo, Troskovi, Kilometraza, OpisProblema, PredvidjenoVrijemeZavrsetka, CijenaUsluge, Izbrisano "
                + "FROM radni_nalog "
                + "WHERE IdVozilo=? AND Izbrisano!=true "
                + "ORDER BY DatumOtvaranjaNaloga ASC ";

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setObject(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                retVal.add(new RadniNalogDTO(rs.getBoolean("Placeno"), rs.getDate("DatumOtvaranjaNaloga"), rs.getDate("DatumZatvaranjaNaloga"), rs.getInt("IdVozilo"), rs.getDouble("Troskovi"), rs.getInt("Kilometraza"), rs.getDate("PredvidjenoVrijemeZavrsetka"), rs.getDouble("CijenaUsluge"), rs.getString("OpisProblema"), rs.getInt("IdRadniNalog"), rs.getBoolean("Izbrisano")));
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
    public ArrayList<RadniNalogDTO> getRadniNalozi(Date dat1, Date dat2) {
        ArrayList<RadniNalogDTO> retVal = new ArrayList<RadniNalogDTO>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdRadniNalog, Placeno, DatumOtvaranjaNaloga, DatumZatvaranjaNaloga, IdVozilo, Troskovi, Kilometraza, OpisProblema, PredvidjenoVrijemeZavrsetka, CijenaUsluge, Izbrisano "
                + "FROM radni_nalog WHERE PredvidjenoVrijemeZavrsetka>=? AND PredvidjenoVrijemeZavrsetka<=? "
                + "ORDER BY DatumOtvaranjaNaloga ASC ";

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(dat1.getTime()));
            ps.setDate(2, new java.sql.Date(dat2.getTime()));
            rs = ps.executeQuery();

            while (rs.next()) {
                retVal.add(new RadniNalogDTO(rs.getBoolean("Placeno"), rs.getDate("DatumOtvaranjaNaloga"), rs.getDate("DatumZatvaranjaNaloga"), rs.getInt("IdVozilo"), rs.getDouble("Troskovi"), rs.getInt("Kilometraza"), rs.getDate("PredvidjenoVrijemeZavrsetka"), rs.getDouble("CijenaUsluge"), rs.getString("OpisProblema"), rs.getInt("IdRadniNalog"), rs.getBoolean("Izbrisano")));
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
    public boolean dodajRadniNalog(RadniNalogDTO nalog) {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO radni_nalog(Placeno, DatumOtvaranjaNaloga, DatumZatvaranjaNaloga, IdVozilo, Troskovi, Kilometraza, PredvidjenoVrijemeZavrsetka, CijenaUsluge, OpisProblema) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, nalog.isPlaceno());
            ps.setObject(2, nalog.getDatumOtvaranjaNaloga());
            ps.setObject(3, nalog.getDatumZatvaranjaNaloga());
            ps.setInt(4, nalog.getIdVozilo());
            ps.setObject(5, nalog.getTroskovi());
            ps.setObject(6, nalog.getKilometraza());
            ps.setObject(7, nalog.getPredvidjenoVrijemeZavrsetka());
            ps.setObject(8, nalog.getCijenaUsluge());
            ps.setObject(9, nalog.getOpisProblema());

            retVal = ps.executeUpdate() == 1;

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    nalog.setIdRadniNalog(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

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
    public boolean azurirajRadniNalog(RadniNalogDTO nalog) {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "UPDATE radni_nalog SET "
                + "Placeno=?, "
                + "DatumOtvaranjaNaloga=?, "
                + "DatumZatvaranjaNaloga=?, "
                + "Troskovi=?, "
                + "Kilometraza=?, "
                + "OpisProblema=?, "
                + "PredvidjenoVrijemeZavrsetka=?, "
                + "CijenaUsluge=? "
                + "WHERE IdRadniNalog=? ";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setObject(1, nalog.isPlaceno());
            ps.setObject(2, nalog.getDatumOtvaranjaNaloga());
            ps.setObject(3, nalog.getDatumZatvaranjaNaloga());
            ps.setObject(4, nalog.getTroskovi());
            ps.setObject(5, nalog.getKilometraza());
            ps.setObject(6, nalog.getOpisProblema());
            ps.setObject(7, nalog.getPredvidjenoVrijemeZavrsetka());
            ps.setObject(8, nalog.getCijenaUsluge());
            ps.setInt(9, nalog.getIdRadniNalog());
            //System.out.println(ps.toString());
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
    public RadniNalogDTO getRadniNalog(int id) {
        RadniNalogDTO retVal = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT IdRadniNalog, Placeno, DatumOtvaranjaNaloga, DatumZatvaranjaNaloga, IdVozilo, Troskovi, Kilometraza, OpisProblema, PredvidjenoVrijemeZavrsetka, CijenaUsluge, Izbrisano "
                + "FROM radni_nalog "
                + "WHERE IdRadniNalog=?;";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            //public RadniNalogDTO(boolean placeno, java.sql.Date datumOtvaranjaNaloga, java.sql.Date datumZatvaranjaNaloga, int idVozilo, double troskovi, int kilometraza, java.sql.Date predvidjenoVrijemeZavrsetka, double cijenaUsluge) {
            while (rs.next()) {
                retVal = new RadniNalogDTO(rs.getInt("IdRadniNalog"), rs.getBoolean("Placeno"), rs.getDate("DatumOtvaranjaNaloga"), rs.getDate("DatumZatvaranjaNaloga"), rs.getInt("IdVozilo"), rs.getDouble("Troskovi"), rs.getInt("Kilometraza"), rs.getDate("PredvidjenoVrijemeZavrsetka"), rs.getDouble("CijenaUsluge"), rs.getString("OpisProblema"), rs.getBoolean("Izbrisano"));
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
    public boolean izbrisiRadniNalog(int id) {
        boolean retVal = false;
        Connection conn = null;
        PreparedStatement ps = null;

        String query = "UPDATE radni_nalog SET "
                + "Izbrisano=true "
                + "WHERE IdRadniNalog=? ";
        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            ps.setObject(1, id);

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
    public ArrayList<RezultatRNPretrazivanje> pretragaRadnihNaloga(RadniNalogParametri objekat) {
        ArrayList<RezultatRNPretrazivanje> retVal = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = objekat.kreirajIskaz();

        try {
            conn = ConnectionPool.getInstance().checkOut();
            ps = conn.prepareStatement(query);
            System.out.println(ps.toString());
            rs = ps.executeQuery();

            
            
            while (rs.next()) {
                String vlasnik="";
                if(rs.getString("Ime") == null || "".equals(rs.getString("Ime"))){
                    vlasnik = rs.getString("Naziv");
                }
                else{
                    vlasnik = rs.getString("Ime") + " " + rs.getString("Prezime");
                }
                retVal.add(new RezultatRNPretrazivanje(rs.getInt("IdRadniNalog"), rs.getString("BrojRegistracije"), vlasnik, rs.getDate("DatumOtvaranjaNaloga"), 
                        rs.getDate("DatumZatvaranjaNaloga"), rs.getDate("PredvidjenoVrijemeZavrsetka"), rs.getDouble("Troskovi"), rs.getDouble("CijenaUsluge"), rs.getBoolean("Placeno")));
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
