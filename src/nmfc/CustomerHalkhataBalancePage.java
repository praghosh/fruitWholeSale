package nmfc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXDatePicker;

import entities.ContainerItems;
import entities.CreditEntry;
import entities.Customer;
import entities.CustomerHalkhataEntry;
import entities.CustomerTransaction;
import entities.HalKhata;
import entities.Route;
import entities.TruckSales;
import nmfc.BillPrint.GenerateBillPrintReportListener;
import nmfc.CreditEntryPage.CustomTableCellRenderer;
import nmfc.TruckSalePage.RateOrAmountChangeListener;
import nmfc.helper.CloseButton;
import nmfc.helper.PrintPDFA6;
import nmfc.helper.PrintPdf;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StandardItems;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import pdftables.AllCustomerDueReport;
import pdftables.BillPrintPDF;
import pdftables.PrintHalkhataBalance;
import pdftables.RouteWiseReportPDF;
import pdftables.SmallBillPrint;

public class CustomerHalkhataBalancePage extends JDialog{

	private JPanel rightPanel;

	private String[] customerLedgerColumns;
	private  JTable customerLedgerTable = new JTable ();
	private JScrollPane scrollForCustomerLedgerTable;
	private DefaultTableModel customerLedgerModel;
	private JXDatePicker fromEntryDatePicker = new JXDatePicker();
	private JXDatePicker toEntryDatePicker = new JXDatePicker();
	private DefaultTableModel creditEntryModel;
	private Class[] columnClasses;
	private JComboBox customerItems;
	Customer customer; 
	HalKhata halkhata;
	JFileChooser fc = new JFileChooser();

	private JLabel customerLabel = new JLabel ();

	private JLabel totalDiscountedSellLabel = new JLabel ();

	private JLabel totalNonDiscountedSellLabel = new JLabel ();

	private JLabel totalSellLabel = new JLabel ();

	private JLabel totalDepositMadeLabel = new JLabel ();

	private JLabel totalSmallCrateDueLabel =  new JLabel ();

	private JLabel totalBigCrateDueLabel  = new JLabel ();

	private JLabel totalCashDueLabel = new JLabel ();

	private JLabel normalDiscountElligibleLabel = new JLabel ();

	private long discountedSell ;

	private long nonDiscountedSell;

	private long totalDepositMade;

	private float discountPercentGiven;

	private long eligibleDiscount;

	private long totalCashDue;

	private long cashDepositInHalkhata;

	private long cashDueAfterDiscount ;

	private long smallCrateDue;

	private long bigCrateDue;

	private boolean isEditable=true;

	private long finalDueAfterHalkhata;

	private long smallCrateDepositInHalkhata;

	private long bigCrateDepositInHalkhata;

	private long specialDiscount;
	
	private long billNumber;

	private JFormattedTextField specialDiscountEntry;

	private JLabel cashDueAfterDiscountLabel = new JLabel();

	private JFormattedTextField depositMadeInHalkhataEntry;

	private JFormattedTextField smallCrateDepositEntry;

	private JFormattedTextField bigCrateDepositEntry;

	private JFormattedTextField billNumberEntry;

	private JFormattedTextField actualDiscountEntry;

	private long actualDiscount;


