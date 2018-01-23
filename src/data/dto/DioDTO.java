/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dto;

/**
 *
 * @author Aco
 */
public class DioDTO {
    private int id;
    private String sifra;
    private String naziv;
    private String vrstaGoriva;
    private Integer godVozila;
    private boolean novo;
    private Double trenCijena;
    private Integer kol;
    private boolean zaSve;
    private String marka;
    private String model;
    
    public DioDTO(int id, String sifra, String naziv, String vrsGor, Integer god, boolean novo, Double cijena, Integer kol, 
            boolean zaSve, String marka, String model){
        this.id = id;
        this.sifra = sifra;
        this.naziv = naziv;
        this.vrstaGoriva = vrsGor;
        this.godVozila = god;
        this.novo = novo;
        this.trenCijena = cijena;
        this.kol = kol;
        this.zaSve = zaSve;
        this.marka = marka;
        this.model = model;
    }
    public DioDTO(String sifra, String naziv, String vrsGor, Integer god, boolean novo, Double cijena, Integer kol, 
            boolean zaSve, String marka, String model){
        this.sifra = sifra;
        this.naziv = naziv;
        this.vrstaGoriva = vrsGor;
        this.godVozila = god;
        this.novo = novo;
        this.trenCijena = cijena;
        this.kol = kol;
        this.zaSve = zaSve;
        this.marka = marka;
        this.model = model;
    }
    public DioDTO(int id, String sifra, String naziv, String vrsGor, Integer god, boolean novo, Double cijena, Integer kol, 
            boolean zaSve){
        this.id = id;
        this.sifra = sifra;
        this.naziv = naziv;
        this.vrstaGoriva = vrsGor;
        this.godVozila = god;
        this.novo = novo;
        this.trenCijena = cijena;
        this.kol = kol;
        this.zaSve = zaSve;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setSifra(String sifra){
        this.sifra = sifra;
    }
    public void setNaziv(String naziv){
        this.naziv = naziv;
    }
    public void setVrstaGoriva(String vrsGor){
        this.vrstaGoriva = vrsGor;
    }
    public void setGodisteVozila(Integer godVoz){
        this.godVozila = godVoz;
    }
    public void setNovo(boolean novo){
        this.novo = novo;
    }
    public void setTrenutnaCijena(Double cijena){
        this.trenCijena = cijena;
    }
    public void setKolicina(Integer kol){
        this.kol = kol;
    }
    public void setZaSve(boolean zaSve){
        this.zaSve = zaSve;
    }
    public void setModel(String model){
        this.model = model;
    }
    public void setMarka(String marka){
        this.marka = marka;
    }
    
    public int getId(){
        return this.id;
    }
    public String getSifra(){
        return this.sifra;
    }
    public String getNaziv(){
        return this.naziv;
    }
    public String getVrstaGoriva(){
        return this.vrstaGoriva;
    }
    public Integer getGodisteVozila(){
        return this.godVozila;
    }
    public boolean getNovo(){
        return this.novo;
    }
    public Double getTrenutnaCijena(){
        return this.trenCijena;
    }
    public Integer getKolicina(){
        return this.kol;
    }
    public boolean getZaSve(){
        return this.zaSve;
    }
    public String getModel(){
        return this.model;
    }
    public String getMarka(){
        return this.marka;
    }
}
