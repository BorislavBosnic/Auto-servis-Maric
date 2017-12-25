
package data.dao;

import data.dto.KupacDTO;
import java.util.List;

public interface KupacDAO {

	public List<KupacDTO> kupci(String ime, String prezime);
        
        public List<KupacDTO> kupci(String naziv);
        
        public List<KupacDTO> sviKupci();

	public boolean dodajKupca(KupacDTO kupac);

	public boolean azurirajKupca(KupacDTO kupac);

	public boolean obrisiKupca(String kupac);

}
