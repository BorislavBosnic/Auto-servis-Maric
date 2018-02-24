/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoservismaric.forms;

import autoservismaric.dialog.*;
import autoservismaric.dialog.DodajModel;
import autoservismaric.dialog.DodajVlasnikaDialog;
import autoservismaric.dialog.DodajVoziloDialog;
import autoservismaric.dialog.IzmijeniIzbrisiModelDialog;
import autoservismaric.dialog.OtpustiRadnikaDialog;
import autoservismaric.dialog.SviBivsiZaposleniDialog;
import com.toedter.calendar.JDateChooser;
import data.AutoSuggestor;
import data.dao.*;
import data.dto.DioDTO;
import data.dto.KupacDTO;
import data.dto.ModelVozilaDTO;
import data.dto.RadniNalogDTO;
import data.dto.TerminDTO;
import data.dto.ZaposleniDTO;
import java.io.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import poslovnalogika.DioLogika;
import poslovnalogika.KnjigovodstvoLogika;
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
import poslovnalogika.ModelVozilaLogika;
import poslovnalogika.PocetnaLogika;
import poslovnalogika.ZaposleniLogika;
import poslovnalogika.StatistikaLogika;
import poslovnalogika.VoziloKupacMeniLogika;
import static poslovnalogika.ZakazivanjaLogika.dodajTermin;
import static poslovnalogika.ZakazivanjaLogika.terminiZaTabelu;

/**
 *
 * @author HP BOOK
 */
public class HomeForm1 extends javax.swing.JFrame {

    boolean flagVoziloVlasnik = true;
    
    /**
     * Karpin kod*
     */
    private static HomeForm1 homeForm;

    private void inicijalisuciKod() {
        homeForm = this;
        uslugeKnjigovodstva();
        uslugeZakazivanja();
        uslugePocetneStrane();
    }

    private void uslugeKnjigovodstva() {
        inicijalizacijaTabelaKnjigovodstva();
        brisanjeStarihVrijednostiPoljaKnjigovodstva();
    }

    private void uslugeZakazivanja() {
        inicijalizacijaTabelaZakazivanja();
    }

    private void uslugePocetneStrane() {
        inicijalizacijaTabeleNeplacenihFaktura();
    }

    private void inicijalizacijaTabelaKnjigovodstva() {
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        radniNaloziZaTabelu(lista, tblRadniNalozi);
        stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
        if (tblRadniNalozi.getRowCount() > 0) {
            tblRadniNalozi.setRowSelectionInterval(0, 0);
        }
        if (tblFaktura.getRowCount() > 0) {
            tblFaktura.setRowSelectionInterval(0, 0);
        }
        provjeraZaDugmiceZaTabeluNaloga();
        dodajIskacuciMeniUTabeluRadnihNaloga();
    }

