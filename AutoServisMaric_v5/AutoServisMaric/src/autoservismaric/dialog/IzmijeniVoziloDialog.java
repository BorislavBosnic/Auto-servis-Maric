/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoservismaric.dialog;

import data.AutoSuggestor;
import data.dao.DAOFactory;
import data.dto.KupacDTO;
import data.dto.ModelVozilaDTO;
import data.dto.VoziloDTO;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import poslovnalogika.VozilaLogika;

/**
 *
 * @author DulleX
 */
public class IzmijeniVoziloDialog extends javax.swing.JDialog {

    private int idVozila;
    ButtonGroup bg;
    public AutoSuggestor marke;
    public AutoSuggestor model;
    
    /**
     * Creates new form IzmijeniVoziloDialog
     */
    public IzmijeniVoziloDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public IzmijeniVoziloDialog(java.awt.Frame parent, boolean modal, int idVozila) {
        this(parent, modal);
        this.idVozila = idVozila;
                
        VoziloDTO vozilo = DAOFactory.getDAOFactory().getVoziloDAO().vozilo(idVozila);
        ModelVozilaDTO model = DAOFactory.getDAOFactory().getModelVozilaDAO().model(vozilo.getIdModelVozila());
        KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(vozilo.getIdKupac());
        
        bg = new ButtonGroup();
        bg.add(rbPravni);
        bg.add(rbPrivatni);
        
        tfRegistracija.setText(vozilo.getBrojRegistracije());
        tfMarka.setText(model.getMarka());
        tfModel.setText(model.getModel());
        tfGodiste.setText(vozilo.getGodiste().toString());
        tfKilovat.setText(vozilo.getKilovat().toString());
        tfKubikaza.setText(vozilo.getKubikaza().toString());
        if(kupac.getNaziv() == null){
            rbPrivatni.setSelected(true);
            tfVlasnikIme.setEditable(true);
            tfVlasnikPrezime.setEditable(true);
            tfVlasnikNaziv.setEditable(false);
            
            tfVlasnikNaziv.setText("");
            tfVlasnikNaziv.setBackground(Color.gray);
            
            tfVlasnikIme.setText(kupac.getIme());
            tfVlasnikPrezime.setText(kupac.getPrezime());
            tfVlasnikIme.setBackground(Color.white);
            tfVlasnikPrezime.setBackground(Color.white);
        }
        else{
            rbPravni.setSelected(true);
           
            tfVlasnikIme.setEditable(false);
            tfVlasnikPrezime.setEditable(false);
            tfVlasnikNaziv.setEditable(true);
            
            tfVlasnikNaziv.setText(kupac.getNaziv());
            tfVlasnikNaziv.setBackground(Color.white);
            
            tfVlasnikIme.setText("");
            tfVlasnikPrezime.setText("");
            tfVlasnikIme.setBackground(Color.gray);
            tfVlasnikPrezime.setBackground(Color.gray);
            
        }
        
        marke = ucitajPreporukeMarke();
        this.model = ucitajPreporukeModel();
        
    }
    
    public AutoSuggestor ucitajPreporukeMarke(){
        
        AutoSuggestor autoSuggestorMarke = new AutoSuggestor(tfMarka, this, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {
                

                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> modeli = new ArrayList<>();
                
                ArrayList<ModelVozilaDTO> lista = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();
                
                VozilaLogika.modeli = lista;

                for(ModelVozilaDTO mv : lista){
                    if(!modeli.contains(mv.getMarka()))
                        modeli.add(mv.getMarka());
                }
                
                setDictionary(modeli);
                //addToDictionary("bye");//adds a single word

                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
            }
        };
        return autoSuggestorMarke;
   }
    
