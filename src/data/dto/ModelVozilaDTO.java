package data.dto;

import java.util.Objects;

public class ModelVozilaDTO {
    private Integer IdModelVozila;
    private String marka;
    private String model;
    private boolean aktivan;

    public ModelVozilaDTO(Integer IdModelVozila, String marka, String model) {
        this.IdModelVozila = IdModelVozila;
        this.marka = marka;
        this.model = model;
    }
    
    public ModelVozilaDTO(String marka, String model){
        this.model = model;
        this.marka = marka;
    }
    public ModelVozilaDTO(){   }

    public ModelVozilaDTO(Integer IdModelVozila, String marka, String model, boolean aktivan) {
        this.IdModelVozila = IdModelVozila;
        this.marka = marka;
        this.model = model;
        this.aktivan = aktivan;
    }
    
    public boolean isAktivan() {
        return aktivan;
    }

    public void setAktivan(boolean aktivan) {
        this.aktivan = aktivan;
    }
    
    

    public Integer getIdModelVozila() {
        return IdModelVozila;
    }

    public void setIdModelVozila(Integer IdModelVozila) {
        this.IdModelVozila = IdModelVozila;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.IdModelVozila);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelVozilaDTO other = (ModelVozilaDTO) obj;
        if (!Objects.equals(this.marka, other.marka) && !Objects.equals(this.model, other.model)) {
            return false;
        }
        return true;
    }
}
