
package data.dao;

import data.dto.KupacDTO;
import java.util.ArrayList;
import java.util.List;

public interface KupacDAO {

	public ArrayList<KupacDTO> kupciPrivatni(String ime, String prezime);
        
        public ArrayList<KupacDTO> kupciIme(String ime);
        
        public ArrayList<KupacDTO> sviPrivatni();
        
        public ArrayList<KupacDTO> sviPravni();
        
        public ArrayList<KupacDTO> kupciPrezime(String prezime);
        
        public ArrayList<KupacDTO> kupciPravni(String naziv);
        
        public ArrayList<KupacDTO> sviKupci();

	public boolean dodajKupca(KupacDTO kupac);

	public boolean azurirajKupca(KupacDTO kupac);

	public boolean obrisiKupca(String kupac);

}