     public AutoSuggestor ucitajPreporukeModel(){
        
        AutoSuggestor autoSuggestorModel = new AutoSuggestor(tfModel, this, null, Color.BLUE.brighter(), Color.WHITE, Color.RED, 0.75f) {
            @Override
            public boolean wordTyped(String typedWord) {

                String marka = tfMarka.getText();
                
                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
                ArrayList<String> modeli = new ArrayList<>();
                
                ArrayList<ModelVozilaDTO> lista = DAOFactory.getDAOFactory().getModelVozilaDAO().sviModeli();

                VozilaLogika.modeli = lista;
                
                for(ModelVozilaDTO mv : lista){
                    if(tfModel.getText() != null && !"".equals(tfModel.getText())){
                        if(mv.getMarka().equals(marka.trim())){
                            modeli.add(mv.getModel());
                        }
                    }
                    else if(tfModel.getText() == null || "".equals(tfModel.getText())){
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelDodajVozilo = new javax.swing.JPanel();
        tfRegistracija = new javax.swing.JTextField();
        tfMarka = new javax.swing.JTextField();
        tfGodiste = new javax.swing.JTextField();
        jLabel98 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel102 = new javax.swing.JLabel();
        tfVlasnikIme = new javax.swing.JTextField();
        scrollPane1 = new java.awt.ScrollPane();
        tfKubikaza = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        tfGorivo = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        rbPrivatni = new javax.swing.JRadioButton();
        rbPravni = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        tfVlasnikPrezime = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnDodaj = new javax.swing.JButton();
        btnOdustani = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tfVlasnikNaziv = new javax.swing.JTextField();
        btnPrikaziSve = new javax.swing.JButton();
        btnAddModel = new javax.swing.JButton();
        tfModel = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnTraziVlasnika = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        btnDodajVlasnika = new javax.swing.JButton();
        lbPoruka = new javax.swing.JLabel();
        tfKilovat = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Izmijeni podatke o vozilu");

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));
        jPanel1.setForeground(new java.awt.Color(102, 153, 255));

        panelDodajVozilo.setBackground(new java.awt.Color(102, 153, 255));
        panelDodajVozilo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel98.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(255, 255, 255));
        jLabel98.setText("Registracija:");

        jLabel99.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(255, 255, 255));
        jLabel99.setText("Kilovat");

        jLabel100.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(255, 255, 255));
        jLabel100.setText("Tip vozila:");

