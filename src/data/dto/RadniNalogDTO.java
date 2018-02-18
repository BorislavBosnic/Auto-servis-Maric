/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dto;

import java.sql.Date;

/**
 *
 * @author haker
 */
public class RadniNalogDTO {
        private int idRadniNalog;
        private boolean placeno;
        private java.sql.Date datumOtvaranjaNaloga;
	private java.sql.Date datumZatvaranjaNaloga;
	private int idVozilo;
	private double troskovi;
	private int kilometraza;
	private java.sql.Date predvidjenoVrijemeZavrsetka;
	private double cijenaUsluge; 
        private String opisProblema;

    public String getOpisProblema() {
        return opisProblema;
    }

    public void setOpisProblema(String opisProblema) {
        this.opisProblema = opisProblema;
    }

        
    public RadniNalogDTO() {
    }
    
     public RadniNalogDTO(boolean placeno, Date datumOtvaranjaNaloga, Date datumZatvaranjaNaloga, int idVozilo, double troskovi, int kilometraza, Date predvidjenoVrijemeZavrsetka, double cijenaUsluge, String opisProblema, int id) {
        this.placeno = placeno;
        this.datumOtvaranjaNaloga = datumOtvaranjaNaloga;
        this.datumZatvaranjaNaloga = datumZatvaranjaNaloga;
        this.idVozilo = idVozilo;
        this.troskovi = troskovi;
        this.kilometraza = kilometraza;
        this.predvidjenoVrijemeZavrsetka = predvidjenoVrijemeZavrsetka;
        this.cijenaUsluge = cijenaUsluge;
        this.opisProblema = opisProblema;
        this.idRadniNalog = id;
    }

    public RadniNalogDTO(boolean placeno, Date datumOtvaranjaNaloga, Date datumZatvaranjaNaloga, int idVozilo, double troskovi, int kilometraza, Date predvidjenoVrijemeZavrsetka, double cijenaUsluge, String opisProblema) {
        this.placeno = placeno;
        this.datumOtvaranjaNaloga = datumOtvaranjaNaloga;
        this.datumZatvaranjaNaloga = datumZatvaranjaNaloga;
        this.idVozilo = idVozilo;
        this.troskovi = troskovi;
        this.kilometraza = kilometraza;
        this.predvidjenoVrijemeZavrsetka = predvidjenoVrijemeZavrsetka;
        this.cijenaUsluge = cijenaUsluge;
        this.opisProblema = opisProblema;
    }
    
    public RadniNalogDTO(boolean placeno, Date datumOtvaranjaNaloga, Date datumZatvaranjaNaloga, int idVozilo, double troskovi, int kilometraza, Date predvidjenoVrijemeZavrsetka, double cijenaUsluge) {
        this.placeno = placeno;
        this.datumOtvaranjaNaloga = datumOtvaranjaNaloga;
        this.datumZatvaranjaNaloga = datumZatvaranjaNaloga;
        this.idVozilo = idVozilo;
        this.troskovi = troskovi;
        this.kilometraza = kilometraza;
        this.predvidjenoVrijemeZavrsetka = predvidjenoVrijemeZavrsetka;
        this.cijenaUsluge = cijenaUsluge;
    }
    public RadniNalogDTO(int idRadniNalog, boolean placeno, Date datumOtvaranjaNaloga, Date datumZatvaranjaNaloga, int idVozilo, double troskovi, int kilometraza, Date predvidjenoVrijemeZavrsetka, double cijenaUsluge) {
        this.idRadniNalog = idRadniNalog;
        this.placeno = placeno;
        this.datumOtvaranjaNaloga = datumOtvaranjaNaloga;
        this.datumZatvaranjaNaloga = datumZatvaranjaNaloga;
        this.idVozilo = idVozilo;
        this.troskovi = troskovi;
        this.kilometraza = kilometraza;
        this.predvidjenoVrijemeZavrsetka = predvidjenoVrijemeZavrsetka;
        this.cijenaUsluge = cijenaUsluge;
    }

    public int getIdRadniNalog() {
        return idRadniNalog;
    }

    public void setIdRadniNalog(int idRadniNalog) {
        this.idRadniNalog = idRadniNalog;
    }

    public Date getDatumOtvaranjaNaloga() {
        return datumOtvaranjaNaloga;
    }

    public void setDatumOtvaranjaNaloga(Date datumOtvaranjaNaloga) {
        this.datumOtvaranjaNaloga = datumOtvaranjaNaloga;
    }

    public Date getDatumZatvaranjaNaloga() {
        return datumZatvaranjaNaloga;
    }

    public void setDatumZatvaranjaNaloga(Date datumZatvaranjaNaloga) {
        this.datumZatvaranjaNaloga = datumZatvaranjaNaloga;
    }

    public int getIdVozilo() {
        return idVozilo;
    }

    public void setIdVozilo(int idVozilo) {
        this.idVozilo = idVozilo;
    }

    public double getTroskovi() {
        return troskovi;
    }

    public void setTroskovi(double troskovi) {
        this.troskovi = troskovi;
    }

    public int getKilometraza() {
        return kilometraza;
    }

    public void setKilometraza(int kilometraza) {
        this.kilometraza = kilometraza;
    }

    public Date getPredvidjenoVrijemeZavrsetka() {
        return predvidjenoVrijemeZavrsetka;
    }

    public void setPredvidjenoVrijemeZavrsetka(Date predvidjenoVrijemeZavrsetka) {
        this.predvidjenoVrijemeZavrsetka = predvidjenoVrijemeZavrsetka;
    }

    public double getCijenaUsluge() {
        return cijenaUsluge;
    }

    public void setCijenaUsluge(double cijenaUsluge) {
        this.cijenaUsluge = cijenaUsluge;
    }

    public boolean isPlaceno() {
        return placeno;
    }

    public void setPlaceno(boolean placeno) {
        this.placeno = placeno;
    }     

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.idRadniNalog;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RadniNalogDTO other = (RadniNalogDTO) obj;
        if (this.idRadniNalog != other.idRadniNalog) {
            return false;
        }
        return true;
    }
        
    
}