    private void inicijalizacijaTabelaZakazivanja() {
        ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, tblTermini);
        if (tblTermini.getRowCount() > 0) {
            tblTermini.setRowSelectionInterval(0, 0);
        }
        dodajIskacuciMeniUTabeluTermina();
    }

    private void inicijalizacijaTabeleNeplacenihFaktura() {
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        neplaceneFaktureZaPocetnuStranu(lista, tblNeplaceneFakture);
        if (tblNeplaceneFakture.getRowCount() > 0) {
            tblNeplaceneFakture.setRowSelectionInterval(0, 0);
        }
        dodajIskacuciMeniUTabeluNeplacenihFaktura();
    }

    private void provjeraZaDugmiceZaTabeluNaloga() {
        if (tblRadniNalozi.getRowCount() > 0
                && !("Nema fakture".equals((String) (tblRadniNalozi.getValueAt(tblRadniNalozi.getSelectedRow(), 4))))) {
            btnRacun.setEnabled(true);
            btnPredracun.setEnabled(false);
        } else {
            btnRacun.setEnabled(false);
            btnPredracun.setEnabled(true);
        }
    }
    
    
    
    
     private void dodajPopupMeniPretragaRadniNaloga() {
        opcija = -1;
        popupMenu = new JPopupMenu();
        JMenuItem izmijeniItem = new JMenuItem("Izmijeni");
        JMenuItem izbrisiItem = new JMenuItem("Izbriši");
        JMenuItem zatvoriNalogItem = new JMenuItem("Zatvori radni nalog");


        izmijeniItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Integer id = Integer.parseInt(tableRNalozi.getModel().getValueAt(selektovanRed, 0).toString());
                new IzmijeniRadniNalogDialog(new JFrame(), true, id).setVisible(true);
            }
        });
        popupMenu.add(izmijeniItem);

        izbrisiItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                   Integer id = Integer.parseInt(tableRNalozi.getModel().getValueAt(selektovanRed, 0).toString());
                   
                    int dialogResult = JOptionPane.showConfirmDialog(new JFrame(), "Da li ste sigurni?", "Upozorenje", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                   
                        
                        DAOFactory.getDAOFactory().getRadniNalogDAO().izbrisiRadniNalog(id);
                        
                        JOptionPane.showMessageDialog(new JFrame(), "Uspješno obrisano!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                            for (int i = tableRNalozi.getModel().getRowCount() - 1; i >= 0; i--) {
                                if ((Integer.parseInt(tableRNalozi.getModel().getValueAt(i, 0).toString())) == id) {
                                    ((DefaultTableModel) tableRNalozi.getModel()).removeRow(i);
                                }
                            }
                        
                    }
                    else{
                        
                    }
                   
            }
        });
        popupMenu.add(izbrisiItem);
        
         zatvoriNalogItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Integer id = Integer.parseInt(tableRNalozi.getModel().getValueAt(selektovanRed, 0).toString());
                if(DAOFactory.getDAOFactory().getRadniNalogDAO().zatvoriRadniNalog(id)){
                    JOptionPane.showMessageDialog(new JFrame(), "Uspješno zatvoren radni nalog!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
   
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Radni nalog već zatvoren!!", "Greška", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        popupMenu.add(zatvoriNalogItem);

        tableRNalozi.setComponentPopupMenu(popupMenu);

        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = tableRNalozi.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), tableRNalozi));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                       //selektovanRed = tableRNalozi.getSelectedRow(); //??
                        String imeKolone = tableRNalozi.getModel().getColumnName(0);

                        if (selektovanRed >= 0) {

                        }
                        if (rowAtPoint > -1) {
                            tableRNalozi.setRowSelectionInterval(rowAtPoint, rowAtPoint);
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    private void brisanjeStarihVrijednostiPoljaKnjigovodstva() {
        txtID.setText("");
        dtmDatum.setDate(null);
    }
    private static int opcija = -1;

    private void dodajIskacuciMeniUTabeluRadnihNaloga() {
        opcija = -1;
        popupMenu = new JPopupMenu();
        JMenuItem detaljnoItem = new JMenuItem("Detaljno");
        JMenuItem placenoItem = new JMenuItem("Plaćeno");
        JMenuItem fakturisiItem = new JMenuItem("Fakturiši");

        detaljnoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JFrame(), radniNalogIFakturaUTekst(Integer.parseInt((String) tblRadniNalozi.getValueAt(selektovanRed, 0))));
                stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
                provjeraZaDugmiceZaTabeluNaloga();
            }
        });
        popupMenu.add(detaljnoItem);

        placenoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int izbor = JOptionPane.showOptionDialog(
                        new JFrame(),
                        "Da li ste sigurni da je placeno?",
                        "Da li ste sigurni?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"DA", "NE"},
                        "NE"
                );
                if (izbor == JOptionPane.YES_OPTION) {
                    plati(Integer.parseInt((String) tblRadniNalozi.getValueAt(selektovanRed, 0)));
                }
                stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
                provjeraZaDugmiceZaTabeluNaloga();
                inicijalizacijaTabelaKnjigovodstva();
            }
        });
        popupMenu.add(placenoItem);

        fakturisiItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int izbor = JOptionPane.showOptionDialog(
                        new JFrame(),
                        "Da li ste sigurni da želite fakturisati?",
                        "Da li ste sigurni?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"DA", "NE"},
                        "NE"
                );
                if (izbor == JOptionPane.YES_OPTION) {
                    fakturisi(Integer.parseInt((String) tblRadniNalozi.getValueAt(selektovanRed, 0)), txtUkupno);
                }
                stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
                provjeraZaDugmiceZaTabeluNaloga();
                inicijalizacijaTabelaKnjigovodstva();
            }
        });
        popupMenu.add(fakturisiItem);

        tblRadniNalozi.setComponentPopupMenu(popupMenu);

        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = tblRadniNalozi.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), tblRadniNalozi));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = tblRadniNalozi.getModel().getColumnName(0);

                        if (selektovanRed >= 0) {

                        }
                        if (rowAtPoint > -1) {
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

    private void dodajIskacuciMeniUTabeluTermina() {
        opcija = -1;
        popupMenu = new JPopupMenu();
        JMenuItem obrisiItem = new JMenuItem("Obriši");

        obrisiItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (obrisiTermin(
                        (String) tblTermini.getValueAt(selektovanRed, 0),
                        (String) tblTermini.getValueAt(selektovanRed, 1)
                )) {
                    JOptionPane.showMessageDialog(new JFrame(), "Termin je uklonjen.");
                    ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
                    terminiZaTabelu(lista, tblTermini);
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Termin nije uklonjen.");
                }
            }
        });
        popupMenu.add(obrisiItem);

        tblTermini.setComponentPopupMenu(popupMenu);

        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = tblTermini.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), tblTermini));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = tblTermini.getModel().getColumnName(0);

                        if (selektovanRed >= 0) {

                        }
                        if (rowAtPoint > -1) {
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

    private void dodajIskacuciMeniUTabeluNeplacenihFaktura() {
        opcija = -1;
        popupMenu = new JPopupMenu();
        JMenuItem detaljnoItem = new JMenuItem("Detaljno");
        JMenuItem placenoItem = new JMenuItem("Plaćeno");
        JMenuItem fakturisiItem = new JMenuItem("Fakturiši");

        detaljnoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JFrame(), radniNalogIFakturaUTekst(Integer.parseInt((String) tblNeplaceneFakture.getValueAt(selektovanRed, 0))));
            }
        });
        popupMenu.add(detaljnoItem);

        placenoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int izbor = JOptionPane.showOptionDialog(
                        new JFrame(),
                        "Da li ste sigurni da je placeno?",
                        "Da li ste sigurni?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"DA", "NE"},
                        "NE"
                );
                if (izbor == JOptionPane.YES_OPTION && selektovanRed>-1) {
                    plati(Integer.parseInt((String) tblNeplaceneFakture.getValueAt(selektovanRed, 0)));
                }
                inicijalizacijaTabeleNeplacenihFaktura();
            }
        });
        popupMenu.add(placenoItem);

        /*fakturisiItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int izbor = JOptionPane.showOptionDialog(
                        new JFrame(),
                        "Da li ste sigurni da želite fakturisati?",
                        "Da li ste sigurni?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"DA", "NE"},
                        "NE"
                );
                if (izbor == JOptionPane.YES_OPTION) {
                    fakturisi(Integer.parseInt((String) tblNeplaceneFakture.getValueAt(selektovanRed, 0)));
                }
            }
        });
        popupMenu.add(fakturisiItem);*/

        tblNeplaceneFakture.setComponentPopupMenu(popupMenu);

        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = tblNeplaceneFakture.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), tblNeplaceneFakture));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = tblNeplaceneFakture.getModel().getColumnName(0);

                        if (selektovanRed >= 0) {

                        }
                        if (rowAtPoint > -1) {
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

    public ButtonGroup getbGTraziVozilo() {
        return bGTraziVozilo;
    }

    public ButtonGroup getbGTraziVlasnika() {
        return bGTraziVlasnika;
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public JPopupMenu getPopupMenuVlasnik() {
        return popupMenuVlasnik;
    }

    public JPopupMenu getPopupDio() {
        return popupDio;
    }
    
    public JPopupMenu getPopupAktivnosti() {
        return popupAktivnosti;
    }

    /**
     * ***Kraj****
     */
    //Sve moje, ne diraj!
    public int getIdVlasnika() {
        return idVlasnika;
    }
    ButtonGroup bGTraziVozilo;
    ButtonGroup bGTraziRadniNalog;
    ButtonGroup bGTraziVlasnika;
    AutoSuggestor modelAU, markeAU, vlasnikAU, registracijeAU, pravniNazivAU;
    AutoSuggestor autoModel = null, autoMarka = null, autoNaziv = null, autoSifra = null;
    static boolean voziloPanelPrviPut = true; ///flag koji se koristi za ucitavanje za suggestor u vozilo panelu
    JPopupMenu popupMenu = new JPopupMenu(); // za popup u tabeli
    JPopupMenu popupMenuVlasnik = new JPopupMenu();
    JPopupMenu popupDio = new JPopupMenu();
    JPopupMenu popupAktivnosti = new JPopupMenu();
    public static int selRedDio;// ua popup dio
    public static Double pdv = 17.0;
    public int idVlasnika = -1;
    public static int selektovanRed; //za popup
    public static int idVozila = -1; //za popup
    
    

    public void izbrisiPopupZaVozila() {
        popupMenu.removeAll();
       // tableVozila.setComponentPopupMenu(popupMenu);
    }

    public void izbrisiPopupZaVlasnike() {
        popupMenuVlasnik.removeAll();
    //    tableVozila.setComponentPopupMenu(popupMenuVlasnik);
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public void setPopupMenuVlasnik(JPopupMenu popupMenuVlasnik) {
        this.popupMenuVlasnik = popupMenuVlasnik;
    }

    public void setPopupDio(JPopupMenu popupDio) {
        this.popupDio = popupDio;
    }
    
    public void setPopupAktivnosti(JPopupMenu popupAktivnosti) {
        this.popupAktivnosti = popupAktivnosti;
    }

    VoziloKupacMeniLogika voziloKupacMeniLogika = new VoziloKupacMeniLogika();
    ModelVozilaLogika modelVozilaLogika = new ModelVozilaLogika();
    PocetnaLogika pocetnaLogika = new PocetnaLogika(this);
    DioLogika dioLogika = new DioLogika();

    public void ucitajPopupZaVlasnike() {
        voziloKupacMeniLogika.ucitajPopupZaVlasnike(this);
    }
    
    public void ucitajPopupZaAktivnosti(){
        pocetnaLogika.ucitajPopupZaAktivnosti();
    }
    
    public void ucitajPopupZaDijelove(){
        dioLogika.ucitajPopupZaDijelove(this);
    }

    public void ucitajPopupZaVozila() {
        voziloKupacMeniLogika.ucitajPopupZaVozila(this);
    }

    static int numberOfItems = 8;
    public static String[] mjeseci = {"Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar"};
    public static boolean menu[] = new boolean[numberOfItems];
    public StatistikaLogika statistikaLogika;
    public static int trenutnaGodina=Calendar.getInstance().get(Calendar.YEAR);

    // public static JPanel employeePanel=new EmployeesForm().getPanel();
    // public static JPanel partsPanel=new PartsForm().getPanel();
    /**
     * Creates new form HomeForm1
     */
    public HomeForm1() {
        initComponents();
        
        //za slucaj da je dosla nova godina, automatski se dodaju nove vrijednosti u comboBoxGodina
        if(((trenutnaGodina-1)+"").equals(cbGodina.getItemAt(1))){
           // System.out.println(comboBoxGodina.getItemAt(1)+"");
            cbGodina.insertItemAt(Calendar.getInstance().get(Calendar.YEAR)+"",1);
       
        }
        statistikaLogika = new StatistikaLogika();
        btnPrikaziSvaVozila.doClick();
        dodajPopupMeniPretragaRadniNaloga();
        
        tabpnlGrafici.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabpnlGrafici.getSelectedIndex() == 2 || tabpnlGrafici.getSelectedIndex() == 3) {
                    pnlOdabirMjesecaStatistika.setVisible(false);
                } else {
                    pnlOdabirMjesecaStatistika.setVisible(true);

                }
            }
        });
        /*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        pack();
        setSize(screenSize.width,screenSize.height);*/

        //tableVozila.addMouseListener(new PopClickListener());
        ucitajPopupZaVozila();
        ucitajPopupZaDijelove();
        ucitajPopupZaAktivnosti();

        tfNazivv.setBackground(Color.gray);
        tfNazivv.setEditable(false);
        
        txtNazivTrazi.setBackground(Color.gray);
        txtNazivVozilo.setBackground(Color.gray);
        txtNazivVozilo.setEditable(false);
        txtNazivTrazi.setEditable(false);

        bGTraziVlasnika = new ButtonGroup();
        bGTraziVlasnika.add(rbPrivatnoTrazi);
        bGTraziVlasnika.add(rbPravnoTrazi);

        bGTraziRadniNalog = new ButtonGroup();
        bGTraziRadniNalog.add(rbPravnoRadniNalog);
        bGTraziRadniNalog.add(rbPrivatnoRadniNalog);
        
        bGTraziVozilo = new ButtonGroup();
        bGTraziVozilo.add(rbPravnoLiceVozilo);
        bGTraziVozilo.add(rbPrivatnoLiceVozilo);

        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setIconImage(new ImageIcon(getClass().getResource("/autoservismaric/images/ikona2.png")).getImage());

        datumLabel.setText("Aktivnosti: " + new SimpleDateFormat("dd.MM.yyyy.").format(Calendar.getInstance().getTime()));

        dateChooserAktivnosti1.setCalendar(Calendar.getInstance());
        dateChooserAktivnosti2.setCalendar(Calendar.getInstance());
        btnPrikazii.doClick();

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("baza.ser"));
            java.util.Date dat = (java.util.Date) ois.readObject();
            labelBaza.setText(new SimpleDateFormat("dd.MM.yyyy.").format(dat));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        inicijalizujZaposleniPanel();

        /**
         * Karpin kod*
         */
        inicijalisuciKod();
        /**
         * ***Kraj****
         */
    }

    public AutoSuggestor ucitajPreporukeMarke() {
        return voziloKupacMeniLogika.ucitajPreporukeMarke(this);
    }

    public AutoSuggestor ucitajPreporukeModel() {
        return voziloKupacMeniLogika.ucitajPreporukeModel(this);
    }

    public AutoSuggestor ucitajPreporukeRegistracija() {
        return voziloKupacMeniLogika.ucitajPreporukeRegistracija(this);
    }

    public AutoSuggestor ucitajPreporukeVlasnik() {
        return voziloKupacMeniLogika.ucitajPreporukeVlasnika(this);
    }

    public AutoSuggestor ucitajPreporukePravniNaziv() {
        return voziloKupacMeniLogika.ucitajPreporukePravniNaziv(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel59 = new javax.swing.JLabel();
        popupMenuZaposleni = new javax.swing.JPopupMenu();
        menuItemDetaljniOpis = new javax.swing.JMenuItem();
        menuItemIzmjeniRadnika = new javax.swing.JMenuItem();
        menuItemOtpustiRadnika = new javax.swing.JMenuItem();
        pnlMeni = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlMeniRadniNalozi = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        pnlSlika = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pnlMeniVozila = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pnlMeniKnjigovodstvo = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        pnlMeniStatistika = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlMeniZaposleni = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        pnlMeniDijelovi = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        pnlMeniPocetnaStrana = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        pnlMeniZakazivanja = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlParent = new javax.swing.JPanel();
        pnlPocetna = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        dateChooserAktivnosti1 = new com.toedter.calendar.JDateChooser();
        dateChooserAktivnosti2 = new com.toedter.calendar.JDateChooser();
        btnPrikazii = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        btnPrikaziSvePredracune = new javax.swing.JButton();
        btnPrikaziPredracune = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jrbPrivatno = new javax.swing.JRadioButton();
        jrbPravnoo = new javax.swing.JRadioButton();
        jLabel39 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        tfNazivv = new javax.swing.JTextField();
        tfImee = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        tfPrezimee = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        btnBaza = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        labelBaza = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        Napomene2 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblNeplaceneFakture = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        danasnjeAktivnostiPanel = new javax.swing.JPanel();
        datumLabel = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tableAktivnosti = new javax.swing.JTable();
        pnlRadniNalozi = new javax.swing.JPanel();
        panelOsnovniRN = new javax.swing.JPanel();
        panelOsnovniPretragaRN = new javax.swing.JPanel();
        btnNoviRadniNalog = new javax.swing.JButton();
        panelPretraga = new javax.swing.JPanel();
        dcDatumOtvaranjaOD = new com.toedter.calendar.JDateChooser();
        dcDatumOtvaranjaDO = new com.toedter.calendar.JDateChooser();
        dcPotrebnoZavrsitiOD = new com.toedter.calendar.JDateChooser();
        dcPotrebnoZavrsitiDO = new com.toedter.calendar.JDateChooser();
        dcDatumZatvaranjaOD = new com.toedter.calendar.JDateChooser();
        dcDatumZatvaranjaDO = new com.toedter.calendar.JDateChooser();
        lblDatumOtvaranja = new javax.swing.JLabel();
        lblDatumOtvaranjaOD = new javax.swing.JLabel();
        lblDatumOtvaranjaDO = new javax.swing.JLabel();
        lblPotrebnoZavrsiti = new javax.swing.JLabel();
        lblPotrebnoZavrsitiOD = new javax.swing.JLabel();
        lblPotrebnoZavrsitiDO = new javax.swing.JLabel();
        lblDatumZatvaranjaOD = new javax.swing.JLabel();
        lblDatumZatvaranjaDO = new javax.swing.JLabel();
        lblDatumZatvaranja = new javax.swing.JLabel();
        cbDatumOtvaranja = new javax.swing.JCheckBox();
        cbPotrebnoZavrsiti = new javax.swing.JCheckBox();
        cbDatumZatvaranja = new javax.swing.JCheckBox();
        btnPrikaziRadniNalog = new javax.swing.JButton();
        tfRegistracijaRadniNalog = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        tfNazivRadniNalog = new javax.swing.JTextField();
        tfImeRadniNalog = new javax.swing.JTextField();
        rbPravnoRadniNalog = new javax.swing.JRadioButton();
        rbPrivatnoRadniNalog = new javax.swing.JRadioButton();
        lblVlasnik = new javax.swing.JLabel();
        lblPrezimeRN = new javax.swing.JLabel();
        lblImeRN = new javax.swing.JLabel();
        lblNazivRN = new javax.swing.JLabel();
        separatorRN = new javax.swing.JSeparator();
        cbSviRadniNalog = new javax.swing.JCheckBox();
        tfPrezimeRadniNalog = new javax.swing.JTextField();
        lbRadniNaloziNaslov = new javax.swing.JLabel();
        lbRadniNaloziSlika = new javax.swing.JLabel();
        spanelTabela = new javax.swing.JScrollPane();
        tableRNalozi = new javax.swing.JTable();
        pnlZakazivanja = new javax.swing.JPanel();
        pnlOsnovniZakazivanja = new javax.swing.JPanel();
        pnlOkvirZakazivanja = new javax.swing.JPanel();
        txtPronadjiZakazivanja = new javax.swing.JLabel();
        txtZakaziZakazivanja = new javax.swing.JLabel();
        pnlPronadjiZakazivanja = new javax.swing.JPanel();
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
        pnlZakaziZakazivanja = new javax.swing.JPanel();
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
        pnlTabelaZakazivanje = new javax.swing.JScrollPane();
        tblTermini = new javax.swing.JTable();
        txtZakazivanja = new javax.swing.JLabel();
        txtSlikaZakazivanja = new javax.swing.JLabel();
        pnlVozila = new javax.swing.JPanel();
        pnllAkcijeNaFormi = new javax.swing.JPanel();
        lblNaslovVozila = new javax.swing.JLabel();
        lblSlikaVozila = new javax.swing.JLabel();
        pnlVozilo = new javax.swing.JPanel();
        pnllPronadjiVozilo = new javax.swing.JPanel();
        lblRegistracijaVozila = new javax.swing.JLabel();
        lblGodisteVozila = new javax.swing.JLabel();
        lblMarkaVozila = new javax.swing.JLabel();
        lblPrezimeVozila = new javax.swing.JLabel();
        txtRegistracijaTrazi = new javax.swing.JTextField();
        txtGodisteTrazi = new javax.swing.JTextField();
        txtModelTrazi = new javax.swing.JTextField();
        txtPrezimeVozilo = new javax.swing.JTextField();
        btnPronadjiVozilo = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        txtMarkaTrazi = new javax.swing.JTextField();
        txtNazivVozilo = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        btnPonistiSve = new javax.swing.JButton();
        txtImeVozilo = new javax.swing.JTextField();
        rbPrivatnoLiceVozilo = new javax.swing.JRadioButton();
        rbPravnoLiceVozilo = new javax.swing.JRadioButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        btnPrikaziSvaVozila = new javax.swing.JButton();
        cbSvi = new javax.swing.JCheckBox();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        pnlPronadjiVlasnika = new javax.swing.JPanel();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        txtImeTrazi = new javax.swing.JTextField();
        txtPrezimeTrazi = new javax.swing.JTextField();
        btnTrazi = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txtNazivTrazi = new javax.swing.JTextField();
        rbPrivatnoTrazi = new javax.swing.JRadioButton();
        rbPravnoTrazi = new javax.swing.JRadioButton();
        btnPrikaziSve = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        btnPonisti = new javax.swing.JButton();
        labelPoruka = new javax.swing.JLabel();
        btnDodajVozilo = new javax.swing.JButton();
        btnDodajVlasnika = new javax.swing.JButton();
        btnDodajModel2 = new javax.swing.JButton();
        btnIzmijeniIzbrisiModel = new javax.swing.JButton();
        spVoziloPretraga = new javax.swing.JScrollPane();
        tableVozila = new javax.swing.JTable();
        pnlDijelovi = new javax.swing.JPanel();
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
        btnDodajModel = new javax.swing.JButton();
        jLabel49 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        pnlKnjigovodstvo = new javax.swing.JPanel();
        pnlOsnovniKnjigovodstvo = new javax.swing.JPanel();
        pnlOkvirKnjigovodstvo = new javax.swing.JPanel();
        lblRadniNalozi = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btnPredracun = new javax.swing.JButton();
        pnlNaloziKnjigovodstvo = new javax.swing.JPanel();
        pnlRadniNaloziKnjigovodstvo = new javax.swing.JScrollPane();
        tblRadniNalozi = new javax.swing.JTable();
        jLabel161 = new javax.swing.JLabel();
        txtUkupno = new javax.swing.JTextField();
        jLabel162 = new javax.swing.JLabel();
        pnlTabelaKnjigovodstvo = new javax.swing.JScrollPane();
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
        txtKnjigovodstvo = new javax.swing.JLabel();
        txtSlikaKnjigovodstvo = new javax.swing.JLabel();
        pnlZaposleni = new javax.swing.JPanel();
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
        pnlStatistika = new javax.swing.JPanel();
        pnlHeaderStatistika = new javax.swing.JPanel();
        pnlKontrolnaTablaStatistika = new javax.swing.JPanel();
        pnlZaradaUkupno = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        lblIntervalZarada = new javax.swing.JLabel();
        lblGodisnjaZarada = new javax.swing.JLabel();
        lblMjesecnaZarada = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        lblDnevnaZarada = new javax.swing.JLabel();
        pnlBrojPopravki = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        lblPopravkeDanas = new javax.swing.JLabel();
        lblPopravkeGodina = new javax.swing.JLabel();
        lblPopravkeMjesec = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        lblPopravkeInterval = new javax.swing.JLabel();
        pnlFakture = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        lblBrojPlacenihFaktura = new javax.swing.JLabel();
        lblBrojFaktura = new javax.swing.JLabel();
        lblBrojNeplacenihFaktura = new javax.swing.JLabel();
        pnlOdabirIntervala = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        btnPregled = new javax.swing.JButton();
        dcDatumOd = new com.toedter.calendar.JDateChooser();
        dcDatumDo = new com.toedter.calendar.JDateChooser();
        jLabel31 = new javax.swing.JLabel();
        pnlZaradaDijelovi = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        lblDnevnaZaradaDijelovi = new javax.swing.JLabel();
        lblGodisnjaZaradaDijelovi = new javax.swing.JLabel();
        lblMjesecnaZaradaDijelovi = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        lblIntervalZaradaDijelovi = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        tabpnlGrafici = new javax.swing.JTabbedPane();
        pnlGrafikPrihodiUkupno = new javax.swing.JPanel();
        pnlGrafikPrihodiDijelovi = new javax.swing.JPanel();
        pnlGrafikAuta = new javax.swing.JPanel();
        pnlGrafikFakture = new javax.swing.JPanel();
        pnlGrafikPopravke = new javax.swing.JPanel();
        pnlOdabirMjesecaStatistika = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        cbMjesec = new javax.swing.JComboBox<>();
        cbGodina = new javax.swing.JComboBox<>();
        btnPregledGrafik = new javax.swing.JButton();

        jLabel59.setText("jLabel59");

        menuItemDetaljniOpis.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        menuItemDetaljniOpis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/document.png"))); // NOI18N
        menuItemDetaljniOpis.setText("Detaljan opis");
        menuItemDetaljniOpis.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemDetaljniOpisActionPerformed(evt);
            }
        });
        popupMenuZaposleni.add(menuItemDetaljniOpis);

        menuItemIzmjeniRadnika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Settings_32px_3.png"))); // NOI18N
        menuItemIzmjeniRadnika.setText("Izmjeni radnika");
        menuItemIzmjeniRadnika.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemIzmjeniRadnikaActionPerformed(evt);
            }
        });
        popupMenuZaposleni.add(menuItemIzmjeniRadnika);

        menuItemOtpustiRadnika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/icon.png"))); // NOI18N
        menuItemOtpustiRadnika.setText("Otpusti radnika");
        menuItemOtpustiRadnika.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuItemOtpustiRadnikaActionPerformed(evt);
            }
        });
        popupMenuZaposleni.add(menuItemOtpustiRadnika);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AUTO-SERVIS");

        pnlMeni.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeni.setMaximumSize(new java.awt.Dimension(266, 800));
        pnlMeni.setLayout(null);
        pnlMeni.add(jLabel1);
        jLabel1.setBounds(10, 408, 0, 33);

        pnlMeniRadniNalozi.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeniRadniNalozi.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlMeniRadniNaloziMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                pnlMeniRadniNaloziMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                pnlMeniRadniNaloziMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                pnlMeniRadniNaloziMousePressed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(51, 51, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Maintenance_32px_3.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Radni nalozi");

        javax.swing.GroupLayout pnlMeniRadniNaloziLayout = new javax.swing.GroupLayout(pnlMeniRadniNalozi);
        pnlMeniRadniNalozi.setLayout(pnlMeniRadniNaloziLayout);
        pnlMeniRadniNaloziLayout.setHorizontalGroup(
            pnlMeniRadniNaloziLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMeniRadniNaloziLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlMeniRadniNaloziLayout.setVerticalGroup(
            pnlMeniRadniNaloziLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        pnlMeni.add(pnlMeniRadniNalozi);
        pnlMeniRadniNalozi.setBounds(10, 290, 260, 50);

        pnlSlika.setBackground(new java.awt.Color(51, 51, 255));

        jLabel3.setBackground(new java.awt.Color(153, 204, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/image.png"))); // NOI18N
        jLabel3.setMaximumSize(new java.awt.Dimension(640, 320));
        jLabel3.setMinimumSize(new java.awt.Dimension(640, 320));
        jLabel3.setPreferredSize(new java.awt.Dimension(640, 320));

        javax.swing.GroupLayout pnlSlikaLayout = new javax.swing.GroupLayout(pnlSlika);
        pnlSlika.setLayout(pnlSlikaLayout);
        pnlSlikaLayout.setHorizontalGroup(
            pnlSlikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSlikaLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlSlikaLayout.setVerticalGroup(
            pnlSlikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pnlMeni.add(pnlSlika);
        pnlSlika.setBounds(-1, -1, 257, 113);

        pnlMeniVozila.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeniVozila.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlMeniVozilaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                pnlMeniVozilaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                pnlMeniVozilaMouseExited(evt);
            }
        });

        jLabel7.setBackground(new java.awt.Color(51, 51, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Car_32px.png"))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Vozila");

        javax.swing.GroupLayout pnlMeniVozilaLayout = new javax.swing.GroupLayout(pnlMeniVozila);
        pnlMeniVozila.setLayout(pnlMeniVozilaLayout);
        pnlMeniVozilaLayout.setHorizontalGroup(
            pnlMeniVozilaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMeniVozilaLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMeniVozilaLayout.setVerticalGroup(
            pnlMeniVozilaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        pnlMeni.add(pnlMeniVozila);
        pnlMeniVozila.setBounds(10, 390, 260, 50);

        pnlMeniKnjigovodstvo.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeniKnjigovodstvo.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlMeniKnjigovodstvoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                pnlMeniKnjigovodstvoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                pnlMeniKnjigovodstvoMouseExited(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(51, 51, 255));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Book Stack_32px_2.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Knjigovodstvo");

        javax.swing.GroupLayout pnlMeniKnjigovodstvoLayout = new javax.swing.GroupLayout(pnlMeniKnjigovodstvo);
        pnlMeniKnjigovodstvo.setLayout(pnlMeniKnjigovodstvoLayout);
        pnlMeniKnjigovodstvoLayout.setHorizontalGroup(
            pnlMeniKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMeniKnjigovodstvoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMeniKnjigovodstvoLayout.setVerticalGroup(
            pnlMeniKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlMeni.add(pnlMeniKnjigovodstvo);
        pnlMeniKnjigovodstvo.setBounds(10, 490, 260, 50);

        pnlMeniStatistika.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeniStatistika.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlMeniStatistikaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                pnlMeniStatistikaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                pnlMeniStatistikaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                pnlMeniStatistikaMousePressed(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(51, 51, 255));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Combo Chart_32px.png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Statistika");

        javax.swing.GroupLayout pnlMeniStatistikaLayout = new javax.swing.GroupLayout(pnlMeniStatistika);
        pnlMeniStatistika.setLayout(pnlMeniStatistikaLayout);
        pnlMeniStatistikaLayout.setHorizontalGroup(
            pnlMeniStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMeniStatistikaLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlMeniStatistikaLayout.setVerticalGroup(
            pnlMeniStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        pnlMeni.add(pnlMeniStatistika);
        pnlMeniStatistika.setBounds(10, 590, 260, 50);

        jLabel2.setFont(new java.awt.Font("Goudy Old Style", 2, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Copyright Boro 2017.");
        pnlMeni.add(jLabel2);
        jLabel2.setBounds(40, 810, 160, 30);

        pnlMeniZaposleni.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeniZaposleni.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlMeniZaposleniMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                pnlMeniZaposleniMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                pnlMeniZaposleniMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                pnlMeniZaposleniMousePressed(evt);
            }
        });

        jLabel14.setBackground(new java.awt.Color(51, 51, 255));
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Gender Neutral User_32px_2.png"))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Zaposleni");

        javax.swing.GroupLayout pnlMeniZaposleniLayout = new javax.swing.GroupLayout(pnlMeniZaposleni);
        pnlMeniZaposleni.setLayout(pnlMeniZaposleniLayout);
        pnlMeniZaposleniLayout.setHorizontalGroup(
            pnlMeniZaposleniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMeniZaposleniLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlMeniZaposleniLayout.setVerticalGroup(
            pnlMeniZaposleniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlMeni.add(pnlMeniZaposleni);
        pnlMeniZaposleni.setBounds(10, 540, 261, 50);

        pnlMeniDijelovi.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeniDijelovi.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlMeniDijeloviMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                pnlMeniDijeloviMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                pnlMeniDijeloviMouseExited(evt);
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
        jLabel36.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                jLabel36KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlMeniDijeloviLayout = new javax.swing.GroupLayout(pnlMeniDijelovi);
        pnlMeniDijelovi.setLayout(pnlMeniDijeloviLayout);
        pnlMeniDijeloviLayout.setHorizontalGroup(
            pnlMeniDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMeniDijeloviLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMeniDijeloviLayout.setVerticalGroup(
            pnlMeniDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlMeni.add(pnlMeniDijelovi);
        pnlMeniDijelovi.setBounds(10, 440, 260, 50);

        pnlMeniPocetnaStrana.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeniPocetnaStrana.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlMeniPocetnaStranaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                pnlMeniPocetnaStranaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                pnlMeniPocetnaStranaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                pnlMeniPocetnaStranaMousePressed(evt);
            }
        });

        jLabel54.setBackground(new java.awt.Color(51, 51, 255));
        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Home_32px.png"))); // NOI18N

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Početna strana");

        javax.swing.GroupLayout pnlMeniPocetnaStranaLayout = new javax.swing.GroupLayout(pnlMeniPocetnaStrana);
        pnlMeniPocetnaStrana.setLayout(pnlMeniPocetnaStranaLayout);
        pnlMeniPocetnaStranaLayout.setHorizontalGroup(
            pnlMeniPocetnaStranaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMeniPocetnaStranaLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlMeniPocetnaStranaLayout.setVerticalGroup(
            pnlMeniPocetnaStranaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        pnlMeni.add(pnlMeniPocetnaStrana);
        pnlMeniPocetnaStrana.setBounds(10, 240, 260, 50);

        pnlMeniZakazivanja.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeniZakazivanja.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                pnlMeniZakazivanjaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                pnlMeniZakazivanjaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                pnlMeniZakazivanjaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                pnlMeniZakazivanjaMousePressed(evt);
            }
        });

        jLabel20.setBackground(new java.awt.Color(51, 51, 255));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Clock_32px.png"))); // NOI18N

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Zakazivanja");

        javax.swing.GroupLayout pnlMeniZakazivanjaLayout = new javax.swing.GroupLayout(pnlMeniZakazivanja);
        pnlMeniZakazivanja.setLayout(pnlMeniZakazivanjaLayout);
        pnlMeniZakazivanjaLayout.setHorizontalGroup(
            pnlMeniZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMeniZakazivanjaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMeniZakazivanjaLayout.setVerticalGroup(
            pnlMeniZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        pnlMeni.add(pnlMeniZakazivanja);
        pnlMeniZakazivanja.setBounds(10, 340, 260, 50);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(8);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        pnlParent.setBackground(new java.awt.Color(255, 255, 255));
        pnlParent.setLayout(new java.awt.CardLayout());

        pnlPocetna.setBackground(new java.awt.Color(102, 153, 255));

        jPanel5.setBackground(new java.awt.Color(102, 153, 255));

        jPanel6.setBackground(new java.awt.Color(102, 153, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel7.setBackground(new java.awt.Color(102, 153, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel11.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Datum do:");

        jLabel118.setBackground(new java.awt.Color(255, 255, 255));
        jLabel118.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel118.setForeground(new java.awt.Color(255, 255, 255));
        jLabel118.setText("Datum od:");

        btnPrikazii.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrikazii.setText("Prikaži");
        btnPrikazii.setPreferredSize(new java.awt.Dimension(105, 38));
        btnPrikazii.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrikaziiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel118, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateChooserAktivnosti2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateChooserAktivnosti1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(btnPrikazii, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateChooserAktivnosti1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel118, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateChooserAktivnosti2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnPrikazii, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Pregled aktivnosti:");
        jLabel4.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                jLabel4PropertyChange(evt);
            }
        });

        jPanel16.setBackground(new java.awt.Color(102, 153, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));

        btnPrikaziSvePredracune.setText("Prikaži sve");
        btnPrikaziSvePredracune.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrikaziSvePredracuneActionPerformed(evt);
            }
        });

        btnPrikaziPredracune.setText("Prikaži");
        btnPrikaziPredracune.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrikaziPredracuneActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(240, 240, 240));
        jLabel19.setText("Vlasnik:");

        jrbPrivatno.setForeground(new java.awt.Color(240, 240, 240));
        jrbPrivatno.setSelected(true);
        jrbPrivatno.setText("Privatno lice");
        jrbPrivatno.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jrbPrivatnoActionPerformed(evt);
            }
        });

        jrbPravnoo.setForeground(new java.awt.Color(240, 240, 240));
        jrbPravnoo.setText("Pravno lice");
        jrbPravnoo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jrbPravnooActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(240, 240, 240));
        jLabel39.setText("Naziv:");

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(240, 240, 240));
        jLabel42.setText("Ime:");

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(240, 240, 240));
        jLabel43.setText("Prezime:");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfNazivv, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel16Layout.createSequentialGroup()
                                        .addComponent(btnPrikaziSvePredracune)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPrikaziPredracune, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                                        .addComponent(jrbPrivatno, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jrbPravnoo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel16Layout.createSequentialGroup()
                                        .addComponent(tfImee, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel43)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfPrezimee, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jrbPrivatno)
                    .addComponent(jrbPravnoo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(tfNazivv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfImee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)
                    .addComponent(tfPrezimee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrikaziSvePredracune, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrikaziPredracune, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(102, 153, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));

        btnBaza.setText("Čuvanje podataka");
        btnBaza.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBazaActionPerformed(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(240, 240, 240));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("Poslednji put:");

        labelBaza.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelBaza.setForeground(new java.awt.Color(240, 240, 240));
        labelBaza.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelBaza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnBaza)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addGap(18, 18, 18)
                .addComponent(labelBaza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnBaza, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(240, 240, 240));
        jLabel37.setText("Pretraga predračuna:");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(240, 240, 240));
        jLabel38.setText("Čuvanje podataka:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addGap(42, 42, 42)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38))
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(229, 229, 229));
        jLabel16.setText("> Početna strana");

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Home_20px_1.png"))); // NOI18N

        Napomene2.setBackground(new java.awt.Color(102, 153, 255));
        Napomene2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));

        tblNeplaceneFakture.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
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
            new String []
            {
                "ID", "Automobil", "Vlasnik", "Datum otvaranja", "Datum fakturisanja", "Iznos", "Plaćeno"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblNeplaceneFakture.getTableHeader().setResizingAllowed(false);
        tblNeplaceneFakture.getTableHeader().setReorderingAllowed(false);
        tblNeplaceneFakture.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblNeplaceneFaktureMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblNeplaceneFakture);
        if (tblNeplaceneFakture.getColumnModel().getColumnCount() > 0)
        {
            tblNeplaceneFakture.getColumnModel().getColumn(0).setResizable(false);
            tblNeplaceneFakture.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblNeplaceneFakture.getColumnModel().getColumn(1).setResizable(false);
            tblNeplaceneFakture.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(240, 240, 240));
        jLabel18.setText("Predračuni:");

        javax.swing.GroupLayout Napomene2Layout = new javax.swing.GroupLayout(Napomene2);
        Napomene2.setLayout(Napomene2Layout);
        Napomene2Layout.setHorizontalGroup(
            Napomene2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Napomene2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Napomene2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
                .addContainerGap())
        );
        Napomene2Layout.setVerticalGroup(
            Napomene2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Napomene2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        danasnjeAktivnostiPanel.setBackground(new java.awt.Color(102, 153, 255));
        danasnjeAktivnostiPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));
        danasnjeAktivnostiPanel.setToolTipText("");

        datumLabel.setBackground(new java.awt.Color(255, 255, 255));
        datumLabel.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        datumLabel.setForeground(new java.awt.Color(240, 240, 240));
        datumLabel.setText("Aktivnosti: ");

        tableAktivnosti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "Id", "Vlasnik", "Auto", "Pocetak rada", "Rok"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(tableAktivnosti);
        if (tableAktivnosti.getColumnModel().getColumnCount() > 0)
        {
            tableAktivnosti.getColumnModel().getColumn(0).setResizable(false);
            tableAktivnosti.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableAktivnosti.getColumnModel().getColumn(1).setResizable(false);
            tableAktivnosti.getColumnModel().getColumn(2).setResizable(false);
            tableAktivnosti.getColumnModel().getColumn(3).setResizable(false);
            tableAktivnosti.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout danasnjeAktivnostiPanelLayout = new javax.swing.GroupLayout(danasnjeAktivnostiPanel);
        danasnjeAktivnostiPanel.setLayout(danasnjeAktivnostiPanelLayout);
        danasnjeAktivnostiPanelLayout.setHorizontalGroup(
            danasnjeAktivnostiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danasnjeAktivnostiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(danasnjeAktivnostiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(danasnjeAktivnostiPanelLayout.createSequentialGroup()
                        .addComponent(datumLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(danasnjeAktivnostiPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        danasnjeAktivnostiPanelLayout.setVerticalGroup(
            danasnjeAktivnostiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danasnjeAktivnostiPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(datumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(danasnjeAktivnostiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Napomene2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(134, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Napomene2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(danasnjeAktivnostiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        Napomene2.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout pnlPocetnaLayout = new javax.swing.GroupLayout(pnlPocetna);
        pnlPocetna.setLayout(pnlPocetnaLayout);
        pnlPocetnaLayout.setHorizontalGroup(
            pnlPocetnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnlPocetnaLayout.setVerticalGroup(
            pnlPocetnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPocetnaLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2797, Short.MAX_VALUE))
        );

        pnlParent.add(pnlPocetna, "card2");

        panelOsnovniRN.setBackground(new java.awt.Color(102, 153, 255));

        panelOsnovniPretragaRN.setBackground(new java.awt.Color(102, 153, 255));
        panelOsnovniPretragaRN.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        btnNoviRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnNoviRadniNalog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/add-documents.png"))); // NOI18N
        btnNoviRadniNalog.setText("Novi radni nalog");
        btnNoviRadniNalog.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNoviRadniNalogActionPerformed(evt);
            }
        });

        panelPretraga.setBackground(new java.awt.Color(102, 153, 255));
        panelPretraga.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblDatumOtvaranja.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDatumOtvaranja.setForeground(new java.awt.Color(255, 255, 255));
        lblDatumOtvaranja.setText("Datum otvaranja:");

        lblDatumOtvaranjaOD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDatumOtvaranjaOD.setForeground(new java.awt.Color(255, 255, 255));
        lblDatumOtvaranjaOD.setText("OD:");

        lblDatumOtvaranjaDO.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDatumOtvaranjaDO.setForeground(new java.awt.Color(255, 255, 255));
        lblDatumOtvaranjaDO.setText("DO:");

        lblPotrebnoZavrsiti.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPotrebnoZavrsiti.setForeground(new java.awt.Color(255, 255, 255));
        lblPotrebnoZavrsiti.setText("Potrebno završiti:");

        lblPotrebnoZavrsitiOD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPotrebnoZavrsitiOD.setForeground(new java.awt.Color(255, 255, 255));
        lblPotrebnoZavrsitiOD.setText("OD:");

        lblPotrebnoZavrsitiDO.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPotrebnoZavrsitiDO.setForeground(new java.awt.Color(255, 255, 255));
        lblPotrebnoZavrsitiDO.setText("DO:");

        lblDatumZatvaranjaOD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDatumZatvaranjaOD.setForeground(new java.awt.Color(255, 255, 255));
        lblDatumZatvaranjaOD.setText("OD:");

        lblDatumZatvaranjaDO.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDatumZatvaranjaDO.setForeground(new java.awt.Color(255, 255, 255));
        lblDatumZatvaranjaDO.setText("DO:");

        lblDatumZatvaranja.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDatumZatvaranja.setForeground(new java.awt.Color(255, 255, 255));
        lblDatumZatvaranja.setText("Datum zatvaranja:");

        cbDatumOtvaranja.setBackground(new java.awt.Color(102, 153, 255));
        cbDatumOtvaranja.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbDatumOtvaranja.setForeground(new java.awt.Color(255, 255, 255));
        cbDatumOtvaranja.setSelected(true);
        cbDatumOtvaranja.setText("Svi");

        cbPotrebnoZavrsiti.setBackground(new java.awt.Color(102, 153, 255));
        cbPotrebnoZavrsiti.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbPotrebnoZavrsiti.setForeground(new java.awt.Color(255, 255, 255));
        cbPotrebnoZavrsiti.setSelected(true);
        cbPotrebnoZavrsiti.setText("Svi");

        cbDatumZatvaranja.setBackground(new java.awt.Color(102, 153, 255));
        cbDatumZatvaranja.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbDatumZatvaranja.setForeground(new java.awt.Color(255, 255, 255));
        cbDatumZatvaranja.setSelected(true);
        cbDatumZatvaranja.setText("Svi");

        btnPrikaziRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnPrikaziRadniNalog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnPrikaziRadniNalog.setText("Prikaži radne naloge");
        btnPrikaziRadniNalog.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrikaziRadniNalogActionPerformed(evt);
            }
        });

        tfRegistracijaRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel84.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(255, 255, 255));
        jLabel84.setText("Registracija:");

        tfNazivRadniNalog.setEditable(false);
        tfNazivRadniNalog.setBackground(java.awt.Color.gray);
        tfNazivRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        tfImeRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        rbPravnoRadniNalog.setBackground(new java.awt.Color(102, 153, 255));
        rbPravnoRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPravnoRadniNalog.setForeground(new java.awt.Color(255, 255, 255));
        rbPravnoRadniNalog.setText("Pravno lice");
        rbPravnoRadniNalog.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbPravnoRadniNalogActionPerformed(evt);
            }
        });

        rbPrivatnoRadniNalog.setBackground(new java.awt.Color(102, 153, 255));
        rbPrivatnoRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPrivatnoRadniNalog.setForeground(new java.awt.Color(255, 255, 255));
        rbPrivatnoRadniNalog.setSelected(true);
        rbPrivatnoRadniNalog.setText("Privatno lice");
        rbPrivatnoRadniNalog.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbPrivatnoRadniNalogActionPerformed(evt);
            }
        });

        lblVlasnik.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblVlasnik.setForeground(new java.awt.Color(255, 255, 255));
        lblVlasnik.setText("Vlasnik:");

        lblPrezimeRN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPrezimeRN.setForeground(new java.awt.Color(255, 255, 255));
        lblPrezimeRN.setText("Prezime:");

        lblImeRN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblImeRN.setForeground(new java.awt.Color(255, 255, 255));
        lblImeRN.setText("Ime:");

        lblNazivRN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNazivRN.setForeground(new java.awt.Color(255, 255, 255));
        lblNazivRN.setText("Naziv:");

        cbSviRadniNalog.setBackground(new java.awt.Color(102, 153, 255));
        cbSviRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSviRadniNalog.setForeground(new java.awt.Color(255, 255, 255));
        cbSviRadniNalog.setText("Svi");
        cbSviRadniNalog.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cbSviRadniNalogActionPerformed(evt);
            }
        });

        tfPrezimeRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout panelPretragaLayout = new javax.swing.GroupLayout(panelPretraga);
        panelPretraga.setLayout(panelPretragaLayout);
        panelPretragaLayout.setHorizontalGroup(
            panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPretragaLayout.createSequentialGroup()
                .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelPretragaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(panelPretragaLayout.createSequentialGroup()
                                .addComponent(lblDatumOtvaranja)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblDatumOtvaranjaOD))
                            .addGroup(panelPretragaLayout.createSequentialGroup()
                                .addComponent(lblDatumZatvaranja)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblDatumZatvaranjaOD))
                            .addGroup(panelPretragaLayout.createSequentialGroup()
                                .addComponent(lblPotrebnoZavrsiti)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblPotrebnoZavrsitiOD))))
                    .addGroup(panelPretragaLayout.createSequentialGroup()
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPretragaLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbDatumOtvaranja)
                                    .addComponent(cbPotrebnoZavrsiti)))
                            .addGroup(panelPretragaLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cbDatumZatvaranja)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblDatumOtvaranjaDO)
                            .addComponent(lblDatumZatvaranjaDO)
                            .addComponent(lblPotrebnoZavrsitiDO))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dcDatumOtvaranjaDO, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                    .addComponent(dcDatumOtvaranjaOD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcPotrebnoZavrsitiOD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcPotrebnoZavrsitiDO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcDatumZatvaranjaOD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcDatumZatvaranjaDO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(72, 72, 72)
                .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPretragaLayout.createSequentialGroup()
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPrezimeRN)
                            .addComponent(lblImeRN)
                            .addComponent(lblNazivRN)
                            .addComponent(lblVlasnik))
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPretragaLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(rbPrivatnoRadniNalog)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbPravnoRadniNalog)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbSviRadniNalog))
                            .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelPretragaLayout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tfPrezimeRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panelPretragaLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(btnPrikaziRadniNalog)
                                            .addComponent(tfNazivRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelPretragaLayout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(tfImeRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(separatorRN, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelPretragaLayout.createSequentialGroup()
                            .addComponent(jLabel84)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tfRegistracijaRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelPretragaLayout.setVerticalGroup(
            panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPretragaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPretragaLayout.createSequentialGroup()
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcDatumOtvaranjaOD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDatumOtvaranja)
                                .addComponent(lblDatumOtvaranjaOD)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcDatumOtvaranjaDO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDatumOtvaranjaDO)
                                .addComponent(cbDatumOtvaranja)))
                        .addGap(29, 29, 29)
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPretragaLayout.createSequentialGroup()
                                .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dcPotrebnoZavrsitiOD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblPotrebnoZavrsiti)
                                        .addComponent(lblPotrebnoZavrsitiOD)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dcPotrebnoZavrsitiDO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblPotrebnoZavrsitiDO)
                                        .addComponent(cbPotrebnoZavrsiti)))
                                .addGap(26, 26, 26)
                                .addComponent(dcDatumZatvaranjaOD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDatumZatvaranjaOD)
                                .addComponent(lblDatumZatvaranja)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcDatumZatvaranjaDO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDatumZatvaranjaDO)
                                .addComponent(cbDatumZatvaranja))))
                    .addGroup(panelPretragaLayout.createSequentialGroup()
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfRegistracijaRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel84))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(separatorRN, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbPrivatnoRadniNalog)
                            .addComponent(rbPravnoRadniNalog)
                            .addComponent(lblVlasnik)
                            .addComponent(cbSviRadniNalog))
                        .addGap(27, 27, 27)
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPrezimeRN)
                            .addComponent(tfPrezimeRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfImeRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblImeRN))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNazivRN)
                            .addComponent(tfNazivRadniNalog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrikaziRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelOsnovniPretragaRNLayout = new javax.swing.GroupLayout(panelOsnovniPretragaRN);
        panelOsnovniPretragaRN.setLayout(panelOsnovniPretragaRNLayout);
        panelOsnovniPretragaRNLayout.setHorizontalGroup(
            panelOsnovniPretragaRNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOsnovniPretragaRNLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(panelPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnNoviRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelOsnovniPretragaRNLayout.setVerticalGroup(
            panelOsnovniPretragaRNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOsnovniPretragaRNLayout.createSequentialGroup()
                .addGroup(panelOsnovniPretragaRNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOsnovniPretragaRNLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOsnovniPretragaRNLayout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(btnNoviRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbRadniNaloziNaslov.setBackground(new java.awt.Color(255, 255, 255));
        lbRadniNaloziNaslov.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbRadniNaloziNaslov.setForeground(new java.awt.Color(229, 229, 229));
        lbRadniNaloziNaslov.setText("> Radni nalozi");

        lbRadniNaloziSlika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Maintenance_20px_2.png"))); // NOI18N

        javax.swing.GroupLayout panelOsnovniRNLayout = new javax.swing.GroupLayout(panelOsnovniRN);
        panelOsnovniRN.setLayout(panelOsnovniRNLayout);
        panelOsnovniRNLayout.setHorizontalGroup(
            panelOsnovniRNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOsnovniRNLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelOsnovniPretragaRN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelOsnovniRNLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(lbRadniNaloziSlika, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbRadniNaloziNaslov)
                .addContainerGap(2994, Short.MAX_VALUE))
        );
        panelOsnovniRNLayout.setVerticalGroup(
            panelOsnovniRNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOsnovniRNLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(panelOsnovniRNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbRadniNaloziNaslov)
                    .addComponent(lbRadniNaloziSlika, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(panelOsnovniPretragaRN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        spanelTabela.setBackground(new java.awt.Color(102, 153, 255));

        tableRNalozi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {
                "ID", "Registracija vozila", "Vlasnik vozila", "Datum otvaranja", "Potrebno završiti do", "Datum zatvaranja", "Troškovi dijelova", "Cijena usluge", "Plaćeno"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        spanelTabela.setViewportView(tableRNalozi);

        javax.swing.GroupLayout pnlRadniNaloziLayout = new javax.swing.GroupLayout(pnlRadniNalozi);
        pnlRadniNalozi.setLayout(pnlRadniNaloziLayout);
        pnlRadniNaloziLayout.setHorizontalGroup(
            pnlRadniNaloziLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelOsnovniRN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlRadniNaloziLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spanelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 1077, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlRadniNaloziLayout.setVerticalGroup(
            pnlRadniNaloziLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRadniNaloziLayout.createSequentialGroup()
                .addComponent(panelOsnovniRN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spanelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2825, Short.MAX_VALUE))
        );

        pnlParent.add(pnlRadniNalozi, "card9");

        pnlOsnovniZakazivanja.setBackground(new java.awt.Color(102, 153, 255));
        pnlOsnovniZakazivanja.setPreferredSize(new java.awt.Dimension(1485, 980));

        pnlOkvirZakazivanja.setBackground(new java.awt.Color(102, 153, 255));
        pnlOkvirZakazivanja.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        pnlOkvirZakazivanja.setAlignmentX(1.0F);
        pnlOkvirZakazivanja.setAlignmentY(1.0F);

        txtPronadjiZakazivanja.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtPronadjiZakazivanja.setForeground(new java.awt.Color(255, 255, 255));
        txtPronadjiZakazivanja.setText("Zakaži termin:");

        txtZakaziZakazivanja.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtZakaziZakazivanja.setForeground(new java.awt.Color(255, 255, 255));
        txtZakaziZakazivanja.setText("Pronađi termin:");

        pnlPronadjiZakazivanja.setBackground(new java.awt.Color(102, 153, 255));
        pnlPronadjiZakazivanja.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
        btnPronadjiTermin.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPronadjiTerminActionPerformed(evt);
            }
        });

        btnPonistiUnosePretraga.setText("Poništi unose");
        btnPonistiUnosePretraga.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPonistiUnosePretragaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPronadjiZakazivanjaLayout = new javax.swing.GroupLayout(pnlPronadjiZakazivanja);
        pnlPronadjiZakazivanja.setLayout(pnlPronadjiZakazivanjaLayout);
        pnlPronadjiZakazivanjaLayout.setHorizontalGroup(
            pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPronadjiZakazivanjaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPronadjiZakazivanjaLayout.createSequentialGroup()
                        .addComponent(btnPronadjiTermin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                        .addComponent(btnPonistiUnosePretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPronadjiZakazivanjaLayout.createSequentialGroup()
                        .addComponent(jLabel144)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtBrojTelefonaPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlPronadjiZakazivanjaLayout.createSequentialGroup()
                        .addComponent(jLabel133)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMarkaPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlPronadjiZakazivanjaLayout.createSequentialGroup()
                        .addComponent(jLabel145)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPrezimePretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPronadjiZakazivanjaLayout.createSequentialGroup()
                        .addGroup(pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel143)
                            .addComponent(jLabel136))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtImePretraga, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dtmDatumPretraga, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10))
        );
        pnlPronadjiZakazivanjaLayout.setVerticalGroup(
            pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPronadjiZakazivanjaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel133)
                    .addComponent(txtMarkaPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPronadjiZakazivanjaLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel136))
                    .addGroup(pnlPronadjiZakazivanjaLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(dtmDatumPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel143)
                    .addComponent(txtImePretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel145)
                    .addComponent(txtPrezimePretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel144)
                    .addComponent(txtBrojTelefonaPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlPronadjiZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPronadjiTermin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPonistiUnosePretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnlZakaziZakazivanja.setBackground(new java.awt.Color(102, 153, 255));
        pnlZakaziZakazivanja.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
        btnDodajTermin.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDodajTerminActionPerformed(evt);
            }
        });

        jLabel130.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel130.setForeground(new java.awt.Color(255, 255, 255));
        jLabel130.setText("Vrijeme termina:");

        jLabel134.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(255, 255, 255));
        jLabel134.setText("Datum termina:");

        txtVrijemeTerminaSati.setModel(new javax.swing.SpinnerNumberModel(8, 7, 23, 1));

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
        btnPonistiUnoseTermin.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPonistiUnoseTerminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlZakaziZakazivanjaLayout = new javax.swing.GroupLayout(pnlZakaziZakazivanja);
        pnlZakaziZakazivanja.setLayout(pnlZakaziZakazivanjaLayout);
        pnlZakaziZakazivanjaLayout.setHorizontalGroup(
            pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZakaziZakazivanjaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlZakaziZakazivanjaLayout.createSequentialGroup()
                        .addComponent(btnDodajTermin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                        .addComponent(btnPonistiUnoseTermin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlZakaziZakazivanjaLayout.createSequentialGroup()
                        .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel131)
                            .addComponent(jLabel135)
                            .addComponent(jLabel141)
                            .addComponent(jLabel140)
                            .addComponent(jLabel142)
                            .addComponent(jLabel130))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlZakaziZakazivanjaLayout.createSequentialGroup()
                                .addComponent(txtVrijemeTerminaSati, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtVrijemeTerminaMinuti, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtMarkaTermina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtModelTermina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtImeTermina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrezimeTermina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBrojTelefonaTermina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlZakaziZakazivanjaLayout.createSequentialGroup()
                        .addComponent(jLabel134)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dtmDatumTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlZakaziZakazivanjaLayout.setVerticalGroup(
            pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZakaziZakazivanjaLayout.createSequentialGroup()
                .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlZakaziZakazivanjaLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDodajTermin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlZakaziZakazivanjaLayout.createSequentialGroup()
                        .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlZakaziZakazivanjaLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel134))
                            .addGroup(pnlZakaziZakazivanjaLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(dtmDatumTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtVrijemeTerminaSati, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtVrijemeTerminaMinuti, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel130)
                                    .addComponent(jLabel156)
                                    .addComponent(jLabel157))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel131)
                            .addComponent(txtMarkaTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel135)
                            .addComponent(txtModelTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel141)
                            .addComponent(txtImeTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel140)
                            .addComponent(txtPrezimeTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(pnlZakaziZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel142)
                            .addComponent(txtBrojTelefonaTermina, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPonistiUnoseTermin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlTabelaZakazivanje.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnlTabelaZakazivanje.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        pnlTabelaZakazivanje.setMaximumSize(new java.awt.Dimension(920, 800));
        pnlTabelaZakazivanje.setPreferredSize(new java.awt.Dimension(920, 800));

        tblTermini.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
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
            new String []
            {
                "Datum termina", "Vrijeme termina", "Datum zakazivanja", "Marka", "Model", "Ime", "Prezime", "Broj telefona"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblTermini.setMaximumSize(new java.awt.Dimension(1000, 288));
        tblTermini.setMinimumSize(new java.awt.Dimension(1000, 288));
        tblTermini.setPreferredSize(new java.awt.Dimension(1000, 288));
        tblTermini.getTableHeader().setResizingAllowed(false);
        tblTermini.getTableHeader().setReorderingAllowed(false);
        pnlTabelaZakazivanje.setViewportView(tblTermini);

        javax.swing.GroupLayout pnlOkvirZakazivanjaLayout = new javax.swing.GroupLayout(pnlOkvirZakazivanja);
        pnlOkvirZakazivanja.setLayout(pnlOkvirZakazivanjaLayout);
        pnlOkvirZakazivanjaLayout.setHorizontalGroup(
            pnlOkvirZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOkvirZakazivanjaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOkvirZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTabelaZakazivanje, javax.swing.GroupLayout.DEFAULT_SIZE, 1043, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOkvirZakazivanjaLayout.createSequentialGroup()
                        .addGroup(pnlOkvirZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlPronadjiZakazivanja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtZakaziZakazivanja))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlOkvirZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPronadjiZakazivanja)
                            .addComponent(pnlZakaziZakazivanja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        pnlOkvirZakazivanjaLayout.setVerticalGroup(
            pnlOkvirZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOkvirZakazivanjaLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(pnlOkvirZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtZakaziZakazivanja)
                    .addComponent(txtPronadjiZakazivanja))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlOkvirZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPronadjiZakazivanja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlZakaziZakazivanja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(pnlTabelaZakazivanje, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        txtZakazivanja.setBackground(new java.awt.Color(255, 255, 255));
        txtZakazivanja.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtZakazivanja.setForeground(new java.awt.Color(229, 229, 229));
        txtZakazivanja.setText(">Zakazivanja");

        txtSlikaZakazivanja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Clock_20px.png"))); // NOI18N

        javax.swing.GroupLayout pnlOsnovniZakazivanjaLayout = new javax.swing.GroupLayout(pnlOsnovniZakazivanja);
        pnlOsnovniZakazivanja.setLayout(pnlOsnovniZakazivanjaLayout);
        pnlOsnovniZakazivanjaLayout.setHorizontalGroup(
            pnlOsnovniZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOsnovniZakazivanjaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSlikaZakazivanja, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtZakazivanja)
                .addContainerGap(2875, Short.MAX_VALUE))
            .addGroup(pnlOsnovniZakazivanjaLayout.createSequentialGroup()
                .addComponent(pnlOkvirZakazivanja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlOsnovniZakazivanjaLayout.setVerticalGroup(
            pnlOsnovniZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOsnovniZakazivanjaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOsnovniZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSlikaZakazivanja, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtZakazivanja))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlOkvirZakazivanja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2018, 2018, 2018))
        );

        javax.swing.GroupLayout pnlZakazivanjaLayout = new javax.swing.GroupLayout(pnlZakazivanja);
        pnlZakazivanja.setLayout(pnlZakazivanjaLayout);
        pnlZakazivanjaLayout.setHorizontalGroup(
            pnlZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZakazivanjaLayout.createSequentialGroup()
                .addComponent(pnlOsnovniZakazivanja, javax.swing.GroupLayout.PREFERRED_SIZE, 3042, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 124, Short.MAX_VALUE))
        );
        pnlZakazivanjaLayout.setVerticalGroup(
            pnlZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZakazivanjaLayout.createSequentialGroup()
                .addComponent(pnlOsnovniZakazivanja, javax.swing.GroupLayout.DEFAULT_SIZE, 3512, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlParent.add(pnlZakazivanja, "card8");

        pnlVozila.setBackground(new java.awt.Color(102, 153, 255));
        pnlVozila.setPreferredSize(new java.awt.Dimension(1088, 685));

        pnllAkcijeNaFormi.setBackground(new java.awt.Color(102, 153, 255));

        lblNaslovVozila.setBackground(new java.awt.Color(255, 255, 255));
        lblNaslovVozila.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblNaslovVozila.setForeground(new java.awt.Color(229, 229, 229));
        lblNaslovVozila.setText(">Vozila");

        lblSlikaVozila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Car_32px.png"))); // NOI18N

        pnlVozilo.setBackground(new java.awt.Color(102, 153, 255));
        pnlVozilo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        pnllPronadjiVozilo.setBackground(new java.awt.Color(102, 153, 255));
        pnllPronadjiVozilo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        lblRegistracijaVozila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblRegistracijaVozila.setForeground(new java.awt.Color(255, 255, 255));
        lblRegistracijaVozila.setText("Registracija:");

        lblGodisteVozila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblGodisteVozila.setForeground(new java.awt.Color(255, 255, 255));
        lblGodisteVozila.setText("Godište:");

        lblMarkaVozila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMarkaVozila.setForeground(new java.awt.Color(255, 255, 255));
        lblMarkaVozila.setText("Model:");

        lblPrezimeVozila.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPrezimeVozila.setForeground(new java.awt.Color(255, 255, 255));
        lblPrezimeVozila.setText("Prezime:");

        txtModelTrazi.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusGained(java.awt.event.FocusEvent evt)
            {
                txtModelTraziFocusGained(evt);
            }
        });
        txtModelTrazi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                txtModelTraziActionPerformed(evt);
            }
        });

        btnPronadjiVozilo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPronadjiVozilo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnPronadjiVozilo.setText("Traži");
        btnPronadjiVozilo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPronadjiVoziloActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Marka:");

        txtNazivVozilo.setEditable(false);

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Naziv:");

        btnPonistiSve.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPonistiSve.setText("Poništi unose");
        btnPonistiSve.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPonistiSveActionPerformed(evt);
            }
        });

        rbPrivatnoLiceVozilo.setBackground(new java.awt.Color(102, 153, 255));
        rbPrivatnoLiceVozilo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPrivatnoLiceVozilo.setForeground(new java.awt.Color(255, 255, 255));
        rbPrivatnoLiceVozilo.setSelected(true);
        rbPrivatnoLiceVozilo.setText("Privatno lice");
        rbPrivatnoLiceVozilo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbPrivatnoLiceVoziloActionPerformed(evt);
            }
        });

        rbPravnoLiceVozilo.setBackground(new java.awt.Color(102, 153, 255));
        rbPravnoLiceVozilo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPravnoLiceVozilo.setForeground(new java.awt.Color(255, 255, 255));
        rbPravnoLiceVozilo.setText("Pravno lice");
        rbPravnoLiceVozilo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbPravnoLiceVoziloActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Ime:");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Vlasnik:");

        btnPrikaziSvaVozila.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrikaziSvaVozila.setText("Prikaži sve");
        btnPrikaziSvaVozila.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrikaziSvaVozilaActionPerformed(evt);
            }
        });

        cbSvi.setBackground(new java.awt.Color(102, 153, 255));
        cbSvi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSvi.setForeground(new java.awt.Color(240, 240, 240));
        cbSvi.setText("Svi");
        cbSvi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cbSviActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnllPronadjiVoziloLayout = new javax.swing.GroupLayout(pnllPronadjiVozilo);
        pnllPronadjiVozilo.setLayout(pnllPronadjiVoziloLayout);
        pnllPronadjiVoziloLayout.setHorizontalGroup(
            pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnllPronadjiVoziloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnPrikaziSvaVozila, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRegistracijaVozila)
                            .addComponent(lblGodisteVozila))
                        .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPonistiSve, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPrezimeVozila, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtImeVozilo)
                    .addComponent(txtNazivVozilo)
                    .addComponent(txtPrezimeVozilo)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnllPronadjiVoziloLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnPronadjiVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnllPronadjiVoziloLayout.createSequentialGroup()
                        .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtGodisteTrazi, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnllPronadjiVoziloLayout.createSequentialGroup()
                                .addComponent(txtMarkaTrazi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblMarkaVozila)
                                .addGap(2, 2, 2)
                                .addComponent(txtModelTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnllPronadjiVoziloLayout.createSequentialGroup()
                                .addComponent(rbPrivatnoLiceVozilo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rbPravnoLiceVozilo))
                            .addComponent(txtRegistracijaTrazi, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 4, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnllPronadjiVoziloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbSvi, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );
        pnllPronadjiVoziloLayout.setVerticalGroup(
            pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnllPronadjiVoziloLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegistracijaVozila)
                    .addComponent(txtRegistracijaTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGodisteVozila)
                    .addComponent(txtGodisteTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMarkaVozila)
                    .addComponent(txtModelTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(txtMarkaTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbSvi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbPrivatnoLiceVozilo)
                    .addComponent(rbPravnoLiceVozilo)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrezimeVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrezimeVozila))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtImeVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNazivVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnllPronadjiVoziloLayout.createSequentialGroup()
                        .addComponent(btnPrikaziSvaVozila, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPonistiSve, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnPronadjiVozilo))
                .addContainerGap())
        );

        jLabel96.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 255, 255));
        jLabel96.setText("Pronađi vozilo");

        jLabel97.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 255, 255));
        jLabel97.setText("Pronađi vlasnika:");

        pnlPronadjiVlasnika.setBackground(new java.awt.Color(102, 153, 255));
        pnlPronadjiVlasnika.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel109.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel109.setForeground(new java.awt.Color(255, 255, 255));
        jLabel109.setText("Ime:");

        jLabel110.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel110.setForeground(new java.awt.Color(255, 255, 255));
        jLabel110.setText("Prezime:");

        btnTrazi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTrazi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnTrazi.setText("Traži");
        btnTrazi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTraziActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Naziv:");

        txtNazivTrazi.setEditable(false);

        rbPrivatnoTrazi.setBackground(new java.awt.Color(102, 153, 255));
        rbPrivatnoTrazi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPrivatnoTrazi.setForeground(new java.awt.Color(255, 255, 255));
        rbPrivatnoTrazi.setSelected(true);
        rbPrivatnoTrazi.setText("Privatno lice");
        rbPrivatnoTrazi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbPrivatnoTraziActionPerformed(evt);
            }
        });

        rbPravnoTrazi.setBackground(new java.awt.Color(102, 153, 255));
        rbPravnoTrazi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPravnoTrazi.setForeground(new java.awt.Color(255, 255, 255));
        rbPravnoTrazi.setText("Pravno lice");
        rbPravnoTrazi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbPravnoTraziActionPerformed(evt);
            }
        });

        btnPrikaziSve.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrikaziSve.setText("Prikaži sve");
        btnPrikaziSve.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPrikaziSveActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Vlasnik:");

        btnPonisti.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPonisti.setText("Poništi unose");
        btnPonisti.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPonistiActionPerformed(evt);
            }
        });

        labelPoruka.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout pnlPronadjiVlasnikaLayout = new javax.swing.GroupLayout(pnlPronadjiVlasnika);
        pnlPronadjiVlasnika.setLayout(pnlPronadjiVlasnikaLayout);
        pnlPronadjiVlasnikaLayout.setHorizontalGroup(
            pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPronadjiVlasnikaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPronadjiVlasnikaLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPronadjiVlasnikaLayout.createSequentialGroup()
                                .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel110)
                                    .addComponent(jLabel109)
                                    .addComponent(jLabel27))
                                .addGap(18, 18, 18)
                                .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPronadjiVlasnikaLayout.createSequentialGroup()
                                            .addComponent(rbPrivatnoTrazi)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(rbPravnoTrazi))
                                        .addComponent(txtPrezimeTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtNazivTrazi, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                                    .addComponent(txtImeTrazi)
                                    .addComponent(labelPoruka))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlPronadjiVlasnikaLayout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(223, 223, 223))))
                    .addGroup(pnlPronadjiVlasnikaLayout.createSequentialGroup()
                        .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnPonisti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPrikaziSve, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTrazi)))
                .addContainerGap())
        );

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {rbPravnoTrazi, rbPrivatnoTrazi});

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtImeTrazi, txtNazivTrazi, txtPrezimeTrazi});

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel109, jLabel110, jLabel27});

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnPonisti, btnPrikaziSve, btnTrazi});

        pnlPronadjiVlasnikaLayout.setVerticalGroup(
            pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPronadjiVlasnikaLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbPravnoTrazi)
                    .addComponent(rbPrivatnoTrazi)
                    .addComponent(jLabel24))
                .addGap(21, 21, 21)
                .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel110)
                    .addComponent(txtPrezimeTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtImeTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel109))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNazivTrazi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelPoruka)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrikaziSve, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTrazi)
                    .addGroup(pnlPronadjiVlasnikaLayout.createSequentialGroup()
                        .addComponent(btnPonisti, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addContainerGap())
        );

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {rbPravnoTrazi, rbPrivatnoTrazi});

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtImeTrazi, txtNazivTrazi, txtPrezimeTrazi});

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel109, jLabel110, jLabel27});

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnPonisti, btnPrikaziSve, btnTrazi});

        btnDodajVozilo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnDodajVozilo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/racing (1).png"))); // NOI18N
        btnDodajVozilo.setText("Dodaj Vozilo");
        btnDodajVozilo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDodajVoziloActionPerformed(evt);
            }
        });

        btnDodajVlasnika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnDodajVlasnika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/user (2).png"))); // NOI18N
        btnDodajVlasnika.setText("Dodaj Vlasnika");
        btnDodajVlasnika.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDodajVlasnikaActionPerformed(evt);
            }
        });

        btnDodajModel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnDodajModel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/cube.png"))); // NOI18N
        btnDodajModel2.setText("Dodaj Model");
        btnDodajModel2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDodajModel2ActionPerformed(evt);
            }
        });

        btnIzmijeniIzbrisiModel.setText("Izmijeni/Izbriši model");
        btnIzmijeniIzbrisiModel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnIzmijeniIzbrisiModelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlVoziloLayout = new javax.swing.GroupLayout(pnlVozilo);
        pnlVozilo.setLayout(pnlVoziloLayout);
        pnlVoziloLayout.setHorizontalGroup(
            pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVoziloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel96)
                    .addComponent(pnllPronadjiVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel97)
                    .addGroup(pnlVoziloLayout.createSequentialGroup()
                        .addComponent(pnlPronadjiVlasnika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDodajVozilo, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .addComponent(btnDodajVlasnika, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDodajModel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnIzmijeniIzbrisiModel, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(210, 210, 210))
        );

        pnlVoziloLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDodajModel2, btnDodajVlasnika, btnDodajVozilo});

        pnlVoziloLayout.setVerticalGroup(
            pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVoziloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel96)
                    .addComponent(jLabel97))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnllPronadjiVozilo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlPronadjiVlasnika, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlVoziloLayout.createSequentialGroup()
                        .addComponent(btnDodajVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDodajVlasnika, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnIzmijeniIzbrisiModel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDodajModel2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
        );

        pnlVoziloLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDodajModel2, btnDodajVlasnika, btnDodajVozilo});

        tableVozila.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        spVoziloPretraga.setViewportView(tableVozila);

        javax.swing.GroupLayout pnllAkcijeNaFormiLayout = new javax.swing.GroupLayout(pnllAkcijeNaFormi);
        pnllAkcijeNaFormi.setLayout(pnllAkcijeNaFormiLayout);
        pnllAkcijeNaFormiLayout.setHorizontalGroup(
            pnllAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnllAkcijeNaFormiLayout.createSequentialGroup()
                .addGroup(pnllAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnllAkcijeNaFormiLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblSlikaVozila, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNaslovVozila))
                    .addGroup(pnllAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(spVoziloPretraga, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnllAkcijeNaFormiLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(pnlVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, 1053, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(2103, Short.MAX_VALUE))
        );
        pnllAkcijeNaFormiLayout.setVerticalGroup(
            pnllAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnllAkcijeNaFormiLayout.createSequentialGroup()
                .addGroup(pnllAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNaslovVozila)
                    .addComponent(lblSlikaVozila, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spVoziloPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlVozilaLayout = new javax.swing.GroupLayout(pnlVozila);
        pnlVozila.setLayout(pnlVozilaLayout);
        pnlVozilaLayout.setHorizontalGroup(
            pnlVozilaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnllAkcijeNaFormi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlVozilaLayout.setVerticalGroup(
            pnlVozilaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVozilaLayout.createSequentialGroup()
                .addComponent(pnllAkcijeNaFormi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlParent.add(pnlVozila, "card6");

        pnlDijelovi.setBackground(new java.awt.Color(255, 255, 255));
        pnlDijelovi.setNextFocusableComponent(tfId);

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
        cbMarka.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                cbMarkaItemStateChanged(evt);
            }
        });

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
        btnPretrazi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnPretrazi.setText("Traži");
        btnPretrazi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPretraziActionPerformed(evt);
            }
        });

        btnSviDijelovi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSviDijelovi.setText("Prikaži sve");
        btnSviDijelovi.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSviDijeloviActionPerformed(evt);
            }
        });

        btnProdani.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnProdani.setText("Prodani dijelovi");
        btnProdani.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
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
                        .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pretraziPanelLayout.createSequentialGroup()
                                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel77, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(pretraziPanelLayout.createSequentialGroup()
                                .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)))
                        .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbModel, 0, 100, Short.MAX_VALUE)
                            .addComponent(cbMarka, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbGorivo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10))
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
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pretraziPanelLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSviDijelovi)
                            .addComponent(btnProdani)))
                    .addGroup(pretraziPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnPretrazi)))
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
        jcbMarka.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                jcbMarkaItemStateChanged(evt);
            }
        });

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
        btnDodaj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/add (1).png"))); // NOI18N
        btnDodaj.setText("DODAJ");
        btnDodaj.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDodajActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Količina:");

        btnDodajModel.setText("Dodaj model");
        btnDodajModel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDodajModelActionPerformed(evt);
            }
        });

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
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dodajDioPanelLayout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(dodajDioPanelLayout.createSequentialGroup()
                                .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jcbGorivo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dodajDioPanelLayout.createSequentialGroup()
                                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jcbModel, 0, 100, Short.MAX_VALUE)
                                    .addComponent(jcbMarka, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dodajDioPanelLayout.createSequentialGroup()
                                .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbStanje, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dodajDioPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDodaj)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDodajModel)))
                .addContainerGap(68, Short.MAX_VALUE))
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
                .addGap(15, 15, 15)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(jtfKolicina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnDodajModel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        dodajDioPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDodaj, btnDodajModel});

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
                .addContainerGap(24, Short.MAX_VALUE))
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
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel47)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            new Object [][]
            {

            },
            new String []
            {
                "Id", "Šifra", "Naziv", "Marka", "Model", "Godište", "Vrsta goriva", "Cijena", "Količina", "Novo"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        jTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable);

        javax.swing.GroupLayout pnlDijeloviLayout = new javax.swing.GroupLayout(pnlDijelovi);
        pnlDijelovi.setLayout(pnlDijeloviLayout);
        pnlDijeloviLayout.setHorizontalGroup(
            pnlDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDijeloviLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1048, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2108, Short.MAX_VALUE))
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlDijeloviLayout.setVerticalGroup(
            pnlDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDijeloviLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2748, Short.MAX_VALUE))
        );

        pnlParent.add(pnlDijelovi, "card4");
        pnlDijelovi.getAccessibleContext().setAccessibleName("");

        pnlKnjigovodstvo.setBackground(new java.awt.Color(102, 153, 255));

        pnlOsnovniKnjigovodstvo.setBackground(new java.awt.Color(102, 153, 255));
        pnlOsnovniKnjigovodstvo.setPreferredSize(new java.awt.Dimension(895, 600));

        pnlOkvirKnjigovodstvo.setBackground(new java.awt.Color(102, 153, 255));
        pnlOkvirKnjigovodstvo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        lblRadniNalozi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblRadniNalozi.setForeground(new java.awt.Color(255, 255, 255));
        lblRadniNalozi.setText("Radni nalozi:");

        btnPredracun.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPredracun.setText("Predračun");
        btnPredracun.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPredracunActionPerformed(evt);
            }
        });

        pnlNaloziKnjigovodstvo.setBackground(new java.awt.Color(102, 153, 255));
        pnlNaloziKnjigovodstvo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnlRadniNaloziKnjigovodstvo.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnlRadniNaloziKnjigovodstvo.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblRadniNalozi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
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
            new String []
            {
                "ID", "Automobil", "Vlasnik", "Datum otvaranja", "Datum fakturisanja", "Iznos", "Plaćeno"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblRadniNalozi.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblRadniNaloziMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                tblRadniNaloziMouseReleased(evt);
            }
        });
        pnlRadniNaloziKnjigovodstvo.setViewportView(tblRadniNalozi);
        if (tblRadniNalozi.getColumnModel().getColumnCount() > 0)
        {
            tblRadniNalozi.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblRadniNalozi.getColumnModel().getColumn(0).setMaxWidth(40);
            tblRadniNalozi.getColumnModel().getColumn(5).setPreferredWidth(40);
            tblRadniNalozi.getColumnModel().getColumn(5).setMaxWidth(40);
            tblRadniNalozi.getColumnModel().getColumn(6).setPreferredWidth(40);
            tblRadniNalozi.getColumnModel().getColumn(6).setMaxWidth(40);
        }

        javax.swing.GroupLayout pnlNaloziKnjigovodstvoLayout = new javax.swing.GroupLayout(pnlNaloziKnjigovodstvo);
        pnlNaloziKnjigovodstvo.setLayout(pnlNaloziKnjigovodstvoLayout);
        pnlNaloziKnjigovodstvoLayout.setHorizontalGroup(
            pnlNaloziKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNaloziKnjigovodstvoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlRadniNaloziKnjigovodstvo)
                .addContainerGap())
        );
        pnlNaloziKnjigovodstvoLayout.setVerticalGroup(
            pnlNaloziKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNaloziKnjigovodstvoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlRadniNaloziKnjigovodstvo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel161.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel161.setForeground(new java.awt.Color(255, 255, 255));
        jLabel161.setText("Ukupno:");

        txtUkupno.setEditable(false);
        txtUkupno.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtUkupno.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel162.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel162.setForeground(new java.awt.Color(255, 255, 255));
        jLabel162.setText("KM");

        pnlTabelaKnjigovodstvo.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnlTabelaKnjigovodstvo.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tblFaktura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
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
            new String []
            {
                "ID", "Naziv", "Količina", "Osnovica", "Cijena sa PDV"
            }
        )
        {
            boolean[] canEdit = new boolean []
            {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        tblFaktura.setMaximumSize(new java.awt.Dimension(1000, 480));
        tblFaktura.setMinimumSize(new java.awt.Dimension(1000, 480));
        tblFaktura.setPreferredSize(new java.awt.Dimension(1000, 480));
        tblFaktura.getTableHeader().setResizingAllowed(false);
        tblFaktura.getTableHeader().setReorderingAllowed(false);
        pnlTabelaKnjigovodstvo.setViewportView(tblFaktura);
        if (tblFaktura.getColumnModel().getColumnCount() > 0)
        {
            tblFaktura.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblFaktura.getColumnModel().getColumn(0).setMaxWidth(40);
            tblFaktura.getColumnModel().getColumn(1).setResizable(false);
            tblFaktura.getColumnModel().getColumn(2).setPreferredWidth(40);
            tblFaktura.getColumnModel().getColumn(2).setMaxWidth(40);
            tblFaktura.getColumnModel().getColumn(3).setPreferredWidth(40);
            tblFaktura.getColumnModel().getColumn(3).setMaxWidth(40);
            tblFaktura.getColumnModel().getColumn(4).setPreferredWidth(40);
            tblFaktura.getColumnModel().getColumn(4).setMaxWidth(40);
        }

        btnRacun.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRacun.setText("Račun");
        btnRacun.setEnabled(false);
        btnRacun.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRacunActionPerformed(evt);
            }
        });

        btnPoIDu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPoIDu.setText("Po ID-u");
        btnPoIDu.setToolTipText("");
        btnPoIDu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPoIDuActionPerformed(evt);
            }
        });

        btnPoDatumu.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPoDatumu.setText("Po datumu");
        btnPoDatumu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPoDatumuActionPerformed(evt);
            }
        });

        txtBezPDV.setEditable(false);
        txtBezPDV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtPDV.setEditable(false);
        txtPDV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

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
        btnNefakturisano.setText("Nefakturisani nalozi");
        btnNefakturisano.setToolTipText("");
        btnNefakturisano.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnNefakturisanoActionPerformed(evt);
            }
        });

        btnFakturisano.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnFakturisano.setText("Fakturisani nalozi");
        btnFakturisano.setToolTipText("");
        btnFakturisano.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnFakturisanoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlOkvirKnjigovodstvoLayout = new javax.swing.GroupLayout(pnlOkvirKnjigovodstvo);
        pnlOkvirKnjigovodstvo.setLayout(pnlOkvirKnjigovodstvoLayout);
        pnlOkvirKnjigovodstvoLayout.setHorizontalGroup(
            pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                .addComponent(lblRadniNalozi)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                .addComponent(pnlNaloziKnjigovodstvo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                                .addComponent(btnPoIDu, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                                .addComponent(btnPoDatumu, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(dtmDatum, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                        .addGap(62, 62, 62)
                                        .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(btnNefakturisano)
                                            .addComponent(btnFakturisano, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(8, 8, 8))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlTabelaKnjigovodstvo))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 244, Short.MAX_VALUE)
                        .addComponent(jLabel161)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUkupno, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel162)))
                .addContainerGap())
            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                .addGap(277, 277, 277)
                .addComponent(btnPredracun, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRacun, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlOkvirKnjigovodstvoLayout.setVerticalGroup(
            pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRadniNalozi)
                .addGap(18, 18, 18)
                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                        .addComponent(btnNefakturisano, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnFakturisano, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnPoIDu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPoDatumu, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(dtmDatum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(pnlNaloziKnjigovodstvo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPredracun, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(btnRacun, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTabelaKnjigovodstvo, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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

        txtKnjigovodstvo.setBackground(new java.awt.Color(255, 255, 255));
        txtKnjigovodstvo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtKnjigovodstvo.setForeground(new java.awt.Color(229, 229, 229));
        txtKnjigovodstvo.setText(">Knjigovodstvo");

        txtSlikaKnjigovodstvo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Book Stack_20px.png"))); // NOI18N

        javax.swing.GroupLayout pnlOsnovniKnjigovodstvoLayout = new javax.swing.GroupLayout(pnlOsnovniKnjigovodstvo);
        pnlOsnovniKnjigovodstvo.setLayout(pnlOsnovniKnjigovodstvoLayout);
        pnlOsnovniKnjigovodstvoLayout.setHorizontalGroup(
            pnlOsnovniKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOsnovniKnjigovodstvoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOsnovniKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlOkvirKnjigovodstvo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlOsnovniKnjigovodstvoLayout.createSequentialGroup()
                        .addComponent(txtSlikaKnjigovodstvo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKnjigovodstvo)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlOsnovniKnjigovodstvoLayout.setVerticalGroup(
            pnlOsnovniKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOsnovniKnjigovodstvoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOsnovniKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSlikaKnjigovodstvo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKnjigovodstvo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlOkvirKnjigovodstvo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2141, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlKnjigovodstvoLayout = new javax.swing.GroupLayout(pnlKnjigovodstvo);
        pnlKnjigovodstvo.setLayout(pnlKnjigovodstvoLayout);
        pnlKnjigovodstvoLayout.setHorizontalGroup(
            pnlKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKnjigovodstvoLayout.createSequentialGroup()
                .addComponent(pnlOsnovniKnjigovodstvo, javax.swing.GroupLayout.PREFERRED_SIZE, 1065, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2101, Short.MAX_VALUE))
        );
        pnlKnjigovodstvoLayout.setVerticalGroup(
            pnlKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKnjigovodstvoLayout.createSequentialGroup()
                .addComponent(pnlOsnovniKnjigovodstvo, javax.swing.GroupLayout.DEFAULT_SIZE, 2863, Short.MAX_VALUE)
                .addGap(660, 660, 660))
        );

        pnlParent.add(pnlKnjigovodstvo, "card7");

        pnlZaposleni.setBackground(new java.awt.Color(255, 255, 255));

        jPanel27.setBackground(new java.awt.Color(102, 153, 255));
        jPanel27.setPreferredSize(new java.awt.Dimension(1000, 800));

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
            new Object [][]
            {
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
            new String []
            {
                "Ime", "Ime oca", "Prezime", "Telefon"
            }
        ));
        jTableZaposleni.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                jTableZaposleniMouseReleased(evt);
            }
        });
        jScrollPane12.setViewportView(jTableZaposleni);
        if (jTableZaposleni.getColumnModel().getColumnCount() > 0)
        {
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
        buttonTraziRadneNalogeRadnika.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
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
        buttonDodajZaposlenog.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
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
        buttonPrikazSvihBivsihRadnika.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
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
            new Object [][]
            {
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
            new String []
            {
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
                .addContainerGap(2066, Short.MAX_VALUE))
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

        javax.swing.GroupLayout pnlZaposleniLayout = new javax.swing.GroupLayout(pnlZaposleni);
        pnlZaposleni.setLayout(pnlZaposleniLayout);
        pnlZaposleniLayout.setHorizontalGroup(
            pnlZaposleniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZaposleniLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, 3156, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlZaposleniLayout.setVerticalGroup(
            pnlZaposleniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZaposleniLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, 801, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2699, Short.MAX_VALUE))
        );

        pnlParent.add(pnlZaposleni, "card3");

        pnlStatistika.setBackground(new java.awt.Color(102, 153, 255));

        pnlHeaderStatistika.setBackground(new java.awt.Color(102, 153, 255));
        pnlHeaderStatistika.setPreferredSize(new java.awt.Dimension(1500, 271));

        pnlKontrolnaTablaStatistika.setBackground(new java.awt.Color(102, 153, 255));
        pnlKontrolnaTablaStatistika.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        pnlKontrolnaTablaStatistika.setMaximumSize(new java.awt.Dimension(1000, 32767));
        pnlKontrolnaTablaStatistika.setPreferredSize(new java.awt.Dimension(500, 209));

        pnlZaradaUkupno.setBackground(new java.awt.Color(102, 153, 255));
        pnlZaradaUkupno.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ukupna zarada", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Dnevna:");

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Mjesečna:");

        jLabel65.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 255, 255));
        jLabel65.setText("Godišnja:");

        lblIntervalZarada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblIntervalZarada.setForeground(new java.awt.Color(255, 255, 255));
        lblIntervalZarada.setText(" KM");

        lblGodisnjaZarada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblGodisnjaZarada.setForeground(new java.awt.Color(255, 255, 255));
        lblGodisnjaZarada.setText(" KM");

        lblMjesecnaZarada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMjesecnaZarada.setForeground(new java.awt.Color(255, 255, 255));
        lblMjesecnaZarada.setText(" KM");

        jLabel88.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(255, 255, 255));
        jLabel88.setText("Interval:");

        lblDnevnaZarada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDnevnaZarada.setForeground(new java.awt.Color(255, 255, 255));
        lblDnevnaZarada.setText(" KM");

        javax.swing.GroupLayout pnlZaradaUkupnoLayout = new javax.swing.GroupLayout(pnlZaradaUkupno);
        pnlZaradaUkupno.setLayout(pnlZaradaUkupnoLayout);
        pnlZaradaUkupnoLayout.setHorizontalGroup(
            pnlZaradaUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZaradaUkupnoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlZaradaUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57)
                    .addComponent(jLabel65)
                    .addComponent(jLabel58)
                    .addComponent(jLabel88))
                .addGap(33, 33, 33)
                .addGroup(pnlZaradaUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIntervalZarada)
                    .addComponent(lblMjesecnaZarada)
                    .addComponent(lblGodisnjaZarada)
                    .addComponent(lblDnevnaZarada))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlZaradaUkupnoLayout.setVerticalGroup(
            pnlZaradaUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZaradaUkupnoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlZaradaUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(lblDnevnaZarada))
                .addGap(18, 18, 18)
                .addGroup(pnlZaradaUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(lblMjesecnaZarada))
                .addGap(18, 18, 18)
                .addGroup(pnlZaradaUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(lblGodisnjaZarada))
                .addGap(18, 18, 18)
                .addGroup(pnlZaradaUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88)
                    .addComponent(lblIntervalZarada))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBrojPopravki.setBackground(new java.awt.Color(102, 153, 255));
        pnlBrojPopravki.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Broj popravki", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel69.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Danas:");

        jLabel70.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(255, 255, 255));
        jLabel70.setText("Mjesec:");

        jLabel71.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setText("Godina:");

        lblPopravkeDanas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPopravkeDanas.setForeground(new java.awt.Color(255, 255, 255));
        lblPopravkeDanas.setText("8");

        lblPopravkeGodina.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPopravkeGodina.setForeground(new java.awt.Color(255, 255, 255));
        lblPopravkeGodina.setText("2542");

        lblPopravkeMjesec.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPopravkeMjesec.setForeground(new java.awt.Color(255, 255, 255));
        lblPopravkeMjesec.setText("220");

        jLabel89.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(255, 255, 255));
        jLabel89.setText("Interval:");

        lblPopravkeInterval.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPopravkeInterval.setForeground(new java.awt.Color(255, 255, 255));
        lblPopravkeInterval.setText(" ");

        javax.swing.GroupLayout pnlBrojPopravkiLayout = new javax.swing.GroupLayout(pnlBrojPopravki);
        pnlBrojPopravki.setLayout(pnlBrojPopravkiLayout);
        pnlBrojPopravkiLayout.setHorizontalGroup(
            pnlBrojPopravkiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBrojPopravkiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBrojPopravkiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel71)
                    .addComponent(jLabel70)
                    .addComponent(jLabel89)
                    .addComponent(jLabel69))
                .addGap(30, 30, 30)
                .addGroup(pnlBrojPopravkiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPopravkeDanas)
                    .addComponent(lblPopravkeMjesec)
                    .addComponent(lblPopravkeGodina)
                    .addComponent(lblPopravkeInterval, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 20, Short.MAX_VALUE))
        );
        pnlBrojPopravkiLayout.setVerticalGroup(
            pnlBrojPopravkiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBrojPopravkiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBrojPopravkiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(lblPopravkeDanas))
                .addGap(18, 18, 18)
                .addGroup(pnlBrojPopravkiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(lblPopravkeMjesec))
                .addGap(18, 18, 18)
                .addGroup(pnlBrojPopravkiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71)
                    .addComponent(lblPopravkeGodina))
                .addGap(18, 18, 18)
                .addGroup(pnlBrojPopravkiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(lblPopravkeInterval))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlFakture.setBackground(new java.awt.Color(102, 153, 255));
        pnlFakture.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fakture", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel80.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("Plaćenih:");

        jLabel81.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("Neplaćenih:");

        jLabel82.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(255, 255, 255));
        jLabel82.setText("Ukupno:");

        lblBrojPlacenihFaktura.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBrojPlacenihFaktura.setForeground(new java.awt.Color(255, 255, 255));
        lblBrojPlacenihFaktura.setText("10");

        lblBrojFaktura.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBrojFaktura.setForeground(new java.awt.Color(255, 255, 255));
        lblBrojFaktura.setText("15");

        lblBrojNeplacenihFaktura.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBrojNeplacenihFaktura.setForeground(new java.awt.Color(255, 255, 255));
        lblBrojNeplacenihFaktura.setText("5");

        javax.swing.GroupLayout pnlFaktureLayout = new javax.swing.GroupLayout(pnlFakture);
        pnlFakture.setLayout(pnlFaktureLayout);
        pnlFaktureLayout.setHorizontalGroup(
            pnlFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFaktureLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel80)
                    .addComponent(jLabel82)
                    .addComponent(jLabel81))
                .addGap(33, 33, 33)
                .addGroup(pnlFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblBrojNeplacenihFaktura)
                    .addComponent(lblBrojFaktura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBrojPlacenihFaktura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlFaktureLayout.setVerticalGroup(
            pnlFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFaktureLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(lblBrojPlacenihFaktura))
                .addGap(18, 18, 18)
                .addGroup(pnlFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(lblBrojNeplacenihFaktura))
                .addGap(18, 18, 18)
                .addGroup(pnlFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(lblBrojFaktura))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlOdabirIntervala.setBackground(new java.awt.Color(102, 153, 255));
        pnlOdabirIntervala.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Odabir vremenskog intervala:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Do:");

        btnPregled.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPregled.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnPregled.setText("Pregled");
        btnPregled.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPregledActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Od:");

        javax.swing.GroupLayout pnlOdabirIntervalaLayout = new javax.swing.GroupLayout(pnlOdabirIntervala);
        pnlOdabirIntervala.setLayout(pnlOdabirIntervalaLayout);
        pnlOdabirIntervalaLayout.setHorizontalGroup(
            pnlOdabirIntervalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOdabirIntervalaLayout.createSequentialGroup()
                .addGroup(pnlOdabirIntervalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlOdabirIntervalaLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(18, 18, 18)
                        .addComponent(dcDatumDo, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlOdabirIntervalaLayout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(18, 18, 18)
                        .addComponent(dcDatumOd, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOdabirIntervalaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPregled)
                .addGap(52, 52, 52))
        );
        pnlOdabirIntervalaLayout.setVerticalGroup(
            pnlOdabirIntervalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOdabirIntervalaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlOdabirIntervalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOdabirIntervalaLayout.createSequentialGroup()
                        .addGroup(pnlOdabirIntervalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dcDatumOd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(dcDatumDo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPregled)
                .addGap(106, 106, 106))
        );

        pnlZaradaDijelovi.setBackground(new java.awt.Color(102, 153, 255));
        pnlZaradaDijelovi.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Zarada od prodaje dijelova", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel91.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(255, 255, 255));
        jLabel91.setText("Dnevna:");

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(255, 255, 255));
        jLabel93.setText("Mjesečna:");

        jLabel94.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(255, 255, 255));
        jLabel94.setText("Godišnja:");

        lblDnevnaZaradaDijelovi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDnevnaZaradaDijelovi.setForeground(new java.awt.Color(255, 255, 255));
        lblDnevnaZaradaDijelovi.setText(" KM");

        lblGodisnjaZaradaDijelovi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblGodisnjaZaradaDijelovi.setForeground(new java.awt.Color(255, 255, 255));
        lblGodisnjaZaradaDijelovi.setText(" KM");

        lblMjesecnaZaradaDijelovi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMjesecnaZaradaDijelovi.setForeground(new java.awt.Color(255, 255, 255));
        lblMjesecnaZaradaDijelovi.setText(" KM");

        jLabel101.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(255, 255, 255));
        jLabel101.setText("Interval:");

        lblIntervalZaradaDijelovi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblIntervalZaradaDijelovi.setForeground(new java.awt.Color(255, 255, 255));
        lblIntervalZaradaDijelovi.setText(" KM");

        javax.swing.GroupLayout pnlZaradaDijeloviLayout = new javax.swing.GroupLayout(pnlZaradaDijelovi);
        pnlZaradaDijelovi.setLayout(pnlZaradaDijeloviLayout);
        pnlZaradaDijeloviLayout.setHorizontalGroup(
            pnlZaradaDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZaradaDijeloviLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlZaradaDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel91)
                    .addComponent(jLabel94)
                    .addComponent(jLabel93)
                    .addComponent(jLabel101))
                .addGap(33, 33, 33)
                .addGroup(pnlZaradaDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIntervalZaradaDijelovi)
                    .addComponent(lblMjesecnaZaradaDijelovi)
                    .addComponent(lblGodisnjaZaradaDijelovi)
                    .addComponent(lblDnevnaZaradaDijelovi))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlZaradaDijeloviLayout.setVerticalGroup(
            pnlZaradaDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZaradaDijeloviLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlZaradaDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel91)
                    .addComponent(lblDnevnaZaradaDijelovi))
                .addGap(18, 18, 18)
                .addGroup(pnlZaradaDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel93)
                    .addComponent(lblMjesecnaZaradaDijelovi))
                .addGap(18, 18, 18)
                .addGroup(pnlZaradaDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(lblGodisnjaZaradaDijelovi))
                .addGap(18, 18, 18)
                .addGroup(pnlZaradaDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(lblIntervalZaradaDijelovi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlKontrolnaTablaStatistikaLayout = new javax.swing.GroupLayout(pnlKontrolnaTablaStatistika);
        pnlKontrolnaTablaStatistika.setLayout(pnlKontrolnaTablaStatistikaLayout);
        pnlKontrolnaTablaStatistikaLayout.setHorizontalGroup(
            pnlKontrolnaTablaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKontrolnaTablaStatistikaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlOdabirIntervala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlZaradaUkupno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlZaradaDijelovi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBrojPopravki, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFakture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(140, Short.MAX_VALUE))
        );
        pnlKontrolnaTablaStatistikaLayout.setVerticalGroup(
            pnlKontrolnaTablaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKontrolnaTablaStatistikaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlKontrolnaTablaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlFakture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBrojPopravki, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlZaradaUkupno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlOdabirIntervala, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlZaradaDijelovi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel86.setBackground(new java.awt.Color(255, 255, 255));
        jLabel86.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(229, 229, 229));
        jLabel86.setText("> Statistika");

        jLabel87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Combo Chart_20px.png"))); // NOI18N

        javax.swing.GroupLayout pnlHeaderStatistikaLayout = new javax.swing.GroupLayout(pnlHeaderStatistika);
        pnlHeaderStatistika.setLayout(pnlHeaderStatistikaLayout);
        pnlHeaderStatistikaLayout.setHorizontalGroup(
            pnlHeaderStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderStatistikaLayout.createSequentialGroup()
                .addGroup(pnlHeaderStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHeaderStatistikaLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel86))
                    .addGroup(pnlHeaderStatistikaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlKontrolnaTablaStatistika, javax.swing.GroupLayout.PREFERRED_SIZE, 1046, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(144, Short.MAX_VALUE))
        );
        pnlHeaderStatistikaLayout.setVerticalGroup(
            pnlHeaderStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderStatistikaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlHeaderStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel86)
                    .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pnlKontrolnaTablaStatistika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlGrafikPrihodiUkupno.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlGrafikPrihodiUkupnoLayout = new javax.swing.GroupLayout(pnlGrafikPrihodiUkupno);
        pnlGrafikPrihodiUkupno.setLayout(pnlGrafikPrihodiUkupnoLayout);
        pnlGrafikPrihodiUkupnoLayout.setHorizontalGroup(
            pnlGrafikPrihodiUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 724, Short.MAX_VALUE)
        );
        pnlGrafikPrihodiUkupnoLayout.setVerticalGroup(
            pnlGrafikPrihodiUkupnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        tabpnlGrafici.addTab("Ukupni prihodi", pnlGrafikPrihodiUkupno);

        pnlGrafikPrihodiDijelovi.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlGrafikPrihodiDijeloviLayout = new javax.swing.GroupLayout(pnlGrafikPrihodiDijelovi);
        pnlGrafikPrihodiDijelovi.setLayout(pnlGrafikPrihodiDijeloviLayout);
        pnlGrafikPrihodiDijeloviLayout.setHorizontalGroup(
            pnlGrafikPrihodiDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 724, Short.MAX_VALUE)
        );
        pnlGrafikPrihodiDijeloviLayout.setVerticalGroup(
            pnlGrafikPrihodiDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        tabpnlGrafici.addTab("Prihodi od dijelova", pnlGrafikPrihodiDijelovi);

        pnlGrafikAuta.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlGrafikAutaLayout = new javax.swing.GroupLayout(pnlGrafikAuta);
        pnlGrafikAuta.setLayout(pnlGrafikAutaLayout);
        pnlGrafikAutaLayout.setHorizontalGroup(
            pnlGrafikAutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 724, Short.MAX_VALUE)
        );
        pnlGrafikAutaLayout.setVerticalGroup(
            pnlGrafikAutaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        tabpnlGrafici.addTab("Auta na stanju", pnlGrafikAuta);

        pnlGrafikFakture.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlGrafikFaktureLayout = new javax.swing.GroupLayout(pnlGrafikFakture);
        pnlGrafikFakture.setLayout(pnlGrafikFaktureLayout);
        pnlGrafikFaktureLayout.setHorizontalGroup(
            pnlGrafikFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 724, Short.MAX_VALUE)
        );
        pnlGrafikFaktureLayout.setVerticalGroup(
            pnlGrafikFaktureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        tabpnlGrafici.addTab("Fakture", pnlGrafikFakture);

        pnlGrafikPopravke.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlGrafikPopravkeLayout = new javax.swing.GroupLayout(pnlGrafikPopravke);
        pnlGrafikPopravke.setLayout(pnlGrafikPopravkeLayout);
        pnlGrafikPopravkeLayout.setHorizontalGroup(
            pnlGrafikPopravkeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 724, Short.MAX_VALUE)
        );
        pnlGrafikPopravkeLayout.setVerticalGroup(
            pnlGrafikPopravkeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        tabpnlGrafici.addTab("Broj popravljenih auta", pnlGrafikPopravke);

        pnlOdabirMjesecaStatistika.setBackground(new java.awt.Color(102, 153, 255));
        pnlOdabirMjesecaStatistika.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Odaberite mjesec:");

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Odaberite godinu:");

        cbMjesec.setMaximumRowCount(13);
        cbMjesec.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar" }));

        cbGodina.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "2018", "2017" }));

        btnPregledGrafik.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPregledGrafik.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnPregledGrafik.setText("Pregled");
        btnPregledGrafik.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPregledGrafikActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlOdabirMjesecaStatistikaLayout = new javax.swing.GroupLayout(pnlOdabirMjesecaStatistika);
        pnlOdabirMjesecaStatistika.setLayout(pnlOdabirMjesecaStatistikaLayout);
        pnlOdabirMjesecaStatistikaLayout.setHorizontalGroup(
            pnlOdabirMjesecaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOdabirMjesecaStatistikaLayout.createSequentialGroup()
                .addGroup(pnlOdabirMjesecaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOdabirMjesecaStatistikaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlOdabirMjesecaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel33))
                        .addGap(40, 40, 40)
                        .addGroup(pnlOdabirMjesecaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbGodina, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbMjesec, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlOdabirMjesecaStatistikaLayout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(btnPregledGrafik)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlOdabirMjesecaStatistikaLayout.setVerticalGroup(
            pnlOdabirMjesecaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOdabirMjesecaStatistikaLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(pnlOdabirMjesecaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(cbMjesec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(pnlOdabirMjesecaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(cbGodina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnPregledGrafik)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlStatistikaLayout = new javax.swing.GroupLayout(pnlStatistika);
        pnlStatistika.setLayout(pnlStatistikaLayout);
        pnlStatistikaLayout.setHorizontalGroup(
            pnlStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatistikaLayout.createSequentialGroup()
                .addGroup(pnlStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlHeaderStatistika, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlStatistikaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabpnlGrafici, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlOdabirMjesecaStatistika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 1966, Short.MAX_VALUE))
        );
        pnlStatistikaLayout.setVerticalGroup(
            pnlStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatistikaLayout.createSequentialGroup()
                .addComponent(pnlHeaderStatistika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlStatistikaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tabpnlGrafici, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlStatistikaLayout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(pnlOdabirMjesecaStatistika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(2830, Short.MAX_VALUE))
        );

        tabpnlGrafici.getAccessibleContext().setAccessibleName("Ukupni prihodi");

        pnlParent.add(pnlStatistika, "card5");

        jScrollPane1.setViewportView(pnlParent);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlMeni, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 2621, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMeni, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pnlMeniRadniNaloziMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniRadniNaloziMouseClicked
        dodajPopupMeniPretragaRadniNaloga();
        menuItemClick(pnlMeniRadniNalozi, 1, pnlRadniNalozi);


    }//GEN-LAST:event_pnlMeniRadniNaloziMouseClicked

    private void pnlMeniRadniNaloziMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniRadniNaloziMouseEntered
        // TODO add your handling code here:
        setColor(pnlMeniRadniNalozi);
    }//GEN-LAST:event_pnlMeniRadniNaloziMouseEntered

    private void pnlMeniRadniNaloziMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniRadniNaloziMouseExited
        // TODO add your handling code here:
        if (!menu[1]) {
            resetColor(pnlMeniRadniNalozi);
        }
    }//GEN-LAST:event_pnlMeniRadniNaloziMouseExited

    private void pnlMeniRadniNaloziMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniRadniNaloziMousePressed

    }//GEN-LAST:event_pnlMeniRadniNaloziMousePressed

    private void pnlMeniVozilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniVozilaMouseClicked
        menuItemClick(pnlMeniVozila, 2, pnlVozila);
        loadAutosuggester();
        tableVozila.getTableHeader().setReorderingAllowed(false);
        tableVozila.setDefaultEditor(Object.class, null);

        tableVozila.setAutoCreateRowSorter(true);
        tableVozila.setAutoCreateRowSorter(true);
       
        if(flagVoziloVlasnik)
            ucitajPopupZaVozila();
        else
            ucitajPopupZaVlasnike();
    }//GEN-LAST:event_pnlMeniVozilaMouseClicked

    private void pnlMeniVozilaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniVozilaMouseEntered
        // TODO add your handling code here:
        setColor(pnlMeniVozila);
    }//GEN-LAST:event_pnlMeniVozilaMouseEntered

    private void pnlMeniVozilaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniVozilaMouseExited
        if (!menu[2]) {
            resetColor(pnlMeniVozila);
        }
    }//GEN-LAST:event_pnlMeniVozilaMouseExited

    private void pnlMeniKnjigovodstvoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniKnjigovodstvoMouseClicked
        menuItemClick(pnlMeniKnjigovodstvo, 3, pnlKnjigovodstvo);
        uslugeKnjigovodstva();
        lblRadniNalozi.setText("Radni nalozi:");

    }//GEN-LAST:event_pnlMeniKnjigovodstvoMouseClicked

    private void pnlMeniKnjigovodstvoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniKnjigovodstvoMouseEntered
        setColor(pnlMeniKnjigovodstvo);
    }//GEN-LAST:event_pnlMeniKnjigovodstvoMouseEntered

    private void pnlMeniKnjigovodstvoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniKnjigovodstvoMouseExited
        if (!menu[3]) {
            resetColor(pnlMeniKnjigovodstvo);
        }
    }//GEN-LAST:event_pnlMeniKnjigovodstvoMouseExited

    private void pnlMeniStatistikaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniStatistikaMouseClicked

        menuItemClick(pnlMeniStatistika, 6, pnlStatistika);
        statistikaLogika.loadGraph(this);
        statistikaLogika.loadStatistics(this);


    }//GEN-LAST:event_pnlMeniStatistikaMouseClicked

    private void pnlMeniStatistikaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniStatistikaMouseEntered
        // TODO add your handling code here:
        setColor(pnlMeniStatistika);
    }//GEN-LAST:event_pnlMeniStatistikaMouseEntered

    private void pnlMeniStatistikaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniStatistikaMouseExited
        if (!menu[6]) {
            resetColor(pnlMeniStatistika);
        }
    }//GEN-LAST:event_pnlMeniStatistikaMouseExited

    private void pnlMeniStatistikaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniStatistikaMousePressed
        setColor(pnlMeniStatistika);
    }//GEN-LAST:event_pnlMeniStatistikaMousePressed

    private void pnlMeniZaposleniMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniZaposleniMouseClicked

        menuItemClick(pnlMeniZaposleni, 4, pnlZaposleni);


    }//GEN-LAST:event_pnlMeniZaposleniMouseClicked

    private void pnlMeniZaposleniMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniZaposleniMouseEntered
        setColor(pnlMeniZaposleni);
    }//GEN-LAST:event_pnlMeniZaposleniMouseEntered

    private void pnlMeniZaposleniMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniZaposleniMouseExited
        if (!menu[4]) {
            resetColor(pnlMeniZaposleni);
        }
    }//GEN-LAST:event_pnlMeniZaposleniMouseExited

    private void pnlMeniZaposleniMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniZaposleniMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlMeniZaposleniMousePressed

    private void pnlMeniDijeloviMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniDijeloviMouseClicked

        menuItemClick(pnlMeniDijelovi, 5, pnlDijelovi);
        loadDijeloviForm();

    }//GEN-LAST:event_pnlMeniDijeloviMouseClicked

    private void pnlMeniDijeloviMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniDijeloviMouseEntered
        setColor(pnlMeniDijelovi);
    }//GEN-LAST:event_pnlMeniDijeloviMouseEntered

    private void pnlMeniDijeloviMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniDijeloviMouseExited
        if (!menu[5]) {
            resetColor(pnlMeniDijelovi);
        }
    }//GEN-LAST:event_pnlMeniDijeloviMouseExited

    private void pnlMeniPocetnaStranaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniPocetnaStranaMouseClicked

        menuItemClick(pnlMeniPocetnaStrana, 0, pnlPocetna);
        inicijalizacijaTabeleNeplacenihFaktura();


    }//GEN-LAST:event_pnlMeniPocetnaStranaMouseClicked

    private void pnlMeniPocetnaStranaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniPocetnaStranaMouseEntered
        setColor(pnlMeniPocetnaStrana);
    }//GEN-LAST:event_pnlMeniPocetnaStranaMouseEntered

    private void pnlMeniPocetnaStranaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniPocetnaStranaMouseExited
        if (!menu[0]) {
            resetColor(pnlMeniPocetnaStrana);
        }
    }//GEN-LAST:event_pnlMeniPocetnaStranaMouseExited

    private void pnlMeniPocetnaStranaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniPocetnaStranaMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlMeniPocetnaStranaMousePressed

    private void pnlMeniZakazivanjaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniZakazivanjaMouseClicked
        menuItemClick(pnlMeniZakazivanja, 7, pnlZakazivanja);
        uslugeZakazivanja();
    }//GEN-LAST:event_pnlMeniZakazivanjaMouseClicked

    private void pnlMeniZakazivanjaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniZakazivanjaMouseEntered
        setColor(pnlMeniZakazivanja);
    }//GEN-LAST:event_pnlMeniZakazivanjaMouseEntered

    private void pnlMeniZakazivanjaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniZakazivanjaMouseExited
        if (!menu[7])
        resetColor(pnlMeniZakazivanja);    }//GEN-LAST:event_pnlMeniZakazivanjaMouseExited

    private void pnlMeniZakazivanjaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniZakazivanjaMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlMeniZakazivanjaMousePressed

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
        new ZaposleniLogika("statistika", new java.sql.Date(dateChooserDatumOdZaposlenog.getDate().getTime()), new java.sql.Date(dateChooserDatumDoZaposlenog.getDate().getTime()), labelBrojRadnihNaloga, labelOstvareniProfitRadnika).run();
    }//GEN-LAST:event_buttonTraziRadneNalogeRadnikaActionPerformed

    private void rbPravnoTraziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPravnoTraziActionPerformed
        txtImeTrazi.setEditable(false);
        txtPrezimeTrazi.setEditable(false);
        txtImeTrazi.setText("");
        txtPrezimeTrazi.setText("");
        txtImeTrazi.setBackground(Color.gray);
        txtPrezimeTrazi.setBackground(Color.gray);
        txtNazivTrazi.setBackground(Color.white);
        txtNazivTrazi.setEditable(true);
    }//GEN-LAST:event_rbPravnoTraziActionPerformed

    private void rbPrivatnoTraziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPrivatnoTraziActionPerformed
        txtNazivTrazi.setText("");
        txtNazivTrazi.setEditable(false);
        txtNazivTrazi.setBackground(Color.gray);
        txtImeTrazi.setBackground(Color.white);
        txtPrezimeTrazi.setBackground(Color.white);
        txtImeTrazi.setEditable(true);
        txtPrezimeTrazi.setEditable(true);
        rbPrivatnoTrazi.setSelected(true);
        rbPravnoTrazi.setSelected(false);
        rbPrivatnoTrazi.setSelected(false);
    }//GEN-LAST:event_rbPrivatnoTraziActionPerformed

    public void prikaziKupceUTabeli(ArrayList<KupacDTO> kupci) {
        voziloKupacMeniLogika.prikaziKupceUTabeli(kupci, this);
    }

    private void btnTraziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraziActionPerformed
        voziloKupacMeniLogika.traziKupce(this);
        flagVoziloVlasnik = false;
    }//GEN-LAST:event_btnTraziActionPerformed

    private void btnPrikaziSveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziSveActionPerformed
        izbrisiPopupZaVozila();
        izbrisiPopupZaVlasnike();
        ucitajPopupZaVlasnike();
        flagVoziloVlasnik = false;
        ArrayList<KupacDTO> kupci = DAOFactory.getDAOFactory().getKupacDAO().sviKupci();
        prikaziKupceSveUTabeli(kupci);
    }//GEN-LAST:event_btnPrikaziSveActionPerformed

    private void btnPonistiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPonistiActionPerformed
        txtPrezimeTrazi.setText("");
        txtImeTrazi.setText("");
        txtNazivTrazi.setText("");
        rbPrivatnoTrazi.setSelected(true);
        rbPravnoTrazi.setSelected(true);
        txtNazivTrazi.setEditable(false);
        txtImeTrazi.setEditable(true);
        txtPrezimeTrazi.setEditable(true);
        txtNazivTrazi.setBackground(Color.gray);
        txtImeTrazi.setBackground(Color.white);
        txtPrezimeTrazi.setBackground(Color.white);
        rbPravnoTrazi.setSelected(false);
        rbPrivatnoTrazi.setSelected(true);
    }//GEN-LAST:event_btnPonistiActionPerformed

    private void btnDodajModel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajModel2ActionPerformed
        new DodajModel(this, true).setVisible(true);
    }//GEN-LAST:event_btnDodajModel2ActionPerformed

    private void btnDodajVlasnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajVlasnikaActionPerformed
        new DodajVlasnikaDialog(this, true).setVisible(true);
    }//GEN-LAST:event_btnDodajVlasnikaActionPerformed

    private void btnDodajVoziloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajVoziloActionPerformed
        new DodajVoziloDialog(this, true).setVisible(true);
    }//GEN-LAST:event_btnDodajVoziloActionPerformed

    private void btnIzmijeniIzbrisiModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIzmijeniIzbrisiModelActionPerformed
        new IzmijeniIzbrisiModelDialog(new JFrame(), true).setVisible(true);
    }//GEN-LAST:event_btnIzmijeniIzbrisiModelActionPerformed

    private void btnPregledGrafikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPregledGrafikActionPerformed
        statistikaLogika.loadGraph(this);
    }//GEN-LAST:event_btnPregledGrafikActionPerformed

    private void cbSviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSviActionPerformed
        if (cbSvi.isSelected()) {
            bGTraziVozilo.clearSelection();
            txtImeVozilo.setEditable(false);
            txtImeVozilo.setBackground(Color.gray);
            txtImeVozilo.setText("");
            txtPrezimeVozilo.setEditable(false);
            txtPrezimeVozilo.setBackground(Color.gray);
            txtPrezimeVozilo.setText("");
            txtNazivVozilo.setEditable(false);
            txtNazivVozilo.setBackground(Color.gray);
            txtNazivVozilo.setText("");
        } else {
            rbPrivatnoLiceVozilo.setSelected(true);
            rbPravnoLiceVozilo.setSelected(false);
            txtImeVozilo.setEditable(true);
            txtImeVozilo.setText("");
            txtImeVozilo.setBackground(Color.white);
            txtPrezimeVozilo.setEditable(true);
            txtPrezimeVozilo.setText("");
            txtPrezimeVozilo.setBackground(Color.white);
        }
    }//GEN-LAST:event_cbSviActionPerformed

    private void btnPrikaziSvaVozilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziSvaVozilaActionPerformed
        izbrisiPopupZaVlasnike();
        izbrisiPopupZaVozila();
        ucitajPopupZaVozila();
        flagVoziloVlasnik = true;
        voziloKupacMeniLogika.prikaziSvaVozila(this);
    }//GEN-LAST:event_btnPrikaziSvaVozilaActionPerformed

    private void rbPravnoLiceVoziloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPravnoLiceVoziloActionPerformed
        txtNazivVozilo.setEditable(true);
        txtImeVozilo.setEditable(false);
        txtPrezimeVozilo.setEditable(false);
        txtImeVozilo.setText("");
        txtPrezimeVozilo.setText("");
        txtImeVozilo.setBackground(Color.gray);
        txtPrezimeVozilo.setBackground(Color.gray);
        txtNazivVozilo.setBackground(Color.white);
        rbPrivatnoLiceVozilo.setSelected(false);
        cbSvi.setSelected(false);
    }//GEN-LAST:event_rbPravnoLiceVoziloActionPerformed

    private void rbPrivatnoLiceVoziloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPrivatnoLiceVoziloActionPerformed
        cbSvi.setSelected(false);
        txtNazivVozilo.setEditable(false);
        txtNazivVozilo.setText("");
        txtImeVozilo.setEditable(true);
        txtPrezimeVozilo.setEditable(true);
        txtNazivVozilo.setBackground(Color.gray);
        rbPravnoLiceVozilo.setSelected(false);
        txtImeVozilo.setBackground(Color.white);
        txtPrezimeVozilo.setBackground(Color.white);
    }//GEN-LAST:event_rbPrivatnoLiceVoziloActionPerformed

    private void btnPonistiSveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPonistiSveActionPerformed
        txtRegistracijaTrazi.setText("");
        txtGodisteTrazi.setText("");
        txtMarkaTrazi.setText("");
        txtModelTrazi.setText("");
        txtPrezimeVozilo.setText("");
        txtImeVozilo.setText("");
        txtNazivVozilo.setText("");
        rbPrivatnoLiceVozilo.setSelected(true);
        rbPravnoLiceVozilo.setSelected(false);
        txtNazivVozilo.setEditable(false);
        txtNazivVozilo.setBackground(Color.gray);
        txtImeVozilo.setBackground(Color.white);
        txtPrezimeVozilo.setBackground(Color.white);
        txtImeVozilo.setEditable(true);
        txtPrezimeVozilo.setEditable(true);
    }//GEN-LAST:event_btnPonistiSveActionPerformed

    private void btnPronadjiVoziloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPronadjiVoziloActionPerformed
        izbrisiPopupZaVlasnike();
        izbrisiPopupZaVozila();
        ucitajPopupZaVozila();
        flagVoziloVlasnik = true;
        voziloKupacMeniLogika.pronadjiVozilo(this);
    }//GEN-LAST:event_btnPronadjiVoziloActionPerformed

    private void txtModelTraziFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtModelTraziFocusGained
        //ucitajPreporukeModel();
    }//GEN-LAST:event_txtModelTraziFocusGained

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
        if ("".equals(sifra)) {
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(this, "Unesite šifru", "Upozorenje", JOptionPane.ERROR_MESSAGE);
            flag = true;
            return;
        }
        naziv = jtfNaziv.getText();
        if ("".equals(naziv)) {
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(this, "Unesite naziv", "Upozorenje", JOptionPane.ERROR_MESSAGE);
            flag = true;
            return;
        }
        try {
            cijena = Double.parseDouble(jtfCijena.getText());
        } catch (NumberFormatException e) {
            if (!"".equals(jtfCijena.getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format cijene!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                return;
            } else {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Unesite cijenu!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                return;
            }
        }
        try {
            godiste = Integer.parseInt(jtfGodiste.getText());
        } catch (NumberFormatException e) {
            if (!"".equals(jtfGodiste.getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format godišta!!!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                return;
            }
            godiste = null;
        }
        stanje = cbStanje.isSelected();
        gorivo = (String) jcbGorivo.getSelectedItem();
        marka = (String) jcbMarka.getSelectedItem();
        model = (String) jcbModel.getSelectedItem();
        try {
            kolicina = Integer.parseInt(jtfKolicina.getText());
            if(kolicina < 1) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Količina mora biti pozitivan broj", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            if (!"".equals(jtfKolicina.getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format količine!!!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                return;
            } else {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Unesite količinu!", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                return;
            }
        }
        if(!flag)
            dioLogika.dodajDio(this, sifra, naziv, gorivo, godiste, stanje, cijena, kolicina, true, marka, model);

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
        try {
            id = Integer.parseInt(tfId.getText());
        } catch (NumberFormatException e) {
            if (!"".equals(tfId.getText())) {
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
        
        dioLogika.pretraziDijelove(this, id, sifra, naziv, gorivo, godiste, stanje, cijena, true, marka, model);
        /*ArrayList<DioDTO> dijelovi = new ArrayList<DioDTO>();
        if (!"".equals(tfId.getText())) {
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
            dijelovi = null;
        }

        DefaultTableModel dtm = (DefaultTableModel) jTable.getModel();
        dtm.setRowCount(0);

        int i = 0;
        if (dijelovi != null) {
            while (i < dijelovi.size()) {
                DioDTO d = dijelovi.get(i);
                dtm.addRow(new Object[]{d.getId(), d.getSifra(), d.getNaziv(), d.getMarka(), d.getModel(), d.getGodisteVozila(),
                    d.getVrstaGoriva(), Math.round(d.getTrenutnaCijena() * 100) / 100, d.getKolicina(), d.getNovo() ? "Da" : "Ne"});
                i++;
            }
        }*/
    }//GEN-LAST:event_btnPretraziActionPerformed

    private void jLabel36KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel36KeyPressed

    }//GEN-LAST:event_jLabel36KeyPressed

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked

    }//GEN-LAST:event_jTableMouseClicked

    private void btnSviDijeloviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSviDijeloviActionPerformed
        ArrayList<DioDTO> dijelovi = DAOFactory.getDAOFactory().getDioDAO().getSviDijelovi();

        DefaultTableModel dtm = (DefaultTableModel) jTable.getModel();
        dtm.setRowCount(0);

        int i = 0;
        if (dijelovi != null) {
            while (i < dijelovi.size()) {
                DioDTO d = dijelovi.get(i);
                dtm.addRow(new Object[]{d.getId(), d.getSifra(), d.getNaziv(), d.getMarka(), d.getModel(), d.getGodisteVozila(),
                    d.getVrstaGoriva(), d.getTrenutnaCijena(), d.getKolicina(), d.getNovo() ? "Da" : "Ne"});
                i++;
            }
        }
        // if(evt.getButton() == MouseEvent.BUTTON3 && jTable.getSelectedRow() != -1){
        //IzmijeniDioDialog dialog = new IzmijeniDioDialog((DefaultTableModel)jTable.getModel(), jTable.getSelectedRow());
        //dialog.setVisible(true);
        //}
    }//GEN-LAST:event_btnSviDijeloviActionPerformed

    private void btnProdaniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdaniActionPerformed
        new PregledProdanihDijelovaDialog(this).setVisible(true);
    }//GEN-LAST:event_btnProdaniActionPerformed

    private void btnPronadjiTerminActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPronadjiTerminActionPerformed
    {//GEN-HEADEREND:event_btnPronadjiTerminActionPerformed
        ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        Date datum = null;
        if (dtmDatumPretraga.getDate() != null) {
            dtmDatumPretraga.setCalendar(Calendar.getInstance());
        }
        filtrirajTermine(
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
        ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, tblTermini);
    }//GEN-LAST:event_btnPonistiUnosePretragaActionPerformed

    private void btnDodajTerminActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDodajTerminActionPerformed
    {//GEN-HEADEREND:event_btnDodajTerminActionPerformed
        Date datum = null;
        if (dtmDatumTermina.getDate() != null) {
            dtmDatumTermina.setCalendar(Calendar.getInstance());
        }
        boolean test = dodajTermin(
                datum,
                "" + (txtVrijemeTerminaSati.getValue()),
                "" + (txtVrijemeTerminaMinuti.getValue()),
                txtMarkaTermina.getText(),
                txtModelTermina.getText(),
                txtImeTermina.getText(),
                txtPrezimeTermina.getText(),
                txtBrojTelefonaTermina.getText()
        );
        ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, tblTermini);
    }//GEN-LAST:event_btnDodajTerminActionPerformed

    private void btnPonistiUnoseTerminActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPonistiUnoseTerminActionPerformed
    {//GEN-HEADEREND:event_btnPonistiUnoseTerminActionPerformed
        ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
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
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        nefakturisaniRadniNalozi(lista, tblRadniNalozi);
        lblRadniNalozi.setText("Nefakturisani radni nalozi:");
    }//GEN-LAST:event_btnNefakturisanoActionPerformed

    private void btnFakturisanoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFakturisanoActionPerformed
    {//GEN-HEADEREND:event_btnFakturisanoActionPerformed
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        fakturisaniRadniNalozi(lista, tblRadniNalozi);
        lblRadniNalozi.setText("Fakturisani radni nalozi:");
    }//GEN-LAST:event_btnFakturisanoActionPerformed

    private void btnPoIDuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPoIDuActionPerformed
    {//GEN-HEADEREND:event_btnPoIDuActionPerformed
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        poIDuRadniNalozi(lista, tblRadniNalozi, txtID);
        lblRadniNalozi.setText("Radni nalozi:");
    }//GEN-LAST:event_btnPoIDuActionPerformed

    private void btnPoDatumuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPoDatumuActionPerformed
    {//GEN-HEADEREND:event_btnPoDatumuActionPerformed
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        poDatumuRadniNalozi(lista, tblRadniNalozi, dtmDatum);
        lblRadniNalozi.setText("Radni nalozi:");
    }//GEN-LAST:event_btnPoDatumuActionPerformed

    private void tblRadniNaloziMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblRadniNaloziMouseReleased
    {//GEN-HEADEREND:event_tblRadniNaloziMouseReleased
        tblRadniNaloziMouseClicked(evt);
    }//GEN-LAST:event_tblRadniNaloziMouseReleased

    private void tblNeplaceneFaktureMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblNeplaceneFaktureMouseClicked
    {//GEN-HEADEREND:event_tblNeplaceneFaktureMouseClicked

    }//GEN-LAST:event_tblNeplaceneFaktureMouseClicked

    private void txtModelTraziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModelTraziActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModelTraziActionPerformed

    private void btnPrikaziiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziiActionPerformed
        java.util.Date dat1 = null;
        java.util.Date dat2 = null;
        boolean flag = true;
        try {
            dat1 = (java.util.Date) dateChooserAktivnosti1.getDate();
            dat2 = (java.util.Date) dateChooserAktivnosti2.getDate();
        } catch (NullPointerException e) {
            //e.printStackTrace();
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(new JFrame(), "Nepravilan format datuma", "Greška", JOptionPane.ERROR_MESSAGE);
            flag = false;
        }
        try {
            if (flag) {
                PocetnaLogika pocetnaLogika = new PocetnaLogika(this);
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
                if (!dat1.equals(dat2)) {
                    datumLabel.setText("Aktivnosti: " + sdf.format(dat1) + "-" + sdf.format(dat2));
                    pocetnaLogika.prikaziAktivnosti(dat1, dat2);
                } else {
                    datumLabel.setText("Aktivnosti: " + sdf.format(dat1));
                    pocetnaLogika.prikaziAktivnosti(dat1, dat1);
                }
            }
        } catch (NullPointerException e) {
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(new JFrame(), "Nepravilan format datuma", "Greška",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPrikaziiActionPerformed

    private void jLabel4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLabel4PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4PropertyChange

    private void btnBazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBazaActionPerformed
        // treba serijalizovati datum u file baza.ser
         JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(this);
        String path = "";
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        
        try {
            File f =fc.getSelectedFile();
            path = f.getAbsolutePath();
            path = path.replace('\\', '/');
            path = path + "_" + date + ".sql";
            
        } catch (Exception e) {
            e.printStackTrace();
        }

 Process p=null;
        try {
            Runtime runtime = Runtime.getRuntime();
            p=runtime.exec("C:/Program Files (x86)/MySQL/MySQL Server 5.7/bin/mysqldump.exe -uroot -pmrkonjicgrad --add-drop-database -B autoservismaric -r"+path);
            
            int processComplete = p.waitFor();
            if (processComplete==0) {
                jLabel1.setText("Backup Created Succuss");
            }else{
                jLabel1.setText("Can't Create backup");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnBazaActionPerformed

    private void btnDodajModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajModelActionPerformed
        new DodajModel(new JFrame(), true).setVisible(true);

        int size = cbModel.getItemCount();
        for (int i = 0; i < size; i++) {
            cbModel.removeItemAt(0);
        }
        size = cbMarka.getItemCount();
        for (int i = 0; i < size; i++) {
            cbMarka.removeItemAt(0);
        }

        //za dodavanje
        ArrayList<ModelVozilaDTO> modeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
        ArrayList<String> markee = new ArrayList<String>();
        ArrayList<String> modelii = new ArrayList<String>();
        markee.add("Svi");
        modelii.add("Svi");

        jcbModel.removeAllItems();
        jcbMarka.removeAllItems();
        cbModel.removeAllItems();
        cbMarka.removeAllItems();

        jcbModel.addItem("Svi");
        jcbMarka.addItem("Svi");

        cbModel.addItem("Svi");
        cbMarka.addItem("Svi");
        //cbModel.addItem("Svi");
        for (ModelVozilaDTO d : modeli) {
            if (!modelii.contains(d.getModel())) {
                modelii.add(d.getModel());
                jcbModel.addItem(d.getModel());
                cbModel.addItem(d.getModel());
            }
            if (!markee.contains(d.getMarka())) {
                markee.add(d.getMarka());
                jcbMarka.addItem(d.getMarka());
                cbMarka.addItem(d.getMarka());
            }
        }
    }//GEN-LAST:event_btnDodajModelActionPerformed

    private void jcbMarkaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbMarkaItemStateChanged
        modelVozilaLogika.ucitajModeleDodavanje(this, jcbMarka.getItemAt(jcbMarka.getSelectedIndex()));
    }//GEN-LAST:event_jcbMarkaItemStateChanged

    private void cbMarkaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbMarkaItemStateChanged
        modelVozilaLogika.ucitajModelePretrazivanje(this, cbMarka.getItemAt(cbMarka.getSelectedIndex()));
    }//GEN-LAST:event_cbMarkaItemStateChanged

    private void jrbPravnooActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbPravnooActionPerformed
        tfNazivv.setEditable(true);
        tfImee.setEditable(false);
        tfPrezimee.setEditable(false);
        tfImee.setText("");
        tfPrezimee.setText("");
        tfImee.setBackground(Color.gray);
        tfPrezimee.setBackground(Color.gray);
        tfNazivv.setBackground(Color.white);
        jrbPrivatno.setSelected(false);
    }//GEN-LAST:event_jrbPravnooActionPerformed

    private void jrbPrivatnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbPrivatnoActionPerformed
        tfNazivv.setEditable(false);
        tfNazivv.setText("");
        tfImee.setEditable(true);
        tfPrezimee.setEditable(true);
        tfNazivv.setBackground(Color.gray);
        jrbPravnoo.setSelected(false);
        tfImee.setBackground(Color.white);
        tfPrezimee.setBackground(Color.white);
    }//GEN-LAST:event_jrbPrivatnoActionPerformed

    private void btnPrikaziSvePredracuneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziSvePredracuneActionPerformed
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        neplaceneFaktureZaPocetnuStranu(lista, tblNeplaceneFakture);
        if (tblNeplaceneFakture.getRowCount() > 0) {
            tblNeplaceneFakture.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_btnPrikaziSvePredracuneActionPerformed

    private void btnPrikaziPredracuneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziPredracuneActionPerformed
        //KnjigovodstvoLogika knjLogika = new KnjigovodstvoLogika();
        //if(!"".equals(tfNazivv.getText()))
            //knjLogika.pretraziNeplaceneFakturePoNazivu(this, tfNazivv.getText(), tfImee.getText(), tfPrezimee.getText());
            KnjigovodstvoLogika.pretraziNeplaceneFakturePoNazivu(this, tfNazivv.getText(), tfImee.getText(), tfPrezimee.getText());
    }//GEN-LAST:event_btnPrikaziPredracuneActionPerformed

    private void btnPregledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPregledActionPerformed
        statistikaLogika.loadInterval(this);
    }//GEN-LAST:event_btnPregledActionPerformed

    private void rbPravnoRadniNalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPravnoRadniNalogActionPerformed
        tfNazivRadniNalog.setEditable(true);
        tfImeRadniNalog.setEditable(false);
        tfPrezimeRadniNalog.setEditable(false);
        tfImeRadniNalog.setText("");
        tfPrezimeRadniNalog.setText("");
        tfImeRadniNalog.setBackground(Color.gray);
        tfPrezimeRadniNalog.setBackground(Color.gray);
        tfNazivRadniNalog.setBackground(Color.white);
        rbPrivatnoRadniNalog.setSelected(false);
        cbSviRadniNalog.setSelected(false);
    }//GEN-LAST:event_rbPravnoRadniNalogActionPerformed

    private void rbPrivatnoRadniNalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPrivatnoRadniNalogActionPerformed
        cbSviRadniNalog.setSelected(false);
        tfNazivRadniNalog.setEditable(false);
        tfNazivRadniNalog.setText("");
        tfImeRadniNalog.setEditable(true);
        tfPrezimeRadniNalog.setEditable(true);
        tfNazivRadniNalog.setBackground(Color.gray);
        rbPravnoRadniNalog.setSelected(false);
        tfImeRadniNalog.setBackground(Color.white);
        tfPrezimeRadniNalog.setBackground(Color.white);
    }//GEN-LAST:event_rbPrivatnoRadniNalogActionPerformed

    private void btnNoviRadniNalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoviRadniNalogActionPerformed
        new IzaberiVoziloDialog(this, true).setVisible(true);
    }//GEN-LAST:event_btnNoviRadniNalogActionPerformed

    private void btnPrikaziRadniNalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziRadniNalogActionPerformed
        voziloKupacMeniLogika.prikaziRadneNaloge(this);
    }//GEN-LAST:event_btnPrikaziRadniNalogActionPerformed

    private void cbSviRadniNalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSviRadniNalogActionPerformed
        if (cbSviRadniNalog.isSelected()) {
            bGTraziRadniNalog.clearSelection();
            tfImeRadniNalog.setEditable(false);
            tfImeRadniNalog.setBackground(Color.gray);
            tfImeRadniNalog.setText("");
            tfPrezimeRadniNalog.setEditable(false);
            tfPrezimeRadniNalog.setBackground(Color.gray);
            tfPrezimeRadniNalog.setText("");
            tfNazivRadniNalog.setEditable(false);
            tfNazivRadniNalog.setBackground(Color.gray);
            tfNazivRadniNalog.setText("");
        } else {
            rbPrivatnoRadniNalog.setSelected(true);
            rbPravnoRadniNalog.setSelected(false);
            tfImeRadniNalog.setEditable(true);
            tfImeRadniNalog.setText("");
            tfImeRadniNalog.setBackground(Color.white);
            tfPrezimeRadniNalog.setEditable(true);
            tfPrezimeRadniNalog.setText("");
            tfPrezimeRadniNalog.setBackground(Color.white);
        }
    }//GEN-LAST:event_cbSviRadniNalogActionPerformed

    public void prikaziKupceSveUTabeli(ArrayList<KupacDTO> kupci) {
        voziloKupacMeniLogika.prikaziKupceSveUTabeli(kupci, this);
    }

    public void prikaziKupcePravneUTabeli(ArrayList<KupacDTO> kupci) {
        voziloKupacMeniLogika.prikaziKupcePravneUTabeli(kupci, this);
    }

    public void setColor(JPanel panel) {
        panel.setBackground(new Color(51, 102, 255));

    }

    public void resetColor(JPanel panel) {
        panel.setBackground(new Color(51, 51, 255));

    }

    public void resetAllColors(JPanel panel) {
        resetColor(pnlMeniPocetnaStrana);
        resetColor(pnlMeniRadniNalozi);
        resetColor(pnlMeniVozila);
        resetColor(pnlMeniKnjigovodstvo);
        resetColor(pnlMeniZaposleni);
        resetColor(pnlMeniDijelovi);
        resetColor(pnlMeniStatistika);
        resetColor(pnlMeniZakazivanja);

        setColor(panel);

    }

    public void menuItemClick(JPanel menuItem, int index, JPanel newPanel) {
        setColor(menuItem);
        for (int i = 0; i < numberOfItems; i++) {
            menu[i] = false;
        }

        menu[index] = true;

        resetAllColors(menuItem);

        pnlParent.removeAll();
        pnlParent.add(newPanel);
        pnlParent.repaint();
        pnlParent.revalidate();

    }

    private void loadAutosuggester() {
        //ucitavanje autosuggestora
        if (voziloPanelPrviPut == true) {
            voziloPanelPrviPut = false;
            markeAU = ucitajPreporukeMarke();
            registracijeAU = ucitajPreporukeRegistracija();
            //vlasnikAU = ucitajPreporukeVlasnik();
            //pravniNazivAU = ucitajPreporukePravniNaziv();
            modelAU = ucitajPreporukeModel();
        }
    }

    private void loadDijeloviForm() {

        ArrayList<DioDTO> dijelovi = DAOFactory.getDAOFactory().getDioDAO().getSviDijelovi();
        DefaultTableModel dtm = (DefaultTableModel) jTable.getModel();
        dtm.setRowCount(0);

        int i = 0;
        if (dijelovi != null) {
            while (i < dijelovi.size()) {
                DioDTO d = dijelovi.get(i);
                dtm.addRow(new Object[]{d.getId(), d.getSifra(), d.getNaziv(), d.getMarka(), d.getModel(), d.getGodisteVozila(),
                    d.getVrstaGoriva(), d.getTrenutnaCijena(), d.getKolicina(), d.getNovo() ? "Da" : "Ne"});
                i++;
            }
        }
        int size = cbModel.getItemCount();
        for (int j = 0; j < size; j++) {
            cbModel.removeItemAt(0);
        }
        size = cbMarka.getItemCount();
        for (int j = 0; j < size; j++) {
            cbMarka.removeItemAt(0);
        }
        //ArrayList<DioDTO> nazivi = DAOFactory.getDAOFactory().getDioDAO().getSviDijelovi();
        //ArrayList<String> naz = new ArrayList<String>();
        //za dodavanje
        ArrayList<ModelVozilaDTO> modeli = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
        ArrayList<String> markee = new ArrayList<String>();
        ArrayList<String> modelii = new ArrayList<String>();
        markee.add("Svi");
        modelii.add("Svi");

        jcbModel.removeAllItems();
        jcbMarka.removeAllItems();
        cbModel.removeAllItems();
        cbMarka.removeAllItems();

        jcbModel.addItem("Svi");
        jcbMarka.addItem("Svi");

        cbModel.addItem("Svi");
        cbMarka.addItem("Svi");
        //cbModel.addItem("Svi");
        /*for (DioDTO d : nazivi) {
            if (!naz.contains(d.getModel())) {
                naz.add(d.getModel());*/
        for (ModelVozilaDTO d : modeli) {
            if (!modelii.contains(d.getModel())) {
                modelii.add(d.getModel());
                jcbModel.addItem(d.getModel());
                cbModel.addItem(d.getModel());
            }
            /*}

        //ArrayList<DioDTO> marke = DAOFactory.getDAOFactory().getDioDAO().getSviDijelovi();
        ArrayList<String> mar = new ArrayList<String>();
        cbMarka.removeAllItems();
        //cbMarka.addItem("Svi");
        for (DioDTO d : nazivi) {
            if (!mar.contains(d.getMarka())) {
                mar.add(d.getMarka());*/
            if (!markee.contains(d.getMarka())) {
                markee.add(d.getMarka());
                jcbMarka.addItem(d.getMarka());
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
        return cbGodina;
    }

    public void setComboBoxGodina(JComboBox<String> comboBoxGodina) {
        this.cbGodina = comboBoxGodina;
    }

    public JComboBox<String> getComboBoxMjesec() {
        return cbMjesec;
    }

    public void setComboBoxMjesec(JComboBox<String> comboBoxMjesec) {
        this.cbMjesec = comboBoxMjesec;
    }

    public JLabel getLabelDnevnaZarada() {
        return lblDnevnaZarada;
    }

    public void setLabelDnevnaZarada(JLabel labelDnevnaZarada) {
        this.lblDnevnaZarada = labelDnevnaZarada;
    }

    public JLabel getLabelDnevnaZaradaDijelovi() {
        return lblDnevnaZaradaDijelovi;
    }

    public void setLabelDnevnaZaradaDijelovi(JLabel labelDnevnaZaradaDijelovi) {
        this.lblDnevnaZaradaDijelovi = labelDnevnaZaradaDijelovi;
    }

    public JLabel getLabelGodisnjaZarada() {
        return lblGodisnjaZarada;
    }

    public void setLabelGodisnjaZarada(JLabel labelGodisnjaZarada) {
        this.lblGodisnjaZarada = labelGodisnjaZarada;
    }

    public JLabel getLabelGodisnjaZaradaDijelovi() {
        return lblGodisnjaZaradaDijelovi;
    }

    public void setLabelGodisnjaZaradaDijelovi(JLabel labelGodisnjaZaradaDijelovi) {
        this.lblGodisnjaZaradaDijelovi = labelGodisnjaZaradaDijelovi;
    }

    public JLabel getLabelIntervalZarada() {
        return lblIntervalZarada;
    }

    public void setLabelIntervalZarada(JLabel labelIntervalZarada) {
        this.lblIntervalZarada = labelIntervalZarada;
    }

    public JLabel getLabelIntervalZaradaDijelovi() {
        return lblIntervalZaradaDijelovi;
    }

    public void setLabelIntervalZaradaDijelovi(JLabel labelIntervalZaradaDijelovi) {
        this.lblIntervalZaradaDijelovi = labelIntervalZaradaDijelovi;
    }

    public JLabel getLabelMjesecnaZarada() {
        return lblMjesecnaZarada;
    }

    public void setLabelMjesecnaZarada(JLabel labelMjesecnaZarada) {
        this.lblMjesecnaZarada = labelMjesecnaZarada;
    }

    public JLabel getLabelMjesecnaZaradaDijelovi() {
        return lblMjesecnaZaradaDijelovi;
    }

    public void setLabelMjesecnaZaradaDijelovi(JLabel labelMjesecnaZaradaDijelovi) {
        this.lblMjesecnaZaradaDijelovi = labelMjesecnaZaradaDijelovi;
    }

    public JLabel getLabelPopravkeDanas() {
        return lblPopravkeDanas;
    }

    public void setLabelPopravkeDanas(JLabel labelPopravkeDanas) {
        this.lblPopravkeDanas = labelPopravkeDanas;
    }

    public JLabel getLabelPopravkeGodina() {
        return lblPopravkeGodina;
    }

    public void setLabelPopravkeGodina(JLabel labelPopravkeGodina) {
        this.lblPopravkeGodina = labelPopravkeGodina;
    }

    public JLabel getLabelPopravkeInterval() {
        return lblPopravkeInterval;
    }

    public void setLabelPopravkeInterval(JLabel labelPopravkeInterval) {
        this.lblPopravkeInterval = labelPopravkeInterval;
    }

    public JLabel getLabelPopravkeMjesec() {
        return lblPopravkeMjesec;
    }

    public void setLabelPopravkeMjesec(JLabel labelPopravkeMjesec) {
        this.lblPopravkeMjesec = labelPopravkeMjesec;
    }

    public JLabel getLblBrojFaktura() {
        return lblBrojFaktura;
    }

    public JLabel getLblBrojNeplacenihFaktura() {
        return lblBrojNeplacenihFaktura;
    }

    public JLabel getLblBrojPlacenihFaktura() {
        return lblBrojPlacenihFaktura;
    }
    
    
   
    public JPanel getPanelGrafikAuta() {
        return pnlGrafikAuta;
    }

    public void setPanelGrafikAuta(JPanel panelGrafikAuta) {
        this.pnlGrafikAuta = panelGrafikAuta;
    }

    public JPanel getPanelGrafikFakture() {
        return pnlGrafikFakture;
    }

    public void setPanelGrafikFakture(JPanel panelGrafikFakture) {
        this.pnlGrafikFakture = panelGrafikFakture;
    }

    public JPanel getPanelGrafikPopravke() {
        return pnlGrafikPopravke;
    }

    public void setPanelGrafikPopravke(JPanel panelGrafikPopravke) {
        this.pnlGrafikPopravke = panelGrafikPopravke;
    }

    public JPanel getPanelGrafikPrihodiUkupno() {
        return pnlGrafikPrihodiUkupno;
    }

    public void setPanelGrafikPrihodiUkupno(JPanel panelGrafikPrihodiUkupno) {
        this.pnlGrafikPrihodiUkupno = panelGrafikPrihodiUkupno;
    }

    public JPanel getPanelGrafikPrihodiDijelovi() {
        return pnlGrafikPrihodiDijelovi;
    }

    public void setPanelGrafikPrihodiDijelovi(JPanel PanelGrafikPrihodiDijelovi) {
        this.pnlGrafikPrihodiDijelovi = PanelGrafikPrihodiDijelovi;
    }

    public void setIdVlasnika(int idVlasnika) {
        this.idVlasnika = idVlasnika;
    }

    public static HomeForm1 getHomeForm() {
        return homeForm;
    }

    public static int getOpcija() {
        return opcija;
    }

    public AutoSuggestor getModelAU() {
        return modelAU;
    }

    public AutoSuggestor getMarkeAU() {
        return markeAU;
    }

    public AutoSuggestor getVlasnikAU() {
        return vlasnikAU;
    }

    public AutoSuggestor getRegistracijeAU() {
        return registracijeAU;
    }

    public AutoSuggestor getPravniNazivAU() {
        return pravniNazivAU;
    }

    public AutoSuggestor getAutoModel() {
        return autoModel;
    }

    public AutoSuggestor getAutoMarka() {
        return autoMarka;
    }

    public AutoSuggestor getAutoNaziv() {
        return autoNaziv;
    }

    public AutoSuggestor getAutoSifra() {
        return autoSifra;
    }

    public static boolean isVoziloPanelPrviPut() {
        return voziloPanelPrviPut;
    }

    public static int getSelRedDio() {
        return selRedDio;
    }

    public static Double getPdv() {
        return pdv;
    }

    public static int getSelektovanRed() {
        return selektovanRed;
    }

    public static int getIdVozila() {
        return idVozila;
    }

    public static int getNumberOfItems() {
        return numberOfItems;
    }

    public static String[] getMjeseci() {
        return mjeseci;
    }

    public static boolean[] getMenu() {
        return menu;
    }

    public StatistikaLogika getStatistikaLogika() {
        return statistikaLogika;
    }

    public JPanel getNapomene2() {
        return Napomene2;
    }

    public JButton getBtnDodaj() {
        return btnDodaj;
    }

    public JButton getBtnDodajTermin() {
        return btnDodajTermin;
    }

    public JButton getBtnFakturisano() {
        return btnFakturisano;
    }

    public JButton getBtnNefakturisano() {
        return btnNefakturisano;
    }

    public JButton getBtnPoDatumu() {
        return btnPoDatumu;
    }

    public JButton getBtnPoIDu() {
        return btnPoIDu;
    }

    public JButton getBtnPonisti() {
        return btnPonisti;
    }

    public JButton getBtnPonistiSve() {
        return btnPonistiSve;
    }

    public JButton getBtnPonistiUnosePretraga() {
        return btnPonistiUnosePretraga;
    }

    public JButton getBtnPonistiUnoseTermin() {
        return btnPonistiUnoseTermin;
    }

    public JButton getBtnPredracun() {
        return btnPredracun;
    }

    public JButton getBtnPretrazi() {
        return btnPretrazi;
    }

    public JButton getBtnPrikaziSve() {
        return btnPrikaziSve;
    }

    public JButton getBtnProdani() {
        return btnProdani;
    }

    public JButton getBtnPronadji() {
        return btnPronadjiVozilo;
    }

    public JButton getBtnPronadjiTermin() {
        return btnPronadjiTermin;
    }

    public JButton getBtnRacun() {
        return btnRacun;
    }

    public JButton getBtnSviDijelovi() {
        return btnSviDijelovi;
    }

    public JButton getBtnTrazi() {
        return btnTrazi;
    }

    public JButton getButtonDodajZaposlenog() {
        return buttonDodajZaposlenog;
    }

    public JButton getButtonPregled() {
        return btnPregled;
    }

    public JButton getButtonPregledGrafik() {
        return btnPregledGrafik;
    }

    public JButton getButtonPrikazSvihBivsihRadnika() {
        return buttonPrikazSvihBivsihRadnika;
    }

    public JButton getButtonTraziRadneNalogeRadnika() {
        return buttonTraziRadneNalogeRadnika;
    }

    public JComboBox<String> getCbGorivo() {
        return cbGorivo;
    }

    public JComboBox<String> getCbMarka() {
        return cbMarka;
    }

    public JComboBox<String> getCbModel() {
        return cbModel;
    }

    public JCheckBox getCbNovo() {
        return cbNovo;
    }

    public JCheckBox getCbStanje() {
        return cbStanje;
    }

    public JCheckBox getCbSvi() {
        return cbSvi;
    }

    public JPanel getDanasnjeAktivnostiPanel() {
        return danasnjeAktivnostiPanel;
    }

    public JDateChooser getDateChooserDatumDoZaposlenog() {
        return dateChooserDatumDoZaposlenog;
    }

    public JDateChooser getDateChooserDatumOdZaposlenog() {
        return dateChooserDatumOdZaposlenog;
    }

    public JDateChooser getDateChooserDatumPrimanjaURadniOdnos() {
        return dateChooserDatumPrimanjaURadniOdnos;
    }

    public JDateChooser getDateChooserDatumRodjenja() {
        return dateChooserDatumRodjenja;
    }

    public JLabel getDatumLabel() {
        return datumLabel;
    }

    public JPanel getDijeloviPanel() {
        return pnlDijelovi;
    }

    public JPanel getDodajDioPanel() {
        return dodajDioPanel;
    }

    public JDateChooser getDtmDatum() {
        return dtmDatum;
    }

    public JDateChooser getDtmDatumPretraga() {
        return dtmDatumPretraga;
    }

    public JDateChooser getDtmDatumTermina() {
        return dtmDatumTermina;
    }

    public JButton getjButton1() {
        return btnPrikaziSvaVozila;
    }

    public JButton getjButton10() {
        return btnIzmijeniIzbrisiModel;
    }

    public JButton getjButton2() {
        return btnDodajVozilo;
    }

    public JButton getjButton4() {
        return btnDodajVlasnika;
    }

    public JButton getjButton5() {
        return btnDodajModel2;
    }

    public JDateChooser getjDateChooserDatumDo() {
        return dcDatumDo;
    }

    public JDateChooser getjDateChooserDatumOd() {
        return dcDatumOd;
    }

    public JPanel getjPanel1() {
        return pnlPronadjiZakazivanja;
    }

    public JPanel getjPanel10() {
        return panelOsnovniRN;
    }

    public JPanel getjPanel13() {
        return jPanel13;
    }

    public JPanel getjPanel14() {
        return jPanel14;
    }

    public JPanel getjPanel17() {
        return pnlOsnovniKnjigovodstvo;
    }

    public JPanel getjPanel18() {
        return pnlKontrolnaTablaStatistika;
    }

    public JPanel getjPanel19() {
        return pnlZaradaUkupno;
    }

    public JPanel getjPanel2() {
        return pnlZakaziZakazivanja;
    }

    public JPanel getjPanel20() {
        return pnlBrojPopravki;
    }

    public JPanel getjPanel21() {
        return pnlFakture;
    }

    public JPanel getjPanel22() {
        return pnlOkvirKnjigovodstvo;
    }

    public JPanel getjPanel23() {
        return pnlOsnovniZakazivanja;
    }

    public JPanel getjPanel24() {
        return pnlOkvirZakazivanja;
    }

    public JPanel getjPanel26() {
        return panelOsnovniPretragaRN;
    }

    public JPanel getjPanel27() {
        return jPanel27;
    }

    public JPanel getjPanel28() {
        return jPanel28;
    }

    public JPanel getjPanel29() {
        return jPanel29;
    }

    public JPanel getjPanel3() {
        return pnlSlika;
    }

    public JPanel getjPanel30() {
        return pnlZaradaDijelovi;
    }

    public JPanel getjPanel4() {
        return pnlNaloziKnjigovodstvo;
    }

    public JPanel getjPanel5() {
        return jPanel5;
    }

    public JPanel getjPanel6() {
        return jPanel6;
    }

    public JPanel getjPanel7() {
        return jPanel7;
    }

    public JPanel getjPanel8() {
        return pnlOdabirIntervala;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public JScrollPane getjScrollPane10() {
        return pnlTabelaZakazivanje;
    }

    public JScrollPane getjScrollPane11() {
        return jScrollPane11;
    }

    public JScrollPane getjScrollPane12() {
        return jScrollPane12;
    }

    public JScrollPane getjScrollPane13() {
        return jScrollPane13;
    }

    public JScrollPane getjScrollPane2() {
        return pnlTabelaKnjigovodstvo;
    }

    public JScrollPane getjScrollPane4() {
        return jScrollPane4;
    }

    public JScrollPane getjScrollPane8() {
        return jScrollPane8;
    }

    public JScrollPane getjScrollPane9() {
        return pnlRadniNaloziKnjigovodstvo;
    }

    public JSeparator getjSeparator2() {
        return jSeparator2;
    }

    /*public JSeparator getjSeparator24() {
        return jSeparator24;
    }

    public JSeparator getjSeparator25() {
        return jSeparator25;
    }*/
    public JSeparator getjSeparator26() {
        return jSeparator26;
    }

    public JTabbedPane getjTabbedPane1() {
        return tabpnlGrafici;
    }

    public JTable getjTable() {
        return jTable;
    }

    public JTable getjTable10() {
        return tableAktivnosti;
    }

    public static JTable getjTableZaposleni() {
        return jTableZaposleni;
    }

    /*public JTextField getjTextField21() {
        return jTextField21;
    }

    public JTextField getjTextField22() {
        return jTextField22;
    }*/
    public JComboBox<String> getJcbGorivo() {
        return jcbGorivo;
    }

    public JComboBox<String> getJcbMarka() {
        return jcbMarka;
    }

    public JComboBox<String> getJcbModel() {
        return jcbModel;
    }

    public JTextField getJtfCijena() {
        return jtfCijena;
    }

    public JTextField getJtfGodiste() {
        return jtfGodiste;
    }

    public JTextField getJtfKolicina() {
        return jtfKolicina;
    }

    public JTextField getJtfNaziv() {
        return jtfNaziv;
    }

    public JTextField getJtfSifra() {
        return jtfSifra;
    }

    public JPanel getKnjigovodstvoPanel() {
        return pnlKnjigovodstvo;
    }

    public JLabel getLabelBrojRadnihNaloga() {
        return labelBrojRadnihNaloga;
    }

    public JLabel getLabelOstvareniProfitRadnika() {
        return labelOstvareniProfitRadnika;
    }

    public JLabel getLabelPoruka() {
        return labelPoruka;
    }

    public JPanel getMenu1jPanel() {
        return pnlMeniPocetnaStrana;
    }

    public JPanel getMenu2jPanel() {
        return pnlMeniRadniNalozi;
    }

    public JPanel getMenu3jPanel() {
        return pnlMeniVozila;
    }

    public JPanel getMenu4jPanel() {
        return pnlMeniKnjigovodstvo;
    }

    public JPanel getMenu5jPanel() {
        return pnlMeniZaposleni;
    }

    public JPanel getMenu6jPanel() {
        return pnlMeniDijelovi;
    }

    public JPanel getMenu7jPanel() {
        return pnlMeniStatistika;
    }

    public JPanel getMenu8jPanel() {
        return pnlMeniZakazivanja;
    }

    public JMenuItem getMenuItemDetaljniOpis() {
        return menuItemDetaljniOpis;
    }

    public JMenuItem getMenuItemIzmjeniRadnika() {
        return menuItemIzmjeniRadnika;
    }

    public JMenuItem getMenuItemOtpustiRadnika() {
        return menuItemOtpustiRadnika;
    }

    public JPanel getMenuPanel() {
        return pnlMeni;
    }

    public JPanel getOdabirMjesecaStatistikaPanel() {
        return pnlOdabirMjesecaStatistika;
    }

    public JPanel getPanelAkcijeNaFormi() {
        return pnllAkcijeNaFormi;
    }

    public JPanel getPanelPronadjiVlasnika() {
        return pnlPronadjiVlasnika;
    }

    public JPanel getPanelPronadjiVozilo() {
        return pnllPronadjiVozilo;
    }

    public JPanel getPanelVozilo() {
        return pnlVozilo;
    }

    public JPanel getParentPanel() {
        return pnlParent;
    }

    public JPanel getPocetnajPanel() {
        return pnlPocetna;
    }
    

    public JPopupMenu getPopupMenuZaposleni() {
        return popupMenuZaposleni;
    }

    public JPanel getPretraziPanel() {
        return pretraziPanel;
    }

    public JPanel getRadniNaloziPanel() {
        return pnlRadniNalozi;
    }

    public JRadioButton getRbPravnoLiceVozilo() {
        return rbPravnoLiceVozilo;
    }

    public JRadioButton getRbPravnoTrazi() {
        return rbPravnoTrazi;
    }

    public JRadioButton getRbPrivatnoLiceVozilo() {
        return rbPrivatnoLiceVozilo;
    }

    public JRadioButton getRbPrivatnoTrazi() {
        return rbPrivatnoTrazi;
    }

    public JScrollPane getSpVoziloPretraga() {
        return spVoziloPretraga;
    }

    public JPanel getStatistikaHeaderjPanel() {
        return pnlHeaderStatistika;
    }

    public JPanel getStatistikajPanel() {
        return pnlStatistika;
    }

    public static JTable getTableRadniNalozi() {
        return tableRadniNalozi;
    }

    public JTable getTableVozila() {
        return tableVozila;
    }

    public JTable getTblFaktura() {
        return tblFaktura;
    }

    public JTable getTblNeplaceneFakture() {
        return tblNeplaceneFakture;
    }

    public JTable getTblRadniNalozi() {
        return tblRadniNalozi;
    }

    public JTable getTblTermini() {
        return tblTermini;
    }
    public JTable getJTable(){
        return jTable;
    }

    public JTextField getTextFieldAdresa() {
        return textFieldAdresa;
    }

    public JTextField getTextFieldBrojLicneKarte() {
        return textFieldBrojLicneKarte;
    }

    public JTextField getTextFieldFunkcijaRadnika() {
        return textFieldFunkcijaRadnika;
    }

    public JTextField getTextFieldIme() {
        return textFieldIme;
    }

    public JTextField getTextFieldImeOca() {
        return textFieldImeOca;
    }

    public JTextField getTextFieldPrezime() {
        return textFieldPrezime;
    }

    public JTextField getTextFieldStrucnaSprema() {
        return textFieldStrucnaSprema;
    }

    public JTextField getTextFieldTelefon() {
        return textFieldTelefon;
    }

    public JTextField getTfGodiste() {
        return tfGodiste;
    }

    public JTextField getTfGodisteTrazi() {
        return txtGodisteTrazi;
    }

    public JTextField getTfId() {
        return tfId;
    }

    public JTextField getTfImeTrazi() {
        return txtImeTrazi;
    }

    public JTextField getTfImeVozilo() {
        return txtImeVozilo;
    }

    public JTextField getTfMarkaTrazi() {
        return txtMarkaTrazi;
    }

    public JTextField getTfModelTrazi() {
        return txtModelTrazi;
    }

    public JTextField getTfNaziv() {
        return tfNaziv;
    }

    public JTextField getTfNazivTrazi() {
        return txtNazivTrazi;
    }

    public JTextField getTfNazivVozilo() {
        return txtNazivVozilo;
    }

    public JTextField getTfPrezimeTrazi() {
        return txtPrezimeTrazi;
    }

    public JTextField getTfPrezimeVozilo() {
        return txtPrezimeVozilo;
    }

    public JTextField getTfRegistracijaTrazi() {
        return txtRegistracijaTrazi;
    }

    public JTextField getTfSifra() {
        return tfSifra;
    }

    public JTextField getTxtBezPDV() {
        return txtBezPDV;
    }

    public JTextField getTxtBrojTelefonaPretraga() {
        return txtBrojTelefonaPretraga;
    }

    public JTextField getTxtBrojTelefonaTermina() {
        return txtBrojTelefonaTermina;
    }

    public JTextField getTxtID() {
        return txtID;
    }

    public JTextField getTxtImePretraga() {
        return txtImePretraga;
    }

    public JTextField getTxtImeTermina() {
        return txtImeTermina;
    }

    public JTextField getTxtMarkaPretraga() {
        return txtMarkaPretraga;
    }

    public JTextField getTxtMarkaTermina() {
        return txtMarkaTermina;
    }

    public JTextField getTxtModelTermina() {
        return txtModelTermina;
    }

    public JTextField getTxtPDV() {
        return txtPDV;
    }

    public JTextField getTxtPrezimePretraga() {
        return txtPrezimePretraga;
    }

    public JTextField getTxtPrezimeTermina() {
        return txtPrezimeTermina;
    }

    public JTextField getTxtUkupno() {
        return txtUkupno;
    }

    public JSpinner getTxtVrijemeTerminaMinuti() {
        return txtVrijemeTerminaMinuti;
    }

    public JSpinner getTxtVrijemeTerminaSati() {
        return txtVrijemeTerminaSati;
    }

    public JPanel getVozilaPanel() {
        return pnlVozila;
    }

    public ButtonGroup getbGTraziRadniNalog() {
        return bGTraziRadniNalog;
    }

    public VoziloKupacMeniLogika getVoziloKupacMeniLogika() {
        return voziloKupacMeniLogika;
    }

    public ModelVozilaLogika getModelVozilaLogika() {
        return modelVozilaLogika;
    }

    public PocetnaLogika getPocetnaLogika() {
        return pocetnaLogika;
    }

    public DioLogika getDioLogika() {
        return dioLogika;
    }

    public static int getTrenutnaGodina() {
        return trenutnaGodina;
    }

    public JButton getBtnBaza() {
        return btnBaza;
    }

    public JButton getBtnDodajModel() {
        return btnDodajModel;
    }

    public JButton getBtnDodajModel2() {
        return btnDodajModel2;
    }

    public JButton getBtnDodajVlasnika() {
        return btnDodajVlasnika;
    }

    public JButton getBtnDodajVozilo() {
        return btnDodajVozilo;
    }

    public JButton getBtnIzmijeniIzbrisiModel() {
        return btnIzmijeniIzbrisiModel;
    }

    public JButton getBtnNoviRadniNalog() {
        return btnNoviRadniNalog;
    }

    public JButton getBtnPrikaziPredracune() {
        return btnPrikaziPredracune;
    }

    public JButton getBtnPrikaziRadniNalog() {
        return btnPrikaziRadniNalog;
    }

    public JButton getBtnPrikaziSvaVozila() {
        return btnPrikaziSvaVozila;
    }

    public JButton getBtnPrikaziSvePredracune() {
        return btnPrikaziSvePredracune;
    }

    public JButton getBtnPrikazii() {
        return btnPrikazii;
    }

    public JButton getBtnPronadjiVozilo() {
        return btnPronadjiVozilo;
    }

    public JCheckBox getCbDatumOtvaranja() {
        return cbDatumOtvaranja;
    }

    public JCheckBox getCbDatumZatvaranja() {
        return cbDatumZatvaranja;
    }

    public JCheckBox getCbPotrebnoZavrsiti() {
        return cbPotrebnoZavrsiti;
    }

    public JCheckBox getCbSviRadniNalog() {
        return cbSviRadniNalog;
    }

    public JDateChooser getDateChooserAktivnosti1() {
        return dateChooserAktivnosti1;
    }

    public JDateChooser getDateChooserAktivnosti2() {
        return dateChooserAktivnosti2;
    }

    public JDateChooser getDcDatumOtvaranjaDO() {
        return dcDatumOtvaranjaDO;
    }

    public JDateChooser getDcDatumOtvaranjaOD() {
        return dcDatumOtvaranjaOD;
    }

    public JDateChooser getDcDatumZatvaranjaDO() {
        return dcDatumZatvaranjaDO;
    }

    public JDateChooser getDcDatumZatvaranjaOD() {
        return dcDatumZatvaranjaOD;
    }

    public JDateChooser getDcPotrebnoZavrsitiDO() {
        return dcPotrebnoZavrsitiDO;
    }

    public JDateChooser getDcPotrebnoZavrsitiOD() {
        return dcPotrebnoZavrsitiOD;
    }

    public JLabel getjLabel1() {
        return jLabel1;
    }

    public JLabel getjLabel10() {
        return jLabel10;
    }

    public JLabel getjLabel101() {
        return jLabel101;
    }

    public JLabel getjLabel105() {
        return lblRegistracijaVozila;
    }

    public JLabel getjLabel106() {
        return lblGodisteVozila;
    }

    public JLabel getjLabel107() {
        return lblMarkaVozila;
    }

    public JLabel getjLabel108() {
        return lblPrezimeVozila;
    }

    public JLabel getjLabel109() {
        return jLabel109;
    }

    public JLabel getjLabel11() {
        return jLabel11;
    }

    public JLabel getjLabel110() {
        return jLabel110;
    }

    public JLabel getjLabel111() {
        return lblPrezimeRN;
    }

    public JLabel getjLabel112() {
        return lblImeRN;
    }

    public JLabel getjLabel117() {
        return jLabel117;
    }

    public JLabel getjLabel118() {
        return jLabel118;
    }

    public JLabel getjLabel119() {
        return jLabel119;
    }

    public JLabel getjLabel12() {
        return jLabel12;
    }

    public JLabel getjLabel122() {
        return jLabel122;
    }

    public JLabel getjLabel124() {
        return jLabel124;
    }

    public JLabel getjLabel126() {
        return txtKnjigovodstvo;
    }

    public JLabel getjLabel127() {
        return txtSlikaKnjigovodstvo;
    }

    public JLabel getjLabel128() {
        return txtPronadjiZakazivanja;
    }

    public JLabel getjLabel129() {
        return jLabel129;
    }

    public JLabel getjLabel13() {
        return jLabel13;
    }

    public JLabel getjLabel130() {
        return jLabel130;
    }

    public JLabel getjLabel131() {
        return jLabel131;
    }

    public JLabel getjLabel132() {
        return txtZakaziZakazivanja;
    }

    public JLabel getjLabel133() {
        return jLabel133;
    }

    public JLabel getjLabel134() {
        return jLabel134;
    }

    public JLabel getjLabel135() {
        return jLabel135;
    }

    public JLabel getjLabel136() {
        return jLabel136;
    }

    public JLabel getjLabel137() {
        return jLabel137;
    }

    public JLabel getjLabel138() {
        return txtZakazivanja;
    }

    public JLabel getjLabel139() {
        return txtSlikaZakazivanja;
    }

    public JLabel getjLabel14() {
        return jLabel14;
    }

    public JLabel getjLabel140() {
        return jLabel140;
    }

    public JLabel getjLabel141() {
        return jLabel141;
    }

    public JLabel getjLabel142() {
        return jLabel142;
    }

    public JLabel getjLabel143() {
        return jLabel143;
    }

    public JLabel getjLabel144() {
        return jLabel144;
    }

    public JLabel getjLabel145() {
        return jLabel145;
    }

    public JLabel getjLabel146() {
        return jLabel146;
    }

    public JLabel getjLabel147() {
        return jLabel147;
    }

    public JLabel getjLabel148() {
        return jLabel148;
    }

    public JLabel getjLabel149() {
        return jLabel149;
    }

    public JLabel getjLabel15() {
        return jLabel15;
    }

    public JLabel getjLabel150() {
        return jLabel150;
    }

    public JLabel getjLabel151() {
        return jLabel151;
    }

    public JLabel getjLabel152() {
        return jLabel152;
    }

    public JLabel getjLabel153() {
        return jLabel153;
    }

    public JLabel getjLabel154() {
        return jLabel154;
    }

    public JLabel getjLabel155() {
        return jLabel155;
    }

    public JLabel getjLabel156() {
        return jLabel156;
    }

    public JLabel getjLabel157() {
        return jLabel157;
    }

    public JLabel getjLabel16() {
        return jLabel16;
    }

    public JLabel getjLabel161() {
        return jLabel161;
    }

    public JLabel getjLabel162() {
        return jLabel162;
    }

    public JLabel getjLabel163() {
        return jLabel163;
    }

    public JLabel getjLabel164() {
        return jLabel164;
    }

    public JLabel getjLabel165() {
        return jLabel165;
    }

    public JLabel getjLabel17() {
        return jLabel17;
    }

    public JLabel getjLabel18() {
        return jLabel18;
    }

    public JLabel getjLabel19() {
        return jLabel19;
    }

    public JLabel getjLabel2() {
        return jLabel2;
    }

    public JLabel getjLabel20() {
        return jLabel20;
    }

    public JLabel getjLabel21() {
        return jLabel21;
    }

    public JLabel getjLabel22() {
        return jLabel22;
    }

    public JLabel getjLabel23() {
        return jLabel23;
    }

    public JLabel getjLabel24() {
        return jLabel24;
    }

    public JLabel getjLabel25() {
        return lbRadniNaloziNaslov;
    }

    public JLabel getjLabel26() {
        return lbRadniNaloziSlika;
    }

    public JLabel getjLabel27() {
        return jLabel27;
    }

    public JLabel getjLabel28() {
        return jLabel28;
    }

    public JLabel getjLabel29() {
        return jLabel29;
    }

    public JLabel getjLabel3() {
        return jLabel3;
    }

    public JLabel getjLabel30() {
        return jLabel30;
    }

    public JLabel getjLabel31() {
        return jLabel31;
    }

    public JLabel getjLabel32() {
        return jLabel32;
    }

    public JLabel getjLabel33() {
        return jLabel33;
    }

    public JLabel getjLabel34() {
        return jLabel34;
    }

    public JLabel getjLabel35() {
        return jLabel35;
    }

    public JLabel getjLabel36() {
        return jLabel36;
    }

    public JLabel getjLabel37() {
        return jLabel37;
    }

    public JLabel getjLabel38() {
        return jLabel38;
    }

    public JLabel getjLabel39() {
        return jLabel39;
    }

    public JLabel getjLabel4() {
        return jLabel4;
    }

    public JLabel getjLabel40() {
        return jLabel40;
    }

    public JLabel getjLabel41() {
        return jLabel41;
    }

    public JLabel getjLabel42() {
        return jLabel42;
    }

    public JLabel getjLabel43() {
        return jLabel43;
    }

    public JLabel getjLabel44() {
        return jLabel44;
    }

    public JLabel getjLabel45() {
        return jLabel45;
    }

    public JLabel getjLabel46() {
        return jLabel46;
    }

    public JLabel getjLabel47() {
        return jLabel47;
    }

    public JLabel getjLabel48() {
        return jLabel48;
    }

    public JLabel getjLabel49() {
        return jLabel49;
    }

    public JLabel getjLabel5() {
        return jLabel5;
    }

    public JLabel getjLabel50() {
        return jLabel50;
    }

    public JLabel getjLabel51() {
        return lblDatumOtvaranja;
    }

    public JLabel getjLabel52() {
        return jLabel52;
    }

    public JLabel getjLabel53() {
        return jLabel53;
    }

    public JLabel getjLabel54() {
        return jLabel54;
    }

    public JLabel getjLabel55() {
        return jLabel55;
    }

    public JLabel getjLabel56() {
        return lblDatumOtvaranjaOD;
    }

    public JLabel getjLabel57() {
        return jLabel57;
    }

    public JLabel getjLabel58() {
        return jLabel58;
    }

    public JLabel getjLabel59() {
        return jLabel59;
    }

    public JLabel getjLabel6() {
        return jLabel6;
    }

    public JLabel getjLabel60() {
        return jLabel60;
    }

    public JLabel getjLabel61() {
        return jLabel61;
    }

    public JLabel getjLabel62() {
        return jLabel62;
    }

    public JLabel getjLabel63() {
        return jLabel63;
    }

    public JLabel getjLabel64() {
        return jLabel64;
    }

    public JLabel getjLabel65() {
        return jLabel65;
    }

    public JLabel getjLabel66() {
        return lblDatumOtvaranjaDO;
    }

    public JLabel getjLabel67() {
        return lblPotrebnoZavrsiti;
    }

    public JLabel getjLabel68() {
        return lblPotrebnoZavrsitiOD;
    }

    public JLabel getjLabel69() {
        return jLabel69;
    }

    public JLabel getjLabel7() {
        return jLabel7;
    }

    public JLabel getjLabel70() {
        return jLabel70;
    }

    public JLabel getjLabel71() {
        return jLabel71;
    }

    public JLabel getjLabel72() {
        return lblPotrebnoZavrsitiDO;
    }

    public JLabel getjLabel73() {
        return jLabel73;
    }

    public JLabel getjLabel74() {
        return jLabel74;
    }

    public JLabel getjLabel75() {
        return jLabel75;
    }

    public JLabel getjLabel76() {
        return jLabel76;
    }

    public JLabel getjLabel77() {
        return jLabel77;
    }

    public JLabel getjLabel78() {
        return lblDatumZatvaranjaOD;
    }

    public JLabel getjLabel79() {
        return lblDatumZatvaranjaDO;
    }

    public JLabel getjLabel8() {
        return jLabel8;
    }

    public JLabel getjLabel80() {
        return jLabel80;
    }

    public JLabel getjLabel81() {
        return jLabel81;
    }

    public JLabel getjLabel82() {
        return jLabel82;
    }

    public JLabel getjLabel83() {
        return lblDatumZatvaranja;
    }

    public JLabel getjLabel84() {
        return jLabel84;
    }

    public JLabel getjLabel85() {
        return lblVlasnik;
    }

    public JLabel getjLabel86() {
        return jLabel86;
    }

    public JLabel getjLabel87() {
        return jLabel87;
    }

    public JLabel getjLabel88() {
        return jLabel88;
    }

    public JLabel getjLabel89() {
        return jLabel89;
    }

    public JLabel getjLabel9() {
        return jLabel9;
    }

    public JLabel getjLabel90() {
        return lblNazivRN;
    }

    public JLabel getjLabel91() {
        return jLabel91;
    }

    public JLabel getjLabel92() {
        return lblNaslovVozila;
    }

    public JLabel getjLabel93() {
        return jLabel93;
    }

    public JLabel getjLabel94() {
        return jLabel94;
    }

    public JLabel getjLabel95() {
        return lblSlikaVozila;
    }

    public JLabel getjLabel96() {
        return jLabel96;
    }

    public JLabel getjLabel97() {
        return jLabel97;
    }

    public JPanel getjPanel12() {
        return jPanel12;
    }

    public JPanel getjPanel16() {
        return jPanel16;
    }

    public JPanel getjPanel9() {
        return panelPretraga;
    }

    public JScrollPane getjScrollPane3() {
        return spanelTabela;
    }

    public JSeparator getjSeparator1() {
        return separatorRN;
    }

    public JRadioButton getJrbPravnoo() {
        return jrbPravnoo;
    }

    public JRadioButton getJrbPrivatno() {
        return jrbPrivatno;
    }

    public JLabel getLabelBaza() {
        return labelBaza;
    }

    public JLabel getLblRadniNalozi() {
        return lblRadniNalozi;
    }

    public JRadioButton getRbPravnoRadniNalog() {
        return rbPravnoRadniNalog;
    }

    public JRadioButton getRbPrivatnoRadniNalog() {
        return rbPrivatnoRadniNalog;
    }

    public JTable getTableAktivnosti() {
        return tableAktivnosti;
    }

    public JTable getTableRNalozi() {
        return tableRNalozi;
    }

    public JTextField getTfImeRadniNalog() {
        return tfImeRadniNalog;
    }

    public JTextField getTfImee() {
        return tfImee;
    }

    public JTextField getTfNazivRadniNalog() {
        return tfNazivRadniNalog;
    }

    public JTextField getTfNazivv() {
        return tfNazivv;
    }

    public JTextField getTfPrezimeRadniNalog() {
        return tfPrezimeRadniNalog;
    }

    public JTextField getTfPrezimee() {
        return tfPrezimee;
    }

    public JTextField getTfRegistracijaRadniNalog() {
        return tfRegistracijaRadniNalog;
    }

    
    
    public JPanel getZakazivanjaPanel() {
        return pnlZakazivanja;
    }

    public JPanel getZaposleniPanel() {
        return pnlZaposleni;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Napomene2;
    private javax.swing.JButton btnBaza;
    private javax.swing.JButton btnDodaj;
    private javax.swing.JButton btnDodajModel;
    private javax.swing.JButton btnDodajModel2;
    private javax.swing.JButton btnDodajTermin;
    private javax.swing.JButton btnDodajVlasnika;
    private javax.swing.JButton btnDodajVozilo;
    private javax.swing.JButton btnFakturisano;
    private javax.swing.JButton btnIzmijeniIzbrisiModel;
    private javax.swing.JButton btnNefakturisano;
    private javax.swing.JButton btnNoviRadniNalog;
    private javax.swing.JButton btnPoDatumu;
    private javax.swing.JButton btnPoIDu;
    private javax.swing.JButton btnPonisti;
    private javax.swing.JButton btnPonistiSve;
    private javax.swing.JButton btnPonistiUnosePretraga;
    private javax.swing.JButton btnPonistiUnoseTermin;
    private javax.swing.JButton btnPredracun;
    private javax.swing.JButton btnPregled;
    private javax.swing.JButton btnPregledGrafik;
    private javax.swing.JButton btnPretrazi;
    private javax.swing.JButton btnPrikaziPredracune;
    private javax.swing.JButton btnPrikaziRadniNalog;
    private javax.swing.JButton btnPrikaziSvaVozila;
    private javax.swing.JButton btnPrikaziSve;
    private javax.swing.JButton btnPrikaziSvePredracune;
    private javax.swing.JButton btnPrikazii;
    private javax.swing.JButton btnProdani;
    private javax.swing.JButton btnPronadjiTermin;
    private javax.swing.JButton btnPronadjiVozilo;
    private javax.swing.JButton btnRacun;
    private javax.swing.JButton btnSviDijelovi;
    private javax.swing.JButton btnTrazi;
    private javax.swing.JButton buttonDodajZaposlenog;
    private javax.swing.JButton buttonPrikazSvihBivsihRadnika;
    private javax.swing.JButton buttonTraziRadneNalogeRadnika;
    private javax.swing.JCheckBox cbDatumOtvaranja;
    private javax.swing.JCheckBox cbDatumZatvaranja;
    private javax.swing.JComboBox<String> cbGodina;
    private javax.swing.JComboBox<String> cbGorivo;
    private javax.swing.JComboBox<String> cbMarka;
    private javax.swing.JComboBox<String> cbMjesec;
    private javax.swing.JComboBox<String> cbModel;
    private javax.swing.JCheckBox cbNovo;
    private javax.swing.JCheckBox cbPotrebnoZavrsiti;
    private javax.swing.JCheckBox cbStanje;
    private javax.swing.JCheckBox cbSvi;
    private javax.swing.JCheckBox cbSviRadniNalog;
    private javax.swing.JPanel danasnjeAktivnostiPanel;
    private com.toedter.calendar.JDateChooser dateChooserAktivnosti1;
    private com.toedter.calendar.JDateChooser dateChooserAktivnosti2;
    private com.toedter.calendar.JDateChooser dateChooserDatumDoZaposlenog;
    private com.toedter.calendar.JDateChooser dateChooserDatumOdZaposlenog;
    private com.toedter.calendar.JDateChooser dateChooserDatumPrimanjaURadniOdnos;
    private com.toedter.calendar.JDateChooser dateChooserDatumRodjenja;
    private javax.swing.JLabel datumLabel;
    private com.toedter.calendar.JDateChooser dcDatumDo;
    private com.toedter.calendar.JDateChooser dcDatumOd;
    private com.toedter.calendar.JDateChooser dcDatumOtvaranjaDO;
    private com.toedter.calendar.JDateChooser dcDatumOtvaranjaOD;
    private com.toedter.calendar.JDateChooser dcDatumZatvaranjaDO;
    private com.toedter.calendar.JDateChooser dcDatumZatvaranjaOD;
    private com.toedter.calendar.JDateChooser dcPotrebnoZavrsitiDO;
    private com.toedter.calendar.JDateChooser dcPotrebnoZavrsitiOD;
    private javax.swing.JPanel dodajDioPanel;
    private com.toedter.calendar.JDateChooser dtmDatum;
    private com.toedter.calendar.JDateChooser dtmDatumPretraga;
    private com.toedter.calendar.JDateChooser dtmDatumTermina;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
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
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
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
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JTable jTable;
    public static javax.swing.JTable jTableZaposleni;
    private javax.swing.JComboBox<String> jcbGorivo;
    private javax.swing.JComboBox<String> jcbMarka;
    private javax.swing.JComboBox<String> jcbModel;
    private javax.swing.JRadioButton jrbPravnoo;
    private javax.swing.JRadioButton jrbPrivatno;
    private javax.swing.JTextField jtfCijena;
    private javax.swing.JTextField jtfGodiste;
    private javax.swing.JTextField jtfKolicina;
    private javax.swing.JTextField jtfNaziv;
    private javax.swing.JTextField jtfSifra;
    private javax.swing.JLabel labelBaza;
    private javax.swing.JLabel labelBrojRadnihNaloga;
    private javax.swing.JLabel labelOstvareniProfitRadnika;
    private javax.swing.JLabel labelPoruka;
    private javax.swing.JLabel lbRadniNaloziNaslov;
    private javax.swing.JLabel lbRadniNaloziSlika;
    private javax.swing.JLabel lblBrojFaktura;
    private javax.swing.JLabel lblBrojNeplacenihFaktura;
    private javax.swing.JLabel lblBrojPlacenihFaktura;
    private javax.swing.JLabel lblDatumOtvaranja;
    private javax.swing.JLabel lblDatumOtvaranjaDO;
    private javax.swing.JLabel lblDatumOtvaranjaOD;
    private javax.swing.JLabel lblDatumZatvaranja;
    private javax.swing.JLabel lblDatumZatvaranjaDO;
    private javax.swing.JLabel lblDatumZatvaranjaOD;
    private javax.swing.JLabel lblDnevnaZarada;
    private javax.swing.JLabel lblDnevnaZaradaDijelovi;
    private javax.swing.JLabel lblGodisnjaZarada;
    private javax.swing.JLabel lblGodisnjaZaradaDijelovi;
    private javax.swing.JLabel lblGodisteVozila;
    private javax.swing.JLabel lblImeRN;
    private javax.swing.JLabel lblIntervalZarada;
    private javax.swing.JLabel lblIntervalZaradaDijelovi;
    private javax.swing.JLabel lblMarkaVozila;
    private javax.swing.JLabel lblMjesecnaZarada;
    private javax.swing.JLabel lblMjesecnaZaradaDijelovi;
    private javax.swing.JLabel lblNaslovVozila;
    private javax.swing.JLabel lblNazivRN;
    private javax.swing.JLabel lblPopravkeDanas;
    private javax.swing.JLabel lblPopravkeGodina;
    private javax.swing.JLabel lblPopravkeInterval;
    private javax.swing.JLabel lblPopravkeMjesec;
    private javax.swing.JLabel lblPotrebnoZavrsiti;
    private javax.swing.JLabel lblPotrebnoZavrsitiDO;
    private javax.swing.JLabel lblPotrebnoZavrsitiOD;
    private javax.swing.JLabel lblPrezimeRN;
    private javax.swing.JLabel lblPrezimeVozila;
    private javax.swing.JLabel lblRadniNalozi;
    private javax.swing.JLabel lblRegistracijaVozila;
    private javax.swing.JLabel lblSlikaVozila;
    private javax.swing.JLabel lblVlasnik;
    private javax.swing.JMenuItem menuItemDetaljniOpis;
    private javax.swing.JMenuItem menuItemIzmjeniRadnika;
    private javax.swing.JMenuItem menuItemOtpustiRadnika;
    private javax.swing.JPanel panelOsnovniPretragaRN;
    private javax.swing.JPanel panelOsnovniRN;
    private javax.swing.JPanel panelPretraga;
    private javax.swing.JPanel pnlBrojPopravki;
    private javax.swing.JPanel pnlDijelovi;
    private javax.swing.JPanel pnlFakture;
    private javax.swing.JPanel pnlGrafikAuta;
    private javax.swing.JPanel pnlGrafikFakture;
    private javax.swing.JPanel pnlGrafikPopravke;
    private javax.swing.JPanel pnlGrafikPrihodiDijelovi;
    private javax.swing.JPanel pnlGrafikPrihodiUkupno;
    private javax.swing.JPanel pnlHeaderStatistika;
    private javax.swing.JPanel pnlKnjigovodstvo;
    private javax.swing.JPanel pnlKontrolnaTablaStatistika;
    private javax.swing.JPanel pnlMeni;
    private javax.swing.JPanel pnlMeniDijelovi;
    private javax.swing.JPanel pnlMeniKnjigovodstvo;
    private javax.swing.JPanel pnlMeniPocetnaStrana;
    private javax.swing.JPanel pnlMeniRadniNalozi;
    private javax.swing.JPanel pnlMeniStatistika;
    private javax.swing.JPanel pnlMeniVozila;
    private javax.swing.JPanel pnlMeniZakazivanja;
    private javax.swing.JPanel pnlMeniZaposleni;
    private javax.swing.JPanel pnlNaloziKnjigovodstvo;
    private javax.swing.JPanel pnlOdabirIntervala;
    private javax.swing.JPanel pnlOdabirMjesecaStatistika;
    private javax.swing.JPanel pnlOkvirKnjigovodstvo;
    private javax.swing.JPanel pnlOkvirZakazivanja;
    private javax.swing.JPanel pnlOsnovniKnjigovodstvo;
    private javax.swing.JPanel pnlOsnovniZakazivanja;
    private javax.swing.JPanel pnlParent;
    private javax.swing.JPanel pnlPocetna;
    private javax.swing.JPanel pnlPronadjiVlasnika;
    private javax.swing.JPanel pnlPronadjiZakazivanja;
    private javax.swing.JPanel pnlRadniNalozi;
    private javax.swing.JScrollPane pnlRadniNaloziKnjigovodstvo;
    private javax.swing.JPanel pnlSlika;
    private javax.swing.JPanel pnlStatistika;
    private javax.swing.JScrollPane pnlTabelaKnjigovodstvo;
    private javax.swing.JScrollPane pnlTabelaZakazivanje;
    private javax.swing.JPanel pnlVozila;
    private javax.swing.JPanel pnlVozilo;
    private javax.swing.JPanel pnlZakaziZakazivanja;
    private javax.swing.JPanel pnlZakazivanja;
    private javax.swing.JPanel pnlZaposleni;
    private javax.swing.JPanel pnlZaradaDijelovi;
    private javax.swing.JPanel pnlZaradaUkupno;
    private javax.swing.JPanel pnllAkcijeNaFormi;
    private javax.swing.JPanel pnllPronadjiVozilo;
    private javax.swing.JPopupMenu popupMenuZaposleni;
    private javax.swing.JPanel pretraziPanel;
    private javax.swing.JRadioButton rbPravnoLiceVozilo;
    private javax.swing.JRadioButton rbPravnoRadniNalog;
    private javax.swing.JRadioButton rbPravnoTrazi;
    private javax.swing.JRadioButton rbPrivatnoLiceVozilo;
    private javax.swing.JRadioButton rbPrivatnoRadniNalog;
    private javax.swing.JRadioButton rbPrivatnoTrazi;
    private javax.swing.JSeparator separatorRN;
    private javax.swing.JScrollPane spVoziloPretraga;
    private javax.swing.JScrollPane spanelTabela;
    private javax.swing.JTable tableAktivnosti;
    private javax.swing.JTable tableRNalozi;
    public static javax.swing.JTable tableRadniNalozi;
    private javax.swing.JTable tableVozila;
    private javax.swing.JTabbedPane tabpnlGrafici;
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
    private javax.swing.JTextField tfId;
    private javax.swing.JTextField tfImeRadniNalog;
    private javax.swing.JTextField tfImee;
    private javax.swing.JTextField tfNaziv;
    private javax.swing.JTextField tfNazivRadniNalog;
    private javax.swing.JTextField tfNazivv;
    private javax.swing.JTextField tfPrezimeRadniNalog;
    private javax.swing.JTextField tfPrezimee;
    private javax.swing.JTextField tfRegistracijaRadniNalog;
    private javax.swing.JTextField tfSifra;
    private javax.swing.JTextField txtBezPDV;
    private javax.swing.JTextField txtBrojTelefonaPretraga;
    private javax.swing.JTextField txtBrojTelefonaTermina;
    private javax.swing.JTextField txtGodisteTrazi;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtImePretraga;
    private javax.swing.JTextField txtImeTermina;
    private javax.swing.JTextField txtImeTrazi;
    private javax.swing.JTextField txtImeVozilo;
    private javax.swing.JLabel txtKnjigovodstvo;
    private javax.swing.JTextField txtMarkaPretraga;
    private javax.swing.JTextField txtMarkaTermina;
    private javax.swing.JTextField txtMarkaTrazi;
    private javax.swing.JTextField txtModelTermina;
    private javax.swing.JTextField txtModelTrazi;
    private javax.swing.JTextField txtNazivTrazi;
    private javax.swing.JTextField txtNazivVozilo;
    private javax.swing.JTextField txtPDV;
    private javax.swing.JTextField txtPrezimePretraga;
    private javax.swing.JTextField txtPrezimeTermina;
    private javax.swing.JTextField txtPrezimeTrazi;
    private javax.swing.JTextField txtPrezimeVozilo;
    private javax.swing.JLabel txtPronadjiZakazivanja;
    private javax.swing.JTextField txtRegistracijaTrazi;
    private javax.swing.JLabel txtSlikaKnjigovodstvo;
    private javax.swing.JLabel txtSlikaZakazivanja;
    private javax.swing.JTextField txtUkupno;
    private javax.swing.JSpinner txtVrijemeTerminaMinuti;
    private javax.swing.JSpinner txtVrijemeTerminaSati;
    private javax.swing.JLabel txtZakaziZakazivanja;
    private javax.swing.JLabel txtZakazivanja;
    // End of variables declaration//GEN-END:variables
}
