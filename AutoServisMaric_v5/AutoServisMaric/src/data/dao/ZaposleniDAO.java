/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.ZaposleniDTO;
import data.dto.ZaposleniPomocniDTO;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author haker
 */
public interface ZaposleniDAO {
    
    public List<ZaposleniDTO> sviZaposleni();
    
    public List<ZaposleniDTO> sviBivsiZaposleni();

    public boolean dodajZaposlenog(ZaposleniDTO zaposleni);

    public boolean azurirajZaposlenog(ZaposleniDTO zaposleni);

    public boolean obrisiZaposlenog(ZaposleniDTO zaposleni);
    
    public boolean ponistiOtkaz(ZaposleniDTO zaposleni);
    
    public List<ZaposleniPomocniDTO> sviRadniNaloziZaposlenog(ZaposleniDTO zaposleni,Date datumOd,Date datumDo);
}
