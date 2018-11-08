package pdftables;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import entities.Route;
import entities.TruckSales;
import nmfc.helper.Utility;


public class TotalTruckSaleReport {
	private static String FILE = "C:\\tmp\\FirstPdf2.pdf";
	private static Font catFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.BOLD);
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
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

	public static boolean createTotalTruckSalePDF ( String FILE , 
			Date startDate, Date endDate ) {

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			addMetaData(document);
			addContent(document, endDate);
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
		document.addTitle("All Customer Bill  Print");
		document.addSubject("For New Madina");
		document.addKeywords("Customer, Bill, PDF, iText");
		document.addAuthor("Prabir Ghosh");
		document.addCreator("Prabir Ghosh");
	}


	private static void addContent(Document document, Date date) throws DocumentException {

		// Set Title of the Report

		Paragraph paragraph = new Paragraph();
		Paragraph rightParagraph = new Paragraph();
		paragraph.setFont( catFont);
		paragraph.add(new Paragraph("All Customers Due List")); 
		paragraph.setFont( smallBold);	 	 		 		 
		rightParagraph.add( "Balance Update Date :  " + formatter.format 
				(CustomerTransaction. getLastUpadateDate())) ;
		rightParagraph.add( "              Bill Print Date :  " + formatter.format 
				(new Date())) ;
		
		paragraph.add (rightParagraph);
		paragraph.add(new Paragraph("Page Number -- 1"));
		addEmptyLine(paragraph, 2);
		document.add(paragraph);
		// add a table
		createTable( document, date);

	}

	public static void addHeader (Document document, int pageNumber) throws DocumentException {

		Paragraph paragraph = new Paragraph();
		Paragraph rightParagraph = new Paragraph();
		paragraph.setFont( catFont);
		paragraph.add(new Paragraph("All Customer Due List")); 
		paragraph.setFont( smallBold);			
		paragraph.setFont( smallBold);	 	 		 		 
		rightParagraph.add( "Balance Update Date :  " + formatter.format 
				(CustomerTransaction. getLastUpadateDate())) ;
		rightParagraph.add( "              Bill Print Date :  " + formatter.format 
				(new Date())) ;		
		paragraph.add (rightParagraph);		 
		paragraph.add(new Paragraph("Page Number -- " + pageNumber)); 
	
		addEmptyLine(paragraph, 2);
		document.add(paragraph);
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

	private static void addTableCellCenter (PdfPTable table, String data)  {

		PdfPCell c1 = new PdfPCell(new Phrase(data, smallerFont));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c1.setPaddingRight(4f);
		table.addCell(c1);		
	}

	private static void createTableHeader (PdfPTable table) {

		table.setWidthPercentage(100);

		try {
			table.setWidths(new float[] { .5f, .7f, .8f, 1.2f, 1.8f ,1.8f });
		} 
		catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPCell c1 = new PdfPCell(new Phrase("Sl. No.", smallerBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_LEFT);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("S. Crt Due", smallerBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);		 
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Big Crt Due", smallerBold));
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Cash Due", smallerBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER); 
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Name", smallerBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER); 
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Mobile Phone", smallerBold));
		c1.setVerticalAlignment(Element.ALIGN_CENTER);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		table.setHeaderRows(1); 

	}

	private static void createTable( Document document, Date date)
			throws DocumentException {
		PdfPTable table = new PdfPTable(6);
		table.getDefaultCell().setMinimumHeight(25f);
		int pageNumber =1;
		createTableHeader (table);	 
		java.util.List <CustomerTransaction> custrList = CustomerTransaction.getFinalBalanceList ();
		int counter =1;
		int serialNumber=1;
		float total =0;
		int totalBigCrate=0;
		int totalSmallCrate=0;
		float totalCash =0;

		for ( CustomerTransaction transcationEntry: custrList) {
			//table.addCell (counter+"");
			//table.addCell (transcationEntry.getEntryDate() +"");

			//table.addCell (transcationEntry.getParticular()+"");
			//table.addCell (transcationEntry.getCreditOrSalesDate()+"");
			addTableCellCenter( table, serialNumber +"");
			addTableCellRight (table, transcationEntry.getSmallCrateDue()+"  ");	
			totalSmallCrate+=transcationEntry.getSmallCrateDue();
			addTableCellRight (table,   transcationEntry.getBigCrateDue()+"   ");
			totalBigCrate+= transcationEntry.getBigCrateDue();
			float cashDue = Utility.getValue( transcationEntry.getCashDue());
			float customerLimit = Utility.getValue(transcationEntry.getCustomer().getCreditLImit());
			String creditAlertString="";
			// If customer balance become less than 10% of the 
			// customer limit then show the alert
			if (customerLimit!=0 && (customerLimit -cashDue) <=customerLimit*0.1 ) {
				creditAlertString="**";
			}
			addTableCellRight (table, creditAlertString +  String.format( "%.0f", cashDue ));
			totalCash+=transcationEntry.getCashDue();
			addTableCellCenter( table, transcationEntry.getCustomer().toString() );
			addTableCellCenter( table, transcationEntry.getCustomer().getMobile() );

			serialNumber++;
			counter++;
			if (counter >35) {

				try {
					document.add(table);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pageNumber++;
				document.newPage();
				addHeader (document, pageNumber);
				table = new PdfPTable(6);
				table.getDefaultCell().setMinimumHeight(25f);
				createTableHeader (table);
				counter=0;

			}

		}

		table.addCell (new PdfPCell(new Phrase("Total", smallerBold)));
		addTableCellRightBold (table, totalSmallCrate + " ") ;
		addTableCellRightBold (table, totalBigCrate + " ") ;
		addTableCellRightBold (table,  String.format( "%.0f", totalCash)  + " ") ;
		table.addCell (  "") ;
		table.addCell (  "") ;
		table.addCell (  "") ;

		try {
			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}