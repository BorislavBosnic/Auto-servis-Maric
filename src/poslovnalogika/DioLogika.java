/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.dialog.IzmijeniDioDialog;
import autoservismaric.dialog.PregledProdanihDijelovaDialog;
import autoservismaric.dialog.ProdajDioDialog;
import autoservismaric.forms.HomeForm1;
import static autoservismaric.forms.HomeForm1.selRedDio;
import data.dao.DAOFactory;
import data.dto.DioDTO;
import data.dto.DioModelVozilaDTO;
import data.dto.ModelVozilaDTO;
import data.dto.ProdanDioDTO;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Aco
 */
public class DioLogika {

    public DioLogika() {
    }

    
    public void dodajDio(HomeForm1 form) {
        String sifra = "";
        String naziv = "";
        Double cijena = null;
        Integer godiste = null;
        boolean stanje;
        String gorivo = "";
        String marka = "";
        String model = "";
        Integer kolicina = null;
        boolean zaSve = false;
        boolean flag = false;

        sifra = form.getTfSifraDio().getText();
        if ("".equals(sifra)) {
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(form, "Unesite šifru", "Upozorenje", JOptionPane.ERROR_MESSAGE);
            flag = true;
            return;
        }
        naziv = form.getTfNazivDio().getText();
        if ("".equals(naziv)) {
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(form, "Unesite naziv", "Upozorenje", JOptionPane.ERROR_MESSAGE);
            flag = true;
            return;
        }
        try {
            cijena = Double.parseDouble(form.getTfCijenaDio().getText());
        } catch (NumberFormatException e) {
            if (!"".equals(form.getTfCijenaDio().getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(form, "Nepravilan format cijene!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                return;
            } else {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(form, "Unesite cijenu!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                return;
            }
        }
        try {
            godiste = Integer.parseInt(form.getTfGodisteDio().getText());
        } catch (NumberFormatException e) {
            if (!"".equals(form.getTfGodisteDio().getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(form, "Nepravilan format godišta!!!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                return;
            }
            godiste = null;
        }
        stanje = form.getCbStanje().isSelected();
        gorivo = (String) form.getCbGorivoDio().getSelectedItem();
        marka = (String) form.getCbMarkaDio().getSelectedItem();
        model = (String) form.getCbModelDio().getSelectedItem();
        try {
            kolicina = Integer.parseInt(form.getTfKolicinaDio().getText());
            if (kolicina < 1) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(form, "Količina mora biti pozitivan broj", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            if (!"".equals(form.getTfKolicinaDio().getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(form, "Nepravilan format količine!!!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                return;
            } else {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(form, "Unesite količinu!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                return;
            }
        }
        if (flag) {
            return;
        }

        if (!stanje) {
            sifra += "s";
        }

        DioDTO pom = DAOFactory.getDAOFactory().getDioDAO().getDio(sifra);
        if (pom != null) {
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(form, "Dio sa ovom šifrom već postoji.", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if ("Svi".equals(marka)) {
            zaSve = true;
        }
        DioDTO noviDio = new DioDTO(sifra, naziv, gorivo, godiste, stanje, cijena, kolicina, zaSve, marka, model);
        DioDTO dio = DAOFactory.getDAOFactory().getDioDAO().dio(noviDio);
        ModelVozilaDTO modVoz = DAOFactory.getDAOFactory().getModelVozilaDAO().model(marka, model);
        DioModelVozilaDTO dmv = null;
        if (dio != null && modVoz != null) {
            dmv = DAOFactory.getDAOFactory().getDioModelVozilaDAO().getDioModelVozila(dio.getId(),
                    modVoz.getIdModelVozila());
        }

        if (dmv != null) {
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(form, "Dio sa ovom šifrom već postoji.", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);

        } else {
            if (DAOFactory.getDAOFactory().getDioDAO().dodajDio(noviDio)) {
                if (modVoz == null) {
                    DAOFactory.getDAOFactory().getModelVozilaDAO().dodajModel(new ModelVozilaDTO(marka, model));
                }
                DioDTO dioo = DAOFactory.getDAOFactory().getDioDAO().dio(noviDio);
                ModelVozilaDTO mv = DAOFactory.getDAOFactory().getModelVozilaDAO().model(marka, model);
                DAOFactory.getDAOFactory().getDioModelVozilaDAO().dodajDioModelVozila(dioo.getId(), mv.getIdModelVozila());
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(form, "Uspješno dodan dio!!!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                DefaultTableModel dtm = (DefaultTableModel) form.getjTable().getModel();
                dtm.addRow(new Object[]{dioo.getId(), sifra, naziv, marka, model, godiste, gorivo, cijena, kolicina, stanje ? "Da" : "Ne"});
            }
        }
        //return true;
    }

    public void pretraziDijelove(HomeForm1 form) {

        String sifra = "";
        String naziv = "";
        Double cijena = null;
        Integer godiste = null;
        boolean stanje;
        String gorivo = "";
        String marka = "";
        String model = "";
        Integer kolicina = null;
        boolean flag = false;
        Integer id = null;

        sifra = form.getTfSifra().getText();
        naziv = form.getTfNaziv().getText();
        stanje = form.getCbNovo().isSelected();
        try {
            id = Integer.parseInt(form.getTfId().getText());
        } catch (NumberFormatException e) {
            if (!"".equals(form.getTfId().getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(form, "Nepravilan format cijene!", "Greška", JOptionPane.ERROR_MESSAGE);
            }
            id = 0;
            //flag = true;
        }
        try {
            godiste = Integer.parseInt(form.getTfGodiste().getText());
        } catch (NumberFormatException e) {
            if (!"".equals(form.getTfGodiste().getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(form, "Nepravilan format godišta!!!", "Greška", JOptionPane.ERROR_MESSAGE);
                //flag = true;
            }
            godiste = null;
        }
        stanje = form.getCbNovo().isSelected();
        gorivo = (String) form.getCbGorivo().getSelectedItem();
        marka = (String) form.getCbMarka().getSelectedItem();
        model = (String) form.getCbModel().getSelectedItem();
        DioDTO dio = new DioDTO(id, sifra, naziv, gorivo, godiste, stanje, cijena, kolicina, true, marka, model);
        ArrayList<DioDTO> dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijeloviZaBiloKojiParametar(dio);
        /*if (id != 0) {
            dijelovi.add(DAOFactory.getDAOFactory().getDioDAO().getDio(id));
        } else if (!"".equals(sifra)) {
            dijelovi.add(DAOFactory.getDAOFactory().getDioDAO().getDio(sifra));
        } else if (!"".equals(naziv) && godiste != null && !"Svi".equals(marka) && !"Svi".equals(gorivo)) {
            if ("Svi".equals(model)) {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(naziv, godiste, marka, gorivo, stanje);
            } else {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(naziv, godiste, marka, model, gorivo, stanje);
            }
        } else if (!"".equals(naziv) && godiste != null && !"Svi".equals(marka)) {
            if ("Svi".equals(model)) {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(naziv, godiste, marka, stanje);
            } else {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovi(naziv, godiste, marka, model, stanje);
            }
        } else if (!"".equals(naziv) && godiste != null) {
            if (!"Svi".equals(gorivo)) {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovi(naziv, godiste, gorivo, stanje);
            } else {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovi(naziv, godiste, stanje);
            }
        } else if (!"".equals(naziv) && !"Svi".equals(marka) && !"Svi".equals(gorivo)) {
            if ("Svi".equals(model)) {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovibg2(naziv, marka, gorivo, stanje);
            } else {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovibg(naziv, marka, model, gorivo, stanje);
            }
        } else if (!"".equals(naziv) && !"Svi".equals(marka)) {
            if ("Svi".equals(model)) {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovibg(naziv, marka, stanje);
            } else {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovibg(naziv, marka, model, stanje);
            }
        } else if (!"".equals(naziv) && !"Svi".equals(gorivo)) {
            dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovibg2(naziv, gorivo, stanje);
        } else if (!"".equals(naziv)) {
            dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijeloviNaziv(naziv, stanje);
        } else if (!"Svi".equals(gorivo) && !"Svi".equals(marka)) {
            if ("Svi".equals(model)) {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(gorivo, marka, stanje);
            } else {
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(gorivo, marka, model, stanje);
            }
        } else {
            JOptionPane.showMessageDialog(null,"Polja za pretragu su prazna.Popunite još neka polja pa ponovite pretragu", "Problem", JOptionPane.ERROR_MESSAGE);
            dijelovi = null;
        }*/

        DefaultTableModel dtm = (DefaultTableModel) form.getJTable().getModel();
        dtm.setRowCount(0);

        int i = 0;
        if (dijelovi != null) {
            while (i < dijelovi.size()) {
                DioDTO d = dijelovi.get(i);
                dtm.addRow(new Object[]{d.getId(), d.getSifra(), d.getNaziv(), d.getMarka(), d.getModel(), d.getGodisteVozila(),
                    d.getVrstaGoriva(), Math.round(d.getTrenutnaCijena() * 100) / 100, d.getKolicina(), d.getNovo() ? "Da" : "Ne"});
                i++;
            }
        }
    }

    public void ucitajPopupZaDijelove(HomeForm1 form) {
        JTable jTable = form.getJTable();
        JMenuItem prodaj = new JMenuItem("Prodaj");
        prodaj.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new ProdajDioDialog((DefaultTableModel) jTable.getModel(), jTable.getSelectedRow(), 0.0, form).setVisible(true);
            }
        });
        form.getPopupDio().add(prodaj);

        JMenuItem azuriraj = new JMenuItem("Ažuriraj");
        azuriraj.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new IzmijeniDioDialog((DefaultTableModel) jTable.getModel(), jTable.getSelectedRow(), form).setVisible(true);
            }
        });
        form.getPopupDio().add(azuriraj);

        JMenuItem obrisi = new JMenuItem("Obriši");
        obrisi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel dtm = (DefaultTableModel) jTable.getModel();
                int id = (Integer) ((DefaultTableModel) jTable.getModel()).getValueAt(jTable.getSelectedRow(), 0);
                if (DAOFactory.getDAOFactory().getDioModelVozilaDAO().obrisiDioModelVozila(id)
                        && DAOFactory.getDAOFactory().getDioDAO().obrisiDio(id)) {
                    dtm.removeRow(jTable.getSelectedRow());
                    JOptionPane jop = new JOptionPane();
                    jop.showMessageDialog(new JFrame(), "Uspješno obrisan dio", "Obavještenje",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        form.getPopupDio().add(obrisi);
        jTable.setComponentPopupMenu(form.getPopupDio());

        form.getPopupDio().addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = jTable.rowAtPoint(SwingUtilities.convertPoint(form.getPopupDio(), new Point(0, 0), jTable));
                        selRedDio = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = jTable.getModel().getColumnName(0);

                        if (selRedDio >= 0) {
                            if ("Id".equals(imeKolone)) {
                                int idDio = Integer.parseInt(jTable.getModel().getValueAt(selRedDio, column).toString());
                            }
                        }
                        if (rowAtPoint > -1) {
                            jTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
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

    public void prikazSvihProdanihDijelova(PregledProdanihDijelovaDialog parent) {
        ArrayList<ProdanDioDTO> lista = DAOFactory.getDAOFactory().getProdanDioDAO().getSviProdaniDijelovi();
        DefaultTableModel table = (DefaultTableModel) parent.getTblProdaniDijelovi().getModel();
        ProdanDioDTO d=new ProdanDioDTO();
        for (int i = 0; i < lista.size(); i++) {
            d = lista.get(i);
            table.addRow(new Object[]{d.getIdDio(), d.getSifra(), d.getNaziv(), d.getCijena(), d.getKolicina(), d.getDatum()});
        }
    }

    public void traziProdanDio(PregledProdanihDijelovaDialog form) {
        ProdanDioDTO parametri = new ProdanDioDTO();
        //
        String sifra = "";
        String naziv = "";
        Double cijena = null;
        Integer kolicina = null;
        Integer id = null;
        java.sql.Date datumProdaje = null;

        sifra = form.getTfSifra().getText();
        if(!"".equals(sifra)){
            parametri.setSifra(sifra);
        }
        
        naziv=form.getTfNaziv().getText();
        if(!"".equals(naziv)){
            parametri.setNaziv(naziv);
        }
        
        if(!"".equals(form.getTfProdajnaCijena().getText())){
            try{
                cijena=Double.valueOf(form.getTfProdajnaCijena().getText());
                parametri.setCijena(cijena);
            }catch(Exception e){
                JOptionPane.showConfirmDialog(null,"Cijena nije u dobrom formatu.", "Problem", JOptionPane.ERROR_MESSAGE);
                return;
            } 
        }
        
        if(!"".equals(form.getTfKolicina().getText())){
            try{
                kolicina=Integer.valueOf(form.getTfKolicina().getText());
                parametri.setKolicina(kolicina);
            }catch(Exception e){
                JOptionPane.showConfirmDialog(null,"Količina nije u dobrom formatu.", "Problem", JOptionPane.ERROR_MESSAGE);
                return;
            }   
        }
        if(!"".equals(form.getTxtId().getText())){
            try{
                id=Integer.valueOf(form.getTxtId().getText());
                parametri.setId(id);
            }catch(Exception e){
                JOptionPane.showConfirmDialog(null,"Id nije u dobrom formatu.", "Problem", JOptionPane.ERROR_MESSAGE);
                return;
            }   
        }
        try{
            datumProdaje=new java.sql.Date(form.getDcDatumProdaje().getDate().getTime());
            parametri.setDatum(datumProdaje);
        }catch(Exception e){
            //nista
        }
        
        ArrayList<ProdanDioDTO> lista = DAOFactory.getDAOFactory().getProdanDioDAO().traziProdanDio(parametri);
        if(lista==null){
            JOptionPane.showMessageDialog(null, "Nema traženih rezultata.", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        DefaultTableModel table = (DefaultTableModel) form.getTblProdaniDijelovi().getModel();
        table.setRowCount(0);
        ProdanDioDTO d;
        for (int i = 0; i < lista.size(); i++) {
            d = lista.get(i);
            table.addRow(new Object[]{d.getIdDio(), d.getSifra(), d.getNaziv(), d.getCijena(), d.getKolicina(), d.getDatum()});
        }
    }
}
