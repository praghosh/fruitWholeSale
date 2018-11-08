package nmfc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXDatePicker;

import entities.CreditEntry;
import entities.Customer;
import entities.CustomerTransaction;
import entities.Route;
import entities.Truck;
import entities.TruckSales;
import nmfc.BillPrint.GenerateAllSmallBillReportListener;
import nmfc.BillPrint.PrintSmallBillListener;
import nmfc.CreditEntryPage.CustomTableCellRenderer;
import nmfc.TruckWiseSaleReport.GenerateTruckWiseReportListener;
import nmfc.helper.CloseButton;
import nmfc.helper.PrintPdf;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import pdftables.AllCustomersBillPrint;
import pdftables.BillPrintPDF;
import pdftables.CreateTruckSalePDFReport;
import pdftables.SmallBillPrint;


public class BillPrint extends JFrame {



	private JPanel leftPanel;
	private JPanel rightPanel;
	private JScrollPane routeListScrollPane;
	private Route selectedRoute;
	private List<Customer> customerList;
	private JScrollPane customerListScrollPane;
	private JList routeRowList;
	private JList customerRowList;
	private String[] customerLedgerColumns;
	private  JTable customerBillTable = new JTable ();
	private JScrollPane scrollForCustomerSaleTable;
	private DefaultTableModel customerLedgerModel;
	private JXDatePicker fromEntryDatePicker = new JXDatePicker();
	private JXDatePicker toEntryDatePicker = new JXDatePicker();
	private Customer selectedCustomer;
	private DefaultTableModel creditEntryModel;
	// Constants. 
	private static final int  INDEX_OF_SL_NO_COLUMN = 0;
	//private static final int  INDEX_OF_PARTICULAR_COLUMN = 1; 
	private static final int  INDEX_OF_ITEM_COLUMN= 1	;
	private static final int  INDEX_OF_AMOUNT_RATE= 3	;
	private static final int  INDEX_OF_AMOUNT_QUANTITY= 2;
	private static final int  INDEX_OF_SELLING_UNIT= 4	;
	private static final int  INDEX_OF_AMOUNT_COLUMN= 5	;

	private static final int  SIZE_OF_ALL_COLUMN = 6;
	private JLabel customerNameLabel;
	private JLabel customerAddressLabel;
	private JLabel lastDepositDateLabel;
	private JLabel lastDepositAmount;
	private JLabel lastDepositDate;
	private JLabel billAmount;
	private JLabel previousBalance;
	private JLabel totalAmount;
	JFileChooser fc = new JFileChooser();


	public BillPrint (Frame homePage, String string, boolean b) {
		super (string);
		getContentPane().setLayout( null );
		setBounds( 10, 10, 920, 690 );
		getContentPane().setBackground(new Color(230, 245, 240) );

		leftPanel = new JPanel();
		leftPanel.setLayout( null );
		leftPanel.setBounds( 10, 10, 145, 620 );
		leftPanel.setBackground(new Color(160, 230, 210) );
		leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createLeftPanel();
		add(leftPanel);


		rightPanel = new JPanel();
		rightPanel.setLayout( null );
		rightPanel.setBounds( 156, 10, 820, 620 );
		rightPanel.setBackground(new Color(200, 250, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);



		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 395, 640, 160, 35 );
		add(closeButton);
	}

	private void createRightPanel() {

		createDateRangeButtons();
		createBillDetailTable();
		createRightBottomButtons () ;


	}

