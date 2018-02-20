/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoservismaric.dialog;

import data.dao.DAOFactory;
import data.dto.KupacDTO;
import java.awt.Color;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import poslovnalogika.KupacLogika;

/**
 *
 * @author DulleX
 */
public class IzmijeniVlasnikaDialog extends javax.swing.JDialog {

    public int idVlasnika;
    ButtonGroup bg;
    public KupacLogika kupacLogika = new KupacLogika();
    /**
     * Creates new form IzmijeniVlasnikaDialog
     */
    public IzmijeniVlasnikaDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        kupacLogika.inicijalizacijaIzmijeniDijaloga(this);     
    }
    
    public IzmijeniVlasnikaDialog(java.awt.Frame parent, boolean modal, int idVlasnika) {
        super(parent, modal);
        initComponents();
        this.idVlasnika = idVlasnika;
        bg = new ButtonGroup();
        kupacLogika.inicijalizacijaIzmijeniDijaloga(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelDodajVlasnika = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        tfImeDodaj = new javax.swing.JTextField();
        tfPrezimeDodaj = new javax.swing.JTextField();
        tfNazivDodaj = new javax.swing.JTextField();
        tfTelefonDodaj = new javax.swing.JTextField();
        tfGradDodaj = new javax.swing.JTextField();
        tfAdresaDodaj = new javax.swing.JTextField();
        rbPrivatno = new javax.swing.JRadioButton();
        rbPravno = new javax.swing.JRadioButton();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        btnDodaj = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnOdustani = new javax.swing.JButton();
        lbPoruka = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Izmijeni vlasnika");
        setPreferredSize(new java.awt.Dimension(359, 409));
        setResizable(false);

        panelDodajVlasnika.setBackground(new java.awt.Color(102, 153, 255));
        panelDodajVlasnika.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panelDodajVlasnika.setPreferredSize(new java.awt.Dimension(359, 409));

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(359, 409));

        tfNazivDodaj.setEditable(false);

        tfTelefonDodaj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfTelefonDodajActionPerformed(evt);
            }
        });

        rbPrivatno.setBackground(new java.awt.Color(102, 153, 255));
        rbPrivatno.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPrivatno.setForeground(new java.awt.Color(255, 255, 255));
        rbPrivatno.setText("Privatno");
        rbPrivatno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPrivatnoActionPerformed(evt);
            }
        });

        rbPravno.setBackground(new java.awt.Color(102, 153, 255));
        rbPravno.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rbPravno.setForeground(new java.awt.Color(255, 255, 255));
        rbPravno.setText("Pravno");
        rbPravno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPravnoActionPerformed(evt);
            }
        });

        jLabel111.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(255, 255, 255));
        jLabel111.setText("Ime:");

        jLabel112.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(255, 255, 255));
        jLabel112.setText("Prezime:");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Naziv:");

        jLabel113.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel113.setForeground(new java.awt.Color(255, 255, 255));
        jLabel113.setText("Telefon:");

        jLabel115.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel115.setForeground(new java.awt.Color(255, 255, 255));
        jLabel115.setText("Adresa:");

        jLabel114.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel114.setForeground(new java.awt.Color(255, 255, 255));
        jLabel114.setText("Grad:");

        btnDodaj.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDodaj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/refresh-button (1).png"))); // NOI18N
        btnDodaj.setText("Ažuriraj");
        btnDodaj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDodajActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Ažuriraj podatke o vlasniku:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tip vlasnika:");

        btnOdustani.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnOdustani.setIcon(new javax.swing.ImageIcon(getClass().getResource("/autoservismaric/images/cancel (1).png"))); // NOI18N
        btnOdustani.setText("Odustani");
        btnOdustani.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOdustaniActionPerformed(evt);
            }
        });

        lbPoruka.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbPoruka.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel111)
                                .addComponent(jLabel112)
                                .addComponent(jLabel24)
                                .addComponent(jLabel113)
                                .addComponent(jLabel115)
                                .addComponent(jLabel114)
                                .addComponent(jLabel2))
                            .addGap(40, 40, 40)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(rbPrivatno, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tfImeDodaj, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tfNazivDodaj, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tfTelefonDodaj)
                                .addComponent(tfAdresaDodaj, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tfGradDodaj, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tfPrezimeDodaj, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(rbPravno, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                    .addGap(50, 50, 50))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(lbPoruka)
                            .addGap(31, 31, 31)
                            .addComponent(btnOdustani)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 60, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbPrivatno)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbPravno)
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfImeDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel111))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPrezimeDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel112))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(tfNazivDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfTelefonDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel113)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfGradDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel115))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfAdresaDodaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel114))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbPoruka)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDodaj)
                        .addComponent(btnOdustani)))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelDodajVlasnikaLayout = new javax.swing.GroupLayout(panelDodajVlasnika);
        panelDodajVlasnika.setLayout(panelDodajVlasnikaLayout);
        panelDodajVlasnikaLayout.setHorizontalGroup(
            panelDodajVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDodajVlasnikaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelDodajVlasnikaLayout.setVerticalGroup(
            panelDodajVlasnikaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDodajVlasnikaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelDodajVlasnika, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelDodajVlasnika, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tfTelefonDodajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfTelefonDodajActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfTelefonDodajActionPerformed

    private void rbPrivatnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPrivatnoActionPerformed
        tfNazivDodaj.setEditable(false);
        tfNazivDodaj.setBackground(Color.gray);
        tfImeDodaj.setEditable(true);
        tfPrezimeDodaj.setEditable(true);
        tfImeDodaj.setBackground(Color.white);
        tfPrezimeDodaj.setBackground(Color.white);
    }//GEN-LAST:event_rbPrivatnoActionPerformed

    private void rbPravnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPravnoActionPerformed
        tfImeDodaj.setEditable(false);
        tfPrezimeDodaj.setEditable(false);
        tfNazivDodaj.setEditable(true);
        tfImeDodaj.setBackground(Color.gray);
        tfPrezimeDodaj.setBackground(Color.gray);
        tfNazivDodaj.setBackground(Color.white);
    }//GEN-LAST:event_rbPravnoActionPerformed

    private void btnDodajActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDodajActionPerformed
        kupacLogika.izmijeniKupca(this);
    }//GEN-LAST:event_btnDodajActionPerformed

    private void btnOdustaniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOdustaniActionPerformed
        dispose();
    }//GEN-LAST:event_btnOdustaniActionPerformed

    public int getIdVlasnika() {
        return idVlasnika;
    }

    public ButtonGroup getBg() {
        return bg;
    }

    public JButton getBtnDodaj() {
        return btnDodaj;
    }

    public JButton getBtnOdustani() {
        return btnOdustani;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public JLabel getLbPoruka() {
        return lbPoruka;
    }

    public JPanel getPanelDodajVlasnika() {
        return panelDodajVlasnika;
    }

    public JRadioButton getRbPravno() {
        return rbPravno;
    }

    public JRadioButton getRbPrivatno() {
        return rbPrivatno;
    }

    public JTextField getTfAdresaDodaj() {
        return tfAdresaDodaj;
    }

    public JTextField getTfGradDodaj() {
        return tfGradDodaj;
    }

    public JTextField getTfImeDodaj() {
        return tfImeDodaj;
    }

    public JTextField getTfNazivDodaj() {
        return tfNazivDodaj;
    }

    public JTextField getTfPrezimeDodaj() {
        return tfPrezimeDodaj;
    }

    public JTextField getTfTelefonDodaj() {
        return tfTelefonDodaj;
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
            java.util.logging.Logger.getLogger(IzmijeniVlasnikaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IzmijeniVlasnikaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IzmijeniVlasnikaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IzmijeniVlasnikaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IzmijeniVlasnikaDialog dialog = new IzmijeniVlasnikaDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnDodaj;
    private javax.swing.JButton btnOdustani;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbPoruka;
    private javax.swing.JPanel panelDodajVlasnika;
    private javax.swing.JRadioButton rbPravno;
    private javax.swing.JRadioButton rbPrivatno;
    private javax.swing.JTextField tfAdresaDodaj;
    private javax.swing.JTextField tfGradDodaj;
    private javax.swing.JTextField tfImeDodaj;
    private javax.swing.JTextField tfNazivDodaj;
    private javax.swing.JTextField tfPrezimeDodaj;
    private javax.swing.JTextField tfTelefonDodaj;
    // End of variables declaration//GEN-END:variables
}
