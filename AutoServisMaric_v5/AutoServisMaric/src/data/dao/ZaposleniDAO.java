/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.dao;

import data.dto.ZaposleniDTO;
import java.util.List;

/**
 *
 * @author haker
 */
public interface ZaposleniDAO {
    
    public List<ZaposleniDTO> sviZaposleni();

	public boolean dodajZaposlenog(ZaposleniDTO zaposleni);

	public boolean azurirajZaposlenog(ZaposleniDTO zaposleni);

	public boolean obrisiZaposlenog(ZaposleniDTO zaposleni);
}
