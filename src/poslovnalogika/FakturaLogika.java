package poslovnalogika;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import net.sf.dynamicreports.examples.Templates;
import net.sf.dynamicreports.examples.complex.invoice.Customer;
import net.sf.dynamicreports.examples.complex.invoice.InvoiceData;
import net.sf.dynamicreports.examples.complex.invoice.Item;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;

public class FakturaLogika {
   private InvoiceData data = new InvoiceData();
   private AggregationSubtotalBuilder<BigDecimal> totalSum;
   public JasperReportBuilder create(ArrayList<Item> items,
                                    int pdv, int brojFakture,
                                    String imeProdavca, String adresaProdavca,
                                    String gradProdavca, String emailProdavca,
                                    String imeKupca, String adresaKupca,
                                    String gradKupca, String emailKupca)throws DRException
   {
       return build();
   }
   

   public JasperReportBuilder build(/*ArrayList<Item> items, int pdv, int brojFakture
                       Customer prodavac, Customer kupac*/) throws DRException {
          
       double pdv=0.17;
       data.getInvoice().setId(/*brojFakture*/5);
       
       /*OVOGA ISPOD SE TREBA RIJESITI*/
       ArrayList<Item> items=new ArrayList<>();
       Item it=new Item();
       it.setDescription("Bosch pumpa");
       it.setQuantity(Integer.valueOf(3));
       it.setUnitprice(BigDecimal.valueOf(1.3));
       items.add(it);
       
       it=new Item();
       it.setDescription("Brisači Yugo Koral");
       it.setQuantity(Integer.valueOf(1));
       it.setUnitprice(BigDecimal.valueOf(1.7));
       items.add(it);
       
       it=new Item();
       it.setDescription("Rad");
       it.setQuantity(Integer.valueOf(5));
       it.setUnitprice(BigDecimal.valueOf(5));
       items.add(it);
       
       //*PROMJENE KUPCA I PRODAVCA*//
      Customer prodavac=new Customer();
      prodavac.setAddress("Marića Marka 56");
      prodavac.setCity("Prnjavor");
      prodavac.setEmail("maric.najbolji@live.com");
      prodavac.setName("Autoservis Marić");
              data.getInvoice().setBillTo(prodavac);
              
      Customer kupac=new Customer();
      kupac.setAddress("Marića Marka 57");
      kupac.setCity("Prnjavor");
      kupac.setEmail("maric.slabi@live.com");
      kupac.setName("Marić Milorad");
              data.getInvoice().setShipTo(kupac);
      /*---------------------------------------------------------------------*/
       
      data.getInvoice().setItems(items);
       
      JasperReportBuilder report = report();

      //init styles
      StyleBuilder columnStyle = stl.style(Templates.columnStyle)
         .setBorder(stl.pen1Point());
      StyleBuilder subtotalStyle = stl.style(columnStyle)
         .bold();
      StyleBuilder shippingStyle = stl.style(Templates.boldStyle)
         .setHorizontalAlignment(HorizontalAlignment.RIGHT);

      //init columns
      TextColumnBuilder<Integer> rowNumberColumn = col.reportRowNumberColumn()
         .setFixedColumns(2)
         .setHorizontalAlignment(HorizontalAlignment.CENTER);
      TextColumnBuilder<String> descriptionColumn = col.column("Naziv", "description", type.stringType())
         .setFixedWidth(250);
      TextColumnBuilder<Integer> quantityColumn = col.column("Količina", "quantity", type.integerType())
         .setHorizontalAlignment(HorizontalAlignment.CENTER);
      TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Cijena", "unitprice", type.bigDecimalType());
      TextColumnBuilder<String> taxColumn = col.column("Porez", exp.text("17%"))
         .setFixedColumns(3);
      //price = unitPrice * quantity
      TextColumnBuilder<BigDecimal> priceColumn = unitPriceColumn.multiply(quantityColumn)
         .setTitle("Cijena")
         .setDataType(type.bigDecimalType());
      //vat = price * tax
      
      //*UBACIVANJE PDV-A I IZBACIVANJE DOSTAVE*//
      data.getInvoice().setTax(pdv);
      data.getInvoice().setShipping(BigDecimal.ZERO);
      
      TextColumnBuilder<BigDecimal> vatColumn = priceColumn.multiply(data.getInvoice().getTax())
         .setTitle("PDV")
         .setDataType(type.bigDecimalType());
      //total = price + vat
      TextColumnBuilder<BigDecimal> totalColumn = priceColumn.add(vatColumn)
         .setTitle("Ukupno")
         .setDataType(type.bigDecimalType())
         .setRows(2)
         .setStyle(subtotalStyle);
      //init subtotals
      totalSum = sbt.sum(totalColumn)
         .setLabel("Iznos:")
         .setLabelStyle(Templates.boldStyle);

      //configure report
      
      report
         .setTemplate(Templates.reportTemplate)
         .setColumnStyle(columnStyle)
         .setSubtotalStyle(subtotalStyle)
         //columns
         .columns(
            rowNumberColumn, descriptionColumn, quantityColumn, unitPriceColumn, totalColumn, priceColumn, /*taxColumn, */vatColumn)
         .columnGrid(
            rowNumberColumn, descriptionColumn, quantityColumn, unitPriceColumn,
            grid.horizontalColumnGridList()
               .add(totalColumn).newRow()
               .add(priceColumn, /*taxColumn, */vatColumn))
         //subtotals
         .subtotalsAtSummary(
            totalSum, sbt.sum(priceColumn), sbt.sum(vatColumn))
         //band components
         .title(
            /*Templates.createTitleComponent("Broj fakture: " + data.getInvoice().getId()),*/
            cmp.text("Faktura broj: " + data.getInvoice().getId()).setStyle(Templates.bold22CenteredStyle),
            cmp.horizontalList().setStyle(stl.style(10)).setGap(50).add(
               cmp.hListCell(createCustomerComponent("Prodavac", data.getInvoice().getBillTo())).heightFixedOnTop(),
               cmp.hListCell(createCustomerComponent("Kupac", data.getInvoice().getShipTo())).heightFixedOnTop()),
            cmp.verticalGap(10))
         .pageFooter(
            Templates.footerComponent)
         .summary(
            /*cmp.text(data.getInvoice().getShipping()).setValueFormatter(Templates.createCurrencyValueFormatter("Dostava:")).setStyle(shippingStyle),*/
            cmp.horizontalList(
               cmp.text("Rok za plaćanje: 30 dana").setStyle(Templates.bold12CenteredStyle),
               cmp.text(new TotalPaymentExpression()).setStyle(Templates.bold22CenteredStyle.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))),
            cmp.verticalGap(30),
            cmp.text("Hvala Vam na povjerenju i dođite nam opet.").setStyle(Templates.bold12CenteredStyle))
         .setDataSource(data.createDataSource());
      
