/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.FakturaDTO;
import java.util.ArrayList;
import java.sql.Date;

/**
 *
 * @author nikol
 */
public interface FakturaDAO
{
    public ArrayList<FakturaDTO> sveFakture();
    public FakturaDTO faktura(int idFaktura);
    public FakturaDTO fakturaDatum(Date datumFakturisanja);
    public boolean dodajFakturu(FakturaDTO faktura);
    public boolean azurirajFakturu(FakturaDTO faktura);
    public boolean obrisiFakturu(int idFaktura);
}
