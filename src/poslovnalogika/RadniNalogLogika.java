/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.dialog.DodajRadniNalogDialog;
import autoservismaric.dialog.IzmijeniRadniNalogDialog;
import autoservismaric.dialog.PregledIstorijePopravkiDialog;
import data.dao.DAOFactory;
import data.dto.DioDTO;
import data.dto.KupacDTO;
import data.dto.ModelVozilaDTO;
import data.dto.RadniNalogDTO;
import data.dto.RadniNalogDioDTO;
import data.dto.RadniNalogRadnikDTO;
import data.dto.VoziloDTO;
import data.dto.ZaposleniDTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.scene.paint.Color;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DulleX
 */
public class RadniNalogLogika {

    public void inicijalizacijaDodajRadniNalog(DodajRadniNalogDialog dijalog) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 7);

        dijalog.getDatumOtvaranjaNaloga().setDate(new Date());
        dijalog.getPotrebnoZavrsitiDo().setDate(c.getTime());
        dijalog.getTabelaIzabraniDijelovi().setAutoCreateRowSorter(true);
        dijalog.getTabelaDijelovi().setAutoCreateRowSorter(true);
        dijalog.getTabelaDijelovi().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dijalog.getTabelaIzabraniDijelovi().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dijalog.getTabelaDijelovi().setDefaultEditor(Object.class, null);
        dijalog.getTabelaIzabraniDijelovi().setDefaultEditor(Object.class, null);
        dijalog.getTabelaDijelovi().getTableHeader().setReorderingAllowed(false);
        dijalog.getTabelaIzabraniDijelovi().getTableHeader().setReorderingAllowed(false);
        VoziloDTO vozilo = DAOFactory.getDAOFactory().getVoziloDAO().vozilo(dijalog.getIdVozila());
        
        ModelVozilaDTO modelVozila = DAOFactory.getDAOFactory().getModelVozilaDAO().model(vozilo.getIdModelVozila());

        dijalog.getTfIdVozila().setText(new Integer(dijalog.getIdVozila()).toString());
        dijalog.getTfIdVozila().setEditable(false);

        DefaultListModel<ZaposleniDTO> model = new DefaultListModel<>();

        List<ZaposleniDTO> listaZaposlenih = DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni();

        for (ZaposleniDTO z : listaZaposlenih) {
            model.addElement(z);
        }

        dijalog.getListaZaposleni().setModel(model);
        dijalog.getListaZaposleni().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] columns = {"Naziv", "Šifra", "Godište vozila", "Novo", "Vrsta goriva", "Dostupna količina", "Cijena"};
        DefaultTableModel modelDijelovi = new DefaultTableModel(columns, 0);
        dijalog.getTabelaDijelovi().setModel(modelDijelovi);

        List<DioDTO> listaDijelova = null;
        //listaDijelova = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(modelVozila.getMarka(), modelVozila.getModel());
        //List<DioDTO> listaZaSvaVozila = DAOFactory.getDAOFactory().getDioDAO().getDijeloviZaSvaVozila();

        listaDijelova = DAOFactory.getDAOFactory().getDioDAO().getDijeloviZaPonudu(modelVozila.getMarka(), modelVozila.getModel());
        for (DioDTO d : listaDijelova) {
            Object[] rowData = {d.getNaziv(), d.getSifra(), d.getGodisteVozila(), d.getNovo() == true ? "Da" : "Ne", d.getVrstaGoriva(), d.getKolicina(), d.getTrenutnaCijena()};
            modelDijelovi.addRow(rowData);
        }

