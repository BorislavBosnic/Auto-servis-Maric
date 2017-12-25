package data.dao;

import data.dto.ModelVozilaDTO;
import java.util.List;

public interface ModelVozilaDAO {
    public List<ModelVozilaDTO> sviModeli();

    public boolean dodajModel(ModelVozilaDTO model);

    public boolean azurirajModel(ModelVozilaDTO model);

    public boolean obrisiModel(ModelVozilaDTO model);
}
