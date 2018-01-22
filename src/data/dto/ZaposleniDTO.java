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
public class ZaposleniDTO {
        private int idRadnik;
	private String ime;
	private String prezime;
	private String telefon;
	private String adresa;
	private String strucnaSprema;
	private String imeOca;
	private String brojLicneKarte;
	private java.sql.Date datumRodjenja;
        private String funkcija;
        private java.sql.Date datumOd;
        private java.sql.Date datumDo;

        public ZaposleniDTO(){
            
        }

    public ZaposleniDTO(int idRadnik, String ime, String prezime, String telefon, String adresa, String strucnaSprema, String imeOca, String brojLicneKarte, Date datumRodjenja, String funkcija, Date datumOd, Date datumDo) {
        this.idRadnik = idRadnik;
        this.ime = ime;
        this.prezime = prezime;
        this.telefon = telefon;
        this.adresa = adresa;
        this.strucnaSprema = strucnaSprema;
        this.imeOca = imeOca;
        this.brojLicneKarte = brojLicneKarte;
        this.datumRodjenja = datumRodjenja;
        this.funkcija = funkcija;
        this.datumOd = datumOd;
        this.datumDo = datumDo;
    }
    
     public ZaposleniDTO( String ime, String prezime, String telefon, String adresa, String strucnaSprema, String imeOca, String brojLicneKarte, Date datumRodjenja, String funkcija, Date datumOd, Date datumDo) {
        this.ime = ime;
        this.prezime = prezime;
        this.telefon = telefon;
        this.adresa = adresa;
        this.strucnaSprema = strucnaSprema;
        this.imeOca = imeOca;
        this.brojLicneKarte = brojLicneKarte;
        this.datumRodjenja = datumRodjenja;
        this.funkcija = funkcija;
        this.datumOd = datumOd;
        this.datumDo = datumDo;
    }

    public int getIdRadnik() {
        return idRadnik;
    }

    public void setIdRadnik(int idRadnik) {
        this.idRadnik = idRadnik;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getStrucnaSprema() {
        return strucnaSprema;
    }

    public void setStrucnaSprema(String strucnaSprema) {
        this.strucnaSprema = strucnaSprema;
    }

    public String getImeOca() {
        return imeOca;
    }

    public void setImeOca(String imeOca) {
        this.imeOca = imeOca;
    }

    public String getBrojLicneKarte() {
        return brojLicneKarte;
    }

    public void setBrojLicneKarte(String brojLicneKarte) {
        this.brojLicneKarte = brojLicneKarte;
    }

    public Date getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(Date datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getFunkcija() {
        return funkcija;
    }

    public void setFunkcija(String funkcija) {
        this.funkcija = funkcija;
    }

    public Date getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(Date datumOd) {
        this.datumOd = datumOd;
    }

    public Date getDatumDo() {
        return datumDo;
    }

    public void setDatumDo(Date datumDo) {
        this.datumDo = datumDo;
    }
        
    
}