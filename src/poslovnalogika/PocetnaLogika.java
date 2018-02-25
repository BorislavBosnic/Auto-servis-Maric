/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.dialog.OpisDialog;
import autoservismaric.forms.HomeForm1;
import static autoservismaric.forms.HomeForm1.selRedDio;
import static autoservismaric.forms.HomeForm1.selektovanRed;
import data.dao.DAOFactory;
import data.dto.KupacDTO;
import data.dto.RadniNalogDTO;
import data.dto.VoziloDTO;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import static poslovnalogika.KnjigovodstvoLogika.plati;
import static poslovnalogika.KnjigovodstvoLogika.radniNalogIFakturaUTekst;
import static poslovnalogika.Usluge.forma;

/**
 *
 * @author Aco
 */
public class PocetnaLogika {
    HomeForm1 form;
    public PocetnaLogika(HomeForm1 form){
        this.form = form;
    }
    
    public void prikaziAktivnosti(Date dat1, Date dat2){
        ArrayList<RadniNalogDTO> nalozi = new ArrayList<RadniNalogDTO>();
        nalozi = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi(dat1, dat2);
        
        DefaultTableModel dtm = (DefaultTableModel) form.getjTableAktivnosti().getModel();
        dtm.setRowCount(0);
        
        int i = 0;
        if (nalozi != null) {
            while (i < nalozi.size()) {
                RadniNalogDTO d = nalozi.get(i);
                VoziloDTO vozilo = DAOFactory.getDAOFactory().getVoziloDAO().vozilo(d.getIdVozilo());
                KupacDTO kupac = DAOFactory.getDAOFactory().getKupacDAO().kupac(vozilo.getIdKupac());
                String naziv = "";
                if(kupac.getNaziv() == null){
                    naziv = kupac.getIme() + " " + kupac.getPrezime();
                }else
                    naziv = kupac.getNaziv();
                
                dtm.addRow(new Object[]{d.getIdRadniNalog(), naziv, vozilo.getBrojRegistracije(),new SimpleDateFormat("dd.MM.yyyy.").format(d.getDatumOtvaranjaNaloga()), new SimpleDateFormat("dd.MM.yyyy.").format(d.getPredvidjenoVrijemeZavrsetka())});
                i++;
            }
        }
    }
    
    public void ucitajPopupZaAktivnosti() {
        JMenuItem opis = new JMenuItem("Prikaži opis");
        JTable jTable = form.getjTableAktivnosti();
        opis.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Integer id = (Integer)jTable.getValueAt(jTable.getSelectedRow(), 0);
                new OpisDialog(form, true, id).setVisible(true);
            }
        });
        form.getPopupAktivnosti().add(opis);

        
        jTable.setComponentPopupMenu(form.getPopupAktivnosti());

        
        form.getPopupAktivnosti().addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = jTable.rowAtPoint(SwingUtilities.convertPoint(form.getPopupAktivnosti(), new Point(0, 0), jTable));
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
    public void ucitajPopupZaPredracune() {
        JMenuItem opis = new JMenuItem("Detaljno");
        JMenuItem placeno = new JMenuItem("Plati");
        JTable jTable = form.getTblNeplaceneFakture();
        opis.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JFrame(), radniNalogIFakturaUTekst(Integer.parseInt((String) forma.getTblNeplaceneFakture().getValueAt(selektovanRed, 0))));
            }
        });
        form.getPopupPredracuni().add(opis);

        placeno.addActionListener(new ActionListener() {

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
                selektovanRed = jTable.getSelectedRow();
                if (izbor == JOptionPane.YES_OPTION && selektovanRed>-1) {
                    plati(Integer.parseInt((String) forma.getTblNeplaceneFakture().getValueAt(selektovanRed, 0)));
                    if(form.getJrbPrivatno().isSelected())
                        forma.getBtnPrikaziSvePredracune().doClick();
                    else
                        forma.getBtnPrikazii().doClick();
                }
            }
        });
        forma.getPopupPredracuni().add(placeno);
        
        jTable.setComponentPopupMenu(form.getPopupPredracuni());

        
        form.getPopupPredracuni().addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = jTable.rowAtPoint(SwingUtilities.convertPoint(form.getPopupPredracuni(), new Point(0, 0), jTable));
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
    
}
