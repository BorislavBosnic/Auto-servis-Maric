/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poslovnalogika;

import autoservismaric.forms.HomeForm1;
import data.dao.DAOFactory;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author HP BOOK
 */
public class StatistikaLogika {
    
    
     public StatistikaLogika(){
   
    }
    
   
     public void loadGraph(HomeForm1 forma) {

        loadGraphAutaNaStanju(forma);
        loadGraphFakture(forma);
        int mjesecIndex = forma.getComboBoxMjesec().getSelectedIndex();
        int godinaIndex = forma.getComboBoxGodina().getSelectedIndex();
        if (godinaIndex != 0) {

            DefaultCategoryDataset prihodiUkupnoBarChart = new DefaultCategoryDataset();
            DefaultCategoryDataset prihodiDijeloviBarChart = new DefaultCategoryDataset();
            DefaultCategoryDataset popravkeBarChart = new DefaultCategoryDataset();

            JFreeChart prihodiUkupnoChart = null;
            JFreeChart prihodiDijeloviChart = null;
            JFreeChart popravkeChart = null;

            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            if (mjesecIndex == 0 && godinaIndex != 0) {

                prihodiUkupnoChart = ChartFactory.createLineChart("Prihodi u " + (String) forma.getComboBoxGodina().getSelectedItem() + ". godini", "Mjesecno", "Prihodi", prihodiUkupnoBarChart, PlotOrientation.VERTICAL, false, true, false);
                prihodiDijeloviChart = ChartFactory.createLineChart("Prihodi od dijelova u " + (String) forma.getComboBoxGodina().getSelectedItem() + ". godini", "Mjesecno", "Prihodi od dijelova", prihodiDijeloviBarChart);
                popravkeChart = ChartFactory.createBarChart("Popravke u " + (String) forma.getComboBoxGodina().getSelectedItem() + ". godini", "Mjesecno", "Broj popravljenih auta", popravkeBarChart, PlotOrientation.VERTICAL, false, true, false);

                for (int i = 0; i < 12; i++) {

                    calendar1.set(Integer.parseInt((String) forma.getComboBoxGodina().getSelectedItem()), i, 1);
                    calendar2.set(Integer.parseInt((String) forma.getComboBoxGodina().getSelectedItem()), i, 31);
                    prihodiUkupnoBarChart.setValue(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis()))
                            + DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaUsluga(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis())), "Prihodi", HomeForm1.mjeseci[i]);

                    prihodiDijeloviBarChart.setValue(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis())), "Prihodi od dijelova", HomeForm1.mjeseci[i]);

                    popravkeBarChart.setValue(DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPopravki(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis())), "Broj popravljenih auta", HomeForm1.mjeseci[i]);

                }

                /* prihodiBarChart.setValue(1200, "Prihodi", "Februar");
            prihodiBarChart.setValue(800, "Prihodi", "Mart");
            prihodiBarChart.setValue(700, "Prihodi", "April");
            prihodiBarChart.setValue(900, "Prihodi", "Maj");
            prihodiBarChart.setValue(1100, "Prihodi", "Jun");
            prihodiBarChart.setValue(600, "Prihodi", "Jul");
            prihodiBarChart.setValue(800, "Prihodi", "Avgust");
            prihodiBarChart.setValue(1000, "Prihodi", "Septembar");
            prihodiBarChart.setValue(800, "Prihodi", "Oktobar");
            prihodiBarChart.setValue(900, "Prihodi", "Novembar");
            prihodiBarChart.setValue(500, "Prihodi", "Decembar");
            
             
            pieChart.setValue("Placene", 10);
            pieChart.setValue("Neplacene", 5);

            autaPieChart.setValue("Popravljena", 7);
            autaPieChart.setValue("Nepopravljena", 5);

            
            popravkeBarChart.setValue(25, "Prihodi", "Januar");
            popravkeBarChart.setValue(20, "Prihodi", "Februar");
            popravkeBarChart.setValue(31, "Prihodi", "Mart");
            popravkeBarChart.setValue(28, "Prihodi", "April");
            popravkeBarChart.setValue(25, "Prihodi", "Maj");
            popravkeBarChart.setValue(22, "Prihodi", "Jun");
            popravkeBarChart.setValue(29, "Prihodi", "Jul");
            popravkeBarChart.setValue(30, "Prihodi", "Avgust");
            popravkeBarChart.setValue(28, "Prihodi", "Septembar");
            popravkeBarChart.setValue(23, "Prihodi", "Oktobar");
            popravkeBarChart.setValue(34, "Prihodi", "Novembar");
            popravkeBarChart.setValue(28, "Prihodi", "Decembar");
            // prihodiBarChart.incrementValue(200, "Prihodi", "Decembar");
            
                 */
            } else if (mjesecIndex != 0 && godinaIndex != 0) {

                int kraj = 0;
                if (mjesecIndex == 1 || mjesecIndex == 3 || mjesecIndex == 5 || mjesecIndex == 7 || mjesecIndex == 8 || mjesecIndex == 10 || mjesecIndex == 12) {
                    kraj = 31;
                } else if (mjesecIndex == 2) {
                    if (Integer.parseInt((String) forma.getComboBoxGodina().getSelectedItem()) % 4 == 0) {
                        kraj = 29;
                    } else {
                        kraj = 28;
                    }
                } else {
                    kraj = 30;
                }

                prihodiUkupnoChart = ChartFactory.createLineChart("Prihodi za mjesec: " + (String) forma.getComboBoxMjesec().getSelectedItem(), "Dnevno", "Prihodi", prihodiUkupnoBarChart, PlotOrientation.VERTICAL, false, true, false);
                prihodiDijeloviChart = ChartFactory.createLineChart("Prihodi od dijelova za mjesec: " + (String) forma.getComboBoxMjesec().getSelectedItem(), "Dnevno", "Prihodi od dijelova", prihodiDijeloviBarChart);
                popravkeChart = ChartFactory.createBarChart("Popravke za mjesec: " + (String) forma.getComboBoxMjesec().getSelectedItem(), "Dnevno", "Broj popravljenih auta", popravkeBarChart, PlotOrientation.VERTICAL, false, true, false);

                for (int i = 1; i <= kraj; i++) {

                    calendar1.set(Integer.parseInt((String) forma.getComboBoxGodina().getSelectedItem()), forma.getComboBoxMjesec().getSelectedIndex() - 1, i);
                    calendar2.set(Integer.parseInt((String) forma.getComboBoxGodina().getSelectedItem()), forma.getComboBoxMjesec().getSelectedIndex() - 1, i);
                    prihodiUkupnoBarChart.setValue(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis()))
                            + DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaUsluga(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis())), "Prihodi", "" + i);

                    prihodiDijeloviBarChart.setValue(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis())), "Prihodi od dijelova", "" + i);

                    popravkeBarChart.setValue(DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPopravki(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis())), "Broj popravljenih auta", "" + i);

                }

            }

            //JFreeChart myChart= ChartFactory.createBarChart("Prihodi u 2017. godini","Mjesecno","Prihodi", barChart,PlotOrientation.VERTICAL,false,true,false);
            CategoryPlot prihodiUkupnoPlot = (CategoryPlot) prihodiUkupnoChart.getPlot();
            prihodiUkupnoPlot.setRangeGridlinePaint(Color.BLUE);
            prihodiUkupnoPlot.setBackgroundPaint(new Color(207, 229, 235));

            CategoryPlot prihodiDijeloviPlot = (CategoryPlot) prihodiDijeloviChart.getPlot();
            prihodiDijeloviPlot.setRangeGridlinePaint(Color.BLUE);
            prihodiDijeloviPlot.setBackgroundPaint(new Color(207, 229, 235));

            CategoryPlot popravkePlot = (CategoryPlot) popravkeChart.getPlot();
            popravkePlot.setRangeGridlinePaint(Color.BLUE);
            popravkePlot.setBackgroundPaint(new Color(207, 229, 235));

            LineAndShapeRenderer prihodiUkupnoRenderer = (LineAndShapeRenderer) prihodiUkupnoPlot.getRenderer();
            prihodiUkupnoRenderer.setSeriesPaint(0, new Color(40, 106, 155));
            prihodiUkupnoRenderer.setSeriesStroke(0, new BasicStroke(3.5f));

            LineAndShapeRenderer prihodiDijeloviRenderer = (LineAndShapeRenderer) prihodiDijeloviPlot.getRenderer();
            prihodiDijeloviRenderer.setSeriesPaint(0, new Color(40, 106, 155));
            prihodiDijeloviRenderer.setSeriesStroke(0, new BasicStroke(3.5f));

            BarRenderer popravkeRenderer = (BarRenderer) popravkePlot.getRenderer();
            popravkeRenderer.setSeriesPaint(0, new Color(40, 106, 155));

            ChartPanel prihodiukupnoBarPanel = new ChartPanel(prihodiUkupnoChart);
            ChartPanel prihodiDijeloviBarPanel = new ChartPanel(prihodiDijeloviChart);
            ChartPanel popravkeBarPanel = new ChartPanel(popravkeChart);

            forma.getPanelGrafikPrihodiUkupno().setLayout(new java.awt.BorderLayout());
            forma.getPanelGrafikPrihodiUkupno().removeAll();
            forma.getPanelGrafikPrihodiUkupno().add(prihodiukupnoBarPanel, BorderLayout.CENTER);
            forma.getPanelGrafikPrihodiUkupno().validate();

            forma.getPanelGrafikPrihodiDijelovi().setLayout(new java.awt.BorderLayout());
            forma.getPanelGrafikPrihodiDijelovi().removeAll();
            forma.getPanelGrafikPrihodiDijelovi().add(prihodiDijeloviBarPanel, BorderLayout.CENTER);
            forma.getPanelGrafikPrihodiDijelovi().validate();

            forma.getPanelGrafikPopravke().setLayout(new java.awt.BorderLayout());
            forma.getPanelGrafikPopravke().removeAll();
            forma.getPanelGrafikPopravke().add(popravkeBarPanel, BorderLayout.CENTER);
            forma.getPanelGrafikPopravke().validate();

        }
    }
    
     void loadGraphAutaNaStanju(HomeForm1 forma) {
        DefaultPieDataset autaPieChart = new DefaultPieDataset();
        JFreeChart autaChart = ChartFactory.createPieChart("Auta na stanju", autaPieChart);
        autaPieChart.setValue("Popravljena", (DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojAutaNaStanju() - DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojAutaKojaCekajuPopravku()));
        autaPieChart.setValue("Nepopravljena", (DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojAutaKojaCekajuPopravku()));
        autaChart = ChartFactory.createPieChart("Auta na stanju", autaPieChart);
        PiePlot autaPlot = (PiePlot) autaChart.getPlot();
        autaPlot.setBackgroundPaint(new Color(207, 229, 235));
        // autaPlot.setSectionPaint("Popravljeni", Color.green);// ne radi ovako!
        //autaPlot.setSectionPaint("Nepopravljeni", Color.yellow);
        //autaPlot.setExplodePercent("Nepopravljeni", 0.10);
        autaPlot.setSimpleLabels(true);

        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        autaPlot.setLabelGenerator(gen);

        ChartPanel autaBarPanel = new ChartPanel(autaChart);
        forma.getPanelGrafikAuta().setLayout(new java.awt.BorderLayout());
        forma.getPanelGrafikAuta().removeAll();
        forma.getPanelGrafikAuta().add(autaBarPanel, BorderLayout.CENTER);
        forma.getPanelGrafikAuta().validate();
    }

    void loadGraphFakture(HomeForm1 forma) {

        DefaultPieDataset pieChart = new DefaultPieDataset();

        JFreeChart faktureChart = ChartFactory.createRingChart("Fakture", pieChart, true, true, Locale.ITALY);

        faktureChart = ChartFactory.createRingChart("Fakture", pieChart, true, true, Locale.ITALY);

        pieChart.setValue("Placene", DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPlacenihFaktura());
        pieChart.setValue("Neplacene", DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojFaktura() - DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPlacenihFaktura());

        RingPlot fakturePlot = (RingPlot) faktureChart.getPlot();
        fakturePlot.setBackgroundPaint(new Color(207, 229, 235));
        //fakturePlot.setSimpleLabels(true);
        
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        fakturePlot.setLabelGenerator(gen);

        ChartPanel faktureBarPanel = new ChartPanel(faktureChart);

        forma.getPanelGrafikFakture().setLayout(new java.awt.BorderLayout());
        forma.getPanelGrafikFakture().removeAll();
        forma.getPanelGrafikFakture().add(faktureBarPanel, BorderLayout.CENTER);
        forma.getPanelGrafikFakture().validate();

    }

    public void loadStatistics(HomeForm1 forma) {

        loadZaradaUkupno(forma);
        loadZaradaDijelovi(forma);
        loadBrojPopravki(forma);
        loadBrojFaktura(forma);

    }

    void loadZaradaUkupno(HomeForm1 forma) {

        NumberFormat formatter = new DecimalFormat("#0.00");

        forma.getLabelDnevnaZarada().setText(formatter.format(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(), new Date())
                + DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaUsluga(new Date(), new Date())) + " KM");

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1);
        calendar2.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 31);

        forma.getLabelMjesecnaZarada().setText(formatter.format(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis()))
                + DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaUsluga(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis()))) + " KM");

        calendar1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.JANUARY, 1);
        calendar2.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 31);

        forma.getLabelGodisnjaZarada().setText(formatter.format(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis()))
                + DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaUsluga(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis()))) + " KM");

    }

    void loadZaradaDijelovi(HomeForm1 forma) {

        NumberFormat formatter = new DecimalFormat("#0.00");

        forma.getLabelDnevnaZaradaDijelovi().setText(formatter.format(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(), new Date())) + " KM");

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1);
        calendar2.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 31);

        forma.getLabelMjesecnaZaradaDijelovi().setText(formatter.format(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis()))) + " KM");

        calendar1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.JANUARY, 1);
        calendar2.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 31);

        forma.getLabelGodisnjaZaradaDijelovi().setText(formatter.format(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis()))) + " KM");

    }
    
    void loadBrojPopravki(HomeForm1 forma ){
        
        forma.getLabelPopravkeDanas().setText(DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPopravki(new Date(), new Date()) + "");
        
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1);
        calendar2.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 31);

        forma.getLabelPopravkeMjesec().setText(DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPopravki(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis())) + "");
        
        calendar1.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.JANUARY, 1);
        calendar2.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.DECEMBER, 31);
        
      



        forma.getLabelPopravkeGodina().setText(DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPopravki(new Date(calendar1.getTimeInMillis()), new Date(calendar2.getTimeInMillis())) + "");

    }
    
    void loadBrojFaktura(HomeForm1 forma){
        forma.getLblBrojFaktura().setText(DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojFaktura() + "");
        forma.getLblBrojPlacenihFaktura().setText(DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPlacenihFaktura()+ "");
        forma.getLblBrojNeplacenihFaktura().setText(DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojFaktura() - DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPlacenihFaktura()+ "");

    }
    
    public void loadInterval(HomeForm1 forma) {
               // new StatistikaLogika(new java.sql.Date(jDateChooserDatumOd.getDate().getTime()), new java.sql.Date(jDateChooserDatumDo.getDate().getTime())).run();
        NumberFormat formatter = new DecimalFormat("#0.00");

        forma.getLabelIntervalZarada().setText(formatter.format(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new java.sql.Date(forma.getjDateChooserDatumOd().getDate().getTime()), new java.sql.Date(forma.getjDateChooserDatumDo().getDate().getTime()))
            + DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaUsluga(new java.sql.Date(forma.getjDateChooserDatumOd().getDate().getTime()), new java.sql.Date(forma.getjDateChooserDatumDo().getDate().getTime()))) + " KM");

    forma.getLabelIntervalZaradaDijelovi().setText(formatter.format(DAOFactory.getDAOFactory().getRadniNalogDAO().getSumaCijenaDijelova(new java.sql.Date(forma.getjDateChooserDatumOd().getDate().getTime()), new java.sql.Date(forma.getjDateChooserDatumDo().getDate().getTime()))) + " KM");

    forma.getLabelPopravkeInterval().setText(DAOFactory.getDAOFactory().getRadniNalogDAO().getBrojPopravki(new java.sql.Date(forma.getjDateChooserDatumOd().getDate().getTime()), new java.sql.Date(forma.getjDateChooserDatumDo().getDate().getTime())) + "");
    
        
    }
    
    
}