        jLabel101.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(255, 255, 255));
        jLabel101.setText("Godiste:");

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setText("Dodaj");

        jLabel102.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(255, 255, 255));
        jLabel102.setText("Prezime:");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Kubikaza:");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Gorivo:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/add2.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        rbPrivatni.setBackground(new java.awt.Color(102, 153, 255));
        rbPrivatni.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPrivatni.setForeground(new java.awt.Color(255, 255, 255));
        rbPrivatni.setText("Privatno lice");
        rbPrivatni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPrivatniActionPerformed(evt);
            }
        });

        rbPravni.setBackground(new java.awt.Color(102, 153, 255));
        rbPravni.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPravni.setForeground(new java.awt.Color(255, 255, 255));
        rbPravni.setText("Pravno lice");
        rbPravni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPravniActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Vlasnik:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Ime:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Izmijeni podatke o vozilu:");

        btnDodaj.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDodaj.setText("Ažuriraj");
        btnDodaj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajActionPerformed(evt);
            }
        });

        btnOdustani.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnOdustani.setText("Odustani");
        btnOdustani.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOdustaniActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Naziv:");

        tfVlasnikNaziv.setEditable(false);

        btnPrikaziSve.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnPrikaziSve.setText("Prikaži sve vlasnike");
        btnPrikaziSve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrikaziSveActionPerformed(evt);
            }
        });

        btnAddModel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/add2.png"))); // NOI18N
        btnAddModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddModelActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Marka:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Model:");

        btnTraziVlasnika.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnTraziVlasnika.setText("Traži vlasnika");
        btnTraziVlasnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraziVlasnikaActionPerformed(evt);
            }
        });

        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tabela);

        btnDodajVlasnika.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDodajVlasnika.setText("Dodaj vlasnika");
        btnDodajVlasnika.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajVlasnikaActionPerformed(evt);
            }
        });

        lbPoruka.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout panelDodajVoziloLayout = new javax.swing.GroupLayout(panelDodajVozilo);
        panelDodajVozilo.setLayout(panelDodajVoziloLayout);
        panelDodajVoziloLayout.setHorizontalGroup(
            panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel98)
                            .addComponent(jLabel1)
                            .addComponent(jLabel102)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel100))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                        .addComponent(rbPrivatni)
                                        .addGap(18, 18, 18)
                                        .addComponent(rbPravni)
                                        .addGap(463, 463, 463)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                        .addComponent(lbPoruka)
                                        .addGap(100, 100, 100)
                                        .addComponent(btnOdustani)
                                        .addGap(31, 31, 31)
                                        .addComponent(btnDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(tfRegistracija, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel5)
                                                    .addComponent(jLabel6))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(tfModel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                                    .addComponent(tfMarka, javax.swing.GroupLayout.Alignment.TRAILING))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnAddModel)
                                        .addGap(47, 47, 47)
                                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel23)
                                            .addComponent(jLabel22)
                                            .addComponent(jLabel99)
                                            .addComponent(jLabel101))
                                        .addGap(18, 18, 18)
                                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tfGorivo)
                                            .addComponent(tfKubikaza)
                                            .addComponent(tfKilovat, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfGodiste)))
                                    .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                            .addGap(94, 94, 94)
                                            .addComponent(btnPrikaziSve))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton4)
                                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                        .addGap(55, 55, 55)
                                        .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfVlasnikNaziv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfVlasnikIme, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfVlasnikPrezime, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTraziVlasnika, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDodajVlasnika)))
                        .addGap(40, 40, 40))
                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        panelDodajVoziloLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfVlasnikIme, tfVlasnikNaziv, tfVlasnikPrezime});

        panelDodajVoziloLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfGodiste, tfGorivo, tfKilovat, tfKubikaza, tfRegistracija});

        panelDodajVoziloLayout.setVerticalGroup(
            panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfRegistracija, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel98))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddModel, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel100)
                                        .addComponent(jLabel5))
                                    .addComponent(tfMarka, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(tfModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(tfKilovat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfKubikaza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(tfGorivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addComponent(jLabel99)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel22)))))
                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(tfGodiste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel101)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbPrivatni)
                            .addComponent(rbPravni)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel102)
                                    .addComponent(tfVlasnikPrezime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)
                                        .addGap(3, 3, 3))
                                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                        .addComponent(tfVlasnikIme, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tfVlasnikNaziv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(btnTraziVlasnika, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDodajVlasnika, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 5, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                        .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDodajVoziloLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPrikaziSve, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(lbPoruka)
                                .addGap(0, 40, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDodajVoziloLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelDodajVoziloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnOdustani, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())))))
        );

        panelDodajVoziloLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tfVlasnikIme, tfVlasnikNaziv, tfVlasnikPrezime});

        panelDodajVoziloLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tfGodiste, tfGorivo, tfKilovat, tfKubikaza, tfRegistracija});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDodajVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, 673, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelDodajVozilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void rbPrivatniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPrivatniActionPerformed
        tfVlasnikIme.setEditable(true);
        tfVlasnikPrezime.setEditable(true);
        tfVlasnikNaziv.setEditable(false);
        tfVlasnikNaziv.setBackground(Color.gray);
        tfVlasnikIme.setBackground(Color.white);
        tfVlasnikPrezime.setBackground(Color.white);
        tfVlasnikIme.setText("");
        tfVlasnikPrezime.setText("");
        tfVlasnikNaziv.setText("");

        String[] columns = {"ID","Prezime", "Ime", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tabela.setModel(model);
    }//GEN-LAST:event_rbPrivatniActionPerformed

    private void rbPravniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPravniActionPerformed
        tfVlasnikIme.setEditable(false);
        tfVlasnikPrezime.setEditable(false);
        tfVlasnikNaziv.setEditable(true);
        tfVlasnikNaziv.setBackground(Color.white);
        tfVlasnikIme.setBackground(Color.gray);
        tfVlasnikPrezime.setBackground(Color.gray);
        tfVlasnikIme.setText("");
        tfVlasnikPrezime.setText("");
        tfVlasnikNaziv.setText("");

        String[] columns = {"ID","Naziv", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tabela.setModel(model);
    }//GEN-LAST:event_rbPravniActionPerformed

    private void btnDodajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajActionPerformed
        String registracija = tfRegistracija.getText();
        String marka = tfMarka.getText();
        String model = tfModel.getText();
        String gorivo = tfGorivo.getText();
        Integer godiste = 0, kilovati = 0;
        Double kubikaza = 0.0;
        try{
            godiste = Integer.parseInt(tfGodiste.getText());
        }
        catch(NumberFormatException e){
            lbPoruka.setText("Nevalidno godiste.");
            lbPoruka.setBackground(Color.red);
            return;
        }

        try{
            kilovati = Integer.parseInt(tfKilovat.getText());
        }
        catch(NumberFormatException e){
            lbPoruka.setText("Nevalidni kilovati.");
            lbPoruka.setBackground(Color.red);
            return;
        }

        try{
            kubikaza = Double.parseDouble(tfKubikaza.getText());
        }
        catch(NumberFormatException e){
            lbPoruka.setText("Nevalidna kubikaza.");
            lbPoruka.setBackground(Color.red);
            return;
        }

        if(registracija == null){
            lbPoruka.setText("Morate unijeti registraciju.");
            lbPoruka.setBackground(Color.red);
            return;
        }

        int column = 0;
        int row = tabela.getSelectedRow();
        Integer idVlasnik = Integer.parseInt(tabela.getModel().getValueAt(row, 0).toString());

        ModelVozilaDTO mv = DAOFactory.getDAOFactory().getModelVozilaDAO().model(marka, model);
        if(mv != null){
            VoziloDTO vozilo = new VoziloDTO();
            vozilo.setIdVozilo(idVozila);
            vozilo.setBrojRegistracije(registracija);
            vozilo.setGodiste(godiste);
            vozilo.setKilovat(kilovati);
            vozilo.setKubikaza(kubikaza);
            vozilo.setVrstaGoriva(gorivo);
            vozilo.setIdModelVozila(mv.getIdModelVozila());
            vozilo.setIdKupac(idVlasnik);

            if(DAOFactory.getDAOFactory().getVoziloDAO().azurirajVozilo(vozilo)){
                lbPoruka.setText("Uspješno");
                lbPoruka.setForeground(Color.green);
            }
            else{
                lbPoruka.setText("Neuspješno. Greška pri upisu u bazu.");
                lbPoruka.setBackground(Color.red);
                return;
            }
        }
        else{
            lbPoruka.setText("Greška sa modelom.");
            lbPoruka.setBackground(Color.red);
            return;
        }
    }//GEN-LAST:event_btnDodajActionPerformed

    private void btnOdustaniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOdustaniActionPerformed
        dispose();
    }//GEN-LAST:event_btnOdustaniActionPerformed

    private void btnPrikaziSveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziSveActionPerformed

        ArrayList<KupacDTO> kupci = VozilaLogika.vlasnici;

        bg.clearSelection();
        
        System.out.println(kupci.size());

        String[] columns = {"ID","Ime", "Prezime", "Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tabela.setModel(model);
        for(KupacDTO k : kupci){
            System.out.println(k.getIme() + k.getNaziv());
            Object[] rowData = { k.getIdKupac(), (k.getIme() == null) ? "---" : k.getIme(), (k.getPrezime() == null) ? "---" : k.getPrezime(), (k.getNaziv() == null) ? "---" : k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
            model.addRow(rowData);
        }

        tabela.setModel(model);
    }//GEN-LAST:event_btnPrikaziSveActionPerformed

    private void btnAddModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddModelActionPerformed
        new DodajModel(this, new JFrame(), true).setVisible(true);
    }//GEN-LAST:event_btnAddModelActionPerformed

    private void btnTraziVlasnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraziVlasnikaActionPerformed
        ArrayList<KupacDTO> kupci = VozilaLogika.vlasnici;
        if(rbPrivatni.isSelected()){
            String ime = tfVlasnikIme.getText();
            String prezime = tfVlasnikPrezime.getText();

            String[] columns = {"ID","Ime", "Prezime", "Telefon", "Adresa", "Grad"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            tabela.setModel(model);

            if((ime == null || "".equals(ime)) && (prezime == null || "".equals(prezime))){

            }
            else if((ime == null || "".equals(ime))){
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPrezime(prezime);
            }
            else if((prezime == null || "".equals(prezime))){
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciIme(ime);
            }
            else{
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPrivatni(ime, prezime);
            }

            for(KupacDTO k : kupci){
                Object[] rowData = { k.getIdKupac(),(k.getIme() == null) ? "---" : k.getIme(), (k.getPrezime() == null) ? "---" : k.getPrezime(), k.getTelefon(), k.getAdresa(), k.getGrad()};
                model.addRow(rowData);
                tabela.setModel(model);
            }

        }
        else if(rbPravni.isSelected()){
            String naziv = tfVlasnikNaziv.getText();

            String[] columns = {"ID","Naziv pravnog lica", "Telefon", "Adresa", "Grad"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            tabela.setModel(model);

            if(naziv == null || "".equals(naziv)){
                kupci = DAOFactory.getDAOFactory().getKupacDAO().sviPravni();
            }
            else{
                kupci = DAOFactory.getDAOFactory().getKupacDAO().kupciPravni(naziv);
            }

            for(KupacDTO k : kupci){
                Object[] rowData = { k.getIdKupac(), k.getNaziv(), k.getTelefon(), k.getAdresa(), k.getGrad()};
                model.addRow(rowData);
                tabela.setModel(model);
            }

        }
    }//GEN-LAST:event_btnTraziVlasnikaActionPerformed

    private void btnDodajVlasnikaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajVlasnikaActionPerformed
        new DodajVlasnikaDialog(new JFrame(), true).setVisible(true);
    }//GEN-LAST:event_btnDodajVlasnikaActionPerformed

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
            java.util.logging.Logger.getLogger(IzmijeniVoziloDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IzmijeniVoziloDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IzmijeniVoziloDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IzmijeniVoziloDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IzmijeniVoziloDialog dialog = new IzmijeniVoziloDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAddModel;
    private javax.swing.JButton btnDodaj;
    private javax.swing.JButton btnDodajVlasnika;
    private javax.swing.JButton btnOdustani;
    private javax.swing.JButton btnPrikaziSve;
    private javax.swing.JButton btnTraziVlasnika;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbPoruka;
    private javax.swing.JPanel panelDodajVozilo;
    private javax.swing.JRadioButton rbPravni;
    private javax.swing.JRadioButton rbPrivatni;
    private java.awt.ScrollPane scrollPane1;
    private javax.swing.JTable tabela;
    private javax.swing.JTextField tfGodiste;
    private javax.swing.JTextField tfGorivo;
    private javax.swing.JTextField tfKilovat;
    private javax.swing.JTextField tfKubikaza;
    private javax.swing.JTextField tfMarka;
    private javax.swing.JTextField tfModel;
    private javax.swing.JTextField tfRegistracija;
    private javax.swing.JTextField tfVlasnikIme;
    private javax.swing.JTextField tfVlasnikNaziv;
    private javax.swing.JTextField tfVlasnikPrezime;
    // End of variables declaration//GEN-END:variables
}