	private void createDateRangeButtons() {

		JLabel titleLabel = new JLabel ("<html>" +
				"নিউ মদিনা ফ্রুট কোম্পানী <br>" +
				"বাগনান বাস স্ট্যান্ড, হাওড়া" 

				);
		titleLabel.setBounds( 180, 10,250,55);
		StyleItems.styleComponentBig(titleLabel);
		rightPanel.add(titleLabel);

		customerNameLabel = new JLabel ("Customer:");
		customerNameLabel.setBounds( 450, 10,210,25);
		StyleItems.styleComponentSmall (customerNameLabel);
		rightPanel.add(customerNameLabel);	

		customerAddressLabel = new JLabel ("Address:");
		customerAddressLabel.setBounds( 450, 40,230,25);
		StyleItems.styleComponentSmall (customerAddressLabel);
		rightPanel.add(customerAddressLabel);


		JLabel OnDateLabel = new JLabel ("Bill Date:");
		OnDateLabel.setBounds( 160, 80,120,25);
		StyleItems.styleComponentSmall (OnDateLabel);
		rightPanel.add(OnDateLabel);

		fromEntryDatePicker.setDate(new Date());
		StyleItems.styleDatePickerSmall(fromEntryDatePicker);
		fromEntryDatePicker.setBounds( 240, 80,108,20);
		fromEntryDatePicker.setFormats("dd/MM/yyyy");

		PropertyChangeListener fromDateListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**fromEntryDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("fromEntryDatePicker.isEditValid()  " +fromEntryDatePicker.isEditValid());
				if ((fromEntryDatePicker.getDate()!=null) && fromEntryDatePicker.isEditValid()) {
					System.out.println(fromEntryDatePicker.getDate());
					populateCustomerBillTable(); 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		fromEntryDatePicker.addPropertyChangeListener(fromDateListener);
		rightPanel.add (fromEntryDatePicker);


		billAmount = new JLabel ("Bill Amount");
		billAmount.setBounds( 480, 380,370,25);
		StyleItems.styleComponent2( billAmount);
		rightPanel.add(billAmount);

		previousBalance = new JLabel ("Previous Balance");
		previousBalance.setBounds( 440, 405,370,25);
		StyleItems.styleComponent2( previousBalance);
		rightPanel.add(previousBalance);

		totalAmount = new JLabel ("Total Due");
		totalAmount.setBounds( 495, 430, 270,25);
		StyleItems.styleComponent2( totalAmount);
		rightPanel.add(totalAmount);



		lastDepositDateLabel = new JLabel ("");
		lastDepositDateLabel.setBounds( 470, 460,270,25);
		StyleItems.styleComponent2 (lastDepositDateLabel);
		rightPanel.add(lastDepositDateLabel);


		lastDepositAmount = new JLabel ("");
		lastDepositAmount.setBounds( 470, 490,270,25);
		StyleItems.styleComponent2 (lastDepositAmount);
		rightPanel.add(lastDepositAmount);
 
	}

	public void createRightBottomButtons () {
		
		
		JButton genarateReport = new JButton ("Generate Report");
		genarateReport.setBounds( 20,530,160,20);
		StyleItems.styleLabel (genarateReport);
		genarateReport.addActionListener(new GenerateBillPrintReportListener ());
		rightPanel.add(genarateReport);
		
		JButton printSmallBillReport = new JButton ("Print Small Bill");
		printSmallBillReport.setBounds( 20,560,160,20);
		StyleItems.styleLabel (printSmallBillReport);
		printSmallBillReport.addActionListener(new PrintSmallBillListener ());
		rightPanel.add(printSmallBillReport);

		JButton genarateAllBillReport = new JButton ("Generate All Bills");
		genarateAllBillReport.setBounds( 190,530,180,20);
		StyleItems.styleLabel (genarateAllBillReport);
		genarateAllBillReport.addActionListener(new GenerateAllBillReportListener ());
		rightPanel.add(genarateAllBillReport);
		
		JButton printAllBillReport = new JButton ("Print All Small Bills");
		printAllBillReport.setBounds( 190,560,180,20);
		StyleItems.styleLabel (printAllBillReport);
		printAllBillReport.addActionListener(new PrintAllSmallBillReportListener ());
		rightPanel.add(printAllBillReport);


		JButton genarateSmallReport = new JButton ("Generate Small Bill");
		genarateSmallReport.setBounds( 380,530,180,20);
		StyleItems.styleLabel (genarateSmallReport);
		genarateSmallReport.addActionListener(new GenerateSmallBillPrintReportListener ());
		rightPanel.add(genarateSmallReport);
		
		JButton genarateAllSmallReport = new JButton ("Generate All Small Bills");
		genarateAllSmallReport.setBounds( 580,530,210,20);
		StyleItems.styleLabel (genarateAllSmallReport);
		genarateAllSmallReport.addActionListener(new GenerateAllSmallBillReportListener ());
		rightPanel.add(genarateAllSmallReport);
	}

	
	
	public class GenerateAllBillReportListener   implements ActionListener {

		public void actionPerformed(ActionEvent evt) {


			Date startDate = fromEntryDatePicker.getDate();
			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(BillPrint.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}

					AllCustomersBillPrint.createAllBillPrintPDF(  finalBackupPath,   startDate);
					ToastMessage toastMessage1 = new ToastMessage("Report created Successfully",3000);
					toastMessage1.setVisible(true);
					//	FileUtils.writeStringToFile(file, data);//avaible when import org.apache.commons.io.FileUtils;
					System.out.println( "Report Successfull" + finalBackupPath  );
					//	xputils.showMessage("Backup Successfull");

				}

			} catch (Exception ex) {

				ToastMessage toastMessage1 = new ToastMessage("Could not create the report",3000);
				toastMessage1.setVisible(true);
				ex.printStackTrace();
				//xputils.showErrorMessage(e.toString());

			}



		}

	}




	public class PrintSmallBillListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

		 

				int idx = customerRowList.getSelectedIndex();

				if (idx == -1){
					System.out.println ("Please select a customer");
					ToastMessage toastMessage = new ToastMessage("Please choose a customer",3000);
					toastMessage.setVisible(true);
					return;

				}
				selectedCustomer = (Customer)customerRowList.getSelectedValue();
				Date startDate = fromEntryDatePicker.getDate();
				try {

					fc.setDialogTitle("Create a file to create report");

					fc.setCurrentDirectory(fc.getCurrentDirectory());

					System.out.println("File=" + fc.getSelectedFile());
					if (fc.showSaveDialog(BillPrint.this) == JFileChooser.APPROVE_OPTION) {

						String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
						String finalBackupPath = pdfReportPath;

						if (!pdfReportPath.endsWith(".pdf") )
						{
							finalBackupPath = pdfReportPath + 	 ".pdf";
						}

						SmallBillPrint.createBillPrintPDF ( finalBackupPath,selectedCustomer, startDate);
						
						PrintPdf printJob = new PrintPdf(finalBackupPath, "Small Bill Job", true);
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
  
	
	public class GenerateBillPrintReportListener   implements ActionListener {


		public void actionPerformed(ActionEvent evt) {

			int idx = customerRowList.getSelectedIndex();

			if (idx == -1){
				System.out.println ("Please select a customer");
				ToastMessage toastMessage = new ToastMessage("Please choose a customer",3000);
				toastMessage.setVisible(true);
				return;

			}
			selectedCustomer = (Customer)customerRowList.getSelectedValue();
			Date startDate = fromEntryDatePicker.getDate();
			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(BillPrint.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}

					BillPrintPDF.createBillPrintPDF ( finalBackupPath,selectedCustomer, startDate);


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

	public class GenerateSmallBillPrintReportListener   implements ActionListener {


		public void actionPerformed(ActionEvent evt) {

			int idx = customerRowList.getSelectedIndex();

			if (idx == -1){
				System.out.println ("Please select a customer");
				ToastMessage toastMessage = new ToastMessage("Please choose a customer",3000);
				toastMessage.setVisible(true);
				return;

			}
			selectedCustomer = (Customer)customerRowList.getSelectedValue();
			Date startDate = fromEntryDatePicker.getDate();
			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(BillPrint.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}

					SmallBillPrint.createBillPrintPDF ( finalBackupPath,selectedCustomer, startDate);


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

	public class PrintAllSmallBillReportListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Create a  Directory  to create and print report");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			Date startDate = fromEntryDatePicker.getDate();
			try {

				System.out.println("Dir=" + chooser.getSelectedFile());
				if (chooser.showSaveDialog(BillPrint.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = chooser.getSelectedFile().getAbsolutePath();
					List <Customer> customers = TruckSales.getDisctinctCustomers (startDate);

					int slNo=0;
					for (Customer cust: customers  ) {
						slNo++;
						String finalBackupPath = pdfReportPath;
						finalBackupPath = pdfReportPath + "\\"  + slNo + 	cust.getName() +  ".pdf";
						SmallBillPrint.createBillPrintPDF ( finalBackupPath,cust, startDate);
						
						// Now print the pdf file created
						PrintPdf printJob = new PrintPdf(finalBackupPath, "Small Bill Job", true);
						printJob.print();
						
					}
					ToastMessage toastMessage1 = new ToastMessage("Report created Successfully",3000);
					toastMessage1.setVisible(true);			 

				}

			} catch (Exception ex) {

				ToastMessage toastMessage1 = new ToastMessage("Could not create the report",3000);
				toastMessage1.setVisible(true);
				ex.printStackTrace();

			}
		}
	}
	
	public class GenerateAllSmallBillReportListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Create a Ditector  to create report");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			Date startDate = fromEntryDatePicker.getDate();
			try {

				System.out.println("Dir=" + chooser.getSelectedFile());
				if (chooser.showSaveDialog(BillPrint.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = chooser.getSelectedFile().getAbsolutePath();
					List <Customer> customers = TruckSales.getDisctinctCustomers (startDate);

					int slNo=0;
					for (Customer cust: customers  ) {
						slNo++;
						String finalBackupPath = pdfReportPath;
						finalBackupPath = pdfReportPath + "\\"  + slNo + 	cust.getName() +  ".pdf";
						SmallBillPrint.createBillPrintPDF ( finalBackupPath,cust, startDate);
					}
					ToastMessage toastMessage1 = new ToastMessage("Report created Successfully",3000);
					toastMessage1.setVisible(true);			 

				}

			} catch (Exception ex) {

				ToastMessage toastMessage1 = new ToastMessage("Could not create the report",3000);
				toastMessage1.setVisible(true);
				ex.printStackTrace();

			}
		}
	}

	/**
	 *  Update the bill related data on the page
	 *  while selecting a customer.
	 */
	public void setBillData () {
		selectedCustomer = (Customer)customerRowList.getSelectedValue();
		if (selectedCustomer==null) {
			return;
		}
		customerNameLabel.setText("Customer: " + selectedCustomer.getName());
		customerAddressLabel.setText("Customer Address: " + selectedCustomer.getAddress());
		//
		//	customerNameLabel
		//	customerAddressLabel
		//	lastDepositAmount
		//	lastDepositDateLabel
	}

	public void createBillDetailTable() 

	{   
		customerLedgerColumns = new String[SIZE_OF_ALL_COLUMN ];             
		customerLedgerColumns [INDEX_OF_SL_NO_COLUMN] ="<html>Serial<br>No";      
		//	customerLedgerColumns [ INDEX_OF_PARTICULAR_COLUMN ]=  "Particular"; 
		customerLedgerColumns [ INDEX_OF_AMOUNT_RATE] = "Rate"	;
		customerLedgerColumns [INDEX_OF_ITEM_COLUMN] ="<html>Item<br>Name";   
		customerLedgerColumns [INDEX_OF_AMOUNT_QUANTITY] ="<html>Quantity<br>";  
		customerLedgerColumns [INDEX_OF_SELLING_UNIT] ="<html>Selling<br>Unit";  
		customerLedgerColumns [INDEX_OF_AMOUNT_COLUMN] = "<html>Amount";             


		SimpleHeaderRenderer headerRenderForTRuckSale = new SimpleHeaderRenderer();
		headerRenderForTRuckSale.setFont(new Font("Consolas", Font.BOLD, 12)); 
		customerBillTable.getTableHeader().setDefaultRenderer(headerRenderForTRuckSale);
		customerBillTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleComponent(customerBillTable);
		scrollForCustomerSaleTable  = new JScrollPane(customerBillTable); 
		populateCustomerBillTable();
		scrollForCustomerSaleTable.setBounds( 4, 120, 730, 250 );
		customerBillTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (customerBillTable.getSelectedRow() > -1) {
					// print first column value from selected row
					//  System.out.println("Value = " + truckEntryTable.getValueAt(truckSaleTable.getSelectedRow(), 0).toString());
					//   fillCustomerEntryItems();
				}
			}
		});
		rightPanel.add(scrollForCustomerSaleTable);		
	}

	private void createLeftPanel() {

		JLabel selectRouteLabel = new JLabel ("Select Route");
		selectRouteLabel.setBounds( 10,10,115,20);
		StyleItems.styleLabel (selectRouteLabel); 
		leftPanel.add (selectRouteLabel);


		routeListScrollPane = new JScrollPane();
		routeListScrollPane.setBounds( 15, 30, 105, 140 );
		List routes = Route.getRouteNames();
		routeRowList = new JList(routes.toArray());
		StyleItems.styleList2(routeRowList);
		routeRowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = routeRowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " +  idx);				 
					populateCustomerData();
					populateCustomerBillTable();

				}
				else
					System.out.println("Please choose a Route.");
			}

		}); 	
		routeRowList.setVisibleRowCount(12);		 
		routeListScrollPane.setViewportView(routeRowList);
		routeRowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

		//	listScrollPane.add(rowList);
		leftPanel.add(routeListScrollPane);


		JLabel selectEntryDateLabel = new JLabel ("Select Customer");
		selectEntryDateLabel.setBounds( 10,180,135,20);
		StyleItems.styleLabel (selectEntryDateLabel); 
		leftPanel.add (selectEntryDateLabel);	


		customerListScrollPane = new JScrollPane();
		customerListScrollPane.setBounds( 15, 210, 110, 300 );
		selectedRoute = (Route) routeRowList.getSelectedValue();
		if (selectedRoute != null) {
			customerList =  Route.getAllCustomerInRoute(selectedRoute);
		}
		else {
			customerList = new ArrayList <Customer> ();
		}
		customerRowList = new JList(customerList.toArray());		
		customerRowList.setVisibleRowCount(12);		
		StyleItems.styleList(customerRowList);
		customerListScrollPane.setViewportView(customerRowList);
		customerRowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = customerRowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " +  idx);
					setBillData();
					populateCustomerBillTable();


				}
				else
					System.out.println("Please choose a Route.");
			}
		});
		customerRowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		leftPanel.add(customerListScrollPane);

	}


	private void populateCustomerBillTable() {
		if (customerRowList.getSelectedIndex()==-1)
			return;
		selectedCustomer = (Customer)customerRowList.getSelectedValue();
		Date startDate = fromEntryDatePicker.getDate();
		//	Date endDate = toEntryDatePicker.getDate();
		List <TruckSales>  truckSaleList = 
				TruckSales.getCustomerWiseSaleList(selectedCustomer, startDate);		
		ArrayList<Object[]> tableValueList = new ArrayList <Object []>();
		int slNo=1;
		float toTal =0;
		for (TruckSales trSales : truckSaleList) {
			Object[] obj = new Object[SIZE_OF_ALL_COLUMN ];
			//	CustomerTransaction transcationEntry = (CustomerTransaction) s;
			//	obj[INDEX_OF_ID] = transcationEntry.getId();
			obj[INDEX_OF_SL_NO_COLUMN ] = slNo;
			slNo++;
			//	StringBuilder sb = new StringBuilder (); // to be done if entries are clubbed

			obj [INDEX_OF_ITEM_COLUMN] =trSales.getFruit();
			obj [ INDEX_OF_AMOUNT_RATE] = trSales.getRate();
			obj [INDEX_OF_AMOUNT_QUANTITY] =trSales.getQuantity();
			obj[INDEX_OF_SELLING_UNIT] = trSales.getSalesUnit();
			float amount=Utility.getValue(trSales.getAmount());
			obj[INDEX_OF_AMOUNT_COLUMN ] = amount;
			toTal+=amount;
			tableValueList.add(obj);
		}

		billAmount.setText("Bill Amount = Rs" +  String.format( "%.2f", toTal ));

		float previousDue = CustomerTransaction.getLastBalance(selectedCustomer, startDate);

		previousBalance.setText ("Previous Balance=" + String.format( "%.2f", previousDue ) );

		float totalDue = previousDue + toTal;

		totalAmount.setText ("Total Due=" + String.format( "%.2f", totalDue ) );

		Date lastdepositDate = CreditEntry.getLastDepositDate(selectedCustomer, startDate);

		if (lastdepositDate!=null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			lastDepositDateLabel.setText ("Last Deposit Date=" + formatter.format (lastdepositDate ) );
		}
		else {
			lastDepositDateLabel.setText ("Last Deposit Date= N/A"  );
		}

		float previousPayment = CreditEntry.getLastDepositAmount (selectedCustomer, startDate);

		if (previousPayment!=0) {
			lastDepositAmount.setText ("Last Deposit Amount=" + String.format( "%.2f", previousPayment ) );
		}

		else {
			lastDepositAmount.setText ("Last Deposit Amount=N/A"  ); 
		}


		creditEntryModel = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {	

				return false ;
			}
			/*	public Class getColumnClass(int c) {
					return  
				} */
		};

		//	List  customerCreditEntryList = CreditEntry.getCreditEntryList(selectedCustomer,startDate , endDate);		
		creditEntryModel.setColumnIdentifiers(customerLedgerColumns);		 
		for (Object s : tableValueList) {

			Object [] objArr =  (Object []) s;
			creditEntryModel.addRow(objArr);
		}

		customerBillTable.setModel(creditEntryModel);
		customerBillTable.getColumnModel().getColumn(INDEX_OF_SL_NO_COLUMN).setWidth(18);
		customerBillTable.getColumnModel().getColumn(INDEX_OF_ITEM_COLUMN ).setPreferredWidth(50);
		customerBillTable.getColumnModel().getColumn(INDEX_OF_AMOUNT_COLUMN).setWidth(18);

	}

	private void populateCustomerData() {
		selectedRoute = (Route) routeRowList.getSelectedValue();
		if (selectedRoute != null) {
			customerList = Route.getAllCustomerInRoute(selectedRoute);
		}
		else {
			customerList = new ArrayList <Customer> ();
		}
		System.out.println( "*******" + customerList.size());
		customerRowList.setListData(customerList.toArray());
		//	customerRowList.repaint();
		customerListScrollPane.repaint();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			BillPrint dialog = new BillPrint (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(870, 560);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}





}
