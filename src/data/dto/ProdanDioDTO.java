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
    private Integer id;
    private Integer idDio;
    private String naziv;
    private Double cijena;
    private Integer kol;
    private Date datum;
    private String sifra;

    public ProdanDioDTO() {
    }

    public ProdanDioDTO( Integer idDio,Integer id, String sifra, String naziv, Double cijena, Integer kol, Date datum) {
        this.id = id;
        this.idDio = idDio;
        this.naziv = naziv;
        this.cijena = cijena;
        this.kol = kol;
        this.datum = datum;
        this.sifra = sifra;
    }

    public ProdanDioDTO(Integer id, Integer idDio, Double cijena, Integer kol, Date datum) {
        this.id = id;
        this.idDio = idDio;
        this.cijena = cijena;
        this.kol = kol;
        this.datum = datum;
    }

    public ProdanDioDTO(Integer idDio, Double cijena, Integer kol, Date datum) {
        this.idDio = idDio;
        this.cijena = cijena;
        this.kol = kol;
        this.datum = datum;
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdDio() {
        return idDio;
    }

    public void setIdDio(Integer idDio) {
        this.idDio = idDio;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Double getCijena() {
        return cijena;
    }

    public void setCijena(Double cijena) {
        this.cijena = cijena;
    }

    public Integer getKolicina() {
        return kol;
    }

    public void setKolicina(Integer kol) {
        this.kol = kol;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    
}
