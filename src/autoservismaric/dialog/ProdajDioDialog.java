/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoservismaric.dialog;

import data.dao.DAOFactory;
import data.dto.DioDTO;
import data.dto.ProdanDioDTO;
import java.sql.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Aco
 */
public class ProdajDioDialog extends javax.swing.JDialog {
    DefaultTableModel dtm;
    int red;
    Double pdv;
    Double cijena = null;
    Integer kolicina = null;
    /**
     * Creates new form ProdajDioDialog
     */
    public ProdajDioDialog(DefaultTableModel dtm, int red, Double pdv, java.awt.Frame parent) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Prodaja dijelova");
        this.dtm = dtm;
        this.red = red;
        this.pdv = pdv;
        System.out.println("");
        jtfPDV.setText(String.format("%.2f", pdv));
        try {
            tfId.setText(((Integer) dtm.getValueAt(red, 0)).toString());
            tfSifra.setText((String) dtm.getValueAt(red, 1));
            tfNaziv.setText((String) dtm.getValueAt(red, 2));
            tfMarka.setText((String) dtm.getValueAt(red, 3));
            tfModel.setText((String) dtm.getValueAt(red, 4));
            tfGorivo.setText((String) dtm.getValueAt(red, 6));
            //Object d = (Object)dtm.getValueAt(red, 6);
            tfCijena.setText(((Object) dtm.getValueAt(red, 7)).toString());
            tfKolicina.setText(((Integer) dtm.getValueAt(red, 8)).toString());
            tfNovo.setText((String) dtm.getValueAt(red, 9));
            tfGodiste.setText(((Integer) dtm.getValueAt(red, 5)).toString());
        } catch (Exception e) {

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelzmijeniDio = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbPoruka = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfId = new javax.swing.JTextField();
        tfSifra = new javax.swing.JTextField();
        tfNaziv = new javax.swing.JTextField();
        tfMarka = new javax.swing.JTextField();
        tfModel = new javax.swing.JTextField();
        tfGorivo = new javax.swing.JTextField();
        tfCijena = new javax.swing.JTextField();
        tfKolicina = new javax.swing.JTextField();
        tfNovo = new javax.swing.JTextField();
        tfGodiste = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnOdustani = new javax.swing.JButton();
        btnProdaj = new javax.swing.JButton();
        jtfPDV = new javax.swing.JTextField();
        jtfKolicina = new javax.swing.JTextField();
        jtfCijena = new javax.swing.JTextField();
        jSeparator11 = new javax.swing.JSeparator();
        tfUkupno = new javax.swing.JTextField();
        btnUkupno = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelzmijeniDio.setBackground(new java.awt.Color(102, 153, 255));
        panelzmijeniDio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panelzmijeniDio.setNextFocusableComponent(jtfCijena);

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(476, 482));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Podaci o dijelu:");

        jPanel2.setBackground(new java.awt.Color(102, 153, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel2.setEnabled(false);

        jLabel111.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(255, 255, 255));
        jLabel111.setText("Naziv:");

        jLabel112.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(255, 255, 255));
        jLabel112.setText("Marka:");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Model:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Novo:");

        jLabel113.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel113.setForeground(new java.awt.Color(255, 255, 255));
        jLabel113.setText("Vrsta goriva:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Godište:");

        jLabel115.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel115.setForeground(new java.awt.Color(255, 255, 255));
        jLabel115.setText("Cijena");

        jLabel114.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel114.setForeground(new java.awt.Color(255, 255, 255));
        jLabel114.setText("Količina:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Id:");

        lbPoruka.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbPoruka.setText(" ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Šifra:");

        tfId.setEditable(false);

