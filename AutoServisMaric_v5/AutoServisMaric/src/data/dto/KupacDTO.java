package data.dto;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class KupacDTO implements Serializable{
    private Integer idKupac;
    private String ime;
    private String prezime;
    private String naziv;
    private String telefon;
    private String adresa;
    private String grad;

    public KupacDTO(){
        
    }
    
    public KupacDTO(Integer idKupac, String ime, String prezime, String naziv, String telefon, String adresa, String grad) {
        this.idKupac = idKupac;
        this.ime = ime;
        this.prezime = prezime;
        this.naziv = naziv;
        this.telefon = telefon;
        this.adresa = adresa;
        this.grad = grad;
    }

    public Integer getIdKupac() {
        return idKupac;
    }

    public void setIdKupac(Integer idKupac) {
        this.idKupac = idKupac;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.idKupac);
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
        final KupacDTO other = (KupacDTO) obj;
        if (!Objects.equals(this.idKupac, other.idKupac)) {
            return false;
        }
        return true;
    }
    
}
