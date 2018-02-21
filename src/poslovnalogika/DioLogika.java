/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.forms.HomeForm1;
import data.dao.DAOFactory;
import data.dto.DioDTO;
import data.dto.DioModelVozilaDTO;
import data.dto.ModelVozilaDTO;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Aco
 */
public class DioLogika {
    public boolean dodajDio(HomeForm1 form, String sifra, String naziv, String gorivo, Integer godiste, boolean stanje, 
            Double cijena, Integer kolicina, boolean zaSve,String marka, String model){
        if(!stanje){
            sifra += "s";
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
            return true;
    }
}
