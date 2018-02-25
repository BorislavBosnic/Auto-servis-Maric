/*
 * To change this license header, choose License Headerez in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.dialog.DetaljiZaposlenogDialog;
import autoservismaric.dialog.OtpustiRadnikaDialog;
import autoservismaric.forms.HomeForm1;
import data.dao.DAOFactory;
import data.dto.ZaposleniDTO;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import data.dto.ZaposleniPomocniDTO;
import java.util.ArrayList;

/**
 *
 * @author haker
 */
public class ZaposleniLogika extends Thread{
    
    private String opcija;
    private HomeForm1 homeForm;

    public ZaposleniLogika(String opcija,HomeForm1 homeForm){
        this.opcija=opcija;
        this.homeForm=homeForm;
    }
  
   @Override
   public void run(){
        if("insert".equals(opcija)){
           dodajZaposlenog();           
       }else if("detaljan opis".equals(opcija)){
           opisRadnika();
       }else if("svi".equals(opcija)){
           popuniTabeluRadnika();
       }else if("statistika".equals(opcija)){
           popuniTabeluRadnihNaloga();
       }else if("izmjeni".equals(opcija)){
           izmjeniRadnika();
       }else if("otpusti".equals(opcija)){
           otpustiRadnika();
       }
   }
   
   private void otpustiRadnika(){
       int redniBroj = homeForm.getTableZaposleni().getSelectedRow();
        if (redniBroj != -1) {
            ArrayList<ZaposleniDTO> zaposleni = new ArrayList<ZaposleniDTO>(DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni());
            ZaposleniDTO selektovanRadnik = zaposleni.get(redniBroj);
            new OtpustiRadnikaDialog(homeForm, true, selektovanRadnik).show();
        } else {
            JOptionPane.showMessageDialog(null, "Niste odabrali zaposlenog!", "Problem", JOptionPane.ERROR_MESSAGE);
        }
   }
   private void izmjeniRadnika(){
       int redniBroj = homeForm.getTableZaposleni().getSelectedRow();
        if (redniBroj != -1) {
            ArrayList<ZaposleniDTO> zaposleni = new ArrayList<ZaposleniDTO>(DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni());
            ZaposleniDTO selektovanRadnik = zaposleni.get(redniBroj);
            new DetaljiZaposlenogDialog(homeForm, true, true, selektovanRadnik).show();
        } else {
            JOptionPane.showMessageDialog(null, "Niste odabrali zaposlenog!", "Problem", JOptionPane.ERROR_MESSAGE);
        }
   }
   private void opisRadnika(){
       int redniBroj = homeForm.getTableZaposleni().getSelectedRow();
        if (redniBroj != -1) {
            ArrayList<ZaposleniDTO> zaposleni = new ArrayList<ZaposleniDTO>(DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni());
            ZaposleniDTO selektovanRadnik = zaposleni.get(redniBroj);
            new DetaljiZaposlenogDialog(homeForm, true, false, selektovanRadnik).show();
        } else {
            JOptionPane.showMessageDialog(null, "Niste odabrali zaposlenog!", "Problem", JOptionPane.ERROR_MESSAGE);
        }
   }
   
   private void popuniTabeluRadnika(){
       DefaultTableModel table=(DefaultTableModel)homeForm.getTableZaposleni().getModel();
       List<ZaposleniDTO> lista=DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni();
       Iterator<ZaposleniDTO> i=lista.iterator();
       table.setRowCount(0);//brise sadrzaj tabele
       ZaposleniDTO rez=new ZaposleniDTO();
       while(i.hasNext()){
           rez=i.next();
           table.addRow(new Object[]{rez.getIme(),rez.getImeOca(),rez.getPrezime(),rez.getTelefon()});
       }
   }
   
