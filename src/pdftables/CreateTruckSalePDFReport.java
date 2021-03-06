package pdftables;
 
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import entities.Franchise;
import entities.Fruit;
import entities.FruitQuality;
import entities.Truck;
import entities.TruckEntry;
import entities.TruckSales;
import nmfc.helper.StandardItems;
import nmfc.helper.Utility;


public class CreateTruckSalePDFReport {


	private PDDocument doc;
	private PDPageContentStream contentStream;
	private PDFont font = PDType1Font.HELVETICA_BOLD;
	private PDPage page;
	private int topMargin;
	private int columnArrays[];
	private String[] [] contents;

	public void createPage () throws IOException, COSVisitorException {

		// Create a document and add a page to it
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(PDPage.PAGE_SIZE_A4);

		document.addPage( page );
 	// Start a new content stream which will "hold" the to be created content
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
		contentStream.beginText();
		contentStream.setFont( font, 12 );
		contentStream.moveTextPositionByAmount( 100, 700 );
		contentStream.drawString( "Final Sales On Truck" );
		contentStream.endText();


		PDRectangle mediabox = page.findMediaBox();
		float margin = 20;
		float startX = mediabox.getLowerLeftX() + margin;
		float startY = mediabox.getUpperRightY() - margin;
		final int rows = 3;
		final float rowHeight = 20f;

		final float tableWidth = page.findMediaBox().getWidth() - (2 * margin) ;

		//draw the rows
		float nexty = 650;
		for (int i = 0; i <= rows; i++) {
			if (i == 0 || i == 1) {
				contentStream.drawLine(margin, nexty, margin + tableWidth, nexty);
			}
			nexty -= rowHeight;
		}

		contentStream.drawLine(margin, 300, margin + tableWidth, 300);
		int y = 650;

		//drawing columns the columns
		float nextx = 50;

		int h = 300;
		contentStream.drawLine(nextx, y, nextx, h);
		nextx = 100;
		contentStream.drawLine(nextx, y, nextx, h);
		nextx = 350;
		contentStream.drawLine(nextx, y, nextx, h);
		nextx = 420;
		contentStream.drawLine(nextx, y, nextx, h);
		nextx = 475;
		contentStream.drawLine(nextx, y, nextx, h);
		nextx = 545;
		contentStream.drawLine(nextx, y, nextx, h);


		// Make sure that the content stream is closed:
		contentStream.close();

		// Save the results and ensure that the document is properly closed:
		document.save( "FinalSale.pdf");
		document.close();


	}

 
	void cratePDFdoc  () throws IOException, COSVisitorException {

		doc = new PDDocument();
		PDPage page  = new PDPage(PDPage.PAGE_SIZE_A4);
		doc.addPage( page );

		// Start a new content stream which will "hold" the to be created content

		contentStream = new PDPageContentStream(doc, page);

		// Create a new font object selecting one of the PDF base fonts
		PDFont font = PDType1Font.HELVETICA_BOLD;



		// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
		contentStream.beginText();
		contentStream.setFont( font, 12 );
		contentStream.moveTextPositionByAmount( 200, PDPage.PAGE_SIZE_A4.getHeight()-25 );
		contentStream.drawString( "Final Sales On Truck" );
		contentStream.endText();
		contentStream.drawLine(200, PDPage.PAGE_SIZE_A4.getHeight()-30, 320,
				PDPage.PAGE_SIZE_A4.getHeight()-30);

		String[][] content = {{"a","b", "1", "g"}, 
				{"c","d", "2", "L"}, 
				{"e","f", "3", "o"}, 
				{"g","h", "4", "1"}, 
				{"i","j", "5", "d"}} ;
		String[][] content2 = {{"a","b", "1", "g"}, 
				{"c","d", "2", "L"}, 				  		 
				{"i","j", "5", "d"}} ;

		String[][] content3 = {{"a","b", "1", "g"}, 
				{"c","d", "2", "L"}, 
				{"e","X", "3", "o"}, 		 
				{"i","j", "5", "d"}} ;

		List <String[][]> contentList = new ArrayList<>();
		contentList.add(content);
		contentList.add(content2);
		contentList.add(content3);

		int topMargin =80;
		for (String[][] item: contentList ) {
			System.out.println(item.length);
			int height = item.length *22;

			CreateTableUtitlity.drawTable(page, contentStream, topMargin, 10, item);
			topMargin+=height +40;

		}
		//drawTable(page, contentStream, 80, 10, content);


		contentStream.close();
		doc.save("test.pdf" );
	}

