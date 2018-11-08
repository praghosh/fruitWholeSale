package nmfc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

import nmfc.CreditEntryPage.CustomTableCellRenderer;
import nmfc.TruckWiseSaleReport.GenerateTruckWiseReportListener;
import nmfc.helper.CloseButton;
import nmfc.helper.DateUtility;
import nmfc.helper.PrintPdf;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import pdftables.CreateCustomerLedgerPDFReport;

public class CustomerLedgerPage extends JFrame{
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JScrollPane routeListScrollPane;
	private Route selectedRoute;
	private List<Customer> customerList;
	private JScrollPane customerListScrollPane;
	private JList routeRowList;
	private JList customerRowList;
	private String[] customerLedgerColumns;
	private  JTable customerLedgerTable = new JTable ();
	private JScrollPane scrollForCustomerLedgerTable;
	private DefaultTableModel customerLedgerModel;
	private JXDatePicker fromEntryDatePicker = new JXDatePicker();
	private JXDatePicker toEntryDatePicker = new JXDatePicker();
	private Customer selectedCustomer;
	private DefaultTableModel creditEntryModel;
	private Class[] columnClasses;
	// Constants. 
	private static final int  INDEX_OF_TRANSCATION_DATE_COLUMN = 0;
	private static final int  INDEX_OF_ACTUAL_DATE_COLUMN = 1;

	private static final int  INDEX_OF_PARTICULAR_COLUMN = 2;

	private static final int  INDEX_OF_CRATE__DEPOSIT_COLUMN = 3;
	private static final int  INDEX_OF_CRATE_ISSUE_COLUMN = 4;
	private static final int  INDEX_OF_CRATE_BALANCE_COLUMN = 5;

	private static final int  INDEX_OF_BIG_CRATE__DEPOSIT_COLUMN = 6;
	private static final int  INDEX_OF_BIG_CRATE_ISSUE_COLUMN = 7;
	private static final int  INDEX_OF__BIG_CRATE_BALANCE_COLUMN = 8;


	private static final int  INDEX_OF_PAGE_NUMBER_COLUMN = 9;	
	private static final int  INDEX_OF_NORMAL_AMOUNT_COLUMN = 10;
	private static final int  INDEX_OF_DISCOUNTED_AMOUNT_COLUMN = 11;

	private static final int  INDEX_OF_CASH_DEPOSIT_COLUMN = 12;
	private static final int  INDEX_OF__FINAL_CASH_BALANCE_COLUMN = 13;	 
	private static final int  SIZE_OF_ALL_COLUMN = 14;
	JFileChooser fc = new JFileChooser();

	public CustomerLedgerPage (Frame homePage, String string, boolean b) {
		//super (homePage,  string,  b);
		super (string);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 920, 650 );
		getContentPane().setBackground(new Color(230, 245, 240) );
		initializeColumnClass ();
		topPanel = new JPanel();
		topPanel.setLayout( null );
		topPanel.setBounds( 10, 10, 1310, 200 );
		topPanel.setBackground(new Color(160, 230, 210) );
		topPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createTopPanel();
		add(topPanel);


