package pdftables;

import java.io.FileOutputStream;
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
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import entities.Customer;


public class CustomerListPDFTable {
	private static String FILE = "C:\\tmp\\FirstPdf2.pdf";
	private static Font catFont  = new Font(Font.FontFamily.TIMES_ROMAN, 16,
			Font.BOLD);
	private static Font catFont1 = FontFactory.getFont("Siyamrupali_1_01", 12,Font.BOLD);
			
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
			Font.BOLD);
	private static Font smallBold  = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);
	Font smallBold1 = FontFactory.getFont("Siyamrupali_1_01", 12); // Not working
	
	
	public static void main(String[] args) {

		createCustomerListPDF (FILE);

	}

	public static boolean createCustomerListPDF ( String FILE) {

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			addMetaData(document);
			addContent(document);
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
		document.addTitle("List of Customers");
		document.addSubject("For New Madina");
		document.addKeywords("Customer, PDF, iText");
		document.addAuthor("Prabir Ghosh");
		document.addCreator("Prabir Ghosh");
	}


	private static void addContent(Document document) throws DocumentException {

		// Set Title of the Report

		Paragraph paragraph = new Paragraph();
		paragraph.setFont( catFont);
		Paragraph title1 = new Paragraph("  New Madina Fruit Company\n Bagnan New Bus Stand"); 
		title1.setAlignment(Element.ALIGN_MIDDLE);
		 
		paragraph.add(title1); 
		paragraph.setFont( subFont);
		paragraph.add(new Paragraph("List Of Customers")); 
		addEmptyLine(paragraph, 2);
		document.add(paragraph);
		// add a table
		createTable( document);


	}
	
	private static void createTableHeader (PdfPTable table) {
		 
		table.setWidthPercentage(100);
		try {
			table.setWidths(new float[] { 2, 1, 1, 1 });
		} 
		catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		table.getDefaultCell().setMinimumHeight(25f);

		PdfPCell c1 = new CustomerTableCell("Customer Name") ;
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Route"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Address"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Mobile Number"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);


		table.addCell(c1);
		table.setHeaderRows(2);
		 
		
	}

	private static void createTable( Document document)
			throws BadElementException {
		PdfPTable table = new PdfPTable(4);
		/*
		table.setWidthPercentage(100);
		try {
			table.setWidths(new float[] { 2, 1, 1, 1 });
		} 
		catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		PdfPCell c1 = new PdfPCell(new Phrase("Customer Name"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Route"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Address"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Mobile Number"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);


		table.addCell(c1);
		table.setHeaderRows(1);
		*/
		createTableHeader (table);
		java.util.List <Customer> custrList = Customer.getCustomerLists();
		int counter =0;
		for ( Customer cust: custrList) {
			table.addCell (cust.getName());
			table.addCell (cust.getRoute().toString());
			table.addCell (cust.getAddress().getAddressLine1() + ", " +
					cust.getAddress().getAddressLine2()
					);
			table.addCell (cust.getMobile());
			counter++;
			if (counter >40) {
				
				try {
					document.add(table);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				document.newPage();
				table = new PdfPTable(4);
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

	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	static class CustomerTableCell extends PdfPCell {
		
		CustomerTableCell (String phrase) {
			super(new Phrase(phrase));
			this.setHorizontalAlignment(Element.ALIGN_CENTER);
			this.setMinimumHeight(20f); 
	 	}
		
		
		
	}
}