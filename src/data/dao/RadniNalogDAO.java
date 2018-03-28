/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.RadniNalogDTO;
import java.util.ArrayList;
import java.util.Date;
import data.dto.RadniNalogParametri;
import data.dto.RezultatRNPretrazivanje;

/**
 *
 * @author HP BOOK
 */
public interface RadniNalogDAO {

    public boolean izbrisiRadniNalog(int id);

    public double getSumaCijenaUsluga(Date datumOd, Date datumDo);

    public double getSumaCijenaDijelova(Date datumOd, Date datumDo);

    public int getBrojPopravki(Date datumOd, Date datumDo);

    public int getBrojAutaNaStanju();

    public int getBrojAutaKojaCekajuPopravku();

    public int getBrojPlacenihFaktura();

    public int getBrojFaktura();

    public ArrayList<RadniNalogDTO> getRadniNalozi();

    public ArrayList<RadniNalogDTO> getRadniNalozi(int id);

    public ArrayList<RadniNalogDTO> getRadniNalozi(Date dat1, Date dat2);

    public ArrayList<RadniNalogDTO> radniNaloziVozila(int idVozila);

    public ArrayList<RezultatRNPretrazivanje> nezatvoreniRadniNaloziVozila();

    public boolean dodajRadniNalog(RadniNalogDTO nalog);

    public boolean azurirajRadniNalog(RadniNalogDTO nalog);

    public RadniNalogDTO getRadniNalog(int id);

    public ArrayList<RezultatRNPretrazivanje> pretragaRadnihNaloga(RadniNalogParametri objekat);

    public boolean zatvoriRadniNalog(int id);
}
