package autoservismaric.dialog;

import data.dao.DAOFactory;
import data.dto.DioDTO;
import data.dto.ModelVozilaDTO;
import data.dto.RadniNalogDTO;
import data.dto.RadniNalogDioDTO;
import data.dto.RadniNalogRadnikDTO;
import data.dto.VoziloDTO;
import data.dto.ZaposleniDTO;
import java.awt.Color;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DulleX
 */
public class IzmijeniRadniNalogDialog extends javax.swing.JDialog {

    /**
     * Creates new form IzmijeniRadniNalogDialog2
     */
    
    private int idRadnogNaloga;
    private int idVozila;
    private RadniNalogDTO nalog;

    public IzmijeniRadniNalogDialog(Frame owner, boolean modal) {
        super(owner, modal);
        initComponents();
    }
    
    
    
    public IzmijeniRadniNalogDialog(java.awt.Frame parent, boolean modal, int idRadnogNaloga) {
        this(parent, modal);
        
        this.idRadnogNaloga = idRadnogNaloga;
        RadniNalogDTO rn = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(this.idRadnogNaloga);
        
        datumOtvaranjaNaloga.setDate(rn.getDatumOtvaranjaNaloga());
        datumZatvaranjaNaloga.setDate(rn.getDatumZatvaranjaNaloga());
        potrebnoZavrsitiDo.setDate(rn.getPredvidjenoVrijemeZavrsetka());
        
        rn.setIdRadniNalog(this.idRadnogNaloga);
        nalog = rn;
        this.idVozila = rn.getIdVozilo();
        tfIdVozila.setText((new Integer(rn.getIdVozilo()).toString()));
        tfTroskovi.setText((new Double(rn.getTroskovi())).toString());
        tfKilometraza.setText((new Integer(rn.getKilometraza()).toString()));
        tfCijena.setText((new Double(rn.getCijenaUsluge()).toString()));
        cbPlaceno.setSelected(rn.isPlaceno());
        taProblem.setText(rn.getOpisProblema());
        
        tabelaIzabraniDijelovi.setAutoCreateRowSorter(true);
        tabelaDijelovi.setAutoCreateRowSorter(true);
        tabelaDijelovi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaIzabraniDijelovi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaDijelovi.setDefaultEditor(Object.class, null);
        tabelaIzabraniDijelovi.setDefaultEditor(Object.class, null);
        
        tabelaDijelovi.getTableHeader().setReorderingAllowed(false);
        tabelaIzabraniDijelovi.getTableHeader().setReorderingAllowed(false);


        VoziloDTO vozilo = DAOFactory.getDAOFactory().getVoziloDAO().vozilo(idVozila);
        ModelVozilaDTO modelVozila = DAOFactory.getDAOFactory().getModelVozilaDAO().model(vozilo.getIdVozilo());
    
        tfIdVozila.setEditable(false);
        spinnerKolicina.setValue(1);
        
        ArrayList<RadniNalogRadnikDTO> rnr = DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().radniciNaRadnomNalogu(this.idRadnogNaloga);
        DefaultListModel<ZaposleniDTO> modelZaposleni = new DefaultListModel<>();
                
        for(RadniNalogRadnikDTO r : rnr){
            ZaposleniDTO z = DAOFactory.getDAOFactory().getZaposleniDAO().zaposleni(r.getIdRadnik());
            modelZaposleni.addElement(z);              
        }
        
         listaZaposleniZaduzeni.setModel(modelZaposleni);
         listaZaposleniZaduzeni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         
         DefaultListModel<ZaposleniDTO> modelOstaliZaposleni = new DefaultListModel<>();

         
         ArrayList<ZaposleniDTO> sviAktivniZaposleni = (ArrayList<ZaposleniDTO>) DAOFactory.getDAOFactory().getZaposleniDAO().sviZaposleni();
         for(ZaposleniDTO z : sviAktivniZaposleni){
             if(!((DefaultListModel<ZaposleniDTO>)listaZaposleniZaduzeni.getModel()).contains(z)){
                 modelOstaliZaposleni.addElement(z);
             }
         }
         
         listaZaposleni.setModel(modelOstaliZaposleni);
         listaZaposleni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         
         //za tabelu dostupnih dijelova
        String[] columns = {"Naziv","Šifra","Godište vozila", "Novo", "Vrsta goriva", "Dostupna količina" ,"Cijena"};
        DefaultTableModel modelDijelovi = new DefaultTableModel(columns, 0);
        tabelaDijelovi.setModel(modelDijelovi);
        

        List<DioDTO> listaDijelova=null;
         
        if(null != vozilo.getVrstaGoriva()){
           listaDijelova = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(vozilo.getVrstaGoriva(), modelVozila.getMarka(), modelVozila.getModel(), true);
        }
        else{
             listaDijelova = DAOFactory.getDAOFactory().getDioDAO().getDijelovi(modelVozila.getMarka(), modelVozila.getModel());
        }

        List<DioDTO> listaZaSvaVozila = DAOFactory.getDAOFactory().getDioDAO().getDijeloviZaSvaVozila();
        
        for(DioDTO d: listaDijelova){
            Object[] rowData = { d.getNaziv(), d.getSifra(), d.getGodisteVozila(), d.getNovo()==true?"Da":"Ne", d.getVrstaGoriva(), d.getKolicina(), d.getTrenutnaCijena()};
            modelDijelovi.addRow(rowData);
       }
        
        for(DioDTO d: listaZaSvaVozila){
            Object[] rowData = { d.getNaziv(), d.getSifra(), d.getGodisteVozila(), d.getNovo()==true?"Da":"Ne", d.getVrstaGoriva(), d.getKolicina(), d.getTrenutnaCijena()};
            modelDijelovi.addRow(rowData);
       }
        
        tabelaDijelovi.setModel(modelDijelovi); 
        //----------------
        
        //--vec dodati dijelovi u radni nalog
        List<RadniNalogDioDTO> listaDodatihDijelova = DAOFactory.getDAOFactory().getRadniNalogDioDAO().radniNalogDioIdRadniNalog(rn.getIdRadniNalog());
        String[] kolone = {"Naziv","Šifra", "Količina" ,"Cijena"};
        DefaultTableModel modelDodatihDijelova = new DefaultTableModel(kolone, 0);
        
        for(RadniNalogDioDTO rndDTO : listaDodatihDijelova){
            DioDTO dio = DAOFactory.getDAOFactory().getDioDAO().getDio(rndDTO.getIdDio());
            Object[] rowData = { dio.getNaziv(), dio.getSifra(), rndDTO.getKolicina(), rndDTO.getCijena()};
            modelDodatihDijelova.addRow(rowData);
        }
        
        tabelaIzabraniDijelovi.setModel(modelDodatihDijelova);
         

       tabelaDijelovi.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent event) {
        if (tabelaDijelovi.getSelectedRow() > -1) {
            Double cijena = Double.parseDouble(tabelaDijelovi.getValueAt(tabelaDijelovi.getSelectedRow(), 6).toString());
            tfCijenaDijela.setText(cijena.toString());
        }
    }
});
        
      
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfIdVozila = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfTroskovi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tfKilometraza = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tfCijena = new javax.swing.JTextField();
        cbPlaceno = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        taProblem = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnDodajDio = new javax.swing.JButton();
        Zaduzeni = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaZaposleni = new javax.swing.JList<>();
        btnRazduzi = new javax.swing.JButton();
        datumOtvaranjaNaloga = new com.toedter.calendar.JDateChooser();
        datumZatvaranjaNaloga = new com.toedter.calendar.JDateChooser();
        potrebnoZavrsitiDo = new com.toedter.calendar.JDateChooser();
        btnAzurirajNalog = new javax.swing.JButton();
        spinnerKolicina = new javax.swing.JSpinner();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelaIzabraniDijelovi = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelaDijelovi = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        btnUkloniDio = new javax.swing.JButton();
        btnZaduzi = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaZaposleniZaduzeni = new javax.swing.JList<>();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        taUlogaRadnika = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        tfCijenaDijela = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Izmijeni radni nalog");

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));

        jPanel2.setBackground(new java.awt.Color(102, 153, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Novi radni nalog:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Id vozila:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Datum otvaranja naloga:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Datum završetka:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Troškovi:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Kilometraža:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Potrebno završiti do:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Cijena usluge:");

        cbPlaceno.setBackground(new java.awt.Color(102, 153, 255));
        cbPlaceno.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbPlaceno.setForeground(new java.awt.Color(240, 240, 240));
        cbPlaceno.setText("Plaćeno");

        taProblem.setColumns(20);
        taProblem.setRows(5);
        taProblem.setWrapStyleWord(true);
        jScrollPane1.setViewportView(taProblem);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Opis problema:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(240, 240, 240));
        jLabel10.setText("Lista dijelova:");

        btnDodajDio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDodajDio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/add (1).png"))); // NOI18N
        btnDodajDio.setText("Dodaj");
        btnDodajDio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajDioActionPerformed(evt);
            }
        });

        Zaduzeni.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Zaduzeni.setForeground(new java.awt.Color(240, 240, 240));
        Zaduzeni.setText("Zaduženi radnici:");

        listaZaposleni.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listaZaposleniValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(listaZaposleni);

        btnRazduzi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRazduzi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/fast-backward-double-left-arrow-symbol.png"))); // NOI18N
        btnRazduzi.setText("Razduži radnika");
        btnRazduzi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRazduziActionPerformed(evt);
            }
        });

        datumOtvaranjaNaloga.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                datumOtvaranjaNalogaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                datumOtvaranjaNalogaFocusLost(evt);
            }
        });

        btnAzurirajNalog.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnAzurirajNalog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/add-documents.png"))); // NOI18N
        btnAzurirajNalog.setText("Ažuriraj nalog");
        btnAzurirajNalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAzurirajNalogActionPerformed(evt);
            }
        });

        spinnerKolicina.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        tabelaIzabraniDijelovi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Naziv", "Šifra", "Količina", "Cijena"
            }
        ));
        jScrollPane4.setViewportView(tabelaIzabraniDijelovi);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Količina:");

        tabelaDijelovi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(tabelaDijelovi);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Izabrani dijelovi:");

        btnUkloniDio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnUkloniDio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/eraser.png"))); // NOI18N
        btnUkloniDio.setText("Ukloni dio");
        btnUkloniDio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUkloniDioActionPerformed(evt);
            }
        });

        btnZaduzi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnZaduzi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/fast-forward.png"))); // NOI18N
        btnZaduzi.setText("Zaduži radnika");
        btnZaduzi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZaduziActionPerformed(evt);
            }
        });

        listaZaposleniZaduzeni.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listaZaposleniZaduzeniValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(listaZaposleniZaduzeni);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Svi radnici");

        taUlogaRadnika.setEditable(false);
        taUlogaRadnika.setBackground(new java.awt.Color(204, 204, 204));
        taUlogaRadnika.setColumns(20);
        taUlogaRadnika.setLineWrap(true);
        taUlogaRadnika.setRows(5);
        taUlogaRadnika.setWrapStyleWord(true);
        jScrollPane6.setViewportView(taUlogaRadnika);

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Uloga radnika:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Cijena dijela:");

        tfCijenaDijela.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel4)
                                                .addComponent(jLabel5)
                                                .addComponent(jLabel6)
                                                .addComponent(jLabel7)
                                                .addComponent(jLabel8))
                                            .addGap(29, 29, 29)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(cbPlaceno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(tfIdVozila)
                                                .addComponent(tfTroskovi)
                                                .addComponent(tfKilometraza)
                                                .addComponent(tfCijena)
                                                .addComponent(datumOtvaranjaNaloga, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                                                .addComponent(datumZatvaranjaNaloga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(potrebnoZavrsitiDo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addComponent(btnAzurirajNalog, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(spinnerKolicina, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnDodajDio, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 656, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(66, 66, 66))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUkloniDio))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(220, 220, 220)
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(tfCijenaDijela, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(jLabel11))
                            .addComponent(jLabel12))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnZaduzi, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRazduzi))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Zaduzeni)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGap(66, 66, 66))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfIdVozila, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel4))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(datumOtvaranjaNaloga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(datumZatvaranjaNaloga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfTroskovi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(tfKilometraza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(potrebnoZavrsitiDo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spinnerKolicina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDodajDio)
                            .addComponent(jLabel11)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15)
                                .addComponent(tfCijenaDijela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnUkloniDio)
                        .addGap(56, 56, 56))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tfCijena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addGap(11, 11, 11)
                                .addComponent(cbPlaceno)
                                .addGap(59, 59, 59)
                                .addComponent(jLabel9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Zaduzeni)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2)
                                    .addComponent(jScrollPane3))
                                .addGap(59, 59, 59))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnZaduzi)
                                .addGap(4, 4, 4)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRazduzi))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAzurirajNalog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1055, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDodajDioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajDioActionPerformed

        int red = tabelaDijelovi.getSelectedRow();
        
        if(red < 0){
            JOptionPane.showMessageDialog(rootPane, "Izaberite dio koji želite dodati!", "Greška", JOptionPane.OK_OPTION);
            return;
        }
        
        String sifra = tabelaDijelovi.getModel().getValueAt(red, 1).toString();
        int dodajKolicinu = (Integer)spinnerKolicina.getValue();

        String naziv = tabelaDijelovi.getModel().getValueAt(red, 0).toString();
        Integer kolicina = Integer.parseInt(tabelaDijelovi.getModel().getValueAt(red, 5).toString());
        //Double cijena = Double.parseDouble(tabelaDijelovi.getModel().getValueAt(red, 6).toString());
        Double cijena = Double.parseDouble(tfCijenaDijela.getText());
        boolean postoji = false;

        if(dodajKolicinu > kolicina){
            JOptionPane.showMessageDialog(rootPane, "Nema toliko jedinica izabranog dijela na stanju!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        for(int i = 0; i < tabelaIzabraniDijelovi.getRowCount(); i++){
            if(sifra.equals(tabelaIzabraniDijelovi.getModel().getValueAt(i, 1))){
                tabelaIzabraniDijelovi.getModel().setValueAt((Integer)tabelaIzabraniDijelovi.getModel().getValueAt(i, 2) + dodajKolicinu, i, 2);
                postoji = true;
            }
        }

        if(!postoji){

            DefaultTableModel dtm = (DefaultTableModel) tabelaIzabraniDijelovi.getModel();

            Object[] rowData = { naziv, sifra, dodajKolicinu, cijena};
            dtm.addRow(rowData);
        }

        if(tfTroskovi.getText() != null && !"".equals(tfTroskovi.getText())){
            Double trenutnaCijena = Double.parseDouble(tfTroskovi.getText());
            trenutnaCijena += dodajKolicinu*cijena;
            trenutnaCijena = Math.round(trenutnaCijena * 100.0) / 100.0;
            tfTroskovi.setText(trenutnaCijena.toString());
        }
        else{
            Double trenutnaCijena = 0.0;
            trenutnaCijena += dodajKolicinu*cijena;
            trenutnaCijena = Math.round(trenutnaCijena * 100.0) / 100.0;
            tfTroskovi.setText(trenutnaCijena.toString());
        }

        tabelaDijelovi.getModel().setValueAt(Integer.parseInt(tabelaDijelovi.getModel().getValueAt(red, 5).toString()) - dodajKolicinu,red, 5);
        spinnerKolicina.setValue(1);
    }//GEN-LAST:event_btnDodajDioActionPerformed

    private void listaZaposleniValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listaZaposleniValueChanged
        if(listaZaposleni.getSelectedIndex() >= 0){
            taUlogaRadnika.setEditable(true);
            taUlogaRadnika.setBackground(Color.WHITE);
        }
        else{
            taUlogaRadnika.setEditable(false);
            taUlogaRadnika.setBackground(Color.GRAY);
        }
    }//GEN-LAST:event_listaZaposleniValueChanged

    private void btnRazduziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRazduziActionPerformed
        int brojReda = listaZaposleniZaduzeni.getSelectedIndex();

        if(brojReda < 0)
        {
            JOptionPane.showMessageDialog(rootPane, "Morate odabrati zaposlenog iz liste zaduženih radnika!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        ZaposleniDTO red = listaZaposleniZaduzeni.getSelectedValue();

        ((DefaultListModel<ZaposleniDTO>)listaZaposleniZaduzeni.getModel()).removeElementAt(brojReda);

        DefaultListModel<ZaposleniDTO> mod = (DefaultListModel<ZaposleniDTO>) listaZaposleni.getModel();
        mod.addElement(red);
    }//GEN-LAST:event_btnRazduziActionPerformed

    private void btnAzurirajNalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAzurirajNalogActionPerformed
        Date datumOtvaranja = datumOtvaranjaNaloga.getDate();
        java.sql.Date o = new java.sql.Date(datumOtvaranja.getTime());

        java.sql.Date z = null;
        Date datumZatvaranja = datumZatvaranjaNaloga.getDate();
        try{
            z = new java.sql.Date(datumZatvaranja.getTime());
        }
        catch(Exception ex){   }

        Date potrebnoZavristi = potrebnoZavrsitiDo.getDate();
        java.sql.Date pz = new java.sql.Date(potrebnoZavristi.getTime());

        Double troskovi = 0.0;
        Integer kilometraza = 0;
        Double cijena = 0.0;
        Boolean placeno = cbPlaceno.isSelected();

        if(tfTroskovi.getText() != null && !"".equals(tfTroskovi.getText())){
            try{
                troskovi = new Double(tfTroskovi.getText());
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(rootPane, "Troškovi moraju biti realan broj.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        if(tfKilometraza.getText() != null && !"".equals(tfKilometraza.getText())){
            try{
                kilometraza = new Integer(tfKilometraza.getText());
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(rootPane, "Kilometraža mora biti cjelobrojni podatak.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        if(tfCijena.getText() != null && !"".equals(tfCijena.getText())){
            try{
                cijena = new Double(tfCijena.getText());
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(rootPane, "Cijena mora biti realan broj.", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }

        String opisProblema = taProblem.getText();

        RadniNalogDTO rn = new RadniNalogDTO(placeno, o, z, this.idVozila, troskovi, kilometraza, pz, cijena);
        rn.setOpisProblema(opisProblema);
        rn.setIdRadniNalog(this.idRadnogNaloga);
        
        DAOFactory.getDAOFactory().getRadniNalogDAO().azurirajRadniNalog(rn);

        if(true){
            int idRadnogNaloga = rn.getIdRadniNalog();
            
            //brisi radnike izmjena;
            ArrayList<RadniNalogRadnikDTO> listaPrethodnih = DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().radniciNaRadnomNalogu(nalog.getIdRadniNalog());
            
            for(int i = 0; i < listaPrethodnih.size(); i++){
                               
                ZaposleniDTO zaposleni = DAOFactory.getDAOFactory().getZaposleniDAO().zaposleni(listaPrethodnih.get(i).getIdRadnik());
                
                if(!((DefaultListModel<ZaposleniDTO>)listaZaposleniZaduzeni.getModel()).contains(zaposleni)){
                        
                        DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().izbrisi(listaPrethodnih.get(i));
                    }
                
            }

            //dodaj radnike izmjena
            for(int i = 0; i < listaZaposleniZaduzeni.getModel().getSize(); i++){
                ZaposleniDTO zaposleni = listaZaposleniZaduzeni.getModel().getElementAt(i);
                RadniNalogRadnikDTO rnr = new RadniNalogRadnikDTO(this.idRadnogNaloga, zaposleni.getIdRadnik(), zaposleni.getUlogaRadnika());
       
                if(!DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().postojiLiZapis(this.idRadnogNaloga, zaposleni.getIdRadnik())){
                    DAOFactory.getDAOFactory().getRadniNalogRadnikDAO().dodajRadniNalogRadnik(rnr);
                }

            }
            
            //dijelovi
            
            //brisanje izbrisanih
            ArrayList<RadniNalogDioDTO> prethodniRND = DAOFactory.getDAOFactory().getRadniNalogDioDAO().radniNalogDioIdRadniNalog(this.idRadnogNaloga);
            for(RadniNalogDioDTO rnd : prethodniRND){
                
                DioDTO dioDTO = DAOFactory.getDAOFactory().getDioDAO().getDio(rnd.getIdDio());
                
                boolean pronadjen = false;
                for(int i = 0; i < tabelaIzabraniDijelovi.getModel().getRowCount(); i++){
                    if(dioDTO.getSifra().equals(tabelaIzabraniDijelovi.getModel().getValueAt(i, 1))){
                        pronadjen = true;
                        //rnd.setKolicina(new Integer(((DefaultTableModel)tabelaIzabraniDijelovi.getModel()).getValueAt(i, 2).toString()));
                    }
                }
                
                if(pronadjen == false){
                    dioDTO.setKolicina(dioDTO.getKolicina() + rnd.getKolicina());
                    DAOFactory.getDAOFactory().getDioDAO().azurirajDio(dioDTO);
                    DAOFactory.getDAOFactory().getRadniNalogDioDAO().obrisiRadniNalogDio(this.idRadnogNaloga, dioDTO.getId());
                }
            }
            
            
            //dodavanje novih
            for(int i = 0; i < tabelaIzabraniDijelovi.getModel().getRowCount(); i++){
                String sifraDijela = ((DefaultTableModel)tabelaIzabraniDijelovi.getModel()).getValueAt(i, 1).toString();
                DioDTO dio = DAOFactory.getDAOFactory().getDioDAO().getDio(sifraDijela);
                
                RadniNalogDioDTO rnd = DAOFactory.getDAOFactory().getRadniNalogDioDAO().radniNalogDio(this.idRadnogNaloga, dio.getId());
                if(rnd != null){
                    
                    int novoStanje = rnd.getKolicina() - new Integer(((DefaultTableModel)tabelaIzabraniDijelovi.getModel()).getValueAt(i, 2).toString());
                    dio.setKolicina(dio.getKolicina() + novoStanje);
                    DAOFactory.getDAOFactory().getDioDAO().azurirajDio(dio);

                    rnd.setKolicina(new Integer(((DefaultTableModel)tabelaIzabraniDijelovi.getModel()).getValueAt(i, 2).toString()));
                    rnd.setCijena(new Double(((DefaultTableModel)tabelaIzabraniDijelovi.getModel()).getValueAt(i, 3).toString()));
                    DAOFactory.getDAOFactory().getRadniNalogDioDAO().azurirajRadniNalogDio(rnd);
                }
                else{
                    RadniNalogDioDTO noviRadniNalogDio = new RadniNalogDioDTO();
                    noviRadniNalogDio.setIdRadniNalog(this.idRadnogNaloga);
                    noviRadniNalogDio.setIdDio(dio.getId());
                    noviRadniNalogDio.setKolicina(new Integer(((DefaultTableModel)tabelaIzabraniDijelovi.getModel()).getValueAt(i, 2).toString()));
                    noviRadniNalogDio.setCijena(new Double(((DefaultTableModel)tabelaIzabraniDijelovi.getModel()).getValueAt(i, 3).toString()));
                    DAOFactory.getDAOFactory().getRadniNalogDioDAO().dodajRadniNalogDio(noviRadniNalogDio);
                }
            }

//            DefaultTableModel modelDio = (DefaultTableModel) tabelaIzabraniDijelovi.getModel();
//            for(int j = 0; j < modelDio.getRowCount(); j++){
//                String sifraDio = tabelaIzabraniDijelovi.getModel().getValueAt(j, 1).toString();
//                Integer kolicinaDio = Integer.parseInt(tabelaIzabraniDijelovi.getModel().getValueAt(j, 2).toString());
//                Double cijenaDio = Double.parseDouble(tabelaIzabraniDijelovi.getModel().getValueAt(j, 3).toString());
//
//                RadniNalogDioDTO rnd = new RadniNalogDioDTO();
//                DioDTO dDTO =DAOFactory.getDAOFactory().getDioDAO().getDio(sifraDio);
//                rnd.setIdRadniNalog(idRadnogNaloga);
//                rnd.setIdDio(dDTO.getId());
//                rnd.setKolicina(kolicinaDio);
//                rnd.setCijena(cijenaDio);
//
//                DAOFactory.getDAOFactory().getRadniNalogDioDAO().dodajRadniNalogDio(rnd);
//            }

            
             //azuriranje kolicine dijelova
            for(int k = 0; k < tabelaDijelovi.getModel().getRowCount(); k++){
                String sifra = tabelaDijelovi.getModel().getValueAt(k, 1).toString();
                Integer novaKolicina = Integer.parseInt(tabelaDijelovi.getModel().getValueAt(k, 5).toString());
                DioDTO dio = DAOFactory.getDAOFactory().getDioDAO().getDio(sifra);
                dio.setKolicina(novaKolicina);
                
                DAOFactory.getDAOFactory().getDioDAO().azurirajDio(dio);
            }
            
            
            
            JOptionPane.showMessageDialog(rootPane, "Uspješno ažuriran radni nalog", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Greška", "Greška", JOptionPane.OK_OPTION);
            return;
        }
    }//GEN-LAST:event_btnAzurirajNalogActionPerformed

    private void btnUkloniDioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUkloniDioActionPerformed
        int red = tabelaIzabraniDijelovi.getSelectedRow();

        if(red < 0){
            JOptionPane.showMessageDialog(rootPane, "Morate odabrati dio u tabeli koji želite ukloniti!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        Integer kolicina = Integer.parseInt(tabelaIzabraniDijelovi.getModel().getValueAt(red, 2).toString());
        String sifra = tabelaIzabraniDijelovi.getModel().getValueAt(red, 1).toString();
        boolean pronasao = false;
        Double cijenaJednogDijela=0.0;
        
                        cijenaJednogDijela = Double.parseDouble(tabelaIzabraniDijelovi.getModel().getValueAt(red, 3).toString());


        for(int i = 0; i < tabelaDijelovi.getRowCount() && pronasao==false; i++){
            if(sifra.equals(tabelaDijelovi.getModel().getValueAt(i, 1))){
                pronasao = true;
                tabelaDijelovi.getModel().setValueAt(Integer.parseInt(tabelaDijelovi.getModel().getValueAt(i, 5).toString()) + kolicina, i, 5);
              //  cijenaJednogDijela = Double.parseDouble(tabelaDijelovi.getModel().getValueAt(i, 6).toString());
            }
        }

        Double cijena = Double.parseDouble(tfTroskovi.getText());
        Double novaCijena = cijena-kolicina*cijenaJednogDijela;
        tfTroskovi.setText(novaCijena.toString());

        if(novaCijena < 0){
            tfTroskovi.setText(new String("0"));
        }

        ((DefaultTableModel)tabelaIzabraniDijelovi.getModel()).removeRow(red);
    }//GEN-LAST:event_btnUkloniDioActionPerformed

    private void btnZaduziActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZaduziActionPerformed
        int brojReda = listaZaposleni.getSelectedIndex();

        if(brojReda < 0)
        {
            JOptionPane.showMessageDialog(rootPane, "Morate odabrati zaposlenog iz liste svih radnika!", "Greška", JOptionPane.OK_OPTION);
            return;
        }

        ZaposleniDTO red = listaZaposleni.getSelectedValue();
        red.setUlogaRadnika(taUlogaRadnika.getText());

        ((DefaultListModel<ZaposleniDTO>)listaZaposleni.getModel()).removeElementAt(brojReda);

        DefaultListModel<ZaposleniDTO> mod = (DefaultListModel<ZaposleniDTO>) listaZaposleniZaduzeni.getModel();
        mod.addElement(red);

        taUlogaRadnika.setText("");
    }//GEN-LAST:event_btnZaduziActionPerformed

    private void listaZaposleniZaduzeniValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listaZaposleniZaduzeniValueChanged

    }//GEN-LAST:event_listaZaposleniZaduzeniValueChanged
    
    private void datumOtvaranjaNalogaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_datumOtvaranjaNalogaFocusGained
      
    }//GEN-LAST:event_datumOtvaranjaNalogaFocusGained

    private void datumOtvaranjaNalogaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_datumOtvaranjaNalogaFocusLost
        
    }//GEN-LAST:event_datumOtvaranjaNalogaFocusLost

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
            java.util.logging.Logger.getLogger(IzmijeniRadniNalogDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IzmijeniRadniNalogDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IzmijeniRadniNalogDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IzmijeniRadniNalogDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IzmijeniRadniNalogDialog dialog = new IzmijeniRadniNalogDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Zaduzeni;
    private javax.swing.JButton btnAzurirajNalog;
    private javax.swing.JButton btnDodajDio;
    private javax.swing.JButton btnRazduzi;
    private javax.swing.JButton btnUkloniDio;
    private javax.swing.JButton btnZaduzi;
    private javax.swing.JCheckBox cbPlaceno;
    private com.toedter.calendar.JDateChooser datumOtvaranjaNaloga;
    private com.toedter.calendar.JDateChooser datumZatvaranjaNaloga;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JList<ZaposleniDTO> listaZaposleni;
    private javax.swing.JList<ZaposleniDTO> listaZaposleniZaduzeni;
    private com.toedter.calendar.JDateChooser potrebnoZavrsitiDo;
    private javax.swing.JSpinner spinnerKolicina;
    private javax.swing.JTextArea taProblem;
    private javax.swing.JTextArea taUlogaRadnika;
    private javax.swing.JTable tabelaDijelovi;
    private javax.swing.JTable tabelaIzabraniDijelovi;
    private javax.swing.JTextField tfCijena;
    private javax.swing.JTextField tfCijenaDijela;
    private javax.swing.JTextField tfIdVozila;
    private javax.swing.JTextField tfKilometraza;
    private javax.swing.JTextField tfTroskovi;
    // End of variables declaration//GEN-END:variables
}