//        for (DioDTO d : listaZaSvaVozila) {
//            Object[] rowData = {d.getNaziv(), d.getSifra(), d.getGodisteVozila(), d.getNovo() == true ? "Da" : "Ne", d.getVrstaGoriva(), d.getKolicina(), d.getTrenutnaCijena()};
//            modelDijelovi.addRow(rowData);
//        }

        dijalog.getTabelaDijelovi().setModel(modelDijelovi);

        dijalog.getListaZaposleniZaduzeni().setModel(new DefaultListModel<>());

        dijalog.getSpinnerKolicina().setValue(1);

        dijalog.getTabelaDijelovi().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (dijalog.getTabelaDijelovi().getSelectedRow() > -1) {
                    Double cijena = Double.parseDouble(dijalog.getTabelaDijelovi().getValueAt(dijalog.getTabelaDijelovi().getSelectedRow(), 6).toString());
                    dijalog.getTfCijenaDijela().setText(cijena.toString());
                }
            }
        });

    }

    public void dodajDio(DodajRadniNalogDialog dijalog) {
        int red = dijalog.getTabelaDijelovi().getSelectedRow();

        if (red >= 0) {

            String sifra = dijalog.getTabelaDijelovi().getModel().getValueAt(red, 1).toString();
            int dodajKolicinu = (Integer) (dijalog.getSpinnerKolicina().getValue());

            String naziv = dijalog.getTabelaDijelovi().getModel().getValueAt(red, 0).toString();
            Integer kolicina = Integer.parseInt(dijalog.getTabelaDijelovi().getModel().getValueAt(red, 5).toString());
            // Double cijena = Double.parseDouble(tabelaDijelovi.getModel().getValueAt(red, 6).toString());  
            Double cijena = Double.parseDouble(dijalog.getTfCijenaDijela().getText());
            boolean postoji = false;

            if (dodajKolicinu > kolicina) {
                JOptionPane.showMessageDialog(dijalog, "Nema toliko jedinica izabranog dijela na stanju!", "Greška", JOptionPane.OK_OPTION);
                return;
            }

            for (int i = 0; i < dijalog.getTabelaIzabraniDijelovi().getRowCount(); i++) {
                if (sifra.equals(dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(i, 1))) {
                    dijalog.getTabelaIzabraniDijelovi().getModel().setValueAt((Integer) dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(i, 2) + dodajKolicinu, i, 2);
                    postoji = true;
                }
            }

            if (!postoji) {

                DefaultTableModel dtm = (DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel();

                Object[] rowData = {naziv, sifra, dodajKolicinu, cijena};
                dtm.addRow(rowData);
            }

            if (dijalog.getTfTroskovi().getText() != null && !"".equals(dijalog.getTfTroskovi().getText())) {
                Double trenutnaCijena = Double.parseDouble(dijalog.getTfTroskovi().getText());
                trenutnaCijena += dodajKolicinu * cijena;
                trenutnaCijena = Math.round(trenutnaCijena * 100.0) / 100.0;
                dijalog.getTfTroskovi().setText(trenutnaCijena.toString());
            } else {
                Double trenutnaCijena = 0.0;
                trenutnaCijena += dodajKolicinu * cijena;
                trenutnaCijena = Math.round(trenutnaCijena * 100.0) / 100.0;
                dijalog.getTfTroskovi().setText(trenutnaCijena.toString());
            }

            dijalog.getTabelaDijelovi().getModel().setValueAt(Integer.parseInt(dijalog.getTabelaDijelovi().getModel().getValueAt(red, 5).toString()) - dodajKolicinu, red, 5);
            dijalog.getSpinnerKolicina().setValue(1);
        } else {
            JOptionPane.showMessageDialog(dijalog, "Izaberite dio koji želite dodati!", "Greška", JOptionPane.OK_OPTION);
            return;
        }
    }

    public void ukloniDio(DodajRadniNalogDialog dijalog) {
        int red = dijalog.getTabelaIzabraniDijelovi().getSelectedRow();

        if (red < 0) {
            JOptionPane.showMessageDialog(dijalog, "Morate odabrati dio u tabeli koji želite ukloniti!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        Integer kolicina = Integer.parseInt(dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(red, 2).toString());
        String sifra = dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(red, 1).toString();
        boolean pronasao = false;
        Double cijenaJednogDijela = 0.0;

        for (int i = 0; i < dijalog.getTabelaDijelovi().getRowCount() && pronasao == false; i++) {
            if (sifra.equals(dijalog.getTabelaDijelovi().getModel().getValueAt(i, 1))) {
                pronasao = true;
                dijalog.getTabelaDijelovi().getModel().setValueAt(Integer.parseInt(dijalog.getTabelaDijelovi().getModel().getValueAt(i, 5).toString()) + kolicina, i, 5);
                cijenaJednogDijela = Double.parseDouble(dijalog.getTabelaDijelovi().getModel().getValueAt(i, 6).toString());
            }
        }

        Double cijena = Double.parseDouble(dijalog.getTfTroskovi().getText());
        Double novaCijena = cijena - kolicina * cijenaJednogDijela;
        dijalog.getTfTroskovi().setText(novaCijena.toString());

        if (novaCijena < 0) {
            dijalog.getTfTroskovi().setText(new String("0"));
        }

        ((DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel()).removeRow(red);
    }

    public void kreirajNalog(DodajRadniNalogDialog dijalog) {
        Date datumOtvaranja = dijalog.getDatumOtvaranjaNaloga().getDate();
        java.sql.Date o = new java.sql.Date(datumOtvaranja.getTime());

        java.sql.Date z = null;
        Date datumZatvaranja = dijalog.getDatumZatvaranjaNaloga().getDate();
        try {
            z = new java.sql.Date(datumZatvaranja.getTime());
        } catch (Exception ex) {
        }

        Date potrebnoZavristi = dijalog.getPotrebnoZavrsitiDo().getDate();
        java.sql.Date pz = new java.sql.Date(potrebnoZavristi.getTime());

        Double troskovi = 0.0;
        Integer kilometraza = 0;
        Double cijena = 0.0;
        Boolean placeno = dijalog.getCbPlaceno().isSelected();

        if (dijalog.getTfTroskovi().getText() != null && !"".equals(dijalog.getTfTroskovi().getText())) {
            try {
                troskovi = new Double(dijalog.getTfTroskovi().getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dijalog, "Troškovi moraju biti realan broj.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        if (dijalog.getTfKilometraza().getText() != null && !"".equals(dijalog.getTfKilometraza().getText())) {
            try {
                kilometraza = new Integer(dijalog.getTfKilometraza().getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dijalog, "Kilometraža mora biti cjelobrojni podatak.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        if (dijalog.getTfCijena().getText() != null && !"".equals(dijalog.getTfCijena().getText())) {
            try {
                cijena = new Double(dijalog.getTfCijena().getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dijalog, "Cijena mora biti realan broj.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        String opisProblema = dijalog.getTaProblem().getText();

        RadniNalogDTO rn = new RadniNalogDTO(placeno, o, z, dijalog.getIdVozila(), troskovi, kilometraza, pz, cijena);
        rn.setOpisProblema(opisProblema);

        if (DAOFactory.getDAOFactory().getRadniNalogDAO().dodajRadniNalog(rn)) {
            int idRadnogNaloga = rn.getIdRadniNalog();

            for (int i = 0; i < dijalog.getListaZaposleniZaduzeni().getModel().getSize(); i++) {
                ZaposleniDTO zaposleni = dijalog.getListaZaposleniZaduzeni().getModel().getElementAt(i);
                RadniNalogRadnikDTO rnr = new RadniNalogRadnikDTO(idRadnogNaloga, zaposleni.getIdRadnik(), zaposleni.getUlogaRadnika());
                DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().dodajRadniNalogRadnik(rnr);

            }

            DefaultTableModel modelDio = (DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel();
            for (int j = 0; j < modelDio.getRowCount(); j++) {
                String sifraDio = dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(j, 1).toString();
                Integer kolicinaDio = Integer.parseInt(dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(j, 2).toString());
                Double cijenaDio = Double.parseDouble(dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(j, 3).toString());

                RadniNalogDioDTO rnd = new RadniNalogDioDTO();
                DioDTO dDTO = DAOFactory.getDAOFactory().getDioDAO().getDio(sifraDio);
                rnd.setIdRadniNalog(idRadnogNaloga);
                rnd.setIdDio(dDTO.getId());
                rnd.setKolicina(kolicinaDio);
                rnd.setCijena(cijenaDio);

                DAOFactory.getDAOFactory().getRadniNalogDioDAO().dodajRadniNalogDio(rnd);
            }

            //azuriranje kolicine dijelova
            for (int k = 0; k < dijalog.getTabelaDijelovi().getModel().getRowCount(); k++) {
                String sifra = dijalog.getTabelaDijelovi().getModel().getValueAt(k, 1).toString();
                Integer novaKolicina = Integer.parseInt(dijalog.getTabelaDijelovi().getModel().getValueAt(k, 5).toString());
                DioDTO dio = DAOFactory.getDAOFactory().getDioDAO().getDio(sifra);
                dio.setKolicina(novaKolicina);

                DAOFactory.getDAOFactory().getDioDAO().azurirajDio(dio);
            }

            if(dijalog.getForma() != null){
                dijalog.getForma().getBtnPrikaziRadniNalog().doClick();
            }
            JOptionPane.showMessageDialog(dijalog, "Uspješno dodat radni nalog", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
            dijalog.dispose();
        } else {
            JOptionPane.showMessageDialog(dijalog, "Greška", "Greška", JOptionPane.OK_OPTION);
            return;
        }
    }

    public void inicijalizujIzmijeniRadniNalog(IzmijeniRadniNalogDialog dijalog) {
        RadniNalogDTO rn = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(dijalog.getIdRadnogNaloga());

        dijalog.getDatumOtvaranjaNaloga().setDate(rn.getDatumOtvaranjaNaloga());
        dijalog.getDatumZatvaranjaNaloga().setDate(rn.getDatumZatvaranjaNaloga());
        dijalog.getPotrebnoZavrsitiDo().setDate(rn.getPredvidjenoVrijemeZavrsetka());

        rn.setIdRadniNalog(dijalog.getIdRadnogNaloga());
        dijalog.setNalog(rn);
        dijalog.setIdVozila(rn.getIdVozilo());
        dijalog.getTfIdVozila().setText((new Integer(rn.getIdVozilo()).toString()));
        dijalog.getTfTroskovi().setText((new Double(rn.getTroskovi())).toString());
        dijalog.getTfKilometraza().setText((new Integer(rn.getKilometraza()).toString()));
        dijalog.getTfCijena().setText((new Double(rn.getCijenaUsluge()).toString()));
        dijalog.getCbPlaceno().setSelected(rn.isPlaceno());
        dijalog.getTaProblem().setText(rn.getOpisProblema());

        dijalog.getTabelaIzabraniDijelovi().setAutoCreateRowSorter(true);
        dijalog.getTabelaDijelovi().setAutoCreateRowSorter(true);
        dijalog.getTabelaDijelovi().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dijalog.getTabelaIzabraniDijelovi().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dijalog.getTabelaDijelovi().setDefaultEditor(Object.class, null);
        dijalog.getTabelaIzabraniDijelovi().setDefaultEditor(Object.class, null);

        dijalog.getTabelaDijelovi().getTableHeader().setReorderingAllowed(false);
        dijalog.getTabelaIzabraniDijelovi().getTableHeader().setReorderingAllowed(false);

        VoziloDTO vozilo = DAOFactory.getDAOFactory().getVoziloDAO().vozilo(dijalog.getIdVozila());
        ModelVozilaDTO modelVozila = DAOFactory.getDAOFactory().getModelVozilaDAO().model(vozilo.getIdModelVozila());

        dijalog.getTfIdVozila().setEditable(false);
        dijalog.getSpinnerKolicina().setValue(1);

        ArrayList<RadniNalogRadnikDTO> rnr = DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().radniciNaRadnomNalogu(dijalog.getIdRadnogNaloga());
        DefaultListModel<ZaposleniDTO> modelZaposleni = new DefaultListModel<>();

        for (RadniNalogRadnikDTO r : rnr) {
            ZaposleniDTO z = DAOFactory.getDAOFactory().getZaposleniDAO().zaposleni(r.getIdRadnik());
            modelZaposleni.addElement(z);
        }

        dijalog.getListaZaposleniZaduzeni().setModel(modelZaposleni);
        dijalog.getListaZaposleniZaduzeni().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultListModel<ZaposleniDTO> modelOstaliZaposleni = new DefaultListModel<>();

        ArrayList<ZaposleniDTO> sviAktivniZaposleni = (ArrayList<ZaposleniDTO>) DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni();
        for (ZaposleniDTO z : sviAktivniZaposleni) {
            if (!((DefaultListModel<ZaposleniDTO>) dijalog.getListaZaposleniZaduzeni().getModel()).contains(z)) {
                modelOstaliZaposleni.addElement(z);
            }
        }

        dijalog.getListaZaposleni().setModel(modelOstaliZaposleni);
        dijalog.getListaZaposleni().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //za tabelu dostupnih dijelova
        String[] columns = {"Naziv", "Šifra", "Godište vozila", "Novo", "Vrsta goriva", "Dostupna količina", "Cijena"};
        DefaultTableModel modelDijelovi = new DefaultTableModel(columns, 0);
        dijalog.getTabelaDijelovi().setModel(modelDijelovi);

        List<DioDTO> listaDijelova = null;

        listaDijelova = DAOFactory.getDAOFactory().getDioDAO().getDijeloviZaPonudu(modelVozila.getMarka(), modelVozila.getModel());
        for (DioDTO d : listaDijelova) {
            Object[] rowData = {d.getNaziv(), d.getSifra(), d.getGodisteVozila(), d.getNovo() == true ? "Da" : "Ne", d.getVrstaGoriva(), d.getKolicina(), d.getTrenutnaCijena()};
            modelDijelovi.addRow(rowData);
        }

        dijalog.getTabelaDijelovi().setModel(modelDijelovi);
        //----------------

        //--vec dodati dijelovi u radni nalog
        List<RadniNalogDioDTO> listaDodatihDijelova = DAOFactory.getDAOFactory().getRadniNalogDioDAO().radniNalogDioIdRadniNalog(rn.getIdRadniNalog());
        String[] kolone = {"Naziv", "Šifra", "Količina", "Cijena"};
        DefaultTableModel modelDodatihDijelova = new DefaultTableModel(kolone, 0);

        for (RadniNalogDioDTO rndDTO : listaDodatihDijelova) {
            DioDTO dio = DAOFactory.getDAOFactory().getDioDAO().getDio(rndDTO.getIdDio());
            Object[] rowData = {dio.getNaziv(), dio.getSifra(), rndDTO.getKolicina(), rndDTO.getCijena()};
            modelDodatihDijelova.addRow(rowData);
        }

        dijalog.getTabelaIzabraniDijelovi().setModel(modelDodatihDijelova);

        dijalog.getTabelaDijelovi().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (dijalog.getTabelaDijelovi().getSelectedRow() > -1) {
                    Double cijena = Double.parseDouble(dijalog.getTabelaDijelovi().getValueAt(dijalog.getTabelaDijelovi().getSelectedRow(), 6).toString());
                    dijalog.getTfCijenaDijela().setText(cijena.toString());
                }
            }
        });
    }

    public void dodajDio(IzmijeniRadniNalogDialog dijalog) {
        int red = dijalog.getTabelaDijelovi().getSelectedRow();

        if (red < 0) {
            JOptionPane.showMessageDialog(dijalog, "Izaberite dio koji želite dodati!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        String sifra = dijalog.getTabelaDijelovi().getModel().getValueAt(red, 1).toString();
        int dodajKolicinu = (Integer) dijalog.getSpinnerKolicina().getValue();

        String naziv = dijalog.getTabelaDijelovi().getModel().getValueAt(red, 0).toString();
        Integer kolicina = Integer.parseInt(dijalog.getTabelaDijelovi().getModel().getValueAt(red, 5).toString());
        //Double cijena = Double.parseDouble(tabelaDijelovi.getModel().getValueAt(red, 6).toString());
        Double cijena = Double.parseDouble(dijalog.getTfCijenaDijela().getText());
        boolean postoji = false;

        if (dodajKolicinu > kolicina) {
            JOptionPane.showMessageDialog(dijalog, "Nema toliko jedinica izabranog dijela na stanju!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        for (int i = 0; i < dijalog.getTabelaIzabraniDijelovi().getRowCount(); i++) {
            if (sifra.equals(dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(i, 1))) {
                dijalog.getTabelaIzabraniDijelovi().getModel().setValueAt((Integer) dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(i, 2) + dodajKolicinu, i, 2);
                postoji = true;
            }
        }

        if (!postoji) {

            DefaultTableModel dtm = (DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel();

            Object[] rowData = {naziv, sifra, dodajKolicinu, cijena};
            dtm.addRow(rowData);
        }

        if (dijalog.getTfTroskovi().getText() != null && !"".equals(dijalog.getTfTroskovi().getText())) {
            Double trenutnaCijena = Double.parseDouble(dijalog.getTfTroskovi().getText());
            trenutnaCijena += dodajKolicinu * cijena;
            trenutnaCijena = Math.round(trenutnaCijena * 100.0) / 100.0;
            dijalog.getTfTroskovi().setText(trenutnaCijena.toString());
        } else {
            Double trenutnaCijena = 0.0;
            trenutnaCijena += dodajKolicinu * cijena;
            trenutnaCijena = Math.round(trenutnaCijena * 100.0) / 100.0;
            dijalog.getTfTroskovi().setText(trenutnaCijena.toString());
        }

        dijalog.getTabelaDijelovi().getModel().setValueAt(Integer.parseInt(dijalog.getTabelaDijelovi().getModel().getValueAt(red, 5).toString()) - dodajKolicinu, red, 5);
        dijalog.getSpinnerKolicina().setValue(1);
    }

    public void azurirajNalog(IzmijeniRadniNalogDialog dijalog) {
        Date datumOtvaranja = dijalog.getDatumOtvaranjaNaloga().getDate();
        java.sql.Date o = new java.sql.Date(datumOtvaranja.getTime());

        java.sql.Date z = null;
        Date datumZatvaranja = dijalog.getDatumZatvaranjaNaloga().getDate();
        try {
            z = new java.sql.Date(datumZatvaranja.getTime());
        } catch (Exception ex) {
        }

        Date potrebnoZavristi = dijalog.getPotrebnoZavrsitiDo().getDate();
        java.sql.Date pz = new java.sql.Date(potrebnoZavristi.getTime());

        Double troskovi = 0.0;
        Integer kilometraza = 0;
        Double cijena = 0.0;
        Boolean placeno = dijalog.getCbPlaceno().isSelected();

        if (dijalog.getTfTroskovi().getText() != null && !"".equals(dijalog.getTfTroskovi().getText())) {
            try {
                troskovi = new Double(dijalog.getTfTroskovi().getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dijalog, "Troškovi moraju biti realan broj.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        if (dijalog.getTfKilometraza().getText() != null && !"".equals(dijalog.getTfKilometraza().getText())) {
            try {
                kilometraza = new Integer(dijalog.getTfKilometraza().getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dijalog, "Kilometraža mora biti cjelobrojni podatak.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        if (dijalog.getTfCijena().getText() != null && !"".equals(dijalog.getTfCijena().getText())) {
            try {
                cijena = new Double(dijalog.getTfCijena().getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dijalog, "Cijena mora biti realan broj.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        String opisProblema = dijalog.getTaProblem().getText();

        RadniNalogDTO rn = new RadniNalogDTO(placeno, o, z, dijalog.getIdRadnogNaloga(), troskovi, kilometraza, pz, cijena);
        rn.setOpisProblema(opisProblema);
        rn.setIdRadniNalog(dijalog.getIdRadnogNaloga());

        DAOFactory.getDAOFactory().getRadniNalogDAO().azurirajRadniNalog(rn);

        if (true) {
            int idRadnogNaloga = rn.getIdRadniNalog();

            //brisi radnike izmjena;
            ArrayList<RadniNalogRadnikDTO> listaPrethodnih = DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().radniciNaRadnomNalogu(dijalog.getNalog().getIdRadniNalog());

            for (int i = 0; i < listaPrethodnih.size(); i++) {

                ZaposleniDTO zaposleni = DAOFactory.getDAOFactory().getZaposleniDAO().zaposleni(listaPrethodnih.get(i).getIdRadnik());

                if (!((DefaultListModel<ZaposleniDTO>) dijalog.getListaZaposleniZaduzeni().getModel()).contains(zaposleni)) {

                    DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().izbrisi(listaPrethodnih.get(i));
                }

            }

            //dodaj radnike izmjena
            for (int i = 0; i < dijalog.getListaZaposleniZaduzeni().getModel().getSize(); i++) {
                ZaposleniDTO zaposleni = dijalog.getListaZaposleniZaduzeni().getModel().getElementAt(i);
                RadniNalogRadnikDTO rnr = new RadniNalogRadnikDTO(dijalog.getIdRadnogNaloga(), zaposleni.getIdRadnik(), zaposleni.getUlogaRadnika());

                if (!DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().postojiLiZapis(dijalog.getIdRadnogNaloga(), zaposleni.getIdRadnik())) {
                    DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().dodajRadniNalogRadnik(rnr);
                }

            }

            //dijelovi
            //brisanje izbrisanih
            ArrayList<RadniNalogDioDTO> prethodniRND = DAOFactory.getDAOFactory().getRadniNalogDioDAO().radniNalogDioIdRadniNalog(dijalog.getIdRadnogNaloga());
            for (RadniNalogDioDTO rnd : prethodniRND) {

                DioDTO dioDTO = DAOFactory.getDAOFactory().getDioDAO().getDio(rnd.getIdDio());

                boolean pronadjen = false;
                for (int i = 0; i < dijalog.getTabelaIzabraniDijelovi().getModel().getRowCount(); i++) {
                    if (dioDTO.getSifra().equals(dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(i, 1))) {
                        pronadjen = true;
                        //rnd.setKolicina(new Integer(((DefaultTableModel)tabelaIzabraniDijelovi.getModel()).getValueAt(i, 2).toString()));
                    }
                }

                if (pronadjen == false) {
                    dioDTO.setKolicina(dioDTO.getKolicina() + rnd.getKolicina());
                    DAOFactory.getDAOFactory().getDioDAO().azurirajDio(dioDTO);
                    DAOFactory.getDAOFactory().getRadniNalogDioDAO().obrisiRadniNalogDio(dijalog.getIdRadnogNaloga(), dioDTO.getId());
                }
            }

            //dodavanje novih
            for (int i = 0; i < dijalog.getTabelaIzabraniDijelovi().getModel().getRowCount(); i++) {
                String sifraDijela = ((DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel()).getValueAt(i, 1).toString();
                DioDTO dio = DAOFactory.getDAOFactory().getDioDAO().getDio(sifraDijela);

                RadniNalogDioDTO rnd = DAOFactory.getDAOFactory().getRadniNalogDioDAO().radniNalogDio(dijalog.getIdRadnogNaloga(), dio.getId());
                if (rnd != null) {

                    int novoStanje = rnd.getKolicina() - new Integer(((DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel()).getValueAt(i, 2).toString());
                    dio.setKolicina(dio.getKolicina() + novoStanje);
                    DAOFactory.getDAOFactory().getDioDAO().azurirajDio(dio);

                    rnd.setKolicina(new Integer(((DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel()).getValueAt(i, 2).toString()));
                    rnd.setCijena(new Double(((DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel()).getValueAt(i, 3).toString()));
                    DAOFactory.getDAOFactory().getRadniNalogDioDAO().azurirajRadniNalogDio(rnd);
                } else {
                    RadniNalogDioDTO noviRadniNalogDio = new RadniNalogDioDTO();
                    noviRadniNalogDio.setIdRadniNalog(dijalog.getIdRadnogNaloga());
                    noviRadniNalogDio.setIdDio(dio.getId());
                    noviRadniNalogDio.setKolicina(new Integer(((DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel()).getValueAt(i, 2).toString()));
                    noviRadniNalogDio.setCijena(new Double(((DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel()).getValueAt(i, 3).toString()));
                    DAOFactory.getDAOFactory().getRadniNalogDioDAO().dodajRadniNalogDio(noviRadniNalogDio);
                }
            }

//            DefaultTableModel modelDio = (DefaultTableModel) tabelaIzabraniDijelovi.getModel();
//            for(int j = 0; j < modelDio.getRowCount(); j++){
//                String sifraDio = tabelaIzabraniDijelovi.getModel().getValueAt(j, 1).toString();
//                Integer kolicinaDio = Integer.parseInt(tabelaIzabraniDijelovi.getModel().getValueAt(j, 2).toString());
//                Double cijenaDio = Double.parseDouble(tabelaIzabraniDijelovi.getModel().getValueAt(j, 3).toString());
//
//                RadniNalogDioDTO rnd = new RadniNalogDioDTO();
//                DioDTO dDTO =DAOFactory.getDAOFactory().getDioDAO().getDio(sifraDio);
//                rnd.setIdRadniNalog(idRadnogNaloga);
//                rnd.setIdDio(dDTO.getId());
//                rnd.setKolicina(kolicinaDio);
//                rnd.setCijena(cijenaDio);
//
//                DAOFactory.getDAOFactory().getRadniNalogDioDAO().dodajRadniNalogDio(rnd);
//            }
            //azuriranje kolicine dijelova
            for (int k = 0; k < dijalog.getTabelaDijelovi().getModel().getRowCount(); k++) {
                String sifra = dijalog.getTabelaDijelovi().getModel().getValueAt(k, 1).toString();
                Integer novaKolicina = Integer.parseInt(dijalog.getTabelaDijelovi().getModel().getValueAt(k, 5).toString());
                DioDTO dio = DAOFactory.getDAOFactory().getDioDAO().getDio(sifra);
                dio.setKolicina(novaKolicina);

                DAOFactory.getDAOFactory().getDioDAO().azurirajDio(dio);
            }

            JOptionPane.showMessageDialog(dijalog, "Uspješno ažuriran radni nalog", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
            dijalog.dispose();
        } else {
            JOptionPane.showMessageDialog(dijalog, "Greška", "Greška", JOptionPane.OK_OPTION);
            return;
        }
    }

    public void ukloniDio(IzmijeniRadniNalogDialog dijalog) {
        int red = dijalog.getTabelaIzabraniDijelovi().getSelectedRow();

        if (red < 0) {
            JOptionPane.showMessageDialog(dijalog, "Morate odabrati dio u tabeli koji želite ukloniti!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        Integer kolicina = Integer.parseInt(dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(red, 2).toString());
        String sifra = dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(red, 1).toString();
        boolean pronasao = false;
        Double cijenaJednogDijela = 0.0;

        cijenaJednogDijela = Double.parseDouble(dijalog.getTabelaIzabraniDijelovi().getModel().getValueAt(red, 3).toString());

        for (int i = 0; i < dijalog.getTabelaDijelovi().getRowCount() && pronasao == false; i++) {
            if (sifra.equals(dijalog.getTabelaDijelovi().getModel().getValueAt(i, 1))) {
                pronasao = true;
                dijalog.getTabelaDijelovi().getModel().setValueAt(Integer.parseInt(dijalog.getTabelaDijelovi().getModel().getValueAt(i, 5).toString()) + kolicina, i, 5);
                //  cijenaJednogDijela = Double.parseDouble(tabelaDijelovi.getModel().getValueAt(i, 6).toString());
            }
        }

        Double cijena = Double.parseDouble(dijalog.getTfTroskovi().getText());
        Double novaCijena = cijena - kolicina * cijenaJednogDijela;
        dijalog.getTfTroskovi().setText(novaCijena.toString());

        if (novaCijena < 0) {
            dijalog.getTfTroskovi().setText(new String("0"));
        }

        ((DefaultTableModel) dijalog.getTabelaIzabraniDijelovi().getModel()).removeRow(red);
    }
    
    public void inicijalizacijaPregledIstorije(PregledIstorijePopravkiDialog dijalog){
        dijalog.getTabela().getTableHeader().setReorderingAllowed(false);
        dijalog.getTabela().setDefaultEditor(Object.class, null);
        dijalog.getTabela().setAutoCreateRowSorter(true);

        VoziloDTO vozilo = DAOFactory.getDAOFactory().getVoziloDAO().vozilo(dijalog.getIdVozila());
        ModelVozilaDTO model = DAOFactory.getDAOFactory().getModelVozilaDAO().model(vozilo.getIdModelVozila());
        KupacDTO vlasnik = DAOFactory.getDAOFactory().getKupacDAO().kupac(vozilo.getIdKupac());

        dijalog.setTitle("Istorija vozila");

        if (vlasnik.getNaziv() == null) {
            dijalog.getLbVlasnik().setText(vlasnik.getIme() + " " + vlasnik.getPrezime() + " (Privatno lice)");
        } else {
            dijalog.getLbVlasnik().setText(vlasnik.getNaziv() + " (Pravno lice)");
        }

        dijalog.getLbAdresa().setText(vlasnik.getAdresa());
        dijalog.getLbTelefon().setText(vlasnik.getTelefon());
        dijalog.getLbGrad().setText(vlasnik.getGrad());

        dijalog.getLbBrojRegistracije().setText(vozilo.getBrojRegistracije());
        dijalog.getLbKilovati().setText(vozilo.getKilovat().toString());
        dijalog.getLbKubikaza().setText(vozilo.getKubikaza().toString());
        dijalog.getLbGodiste().setText(vozilo.getGodiste().toString());
        dijalog.getLbVrstaGoriva().setText(vozilo.getVrstaGoriva());
        dijalog.getLbModelVozila().setText(model.getMarka() + " " + model.getModel());

        ArrayList<RadniNalogDTO> rn = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi(dijalog.getIdVozila());
        String[] columns = {"ID", "Datum otvaranja naloga", "Datum zatvaranja naloga", "Datum potrebnog završetka", "Plaćeno"};
        DefaultTableModel modell = new DefaultTableModel(columns, 0);

        for (RadniNalogDTO r : rn) {
            String placeno;
            if (r.isPlaceno()) {
                placeno = "Da";
            } else {
                placeno = "Ne";
            }
            Object[] rowData = {r.getIdRadniNalog(), r.getDatumOtvaranjaNaloga(), r.getDatumZatvaranjaNaloga(), r.getPredvidjenoVrijemeZavrsetka(), placeno};
            modell.addRow(rowData);
        }

        dijalog.getTabela().setModel(modell);

        dijalog.getTabela().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (dijalog.getTabela().getSelectedRow() >= 0) {

                    dijalog.getBtnAzuriraj().setEnabled(true);
                    int column = 0;
                    int row = dijalog.getTabela().getSelectedRow();
                    Integer value = new Integer(dijalog.getTabela().getModel().getValueAt(row, column).toString());

                    RadniNalogDTO r = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(value);

                    ArrayList<RadniNalogRadnikDTO> listaRadnika = DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().radniciNaRadnomNalogu(value);

                    DefaultListModel mod = new DefaultListModel();

                    for (RadniNalogRadnikDTO rnt : listaRadnika) {
                        ZaposleniDTO zap = DAOFactory.getDAOFactory().getZaposleniDAO().zaposleni(rnt.getIdRadnik());
                        mod.addElement(zap.getIme() + "(" + zap.getImeOca() + ")" + zap.getPrezime());
                    }

                    dijalog.getListRadnici().setModel(mod);

                    dijalog.getTbIdNaloga().setText(value.toString());
                    dijalog.getTbIdNaloga().setEditable(false);
                    Date d = new Date(r.getDatumOtvaranjaNaloga().getTime());
                    dijalog.getTbNalogOtvoren().setDate(d);
                    
                    if(r.getDatumZatvaranjaNaloga() != null){
                    Date dd = new Date(r.getDatumZatvaranjaNaloga().getTime());
                    dijalog.getTbNalogZatvoren().setDate(dd);
                    dijalog.getTbNalogZatvoren().setVisible(true);
                    dijalog.getjLabelNalogZatvorenLABEL().setVisible(true);
                    }
                    else{
                        dijalog.getTbNalogZatvoren().setVisible(false);
                        dijalog.getjLabelNalogZatvorenLABEL().setVisible(false);
                    }
                    
                    
                    if(r.getPredvidjenoVrijemeZavrsetka() != null){
                    Date ddd = new Date(r.getPredvidjenoVrijemeZavrsetka().getTime());
                    dijalog.getTbRokZatvaranja().setDate(ddd);
                    dijalog.getTbRokZatvaranja().setVisible(true);
                    dijalog.getjLabelNalogZatvorenLABEL().setVisible(true);
                   dijalog.getjLabelNalogZatvorenLABEL().setText("Nalog zatvoren:");
                    }
                    else{
                        dijalog.getTbRokZatvaranja().setVisible(false);
                        dijalog.getjLabelNalogZatvorenLABEL().setVisible(false);
                        dijalog.getjLabelNalogZatvorenLABEL().setText("");
                    }

                    if (r.isPlaceno()) {
                        dijalog.getCbPlaceno().setSelected(true);
                    } else {
                        dijalog.getCbPlaceno().setSelected(false);
                    }

                    dijalog.getTbTroskovi().setText(new Double(r.getTroskovi()).toString());
                    dijalog.getTbCijena().setText(new Double(r.getCijenaUsluge()).toString());
                    dijalog.getTaProblem().setText(r.getOpisProblema());
                }
            }
        });
    }
}