        tfSifra.setEditable(false);
        tfSifra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfSifraActionPerformed(evt);
            }
        });

        tfNaziv.setEditable(false);

        tfMarka.setEditable(false);

        tfModel.setEditable(false);

        tfGorivo.setEditable(false);

        tfCijena.setEditable(false);

        tfKolicina.setEditable(false);

        tfNovo.setEditable(false);

        tfGodiste.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel112, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel111, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(tfSifra, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfNaziv, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfModel)
                            .addComponent(tfMarka, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfId)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tfGorivo, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                .addGap(1, 1, 1))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel113, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel115)
                                .addComponent(jLabel114)))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(tfNovo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfKolicina, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfCijena, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfGodiste, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                        .addGap(9, 9, 9)
                        .addComponent(lbPoruka)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfSifra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel111)
                    .addComponent(tfNaziv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel112)
                    .addComponent(tfMarka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tfModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel113)
                            .addComponent(tfGorivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel115)
                            .addComponent(tfCijena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel114)
                                .addGap(0, 0, 0)
                                .addComponent(lbPoruka))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(tfKolicina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(tfNovo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel24))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(tfGodiste, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tfId, tfSifra});

        jPanel3.setBackground(new java.awt.Color(102, 153, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Cijena:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(240, 240, 240));
        jLabel8.setText("Količina:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(240, 240, 240));
        jLabel9.setText("Vrijednost PDV-a:");

        btnOdustani.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnOdustani.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/cancel (1).png"))); // NOI18N
        btnOdustani.setText("Odustani");
        btnOdustani.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOdustaniActionPerformed(evt);
            }
        });

        btnProdaj.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnProdaj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/shop-cart.png"))); // NOI18N
        btnProdaj.setText("Prodaj");
        btnProdaj.setIconTextGap(10);
        btnProdaj.setNextFocusableComponent(btnOdustani);
        btnProdaj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdajActionPerformed(evt);
            }
        });

        jtfPDV.setNextFocusableComponent(btnUkupno);

        jtfKolicina.setNextFocusableComponent(jtfPDV);

        jtfCijena.setNextFocusableComponent(jtfKolicina);

        tfUkupno.setEditable(false);

        btnUkupno.setText("Izračunaj ukupno:");
        btnUkupno.setNextFocusableComponent(btnProdaj);
        btnUkupno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUkupnoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jSeparator11)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtfKolicina, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(jtfCijena, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(jtfPDV, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnUkupno)
                            .addComponent(btnOdustani))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnProdaj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfUkupno, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                        .addGap(20, 20, 20))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jtfCijena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtfKolicina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jtfPDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUkupno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUkupno))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOdustani, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProdaj))
                .addGap(27, 27, 27))
        );

        tfUkupno.getAccessibleContext().setAccessibleDescription("");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Podaci za prodaju:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelzmijeniDioLayout = new javax.swing.GroupLayout(panelzmijeniDio);
        panelzmijeniDio.setLayout(panelzmijeniDioLayout);
        panelzmijeniDioLayout.setHorizontalGroup(
            panelzmijeniDioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelzmijeniDioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelzmijeniDioLayout.setVerticalGroup(
            panelzmijeniDioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelzmijeniDioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelzmijeniDio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelzmijeniDio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProdajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdajActionPerformed
       
       boolean flag = false;
       Integer kolNaStanju = (Integer) dtm.getValueAt(red, 8);
       
       try {
            kolicina = Integer.parseInt(jtfKolicina.getText());
            if(kolicina > kolNaStanju || kolicina < 0){
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Na stanju imate "+kolNaStanju + ", a želite da prodate "+kolicina,
                        "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                kolicina = null;
            }                
        } catch (NumberFormatException e) {
            if (!"".equals(jtfKolicina.getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format količine.", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
            }
            kolicina = null;
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
            cijena = null;
        }
       if(!flag){
           Date datum = new Date(System.currentTimeMillis());
           System.out.println("datum " +datum);
           ProdanDioDTO dio = new ProdanDioDTO((Integer)dtm.getValueAt(red, 0), cijena, kolicina, datum);
           DAOFactory.getDAOFactory().getProdanDioDAO().dodajProdanDio(dio);
           DioDTO prodajniDio = DAOFactory.getDAOFactory().getDioDAO().getDio((Integer)dtm.getValueAt(red, 0));
           prodajniDio.setKolicina(prodajniDio.getKolicina() - kolicina);
           DAOFactory.getDAOFactory().getDioDAO().azurirajDio(prodajniDio);
           dtm.setValueAt(prodajniDio.getKolicina() - kolicina, red, 8);
           JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Uspješno ste prodali dio", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
       }
    }//GEN-LAST:event_btnProdajActionPerformed

    private void btnOdustaniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOdustaniActionPerformed
        dispose();
    }//GEN-LAST:event_btnOdustaniActionPerformed

    private void btnUkupnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUkupnoActionPerformed
        boolean flag = false;
       Integer kolNaStanju = (Integer) dtm.getValueAt(red, 8);
       
       try {
            kolicina = Integer.parseInt(jtfKolicina.getText());
            if(kolicina > kolNaStanju || kolicina < 0){
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Na stanju imate "+kolNaStanju + ", a želite da prodate "+kolicina,
                        "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
                kolicina = null;
            }                
        } catch (NumberFormatException e) {
            if (!"".equals(jtfKolicina.getText())) {
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format količine.", "Greška", JOptionPane.ERROR_MESSAGE);
                flag = true;
            }
            kolicina = null;
        }
       
       try{
            cijena = Double.parseDouble(jtfCijena.getText());
        }catch(NumberFormatException e){
            if(!"".equals(jtfCijena.getText())){
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Nepravilan format cijene, primjer(33.32)!", "Greška", JOptionPane.ERROR_MESSAGE);      
            }else{
                JOptionPane jop = new JOptionPane();
                jop.showMessageDialog(this, "Unesite cijenu!", "Greška", JOptionPane.ERROR_MESSAGE);
            }
            flag = true;
            cijena = null;
        }
        if(cijena != null && kolicina != null){
             Double rez = (cijena + pdv*cijena/100)* kolicina;
            tfUkupno.setText(String.format("%.2f", rez));
        }else{
            tfUkupno.setText("0");
        }
    }//GEN-LAST:event_btnUkupnoActionPerformed

    private void tfSifraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfSifraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfSifraActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
      
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOdustani;
    private javax.swing.JButton btnProdaj;
    private javax.swing.JButton btnUkupno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JTextField jtfCijena;
    private javax.swing.JTextField jtfKolicina;
    private javax.swing.JTextField jtfPDV;
    private javax.swing.JLabel lbPoruka;
    private javax.swing.JPanel panelzmijeniDio;
    private javax.swing.JTextField tfCijena;
    private javax.swing.JTextField tfGodiste;
    private javax.swing.JTextField tfGorivo;
    private javax.swing.JTextField tfId;
    private javax.swing.JTextField tfKolicina;
    private javax.swing.JTextField tfMarka;
    private javax.swing.JTextField tfModel;
    private javax.swing.JTextField tfNaziv;
    private javax.swing.JTextField tfNovo;
    private javax.swing.JTextField tfSifra;
    private javax.swing.JTextField tfUkupno;
    // End of variables declaration//GEN-END:variables
}
