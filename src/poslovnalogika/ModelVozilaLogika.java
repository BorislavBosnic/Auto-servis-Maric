/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.dialog.DodajModel;
import autoservismaric.dialog.IzmijeniIzbrisiModelDialog;
import autoservismaric.forms.HomeForm1;
import data.dao.DAOFactory;
import data.dto.ModelVozilaDTO;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Aco
 */
public class ModelVozilaLogika {

    public void ucitajModeleDodavanje(HomeForm1 form, String marka) {
        ArrayList<ModelVozilaDTO> list = new ArrayList<ModelVozilaDTO>();
        list = DAOFactory.getDAOFactory().getModelVozilaDAO().getModeli(marka);

        ArrayList<String> modeli = new ArrayList<String>();
        modeli.add("Svi");

        form.getJcbModel().removeAllItems();
        form.getJcbModel().addItem("Svi");

        if (list != null) {
            for (ModelVozilaDTO d : list) {
                if (!modeli.contains(d.getModel())) {
                    modeli.add(d.getModel());
                    form.getJcbModel().addItem(d.getModel());
                }
            }
        }
    }

    public void ucitajModelePretrazivanje(HomeForm1 form, String marka) {
        ArrayList<ModelVozilaDTO> list = new ArrayList<ModelVozilaDTO>();
        list = DAOFactory.getDAOFactory().getModelVozilaDAO().getModeli(marka);

        ArrayList<String> modeli = new ArrayList<String>();
        modeli.add("Svi");

        form.getCbModel().removeAllItems();
        form.getCbModel().addItem("Svi");

        if (list != null) {
            for (ModelVozilaDTO d : list) {
                if (!modeli.contains(d.getModel())) {
                    modeli.add(d.getModel());
                    form.getCbModel().addItem(d.getModel());
                }
            }
        }
    }

    public void dodajModel(DodajModel dijalog) {
        String marka = dijalog.getTfMarkaDodaj().getText();
        String model = dijalog.getTfModel().getText();
        if (marka != null && !"".equals(marka) && model != null && !"".equals(model)) {
            ModelVozilaDTO e = DAOFactory.getDAOFactory().getModelVozilaDAO().model(marka, model);
            if (e == null) {
                ModelVozilaDTO mv = new ModelVozilaDTO();
                mv.setMarka(marka);
                mv.setModel(model);
                if (DAOFactory.getDAOFactory().getModelVozilaDAO().dodajModel(mv)) {
                    //lbPoruka.setText("Uspješno dodato.");

                    JOptionPane.showMessageDialog(dijalog, "Uspješno dodat model", "Obavješenje", JOptionPane.INFORMATION_MESSAGE);

                    if (dijalog.getDvd() != null) {
                        // dvd.ucitajPreporukeMarke();
                        //dvd.ucitajPreporukeModel();
                        dijalog.getDvd().marka.addToDictionary(marka);
                        dijalog.getDvd().model.addToDictionary(model);
                    }

                    if (dijalog.getIvd() != null) {
                        //ivd.ucitajPreporukeMarke();
                        //ivd.ucitajPreporukeModel();
                        dijalog.getDvd().marka.addToDictionary(marka);
                        dijalog.getDvd().model.addToDictionary(model);

                    }

                    dijalog.setM(mv);
//                    if(VozilaLogika.modeli == null){
//                        try{
//                            VozilaLogika.modeli.add(mv);
//                        }catch(NullPointerException ex){
//                            //ex.printStackTrace();
//                        }
//                    }

                    dijalog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dijalog, "Nije dodat model", "Greška", JOptionPane.OK_OPTION);
                }
            } else {
                JOptionPane.showMessageDialog(dijalog, "Već postoji taj model", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(dijalog, "Morate popuniti polja za model i marku", "Greška", JOptionPane.OK_OPTION);
            return;
        }
    }

    public void azurirajModel(IzmijeniIzbrisiModelDialog dijalog) {
        String marka = dijalog.getTfMarkaAzuriraj().getText();
        String model = dijalog.getTfModelAzuriraj().getText();

        if (DAOFactory.getDAOFactory().getModelVozilaDAO().model(marka, model) != null) {
            JOptionPane.showMessageDialog(dijalog, "Već postoji takav tip vozila!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ModelVozilaDTO m = DAOFactory.getDAOFactory().getModelVozilaDAO().model(dijalog.getMarka(), dijalog.getModel());
        m.setMarka(marka);
        m.setModel(model);
        if (DAOFactory.getDAOFactory().getModelVozilaDAO().azurirajModel(m)) {
            JOptionPane.showMessageDialog(dijalog, "Uspješno ažuriranje!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(dijalog, "Neuspješno ažuriranje!", "Greška", JOptionPane.OK_OPTION);
        }

        dijalog.setModeli(DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli());
        Object[] columns = {"Marka", "Model"};
        DefaultTableModel dtm = new DefaultTableModel(columns, 0);
        for (ModelVozilaDTO mmmm : dijalog.getModeli()) {
            Object[] row = {mmmm.getMarka(), mmmm.getModel()};
            dtm.addRow(row);
        }
        dijalog.getTabela().setModel(dtm);
    }

    public void inicijalizujIzmijeniIzbrisiModel(IzmijeniIzbrisiModelDialog dijalog) {
        dijalog.getTabela().getTableHeader().setReorderingAllowed(false);

        dijalog.setModeli(DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli());
        DefaultTableModel model = (DefaultTableModel) dijalog.getTabela().getModel();
        for (ModelVozilaDTO m : dijalog.getModeli()) {
            Object[] objekat = {m.getMarka(), m.getModel()};
            model.addRow(objekat);
        }

        dijalog.getTabela().setModel(model);
        dijalog.getTabela().setAutoCreateRowSorter(true);
        dijalog.getTabela().setDefaultEditor(Object.class, null);
    }

    public void izbrisi(IzmijeniIzbrisiModelDialog dijalog) {
        int red = dijalog.getTabela().getSelectedRow();
        String model = dijalog.getTabela().getModel().getValueAt(red, 1).toString();
        String marka = dijalog.getTabela().getModel().getValueAt(red, 0).toString();
        ModelVozilaDTO mv = DAOFactory.getDAOFactory().getModelVozilaDAO().model(marka, model);
        if (DAOFactory.getDAOFactory().getModelVozilaDAO().postojiLiVoziloSaOvimModelom(mv)) {
            JOptionPane.showMessageDialog(dijalog, "Postoje vozila koja ovog tipa u sistemu. Brisanje nije moguće dok oni postoje.", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (DAOFactory.getDAOFactory().getModelVozilaDAO().obrisiModel(mv)) {
                JOptionPane.showMessageDialog(dijalog, "Uspješno izbrisan radni nalog", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                for (int i = dijalog.getTabela().getModel().getRowCount() - 1; i >= 0; i--) {
                    if ((dijalog.getTabela().getModel().getValueAt(i, 0).toString().toLowerCase()).equals(marka.toLowerCase()) && (dijalog.getTabela().getModel().getValueAt(i, 1).toString().toLowerCase()).equals(model.toLowerCase())) {
                        ((DefaultTableModel) dijalog.getTabela().getModel()).removeRow(i);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dijalog, "Brisanje nije moguće.", "Greška", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
