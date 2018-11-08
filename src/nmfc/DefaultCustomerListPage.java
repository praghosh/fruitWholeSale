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
import entities.HalKhata;
import entities.Route;
import nmfc.BillPrint.GenerateBillPrintReportListener;
import nmfc.CreditEntryPage.CustomTableCellRenderer;
import nmfc.HalkhataEntryPage.SaveHalKhataForAllCustomers;
import nmfc.helper.CloseButton;
import nmfc.helper.PrintPdf;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import pdftables.AllCustomerDueOnDatePDF;
import pdftables.AllCustomerDueReport;
import pdftables.BillPrintPDF;
import pdftables.CustomerDefaulterReport;
import pdftables.PrintHalkhataBalanceForAllCustomers;
import pdftables.RouteWiseReportPDF;

public class DefaultCustomerListPage extends JFrame{
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


	private static final int  INDEX_OF__SERIAL_NUMBER =0;
	private static final int  INDEX_OF__NAME_OF_CUSTOMER =1;
	private static final int  INDEX_OF__FINAL_CASH_BALANCE_COLUMN = 2;
  
	
	private static final int  INDEX_OF__NAME_OF_CUSTOMER_PHONE =3;

	private static final int  SIZE_OF_ALL_COLUMN = 4;
	JFileChooser fc = new JFileChooser();
	public DefaultCustomerListPage (Frame homePage, String string, boolean b) {
		//super (homePage,  string,  b);
		super ( string );
		this.setResizable(false);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 820, 590 );
		getContentPane().setBackground(new Color(230, 245, 240) );
		initializeColumnClass ();
	 
		rightPanel = new JPanel();
		rightPanel.setLayout( null );
		rightPanel.setBounds( 10, 10, 520, 680 );
		rightPanel.setBackground(new Color(200, 250, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);



		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 180, 710, 160, 25 );
		add(closeButton);
	}
	
	

	private void createRightPanel() {

		createRouteWiseDetailTable();
		createDownButtons();
	 
	}
	private void createDownButtons() {

		JButton saveDefaulters = new JButton ("Save Defaulter Detail");
		saveDefaulters.setBounds( 120,650,225,35);
		saveDefaulters.setFont(StyleItems.buttonFont);
		saveDefaulters.setBackground(StyleItems.componentBackgroundColor2);	 
		saveDefaulters.addActionListener(new SaveHalDefaulterCustomers ());
		rightPanel.add(saveDefaulters);	 
	 
	}
	
	  

	public void createRouteWiseDetailTable() 

	{   
		JLabel title = new JLabel (" Defaulters List ");
		title.setBounds( 180, 20, 230, 35 );
		StyleItems.styleLabel (title);
		rightPanel.add(title);
		
		customerLedgerColumns = new String[SIZE_OF_ALL_COLUMN ]; 
		customerLedgerColumns [INDEX_OF__SERIAL_NUMBER ] = "<html>Serial<br>Number <br>"; 
		customerLedgerColumns [INDEX_OF__NAME_OF_CUSTOMER ] = "<html>Customer<br>Name <br>";            		            
	 	customerLedgerColumns [INDEX_OF__FINAL_CASH_BALANCE_COLUMN ] = "<html>Current Cash<br>Balance"; 
		customerLedgerColumns [INDEX_OF__NAME_OF_CUSTOMER_PHONE ] = "<html>Phone<br>Number"; 

		SimpleHeaderRenderer headerRenderForTRuckSale = new SimpleHeaderRenderer();
		headerRenderForTRuckSale.setFont(new Font("Consolas", Font.BOLD, 12)); 
		customerLedgerTable.getTableHeader().setDefaultRenderer(headerRenderForTRuckSale);
		customerLedgerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleTableMediumFont(customerLedgerTable);
		scrollForCustomerLedgerTable  = new JScrollPane(customerLedgerTable); 
		populateCustomerDefaulterTable();
		scrollForCustomerLedgerTable.setBounds( 10, 60, 500, 560 );
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
 

	private void populateCustomerDefaulterTable() {
		List  <CustomerTransaction> customerTransactionList = new ArrayList <CustomerTransaction> ();
	
		List <Customer> customerList= Customer.getDefaulterCustomerList ();
		
		long totalCashDueForAll =0;
		int serialNumber=1;
		ArrayList<Object[]> tableValueList = new ArrayList <Object []>();
		for (Customer s : customerList) {
			Object[] obj = new Object[SIZE_OF_ALL_COLUMN ];	
			obj[INDEX_OF__SERIAL_NUMBER ] = serialNumber++;
			obj [INDEX_OF__NAME_OF_CUSTOMER] = s.toString();
		 	long cashDue =   (long)  Utility.getValue( CustomerTransaction.getLastBalance (s));
			obj[INDEX_OF__FINAL_CASH_BALANCE_COLUMN ] = cashDue;						 
			obj[INDEX_OF__NAME_OF_CUSTOMER_PHONE ] = s.getMobile(); 
		 	tableValueList.add(obj);
		}
		
		creditEntryModel = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {	

				return false ;
			}
			public Class getColumnClass(int c) {

				Class requiredClass = DefaultCustomerListPage.this.columnClasses [c];
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
	
	private void initializeColumnClass () {

		columnClasses = new Class [SIZE_OF_ALL_COLUMN ];                                                 
		columnClasses [   INDEX_OF__FINAL_CASH_BALANCE_COLUMN]  = Long.class;
	}

	public class SaveHalDefaulterCustomers implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();

			try {
				if (fc.showSaveDialog(DefaultCustomerListPage.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}					 
					CustomerDefaulterReport.createDefaulterCustomerDuePDF  ( finalBackupPath);

				//	PrintPdf printJob = new PrintPdf(finalBackupPath, "Defaulter Job", false);
				//	printJob.print();
 
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

	
	public class PrintHalKhataForAllCustomers implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();

			try {
				if (fc.showSaveDialog(DefaultCustomerListPage.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}					 
					CustomerDefaulterReport.createDefaulterCustomerDuePDF  ( finalBackupPath);

					PrintPdf printJob = new PrintPdf(finalBackupPath, "Defaulter Job", false);
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
	/**                                                       
	 * Launch the applicSIZE_OF_ALL_COLUMN = 14;              
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			DefaultCustomerListPage dialog = new DefaultCustomerListPage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(870, 560);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}