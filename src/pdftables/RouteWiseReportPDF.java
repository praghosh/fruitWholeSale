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


public class RouteWiseReportPDF {
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
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	public static void main(String[] args) {

		//createBillPrintPDF (FILE);

	}

	public static boolean createRouteWiseDetailPDF ( String FILE, Route route, 
			Date billDate) {

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			addMetaData(document);
			addContent(document, route, billDate);
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
		document.addTitle("Route Wise Due");
		document.addSubject("For New Madina");
		document.addKeywords("Customer, Bill, PDF, iText");
		document.addAuthor("Prabir Ghosh");
		document.addCreator("Prabir Ghosh");
	}


	private static void addContent(Document document, Route route
			, Date date) throws DocumentException {

		// Set Title of the Report

		Paragraph paragraph = new Paragraph();
		paragraph.setFont( catFont);
		paragraph.add(new Paragraph("Routewise Due List")); 
		paragraph.setFont( smallBold);
		paragraph.add(new Paragraph("Route Name -- " + route.getName())); 		 	 
 		paragraph.add(new Paragraph("Last Balance Update Date -- " + formatter.format 
				(CustomerTransaction. getLastUpadateDate()))); 
 		paragraph.add(new Paragraph("Page Number -- 1")); 
		addEmptyLine(paragraph, 2);
		document.add(paragraph);
		// add a table
		createTable( document, route, date);
 	 
	}
	
	public static void addHeader (Document document, Route route, int pageNumber) throws DocumentException {
		
		Paragraph paragraph = new Paragraph();
		paragraph.setFont( catFont);
		paragraph.add(new Paragraph("Routewise Due List")); 
		paragraph.setFont( smallBold);
		paragraph.add(new Paragraph("Route Name -- " + route.getName())); 		 	 
 		paragraph.add(new Paragraph("Last Balance Update Date -- " + formatter.format 
				(CustomerTransaction. getLastUpadateDate())));
 		paragraph.add(new Paragraph("Page Number -- " + pageNumber)); 
		addEmptyLine(paragraph, 2);
		document.add(paragraph);
	}
	
	private static void addTableCellRight (PdfPTable table, String data)  {
		
		PdfPCell c1 = new PdfPCell(new Phrase(data));
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setPaddingRight(4f);
		table.addCell(c1);
		
		
	}
 
	private static void createTableHeader (PdfPTable table, int pageNumber) {

		table.setWidthPercentage(100);
		try {
			table.setWidths(new float[] { .6f, 1, 1, 1.6f, 2,2 });
		} 
		catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPCell c1 = new PdfPCell(new Phrase("Sl. No."));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_LEFT);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("S. Crt Due"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);		 
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Big Crt Due"));
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Cash Due"));
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
  
		c1 = new PdfPCell(new Phrase("Name"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Mobile Phone"));
		c1.setVerticalAlignment(Element.ALIGN_CENTER);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		table.setHeaderRows(1); 

	}

	private static void createTable( Document document, Route route, Date date)
			throws DocumentException {
		PdfPTable table = new PdfPTable(6);
		int pageNumber =1;
		createTableHeader (table, pageNumber);	 
		java.util.List <CustomerTransaction> custrList = CustomerTransaction.getFinalBalanceList (route);
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
			table.addCell (serialNumber +"");
			addTableCellRight (table, transcationEntry.getSmallCrateDue()+"  ");	
			totalSmallCrate+=transcationEntry.getSmallCrateDue();
			addTableCellRight (table,   transcationEntry.getBigCrateDue()+"   ");
			totalBigCrate+= transcationEntry.getBigCrateDue();
			addTableCellRight (table, transcationEntry.getCashDue()+"  ");
			totalCash+=transcationEntry.getCashDue();
			table.addCell ( transcationEntry.getCustomer().toString()+"");
			table.addCell ( transcationEntry.getCustomer().getMobile()+"");
			
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
				addHeader (document, route, pageNumber);
				table = new PdfPTable(6);
				createTableHeader (table, ++pageNumber);
				counter=0;

			}

		}
		
		table.addCell ("Total");
		addTableCellRight (table, totalSmallCrate + " ") ;
		addTableCellRight (table, totalBigCrate + " ") ;
		addTableCellRight (table, totalCash + " ") ;
		table.addCell (  "") ;
		table.addCell (  "") ;
		table.addCell (  "") ;

		try {
			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	Paragraph paragraph = new Paragraph();
		addEmptyLine(paragraph, 2);
		paragraph.setFont( smallBold);
		String billAmount = "Bill Amount =  Rs " +  String.format( "%.2f", total ) ; 
		paragraph.setFont( subFont);
		paragraph.add(new Paragraph(billAmount)); 
		addEmptyLine(paragraph, 1);
		try {
			document.add(paragraph);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	*/
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}