   private void popuniTabeluRadnihNaloga(){  
        int redniBroj=homeForm.getTableZaposleni().getSelectedRow();
        if(redniBroj!=-1){
            ArrayList<ZaposleniDTO> zaposleni=new ArrayList<ZaposleniDTO>(DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni());
            ZaposleniDTO selektovanRadnik=zaposleni.get(redniBroj);
            DefaultTableModel table=(DefaultTableModel)homeForm.getTableRadniNalozi().getModel();
            java.sql.Date datumOd=new java.sql.Date(homeForm.getDateChooserDatumOdZaposlenog().getDate().getTime());
            java.sql.Date datumDo=new java.sql.Date(homeForm.getDateChooserDatumDoZaposlenog().getDate().getTime());
            List<ZaposleniPomocniDTO> lista=DAOFactory.getDAOFactory().getZaposleniDAO().sviRadniNaloziZaposlenog(selektovanRadnik,datumOd,datumDo);
            Iterator<ZaposleniPomocniDTO> i=lista.iterator();
            table.setRowCount(0);//brise sadrzaj tabele
            ZaposleniPomocniDTO rez=new ZaposleniPomocniDTO();
            Double ukupanProfit=0.0;
            Integer brojNaloga=0;
            while(i.hasNext()){
               rez=i.next();
               table.addRow(new Object[]{rez.getMarka()
                            ,rez.getModel(),rez.getBrojRegistracije(),
                            rez.getDatumZatvaranjaNaloga()
                            ,rez.getOpis(),rez.getTroskovi(),rez.getCijenaUsluge(),rez.getCijenaUsluge()-rez.getTroskovi()});
               brojNaloga++;
               ukupanProfit+=(rez.getCijenaUsluge()-rez.getTroskovi());
           }
            homeForm.getLabelBrojRadnihNaloga().setText("Radni nalozi: "+brojNaloga);
            homeForm.getLabelOstvareniProfitRadnika().setText("Ostvareni profit radnika: "+ukupanProfit+" KM");
        }else{
            JOptionPane.showMessageDialog(null, "Niste odabrali zaposlenog.", "Problem", JOptionPane.ERROR_MESSAGE);
        }
   }
   
   private void dodajZaposlenog(){
       //provjera imena zaposlenog
       String ime=homeForm.getTextFieldIme().getText();
       String prezime=homeForm.getTextFieldPrezime().getText();
           if("".equals(ime) || "".equals(prezime)){
               JOptionPane.showMessageDialog(null,"Ime i prezime zaposlenog moraju biti uneseni!", "Problem", JOptionPane.ERROR_MESSAGE);
           }else{
               //polja za unos podataka
               String telefon=homeForm.getTextFieldTelefon().getText();
               String adresa=homeForm.getTextFieldAdresa().getText();
               String strucnaSprema=homeForm.getTextFieldStrucnaSprema().getText();
               String imeOca=homeForm.getTextFieldImeOca().getText();
               String brojLicneKarte=homeForm.getTextFieldBrojLicneKarte().getText();
               java.util.Date datumRodjenja=homeForm.getDateChooserDatumRodjenja().getDate();
               java.util.Date datumPrimanja=homeForm.getDateChooserDatumPrimanjaURadniOdnos().getDate();
               String funkcija=homeForm.getTextFieldFunkcijaRadnika().getText();
               //dodajemo zaposlenog
               ZaposleniDTO zaposleni=new ZaposleniDTO(ime, prezime, telefon, adresa, strucnaSprema, imeOca, brojLicneKarte, new java.sql.Date(datumRodjenja.getTime()),funkcija,new java.sql.Date(datumPrimanja.getTime()),null);
               if(DAOFactory.getDAOFactory().getZaposleniDAO().dodajZaposlenog(zaposleni)){
                   JOptionPane.showMessageDialog(null, "Uspješno dodan zaposleni.", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                   popuniTabeluRadnika();
               }else{
                   JOptionPane.showMessageDialog(null, "Zaposleni nije dodan.", "Problem", JOptionPane.ERROR_MESSAGE);
               }
           }
   }

}
