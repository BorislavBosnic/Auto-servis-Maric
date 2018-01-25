/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.TerminDTO;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

/**
 *
 * @author nikol
 */
public interface TerminDAO
{
    public ArrayList<TerminDTO> sviTermini();
    public ArrayList<TerminDTO> sviTerminiDatumZakazivanja(Date datumZakazivanja);
    public TerminDTO termin(int idFaktura);
    public TerminDTO terminDatum(Date datum);
    public TerminDTO terminVrijeme(Time vrijeme);
    public TerminDTO terminDatumVrijeme(Date datum, Time vrijeme);
    public boolean dodajTermin(TerminDTO termin);
    public boolean azurirajTerminId(TerminDTO termin);
    public boolean obrisiTermin(int idTermina);
    public boolean obrisiTerminDatumVrijeme(Date datum, Time vrijeme);
}
