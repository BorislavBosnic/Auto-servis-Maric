/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.RadniNalogDioDTO;
import java.util.ArrayList;

/**
 *
 * @author nikol
 */
public interface RadniNalogDioDAO
{
    public ArrayList<RadniNalogDioDTO> sviRadniNalogDijelovi();
    public ArrayList<RadniNalogDioDTO> radniNalogDioIdRadniNalog(int idRadniNalog);
    public RadniNalogDioDTO radniNalogDio(int idRadniNalog, int idDio);
    public boolean dodajRadniNalogDio(RadniNalogDioDTO radniNalogDio);
    public boolean azurirajRadniNalogDio(RadniNalogDioDTO radniNalogDio);
    public boolean obrisiRadniNalogDio(int idRadniNalog, int idDio);
}
