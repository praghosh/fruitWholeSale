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
import entities.CustomerHalkhataEntry;
import entities.CustomerTransaction;
import entities.HalKhata;
import entities.Route;
import entities.TruckSales;
import nmfc.helper.Utility;


public class PrintHalkhataBalanceForAllCustomers {
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
	private static Font smallerBold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
			Font.BOLD);
	private static Font smallerFont = new Font(Font.FontFamily.TIMES_ROMAN, 9,
			Font.NORMAL);
	
	private static Font verySmallFont = new Font(Font.FontFamily.TIMES_ROMAN, 9,
			Font.NORMAL);
	
	private static Font tinyFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
			Font.NORMAL);
	
	private static Font veryTinyFont = new Font(Font.FontFamily.TIMES_ROMAN, 9,
			Font.NORMAL);
	 
	private static Font verySmallFontBold = new Font(Font.FontFamily.TIMES_ROMAN, 9,
			Font.BOLD);
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	public static void main(String[] args) {

		//createBillPrintPDF (FILE);

	}

	public static boolean createAllCustomerHalkhataPDF ( String FILE , 
			HalKhata halKhata) {

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(FILE));
			document.open();
			addMetaData(document);
			addContent(document, halKhata);
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


	private static void addContent(Document document, HalKhata halKhata) throws DocumentException {

		addHeader (document,  halKhata, 1);
		createTable( document, halKhata);

	}

	public static void addHeader (Document document, HalKhata halKhata,  int pageNumber) throws DocumentException {

		Paragraph paragraph = new Paragraph();		 
		paragraph.setFont( catFont);
		paragraph.add(new Paragraph("All Customers HalKhata Due List")); 
		paragraph.setFont( smallBold);	
		
	 	paragraph.add( "Halkhata Start Date :  " + formatter.format 
				(halKhata.getStartDate())) ;		 
		paragraph.add( "   Halkhata End Date :  " + formatter.format 
				(halKhata.getEndDate()));
		
		
		paragraph.add(new Paragraph("   Page Number -- " + pageNumber)); 
	
		addEmptyLine(paragraph, 2);
		document.add(paragraph);
	}
	
	
	
	private static void addTableCellLeftSmall (PdfPTable table, String data)  {

		PdfPCell c1 = new PdfPCell(new Phrase(data, smallerFont));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment(Element.ALIGN_LEFT);
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

	private static void addTableCellRight (PdfPTable table, String data)  {

		PdfPCell c1 = new PdfPCell(new Phrase(data, smallerFont));
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
			table.setWidths(new float[] { 1.6f, 1.2f, 1.2f, 1.2f, 1.2f, .7f ,.7f, 1.1f, 1.2f, 1.2f ,1.2f  });
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
		PdfPCell c1 = new PdfPCell(new Phrase("Customer Name", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment(Element.ALIGN_LEFT);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Disc. Sell", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);		 
		table.addCell(c1);
		
		
		c1 = new PdfPCell(new Phrase("Non-Disc. Sell", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);		 
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Tota Sell", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);		 
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Eligible Discount", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);		 
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("S. Crt Due", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);		 
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Big Crt Due", smallBold));
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("CashDue Befr Halkhata", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER); 
		c1.setVerticalAlignment(Element.ALIGN_RIGHT);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Total Discnt Given", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER); 
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
 	
		c1 = new PdfPCell(new Phrase("Cash Deposit on Halkhata", smallBold));
		c1.setVerticalAlignment(Element.ALIGN_CENTER);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("CashDue After Halkhata", smallBold));
		c1.setVerticalAlignment(Element.ALIGN_CENTER);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		table.setHeaderRows(1); 

	}

	private static void createTable( Document document, HalKhata halKhata)
			throws DocumentException {
		PdfPTable table = new PdfPTable(11);
		table.getDefaultCell().setMinimumHeight(25f);
		int pageNumber =1;
		createTableHeader (table);	 
		java.util.List <Customer> customerList = Customer.getCustomerLists() ;
		int counter =1;
		float total =0;
		// Total
	 	long totalDepositInHalkhataForAll =0;
		long totalDepositMadeForAll=0;
		long specialDiscountForAll=0;
	 	
		long totalBigCrateForAll=0;
		long totalSmallCrateForAll=0;
		long totalCashDueForAll =0;
		long totalCashDueAfterHalkhataForAll =0;
		long totalElligibleDiscountForAll =0;
		long totalDiscountSellForAll =0;
		long totalNonDiscountSellForAll =0;
		long totalSellForAll =0;
		long totalActualDiscountForAll=0;
		
		// page total
		long pageTotalBigCrateForAll=0;
		long pageTotalSmallCrateForAll=0;
		long pageTotalCashDueForAll =0;
		long pageTotalFinalDueForAll =0;
		long pageTotalEligibleDiscountForAll =0;		      
		long pageTotalDiscountSellForAll =0;
		long pageTotalNonDiscountSellForAll =0;
		long pageTotalSellForAll =0;
		long pageTotalDepositForAll =0;
		long pageTotalCashDueAfterHalkhataForAll =0;
		long pageTotalDepositInHalkhataForAll =0;
		long pageTotalActualDiscountForAll =0;
		
		String halKahataName = halKhata.getName();
		Date startDate = halKhata.getStartDate();
		Date endDate = halKhata.getEndDate();
		
		for ( Customer  customer: customerList) {
			/*  Table Data will be entered in this order
		           // Discount sell             
                   //non disc. sell         
                   //  total sell                  
                   // elligible disc.           
                   //  small crate                 
                   //  big crate                   
                   //  cash due before halkhata    
                   // Actual Discount 
                   // Cash Deposit    
                    // Final Due    
 		  
			 */
			CustomerHalkhataEntry  custHalkhata = 
					CustomerHalkhataEntry.getCustomerHalkhataEntry(customer, halKhata);	
			
			long actualDiscount=0;			
			float discountPercentGiven;
			long discountedSell, nonDiscountedSell,totalDepositMade;
			long eligibleDiscount, totalCashDue, smallCrateDue, bigCrateDue;
			long specialDiscount, cashDepositInHalkhata, bigCrateDepositInHalkhata;
			long smallCrateDepositInHalkhata, cashDueAfterDiscount, finalDueAfterHalkhata;
			
			if (custHalkhata==null) {
				discountedSell = (long) TruckSales.getTotalDiscountedSales 
						(customer, startDate, endDate );
				nonDiscountedSell = (long) TruckSales.getTotalNonDiscountedSales 
						(customer, startDate, endDate);
				totalDepositMade = (long) CreditEntry.getTotalDepositForCustomerBeforeDate 
						(customer, startDate, endDate );
				discountPercentGiven = Utility.getValue (customer.getDiscountEligible());
				if (discountPercentGiven==0) discountPercentGiven = 1; // If not defined then take as 1 %
				actualDiscount=0;
				eligibleDiscount = (long)  (discountedSell * discountPercentGiven *0.01);
				totalCashDue =0;
				smallCrateDue=0;
				bigCrateDue =0;
				specialDiscount = 0;
				// If some entry is entered in Credit Entry then
				// Find deposit from there
				cashDepositInHalkhata=0;
				java.util.List <CreditEntry> depositList = CreditEntry.getCreditEntryListOnDate
						(customer, endDate);
				if (depositList!=null && depositList.size() >0) {
					cashDepositInHalkhata = (long) Utility.getValue (depositList.get(0).getCashDeposit()) ;
				}
		 		
				bigCrateDepositInHalkhata=0;
				smallCrateDepositInHalkhata =0;
				java.util.List lastBlance = CustomerTransaction.getBalanceBeforeDate(customer, endDate);
				if (lastBlance!=null && lastBlance.size()!=0) {		
					totalCashDue =(long) Utility.getValue( (Float) lastBlance.get(2)); 							
					bigCrateDue = (long)lastBlance.get(0);
					smallCrateDue = (long) lastBlance.get(1);

				}
				cashDueAfterDiscount = totalCashDue - eligibleDiscount ;
				finalDueAfterHalkhata = totalCashDue - cashDepositInHalkhata - actualDiscount;				 
			}

			else {
				discountedSell = Utility.getValue(custHalkhata.getDiscountedSell());
				nonDiscountedSell =  Utility.getValue( custHalkhata.getNonDiscountedSell());
				totalDepositMade = Utility.getValue(custHalkhata.getTotalDepositMade());			
				discountPercentGiven = Utility.getValue( custHalkhata.getDiscountPercentGiven());
				eligibleDiscount = Utility.getValue(custHalkhata.getEligibleDiscount());
				totalCashDue =Utility.getValue(custHalkhata.getCashDue());
				smallCrateDue=Utility.getValue(custHalkhata.getSmallCrateDue());
				bigCrateDue = Utility.getValue(custHalkhata.getBigCrateDue());
				specialDiscount = Utility.getValue(custHalkhata.getSpecialDiscount());	
				cashDepositInHalkhata = Utility.getValue(custHalkhata.getCashDepositInHalkhata());
				bigCrateDepositInHalkhata =Utility.getValue( custHalkhata.getBigCrateDepositInHalkhata());
				smallCrateDepositInHalkhata = Utility.getValue( custHalkhata.getSmallCrateDepositInHalkhata());				 			 
				if (custHalkhata.getActualTotalDiscount()!=null) {
					actualDiscount =  custHalkhata.getActualTotalDiscount();

				}
				else {
					
					// This is done because acutal Discount was not entered for all customer.
					actualDiscount =  eligibleDiscount + specialDiscount;
				}

				
				cashDueAfterDiscount = totalCashDue - eligibleDiscount ;
				finalDueAfterHalkhata = totalCashDue - cashDepositInHalkhata - actualDiscount;	
			}
	 		 
			addTableCellLeftSmall (table, customer.getName());	
                                   
		                                                    
			addTableCellRight (table, discountedSell+" ");    // Discount sell                                  
			addTableCellRight (table, nonDiscountedSell+" ");   //non disc. sell                                     
			addTableCellRight (table, (nonDiscountedSell+ discountedSell) + " ");                    //  total sell                                 
			addTableCellRight (table, eligibleDiscount+" ");      // eligible disc.                               
			addTableCellRight (table, smallCrateDue + " ");    //  small crate                                
			addTableCellRight (table, bigCrateDue+" ");             //  big crate                                  
			addTableCellRight (table, totalCashDue+" ");      //  cash due before halkhata                      
			addTableCellRight (table, actualDiscount+" ");        // Actual Discount                             
			addTableCellRight (table, cashDepositInHalkhata+" ");            // Cash Deposit                                      
			addTableCellRight (table, finalDueAfterHalkhata + " ");     // Final Due                                       
	 	  	
		 	// Create  Grand Total
			
			totalDiscountSellForAll+=discountedSell;                   // Discount sell             
		    totalNonDiscountSellForAll+=nonDiscountedSell;          //non disc. sell         
		    totalSellForAll += discountedSell + nonDiscountedSell;        //  total sell                                 
            totalElligibleDiscountForAll+=eligibleDiscount;              // eligible disc.             
            totalSmallCrateForAll+=smallCrateDue;                         //  small crate                 
            totalBigCrateForAll+=bigCrateDue;                             //  big crate                   
            totalCashDueForAll+=totalCashDue;                          //  cash due before halkhata               
            totalActualDiscountForAll+=actualDiscount;                    // Actual Discount 
            totalDepositInHalkhataForAll+=cashDepositInHalkhata;       // Cash Deposit    
            totalCashDueAfterHalkhataForAll+=finalDueAfterHalkhata;   // Final Due   
     		
			// Create Total for Page 
            pageTotalDiscountSellForAll+=discountedSell;                   // Discount sell             
            pageTotalNonDiscountSellForAll+=nonDiscountedSell;          //non disc. sell         
            pageTotalSellForAll += discountedSell + nonDiscountedSell;        //  total sell                                  
            pageTotalEligibleDiscountForAll+=eligibleDiscount;              // eligible disc.            
            pageTotalSmallCrateForAll+=smallCrateDue;                         //  small crate                 
            pageTotalBigCrateForAll+=bigCrateDue;                             //  big crate                   
            pageTotalCashDueForAll+=totalCashDue;                          //  cash due before halkhata               
            pageTotalActualDiscountForAll+=actualDiscount;                    // Actual Discount 
            pageTotalDepositInHalkhataForAll+=cashDepositInHalkhata;       // Cash Deposit    
            pageTotalCashDueAfterHalkhataForAll+=finalDueAfterHalkhata;   // Final Due  
			 
			
			counter++;
			if (counter >30) {
				
				table.addCell (new PdfPCell(new Phrase("Page Total", smallerBold)));
				addTableCellRightBold (table, pageTotalDiscountSellForAll + " ") ; // Discount sell
				addTableCellRightBold (table, pageTotalNonDiscountSellForAll   + " "); //non disc. sell
				addTableCellRightBold (table, (pageTotalDiscountSellForAll + pageTotalNonDiscountSellForAll)  
						+ " ") ; // total sell
				addTableCellRightBold (table, pageTotalEligibleDiscountForAll   + " ") ; // Eligible disc.
				addTableCellRightBold (table, pageTotalSmallCrateForAll   + " ") ; // small crate
				addTableCellRightBold (table, pageTotalBigCrateForAll   + " ") ; //   big crate
				addTableCellRightBold (table, (pageTotalCashDueForAll )   + " ") ;  // cash due before halkhata
				addTableCellRightBold (table, (pageTotalActualDiscountForAll )   + " ") ; // Actual Discount
				addTableCellRightBold (table, (pageTotalDepositInHalkhataForAll )   + " ") ; // Cash Deposit
				addTableCellRightBold (table, (pageTotalCashDueAfterHalkhataForAll )   + " ") ; // Final Due
			 

				try {
					document.add(table);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				    pageTotalDiscountSellForAll=0;                  // Discount sell             
		            pageTotalNonDiscountSellForAll=0;               //non disc. sell         
		            pageTotalSellForAll=0;                         //  total sell                                  
		            pageTotalEligibleDiscountForAll=0;              // eligible disc.            
		            pageTotalSmallCrateForAll=0;                         //  small crate                 
		            pageTotalBigCrateForAll=0;                             //  big crate                   
		            pageTotalCashDueForAll=0;                          //  cash due before halkhata               
		            pageTotalActualDiscountForAll=0;                    // Actual Discount 
		            pageTotalDepositInHalkhataForAll=0;                  // Cash Deposit    
		            pageTotalCashDueAfterHalkhataForAll=0;              // Final Due   
	 	 		
				pageNumber++;
				document.newPage();
				addHeader (document, halKhata, pageNumber);
				table = new PdfPTable(11);
				table.getDefaultCell().setMinimumHeight(25f);
				createTableHeader (table);
				counter=0;
			}

		}
		
		// Page Total 
		table.addCell (new PdfPCell(new Phrase("Page Total", smallerBold)));
		addTableCellRightBold (table, pageTotalDiscountSellForAll + " ") ; // Discount sell
		addTableCellRightBold (table, pageTotalNonDiscountSellForAll   + " "); //non disc. sell
		addTableCellRightBold (table, (pageTotalDiscountSellForAll + pageTotalNonDiscountSellForAll)  
				+ " ") ; // total sell
		addTableCellRightBold (table, pageTotalEligibleDiscountForAll   + " ") ; // Eligible disc.
		addTableCellRightBold (table, pageTotalSmallCrateForAll   + " ") ; // small crate
		addTableCellRightBold (table, pageTotalBigCrateForAll   + " ") ; //   big crate
		addTableCellRightBold (table, (pageTotalCashDueForAll )   + " ") ;  // cash due before halkhata
		addTableCellRightBold (table, (pageTotalActualDiscountForAll )   + " ") ; // Actual Discount
		addTableCellRightBold (table, (pageTotalDepositInHalkhataForAll )   + " ") ; // Cash Deposit
		addTableCellRightBold (table, (pageTotalCashDueAfterHalkhataForAll )   + " ") ; // Final Due
		
		 
		// Grand Total
		table.addCell (new PdfPCell(new Phrase("Grand Total", smallerBold)));	        
		addTableCellRightBold (table, totalDiscountSellForAll + " ") ;// Discount sell                 
		addTableCellRightBold (table, totalNonDiscountSellForAll   + " ") ;//non disc. sell                 
		addTableCellRightBold (table, (totalNonDiscountSellForAll + totalDiscountSellForAll)  + " ") ;//total sell                    
		addTableCellRightBold (table, totalElligibleDiscountForAll   + " ") ;// eligible disc.                
		addTableCellRightBold (table, totalSmallCrateForAll   + " ") ;//  small crate             
		addTableCellRightBold (table, totalBigCrateForAll   + " ") ;//  big crate             
		addTableCellRightBold (table, totalCashDueForAll     + " ") ;//  cash due before halkhata  
		addTableCellRightBold (table, totalActualDiscountForAll  + " ");// Actual Discount           
		addTableCellRightBold (table, (totalDepositInHalkhataForAll )   + " ") ; // Cash Deposit             
		addTableCellRightBold (table,  totalCashDueAfterHalkhataForAll   + " ") ;// Final Due                 
        
		 	 

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