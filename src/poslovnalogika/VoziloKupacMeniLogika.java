/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.dialog.DodajRadniNalogDialog;
import autoservismaric.dialog.IzaberiVoziloDialog;
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
import data.dto.RadniNalogParametri;
import data.dto.RezultatRNPretrazivanje;
import data.dto.VoziloDTO;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author DulleX
 */
public class VoziloKupacMeniLogika {

    public void ucitajPopupZaVlasnike(HomeForm1 forma) {
        forma.setPopupMenuVlasnik(new JPopupMenu());
        JMenuItem izmijeniVlasnika = new JMenuItem("Izmijeni vlasnika");

        izmijeniVlasnika.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IzmijeniVlasnikaDialog iv = new IzmijeniVlasnikaDialog(new JFrame(), true, forma.getIdVlasnika(), forma);

                iv.setVisible(true);
            }
        });
        forma.getPopupMenuVlasnik().add(izmijeniVlasnika);
        forma.getTableVozila().setComponentPopupMenu(forma.getPopupMenuVlasnik());

        JMenuItem izbrisiVlasnika = new JMenuItem("Izbriši vlasnika");
        izbrisiVlasnika.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(forma, "Da li ste sigurni da želite da izbrišete podatke o izabranom vlasniku vozila?", "Upozorenje!", dialogButton);
                if (dialogResult == 0) {
                    if (DAOFactory.getDAOFactory().getKupacDAO().obrisiKupca(forma.getIdVlasnika())) {
                        JOptionPane.showMessageDialog(forma, "Uspješno obrisani podaci o vlasniku vozila!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                        for (int i = forma.getTableVozila().getModel().getRowCount() - 1; i >= 0; i--) {
                            if ((Integer.parseInt(forma.getTableVozila().getModel().getValueAt(i, 0).toString())) == forma.getIdVlasnika()) {
                                ((DefaultTableModel) forma.getTableVozila().getModel()).removeRow(i);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(forma, "Nemoguće brisanje.", "Greška", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                }
            }
        });
        forma.getPopupMenuVlasnik().add(izbrisiVlasnika);

        forma.getPopupMenuVlasnik().addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = forma.getTableVozila().rowAtPoint(SwingUtilities.convertPoint(forma.getPopupMenuVlasnik(), new Point(0, 0), forma.getTableVozila()));
                        selektovanRed = rowAtPoint;
                        int column = 0;
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

    public void ucitajPopupZaVozila(HomeForm1 forma) {
        forma.setPopupMenu(new JPopupMenu());
        JMenuItem deleteItem = new JMenuItem("Izbriši vozilo");
        JMenuItem editItem = new JMenuItem("Izmijeni vozilo");
        JMenuItem noviRadniNalog = new JMenuItem("Dodaj novi radni nalog");
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
                new DodajRadniNalogDialog(new JFrame(), true, idVozila, false, forma).setVisible(true);
            }
        });
        forma.getPopupMenu().add(noviRadniNalog);

        editItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new IzmijeniVoziloDialog(new JFrame(), true, idVozila, forma).setVisible(true);
            }
        });
        forma.getPopupMenu().add(editItem);

        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<RadniNalogDTO> nalozi = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi(idVozila);
                if (nalozi != null && nalozi.size() > 0) {
                    JOptionPane jop = new JOptionPane();
                    int dialogResult = jop.showConfirmDialog(new JFrame(), "Postoje radni nalozi za vozilo koje želite ukloniti.\n "
                            + "Ako nastavite, oni će biti izbrisani, kao i podaci o vozilu.\n Da li ste sigurni da želite da nastavite?", "Upozorenje", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {

//                        for (RadniNalogDTO r : nalozi) {
//                            DAOFactory.getDAOFactory().getRadniNalogDAO().izbrisiRadniNalog(r.getIdRadniNalog());
//                        }
                        if (DAOFactory.getDAOFactory().getVoziloDAO().obrisiVozilo(idVozila)) {
                            JOptionPane.showMessageDialog(forma, "Uspješno obrisano!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                            for (int i = forma.getTableVozila().getModel().getRowCount() - 1; i >= 0; i--) {
                                if ((Integer.parseInt(forma.getTableVozila().getModel().getValueAt(i, 0).toString())) == idVozila) {
                                    ((DefaultTableModel) forma.getTableVozila().getModel()).removeRow(i);
                                }
                            }
                        }
                    } else if (dialogResult == JOptionPane.NO_OPTION) {

                    }
                } else {
                    int dialogResult = JOptionPane.showConfirmDialog(new JFrame(), "Da li ste sigurni želite da izbrišete podatke o vozilu?", "Upozorenje", JOptionPane.YES_NO_OPTION);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        if (DAOFactory.getDAOFactory().getVoziloDAO().obrisiVozilo(idVozila)) {

                            JOptionPane.showMessageDialog(forma, "Uspješno obrisano!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);

                            for (int i = forma.getTableVozila().getModel().getRowCount() - 1; i >= 0; i--) {
                                if ((Integer.parseInt(forma.getTableVozila().getModel().getValueAt(i, 0).toString())) == idVozila) {
                                    ((DefaultTableModel) forma.getTableVozila().getModel()).removeRow(i);
                                }
                            }
                        }
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
                        //int row = forma.getTableVozila().getSelectedRow();
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

    public AutoSuggestor ucitajPreporukeMarke(HomeForm1 forma) {
        AutoSuggestor autoSuggestorMarke = new AutoSuggestor(forma.getTfMarkaTrazi(), forma, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> marke = new ArrayList<>();
                ArrayList<ModelVozilaDTO> modeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();

                for (ModelVozilaDTO mv : modeli) {
                    marke.add(mv.getMarka());
                }

                setDictionary(marke);

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };
        return autoSuggestorMarke;
    }

    public AutoSuggestor ucitajPreporukeModel(HomeForm1 forma) {
        AutoSuggestor autoSuggestorModel = new AutoSuggestor(forma.getTfModelTrazi(), forma, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                String marka = forma.getTfMarkaTrazi().getText();

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> modeli = new ArrayList<>();

                ArrayList<ModelVozilaDTO> markaModeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();

                for (ModelVozilaDTO mv : markaModeli) {
                    if (forma.getTfModelTrazi().getText() != null && !"".equals(forma.getTfModelTrazi().getText())) {
                        if (mv.getMarka().equals(marka.trim())) {
                            modeli.add(mv.getModel());
                        }
                    } else if (forma.getTfModelTrazi().getText() == null || "".equals(forma.getTfModelTrazi().getText())) {
                        modeli.add(mv.getModel());
                    }
                }

                setDictionary(modeli);

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };
        return autoSuggestorModel;
    }

    public AutoSuggestor ucitajPreporukeRegistracija(HomeForm1 forma) {
        AutoSuggestor autoSuggestorRegistracija = new AutoSuggestor(forma.getTfRegistracijaTrazi(), forma, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                ArrayList<String> registracije = new ArrayList<>();
                ArrayList<VoziloDTO> vozila = DAOFactory.getDAOFactory().getVoziloDAO().svaVozila();

                for (VoziloDTO v : vozila) {
                    registracije.add(v.getBrojRegistracije());
                }

                setDictionary(registracije);

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };

        return autoSuggestorRegistracija;
    }

    public AutoSuggestor ucitajPreporukeVlasnika(HomeForm1 forma) {
        AutoSuggestor autoSuggestorVlasnik = new AutoSuggestor(forma.getTfPrezimeVozilo(), forma, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                ArrayList<String> vlasnici = new ArrayList<>();
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviKupci();

                for (KupacDTO v : kupci) {
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

    public AutoSuggestor ucitajPreporukePravniNaziv(HomeForm1 forma) {
        AutoSuggestor autoSuggestorPravniNaziv = new AutoSuggestor(forma.getTfNazivVozilo(), forma, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                ArrayList<String> vlasnici = new ArrayList<>();

                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviKupci();

                for (KupacDTO v : kupci) {
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

    public void prikaziKupceUTabeli(ArrayList<KupacDTO> kupci, HomeForm1 forma) {
        String[] columns = {"ID", "Ime", "Prezime", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        forma.getTableVozila().setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), k.getIme(), k.getPrezime(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }
        forma.getTableVozila().setModel(model);
    }

    public void traziKupce(HomeForm1 forma) {
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

    public void prikaziKupcePravneUTabeli(ArrayList<KupacDTO> kupci, HomeForm1 forma) {
        String[] columns = {"ID", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        forma.getTableVozila().setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }
        forma.getTableVozila().setModel(model);
    }

    public void prikaziKupceSveUTabeli(ArrayList<KupacDTO> kupci, HomeForm1 forma) {
        String[] columns = {"ID", "Ime", "Prezime", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        forma.getTableVozila().setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), (k.getIme() == null) ? "---" : k.getIme(), (k.getPrezime() == null) ? "---" : k.getPrezime(), (k.getNaziv() == null) ? "---" : k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }

        forma.getTableVozila().setModel(model);
    }

    public void prikaziSvaVozila(HomeForm1 forma) {
        ArrayList<VoziloDTO> vozila = DAOFactory.getDAOFactory().getVoziloDAO().svaVozila();
        String[] columns = {"ID", "Registracija", "Marka", "Model", "Godište", "Prezime", "Ime", "Naziv", "Gorivo", "Kilovat", "Kubikaža"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        forma.getTableVozila().setModel(model);

        for (VoziloDTO v : vozila) {
            KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(v.getIdKupac());
            ModelVozilaDTO mod = new ModelVozilaDTO();
            ArrayList<ModelVozilaDTO> modeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
            for (ModelVozilaDTO m : modeli) {
                if (m.getIdModelVozila() == v.getIdModelVozila()) {
                    mod = m;
                }
            }

            Object[] rowData = {v.getIdVozilo(), v.getBrojRegistracije() == null ? "" : v.getBrojRegistracije(), mod.getMarka(), mod.getModel(), v.getGodiste() == null ? "" : v.getGodiste(), (kupac.getPrezime() == null || "".equals(kupac.getPrezime())) ? "---" : kupac.getPrezime(), (kupac.getIme() == null || "".equals(kupac.getIme())) ? "---" : kupac.getIme(), (kupac.getNaziv() == null || "".equals(kupac.getNaziv())) ? "---" : kupac.getNaziv(), v.getVrstaGoriva() == null ? "" : v.getVrstaGoriva(), v.getKilovat() == null ? "" : v.getKilovat(), v.getKubikaza() == null ? "" : v.getKubikaza()};
            model.addRow(rowData);
        }

        forma.getTableVozila().setModel(model);
    }

    public void pronadjiVozilo(HomeForm1 forma) {
        String registracija = forma.getTfRegistracijaTrazi().getText();
        Integer godiste = 0;

        if (forma.getTfGodisteTrazi().getText() != null && !"".equals(forma.getTfGodisteTrazi().getText())) {
            try {
                godiste = Integer.parseInt(forma.getTfGodisteTrazi().getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(forma, "Pogrešan format godišta. Mora da bude cjelobrojni podatak. ", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        }

        String marka = forma.getTfMarkaTrazi().getText();
        String model = forma.getTfModelTrazi().getText();
        boolean svi = forma.getCbSvi().isSelected();

        if (svi) {

            String[] columns = {"ID", "Registracija", "Marka", "Model", "Godište", "Prezime", "Ime", "Naziv", "Gorivo", "Kilovat", "Kubikaža"};
            DefaultTableModel modell = new DefaultTableModel(columns, 0);
            forma.getTableVozila().setModel(modell);

            ArrayList<VoziloDTO> vozila = DAOFactory.getDAOFactory().getVoziloDAO().svaVozila();
            for (VoziloDTO v : vozila) {

                KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(v.getIdKupac());
                ModelVozilaDTO mod = new ModelVozilaDTO();
                ArrayList<ModelVozilaDTO> modeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
                for (ModelVozilaDTO m : modeli) {
                    if (m.getIdModelVozila() == v.getIdModelVozila()) {
                        mod = m;
                    }
                }

                boolean dodati = true;
                if (registracija != null && !"".equals(registracija)) {
                    if (!v.getBrojRegistracije().toLowerCase().startsWith(registracija.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (godiste != 0) {
                    if (!Objects.equals(v.getGodiste(), godiste)) {
                        dodati = false;
                    }
                }
                if (marka != null && !"".equals(marka)) {
                    if (!mod.getMarka().toLowerCase().equals(marka.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (model != null && !"".equals(model)) {
                    if (!mod.getModel().toLowerCase().equals(model.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (dodati == true) {
                    Object[] rowData = {v.getIdVozilo(), v.getBrojRegistracije() == null ? "" : v.getBrojRegistracije(), mod.getMarka(), mod.getModel(), v.getGodiste() == null ? "" : v.getGodiste(), (kupac.getPrezime() == null || "".equals(kupac.getPrezime())) ? "---" : kupac.getPrezime(), (kupac.getIme() == null || "".equals(kupac.getIme())) ? "---" : kupac.getIme(), (kupac.getNaziv() == null || "".equals(kupac.getNaziv())) ? "---" : kupac.getNaziv(), v.getVrstaGoriva() == null ? "" : v.getVrstaGoriva(), v.getKilovat() == null ? "" : v.getKilovat(), v.getKubikaza() == null ? "" : v.getKubikaza()};
                    modell.addRow(rowData);
                    forma.getTableVozila().setModel(modell);
                }
            }
        } else {

            String[] columns = {"ID", "Registracija", "Marka", "Model", "Godište", "Prezime", "Ime", "Naziv", "Gorivo", "Kilovat", "Kubikaža"};
            DefaultTableModel modell = new DefaultTableModel(columns, 0);
            forma.getTableVozila().setModel(modell);

            ArrayList<VoziloDTO> vozila = DAOFactory.getDAOFactory().getVoziloDAO().svaVozila();
            for (VoziloDTO v : vozila) {

                KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(v.getIdKupac());
                ModelVozilaDTO mod = new ModelVozilaDTO();
                ArrayList<ModelVozilaDTO> modeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
                for (ModelVozilaDTO m : modeli) {
                    if (m.getIdModelVozila() == v.getIdModelVozila()) {
                        mod = m;
                    }
                }

                boolean dodati = true;
                if (registracija != null && !"".equals(registracija)) {
                    if (!v.getBrojRegistracije().toLowerCase().startsWith(registracija.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (godiste != 0) {
                    if (!Objects.equals(v.getGodiste(), godiste)) {
                        dodati = false;
                    }
                }
                if (marka != null && !"".equals(marka)) {
                    if (!mod.getMarka().toLowerCase().equals(marka.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (model != null && !"".equals(model)) {
                    if (!mod.getModel().toLowerCase().equals(model.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (dodati == true) {

                    if (forma.getRbPrivatnoLiceVozilo().isSelected()) {
                        String ime = forma.getTfImeVozilo().getText();
                        String prezime = forma.getTfPrezimeVozilo().getText();

                        if (ime != null && !"".equals(ime) && kupac.getIme() != null && !"".equals(kupac.getIme())) {
                            if (!kupac.getIme().toLowerCase().startsWith(ime.toLowerCase())) {
                                dodati = false;
                            }
                        }
                        if (prezime != null && !"".equals(prezime) && kupac.getPrezime() != null && !"".equals(kupac.getPrezime())) {
                            if (!kupac.getPrezime().toLowerCase().startsWith(prezime.toLowerCase())) {
                                dodati = false;
                            }
                        }
                    } else if (forma.getRbPravnoLiceVozilo().isSelected()) {
                        String naziv = forma.getTfNazivVozilo().getText();
                        if (naziv != null && !"".equals(naziv) && kupac.getNaziv() != null && !"".equals(kupac.getNaziv())) {
                            if (!kupac.getNaziv().toLowerCase().startsWith(naziv.toLowerCase())) {
                                dodati = false;
                            }
                        } else if (kupac.getNaziv() == null || "".equals(kupac.getNaziv())) {
                            dodati = false;
                        }
                    }

                    if (dodati == true) {
                        Object[] rowData = {v.getIdVozilo(), v.getBrojRegistracije() == null ? "" : v.getBrojRegistracije(), mod.getMarka(), mod.getModel(), v.getGodiste() == null ? "" : v.getGodiste(), (kupac.getPrezime() == null || "".equals(kupac.getPrezime())) ? "---" : kupac.getPrezime(), (kupac.getIme() == null || "".equals(kupac.getIme())) ? "---" : kupac.getIme(), (kupac.getNaziv() == null || kupac.getNaziv().equals("")) ? "---" : kupac.getNaziv(), v.getVrstaGoriva() == null ? "" : v.getVrstaGoriva(), v.getKilovat() == null ? "" : v.getKilovat(), v.getKubikaza() == null ? "" : v.getKubikaza()};
                        modell.addRow(rowData);
                        forma.getTableVozila().setModel(modell);
                    }
                }
            }

        }
    }
    
    public void prikaziSvaVozila(IzaberiVoziloDialog dijalog) {
        ArrayList<VoziloDTO> vozila = DAOFactory.getDAOFactory().getVoziloDAO().svaVozila();
        String[] columns = {"ID", "Registracija", "Marka", "Model", "Godište", "Prezime", "Ime", "Naziv", "Gorivo", "Kilovat", "Kubikaža"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        dijalog.getTableVozila().setModel(model);

        for (VoziloDTO v : vozila) {
            KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(v.getIdKupac());
            ModelVozilaDTO mod = new ModelVozilaDTO();
            ArrayList<ModelVozilaDTO> modeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
            for (ModelVozilaDTO m : modeli) {
                if (m.getIdModelVozila() == v.getIdModelVozila()) {
                    mod = m;
                }
            }

            Object[] rowData = {v.getIdVozilo(), v.getBrojRegistracije() == null ? "" : v.getBrojRegistracije(), mod.getMarka(), mod.getModel(), v.getGodiste() == null ? "" : v.getGodiste(), (kupac.getPrezime() == null || "".equals(kupac.getPrezime())) ? "---" : kupac.getPrezime(), (kupac.getIme() == null || "".equals(kupac.getIme())) ? "---" : kupac.getIme(), (kupac.getNaziv() == null || "".equals(kupac.getNaziv())) ? "---" : kupac.getNaziv(), v.getVrstaGoriva() == null ? "" : v.getVrstaGoriva(), v.getKilovat() == null ? "" : v.getKilovat(), v.getKubikaza() == null ? "" : v.getKubikaza()};
            model.addRow(rowData);
        }

        dijalog.getTableVozila().setModel(model);
    }
    
     public void pronadjiVozilo(IzaberiVoziloDialog forma) {
        String registracija = forma.getTfRegistracijaTrazi().getText();
        Integer godiste = 0;

        if (forma.getTfGodisteTrazi().getText() != null && !"".equals(forma.getTfGodisteTrazi().getText())) {
            try {
                godiste = Integer.parseInt(forma.getTfGodisteTrazi().getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(forma, "Pogrešan format godišta. Mora da bude cjelobrojni podatak. ", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        }

        String marka = forma.getTfMarkaTrazi().getText();
        String model = forma.getTfModelTrazi().getText();
        boolean svi = forma.getCbSvi().isSelected();

        if (svi) {

            String[] columns = {"ID", "Registracija", "Marka", "Model", "Godište", "Prezime", "Ime", "Naziv", "Gorivo", "Kilovat", "Kubikaža"};
            DefaultTableModel modell = new DefaultTableModel(columns, 0);
            forma.getTableVozila().setModel(modell);

            ArrayList<VoziloDTO> vozila = DAOFactory.getDAOFactory().getVoziloDAO().svaVozila();
            for (VoziloDTO v : vozila) {

                KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(v.getIdKupac());
                ModelVozilaDTO mod = new ModelVozilaDTO();
                ArrayList<ModelVozilaDTO> modeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
                for (ModelVozilaDTO m : modeli) {
                    if (m.getIdModelVozila() == v.getIdModelVozila()) {
                        mod = m;
                    }
                }

                boolean dodati = true;
                if (registracija != null && !"".equals(registracija)) {
                    if (!v.getBrojRegistracije().toLowerCase().startsWith(registracija.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (godiste != 0) {
                    if (!Objects.equals(v.getGodiste(), godiste)) {
                        dodati = false;
                    }
                }
                if (marka != null && !"".equals(marka)) {
                    if (!mod.getMarka().toLowerCase().equals(marka.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (model != null && !"".equals(model)) {
                    if (!mod.getModel().toLowerCase().equals(model.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (dodati == true) {
                    Object[] rowData = {v.getIdVozilo(), v.getBrojRegistracije() == null ? "" : v.getBrojRegistracije(), mod.getMarka(), mod.getModel(), v.getGodiste() == null ? "" : v.getGodiste(), (kupac.getPrezime() == null || "".equals(kupac.getPrezime())) ? "---" : kupac.getPrezime(), (kupac.getIme() == null || "".equals(kupac.getIme())) ? "---" : kupac.getIme(), (kupac.getNaziv() == null || "".equals(kupac.getNaziv())) ? "---" : kupac.getNaziv(), v.getVrstaGoriva() == null ? "" : v.getVrstaGoriva(), v.getKilovat() == null ? "" : v.getKilovat(), v.getKubikaza() == null ? "" : v.getKubikaza()};
                    modell.addRow(rowData);
                    forma.getTableVozila().setModel(modell);
                }
            }
        } else {

            String[] columns = {"ID", "Registracija", "Marka", "Model", "Godište", "Prezime", "Ime", "Naziv", "Gorivo", "Kilovat", "Kubikaža"};
            DefaultTableModel modell = new DefaultTableModel(columns, 0);
            forma.getTableVozila().setModel(modell);

            ArrayList<VoziloDTO> vozila = DAOFactory.getDAOFactory().getVoziloDAO().svaVozila();
            for (VoziloDTO v : vozila) {

                KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(v.getIdKupac());
                ModelVozilaDTO mod = new ModelVozilaDTO();
                ArrayList<ModelVozilaDTO> modeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
                for (ModelVozilaDTO m : modeli) {
                    if (m.getIdModelVozila() == v.getIdModelVozila()) {
                        mod = m;
                    }
                }

                boolean dodati = true;
                if (registracija != null && !"".equals(registracija)) {
                    if (!v.getBrojRegistracije().toLowerCase().startsWith(registracija.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (godiste != 0) {
                    if (!Objects.equals(v.getGodiste(), godiste)) {
                        dodati = false;
                    }
                }
                if (marka != null && !"".equals(marka)) {
                    if (!mod.getMarka().toLowerCase().equals(marka.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (model != null && !"".equals(model)) {
                    if (!mod.getModel().toLowerCase().equals(model.toLowerCase())) {
                        dodati = false;
                    }
                }
                if (dodati == true) {

                    if (forma.getRbPrivatnoLiceVozilo().isSelected()) {
                        String ime = forma.getTfImeVozilo().getText();
                        String prezime = forma.getTfPrezimeVozilo().getText();

                        if (ime != null && !"".equals(ime) && kupac.getIme() != null && !"".equals(kupac.getIme())) {
                            if (!kupac.getIme().toLowerCase().startsWith(ime.toLowerCase())) {
                                dodati = false;
                            }
                        }
                        if (prezime != null && !"".equals(prezime) && kupac.getPrezime() != null && !"".equals(kupac.getPrezime())) {
                            if (!kupac.getPrezime().toLowerCase().startsWith(prezime.toLowerCase())) {
                                dodati = false;
                            }
                        }
                    } else if (forma.getRbPravnoLiceVozilo().isSelected()) {
                        String naziv = forma.getTfNazivVozilo().getText();
                        if (naziv != null && !"".equals(naziv) && kupac.getNaziv() != null && !"".equals(kupac.getNaziv())) {
                            if (!kupac.getNaziv().toLowerCase().startsWith(naziv.toLowerCase())) {
                                dodati = false;
                            }
                        } else if (kupac.getNaziv() == null || "".equals(kupac.getNaziv())) {
                            dodati = false;
                        }
                    }

                    if (dodati == true) {
                        Object[] rowData = {v.getIdVozilo(), v.getBrojRegistracije() == null ? "" : v.getBrojRegistracije(), mod.getMarka(), mod.getModel(), v.getGodiste() == null ? "" : v.getGodiste(), (kupac.getPrezime() == null || "".equals(kupac.getPrezime())) ? "---" : kupac.getPrezime(), (kupac.getIme() == null || "".equals(kupac.getIme())) ? "---" : kupac.getIme(), (kupac.getNaziv() == null || kupac.getNaziv().equals("")) ? "---" : kupac.getNaziv(), v.getVrstaGoriva() == null ? "" : v.getVrstaGoriva(), v.getKilovat() == null ? "" : v.getKilovat(), v.getKubikaza() == null ? "" : v.getKubikaza()};
                        modell.addRow(rowData);
                        forma.getTableVozila().setModel(modell);
                    }
                }
            }

        }
     }
        
        public void prikaziRadneNaloge(HomeForm1 forma){
            RadniNalogParametri parametri = new RadniNalogParametri();
        
        if(!forma.getCbDatumOtvaranja().isSelected()){
            if(forma.getDcDatumOtvaranjaOD().getDate() != null)
                parametri.setDatumOtvaranjaOD(new java.sql.Date(forma.getDcDatumOtvaranjaOD().getDate().getTime()));
            if(forma.getDcDatumOtvaranjaDO().getDate() != null)
                parametri.setDatumOtvaranjaDO(new java.sql.Date(forma.getDcDatumOtvaranjaDO().getDate().getTime()));
        }
        
        if(!forma.getCbDatumZatvaranja().isSelected()){
            if(forma.getDcDatumZatvaranjaOD().getDate() != null)
                parametri.setDatumZatvaranjaOD(new java.sql.Date(forma.getDcDatumZatvaranjaOD().getDate().getTime()));
            if(forma.getDcDatumZatvaranjaDO().getDate() != null)
                parametri.setDatumZatvaranjaDO(new java.sql.Date(forma.getDcDatumZatvaranjaDO().getDate().getTime()));
        }
        
        if(!forma.getCbPotrebnoZavrsiti().isSelected()){
            if(forma.getDcPotrebnoZavrsitiOD().getDate() != null)
                parametri.setDatumPotrebnoZavrsitiOD(new java.sql.Date(forma.getDcPotrebnoZavrsitiOD().getDate().getTime()));
            if(forma.getDcPotrebnoZavrsitiDO().getDate() != null){
                parametri.setDatumPotrebnoZavrsitiDO(new java.sql.Date(forma.getDcPotrebnoZavrsitiDO().getDate().getTime()));
            }
        }
        
        String registracija = forma.getTfRegistracijaRadniNalog().getText();
        if(registracija != null && !"".equals(registracija))
        parametri.setRegistracija(registracija);
        String prezime = forma.getTfPrezimeRadniNalog().getText();
        if(prezime != null && !"".equals(prezime))
        parametri.setPrezime(prezime);
        String ime = forma.getTfImeRadniNalog().getText();
        if(ime != null && !"".equals(ime))
        parametri.setIme(ime);
        String naziv = forma.getTfNazivRadniNalog().getText();
        if(naziv != null && !"".equals(naziv))
        parametri.setNaziv(naziv);
        
        boolean privatni = forma.getRbPrivatnoRadniNalog().isSelected();
        boolean pravno = forma.getRbPravnoRadniNalog().isSelected();
        boolean svi = forma.getCbSviRadniNalog().isSelected();
        parametri.setPrivatni(privatni);
        parametri.setSvi(svi);
        parametri.setPravni(pravno);
        
        ArrayList<RezultatRNPretrazivanje> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().pretragaRadnihNaloga(parametri);
        
        //String.valueOf(new SimpleDateFormat("dd.MM.yyyy.").format(lista.get(i).getDatumOtvaranjaNaloga()));
        
        String[] columns = {"ID", "Registracija vozila", "Vlasnik vozila", "Datum otvaranja", "Rok za završetak", "Datum zatvaranja", "Troškovi dijela", "Cijena usluge", "Plaćeno"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        forma.getTableRNalozi().setModel(model);
        for (RezultatRNPretrazivanje k : lista) {
            Object[] rowData = {k.getId(), k.getRegistracija(), k.getVlasnik(), k.getDatumOtvaranja()!=null? (new SimpleDateFormat("dd.MM.yyyy.").format(k.getDatumOtvaranja())):"", k.getDatumPotrebnoZavrsitiDo()!=null?(new SimpleDateFormat("dd.MM.yyyy.").format(k.getDatumPotrebnoZavrsitiDo())) : "", k.getDatumZatvaranja() !=null ? (new SimpleDateFormat("dd.MM.yyyy.").format(k.getDatumZatvaranja())) :"", k.getTroskoviDijelova(), k.getCijenaUsluge(), k.isPlaceno()? "Da" : "Ne"};
            model.addRow(rowData);
        }
        forma.getTableRNalozi().setModel(model);
        }
        
    }
