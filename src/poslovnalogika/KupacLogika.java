/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.dialog.DodajVlasnikaDialog;
import autoservismaric.dialog.IzmijeniVlasnikaDialog;
import data.dao.DAOFactory;
import data.dto.KupacDTO;
import java.awt.Color;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

/**
 *
 * @author DulleX
 */
public class KupacLogika {

    public void inicijalizacijaDodajDijaloga(DodajVlasnikaDialog dijalog) {
        dijalog.getBg().add(dijalog.getRbPravno());
        dijalog.getBg().add(dijalog.getRbPrivatno());
        dijalog.getTfNazivDodaj().setEditable(false);
        dijalog.getTfNazivDodaj().setBackground(Color.gray);
    }

    public void dodajKupca(DodajVlasnikaDialog dijalog) {
        if (dijalog.getRbPravno().isSelected()) {
            String naziv = dijalog.getTfNazivDodaj().getText();
            String telefon = dijalog.getTfTelefonDodaj().getText();
            String adresa = dijalog.getTfGradDodaj().getText();
            String grad = dijalog.getTfGradDodaj().getText();
            KupacDTO kupac = new KupacDTO();
            kupac.setNaziv(naziv);
            kupac.setTelefon(telefon);
            kupac.setAdresa(adresa);
            kupac.setGrad(grad);
            kupac.setIme(null);
            kupac.setPrezime(null);

            if (naziv != null && !"".equals(naziv)) {
                if (DAOFactory.getDAOFactory().getKupacDAO().dodajKupca(kupac)) {
                    JOptionPane.showMessageDialog(dijalog, "Uspješno dodat vlasnik vozila!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                    dijalog.setKup(kupac);
                    dijalog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dijalog, "Greška!", "Greška", JOptionPane.OK_OPTION);
                }
            } else {
                JOptionPane.showMessageDialog(dijalog, "Morate popuniti polje naziv pravnog lica!", "Greška", JOptionPane.OK_OPTION);
            }
        } else {
            String ime = dijalog.getTfImeDodaj().getText();
            String prezime = dijalog.getTfPrezimeDodaj().getText();
            String telefon = dijalog.getTfTelefonDodaj().getText();
            String adresa = dijalog.getTfAdresaDodaj().getText();
            String grad = dijalog.getTfGradDodaj().getText();
            KupacDTO kupac = new KupacDTO();
            kupac.setIme(ime);
            kupac.setPrezime(prezime);
            kupac.setTelefon(telefon);
            kupac.setAdresa(adresa);
            kupac.setGrad(grad);
            kupac.setNaziv(null);

            if (ime != null && prezime != null && !"".equals(ime) && !"".equals(prezime)) {

                if (DAOFactory.getDAOFactory().getKupacDAO().dodajKupca(kupac)) {
                    JOptionPane.showMessageDialog(dijalog, "Uspješno dodat vlasnik vozila!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                    dijalog.setKup(kupac);
                    dijalog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dijalog, "Greška!", "Greška", JOptionPane.OK_OPTION);
                }
            } else {
                JOptionPane.showMessageDialog(dijalog, "Morate popuniti polja za ime i prezime!", "Greška", JOptionPane.OK_OPTION);
            }

        }
    }
    
    public void inicijalizacijaIzmijeniDijaloga(IzmijeniVlasnikaDialog dijalog){
        dijalog.getBg().add(dijalog.getRbPravno());
        dijalog.getBg().add(dijalog.getRbPrivatno());
 
        KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(dijalog.getIdVlasnika());
        if(kupac.getNaziv() == null){
            
            for (Enumeration<AbstractButton> buttons = dijalog.getBg().getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if ("Privatno".equals(button.getText())) {
                button.setSelected(true);
            }
            }
            
            dijalog.getTfImeDodaj().setText(kupac.getIme());
            dijalog.getTfPrezimeDodaj().setText(kupac.getPrezime()); 
            dijalog.getTfNazivDodaj().setEditable(false);
            dijalog.getTfNazivDodaj().setBackground(Color.gray);
            dijalog.getTfNazivDodaj().setText("");
                  
        }
        else{
            for (Enumeration<AbstractButton> buttons = dijalog.getBg().getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if ("Pravno".equals(button.getText())) {
                button.setSelected(true);
            }
            }
 
            dijalog.getTfNazivDodaj().setText(kupac.getNaziv());
            dijalog.getTfImeDodaj().setText("");
            dijalog.getTfPrezimeDodaj().setText("");
            dijalog.getTfImeDodaj().setBackground(Color.gray);
            dijalog.getTfPrezimeDodaj().setBackground(Color.gray);
            dijalog.getTfImeDodaj().setEditable(false);
            dijalog.getTfPrezimeDodaj().setEditable(false);
        }
        dijalog.getTfAdresaDodaj().setText(kupac.getAdresa());
        dijalog.getTfTelefonDodaj().setText(kupac.getTelefon());
        dijalog.getTfGradDodaj().setText(kupac.getGrad());
    }
    
    public void izmijeniKupca(IzmijeniVlasnikaDialog dijalog){
        KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(dijalog.getIdVlasnika());
         if (dijalog.getRbPravno().isSelected()) {
            String naziv = dijalog.getTfNazivDodaj().getText();
            String telefon = dijalog.getTfTelefonDodaj().getText();
            String adresa = dijalog.getTfGradDodaj().getText();
            String grad = dijalog.getTfGradDodaj().getText();
            kupac.setNaziv(naziv);
            kupac.setTelefon(telefon);
            kupac.setAdresa(adresa);
            kupac.setGrad(grad);

            if (naziv != null && !"".equals(naziv)) {
                if (DAOFactory.getDAOFactory().getKupacDAO().azurirajKupca(kupac)) {
                    JOptionPane.showMessageDialog(dijalog, "Uspješno izmijenjeni podaci o vlasniku vozila!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                    dijalog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dijalog, "Greška!", "Greška", JOptionPane.OK_OPTION);
                }
            } else {
                JOptionPane.showMessageDialog(dijalog, "Morate popuniti polje naziv pravnog lica!", "Greška", JOptionPane.OK_OPTION);
            }
        } else {
            String ime = dijalog.getTfImeDodaj().getText();
            String prezime = dijalog.getTfPrezimeDodaj().getText();
            String telefon = dijalog.getTfTelefonDodaj().getText();
            String adresa = dijalog.getTfAdresaDodaj().getText();
            String grad = dijalog.getTfGradDodaj().getText();
            kupac.setIme(ime);
            kupac.setPrezime(prezime);
            kupac.setTelefon(telefon);
            kupac.setAdresa(adresa);
            kupac.setGrad(grad);

            if (ime != null && prezime != null && !"".equals(ime) && !"".equals(prezime)) {
                
                if (DAOFactory.getDAOFactory().getKupacDAO().azurirajKupca(kupac)) {
                    JOptionPane.showMessageDialog(dijalog, "Uspješno izmijenjeni podaci podaci o vlasniku vozila!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                    dijalog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dijalog, "Greška!", "Greška", JOptionPane.OK_OPTION);
                }
            } else {
                JOptionPane.showMessageDialog(dijalog, "Morate popuniti polja za ime i prezime!", "Greška", JOptionPane.OK_OPTION);
            }

        }
    }
}