	public CustomerHalkhataBalancePage (JFrame parentPage, String string, boolean b,  HalKhata halkhata) {
		super (parentPage,  string,  b);
		this.halkhata = halkhata;
		getContentPane().setLayout( null );
		setBounds( 160, 140, 620, 650 );
		getContentPane().setBackground(new Color(230, 245, 240) );

		rightPanel = new JPanel();
		rightPanel.setLayout( null );
		rightPanel.setBounds( 10, 10, 550, 650 );
		rightPanel.setBackground(new Color(200, 250, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);

	}

	private void createRightPanel() {


		createCustomersearch();
		createHalKhataDisplay ();
		createHalkhataButtons ();
		createCancelButton ();

	}

	private void createCustomersearch() {
		JLabel selectCustomer = new JLabel ("Select a Customer");
		selectCustomer.setBounds( 310,10,155,30);		 
		StyleItems.styleComponent2(  selectCustomer);
		rightPanel.add(selectCustomer);

		List <Customer> customerList =  Customer.getCustomerLists(); // i 
		customerItems = new JComboBox (customerList.toArray());
		StyleItems.styleComponent (customerItems);  
		customerItems.setBounds( 310,55,190,30);
		customerItems.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				System.out.println( "********" +  customerItems.getSelectedItem()  );
				refreshHalkhataData();
				refreshHalkhataDisplay();
				refreshEnteredValue ();
				disableEnableEntry ();
			}


		});	 	
		rightPanel.add(customerItems);

	}


	private void refreshHalkhataData() {
		if (customerItems==null || customerItems.getSelectedItem()==null) {
			return;

		}
		customer = (Customer)  customerItems.getSelectedItem();		
		CustomerHalkhataEntry  custHalkhata = 
				CustomerHalkhataEntry.getCustomerHalkhataEntry(customer, halkhata);		
		Date startDate =  halkhata.getStartDate();
		Date endDate =  halkhata.getEndDate();

		if (custHalkhata==null) {
			isEditable = true;	
			discountedSell = (long) TruckSales.getTotalDiscountedSales 
					(customer, startDate, endDate );
			nonDiscountedSell = (long) TruckSales.getTotalNonDiscountedSales 
					(customer, startDate, endDate );
			totalDepositMade = (long) CreditEntry.getTotalDepositForCustomerBeforeDate 
					(customer, startDate, endDate);
			discountPercentGiven = Utility.getValue (customer.getDiscountEligible());
			if (discountPercentGiven==0) discountPercentGiven = 1; // If not defined then take as 1 %

			eligibleDiscount = (long)  (discountedSell * discountPercentGiven *0.01);
			actualDiscount = eligibleDiscount;
			totalCashDue =0;
			smallCrateDue=0;
			bigCrateDue =0;
			specialDiscount = 0;	
			cashDepositInHalkhata = 0;
			bigCrateDepositInHalkhata=0;
			smallCrateDepositInHalkhata =0;
			java.util.List lastBlance = CustomerTransaction.getBalanceBeforeDate(customer, endDate);
			if (lastBlance!=null && lastBlance.size()!=0) {		
				totalCashDue =(long) Utility.getValue( (Float) lastBlance.get(2)); 							
				bigCrateDue = (long)lastBlance.get(1);
				smallCrateDue = (long) lastBlance.get(0);

			}
			cashDueAfterDiscount = totalCashDue - eligibleDiscount ;
			finalDueAfterHalkhata = totalCashDue - eligibleDiscount 
					- specialDiscount - cashDepositInHalkhata;
			billNumber =0;
		}

		else {
			isEditable =false;
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
			billNumber = Utility.getValue( custHalkhata.getBillNumber());
			actualDiscount = Utility.getValue( custHalkhata.getActualTotalDiscount());
			cashDueAfterDiscount = totalCashDue - eligibleDiscount ;
			finalDueAfterHalkhata = totalCashDue - eligibleDiscount 
					- specialDiscount - cashDepositInHalkhata;	
		}



	}

	private void refreshHalkhataDisplay () {
		customerLabel.setText("Customer:  " + customer);		
		totalDiscountedSellLabel.setText ("Total Discounted Sell:  " + discountedSell );	
		totalNonDiscountedSellLabel .setText("Total Non Discounted Sell:  " + nonDiscountedSell );	
		totalSellLabel.setText ("Total  Sell:  " + (nonDiscountedSell + discountedSell) );
		totalDepositMadeLabel .setText ("Total Deposit Made:  " + totalDepositMade );
		totalSmallCrateDueLabel.setText("Total Small Crate Due:  " + smallCrateDue );
		totalBigCrateDueLabel.setText("Total Big Crate Due:  " + bigCrateDue ); 
		totalCashDueLabel.setText("Total Cash Due:  " + totalCashDue );
		normalDiscountElligibleLabel.setText("Normal Discount Elligible:  " + eligibleDiscount);
		cashDueAfterDiscountLabel .setText ("Cash Due After All Discount--:   " +  cashDueAfterDiscount );	 

	}

	public void disableEnableEntry () {
		actualDiscountEntry.setEditable(isEditable);
		bigCrateDepositEntry.setEditable(isEditable);
		smallCrateDepositEntry.setEditable(isEditable);
		depositMadeInHalkhataEntry.setEditable(isEditable);
		billNumberEntry.setEditable(isEditable);
	}

	public void refreshEnteredValue () {

		depositMadeInHalkhataEntry.setValue(cashDepositInHalkhata);
		actualDiscountEntry.setValue(actualDiscount);
		bigCrateDepositEntry.setValue(bigCrateDepositInHalkhata);
		smallCrateDepositEntry.setValue(smallCrateDepositInHalkhata);

	}
	private void createHalKhataDisplay() {

		refreshHalkhataData();

		customerLabel.setBounds( 10,10,350,20);
		StyleItems.styleComponent(customerLabel);
		rightPanel.add(customerLabel);


		JLabel halKhataLabel = new JLabel ("Halkhata:  " + halkhata);
		halKhataLabel.setBounds( 10,35,320,20);
		StyleItems.styleComponent(halKhataLabel);
		rightPanel.add(halKhataLabel);


		JLabel startDateLAbel = new JLabel ("HalKhata Start Date:  " + 
				StandardItems.getFormatter().format(halkhata.getStartDate()));
		startDateLAbel.setBounds( 10,60,310,20);
		StyleItems.styleComponent(startDateLAbel);
		rightPanel.add(startDateLAbel);

		JLabel endDateLabel = new JLabel ("HalKhata End Date:  " + 
				StandardItems.getFormatter().format(halkhata.getEndDate()));
		endDateLabel.setBounds( 10,85,310,20);
		StyleItems.styleComponent(endDateLabel);
		rightPanel.add(endDateLabel);

		totalDiscountedSellLabel.setBounds( 10,120,260,20);
		StyleItems.styleComponent(totalDiscountedSellLabel);
		rightPanel.add(totalDiscountedSellLabel);


		totalNonDiscountedSellLabel.setBounds( 10,145,350,20);
		StyleItems.styleComponent(totalNonDiscountedSellLabel);
		rightPanel.add(totalNonDiscountedSellLabel);

		totalSellLabel.setBounds( 310,130,350,20);
		StyleItems.styleComponent(totalSellLabel);
		rightPanel.add(totalSellLabel);

		totalDepositMadeLabel.setBounds( 10,160,320,20);
		StyleItems.styleComponent(totalDepositMadeLabel);
		rightPanel.add(totalDepositMadeLabel);


		totalSmallCrateDueLabel.setBounds( 10,200,270,20);
		StyleItems.styleComponent(totalSmallCrateDueLabel);
		rightPanel.add(totalSmallCrateDueLabel);


		totalBigCrateDueLabel.setBounds( 310,200,320,20);
		StyleItems.styleComponent(totalBigCrateDueLabel);
		rightPanel.add(totalBigCrateDueLabel);


		totalCashDueLabel.setBounds( 10,225,260,20);
		StyleItems.styleComponent(totalCashDueLabel);
		rightPanel.add(totalCashDueLabel);


		normalDiscountElligibleLabel.setBounds( 10,250,320,20);
		StyleItems.styleComponent(normalDiscountElligibleLabel);
		rightPanel.add(normalDiscountElligibleLabel);


		cashDueAfterDiscountLabel.setBounds( 10,310,310,20);
		StyleItems.styleComponent(cashDueAfterDiscountLabel);
		rightPanel.add(cashDueAfterDiscountLabel);

		JLabel actualDiscountLabel = new JLabel ("Actual Discount:  "   );
		actualDiscountLabel.setBounds( 10,340,260,20);
		StyleItems.styleComponent(actualDiscountLabel);
		rightPanel.add(actualDiscountLabel);

		actualDiscountEntry = new JFormattedTextField ();
		actualDiscountEntry.setBounds( 310,340,100,25);		    		
		StyleItems.styleComponent2(actualDiscountEntry);
		rightPanel.add(actualDiscountEntry);



		JLabel depositMadeInHalkhataLabel = new JLabel ("Cash Deposited in This Halkhata:  "   );
		depositMadeInHalkhataLabel.setBounds( 10,380,280,20);
		StyleItems.styleComponent(depositMadeInHalkhataLabel);
		rightPanel.add(depositMadeInHalkhataLabel);

		depositMadeInHalkhataEntry = new JFormattedTextField ();
		depositMadeInHalkhataEntry.setBounds( 310,380,100,25);
		StyleItems.styleComponent2(depositMadeInHalkhataEntry);	   				
		rightPanel.add(depositMadeInHalkhataEntry);

		JLabel smallCratedepositLabel = new JLabel ("Small Crate returned in This Halkhata:  "   );
		smallCratedepositLabel.setBounds( 10,420,280,20);
		StyleItems.styleComponent(smallCratedepositLabel);
		rightPanel.add(smallCratedepositLabel);

		smallCrateDepositEntry = new JFormattedTextField ();
		smallCrateDepositEntry.setBounds( 310,420,100,25);
		StyleItems.styleComponent2(smallCrateDepositEntry);
		rightPanel.add(smallCrateDepositEntry);


		JLabel bigCratedepositLabel = new JLabel ("Big Crate returned in This Halkhata:  "   );
		bigCratedepositLabel.setBounds( 10,460,280,20);
		StyleItems.styleComponent(bigCratedepositLabel);
		rightPanel.add(bigCratedepositLabel);

		bigCrateDepositEntry = new JFormattedTextField ();
		bigCrateDepositEntry.setBounds( 310,460,100,25);
		StyleItems.styleComponent2(bigCrateDepositEntry);
		rightPanel.add(bigCrateDepositEntry);
		
		JLabel billNumberLabel = new JLabel ("Bill Number:  "   );
		billNumberLabel.setBounds( 10,500,280,20);
		StyleItems.styleComponent(billNumberLabel);
		rightPanel.add(billNumberLabel);

		billNumberEntry = new JFormattedTextField ();
		billNumberEntry.setBounds( 310,500,100,25);
		StyleItems.styleComponent2(billNumberEntry);
		rightPanel.add(billNumberEntry);

		refreshHalkhataDisplay ();
		refreshEnteredValue (); // Refresh Data entered in the box
		disableEnableEntry ();

	}

	private void createCancelButton () {

		JButton cancelButton = new CloseButton (this);
		cancelButton.setBounds( 130, 590, 100, 25 );
		rightPanel.add(cancelButton);
	}


	private void	createHalkhataButtons () {

		JButton updateData = new JButton ("Update");
		updateData.setBounds( 10,590,100,25);
		updateData.setForeground(StyleItems.buttonTextColor);
		updateData.setFont(StyleItems.buttonFont);
		updateData.setBorder(new LineBorder(new Color(20, 180, 70), 2));		 
		updateData.addActionListener(new upadteCustomerHalKhataData ());
		rightPanel.add(updateData);

		JButton printHalkhata = new JButton ("Print HalKhata");
		printHalkhata.setBounds( 260,590,120,25);
		printHalkhata.setForeground(StyleItems.buttonTextColor);
		printHalkhata.setFont(StyleItems.buttonFont);
		printHalkhata.setBorder(new LineBorder(new Color(20, 180, 70), 2));		 
		printHalkhata.addActionListener(new PrintHalkhataBlanceListener ());
		rightPanel.add(printHalkhata);

		JButton saveHalkhata = new JButton ("Save HalKhata");
		saveHalkhata.setBounds( 410,590,120,25);
		saveHalkhata.setForeground(StyleItems.buttonTextColor);
		saveHalkhata.setFont(StyleItems.buttonFont);
		saveHalkhata.setBorder(new LineBorder(new Color(20, 180, 70), 2));		 
		saveHalkhata.addActionListener(new SaveHalkhataBlanceListener ());
		rightPanel.add(saveHalkhata);
	}



	public class PrintHalkhataBlanceListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {


			Date startDate = fromEntryDatePicker.getDate();
			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(CustomerHalkhataBalancePage.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}

					PrintHalkhataBalance.createHalKhataPrintPDF  ( finalBackupPath,customer, halkhata);

					PrintPDFA6 printJob = new PrintPDFA6(finalBackupPath, "Small Bill Job", true);
					printJob.print();

					ToastMessage toastMessage = new ToastMessage("Report created Successfully",3000);
					toastMessage.setVisible(true);
					//	FileUtils.writeStringToFile(file, data);//avaible when import org.apache.commons.io.FileUtils;
					System.out.println( "Report Successfull" + finalBackupPath  );
					//	xputils.showMessage("Backup Successfull");

				}

			} catch (Exception ex) {

				ToastMessage toastMessage = new ToastMessage("Could not create the report",3000);
				toastMessage.setVisible(true);
				ex.printStackTrace();
				//xputils.showErrorMessage(e.toString());

			}



		}
	}


	public class SaveHalkhataBlanceListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {


			Date startDate = fromEntryDatePicker.getDate();
			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(CustomerHalkhataBalancePage.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}

					PrintHalkhataBalance.createHalKhataPrintPDF  ( finalBackupPath,customer, halkhata);

					ToastMessage toastMessage = new ToastMessage("Report created Successfully",3000);
					toastMessage.setVisible(true);
					//	FileUtils.writeStringToFile(file, data);//avaible when import org.apache.commons.io.FileUtils;
					System.out.println( "Report Successfull" + finalBackupPath  );


				}

			} catch (Exception ex) {

				ToastMessage toastMessage = new ToastMessage("Could not create the report",3000);
				toastMessage.setVisible(true);
				ex.printStackTrace();
				//xputils.showErrorMessage(e.toString());

			}



		}
	}

	public void updateCustomerHalKhata (CustomerHalkhataEntry customerHal) {

		customerHal.setCustomer(customer);
		customerHal.setHalkhata(halkhata);
		customerHal.setDiscountedSell(discountedSell); 
		customerHal.setNonDiscountedSell(nonDiscountedSell); 	    
		customerHal.setTotalDepositMade(totalDepositMade);
		customerHal.setDiscountPercentGiven(discountPercentGiven);
		customerHal.setEligibleDiscount(eligibleDiscount);
		customerHal.setCashDue(totalCashDue); 		
		customerHal.setSmallCrateDue(smallCrateDue);
		customerHal.setBigCrateDue(bigCrateDue);

		//specialDiscount=   Utility.getValue( (Long) specialDiscountEntry.getValue());
		cashDepositInHalkhata= Utility.getValue( (Long) depositMadeInHalkhataEntry.getValue());	 	 
		smallCrateDepositInHalkhata = Utility.getValue( (Long) smallCrateDepositEntry.getValue()); 
		bigCrateDepositInHalkhata = Utility.getValue( (Long) bigCrateDepositEntry.getValue()); 
		billNumber  =  Utility.getValue( (Long) billNumberEntry.getValue()); 
		actualDiscount = Utility.getValue( (Long) actualDiscountEntry.getValue()); 
		customerHal.setSpecialDiscount(actualDiscount- eligibleDiscount);
		customerHal.setCashDepositInHalkhata(cashDepositInHalkhata);
		customerHal.setSmallCrateDepositInHalkhata(smallCrateDepositInHalkhata);
		customerHal.setBigCrateDepositInHalkhata(bigCrateDepositInHalkhata);
		customerHal.setBillNumber(billNumber);
		customerHal.setTotalDiscount(actualDiscount);
	}

	public class upadteCustomerHalKhataData   implements ActionListener {

		public void actionPerformed(ActionEvent evt) {

			if  (CustomerHalkhataEntry.getCustomerHalkhataEntry (customer, halkhata)!=null)	 {

				ToastMessage toastMessage = new ToastMessage("Halkhata Entry Already Added, Can not be done again ",3000);
				toastMessage.setVisible(true);				 
				return;

			}
			actualDiscount=   Utility.getValue( (Long) actualDiscountEntry.getValue());
			cashDepositInHalkhata= Utility.getValue( (Long) depositMadeInHalkhataEntry.getValue());	 	 			  
			long finalDue = totalCashDue - cashDepositInHalkhata - eligibleDiscount;
			int dialogButton = JOptionPane.YES_NO_OPTION;
			if (finalDue != 0) {	
				 
				ToastMessage toastMessage = new ToastMessage("Final Due is Not  Zero. " +
						finalDue,  2000, Color.RED); 
				
				toastMessage.setVisible(true);				 				
			}

			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Update Halkhata And Ledger?","Warning",dialogButton);
			if(dialogResult == JOptionPane.NO_OPTION){
				return;
			}

  
			CustomerHalkhataEntry	halkhataEntry = new CustomerHalkhataEntry ();
			updateCustomerHalKhata (halkhataEntry);

			if (CustomerHalkhataEntry.commitHalkhataEntry(halkhataEntry) ) {				
				ToastMessage toastMessage = new ToastMessage("Halkhata Entry Added Successfully",3000);
				toastMessage.setVisible(true);
				isEditable=false;
				disableEnableEntry ();

			}
			else {
				ToastMessage toastMessage = new ToastMessage("Halkhata Entry Not Added",3000);
				toastMessage.setVisible(true);

			} 

		}
	}




}