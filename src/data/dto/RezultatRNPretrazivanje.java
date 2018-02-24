/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dto;

import java.sql.Date;

public class RezultatRNPretrazivanje {
    private int id;
    private String registracija;
    private String vlasnik;
    private Date datumOtvaranja;
    private Date datumZatvaranja;
    private Date datumPotrebnoZavrsitiDo;
    private double troskoviDijelova;
    private double cijenaUsluge;
    private boolean placeno;

    public RezultatRNPretrazivanje(){}
    
    public RezultatRNPretrazivanje(int id, String registracija, String vlasnik, Date datumOtvaranja, Date datumZatvaranja, Date datumPotrebnoZavrsitiDo, double troskoviDijelova, double cijenaUsluge, boolean placeno) {
        this.id = id;
        this.registracija = registracija;
        this.vlasnik = vlasnik;
        this.datumOtvaranja = datumOtvaranja;
        this.datumZatvaranja = datumZatvaranja;
        this.datumPotrebnoZavrsitiDo = datumPotrebnoZavrsitiDo;
        this.troskoviDijelova = troskoviDijelova;
        this.cijenaUsluge = cijenaUsluge;
        this.placeno = placeno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistracija() {
        return registracija;
    }

    public void setRegistracija(String registracija) {
        this.registracija = registracija;
    }

    public String getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(String vlasnik) {
        this.vlasnik = vlasnik;
    }

    public Date getDatumOtvaranja() {
        return datumOtvaranja;
    }

    public void setDatumOtvaranja(Date datumOtvaranja) {
        this.datumOtvaranja = datumOtvaranja;
    }

    public Date getDatumZatvaranja() {
        return datumZatvaranja;
    }

    public void setDatumZatvaranja(Date datumZatvaranja) {
        this.datumZatvaranja = datumZatvaranja;
    }

    public Date getDatumPotrebnoZavrsitiDo() {
        return datumPotrebnoZavrsitiDo;
    }

    public void setDatumPotrebnoZavrsitiDo(Date datumPotrebnoZavrsitiDo) {
        this.datumPotrebnoZavrsitiDo = datumPotrebnoZavrsitiDo;
    }

    public double getTroskoviDijelova() {
        return troskoviDijelova;
    }

    public void setTroskoviDijelova(double troskoviDijelova) {
        this.troskoviDijelova = troskoviDijelova;
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
    
}
