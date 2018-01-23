/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import data.dao.DAOFactory;
import data.dto.KupacDTO;
import data.dto.ModelVozilaDTO;
import data.dto.VoziloDTO;
import java.util.ArrayList;

/**
 *
 * @author DulleX
 */
public class VozilaLogika extends Thread {
    
    public static int UCITAJ_VOZILA = 1;
    public static int UCITAJ_MODELE = 2;
    public static int UCITAJ_VLASNIKE = 3;
    public static ArrayList<VoziloDTO> vozila;
    public static ArrayList<ModelVozilaDTO> modeli;
    public static ArrayList<KupacDTO> vlasnici;
    
    private int akcija;
    
    public VozilaLogika(int akcija){
       this.akcija = akcija;
       start();
    }
    
    public void run(){
         if(akcija == UCITAJ_VOZILA){
            vozila = DAOFactory.getDAOFactory().getVoziloDAO().svaVozila();
        }
        else if(akcija == UCITAJ_MODELE){
            modeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
           // System.out.println(modeli.size());
        }
        else if(akcija == UCITAJ_VLASNIKE){
            vlasnici = DAOFactory.getDAOFactory().getKupacDAO().sviKupci();
        }
    }
    
      public ArrayList<VoziloDTO> getVozila() {
        return vozila;
    }

    public ArrayList<ModelVozilaDTO> getModeli() {
        return modeli;
    }

}
