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
        private String IdRadnik;
	private String Ime;
	private String Prezime;
	private String Telefon;
	private String Adresa;
	private String StrucnaSprema;
	private String ImeOca;
	private String BrojLicneKarte;
	private java.sql.Date DatumRodjenja;

    public ZaposleniDTO(String Ime, String Prezime, String Telefon, String Adresa, String StrucnaSprema, String ImeOca, String BrojLicneKarte, Date DatumRodjenja) {
        this.Ime = Ime;
        this.Prezime = Prezime;
        this.Telefon = Telefon;
        this.Adresa = Adresa;
        this.StrucnaSprema = StrucnaSprema;
        this.ImeOca = ImeOca;
        this.BrojLicneKarte = BrojLicneKarte;
        this.DatumRodjenja = DatumRodjenja;
    }

    public String getIdRadnik() {
        return IdRadnik;
    }

    public void setIdRadnik(String IdRadnik) {
        this.IdRadnik = IdRadnik;
    }

        
    
	public String getIme(){
		return Ime;
	}

	public void setIme(String Ime){
		this.Ime=Ime;
	}

	public String getPrezime(){
		return Prezime;
	}

	public void setPrezime(String Prezime){
		this.Prezime=Prezime;
	}

	public String getTelefon(){
		return Telefon;
	}

	public void setTelefon(String Telefon){
		this.Telefon=Telefon;
	}

	public String getAdresa(){
		return Adresa;
	}

	public void setAdresa(String Adresa){
		this.Adresa=Adresa;
	}

	public String getStrucnaSprema(){
		return StrucnaSprema;
	}

	public void setStrucnaSprema(String StrucnaSprema){
		this.StrucnaSprema=StrucnaSprema;
	}

	public String getImeoca(){
		return ImeOca;
	}

	public void setImeoca(String ImeOca){
		this.ImeOca=ImeOca;
	}

	public String getBrojlicnekarte(){
		return BrojLicneKarte;
	}

	public void setBrojlicnekarte(String BrojLicneKarte){
		this.BrojLicneKarte=BrojLicneKarte;
	}

	public java.sql.Date getDatumrodjenja(){
		return DatumRodjenja;
	}

	public void setDatumrodjenja(java.sql.Date DatumRodjenja){
		this.DatumRodjenja=DatumRodjenja;
	}

}
