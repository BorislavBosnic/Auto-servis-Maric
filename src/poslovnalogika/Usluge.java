/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.dialog.IzmijeniRadniNalogDialog;
import autoservismaric.forms.HomeForm1;
import static autoservismaric.forms.HomeForm1.selektovanRed;
import data.dao.DAOFactory;
import data.dto.RadniNalogDTO;
import data.dto.TerminDTO;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import static poslovnalogika.KnjigovodstvoLogika.fakturisaniRadniNalozi;
import static poslovnalogika.KnjigovodstvoLogika.fakturisi;
import static poslovnalogika.KnjigovodstvoLogika.nefakturisaniRadniNalozi;
import static poslovnalogika.KnjigovodstvoLogika.neplaceneFaktureZaPocetnuStranu;
import static poslovnalogika.KnjigovodstvoLogika.plati;
import static poslovnalogika.KnjigovodstvoLogika.poDatumuRadniNalozi;
import static poslovnalogika.KnjigovodstvoLogika.poIDuRadniNalozi;
import static poslovnalogika.KnjigovodstvoLogika.radniNalogIFakturaUTekst;
import static poslovnalogika.KnjigovodstvoLogika.radniNaloziZaTabelu;
import static poslovnalogika.KnjigovodstvoLogika.stavkeSaNalogaZaTabelu;
import static poslovnalogika.ZakazivanjaLogika.dodajTermin;
import static poslovnalogika.ZakazivanjaLogika.filtrirajTermine;
import static poslovnalogika.ZakazivanjaLogika.obrisiTermin;
import static poslovnalogika.ZakazivanjaLogika.terminiZaTabelu;

/**
 *
 * @author nikol
 */
public class Usluge
{
    public static HomeForm1 forma;
    public static void inicijalisuciKod(HomeForm1 forma)
    {
        Usluge.forma=forma;
        uslugeKnjigovodstva();
        uslugeZakazivanja();
        uslugePocetneStrane();
    }

    public static void uslugeKnjigovodstva() {
        inicijalizacijaTabelaKnjigovodstva();
        brisanjeStarihVrijednostiPoljaKnjigovodstva();
    }

    public static void uslugeZakazivanja() {
        inicijalizacijaTabelaZakazivanja();
    }

    public static void uslugePocetneStrane() {
        inicijalizacijaTabeleNeplacenihFaktura();
    }

    public static void inicijalizacijaTabelaKnjigovodstva() {
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        radniNaloziZaTabelu(lista, forma.getTblRadniNalozi());
        stavkeSaNalogaZaTabelu(forma.getTblFaktura(), forma.getTblRadniNalozi(), forma.getTxtBezPDV(), forma.getTxtPDV(), forma.getTxtUkupno());
        if (forma.getTblRadniNalozi().getRowCount() > 0) {
            forma.getTblRadniNalozi().setRowSelectionInterval(0, 0);
        }
        if (forma.getTblFaktura().getRowCount() > 0) {
            forma.getTblFaktura().setRowSelectionInterval(0, 0);
        }
        provjeraZaDugmiceZaTabeluNaloga();
        dodajIskacuciMeniUTabeluRadnihNaloga();
    }

