/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.RadniNalogDTO;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author HP BOOK
 */
public interface RadniNalogDAO {
    
    public double getSumaCijenaUsluga(Date datumOd, Date datumDo);
    
    public double getSumaCijenaDijelova(Date datumOd, Date datumDo);
    
    public int getBrojPopravki(Date datumOd,Date datumDo);
        
    public int getBrojAutaNaStanju();
    
    public int getBrojAutaKojaCekajuPopravku();

    public ArrayList<RadniNalogDTO> getRadniNalozi();

    public boolean dodajRadniNalog(RadniNalogDTO nalog);
    
    public boolean azurirajRadniNalog(RadniNalogDTO nalog); 
    
    public RadniNalogDTO getRadniNalog(int id);
}
