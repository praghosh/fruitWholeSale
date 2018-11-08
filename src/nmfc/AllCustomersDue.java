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
import nmfc.BillPrint.GenerateBillPrintReportListener;
import nmfc.CreditEntryPage.CustomTableCellRenderer;
import nmfc.helper.CloseButton;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import pdftables.AllCustomerDueOnDatePDF;
import pdftables.AllCustomerDueReport;
import pdftables.BillPrintPDF;
import pdftables.RouteWiseReportPDF;

public class AllCustomersDue extends JFrame{
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


	private static final int  INDEX_OF_CRATE_BALANCE_COLUMN = 0;
	private static final int  INDEX_OF__BIG_CRATE_BALANCE_COLUMN = 1;;
	private static final int  INDEX_OF__FINAL_CASH_BALANCE_COLUMN = 2;
  
	private static final int  INDEX_OF__NAME_OF_CUSTOMER =3;
	private static final int  INDEX_OF__NAME_OF_CUSTOMER_PHONE =4;

	private static final int  SIZE_OF_ALL_COLUMN = 5;
	JFileChooser fc = new JFileChooser();
	public AllCustomersDue (Frame homePage, String string, boolean b) {
		//super (homePage,  string,  b);
		super ( string );
		getContentPane().setLayout( null );
		setBounds( 160, 140, 820, 550 );
		getContentPane().setBackground(new Color(230, 245, 240) );
		initializeColumnClass ();
	 
		rightPanel = new JPanel();
		rightPanel.setLayout( null );
		rightPanel.setBounds( 10, 10, 690, 550 );
		rightPanel.setBackground(new Color(200, 250, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);



		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 280, 580, 160, 35 );
		add(closeButton);
	}

