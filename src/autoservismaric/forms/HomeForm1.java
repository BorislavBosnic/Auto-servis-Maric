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
import autoservismaric.dialog.SviBivsiZaposleniDialog;
import com.toedter.calendar.JDateChooser;
import data.AutoSuggestor;
import data.dao.*;
import data.dto.DioDTO;
import data.dto.KupacDTO;
import data.dto.ModelVozilaDTO;
import java.io.*;
import java.awt.Color;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import poslovnalogika.DioLogika;
import poslovnalogika.KnjigovodstvoLogika;
import static poslovnalogika.KnjigovodstvoLogika.prikaziFakturu;
import static poslovnalogika.KnjigovodstvoLogika.stavkeSaNalogaZaTabelu;
import poslovnalogika.ModelVozilaLogika;
import poslovnalogika.PocetnaLogika;
import poslovnalogika.ZaposleniLogika;
import poslovnalogika.StatistikaLogika;
import poslovnalogika.Usluge;
import static poslovnalogika.Usluge.btnDodajTerminAkcija;
import static poslovnalogika.Usluge.btnFakturisanoAkcija;
import static poslovnalogika.Usluge.btnNefakturisanoAkcija;
import static poslovnalogika.Usluge.btnPoDatumuAkcija;
import static poslovnalogika.Usluge.btnPoIDuAkcija;
import static poslovnalogika.Usluge.btnPonistiUnosePretragaAkcija;
import static poslovnalogika.Usluge.btnPonistiUnoseTerminAkcija;
import static poslovnalogika.Usluge.btnPrikaziSvePredracuneAkcija;
import static poslovnalogika.Usluge.btnPronadjiTerminAkcija;
import static poslovnalogika.Usluge.btnSviNaloziAkcija;
import static poslovnalogika.Usluge.dodajPopupMeniPretragaRadniNaloga;
import static poslovnalogika.Usluge.inicijalizacijaTabeleNeplacenihFaktura;
import static poslovnalogika.Usluge.provjeraZaDugmiceZaTabeluNaloga;
import static poslovnalogika.Usluge.uslugeKnjigovodstva;
import static poslovnalogika.Usluge.uslugeZakazivanja;
import poslovnalogika.VoziloKupacMeniLogika;

/**
 *
 * @author HP BOOK
 */
public class HomeForm1 extends javax.swing.JFrame {

