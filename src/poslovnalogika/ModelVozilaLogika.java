/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.forms.HomeForm1;
import data.dao.DAOFactory;
import data.dto.ModelVozilaDTO;
import java.util.ArrayList;

/**
 *
 * @author Aco
 */
public class ModelVozilaLogika {
    public void ucitajModeleDodavanje(HomeForm1 form, String marka){
        ArrayList<ModelVozilaDTO> list = new ArrayList<ModelVozilaDTO>();
        list = DAOFactory.getDAOFactory().getModelVozilaDAO().getModeli(marka);
        
        ArrayList<String> modeli = new ArrayList<String>();
        modeli.add("Svi");
                        
        form.getJcbModel().removeAllItems();
        form.getJcbModel().addItem("Svi");
        
        if(list != null)
        for(ModelVozilaDTO d : list){
            if(!modeli.contains(d.getModel())){
                modeli.add(d.getModel());
                form.getJcbModel().addItem(d.getModel());                
            }
        }
    }
    public void ucitajModelePretrazivanje(HomeForm1 form, String marka){
        ArrayList<ModelVozilaDTO> list = new ArrayList<ModelVozilaDTO>();
        list = DAOFactory.getDAOFactory().getModelVozilaDAO().getModeli(marka);
        
        ArrayList<String> modeli = new ArrayList<String>();
        modeli.add("Svi");
                        
        form.getCbModel().removeAllItems();
        form.getCbModel().addItem("Svi");
        
        if(list != null)
        for(ModelVozilaDTO d : list){
            if(!modeli.contains(d.getModel())){
                modeli.add(d.getModel());
                form.getCbModel().addItem(d.getModel());                
            }
        }
    }
}