	private void createRightPanel() {

		createRouteWiseDetailTable();
		createRouteReportButton ();
		createSelectDateButton ();
		createDueReportOnDateButton ();

	}

	
	private void	createRouteReportButton () {
		
		JButton genarateReport = new JButton ("Generate Report");
		genarateReport.setBounds( 265,420,170,20);
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

	public void createRouteWiseDetailTable() 

	{   
		JLabel title = new JLabel (" Final Due of All Customers ");
		title.setBounds( 210, 20, 230, 35 );
		StyleItems.styleLabel (title);
		rightPanel.add(title);
		
		customerLedgerColumns = new String[SIZE_OF_ALL_COLUMN ]; 
		customerLedgerColumns [INDEX_OF__NAME_OF_CUSTOMER ] = "<html>Customer<br>Name";            		            
		customerLedgerColumns [INDEX_OF_CRATE_BALANCE_COLUMN] = "<html>Crate<br>Balance";            
		customerLedgerColumns [INDEX_OF__BIG_CRATE_BALANCE_COLUMN]  = "<html><b>Big Crate<br>Balance<b>"; 
		customerLedgerColumns [INDEX_OF__FINAL_CASH_BALANCE_COLUMN ] = "<html>Cash<br>Balance"; 
		customerLedgerColumns [INDEX_OF__NAME_OF_CUSTOMER_PHONE ] = "<html>Phone<br>Number"; 



		SimpleHeaderRenderer headerRenderForTRuckSale = new SimpleHeaderRenderer();
		headerRenderForTRuckSale.setFont(new Font("Consolas", Font.BOLD, 12)); 
		customerLedgerTable.getTableHeader().setDefaultRenderer(headerRenderForTRuckSale);
		customerLedgerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleTableMediumFont(customerLedgerTable);
		scrollForCustomerLedgerTable  = new JScrollPane(customerLedgerTable); 
		populateCustomerLedgerTable();
		scrollForCustomerLedgerTable.setBounds( 4, 60, 670, 340 );
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

	 

	private void populateCustomerLedgerTable() {
		List  <CustomerTransaction> customerTransactionList = new ArrayList <CustomerTransaction> ();
	
		customerTransactionList = CustomerTransaction.getFinalBalanceList ();
		
		long totalCashDueForAll =0;
		
		ArrayList<Object[]> tableValueList = new ArrayList <Object []>();
		for (Object s : customerTransactionList) {
			Object[] obj = new Object[SIZE_OF_ALL_COLUMN ];
			CustomerTransaction transcationEntry = (CustomerTransaction) s;
			//	obj[INDEX_OF_ID] = transcationEntry.getId();
			obj [INDEX_OF__NAME_OF_CUSTOMER] = transcationEntry.getCustomer().toString();
			 obj[INDEX_OF_CRATE_BALANCE_COLUMN] = transcationEntry.getSmallCrateDue();			 
			obj [INDEX_OF__BIG_CRATE_BALANCE_COLUMN]  = transcationEntry.getBigCrateDue();
		 	long cashDue =   (long)  Utility.getValue( transcationEntry.getCashDue());
			obj[INDEX_OF__FINAL_CASH_BALANCE_COLUMN ] = cashDue;			
			totalCashDueForAll+=cashDue;
			obj[INDEX_OF__NAME_OF_CUSTOMER_PHONE ] = transcationEntry.getCustomer().getMobile();
			tableValueList.add(obj);
		}
		// Add Total;
		Object[] obj = new Object[SIZE_OF_ALL_COLUMN ];
		obj [INDEX_OF__NAME_OF_CUSTOMER] ="Total";
		obj[INDEX_OF__FINAL_CASH_BALANCE_COLUMN ] = totalCashDueForAll;
		tableValueList.add(obj);
		creditEntryModel = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {	

				return false ;
			}
			public Class getColumnClass(int c) {

				Class requiredClass = AllCustomersDue.this.columnClasses [c];
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
	public void createSelectDateButton () {
		
		JLabel selectFromEntryDateLabel = new JLabel ("Select Balance Date");
		selectFromEntryDateLabel.setBounds( 10,470,115,20);
		StyleItems.styleLabel (selectFromEntryDateLabel); 
		rightPanel.add (selectFromEntryDateLabel);

		fromEntryDatePicker = new JXDatePicker();
		fromEntryDatePicker.setDate(new Date());
		//StyleItems.styleButton (entryDatePicker);
		fromEntryDatePicker.setBounds( 10,510,160,20);
		fromEntryDatePicker.setFormats("dd/MM/yyyy");
		PropertyChangeListener listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**receiveDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("receiveDatePicker.isEditValid()  " +fromEntryDatePicker.isEditValid());
				if ((fromEntryDatePicker.getDate()!=null) && fromEntryDatePicker.isEditValid()) {
					System.out.println(fromEntryDatePicker.getDate());
					// refreshTruckList();
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		fromEntryDatePicker.addPropertyChangeListener(listener);
		rightPanel.add (fromEntryDatePicker);
	}

	public class GenerateRouteWiseDueReportListener   implements ActionListener {


		public void actionPerformed(ActionEvent evt) {

			 
			Date startDate = fromEntryDatePicker.getDate();
			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(AllCustomersDue.this) == JFileChooser.APPROVE_OPTION) {

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
				if (fc.showSaveDialog(AllCustomersDue.this) == JFileChooser.APPROVE_OPTION) {

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

	private void populateCustomerData() {

	}

	private void initializeColumnClass () {

		columnClasses = new Class [SIZE_OF_ALL_COLUMN ];                                                 
		columnClasses [ INDEX_OF_CRATE_BALANCE_COLUMN] = Long.class;    
		columnClasses [ INDEX_OF__BIG_CRATE_BALANCE_COLUMN]  = Long.class;
		columnClasses [   INDEX_OF__FINAL_CASH_BALANCE_COLUMN]  = Double.class;
	}


	/**                                                       
	 * Launch the applicSIZE_OF_ALL_COLUMN = 14;              
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			AllCustomersDue dialog = new AllCustomersDue (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(870, 560);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}