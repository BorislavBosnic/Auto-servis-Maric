package data.dto;

import java.util.Objects;

public class VoziloDTO {
    private Integer idVozilo;
    private String brojRegistracije;
    private Integer kilovat;
    private Double kubikaza;
    private Integer godiste;
    private Integer idKupac;
    private Integer idModelVozila;
    private String vrstaGoriva;

    public VoziloDTO(){}
    
    public VoziloDTO(Integer idVozilo, String brojRegistracije, Integer kilovat, Double kubikaza, Integer godiste, Integer idKupac, Integer idModelVozila, String vrstaGoriva) {
        this.idVozilo = idVozilo;
        this.brojRegistracije = brojRegistracije;
        this.kilovat = kilovat;
        this.kubikaza = kubikaza;
        this.godiste = godiste;
        this.idKupac = idKupac;
        this.idModelVozila = idModelVozila;
        this.vrstaGoriva = vrstaGoriva;
    }

    public Integer getIdVozilo() {
        return idVozilo;
    }

    public void setIdVozilo(Integer idVozilo) {
        this.idVozilo = idVozilo;
    }

    public String getBrojRegistracije() {
        return brojRegistracije;
    }

    public void setBrojRegistracije(String brojRegistracije) {
        this.brojRegistracije = brojRegistracije;
    }

    public Integer getKilovat() {
        return kilovat;
    }

    public void setKilovat(Integer kilovat) {
        this.kilovat = kilovat;
    }

    public Double getKubikaza() {
        return kubikaza;
    }

    public void setKubikaza(Double kubikaza) {
        this.kubikaza = kubikaza;
    }

    public Integer getGodiste() {
        return godiste;
    }

    public void setGodiste(Integer godiste) {
        this.godiste = godiste;
    }

    public Integer getIdKupac() {
        return idKupac;
    }

    public void setIdKupac(Integer idKupac) {
        this.idKupac = idKupac;
    }

    public Integer getIdModelVozila() {
        return idModelVozila;
    }

    public void setIdModelVozila(Integer idModelVozila) {
        this.idModelVozila = idModelVozila;
    }

    public String getVrstaGoriva() {
        return vrstaGoriva;
    }

    public void setVrstaGoriva(String vrstaGoriva) {
        this.vrstaGoriva = vrstaGoriva;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.idVozilo);
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
        final VoziloDTO other = (VoziloDTO) obj;
        if (!Objects.equals(this.idVozilo, other.idVozilo)) {
            return false;
        }
        return true;
    }
    
}
