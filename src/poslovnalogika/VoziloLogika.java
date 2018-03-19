/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.dialog.DodajVoziloDialog;
import autoservismaric.dialog.IzmijeniVoziloDialog;
import data.dao.DAOFactory;
import data.dto.KupacDTO;
import data.dto.ModelVozilaDTO;
import data.dto.VoziloDTO;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DulleX
 */
public class VoziloLogika {

//    public AutoSuggestor ucitajPreporukeMarke(DodajVoziloDialog dijalog) {
//
//        AutoSuggestor autoSuggestorMarke = new AutoSuggestor(dijalog.getTfMarka(), dijalog, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
//            @Override
//            public boolean wordTyped(String typedWord) {
//                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
//                ArrayList<String> modeli = new ArrayList<>();
//                ArrayList<ModelVozilaDTO> lista = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
//
//                for (ModelVozilaDTO mv : lista) {
//                    if (!modeli.contains(mv.getMarka())) {
//                        modeli.add(mv.getMarka());
//                    }
//                }
//
//                setDictionary(modeli);
//                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
//            }
//        };
//        return autoSuggestorMarke;
//    }

//    public AutoSuggestor ucitajPreporukeModel(DodajVoziloDialog dijalog) {
//
//        AutoSuggestor autoSuggestorModel = new AutoSuggestor(dijalog.getTfModel(), dijalog, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
//            @Override
//            public boolean wordTyped(String typedWord) {
//
//                String marka = dijalog.getTfMarka().getText();
//
//                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
//                ArrayList<String> modeli = new ArrayList<>();
//
//                ArrayList<ModelVozilaDTO> lista = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
//
//                for (ModelVozilaDTO mv : lista) {
//                    if (dijalog.getTfModel().getText() != null && !"".equals(dijalog.getTfModel().getText())) {
//                        if (mv.getMarka().equals(marka.trim())) {
//                            modeli.add(mv.getModel());
//                        }
//                    } else if (dijalog.getTfModel().getText() == null || "".equals(dijalog.getTfModel().getText())) {
//                        modeli.add(mv.getModel());
//                    }
//                }
//
//                setDictionary(modeli);
//                //addToDictionary("bye");//adds a single word
//
//                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
//            }
//        };
//
//        return autoSuggestorModel;
//    }

    public void inicijalizacijaDodajDijaloga(DodajVoziloDialog dijalog) {
        dijalog.getBg().add(dijalog.getRbPravni());
        dijalog.getBg().add(dijalog.getRbPrivatni());
        dijalog.getTabela().setAutoCreateRowSorter(true);
        dijalog.getTfVlasnikNaziv().setBackground(Color.gray);
        dijalog.getTfVlasnikNaziv().setEditable(false);
        dijalog.getTabela().setDefaultEditor(Object.class, null);
        dijalog.getTabela().getTableHeader().setReorderingAllowed(false);
    }

