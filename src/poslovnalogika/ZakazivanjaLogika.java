/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import data.dao.DAOFactory;
import data.dto.TerminDTO;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nikol
 */
public class ZakazivanjaLogika
{
    public static void terminiZaTabelu(ArrayList<TerminDTO>lista, JTable tabela)
    {
        DefaultTableModel dtm = (DefaultTableModel)tabela.getModel();
        Object[][] sve=new Object[lista.size()][8];
        for(int i=0;i<lista.size();++i)
        {
            sve[i][0]=String.valueOf(new SimpleDateFormat("yyyy.MM.dd").format(lista.get(i).getDatum()));
            sve[i][1]=String.valueOf(lista.get(i).getVrijeme());
            sve[i][2]=String.valueOf(new SimpleDateFormat("yyyy.MM.dd").format(lista.get(i).getDatumZakazivanja()));
            sve[i][3]=String.valueOf(lista.get(i).getMarka());
            sve[i][4]=String.valueOf(lista.get(i).getModel());
            sve[i][5]=String.valueOf(lista.get(i).getIme());
            sve[i][6]=String.valueOf(lista.get(i).getPrezime());
            sve[i][7]=String.valueOf(lista.get(i).getBrojTelefona());
        }
        String[] nazivi={"Datum","Vrijeme","Datum zakazivanja","Marka","Model","Ime","Prezime","Broj telefona"};
        dtm.setDataVector(sve,nazivi);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public static void filtrirajTermine(ArrayList<TerminDTO>lista, JTable tabela,
                                        String marka, Date datum, String ime,
                                        String prezime, String brojTelefona)
    {
        ArrayList<TerminDTO>filtriranaLista=new ArrayList<>();
        for(TerminDTO nalog:lista)
            if("".equals(marka) || nalog.getMarka().equals(marka))
                if(datum==null || (nalog.getDatum().toString().compareTo(datum.toString()))==0)
                    if("".equals(ime) || nalog.getIme().equals(ime))
                        if("".equals(prezime) || nalog.getPrezime().equals(prezime))
                            if("".equals(brojTelefona) || nalog.getBrojTelefona().equals(brojTelefona))
                                filtriranaLista.add(nalog);
        terminiZaTabelu(filtriranaLista, tabela);
    }
    
    public static boolean dodajTermin
    (
        Date datum,
        String sati,
        String minuti,
        String marka,
        String model,
        String ime,
        String prezime,
        String brojTelefona
    )
    {
        TerminDTO termin=new TerminDTO
        (
            0,
            datum,
            new Time(Integer.parseInt(sati),Integer.parseInt(minuti),0),
            marka,
            model,
            ime,
            prezime,
            brojTelefona,
            new Date(Calendar.getInstance().getTime().getTime())
        );
        return DAOFactory.getDAOFactory().getTerminDAO().dodajTermin(termin);
    }
    
    public static boolean obrisiTermin(String datum, String vrijeme)
    {
        Date datumSQL=new Date
        (
                /*Integer.parseInt(datum.split("-")[0])-1900,
                Integer.parseInt(datum.split("-")[1])-1,
                Integer.parseInt(datum.split("-")[2])*/
                Integer.parseInt(datum.split("\\.")[0])-1900,
                Integer.parseInt(datum.split("\\.")[1])-1,
                Integer.parseInt(datum.split("\\.")[2])
        );
        Time vrijemeSQL=new Time
        (
                Integer.parseInt(vrijeme.split(":")[0]),
                Integer.parseInt(vrijeme.split(":")[1]),
                Integer.parseInt(vrijeme.split(":")[2])
        );
        return DAOFactory.getDAOFactory().getTerminDAO().obrisiTerminDatumVrijeme(datumSQL, vrijemeSQL);
    }
}
