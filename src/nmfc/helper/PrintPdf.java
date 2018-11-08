package nmfc.helper;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;

/**
 * Converts the PDF content into printable format
 */
public class PrintPdf {

	private PrinterJob pjob = null;

	public static void main(String[] args) throws IOException, PrinterException {
		if (args.length != 1) {
			System.err.println("The first parameter must have the location of the PDF file to be printed");
		}
		System.out.println("Printing: " + args[0]);
		// Create a PDFFile from a File reference
		FileInputStream fis = new FileInputStream(args[0]);
		PrintPdf printPDFFile = new PrintPdf(fis, "Test Print PDF");
		printPDFFile.print();
	}
	
	/**
	 * Constructs the print job based on the input stream
	 * 
	 * @param inputStream
	 * @param jobName
	 * @throws IOException
	 * @throws PrinterException
	 */
	public PrintPdf(String fileName, String jobName, boolean isA5) throws IOException, PrinterException {
		FileInputStream inputStream = new FileInputStream(fileName);
		byte[] pdfContent = new byte[inputStream.available()];
		inputStream.read(pdfContent, 0, inputStream.available());
		initialize(pdfContent, jobName, isA5);
	}
	

	/**
	 * Constructs the print job based on the input stream
	 * 
	 * @param inputStream
	 * @param jobName
	 * @throws IOException
	 * @throws PrinterException
	 */
	public PrintPdf(InputStream inputStream, String jobName) throws IOException, PrinterException {
		byte[] pdfContent = new byte[inputStream.available()];
		inputStream.read(pdfContent, 0, inputStream.available());
		initialize(pdfContent, jobName, false);
	}

	/**
	 * Constructs the print job based on the byte array content
	 * 
	 * @param content
	 * @param jobName
	 * @throws IOException
	 * @throws PrinterException
	 */
	public PrintPdf(byte[] content, String jobName) throws IOException, PrinterException {
		initialize(content, jobName, false);
	}

	/**
	 * Initializes the job
	 * 
	 * @param pdfContent
	 * @param jobName
	 * @param isA5 
	 * @throws IOException
	 * @throws PrinterException
	 */
	private void initialize(byte[] pdfContent, String jobName, boolean isA5) throws IOException, PrinterException {
		ByteBuffer bb = ByteBuffer.wrap(pdfContent);
		// Create PDF Print Page
		PDFFile pdfFile = new PDFFile(bb);
		PDFPrintPage pages = new PDFPrintPage(pdfFile);

		// Create Print Job
		pjob = PrinterJob.getPrinterJob();
		PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
		pjob.setJobName(jobName);
		Book book = new Book();
		book.append(pages, pf, pdfFile.getNumPages());
		pjob.setPageable(book);

		
		Paper paper = new Paper();
		
		// For A5 size paper 
	    double width = 5.8d * 72d;
        double height = 8.3d * 72d;
        double margin =0;  // Try without margin first
   //     double margin = 1d * 72d;
        paper.setSize(width, height);
        
        if (isA5) {
        	  paper.setImageableArea(
                      margin,
                      0,
                      width - (margin * 2),
                      height);
        }
        else {
        	
       // 	width =8.3d*72d;
       // 	height=11.6*72d;   
       // 	paper.setImageableArea(0, 0, width, height);
        	paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
        }
      
         
     //   pf.setOrientation(PageFormat.PORTRAIT);
        pf.setPaper(paper);
		
     // to remove margins 
	//	paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
	//	pf.setPaper(paper);
	}

	public void print() throws PrinterException {
		// Send print job to default printer
		pjob.print();
	}
}

/**
 * Class that actually converts the PDF file into Printable format
 */
class PDFPrintPage implements Printable {

	private PDFFile file;

	PDFPrintPage(PDFFile file) {
		this.file = file;
	}

	public int print(Graphics g, PageFormat format, int index) throws PrinterException {
		int pagenum = index + 1;
		if ((pagenum >= 1) && (pagenum <= file.getNumPages())) {
			Graphics2D g2 = (Graphics2D) g;
			PDFPage page = file.getPage(pagenum);

			// fit the PDFPage into the printing area
			Rectangle imageArea = new Rectangle((int) format.getImageableX(), (int) format.getImageableY(),
					(int) format.getImageableWidth(), (int) format.getImageableHeight());
			g2.translate(0, 0);
			PDFRenderer pgs = new PDFRenderer(page, g2, imageArea, null, null);
			try {
				page.waitForFinish();
				pgs.run();
			} catch (InterruptedException ie) {
				// nothing to do
			}
			return PAGE_EXISTS;
		} else {
			System.out.println("Could not Print the Page");
			return NO_SUCH_PAGE;
		}
	}
}
