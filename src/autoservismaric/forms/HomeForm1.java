/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoservismaric.forms;

import autoservismaric.dialog.*;
import autoservismaric.dialog.DodajModel;
import autoservismaric.dialog.DodajRadniNalogDialog;
import autoservismaric.dialog.DodajVlasnikaDialog;
import autoservismaric.dialog.DodajVoziloDialog;
import autoservismaric.dialog.IzmijeniIzbrisiModelDialog;
import autoservismaric.dialog.IzmijeniVlasnikaDialog;
import autoservismaric.dialog.IzmijeniVoziloDialog;
import autoservismaric.dialog.OtpustiRadnikaDialog;
import autoservismaric.dialog.PregledIstorijePopravkiDialog;
import autoservismaric.dialog.SviBivsiZaposleniDialog;
import data.AutoSuggestor;
import data.dao.*;
import data.dto.DioDTO;
import data.dto.DioModelVozilaDTO;
import data.dto.KupacDTO;
import data.dto.ModelVozilaDTO;
import data.dto.RadniNalogDTO;
import data.dto.TerminDTO;
import data.dto.VoziloDTO;
import data.dto.ZaposleniDTO;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Objects;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static poslovnalogika.KnjigovodstvoLogika.fakturisaniRadniNalozi;
import static poslovnalogika.KnjigovodstvoLogika.fakturisi;
import static poslovnalogika.KnjigovodstvoLogika.nefakturisaniRadniNalozi;
import static poslovnalogika.KnjigovodstvoLogika.neplaceneFaktureZaPocetnuStranu;
import static poslovnalogika.KnjigovodstvoLogika.plati;
import static poslovnalogika.KnjigovodstvoLogika.poDatumuRadniNalozi;
import static poslovnalogika.KnjigovodstvoLogika.poIDuRadniNalozi;
import static poslovnalogika.ZakazivanjaLogika.filtrirajTermine;
import static poslovnalogika.ZakazivanjaLogika.obrisiTermin;   

import static poslovnalogika.KnjigovodstvoLogika.prikaziFakturu;
import static poslovnalogika.KnjigovodstvoLogika.radniNalogIFakturaUTekst;
import static poslovnalogika.KnjigovodstvoLogika.radniNaloziZaTabelu;
import static poslovnalogika.KnjigovodstvoLogika.stavkeSaNalogaZaTabelu;
import poslovnalogika.VozilaLogika;
import poslovnalogika.ZaposleniLogika;
import poslovnalogika.StatistikaLogika;
import static poslovnalogika.ZakazivanjaLogika.dodajTermin;
import static poslovnalogika.ZakazivanjaLogika.terminiZaTabelu;

/**
 *
 * @author HP BOOK
 */

public class HomeForm1 extends javax.swing.JFrame {
    /**Karpin kod**/
    private static HomeForm1 homeForm;
    private void inicijalisuciKod()
    {
        homeForm=this;
        uslugeKnjigovodstva();
        uslugeZakazivanja(); 
        uslugePocetneStrane();
    }
    private void uslugeKnjigovodstva()
    {
        inicijalizacijaTabelaKnjigovodstva();
        brisanjeStarihVrijednostiPoljaKnjigovodstva();
    }
    private void uslugeZakazivanja()
    {
        inicijalizacijaTabelaZakazivanja();
    }
    private void uslugePocetneStrane()
    {
        inicijalizacijaTabeleNeplacenihFaktura();
    }
    private void inicijalizacijaTabelaKnjigovodstva()
    {
        ArrayList<RadniNalogDTO> lista=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        radniNaloziZaTabelu(lista,tblRadniNalozi);
        stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
        if(tblRadniNalozi.getRowCount()>0)tblRadniNalozi.setRowSelectionInterval(0, 0);
        if(tblFaktura.getRowCount()>0)tblFaktura.setRowSelectionInterval(0, 0);
        provjeraZaDugmiceZaTabeluNaloga();
        dodajIskacuciMeniUTabeluRadnihNaloga();
    }
    private void inicijalizacijaTabelaZakazivanja()
    {
        ArrayList<TerminDTO> lista=DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, tblTermini);
        if(tblTermini.getRowCount()>0)tblTermini.setRowSelectionInterval(0, 0);
        dodajIskacuciMeniUTabeluTermina();
    }
    private void inicijalizacijaTabeleNeplacenihFaktura()
    {
        ArrayList<RadniNalogDTO> lista=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        neplaceneFaktureZaPocetnuStranu(lista,tblNeplaceneFakture);
        if(tblNeplaceneFakture.getRowCount()>0)tblNeplaceneFakture.setRowSelectionInterval(0, 0);
        dodajIskacuciMeniUTabeluNeplacenihFaktura();
    }
    private void provjeraZaDugmiceZaTabeluNaloga()
    {
        if(tblRadniNalozi.getRowCount()>0 &&
                !("Nema fakture".equals((String)(
                        tblRadniNalozi.getValueAt(tblRadniNalozi.getSelectedRow(), 4)))))
        {
            btnRacun.setEnabled(true);
            btnPredracun.setEnabled(false);
        }
        else
        {
            btnRacun.setEnabled(false);
            btnPredracun.setEnabled(true);
        }
    }
    private void brisanjeStarihVrijednostiPoljaKnjigovodstva()
    {
        txtID.setText("");
        dtmDatum.setDate(null);
    }
    private static int opcija=-1;
    private void dodajIskacuciMeniUTabeluRadnihNaloga()
    {
        opcija=-1;
        popupMenu = new JPopupMenu();
        JMenuItem detaljnoItem = new JMenuItem("Detaljno");
        JMenuItem placenoItem = new JMenuItem("Plaćeno");
        JMenuItem fakturisiItem = new JMenuItem("Fakturiši");
        
        detaljnoItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(new JFrame(), radniNalogIFakturaUTekst
                    (Integer.parseInt((String)tblRadniNalozi.getValueAt(selektovanRed, 0))));
                stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
                provjeraZaDugmiceZaTabeluNaloga();
            }
        });
        popupMenu.add(detaljnoItem);

        placenoItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                int izbor=JOptionPane.showOptionDialog
                (
                    new JFrame(),
                    "Da li ste sigurni da je placeno?",
                    "Da li ste sigurni?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"DA","NE"},
                    "NE"
                );
                if(izbor==JOptionPane.YES_OPTION)
                    plati(Integer.parseInt((String)tblRadniNalozi.getValueAt(selektovanRed, 0)));
                stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
                provjeraZaDugmiceZaTabeluNaloga();
            }
        });
        popupMenu.add(placenoItem);

        fakturisiItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                int izbor=JOptionPane.showOptionDialog
                (
                    new JFrame(),
                    "Da li ste sigurni da želite fakturisati?",
                    "Da li ste sigurni?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"DA","NE"},
                    "NE"
                );
                if(izbor==JOptionPane.YES_OPTION)
                    fakturisi(Integer.parseInt((String)tblRadniNalozi.getValueAt(selektovanRed, 0)));
                stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
                provjeraZaDugmiceZaTabeluNaloga();
            }
        });
        popupMenu.add(fakturisiItem);
        
        tblRadniNalozi.setComponentPopupMenu(popupMenu);

        popupMenu.addPopupMenuListener(new PopupMenuListener()
        {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int rowAtPoint = tblRadniNalozi.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), tblRadniNalozi));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = tblRadniNalozi.getModel().getColumnName(0);

                        if (selektovanRed >= 0)
                        {
                            
                        }
                        if (rowAtPoint > -1)
                        {
                            tblRadniNalozi.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                        stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
                        provjeraZaDugmiceZaTabeluNaloga();
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
    
    private void dodajIskacuciMeniUTabeluTermina()
    {
        opcija=-1;
        popupMenu = new JPopupMenu();
        JMenuItem obrisiItem = new JMenuItem("Obriši");
        
        obrisiItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if
                (
                    obrisiTermin
                    (
                        (String)tblTermini.getValueAt(selektovanRed, 0),
                        (String)tblTermini.getValueAt(selektovanRed, 1)
                    )
                )
                {
                    JOptionPane.showMessageDialog(new JFrame(), "Termin je uklonjen.");
                    ArrayList<TerminDTO> lista=DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
                    terminiZaTabelu(lista, tblTermini);
                }
                else
                    JOptionPane.showMessageDialog(new JFrame(), "Termin nije uklonjen.");
            }
        });
        popupMenu.add(obrisiItem);
        
        tblTermini.setComponentPopupMenu(popupMenu);

        popupMenu.addPopupMenuListener(new PopupMenuListener()
        {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int rowAtPoint = tblTermini.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), tblTermini));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = tblTermini.getModel().getColumnName(0);

                        if (selektovanRed >= 0)
                        {
                            
                        }
                        if (rowAtPoint > -1)
                        {
                            tblTermini.setRowSelectionInterval(rowAtPoint, rowAtPoint);
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
    private void dodajIskacuciMeniUTabeluNeplacenihFaktura()
    {
        opcija=-1;
        popupMenu = new JPopupMenu();
        JMenuItem detaljnoItem = new JMenuItem("Detaljno");
        JMenuItem placenoItem = new JMenuItem("Plaćeno");
        JMenuItem fakturisiItem = new JMenuItem("Fakturiši");
        
        detaljnoItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(new JFrame(), radniNalogIFakturaUTekst
                    (Integer.parseInt((String)tblNeplaceneFakture.getValueAt(selektovanRed, 0))));
            }
        });
        popupMenu.add(detaljnoItem);

        placenoItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                int izbor=JOptionPane.showOptionDialog
                (
                    new JFrame(),
                    "Da li ste sigurni da je placeno?",
                    "Da li ste sigurni?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"DA","NE"},
                    "NE"
                );
                if(izbor==JOptionPane.YES_OPTION)
                    plati(Integer.parseInt((String)tblNeplaceneFakture.getValueAt(selektovanRed, 0)));
            }
        });
        popupMenu.add(placenoItem);

        fakturisiItem.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                int izbor=JOptionPane.showOptionDialog
                (
                    new JFrame(),
                    "Da li ste sigurni da želite fakturisati?",
                    "Da li ste sigurni?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"DA","NE"},
                    "NE"
                );
                if(izbor==JOptionPane.YES_OPTION)
                    fakturisi(Integer.parseInt((String)tblNeplaceneFakture.getValueAt(selektovanRed, 0)));
            }
        });
        popupMenu.add(fakturisiItem);
        
        tblNeplaceneFakture.setComponentPopupMenu(popupMenu);

        popupMenu.addPopupMenuListener(new PopupMenuListener()
        {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int rowAtPoint = tblNeplaceneFakture.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), tblNeplaceneFakture));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = tblNeplaceneFakture.getModel().getColumnName(0);

                        if (selektovanRed >= 0)
                        {
                            
                        }
                        if (rowAtPoint > -1)
                        {
                            tblNeplaceneFakture.setRowSelectionInterval(rowAtPoint, rowAtPoint);
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
    /*****Kraj*****/
    
    
    
    
    //Sve moje, ne diraj!
    ButtonGroup bGTraziVozilo;
    ButtonGroup bGTraziVlasnika;
    AutoSuggestor modelAU, markeAU, vlasnikAU, registracijeAU, pravniNazivAU;
    AutoSuggestor autoModel = null, autoMarka = null, autoNaziv = null, autoSifra = null;
    static boolean voziloPanelPrviPut = true; ///flag koji se koristi za ucitavanje za suggestor u vozilo panelu
    JPopupMenu popupMenu = new JPopupMenu(); // za popup u tabeli
    JPopupMenu popupMenuVlasnik = new JPopupMenu();
    JPopupMenu popupDio = new JPopupMenu();
    public static int selRedDio;// ua popup dio
    public static Double pdv = 17.0;
    public int idVlasnika = -1;
    public static int selektovanRed; //za popup
    public static int idVozila = -1; //za popup

    public void izbrisiPopupZaVozila() {
        popupMenu.removeAll();
        tableVozila.setComponentPopupMenu(popupMenu);
    }

    public void izbrisiPopupZaVlasnike() {
        popupMenuVlasnik.removeAll();
        tableVozila.setComponentPopupMenu(popupMenuVlasnik);
    }

    public void ucitajPopupZaVlasnike() {

        popupMenuVlasnik = new JPopupMenu();
        JMenuItem izmijeniVlasnika = new JMenuItem("Izmijeni vlasnika");

        izmijeniVlasnika.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                IzmijeniVlasnikaDialog iv = new IzmijeniVlasnikaDialog(new JFrame(), true, idVlasnika);
                
                iv.setVisible(true);
            }
        });
        popupMenuVlasnik.add(izmijeniVlasnika);
        tableVozila.setComponentPopupMenu(popupMenuVlasnik);

        popupMenuVlasnik.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = tableVozila.rowAtPoint(SwingUtilities.convertPoint(popupMenuVlasnik, new Point(0, 0), tableVozila));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = tableVozila.getModel().getColumnName(0);

                        if (selektovanRed >= 0) {

                            if ("ID".equals(imeKolone)) {
                                idVlasnika = Integer.parseInt(tableVozila.getModel().getValueAt(selektovanRed, column).toString());
                            }
                        }
                        if (rowAtPoint > -1) {
                            tableVozila.setRowSelectionInterval(rowAtPoint, rowAtPoint);
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
    public void ucitajPopupZaDijelove(){
        JMenuItem prodaj = new JMenuItem("Prodaj");
        prodaj.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new ProdajDioDialog((DefaultTableModel)jTable.getModel(), jTable.getSelectedRow(), pdv).setVisible(true);
            }
        });
        popupDio.add(prodaj);
        
        JMenuItem azuriraj = new JMenuItem("Ažuriraj");
        azuriraj.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new IzmijeniDioDialog((DefaultTableModel)jTable.getModel(), jTable.getSelectedRow()).setVisible(true);
            }
        });
        popupDio.add(azuriraj);
        
        JMenuItem obrisi = new JMenuItem("Obriši");
        obrisi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel dtm = (DefaultTableModel)jTable.getModel();
                //int id = 
                int id = (Integer)((DefaultTableModel)jTable.getModel()).getValueAt(jTable.getSelectedRow(), 0);
                System.out.println("idddddddd  " + id);
                if(DAOFactory.getDAOFactory().getDioModelVozilaDAO().obrisiDioModelVozila(id) &&
                DAOFactory.getDAOFactory().getDioDAO().obrisiDio(id)){
                    dtm.removeRow(jTable.getSelectedRow());
                    JOptionPane jop = new JOptionPane();
                        jop.showMessageDialog(new JFrame(), "Uspješno obrisan dio", "Obavještenje",
                                JOptionPane.INFORMATION_MESSAGE);                        
                }
            }
        });
        popupDio.add(obrisi);
        jTable.setComponentPopupMenu(popupDio);
        
        /*jTable.addMouseListener(new MouseAdapter (){
            public void MouseClicked(MouseEvent me){
                if(SwingUtilities.isRightMouseButton(me))
                    popupDio.show(me.getComponent(), me.getX(), me.getY());
            }
         });*/
    
        
        popupDio.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = jTable.rowAtPoint(SwingUtilities.convertPoint(popupDio, new Point(0, 0), jTable));
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
    
    public void ucitajPopupZaVozila() {
        popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Izbrisi vozilo");
        JMenuItem editItem = new JMenuItem("Izmijeni vozilo");
        JMenuItem noviRadniNalog = new JMenuItem("Dodaj nov radni nalog");
        JMenuItem pogledajIstorijuPopravki = new JMenuItem("Pogledaj istoriju popravki");

        pogledajIstorijuPopravki.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new PregledIstorijePopravkiDialog(new JFrame(), true, idVozila).setVisible(true);
            }
        });
        popupMenu.add(pogledajIstorijuPopravki);

        noviRadniNalog.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new DodajRadniNalogDialog(new JFrame(), true, idVozila).setVisible(true);
            }
        });
        popupMenu.add(noviRadniNalog);

        editItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new IzmijeniVoziloDialog(new JFrame(), true, idVozila).setVisible(true);
            }
        });
        popupMenu.add(editItem);

        deleteItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JFrame(), "Right-click performed on table and choose DELETE");
            }
        });

        popupMenu.add(deleteItem);
        tableVozila.setComponentPopupMenu(popupMenu);

        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = tableVozila.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), tableVozila));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = tableVozila.getModel().getColumnName(0);

                        if (selektovanRed >= 0) {
                            if ("ID".equals(imeKolone)) {
                                idVozila = Integer.parseInt(tableVozila.getModel().getValueAt(selektovanRed, column).toString());
                            }
                        }
                        if (rowAtPoint > -1) {
                            tableVozila.setRowSelectionInterval(rowAtPoint, rowAtPoint);
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

    static int numberOfItems = 8;
    public static String[] mjeseci = {"Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar"};
    public static boolean menu[] = new boolean[numberOfItems];
    public StatistikaLogika statistikaLogika;
    
    // public static JPanel employeePanel=new EmployeesForm().getPanel();
    // public static JPanel partsPanel=new PartsForm().getPanel();

    /**
     * Creates new form HomeForm1
     */
    public HomeForm1() {
        initComponents();
        statistikaLogika = new StatistikaLogika();
        
        jTabbedPane1.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            System.out.println("Tab: " + jTabbedPane1.getSelectedIndex());
            if(jTabbedPane1.getSelectedIndex() == 2 || jTabbedPane1.getSelectedIndex() == 3){
                odabirMjesecaStatistikaPanel.setVisible(false);
            } else {
               odabirMjesecaStatistikaPanel.setVisible(true);

            }
        }
    });
        /*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        pack();
        setSize(screenSize.width,screenSize.height);*/

        //tableVozila.addMouseListener(new PopClickListener());
        ucitajPopupZaVozila();
        ucitajPopupZaDijelove();
        

        //za autosuggestor zovila
