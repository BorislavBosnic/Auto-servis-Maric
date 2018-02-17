package data.dto;

public class RadniNalogRadnikDTO {
    private int idRadniNalog;
    private int idRadnik;
    private String opis;

    public RadniNalogRadnikDTO(int idRadniNalog, int idRadnik, String opis) {
        this.idRadniNalog = idRadniNalog;
        this.idRadnik = idRadnik;
        this.opis = opis;
    }

    public RadniNalogRadnikDTO() {
    }

    public int getIdRadniNalog() {
        return idRadniNalog;
    }

    public void setIdRadniNalog(int idRadniNalog) {
        this.idRadniNalog = idRadniNalog;
    }

    public int getIdRadnik() {
        return idRadnik;
    }

    public void setIdRadnik(int idRadnik) {
        this.idRadnik = idRadnik;
    }
    
    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
    
    
}