	public void createTitle (int pageNumber,Truck selectedTruck) throws IOException {

		page = new PDPage(PDPage.PAGE_SIZE_A4);
		doc.addPage( page );
		// Start a new content stream which will "hold" the to be created content

		contentStream = new PDPageContentStream(doc, page);

		contentStream.beginText();	 
		contentStream.setFont( font, 12 );
		contentStream.moveTextPositionByAmount( 200, PDPage.PAGE_SIZE_A4.getHeight()-25 );
		contentStream.drawString( "Final Sales On Truck" );
		contentStream.endText();
		contentStream.drawLine(200, PDPage.PAGE_SIZE_A4.getHeight()-30, 320,
				PDPage.PAGE_SIZE_A4.getHeight()-30);
		contentStream.beginText();
		contentStream.setFont( font, 12 );
		contentStream.moveTextPositionByAmount( 20, PDPage.PAGE_SIZE_A4.getHeight()-55 );		
		try {
			String truckDetail = "Truck Number: " + selectedTruck.getName();
			truckDetail+= "      Receive Date: " +  StandardItems.getFormatter().format
					(selectedTruck.getReceiveDate());
			truckDetail+= "  PageNumber = "+  pageNumber;
			contentStream.drawString( truckDetail );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contentStream.endText();


		contentStream.beginText();
		contentStream.setFont( font, 12 );
		contentStream.moveTextPositionByAmount( 20, PDPage.PAGE_SIZE_A4.getHeight()-75 );
		Franchise frn = selectedTruck.getFranchise();
		String franchiseName ="N/A";
		if (frn != null  )
		{
			franchiseName = frn.getName();
		}
		String franchiseAddress ="N/A";
		if (frn!=null  )
		{
			franchiseAddress = frn.getAddress().getAddressLine1();
		}

		contentStream.drawString( "Franchise Name: " + franchiseName +
				"   \n Franchise Address: " + 
				franchiseAddress);
		contentStream.endText();


		List <TruckEntry> truckEntrList =  TruckEntry.getTruckEntryList (selectedTruck);

		if (truckEntrList!=null) {
			int contentTopMargin = 95;
			contentStream.beginText();
			contentStream.setFont( font, 12 );
			contentStream.moveTextPositionByAmount( 20, PDPage.PAGE_SIZE_A4.getHeight()-contentTopMargin);
			contentStream.drawString("Fruit Recived:");
			contentStream.endText();
			for ( int i=0; i<truckEntrList.size(); i++) {
				int leftMargin = (i%3)*150 +100;
				contentStream.beginText();
				contentStream.setFont( font, 10 );
				int  actualTopMargin  = contentTopMargin + (i/3)*20;
				contentStream.moveTextPositionByAmount( leftMargin, PDPage.PAGE_SIZE_A4.getHeight()-actualTopMargin );
				String truckFruitItem = " " +   truckEntrList.get(i).getFruit();
				String fruitQuality = " " + truckEntrList.get(i).getFruitQuality();
				long quanity = Utility.getValue(truckEntrList.get(i).getQuantity()); 
				String purchaseUnit = truckEntrList.get(i).getPurchaseUnit();
				String truckEntryDetail= truckFruitItem + fruitQuality + " " + quanity + " " + purchaseUnit +". ";
				contentStream.drawString( truckEntryDetail );
				contentStream.endText();

			}

		}



	}


	public void createFruitHeader (String frt, String ftQualityText, int topMargin) throws IOException {		
		contentStream.beginText();	 
		contentStream.setFont( font, 12 );
		contentStream.moveTextPositionByAmount( 40, PDPage.PAGE_SIZE_A4.getHeight()-topMargin );
		contentStream.drawString( "Product Name: "  +  frt +  ":-- " + ftQualityText );
		contentStream.endText();

		//	contentStream.drawLine(200, PDPage.PAGE_SIZE_A4.getHeight()-30, 320,
		//			PDPage.PAGE_SIZE_A4.getHeight()-30);
		contents = new String[][] {{"Sales Date","Customer Name", "Purchase Unit",
			"Sales Quantity X Sales Cost Per Unit", "Total Amount  Rs."}};
			//	topMargin = 140;
			columnArrays = new int[] {10, 15, 13, 18, 10};
			CreateTableUtitlity.drawTable(page, contentStream, topMargin+10, 10, columnArrays, contents);
	}

	/*
	 *  Create Report on Truck Sales Fruit (along with Quality) wise.
	 */
	public    void createTruckWiseSaleReport (Truck selectedTruck, String docPath) throws IOException, COSVisitorException {


		doc = new PDDocument();		
		font = PDType1Font.HELVETICA_BOLD;
		int pageNumber=1;
		createTitle (pageNumber++,selectedTruck);

		List frtAndQuality = TruckSales.getDisctinctFruitAndQuality (selectedTruck);
		topMargin =190;
		float toTotalSaleAmount = 0;
		int bottomMargin = 30;
		System.out.println (frtAndQuality.size());
		for (Object item:frtAndQuality) {

			// Margin is taken 40 more than actual bottom margin
			// to ensure at list two rows are printed if Fruit title is printed
			if (topMargin > PDPage.PAGE_SIZE_A4.getHeight()-bottomMargin -60) {
				contentStream.close();
				createTitle (pageNumber++,selectedTruck);
				topMargin =190;
			}

			Object [] itemList = (Object []) item;
			Fruit frt = (Fruit)  itemList [0];
			FruitQuality ftQuality = (FruitQuality) itemList [1];
			List <TruckSales> trkSaleList = TruckSales.getTruckSaleList (selectedTruck, frt, ftQuality);
			System.out.println ("frt " + frt + "ftQuality="  + ftQuality + "."); 
			String ftQualityText ="";
			if (! (ftQuality==null)) {
				ftQualityText = ftQuality.getName(); 
			}

			createFruitHeader(frt.getName(), ftQualityText, topMargin -30);

			int sizeOfItems = trkSaleList.size();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			contents = new String [sizeOfItems] [5];
			List <String []> contentList = new ArrayList <String []> ();
			// Current manrgin keeps track of margin   after
			// creating each line of row. In case margin space is not left
			// Then a new page is created. 
			int curentMargin = topMargin;  
			int totalRowHeight = 0;
			String purchaseUnit="";
			long totalPurchaseQty =0;
			float totalFruitSaleAmount=0;
			HashMap <String, Float> sellingUnitQtymap = new HashMap <String, Float> ();
			for (TruckSales trckSale: trkSaleList) {

				// This checks if any margin is left.
				// If not then continue to next page
				if (curentMargin > PDPage.PAGE_SIZE_A4.getHeight()-bottomMargin -60) {
					CreateTableUtitlity.drawTable(page, contentStream, topMargin, 10, columnArrays, contentList);
					contentStream.close();
					createTitle (pageNumber++,selectedTruck);
					curentMargin = topMargin =200;
					totalRowHeight=0;
					createFruitHeader(frt.getName(), ftQualityText, topMargin -30);
					contentList = new ArrayList <String []> ();					
				}

				String [] content = new String [5];  // number of columns;				 
				content [0] = formatter.format (trckSale.getSellingDate()) ;
				content [1] = trckSale.getCustomer().toString();
				purchaseUnit = trckSale.getPurchaseUnit();
				long qtyinPurchaseUnit = Utility.getValue(trckSale.getQuantityInPurchaseUnit());
				totalPurchaseQty+= qtyinPurchaseUnit;

				content [2] = qtyinPurchaseUnit + " " + purchaseUnit;
				Float quantity = trckSale.getQuantity();
				String quantityString = (quantity==null)?"--":quantity.toString();
				Float rate = trckSale.getRate();
				String rateString = (rate==null)?"--":rate.toString();
				String salesUnit = trckSale.getSalesUnit();
				
				// Store selling Unit in Map
				
				float currentSellQty =Utility.getValue ( sellingUnitQtymap.get(salesUnit));
				sellingUnitQtymap.put(salesUnit, currentSellQty+quantity);
				String isDebit ="";
				if (trckSale.getIsDebitPurchase()) {
					isDebit = " ---(Cash)";
				}
				else {
					isDebit = " ---(Credit)";
				}
				content [3] = quantity + salesUnit + " X " + rateString + isDebit  ;
				Float amount = trckSale.getAmount();
				if (amount !=null) {
					toTotalSaleAmount+=  amount.floatValue();
					totalFruitSaleAmount+= amount.floatValue();
				}
				String amountString = (amount==null)?"--":amount.toString();
				content [4]   = amountString;
				contentList.add (content);
				curentMargin+=20; // Current Row Height
				totalRowHeight+=20;
			}			 
			String [] content = new String [5]; 
			content [0] = "";
			content [1] = "" ;
			content [2] = "Total="+ totalPurchaseQty + " " + purchaseUnit;
			String sellUnitTotal="Total: ";
			for (String key : sellingUnitQtymap.keySet()) {
				   
				sellUnitTotal =  sellingUnitQtymap.get(key) + " " + key + ","; // Qty and value;
			}

 		
			content [3] =sellUnitTotal;
			content [4] = "Total =Rs " +  totalFruitSaleAmount;
			contentList.add (content);
			CreateTableUtitlity.drawTable(page, contentStream, topMargin, 10, columnArrays, contentList);
			topMargin+=totalRowHeight +65; // 45 height kept for writing fruit name and Total

		}
		List <String []> contentList = new ArrayList <String []> ();
		String [] content = new String [5]; 
		content [0] = "";
		content [1] = "" ;
		content [2] = "" ;
		content [3] = "";
		content [4] = "Total =Rs " + toTotalSaleAmount  ;
		contentList.add (content);
		CreateTableUtitlity.drawTable(page, contentStream, topMargin-35, 10, columnArrays, contentList);
		contentStream.close();
		doc.save(docPath );

	}

	public static void main(String[] args) throws COSVisitorException, IOException {
		CreateTruckSalePDFReport mt = new CreateTruckSalePDFReport();
		mt.createPage();
		mt.cratePDFdoc ();

	}

}

