/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dto;

import java.sql.Date;

/**
 *
 * @author Aco
 */
public class ProdanDioDTO {
    private int id;
    private int idDio;
    private Double cijena;
    private Integer kol;
    private Date datum;
    
    public ProdanDioDTO(int id, Integer idDio, Double cijena, Integer kol, Date datum){
        this.id = id;
        this.idDio = idDio;
        this.cijena = cijena;
        this.kol = kol;
        this.datum = datum;
    }
    public ProdanDioDTO(Integer idDio, Double cijena, Integer kol, Date datum){
        this.id = 0;
        this.idDio = idDio;
        this.cijena = cijena;
        this.kol = kol;
        this.datum = datum;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public void setIdDio(Integer idDio){
        this.idDio = idDio;
    }
    public void setCijena(Double cijena){
        this.cijena = cijena;
    }
    public void setKolicina(Integer kolicina){
        this.kol = kolicina;
    }
    public void setDatum(Date datum){
        this.datum = datum;
    }
    
    public Integer getId(){
        return this.id;
    }
    public Integer getIdDio(){
        return this.idDio;
    }
    public Double getCijena(){
        return this.cijena;
        
    }
    public Integer getKolicina(){
        return this.kol;
    }
    public Date getDatum(){
        return this.datum;
    }
            
}
