/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.RadniNalogRadnikDTO;
import java.util.ArrayList;

public interface RadniNalogRadnikDAO {
    public boolean dodajRadniNalogRadnik(RadniNalogRadnikDTO rnr);
    
    public ArrayList<RadniNalogRadnikDTO> radniciNaRadnomNalogu(int idRadnogNaloga);
    
    public boolean postojiLiZapis(int idRadnogNaloga, int idRadnik);
    
    public boolean izbrisi(RadniNalogRadnikDTO rnr);
}
