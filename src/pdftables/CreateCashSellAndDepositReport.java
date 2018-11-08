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


public class CreateCashSellAndDepositReport {
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

	public static boolean createCashSellAndDeposititeReport ( String FILE,  
			Date billDate) {

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			addMetaData(document);
			addContent(document,  billDate);
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
		document.addTitle("Cash Sell And Deposit ");
		document.addSubject("For New Madina");
		document.addKeywords("Customer, Bill, PDF ");
		document.addAuthor("Prabir Ghosh");
		document.addCreator("Prabir Ghosh");
	}


	private static void addContent(Document document, Date date) throws DocumentException {

		// Set Title of the Report

		Paragraph paragraph = new Paragraph();
		paragraph.setFont( catFont);
		paragraph.add(new Paragraph("Cash Sell And Deposit")); 
		paragraph.add(new Paragraph("Date -- " + formatter.format 
				( date)));
		paragraph.setFont( smallBold);	 	 	 
 		paragraph.add(new Paragraph("Page Number -- 1")); 
		addEmptyLine(paragraph, 2);
		document.add(paragraph);
		// add a table
		createDepositTable( document, date);
 	 
	}
	
	public static void addHeader (Document document, Date date, int pageNumber) throws DocumentException {
		
		Paragraph paragraph = new Paragraph();
		paragraph.setFont( catFont);
		paragraph.add(new Paragraph("Cash Deposit And Sell")); 
		paragraph.setFont( smallBold);		 	 
 		paragraph.add(new Paragraph("Date -- " + formatter.format 
				( date)));
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
 
	private static void createTableHeader (PdfPTable table, Date date, int pageNumber) {

		table.setWidthPercentage(100);
		try {
			table.setWidths(new float[] { 1, 1.5f, 2,2 });
		} 
		catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPCell c1 = new PdfPCell(new Phrase("Sl. No."));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_LEFT);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Bill Number"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);		 
		table.addCell(c1);
 
		c1 = new PdfPCell(new Phrase("Customer"));
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
  
		c1 = new PdfPCell(new Phrase("Deposit"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
 	 
		table.setHeaderRows(1); 

	}
	
	private static void createAshSellTableHeader (PdfPTable table ) {

		table.setWidthPercentage(100);
		try {
			table.setWidths(new float[] { 1, 1.5f, 1.5f,  2, 2,2 });
		} 
		catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPCell c1 = new PdfPCell(new Phrase("Sl. No."));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_LEFT);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Customer"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);		 
		table.addCell(c1);
 
		c1 = new PdfPCell(new Phrase("Item"));
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Rate"));
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Quantity"));
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
  
		c1 = new PdfPCell(new Phrase("Amount"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
 	 
		table.setHeaderRows(1); 

	}

	private static void createDepositTable( Document document, Date date)
			throws DocumentException {
		PdfPTable cashDepositTable = new PdfPTable(4);
		int pageNumber =1;
		createTableHeader (cashDepositTable,   date, pageNumber);	 
		java.util.List <CreditEntry> depositList = CreditEntry.getLastDepositDetail (date);	
		int counter=0;
		int serialNumber=1;		 
		float totalCash =0;
		
		for ( CreditEntry transcationEntry: depositList) {
			//table.addCell (counter+"");
			//table.addCell (transcationEntry.getEntryDate() +"");
			
			//table.addCell (transcationEntry.getParticular()+"");
			//table.addCell (transcationEntry.getCreditOrSalesDate()+"");
			cashDepositTable.addCell (serialNumber +"");
			addTableCellRight (cashDepositTable, transcationEntry.getBillNumber() +"" );	
			addTableCellRight (cashDepositTable,   transcationEntry.getCustomer()+"   ");
			totalCash+=Utility.getValue(transcationEntry.getCashDeposit());
			addTableCellRight (cashDepositTable, String.format( "%.0f", transcationEntry.getCashDeposit()));	 		
			serialNumber++;
			counter++;
			if (counter >35) {

				try {
					document.add(cashDepositTable);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pageNumber++;
				document.newPage();
				addHeader (document, date,   pageNumber);
				cashDepositTable = new PdfPTable(4);
				createTableHeader (cashDepositTable,   date, ++pageNumber);
				counter=0;

			}

		}
		
		cashDepositTable.addCell ("Total=");
		cashDepositTable.addCell (  "") ;
		cashDepositTable.addCell (  "") ;
		addTableCellRight (cashDepositTable, String.format( "%.0f", totalCash  )) ;
		

		try {
			document.add(cashDepositTable);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Paragraph paragraph = new Paragraph();		 
		addEmptyLine(paragraph, 3);
		document.add(paragraph);
		
		PdfPTable CashSellTable = new PdfPTable(6); 
		createAshSellTableHeader (CashSellTable );
		 
		java.util.List <TruckSales> cashSaleList = TruckSales.getCashPurchaseReport (date);
 
		serialNumber=1;		 
		float totalDeposit =0;
		
		for ( TruckSales trSales: cashSaleList) {
			 
			CashSellTable.addCell (serialNumber +"");
			addTableCellRight (CashSellTable,   trSales.getCustomer().toString());
			CashSellTable.addCell ( trSales.getFruit() +"");
			addTableCellRight (CashSellTable, trSales.getRate()+"");
			addTableCellRight (CashSellTable, trSales.getQuantity () +""  );
			totalDeposit+=Utility.getValue(trSales.getAmount());
			addTableCellRight (CashSellTable,  String.format( "%.0f", trSales.getAmount()  ));	 		
			serialNumber++;
			counter++;
			if (counter >35) {

				try {
					document.add(cashDepositTable);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pageNumber++;
				document.newPage();
				addHeader (document, date,   pageNumber);
				cashDepositTable = new PdfPTable(6);
				createAshSellTableHeader (CashSellTable );
				counter=0;

			}

		}
		
		CashSellTable.addCell ("Total=");
		CashSellTable.addCell (  "") ;
		CashSellTable.addCell (  "") ;
		CashSellTable.addCell (  "") ;
		CashSellTable.addCell (  "") ;
		addTableCellRight (CashSellTable,   String.format( "%.0f", totalDeposit )  ) ;
	 	
		try {
			document.add(CashSellTable);
		} 
		catch (DocumentException e) {
			 
			e.printStackTrace();
		}
		paragraph = new Paragraph();
		addEmptyLine(paragraph, 1);
		paragraph.setFont( smallBold);
		paragraph.setAlignment(Element.ALIGN_RIGHT);
 		paragraph.add( "Total Cash Received " +  String.format( "%.0f", totalDeposit + totalCash)); 
		document.add(paragraph);
	 
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}