//        VozilaLogika vl1 = new VozilaLogika(VozilaLogika.UCITAJ_MODELE);
//        VozilaLogika vl2 = new VozilaLogika(VozilaLogika.UCITAJ_VOZILA);
//        VozilaLogika vl3 = new VozilaLogika(VozilaLogika.UCITAJ_VLASNIKE);
        tfNazivTrazi.setBackground(Color.gray);
        tfNazivVozilo.setBackground(Color.gray);
        tfNazivVozilo.setEditable(false);
        tfNazivTrazi.setEditable(false);

        bGTraziVlasnika = new ButtonGroup();
        bGTraziVlasnika.add(rbPrivatnoTrazi);
        bGTraziVlasnika.add(rbPravnoTrazi);

        bGTraziVozilo = new ButtonGroup();
        bGTraziVozilo.add(rbPravnoLiceVozilo);
        bGTraziVozilo.add(rbPrivatnoLiceVozilo);

        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setIconImage(new ImageIcon(getClass().getResource("/autoservismaric/images/ikona2.png")).getImage());
        datumLabel.setText("Aktivnosti planirane za danas, " + new SimpleDateFormat("dd.MM.yyyy.").format(Calendar.getInstance().getTime()));

        inicijalizujZaposleniPanel();
        
        /**Karpin kod**/
        inicijalisuciKod();
        /*****Kraj*****/
    }

    public AutoSuggestor ucitajPreporukeMarke() {

        AutoSuggestor autoSuggestorMarke = new AutoSuggestor(tfMarkaTrazi, this, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> modeli = new ArrayList<>();

                for (ModelVozilaDTO mv : VozilaLogika.modeli) {
                    modeli.add(mv.getMarka());
                }

                setDictionary(modeli);
                //addToDictionary("bye");//adds a single word

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };
        return autoSuggestorMarke;
    }

    public AutoSuggestor ucitajPreporukeModel() {

        AutoSuggestor autoSuggestorModel = new AutoSuggestor(tfModelTrazi, this, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                String marka = tfMarkaTrazi.getText();

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> modeli = new ArrayList<>();

                for (ModelVozilaDTO mv : VozilaLogika.modeli) {
                    if (tfModelTrazi.getText() != null && !"".equals(tfModelTrazi.getText())) {
                        if (mv.getMarka().equals(marka.trim())) {
                            modeli.add(mv.getModel());
                        }
                    } else if (tfModelTrazi.getText() == null || "".equals(tfModelTrazi.getText())) {
                        modeli.add(mv.getModel());
                    }
                }

                setDictionary(modeli);
                //addToDictionary("bye");//adds a single word

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };
        return autoSuggestorModel;
    }

    public AutoSuggestor ucitajPreporukeRegistracija() {

        AutoSuggestor autoSuggestorRegistracija = new AutoSuggestor(tfRegistracijaTrazi, this, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> registracije = new ArrayList<>();

                for (VoziloDTO v : VozilaLogika.vozila) {
                    registracije.add(v.getBrojRegistracije());
                }

                setDictionary(registracije);
                //addToDictionary("bye");//adds a single word

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };

        return autoSuggestorRegistracija;
    }

    public AutoSuggestor ucitajPreporukeVlasnik() {

        AutoSuggestor autoSuggestorVlasnik = new AutoSuggestor(tfPrezimeVozilo, this, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> vlasnici = new ArrayList<>();

                for (KupacDTO v : VozilaLogika.vlasnici) {
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

    public AutoSuggestor ucitajPreporukePravniNaziv() {

        AutoSuggestor autoSuggestorPravniNaziv = new AutoSuggestor(tfNazivVozilo, this, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> vlasnici = new ArrayList<>();

                for (KupacDTO v : VozilaLogika.vlasnici) {
                    if (v.getPrezime() == null && v.getIme() == null) {
                        vlasnici.add(v.getNaziv());
                    }
                }

                setDictionary(vlasnici);
                //addToDictionary("bye");//adds a single word

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };

        return autoSuggestorPravniNaziv;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel59 = new javax.swing.JLabel();
        popupMenuZaposleni = new javax.swing.JPopupMenu();
        menuItemDetaljniOpis = new javax.swing.JMenuItem();
        menuItemIzmjeniRadnika = new javax.swing.JMenuItem();
        menuItemOtpustiRadnika = new javax.swing.JMenuItem();
        menuPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        menu2jPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        menu3jPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        menu4jPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        menu7jPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        menu5jPanel = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        menu6jPanel = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        menu1jPanel = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        menu8jPanel = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        parentPanel = new javax.swing.JPanel();
        pocetnajPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        jSeparator24 = new javax.swing.JSeparator();
        jSeparator25 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        danasnjeAktivnostiPanel = new javax.swing.JPanel();
        datumLabel = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        Napomene2 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblNeplaceneFakture = new javax.swing.JTable();
        radniNaloziPanel = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        zakazivanjaPanel = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel128 = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel144 = new javax.swing.JLabel();
        jLabel145 = new javax.swing.JLabel();
        jLabel143 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        btnPronadjiTermin = new javax.swing.JButton();
        txtMarkaPretraga = new javax.swing.JTextField();
        txtImePretraga = new javax.swing.JTextField();
        txtPrezimePretraga = new javax.swing.JTextField();
        txtBrojTelefonaPretraga = new javax.swing.JTextField();
        dtmDatumPretraga = new com.toedter.calendar.JDateChooser();
        btnPonistiUnosePretraga = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel131 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jLabel140 = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        btnDodajTermin = new javax.swing.JButton();
        jLabel130 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        txtMarkaTermina = new javax.swing.JTextField();
        txtModelTermina = new javax.swing.JTextField();
        txtImeTermina = new javax.swing.JTextField();
        txtPrezimeTermina = new javax.swing.JTextField();
        txtBrojTelefonaTermina = new javax.swing.JTextField();
        dtmDatumTermina = new com.toedter.calendar.JDateChooser();
        txtVrijemeTerminaSati = new javax.swing.JSpinner();
        txtVrijemeTerminaMinuti = new javax.swing.JSpinner();
        jLabel156 = new javax.swing.JLabel();
        jLabel157 = new javax.swing.JLabel();
        btnPonistiUnoseTermin = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblTermini = new javax.swing.JTable();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        vozilaPanel = new javax.swing.JPanel();
        panelAkcijeNaFormi = new javax.swing.JPanel();
        jLabel92 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        panelVozilo = new javax.swing.JPanel();
        panelPronadjiVozilo = new javax.swing.JPanel();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        tfRegistracijaTrazi = new javax.swing.JTextField();
        tfGodisteTrazi = new javax.swing.JTextField();
        tfModelTrazi = new javax.swing.JTextField();
        tfPrezimeVozilo = new javax.swing.JTextField();
        btnPronadji = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        tfMarkaTrazi = new javax.swing.JTextField();
        tfNazivVozilo = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        btnPonistiSve = new javax.swing.JButton();
        tfImeVozilo = new javax.swing.JTextField();
        rbPrivatnoLiceVozilo = new javax.swing.JRadioButton();
        rbPravnoLiceVozilo = new javax.swing.JRadioButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        cbSvi = new javax.swing.JCheckBox();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        panelPronadjiVlasnika = new javax.swing.JPanel();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        tfImeTrazi = new javax.swing.JTextField();
        tfPrezimeTrazi = new javax.swing.JTextField();
        btnTrazi = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        tfNazivTrazi = new javax.swing.JTextField();
        rbPrivatnoTrazi = new javax.swing.JRadioButton();
        rbPravnoTrazi = new javax.swing.JRadioButton();
        btnPrikaziSve = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        btnPonisti = new javax.swing.JButton();
        labelPoruka = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        spVoziloPretraga = new javax.swing.JScrollPane();
        tableVozila = new javax.swing.JTable();
        dijeloviPanel = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        pretraziPanel = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        cbMarka = new javax.swing.JComboBox<>();
        jLabel53 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        cbNovo = new javax.swing.JCheckBox();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        cbModel = new javax.swing.JComboBox<>();
        jLabel77 = new javax.swing.JLabel();
        cbGorivo = new javax.swing.JComboBox<>();
        btnPretrazi = new javax.swing.JButton();
        btnSviDijelovi = new javax.swing.JButton();
        btnProdani = new javax.swing.JButton();
        tfId = new javax.swing.JTextField();
        tfSifra = new javax.swing.JTextField();
        tfNaziv = new javax.swing.JTextField();
        tfGodiste = new javax.swing.JTextField();
        dodajDioPanel = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        cbStanje = new javax.swing.JCheckBox();
        jLabel60 = new javax.swing.JLabel();
        jcbGorivo = new javax.swing.JComboBox<>();
        jLabel61 = new javax.swing.JLabel();
        jcbMarka = new javax.swing.JComboBox<>();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jcbModel = new javax.swing.JComboBox<>();
        jLabel64 = new javax.swing.JLabel();
        btnDodaj = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jtfSifra = new javax.swing.JTextField();
        jtfNaziv = new javax.swing.JTextField();
        jtfCijena = new javax.swing.JTextField();
        jtfGodiste = new javax.swing.JTextField();
        jtfKolicina = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        knjigovodstvoPanel = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel120 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btnPredracun = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblRadniNalozi = new javax.swing.JTable();
        jLabel161 = new javax.swing.JLabel();
        txtUkupno = new javax.swing.JTextField();
        jLabel162 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblFaktura = new javax.swing.JTable();
        btnRacun = new javax.swing.JButton();
        btnPoIDu = new javax.swing.JButton();
        btnPoDatumu = new javax.swing.JButton();
        txtBezPDV = new javax.swing.JTextField();
        txtPDV = new javax.swing.JTextField();
        jLabel163 = new javax.swing.JLabel();
        jLabel164 = new javax.swing.JLabel();
        jLabel165 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        btnNefakturisano = new javax.swing.JButton();
        btnFakturisano = new javax.swing.JButton();
        dtmDatum = new com.toedter.calendar.JDateChooser();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        zaposleniPanel = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel117 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTableZaposleni = new javax.swing.JTable();
        labelBrojRadnihNaloga = new javax.swing.JLabel();
        jSeparator26 = new javax.swing.JSeparator();
        jLabel122 = new javax.swing.JLabel();
        buttonTraziRadneNalogeRadnika = new javax.swing.JButton();
        labelOstvareniProfitRadnika = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel124 = new javax.swing.JLabel();
        jLabel129 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel146 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        textFieldIme = new javax.swing.JTextField();
        textFieldPrezime = new javax.swing.JTextField();
        textFieldTelefon = new javax.swing.JTextField();
        textFieldBrojLicneKarte = new javax.swing.JTextField();
        buttonDodajZaposlenog = new javax.swing.JButton();
        jLabel150 = new javax.swing.JLabel();
        textFieldImeOca = new javax.swing.JTextField();
        jLabel151 = new javax.swing.JLabel();
        jLabel152 = new javax.swing.JLabel();
        textFieldAdresa = new javax.swing.JTextField();
        jLabel153 = new javax.swing.JLabel();
        textFieldStrucnaSprema = new javax.swing.JTextField();
        dateChooserDatumRodjenja = new com.toedter.calendar.JDateChooser();
        jLabel154 = new javax.swing.JLabel();
        dateChooserDatumPrimanjaURadniOdnos = new com.toedter.calendar.JDateChooser();
        jLabel155 = new javax.swing.JLabel();
        textFieldFunkcijaRadnika = new javax.swing.JTextField();
        dateChooserDatumOdZaposlenog = new com.toedter.calendar.JDateChooser();
        dateChooserDatumDoZaposlenog = new com.toedter.calendar.JDateChooser();
        buttonPrikazSvihBivsihRadnika = new javax.swing.JButton();
        jLabel148 = new javax.swing.JLabel();
        jLabel149 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tableRadniNalozi = new javax.swing.JTable();
        statistikajPanel = new javax.swing.JPanel();
        statistikaHeaderjPanel = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        labelIntervalZarada = new javax.swing.JLabel();
        labelGodisnjaZarada = new javax.swing.JLabel();
        labelMjesecnaZarada = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        labelDnevnaZarada = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        labelPopravkeDanas = new javax.swing.JLabel();
        labelPopravkeGodina = new javax.swing.JLabel();
        labelPopravkeMjesec = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        labelPopravkeInterval = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        buttonPregled = new javax.swing.JButton();
        jDateChooserDatumOd = new com.toedter.calendar.JDateChooser();
        jDateChooserDatumDo = new com.toedter.calendar.JDateChooser();
        jLabel31 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        labelDnevnaZaradaDijelovi = new javax.swing.JLabel();
        labelGodisnjaZaradaDijelovi = new javax.swing.JLabel();
        labelMjesecnaZaradaDijelovi = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        labelIntervalZaradaDijelovi = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelGrafikPrihodiUkupno = new javax.swing.JPanel();
        PanelGrafikPrihodiDijelovi = new javax.swing.JPanel();
        panelGrafikAuta = new javax.swing.JPanel();
        panelGrafikFakture = new javax.swing.JPanel();
        panelGrafikPopravke = new javax.swing.JPanel();
        odabirMjesecaStatistikaPanel = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        comboBoxMjesec = new javax.swing.JComboBox<>();
        comboBoxGodina = new javax.swing.JComboBox<>();
        buttonPregledGrafik = new javax.swing.JButton();

        jLabel59.setText("jLabel59");

        menuItemDetaljniOpis.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        menuItemDetaljniOpis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/document.png"))); // NOI18N
        menuItemDetaljniOpis.setText("Detaljan opis");
        menuItemDetaljniOpis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDetaljniOpisActionPerformed(evt);
            }
        });
        popupMenuZaposleni.add(menuItemDetaljniOpis);

        menuItemIzmjeniRadnika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Settings_32px_3.png"))); // NOI18N
        menuItemIzmjeniRadnika.setText("Izmjeni radnika");
        menuItemIzmjeniRadnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemIzmjeniRadnikaActionPerformed(evt);
            }
        });
        popupMenuZaposleni.add(menuItemIzmjeniRadnika);

        menuItemOtpustiRadnika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/icon.png"))); // NOI18N
        menuItemOtpustiRadnika.setText("Otpusti radnika");
        menuItemOtpustiRadnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemOtpustiRadnikaActionPerformed(evt);
            }
        });
        popupMenuZaposleni.add(menuItemOtpustiRadnika);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AUTO-SERVIS");

        menuPanel.setBackground(new java.awt.Color(51, 51, 255));
        menuPanel.setMaximumSize(new java.awt.Dimension(266, 800));
        menuPanel.setLayout(null);
        menuPanel.add(jLabel1);
        jLabel1.setBounds(10, 408, 0, 33);

        menu2jPanel.setBackground(new java.awt.Color(51, 51, 255));
        menu2jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu2jPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu2jPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu2jPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menu2jPanelMousePressed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(51, 51, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Maintenance_32px_3.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Radni nalozi");

        javax.swing.GroupLayout menu2jPanelLayout = new javax.swing.GroupLayout(menu2jPanel);
        menu2jPanel.setLayout(menu2jPanelLayout);
        menu2jPanelLayout.setHorizontalGroup(
            menu2jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu2jPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );
        menu2jPanelLayout.setVerticalGroup(
            menu2jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        menuPanel.add(menu2jPanel);
        menu2jPanel.setBounds(10, 290, 260, 50);

        jPanel3.setBackground(new java.awt.Color(51, 51, 255));

        jLabel3.setBackground(new java.awt.Color(153, 204, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/image.png"))); // NOI18N
        jLabel3.setMaximumSize(new java.awt.Dimension(640, 320));
        jLabel3.setMinimumSize(new java.awt.Dimension(640, 320));
        jLabel3.setPreferredSize(new java.awt.Dimension(640, 320));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        menuPanel.add(jPanel3);
        jPanel3.setBounds(-1, -1, 257, 113);

        menu3jPanel.setBackground(new java.awt.Color(51, 51, 255));
        menu3jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu3jPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu3jPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu3jPanelMouseExited(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(51, 51, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Car_32px.png"))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Vozila");

        javax.swing.GroupLayout menu3jPanelLayout = new javax.swing.GroupLayout(menu3jPanel);
        menu3jPanel.setLayout(menu3jPanelLayout);
        menu3jPanelLayout.setHorizontalGroup(
            menu3jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu3jPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        menu3jPanelLayout.setVerticalGroup(
            menu3jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        menuPanel.add(menu3jPanel);
        menu3jPanel.setBounds(10, 390, 260, 50);

        menu4jPanel.setBackground(new java.awt.Color(51, 51, 255));
        menu4jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu4jPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu4jPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu4jPanelMouseExited(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(51, 51, 255));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Book Stack_32px_2.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Knjigovodstvo");

        javax.swing.GroupLayout menu4jPanelLayout = new javax.swing.GroupLayout(menu4jPanel);
        menu4jPanel.setLayout(menu4jPanelLayout);
        menu4jPanelLayout.setHorizontalGroup(
            menu4jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu4jPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        menu4jPanelLayout.setVerticalGroup(
            menu4jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menuPanel.add(menu4jPanel);
        menu4jPanel.setBounds(10, 490, 260, 50);

        menu7jPanel.setBackground(new java.awt.Color(51, 51, 255));
        menu7jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu7jPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu7jPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu7jPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menu7jPanelMousePressed(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(51, 51, 255));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Combo Chart_32px.png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Statistika");

        javax.swing.GroupLayout menu7jPanelLayout = new javax.swing.GroupLayout(menu7jPanel);
        menu7jPanel.setLayout(menu7jPanelLayout);
        menu7jPanelLayout.setHorizontalGroup(
            menu7jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu7jPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );
        menu7jPanelLayout.setVerticalGroup(
            menu7jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        menuPanel.add(menu7jPanel);
        menu7jPanel.setBounds(10, 590, 260, 50);

        jLabel2.setFont(new java.awt.Font("Goudy Old Style", 2, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Copyright Boro 2017.");
        menuPanel.add(jLabel2);
        jLabel2.setBounds(40, 810, 160, 30);

        menu5jPanel.setBackground(new java.awt.Color(51, 51, 255));
        menu5jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu5jPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu5jPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu5jPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menu5jPanelMousePressed(evt);
            }
        });

        jLabel14.setBackground(new java.awt.Color(51, 51, 255));
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Gender Neutral User_32px_2.png"))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Zaposleni");

        javax.swing.GroupLayout menu5jPanelLayout = new javax.swing.GroupLayout(menu5jPanel);
        menu5jPanel.setLayout(menu5jPanelLayout);
        menu5jPanelLayout.setHorizontalGroup(
            menu5jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu5jPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addContainerGap())
        );
        menu5jPanelLayout.setVerticalGroup(
            menu5jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menuPanel.add(menu5jPanel);
        menu5jPanel.setBounds(10, 540, 261, 50);

        menu6jPanel.setBackground(new java.awt.Color(51, 51, 255));
        menu6jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu6jPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu6jPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu6jPanelMouseExited(evt);
            }
        });

        jLabel35.setBackground(new java.awt.Color(51, 51, 255));
        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Settings_32px_1.png"))); // NOI18N

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Dijelovi");
        jLabel36.setMaximumSize(new java.awt.Dimension(68, 50));
        jLabel36.setMinimumSize(new java.awt.Dimension(68, 50));
        jLabel36.setPreferredSize(new java.awt.Dimension(68, 50));
        jLabel36.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel36KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout menu6jPanelLayout = new javax.swing.GroupLayout(menu6jPanel);
        menu6jPanel.setLayout(menu6jPanelLayout);
        menu6jPanelLayout.setHorizontalGroup(
            menu6jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu6jPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        menu6jPanelLayout.setVerticalGroup(
            menu6jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menuPanel.add(menu6jPanel);
        menu6jPanel.setBounds(10, 440, 260, 50);

        menu1jPanel.setBackground(new java.awt.Color(51, 51, 255));
        menu1jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu1jPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu1jPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu1jPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menu1jPanelMousePressed(evt);
            }
        });

        jLabel54.setBackground(new java.awt.Color(51, 51, 255));
        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Home_32px.png"))); // NOI18N

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Početna strana");

        javax.swing.GroupLayout menu1jPanelLayout = new javax.swing.GroupLayout(menu1jPanel);
        menu1jPanel.setLayout(menu1jPanelLayout);
        menu1jPanelLayout.setHorizontalGroup(
            menu1jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu1jPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );
        menu1jPanelLayout.setVerticalGroup(
            menu1jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        menuPanel.add(menu1jPanel);
        menu1jPanel.setBounds(10, 240, 260, 50);

        menu8jPanel.setBackground(new java.awt.Color(51, 51, 255));
        menu8jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menu8jPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu8jPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu8jPanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menu8jPanelMousePressed(evt);
            }
        });

        jLabel20.setBackground(new java.awt.Color(51, 51, 255));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Clock_32px.png"))); // NOI18N

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Zakazivanja");

        javax.swing.GroupLayout menu8jPanelLayout = new javax.swing.GroupLayout(menu8jPanel);
        menu8jPanel.setLayout(menu8jPanelLayout);
        menu8jPanelLayout.setHorizontalGroup(
            menu8jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menu8jPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        menu8jPanelLayout.setVerticalGroup(
            menu8jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        menuPanel.add(menu8jPanel);
        menu8jPanel.setBounds(10, 340, 260, 50);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(8);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        parentPanel.setBackground(new java.awt.Color(255, 255, 255));
        parentPanel.setLayout(new java.awt.CardLayout());

        pocetnajPanel.setBackground(new java.awt.Color(102, 153, 255));

        jPanel5.setBackground(new java.awt.Color(102, 153, 255));

        jPanel6.setBackground(new java.awt.Color(102, 153, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel7.setBackground(new java.awt.Color(102, 153, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Search_25px.png"))); // NOI18N
        jLabel18.setText("Pretraži");
        jLabel18.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTextField22.setBackground(new java.awt.Color(102, 153, 255));
        jTextField22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField22.setForeground(new java.awt.Color(255, 255, 255));
        jTextField22.setBorder(null);

        jTextField21.setBackground(new java.awt.Color(102, 153, 255));
        jTextField21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField21.setBorder(null);

        jLabel11.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Datum do:");

        jLabel118.setBackground(new java.awt.Color(255, 255, 255));
        jLabel118.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel118.setForeground(new java.awt.Color(255, 255, 255));
        jLabel118.setText("Datum od:");

        jSeparator24.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator25.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel118, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                            .addComponent(jTextField22, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator25, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField21))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel118)
                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(4, 4, 4)
                .addComponent(jSeparator25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 24, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Pregled aktivnosti:");
        jLabel4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLabel4PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(229, 229, 229));
        jLabel16.setText("> Početna strana");

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Home_20px_1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        danasnjeAktivnostiPanel.setBackground(new java.awt.Color(255, 255, 255));
        danasnjeAktivnostiPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Današnje aktivnosti:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N

        datumLabel.setBackground(new java.awt.Color(255, 255, 255));
        datumLabel.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        datumLabel.setForeground(new java.awt.Color(204, 204, 204));
        datumLabel.setText("  ");

        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Rok", "Opis", "Vlasnik", "Auto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane11.setViewportView(jTable10);

        javax.swing.GroupLayout danasnjeAktivnostiPanelLayout = new javax.swing.GroupLayout(danasnjeAktivnostiPanel);
        danasnjeAktivnostiPanel.setLayout(danasnjeAktivnostiPanelLayout);
        danasnjeAktivnostiPanelLayout.setHorizontalGroup(
            danasnjeAktivnostiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danasnjeAktivnostiPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(danasnjeAktivnostiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(datumLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        danasnjeAktivnostiPanelLayout.setVerticalGroup(
            danasnjeAktivnostiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danasnjeAktivnostiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(datumLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        Napomene2.setBackground(new java.awt.Color(255, 255, 255));
        Napomene2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Neplaćene fakture:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N

        tblNeplaceneFakture.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Automobil", "Vlasnik", "Datum otvaranja", "Datum fakturisanja", "Iznos", "Plaćeno"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNeplaceneFakture.getTableHeader().setResizingAllowed(false);
        tblNeplaceneFakture.getTableHeader().setReorderingAllowed(false);
        tblNeplaceneFakture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNeplaceneFaktureMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblNeplaceneFakture);

        javax.swing.GroupLayout Napomene2Layout = new javax.swing.GroupLayout(Napomene2);
        Napomene2.setLayout(Napomene2Layout);
        Napomene2Layout.setHorizontalGroup(
            Napomene2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        Napomene2Layout.setVerticalGroup(
            Napomene2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Napomene2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout pocetnajPanelLayout = new javax.swing.GroupLayout(pocetnajPanel);
        pocetnajPanel.setLayout(pocetnajPanelLayout);
        pocetnajPanelLayout.setHorizontalGroup(
            pocetnajPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pocetnajPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(danasnjeAktivnostiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(Napomene2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2204, Short.MAX_VALUE))
        );
        pocetnajPanelLayout.setVerticalGroup(
            pocetnajPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pocetnajPanelLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(pocetnajPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(danasnjeAktivnostiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Napomene2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(1013, Short.MAX_VALUE))
        );

        Napomene2.getAccessibleContext().setAccessibleDescription("");

        parentPanel.add(pocetnajPanel, "card2");

        jPanel10.setBackground(new java.awt.Color(102, 153, 255));

        jPanel26.setBackground(new java.awt.Color(102, 153, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 235, Short.MAX_VALUE)
        );

        jLabel25.setBackground(new java.awt.Color(255, 255, 255));
        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(229, 229, 229));
        jLabel25.setText("> Radni nalozi");

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Maintenance_20px_2.png"))); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addContainerGap(2990, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout radniNaloziPanelLayout = new javax.swing.GroupLayout(radniNaloziPanel);
        radniNaloziPanel.setLayout(radniNaloziPanelLayout);
        radniNaloziPanelLayout.setHorizontalGroup(
            radniNaloziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        radniNaloziPanelLayout.setVerticalGroup(
            radniNaloziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, radniNaloziPanelLayout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1512, Short.MAX_VALUE))
        );

        parentPanel.add(radniNaloziPanel, "card9");

        jPanel23.setBackground(new java.awt.Color(102, 153, 255));
        jPanel23.setPreferredSize(new java.awt.Dimension(1485, 980));

        jPanel24.setBackground(new java.awt.Color(102, 153, 255));
        jPanel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel128.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel128.setForeground(new java.awt.Color(255, 255, 255));
        jLabel128.setText("Zakaži termin:");

        jLabel132.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel132.setForeground(new java.awt.Color(255, 255, 255));
        jLabel132.setText("Pronađi termin:");

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel144.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel144.setForeground(new java.awt.Color(255, 255, 255));
        jLabel144.setText("Broj telefona:");

        jLabel145.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel145.setForeground(new java.awt.Color(255, 255, 255));
        jLabel145.setText("Prezime:");

        jLabel143.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(255, 255, 255));
        jLabel143.setText("Ime:");

        jLabel133.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel133.setForeground(new java.awt.Color(255, 255, 255));
        jLabel133.setText("Marka:");

        jLabel136.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel136.setForeground(new java.awt.Color(255, 255, 255));
        jLabel136.setText("Datum termina:");

        btnPronadjiTermin.setText("Pronađi termin");
        btnPronadjiTermin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPronadjiTerminActionPerformed(evt);
            }
        });

        btnPonistiUnosePretraga.setText("Poništi unose");
        btnPonistiUnosePretraga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPonistiUnosePretragaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnPronadjiTermin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPonistiUnosePretraga))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel144)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtBrojTelefonaPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel133)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMarkaPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel145)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPrezimePretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel143)
                            .addComponent(jLabel136))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtImePretraga, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dtmDatumPretraga, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel133)
                    .addComponent(txtMarkaPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel136))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(dtmDatumPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel143)
                    .addComponent(txtImePretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel145)
                    .addComponent(txtPrezimePretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel144)
                    .addComponent(txtBrojTelefonaPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPronadjiTermin)
                    .addComponent(btnPonistiUnosePretraga))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 153, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel131.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel131.setForeground(new java.awt.Color(255, 255, 255));
        jLabel131.setText("Marka:");

        jLabel135.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(255, 255, 255));
        jLabel135.setText("Model:");

        jLabel141.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel141.setForeground(new java.awt.Color(255, 255, 255));
        jLabel141.setText("Ime:");

        jLabel140.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel140.setForeground(new java.awt.Color(255, 255, 255));
        jLabel140.setText("Prezime:");

        jLabel142.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel142.setForeground(new java.awt.Color(255, 255, 255));
        jLabel142.setText("Broj telefona:");

        btnDodajTermin.setText("Dodaj termin");
        btnDodajTermin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajTerminActionPerformed(evt);
            }
        });

        jLabel130.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel130.setForeground(new java.awt.Color(255, 255, 255));
        jLabel130.setText("Vrijeme termina:");

        jLabel134.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(255, 255, 255));
        jLabel134.setText("Datum termina:");

        txtVrijemeTerminaSati.setModel(new javax.swing.SpinnerNumberModel(0, 0, 23, 1));

        txtVrijemeTerminaMinuti.setModel(new javax.swing.SpinnerNumberModel(0, 0, 59, 15));

        jLabel156.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel156.setForeground(new java.awt.Color(255, 255, 255));
        jLabel156.setText("h");
        jLabel156.setToolTipText("");

        jLabel157.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel157.setForeground(new java.awt.Color(255, 255, 255));
        jLabel157.setText("min");
        jLabel157.setToolTipText("");

        btnPonistiUnoseTermin.setText("Poništi unose");
        btnPonistiUnoseTermin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPonistiUnoseTerminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDodajTermin)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel134)
                        .addGap(84, 84, 84)
                        .addComponent(dtmDatumTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel131)
                            .addComponent(jLabel135)
                            .addComponent(jLabel141)
                            .addComponent(jLabel140)
                            .addComponent(jLabel142)
                            .addComponent(jLabel130))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(txtVrijemeTerminaSati, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtVrijemeTerminaMinuti, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMarkaTermina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtModelTermina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtImeTermina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrezimeTermina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBrojTelefonaTermina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPonistiUnoseTermin, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDodajTermin))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel134))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(dtmDatumTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtVrijemeTerminaSati, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtVrijemeTerminaMinuti, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel130)
                                    .addComponent(jLabel156)
                                    .addComponent(jLabel157))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel131)
                            .addComponent(txtMarkaTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel135)
                            .addComponent(txtModelTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel141)
                            .addComponent(txtImeTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel140)
                            .addComponent(txtPrezimeTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel142)
                            .addComponent(txtBrojTelefonaTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPonistiUnoseTermin)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jScrollPane10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane10.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane10.setMaximumSize(new java.awt.Dimension(920, 800));
        jScrollPane10.setPreferredSize(new java.awt.Dimension(920, 800));

        tblTermini.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Datum termina", "Vrijeme termina", "Datum zakazivanja", "Marka", "Model", "Ime", "Prezime", "Broj telefona"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTermini.setMaximumSize(new java.awt.Dimension(1000, 288));
        tblTermini.setMinimumSize(new java.awt.Dimension(1000, 288));
        tblTermini.setPreferredSize(new java.awt.Dimension(1000, 288));
        tblTermini.getTableHeader().setResizingAllowed(false);
        tblTermini.getTableHeader().setReorderingAllowed(false);
        jScrollPane10.setViewportView(tblTermini);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 928, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel132))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel128)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel132)
                    .addComponent(jLabel128))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        jLabel138.setBackground(new java.awt.Color(255, 255, 255));
        jLabel138.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel138.setForeground(new java.awt.Color(229, 229, 229));
        jLabel138.setText(">Zakazivanja");

        jLabel139.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Clock_20px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel138)
                .addContainerGap(2875, Short.MAX_VALUE))
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel138))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1181, 1181, 1181))
        );

        javax.swing.GroupLayout zakazivanjaPanelLayout = new javax.swing.GroupLayout(zakazivanjaPanel);
        zakazivanjaPanel.setLayout(zakazivanjaPanelLayout);
        zakazivanjaPanelLayout.setHorizontalGroup(
            zakazivanjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(zakazivanjaPanelLayout.createSequentialGroup()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, 3042, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 120, Short.MAX_VALUE))
        );
        zakazivanjaPanelLayout.setVerticalGroup(
            zakazivanjaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(zakazivanjaPanelLayout.createSequentialGroup()
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, 1811, Short.MAX_VALUE)
                .addContainerGap())
        );

        parentPanel.add(zakazivanjaPanel, "card8");

        vozilaPanel.setBackground(new java.awt.Color(102, 153, 255));
        vozilaPanel.setPreferredSize(new java.awt.Dimension(1088, 697));

        panelAkcijeNaFormi.setBackground(new java.awt.Color(102, 153, 255));

        jLabel92.setBackground(new java.awt.Color(255, 255, 255));
        jLabel92.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(229, 229, 229));
        jLabel92.setText(">Vozila");

        jLabel95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Car_32px.png"))); // NOI18N

        panelVozilo.setBackground(new java.awt.Color(102, 153, 255));
        panelVozilo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        panelPronadjiVozilo.setBackground(new java.awt.Color(102, 153, 255));
        panelPronadjiVozilo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel105.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel105.setForeground(new java.awt.Color(255, 255, 255));
        jLabel105.setText("Registracija:");

        jLabel106.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel106.setForeground(new java.awt.Color(255, 255, 255));
        jLabel106.setText("Godiste:");

        jLabel107.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel107.setForeground(new java.awt.Color(255, 255, 255));
        jLabel107.setText("Model:");

        jLabel108.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel108.setForeground(new java.awt.Color(255, 255, 255));
        jLabel108.setText("Prezime:");

        tfModelTrazi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfModelTraziFocusGained(evt);
            }
        });

        tfPrezimeVozilo.setEditable(false);

        btnPronadji.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPronadji.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnPronadji.setText("Traži");
        btnPronadji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPronadjiActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Marka:");

        tfNazivVozilo.setEditable(false);

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Naziv:");

        btnPonistiSve.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPonistiSve.setText("Poništi unose");
        btnPonistiSve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPonistiSveActionPerformed(evt);
            }
        });

        tfImeVozilo.setEditable(false);

        rbPrivatnoLiceVozilo.setBackground(new java.awt.Color(102, 153, 255));
        rbPrivatnoLiceVozilo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPrivatnoLiceVozilo.setForeground(new java.awt.Color(255, 255, 255));
        rbPrivatnoLiceVozilo.setSelected(true);
        rbPrivatnoLiceVozilo.setText("Privatno lice");
        rbPrivatnoLiceVozilo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPrivatnoLiceVoziloActionPerformed(evt);
            }
        });

        rbPravnoLiceVozilo.setBackground(new java.awt.Color(102, 153, 255));
        rbPravnoLiceVozilo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPravnoLiceVozilo.setForeground(new java.awt.Color(255, 255, 255));
        rbPravnoLiceVozilo.setText("Pravno lice");
        rbPravnoLiceVozilo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPravnoLiceVoziloActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Ime:");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Vlasnik:");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("Prikaži sve");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cbSvi.setBackground(new java.awt.Color(102, 153, 255));
        cbSvi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSvi.setForeground(new java.awt.Color(240, 240, 240));
        cbSvi.setText("Svi");
        cbSvi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSviActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPronadjiVoziloLayout = new javax.swing.GroupLayout(panelPronadjiVozilo);
        panelPronadjiVozilo.setLayout(panelPronadjiVoziloLayout);
        panelPronadjiVoziloLayout.setHorizontalGroup(
            panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPronadjiVoziloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel105)
                            .addComponent(jLabel106))
                        .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPonistiSve, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel108, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfImeVozilo)
                    .addComponent(tfNazivVozilo)
                    .addComponent(tfPrezimeVozilo)
                    .addComponent(tfGodisteTrazi, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelPronadjiVoziloLayout.createSequentialGroup()
                        .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(panelPronadjiVoziloLayout.createSequentialGroup()
                                    .addComponent(tfMarkaTrazi)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel107)
                                    .addGap(2, 2, 2)
                                    .addComponent(tfModelTrazi))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelPronadjiVoziloLayout.createSequentialGroup()
                                    .addComponent(rbPrivatnoLiceVozilo)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(rbPravnoLiceVozilo)))
                            .addComponent(tfRegistracijaTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPronadjiVoziloLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnPronadji, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPronadjiVoziloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbSvi, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );
        panelPronadjiVoziloLayout.setVerticalGroup(
            panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPronadjiVoziloLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel105)
                    .addComponent(tfRegistracijaTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel106)
                    .addComponent(tfGodisteTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel107)
                    .addComponent(tfModelTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(tfMarkaTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbSvi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbPrivatnoLiceVozilo)
                    .addComponent(rbPravnoLiceVozilo)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPrezimeVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel108))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfImeVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNazivVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelPronadjiVoziloLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPonistiSve, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnPronadji))
                .addContainerGap())
        );

        jLabel96.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 255, 255));
        jLabel96.setText("Pronađi vozilo");

        jLabel97.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 255, 255));
        jLabel97.setText("Pronađi vlasnika:");

        panelPronadjiVlasnika.setBackground(new java.awt.Color(102, 153, 255));
        panelPronadjiVlasnika.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel109.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel109.setForeground(new java.awt.Color(255, 255, 255));
        jLabel109.setText("Ime:");

        jLabel110.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel110.setForeground(new java.awt.Color(255, 255, 255));
        jLabel110.setText("Prezime:");

        btnTrazi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTrazi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnTrazi.setText("Traži");
        btnTrazi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraziActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Naziv:");

        tfNazivTrazi.setEditable(false);

        rbPrivatnoTrazi.setBackground(new java.awt.Color(102, 153, 255));
        rbPrivatnoTrazi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPrivatnoTrazi.setForeground(new java.awt.Color(255, 255, 255));
        rbPrivatnoTrazi.setSelected(true);
        rbPrivatnoTrazi.setText("Privatno lice");
        rbPrivatnoTrazi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPrivatnoTraziActionPerformed(evt);
            }
        });

        rbPravnoTrazi.setBackground(new java.awt.Color(102, 153, 255));
        rbPravnoTrazi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPravnoTrazi.setForeground(new java.awt.Color(255, 255, 255));
        rbPravnoTrazi.setText("Pravno lice");
        rbPravnoTrazi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPravnoTraziActionPerformed(evt);
            }
        });

        btnPrikaziSve.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrikaziSve.setText("Prikaži sve");
        btnPrikaziSve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrikaziSveActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Vlasnik:");

        btnPonisti.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPonisti.setText("Poništi unose");
        btnPonisti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPonistiActionPerformed(evt);
            }
        });

        labelPoruka.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout panelPronadjiVlasnikaLayout = new javax.swing.GroupLayout(panelPronadjiVlasnika);
        panelPronadjiVlasnika.setLayout(panelPronadjiVlasnikaLayout);
        panelPronadjiVlasnikaLayout.setHorizontalGroup(
            panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPronadjiVlasnikaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPronadjiVlasnikaLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPronadjiVlasnikaLayout.createSequentialGroup()
                                .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel110)
                                    .addComponent(jLabel109)
                                    .addComponent(jLabel27))
                                .addGap(18, 18, 18)
                                .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPronadjiVlasnikaLayout.createSequentialGroup()
                                            .addComponent(rbPrivatnoTrazi)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(rbPravnoTrazi))
                                        .addComponent(tfPrezimeTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(tfNazivTrazi, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                                    .addComponent(tfImeTrazi)
                                    .addComponent(labelPoruka))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelPronadjiVlasnikaLayout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(223, 223, 223))))
                    .addGroup(panelPronadjiVlasnikaLayout.createSequentialGroup()
                        .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnPonisti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPrikaziSve, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTrazi)))
                .addContainerGap())
        );

        panelPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {rbPravnoTrazi, rbPrivatnoTrazi});

        panelPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfImeTrazi, tfNazivTrazi, tfPrezimeTrazi});

        panelPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel109, jLabel110, jLabel27});

        panelPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnPonisti, btnPrikaziSve, btnTrazi});

        panelPronadjiVlasnikaLayout.setVerticalGroup(
            panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPronadjiVlasnikaLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbPravnoTrazi)
                    .addComponent(rbPrivatnoTrazi)
                    .addComponent(jLabel24))
                .addGap(21, 21, 21)
                .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel110)
                    .addComponent(tfPrezimeTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfImeTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel109))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNazivTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelPoruka)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrikaziSve, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTrazi)
                    .addGroup(panelPronadjiVlasnikaLayout.createSequentialGroup()
                        .addComponent(btnPonisti, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addContainerGap())
        );

        panelPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {rbPravnoTrazi, rbPrivatnoTrazi});

        panelPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tfImeTrazi, tfNazivTrazi, tfPrezimeTrazi});

        panelPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel109, jLabel110, jLabel27});

        panelPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnPonisti, btnPrikaziSve, btnTrazi});

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/racing (1).png"))); // NOI18N
        jButton2.setText("Dodaj Vozilo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/user (2).png"))); // NOI18N
        jButton4.setText("Dodaj Vlasnika");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/cube.png"))); // NOI18N
        jButton5.setText("Dodaj Model");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton10.setText("Izmijeni/Izbriši model");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelVoziloLayout = new javax.swing.GroupLayout(panelVozilo);
        panelVozilo.setLayout(panelVoziloLayout);
        panelVoziloLayout.setHorizontalGroup(
            panelVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVoziloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel96)
                    .addComponent(panelPronadjiVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel97)
                    .addGroup(panelVoziloLayout.createSequentialGroup()
                        .addComponent(panelPronadjiVlasnika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(210, 210, 210))
        );

        panelVoziloLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton2, jButton4, jButton5});

        panelVoziloLayout.setVerticalGroup(
            panelVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVoziloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel96)
                    .addComponent(jLabel97))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelPronadjiVozilo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelPronadjiVlasnika, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelVoziloLayout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
        );

        panelVoziloLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton2, jButton4, jButton5});

        tableVozila.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        spVoziloPretraga.setViewportView(tableVozila);

        javax.swing.GroupLayout panelAkcijeNaFormiLayout = new javax.swing.GroupLayout(panelAkcijeNaFormi);
        panelAkcijeNaFormi.setLayout(panelAkcijeNaFormiLayout);
        panelAkcijeNaFormiLayout.setHorizontalGroup(
            panelAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAkcijeNaFormiLayout.createSequentialGroup()
                .addGroup(panelAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAkcijeNaFormiLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel92))
                    .addGroup(panelAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(spVoziloPretraga, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelAkcijeNaFormiLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(panelVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, 1053, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(2099, Short.MAX_VALUE))
        );
        panelAkcijeNaFormiLayout.setVerticalGroup(
            panelAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAkcijeNaFormiLayout.createSequentialGroup()
                .addGroup(panelAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel92)
                    .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spVoziloPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout vozilaPanelLayout = new javax.swing.GroupLayout(vozilaPanel);
        vozilaPanel.setLayout(vozilaPanelLayout);
        vozilaPanelLayout.setHorizontalGroup(
            vozilaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(panelAkcijeNaFormi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        vozilaPanelLayout.setVerticalGroup(
            vozilaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vozilaPanelLayout.createSequentialGroup()
                .addComponent(panelAkcijeNaFormi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        parentPanel.add(vozilaPanel, "card6");

        dijeloviPanel.setBackground(new java.awt.Color(255, 255, 255));
        dijeloviPanel.setNextFocusableComponent(tfId);

        jPanel13.setBackground(new java.awt.Color(102, 153, 255));

        jPanel14.setBackground(new java.awt.Color(102, 153, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Pronadji dio:");

        pretraziPanel.setBackground(new java.awt.Color(102, 153, 255));
        pretraziPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel50.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Šifra:");
        jLabel50.setPreferredSize(new java.awt.Dimension(42, 20));

        jLabel52.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Naziv:");
        jLabel52.setPreferredSize(new java.awt.Dimension(48, 20));

        jLabel73.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Gorivo:");

        cbMarka.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbMarka.setMaximumRowCount(50);
        cbMarka.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi" }));
        cbMarka.setMaximumSize(new java.awt.Dimension(81, 20));
        cbMarka.setMinimumSize(new java.awt.Dimension(81, 20));
        cbMarka.setPreferredSize(new java.awt.Dimension(81, 20));

        jLabel53.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Id:");
        jLabel53.setPreferredSize(new java.awt.Dimension(60, 20));

        jLabel74.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("Marka:");
        jLabel74.setPreferredSize(new java.awt.Dimension(60, 20));

        cbNovo.setText("Novo");
        cbNovo.setMaximumSize(new java.awt.Dimension(81, 20));
        cbNovo.setMinimumSize(new java.awt.Dimension(51, 20));
        cbNovo.setPreferredSize(new java.awt.Dimension(81, 20));

        jLabel75.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(255, 255, 255));
        jLabel75.setText("Stanje:");
        jLabel75.setPreferredSize(new java.awt.Dimension(60, 20));

        jLabel76.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setText("Godište:");

        cbModel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbModel.setMaximumRowCount(50);
        cbModel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi" }));
        cbModel.setMinimumSize(new java.awt.Dimension(41, 20));
        cbModel.setPreferredSize(new java.awt.Dimension(81, 20));

        jLabel77.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(255, 255, 255));
        jLabel77.setText("Model:");
        jLabel77.setPreferredSize(new java.awt.Dimension(60, 20));

        cbGorivo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbGorivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi", "Benzin", "Dizel" }));
        cbGorivo.setMinimumSize(new java.awt.Dimension(60, 20));
        cbGorivo.setPreferredSize(new java.awt.Dimension(81, 20));

        btnPretrazi.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPretrazi.setText("Pretraži");
        btnPretrazi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPretraziActionPerformed(evt);
            }
        });

        btnSviDijelovi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSviDijelovi.setText("Prikaži sve");
        btnSviDijelovi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSviDijeloviActionPerformed(evt);
            }
        });

        btnProdani.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnProdani.setText("Prodani dijelovi");
        btnProdani.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdaniActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pretraziPanelLayout = new javax.swing.GroupLayout(pretraziPanel);
        pretraziPanel.setLayout(pretraziPanelLayout);
        pretraziPanelLayout.setHorizontalGroup(
            pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pretraziPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pretraziPanelLayout.createSequentialGroup()
                        .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel76))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfId)
                            .addComponent(tfSifra)
                            .addComponent(tfNaziv)
                            .addComponent(tfGodiste, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                        .addGap(86, 86, 86)
                        .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pretraziPanelLayout.createSequentialGroup()
                                .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(cbModel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pretraziPanelLayout.createSequentialGroup()
                                .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbNovo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pretraziPanelLayout.createSequentialGroup()
                                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbMarka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbGorivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(pretraziPanelLayout.createSequentialGroup()
                        .addComponent(btnSviDijelovi, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(btnPretrazi, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addComponent(btnProdani)))
                .addContainerGap())
        );
        pretraziPanelLayout.setVerticalGroup(
            pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pretraziPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbNovo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfSifra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbGorivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfNaziv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbMarka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76)
                    .addComponent(cbModel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfGodiste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPretrazi, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSviDijelovi)
                    .addComponent(btnProdani))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dodajDioPanel.setBackground(new java.awt.Color(102, 153, 255));
        dodajDioPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel44.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Šifra:");
        jLabel44.setPreferredSize(new java.awt.Dimension(60, 20));

        jLabel45.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Naziv:");
        jLabel45.setPreferredSize(new java.awt.Dimension(42, 20));

        jLabel46.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Cijena:");
        jLabel46.setPreferredSize(new java.awt.Dimension(48, 20));

        cbStanje.setText("Novo");
        cbStanje.setMaximumSize(new java.awt.Dimension(81, 20));
        cbStanje.setMinimumSize(new java.awt.Dimension(51, 20));
        cbStanje.setPreferredSize(new java.awt.Dimension(81, 20));

        jLabel60.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Stanje:");
        jLabel60.setPreferredSize(new java.awt.Dimension(60, 20));

        jcbGorivo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jcbGorivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi", "Benzin", "Dizel" }));
        jcbGorivo.setMinimumSize(new java.awt.Dimension(60, 20));
        jcbGorivo.setPreferredSize(new java.awt.Dimension(81, 20));

        jLabel61.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Gorivo:");

        jcbMarka.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jcbMarka.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi", "Audi", "BMW", "Mercedes" }));
        jcbMarka.setMaximumSize(new java.awt.Dimension(81, 20));
        jcbMarka.setMinimumSize(new java.awt.Dimension(81, 20));
        jcbMarka.setPreferredSize(new java.awt.Dimension(81, 20));

        jLabel62.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("Marka:");
        jLabel62.setPreferredSize(new java.awt.Dimension(60, 20));

        jLabel63.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setText("Godište:");

        jcbModel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jcbModel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi" }));
        jcbModel.setMinimumSize(new java.awt.Dimension(41, 20));
        jcbModel.setPreferredSize(new java.awt.Dimension(81, 20));

        jLabel64.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setText("Model:");
        jLabel64.setPreferredSize(new java.awt.Dimension(60, 20));

        btnDodaj.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnDodaj.setText("DODAJ");
        btnDodaj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Količina:");

        javax.swing.GroupLayout dodajDioPanelLayout = new javax.swing.GroupLayout(dodajDioPanel);
        dodajDioPanel.setLayout(dodajDioPanelLayout);
        dodajDioPanelLayout.setHorizontalGroup(
            dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dodajDioPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtfSifra)
                    .addComponent(jtfNaziv)
                    .addComponent(jtfCijena)
                    .addComponent(jtfGodiste)
                    .addComponent(jtfKolicina, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addGap(86, 86, 86)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(dodajDioPanelLayout.createSequentialGroup()
                        .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jcbModel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dodajDioPanelLayout.createSequentialGroup()
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbStanje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dodajDioPanelLayout.createSequentialGroup()
                        .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbMarka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcbGorivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnDodaj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        dodajDioPanelLayout.setVerticalGroup(
            dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dodajDioPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbStanje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfSifra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfNaziv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jcbGorivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfCijena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jcbMarka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(jcbModel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfGodiste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dodajDioPanelLayout.createSequentialGroup()
                        .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(jtfKolicina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnDodaj, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel49.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Dodaj dio");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addComponent(pretraziPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49)
                    .addComponent(dodajDioPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dodajDioPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pretraziPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel47.setBackground(new java.awt.Color(255, 255, 255));
        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(229, 229, 229));
        jLabel47.setText(">Dijelovi");

        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Settings_20px_1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel47)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Šifra", "Naziv", "Marka", "Model", "Godište", "Vrsta goriva", "Cijena", "Količina", "Novo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable);

        javax.swing.GroupLayout dijeloviPanelLayout = new javax.swing.GroupLayout(dijeloviPanel);
        dijeloviPanel.setLayout(dijeloviPanelLayout);
        dijeloviPanelLayout.setHorizontalGroup(
            dijeloviPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dijeloviPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1048, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2104, Short.MAX_VALUE))
        );
        dijeloviPanelLayout.setVerticalGroup(
            dijeloviPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dijeloviPanelLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1070, Short.MAX_VALUE))
        );

        parentPanel.add(dijeloviPanel, "card4");
        dijeloviPanel.getAccessibleContext().setAccessibleName("");

        knjigovodstvoPanel.setBackground(new java.awt.Color(102, 153, 255));

        jPanel17.setBackground(new java.awt.Color(102, 153, 255));
        jPanel17.setPreferredSize(new java.awt.Dimension(895, 600));

        jPanel22.setBackground(new java.awt.Color(102, 153, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel120.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(255, 255, 255));
        jLabel120.setText("Radni nalozi:");

        btnPredracun.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPredracun.setText("Predračun");
        btnPredracun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPredracunActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(102, 153, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane9.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblRadniNalozi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Automobil", "Vlasnik", "Datum otvaranja", "Datum fakturisanja", "Iznos", "Plaćeno"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRadniNalozi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRadniNaloziMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblRadniNaloziMouseReleased(evt);
            }
        });
        jScrollPane9.setViewportView(tblRadniNalozi);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel161.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel161.setForeground(new java.awt.Color(255, 255, 255));
        jLabel161.setText("Ukupno:");

        txtUkupno.setEditable(false);

        jLabel162.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel162.setForeground(new java.awt.Color(255, 255, 255));
        jLabel162.setText("KM");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblFaktura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Naziv", "Količina", "Osnovica", "Cijena sa PDV"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFaktura.setMaximumSize(new java.awt.Dimension(1000, 480));
        tblFaktura.setMinimumSize(new java.awt.Dimension(1000, 480));
        tblFaktura.setPreferredSize(new java.awt.Dimension(1000, 480));
        tblFaktura.getTableHeader().setResizingAllowed(false);
        tblFaktura.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblFaktura);

        btnRacun.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRacun.setText("Račun");
        btnRacun.setEnabled(false);
        btnRacun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRacunActionPerformed(evt);
            }
        });

        btnPoIDu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPoIDu.setText("Po ID-u");
        btnPoIDu.setToolTipText("");
        btnPoIDu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPoIDuActionPerformed(evt);
            }
        });

        btnPoDatumu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPoDatumu.setText("Po datumu");
        btnPoDatumu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPoDatumuActionPerformed(evt);
            }
        });

        txtBezPDV.setEditable(false);

        txtPDV.setEditable(false);

        jLabel163.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel163.setForeground(new java.awt.Color(255, 255, 255));
        jLabel163.setText("PDV:");

        jLabel164.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel164.setForeground(new java.awt.Color(255, 255, 255));
        jLabel164.setText("Iznos:");

        jLabel165.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel165.setForeground(new java.awt.Color(255, 255, 255));
        jLabel165.setText("Bez PDV-a:");

        btnNefakturisano.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNefakturisano.setText("Nefakturisano");
        btnNefakturisano.setToolTipText("");
        btnNefakturisano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNefakturisanoActionPerformed(evt);
            }
        });

        btnFakturisano.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnFakturisano.setText("Fakturisano");
        btnFakturisano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFakturisanoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jLabel120)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addComponent(btnPoDatumu, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(dtmDatum, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel22Layout.createSequentialGroup()
                                            .addComponent(btnPredracun, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(btnRacun, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel22Layout.createSequentialGroup()
                                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(btnNefakturisano, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnPoIDu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel22Layout.createSequentialGroup()
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                                                    .addGap(10, 10, 10)
                                                    .addComponent(btnFakturisano, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addComponent(jLabel164)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel165)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBezPDV, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel163)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPDV, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                        .addComponent(jLabel161)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUkupno, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel162)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel120)
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNefakturisano)
                            .addComponent(btnFakturisano))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPoIDu)
                            .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPoDatumu)
                            .addComponent(dtmDatum, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRacun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPredracun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel164)
                    .addComponent(jLabel165)
                    .addComponent(txtBezPDV, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel163)
                    .addComponent(txtPDV, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel161)
                    .addComponent(txtUkupno, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel162))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel126.setBackground(new java.awt.Color(255, 255, 255));
        jLabel126.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel126.setForeground(new java.awt.Color(229, 229, 229));
        jLabel126.setText(">Knjigovodstvo");

        jLabel127.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Book Stack_20px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel126)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel126))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(530, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout knjigovodstvoPanelLayout = new javax.swing.GroupLayout(knjigovodstvoPanel);
        knjigovodstvoPanel.setLayout(knjigovodstvoPanelLayout);
        knjigovodstvoPanelLayout.setHorizontalGroup(
            knjigovodstvoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(knjigovodstvoPanelLayout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 952, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2210, Short.MAX_VALUE))
        );
        knjigovodstvoPanelLayout.setVerticalGroup(
            knjigovodstvoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(knjigovodstvoPanelLayout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, 1162, Short.MAX_VALUE)
                .addGap(660, 660, 660))
        );

        parentPanel.add(knjigovodstvoPanel, "card7");

        zaposleniPanel.setBackground(new java.awt.Color(255, 255, 255));

        jPanel27.setBackground(new java.awt.Color(102, 153, 255));

        jPanel28.setBackground(new java.awt.Color(102, 153, 255));
        jPanel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel117.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel117.setForeground(new java.awt.Color(255, 255, 255));
        jLabel117.setText("Pronađi zaposlenog:");

        jLabel119.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(255, 255, 255));
        jLabel119.setText("Do datuma:");

        jTableZaposleni.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        jTableZaposleni.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, "", ""},
                {"", null, null, ""},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Ime", "Ime oca", "Prezime", "Telefon"
            }
        ));
        jTableZaposleni.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTableZaposleniMouseReleased(evt);
            }
        });
        jScrollPane12.setViewportView(jTableZaposleni);
        if (jTableZaposleni.getColumnModel().getColumnCount() > 0) {
            jTableZaposleni.getColumnModel().getColumn(1).setResizable(false);
        }

        labelBrojRadnihNaloga.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelBrojRadnihNaloga.setForeground(new java.awt.Color(255, 255, 255));
        labelBrojRadnihNaloga.setText("Radni nalozi: 0");

        jSeparator26.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator26.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel122.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel122.setForeground(new java.awt.Color(255, 255, 255));
        jLabel122.setText("Od datuma:");

        buttonTraziRadneNalogeRadnika.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        buttonTraziRadneNalogeRadnika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        buttonTraziRadneNalogeRadnika.setText("Traži");
        buttonTraziRadneNalogeRadnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTraziRadneNalogeRadnikaActionPerformed(evt);
            }
        });

        labelOstvareniProfitRadnika.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelOstvareniProfitRadnika.setForeground(new java.awt.Color(255, 255, 255));
        labelOstvareniProfitRadnika.setText("Ostvareni profit radnika: 0 KM");

        jPanel29.setBackground(new java.awt.Color(102, 153, 255));
        jPanel29.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel124.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel124.setForeground(new java.awt.Color(255, 255, 255));
        jLabel124.setText("Unesi zaposlenog:");

        jLabel129.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel129.setForeground(new java.awt.Color(255, 255, 255));
        jLabel129.setText("Ime:");

        jLabel137.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(255, 255, 255));
        jLabel137.setText("Prezime:");

        jLabel146.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel146.setForeground(new java.awt.Color(255, 255, 255));
        jLabel146.setText("Telefon:");

        jLabel147.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel147.setForeground(new java.awt.Color(255, 255, 255));
        jLabel147.setText("Stručna sprema:");

        buttonDodajZaposlenog.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        buttonDodajZaposlenog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/noviZaposleni.png"))); // NOI18N
        buttonDodajZaposlenog.setText("Dodaj zaposlenog");
        buttonDodajZaposlenog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDodajZaposlenogActionPerformed(evt);
            }
        });

        jLabel150.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel150.setForeground(new java.awt.Color(255, 255, 255));
        jLabel150.setText("Ime oca:");

        jLabel151.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel151.setForeground(new java.awt.Color(255, 255, 255));
        jLabel151.setText("Adresa:");

        jLabel152.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel152.setForeground(new java.awt.Color(255, 255, 255));
        jLabel152.setText("Broj lične karte:");

        jLabel153.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel153.setForeground(new java.awt.Color(255, 255, 255));
        jLabel153.setText("Datum rođenja:");

        jLabel154.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel154.setForeground(new java.awt.Color(255, 255, 255));
        jLabel154.setText("Datum primanja:");

        jLabel155.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel155.setForeground(new java.awt.Color(255, 255, 255));
        jLabel155.setText("Funkcija:");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(jLabel124)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel129)
                        .addComponent(jLabel137)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel151)
                                .addComponent(jLabel146)
                                .addComponent(jLabel150))
                            .addGap(59, 59, 59)
                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(textFieldAdresa, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldTelefon, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldPrezime, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldIme, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(textFieldImeOca, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(jLabel152)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldBrojLicneKarte, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel147)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel153)
                                .addGap(14, 14, 14)))
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dateChooserDatumRodjenja, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(textFieldStrucnaSprema)))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel154)
                            .addComponent(jLabel155))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonDodajZaposlenog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textFieldFunkcijaRadnika)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(dateChooserDatumPrimanjaURadniOdnos, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(jLabel124)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel129)
                    .addComponent(textFieldIme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel137)
                    .addComponent(textFieldPrezime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel150)
                    .addComponent(textFieldImeOca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel151)
                    .addComponent(textFieldAdresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel146)
                    .addComponent(textFieldTelefon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel152)
                    .addComponent(textFieldBrojLicneKarte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel147)
                    .addComponent(textFieldStrucnaSprema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel153)
                    .addComponent(dateChooserDatumRodjenja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel154, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateChooserDatumPrimanjaURadniOdnos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel155)
                    .addComponent(textFieldFunkcijaRadnika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonDodajZaposlenog)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonPrikazSvihBivsihRadnika.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        buttonPrikazSvihBivsihRadnika.setText("Prikaz svih bivših zaposlenih");
        buttonPrikazSvihBivsihRadnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrikazSvihBivsihRadnikaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator26, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane12)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(jLabel117)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel122)
                                    .addComponent(dateChooserDatumOdZaposlenog, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(44, 44, 44)
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel119)
                                    .addComponent(dateChooserDatumDoZaposlenog, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(buttonTraziRadneNalogeRadnika, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(buttonPrikazSvihBivsihRadnika)))
                        .addContainerGap())
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(labelBrojRadnihNaloga)
                        .addGap(183, 183, 183)
                        .addComponent(labelOstvareniProfitRadnika)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel117)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                                .addComponent(jLabel122)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dateChooserDatumOdZaposlenog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                                .addComponent(jLabel119)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dateChooserDatumDoZaposlenog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonTraziRadneNalogeRadnika)
                                .addComponent(buttonPrikazSvihBivsihRadnika, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator26, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelBrojRadnihNaloga)
                    .addComponent(labelOstvareniProfitRadnika))
                .addContainerGap())
        );

        jLabel148.setBackground(new java.awt.Color(255, 255, 255));
        jLabel148.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel148.setForeground(new java.awt.Color(229, 229, 229));
        jLabel148.setText("> Zaposleni");

        jLabel149.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/User_20px_1.png"))); // NOI18N

        tableRadniNalozi.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        tableRadniNalozi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Marka", "Model", "Registracija", "Datum popravka", "Opis popravke", "Cijena troskova", "Cijena naplate", "Profit"
            }
        ));
        jScrollPane13.setViewportView(tableRadniNalozi);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel27Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(2062, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel148))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout zaposleniPanelLayout = new javax.swing.GroupLayout(zaposleniPanel);
        zaposleniPanel.setLayout(zaposleniPanelLayout);
        zaposleniPanelLayout.setHorizontalGroup(
            zaposleniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(zaposleniPanelLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        zaposleniPanelLayout.setVerticalGroup(
            zaposleniPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(zaposleniPanelLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 927, Short.MAX_VALUE))
        );

        parentPanel.add(zaposleniPanel, "card3");

        statistikajPanel.setBackground(new java.awt.Color(255, 255, 255));

        statistikaHeaderjPanel.setBackground(new java.awt.Color(102, 153, 255));

        jPanel18.setBackground(new java.awt.Color(102, 153, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel19.setBackground(new java.awt.Color(102, 153, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ukupna zarada", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Dnevna:");

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Mjesečna:");

        jLabel65.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 255, 255));
        jLabel65.setText("Godišnja:");

        labelIntervalZarada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelIntervalZarada.setForeground(new java.awt.Color(255, 255, 255));
        labelIntervalZarada.setText(" KM");

        labelGodisnjaZarada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelGodisnjaZarada.setForeground(new java.awt.Color(255, 255, 255));
        labelGodisnjaZarada.setText(" KM");

        labelMjesecnaZarada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelMjesecnaZarada.setForeground(new java.awt.Color(255, 255, 255));
        labelMjesecnaZarada.setText(" KM");

        jLabel88.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(255, 255, 255));
        jLabel88.setText("Interval:");

        labelDnevnaZarada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelDnevnaZarada.setForeground(new java.awt.Color(255, 255, 255));
        labelDnevnaZarada.setText(" KM");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57)
                    .addComponent(jLabel65)
                    .addComponent(jLabel58)
                    .addComponent(jLabel88))
                .addGap(33, 33, 33)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelIntervalZarada)
                    .addComponent(labelMjesecnaZarada)
                    .addComponent(labelGodisnjaZarada)
                    .addComponent(labelDnevnaZarada))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(labelDnevnaZarada))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(labelMjesecnaZarada))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(labelGodisnjaZarada))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88)
                    .addComponent(labelIntervalZarada))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.setBackground(new java.awt.Color(102, 153, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Broj popravki", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel69.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Danas:");

        jLabel70.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(255, 255, 255));
        jLabel70.setText("Mjesec:");

        jLabel71.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setText("Godina:");

        labelPopravkeDanas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelPopravkeDanas.setForeground(new java.awt.Color(255, 255, 255));
        labelPopravkeDanas.setText("8");

        labelPopravkeGodina.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelPopravkeGodina.setForeground(new java.awt.Color(255, 255, 255));
        labelPopravkeGodina.setText("2542");

        labelPopravkeMjesec.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelPopravkeMjesec.setForeground(new java.awt.Color(255, 255, 255));
        labelPopravkeMjesec.setText("220");

        jLabel89.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(255, 255, 255));
        jLabel89.setText("Interval:");

        labelPopravkeInterval.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelPopravkeInterval.setForeground(new java.awt.Color(255, 255, 255));
        labelPopravkeInterval.setText(" ");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addGap(66, 66, 66)
                        .addComponent(labelPopravkeDanas))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel71)
                            .addComponent(jLabel70)
                            .addComponent(jLabel89))
                        .addGap(58, 58, 58)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPopravkeInterval)
                            .addComponent(labelPopravkeMjesec)
                            .addComponent(labelPopravkeGodina))))
                .addGap(0, 33, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(labelPopravkeDanas))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(labelPopravkeMjesec))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71)
                    .addComponent(labelPopravkeGodina))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(labelPopravkeInterval))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21.setBackground(new java.awt.Color(102, 153, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fakture", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel80.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("Plaćenih faktura:");

        jLabel81.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("Neplaćenih faktura:");

        jLabel82.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(255, 255, 255));
        jLabel82.setText("Ukupno:");

        jLabel83.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setText("10");

        jLabel84.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(255, 255, 255));
        jLabel84.setText("15");

        jLabel85.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(255, 255, 255));
        jLabel85.setText("5");

        jLabel90.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel90.setForeground(new java.awt.Color(255, 255, 255));
        jLabel90.setText("Interval:");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel90)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel80)
                            .addComponent(jLabel82)
                            .addComponent(jLabel81))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel85)
                            .addComponent(jLabel84)
                            .addComponent(jLabel83))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(jLabel83))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(jLabel85))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(jLabel84))
                .addGap(18, 18, 18)
                .addComponent(jLabel90)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(102, 153, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Odabir vremenskog intervala:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Do:");

        buttonPregled.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        buttonPregled.setText("Pregled");
        buttonPregled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPregledActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Od:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addGap(18, 18, 18)
                                .addComponent(jDateChooserDatumDo, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addGap(18, 18, 18)
                                .addComponent(jDateChooserDatumOd, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(buttonPregled)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooserDatumOd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jDateChooserDatumDo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(buttonPregled)
                .addGap(94, 94, 94))
        );

        jPanel30.setBackground(new java.awt.Color(102, 153, 255));
        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Zarada od prodaje dijelova", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel91.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(255, 255, 255));
        jLabel91.setText("Dnevna:");

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(255, 255, 255));
        jLabel93.setText("Mjesečna:");

        jLabel94.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(255, 255, 255));
        jLabel94.setText("Godišnja:");

        labelDnevnaZaradaDijelovi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelDnevnaZaradaDijelovi.setForeground(new java.awt.Color(255, 255, 255));
        labelDnevnaZaradaDijelovi.setText(" KM");

        labelGodisnjaZaradaDijelovi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelGodisnjaZaradaDijelovi.setForeground(new java.awt.Color(255, 255, 255));
        labelGodisnjaZaradaDijelovi.setText(" KM");

        labelMjesecnaZaradaDijelovi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelMjesecnaZaradaDijelovi.setForeground(new java.awt.Color(255, 255, 255));
        labelMjesecnaZaradaDijelovi.setText(" KM");

        jLabel101.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(255, 255, 255));
        jLabel101.setText("Interval:");

        labelIntervalZaradaDijelovi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        labelIntervalZaradaDijelovi.setForeground(new java.awt.Color(255, 255, 255));
        labelIntervalZaradaDijelovi.setText(" KM");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel91)
                    .addComponent(jLabel94)
                    .addComponent(jLabel93)
                    .addComponent(jLabel101))
                .addGap(33, 33, 33)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelIntervalZaradaDijelovi)
                    .addComponent(labelMjesecnaZaradaDijelovi)
                    .addComponent(labelGodisnjaZaradaDijelovi)
                    .addComponent(labelDnevnaZaradaDijelovi))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel91)
                    .addComponent(labelDnevnaZaradaDijelovi))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel93)
                    .addComponent(labelMjesecnaZaradaDijelovi))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(labelGodisnjaZaradaDijelovi))
                .addGap(18, 18, 18)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(labelIntervalZaradaDijelovi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel86.setBackground(new java.awt.Color(255, 255, 255));
        jLabel86.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(229, 229, 229));
        jLabel86.setText("> Statistika");

        jLabel87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Combo Chart_20px.png"))); // NOI18N

        javax.swing.GroupLayout statistikaHeaderjPanelLayout = new javax.swing.GroupLayout(statistikaHeaderjPanel);
        statistikaHeaderjPanel.setLayout(statistikaHeaderjPanelLayout);
        statistikaHeaderjPanelLayout.setHorizontalGroup(
            statistikaHeaderjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statistikaHeaderjPanelLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel86)
                .addContainerGap(3016, Short.MAX_VALUE))
            .addGroup(statistikaHeaderjPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        statistikaHeaderjPanelLayout.setVerticalGroup(
            statistikaHeaderjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statistikaHeaderjPanelLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(statistikaHeaderjPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel86)
                    .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelGrafikPrihodiUkupno.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelGrafikPrihodiUkupnoLayout = new javax.swing.GroupLayout(panelGrafikPrihodiUkupno);
        panelGrafikPrihodiUkupno.setLayout(panelGrafikPrihodiUkupnoLayout);
        panelGrafikPrihodiUkupnoLayout.setHorizontalGroup(
            panelGrafikPrihodiUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 977, Short.MAX_VALUE)
        );
        panelGrafikPrihodiUkupnoLayout.setVerticalGroup(
            panelGrafikPrihodiUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Ukupni prihodi", panelGrafikPrihodiUkupno);

        PanelGrafikPrihodiDijelovi.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout PanelGrafikPrihodiDijeloviLayout = new javax.swing.GroupLayout(PanelGrafikPrihodiDijelovi);
        PanelGrafikPrihodiDijelovi.setLayout(PanelGrafikPrihodiDijeloviLayout);
        PanelGrafikPrihodiDijeloviLayout.setHorizontalGroup(
            PanelGrafikPrihodiDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 977, Short.MAX_VALUE)
        );
        PanelGrafikPrihodiDijeloviLayout.setVerticalGroup(
            PanelGrafikPrihodiDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Prihodi od dijelova", PanelGrafikPrihodiDijelovi);

        panelGrafikAuta.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelGrafikAutaLayout = new javax.swing.GroupLayout(panelGrafikAuta);
        panelGrafikAuta.setLayout(panelGrafikAutaLayout);
        panelGrafikAutaLayout.setHorizontalGroup(
            panelGrafikAutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 977, Short.MAX_VALUE)
        );
        panelGrafikAutaLayout.setVerticalGroup(
            panelGrafikAutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Auta na stanju", panelGrafikAuta);

        panelGrafikFakture.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelGrafikFaktureLayout = new javax.swing.GroupLayout(panelGrafikFakture);
        panelGrafikFakture.setLayout(panelGrafikFaktureLayout);
        panelGrafikFaktureLayout.setHorizontalGroup(
            panelGrafikFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 977, Short.MAX_VALUE)
        );
        panelGrafikFaktureLayout.setVerticalGroup(
            panelGrafikFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Fakture", panelGrafikFakture);

        panelGrafikPopravke.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelGrafikPopravkeLayout = new javax.swing.GroupLayout(panelGrafikPopravke);
        panelGrafikPopravke.setLayout(panelGrafikPopravkeLayout);
        panelGrafikPopravkeLayout.setHorizontalGroup(
            panelGrafikPopravkeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 977, Short.MAX_VALUE)
        );
        panelGrafikPopravkeLayout.setVerticalGroup(
            panelGrafikPopravkeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 518, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Broj popravljenih auta", panelGrafikPopravke);

        odabirMjesecaStatistikaPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setText("Odaberite mjesec:");

        jLabel33.setText("Odaberite godinu:");

        comboBoxMjesec.setMaximumRowCount(13);
        comboBoxMjesec.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar" }));

        comboBoxGodina.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "2017", "2018" }));

        buttonPregledGrafik.setText("Pregled");
        buttonPregledGrafik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPregledGrafikActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout odabirMjesecaStatistikaPanelLayout = new javax.swing.GroupLayout(odabirMjesecaStatistikaPanel);
        odabirMjesecaStatistikaPanel.setLayout(odabirMjesecaStatistikaPanelLayout);
        odabirMjesecaStatistikaPanelLayout.setHorizontalGroup(
            odabirMjesecaStatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(odabirMjesecaStatistikaPanelLayout.createSequentialGroup()
                .addGroup(odabirMjesecaStatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(odabirMjesecaStatistikaPanelLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(odabirMjesecaStatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel33))
                        .addGap(40, 40, 40)
                        .addGroup(odabirMjesecaStatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboBoxGodina, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxMjesec, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(odabirMjesecaStatistikaPanelLayout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(buttonPregledGrafik)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        odabirMjesecaStatistikaPanelLayout.setVerticalGroup(
            odabirMjesecaStatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(odabirMjesecaStatistikaPanelLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(odabirMjesecaStatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(comboBoxMjesec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(odabirMjesecaStatistikaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(comboBoxGodina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(buttonPregledGrafik)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statistikajPanelLayout = new javax.swing.GroupLayout(statistikajPanel);
        statistikajPanel.setLayout(statistikajPanelLayout);
        statistikajPanelLayout.setHorizontalGroup(
            statistikajPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statistikaHeaderjPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(statistikajPanelLayout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 982, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(odabirMjesecaStatistikaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        statistikajPanelLayout.setVerticalGroup(
            statistikajPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statistikajPanelLayout.createSequentialGroup()
                .addComponent(statistikaHeaderjPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(statistikajPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statistikajPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(statistikajPanelLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(odabirMjesecaStatistikaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 988, Short.MAX_VALUE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Ukupni prihodi");

        parentPanel.add(statistikajPanel, "card5");

        jScrollPane1.setViewportView(parentPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 2621, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1572, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1561, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menu2jPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu2jPanelMouseClicked
        
        menuItemClick(menu2jPanel, 1, radniNaloziPanel);
        

    }//GEN-LAST:event_menu2jPanelMouseClicked

    private void menu2jPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu2jPanelMouseEntered
        // TODO add your handling code here:
        setColor(menu2jPanel);
    }//GEN-LAST:event_menu2jPanelMouseEntered

    private void menu2jPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu2jPanelMouseExited
        // TODO add your handling code here:
        if (!menu[1]) {
            resetColor(menu2jPanel);
        }
    }//GEN-LAST:event_menu2jPanelMouseExited

    private void menu2jPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu2jPanelMousePressed

    }//GEN-LAST:event_menu2jPanelMousePressed

    private void menu3jPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu3jPanelMouseClicked
        
        menuItemClick(menu3jPanel, 2, vozilaPanel);
        loadAutosuggester();
        tableVozila.getTableHeader().setReorderingAllowed(false);
        tableVozila.setDefaultEditor(Object.class, null);

    }//GEN-LAST:event_menu3jPanelMouseClicked

    private void menu3jPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu3jPanelMouseEntered
        // TODO add your handling code here:
        setColor(menu3jPanel);
    }//GEN-LAST:event_menu3jPanelMouseEntered

    private void menu3jPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu3jPanelMouseExited
        if (!menu[2]) {
            resetColor(menu3jPanel);
        }
    }//GEN-LAST:event_menu3jPanelMouseExited

    private void menu4jPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu4jPanelMouseClicked
        menuItemClick(menu4jPanel, 3, knjigovodstvoPanel);
        uslugeKnjigovodstva();

    }//GEN-LAST:event_menu4jPanelMouseClicked

    private void menu4jPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu4jPanelMouseEntered
        setColor(menu4jPanel);
    }//GEN-LAST:event_menu4jPanelMouseEntered

    private void menu4jPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu4jPanelMouseExited
        if (!menu[3]) {
            resetColor(menu4jPanel);
        }
    }//GEN-LAST:event_menu4jPanelMouseExited

    private void menu7jPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu7jPanelMouseClicked
        
        menuItemClick(menu7jPanel, 6, statistikajPanel);
        statistikaLogika.loadGraph(this);
        statistikaLogika.loadStatistics(this);
        
    

    }//GEN-LAST:event_menu7jPanelMouseClicked

    private void menu7jPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu7jPanelMouseEntered
        // TODO add your handling code here:
        setColor(menu7jPanel);
    }//GEN-LAST:event_menu7jPanelMouseEntered

    private void menu7jPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu7jPanelMouseExited
        if (!menu[6]) {
            resetColor(menu7jPanel);
        }
    }//GEN-LAST:event_menu7jPanelMouseExited

    private void menu7jPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu7jPanelMousePressed
        setColor(menu7jPanel);
    }//GEN-LAST:event_menu7jPanelMousePressed

    private void menu5jPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu5jPanelMouseClicked
       
        menuItemClick(menu5jPanel, 4, zaposleniPanel);

       
    }//GEN-LAST:event_menu5jPanelMouseClicked

    private void menu5jPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu5jPanelMouseEntered
        setColor(menu5jPanel);
    }//GEN-LAST:event_menu5jPanelMouseEntered

    private void menu5jPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu5jPanelMouseExited
        if (!menu[4]) {
            resetColor(menu5jPanel);
        }
    }//GEN-LAST:event_menu5jPanelMouseExited

    private void menu5jPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu5jPanelMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_menu5jPanelMousePressed

    private void menu6jPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu6jPanelMouseClicked
       
       menuItemClick(menu6jPanel, 5, dijeloviPanel);
       loadDijeloviForm();
        
    }//GEN-LAST:event_menu6jPanelMouseClicked

    private void menu6jPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu6jPanelMouseEntered
        setColor(menu6jPanel);
    }//GEN-LAST:event_menu6jPanelMouseEntered

    private void menu6jPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu6jPanelMouseExited
        if (!menu[5]) {
            resetColor(menu6jPanel);
        }
    }//GEN-LAST:event_menu6jPanelMouseExited

    private void jLabel4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLabel4PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4PropertyChange

    private void menu1jPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu1jPanelMouseClicked
       
        menuItemClick(menu1jPanel, 0, pocetnajPanel);
        
      
    }//GEN-LAST:event_menu1jPanelMouseClicked

    private void menu1jPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu1jPanelMouseEntered
        setColor(menu1jPanel);
    }//GEN-LAST:event_menu1jPanelMouseEntered

    private void menu1jPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu1jPanelMouseExited
        if (!menu[0]) {
            resetColor(menu1jPanel);
        }
    }//GEN-LAST:event_menu1jPanelMouseExited

    private void menu1jPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu1jPanelMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_menu1jPanelMousePressed

    private void menu8jPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu8jPanelMouseClicked
        menuItemClick(menu8jPanel, 7, zakazivanjaPanel);
        uslugeZakazivanja();
    }//GEN-LAST:event_menu8jPanelMouseClicked

    private void menu8jPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu8jPanelMouseEntered
        setColor(menu8jPanel);
    }//GEN-LAST:event_menu8jPanelMouseEntered

    private void menu8jPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu8jPanelMouseExited
        if (!menu[7])
        resetColor(menu8jPanel);    }//GEN-LAST:event_menu8jPanelMouseExited

    private void menu8jPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menu8jPanelMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_menu8jPanelMousePressed

    private void buttonDodajZaposlenogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDodajZaposlenogActionPerformed
        new ZaposleniLogika("insert", textFieldIme.getText(), textFieldPrezime.getText(), textFieldTelefon.getText(),
                textFieldAdresa.getText(), textFieldStrucnaSprema.getText(), textFieldImeOca.getText(),
                textFieldBrojLicneKarte.getText(), dateChooserDatumRodjenja.getDate(), dateChooserDatumPrimanjaURadniOdnos.getDate(),
                textFieldFunkcijaRadnika.getText()).start();
    }//GEN-LAST:event_buttonDodajZaposlenogActionPerformed

    private void jTableZaposleniMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableZaposleniMouseReleased
        int r = jTableZaposleni.rowAtPoint(evt.getPoint());
        if (r >= 0 && r < jTableZaposleni.getRowCount()) {
            jTableZaposleni.setRowSelectionInterval(r, r);
        } else {
            jTableZaposleni.clearSelection();
        }

        int rowindex = jTableZaposleni.getSelectedRow();
        if (rowindex < 0) {
            return;
        }
        if (evt.isPopupTrigger()) {
            popupMenuZaposleni.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jTableZaposleniMouseReleased

    private void menuItemDetaljniOpisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDetaljniOpisActionPerformed
        int redniBroj = jTableZaposleni.getSelectedRow();
        if (redniBroj != -1) {
            ArrayList<ZaposleniDTO> zaposleni = new ArrayList<ZaposleniDTO>(DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni());
            ZaposleniDTO selektovanRadnik = zaposleni.get(redniBroj);
            new DetaljiZaposlenogDialog(this, true, false, selektovanRadnik).show();
        } else {
            JOptionPane.showMessageDialog(null, "Niste odabrali zaposlenog!", "Problem", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_menuItemDetaljniOpisActionPerformed

    private void menuItemIzmjeniRadnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemIzmjeniRadnikaActionPerformed
        int redniBroj = jTableZaposleni.getSelectedRow();
        if (redniBroj != -1) {
            ArrayList<ZaposleniDTO> zaposleni = new ArrayList<ZaposleniDTO>(DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni());
            ZaposleniDTO selektovanRadnik = zaposleni.get(redniBroj);
            new DetaljiZaposlenogDialog(this, true, true, selektovanRadnik).show();
        } else {
            JOptionPane.showMessageDialog(null, "Niste odabrali zaposlenog!", "Problem", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_menuItemIzmjeniRadnikaActionPerformed

    private void menuItemOtpustiRadnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOtpustiRadnikaActionPerformed
        int redniBroj = jTableZaposleni.getSelectedRow();
        if (redniBroj != -1) {
            ArrayList<ZaposleniDTO> zaposleni = new ArrayList<ZaposleniDTO>(DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni());
            ZaposleniDTO selektovanRadnik = zaposleni.get(redniBroj);
            new OtpustiRadnikaDialog(this, true, selektovanRadnik).show();
        } else {
            JOptionPane.showMessageDialog(null, "Niste odabrali zaposlenog!", "Problem", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_menuItemOtpustiRadnikaActionPerformed

    private void buttonPrikazSvihBivsihRadnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrikazSvihBivsihRadnikaActionPerformed
        new SviBivsiZaposleniDialog(this, true).show();
    }//GEN-LAST:event_buttonPrikazSvihBivsihRadnikaActionPerformed

    private void buttonTraziRadneNalogeRadnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTraziRadneNalogeRadnikaActionPerformed
        new ZaposleniLogika("statistika", new java.sql.Date(dateChooserDatumOdZaposlenog.getDate().getTime()), new java.sql.Date(dateChooserDatumDoZaposlenog.getDate().getTime()),labelBrojRadnihNaloga,labelOstvareniProfitRadnika).run();
    }//GEN-LAST:event_buttonTraziRadneNalogeRadnikaActionPerformed

    private void rbPravnoTraziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPravnoTraziActionPerformed
        tfImeTrazi.setEditable(false);
        tfPrezimeTrazi.setEditable(false);
        tfImeTrazi.setText("");
        tfPrezimeTrazi.setText("");
        tfImeTrazi.setBackground(Color.gray);
        tfPrezimeTrazi.setBackground(Color.gray);
        tfNazivTrazi.setBackground(Color.white);
        tfNazivTrazi.setEditable(true);
    }//GEN-LAST:event_rbPravnoTraziActionPerformed

    private void rbPrivatnoTraziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPrivatnoTraziActionPerformed

        tfNazivTrazi.setText("");
        tfNazivTrazi.setEditable(false);
        tfNazivTrazi.setBackground(Color.gray);
        tfImeTrazi.setBackground(Color.white);
        tfPrezimeTrazi.setBackground(Color.white);
        tfImeTrazi.setEditable(true);
        tfPrezimeTrazi.setEditable(true);
        rbPrivatnoTrazi.setSelected(true);

        rbPravnoTrazi.setSelected(false);

        rbPrivatnoTrazi.setSelected(false);

    }//GEN-LAST:event_rbPrivatnoTraziActionPerformed

    public void prikaziKupceUTabeli(ArrayList<KupacDTO> kupci) {
        String[] columns = {"ID", "Ime", "Prezime", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tableVozila.setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), k.getIme(), k.getPrezime(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }
        tableVozila.setModel(model);
    }

    private void btnTraziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraziActionPerformed
        izbrisiPopupZaVozila();
        ucitajPopupZaVlasnike();
        String izabrano = "";

        for (Enumeration<AbstractButton> buttons = bGTraziVlasnika.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                izabrano = button.getText();
            }
        }

        if ("Privatno lice".equals(izabrano)) {
            String ime = tfImeTrazi.getText();
            String prezime = tfPrezimeTrazi.getText();

            if ((ime == null || "".equals(ime)) && (prezime == null || "".equals(prezime))) {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviPrivatni();

                prikaziKupceUTabeli(kupci);

            } else if (prezime == null || "".equals(prezime)) {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciIme(ime);

                prikaziKupceUTabeli(kupci);
            } else if (ime == null || "".equals(ime)) {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPrezime(prezime);

                prikaziKupceUTabeli(kupci);
            } else {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPrivatni(ime, prezime);

                prikaziKupceUTabeli(kupci);
            }
        } else if ("Pravno lice".equals(izabrano)) {
            String naziv = tfNazivTrazi.getText();

            if (naziv == null || "".equals(naziv)) {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviPravni();

                prikaziKupcePravneUTabeli(kupci);
            } else {
                ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPravni(naziv);

                prikaziKupcePravneUTabeli(kupci);
            }
        }


    }//GEN-LAST:event_btnTraziActionPerformed

    private void btnPrikaziSveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziSveActionPerformed
        izbrisiPopupZaVozila();
        ucitajPopupZaVlasnike();
        ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviKupci();
        prikaziKupceSveUTabeli(kupci);
    }//GEN-LAST:event_btnPrikaziSveActionPerformed

    private void btnPonistiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPonistiActionPerformed
        tfPrezimeTrazi.setText("");
        tfImeTrazi.setText("");
        tfNazivTrazi.setText("");
        rbPrivatnoTrazi.setSelected(true);
        rbPravnoTrazi.setSelected(true);
        tfNazivTrazi.setEditable(false);
        tfNazivTrazi.setBackground(Color.gray);
        tfImeTrazi.setBackground(Color.white);
        tfPrezimeTrazi.setBackground(Color.white);
        rbPravnoTrazi.setSelected(false);
        rbPrivatnoTrazi.setSelected(true);
    }//GEN-LAST:event_btnPonistiActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new DodajModel(this, true).setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        new DodajVlasnikaDialog(this, true).setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new DodajVoziloDialog(this, true).setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new IzmijeniIzbrisiModelDialog(new JFrame(), true).setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void buttonPregledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPregledActionPerformed
        // new StatistikaLogika(new java.sql.Date(jDateChooserDatumOd.getDate().getTime()), new java.sql.Date(jDateChooserDatumDo.getDate().getTime())).run();
        NumberFormat formatter = new DecimalFormat("#0.00");

        labelIntervalZarada.setText(formatter.format(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new java.sql.Date(jDateChooserDatumOd.getDate().getTime()), new java.sql.Date(jDateChooserDatumDo.getDate().getTime()))
                + DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(jDateChooserDatumOd.getDate().getTime()), new Date(jDateChooserDatumDo.getDate().getTime()))) + " KM");

        labelIntervalZaradaDijelovi.setText(formatter.format(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(jDateChooserDatumOd.getDate().getTime()), new Date(jDateChooserDatumDo.getDate().getTime()))) + " KM");

        labelPopravkeInterval.setText(DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPopravki(new Date(jDateChooserDatumOd.getDate().getTime()), new Date(jDateChooserDatumDo.getDate().getTime())) + "");

    }//GEN-LAST:event_buttonPregledActionPerformed

    private void buttonPregledGrafikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPregledGrafikActionPerformed
        statistikaLogika.loadGraph(this);
    }//GEN-LAST:event_buttonPregledGrafikActionPerformed

    private void cbSviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSviActionPerformed
        if (cbSvi.isSelected()) {
            bGTraziVozilo.clearSelection();
            tfImeVozilo.setEditable(false);
            tfImeVozilo.setBackground(Color.gray);
            tfImeVozilo.setText("");
            tfPrezimeVozilo.setEditable(false);
            tfPrezimeVozilo.setBackground(Color.gray);
            tfPrezimeVozilo.setText("");
            tfNazivVozilo.setEditable(false);
            tfNazivVozilo.setBackground(Color.gray);
            tfNazivVozilo.setText("");
        } else {
            rbPrivatnoLiceVozilo.setSelected(true);
            rbPravnoLiceVozilo.setSelected(false);
            tfImeVozilo.setEditable(true);
            tfImeVozilo.setText("");
            tfImeVozilo.setBackground(Color.white);
            tfPrezimeVozilo.setEditable(true);
            tfPrezimeVozilo.setText("");
            tfPrezimeVozilo.setBackground(Color.white);
        }
    }//GEN-LAST:event_cbSviActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        izbrisiPopupZaVlasnike();
        ucitajPopupZaVozila();

        ArrayList<VoziloDTO> vozila = VozilaLogika.vozila = DAOFactory.getDAOFactory().getVoziloDAO().svaVozila();

        String[] columns = {"ID", "Registracija", "Marka", "Model", "Godiste", "Prezime", "Ime", "Naziv", "Gorivo", "Kilovat", "Kubikaza"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tableVozila.setModel(model);

        for (VoziloDTO v : vozila) {
            KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(v.getIdKupac());
            ModelVozilaDTO mod = new ModelVozilaDTO();
            for (ModelVozilaDTO m : VozilaLogika.modeli) {
                if (m.getIdModelVozila() == v.getIdModelVozila()) {
                    mod = m;
                }
            }

            Object[] rowData = {v.getIdVozilo(), v.getBrojRegistracije()==null?"":v.getBrojRegistracije(), mod.getMarka(), mod.getModel(), v.getGodiste()==null?"":v.getGodiste(), (kupac.getPrezime() == null || "".equals(kupac.getPrezime())) ? "---" : kupac.getPrezime(), (kupac.getIme() == null || "".equals(kupac.getIme())) ? "---" : kupac.getIme(), (kupac.getNaziv() == null || "".equals(kupac.getNaziv())) ? "---" : kupac.getNaziv(), v.getVrstaGoriva()==null?"":v.getVrstaGoriva(), v.getKilovat()==null?"":v.getKilovat(), v.getKubikaza()==null?"":v.getVrstaGoriva()};
            model.addRow(rowData);
        }

        tableVozila.setModel(model);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rbPravnoLiceVoziloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPravnoLiceVoziloActionPerformed
        tfNazivVozilo.setEditable(true);
        tfImeVozilo.setEditable(false);
        tfPrezimeVozilo.setEditable(false);
        tfImeVozilo.setText("");
        tfPrezimeVozilo.setText("");
        tfImeVozilo.setBackground(Color.gray);
        tfPrezimeVozilo.setBackground(Color.gray);
        tfNazivVozilo.setBackground(Color.white);
        rbPrivatnoLiceVozilo.setSelected(false);
    }//GEN-LAST:event_rbPravnoLiceVoziloActionPerformed

    private void rbPrivatnoLiceVoziloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPrivatnoLiceVoziloActionPerformed

        tfNazivVozilo.setEditable(false);
        tfNazivVozilo.setText("");
        tfImeVozilo.setEditable(true);
        tfPrezimeVozilo.setEditable(true);
        tfNazivVozilo.setBackground(Color.gray);
        rbPravnoLiceVozilo.setSelected(false);
        tfImeVozilo.setBackground(Color.white);
        tfPrezimeVozilo.setBackground(Color.white);
    }//GEN-LAST:event_rbPrivatnoLiceVoziloActionPerformed

    private void btnPonistiSveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPonistiSveActionPerformed
        tfRegistracijaTrazi.setText("");
        tfGodisteTrazi.setText("");
        tfMarkaTrazi.setText("");
        tfModelTrazi.setText("");
        tfPrezimeVozilo.setText("");
        tfImeVozilo.setText("");
        tfNazivVozilo.setText("");
        rbPrivatnoLiceVozilo.setSelected(true);
        rbPravnoLiceVozilo.setSelected(false);
        tfNazivVozilo.setEditable(false);
        tfNazivVozilo.setBackground(Color.gray);
    }//GEN-LAST:event_btnPonistiSveActionPerformed

    private void btnPronadjiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPronadjiActionPerformed
        izbrisiPopupZaVlasnike();
        ucitajPopupZaVozila();

        String registracija = tfRegistracijaTrazi.getText();
        Integer godiste = 0;

        if (tfGodisteTrazi.getText() != null && !"".equals(tfGodisteTrazi.getText())) {
            try {
                godiste = Integer.parseInt(tfGodisteTrazi.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Pogresno godiste.", "Obavjestenje", JOptionPane.ERROR_MESSAGE);
            }
        }

        String marka = tfMarkaTrazi.getText();
        String model = tfModelTrazi.getText();
        boolean svi = cbSvi.isSelected();

        if (svi) {

            String[] columns = {"ID", "Registracija", "Marka", "Model", "Godiste", "Prezime", "Ime", "Naziv", "Gorivo", "Kilovat", "Kubikaza"};
            DefaultTableModel modell = new DefaultTableModel(columns, 0);
            tableVozila.setModel(modell);

            ArrayList<VoziloDTO> vozila = VozilaLogika.vozila = DAOFactory.getDAOFactory().getVoziloDAO().svaVozila();
            for (VoziloDTO v : vozila) {

                KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(v.getIdKupac());
                ModelVozilaDTO mod = new ModelVozilaDTO();
                for (ModelVozilaDTO m : VozilaLogika.modeli) {
                    if (m.getIdModelVozila() == v.getIdModelVozila()) {
                        mod = m;
                    }
                }

                boolean dodati = true;
                //System.out.println(registracija + " " + v.getBrojRegistracije());
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
                    Object[] rowData = {v.getIdVozilo(), v.getBrojRegistracije()==null?"":v.getBrojRegistracije(), mod.getMarka(), mod.getModel(), v.getGodiste()==null?"":v.getGodiste(), (kupac.getPrezime() == null || "".equals(kupac.getPrezime())) ? "---" : kupac.getPrezime(), (kupac.getIme() == null || "".equals(kupac.getIme())) ? "---" : kupac.getIme(), (kupac.getNaziv() == null || "".equals(kupac.getNaziv())) ? "---" : kupac.getNaziv(), v.getVrstaGoriva()==null?"":v.getVrstaGoriva(), v.getKilovat()==null?"":v.getKilovat(), v.getKubikaza()==null?"":v.getKubikaza()};
                    modell.addRow(rowData);
                    tableVozila.setModel(modell);
                }
            }
        } else {

            String[] columns = {"ID", "Registracija", "Marka", "Model", "Godiste", "Prezime", "Ime", "Gorivo", "Kilovat", "Kubikaza"};
            DefaultTableModel modell = new DefaultTableModel(columns, 0);
            tableVozila.setModel(modell);

            ArrayList<VoziloDTO> vozila = VozilaLogika.vozila;
            for (VoziloDTO v : vozila) {

                KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(v.getIdKupac());
                ModelVozilaDTO mod = new ModelVozilaDTO();
                for (ModelVozilaDTO m : VozilaLogika.modeli) {
                    if (m.getIdModelVozila() == v.getIdModelVozila()) {
                        mod = m;
                    }
                }

                boolean dodati = true;
                //System.out.println(registracija + " " + v.getBrojRegistracije());
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

                    if (rbPrivatnoLiceVozilo.isSelected()) {
                        String ime = tfImeVozilo.getText();
                        String prezime = tfPrezimeVozilo.getText();

                        if (ime != null && !"".equals(ime)) {
                            if (!ime.equals(kupac.getIme())) {
                                dodati = false;
                            }
                        }
                        if (prezime != null && !"".equals(prezime)) {
                            if (!prezime.equals(kupac.getPrezime())) {
                                dodati = false;
                            }
                        }
                    } else if (rbPravnoLiceVozilo.isSelected()) {
                        String naziv = tfNazivVozilo.getText();
                        if (naziv != null && !"".equals(naziv)) {
                            if (!naziv.equals(kupac.getNaziv())) {
                                dodati = false;
                            }
                        }
                    }

                    Object[] rowData = {v.getIdVozilo(), v.getBrojRegistracije()==null?"":v.getBrojRegistracije(), mod.getMarka(), mod.getModel(), v.getGodiste() == null ? "":v.getGodiste(), (kupac.getPrezime() == null || "".equals(kupac.getPrezime())) ? "---" : kupac.getPrezime(), (kupac.getIme() == null || "".equals(kupac.getIme())) ? "---" : kupac.getIme(), v.getVrstaGoriva()==null?"":v.getVrstaGoriva(), v.getKilovat()==null?"":v.getKilovat(), v.getKubikaza()==null?"":v.getKubikaza()};
                    modell.addRow(rowData);
                    tableVozila.setModel(modell);

                }
            }

        }

    }//GEN-LAST:event_btnPronadjiActionPerformed

    private void tfModelTraziFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfModelTraziFocusGained
        ucitajPreporukeModel();
    }//GEN-LAST:event_tfModelTraziFocusGained

    private void btnDodajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajActionPerformed
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
        
        sifra = jtfSifra.getText();
        if("".equals(sifra)){
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(this, "Unesite šifru", "Upozorenje", JOptionPane.INFORMATION_MESSAGE);
            flag = true;
        }
        naziv = jtfNaziv.getText();
        if("".equals(naziv)){
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(this, "Unesite naziv", "Upozorenje", JOptionPane.INFORMATION_MESSAGE);            
            flag = true;
        }
        try{
            cijena = Double.parseDouble(jtfCijena.getText());
        }catch(NumberFormatException e){
            if(!"".equals(jtfCijena.getText())){
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format cijene!", "Greška", JOptionPane.ERROR_MESSAGE);      
            }else{
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Unesite cijenu!", "Greška", JOptionPane.ERROR_MESSAGE);
            }
            flag = true;
        }
        try {
            godiste = Integer.parseInt(jtfGodiste.getText());
        } catch (NumberFormatException e) {
            if (!"".equals(jtfGodiste.getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format godišta!!!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
            }
            godiste = null;
        }
        stanje = cbStanje.isSelected();
        gorivo = (String) jcbGorivo.getSelectedItem();
        marka = (String) jcbMarka.getSelectedItem();
        model = (String) jcbModel.getSelectedItem();
        try {
            kolicina = Integer.parseInt(jtfKolicina.getText());
        } catch (NumberFormatException e) {
            if (!"".equals(jtfKolicina.getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format količine!!!", "Greška", JOptionPane.ERROR_MESSAGE);                
            }else{
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Unesite količinu!", "Greška", JOptionPane.ERROR_MESSAGE);
            }
            flag = true;
        }
        if (!flag) {
            DioDTO noviDio = new DioDTO(sifra, naziv, gorivo, godiste, stanje, cijena, kolicina, true, marka, model);
            DioDTO dio = DAOFactory.getDAOFactory().getDioDAO().dio(noviDio);
            ModelVozilaDTO modVoz = DAOFactory.getDAOFactory().getModelVozilaDAO().model(marka, model);
            DioModelVozilaDTO dmv = null;
            if (dio != null && modVoz != null) {
                dmv = DAOFactory.getDAOFactory().getDioModelVozilaDAO().getDioModelVozila(dio.getId(),
                        modVoz.getIdModelVozila());
            }

            if (dmv != null) {
                //DioDTO dioo = DAOFactory.getDAOFactory().getDioDAO().getDioo(noviDio.getSifra(), noviDio.getNaziv());
                //noviDio.setId(dioo.getId());
                //DAOFactory.getDAOFactory().getDioDAO().azurirajDio(noviDio)) {
                    JOptionPane jop = new JOptionPane();
                    jop.showMessageDialog(this, "Dio sa ovom šifrom već postoji.", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                
            } else {
                if (DAOFactory.getDAOFactory().getDioDAO().dodajDio(noviDio)) {
                    if (modVoz == null) {
                        //System.out.println("dodje");
                        DAOFactory.getDAOFactory().getModelVozilaDAO().dodajModel(new ModelVozilaDTO(marka, model));
                    }
                    System.out.println("novi "+ noviDio.getSifra() + " " + noviDio.getId());
                    DioDTO dioo = DAOFactory.getDAOFactory().getDioDAO().dio(noviDio);
                    //System.out.println("dioooo" + dioo.getId());
                    ModelVozilaDTO mv = DAOFactory.getDAOFactory().getModelVozilaDAO().model(marka, model);
                   // System.out.println("model vozila " + mv.getIdModelVozila());
                    DAOFactory.getDAOFactory().getDioModelVozilaDAO().dodajDioModelVozila(dioo.getId(), mv.getIdModelVozila());
                    JOptionPane jop = new JOptionPane();
                    jop.showMessageDialog(this, "Uspješno dodan dio!!!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                    DefaultTableModel dtm = (DefaultTableModel) jTable.getModel();
                    dtm.addRow(new Object[]{dioo.getId(), sifra, naziv, marka, model, godiste, gorivo, cijena, kolicina, stanje ? "Da" : "Ne"});
                }
            }
        }
        /*DioDTO dio = new DioDTO(sifra, naziv, gorivo, godiste, stanje, cijena, kolicina, true, marka, model);
        DAOFactory.getDAOFactory().getDioDAO().dodajDio(dio);*/
    }//GEN-LAST:event_btnDodajActionPerformed

    private void btnPretraziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPretraziActionPerformed
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
        
        sifra = tfSifra.getText();
        naziv = tfNaziv.getText();
        try{
            id = Integer.parseInt(tfId.getText());
        }catch(NumberFormatException e){
            if(!"".equals(tfId.getText())){
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format cijene!", "Greška", JOptionPane.ERROR_MESSAGE);      
            }
            id = 0;
            //flag = true;
        }
        try {
            godiste = Integer.parseInt(tfGodiste.getText());
        } catch (NumberFormatException e) {
            if (!"".equals(tfGodiste.getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format godišta!!!", "Greška", JOptionPane.ERROR_MESSAGE);
                //flag = true;
            }
            godiste = null;
        }
        stanje = cbNovo.isSelected();
        gorivo = (String) cbGorivo.getSelectedItem();
        marka = (String) cbMarka.getSelectedItem();
        model = (String) cbModel.getSelectedItem();
        
        ArrayList<DioDTO> dijelovi = new ArrayList<DioDTO>();
        
        if(!"".equals(tfId.getText())){
            dijelovi.add(DAOFactory.getDAOFactory().getDioDAO().getDio(id));        
        }else if(!"".equals(sifra)){
            dijelovi.add(DAOFactory.getDAOFactory().getDioDAO().getDio(sifra));
        }else if(!"".equals(naziv) && godiste != null && !"Svi".equals(marka) && !"Svi".equals(gorivo)){
            if("Svi".equals(model))
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(naziv, godiste, marka, gorivo, stanje);
            else
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(naziv, godiste, marka, model, gorivo, stanje);
        }else if(!"".equals(naziv) && godiste != null && !"Svi".equals(marka)){
            if("Svi".equals(model))
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(naziv, godiste, marka, stanje);
            else
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovi(naziv, godiste, marka, model, stanje);
        }else if(!"".equals(naziv) && godiste != null){
            if(!"Svi".equals(gorivo))
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovi(naziv, godiste, gorivo, stanje);
            else
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovi(naziv, godiste, stanje);
        }else if(!"".equals(naziv) && !"Svi".equals(marka) && !"Svi".equals(gorivo)){
            if("Svi".equals(model))
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovibg2(naziv, marka, gorivo, stanje);
            else
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovibg(naziv, marka, model, gorivo, stanje);
        }else if(!"".equals(naziv) && !"Svi".equals(marka)){
            if("Svi".equals(model))
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovibg(naziv, marka, stanje);
            else
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovibg(naziv, marka, model, stanje);
        }else if(!"".equals(naziv) && !"Svi".equals(gorivo)){
            dijelovi = DAOFactory.getDAOFactory().getDioDAO().dijelovibg2(naziv, gorivo, stanje);
        }
        else if(!"".equals(naziv)){
            dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijeloviNaziv(naziv, stanje);
        }
        else if(!"Svi".equals(gorivo) && !"Svi".equals(marka)){
            if("Svi".equals(model))
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(gorivo, marka, stanje);
            else
                dijelovi = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(gorivo, marka, model, stanje);
        }
        
        DefaultTableModel dtm = (DefaultTableModel)jTable.getModel();
        dtm.setRowCount(0);
        
        int i = 0;
        if(dijelovi != null)
        while(i < dijelovi.size()){
            DioDTO d = dijelovi.get(i);
            dtm.addRow(new Object[]{d.getId(), d.getSifra(), d.getNaziv(), d.getMarka(), d.getModel(), d.getGodisteVozila(),
                d.getVrstaGoriva(), Math.round(d.getTrenutnaCijena()*100)/100, d.getKolicina(), d.getNovo() ? "Da":"Ne"});
            i++;
        }
    }//GEN-LAST:event_btnPretraziActionPerformed

    private void jLabel36KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel36KeyPressed
        
    }//GEN-LAST:event_jLabel36KeyPressed

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        
    }//GEN-LAST:event_jTableMouseClicked

    private void btnSviDijeloviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSviDijeloviActionPerformed
        ArrayList<DioDTO> dijelovi = DAOFactory.getDAOFactory().getDioDAO().getSviDijelovi();
        
        DefaultTableModel dtm = (DefaultTableModel)jTable.getModel();
        dtm.setRowCount(0);
        
        int i = 0;
        if(dijelovi != null)
        while(i < dijelovi.size()){
            DioDTO d = dijelovi.get(i);
            dtm.addRow(new Object[]{d.getId(), d.getSifra(), d.getNaziv(), d.getMarka(), d.getModel(), d.getGodisteVozila(),
                d.getVrstaGoriva(),  d.getTrenutnaCijena(), d.getKolicina(), d.getNovo() ? "Da":"Ne"});
            i++;
        }
       // if(evt.getButton() == MouseEvent.BUTTON3 && jTable.getSelectedRow() != -1){
            //IzmijeniDioDialog dialog = new IzmijeniDioDialog((DefaultTableModel)jTable.getModel(), jTable.getSelectedRow());
            //dialog.setVisible(true);
        //}
    }//GEN-LAST:event_btnSviDijeloviActionPerformed

    private void btnProdaniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdaniActionPerformed
        new PregledProdanihDijelovaDialog().setVisible(true);
    }//GEN-LAST:event_btnProdaniActionPerformed

    private void btnPronadjiTerminActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPronadjiTerminActionPerformed
    {//GEN-HEADEREND:event_btnPronadjiTerminActionPerformed
        ArrayList<TerminDTO> lista=DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        Date datum=null;
        if(dtmDatumPretraga.getDate()!=null)datum=new Date(dtmDatumPretraga.getDate().getTime());
        filtrirajTermine
        (
            lista,
            tblTermini,
            txtMarkaPretraga.getText(),
            datum,
            txtImePretraga.getText(),
            txtPrezimePretraga.getText(),
            txtBrojTelefonaPretraga.getText()
        );
    }//GEN-LAST:event_btnPronadjiTerminActionPerformed

    private void btnPonistiUnosePretragaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPonistiUnosePretragaActionPerformed
    {//GEN-HEADEREND:event_btnPonistiUnosePretragaActionPerformed
        txtMarkaPretraga.setText("");
        dtmDatumPretraga.setDate(null);
        txtImePretraga.setText("");
        txtPrezimePretraga.setText("");
        txtBrojTelefonaPretraga.setText("");
        ArrayList<TerminDTO> lista=DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, tblTermini);
    }//GEN-LAST:event_btnPonistiUnosePretragaActionPerformed

    private void btnDodajTerminActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDodajTerminActionPerformed
    {//GEN-HEADEREND:event_btnDodajTerminActionPerformed
        Date datum=null;
        if(dtmDatumTermina.getDate()!=null)datum=new Date(dtmDatumTermina.getDate().getTime());
        boolean test=dodajTermin
        (
            datum,
            ""+(txtVrijemeTerminaSati.getValue()),
            ""+(txtVrijemeTerminaMinuti.getValue()),
            txtMarkaTermina.getText(),
            txtModelTermina.getText(),
            txtImeTermina.getText(),
            txtPrezimeTermina.getText(),
            txtBrojTelefonaTermina.getText()
        );
        ArrayList<TerminDTO> lista=DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, tblTermini);
    }//GEN-LAST:event_btnDodajTerminActionPerformed

    private void btnPonistiUnoseTerminActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPonistiUnoseTerminActionPerformed
    {//GEN-HEADEREND:event_btnPonistiUnoseTerminActionPerformed
        ArrayList<TerminDTO> lista=DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, tblTermini);
    }//GEN-LAST:event_btnPonistiUnoseTerminActionPerformed

    private void btnRacunActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRacunActionPerformed
    {//GEN-HEADEREND:event_btnRacunActionPerformed
        prikaziFakturu(tblRadniNalozi, tblFaktura, "Račun");
    }//GEN-LAST:event_btnRacunActionPerformed

    private void btnPredracunActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPredracunActionPerformed
    {//GEN-HEADEREND:event_btnPredracunActionPerformed
        prikaziFakturu(tblRadniNalozi, tblFaktura, "Faktura");
    }//GEN-LAST:event_btnPredracunActionPerformed

    private void tblRadniNaloziMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblRadniNaloziMouseClicked
    {//GEN-HEADEREND:event_tblRadniNaloziMouseClicked
        stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
        provjeraZaDugmiceZaTabeluNaloga();
    }//GEN-LAST:event_tblRadniNaloziMouseClicked

    private void btnNefakturisanoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNefakturisanoActionPerformed
    {//GEN-HEADEREND:event_btnNefakturisanoActionPerformed
        ArrayList<RadniNalogDTO> lista=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        nefakturisaniRadniNalozi(lista, tblRadniNalozi);
    }//GEN-LAST:event_btnNefakturisanoActionPerformed

    private void btnFakturisanoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFakturisanoActionPerformed
    {//GEN-HEADEREND:event_btnFakturisanoActionPerformed
        ArrayList<RadniNalogDTO> lista=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        fakturisaniRadniNalozi(lista, tblRadniNalozi);
    }//GEN-LAST:event_btnFakturisanoActionPerformed

    private void btnPoIDuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPoIDuActionPerformed
    {//GEN-HEADEREND:event_btnPoIDuActionPerformed
        ArrayList<RadniNalogDTO> lista=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        poIDuRadniNalozi(lista, tblRadniNalozi, txtID);
    }//GEN-LAST:event_btnPoIDuActionPerformed

    private void btnPoDatumuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPoDatumuActionPerformed
    {//GEN-HEADEREND:event_btnPoDatumuActionPerformed
        ArrayList<RadniNalogDTO> lista=DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        poDatumuRadniNalozi(lista, tblRadniNalozi, dtmDatum);
    }//GEN-LAST:event_btnPoDatumuActionPerformed

    private void tblRadniNaloziMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblRadniNaloziMouseReleased
    {//GEN-HEADEREND:event_tblRadniNaloziMouseReleased
        tblRadniNaloziMouseClicked(evt);
    }//GEN-LAST:event_tblRadniNaloziMouseReleased

    private void tblNeplaceneFaktureMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblNeplaceneFaktureMouseClicked
    {//GEN-HEADEREND:event_tblNeplaceneFaktureMouseClicked
        
    }//GEN-LAST:event_tblNeplaceneFaktureMouseClicked

    public void prikaziKupceSveUTabeli(ArrayList<KupacDTO> kupci) {
        String[] columns = {"ID", "Ime", "Prezime", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tableVozila.setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), (k.getIme() == null) ? "---" : k.getIme(), (k.getPrezime() == null) ? "---" : k.getPrezime(), (k.getNaziv() == null) ? "---" : k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }

        tableVozila.setModel(model);
    }

    public void prikaziKupcePravneUTabeli(ArrayList<KupacDTO> kupci) {
        String[] columns = {"ID", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tableVozila.setModel(model);
        for (KupacDTO k : kupci) {
            Object[] rowData = {k.getIdKupac(), k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }
        tableVozila.setModel(model);
    }

    public void setColor(JPanel panel) {
        panel.setBackground(new Color(51, 102, 255));

    }

    public void resetColor(JPanel panel) {
        panel.setBackground(new Color(51, 51, 255));

    }
    
    public void resetAllColors(JPanel panel){
        resetColor(menu1jPanel);
        resetColor(menu2jPanel);
        resetColor(menu3jPanel);
        resetColor(menu4jPanel);
        resetColor(menu5jPanel);
        resetColor(menu6jPanel);
        resetColor(menu7jPanel);
        resetColor(menu8jPanel);
        
        setColor(panel);
       
    }
    
    public void menuItemClick(JPanel menuItem,int index, JPanel newPanel){
        setColor(menuItem);
        for (int i = 0; i < numberOfItems; i++) {
            menu[i] = false;
        }
        
        menu[index] = true;
        
        resetAllColors(menuItem);


        parentPanel.removeAll();
        parentPanel.add(newPanel);
        parentPanel.repaint();
        parentPanel.revalidate();

    }
    
    private void loadAutosuggester(){
        VozilaLogika vl = new VozilaLogika(VozilaLogika.UCITAJ_VOZILA);
        VozilaLogika vl2 = new VozilaLogika(VozilaLogika.UCITAJ_VLASNIKE);
        VozilaLogika vl3 = new VozilaLogika((VozilaLogika.UCITAJ_MODELE));

        //ucitavanje autosuggestora
        if (voziloPanelPrviPut == true) {
            voziloPanelPrviPut = false;
            markeAU = ucitajPreporukeMarke();
            registracijeAU = ucitajPreporukeRegistracija();
            vlasnikAU = ucitajPreporukeVlasnik();
            pravniNazivAU = ucitajPreporukePravniNaziv();
            modelAU = ucitajPreporukeModel();
        }
    }
    
    private void loadDijeloviForm(){
        
        ArrayList<DioDTO> dijelovi = DAOFactory.getDAOFactory().getDioDAO().getSviDijelovi();
        DefaultTableModel dtm = (DefaultTableModel)jTable.getModel();
        dtm.setRowCount(0);
        
        int i = 0;
        if(dijelovi != null)
        while(i < dijelovi.size()){
            DioDTO d = dijelovi.get(i);
            dtm.addRow(new Object[]{d.getId(), d.getSifra(), d.getNaziv(), d.getMarka(), d.getModel(), d.getGodisteVozila(),
                d.getVrstaGoriva(), d.getTrenutnaCijena(), d.getKolicina(), d.getNovo() ? "Da":"Ne"});
            i++;
        }
        int size = cbModel.getItemCount();
        for(int j = 0; i < size; i++){
            cbModel.removeItemAt(0);
        }
        size = cbMarka.getItemCount();
        for(int j = 0; i < size; i++){
            cbMarka.removeItemAt(0);
        }
        ArrayList<DioDTO> nazivi = DAOFactory.getDAOFactory().getDioDAO().getSviDijelovi();
        ArrayList<String> naz = new ArrayList<String>();
        cbModel.removeAllItems();
        //cbModel.addItem("Svi");
        for(DioDTO d : nazivi){
            if(!naz.contains(d.getModel())){
                naz.add(d.getModel());
                cbModel.addItem(d.getModel());
            }
        }
        
        //ArrayList<DioDTO> marke = DAOFactory.getDAOFactory().getDioDAO().getSviDijelovi();
        ArrayList<String> mar = new ArrayList<String>();
        cbMarka.removeAllItems();
       //cbMarka.addItem("Svi");
        for(DioDTO d : nazivi){
            if(!mar.contains(d.getMarka())){
                mar.add(d.getMarka());
                cbMarka.addItem(d.getMarka());
            }
        }
        
        
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeForm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeForm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeForm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeForm1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                /*try
                {
                    double pdv=0.17;
                    FileOutputStream fileOut =
                    new FileOutputStream("PDV.data");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeDouble(pdv);
                    out.close();
                    fileOut.close();
                }
                catch(Exception e)
                {
                    
                }*/
                new HomeForm1().setVisible(true);
            }
        });
    }

   

    void inicijalizujZaposleniPanel() {
        dateChooserDatumDoZaposlenog.setCalendar(Calendar.getInstance());
        dateChooserDatumOdZaposlenog.setCalendar(Calendar.getInstance());
        dateChooserDatumRodjenja.setCalendar(Calendar.getInstance());
        dateChooserDatumPrimanjaURadniOdnos.setCalendar(Calendar.getInstance());
        new ZaposleniLogika("svi").run();

    }

    public JComboBox<String> getComboBoxGodina() {
        return comboBoxGodina;
    }

    public void setComboBoxGodina(JComboBox<String> comboBoxGodina) {
        this.comboBoxGodina = comboBoxGodina;
    }

    public JComboBox<String> getComboBoxMjesec() {
        return comboBoxMjesec;
    }

    public void setComboBoxMjesec(JComboBox<String> comboBoxMjesec) {
        this.comboBoxMjesec = comboBoxMjesec;
    }

    public JLabel getLabelDnevnaZarada() {
        return labelDnevnaZarada;
    }

    public void setLabelDnevnaZarada(JLabel labelDnevnaZarada) {
        this.labelDnevnaZarada = labelDnevnaZarada;
    }

    public JLabel getLabelDnevnaZaradaDijelovi() {
        return labelDnevnaZaradaDijelovi;
    }

    public void setLabelDnevnaZaradaDijelovi(JLabel labelDnevnaZaradaDijelovi) {
        this.labelDnevnaZaradaDijelovi = labelDnevnaZaradaDijelovi;
    }

    public JLabel getLabelGodisnjaZarada() {
        return labelGodisnjaZarada;
    }

    public void setLabelGodisnjaZarada(JLabel labelGodisnjaZarada) {
        this.labelGodisnjaZarada = labelGodisnjaZarada;
    }

    public JLabel getLabelGodisnjaZaradaDijelovi() {
        return labelGodisnjaZaradaDijelovi;
    }

    public void setLabelGodisnjaZaradaDijelovi(JLabel labelGodisnjaZaradaDijelovi) {
        this.labelGodisnjaZaradaDijelovi = labelGodisnjaZaradaDijelovi;
    }

    public JLabel getLabelIntervalZarada() {
        return labelIntervalZarada;
    }

    public void setLabelIntervalZarada(JLabel labelIntervalZarada) {
        this.labelIntervalZarada = labelIntervalZarada;
    }

    public JLabel getLabelIntervalZaradaDijelovi() {
        return labelIntervalZaradaDijelovi;
    }

    public void setLabelIntervalZaradaDijelovi(JLabel labelIntervalZaradaDijelovi) {
        this.labelIntervalZaradaDijelovi = labelIntervalZaradaDijelovi;
    }

    public JLabel getLabelMjesecnaZarada() {
        return labelMjesecnaZarada;
    }

    public void setLabelMjesecnaZarada(JLabel labelMjesecnaZarada) {
        this.labelMjesecnaZarada = labelMjesecnaZarada;
    }

    public JLabel getLabelMjesecnaZaradaDijelovi() {
        return labelMjesecnaZaradaDijelovi;
    }

    public void setLabelMjesecnaZaradaDijelovi(JLabel labelMjesecnaZaradaDijelovi) {
        this.labelMjesecnaZaradaDijelovi = labelMjesecnaZaradaDijelovi;
    }

    public JLabel getLabelPopravkeDanas() {
        return labelPopravkeDanas;
    }

    public void setLabelPopravkeDanas(JLabel labelPopravkeDanas) {
        this.labelPopravkeDanas = labelPopravkeDanas;
    }

    public JLabel getLabelPopravkeGodina() {
        return labelPopravkeGodina;
    }

    public void setLabelPopravkeGodina(JLabel labelPopravkeGodina) {
        this.labelPopravkeGodina = labelPopravkeGodina;
    }

    public JLabel getLabelPopravkeInterval() {
        return labelPopravkeInterval;
    }

    public void setLabelPopravkeInterval(JLabel labelPopravkeInterval) {
        this.labelPopravkeInterval = labelPopravkeInterval;
    }

    public JLabel getLabelPopravkeMjesec() {
        return labelPopravkeMjesec;
    }

    public void setLabelPopravkeMjesec(JLabel labelPopravkeMjesec) {
        this.labelPopravkeMjesec = labelPopravkeMjesec;
    }

    public JPanel getPanelGrafikAuta() {
        return panelGrafikAuta;
    }

    public void setPanelGrafikAuta(JPanel panelGrafikAuta) {
        this.panelGrafikAuta = panelGrafikAuta;
    }

    public JPanel getPanelGrafikFakture() {
        return panelGrafikFakture;
    }

    public void setPanelGrafikFakture(JPanel panelGrafikFakture) {
        this.panelGrafikFakture = panelGrafikFakture;
    }

    public JPanel getPanelGrafikPopravke() {
        return panelGrafikPopravke;
    }

    public void setPanelGrafikPopravke(JPanel panelGrafikPopravke) {
        this.panelGrafikPopravke = panelGrafikPopravke;
    }

    public JPanel getPanelGrafikPrihodiUkupno() {
        return panelGrafikPrihodiUkupno;
    }

    public void setPanelGrafikPrihodiUkupno(JPanel panelGrafikPrihodiUkupno) {
        this.panelGrafikPrihodiUkupno = panelGrafikPrihodiUkupno;
    }

    public JPanel getPanelGrafikPrihodiDijelovi() {
        return PanelGrafikPrihodiDijelovi;
    }

    public void setPanelGrafikPrihodiDijelovi(JPanel PanelGrafikPrihodiDijelovi) {
        this.PanelGrafikPrihodiDijelovi = PanelGrafikPrihodiDijelovi;
    }
    
    
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Napomene2;
    private javax.swing.JPanel PanelGrafikPrihodiDijelovi;
    private javax.swing.JButton btnDodaj;
    private javax.swing.JButton btnDodajTermin;
    private javax.swing.JButton btnFakturisano;
    private javax.swing.JButton btnNefakturisano;
    private javax.swing.JButton btnPoDatumu;
    private javax.swing.JButton btnPoIDu;
    private javax.swing.JButton btnPonisti;
    private javax.swing.JButton btnPonistiSve;
    private javax.swing.JButton btnPonistiUnosePretraga;
    private javax.swing.JButton btnPonistiUnoseTermin;
    private javax.swing.JButton btnPredracun;
    private javax.swing.JButton btnPretrazi;
    private javax.swing.JButton btnPrikaziSve;
    private javax.swing.JButton btnProdani;
    private javax.swing.JButton btnPronadji;
    private javax.swing.JButton btnPronadjiTermin;
    private javax.swing.JButton btnRacun;
    private javax.swing.JButton btnSviDijelovi;
    private javax.swing.JButton btnTrazi;
    private javax.swing.JButton buttonDodajZaposlenog;
    private javax.swing.JButton buttonPregled;
    private javax.swing.JButton buttonPregledGrafik;
    private javax.swing.JButton buttonPrikazSvihBivsihRadnika;
    private javax.swing.JButton buttonTraziRadneNalogeRadnika;
    private javax.swing.JComboBox<String> cbGorivo;
    private javax.swing.JComboBox<String> cbMarka;
    private javax.swing.JComboBox<String> cbModel;
    private javax.swing.JCheckBox cbNovo;
    private javax.swing.JCheckBox cbStanje;
    private javax.swing.JCheckBox cbSvi;
    private javax.swing.JComboBox<String> comboBoxGodina;
    private javax.swing.JComboBox<String> comboBoxMjesec;
    private javax.swing.JPanel danasnjeAktivnostiPanel;
    private com.toedter.calendar.JDateChooser dateChooserDatumDoZaposlenog;
    private com.toedter.calendar.JDateChooser dateChooserDatumOdZaposlenog;
    private com.toedter.calendar.JDateChooser dateChooserDatumPrimanjaURadniOdnos;
    private com.toedter.calendar.JDateChooser dateChooserDatumRodjenja;
    private javax.swing.JLabel datumLabel;
    private javax.swing.JPanel dijeloviPanel;
    private javax.swing.JPanel dodajDioPanel;
    private com.toedter.calendar.JDateChooser dtmDatum;
    private com.toedter.calendar.JDateChooser dtmDatumPretraga;
    private com.toedter.calendar.JDateChooser dtmDatumTermina;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private com.toedter.calendar.JDateChooser jDateChooserDatumDo;
    private com.toedter.calendar.JDateChooser jDateChooserDatumOd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JTable jTable10;
    public static javax.swing.JTable jTableZaposleni;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JComboBox<String> jcbGorivo;
    private javax.swing.JComboBox<String> jcbMarka;
    private javax.swing.JComboBox<String> jcbModel;
    private javax.swing.JTextField jtfCijena;
    private javax.swing.JTextField jtfGodiste;
    private javax.swing.JTextField jtfKolicina;
    private javax.swing.JTextField jtfNaziv;
    private javax.swing.JTextField jtfSifra;
    private javax.swing.JPanel knjigovodstvoPanel;
    private javax.swing.JLabel labelBrojRadnihNaloga;
    private javax.swing.JLabel labelDnevnaZarada;
    private javax.swing.JLabel labelDnevnaZaradaDijelovi;
    private javax.swing.JLabel labelGodisnjaZarada;
    private javax.swing.JLabel labelGodisnjaZaradaDijelovi;
    private javax.swing.JLabel labelIntervalZarada;
    private javax.swing.JLabel labelIntervalZaradaDijelovi;
    private javax.swing.JLabel labelMjesecnaZarada;
    private javax.swing.JLabel labelMjesecnaZaradaDijelovi;
    private javax.swing.JLabel labelOstvareniProfitRadnika;
    private javax.swing.JLabel labelPopravkeDanas;
    private javax.swing.JLabel labelPopravkeGodina;
    private javax.swing.JLabel labelPopravkeInterval;
    private javax.swing.JLabel labelPopravkeMjesec;
    private javax.swing.JLabel labelPoruka;
    private javax.swing.JPanel menu1jPanel;
    private javax.swing.JPanel menu2jPanel;
    private javax.swing.JPanel menu3jPanel;
    private javax.swing.JPanel menu4jPanel;
    private javax.swing.JPanel menu5jPanel;
    private javax.swing.JPanel menu6jPanel;
    private javax.swing.JPanel menu7jPanel;
    private javax.swing.JPanel menu8jPanel;
    private javax.swing.JMenuItem menuItemDetaljniOpis;
    private javax.swing.JMenuItem menuItemIzmjeniRadnika;
    private javax.swing.JMenuItem menuItemOtpustiRadnika;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPanel odabirMjesecaStatistikaPanel;
    private javax.swing.JPanel panelAkcijeNaFormi;
    private javax.swing.JPanel panelGrafikAuta;
    private javax.swing.JPanel panelGrafikFakture;
    private javax.swing.JPanel panelGrafikPopravke;
    private javax.swing.JPanel panelGrafikPrihodiUkupno;
    private javax.swing.JPanel panelPronadjiVlasnika;
    private javax.swing.JPanel panelPronadjiVozilo;
    private javax.swing.JPanel panelVozilo;
    private javax.swing.JPanel parentPanel;
    private javax.swing.JPanel pocetnajPanel;
    private javax.swing.JPopupMenu popupMenuZaposleni;
    private javax.swing.JPanel pretraziPanel;
    private javax.swing.JPanel radniNaloziPanel;
    private javax.swing.JRadioButton rbPravnoLiceVozilo;
    private javax.swing.JRadioButton rbPravnoTrazi;
    private javax.swing.JRadioButton rbPrivatnoLiceVozilo;
    private javax.swing.JRadioButton rbPrivatnoTrazi;
    private javax.swing.JScrollPane spVoziloPretraga;
    private javax.swing.JPanel statistikaHeaderjPanel;
    private javax.swing.JPanel statistikajPanel;
    public static javax.swing.JTable tableRadniNalozi;
    private javax.swing.JTable tableVozila;
    private javax.swing.JTable tblFaktura;
    private javax.swing.JTable tblNeplaceneFakture;
    private javax.swing.JTable tblRadniNalozi;
    private javax.swing.JTable tblTermini;
    private javax.swing.JTextField textFieldAdresa;
    private javax.swing.JTextField textFieldBrojLicneKarte;
    private javax.swing.JTextField textFieldFunkcijaRadnika;
    private javax.swing.JTextField textFieldIme;
    private javax.swing.JTextField textFieldImeOca;
    private javax.swing.JTextField textFieldPrezime;
    private javax.swing.JTextField textFieldStrucnaSprema;
    private javax.swing.JTextField textFieldTelefon;
    private javax.swing.JTextField tfGodiste;
    private javax.swing.JTextField tfGodisteTrazi;
    private javax.swing.JTextField tfId;
    private javax.swing.JTextField tfImeTrazi;
    private javax.swing.JTextField tfImeVozilo;
    private javax.swing.JTextField tfMarkaTrazi;
    private javax.swing.JTextField tfModelTrazi;
    private javax.swing.JTextField tfNaziv;
    private javax.swing.JTextField tfNazivTrazi;
    private javax.swing.JTextField tfNazivVozilo;
    private javax.swing.JTextField tfPrezimeTrazi;
    private javax.swing.JTextField tfPrezimeVozilo;
    private javax.swing.JTextField tfRegistracijaTrazi;
    private javax.swing.JTextField tfSifra;
    private javax.swing.JTextField txtBezPDV;
    private javax.swing.JTextField txtBrojTelefonaPretraga;
    private javax.swing.JTextField txtBrojTelefonaTermina;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtImePretraga;
    private javax.swing.JTextField txtImeTermina;
    private javax.swing.JTextField txtMarkaPretraga;
    private javax.swing.JTextField txtMarkaTermina;
    private javax.swing.JTextField txtModelTermina;
    private javax.swing.JTextField txtPDV;
    private javax.swing.JTextField txtPrezimePretraga;
    private javax.swing.JTextField txtPrezimeTermina;
    private javax.swing.JTextField txtUkupno;
    private javax.swing.JSpinner txtVrijemeTerminaMinuti;
    private javax.swing.JSpinner txtVrijemeTerminaSati;
    private javax.swing.JPanel vozilaPanel;
    private javax.swing.JPanel zakazivanjaPanel;
    private javax.swing.JPanel zaposleniPanel;
    // End of variables declaration//GEN-END:variables
}
