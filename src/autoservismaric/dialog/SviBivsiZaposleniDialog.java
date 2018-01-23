/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoservismaric.dialog;

import data.dao.DAOFactory;
import data.dto.ZaposleniDTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import poslovnalogika.ZaposleniLogika;

/**
 *
 * @author haker
 */
public class SviBivsiZaposleniDialog extends javax.swing.JDialog {

    /**
     * Creates new form SviBivsiZaposleniDialog
     */
    public SviBivsiZaposleniDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        dateChooserDatumVracanja.setCalendar(Calendar.getInstance());
        prikazSvihBivsihZaposlenih();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tableBivsiZaposleni = new javax.swing.JTable();
        buttonVratiZaposlenog = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        dateChooserDatumVracanja = new com.toedter.calendar.JDateChooser();
        labelDatumVracanja = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Bivsi zaposleni");

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));

        tableBivsiZaposleni.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "IdRadnika", "Ime", "Prezime", "Ime oca", "Adresa", "Telefon", "Broj lične karte", "Stručna sprema", "Datum rođenja", "Datum primanja", "Datum otpuštanja", "Funkcija"
            }
        ));
        jScrollPane1.setViewportView(tableBivsiZaposleni);

        buttonVratiZaposlenog.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        buttonVratiZaposlenog.setText("Vrati zaposlenog");
        buttonVratiZaposlenog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonVratiZaposlenogActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        labelDatumVracanja.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelDatumVracanja.setForeground(new java.awt.Color(255, 255, 255));
        labelDatumVracanja.setText("Datum prijema u novi radni odnos:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelDatumVracanja)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dateChooserDatumVracanja, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(buttonVratiZaposlenog)
                .addContainerGap())
            .addComponent(jSeparator1)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1192, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonVratiZaposlenog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateChooserDatumVracanja, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelDatumVracanja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonVratiZaposlenogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonVratiZaposlenogActionPerformed
       int redniBroj=tableBivsiZaposleni.getSelectedRow();
        if(redniBroj!=-1){
            ZaposleniDTO selektovanRadnik=zaposleni.get(redniBroj);
            selektovanRadnik.setDatumOd(new java.sql.Date(dateChooserDatumVracanja.getDate().getTime()));
            boolean flag=DAOFactory.getDAOFactory().getZaposleniDAO().ponistiOtkaz(selektovanRadnik);
            if(flag){
                JOptionPane.showMessageDialog(null,"Uspjesno ponistavanja otkaza!","Obavjestenje",JOptionPane.INFORMATION_MESSAGE);
                 prikazSvihBivsihZaposlenih();
                 new ZaposleniLogika("svi").run();
            }else{
                 JOptionPane.showMessageDialog(null,"Neuspjesno ponistavanje otkaza!","Problem",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null,"Niste odabrali zaposlenog!","Problem",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_buttonVratiZaposlenogActionPerformed

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
            java.util.logging.Logger.getLogger(SviBivsiZaposleniDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SviBivsiZaposleniDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SviBivsiZaposleniDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SviBivsiZaposleniDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SviBivsiZaposleniDialog dialog = new SviBivsiZaposleniDialog(new javax.swing.JFrame(), true);
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

    private void prikazSvihBivsihZaposlenih(){
        DefaultTableModel table=(DefaultTableModel)tableBivsiZaposleni.getModel();
        List<ZaposleniDTO> lista=DAOFactory.getDAOFactory().getZaposleniDAO().sviBivsiZaposleni();
       Iterator<ZaposleniDTO> i=lista.iterator();
       table.setRowCount(0);//brise sadrzaj tabele
       ZaposleniDTO rez=new ZaposleniDTO();
       while(i.hasNext()){
           rez=i.next();
           table.addRow(new Object[]{rez.getIdRadnik(),rez.getIme(),rez.getPrezime(),rez.getImeOca(),rez.getAdresa(),rez.getTelefon()
           ,rez.getBrojLicneKarte(),rez.getStrucnaSprema(),rez.getDatumRodjenja(),rez.getDatumOd(),rez.getDatumDo(),rez.getFunkcija()});
       }
    }
    
    //moja polja
    private ArrayList<ZaposleniDTO> zaposleni=new ArrayList<ZaposleniDTO>(DAOFactory.getDAOFactory().getZaposleniDAO().sviBivsiZaposleni());
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonVratiZaposlenog;
    private com.toedter.calendar.JDateChooser dateChooserDatumVracanja;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelDatumVracanja;
    private javax.swing.JTable tableBivsiZaposleni;
    // End of variables declaration//GEN-END:variables
}