    public void prikaziSveVlasnike(DodajVoziloDialog dijalog) {
        ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviKupci();
        String[] columns = {"ID", "Ime", "Prezime", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        dijalog.getTabela().setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), (k.getIme() == null) ? "---" : k.getIme(), (k.getPrezime() == null) ? "---" : k.getPrezime(), (k.getNaziv() == null) ? "---" : k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }
        dijalog.getTabela().setModel(model);
    }

    public void traziVlasnika(DodajVoziloDialog dijalog) {
        ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviKupci();
        if (dijalog.getRbPrivatni().isSelected()) {
            String ime = dijalog.getTfVlasnikIme().getText();
            String prezime = dijalog.getTfVlasnikPrezime().getText();

            String[] columns = {"ID", "Ime", "Prezime", "Telefon", "Adresa", "Grad"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            dijalog.getTabela().setModel(model);

            if ((ime == null || "".equals(ime)) && (prezime == null || "".equals(prezime))) {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().sviPrivatni();
            } else if ((ime == null || "".equals(ime))) {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPrezime(prezime);
            } else if ((prezime == null || "".equals(prezime))) {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciIme(ime);
            } else {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPrivatni(ime, prezime);
            }

            for (KupacDTO k : kupci) {
                Object[] rowData = {k.getIdKupac(), (k.getIme() == null) ? "---" : k.getIme(), (k.getPrezime() == null) ? "---" : k.getPrezime(), k.getTelefon(), k.getAdresa(), k.getGrad()};
                model.addRow(rowData);
                dijalog.getTabela().setModel(model);
            }

        } else if (dijalog.getRbPravni().isSelected()) {
            String naziv = dijalog.getTfVlasnikNaziv().getText();

            String[] columns = {"ID", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            dijalog.getTabela().setModel(model);

            if (naziv == null || "".equals(naziv)) {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().sviPravni();
            } else {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPravni(naziv);
            }

            for (KupacDTO k : kupci) {
                Object[] rowData = {k.getIdKupac(), k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
                model.addRow(rowData);
                dijalog.getTabela().setModel(model);
            }

        }
    }

    public void dodajVozilo(DodajVoziloDialog dijalog) {
        String registracija = dijalog.getTfRegistracija().getText();

        if (registracija == null || "".equals(registracija)) {
            JOptionPane.showMessageDialog(dijalog, "Morate unijeti broj registracije vozila!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        String marka = dijalog.getCbMarkaVozila().getSelectedItem().toString();
        String model = dijalog.getCbModelVozila().getSelectedItem().toString();

        if (model == null || "".equals(model)) {
            JOptionPane.showMessageDialog(dijalog, "Morate unnijei model vozila!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        if (marka == null || "".equals(marka)) {
            JOptionPane.showMessageDialog(dijalog, "Morate unijeti marku vozila!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        String gorivo = dijalog.getCbGorivo().getSelectedItem().toString();
        Integer godiste = null, kilovati = null;
        Double kubikaza = null;
        if (dijalog.getTfGodiste().getText() != null && !"".equals(dijalog.getTfGodiste().getText())) {
            try {
                godiste = Integer.parseInt(dijalog.getTfGodiste().getText());
                if (godiste < 1950) {
                    throw new Exception();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dijalog, "Nevalidan format godišta. Mora biti cjelobrojni podatak.", "Greška", JOptionPane.OK_OPTION);
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dijalog, "Provjerite unesenu vrijednost godišta!", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        if (dijalog.getTfKilovat().getText() != null && !"".equals(dijalog.getTfKilovat().getText())) {
            try {
                kilovati = Integer.parseInt(dijalog.getTfKilovat().getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dijalog, "Nevalidan format kilovata. Mora biti cjelobrojni podatak.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        if (dijalog.getTfKubikaza().getText() != null && !"".equals(dijalog.getTfKubikaza().getText())) {
            try {
                kubikaza = Double.parseDouble(dijalog.getTfKubikaza().getText());
            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(dijalog, "Nevalidan format kubikaže. Mora biti realan broj.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        int row = dijalog.getTabela().getSelectedRow();

        if (row >= 0) {

            Integer idVlasnik = Integer.parseInt(dijalog.getTabela().getModel().getValueAt(row, 0).toString());

            ModelVozilaDTO mv = DAOFactory.getDAOFactory().getModelVozilaDAO().model(marka, model);
            if (mv != null) {
                VoziloDTO vozilo = new VoziloDTO();
                vozilo.setBrojRegistracije(registracija);
                vozilo.setGodiste(godiste);
                vozilo.setKilovat(kilovati);
                vozilo.setKubikaza(kubikaza);
                vozilo.setVrstaGoriva(gorivo);
                vozilo.setIdModelVozila(mv.getIdModelVozila());
                vozilo.setIdKupac(idVlasnik);

                if (DAOFactory.getDAOFactory().getVoziloDAO().dodajVozilo(vozilo)) {
                    JOptionPane.showMessageDialog(dijalog, "Uspješno dodato vozilo!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                    dijalog.getTfRegistracija().setText("");
                    dijalog.getCbMarkaVozila().setSelectedIndex(0);
                    dijalog.getCbModelVozila().setSelectedIndex(0);
                    dijalog.getTfGodiste().setText("");
                    dijalog.getCbGorivo().setSelectedIndex(0);
                    dijalog.getTfKilovat().setText("");
                    dijalog.getTfKubikaza().setText("");
                    dijalog.getTfVlasnikIme().setText("");
                    dijalog.getTfVlasnikPrezime().setText("");
                    dijalog.getTfVlasnikNaziv().setText("");
                    //dijalog.getBtnPrikaziSve().doClick();
                    
                    if (dijalog.getForma() != null) {
                        if (dijalog.getForma().akcijaZaRefreshVozila == 0) {
                            dijalog.getForma().getBtnPrikaziSvaVozila().doClick();
                        }
                        if (dijalog.getForma().akcijaZaRefreshVozila == 1) {
                            dijalog.getForma().getBtnPronadjiVozilo().doClick();
                        }
                    }
                    
                    
                    dijalog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dijalog, "Marka i model vozila ne postoje! \nPrvo ih dodajte, pa pokušajte ponovo!", "Greška", JOptionPane.OK_OPTION);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(dijalog, "Greška", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(dijalog, "Morate odabrati vlasnika u tabeli!", "Greška", JOptionPane.OK_OPTION);
        }
    }

    public void inicijalizujIzmijeniDijaloga(IzmijeniVoziloDialog dijalog) {
        dijalog.getTabela().getTableHeader().setReorderingAllowed(false);

        VoziloDTO vozilo = DAOFactory.getDAOFactory().getVoziloDAO().vozilo(dijalog.getIdVozila());
        ModelVozilaDTO model = DAOFactory.getDAOFactory().getModelVozilaDAO().model(vozilo.getIdModelVozila());
        KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(vozilo.getIdKupac());

        dijalog.getTfRegistracija().setText(vozilo.getBrojRegistracije());
        dijalog.getCbMarkaVozila().setSelectedItem(model.getMarka());
        dijalog.getCbMarkaVozila().setSelectedItem(model.getModel());
        dijalog.getTfGodiste().setText(vozilo.getGodiste().toString());
        dijalog.getTfKilovat().setText(vozilo.getKilovat().toString());
        dijalog.getTfKubikaza().setText(vozilo.getKubikaza().toString());
        if (kupac.getNaziv() == null) {
            dijalog.getRbPrivatni().setSelected(true);
            dijalog.getTfVlasnikIme().setEditable(true);
            dijalog.getTfVlasnikPrezime().setEditable(true);
            dijalog.getTfVlasnikNaziv().setEditable(false);
            dijalog.getTfVlasnikNaziv().setText("");
            dijalog.getTfVlasnikNaziv().setBackground(Color.gray);
            dijalog.getTfVlasnikIme().setText(kupac.getIme());
            dijalog.getTfVlasnikPrezime().setText(kupac.getPrezime());
            dijalog.getTfVlasnikIme().setBackground(Color.white);
            dijalog.getTfVlasnikPrezime().setBackground(Color.white);
        } else {
            dijalog.getRbPravni().setSelected(true);
            dijalog.getTfVlasnikIme().setEditable(false);
            dijalog.getTfVlasnikPrezime().setEditable(false);
            dijalog.getTfVlasnikNaziv().setEditable(true);
            dijalog.getTfVlasnikNaziv().setText(kupac.getNaziv());
            dijalog.getTfVlasnikNaziv().setBackground(Color.white);
            dijalog.getTfVlasnikIme().setText("");
            dijalog.getTfVlasnikPrezime().setText("");
            dijalog.getTfVlasnikIme().setBackground(Color.gray);
            dijalog.getTfVlasnikPrezime().setBackground(Color.gray);

        }

        if (kupac.getNaziv() == null || "".equals(kupac.getNaziv())) {
            String[] columns = {"ID", "Ime", "Prezime", "Telefon", "Adresa", "Grad"};
            DefaultTableModel model2 = new DefaultTableModel(columns, 0);
            dijalog.getTabela().setModel(model2);

            Object[] rowData = {kupac.getIdKupac(), (kupac.getIme() == null) ? "---" : kupac.getIme(), (kupac.getPrezime() == null) ? "---" : kupac.getPrezime(), kupac.getTelefon(), kupac.getAdresa(), kupac.getGrad()};
            model2.addRow(rowData);

            dijalog.getTabela().setModel(model2);
            dijalog.getTabela().setRowSelectionInterval(0, 0);
        } else {
            String[] columns = {"ID", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
            DefaultTableModel model2 = new DefaultTableModel(columns, 0);
            dijalog.getTabela().setModel(model2);

            Object[] rowData = {kupac.getIdKupac(), (kupac.getNaziv() == null) ? "---" : kupac.getNaziv(), kupac.getTelefon(), kupac.getAdresa(), kupac.getGrad()};
            model2.addRow(rowData);

            dijalog.getTabela().setModel(model2);
            dijalog.getTabela().setRowSelectionInterval(0, 0);
        }
    }

//    public AutoSuggestor ucitajPreporukeMarke(IzmijeniVoziloDialog dijalog) {
//
//        AutoSuggestor autoSuggestorMarke = new AutoSuggestor(dijalog.getTfMarka(), dijalog, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
//            @Override
//            public boolean wordTyped(String typedWord) {
//                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
//                ArrayList<String> modeli = new ArrayList<>();
//                ArrayList<ModelVozilaDTO> lista = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
//
//                for (ModelVozilaDTO mv : lista) {
//                    if (!modeli.contains(mv.getMarka())) {
//                        modeli.add(mv.getMarka());
//                    }
//                }
//
//                setDictionary(modeli);
//                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
//            }
//        };
//        return autoSuggestorMarke;
//    }
//
//    public AutoSuggestor ucitajPreporukeModel(IzmijeniVoziloDialog dijalog) {
//
//        AutoSuggestor autoSuggestorModel = new AutoSuggestor(dijalog.getTfModel(), dijalog, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
//            @Override
//            public boolean wordTyped(String typedWord) {
//
//                String marka = dijalog.getTfMarka().getText();
//
//                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
//                ArrayList<String> modeli = new ArrayList<>();
//
//                ArrayList<ModelVozilaDTO> lista = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
//
//                for (ModelVozilaDTO mv : lista) {
//                    if (dijalog.getTfModel().getText() != null && !"".equals(dijalog.getTfModel().getText())) {
//                        if (mv.getMarka().equals(marka.trim())) {
//                            modeli.add(mv.getModel());
//                        }
//                    } else if (dijalog.getTfModel().getText() == null || "".equals(dijalog.getTfModel().getText())) {
//                        modeli.add(mv.getModel());
//                    }
//                }
//
//                setDictionary(modeli);
//
//                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
//            }
//        };
//
//        return autoSuggestorModel;
//    }

    public void izmijeniVozilo(IzmijeniVoziloDialog dijalog) {
        String registracija = dijalog.getTfRegistracija().getText();
        String marka = dijalog.getCbMarkaVozila().getSelectedItem().toString();
        String model = dijalog.getCbModelVozila().getSelectedItem().toString();
        String gorivo = dijalog.getCbGorivo().getSelectedItem().toString();
        Integer godiste = null, kilovati = null;
        Double kubikaza = null;

        if (registracija == null || "".equals(registracija)) {
            JOptionPane.showMessageDialog(dijalog, "Morate unijeti broj registracije vozila!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        if (model == null || "".equals(model)) {
            JOptionPane.showMessageDialog(dijalog, "Morate unnijei model vozila!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        if (marka == null || "".equals(marka)) {
            JOptionPane.showMessageDialog(dijalog, "Morate unijeti marku vozila!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        if (dijalog.getTfGodiste().getText() != null && !"".equals(dijalog.getTfGodiste().getText())) {
            try {
                godiste = Integer.parseInt(dijalog.getTfGodiste().getText());
                if (godiste < 1950) {
                    throw new Exception();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dijalog, "Nevalidan format godišta. Mora biti cjelobrojni podatak.", "Greška", JOptionPane.OK_OPTION);
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dijalog, "Provjerite unesenu vrijednost godišta!", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        if (dijalog.getTfKilovat().getText() != null && !"".equals(dijalog.getTfKilovat().getText())) {
            try {
                kilovati = Integer.parseInt(dijalog.getTfKilovat().getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dijalog, "Nevalidan format kilovata. Mora biti cjelobrojni podatak.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        if (dijalog.getTfKubikaza().getText() != null && !"".equals(dijalog.getTfKubikaza().getText())) {
            try {
                kubikaza = Double.parseDouble(dijalog.getTfKubikaza().getText());
            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(dijalog, "Nevalidan format kubikaže. Mora biti realan broj.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        int column = 0;
        int row = dijalog.getTabela().getSelectedRow();

        if (row >= 0) {

            Integer idVlasnik = Integer.parseInt(dijalog.getTabela().getModel().getValueAt(row, 0).toString());

            ModelVozilaDTO mv = DAOFactory.getDAOFactory().getModelVozilaDAO().model(marka, model);
            if (mv != null) {
                VoziloDTO vozilo = new VoziloDTO();
                vozilo.setIdVozilo(dijalog.getIdVozila());
                vozilo.setBrojRegistracije(registracija);
                vozilo.setGodiste(godiste);
                vozilo.setKilovat(kilovati);
                vozilo.setKubikaza(kubikaza);
                vozilo.setVrstaGoriva(gorivo);
                vozilo.setIdModelVozila(mv.getIdModelVozila());
                vozilo.setIdKupac(idVlasnik);

                if (DAOFactory.getDAOFactory().getVoziloDAO().azurirajVozilo(vozilo)) {
                    JOptionPane.showMessageDialog(dijalog, "Uspješno izmijenjeno vozilo!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                   
                    if (dijalog.getForma() != null) {
                        if (dijalog.getForma().akcijaZaRefreshVozila == 0) {
                            dijalog.getForma().getBtnPrikaziSvaVozila().doClick();
                        }
                        if (dijalog.getForma().akcijaZaRefreshVozila == 1) {
                            dijalog.getForma().getBtnPronadjiVozilo().doClick();
                        }
                    }
                    
                    dijalog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dijalog, "Marka i model vozila ne postoje! \nPrvo ih dodajte, pa pokušajte ponovo!", "Greška", JOptionPane.OK_OPTION);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(dijalog, "Marka i model vozila ne postoje! \nPrvo ih dodajte, pa pokušajte ponovo!", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(dijalog, "Morate odabrati vlasnika u tabeli!", "Greška", JOptionPane.OK_OPTION);
        }
    }

    public void prikaziSveVlasnike(IzmijeniVoziloDialog dijalog) {
        ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviKupci();

        dijalog.getBg().clearSelection();

        String[] columns = {"ID", "Ime", "Prezime", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        dijalog.getTabela().setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), (k.getIme() == null) ? "---" : k.getIme(), (k.getPrezime() == null) ? "---" : k.getPrezime(), (k.getNaziv() == null) ? "---" : k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }

        dijalog.getTabela().setModel(model);
    }

    public void traziVlasnika(IzmijeniVoziloDialog dijalog) {
        ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviKupci();
        if (dijalog.getRbPrivatni().isSelected()) {
            String ime = dijalog.getTfVlasnikIme().getText();
            String prezime = dijalog.getTfVlasnikPrezime().getText();

            String[] columns = {"ID", "Ime", "Prezime", "Telefon", "Adresa", "Grad"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            dijalog.getTabela().setModel(model);

            if ((ime == null || "".equals(ime)) && (prezime == null || "".equals(prezime))) {

            } else if ((ime == null || "".equals(ime))) {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPrezime(prezime);
            } else if ((prezime == null || "".equals(prezime))) {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciIme(ime);
            } else {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPrivatni(ime, prezime);
            }

            for (KupacDTO k : kupci) {
                Object[] rowData = {k.getIdKupac(), (k.getIme() == null) ? "---" : k.getIme(), (k.getPrezime() == null) ? "---" : k.getPrezime(), k.getTelefon(), k.getAdresa(), k.getGrad()};
                model.addRow(rowData);
                dijalog.getTabela().setModel(model);
            }

        } else if (dijalog.getRbPravni().isSelected()) {
            String naziv = dijalog.getTfVlasnikNaziv().getText();

            String[] columns = {"ID", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            dijalog.getTabela().setModel(model);

            if (naziv == null || "".equals(naziv)) {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().sviPravni();
            } else {
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPravni(naziv);
            }

            for (KupacDTO k : kupci) {
                Object[] rowData = {k.getIdKupac(), k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
                model.addRow(rowData);
                dijalog.getTabela().setModel(model);
            }

        }
    }
}
