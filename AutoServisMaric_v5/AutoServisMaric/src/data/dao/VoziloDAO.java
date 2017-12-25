package data.dao;

import data.dto.VoziloDTO;
import java.util.List;

public interface VoziloDAO {
       
        public List<VoziloDTO> svaVozila();

	public boolean dodajVozilo(VoziloDTO vozilo);

	public boolean azurirajVozilo(VoziloDTO vozilo);

	public boolean obrisiVozilo(VoziloDTO vozilo);
}
