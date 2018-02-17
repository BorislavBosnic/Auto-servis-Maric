/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoservismaric.dialog;

import static autoservismaric.forms.HomeForm1.selektovanRed;
import data.dao.DAOFactory;
import data.dto.KupacDTO;
import data.dto.ModelVozilaDTO;
import data.dto.RadniNalogDTO;
import data.dto.VoziloDTO;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DulleX
 */
public class PregledIstorijePopravkiDialog extends javax.swing.JDialog {

    private int idVozila;
    public static Integer IdRadnogNaloga;
    
    /**
     * Creates new form PregledIstorijePopravkiDialog
     */
    public PregledIstorijePopravkiDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        tabela.setDefaultEditor(Object.class, null);
         tabela.setAutoCreateRowSorter(true);
    }
    
    public PregledIstorijePopravkiDialog(java.awt.Frame parent, boolean modal, int idVozila) {
        this(parent, modal);
        this.idVozila = idVozila;
        
        tabela.setDefaultEditor(Object.class, null);
         tabela.setAutoCreateRowSorter(true);
        
        VoziloDTO vozilo = DAOFactory.getDAOFactory().getVoziloDAO().vozilo(idVozila);
        ModelVozilaDTO model = DAOFactory.getDAOFactory().getModelVozilaDAO().model(vozilo.getIdKupac());
        KupacDTO vlasnik = DAOFactory.getDAOFactory().getKupacDAO().kupac(vozilo.getIdKupac());
        
        setTitle("Istorija vozila");
        
        if(vlasnik.getNaziv() == null){
        lbVlasnik.setText(vlasnik.getIme() + " " + vlasnik.getPrezime() + " (Privatno lice)");
        }
        else{
            lbVlasnik.setText(vlasnik.getNaziv() + " (Pravno lice)");
        }
        
        lbAdresa.setText(vlasnik.getAdresa());
        lbTelefon.setText(vlasnik.getTelefon());
        lbGrad.setText(vlasnik.getGrad());
        
        lbBrojRegistracije.setText(vozilo.getBrojRegistracije());
        lbKilovati.setText(vozilo.getKilovat().toString());
        lbKubikaza.setText(vozilo.getKubikaza().toString());
        lbGodiste.setText(vozilo.getGodiste().toString());
        lbVrstaGoriva.setText(vozilo.getVrstaGoriva());
        lbModelVozila.setText(model.getMarka() + " " + model.getModel());
        
        ArrayList<RadniNalogDTO> rn = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi(this.idVozila);
       
        
        String[] columns = {"ID", "Datum otvaranja naloga", "Datum zatvaranja naloga", "Datum potrebnog završetka", "Plaćeno"};
        DefaultTableModel modell = new DefaultTableModel(columns, 0);
        
        
        //DefaultTableModel modell = new DefaultTableModel();
        
        for(RadniNalogDTO r : rn){
            String placeno;
            if(r.isPlaceno())
                placeno = "Da";
            else
                placeno = "Ne";
            Object[] rowData = {r.getIdRadniNalog(), r.getDatumOtvaranjaNaloga(), r.getDatumZatvaranjaNaloga(), r.getPredvidjenoVrijemeZavrsetka(), placeno};
           //System.out.println(r.getIdRadniNalog());
            modell.addRow(rowData);
        }
        
        tabela.setModel(modell);
        
        
        tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        public void valueChanged(ListSelectionEvent event) {
           if(tabela.getSelectedRow() >= 0){
               int column = 0;
               int row = tabela.getSelectedRow();
               //System.out.println(tabela.getModel().getValueAt(row, column).toString());
               Integer value = new Integer(tabela.getModel().getValueAt(row, column).toString());
               
               RadniNalogDTO r = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalog(value);
               
               lbSTATUS.setText(value.toString());
               
               tbIdNaloga.setText(value.toString());
               tbIdNaloga.setEditable(false);
               Date d = new Date(r.getDatumOtvaranjaNaloga().getTime());
               tbNalogOtvoren.setDate(d);
               
               Date dd = new Date(r.getDatumZatvaranjaNaloga().getTime());
               tbNalogZatvoren.setDate(dd);
               Date ddd = new Date(r.getPredvidjenoVrijemeZavrsetka().getTime());
               tbRokZatvaranja.setDate(ddd);
               
               if(r.isPlaceno())
                cbPlaceno.setSelected(true);
               else
                cbPlaceno.setSelected(false);
               
               tbTroskovi.setText(new Double(r.getTroskovi()).toString());
               tbCijena.setText(new Double(r.getCijenaUsluge()).toString());
               taProblem.setText(r.getOpisProblema());
               //radnika treba namjestiti i autosuggestor za radnik
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

        panelGlavni = new javax.swing.JPanel();
        panelTabela = new javax.swing.JPanel();
        skrolPejn = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lbBrojRegistracije = new javax.swing.JLabel();
        lbVrstaGoriva = new javax.swing.JLabel();
        lbModelVozila = new javax.swing.JLabel();
        lbKubikaza = new javax.swing.JLabel();
        lbKilovati = new javax.swing.JLabel();
        lbVlasnik = new javax.swing.JLabel();
        lbTelefon = new javax.swing.JLabel();
        lbAdresa = new javax.swing.JLabel();
        lbGrad = new javax.swing.JLabel();
        lbGodiste = new javax.swing.JLabel();
        panelAzurirajRadniNalog = new javax.swing.JPanel();
        tbIdNaloga = new javax.swing.JTextField();
        tbTroskovi = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cbPlaceno = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        tbCijena = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        taProblem = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        tfRadnik = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnAzuriraj = new javax.swing.JButton();
        tbNalogOtvoren = new com.toedter.calendar.JDateChooser();
        tbNalogZatvoren = new com.toedter.calendar.JDateChooser();
        tbRokZatvaranja = new com.toedter.calendar.JDateChooser();
        lbSTATUS = new javax.swing.JLabel();
        lbSTATUSVozilo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelGlavni.setBackground(new java.awt.Color(102, 153, 255));

        panelTabela.setBackground(new java.awt.Color(102, 153, 255));
        panelTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Datum otvaranja naloga", "Datum zatvaranja naloga", "Datum potrebnog završetka", "Placeno"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabela.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        skrolPejn.setViewportView(tabela);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Broj registracije:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Godište:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Vrsta goriva:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Model vozila:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Vlasnik:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Telefon:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Adresa:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Grad:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Podaci o vozilu");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Podaci o vlasniku vozila");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Kilovati:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Kubikaža:");

        lbBrojRegistracije.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbBrojRegistracije.setForeground(new java.awt.Color(255, 0, 0));
        lbBrojRegistracije.setText("jLabel13");

        lbVrstaGoriva.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbVrstaGoriva.setForeground(new java.awt.Color(255, 0, 0));
        lbVrstaGoriva.setText("jLabel14");

        lbModelVozila.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbModelVozila.setForeground(new java.awt.Color(255, 0, 0));
        lbModelVozila.setText("jLabel15");

        lbKubikaza.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbKubikaza.setForeground(new java.awt.Color(255, 0, 0));
        lbKubikaza.setText("jLabel14");

        lbKilovati.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbKilovati.setForeground(new java.awt.Color(255, 0, 0));
        lbKilovati.setText("jLabel13");

        lbVlasnik.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbVlasnik.setForeground(new java.awt.Color(255, 0, 0));
        lbVlasnik.setText("jLabel13");

        lbTelefon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbTelefon.setForeground(new java.awt.Color(255, 0, 0));
        lbTelefon.setText("jLabel15");

        lbAdresa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbAdresa.setForeground(new java.awt.Color(255, 0, 0));
        lbAdresa.setText("jLabel16");

        lbGrad.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbGrad.setForeground(new java.awt.Color(255, 0, 0));
        lbGrad.setText("jLabel17");

        lbGodiste.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbGodiste.setForeground(new java.awt.Color(255, 0, 0));
        lbGodiste.setText("jLabel13");

        javax.swing.GroupLayout panelTabelaLayout = new javax.swing.GroupLayout(panelTabela);
        panelTabela.setLayout(panelTabelaLayout);
        panelTabelaLayout.setHorizontalGroup(
            panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTabelaLayout.createSequentialGroup()
                        .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(panelTabelaLayout.createSequentialGroup()
                                .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelTabelaLayout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(lbBrojRegistracije))
                                    .addGroup(panelTabelaLayout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbGodiste))
                                    .addGroup(panelTabelaLayout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbModelVozila))
                                    .addGroup(panelTabelaLayout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbVrstaGoriva)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                                .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel11))
                                .addGap(18, 18, 18)
                                .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbKubikaza)
                                    .addComponent(lbKilovati))
                                .addGap(68, 68, 68)
                                .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addGroup(panelTabelaLayout.createSequentialGroup()
                                        .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel8))
                                        .addGap(39, 39, 39)
                                        .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbGrad)
                                            .addComponent(lbAdresa)
                                            .addComponent(lbTelefon)
                                            .addComponent(lbVlasnik))))))
                        .addContainerGap(146, Short.MAX_VALUE))
                    .addGroup(panelTabelaLayout.createSequentialGroup()
                        .addComponent(skrolPejn)
                        .addContainerGap())))
        );
        panelTabelaLayout.setVerticalGroup(
            panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(14, 14, 14)
                .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(jLabel11)
                    .addComponent(lbBrojRegistracije)
                    .addComponent(lbKilovati)
                    .addComponent(lbVlasnik))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jLabel12)
                    .addComponent(lbKubikaza)
                    .addComponent(lbTelefon)
                    .addComponent(lbGodiste))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(lbVrstaGoriva)
                        .addComponent(lbAdresa)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8)
                    .addComponent(lbModelVozila)
                    .addComponent(lbGrad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(skrolPejn, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                .addGap(7, 7, 7))
        );

        panelAzurirajRadniNalog.setBackground(new java.awt.Color(102, 153, 255));
        panelAzurirajRadniNalog.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelAzurirajRadniNalog.setForeground(new java.awt.Color(102, 153, 255));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Id radnog naloga:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Nalog otvoren:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Nalog zatvoren:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Rok za zatvaranje:");

        cbPlaceno.setBackground(new java.awt.Color(102, 153, 255));
        cbPlaceno.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbPlaceno.setForeground(new java.awt.Color(255, 255, 255));
        cbPlaceno.setText("Plaćeno");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Troškovi:");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Cijena usluge:");

        taProblem.setColumns(20);
        taProblem.setLineWrap(true);
        taProblem.setRows(5);
        jScrollPane1.setViewportView(taProblem);

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Opis problema:");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Radnik radio:");

        btnAzuriraj.setText("Ažuriraj");
        btnAzuriraj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAzurirajActionPerformed(evt);
            }
        });

        lbSTATUS.setBackground(new java.awt.Color(102, 153, 255));
        lbSTATUS.setForeground(new java.awt.Color(102, 153, 255));
        lbSTATUS.setText("jLabel21");

        lbSTATUSVozilo.setBackground(new java.awt.Color(102, 153, 255));
        lbSTATUSVozilo.setForeground(new java.awt.Color(102, 153, 255));
        lbSTATUSVozilo.setText("jLabel21");

        javax.swing.GroupLayout panelAzurirajRadniNalogLayout = new javax.swing.GroupLayout(panelAzurirajRadniNalog);
        panelAzurirajRadniNalog.setLayout(panelAzurirajRadniNalogLayout);
        panelAzurirajRadniNalogLayout.setHorizontalGroup(
            panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                            .addComponent(jLabel18)
                            .addGap(50, 50, 50)))
                    .addComponent(jLabel17)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tbCijena)
                    .addComponent(tbIdNaloga)
                    .addComponent(cbPlaceno)
                    .addComponent(tbTroskovi)
                    .addComponent(tfRadnik)
                    .addComponent(tbNalogOtvoren, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(tbNalogZatvoren, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tbRokZatvaranja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1)
                                    .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
                            .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                                .addComponent(btnAzuriraj, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                                .addComponent(lbSTATUSVozilo)
                                .addGap(70, 70, 70))))
                    .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(lbSTATUS)
                        .addContainerGap())))
        );
        panelAzurirajRadniNalogLayout.setVerticalGroup(
            panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbIdNaloga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel20))
                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(tbNalogOtvoren, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(tbNalogZatvoren, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel16)
                                    .addComponent(tbRokZatvaranja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbPlaceno)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tbTroskovi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(tbCijena, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelAzurirajRadniNalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(tfRadnik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(btnAzuriraj, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelAzurirajRadniNalogLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbSTATUSVozilo)))
                        .addContainerGap(36, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAzurirajRadniNalogLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbSTATUS)
                        .addGap(26, 26, 26))))
        );

        javax.swing.GroupLayout panelGlavniLayout = new javax.swing.GroupLayout(panelGlavni);
        panelGlavni.setLayout(panelGlavniLayout);
        panelGlavniLayout.setHorizontalGroup(
            panelGlavniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlavniLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelAzurirajRadniNalog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelGlavniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelGlavniLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        panelGlavniLayout.setVerticalGroup(
            panelGlavniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGlavniLayout.createSequentialGroup()
                .addContainerGap(343, Short.MAX_VALUE)
                .addComponent(panelAzurirajRadniNalog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelGlavniLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelGlavniLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(275, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 744, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelGlavni, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 641, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(panelGlavni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAzurirajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAzurirajActionPerformed
        Integer id = new Integer(tbIdNaloga.getText());
        
        java.sql.Date otvoren = new java.sql.Date(tbNalogOtvoren.getDate().getTime());
        java.sql.Date zatvoren = new java.sql.Date(tbNalogZatvoren.getDate().getTime());
        java.sql.Date potrebno = new java.sql.Date(tbRokZatvaranja.getDate().getTime());
        boolean placeno = cbPlaceno.isSelected();
//        String troskovi = tbTroskovi.getText();
//        String cijena = tbCijena.getText();
       String problem = taProblem.getText();
        
        Double troskovi = 0.0;
        Double cijena = 0.0;
        //Integer problem = 0;
        
        if(!"".equals(tbTroskovi.getText()) && (tbTroskovi.getText() != null)){
            try{
            troskovi = new Double(tbTroskovi.getText());
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(rootPane, "Trošak mora biti realan podatak", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }
        
        if(!"".equals(tbCijena.getText()) && (tbCijena.getText() != null)){
            try{
            cijena = new Double(tbCijena.getText());
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(rootPane, "Cijena mora biti realan podatak", "Greška", JOptionPane.OK_OPTION);
                return;
            }
        }
        
        
        RadniNalogDTO rn = new RadniNalogDTO();
        //rn.setIdRadniNalog();
        rn.setIdRadniNalog(new Integer(lbSTATUS.getText()));
        rn.setDatumOtvaranjaNaloga(otvoren);
        rn.setDatumZatvaranjaNaloga(zatvoren);
        rn.setPredvidjenoVrijemeZavrsetka(potrebno);
        rn.setTroskovi(troskovi);
        rn.setCijenaUsluge(cijena);
        rn.setOpisProblema(problem);
        rn.setPlaceno(placeno);
        rn.setIdVozilo(this.idVozila);
        //rn.setIdVozilo();
        DAOFactory.getDAOFactory().getRadniNalogDAO().azurirajRadniNalog(rn);
        
        
        
    }//GEN-LAST:event_btnAzurirajActionPerformed

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
            java.util.logging.Logger.getLogger(PregledIstorijePopravkiDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PregledIstorijePopravkiDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PregledIstorijePopravkiDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PregledIstorijePopravkiDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PregledIstorijePopravkiDialog dialog = new PregledIstorijePopravkiDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAzuriraj;
    private javax.swing.JCheckBox cbPlaceno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbAdresa;
    private javax.swing.JLabel lbBrojRegistracije;
    private javax.swing.JLabel lbGodiste;
    private javax.swing.JLabel lbGrad;
    private javax.swing.JLabel lbKilovati;
    private javax.swing.JLabel lbKubikaza;
    private javax.swing.JLabel lbModelVozila;
    private javax.swing.JLabel lbSTATUS;
    private javax.swing.JLabel lbSTATUSVozilo;
    private javax.swing.JLabel lbTelefon;
    private javax.swing.JLabel lbVlasnik;
    private javax.swing.JLabel lbVrstaGoriva;
    private javax.swing.JPanel panelAzurirajRadniNalog;
    private javax.swing.JPanel panelGlavni;
    private javax.swing.JPanel panelTabela;
    private javax.swing.JScrollPane skrolPejn;
    private javax.swing.JTextArea taProblem;
    private javax.swing.JTable tabela;
    private javax.swing.JTextField tbCijena;
    private javax.swing.JTextField tbIdNaloga;
    private com.toedter.calendar.JDateChooser tbNalogOtvoren;
    private com.toedter.calendar.JDateChooser tbNalogZatvoren;
    private com.toedter.calendar.JDateChooser tbRokZatvaranja;
    private javax.swing.JTextField tbTroskovi;
    private javax.swing.JTextField tfRadnik;
    // End of variables declaration//GEN-END:variables
}
