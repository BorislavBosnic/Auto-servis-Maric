/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.dialog.DodajRadniNalogDialog;
import autoservismaric.dialog.IzmijeniVlasnikaDialog;
import autoservismaric.dialog.IzmijeniVoziloDialog;
import autoservismaric.dialog.PregledIstorijePopravkiDialog;
import autoservismaric.forms.HomeForm1;
import static autoservismaric.forms.HomeForm1.idVozila;
import static autoservismaric.forms.HomeForm1.selektovanRed;
import data.AutoSuggestor;
import data.dao.DAOFactory;
import data.dto.KupacDTO;
import data.dto.ModelVozilaDTO;
import data.dto.RadniNalogDTO;
import data.dto.VoziloDTO;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DulleX
 */
public class VoziloKupacMeniLogika {
    public void ucitajPopupZaVlasnike(HomeForm1 forma){
        forma.setPopupMenuVlasnik(new JPopupMenu());
        JMenuItem izmijeniVlasnika = new JMenuItem("Izmijeni vlasnika");

        izmijeniVlasnika.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IzmijeniVlasnikaDialog iv = new IzmijeniVlasnikaDialog(new JFrame(), true, forma.getIdVlasnika());

                iv.setVisible(true);
            }
        });
        forma.getPopupMenuVlasnik().add(izmijeniVlasnika);
        forma.getTableVozila().setComponentPopupMenu(forma.getPopupMenuVlasnik());

        forma.getPopupMenuVlasnik().addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = forma.getTableVozila().rowAtPoint(SwingUtilities.convertPoint(forma.getPopupMenuVlasnik(), new Point(0, 0), forma.getTableVozila()));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = forma.getTableVozila().getModel().getColumnName(0);

                        if (selektovanRed >= 0) {

                            if ("ID".equals(imeKolone)) {
                                forma.setIdVlasnika(Integer.parseInt(forma.getTableVozila().getModel().getValueAt(selektovanRed, column).toString()));
                            }
                        }
                        if (rowAtPoint > -1) {
                            forma.getTableVozila().setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }
    
    public void ucitajPopupZaVozila(HomeForm1 forma){
        forma.setPopupMenu(new JPopupMenu());
        JMenuItem deleteItem = new JMenuItem("Izbrisi vozilo");
        JMenuItem editItem = new JMenuItem("Izmijeni vozilo");
        JMenuItem noviRadniNalog = new JMenuItem("Dodaj nov radni nalog");
        JMenuItem pogledajIstorijuPopravki = new JMenuItem("Pogledaj istoriju popravki");

        pogledajIstorijuPopravki.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new PregledIstorijePopravkiDialog(new JFrame(), true, idVozila).setVisible(true);
            }
        });
        forma.getPopupMenu().add(pogledajIstorijuPopravki);

        noviRadniNalog.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new DodajRadniNalogDialog(new JFrame(), true, idVozila).setVisible(true);
            }
        });
        forma.getPopupMenu().add(noviRadniNalog);

        editItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new IzmijeniVoziloDialog(new JFrame(), true, idVozila).setVisible(true);
            }
        });
        forma.getPopupMenu().add(editItem);

        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(new JFrame(), "Right-click performed on table and choose DELETE");

                ArrayList<RadniNalogDTO> nalozi = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi(idVozila);
                if (nalozi != null && nalozi.size() > 0) {
                    JOptionPane jop = new JOptionPane();
                    int dialogResult = jop.showConfirmDialog(new JFrame(), "Postoje radni nalozi za vozilo koje želite ukloniti.\n "
                            + "Ako nastavite, oni će biti izbrisani, kao i podaci o vozilu.\n Da li ste sigurni da želite da nastavite?", "Upozorenje", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {

                        for (RadniNalogDTO r : nalozi) {
                            DAOFactory.getDAOFactory().getRadniNalogDAO().izbrisiRadniNalog(r.getIdRadniNalog());
                        }

                        DAOFactory.getDAOFactory().getVoziloDAO().obrisiVozilo(idVozila);

                        JOptionPane.showMessageDialog(forma, "Uspješno obrisano!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);

                    } else if (dialogResult == JOptionPane.NO_OPTION) {

                    }
                } else {
                    int dialogResult = JOptionPane.showConfirmDialog(new JFrame(), "Da li ste sigurni želite da izbrišete podatke o vozilu?", "Upozorenje", JOptionPane.YES_NO_OPTION);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        DAOFactory.getDAOFactory().getVoziloDAO().obrisiVozilo(idVozila);

                        JOptionPane.showMessageDialog(forma, "Uspješno obrisano!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                    } else if (dialogResult == JOptionPane.NO_OPTION) {

                    }
                }

            }
        });

        forma.getPopupMenu().add(deleteItem);
        forma.getTableVozila().setComponentPopupMenu(forma.getPopupMenu());

        forma.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = forma.getTableVozila().rowAtPoint(SwingUtilities.convertPoint(forma.getPopupMenu(), new Point(0, 0), forma.getTableVozila()));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = forma.getTableVozila().getModel().getColumnName(0);

                        if (selektovanRed >= 0) {
                            if ("ID".equals(imeKolone)) {
                                idVozila = Integer.parseInt(forma.getTableVozila().getModel().getValueAt(selektovanRed, column).toString());
                            }
                        }
                        if (rowAtPoint > -1) {
                            forma.getTableVozila().setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }
        });

    }
    
    public AutoSuggestor ucitajPreporukeMarke(HomeForm1 forma){
        AutoSuggestor autoSuggestorMarke = new AutoSuggestor(forma.getTfMarkaTrazi(), forma, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> modeli = new ArrayList<>();

                for (ModelVozilaDTO mv : VozilaLogika.modeli) {
                    modeli.add(mv.getMarka());
                }

                setDictionary(modeli);
                //addToDictionary("bye");//adds a single word

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };
        return autoSuggestorMarke;
    }
    
    public AutoSuggestor ucitajPreporukeModel(HomeForm1 forma){
         AutoSuggestor autoSuggestorModel = new AutoSuggestor(forma.getTfModelTrazi(), forma, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                String marka = forma.getTfMarkaTrazi().getText();

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> modeli = new ArrayList<>();

                for (ModelVozilaDTO mv : VozilaLogika.modeli) {
                    if (forma.getTfModelTrazi().getText() != null && !"".equals(forma.getTfModelTrazi().getText())) {
                        if (mv.getMarka().equals(marka.trim())) {
                            modeli.add(mv.getModel());
                        }
                    } else if (forma.getTfModelTrazi().getText() == null || "".equals(forma.getTfModelTrazi().getText())) {
                        modeli.add(mv.getModel());
                    }
                }

                setDictionary(modeli);
                //addToDictionary("bye");//adds a single word

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };
        return autoSuggestorModel;
    }
    
    public AutoSuggestor ucitajPreporukeRegistracija(HomeForm1 forma){
        AutoSuggestor autoSuggestorRegistracija = new AutoSuggestor(forma.getTfRegistracijaTrazi(), forma, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> registracije = new ArrayList<>();

                for (VoziloDTO v : VozilaLogika.vozila) {
                    registracije.add(v.getBrojRegistracije());
                }

                setDictionary(registracije);
                //addToDictionary("bye");//adds a single word

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };

        return autoSuggestorRegistracija;
    }
    
    public AutoSuggestor ucitajPreporukeVlasnika(HomeForm1 forma){
        AutoSuggestor autoSuggestorVlasnik = new AutoSuggestor(forma.getTfPrezimeVozilo(), forma, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> vlasnici = new ArrayList<>();

                for (KupacDTO v : VozilaLogika.vlasnici) {
                    if (v.getNaziv() == null) {
                        vlasnici.add(v.getIme() + " " + v.getPrezime());
                    }
                }

                setDictionary(vlasnici);
                //addToDictionary("bye");//adds a single word

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };

        return autoSuggestorVlasnik;
    }
    
    public AutoSuggestor ucitajPreporukePravniNaziv(HomeForm1 forma){
        AutoSuggestor autoSuggestorPravniNaziv = new AutoSuggestor(forma.getTfNazivVozilo(), forma, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> vlasnici = new ArrayList<>();

                for (KupacDTO v : VozilaLogika.vlasnici) {
                    if (v.getPrezime() == null && v.getIme() == null) {
                        vlasnici.add(v.getNaziv());
                    }
                }

                setDictionary(vlasnici);

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };

        return autoSuggestorPravniNaziv;
    }
    
    public void prikaziKupceUTabeli(ArrayList<KupacDTO> kupci, HomeForm1 forma){
        String[] columns = {"ID", "Ime", "Prezime", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        forma.getTableVozila().setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), k.getIme(), k.getPrezime(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }
        forma.getTableVozila().setModel(model);
    }
    
    public void traziKupce(HomeForm1 forma){
        forma.izbrisiPopupZaVozila();
        forma.ucitajPopupZaVlasnike();
        String izabrano = "";

        for (Enumeration<AbstractButton> buttons = forma.getbGTraziVlasnika().getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                izabrano = button.getText();
            }
        }

        if ("Privatno lice".equals(izabrano)) {
            String ime = forma.getTfImeTrazi().getText();
            String prezime = forma.getTfPrezimeTrazi().getText();

            if ((ime == null || "".equals(ime)) && (prezime == null || "".equals(prezime))) {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviPrivatni();

                prikaziKupceUTabeli(kupci, forma);

            } else if (prezime == null || "".equals(prezime)) {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciIme(ime);

                prikaziKupceUTabeli(kupci, forma);
            } else if (ime == null || "".equals(ime)) {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPrezime(prezime);

                prikaziKupceUTabeli(kupci, forma);
            } else {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPrivatni(ime, prezime);

                prikaziKupceUTabeli(kupci, forma);
            }
        } else if ("Pravno lice".equals(izabrano)) {
            String naziv = forma.getTfNazivTrazi().getText();

            if (naziv == null || "".equals(naziv)) {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviPravni();

                prikaziKupcePravneUTabeli(kupci, forma);
            } else {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPravni(naziv);

                prikaziKupcePravneUTabeli(kupci, forma);
            }
        }


    }
    
    public void prikaziKupcePravneUTabeli(ArrayList<KupacDTO> kupci, HomeForm1 forma){
        String[] columns = {"ID", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        forma.getTableVozila().setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }
        forma.getTableVozila().setModel(model);
    }
    
    public void prikaziKupceSveUTabeli(ArrayList<KupacDTO> kupci, HomeForm1 forma){
        String[] columns = {"ID", "Ime", "Prezime", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        forma.getTableVozila().setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), (k.getIme() == null) ? "---" : k.getIme(), (k.getPrezime() == null) ? "---" : k.getPrezime(), (k.getNaziv() == null) ? "---" : k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }

        forma.getTableVozila().setModel(model);
    }
}
