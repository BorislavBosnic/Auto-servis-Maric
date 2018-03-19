/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dto;

import java.sql.Date;

/**
 *
 * @author DulleX
 */
public class RadniNalogParametri {
    private Date datumOtvaranjaOD;//1
    private Date datumOtvaranjaDO;//1
    private Date datumZatvaranjaOD;//2
    private Date datumZatvaranjaDO;//2
    private Date datumPotrebnoZavrsitiOD;//3
    private Date datumPotrebnoZavrsitiDO;//3
    private String registracija;//4
    private String prezime;//5
    private String ime;//6
    private String naziv;//7
    private boolean svi;//8
    private boolean privatni;
    private boolean pravni;

    public RadniNalogParametri() {
    }

    public RadniNalogParametri(Date datumOtvaranjaOD, Date datumOtvaranjaDO, Date datumZatvaranjaOD, Date datumZatvaranjaDO, Date datumPotrebnoZavrsitiOD, Date datumPotrebnoZavrsitiDO, String registracija, String prezime, String ime, String naziv, boolean svi, boolean privatni) {
        this.datumOtvaranjaOD = datumOtvaranjaOD;
        this.datumOtvaranjaDO = datumOtvaranjaDO;
        this.datumZatvaranjaOD = datumZatvaranjaOD;
        this.datumZatvaranjaDO = datumZatvaranjaDO;
        this.datumPotrebnoZavrsitiOD = datumPotrebnoZavrsitiOD;
        this.datumPotrebnoZavrsitiDO = datumPotrebnoZavrsitiDO;
        this.registracija = registracija;
        this.prezime = prezime;
        this.ime = ime;
        this.naziv = naziv;
        this.svi = svi;
        this.privatni = privatni;
    }

    public boolean isPravni() {
        return pravni;
    }

    public void setPravni(boolean pravni) {
        this.pravni = pravni;
    }
    
    
    
    

    public Date getDatumOtvaranjaOD() {
        return datumOtvaranjaOD;
    }

    public void setDatumOtvaranjaOD(Date datumOtvaranjaOD) {
        this.datumOtvaranjaOD = datumOtvaranjaOD;
    }

    public Date getDatumOtvaranjaDO() {
        return datumOtvaranjaDO;
    }

    public void setDatumOtvaranjaDO(Date datumOtvaranjaDO) {
        this.datumOtvaranjaDO = datumOtvaranjaDO;
    }

    public Date getDatumZatvaranjaOD() {
        return datumZatvaranjaOD;
    }

    public void setDatumZatvaranjaOD(Date datumZatvaranjaOD) {
        this.datumZatvaranjaOD = datumZatvaranjaOD;
    }

    public Date getDatumZatvaranjaDO() {
        return datumZatvaranjaDO;
    }

    public void setDatumZatvaranjaDO(Date datumZatvaranjaDO) {
        this.datumZatvaranjaDO = datumZatvaranjaDO;
    }

    public Date getDatumPotrebnoZavrsitiOD() {
        return datumPotrebnoZavrsitiOD;
    }

    public void setDatumPotrebnoZavrsitiOD(Date datumPotrebnoZavrsitiOD) {
        this.datumPotrebnoZavrsitiOD = datumPotrebnoZavrsitiOD;
    }

    public Date getDatumPotrebnoZavrsitiDO() {
        return datumPotrebnoZavrsitiDO;
    }

    public void setDatumPotrebnoZavrsitiDO(Date datumPotrebnoZavrsitiDO) {
        this.datumPotrebnoZavrsitiDO = datumPotrebnoZavrsitiDO;
    }

    public String getRegistracija() {
        return registracija;
    }

    public void setRegistracija(String registracija) {
        this.registracija = registracija;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public boolean isSvi() {
        return svi;
    }

    public void setSvi(boolean svi) {
        this.svi = svi;
    }

    public boolean isPrivatni() {
        return privatni;
    }

    public void setPrivatni(boolean privatni) {
        this.privatni = privatni;
    }
    
    
    
   /* public String kreirajIskaz(){
        String iskaz = "SELECT * FROM radni_nalog r INNER JOIN vozilo v ON r.IdVozilo=v.IdVozilo INNER JOIN kupac k ON v.IdKupac=k.IdKupac WHERE r.Izbrisano!=true AND ";
        if(datumOtvaranjaOD != null){
            iskaz+="DatumOtvaranjaNaloga>='" + datumOtvaranjaOD + "' AND ";
        }
        if(datumOtvaranjaDO != null){
            iskaz+="DatumOtvaranjaNaloga<='" + datumOtvaranjaDO + "' AND ";
        }
        if(datumZatvaranjaOD != null){
            iskaz+="DatumZatvaranjaNaloga>='" + datumZatvaranjaOD + "' AND ";
        }
        if(datumZatvaranjaDO != null){
             iskaz+="DatumZatvaranjaNaloga<='" + datumZatvaranjaDO + "' AND ";
        }
        if(datumPotrebnoZavrsitiOD != null){
            iskaz+="PredvidjenoVrijemeZavrsetka>='" + datumPotrebnoZavrsitiOD + "' AND ";
        }
        if(datumPotrebnoZavrsitiDO != null){
            iskaz+="PredvidjenoVrijemeZavrsetka<='" + datumPotrebnoZavrsitiDO + "' AND ";
        }
        if(registracija != null){
            iskaz+="BrojRegistracije LIKE '" + registracija + "' AND ";
        }  
            if(privatni){
                iskaz+="(Naziv is null OR Naziv LIKE '') AND ";
                if(ime != null){
                    iskaz+="Ime LIKE '" + ime + "' AND ";
                }
                if(prezime != null){
                    iskaz+="Prezime LIKE '" + prezime + "' AND ";
                }
            }
            else if(pravni){
                iskaz+="(Ime is null OR Ime LIKE '' OR Prezime is null OR Prezime LIKE '') AND ";
                if(naziv != null){
                    iskaz+="Naziv LIKE '" + naziv+"'";
                }
            }
        
        if(iskaz.endsWith("WHERE "))
            iskaz = iskaz.replaceAll("WHERE ", "");
        if(iskaz.endsWith("AND ")){
            iskaz = iskaz.substring(0, iskaz.length()-4);
        }
        return iskaz;
    }*/
}
