/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;
import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author nikol
 */
public class TerminDTO
{
    /**Attributes**/
    private int idTermin;
    private Date datum;
    private Time vrijeme;
    private String marka;
    private String model;
    private String ime;
    private String prezime;
    private String brojTelefona;
    private Date datumZakazivanja;
    
    /**Constructors**/
    public TerminDTO
    (
        int idTermin,
        Date datum,
        Time vrijeme,
        String marka,
        String model,
        String ime,
        String prezime,
        String brojTelefona,
        Date datumZakazivanja
    )
    {
        this.idTermin=idTermin;
        this.datum=datum;
        this.vrijeme=vrijeme;
        this.marka=marka;
        this.model=model;
        this.ime=ime;
        this.prezime=prezime;
        this.brojTelefona=brojTelefona;
        this.datumZakazivanja=datumZakazivanja;
    }
    
    /**Getters**/
    public int getIdTermin()
    {
        return idTermin;
    }
    public Date getDatum()
    {
        return datum;
    }
    public Time getVrijeme()
    {
        return vrijeme;
    }
    public String getMarka()
    {
        return marka;
    }
    public String getModel()
    {
        return model;
    }
    public String getIme()
    {
        return ime;
    }
    public String getPrezime()
    {
        return prezime;
    }
    public String getBrojTelefona()
    {
        return brojTelefona;
    }
    public Date getDatumZakazivanja()
    {
        return datumZakazivanja;
    }
    
    /**Setters**/
    public void setIdTermin(int idTermin)
    {
        this.idTermin=idTermin;
    }
    public void setDatum(Date datum)
    {
        this.datum=datum;
    }
    public void setVrijeme(Time vrijeme)
    {
        this.vrijeme=vrijeme;
    }
    public void setMarka(String marka)
    {
        this.marka=marka;
    }
    public void setModel(String model)
    {
        this.model=model;
    }
    public void setIme(String ime)
    {
        this.ime=ime;
    }
    public void setPrezime(String prezime)
    {
        this.prezime=prezime;
    }
    public void setBrojTelefona(String brojTelefona)
    {
        this.brojTelefona=brojTelefona;
    }
    public void setDatumZakazivanja(Date datumZakazivanja)
    {
        this.datumZakazivanja=datumZakazivanja;
    }
}
