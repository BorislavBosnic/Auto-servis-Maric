/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import data.dao.DAOFactory;
import java.util.Date;

/**
 *
 * @author haker
 */
public class ZaposleniLogika extends Thread{
    
    private String opcija;
    private String Ime;
    private String Prezime;
    private String Telefon;
    private String Adresa;
    private String StrucnaSprema;
    private String ImeOca;
    private String BrojLicneKarte;
    private java.util.Date DatumRodjenja;

    public ZaposleniLogika(String opcija, String Ime, String Prezime, String Telefon, String Adresa, String StrucnaSprema, String ImeOca, String BrojLicneKarte, Date DatumRodjenja) {
        this.opcija = opcija;
        this.Ime = Ime;
        this.Prezime = Prezime;
        this.Telefon = Telefon;
        this.Adresa = Adresa;
        this.StrucnaSprema = StrucnaSprema;
        this.ImeOca = ImeOca;
        this.BrojLicneKarte = BrojLicneKarte;
        this.DatumRodjenja = DatumRodjenja;
    }
   
        
   
   @Override
   public void run(){
       if("select".equals(opcija)){
           
       }else if("insert".equals(opcija)){
           //provjera imena zaposlenog
           
       }else if("delete".equals(opcija)){
           
       }else{
           System.out.println("ERROR");
       }
   }
}
