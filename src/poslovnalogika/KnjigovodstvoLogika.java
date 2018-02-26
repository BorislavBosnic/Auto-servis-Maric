/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.forms.HomeForm1;
import com.toedter.calendar.JDateChooser;
import data.dao.DAOFactory;
import data.dto.DioDTO;
import data.dto.FakturaDTO;
import data.dto.KupacDTO;
import data.dto.RadniNalogDTO;
import data.dto.RadniNalogDioDTO;
import data.dto.VoziloDTO;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nikol
 */
public class KnjigovodstvoLogika
{
    public static double PDV;
    public static String IME_PRODAVCA="Auto Servis Marić";
    public static String ADRESA_PRODAVCA="Marka Markovića 33";
    public static String GRAD_PRODAVCA="Prnjavor";
    public static String EMAIL_PRODAVCA="maricmaric@teol.net";
    public static String NEFAKTURISAN="Nije fakturisan";
    public static String NEFAKTURISAN_DATUM="xxx";
    static{
        try
        {
            FileInputStream fileIn =
            new FileInputStream("Data.data");
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
            if("".equals(DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getNaziv()))
                sve[i][2]=DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getNaziv()+" ";
            else
                sve[i][2]=DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getIme()+" "
                    +DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getPrezime();
            sve[i][3]=String.valueOf(new SimpleDateFormat("dd.MM.yyyy.").format(lista.get(i).getDatumOtvaranjaNaloga()));
                FakturaDTO faktura=DAOFactory.getDAOFactory().getFakturaDAO().fakturaRadniNalog(lista.get(i).getIdRadniNalog());
                sve[i][4]=NEFAKTURISAN;
                sve[i][5]=NEFAKTURISAN_DATUM;
            if(faktura!=null)
            {
                sve[i][4]=String.valueOf(new SimpleDateFormat("dd.MM.yyyy.").format(faktura.getDatumIzdavanja()));
                sve[i][5]=String.valueOf(faktura.getIznos());
            }
            sve[i][6]=Boolean.valueOf(DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(lista.get(i).getIdRadniNalog()).isPlaceno());
        }
        String[] nazivi={"ID","Automobil","Vlasnik","Datum otvaranja","Datum fakturisanja","Iznos","Plaćeno"};
        dtm.setDataVector(sve,nazivi);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    public static void stavkeSaNalogaZaTabelu(JTable tabela, JTable nalozi,
            JTextField  txtBezPDV, JTextField txtPDV, JTextField txtUkupno)
    {
        int row = nalozi.convertRowIndexToModel(nalozi.getSelectedRow());
        if(row<0)row=0;
        int idRadnogNaloga=Integer.parseInt((String)nalozi.getValueAt(row, 0));
        RadniNalogDTO nalog=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(idRadnogNaloga);
        DefaultTableModel dtm = (DefaultTableModel)tabela.getModel();
        ArrayList<RadniNalogDioDTO>lista=DAOFactory.getDAOFactory().getRadniNalogDioDAO().radniNalogDioIdRadniNalog(idRadnogNaloga);
        Object[][] sve=new Object[lista.size()+1][5];
        double cijena=0;
        for(int i=0;i<lista.size();++i)
        {
                int idDio=lista.get(i).getIdDio();
                sve[i][0]=String.valueOf(idDio); 
                DioDTO dio=DAOFactory.getDAOFactory().getDioDAO().getDio(idDio);
                sve[i][1]=String.valueOf(
                        ""+dio.getNaziv()+" "+dio.getMarka()+" "+dio.getModel()+
                        " "+dio.getVrstaGoriva()+" "+dio.getGodisteVozila());
                sve[i][2]=String.valueOf(lista.get(i).getKolicina());
                sve[i][3]=String.valueOf(lista.get(i).getCijena());
                sve[i][4]=String.format("%.2f", (lista.get(i).getCijena()*(1.0+PDV)));
                
                cijena+=lista.get(i).getCijena()*lista.get(i).getKolicina();
        }
        sve[lista.size()][0]=String.valueOf(0);
        sve[lista.size()][1]="Rad";
        sve[lista.size()][2]="1";
        sve[lista.size()][3]=String.format("%.2f", nalog.getCijenaUsluge()/*+nalog.getTroskovi()*/);//vezano za troškove
        sve[lista.size()][4]=String.format("%.2f", (nalog.getCijenaUsluge()/*+nalog.getTroskovi()*/)*(1.0+PDV));//vezano za troškove
        cijena+=nalog.getCijenaUsluge()/*+nalog.getTroskovi()*/;//vezano za troškove
        
        String[] nazivi={"ID","Naziv","Količina","Osnovica","Cijena sa PDV-om"};
        dtm.setDataVector(sve,nazivi);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        txtBezPDV.setText(String.format("%.2f", cijena));
        txtPDV.setText(String.format("%.2f", cijena * PDV));
        txtUkupno.setText(String.format("%.2f", cijena * (1+PDV)));
    }
    public static void prikaziFakturu(JTable fakture, JTable stavke, String fakturaIliRacun)
    {
        int selectedRow=fakture.getSelectedRow();
        
        new Thread()
        {
            public void run()
            {
                int brojFakture=Integer.parseInt((String)(fakture.getValueAt(selectedRow,0)));
                FakturaDTO faktura=DAOFactory.getDAOFactory().getFakturaDAO().faktura(brojFakture);
                RadniNalogDTO nalog=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(faktura.getIdRadniNalog());
                VoziloDTO vozilo=DAOFactory.getDAOFactory().getVoziloDAO().vozilo(nalog.getIdVozilo());
                KupacDTO kupac=DAOFactory.getDAOFactory().getKupacDAO().kupac(vozilo.getIdKupac());
                String ime=kupac.getNaziv();
                if(ime==null)ime="";
                else ime+=" ";
                ime+=kupac.getIme();
                ime+=" "+kupac.getPrezime();
                String adresa=kupac.getAdresa();
                String grad=kupac.getGrad();
                //String email=kupac.getEmail();
                FakturaLogika.create
                (
                        stavke,
                        KnjigovodstvoLogika.PDV,
                        //DAOFactory.getDAOFactory().getFakturaDAO().getMaxID()+1, //auto ID
                        brojFakture,
                        KnjigovodstvoLogika.IME_PRODAVCA,
                        KnjigovodstvoLogika.ADRESA_PRODAVCA,
                        KnjigovodstvoLogika.GRAD_PRODAVCA,
                        KnjigovodstvoLogika.EMAIL_PRODAVCA,
                        /*(String)(fakture.getValueAt(selectedRow,2)),
                        "test",
                        "test",
                        "test",*/
                        ime,
                        adresa,
                        grad,
                        ""/*email*/,
                        fakturaIliRacun
                );
            }
        }.start();
    }
    
    public static void fakturisaniRadniNalozi(ArrayList<RadniNalogDTO>lista, JTable tabela)
    {
        ArrayList<RadniNalogDTO>filtriranaLista=new ArrayList<>();
        for(RadniNalogDTO nalog:lista)
            if(DAOFactory.getDAOFactory().getFakturaDAO().fakturaRadniNalog(nalog.getIdRadniNalog())!=null)
                filtriranaLista.add(nalog);
        radniNaloziZaTabelu(filtriranaLista, tabela);
    }
    public static void radniNaloziZaPocetnuTabelu(ArrayList<RadniNalogDTO>lista, JTable tabela)
    {
        DefaultTableModel dtm = (DefaultTableModel)tabela.getModel();
        Object[][] sve=new Object[lista.size()][6];
        for(int i=0;i<lista.size();++i)
        {
            sve[i][0]=String.valueOf(lista.get(i).getIdRadniNalog());
                int idVozila=lista.get(i).getIdVozilo();
                int idMarkeVozila=DAOFactory.getDAOFactory().getVoziloDAO().vozilo(idVozila).getIdModelVozila().intValue();
            sve[i][1]=DAOFactory.getDAOFactory().getModelVozilaDAO().model(idMarkeVozila).getMarka()
                    +DAOFactory.getDAOFactory().getModelVozilaDAO().model(idMarkeVozila).getModel();
                int idKupca=DAOFactory.getDAOFactory().getVoziloDAO().vozilo(idVozila).getIdKupac();
            if("".equals(DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getNaziv()))
                sve[i][2]=DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getNaziv()+" ";
            else
                sve[i][2]=DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getIme()+" "
                    +DAOFactory.getDAOFactory().getKupacDAO().kupac(idKupca).getPrezime();
            sve[i][3]=String.valueOf(new SimpleDateFormat("dd.MM.yyyy.").format(lista.get(i).getDatumOtvaranjaNaloga()));
                FakturaDTO faktura=DAOFactory.getDAOFactory().getFakturaDAO().fakturaRadniNalog(lista.get(i).getIdRadniNalog());
                sve[i][4]=NEFAKTURISAN;
                sve[i][5]=NEFAKTURISAN_DATUM;
            if(faktura!=null)
            {
                sve[i][4]=String.valueOf(new SimpleDateFormat("dd.MM.yyyy.").format(faktura.getDatumIzdavanja()));
                sve[i][5]=String.valueOf(faktura.getIznos());
            }
            //sve[i][6]=Boolean.valueOf(DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(lista.get(i).getIdRadniNalog()).isPlaceno());
        }
        String[] nazivi={"ID","Automobil","Vlasnik","Datum otvaranja","Datum fakturisanja","Iznos"};
        dtm.setDataVector(sve,nazivi);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    public static void neplaceneFaktureZaPocetnuStranu(ArrayList<RadniNalogDTO>lista, JTable tabela)
    {
        ArrayList<RadniNalogDTO>filtriranaLista=new ArrayList<>();
        for(RadniNalogDTO nalog:lista)
            if((!nalog.isPlaceno())&&
                    DAOFactory.getDAOFactory().getFakturaDAO().fakturaRadniNalog(nalog.getIdRadniNalog())!=null)
                filtriranaLista.add(nalog);
        radniNaloziZaPocetnuTabelu(filtriranaLista, tabela);
    }
    
    public static void pretraziNeplaceneFakturePoNazivu(HomeForm1 form, String naziv, String ime, String prezime){
        KupacDTO kupac = null;
        if(!"".equals(naziv))
        {
            ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPravni(naziv);
            if(kupci.size()>0)
                kupac = kupci.get(0);
        }
        else
            kupac = DAOFactory.getDAOFactory().getKupacDAO().kupacImePrezime(ime, prezime);
        ArrayList<VoziloDTO> vozila = null; 
        ArrayList<RadniNalogDTO> nalozi = new ArrayList<RadniNalogDTO>();
        if(kupac != null)
        {
            vozila = DAOFactory.getDAOFactory().getVoziloDAO().vozila(kupac.getIdKupac());
            ArrayList<RadniNalogDTO> pom = null;
            for(int i = 0; i < vozila.size(); i++)
            {
                pom = DAOFactory.getDAOFactory().getRadniNalogDAO().radniNaloziVozila(vozila.get(i).getIdVozilo());
                nalozi.addAll(pom);
            }
            neplaceneFaktureZaPocetnuStranu(nalozi, form.getTblNeplaceneFakture());
        }
        else
            ((DefaultTableModel)form.getTblNeplaceneFakture().getModel()).setRowCount(0);
    }
    
    public static void nefakturisaniRadniNalozi(ArrayList<RadniNalogDTO>lista, JTable tabela)
    {
        ArrayList<RadniNalogDTO>filtriranaLista=new ArrayList<>();
        for(RadniNalogDTO nalog:lista)
            if(DAOFactory.getDAOFactory().getFakturaDAO().fakturaRadniNalog(nalog.getIdRadniNalog())==null)
                filtriranaLista.add(nalog);
        radniNaloziZaTabelu(filtriranaLista, tabela);
    }
    
    public static void poIDuRadniNalozi(ArrayList<RadniNalogDTO>lista, JTable tabela, JTextField txtID)
    {
        int id=0;
        try
        {
            if("".equals(txtID.getText()))
                id=-1;
            else
                id=Integer.parseInt(txtID.getText());
        }
        catch(Exception e)
        {
            id=-1;
            JOptionPane.showMessageDialog(new JFrame(), "Greška!", "Niste izabrali radni nalog ili ste unijeli netačan ID!", JOptionPane.ERROR_MESSAGE);
        }
        ArrayList<RadniNalogDTO>filtriranaLista=new ArrayList<>();
        for(RadniNalogDTO nalog:lista)
            if(id<0 || nalog.getIdRadniNalog()==id)
                filtriranaLista.add(nalog);
        radniNaloziZaTabelu(filtriranaLista, tabela);
    }
    
    public static void poDatumuRadniNalozi(ArrayList<RadniNalogDTO>lista, JTable tabela, JDateChooser txtDatum)
    {
        Date datum=null;
        try
        {
            datum=new Date(txtDatum.getDate().getTime());
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Niste izabrali radni nalog ili datum nije uredu!", "Greška!", JOptionPane.ERROR_MESSAGE);
        }
        ArrayList<RadniNalogDTO>filtriranaLista=new ArrayList<>();
        for(RadniNalogDTO nalog:lista)
            if(datum==null || nalog.getDatumOtvaranjaNaloga().equals(datum))
                filtriranaLista.add(nalog);
        radniNaloziZaTabelu(filtriranaLista, tabela);
    }
    
    public static boolean plati(int id)
    {
        RadniNalogDTO nalog=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(id);
        if(nalog.isPlaceno())
        {
            JOptionPane.showMessageDialog(new JFrame(), "Iznos je već plaćen!", "Greška!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        nalog.setPlaceno(true);
        DAOFactory.getDAOFactory().getRadniNalogDAO().azurirajRadniNalog(nalog);
        return true;
    }
    public static boolean fakturisi(int id, JTextField txtUkupno)
    {
        RadniNalogDTO nalog=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(id);
        if(nalog.getDatumZatvaranjaNaloga()==null)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Nalog nije zatvoren!", "Greška!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(DAOFactory.getDAOFactory().getFakturaDAO().fakturaRadniNalog(id)!=null)
        {
            JOptionPane.showMessageDialog(new JFrame(), "Nalog je već fakturisan!", "Greška!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        //double iznos=0;
        //ArrayList<RadniNalogDioDTO>lista=DAOFactory.getDAOFactory().getRadniNalogDioDAO().radniNalogDioIdRadniNalog(id);
        //for(RadniNalogDioDTO item:lista)iznos+=item.getCijena()*item.getKolicina();
        FakturaDTO faktura=new FakturaDTO
        (
                DAOFactory.getDAOFactory().getFakturaDAO().getMaxID()+1,
                new Date(Calendar.getInstance().getTime().getTime()),
                id,
                Double.parseDouble(txtUkupno.getText())/*(iznos+nalog.getCijenaUsluge()+nalog.getTroskovi())*(PDV+1)*/,
                (int)(nalog.getTroskovi()/30)
        );
        DAOFactory.getDAOFactory().getFakturaDAO().dodajFakturu(faktura);
        return true;
    }
    
    public static String radniNalogIFakturaUTekst(int id)
    {
        RadniNalogDTO nalog=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(id);
        FakturaDTO faktura=DAOFactory.getDAOFactory().getFakturaDAO().fakturaRadniNalog(id);
        String opisProblema=nalog.getOpisProblema();
        String opis="";
        String[] rijeci=opisProblema.split(" ");
        int length=0;
        for(String rijec:rijeci)
        {
            length+=rijec.length();
            if(length>20)
            {
                opis+="\n        ";
                length=0;
            }
            opis+=rijec+" ";
        }
        
        String rez="ID naloga: "+nalog.getIdRadniNalog()
                +"\nOpis problema: "+opis
                +"\nDatum otvaranja naloga: "+new SimpleDateFormat("dd.MM.yyyy.").format(nalog.getDatumOtvaranjaNaloga())
                +"\nDatum zatvaranja naloga: "+new SimpleDateFormat("dd.MM.yyyy.").format(nalog.getDatumZatvaranjaNaloga());
        if(faktura!=null)
            rez+="\nFakturisano:"
                +"\nID fakture: "+faktura.getIdFaktura()
                +"\nDatum izdavanja: "+new SimpleDateFormat("dd.MM.yyyy.").format(faktura.getDatumIzdavanja()).toString()
                +"\nVrijeme rada: "+faktura.getVrijemeRada()
                +"\nIznos: "+faktura.getIznos();
        return rez;
    }
    
    public static void main(String[] args)
    {
        try
        {
            FileOutputStream fileOut =
            new FileOutputStream("Data.data");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeDouble((double)0.17);
            out.close();
            fileOut.close();
        }
        catch (IOException i)
        {
            i.printStackTrace();
        }
    }
}
