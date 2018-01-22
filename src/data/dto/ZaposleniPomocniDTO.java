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
public class ZaposleniPomocniDTO {
    private String marka;
    private String model;
    private String brojRegistracije;
    private java.sql.Date datumZatvaranjaNaloga;
    private String opis;
    private Double troskovi;
    private Double cijenaUsluge;
    private Double profit;
    private Integer brojRadnihnaloga;

    public ZaposleniPomocniDTO() {
    }

    public ZaposleniPomocniDTO(String marka, String model, String brojRegistracije, Date datumZatvaranjaNaloga, String opis, Double troskovi, Double cijenaUsluge, Double profit, Integer brojRadnihnaloga) {
        this.marka = marka;
        this.model = model;
        this.brojRegistracije = brojRegistracije;
        this.datumZatvaranjaNaloga = datumZatvaranjaNaloga;
        this.opis = opis;
        this.troskovi = troskovi;
        this.cijenaUsluge = cijenaUsluge;
        this.profit = profit;
        this.brojRadnihnaloga = brojRadnihnaloga;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrojRegistracije() {
        return brojRegistracije;
    }

    public void setBrojRegistracije(String brojRegistracije) {
        this.brojRegistracije = brojRegistracije;
    }

    public Date getDatumZatvaranjaNaloga() {
        return datumZatvaranjaNaloga;
    }

    public void setDatumZatvaranjaNaloga(Date datumZatvaranjaNaloga) {
        this.datumZatvaranjaNaloga = datumZatvaranjaNaloga;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Double getTroskovi() {
        return troskovi;
    }

    public void setTroskovi(Double troskovi) {
        this.troskovi = troskovi;
    }

    public Double getCijenaUsluge() {
        return cijenaUsluge;
    }

    public void setCijenaUsluge(Double cijenaUsluge) {
        this.cijenaUsluge = cijenaUsluge;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Integer getBrojRadnihnaloga() {
        return brojRadnihnaloga;
    }

    public void setBrojRadnihnaloga(Integer brojRadnihnaloga) {
        this.brojRadnihnaloga = brojRadnihnaloga;
    }

    
}