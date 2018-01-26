/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dto;

/**
 *
 * @author nikol
 */
public class RadniNalogDioDTO
{
    /**Attributes**/
    private int idRadniNalog;
    private int idDio;
    private double cijena;
    private int kolicina;
    
    /**Constructors**/
    public RadniNalogDioDTO(int idRadniNalog, int idDio, double cijena, int kolicina)
    {
        this.idRadniNalog=idRadniNalog;
        this.idDio=idDio;
        this.cijena=cijena;
        this.kolicina=kolicina;
    }
    
    /**Getters**/
    public int getIdRadniNalog()
    {
        return idRadniNalog;
    }
    public int getIdDio()
    {
        return idDio;
    }
    public double getCijena()
    {
        return cijena;
    }
    public int getKolicina()
    {
        return kolicina;
    }
    
    /**Setters**/
    public void setIdRadniNalog(int idRadniNalog)
    {
        this.idRadniNalog=idRadniNalog;
    }
    public void setIdDio(int idDio)
    {
        this.idDio=idDio;
    }
    public void setCijena(double cijena)
    {
        this.cijena=cijena;
    }
    public void setKolicina(int kolicina)
    {
        this.kolicina=kolicina;
    }
}
