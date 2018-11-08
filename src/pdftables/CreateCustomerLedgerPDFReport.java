package pdftables;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

import entities.Address;
import entities.Customer;
import entities.CustomerTransaction;
import entities.Franchise;
import entities.Fruit;
import entities.FruitQuality;
import entities.Truck;
import entities.TruckEntry;
import entities.TruckSales;
import nmfc.helper.StandardItems;
import nmfc.helper.Utility;


public class CreateCustomerLedgerPDFReport {


	private PDDocument doc;
	private PDPageContentStream contentStream;
	private PDFont font = PDType1Font.HELVETICA_BOLD;
	private PDPage page;
	private int topMargin;
	private int columnArrays[];
	private String[] [] contents;
	
	private static String FILE = "C:\\tmp\\FirstPdf2.pdf";
	private static Font catFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 16,
			Font.BOLD);
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
			Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
			Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);
 

	public void createTitle (int pageNumber, Customer customer, Date fromDate, Date toDate) throws IOException {

		page = new PDPage(PDPage.PAGE_SIZE_A4);
		doc.addPage( page );
		// Start a new content stream which will "hold" the to be created content

		contentStream = new PDPageContentStream(doc, page);

		contentStream.beginText();	 
		contentStream.setFont( font, 13 );
		contentStream.moveTextPositionByAmount( 180, PDPage.PAGE_SIZE_A4.getHeight()-25 );
		contentStream.drawString( "New Madina Fruit Company" );
		contentStream.endText();

		contentStream.beginText();	 
		contentStream.moveTextPositionByAmount( 170, PDPage.PAGE_SIZE_A4.getHeight()-50 );
		contentStream.drawString( "Bagnan  *  Bus Stand  *  Howrah" );
		contentStream.endText();

		contentStream.drawLine(200, PDPage.PAGE_SIZE_A4.getHeight()-30, 320,
				PDPage.PAGE_SIZE_A4.getHeight()-30);
		contentStream.beginText();
		contentStream.setFont( font, 11 );
		contentStream.moveTextPositionByAmount( 20, PDPage.PAGE_SIZE_A4.getHeight()-75 );		
		try {
			String customerDetail = "Customer Name: " + customer.getName() + "       ";
			customerDetail+= "Code Name: " + customer.getCode();
			customerDetail+=  "     Father' Name: " + customer.getParentName();
			contentStream.drawString( customerDetail );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentStream.endText();


		contentStream.beginText();
		contentStream.setFont( font, 11 );
		contentStream.moveTextPositionByAmount( 20, PDPage.PAGE_SIZE_A4.getHeight()-95 );
		Address adr = customer.getAddress();
		String adrString ="N/A";
		if (adr != null  )
		{
			adrString =adr.getAddressLine1() + "--" + adr.getCity();
		}
		String phone = customer.getMobile();


		contentStream.drawString( "Address: " + adrString +
				"      Phone: " + 
				phone);
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont( font, 11 );
		contentStream.moveTextPositionByAmount( 20, PDPage.PAGE_SIZE_A4.getHeight()-110 );
		contentStream.drawString("From: " + StandardItems.getFormatter().format(fromDate) +
				"    To: " + StandardItems.getFormatter().format(toDate)
				+ "  ----Page Number" + pageNumber
				);
		contentStream.endText();
		

	}


	public void createLedgerHeader (int topMargin) throws IOException {		

		contents = new String[][] {{"Date","Particular", "S Crate", 
			"Deposit","Due", "B Crate",
			"Deposit", "Due",  "Non Disc.", 
			" Disc. Sale", "Deposit", "Cash Due"}};
			//	topMargin = 140;
			columnArrays = new int[] {11, 22, 7, 7, 7,
					7,7,7, 11,
					11, 11, 11};
			CreateTableUtitlity.drawTable(page, contentStream, topMargin, 10, columnArrays, contents);
	}

	/*
	 *  Create Report on Truck Sales Fruit (along with Quality) wise.
	 */
	public    void createCustomerLedgerReport (Customer customer, String docPath, Date startDate,
			Date endDate) throws IOException, COSVisitorException {


		doc = new PDDocument();		
		font = PDType1Font.HELVETICA_BOLD;
		int pageNumber=1;
		createTitle (pageNumber, customer, startDate, endDate);
		
		// All  Page Total
		int  totalSmallCrateIssuedPageTotal=0;	 
		int    totalSmallCrateDepositPageTotal=0;   
		   
		
		int totalBigCrateIssuedPageTotal=0;	   		 
		int totalBigCrateDepositPageTotal=0;	  
		 
		
		int totalCashPurchase=0 ;  
		int totalDiscountedPurchase=0;
		int totalCashDeposit=0 ; 
		

		List <CustomerTransaction>  transactionList= 
				CustomerTransaction.getTransactionListByEntryDate(customer, startDate, endDate);
		topMargin =190;
		float toTotalSaleAmount = 0;
		int bottomMargin = 30;
		int curentMargin = topMargin;  
		int totalRowHeight = 0;
		List <String []> contentList = new ArrayList <String []> ();
		createLedgerHeader(topMargin-55);
		int sizeOfItems = transactionList.size();
		int counter=0;
		for (CustomerTransaction transactionItem:transactionList) {
			counter++;
			// First check if margin is exhausted after printing the ledger data
			// so far. Then create anew page for the same customer
			if (curentMargin > PDPage.PAGE_SIZE_A4.getHeight()-bottomMargin -50) {
				//Add page Total
				String [] content = new String [12];  // number of columns;	
				content [0] = "Page Total";
				content [1] = " ";
				content [2] = String.format( "%d",totalSmallCrateIssuedPageTotal);	 
				content [3] =  String.format( "%d",totalSmallCrateDepositPageTotal);   
				content [4] =  "";  
				
				content [5] =   String.format( "%d",totalBigCrateIssuedPageTotal);	   		 
				content [6] =  String.format( "%d", totalBigCrateDepositPageTotal);	  
				content [7] = "" ;
				
				content [8] =  String.format( "%d",totalCashPurchase) ;  
				content [9] = String.format( "%d",totalDiscountedPurchase);
				content [10] =  String.format( "%d", totalCashDeposit) ; 	
				
				content [11] =    ""; 
				contentList.add(content);
				  totalSmallCrateIssuedPageTotal=0;	 
				  totalSmallCrateDepositPageTotal=0;   
				   
				
				  totalBigCrateIssuedPageTotal=0;	   		 
				  totalBigCrateDepositPageTotal=0;	  
				 
				
				  totalCashPurchase=0 ;  
				  totalDiscountedPurchase=0;
				  totalCashDeposit=0 ; 
				
			 	
				
				
				
				pageNumber++;
				CreateTableUtitlity.drawTable(page,contentStream, topMargin, 10, columnArrays, contentList);
				contentStream.close();
				createTitle (pageNumber, customer, startDate, endDate);				
				curentMargin = topMargin =190;
				createLedgerHeader(topMargin-55);
				totalRowHeight=0;				 
				contentList = new ArrayList <String []> ();					
			}
 			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");			 
			String [] content = new String [12];  // number of columns;				 
			content [0] = formatter.format (transactionItem.getCreditOrSalesDate()) ;
			String particular =  Utility.getString (transactionItem.getParticular());	
			particular =  particular.substring(0, Math.min(particular.length(), 15));
			
			float quantity = Utility.getValue(transactionItem.getItemRate());
			
			String qtyString="";
			
			if(quantity!=0){
				qtyString="X"+String.format( "%.0f", quantity);
			}
			
			String qty =Utility.getString (transactionItem.getItemRate());
			content [1] = particular + qtyString;
			
			 
		       
		   
		
		  	  
		 
	 		
			content [2] =  Utility.getString (transactionItem.getCrateIssue());	
			totalSmallCrateIssuedPageTotal+= Utility.getValue(transactionItem.getCrateIssue());
			content [3] =  Utility.getString (transactionItem.getCrateDeposit());
			 totalSmallCrateDepositPageTotal+=Utility.getValue(transactionItem.getCrateDeposit());
			content [4] =  Utility.getString ( transactionItem.getSmallCrateDue());   
			
			content [5] =  Utility.getString (transactionItem.getBigCrateIssue()); 
			totalBigCrateIssuedPageTotal+=Utility.getValue(transactionItem.getBigCrateIssue()); 
			content [6] = Utility.getString (transactionItem.getBigCrateDeposit()); 
			totalBigCrateDepositPageTotal+=Utility.getValue (transactionItem.getBigCrateDeposit());;	 
			content [7] = Utility.getString (transactionItem.getBigCrateDue());  
			
			content [8] = String.format( "%.0f",  transactionItem.getCashPurchase()) ;  
			totalCashPurchase+=Utility.getValue (transactionItem.getCashPurchase()) ;  
			content [9] = String.format( "%.0f",  transactionItem.getDiscountedPurchase()) ; 
			totalDiscountedPurchase+=Utility.getValue (transactionItem.getDiscountedPurchase()) ; 
			content [10] =  String.format( "%.0f",  transactionItem.getCashDeposit()) ; 	
			totalCashDeposit+= (int) Utility.getValue ( transactionItem.getCashDeposit())   ;  
			content [11] =    String.format( "%.0f",  transactionItem.getCashDue())   ;  
			
			curentMargin+=20; // Current Row Height
			totalRowHeight+=20; 					
			contentList.add (content);
			// Add another line for final balance if last row is   final row.
			
			if (sizeOfItems==counter) {
				
				content = new String [12];  // number of columns;				 
				content [0] = formatter.format (transactionItem.getCreditOrSalesDate()) ;				 
				content [1] = "";
				
				content [2] =  "";		 
				content [3] =  "";   
				long finalCrateDue =  Utility.getValue( transactionItem.getSmallCrateDue()) +
						 Utility.getValue (transactionItem.getCrateIssue()) - 
						 Utility.getValue(transactionItem.getCrateDeposit());   
				content [4] = finalCrateDue +"";
				
				content [5] = "";  		 
				content [6] = "";	  			  
				long finalBigCrateDue = Utility.getValue (transactionItem.getBigCrateDue()) 
						+  Utility.getValue (transactionItem.getBigCrateIssue()) 
						- Utility.getValue (transactionItem.getBigCrateDeposit()) ;
				content [7] = finalBigCrateDue +"";
				
				content [8] ="";  
				content [9] = "";
				content [10] = ""; 	 			 	
				
				
				float finalCashDue = Utility.getValue (transactionItem.getCashDue()) 
						+  Utility.getValue (transactionItem.getDiscountedPurchase()) 
						+  Utility.getValue (transactionItem.getCashPurchase()) 
						- Utility.getValue (transactionItem.getCashDeposit()) ;
				content [11] = String.format( "%.0f", finalCashDue) + "";
				contentList.add (content);
			}
	 	//	CreateTableUtitlity.drawTable(page, contentStream, topMargin, 10, columnArrays, contentList);
		// 	topMargin+=totalRowHeight +25; // 25 buffer size

		}

		CreateTableUtitlity.drawTable(page, contentStream, topMargin-35, 10, columnArrays, contentList);

		contentStream.close();
		doc.save(docPath );

	}
	

	public static void main(String[] args) throws COSVisitorException, IOException {
		CreateCustomerLedgerPDFReport mt = new CreateCustomerLedgerPDFReport();

	}

}