		bottomPanel = new JPanel();
		bottomPanel.setLayout( null );
		bottomPanel.setBounds( 10, 220, 1310, 430 );
		bottomPanel.setBackground(new Color(200, 250, 210) );
		bottomPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createBottomPanel();
		add(bottomPanel);

		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 395, 660, 160, 35 );
		add(closeButton);
	}

	private void createBottomPanel() {

		createDateRangeButtons();
		createCustomerLedgerTable();
		createReportButtons();


	}

	private void createDateRangeButtons() {
		JLabel fromDateLabel = new JLabel ("From:");
		fromDateLabel.setBounds( 180, 20,70,25);
		StyleItems.styleComponentSmall (fromDateLabel);
		bottomPanel.add(fromDateLabel);

		fromEntryDatePicker.setDate(DateUtility.getFirstDateOfMonth());
		StyleItems.styleDatePickerSmall(fromEntryDatePicker);
		fromEntryDatePicker.setBounds( 210, 20,108,20);
		fromEntryDatePicker.setFormats("dd/MM/yyyy");

		PropertyChangeListener fromDateListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {

				// When page opens and closes these event is called with 
				// name ancestor. This is quite unnecesary.
				if ("ancestor".equals(e.getPropertyName())) { 
					return;
				}

				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("fromEntryDatePicker.isEditValid()  " +fromEntryDatePicker.isEditValid());
				if ((fromEntryDatePicker.getDate()!=null) && fromEntryDatePicker.isEditValid()) {
					System.out.println(fromEntryDatePicker.getDate());
					populateCustomerLedgerTable(); 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		fromEntryDatePicker.addPropertyChangeListener(fromDateListener);


		bottomPanel.add (fromEntryDatePicker);

		JLabel toDateLabel = new JLabel ("To:");
		toDateLabel.setBounds( 390, 20,60,25);
		StyleItems.styleComponentSmall (toDateLabel);
		bottomPanel.add(toDateLabel);

		toEntryDatePicker.setDate(DateUtility.getLastDateOfMonth());
		StyleItems.styleDatePickerSmall(toEntryDatePicker);
		toEntryDatePicker.setBounds( 430, 20,108,20);
		toEntryDatePicker.setFormats("dd/MM/yyyy");		
		bottomPanel.add (toEntryDatePicker);

		PropertyChangeListener toDateListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**receiveDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("toEntryDatePicker.isEditValid()  " +toEntryDatePicker.isEditValid());
				if ((toEntryDatePicker.getDate()!=null) && toEntryDatePicker.isEditValid()) {
					System.out.println(toEntryDatePicker.getDate());
					populateCustomerLedgerTable(); 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		toEntryDatePicker.addPropertyChangeListener(toDateListener);

	}

	public void createCustomerLedgerTable() 

	{   
		customerLedgerColumns = new String[SIZE_OF_ALL_COLUMN ];             
		customerLedgerColumns [INDEX_OF_TRANSCATION_DATE_COLUMN] ="<html>Transaction<br>Entry<br>Date<br>--";      
		customerLedgerColumns [INDEX_OF_ACTUAL_DATE_COLUMN] = "<html>Actual<br>Sale/Credit<br>Date<br>"; 
		customerLedgerColumns [ INDEX_OF_PARTICULAR_COLUMN ]=  "Particular"; 

		customerLedgerColumns [INDEX_OF_CRATE__DEPOSIT_COLUMN ] = "<html>Crate<br>Deposit";             
		customerLedgerColumns [INDEX_OF_CRATE_ISSUE_COLUMN ]= "<html>Small Crate<br>Issue";            
		customerLedgerColumns [INDEX_OF_CRATE_BALANCE_COLUMN] = "<html>Small Crate<br>Due"; 

		customerLedgerColumns [INDEX_OF_BIG_CRATE__DEPOSIT_COLUMN] = "<html>Big Crate<br>Deposit";        
		customerLedgerColumns [INDEX_OF_BIG_CRATE_ISSUE_COLUMN  ] = "<html>Big Crate<br>Issue";           
		customerLedgerColumns [INDEX_OF__BIG_CRATE_BALANCE_COLUMN]  = "<html><b>Big Crate<br>Due<b>"; 

		customerLedgerColumns [INDEX_OF_PAGE_NUMBER_COLUMN]= "<html>Page<br>Number";  
		customerLedgerColumns [INDEX_OF_NORMAL_AMOUNT_COLUMN] = "<html>Normal<br> Amount";     
		customerLedgerColumns [INDEX_OF_DISCOUNTED_AMOUNT_COLUMN] = "<html>Discounted<br> Amount";

		customerLedgerColumns [INDEX_OF_CASH_DEPOSIT_COLUMN ] = "<html>Cash<br>Deposit"; 
		customerLedgerColumns [INDEX_OF__FINAL_CASH_BALANCE_COLUMN ] = "<html>Cash<br>Due";
		 


		SimpleHeaderRenderer headerRenderForTRuckSale = new SimpleHeaderRenderer();
		headerRenderForTRuckSale.setFont(new Font("Consolas", Font.BOLD, 13)); 
		customerLedgerTable.getTableHeader().setDefaultRenderer(headerRenderForTRuckSale);
		customerLedgerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleTableMediumFont(customerLedgerTable);
		scrollForCustomerLedgerTable  = new JScrollPane(customerLedgerTable); 
		populateCustomerLedgerTable();
		scrollForCustomerLedgerTable.setBounds( 4, 60, 1290, 290 );
		customerLedgerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (customerLedgerTable.getSelectedRow() > -1) {
					// print first column value from selected row
					//  System.out.println("Value = " + truckEntryTable.getValueAt(truckSaleTable.getSelectedRow(), 0).toString());
					//   fillCustomerEntryItems();
				}
			}
		});
		bottomPanel.add(scrollForCustomerLedgerTable);		
	}

	private void createTopPanel() {

		JLabel selectRouteLabel = new JLabel ("Select Route");
		selectRouteLabel.setBounds( 10,20,115,20);
		StyleItems.styleLabel (selectRouteLabel); 
		topPanel.add (selectRouteLabel);


		routeListScrollPane = new JScrollPane();
		routeListScrollPane.setBounds( 150, 10, 160, 180 );
		List routes = Route.getRouteNames();
		routeRowList = new JList(routes.toArray());
		routeRowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = routeRowList.getSelectedIndex();
				if (idx != -1)
				{
					if (!le.getValueIsAdjusting()) {
						System.out.println("Current selection: " +  idx);
						populateCustomerData();
						populateCustomerLedgerTable();
					}

				}
				else
					System.out.println("Please choose a Route.");
			}

		}); 	
		routeRowList.setVisibleRowCount(12);
		StyleItems.styleList2 (routeRowList);
		routeListScrollPane.setViewportView(routeRowList);
		routeRowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

		//	listScrollPane.add(rowList);
		topPanel.add(routeListScrollPane);


		JLabel selectEntryDateLabel = new JLabel ("Select Customer");
		selectEntryDateLabel.setBounds( 450,20,135,20);
		StyleItems.styleLabel (selectEntryDateLabel); 
		topPanel.add (selectEntryDateLabel);	


		customerListScrollPane = new JScrollPane();
		customerListScrollPane.setBounds( 575, 10, 180, 180 );
		selectedRoute = (Route) routeRowList.getSelectedValue();
		if (selectedRoute != null) {
			customerList =  Route.getAllCustomerInRoute(selectedRoute);
		}
		else {
			customerList = new ArrayList <Customer> ();
		}		
		customerRowList = new JList(customerList.toArray());
		StyleItems.styleList (customerRowList);
		customerRowList.setVisibleRowCount(12);		 
		customerListScrollPane.setViewportView(customerRowList);
		customerRowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = customerRowList.getSelectedIndex();
				if (idx != -1)
				{
					if (!le.getValueIsAdjusting()) {
						System.out.println("Current selection: " +  idx);
						populateCustomerLedgerTable();
					}


				}
				else
					System.out.println("Please choose a Truck.");
			}
		});
		customerRowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		topPanel.add(customerListScrollPane);

	}


	private void populateCustomerLedgerTable() {
		if (customerRowList.getSelectedIndex()==-1)
			return;
		selectedCustomer = (Customer)customerRowList.getSelectedValue();
		Date startDate = fromEntryDatePicker.getDate();
		Date endDate = toEntryDatePicker.getDate();
		List  customerTransactionList = CustomerTransaction.getTransactionListByEntryDate(selectedCustomer,startDate , endDate);		
		ArrayList<Object[]> tableValueList = new ArrayList <Object []>();
		Object[] obj =null;
		boolean isFinalBalace=true;
		for (Object s : customerTransactionList) {
		    obj = new Object[SIZE_OF_ALL_COLUMN ];
			CustomerTransaction transcationEntry = (CustomerTransaction) s;
			//	obj[INDEX_OF_ID] = transcationEntry.getId();
			isFinalBalace = transcationEntry.getFinalBalance();
			obj[INDEX_OF_TRANSCATION_DATE_COLUMN ] = transcationEntry.getEntryDate();
			obj[INDEX_OF_PARTICULAR_COLUMN  ] = transcationEntry.getParticular();
			obj[INDEX_OF_ACTUAL_DATE_COLUMN] = transcationEntry.getCreditOrSalesDate();
			obj[INDEX_OF_CRATE__DEPOSIT_COLUMN ] = transcationEntry.getCrateDeposit();
			obj[INDEX_OF_CRATE_ISSUE_COLUMN] = transcationEntry.getCrateIssue();
			obj[INDEX_OF_CRATE_BALANCE_COLUMN] = transcationEntry.getSmallCrateDue();

			obj[INDEX_OF_BIG_CRATE_ISSUE_COLUMN ] = transcationEntry.getBigCrateIssue();
			obj[INDEX_OF_BIG_CRATE__DEPOSIT_COLUMN] = transcationEntry.getBigCrateDeposit();
			obj [INDEX_OF__BIG_CRATE_BALANCE_COLUMN]  = transcationEntry.getBigCrateDue();

			obj  [INDEX_OF_NORMAL_AMOUNT_COLUMN] = transcationEntry.getCashPurchase();
			obj[INDEX_OF_DISCOUNTED_AMOUNT_COLUMN]  = (long) ( Utility.getValue(  transcationEntry.getDiscountedPurchase()));

			obj[INDEX_OF_CASH_DEPOSIT_COLUMN] =(long) ( Utility.getValue(  transcationEntry.getCashDeposit()));
			obj[INDEX_OF__FINAL_CASH_BALANCE_COLUMN ] = (long) ( Utility.getValue( transcationEntry.getCashDue()));


			tableValueList.add(obj);
		}

		// If the lastBalnce is not final then add an extra line for final balance
		
				if (!isFinalBalace) 
				{
					float finalCashBlance  = Utility.getValue( (Float) obj[INDEX_OF__FINAL_CASH_BALANCE_COLUMN ] ) +
							Utility.getValue( (Float) obj[INDEX_OF_DISCOUNTED_AMOUNT_COLUMN ] ) +
							Utility.getValue( (Float) obj[INDEX_OF_NORMAL_AMOUNT_COLUMN] ) -
							Utility.getValue( (Float) obj[INDEX_OF_CASH_DEPOSIT_COLUMN] );
					
					long finalCrateDue = Utility.getValue( (Long) obj[INDEX_OF_CRATE_BALANCE_COLUMN]   ) +			 
							Utility.getValue( (Long) obj[INDEX_OF_CRATE_ISSUE_COLUMN] ) -
							Utility.getValue( (Long) obj[INDEX_OF_CRATE__DEPOSIT_COLUMN ] );
					
					long finalBigCrateDue = Utility.getValue( (Long) obj[INDEX_OF__BIG_CRATE_BALANCE_COLUMN]   ) +			 
							Utility.getValue( (Long) obj[INDEX_OF_BIG_CRATE_ISSUE_COLUMN] ) -
							Utility.getValue( (Long) obj[INDEX_OF_BIG_CRATE__DEPOSIT_COLUMN] );
					
					obj = new Object[SIZE_OF_ALL_COLUMN ];
					obj[INDEX_OF__FINAL_CASH_BALANCE_COLUMN ] = finalCashBlance;
					obj[INDEX_OF_CRATE_BALANCE_COLUMN ] = finalCrateDue;
					obj[INDEX_OF__BIG_CRATE_BALANCE_COLUMN ] = finalBigCrateDue;
					tableValueList.add(obj);
					obj=null;
				}


		creditEntryModel = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {	

				return false ;
			}
			public Class getColumnClass(int c) {

				Class requiredClass = CustomerLedgerPage.this.columnClasses [c];
				if (requiredClass!=null) {
					return requiredClass;
				}
				else {
					return Object.class;
				}
			}
		};

		//	List  customerCreditEntryList = CreditEntry.getCreditEntryList(selectedCustomer,startDate , endDate);		
		creditEntryModel.setColumnIdentifiers(customerLedgerColumns);		 
		for (Object s : tableValueList) {

			Object [] objArr =  (Object []) s;
			creditEntryModel.addRow(objArr);
		}

		customerLedgerTable.setModel(creditEntryModel);

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

	private void initializeColumnClass () {

		columnClasses = new Class [SIZE_OF_ALL_COLUMN ];
		columnClasses [ INDEX_OF_TRANSCATION_DATE_COLUMN]  = Date.class; 
		columnClasses [ INDEX_OF_ACTUAL_DATE_COLUMN] = Date.class;      

		columnClasses [ INDEX_OF_PARTICULAR_COLUMN]  =  String.class;                                             
		columnClasses [ INDEX_OF_CRATE__DEPOSIT_COLUMN] = Long.class;   
		columnClasses [ INDEX_OF_CRATE_ISSUE_COLUMN] = Long.class;      
		columnClasses [ INDEX_OF_CRATE_BALANCE_COLUMN] = Long.class;    

		columnClasses [ INDEX_OF_BIG_CRATE__DEPOSIT_COLUMN]  = Long.class;
		columnClasses [ INDEX_OF_BIG_CRATE_ISSUE_COLUMN] = Long.class;  
		columnClasses [ INDEX_OF__BIG_CRATE_BALANCE_COLUMN]  = Long.class;

		columnClasses [ INDEX_OF_PAGE_NUMBER_COLUMN] = String.class;	    
		columnClasses [ INDEX_OF_NORMAL_AMOUNT_COLUMN]  = Double.class;   

		columnClasses [  INDEX_OF_CASH_DEPOSIT_COLUMN] = Long.class;    
		columnClasses [   INDEX_OF__FINAL_CASH_BALANCE_COLUMN]  = Long.class;
	}

	private void createReportButtons() {
		JButton genarateReport = new JButton ("Generate Ledger Report");
		genarateReport.setBounds( 760,50,230,20);
		StyleItems.styleLabel (genarateReport);
		genarateReport.addActionListener(new GenerateTruckWiseReportListener ());
		topPanel.add(genarateReport);
		
		JButton printReport = new JButton ("Print Ledger Report");
		printReport.setBounds( 760,90,230,20);
		StyleItems.styleLabel (printReport);
		printReport.addActionListener(new PrintTruckWiseReportListener ());
		topPanel.add(printReport);
	
	
	}
	
	public class PrintTruckWiseReportListener   implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
			int idx = customerRowList.getSelectedIndex();
			if (idx == -1){
				System.out.println ("Please select a Customer");
				ToastMessage toastMessage = new ToastMessage("Please choose a Customer",3000);
				toastMessage.setVisible(true);
				return;

			}

			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(CustomerLedgerPage.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}
					
					selectedCustomer = (Customer)customerRowList.getSelectedValue();
					Date startDate = fromEntryDatePicker.getDate();
					Date endDate = toEntryDatePicker.getDate();
					 new CreateCustomerLedgerPDFReport().createCustomerLedgerReport
					 (selectedCustomer, finalBackupPath, startDate, endDate);
					 
					 // Now print the pdf file created
					 PrintPdf printJob = new PrintPdf(finalBackupPath, "Print Ledger Job", false);
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

	public class GenerateTruckWiseReportListener   implements ActionListener {


		public void actionPerformed(ActionEvent evt) {
			int idx = customerRowList.getSelectedIndex();
			if (idx == -1){
				System.out.println ("Please select a Customer");
				ToastMessage toastMessage = new ToastMessage("Please choose a Customer",3000);
				toastMessage.setVisible(true);
				return;

			}

			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(CustomerLedgerPage.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}
					
					selectedCustomer = (Customer)customerRowList.getSelectedValue();
					Date startDate = fromEntryDatePicker.getDate();
					Date endDate = toEntryDatePicker.getDate();
					 new CreateCustomerLedgerPDFReport().createCustomerLedgerReport
					 (selectedCustomer, finalBackupPath, startDate, endDate);

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
	/**                                                       
	 * Launch the applicSIZE_OF_ALL_COLUMN = 14;              
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			CustomerLedgerPage dialog = new CustomerLedgerPage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(1230, 860);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}