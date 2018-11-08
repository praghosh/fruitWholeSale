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
import entities.Customer;
import entities.CustomerTransaction;
import entities.Route;
import entities.TruckSales;
import nmfc.helper.CloseButton;
import nmfc.helper.DateUtility;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import pdftables.AllCustomerDueOnDatePDF;
import pdftables.AllCustomerDueReport;
import pdftables.BillPrintPDF;
import pdftables.RouteWiseReportPDF;

public class CustomerTotalSell extends JFrame{
	private JPanel leftPanel;
	private JPanel rightPanel;
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

 	 
	private static final int  INDEX_OF__NAME_OF_CUSTOMER =0;
	private static final int  INDEX_OF_DISCOUNTED_SELL =1; 
	private static final int  INDEX_OF_DISCOUNT_AMOUNT =2;
	private static final int  SIZE_OF_ALL_COLUMN = 3;
	JFileChooser fc = new JFileChooser();
	public CustomerTotalSell (Frame homePage, String string, boolean b) {
		//super (homePage,  string,  b);
		super (string);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 820, 550 );
		getContentPane().setBackground(new Color(230, 245, 240) );
		initializeColumnClass ();
	 
		rightPanel = new JPanel();
		rightPanel.setLayout( null );
		rightPanel.setBounds( 10, 10, 750, 550 );
		rightPanel.setBackground(new Color(200, 250, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);



		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 280, 620, 160, 35 );
		add(closeButton);
	}

	private void createRightPanel() {

		createTotalSellTable();
		createRouteReportButton ();
//		createSelectDateButton ();
	//	createDueReportOnDateButton ();
		createDateRangeButtons();

	}

	
	private void	createRouteReportButton () {
		
		JButton genarateReport = new JButton ("Generate Report");
		genarateReport.setBounds( 265,490,170,20);
		StyleItems.styleLabel (genarateReport);
		genarateReport.addActionListener(new GenerateRouteWiseDueReportListener ());
		rightPanel.add(genarateReport);
	}
   
	private void	createDueReportOnDateButton () {
		
		JButton genarateReport = new JButton ("Balance Report");
		genarateReport.setBounds( 265,500,170,20);
		StyleItems.styleLabel (genarateReport);
		genarateReport.addActionListener(new GenerateAllCustomerDueReportOnDateListener ());
		rightPanel.add(genarateReport);
	}

	public void createTotalSellTable() 

	{   
		JLabel title = new JLabel (" Total Sell of All Customers ");
		title.setBounds( 210, 20, 290, 35 );
		StyleItems.styleLabel (title);
		rightPanel.add(title);
		
		customerLedgerColumns = new String[SIZE_OF_ALL_COLUMN ]; 
		customerLedgerColumns [INDEX_OF__NAME_OF_CUSTOMER ] = "<html>Customer<br>Name";            		            
		customerLedgerColumns [INDEX_OF_DISCOUNTED_SELL] = "<html>Discounted <br>Sell";
		customerLedgerColumns [INDEX_OF_DISCOUNT_AMOUNT] = "<html>Discounted <br>at 4%";  
	 

		SimpleHeaderRenderer headerRenderForTRuckSale = new SimpleHeaderRenderer();
		headerRenderForTRuckSale.setFont(new Font("Consolas", Font.BOLD, 12)); 
		customerLedgerTable.getTableHeader().setDefaultRenderer(headerRenderForTRuckSale);
		customerLedgerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleTableMediumFont(customerLedgerTable);
		scrollForCustomerLedgerTable  = new JScrollPane(customerLedgerTable); 
		populateCustomerSellTable();
		scrollForCustomerLedgerTable.setBounds( 4, 120, 670, 340 );
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
		rightPanel.add(scrollForCustomerLedgerTable);		
	}

	 

	private void populateCustomerSellTable() {
		List  <CustomerTransaction> customerTransactionList = new ArrayList <CustomerTransaction> ();
	
		java.util.List <Customer > custrList = Customer.getCustomerLists();
		customerTransactionList = CustomerTransaction.getFinalBalanceList ();
		Date startDate = fromEntryDatePicker.getDate();
		Date endDate = toEntryDatePicker.getDate();
		
		ArrayList<Object[]> tableValueList = new ArrayList <Object []>();
		for (Customer cust : custrList) {
			Object[] obj = new Object[SIZE_OF_ALL_COLUMN ];
			
			obj [INDEX_OF__NAME_OF_CUSTOMER] = cust.getName();
			long totalDiscountedSell=(long)	TruckSales.getTotalDiscountedSales(cust, startDate, endDate);
			obj[INDEX_OF_DISCOUNTED_SELL]  = totalDiscountedSell
				 ;	
			obj[INDEX_OF_DISCOUNT_AMOUNT] = (long) ( 0.04 * totalDiscountedSell);  // At 4%
			tableValueList.add(obj);
		}



		creditEntryModel = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {	

				return false ;
			}
			public Class getColumnClass(int c) {

				Class requiredClass = CustomerTotalSell.this.columnClasses [c];
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
	 

	public class GenerateRouteWiseDueReportListener   implements ActionListener {


		public void actionPerformed(ActionEvent evt) {

			 
			Date startDate = fromEntryDatePicker.getDate();
			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(CustomerTotalSell.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}

					AllCustomerDueReport.createAllCustomerDuePDF ( finalBackupPath,  startDate);


					ToastMessage toastMessage = new ToastMessage("Report created Successfully",3000);
					toastMessage.setVisible(true);
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
	
	public class GenerateAllCustomerDueReportOnDateListener   implements ActionListener {


		public void actionPerformed(ActionEvent evt) {

			 
			Date startDate = fromEntryDatePicker.getDate();
			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(CustomerTotalSell.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}

					AllCustomerDueOnDatePDF.createAllCustomerOnDateDuePDF ( finalBackupPath,  startDate);


					ToastMessage toastMessage = new ToastMessage("Report created Successfully",3000);
					toastMessage.setVisible(true);
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

	 
	private void initializeColumnClass () {

		columnClasses = new Class [SIZE_OF_ALL_COLUMN ];                                                 
		columnClasses [INDEX_OF_DISCOUNTED_SELL] = Long.class;  
		columnClasses [INDEX_OF_DISCOUNT_AMOUNT  ] = Long.class;
	}
	private void createDateRangeButtons() {
		JLabel fromDateLabel = new JLabel ("From:");
		fromDateLabel.setBounds( 70, 80,70,25);
		StyleItems.styleComponentSmall (fromDateLabel);
		rightPanel.add(fromDateLabel);

		fromEntryDatePicker.setDate(DateUtility.getFirstDateOfMonth());
		StyleItems.styleDatePickerSmall(fromEntryDatePicker);
		fromEntryDatePicker.setBounds( 160, 80,108,20);
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
					populateCustomerSellTable(); 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		fromEntryDatePicker.addPropertyChangeListener(fromDateListener);
		rightPanel.add (fromEntryDatePicker);

		JLabel toDateLabel = new JLabel ("To:");
		toDateLabel.setBounds( 380, 80,60,25);
		StyleItems.styleComponentSmall (toDateLabel);
		rightPanel.add(toDateLabel);

		toEntryDatePicker.setDate(DateUtility.getLastDateOfMonth());
		StyleItems.styleDatePickerSmall(toEntryDatePicker);
		toEntryDatePicker.setBounds( 430, 80,108,20);
		toEntryDatePicker.setFormats("dd/MM/yyyy");		
		rightPanel.add (toEntryDatePicker);

		PropertyChangeListener toDateListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**receiveDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("toEntryDatePicker.isEditValid()  " +toEntryDatePicker.isEditValid());
				if ((toEntryDatePicker.getDate()!=null) && toEntryDatePicker.isEditValid()) {
					System.out.println(toEntryDatePicker.getDate());
					populateCustomerSellTable(); 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		toEntryDatePicker.addPropertyChangeListener(toDateListener);

	}

	/**                                                       
	 * Launch the applicSIZE_OF_ALL_COLUMN = 14;              
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			CustomerTotalSell dialog = new CustomerTotalSell (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(770, 720);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
