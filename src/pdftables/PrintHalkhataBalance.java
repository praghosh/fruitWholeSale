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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import entities.CreditEntry;
import entities.Customer;
import entities.CustomerTransaction;
import entities.HalKhata;
import entities.TruckSales;
import nmfc.helper.StandardItems;
import nmfc.helper.Utility;


public class PrintHalkhataBalance {
	private static String FILE = "C:\\tmp\\FirstPdf2.pdf";
	private static Font catFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.BOLD);
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 11,
			Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 11,
			Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
			Font.BOLD);
	
	private static Font smallerBold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
			Font.BOLD);
	private static Font smallerFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
			Font.NORMAL);

	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


	public static void main(String[] args) {

		//createBillPrintPDF (FILE);

	}

	public static boolean createHalKhataPrintPDF ( String FILE, Customer cust, 
			HalKhata halkhata) {

		try {
			Document document = new Document(PageSize.A6);			 
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			addMetaData(document);
			addContent(document, cust, halkhata);
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
		document.addTitle("Halkhata Print");
		document.addSubject("For New Madina");
		document.addKeywords("Customer, Bill, PDF, iText");
		document.addAuthor("Prabir Ghosh");
		document.addCreator("Prabir Ghosh");
	}


	private static void addContent(Document document, Customer cust
			, HalKhata halkhata) throws DocumentException {

		// Set Title of the Report
		addHeader (document, cust, halkhata);
		createTable (document, cust, halkhata);
 		
	}
	
	

	private static void addHeader(Document document, Customer cust, HalKhata halkhata) throws DocumentException {
		 	 
		Paragraph title1 = new Paragraph("*New Madina Fruit Company*\n*Halkhata Balance*", catFont); 
		title1.setAlignment(Element.ALIGN_CENTER);
		title1.setFont( catFont);
		document.add(title1); 
		Date   startDate = halkhata.getStartDate();
		Date   endDate = halkhata.getEndDate () ;
		Paragraph paragraph = new Paragraph();
		addEmptyLine(paragraph, 1);
	 	paragraph.setFont( smallBold);
		paragraph.add(new Paragraph("Name of the customer: " + cust.getName())); 
		paragraph.add(new Paragraph
				("Start Date: " + StandardItems.getFormatter().format(halkhata.getStartDate()))); 
		paragraph.add(new Paragraph
				("End Date: " + StandardItems.getFormatter().format(halkhata.getEndDate ()))); 		  
		
		addEmptyLine(paragraph, 1);
		document.add(paragraph);
		
	}

	private static void createTableHeader (PdfPTable table) {

		table.setWidthPercentage(100);

		try {
			table.setWidths(new float[] { 1.5f, 1.1f });
		} 
		catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * Columns of Data in tables
		 *  "Customer Name" , "Disc. Sell" , "Non-Disc. Sell",   "Normal Discount", ,"S. Crt Due", 
          "Big Crt Due", "CashDue Befr Halkhata"   "Additional Discnt",    "CashDue After Discnt",  
	
          "Cash Deposit on Halkhata" 
		 * 
		 */
		PdfPCell c1 = new PdfPCell(new Phrase("Item", smallerBold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment(Element.ALIGN_LEFT);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Amount", smallerBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);		 
		table.addCell(c1);
		
	 	 

		table.setHeaderRows(1); 

	}
	
	  
	private static void createTable( Document document, Customer cust, HalKhata halkhata)
			throws BadElementException {
		 
		PdfPTable table = new PdfPTable(2);
		table.getDefaultCell().setMinimumHeight(25f);
		createTableHeader (table);
		Date   startDate = halkhata.getStartDate();
		Date   endDate = halkhata.getEndDate () ;
		long dicountedSell = (long) TruckSales.getTotalDiscountedSales 
				(cust , startDate, endDate );
		 
		long nonDicountedSell = (long) TruckSales.getTotalNonDiscountedSales 
				(cust , startDate, endDate );
		
	 	
		long totalDepositMade = (long) CreditEntry.getTotalDepositForCustomerBeforeDate 
				(cust , startDate, endDate );	
		
	 
		
		float discountPercentGiven =  Utility.getValue (cust.getDiscountEligible());
		if (discountPercentGiven==0) discountPercentGiven = 1; // If not defined then take as 1 %
		
		long eligibleDiscount = (long)  (dicountedSell * discountPercentGiven *0.01);
		
		addTableCellLeft (table,   "Discounted Sell ");
		addTableCellRight (table,   dicountedSell + " ");
		
		addTableCellLeft (table,   "Non Discounted Sell ");
		addTableCellRight (table,   nonDicountedSell + " ");
		
		addTableCellLeft (table,   "Total Deposit Made ");
		addTableCellRight (table,   totalDepositMade + " ");
		
		addTableCellLeft (table,   "Normal Discount Elligible: ");
		addTableCellRight (table,   eligibleDiscount + " ");
		
		addTableCellLeft (table,   "Special Discount : ");
		addTableCellRight (table,     " ");
	 
		  
		
		java.util.List lastBlance = CustomerTransaction.getBalanceOnOrBeforeDate(cust, endDate);

		float cashDue =0;
		long totalCashDue =0;
		long smallCrateDue=0;
		long bigCrateDue =0;

		if (lastBlance!=null && lastBlance.size()!=0) {
			cashDue = Utility.getValue( (Float) lastBlance.get(2));			
			totalCashDue =(long) cashDue; 							
			bigCrateDue = (long)lastBlance.get(0);
			smallCrateDue = (long) lastBlance.get(1);

		}
		addTableCellLeft (table,   "Small Crate Due: ");
		addTableCellRight (table, smallCrateDue + " ");
		
		addTableCellLeft (table,   "Big Crate Due: ");
		addTableCellRight (table, bigCrateDue+" ");
		
		addTableCellLeft (table,   "Cash Due: ");
		addTableCellRight (table, totalCashDue+" ");
		
		long additionalDiscount =0;
	/*	addTableCellLeft (table,   "Special Discount: ");
		addTableCellRight (table, additionalDiscount + " ");
		*/
	 	

		long finalDue = totalCashDue - eligibleDiscount - additionalDiscount ;
		
		addTableCellLeft (table,   "Final Amount to be Paid: ");		
		addTableCellRight (table, finalDue + " ");
		
		try {
			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment(Element.ALIGN_LEFT);
		c1.setPaddingRight(4f);
		table.addCell(c1);		
	}
	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}