package data.dao;

import data.dto.VoziloDTO;
import java.util.ArrayList;
import java.util.List;

public interface VoziloDAO {
       
        public boolean obrisiVozilo(int id);
        
        public ArrayList<VoziloDTO> vozila(int idVlasnika);
    
        public ArrayList<VoziloDTO> dobijVozila(String query);
        
        public ArrayList<VoziloDTO> svaVozila();

	public boolean dodajVozilo(VoziloDTO vozilo);

	public boolean azurirajVozilo(VoziloDTO vozilo);

	public boolean obrisiVozilo(VoziloDTO vozilo);
        
        public VoziloDTO vozilo(int id);
}
