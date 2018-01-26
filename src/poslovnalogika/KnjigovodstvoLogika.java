/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import data.dao.DAOFactory;
import data.dto.DioDTO;
import data.dto.FakturaDTO;
import data.dto.RadniNalogDTO;
import data.dto.RadniNalogDioDTO;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nikol
 */
public class KnjigovodstvoLogika
{
    public static double PDV;
    static{
        try
        {
            FileInputStream fileIn =
            new FileInputStream("PDV.data");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            PDV=in.readDouble();
            in.close();
            fileIn.close();
        }
        catch (IOException i)
        {
            i.printStackTrace();
        }
    }
    public static void radniNaloziZaTabelu(ArrayList<RadniNalogDTO>lista, JTable tabela)
    {
        DefaultTableModel dtm = (DefaultTableModel)tabela.getModel();
        Object[][] sve=new Object[lista.size()][7];
        for(int i=0;i<lista.size();++i)
        {
            sve[i][0]=String.valueOf(lista.get(i).getIdRadniNalog());
                int idVozila=lista.get(i).getIdVozilo();
                int idMarkeVozila=DAOFactory.getDAOFactory().getVoziloDAO().vozilo(idVozila).getIdModelVozila().intValue();
            sve[i][1]=DAOFactory.getDAOFactory().getModelVozilaDAO().model(idMarkeVozila).getMarka()
                    +DAOFactory.getDAOFactory().getModelVozilaDAO().model(idMarkeVozila).getModel();
                int idKupca=DAOFactory.getDAOFactory().getVoziloDAO().vozilo(idVozila).getIdKupac();
            sve[i][2]=DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getNaziv()+" "
                    +DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getIme()+" "
                    +DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getPrezime();
            sve[i][3]=String.valueOf(lista.get(i).getDatumOtvaranjaNaloga());
                FakturaDTO faktura=DAOFactory.getDAOFactory().getFakturaDAO().fakturaRadniNalog(lista.get(i).getIdRadniNalog());
                sve[i][4]="Nema fakture";
            if(faktura!=null)
                sve[i][4]=String.valueOf(faktura.getDatumIzdavanja());
            sve[i][5]=String.valueOf(lista.get(i).getCijenaUsluge());
            sve[i][6]=Boolean.valueOf(DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(lista.get(i).getIdRadniNalog()).isPlaceno());
        }
        String[] nazivi={"ID","Automobil","Vlasnik","Datum otvaranja","Datum fakturisanja","Iznos","Plaćeno"};
        dtm.setDataVector(sve,nazivi);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    public static void stavkeSaNalogaZaTabelu(JTable tabela, JTable nalozi)
    {
        int row = nalozi.convertRowIndexToModel(nalozi.getSelectedRow());
        if(row<0)row=0;
        int idRadnogNaloga=Integer.parseInt((String)nalozi.getValueAt(row, 0));
        RadniNalogDTO nalog=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(idRadnogNaloga);
        DefaultTableModel dtm = (DefaultTableModel)tabela.getModel();
        ArrayList<RadniNalogDioDTO>lista=DAOFactory.getDAOFactory().getRadniNalogDioDAO().radniNalogDioIdRadniNalog(idRadnogNaloga);
        Object[][] sve=new Object[lista.size()+1][5];
        for(int i=0;i<lista.size();++i)
        {
                    int idDio=lista.get(i).getIdDio();
                sve[i][0]=String.valueOf(idDio); 
                    DioDTO dio=DAOFactory.getDAOFactory().getDioDAO().getDio(idDio);
                /*sve[i][1]=String.valueOf(
                        ""+dio.getNaziv()+" "+dio.getMarka()+" "+dio.getModel()+" "
                        +dio.getVrstaGoriva()+" "+dio.getGodisteVozila());*/
                sve[i][2]=String.valueOf(lista.get(i).getKolicina());
                sve[i][3]=String.valueOf(lista.get(i).getCijena());
                sve[i][4]=String.valueOf(lista.get(i).getCijena()*(1.0+PDV));
        }
        sve[lista.size()][0]=String.valueOf(0);
        sve[lista.size()][1]="Rad";
        sve[lista.size()][2]=" ";
        sve[lista.size()][3]=String.valueOf(nalog.getCijenaUsluge());
        sve[lista.size()][4]=String.valueOf(nalog.getCijenaUsluge()*(1.0+PDV));
        
        String[] nazivi={"ID","Naziv","Količina","Osnovica","Cijena sa PDV-om"};
        dtm.setDataVector(sve,nazivi);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