    boolean flagVoziloVlasnik = true;

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
    public JPopupMenu getPopupPredracuni() {
        return popupPredracuni;
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
    JPopupMenu popupPredracuni = new JPopupMenu();
    public static int selRedDio;// ua popup dio
    //public static Double pdv = 17.0;
    public int idVlasnika = -1;
    public static int selektovanRed; //za popup
    public static int idVozila = -1; //za popup
    public static int akcijaZaRefreshVozila = 0;
    
    

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
    public void setPopupPredracuni(JPopupMenu popupPredracuni) {
        this.popupPredracuni = popupPredracuni;
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
    public void ucitajPopupZaPredracune(){
        pocetnaLogika.ucitajPopupZaPredracune();
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
        Usluge.inicijalisuciKod(this);
        //za slucaj da je dosla nova godina, automatski se dodaju nove vrijednosti u comboBoxGodina
        if(((trenutnaGodina-1)+"").equals(cbGodina.getItemAt(1))){
           // System.out.println(comboBoxGodina.getItemAt(1)+"");
            cbGodina.insertItemAt(Calendar.getInstance().get(Calendar.YEAR)+"",1);
       
        }
        statistikaLogika = new StatistikaLogika();
        btnPrikaziSvaVozila.doClick();
        btnPrikaziRadniNalog.doClick();
        dodajPopupMeniPretragaRadniNaloga(this);
        
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
        ucitajPopupZaPredracune();

        tfNazivPocetna.setBackground(Color.gray);
        tfNazivPocetna.setEditable(false);
        
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

        lblAktivnostiOdDo.setText("Aktivnosti: " + new SimpleDateFormat("dd.MM.yyyy.").format(Calendar.getInstance().getTime()));

        //mjesec dana ranije
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH,-1);
        dcAktivnosti1.setCalendar(cal);
        dcAktivnosti2.setCalendar(Calendar.getInstance());
        btnPrikazii.doClick();
        lblAktivnostiOdDo.setText("Aktivnosti: " + new SimpleDateFormat("dd.MM.yyyy").format((java.util.Date) dcAktivnosti1.getDate()));

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("baza.ser"));
            java.util.Date dat = (java.util.Date) ois.readObject();
            lblBaza.setText(new SimpleDateFormat("dd.MM.yyyy.").format(dat));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        inicijalizujZaposleniPanel();
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
    private void initComponents() {

        popupMenuZaposleni = new javax.swing.JPopupMenu();
        mnItDetaljniOpis = new javax.swing.JMenuItem();
        mnItIzmjeniRadnika = new javax.swing.JMenuItem();
        mnItOtpustiRadnika = new javax.swing.JMenuItem();
        popUpMenuRadniNaloziZaposlenog = new javax.swing.JPopupMenu();
        menuItemOpisRadnogNaloga = new javax.swing.JMenuItem();
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
        pnlOsnovnoPocetna = new javax.swing.JPanel();
        pnlPocetnaMenu = new javax.swing.JPanel();
        pnlPregledAktivnosti = new javax.swing.JPanel();
        lblDatumDo = new javax.swing.JLabel();
        lblDatumOd = new javax.swing.JLabel();
        dcAktivnosti1 = new com.toedter.calendar.JDateChooser();
        dcAktivnosti2 = new com.toedter.calendar.JDateChooser();
        btnPrikazii = new javax.swing.JButton();
        lblPregledAktivnostiPocetna = new javax.swing.JLabel();
        pnlPretragaPredracuna = new javax.swing.JPanel();
        btnPrikaziSvePredracune = new javax.swing.JButton();
        btnPrikaziPredracune = new javax.swing.JButton();
        lblVlasnikPocetna = new javax.swing.JLabel();
        rbPrivatnoPocetna = new javax.swing.JRadioButton();
        rbPravnoPocetna = new javax.swing.JRadioButton();
        lblNazivPocetna = new javax.swing.JLabel();
        lblImePocetna = new javax.swing.JLabel();
        tfNazivPocetna = new javax.swing.JTextField();
        tfImePocetna = new javax.swing.JTextField();
        lblPrezimePocetna = new javax.swing.JLabel();
        tfPrezimePocetna = new javax.swing.JTextField();
        pnlCuvanjePodataka = new javax.swing.JPanel();
        btnBaza = new javax.swing.JButton();
        lblPoslednjiPut = new javax.swing.JLabel();
        lblBaza = new javax.swing.JLabel();
        lblPretragaPredracunaPocetna = new javax.swing.JLabel();
        lblCuvanjePodatakaPocetna = new javax.swing.JLabel();
        lblDatt = new javax.swing.JLabel();
        lblDat2 = new javax.swing.JLabel();
        Napomene2 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblNeplaceneFakture = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        danasnjeAktivnostiPanel = new javax.swing.JPanel();
        lblAktivnostiOdDo = new javax.swing.JLabel();
        jScrollAktivnosti = new javax.swing.JScrollPane();
        tblAktivnosti = new javax.swing.JTable();
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
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        spVoziloPretraga = new javax.swing.JScrollPane();
        tableVozila = new javax.swing.JTable();
        pnlDijelovi = new javax.swing.JPanel();
        pnlOsnovnoDijelovi = new javax.swing.JPanel();
        pnlDijeloviMenu = new javax.swing.JPanel();
        lblPronadjiDio = new javax.swing.JLabel();
        pretraziPanel = new javax.swing.JPanel();
        lblSifraDio = new javax.swing.JLabel();
        lblNazivDio = new javax.swing.JLabel();
        lblGorivoDio = new javax.swing.JLabel();
        cbMarka = new javax.swing.JComboBox<>();
        lblIdDio = new javax.swing.JLabel();
        lblMarkaDio = new javax.swing.JLabel();
        cbNovo = new javax.swing.JCheckBox();
        lblStanjeDio = new javax.swing.JLabel();
        lblGodisteDio = new javax.swing.JLabel();
        cbModel = new javax.swing.JComboBox<>();
        lblModelDio = new javax.swing.JLabel();
        cbGorivo = new javax.swing.JComboBox<>();
        btnPretrazi = new javax.swing.JButton();
        btnSviDijelovi = new javax.swing.JButton();
        btnProdani = new javax.swing.JButton();
        tfId = new javax.swing.JTextField();
        tfSifra = new javax.swing.JTextField();
        tfNaziv = new javax.swing.JTextField();
        tfGodiste = new javax.swing.JTextField();
        dodajDioPanel = new javax.swing.JPanel();
        lblSifraDodajDio = new javax.swing.JLabel();
        lblNazivDodajDio = new javax.swing.JLabel();
        lblCijenaDodajDio = new javax.swing.JLabel();
        cbStanje = new javax.swing.JCheckBox();
        lblStanjeDodajDio = new javax.swing.JLabel();
        cbGorivoDio = new javax.swing.JComboBox<>();
        lblGorivoDodajDio = new javax.swing.JLabel();
        cbMarkaDio = new javax.swing.JComboBox<>();
        lblMarkaDodajDio = new javax.swing.JLabel();
        lblGodisteDodajDio = new javax.swing.JLabel();
        cbModelDio = new javax.swing.JComboBox<>();
        lblModelDodajDio = new javax.swing.JLabel();
        btnDodaj = new javax.swing.JButton();
        lblKolicinaDodajDio = new javax.swing.JLabel();
        tfSifraDio = new javax.swing.JTextField();
        tfNazivDio = new javax.swing.JTextField();
        tfCijenaDio = new javax.swing.JTextField();
        tfGodisteDio = new javax.swing.JTextField();
        tfKolicinaDio = new javax.swing.JTextField();
        btnDodajModel = new javax.swing.JButton();
        lblDodajDio = new javax.swing.JLabel();
        lblDijelovi = new javax.swing.JLabel();
        lblDijeloviZnak = new javax.swing.JLabel();
        jScrollTabelaDijelovi = new javax.swing.JScrollPane();
        tblDijelovi = new javax.swing.JTable();
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
        btnSviNalozi = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtKnjigovodstvo = new javax.swing.JLabel();
        txtSlikaKnjigovodstvo = new javax.swing.JLabel();
        pnlZaposleni = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel117 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tbZaposleni = new javax.swing.JTable();
        lbBrojRadnihNaloga = new javax.swing.JLabel();
        jSeparator26 = new javax.swing.JSeparator();
        jLabel122 = new javax.swing.JLabel();
        btnTraziRadneNalogeRadnika = new javax.swing.JButton();
        lbOstvareniProfitRadnika = new javax.swing.JLabel();
        dateChooserDatumOdZaposlenog = new com.toedter.calendar.JDateChooser();
        dateChooserDatumDoZaposlenog = new com.toedter.calendar.JDateChooser();
        btnPrikazSvihBivsihZaposlenih = new javax.swing.JButton();
        btnDodajZaposlenog = new javax.swing.JButton();
        jLabel148 = new javax.swing.JLabel();
        jLabel149 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tbRadniNalozi = new javax.swing.JTable();
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

        mnItDetaljniOpis.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mnItDetaljniOpis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/document.png"))); // NOI18N
        mnItDetaljniOpis.setText("Detaljan opis");
        mnItDetaljniOpis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnItDetaljniOpisActionPerformed(evt);
            }
        });
        popupMenuZaposleni.add(mnItDetaljniOpis);

        mnItIzmjeniRadnika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Settings_32px_3.png"))); // NOI18N
        mnItIzmjeniRadnika.setText("Izmjeni radnika");
        mnItIzmjeniRadnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnItIzmjeniRadnikaActionPerformed(evt);
            }
        });
        popupMenuZaposleni.add(mnItIzmjeniRadnika);

        mnItOtpustiRadnika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/icon.png"))); // NOI18N
        mnItOtpustiRadnika.setText("Otpusti radnika");
        mnItOtpustiRadnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnItOtpustiRadnikaActionPerformed(evt);
            }
        });
        popupMenuZaposleni.add(mnItOtpustiRadnika);

        menuItemOpisRadnogNaloga.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/document.png"))); // NOI18N
        menuItemOpisRadnogNaloga.setText("Opis radnog naloga");
        menuItemOpisRadnogNaloga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemOpisRadnogNalogaActionPerformed(evt);
            }
        });
        popUpMenuRadniNaloziZaposlenog.add(menuItemOpisRadnogNaloga);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AUTO-SERVIS");

        pnlMeni.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeni.setMaximumSize(new java.awt.Dimension(266, 800));
        pnlMeni.setLayout(null);
        pnlMeni.add(jLabel1);
        jLabel1.setBounds(10, 408, 0, 33);

        pnlMeniRadniNalozi.setBackground(new java.awt.Color(51, 51, 255));
        pnlMeniRadniNalozi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlMeniRadniNaloziMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnlMeniRadniNaloziMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnlMeniRadniNaloziMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
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
        pnlMeniVozila.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlMeniVozilaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnlMeniVozilaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
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
        pnlMeniKnjigovodstvo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlMeniKnjigovodstvoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnlMeniKnjigovodstvoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
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
        pnlMeniStatistika.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlMeniStatistikaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnlMeniStatistikaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnlMeniStatistikaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
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
        pnlMeniZaposleni.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlMeniZaposleniMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnlMeniZaposleniMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnlMeniZaposleniMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
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
        pnlMeniDijelovi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlMeniDijeloviMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnlMeniDijeloviMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
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
        jLabel36.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
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
        pnlMeniPocetnaStrana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlMeniPocetnaStranaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnlMeniPocetnaStranaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnlMeniPocetnaStranaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
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
        pnlMeniZakazivanja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlMeniZakazivanjaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnlMeniZakazivanjaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnlMeniZakazivanjaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
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

        pnlOsnovnoPocetna.setBackground(new java.awt.Color(102, 153, 255));

        pnlPocetnaMenu.setBackground(new java.awt.Color(102, 153, 255));
        pnlPocetnaMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        pnlPregledAktivnosti.setBackground(new java.awt.Color(102, 153, 255));
        pnlPregledAktivnosti.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        lblDatumDo.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblDatumDo.setForeground(new java.awt.Color(255, 255, 255));
        lblDatumDo.setText("Datum do:");

        lblDatumOd.setBackground(new java.awt.Color(255, 255, 255));
        lblDatumOd.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblDatumOd.setForeground(new java.awt.Color(255, 255, 255));
        lblDatumOd.setText("Datum od:");

        btnPrikazii.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrikazii.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnPrikazii.setText("Prikaži");
        btnPrikazii.setPreferredSize(new java.awt.Dimension(105, 38));
        btnPrikazii.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrikaziiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPregledAktivnostiLayout = new javax.swing.GroupLayout(pnlPregledAktivnosti);
        pnlPregledAktivnosti.setLayout(pnlPregledAktivnostiLayout);
        pnlPregledAktivnostiLayout.setHorizontalGroup(
            pnlPregledAktivnostiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPregledAktivnostiLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(pnlPregledAktivnostiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPrikazii, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlPregledAktivnostiLayout.createSequentialGroup()
                        .addGroup(pnlPregledAktivnostiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblDatumOd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDatumDo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlPregledAktivnostiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dcAktivnosti2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dcAktivnosti1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        pnlPregledAktivnostiLayout.setVerticalGroup(
            pnlPregledAktivnostiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPregledAktivnostiLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(pnlPregledAktivnostiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dcAktivnosti1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDatumOd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(pnlPregledAktivnostiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dcAktivnosti2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDatumDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnPrikazii, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lblPregledAktivnostiPocetna.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblPregledAktivnostiPocetna.setForeground(new java.awt.Color(255, 255, 255));
        lblPregledAktivnostiPocetna.setText("Pregled aktivnosti:");
        lblPregledAktivnostiPocetna.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                lblPregledAktivnostiPocetnaPropertyChange(evt);
            }
        });

        pnlPretragaPredracuna.setBackground(new java.awt.Color(102, 153, 255));
        pnlPretragaPredracuna.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));

        btnPrikaziSvePredracune.setText("Prikaži sve");
        btnPrikaziSvePredracune.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrikaziSvePredracuneActionPerformed(evt);
            }
        });

        btnPrikaziPredracune.setText("Prikaži");
        btnPrikaziPredracune.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrikaziPredracuneActionPerformed(evt);
            }
        });

        lblVlasnikPocetna.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblVlasnikPocetna.setForeground(new java.awt.Color(240, 240, 240));
        lblVlasnikPocetna.setText("Vlasnik:");

        rbPrivatnoPocetna.setForeground(new java.awt.Color(240, 240, 240));
        rbPrivatnoPocetna.setSelected(true);
        rbPrivatnoPocetna.setText("Privatno lice");
        rbPrivatnoPocetna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPrivatnoPocetnaActionPerformed(evt);
            }
        });

        rbPravnoPocetna.setForeground(new java.awt.Color(240, 240, 240));
        rbPravnoPocetna.setText("Pravno lice");
        rbPravnoPocetna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPravnoPocetnaActionPerformed(evt);
            }
        });

        lblNazivPocetna.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNazivPocetna.setForeground(new java.awt.Color(240, 240, 240));
        lblNazivPocetna.setText("Naziv:");

        lblImePocetna.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblImePocetna.setForeground(new java.awt.Color(240, 240, 240));
        lblImePocetna.setText("Ime:");

        lblPrezimePocetna.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblPrezimePocetna.setForeground(new java.awt.Color(240, 240, 240));
        lblPrezimePocetna.setText("Prezime:");

        javax.swing.GroupLayout pnlPretragaPredracunaLayout = new javax.swing.GroupLayout(pnlPretragaPredracuna);
        pnlPretragaPredracuna.setLayout(pnlPretragaPredracunaLayout);
        pnlPretragaPredracunaLayout.setHorizontalGroup(
            pnlPretragaPredracunaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPretragaPredracunaLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlPretragaPredracunaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVlasnikPocetna)
                    .addGroup(pnlPretragaPredracunaLayout.createSequentialGroup()
                        .addGroup(pnlPretragaPredracunaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblImePocetna, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNazivPocetna, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlPretragaPredracunaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfNazivPocetna, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlPretragaPredracunaLayout.createSequentialGroup()
                                .addGroup(pnlPretragaPredracunaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(pnlPretragaPredracunaLayout.createSequentialGroup()
                                        .addComponent(btnPrikaziSvePredracune)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPrikaziPredracune, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPretragaPredracunaLayout.createSequentialGroup()
                                        .addComponent(rbPrivatnoPocetna, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(rbPravnoPocetna, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPretragaPredracunaLayout.createSequentialGroup()
                                        .addComponent(tfImePocetna, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblPrezimePocetna)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfPrezimePocetna, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        pnlPretragaPredracunaLayout.setVerticalGroup(
            pnlPretragaPredracunaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPretragaPredracunaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPretragaPredracunaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVlasnikPocetna)
                    .addComponent(rbPrivatnoPocetna)
                    .addComponent(rbPravnoPocetna))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPretragaPredracunaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNazivPocetna)
                    .addComponent(tfNazivPocetna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPretragaPredracunaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfImePocetna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrezimePocetna)
                    .addComponent(tfPrezimePocetna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblImePocetna))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlPretragaPredracunaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrikaziSvePredracune, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrikaziPredracune, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnlCuvanjePodataka.setBackground(new java.awt.Color(102, 153, 255));
        pnlCuvanjePodataka.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));

        btnBaza.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/save.png"))); // NOI18N
        btnBaza.setText("Čuvanje podataka");
        btnBaza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBazaActionPerformed(evt);
            }
        });

        lblPoslednjiPut.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPoslednjiPut.setForeground(new java.awt.Color(240, 240, 240));
        lblPoslednjiPut.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPoslednjiPut.setText("Poslednji put:");

        lblBaza.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblBaza.setForeground(new java.awt.Color(240, 240, 240));
        lblBaza.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlCuvanjePodatakaLayout = new javax.swing.GroupLayout(pnlCuvanjePodataka);
        pnlCuvanjePodataka.setLayout(pnlCuvanjePodatakaLayout);
        pnlCuvanjePodatakaLayout.setHorizontalGroup(
            pnlCuvanjePodatakaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCuvanjePodatakaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCuvanjePodatakaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBaza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPoslednjiPut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCuvanjePodatakaLayout.createSequentialGroup()
                        .addComponent(btnBaza, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 12, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlCuvanjePodatakaLayout.setVerticalGroup(
            pnlCuvanjePodatakaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCuvanjePodatakaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPoslednjiPut)
                .addGap(18, 18, 18)
                .addComponent(lblBaza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19)
                .addComponent(btnBaza)
                .addContainerGap())
        );

        lblPretragaPredracunaPocetna.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblPretragaPredracunaPocetna.setForeground(new java.awt.Color(240, 240, 240));
        lblPretragaPredracunaPocetna.setText("Pretraga predračuna:");

        lblCuvanjePodatakaPocetna.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblCuvanjePodatakaPocetna.setForeground(new java.awt.Color(240, 240, 240));
        lblCuvanjePodatakaPocetna.setText("Čuvanje podataka:");

        javax.swing.GroupLayout pnlPocetnaMenuLayout = new javax.swing.GroupLayout(pnlPocetnaMenu);
        pnlPocetnaMenu.setLayout(pnlPocetnaMenuLayout);
        pnlPocetnaMenuLayout.setHorizontalGroup(
            pnlPocetnaMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPocetnaMenuLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(pnlPocetnaMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPregledAktivnostiPocetna)
                    .addComponent(pnlPregledAktivnosti, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(pnlPocetnaMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlCuvanjePodataka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCuvanjePodatakaPocetna))
                .addGap(42, 42, 42)
                .addGroup(pnlPocetnaMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPretragaPredracunaPocetna)
                    .addComponent(pnlPretragaPredracuna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlPocetnaMenuLayout.setVerticalGroup(
            pnlPocetnaMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPocetnaMenuLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(pnlPocetnaMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPregledAktivnostiPocetna)
                    .addComponent(lblPretragaPredracunaPocetna)
                    .addComponent(lblCuvanjePodatakaPocetna))
                .addGap(10, 10, 10)
                .addGroup(pnlPocetnaMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPregledAktivnosti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlPretragaPredracuna, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCuvanjePodataka, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        lblDatt.setBackground(new java.awt.Color(255, 255, 255));
        lblDatt.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDatt.setForeground(new java.awt.Color(229, 229, 229));
        lblDatt.setText("> Početna strana");

        lblDat2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Home_20px_1.png"))); // NOI18N

        Napomene2.setBackground(new java.awt.Color(102, 153, 255));
        Napomene2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));

        tblNeplaceneFakture.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Automobil", "Vlasnik", "Datum otvaranja", "Datum fakturisanja", "Iznos"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
        if (tblNeplaceneFakture.getColumnModel().getColumnCount() > 0) {
            tblNeplaceneFakture.getColumnModel().getColumn(0).setResizable(false);
            tblNeplaceneFakture.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblNeplaceneFakture.getColumnModel().getColumn(1).setResizable(false);
            tblNeplaceneFakture.getColumnModel().getColumn(1).setPreferredWidth(20);
            tblNeplaceneFakture.getColumnModel().getColumn(2).setResizable(false);
            tblNeplaceneFakture.getColumnModel().getColumn(2).setPreferredWidth(20);
            tblNeplaceneFakture.getColumnModel().getColumn(3).setResizable(false);
            tblNeplaceneFakture.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblNeplaceneFakture.getColumnModel().getColumn(4).setResizable(false);
            tblNeplaceneFakture.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblNeplaceneFakture.getColumnModel().getColumn(5).setResizable(false);
            tblNeplaceneFakture.getColumnModel().getColumn(5).setPreferredWidth(20);
        }
        tblNeplaceneFakture.setAutoCreateRowSorter(true);

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
                    .addComponent(jScrollPane8))
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

        lblAktivnostiOdDo.setBackground(new java.awt.Color(255, 255, 255));
        lblAktivnostiOdDo.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblAktivnostiOdDo.setForeground(new java.awt.Color(240, 240, 240));
        lblAktivnostiOdDo.setText("Aktivnosti: ");

        tblAktivnosti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Vlasnik", "Auto", "Pocetak rada", "Rok"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAktivnosti.getTableHeader().setReorderingAllowed(false);
        jScrollAktivnosti.setViewportView(tblAktivnosti);
        if (tblAktivnosti.getColumnModel().getColumnCount() > 0) {
            tblAktivnosti.getColumnModel().getColumn(0).setResizable(false);
            tblAktivnosti.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblAktivnosti.getColumnModel().getColumn(1).setResizable(false);
            tblAktivnosti.getColumnModel().getColumn(2).setResizable(false);
            tblAktivnosti.getColumnModel().getColumn(3).setResizable(false);
            tblAktivnosti.getColumnModel().getColumn(4).setResizable(false);
        }
        tblAktivnosti.setAutoCreateRowSorter(true);

        javax.swing.GroupLayout danasnjeAktivnostiPanelLayout = new javax.swing.GroupLayout(danasnjeAktivnostiPanel);
        danasnjeAktivnostiPanel.setLayout(danasnjeAktivnostiPanelLayout);
        danasnjeAktivnostiPanelLayout.setHorizontalGroup(
            danasnjeAktivnostiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danasnjeAktivnostiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(danasnjeAktivnostiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(danasnjeAktivnostiPanelLayout.createSequentialGroup()
                        .addComponent(lblAktivnostiOdDo, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(danasnjeAktivnostiPanelLayout.createSequentialGroup()
                        .addComponent(jScrollAktivnosti, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        danasnjeAktivnostiPanelLayout.setVerticalGroup(
            danasnjeAktivnostiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(danasnjeAktivnostiPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblAktivnostiOdDo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollAktivnosti, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlOsnovnoPocetnaLayout = new javax.swing.GroupLayout(pnlOsnovnoPocetna);
        pnlOsnovnoPocetna.setLayout(pnlOsnovnoPocetnaLayout);
        pnlOsnovnoPocetnaLayout.setHorizontalGroup(
            pnlOsnovnoPocetnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOsnovnoPocetnaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOsnovnoPocetnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOsnovnoPocetnaLayout.createSequentialGroup()
                        .addComponent(lblDat2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDatt)
                        .addContainerGap(888, Short.MAX_VALUE))
                    .addGroup(pnlOsnovnoPocetnaLayout.createSequentialGroup()
                        .addComponent(danasnjeAktivnostiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Napomene2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlPocetnaMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlOsnovnoPocetnaLayout.setVerticalGroup(
            pnlOsnovnoPocetnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOsnovnoPocetnaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlOsnovnoPocetnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDatt)
                    .addComponent(lblDat2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(pnlPocetnaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlOsnovnoPocetnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Napomene2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlOsnovnoPocetnaLayout.createSequentialGroup()
                        .addComponent(danasnjeAktivnostiPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        Napomene2.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout pnlPocetnaLayout = new javax.swing.GroupLayout(pnlPocetna);
        pnlPocetna.setLayout(pnlPocetnaLayout);
        pnlPocetnaLayout.setHorizontalGroup(
            pnlPocetnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlOsnovnoPocetna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnlPocetnaLayout.setVerticalGroup(
            pnlPocetnaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPocetnaLayout.createSequentialGroup()
                .addComponent(pnlOsnovnoPocetna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2797, Short.MAX_VALUE))
        );

        pnlParent.add(pnlPocetna, "card2");

        pnlRadniNalozi.setBackground(new java.awt.Color(102, 153, 255));

        panelOsnovniRN.setBackground(new java.awt.Color(102, 153, 255));

        panelOsnovniPretragaRN.setBackground(new java.awt.Color(102, 153, 255));
        panelOsnovniPretragaRN.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        btnNoviRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnNoviRadniNalog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/add-documents.png"))); // NOI18N
        btnNoviRadniNalog.setText("Novi radni nalog");
        btnNoviRadniNalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
        btnPrikaziRadniNalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
        rbPravnoRadniNalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPravnoRadniNalogActionPerformed(evt);
            }
        });

        rbPrivatnoRadniNalog.setBackground(new java.awt.Color(102, 153, 255));
        rbPrivatnoRadniNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPrivatnoRadniNalog.setForeground(new java.awt.Color(255, 255, 255));
        rbPrivatnoRadniNalog.setSelected(true);
        rbPrivatnoRadniNalog.setText("Privatno lice");
        rbPrivatnoRadniNalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
        cbSviRadniNalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                        .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblDatumOtvaranjaDO)
                            .addComponent(lblDatumZatvaranjaDO)
                            .addComponent(lblPotrebnoZavrsitiDO))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPretragaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(dcDatumZatvaranjaOD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(dcPotrebnoZavrsitiDO, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcPotrebnoZavrsitiOD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcDatumOtvaranjaDO, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcDatumOtvaranjaOD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dcDatumZatvaranjaDO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 34, Short.MAX_VALUE)
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
                .addGap(20, 20, 20))
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
                .addContainerGap(143, Short.MAX_VALUE))
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
                .addGroup(panelOsnovniRNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelOsnovniRNLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(lbRadniNaloziSlika, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbRadniNaloziNaslov))
                    .addGroup(panelOsnovniRNLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelOsnovniPretragaRN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelOsnovniRNLayout.setVerticalGroup(
            panelOsnovniRNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelOsnovniRNLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelOsnovniRNLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbRadniNaloziNaslov)
                    .addComponent(lbRadniNaloziSlika, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(panelOsnovniPretragaRN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        spanelTabela.setBackground(new java.awt.Color(102, 153, 255));

        tableRNalozi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Registracija vozila", "Vlasnik vozila", "Datum otvaranja", "Potrebno završiti do", "Datum zatvaranja", "Troškovi dijelova", "Cijena usluge", "Plaćeno"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
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
                .addComponent(spanelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2114, Short.MAX_VALUE))
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

        btnPronadjiTermin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
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

        btnDodajTermin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/clipboard.png"))); // NOI18N
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
        btnPonistiUnoseTermin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
        pnlTabelaZakazivanje.setViewportView(tblTermini);
        tblTermini.setAutoCreateRowSorter(true);

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
                .addGap(10, 10, 10)
                .addComponent(pnlOkvirZakazivanja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2018, 2018, 2018))
        );

        javax.swing.GroupLayout pnlZakazivanjaLayout = new javax.swing.GroupLayout(pnlZakazivanja);
        pnlZakazivanja.setLayout(pnlZakazivanjaLayout);
        pnlZakazivanjaLayout.setHorizontalGroup(
            pnlZakazivanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZakazivanjaLayout.createSequentialGroup()
                .addComponent(pnlOsnovniZakazivanja, javax.swing.GroupLayout.PREFERRED_SIZE, 3042, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 152, Short.MAX_VALUE))
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

        txtModelTrazi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtModelTraziFocusGained(evt);
            }
        });
        txtModelTrazi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModelTraziActionPerformed(evt);
            }
        });

        btnPronadjiVozilo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPronadjiVozilo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnPronadjiVozilo.setText("Traži");
        btnPronadjiVozilo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
        btnPonistiSve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPonistiSveActionPerformed(evt);
            }
        });

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

        btnPrikaziSvaVozila.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrikaziSvaVozila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/list.png"))); // NOI18N
        btnPrikaziSvaVozila.setText("Prikaži sve");
        btnPrikaziSvaVozila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrikaziSvaVozilaActionPerformed(evt);
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

        javax.swing.GroupLayout pnllPronadjiVoziloLayout = new javax.swing.GroupLayout(pnllPronadjiVozilo);
        pnllPronadjiVozilo.setLayout(pnllPronadjiVoziloLayout);
        pnllPronadjiVoziloLayout.setHorizontalGroup(
            pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnllPronadjiVoziloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPrikaziSvaVozila, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRegistracijaVozila)
                            .addComponent(lblGodisteVozila))
                        .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel29)
                    .addComponent(lblPrezimeVozila)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(btnPonistiSve, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
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
                .addComponent(btnPrikaziSvaVozila)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnllPronadjiVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPonistiSve, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPronadjiVozilo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        btnTrazi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
        btnPrikaziSve.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/list.png"))); // NOI18N
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
                        .addGroup(pnlPronadjiVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPrikaziSve, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPonisti, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTrazi)))
                .addContainerGap())
        );

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {rbPravnoTrazi, rbPrivatnoTrazi});

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtImeTrazi, txtNazivTrazi, txtPrezimeTrazi});

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel109, jLabel110, jLabel27});

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
                .addComponent(btnPrikaziSve)
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

        pnlPronadjiVlasnikaLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnPonisti, btnTrazi});

        btnDodajVozilo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnDodajVozilo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/racing (1).png"))); // NOI18N
        btnDodajVozilo.setText("Dodaj Vozilo");
        btnDodajVozilo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajVoziloActionPerformed(evt);
            }
        });

        btnDodajVlasnika.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnDodajVlasnika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/user (2).png"))); // NOI18N
        btnDodajVlasnika.setText("Dodaj Vlasnika");
        btnDodajVlasnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajVlasnikaActionPerformed(evt);
            }
        });

        btnDodajModel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnDodajModel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/cube.png"))); // NOI18N
        btnDodajModel2.setText("Dodaj Model");
        btnDodajModel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajModel2ActionPerformed(evt);
            }
        });

        btnIzmijeniIzbrisiModel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnIzmijeniIzbrisiModel.setText("Izmijeni/Izbriši model");
        btnIzmijeniIzbrisiModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIzmijeniIzbrisiModelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlVoziloLayout = new javax.swing.GroupLayout(pnlVozilo);
        pnlVozilo.setLayout(pnlVoziloLayout);
        pnlVoziloLayout.setHorizontalGroup(
            pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVoziloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel96)
                    .addComponent(pnllPronadjiVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVoziloLayout.createSequentialGroup()
                        .addComponent(pnlPronadjiVlasnika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDodajVozilo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnIzmijeniIzbrisiModel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnDodajVlasnika)
                            .addComponent(btnDodajModel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(358, 358, 358))
                    .addGroup(pnlVoziloLayout.createSequentialGroup()
                        .addComponent(jLabel97)
                        .addGap(210, 210, 210))))
        );
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
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDodajVlasnika, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(btnDodajModel2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIzmijeniIzbrisiModel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
        );

        pnlVoziloLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDodajModel2, btnDodajVlasnika, btnDodajVozilo});

        spVoziloPretraga.setBorder(null);

        tableVozila.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

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
                .addContainerGap(2131, Short.MAX_VALUE))
        );
        pnllAkcijeNaFormiLayout.setVerticalGroup(
            pnllAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnllAkcijeNaFormiLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(pnllAkcijeNaFormiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNaslovVozila)
                    .addComponent(lblSlikaVozila, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(pnlVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spVoziloPretraga, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2876, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlVozilaLayout = new javax.swing.GroupLayout(pnlVozila);
        pnlVozila.setLayout(pnlVozilaLayout);
        pnlVozilaLayout.setHorizontalGroup(
            pnlVozilaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnllAkcijeNaFormi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlVozilaLayout.setVerticalGroup(
            pnlVozilaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnllAkcijeNaFormi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlParent.add(pnlVozila, "card6");

        pnlDijelovi.setBackground(new java.awt.Color(102, 153, 255));
        pnlDijelovi.setNextFocusableComponent(tfId);

        pnlOsnovnoDijelovi.setBackground(new java.awt.Color(102, 153, 255));

        pnlDijeloviMenu.setBackground(new java.awt.Color(102, 153, 255));
        pnlDijeloviMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        lblPronadjiDio.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblPronadjiDio.setForeground(new java.awt.Color(255, 255, 255));
        lblPronadjiDio.setText("Pronadji dio:");

        pretraziPanel.setBackground(new java.awt.Color(102, 153, 255));
        pretraziPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        lblSifraDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblSifraDio.setForeground(new java.awt.Color(255, 255, 255));
        lblSifraDio.setText("Šifra:");
        lblSifraDio.setPreferredSize(new java.awt.Dimension(42, 20));

        lblNazivDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblNazivDio.setForeground(new java.awt.Color(255, 255, 255));
        lblNazivDio.setText("Naziv:");
        lblNazivDio.setPreferredSize(new java.awt.Dimension(48, 20));

        lblGorivoDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblGorivoDio.setForeground(new java.awt.Color(255, 255, 255));
        lblGorivoDio.setText("Gorivo:");

        cbMarka.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbMarka.setMaximumRowCount(50);
        cbMarka.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi" }));
        cbMarka.setMaximumSize(new java.awt.Dimension(81, 20));
        cbMarka.setMinimumSize(new java.awt.Dimension(81, 20));
        cbMarka.setPreferredSize(new java.awt.Dimension(81, 20));
        cbMarka.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbMarkaItemStateChanged(evt);
            }
        });

        lblIdDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblIdDio.setForeground(new java.awt.Color(255, 255, 255));
        lblIdDio.setText("Id:");
        lblIdDio.setPreferredSize(new java.awt.Dimension(60, 20));

        lblMarkaDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblMarkaDio.setForeground(new java.awt.Color(255, 255, 255));
        lblMarkaDio.setText("Marka:");
        lblMarkaDio.setPreferredSize(new java.awt.Dimension(60, 20));

        cbNovo.setText("Novo");
        cbNovo.setMaximumSize(new java.awt.Dimension(81, 20));
        cbNovo.setMinimumSize(new java.awt.Dimension(51, 20));
        cbNovo.setPreferredSize(new java.awt.Dimension(81, 20));

        lblStanjeDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblStanjeDio.setForeground(new java.awt.Color(255, 255, 255));
        lblStanjeDio.setText("Stanje:");
        lblStanjeDio.setPreferredSize(new java.awt.Dimension(60, 20));

        lblGodisteDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblGodisteDio.setForeground(new java.awt.Color(255, 255, 255));
        lblGodisteDio.setText("Godište:");

        cbModel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbModel.setMaximumRowCount(50);
        cbModel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi" }));
        cbModel.setMinimumSize(new java.awt.Dimension(41, 20));
        cbModel.setPreferredSize(new java.awt.Dimension(81, 20));

        lblModelDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblModelDio.setForeground(new java.awt.Color(255, 255, 255));
        lblModelDio.setText("Model:");
        lblModelDio.setPreferredSize(new java.awt.Dimension(60, 20));

        cbGorivo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbGorivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi", "Benzin", "Dizel", "Plin" }));
        cbGorivo.setMinimumSize(new java.awt.Dimension(60, 20));
        cbGorivo.setPreferredSize(new java.awt.Dimension(81, 20));

        btnPretrazi.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPretrazi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnPretrazi.setText("Traži");
        btnPretrazi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPretraziActionPerformed(evt);
            }
        });

        btnSviDijelovi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSviDijelovi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/list.png"))); // NOI18N
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
                                .addComponent(lblIdDio, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblSifraDio, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblNazivDio, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblGodisteDio))
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
                                    .addComponent(lblGorivoDio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblMarkaDio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblModelDio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(pretraziPanelLayout.createSequentialGroup()
                                .addComponent(lblStanjeDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)))
                        .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbModel, 0, 100, Short.MAX_VALUE)
                            .addComponent(cbMarka, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbGorivo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbNovo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10))
                    .addGroup(pretraziPanelLayout.createSequentialGroup()
                        .addComponent(btnSviDijelovi)
                        .addGap(38, 38, 38)
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
                        .addComponent(lblIdDio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblStanjeDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblSifraDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfSifra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbGorivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblGorivoDio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNazivDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfNaziv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbMarka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMarkaDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGodisteDio)
                    .addComponent(cbModel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModelDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfGodiste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pretraziPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSviDijelovi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPretrazi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnProdani, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        dodajDioPanel.setBackground(new java.awt.Color(102, 153, 255));
        dodajDioPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        lblSifraDodajDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblSifraDodajDio.setForeground(new java.awt.Color(255, 255, 255));
        lblSifraDodajDio.setText("Šifra:");
        lblSifraDodajDio.setPreferredSize(new java.awt.Dimension(60, 20));

        lblNazivDodajDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblNazivDodajDio.setForeground(new java.awt.Color(255, 255, 255));
        lblNazivDodajDio.setText("Naziv:");
        lblNazivDodajDio.setPreferredSize(new java.awt.Dimension(42, 20));

        lblCijenaDodajDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblCijenaDodajDio.setForeground(new java.awt.Color(255, 255, 255));
        lblCijenaDodajDio.setText("Cijena(KM):");
        lblCijenaDodajDio.setPreferredSize(new java.awt.Dimension(48, 20));

        cbStanje.setText("Novo");
        cbStanje.setMaximumSize(new java.awt.Dimension(81, 20));
        cbStanje.setMinimumSize(new java.awt.Dimension(51, 20));
        cbStanje.setPreferredSize(new java.awt.Dimension(81, 20));

        lblStanjeDodajDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblStanjeDodajDio.setForeground(new java.awt.Color(255, 255, 255));
        lblStanjeDodajDio.setText("Stanje:");
        lblStanjeDodajDio.setPreferredSize(new java.awt.Dimension(60, 20));

        cbGorivoDio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbGorivoDio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi", "Benzin", "Dizel", "Plin" }));
        cbGorivoDio.setMinimumSize(new java.awt.Dimension(60, 20));
        cbGorivoDio.setPreferredSize(new java.awt.Dimension(81, 20));

        lblGorivoDodajDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblGorivoDodajDio.setForeground(new java.awt.Color(255, 255, 255));
        lblGorivoDodajDio.setText("Gorivo:");

        cbMarkaDio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbMarkaDio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi", "Audi", "BMW", "Mercedes" }));
        cbMarkaDio.setMaximumSize(new java.awt.Dimension(81, 20));
        cbMarkaDio.setMinimumSize(new java.awt.Dimension(81, 20));
        cbMarkaDio.setPreferredSize(new java.awt.Dimension(81, 20));
        cbMarkaDio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbMarkaDioItemStateChanged(evt);
            }
        });

        lblMarkaDodajDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblMarkaDodajDio.setForeground(new java.awt.Color(255, 255, 255));
        lblMarkaDodajDio.setText("Marka:");
        lblMarkaDodajDio.setPreferredSize(new java.awt.Dimension(60, 20));

        lblGodisteDodajDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblGodisteDodajDio.setForeground(new java.awt.Color(255, 255, 255));
        lblGodisteDodajDio.setText("Godina proizvodnje:");

        cbModelDio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbModelDio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Svi" }));
        cbModelDio.setMinimumSize(new java.awt.Dimension(41, 20));
        cbModelDio.setPreferredSize(new java.awt.Dimension(81, 20));

        lblModelDodajDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblModelDodajDio.setForeground(new java.awt.Color(255, 255, 255));
        lblModelDodajDio.setText("Model:");
        lblModelDodajDio.setPreferredSize(new java.awt.Dimension(60, 20));

        btnDodaj.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDodaj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/add (1).png"))); // NOI18N
        btnDodaj.setText("DODAJ");
        btnDodaj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajActionPerformed(evt);
            }
        });

        lblKolicinaDodajDio.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lblKolicinaDodajDio.setForeground(new java.awt.Color(255, 255, 255));
        lblKolicinaDodajDio.setText("Količina:");

        btnDodajModel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDodajModel.setText("Dodaj model");
        btnDodajModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajModelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dodajDioPanelLayout = new javax.swing.GroupLayout(dodajDioPanel);
        dodajDioPanel.setLayout(dodajDioPanelLayout);
        dodajDioPanelLayout.setHorizontalGroup(
            dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dodajDioPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCijenaDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblKolicinaDodajDio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblSifraDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNazivDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lblGodisteDodajDio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfSifraDio)
                    .addComponent(tfNazivDio)
                    .addComponent(tfCijenaDio)
                    .addComponent(tfGodisteDio)
                    .addComponent(tfKolicinaDio, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dodajDioPanelLayout.createSequentialGroup()
                        .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStanjeDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMarkaDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbStanje, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(cbGorivoDio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbMarkaDio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(dodajDioPanelLayout.createSequentialGroup()
                        .addComponent(btnDodaj)
                        .addGap(18, 18, 18)
                        .addComponent(btnDodajModel))
                    .addGroup(dodajDioPanelLayout.createSequentialGroup()
                        .addComponent(lblModelDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbModelDio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblGorivoDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dodajDioPanelLayout.setVerticalGroup(
            dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dodajDioPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSifraDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStanjeDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfSifraDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbStanje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNazivDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfNazivDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGorivoDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbGorivoDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCijenaDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfCijenaDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMarkaDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbMarkaDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGodisteDodajDio)
                    .addComponent(cbModelDio, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModelDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfGodisteDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblKolicinaDodajDio)
                        .addComponent(tfKolicinaDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dodajDioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDodajModel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        dodajDioPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDodaj, btnDodajModel});

        lblDodajDio.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblDodajDio.setForeground(new java.awt.Color(255, 255, 255));
        lblDodajDio.setText("Dodaj dio");

        javax.swing.GroupLayout pnlDijeloviMenuLayout = new javax.swing.GroupLayout(pnlDijeloviMenu);
        pnlDijeloviMenu.setLayout(pnlDijeloviMenuLayout);
        pnlDijeloviMenuLayout.setHorizontalGroup(
            pnlDijeloviMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDijeloviMenuLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pnlDijeloviMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPronadjiDio)
                    .addComponent(pretraziPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDijeloviMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDijeloviMenuLayout.createSequentialGroup()
                        .addComponent(lblDodajDio)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(dodajDioPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlDijeloviMenuLayout.setVerticalGroup(
            pnlDijeloviMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDijeloviMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDijeloviMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPronadjiDio)
                    .addComponent(lblDodajDio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDijeloviMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dodajDioPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pretraziPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lblDijelovi.setBackground(new java.awt.Color(255, 255, 255));
        lblDijelovi.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblDijelovi.setForeground(new java.awt.Color(229, 229, 229));
        lblDijelovi.setText(">Dijelovi");

        lblDijeloviZnak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/Settings_20px_1.png"))); // NOI18N

        javax.swing.GroupLayout pnlOsnovnoDijeloviLayout = new javax.swing.GroupLayout(pnlOsnovnoDijelovi);
        pnlOsnovnoDijelovi.setLayout(pnlOsnovnoDijeloviLayout);
        pnlOsnovnoDijeloviLayout.setHorizontalGroup(
            pnlOsnovnoDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOsnovnoDijeloviLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOsnovnoDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDijeloviMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlOsnovnoDijeloviLayout.createSequentialGroup()
                        .addComponent(lblDijeloviZnak, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(lblDijelovi)))
                .addContainerGap(2101, Short.MAX_VALUE))
        );
        pnlOsnovnoDijeloviLayout.setVerticalGroup(
            pnlOsnovnoDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOsnovnoDijeloviLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOsnovnoDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDijelovi)
                    .addComponent(lblDijeloviZnak, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(pnlDijeloviMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tblDijelovi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tblDijelovi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Šifra", "Naziv", "Marka", "Model", "Godište", "Vrsta goriva", "Cijena(KM)", "Količina", "Novo"
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
        tblDijelovi.getTableHeader().setReorderingAllowed(false);
        tblDijelovi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDijeloviMouseClicked(evt);
            }
        });
        jScrollTabelaDijelovi.setViewportView(tblDijelovi);
        tblDijelovi.setAutoCreateRowSorter(true);
        if (tblDijelovi.getColumnModel().getColumnCount() > 0) {
            tblDijelovi.getColumnModel().getColumn(0).setPreferredWidth(15);
        }

        javax.swing.GroupLayout pnlDijeloviLayout = new javax.swing.GroupLayout(pnlDijelovi);
        pnlDijelovi.setLayout(pnlDijeloviLayout);
        pnlDijeloviLayout.setHorizontalGroup(
            pnlDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlOsnovnoDijelovi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlDijeloviLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollTabelaDijelovi, javax.swing.GroupLayout.PREFERRED_SIZE, 1075, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDijeloviLayout.setVerticalGroup(
            pnlDijeloviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDijeloviLayout.createSequentialGroup()
                .addComponent(pnlOsnovnoDijelovi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollTabelaDijelovi, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2828, Short.MAX_VALUE))
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
        btnPredracun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/receipt.png"))); // NOI18N
        btnPredracun.setText("Predračun");
        btnPredracun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPredracunActionPerformed(evt);
            }
        });

        pnlNaloziKnjigovodstvo.setBackground(new java.awt.Color(102, 153, 255));
        pnlNaloziKnjigovodstvo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnlRadniNaloziKnjigovodstvo.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnlRadniNaloziKnjigovodstvo.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

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
                "ID", "Automobil", "Vlasnik", "Datum otvaranja", "Datum fakturisanja", "Iznos(KM)", "Plaćeno"
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
        tblRadniNalozi.getTableHeader().setResizingAllowed(false);
        tblRadniNalozi.getTableHeader().setReorderingAllowed(false);
        tblRadniNalozi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRadniNaloziMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblRadniNaloziMouseReleased(evt);
            }
        });
        pnlRadniNaloziKnjigovodstvo.setViewportView(tblRadniNalozi);
        if (tblRadniNalozi.getColumnModel().getColumnCount() > 0) {
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
                .addComponent(pnlRadniNaloziKnjigovodstvo, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
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
                "ID", "Naziv", "Količina", "Cijena(KM)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
        pnlTabelaKnjigovodstvo.setViewportView(tblFaktura);
        if (tblFaktura.getColumnModel().getColumnCount() > 0) {
            tblFaktura.getColumnModel().getColumn(1).setResizable(false);
        }
        tblFaktura.setAutoCreateRowSorter(true);

        btnRacun.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnRacun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/money-bag.png"))); // NOI18N
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
        txtBezPDV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtPDV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPDV.setText("17.0");
        txtPDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPDVActionPerformed(evt);
            }
        });

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
        btnNefakturisano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNefakturisanoActionPerformed(evt);
            }
        });

        btnFakturisano.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnFakturisano.setText("Fakturisani nalozi");
        btnFakturisano.setToolTipText("");
        btnFakturisano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFakturisanoActionPerformed(evt);
            }
        });

        btnSviNalozi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSviNalozi.setText("Svi nalozi");
        btnSviNalozi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSviNaloziActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("KM");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("%");

        javax.swing.GroupLayout pnlOkvirKnjigovodstvoLayout = new javax.swing.GroupLayout(pnlOkvirKnjigovodstvo);
        pnlOkvirKnjigovodstvo.setLayout(pnlOkvirKnjigovodstvoLayout);
        pnlOkvirKnjigovodstvoLayout.setHorizontalGroup(
            pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lblRadniNalozi)
                        .addGap(0, 0, Short.MAX_VALUE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel163)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPDV, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 194, Short.MAX_VALUE)
                        .addComponent(jLabel161)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUkupno, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel162))
                    .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlNaloziKnjigovodstvo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnFakturisano, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnNefakturisano))
                                .addGap(18, 18, 18)
                                .addComponent(btnSviNalozi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnPoDatumu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnPoIDu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dtmDatum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(8, 8, 8)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(btnPredracun, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101)
                .addComponent(btnRacun, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlOkvirKnjigovodstvoLayout.setVerticalGroup(
            pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRadniNalozi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                        .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                .addComponent(btnNefakturisano, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnFakturisano, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnSviNalozi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnPoIDu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlOkvirKnjigovodstvoLayout.createSequentialGroup()
                                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnPoDatumu, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(dtmDatum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(pnlNaloziKnjigovodstvo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnPredracun, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .addComponent(btnRacun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTabelaKnjigovodstvo, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlOkvirKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel164)
                    .addComponent(jLabel165)
                    .addComponent(txtBezPDV, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel163)
                    .addComponent(txtPDV, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel161)
                    .addComponent(txtUkupno, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel162)
                    .addComponent(jLabel4)
                    .addComponent(jLabel11))
                .addContainerGap(22, Short.MAX_VALUE))
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
                .addGap(18, 18, 18)
                .addComponent(pnlOkvirKnjigovodstvo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(2154, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlKnjigovodstvoLayout = new javax.swing.GroupLayout(pnlKnjigovodstvo);
        pnlKnjigovodstvo.setLayout(pnlKnjigovodstvoLayout);
        pnlKnjigovodstvoLayout.setHorizontalGroup(
            pnlKnjigovodstvoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKnjigovodstvoLayout.createSequentialGroup()
                .addComponent(pnlOsnovniKnjigovodstvo, javax.swing.GroupLayout.PREFERRED_SIZE, 1065, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2129, Short.MAX_VALUE))
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

        tbZaposleni.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        tbZaposleni.setModel(new javax.swing.table.DefaultTableModel(
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
        tbZaposleni.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbZaposleniMouseReleased(evt);
            }
        });
        jScrollPane12.setViewportView(tbZaposleni);
        if (tbZaposleni.getColumnModel().getColumnCount() > 0) {
            tbZaposleni.getColumnModel().getColumn(1).setResizable(false);
        }
        tbZaposleni.getTableHeader().setReorderingAllowed(false);
        tbZaposleni.setDefaultEditor(Object.class, null);
        tbZaposleni.setAutoCreateRowSorter(true);

        lbBrojRadnihNaloga.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbBrojRadnihNaloga.setForeground(new java.awt.Color(255, 255, 255));
        lbBrojRadnihNaloga.setText("Radni nalozi: 0");

        jSeparator26.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator26.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel122.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel122.setForeground(new java.awt.Color(255, 255, 255));
        jLabel122.setText("Od datuma:");

        btnTraziRadneNalogeRadnika.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTraziRadneNalogeRadnika.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnTraziRadneNalogeRadnika.setText("Traži");
        btnTraziRadneNalogeRadnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraziRadneNalogeRadnikaActionPerformed(evt);
            }
        });

        lbOstvareniProfitRadnika.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbOstvareniProfitRadnika.setForeground(new java.awt.Color(255, 255, 255));
        lbOstvareniProfitRadnika.setText("Ostvareni profit radnika: 0 KM");

        btnPrikazSvihBivsihZaposlenih.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnPrikazSvihBivsihZaposlenih.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/lista zaposlenih.png"))); // NOI18N
        btnPrikazSvihBivsihZaposlenih.setText("Prikaz svih bivših zaposlenih");
        btnPrikazSvihBivsihZaposlenih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrikazSvihBivsihZaposlenihActionPerformed(evt);
            }
        });

        btnDodajZaposlenog.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDodajZaposlenog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/add (1).png"))); // NOI18N
        btnDodajZaposlenog.setText("Dodaj zaposlenog");
        btnDodajZaposlenog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajZaposlenogActionPerformed(evt);
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
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(dateChooserDatumOdZaposlenog, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(dateChooserDatumDoZaposlenog, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(jLabel122)
                                .addGap(104, 104, 104)
                                .addComponent(jLabel119)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTraziRadneNalogeRadnika, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(lbBrojRadnihNaloga)
                                .addGap(183, 183, 183)
                                .addComponent(lbOstvareniProfitRadnika))
                            .addComponent(jLabel117)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 831, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnPrikazSvihBivsihZaposlenih)
                                    .addComponent(btnDodajZaposlenog, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel117)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(btnPrikazSvihBivsihZaposlenih, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDodajZaposlenog, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel119)
                            .addComponent(jLabel122))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooserDatumOdZaposlenog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateChooserDatumDoZaposlenog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnTraziRadneNalogeRadnika)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator26, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbBrojRadnihNaloga)
                    .addComponent(lbOstvareniProfitRadnika))
                .addContainerGap())
        );

        jLabel148.setBackground(new java.awt.Color(255, 255, 255));
        jLabel148.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel148.setForeground(new java.awt.Color(229, 229, 229));
        jLabel148.setText("> Zaposleni");

        jLabel149.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/User_20px_1.png"))); // NOI18N

        tbRadniNalozi.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        tbRadniNalozi.setModel(new javax.swing.table.DefaultTableModel(
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
                "Marka", "Model", "Registracija", "Datum popravka", "Opis popravke", "Cijena troskova(KM)", "Cijena naplate(KM)", "Profit(KM)"
            }
        ));
        tbRadniNalozi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbRadniNaloziMouseReleased(evt);
            }
        });
        jScrollPane13.setViewportView(tbRadniNalozi);
        tbRadniNalozi.getTableHeader().setReorderingAllowed(false);
        tbRadniNalozi.setDefaultEditor(Object.class, null);
        tbRadniNalozi.setAutoCreateRowSorter(true);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane13)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(2094, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel148))
                .addGap(10, 10, 10)
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 100, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlZaposleniLayout = new javax.swing.GroupLayout(pnlZaposleni);
        pnlZaposleni.setLayout(pnlZaposleniLayout);
        pnlZaposleniLayout.setHorizontalGroup(
            pnlZaposleniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZaposleniLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, 3184, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlZaposleniLayout.setVerticalGroup(
            pnlZaposleniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlZaposleniLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
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
                .addContainerGap(14, Short.MAX_VALUE))
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
        btnPregled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
            .addGroup(pnlOdabirIntervalaLayout.createSequentialGroup()
                .addContainerGap()
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(pnlKontrolnaTablaStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlFakture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBrojPopravki, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlZaradaUkupno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlZaradaDijelovi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlOdabirIntervala, javax.swing.GroupLayout.PREFERRED_SIZE, 176, Short.MAX_VALUE))
                .addGap(20, 20, 20))
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
                .addGap(11, 11, 11)
                .addGroup(pnlHeaderStatistikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel86)
                    .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(pnlKontrolnaTablaStatistika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabpnlGrafici.setOpaque(true);

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
        cbMjesec.setSelectedIndex(1);

        cbGodina.setMaximumRowCount(6);
        cbGodina.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017" }));
        cbGodina.setSelectedIndex(7);

        btnPregledGrafik.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPregledGrafik.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/search.png"))); // NOI18N
        btnPregledGrafik.setText("Pregled");
        btnPregledGrafik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
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
                .addGap(0, 1994, Short.MAX_VALUE))
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pnlMeniRadniNaloziMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMeniRadniNaloziMouseClicked
        dodajPopupMeniPretragaRadniNaloga(this);
        menuItemClick(pnlMeniRadniNalozi, 1, pnlRadniNalozi);
        tableRNalozi.getTableHeader().setReorderingAllowed(false);
        tableRNalozi.setDefaultEditor(Object.class, null);
        tableRNalozi.setAutoCreateRowSorter(true);
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
        inicijalizujStatistikaPanel();
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

    private void tbZaposleniMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbZaposleniMouseReleased
        int r = tbZaposleni.rowAtPoint(evt.getPoint());
        if (r >= 0 && r < tbZaposleni.getRowCount()) {
            tbZaposleni.setRowSelectionInterval(r, r);
        } else {
            tbZaposleni.clearSelection();
        }

        int rowindex = tbZaposleni.getSelectedRow();
        if (rowindex < 0) {
            return;
        }
        if (evt.isPopupTrigger()) {
            popupMenuZaposleni.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbZaposleniMouseReleased

    private void mnItDetaljniOpisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnItDetaljniOpisActionPerformed
        new ZaposleniLogika("detaljan opis",this).run();
    }//GEN-LAST:event_mnItDetaljniOpisActionPerformed

    private void mnItIzmjeniRadnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnItIzmjeniRadnikaActionPerformed
        new ZaposleniLogika("izmjeni",this).run();
    }//GEN-LAST:event_mnItIzmjeniRadnikaActionPerformed

    private void mnItOtpustiRadnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnItOtpustiRadnikaActionPerformed
        new ZaposleniLogika("otpusti",this).run();
    }//GEN-LAST:event_mnItOtpustiRadnikaActionPerformed

    private void btnPrikazSvihBivsihZaposlenihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikazSvihBivsihZaposlenihActionPerformed
        new SviBivsiZaposleniDialog(this, true).show();
    }//GEN-LAST:event_btnPrikazSvihBivsihZaposlenihActionPerformed

    private void btnTraziRadneNalogeRadnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraziRadneNalogeRadnikaActionPerformed
        new ZaposleniLogika("statistika",this).run();
    }//GEN-LAST:event_btnTraziRadneNalogeRadnikaActionPerformed

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
        akcijaZaRefreshVozila = 3;
        flagVoziloVlasnik = false;
    }//GEN-LAST:event_btnTraziActionPerformed

    private void btnPrikaziSveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziSveActionPerformed
        izbrisiPopupZaVozila();
        izbrisiPopupZaVlasnike();
        ucitajPopupZaVlasnike();
        flagVoziloVlasnik = false;
        akcijaZaRefreshVozila = 2;
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
        new DodajVlasnikaDialog(this, true, this).setVisible(true);
    }//GEN-LAST:event_btnDodajVlasnikaActionPerformed

    private void btnDodajVoziloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajVoziloActionPerformed
        new DodajVoziloDialog(this, true, this).setVisible(true);
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
        akcijaZaRefreshVozila = 0;
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
        akcijaZaRefreshVozila = 1;
        voziloKupacMeniLogika.pronadjiVozilo(this);
    }//GEN-LAST:event_btnPronadjiVoziloActionPerformed

    private void txtModelTraziFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtModelTraziFocusGained
        //ucitajPreporukeModel();
    }//GEN-LAST:event_txtModelTraziFocusGained

    private void btnDodajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajActionPerformed
        dioLogika.dodajDio(this);
    }//GEN-LAST:event_btnDodajActionPerformed

    private void btnPretraziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPretraziActionPerformed
        dioLogika.pretraziDijelove(this);
    }//GEN-LAST:event_btnPretraziActionPerformed

    private void jLabel36KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel36KeyPressed

    }//GEN-LAST:event_jLabel36KeyPressed

    private void tblDijeloviMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDijeloviMouseClicked

    }//GEN-LAST:event_tblDijeloviMouseClicked

    private void btnSviDijeloviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSviDijeloviActionPerformed
        ArrayList<DioDTO> dijelovi = DAOFactory.getDAOFactory().getDioDAO().getSviDijelovi();

        DefaultTableModel dtm = (DefaultTableModel) tblDijelovi.getModel();
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
        btnPronadjiTerminAkcija();
    }//GEN-LAST:event_btnPronadjiTerminActionPerformed

    private void btnPonistiUnosePretragaActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPonistiUnosePretragaActionPerformed
    {//GEN-HEADEREND:event_btnPonistiUnosePretragaActionPerformed
        btnPonistiUnosePretragaAkcija();
    }//GEN-LAST:event_btnPonistiUnosePretragaActionPerformed

    private void btnDodajTerminActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDodajTerminActionPerformed
    {//GEN-HEADEREND:event_btnDodajTerminActionPerformed
        btnDodajTerminAkcija();
    }//GEN-LAST:event_btnDodajTerminActionPerformed

    private void btnPonistiUnoseTerminActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPonistiUnoseTerminActionPerformed
    {//GEN-HEADEREND:event_btnPonistiUnoseTerminActionPerformed
        btnPonistiUnoseTerminAkcija();
    }//GEN-LAST:event_btnPonistiUnoseTerminActionPerformed

    private void btnRacunActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRacunActionPerformed
    {//GEN-HEADEREND:event_btnRacunActionPerformed
       double PDV;
        try{
            PDV=Double.parseDouble(txtPDV.getText());
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "PDV nije uredu!","Problem", JOptionPane.ERROR_MESSAGE);
            return;
        }
        prikaziFakturu(tblRadniNalozi, tblFaktura,PDV, "Račun");
    }//GEN-LAST:event_btnRacunActionPerformed

    private void btnPredracunActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPredracunActionPerformed
    {//GEN-HEADEREND:event_btnPredracunActionPerformed
        Double PDV;
        try{
            PDV=Double.parseDouble(txtPDV.getText());
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "PDV nije uredu!","Problem", JOptionPane.ERROR_MESSAGE);
            return;
        }
        prikaziFakturu(tblRadniNalozi, tblFaktura,PDV, "Faktura");
    }//GEN-LAST:event_btnPredracunActionPerformed

    private void tblRadniNaloziMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblRadniNaloziMouseClicked
    {//GEN-HEADEREND:event_tblRadniNaloziMouseClicked
        if ("".equals(txtPDV.getText())) {
            txtPDV.setText("0.0");
        }
        stavkeSaNalogaZaTabelu(tblFaktura, tblRadniNalozi, txtBezPDV, txtPDV, txtUkupno);
        provjeraZaDugmiceZaTabeluNaloga();

    }//GEN-LAST:event_tblRadniNaloziMouseClicked

    private void btnNefakturisanoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnNefakturisanoActionPerformed
    {//GEN-HEADEREND:event_btnNefakturisanoActionPerformed
        btnNefakturisanoAkcija();
    }//GEN-LAST:event_btnNefakturisanoActionPerformed

    private void btnFakturisanoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnFakturisanoActionPerformed
    {//GEN-HEADEREND:event_btnFakturisanoActionPerformed
        btnFakturisanoAkcija();
    }//GEN-LAST:event_btnFakturisanoActionPerformed

    private void btnPoIDuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPoIDuActionPerformed
    {//GEN-HEADEREND:event_btnPoIDuActionPerformed
        btnPoIDuAkcija();
    }//GEN-LAST:event_btnPoIDuActionPerformed

    private void btnPoDatumuActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPoDatumuActionPerformed
    {//GEN-HEADEREND:event_btnPoDatumuActionPerformed
        btnPoDatumuAkcija();
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
            dat1 = (java.util.Date) dcAktivnosti1.getDate();
            dat2 = (java.util.Date) dcAktivnosti2.getDate();
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
                if (!dat1.after(dat2)) {
                    lblAktivnostiOdDo.setText("Aktivnosti: " + sdf.format(dat1) + "-" + sdf.format(dat2));
                    pocetnaLogika.prikaziAktivnosti(dat1, dat2);
                } else if(dat1.equals(dat2)){                    
                    pocetnaLogika.prikaziAktivnosti(dat1, dat1);
                    lblAktivnostiOdDo.setText("Aktivnosti: " + sdf.format(dat1));
                }
            }
        } catch (NullPointerException e) {
            JOptionPane jop = new JOptionPane();
            jop.showMessageDialog(new JFrame(), "Nepravilan format datuma", "Greška",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPrikaziiActionPerformed

    private void lblPregledAktivnostiPocetnaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_lblPregledAktivnostiPocetnaPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_lblPregledAktivnostiPocetnaPropertyChange

    private void btnBazaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBazaActionPerformed
        // treba serijalizovati datum u file baza.ser
         JFileChooser fc = new JFileChooser();
        fc.showSaveDialog(this);
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
            p=runtime.exec("C:/Program Files/MySQL/MySQL Server 5.7/bin/mysqldump.exe -uroot -proot --add-drop-database -B autoservismaric -r"+path);
            
            int processComplete = p.waitFor();
            if (processComplete==0) {
                jLabel1.setText("Backup Created Succuss");
                File f = new File("baza.ser");
                java.util.Date datum = new java.util.Date();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(datum);
                lblBaza.setText(new SimpleDateFormat("dd.MM.yyyy.").format(datum));                
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

        cbModelDio.removeAllItems();
        cbMarkaDio.removeAllItems();
        cbModel.removeAllItems();
        cbMarka.removeAllItems();

        cbModelDio.addItem("Svi");
        cbMarkaDio.addItem("Svi");

        cbModel.addItem("Svi");
        cbMarka.addItem("Svi");
        //cbModel.addItem("Svi");
        for (ModelVozilaDTO d : modeli) {
            if (!modelii.contains(d.getModel())) {
                modelii.add(d.getModel());
                cbModelDio.addItem(d.getModel());
                cbModel.addItem(d.getModel());
            }
            if (!markee.contains(d.getMarka())) {
                markee.add(d.getMarka());
                cbMarkaDio.addItem(d.getMarka());
                cbMarka.addItem(d.getMarka());
            }
        }
    }//GEN-LAST:event_btnDodajModelActionPerformed

    private void cbMarkaDioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbMarkaDioItemStateChanged
        modelVozilaLogika.ucitajModeleDodavanje(this, cbMarkaDio.getItemAt(cbMarkaDio.getSelectedIndex()));
    }//GEN-LAST:event_cbMarkaDioItemStateChanged

    private void cbMarkaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbMarkaItemStateChanged
        modelVozilaLogika.ucitajModelePretrazivanje(this, cbMarka.getItemAt(cbMarka.getSelectedIndex()));
    }//GEN-LAST:event_cbMarkaItemStateChanged

    private void rbPravnoPocetnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPravnoPocetnaActionPerformed
        tfNazivPocetna.setEditable(true);
        tfImePocetna.setEditable(false);
        tfPrezimePocetna.setEditable(false);
        tfImePocetna.setText("");
        tfPrezimePocetna.setText("");
        tfImePocetna.setBackground(Color.gray);
        tfPrezimePocetna.setBackground(Color.gray);
        tfNazivPocetna.setBackground(Color.white);
        rbPrivatnoPocetna.setSelected(false);
    }//GEN-LAST:event_rbPravnoPocetnaActionPerformed

    private void rbPrivatnoPocetnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPrivatnoPocetnaActionPerformed
        tfNazivPocetna.setEditable(false);
        tfNazivPocetna.setText("");
        tfImePocetna.setEditable(true);
        tfPrezimePocetna.setEditable(true);
        tfNazivPocetna.setBackground(Color.gray);
        rbPravnoPocetna.setSelected(false);
        tfImePocetna.setBackground(Color.white);
        tfPrezimePocetna.setBackground(Color.white);
    }//GEN-LAST:event_rbPrivatnoPocetnaActionPerformed

    private void btnPrikaziSvePredracuneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziSvePredracuneActionPerformed
        btnPrikaziSvePredracuneAkcija();
    }//GEN-LAST:event_btnPrikaziSvePredracuneActionPerformed

    private void btnPrikaziPredracuneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziPredracuneActionPerformed
        //KnjigovodstvoLogika knjLogika = new KnjigovodstvoLogika();
        //if(!"".equals(tfNazivv.getText()))
            //knjLogika.pretraziNeplaceneFakturePoNazivu(this, tfNazivv.getText(), tfImee.getText(), tfPrezimee.getText());
            KnjigovodstvoLogika.pretraziNeplaceneFakturePoNazivu(this, tfNazivPocetna.getText(), tfImePocetna.getText(), tfPrezimePocetna.getText());
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
        new IzaberiVoziloDialog(this, true, this).setVisible(true);
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

    private void btnSviNaloziActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSviNaloziActionPerformed
    {//GEN-HEADEREND:event_btnSviNaloziActionPerformed
        btnSviNaloziAkcija();
    }//GEN-LAST:event_btnSviNaloziActionPerformed

    private void btnDodajZaposlenogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajZaposlenogActionPerformed
        new DodajZaposlenogDialog(this,true,this).setVisible(true);
    }//GEN-LAST:event_btnDodajZaposlenogActionPerformed

    private void tbRadniNaloziMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRadniNaloziMouseReleased
        int r = tbRadniNalozi.rowAtPoint(evt.getPoint());
        if (r >= 0 && r < tbRadniNalozi.getRowCount()) {
            tbRadniNalozi.setRowSelectionInterval(r, r);
        } else {
            tbRadniNalozi.clearSelection();
        }

        int rowindex = tbRadniNalozi.getSelectedRow();
        if (rowindex < 0) {
            return;
        }
        if (evt.isPopupTrigger()) {
            popUpMenuRadniNaloziZaposlenog.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_tbRadniNaloziMouseReleased

    private void menuItemOpisRadnogNalogaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOpisRadnogNalogaActionPerformed
        String value = tbRadniNalozi.getModel().getValueAt(tbRadniNalozi.getSelectedRow(), 4).toString();
        if(value!=null){
            new OpisPopravkeDialog(this,true,value).show();
        }else{
            JOptionPane.showMessageDialog(null, "Nema opisa", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_menuItemOpisRadnogNalogaActionPerformed

    private void txtPDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPDVActionPerformed
        if(!"".equals(txtPDV.getText()) && !"".equals(txtBezPDV.getText())){
            Double cijena=Double.parseDouble(txtBezPDV.getText());
            Double pdv=Double.parseDouble(txtPDV.getText());
            Double ukupno=cijena*(1+pdv/100);
            txtUkupno.setText(String.format("%.2f",ukupno));
        }
    }//GEN-LAST:event_txtPDVActionPerformed

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
            //markeAU = ucitajPreporukeMarke();
            //registracijeAU = ucitajPreporukeRegistracija();
            //vlasnikAU = ucitajPreporukeVlasnik();
            //pravniNazivAU = ucitajPreporukePravniNaziv();
            //modelAU = ucitajPreporukeModel();
        }
    }

    private void loadDijeloviForm() {

        ArrayList<DioDTO> dijelovi = DAOFactory.getDAOFactory().getDioDAO().getSviDijelovi();
        DefaultTableModel dtm = (DefaultTableModel) tblDijelovi.getModel();
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

        cbModelDio.removeAllItems();
        cbMarkaDio.removeAllItems();
        cbModel.removeAllItems();
        cbMarka.removeAllItems();

        cbModelDio.addItem("Svi");
        cbMarkaDio.addItem("Svi");

        cbModel.addItem("Svi");
        cbMarka.addItem("Svi");
        //cbModel.addItem("Svi");
        /*for (DioDTO d : nazivi) {
            if (!naz.contains(d.getModel())) {
                naz.add(d.getModel());*/
        for (ModelVozilaDTO d : modeli) {
            if (!modelii.contains(d.getModel())) {
                modelii.add(d.getModel());
                cbModelDio.addItem(d.getModel());
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
                cbMarkaDio.addItem(d.getMarka());
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

    public void inicijalizujZaposleniPanel()
    {
        dateChooserDatumDoZaposlenog.setCalendar(Calendar.getInstance());
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        dateChooserDatumOdZaposlenog.setCalendar(cal);
        new ZaposleniLogika("svi",this).run();
    }

    private void inicijalizujStatistikaPanel(){
        new StatistikaLogika().inicijalizujStatistikaPanel(this);
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

    public JComboBox<String> getCbMjesec() {
        return cbMjesec;
    }

    public void setCbMjesec(JComboBox<String> cbMjesec) {
        this.cbMjesec = cbMjesec;
    }

    public JComboBox<String> getCbGodina() {
        return cbGodina;
    }

    public void setCbGodina(JComboBox<String> cbGodina) {
        this.cbGodina = cbGodina;
    }

    public JButton getBtnPregled() {
        return btnPregled;
    }

    public void setBtnPregled(JButton btnPregled) {
        this.btnPregled = btnPregled;
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

    public JTable getTbZaposleni() {
        return tbZaposleni;
    }

    public void setTbZaposleni(JTable tbZaposleni) {
        this.tbZaposleni = tbZaposleni;
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
        return btnDodajZaposlenog;
    }

    public JButton getButtonPregled() {
        return btnPregled;
    }

    public JButton getButtonPregledGrafik() {
        return btnPregledGrafik;
    }

    public JButton getButtonPrikazSvihBivsihRadnika() {
        return btnPrikazSvihBivsihZaposlenih;
    }

    public JButton getButtonTraziRadneNalogeRadnika() {
        return btnTraziRadneNalogeRadnika;
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

    public JLabel getDatumLabel() {
        return lblAktivnostiOdDo;
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
        return pnlOsnovnoDijelovi;
    }

    public JPanel getjPanel14() {
        return pnlDijeloviMenu;
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
        return pnlOsnovnoPocetna;
    }

    public JPanel getjPanel6() {
        return pnlPocetnaMenu;
    }

    public JPanel getjPanel7() {
        return pnlPregledAktivnosti;
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
        return jScrollAktivnosti;
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
        return jScrollTabelaDijelovi;
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
        return tblDijelovi;
    }

    public JTable getjTableAktivnosti() {
        return tblAktivnosti;
    }

    public JTable getTableZaposleni() {
        return tbZaposleni;
    }

    public JComboBox<String> getCbGorivoDio() {
        return cbGorivoDio;
    }

    public JComboBox<String> getCbMarkaDio() {
        return cbMarkaDio;
    }

    public JComboBox<String> getCbModelDio() {
        return cbModelDio;
    }

    public JTextField getTfCijenaDio() {
        return tfCijenaDio;
    }

    public JTextField getTfGodisteDio() {
        return tfGodisteDio;
    }

    public JTextField getTfKolicinaDio() {
        return tfKolicinaDio;
    }

    public JTextField getTfNazivDio() {
        return tfNazivDio;
    }

    public JTextField getTfSifraDio() {
        return tfSifraDio;
    }
    public JCheckBox getStanje(){
        return cbStanje;
    }
    public JPanel getKnjigovodstvoPanel() {
        return pnlKnjigovodstvo;
    }

    public JLabel getLabelBrojRadnihNaloga() {
        return lbBrojRadnihNaloga;
    }

    public JLabel getLabelOstvareniProfitRadnika() {
        return lbOstvareniProfitRadnika;
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
        return mnItDetaljniOpis;
    }

    public JMenuItem getMenuItemIzmjeniRadnika() {
        return mnItIzmjeniRadnika;
    }

    public JMenuItem getMenuItemOtpustiRadnika() {
        return mnItOtpustiRadnika;
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

    public JTable getTableRadniNalozi() {
        return tbRadniNalozi;
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
        return tblDijelovi;
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

    public JButton getBtnSviNalozi() {
        return btnSviNalozi;
    }

    public void setBtnSviNalozi(JButton btnSviNalozi) {
        this.btnSviNalozi = btnSviNalozi;
    }

    public JDateChooser getDcDatumDo() {
        return dcDatumDo;
    }

    public void setDcDatumDo(JDateChooser dcDatumDo) {
        this.dcDatumDo = dcDatumDo;
    }

    public JDateChooser getDcDatumOd() {
        return dcDatumOd;
    }

    public void setDcDatumOd(JDateChooser dcDatumOd) {
        this.dcDatumOd = dcDatumOd;
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
        return dcAktivnosti1;
    }

    public JDateChooser getDateChooserAktivnosti2() {
        return dcAktivnosti2;
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
        return lblDatumDo;
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
        return lblDatumOd;
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

    public JLabel getjLabel126() {
        return txtKnjigovodstvo;
    }

    public JLabel getjLabel127() {
        return txtSlikaKnjigovodstvo;
    }

    public JLabel getjLabel128() {
        return txtPronadjiZakazivanja;
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

    public JLabel getjLabel148() {
        return jLabel148;
    }

    public JLabel getjLabel149() {
        return jLabel149;
    }

    public JLabel getjLabel15() {
        return jLabel15;
    }

    public JLabel getjLabel156() {
        return jLabel156;
    }

    public JLabel getjLabel157() {
        return jLabel157;
    }

    public JLabel getjLabel16() {
        return lblDatt;
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
        return lblDat2;
    }

    public JLabel getjLabel18() {
        return jLabel18;
    }

    public JLabel getjLabel19() {
        return lblVlasnikPocetna;
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
        return lblKolicinaDodajDio;
    }

    public JLabel getjLabel35() {
        return jLabel35;
    }

    public JLabel getjLabel36() {
        return jLabel36;
    }

    public JLabel getjLabel37() {
        return lblPretragaPredracunaPocetna;
    }

    public JLabel getjLabel38() {
        return lblCuvanjePodatakaPocetna;
    }

    public JLabel getjLabel39() {
        return lblNazivPocetna;
    }

    public JLabel getjLabel4() {
        return lblPregledAktivnostiPocetna;
    }

    public JLabel getjLabel40() {
        return lblPoslednjiPut;
    }

    public JLabel getjLabel41() {
        return lblPronadjiDio;
    }

    public JLabel getjLabel42() {
        return lblImePocetna;
    }

    public JLabel getjLabel43() {
        return lblPrezimePocetna;
    }

    public JLabel getjLabel44() {
        return lblSifraDodajDio;
    }

    public JLabel getjLabel45() {
        return lblNazivDodajDio;
    }

    public JLabel getjLabel46() {
        return lblCijenaDodajDio;
    }

    public JLabel getjLabel47() {
        return lblDijelovi;
    }

    public JLabel getjLabel48() {
        return lblDijeloviZnak;
    }

    public JLabel getjLabel49() {
        return lblDodajDio;
    }

    public JLabel getjLabel5() {
        return jLabel5;
    }

    public JLabel getjLabel50() {
        return lblSifraDio;
    }

    public JLabel getjLabel51() {
        return lblDatumOtvaranja;
    }

    public JLabel getjLabel52() {
        return lblNazivDio;
    }

    public JLabel getjLabel53() {
        return lblIdDio;
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
    
    public JLabel getjLabel6() {
        return jLabel6;
    }

    public JLabel getjLabel60() {
        return lblStanjeDodajDio;
    }

    public JLabel getjLabel61() {
        return lblGorivoDodajDio;
    }

    public JLabel getjLabel62() {
        return lblMarkaDodajDio;
    }

    public JLabel getjLabel63() {
        return lblGodisteDodajDio;
    }

    public JLabel getjLabel64() {
        return lblModelDodajDio;
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
        return lblGorivoDio;
    }

    public JLabel getjLabel74() {
        return lblMarkaDio;
    }

    public JLabel getjLabel75() {
        return lblStanjeDio;
    }

    public JLabel getjLabel76() {
        return lblGodisteDio;
    }

    public JLabel getjLabel77() {
        return lblModelDio;
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
        return pnlCuvanjePodataka;
    }

    public JPanel getjPanel16() {
        return pnlPretragaPredracuna;
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
        return rbPravnoPocetna;
    }

    public JRadioButton getJrbPrivatno() {
        return rbPrivatnoPocetna;
    }

    public JLabel getLabelBaza() {
        return lblBaza;
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
        return tblAktivnosti;
    }

    public JTable getTableRNalozi() {
        return tableRNalozi;
    }

    public JTextField getTfImeRadniNalog() {
        return tfImeRadniNalog;
    }

    public JTextField getTfImee() {
        return tfImePocetna;
    }

    public JTextField getTfNazivRadniNalog() {
        return tfNazivRadniNalog;
    }

    public JTextField getTfNazivv() {
        return tfNazivPocetna;
    }

    public JTextField getTfPrezimeRadniNalog() {
        return tfPrezimeRadniNalog;
    }

    public JTextField getTfPrezimee() {
        return tfPrezimePocetna;
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
    private javax.swing.JButton btnDodajZaposlenog;
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
    private javax.swing.JButton btnPrikazSvihBivsihZaposlenih;
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
    private javax.swing.JButton btnSviNalozi;
    private javax.swing.JButton btnTrazi;
    private javax.swing.JButton btnTraziRadneNalogeRadnika;
    private javax.swing.JCheckBox cbDatumOtvaranja;
    private javax.swing.JCheckBox cbDatumZatvaranja;
    private javax.swing.JComboBox<String> cbGodina;
    private javax.swing.JComboBox<String> cbGorivo;
    private javax.swing.JComboBox<String> cbGorivoDio;
    private javax.swing.JComboBox<String> cbMarka;
    private javax.swing.JComboBox<String> cbMarkaDio;
    private javax.swing.JComboBox<String> cbMjesec;
    private javax.swing.JComboBox<String> cbModel;
    private javax.swing.JComboBox<String> cbModelDio;
    private javax.swing.JCheckBox cbNovo;
    private javax.swing.JCheckBox cbPotrebnoZavrsiti;
    private javax.swing.JCheckBox cbStanje;
    private javax.swing.JCheckBox cbSvi;
    private javax.swing.JCheckBox cbSviRadniNalog;
    private javax.swing.JPanel danasnjeAktivnostiPanel;
    private com.toedter.calendar.JDateChooser dateChooserDatumDoZaposlenog;
    private com.toedter.calendar.JDateChooser dateChooserDatumOdZaposlenog;
    private com.toedter.calendar.JDateChooser dcAktivnosti1;
    private com.toedter.calendar.JDateChooser dcAktivnosti2;
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
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
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
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JScrollPane jScrollAktivnosti;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollTabelaDijelovi;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel labelPoruka;
    private javax.swing.JLabel lbBrojRadnihNaloga;
    private javax.swing.JLabel lbOstvareniProfitRadnika;
    private javax.swing.JLabel lbRadniNaloziNaslov;
    private javax.swing.JLabel lbRadniNaloziSlika;
    private javax.swing.JLabel lblAktivnostiOdDo;
    private javax.swing.JLabel lblBaza;
    private javax.swing.JLabel lblBrojFaktura;
    private javax.swing.JLabel lblBrojNeplacenihFaktura;
    private javax.swing.JLabel lblBrojPlacenihFaktura;
    private javax.swing.JLabel lblCijenaDodajDio;
    private javax.swing.JLabel lblCuvanjePodatakaPocetna;
    private javax.swing.JLabel lblDat2;
    private javax.swing.JLabel lblDatt;
    private javax.swing.JLabel lblDatumDo;
    private javax.swing.JLabel lblDatumOd;
    private javax.swing.JLabel lblDatumOtvaranja;
    private javax.swing.JLabel lblDatumOtvaranjaDO;
    private javax.swing.JLabel lblDatumOtvaranjaOD;
    private javax.swing.JLabel lblDatumZatvaranja;
    private javax.swing.JLabel lblDatumZatvaranjaDO;
    private javax.swing.JLabel lblDatumZatvaranjaOD;
    private javax.swing.JLabel lblDijelovi;
    private javax.swing.JLabel lblDijeloviZnak;
    private javax.swing.JLabel lblDnevnaZarada;
    private javax.swing.JLabel lblDnevnaZaradaDijelovi;
    private javax.swing.JLabel lblDodajDio;
    private javax.swing.JLabel lblGodisnjaZarada;
    private javax.swing.JLabel lblGodisnjaZaradaDijelovi;
    private javax.swing.JLabel lblGodisteDio;
    private javax.swing.JLabel lblGodisteDodajDio;
    private javax.swing.JLabel lblGodisteVozila;
    private javax.swing.JLabel lblGorivoDio;
    private javax.swing.JLabel lblGorivoDodajDio;
    private javax.swing.JLabel lblIdDio;
    private javax.swing.JLabel lblImePocetna;
    private javax.swing.JLabel lblImeRN;
    private javax.swing.JLabel lblIntervalZarada;
    private javax.swing.JLabel lblIntervalZaradaDijelovi;
    private javax.swing.JLabel lblKolicinaDodajDio;
    private javax.swing.JLabel lblMarkaDio;
    private javax.swing.JLabel lblMarkaDodajDio;
    private javax.swing.JLabel lblMarkaVozila;
    private javax.swing.JLabel lblMjesecnaZarada;
    private javax.swing.JLabel lblMjesecnaZaradaDijelovi;
    private javax.swing.JLabel lblModelDio;
    private javax.swing.JLabel lblModelDodajDio;
    private javax.swing.JLabel lblNaslovVozila;
    private javax.swing.JLabel lblNazivDio;
    private javax.swing.JLabel lblNazivDodajDio;
    private javax.swing.JLabel lblNazivPocetna;
    private javax.swing.JLabel lblNazivRN;
    private javax.swing.JLabel lblPopravkeDanas;
    private javax.swing.JLabel lblPopravkeGodina;
    private javax.swing.JLabel lblPopravkeInterval;
    private javax.swing.JLabel lblPopravkeMjesec;
    private javax.swing.JLabel lblPoslednjiPut;
    private javax.swing.JLabel lblPotrebnoZavrsiti;
    private javax.swing.JLabel lblPotrebnoZavrsitiDO;
    private javax.swing.JLabel lblPotrebnoZavrsitiOD;
    private javax.swing.JLabel lblPregledAktivnostiPocetna;
    private javax.swing.JLabel lblPretragaPredracunaPocetna;
    private javax.swing.JLabel lblPrezimePocetna;
    private javax.swing.JLabel lblPrezimeRN;
    private javax.swing.JLabel lblPrezimeVozila;
    private javax.swing.JLabel lblPronadjiDio;
    private javax.swing.JLabel lblRadniNalozi;
    private javax.swing.JLabel lblRegistracijaVozila;
    private javax.swing.JLabel lblSifraDio;
    private javax.swing.JLabel lblSifraDodajDio;
    private javax.swing.JLabel lblSlikaVozila;
    private javax.swing.JLabel lblStanjeDio;
    private javax.swing.JLabel lblStanjeDodajDio;
    private javax.swing.JLabel lblVlasnik;
    private javax.swing.JLabel lblVlasnikPocetna;
    private javax.swing.JMenuItem menuItemOpisRadnogNaloga;
    private javax.swing.JMenuItem mnItDetaljniOpis;
    private javax.swing.JMenuItem mnItIzmjeniRadnika;
    private javax.swing.JMenuItem mnItOtpustiRadnika;
    private javax.swing.JPanel panelOsnovniPretragaRN;
    private javax.swing.JPanel panelOsnovniRN;
    private javax.swing.JPanel panelPretraga;
    private javax.swing.JPanel pnlBrojPopravki;
    private javax.swing.JPanel pnlCuvanjePodataka;
    private javax.swing.JPanel pnlDijelovi;
    private javax.swing.JPanel pnlDijeloviMenu;
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
    private javax.swing.JPanel pnlOsnovnoDijelovi;
    private javax.swing.JPanel pnlOsnovnoPocetna;
    private javax.swing.JPanel pnlParent;
    private javax.swing.JPanel pnlPocetna;
    private javax.swing.JPanel pnlPocetnaMenu;
    private javax.swing.JPanel pnlPregledAktivnosti;
    private javax.swing.JPanel pnlPretragaPredracuna;
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
    private javax.swing.JPopupMenu popUpMenuRadniNaloziZaposlenog;
    private javax.swing.JPopupMenu popupMenuZaposleni;
    private javax.swing.JPanel pretraziPanel;
    private javax.swing.JRadioButton rbPravnoLiceVozilo;
    private javax.swing.JRadioButton rbPravnoPocetna;
    private javax.swing.JRadioButton rbPravnoRadniNalog;
    private javax.swing.JRadioButton rbPravnoTrazi;
    private javax.swing.JRadioButton rbPrivatnoLiceVozilo;
    private javax.swing.JRadioButton rbPrivatnoPocetna;
    private javax.swing.JRadioButton rbPrivatnoRadniNalog;
    private javax.swing.JRadioButton rbPrivatnoTrazi;
    private javax.swing.JSeparator separatorRN;
    private javax.swing.JScrollPane spVoziloPretraga;
    private javax.swing.JScrollPane spanelTabela;
    private javax.swing.JTable tableRNalozi;
    private javax.swing.JTable tableVozila;
    private javax.swing.JTabbedPane tabpnlGrafici;
    private javax.swing.JTable tbRadniNalozi;
    private javax.swing.JTable tbZaposleni;
    private javax.swing.JTable tblAktivnosti;
    private javax.swing.JTable tblDijelovi;
    private javax.swing.JTable tblFaktura;
    private javax.swing.JTable tblNeplaceneFakture;
    private javax.swing.JTable tblRadniNalozi;
    private javax.swing.JTable tblTermini;
    private javax.swing.JTextField tfCijenaDio;
    private javax.swing.JTextField tfGodiste;
    private javax.swing.JTextField tfGodisteDio;
    private javax.swing.JTextField tfId;
    private javax.swing.JTextField tfImePocetna;
    private javax.swing.JTextField tfImeRadniNalog;
    private javax.swing.JTextField tfKolicinaDio;
    private javax.swing.JTextField tfNaziv;
    private javax.swing.JTextField tfNazivDio;
    private javax.swing.JTextField tfNazivPocetna;
    private javax.swing.JTextField tfNazivRadniNalog;
    private javax.swing.JTextField tfPrezimePocetna;
    private javax.swing.JTextField tfPrezimeRadniNalog;
    private javax.swing.JTextField tfRegistracijaRadniNalog;
    private javax.swing.JTextField tfSifra;
    private javax.swing.JTextField tfSifraDio;
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
