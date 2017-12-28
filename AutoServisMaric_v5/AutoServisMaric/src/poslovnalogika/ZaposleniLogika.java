/*
 * To change this license header, choose License Headerez in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import data.dao.DAOFactory;
import data.dto.ZaposleniDTO;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static autoservismaric.forms.HomeForm1.jTableZaposleni;
import static autoservismaric.forms.HomeForm1.tableRadniNalozi;
import data.dto.ZaposleniPomocniDTO;
import java.util.ArrayList;

/**
 *
 * @author haker
 */
public class ZaposleniLogika extends Thread{
    
    private String opcija;
    private String ime;
    private String prezime;
    private String telefon;
    private String adresa;
    private String strucnaSprema;
    private String imeOca;
    private String brojLicneKarte;
    private java.util.Date datumRodjenja;
    private java.util.Date datumPrimanja;
    private String funkcija;

    private ArrayList<ZaposleniDTO> zaposleni=new ArrayList<ZaposleniDTO>(DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni());

    private java.sql.Date datumOd;
    private java.sql.Date datumDo;
    
    public ZaposleniLogika(String opcija){
        this.opcija=opcija;
    }

    public ZaposleniLogika(String opcija, java.sql.Date datumOd, java.sql.Date datumDo) {
        this.opcija = opcija;
        this.datumOd = datumOd;
        this.datumDo = datumDo;
    }
    
    
   
    public ZaposleniLogika(String opcija, String ime, String prezime, String telefon, String adresa, String strucnaSprema, String imeOca, String brojLicneKarte, Date datumRodjenja, Date datumPrimanja, String funkcija) {
        this.opcija = opcija;
        this.ime = ime;
        this.prezime = prezime;
        this.telefon = telefon;
        this.adresa = adresa;
        this.strucnaSprema = strucnaSprema;
        this.imeOca = imeOca;
        this.brojLicneKarte = brojLicneKarte;
        this.datumRodjenja = datumRodjenja;
        this.datumPrimanja = datumPrimanja;
        this.funkcija = funkcija;
    }     
   
   @Override
   public void run(){
       if("select".equals(opcija)){
           
       }else if("insert".equals(opcija)){
           dodajZaposlenog();
           
       }else if("delete".equals(opcija)){
           
       }else if("svi".equals(opcija)){
           popuniTabeluRadnika();
       }else if("statistika".equals(opcija)){
           popuniTabeluRadnihNaloga();
           izracunajProfit();
       }else{
           System.out.println("ERROR");
       }
   }
   
   private void popuniTabeluRadnika(){
        DefaultTableModel table=(DefaultTableModel)jTableZaposleni.getModel();
        List<ZaposleniDTO> lista=DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni();
       Iterator<ZaposleniDTO> i=lista.iterator();
       table.setRowCount(0);//brise sadrzaj tabele
       ZaposleniDTO rez=new ZaposleniDTO();
       while(i.hasNext()){
           rez=i.next();
           table.addRow(new Object[]{rez.getIme(),rez.getPrezime(),rez.getImeOca(),rez.getAdresa(),rez.getTelefon()
           ,rez.getBrojLicneKarte(),rez.getStrucnaSprema(),rez.getDatumRodjenja(),rez.getDatumOd(),rez.getDatumDo(),rez.getFunkcija()});
       }
        //table.setRowCount(10);
   }
   
   private void popuniTabeluRadnihNaloga(){  
        int redniBroj=jTableZaposleni.getSelectedRow();
        if(redniBroj!=-1){
            ZaposleniDTO selektovanRadnik=zaposleni.get(redniBroj);
            DefaultTableModel table=(DefaultTableModel)tableRadniNalozi.getModel();
            List<ZaposleniPomocniDTO> lista=DAOFactory.getDAOFactory().getZaposleniDAO().sviRadniNaloziZaposlenog(selektovanRadnik,datumOd,datumDo);
            Iterator<ZaposleniPomocniDTO> i=lista.iterator();
            table.setRowCount(0);//brise sadrzaj tabele
            ZaposleniPomocniDTO rez=new ZaposleniPomocniDTO();
            while(i.hasNext()){
               rez=i.next();
               table.addRow(new Object[]{rez.getMarka()
                            ,rez.getModel(),rez.getBrojRegistracije(),
                            rez.getDatumZatvaranjaNaloga()
                            ,rez.getOpis(),rez.getTroskovi(),rez.getCijenaUsluge()});
           }
        }else{
            JOptionPane.showMessageDialog(null, "Niste odabrali zaposlenog.", "Problem", JOptionPane.ERROR_MESSAGE);
        }
   }
   
   private void izracunajProfit(){
       
   }
   
   private void dodajZaposlenog(){
       //provjera imena zaposlenog
           if("".equals(ime) || "".equals(prezime)){
               JOptionPane.showMessageDialog(null,"Ime i prezime zaposlenog moraju biti uneseni!", "Problem", JOptionPane.ERROR_MESSAGE);
           }else{
               //dodajemo zaposlenog
               ZaposleniDTO zaposleni=new ZaposleniDTO(ime, prezime, telefon, adresa, strucnaSprema, imeOca, brojLicneKarte, new java.sql.Date(datumRodjenja.getTime()),funkcija,new java.sql.Date(datumPrimanja.getTime()),null);
               if(DAOFactory.getDAOFactory().getZaposleniDAO().dodajZaposlenog(zaposleni)){
                   JOptionPane.showMessageDialog(null, "Uspjesno dodan zaposleni.", "Obavjestenje", JOptionPane.INFORMATION_MESSAGE);
                   popuniTabeluRadnika();
               }else{
                   JOptionPane.showMessageDialog(null, "Zaposleni nije dodan.", "Problem", JOptionPane.ERROR_MESSAGE);
               }
           }
   }
}
