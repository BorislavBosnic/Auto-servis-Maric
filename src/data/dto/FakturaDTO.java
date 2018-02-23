/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dto;
import java.sql.Date;

/**
 *
 * @author nikol
 */
public class FakturaDTO
{
    /**Attributes**/
    private int idFaktura;
    private Date datumIzdavanja;
    private int idRadniNalog;
    private double iznos;
    private int vrijemeRada;
    
    /**Constructors**/
    public FakturaDTO
    (
        int idFakture,
        Date datumIzdavanja,
        int idRadniNalog,
        double iznos,
        int vrijemeRada
    )
    {
        this.idFaktura=idFakture;
        this.datumIzdavanja=datumIzdavanja;
        this.idRadniNalog=idRadniNalog;
        this.iznos=iznos;
        this.vrijemeRada=vrijemeRada;
    }
    
    /**Getters**/
    public int getIdFaktura()
    {
        return idFaktura;
    }
    public Date getDatumIzdavanja()
    {
        return datumIzdavanja;
    }
    public int getIdRadniNalog()
    {
        return idRadniNalog;
    }
    public double getIznos()
    {
        return iznos;
    }
    public int getVrijemeRada()
    {
        return vrijemeRada;
    }
    
    /**Setters**/
    public void setIdFaktura(int idFaktura)
    {
        this.idFaktura=idFaktura;
    }
    public void setDatumIzdavanja(Date datumIzdavanja)
    {
        this.datumIzdavanja=datumIzdavanja;
    }
    public void setIdRadniNalog(int idRadniNalog)
    {
        this.idRadniNalog=idRadniNalog;
    }
    public void setIznos(double iznos)
    {
        this.iznos=iznos;
    }
    public void setVrijemeRada(int vrijemeRada)
    {
        this.vrijemeRada=vrijemeRada;
    }
}