      return report;
   }

   private ComponentBuilder<?, ?> createCustomerComponent(String label, Customer customer) {
      HorizontalListBuilder list = cmp.horizontalList().setBaseStyle(stl.style().setTopBorder(stl.pen1Point()).setLeftPadding(10));
      addCustomerAttribute(list, "Ime", customer.getName());
      addCustomerAttribute(list, "Adresa", customer.getAddress());
      addCustomerAttribute(list, "Grad", customer.getCity());
      addCustomerAttribute(list, "Email", customer.getEmail());
      return cmp.verticalList(
                     cmp.text(label).setStyle(Templates.boldStyle),
                     list);
   }

   private void addCustomerAttribute(HorizontalListBuilder list, String label, String value) {
      if (value != null) {
         list.add(cmp.text(label + ":").setFixedColumns(8).setStyle(Templates.boldStyle), cmp.text(value)).newRow();
      }
   }

   private class TotalPaymentExpression extends AbstractSimpleExpression<String> {
      private static final long serialVersionUID = 1L;

      @Override
      public String evaluate(ReportParameters reportParameters) {
         BigDecimal total = reportParameters.getValue(totalSum);
         BigDecimal shipping = total.add(data.getInvoice().getShipping());
         return "Iznos: " + type.bigDecimalType().valueToString(shipping, reportParameters.getLocale());
      }
   }

   public static void main(String[] args) {
      FakturaLogika design = new FakturaLogika();
      try {
         //JasperReportBuilder report = design.create(null,0,0,null,null,null,null,null,null,null,null);
         JasperReportBuilder report = design.build();
         report.show();
      } catch (DRException e) {
         e.printStackTrace();
      }
   }
}