package data.dao;

import data.dto.ModelVozilaDTO;
import java.util.ArrayList;
import java.util.List;

public interface ModelVozilaDAO {
    public ArrayList<ModelVozilaDTO> sviModeli();
    
    public ArrayList<ModelVozilaDTO> getModeli(String marka);
    
    public ModelVozilaDTO model(String marka, String model);
    
    public ModelVozilaDTO model(int id);

    public boolean dodajModel(ModelVozilaDTO model);

    public boolean azurirajModel(ModelVozilaDTO model);

    public boolean obrisiModel(ModelVozilaDTO model);
}
