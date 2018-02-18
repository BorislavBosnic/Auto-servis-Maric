/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dto;

import java.sql.Date;
import java.util.Objects;

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
        
        private String ulogaRadnika;

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

    public String getUlogaRadnika() {
        return ulogaRadnika;
    }

    public void setUlogaRadnika(String ulogaRadnika) {
        this.ulogaRadnika = ulogaRadnika;
    }
    
    

    @Override
    public String toString() {
        return ime + " (" + imeOca + ") " + prezime;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.idRadnik;
        hash = 19 * hash + Objects.hashCode(this.ime);
        hash = 19 * hash + Objects.hashCode(this.prezime);
        hash = 19 * hash + Objects.hashCode(this.telefon);
        hash = 19 * hash + Objects.hashCode(this.adresa);
        hash = 19 * hash + Objects.hashCode(this.strucnaSprema);
        hash = 19 * hash + Objects.hashCode(this.imeOca);
        hash = 19 * hash + Objects.hashCode(this.brojLicneKarte);
        hash = 19 * hash + Objects.hashCode(this.datumRodjenja);
        hash = 19 * hash + Objects.hashCode(this.funkcija);
        hash = 19 * hash + Objects.hashCode(this.datumOd);
        hash = 19 * hash + Objects.hashCode(this.datumDo);
        hash = 19 * hash + Objects.hashCode(this.ulogaRadnika);
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
        final ZaposleniDTO other = (ZaposleniDTO) obj;
        if (this.idRadnik != other.idRadnik) {
            return false;
        }
        if (!Objects.equals(this.ime, other.ime)) {
            return false;
        }
        if (!Objects.equals(this.prezime, other.prezime)) {
            return false;
        }
        if (!Objects.equals(this.telefon, other.telefon)) {
            return false;
        }
        if (!Objects.equals(this.adresa, other.adresa)) {
            return false;
        }
        if (!Objects.equals(this.strucnaSprema, other.strucnaSprema)) {
            return false;
        }
        if (!Objects.equals(this.imeOca, other.imeOca)) {
            return false;
        }
        if (!Objects.equals(this.brojLicneKarte, other.brojLicneKarte)) {
            return false;
        }
        if (!Objects.equals(this.funkcija, other.funkcija)) {
            return false;
        }
        if (!Objects.equals(this.ulogaRadnika, other.ulogaRadnika)) {
            return false;
        }
        if (!Objects.equals(this.datumRodjenja, other.datumRodjenja)) {
            return false;
        }
        if (!Objects.equals(this.datumOd, other.datumOd)) {
            return false;
        }
        if (!Objects.equals(this.datumDo, other.datumDo)) {
            return false;
        }
        return true;
    }
        
    
}