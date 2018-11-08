package pdftables;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import entities.CreditEntry;
import entities.Customer;
import entities.CustomerTransaction;
import entities.TruckSales;
import nmfc.helper.StandardItems;


public class AllCustomersBillPrint {

	private static String FILE = "C:\\tmp\\FirstPdf2.pdf";
	private static Font catFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.BOLD);
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,
			Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);

	private static Font smallerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
			Font.BOLD);
	private static Font smallerFont = new Font(Font.FontFamily.TIMES_ROMAN, 11,
			Font.NORMAL);

	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


	public static void main(String[] args) {

		//createBillPrintPDF (FILE);

	}

	public static boolean createAllBillPrintPDF ( String FILE,   
			Date billDate) {

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			addMetaData(document);
			addContent(document, billDate);
			document.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}



		return true;



	}


	// iText allows to add metadata to the PDF which can be viewed in your Adobe
	// Reader
	// under File -> Properties
	private static void addMetaData(Document document) {
		document.addTitle("Bill Print");
		document.addSubject("For New Madina");
		document.addKeywords("Customer, Bill, PDF, iText");
		document.addAuthor("Prabir Ghosh");
		document.addCreator("Prabir Ghosh");
	}


	private static void addContent(Document document,  
			Date date) throws DocumentException {

		// Set Title of the Report
		//	addHeader (document, cust);
		// add a table
		createTable( document, date);


	}



	private static void addPageHeader(Document document, Customer cust, Date date) throws DocumentException {

		Paragraph title1 = new Paragraph("New Madina Fruit Company\n*Bagnan New Bus Stand*\n*Bill*", catFont); 
		title1.setAlignment(Element.ALIGN_CENTER);
		title1.setFont( catFont);
		document.add(title1); 

		Paragraph paragraph = new Paragraph();
		paragraph.setFont( smallBold);
		paragraph.add(new Paragraph("Name of the customer: " + cust.getName())); 
		paragraph.add(new Paragraph("Bill Date: " + StandardItems.getFormatter().format(date))); 
		addEmptyLine(paragraph, 2);
		document.add(paragraph);

	}

	private static void addFooter(Document document, Customer cust , Date date, float total) {

		Paragraph paragraph = new Paragraph();
		paragraph.setFont( subFont);
		paragraph.setAlignment(Element.ALIGN_RIGHT);


		String billAmount = "Bill Amount= " +  String.format( "%.0f", total ) ; 
		paragraph.setAlignment(Element.ALIGN_RIGHT);
		paragraph.add(new Paragraph(billAmount)); 
		float previousDue = CustomerTransaction.getLastBalance(cust, date);
		paragraph.add(new Paragraph("Previous Balance= " + String.format( "%.0f", previousDue ) )); 
		System.out.println("----------------");
		System.out.println( String.format( "%.0f", previousDue ) );	 
		float totalDue = previousDue + total;		 
		paragraph.add(new Paragraph("Total Due= " + String.format( "%.0f", totalDue ) )); 
		System.out.println( String.format( "%.0f", totalDue ) );
		Date lastdepositDate = CreditEntry.getLastDepositDate(cust, date);
		String depositString="";
		if (lastdepositDate!=null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			depositString="Last Deposit Date=" + formatter.format (lastdepositDate )  ;
		}
		else {
			depositString = "Last Deposit Date= N/A"   ;
		}

		float previousPayment = CreditEntry.getLastDepositAmount (cust, date);
		addEmptyLine(paragraph, 1);
		if (previousPayment!=0) {
			depositString  += "  Last Deposit Amount= " + String.format( "%.0f", previousPayment )  ;
		}

		else { 
			depositString  =" Last Deposit Date=N/A Last Deposit Amount=N/A"; 
		}

		try {
			paragraph.add (depositString);
			document.add(paragraph);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void createTableHeader (PdfPTable table) {

		table.setWidthPercentage(100);
		try {
			table.setWidths(new float[] { 1, 2, 1, 2, 1, 1 });
		} 
		catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		PdfPCell c1 = new PdfPCell(new Phrase("Sl No"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Item Name"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Quantity"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Selling Unit"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);


		c1 = new PdfPCell(new Phrase("Rate"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Amount"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		table.setHeaderRows(1); 

	}

	private static void createTable( Document document,  Date date)
			throws DocumentException {


		PdfPTable table = new PdfPTable(6);

		createTableHeader (table);

		java.util.List <Customer> custrList = TruckSales.getDisctinctCustomers(date);



		int counter =1;
		

		for ( Customer cust: custrList) {

			java.util.List <TruckSales>  truckSaleList = 
					TruckSales.getCustomerWiseSaleList(cust, date); 
			
			if (counter >20) {
				document.newPage();				
				counter=0;

			}
			addPageHeader (document, cust, date);
			table = new PdfPTable(6);
			createTableHeader (table);
			float total =0;
			for ( TruckSales trcksale: truckSaleList) {
				table.addCell (counter+"");
				table.addCell ( trcksale.getFruit().toString() );
				addTableCellRight (table, trcksale.getQuantity() +" ");
				addTableCellRight (table,  trcksale.getSalesUnit()+" ");
				addTableCellRight (table,  trcksale.getRate()+" ");
				addTableCellRight (table,  String.format( "%.0f", trcksale.getAmount()));
				total+= trcksale.getAmount();
				counter++;
				if (counter >25) {

					try {
						document.add(table);
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					document.newPage();
					addPageHeader (document, cust, date);
					table = new PdfPTable(6);
					createTableHeader (table);
					counter=0;

				}

			}

			try {
				document.add(table);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Paragraph paragraph = new Paragraph();
			addEmptyLine(paragraph, 1);
			addFooter (document, cust, date, total);

		}	
	}
	private static void addTableCellRight (PdfPTable table, String data)  {

		PdfPCell c1 = new PdfPCell(new Phrase(data, smallerBold));
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setPaddingRight(4f);
		table.addCell(c1);		
	}
	private static void addTableCellRightBold (PdfPTable table, String data)  {

		PdfPCell c1 = new PdfPCell(new Phrase(data, smallerBold));
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setPaddingRight(4f);
		table.addCell(c1);		
	}

	private static void addTableCellLeft (PdfPTable table, String data)  {
		PdfPCell c1 = new PdfPCell(new Phrase(data, smallerFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c1.setPaddingRight(4f);
		table.addCell(c1);		
	}
	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}