    public static void inicijalizacijaTabelaZakazivanja() {
        ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, forma.getTblTermini());
        if (forma.getTblTermini().getRowCount() > 0) {
            forma.getTblTermini().setRowSelectionInterval(0, 0);
        }
        dodajIskacuciMeniUTabeluTermina();
    }

    public static void inicijalizacijaTabeleNeplacenihFaktura() {
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        neplaceneFaktureZaPocetnuStranu(lista, forma.getTblNeplaceneFakture());
        if (forma.getTblNeplaceneFakture().getRowCount() > 0) {
            forma.getTblNeplaceneFakture().setRowSelectionInterval(0, 0);
        }
        dodajIskacuciMeniUTabeluNeplacenihFaktura();
    }

    public static void provjeraZaDugmiceZaTabeluNaloga() {
        if (forma.getTblRadniNalozi().getRowCount() > 0
                && !(KnjigovodstvoLogika.NEFAKTURISAN.equals((String) (forma.getTblRadniNalozi().getValueAt(forma.getTblRadniNalozi().getSelectedRow(), 4))))) {
            forma.getBtnRacun().setEnabled(true);
            forma.getBtnPredracun().setEnabled(false);
        } else {
            forma.getBtnRacun().setEnabled(false);
            forma.getBtnPredracun().setEnabled(true);
        }
    }
    
     public static void dodajPopupMeniPretragaRadniNaloga(HomeForm1 homeForm) {
        opcija = -1;
        forma.setPopupMenu(new JPopupMenu());
        JMenuItem izmijeniItem = new JMenuItem("Izmijeni");
        JMenuItem izbrisiItem = new JMenuItem("Izbriši");
        JMenuItem zatvoriNalogItem = new JMenuItem("Zatvori radni nalog");


        izmijeniItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Integer id = Integer.parseInt(forma.getTableRNalozi().getModel().getValueAt(selektovanRed, 0).toString());
                new IzmijeniRadniNalogDialog(new JFrame(), true, id,homeForm).setVisible(true);
            }
        });
        forma.getPopupMenu().add(izmijeniItem);

        izbrisiItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                   Integer id = Integer.parseInt(forma.getTableRNalozi().getModel().getValueAt(selektovanRed, 0).toString());
                   
                    int dialogResult = JOptionPane.showConfirmDialog(new JFrame(), "Da li ste sigurni?", "Upozorenje", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                   
                        
                        DAOFactory.getDAOFactory().getRadniNalogDAO().izbrisiRadniNalog(id);
                        
                        JOptionPane.showMessageDialog(new JFrame(), "Uspješno obrisano!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
                            for (int i = forma.getTableRNalozi().getModel().getRowCount() - 1; i >= 0; i--) {
                                if ((Integer.parseInt(forma.getTableRNalozi().getModel().getValueAt(i, 0).toString())) == id) {
                                    ((DefaultTableModel) forma.getTableRNalozi().getModel()).removeRow(i);
                                }
                            }
                        
                    }
                    else{
                        
                    }
                   
            }
        });
        forma.getPopupMenu().add(izbrisiItem);
        
         zatvoriNalogItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Integer id = Integer.parseInt(forma.getTableRNalozi().getModel().getValueAt(selektovanRed, 0).toString());
                if(DAOFactory.getDAOFactory().getRadniNalogDAO().zatvoriRadniNalog(id)){
                    JOptionPane.showMessageDialog(new JFrame(), "Uspješno zatvoren radni nalog!", "Obavještenje", JOptionPane.INFORMATION_MESSAGE);
   
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Radni nalog već zatvoren!!", "Greška", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        forma.getPopupMenu().add(zatvoriNalogItem);

        forma.getTableRNalozi().setComponentPopupMenu(forma.getPopupMenu());

        forma.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = forma.getTableRNalozi().rowAtPoint(SwingUtilities.convertPoint(forma.getPopupMenu(), new Point(0, 0), forma.getTableRNalozi()));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                       //selektovanRed = forma.getTableRNalozi().getSelectedRow(); //??
                        String imeKolone = forma.getTableRNalozi().getModel().getColumnName(0);

                        if (selektovanRed >= 0) {

                        }
                        if (rowAtPoint > -1) {
                            forma.getTableRNalozi().setRowSelectionInterval(rowAtPoint, rowAtPoint);
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

    public static void brisanjeStarihVrijednostiPoljaKnjigovodstva() {
        forma.getTxtID().setText("");
        forma.getDtmDatum().setDate(null);
    }
    private static int opcija = -1;

    public static void dodajIskacuciMeniUTabeluRadnihNaloga() {
        opcija = -1;
        forma.setPopupMenu(new JPopupMenu());
        JMenuItem detaljnoItem = new JMenuItem("Detaljno");
        JMenuItem placenoItem = new JMenuItem("Plaćeno");
        JMenuItem fakturisiItem = new JMenuItem("Fakturiši");

        detaljnoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JFrame(), radniNalogIFakturaUTekst(Integer.parseInt((String) forma.getTblRadniNalozi().getValueAt(selektovanRed, 0))));
                stavkeSaNalogaZaTabelu(forma.getTblFaktura(), forma.getTblRadniNalozi(), forma.getTxtBezPDV(), forma.getTxtPDV(), forma.getTxtUkupno());
                provjeraZaDugmiceZaTabeluNaloga();
            }
        });
        forma.getPopupMenu().add(detaljnoItem);

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
                    plati(Integer.parseInt((String) forma.getTblRadniNalozi().getValueAt(selektovanRed, 0)));
                }
                stavkeSaNalogaZaTabelu(forma.getTblFaktura(), forma.getTblRadniNalozi(), forma.getTxtBezPDV(), forma.getTxtPDV(), forma.getTxtUkupno());
                provjeraZaDugmiceZaTabeluNaloga();
                inicijalizacijaTabelaKnjigovodstva();
            }
        });
        forma.getPopupMenu().add(placenoItem);

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
                    fakturisi(Integer.parseInt((String) forma.getTblRadniNalozi().getValueAt(selektovanRed, 0)), forma.getTxtUkupno());
                }
                stavkeSaNalogaZaTabelu(forma.getTblFaktura(), forma.getTblRadniNalozi(), forma.getTxtBezPDV(), forma.getTxtPDV(), forma.getTxtUkupno());
                provjeraZaDugmiceZaTabeluNaloga();
                inicijalizacijaTabelaKnjigovodstva();
            }
        });
        forma.getPopupMenu().add(fakturisiItem);

        forma.getTblRadniNalozi().setComponentPopupMenu(forma.getPopupMenu());

        forma.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = forma.getTblRadniNalozi().rowAtPoint(SwingUtilities.convertPoint(forma.getPopupMenu(), new Point(0, 0), forma.getTblRadniNalozi()));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = forma.getTblRadniNalozi().getModel().getColumnName(0);

                        if (selektovanRed >= 0) {

                        }
                        if (rowAtPoint > -1) {
                            forma.getTblRadniNalozi().setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                        stavkeSaNalogaZaTabelu(forma.getTblFaktura(), forma.getTblRadniNalozi(), forma.getTxtBezPDV(), forma.getTxtPDV(), forma.getTxtUkupno());
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

    public static void dodajIskacuciMeniUTabeluTermina() {
        opcija = -1;
        forma.setPopupMenu(new JPopupMenu());
        JMenuItem obrisiItem = new JMenuItem("Obriši");

        obrisiItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (obrisiTermin(
                        (String) forma.getTblTermini().getValueAt(selektovanRed, 0),
                        (String) forma.getTblTermini().getValueAt(selektovanRed, 1)
                )) {
                    JOptionPane.showMessageDialog(new JFrame(), "Termin je uklonjen.");
                    ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
                    terminiZaTabelu(lista, forma.getTblTermini());
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Termin nije uklonjen.");
                }
            }
        });
        forma.getPopupMenu().add(obrisiItem);

        forma.getTblTermini().setComponentPopupMenu(forma.getPopupMenu());

        forma.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = forma.getTblTermini().rowAtPoint(SwingUtilities.convertPoint(forma.getPopupMenu(), new Point(0, 0), forma.getTblTermini()));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = forma.getTblTermini().getModel().getColumnName(0);

                        if (selektovanRed >= 0) {

                        }
                        if (rowAtPoint > -1) {
                            forma.getTblTermini().setRowSelectionInterval(rowAtPoint, rowAtPoint);
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

    public static void dodajIskacuciMeniUTabeluNeplacenihFaktura() {
        opcija = -1;
        forma.setPopupMenu(new JPopupMenu());
        JMenuItem detaljnoItem = new JMenuItem("Detaljno");
        JMenuItem placenoItem = new JMenuItem("Plaćeno");
        JMenuItem fakturisiItem = new JMenuItem("Fakturiši");

        detaljnoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JFrame(), radniNalogIFakturaUTekst(Integer.parseInt((String) forma.getTblNeplaceneFakture().getValueAt(selektovanRed, 0))));
            }
        });
        forma.getPopupMenu().add(detaljnoItem);

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
                    plati(Integer.parseInt((String) forma.getTblNeplaceneFakture().getValueAt(selektovanRed, 0)));
                }
                inicijalizacijaTabeleNeplacenihFaktura();
            }
        });
        forma.getPopupMenu().add(placenoItem);

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
                    fakturisi(Integer.parseInt((String) forma.getTblNeplaceneFakture().getValueAt(selektovanRed, 0)));
                }
            }
        });
        forma.getPopupMenu().add(fakturisiItem);*/

        forma.getTblNeplaceneFakture().setComponentPopupMenu(forma.getPopupMenu());

        forma.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = forma.getTblNeplaceneFakture().rowAtPoint(SwingUtilities.convertPoint(forma.getPopupMenu(), new Point(0, 0), forma.getTblNeplaceneFakture()));
                        selektovanRed = rowAtPoint;
                        int column = 0;
                        //int row = tableVozila.getSelectedRow();
                        String imeKolone = forma.getTblNeplaceneFakture().getModel().getColumnName(0);

                        if (selektovanRed >= 0) {

                        }
                        if (rowAtPoint > -1) {
                            forma.getTblNeplaceneFakture().setRowSelectionInterval(rowAtPoint, rowAtPoint);
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
    
    public static void btnNefakturisanoAkcija()                                                 
    {                                                     
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        nefakturisaniRadniNalozi(lista, forma.getTblRadniNalozi());
        forma.getLblRadniNalozi().setText("Nefakturisani radni nalozi:");
    }
    
    public static void btnFakturisanoAkcija()                                               
    {                                                   
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        fakturisaniRadniNalozi(lista, forma.getTblRadniNalozi());
        forma.getLblRadniNalozi().setText("Fakturisani radni nalozi:");
    }
    
    public static void btnPoIDuAkcija()                                         
    {                                             
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        poIDuRadniNalozi(lista, forma.getTblRadniNalozi(), forma.getTxtID());
        forma.getLblRadniNalozi().setText("Radni nalozi:");
    }                                        

    public static void btnPoDatumuAkcija()                                            
    {                                                
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        poDatumuRadniNalozi(lista, forma.getTblRadniNalozi(), forma.getDtmDatum());
        forma.getLblRadniNalozi().setText("Radni nalozi:");
    }
    
    public static void btnSviNaloziAkcija()                                            
    {                           
        forma.getTxtID().setText("");
        btnPoIDuAkcija();
    }
    
    public static void btnPrikaziSvePredracuneAkcija()
    {                                                        
        ArrayList<RadniNalogDTO> lista = DAOFactory.getDAOFactory().getRadniNalogDAO().getRadniNalozi();
        neplaceneFaktureZaPocetnuStranu(lista, forma.getTblNeplaceneFakture());
        if (forma.getTblNeplaceneFakture().getRowCount() > 0) {
            forma.getTblNeplaceneFakture().setRowSelectionInterval(0, 0);
        }
    } 
    
    public static void btnPronadjiTerminAkcija()                                                  
    {                                                      
        ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        Date datum = null;
        /*if (forma.getDtmDatumPretraga().getDate() == null) {
            forma.getDtmDatumPretraga().setCalendar(Calendar.getInstance());
        }*/
        if(forma.getDtmDatumPretraga().getDate()==null)
            datum=null;
        else
            datum=new Date(forma.getDtmDatumPretraga().getDate().getTime());
        filtrirajTermine(
                lista,
                forma.getTblTermini(),
                forma.getTxtMarkaPretraga().getText(),
                datum,
                forma.getTxtImePretraga().getText(),
                forma.getTxtPrezimePretraga().getText(),
                forma.getTxtBrojTelefonaPretraga().getText()
        );
    }  
    
    public static void btnPonistiUnosePretragaAkcija()                                                        
    {                                                            
        forma.getTxtMarkaPretraga().setText("");
        forma.getDtmDatumPretraga().setDate(null);
        forma.getTxtImePretraga().setText("");
        forma.getTxtPrezimePretraga().setText("");
        forma.getTxtBrojTelefonaPretraga().setText("");
        ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, forma.getTblTermini());
    }   
    
    public static void btnDodajTerminAkcija()                                            
    {                                                   
        if (forma.getDtmDatumTermina().getDate() == null) {
            forma.getDtmDatumTermina().setCalendar(Calendar.getInstance());
        }
        boolean test = dodajTermin(
                new Date(forma.getDtmDatumTermina().getDate().getTime()),
                "" + (forma.getTxtVrijemeTerminaSati().getValue()),
                "" + (forma.getTxtVrijemeTerminaMinuti().getValue()),
                forma.getTxtMarkaTermina().getText(),
                forma.getTxtModelTermina().getText(),
                forma.getTxtImeTermina().getText(),
                forma.getTxtPrezimeTermina().getText(),
                forma.getTxtBrojTelefonaTermina().getText()
        );
        ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, forma.getTblTermini());
    }
    
    public static void btnPonistiUnoseTerminAkcija()                
    {                                                          
        ArrayList<TerminDTO> lista = DAOFactory.getDAOFactory().getTerminDAO().sviTermini();
        terminiZaTabelu(lista, forma.getTblTermini());
    }   
}
