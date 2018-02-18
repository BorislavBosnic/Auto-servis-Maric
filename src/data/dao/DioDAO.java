/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.DioDTO;
import java.util.ArrayList;

/**
 *
 * @author Aco
 */
public interface DioDAO {
    public ArrayList<DioDTO> getDijeloviZaSvaVozila();
    public DioDTO dio(DioDTO dio);
    public boolean dodajDio(DioDTO kupac);
    public DioDTO getDio(DioDTO dio);
    public DioDTO getDio(int id);
    public DioDTO getDio(String sifra);
    public DioDTO getDioo(String sifra, String naziv);
    public ArrayList<DioDTO> getSviDijelovi();
    public ArrayList<DioDTO> getDijelovi(String naziv, Integer godiste, String marka, String model, String gorivo, Boolean stanje);
    public ArrayList<DioDTO> getDijelovi(String naziv, Integer godiste, String marka, String gorivo, Boolean stanje);
    public ArrayList<DioDTO> dijelovi(String naziv, Integer godiste, String marka, String model, Boolean stanje);
    public ArrayList<DioDTO> dijelovi(String naziv, Integer godiste, String gorivo, Boolean stanje);
    public ArrayList<DioDTO> dijelovi(String naziv, Integer godiste, Boolean stanje);
    public ArrayList<DioDTO> getDijelovi(String naziv, Integer godiste, String marka, Boolean stanje);
    //bez godista
    public ArrayList<DioDTO> dijelovibg(String naziv, String marka, String model, String gorivo, Boolean stanje);
    public ArrayList<DioDTO> dijelovibg(String naziv, String marka, String model, Boolean stanje);
    public ArrayList<DioDTO> dijelovibg(String naziv, String marka, Boolean stanje);
    public ArrayList<DioDTO> dijelovibg2(String naziv, String marka, String gorivo, Boolean stanje);
    public ArrayList<DioDTO> dijelovibg2(String naziv, String gorivo, Boolean stanje);
    public ArrayList<DioDTO> getDijelovi(String gorivo, String marka, String model, Boolean stanje);   
    public ArrayList<DioDTO> getDijelovi(String marka, String model);  
    public ArrayList<DioDTO> getDijelovi(String gorivo, String marka, Boolean stanje);
    public ArrayList<DioDTO> getDijeloviNaziv(String naziv, Boolean stanje);
    public boolean azurirajDio(DioDTO dio);
    public boolean obrisiDio(int id);
}
