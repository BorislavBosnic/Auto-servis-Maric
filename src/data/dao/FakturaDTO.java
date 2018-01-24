/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;
import java.sql.Date;

/**
 *
 * @author nikol
 */
public class FakturaDTO
{
    /**Attributes**/
    private int idFaktura;
    private Date datumFakturisanja;
    private int idRadniNalog;
    private double iznos;
    private int vrijemeRada;
    
    /**Constructors**/
    public FakturaDTO
    (
        int idFakture,
        Date datumFakturisanja,
        int idRadniNalog,
        double iznos,
        int vrijemeRada
    )
    {
        this.idFaktura=idFaktura;
        this.datumFakturisanja=datumFakturisanja;
        this.idRadniNalog=idRadniNalog;
        this.iznos=iznos;
        this.vrijemeRada=vrijemeRada;
    }
    
    /**Getters**/
    public int getIdFaktura()
    {
        return idFaktura;
    }
    public Date getDatumFakturisanja()
    {
        return datumFakturisanja;
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
    public void setDatumFakturisanja(Date datumFakturisanja)
    {
        this.datumFakturisanja=datumFakturisanja;
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
