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
import java.util.Vector;

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
import entities.TruckSales;
import nmfc.AllCustomersDue.GenerateRouteWiseDueReportListener;
import nmfc.CreditEntryPage.CustomTableCellRenderer;
import nmfc.helper.CloseButton;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import pdftables.AllCustomerDueReport;
import pdftables.CreateCashSellAndDepositReport;


public class CashDepositAndCashSellReport extends JFrame {
 	

	private JPanel leftPanel;
	private JPanel rightPanel;
	private JScrollPane routeListScrollPane;
	private Route selectedRoute;
	private List<Customer> customerList;
	private JScrollPane customerListScrollPane;
	private JList routeRowList;
	private JList customerRowList;
	private String[] customerCashDEpoistColumn;
	private  JTable customerCAshDepositTable = new JTable ();
	JFileChooser fc = new JFileChooser();
	 private String[] customerCashSellColumn;
	 private  JTable customerCashSellTable = new JTable ();
     private JScrollPane scrollForCashSaleTable;
	
	private JScrollPane scrollForCustomerSaleTable;
	private DefaultTableModel customerLedgerModel;
	
	
	private JXDatePicker fromEntryDatePicker = new JXDatePicker();
	private JXDatePicker toEntryDatePicker = new JXDatePicker();
	private Customer selectedCustomer;
	private DefaultTableModel creditEntryModel;
	private DefaultTableModel cashSaleTableModel;
	
	// Constants. 
	
	//Cash Deposit Table
	private static final int  INDEX_OF__CASHDEPOSIT_SL_NO_COLUMN = 0;
	private static final int INDEX_OF_BILL_NUMBER =1;	 
	private static final int INDEX_OF_CUSTOMER_NAME =2;	 
	private static final int INDEX_OF_CASH_DEPOSIT =3;	 
	private static final int SIZE_OF_CASH_DEPOSIT_TABLE_COLUMN =4;

	// Cash Sale Table
	private static final int  INDEX_OF_SL_NO_COLUMN = 0;
//	private static final int  INDEX_OF_TRUCK_NUMBER =1;
//	private static final int  INDEX_OF_PARTICULAR_COLUMN = 1; 
	private static final int INDEX_OF_CASH_SELL_CUSTOMER_NAME =1;
	
	private static final int  INDEX_OF_ITEM_COLUMN= 2	;
	private static final int  INDEX_OF_AMOUNT_RATE= 3	;
	private static final int  INDEX_OF_AMOUNT_QUANTITY= 4;
	private static final int  INDEX_OF_AMOUNT_COLUMN= 5	;
	private static final int  SIZE_OF_ALL_CASH_SELL_COLUMN = 6;
	private JLabel cashTotal;
	private JLabel cashDepositTotal;
 

	public CashDepositAndCashSellReport (Frame homePage, String string, boolean b) {
	//	super (homePage,  string,  b);
		super ( string );
		getContentPane().setLayout( null );
		setBounds( 50, 10, 720, 450 );
		getContentPane().setBackground(new Color(230, 245, 240) );
  
		rightPanel = new JPanel();
		rightPanel.setLayout( null );
		rightPanel.setBounds( 10, 10, 890, 740 );
		rightPanel.setBackground(new Color(200, 250, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);



		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 395, 775, 160, 35 );
		add(closeButton);
	}

	private void createRightPanel() {

		createDateRangeButtons();
	 	createCashDepositTable();
		createCashSellTable();
		createRouteReportButton ();


	}

	private void createCashSellTable() {
	 	
		customerCashSellColumn = new String[SIZE_OF_ALL_CASH_SELL_COLUMN] ;
		customerCashSellColumn [	INDEX_OF_SL_NO_COLUMN]  ="<html>Serial<br>No";       
		customerCashSellColumn [	INDEX_OF_CUSTOMER_NAME] ="<html>Customer<br>Name";   ;	              
		customerCashSellColumn [	INDEX_OF_ITEM_COLUMN] ="<html>Itemt<br>Name";
		customerCashSellColumn [	INDEX_OF_AMOUNT_RATE] ="<html>Itemt<br>Rate";	
		customerCashSellColumn [	INDEX_OF_AMOUNT_QUANTITY] ="<html>Quantity";   ;	              
		customerCashSellColumn [	INDEX_OF_AMOUNT_COLUMN] ="<html>Cash Purchase<br>";   ;	          	              
	
		cashTotal = new JLabel ("Total Cash Sale:");
	 	cashTotal.setBounds( 534, 660,280,25);
	 	StyleItems.styleComponent2 (cashTotal);
		rightPanel.add(cashTotal);     
  
		SimpleHeaderRenderer headerRenderForCashSale = new SimpleHeaderRenderer();
		headerRenderForCashSale.setFont(new Font("Consolas", Font.BOLD, 12)); 
		customerCashSellTable.getTableHeader().setDefaultRenderer(headerRenderForCashSale);
		customerCashSellTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleComponent2(customerCashSellTable);
		scrollForCashSaleTable  = new JScrollPane(customerCashSellTable); 
		populateCashSaleTable();
		scrollForCashSaleTable.setBounds( 4, 405, 870, 230 );
		customerCashSellTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (customerCashSellTable.getSelectedRow() > -1) {
					// print first column value from selected row
					//  System.out.println("Value = " + truckEntryTable.getValueAt(truckSaleTable.getSelectedRow(), 0).toString());
					//   fillCustomerEntryItems();
				}
			}
		});
	 	rightPanel.add(scrollForCashSaleTable);
	 	
	 	
	 	
		 
		
	}

	private void createDateRangeButtons() {
		
		JLabel titleLabel = new JLabel ("<html>" +
		 "নিউ মদিনা ফ্রুট কোম্পানী <br>" +
				"বাগনান বাস স্ট্যান্ড, হাওড়া" 
				
				);
		titleLabel.setBounds( 180, 10,250,55);
		StyleItems.styleComponentBig(titleLabel);
		rightPanel.add(titleLabel);
		
		 
		
		JLabel OnDateLabel = new JLabel ("Report Date:");
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
					populateCustomerDepositTable();
					populateCashSaleTable(); 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}

		};
		fromEntryDatePicker.addPropertyChangeListener(fromDateListener);
 		rightPanel.add (fromEntryDatePicker);
 	 
	}
	 

	private void populateCashSaleTable() {
		Date startDate = fromEntryDatePicker.getDate();
		//	Date endDate = toEntryDatePicker.getDate();
		/*	List <TruckSales>  truckSaleList = 
					TruckSales.getCustomerWiseSaleList(selectedCustomer, startDate); */
			List <TruckSales> cashSaleList = TruckSales.getCashPurchaseReport (startDate);
			ArrayList<Object[]> tableValueList = new ArrayList <Object []>();
			int slNo=1;
			float toTal =0;
		  
	 		
			float sum=0;
			for (TruckSales trSales : cashSaleList) {
				Object[] obj = new Object[SIZE_OF_ALL_CASH_SELL_COLUMN ];
		 		obj[INDEX_OF_SL_NO_COLUMN] = slNo;
				slNo++;	 	 
				obj [INDEX_OF_CASH_SELL_CUSTOMER_NAME] = trSales.getCustomer().toString();			 
				obj[INDEX_OF_ITEM_COLUMN]=    trSales.getFruit();
				obj [INDEX_OF_AMOUNT_RATE] = trSales.getRate();
				obj [INDEX_OF_AMOUNT_QUANTITY] = trSales.getQuantity();		 
				obj[INDEX_OF_AMOUNT_COLUMN] = Utility.getValue(trSales.getAmount());
				sum+=Utility.getValue(trSales.getAmount());
				tableValueList.add(obj);
			}
			cashTotal.setText("Total Cash Sell= " + sum);
			cashSaleTableModel = new DefaultTableModel(){

				@Override
				public boolean isCellEditable(int row, int column) {	

					return false ;
				}
				/*	public Class getColumnClass(int c) {
						return  
					} */
			};

			//	List  customerCreditEntryList = CreditEntry.getCreditEntryList(selectedCustomer,startDate , endDate);		
			cashSaleTableModel.setColumnIdentifiers(customerCashSellColumn);		 
			for (Object s : tableValueList) {

				Object [] objArr =  (Object []) s;
				cashSaleTableModel.addRow(objArr);
			}
	 
			customerCashSellTable.setModel(cashSaleTableModel);
		
	}
	public void createCashDepositTable() 

	{   
		customerCashDEpoistColumn = new String[SIZE_OF_CASH_DEPOSIT_TABLE_COLUMN] ;
		customerCashDEpoistColumn [	INDEX_OF__CASHDEPOSIT_SL_NO_COLUMN]  ="<html>Serial<br>No";     
		customerCashDEpoistColumn [	INDEX_OF_BILL_NUMBER] = "Bill Number"	;              
		customerCashDEpoistColumn [	INDEX_OF_CUSTOMER_NAME] ="<html>Customer<br>Name";   ;	              
		customerCashDEpoistColumn [	INDEX_OF_CASH_DEPOSIT] ="<html>Cash Deposit<br>Name";   ;	          	              
		 
		
		cashDepositTotal = new JLabel ("Total Cash Sale:");
		cashDepositTotal.setBounds( 504, 370,280,25);
		StyleItems.styleComponent2 (cashDepositTotal);
		rightPanel.add(cashDepositTotal);
  
		SimpleHeaderRenderer headerForCashDepositTable = new SimpleHeaderRenderer();
		headerForCashDepositTable.setFont(new Font("Consolas", Font.BOLD, 12)); 
		customerCAshDepositTable.getTableHeader().setDefaultRenderer(headerForCashDepositTable);
		customerCAshDepositTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleComponent(customerCAshDepositTable);
		scrollForCustomerSaleTable  = new JScrollPane(customerCAshDepositTable); 
		populateCustomerDepositTable();
		scrollForCustomerSaleTable.setBounds( 4, 120, 870, 230 );
		customerCAshDepositTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (customerCAshDepositTable.getSelectedRow() > -1) {
					// print first column value from selected row
					//  System.out.println("Value = " + truckEntryTable.getValueAt(truckSaleTable.getSelectedRow(), 0).toString());
					//   fillCustomerEntryItems();
				}
			}
		});
		rightPanel.add(scrollForCustomerSaleTable);		
	}
	
	private void	createRouteReportButton () {
		
		JButton genarateReport = new JButton ("Generate Report");
		genarateReport.setBounds( 195, 675,170,20);
		StyleItems.styleLabel (genarateReport);
		genarateReport.addActionListener(new GenerateCashSellReportListener ());
		rightPanel.add(genarateReport);
	}

	public class GenerateCashSellReportListener   implements ActionListener {


		public void actionPerformed(ActionEvent evt) {

			 
			Date startDate = fromEntryDatePicker.getDate();
			try {

				fc.setDialogTitle("Create a file to create report");

				fc.setCurrentDirectory(fc.getCurrentDirectory());

				System.out.println("File=" + fc.getSelectedFile());
				if (fc.showSaveDialog(CashDepositAndCashSellReport.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}

					CreateCashSellAndDepositReport.createCashSellAndDeposititeReport ( finalBackupPath,  startDate);
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

 
	private void populateCustomerDepositTable() {
	 
		Date startDate = fromEntryDatePicker.getDate();
	//	Date endDate = toEntryDatePicker.getDate();
	/*	List <TruckSales>  truckSaleList = 
				TruckSales.getCustomerWiseSaleList(selectedCustomer, startDate); */
		List <CreditEntry> depositList = CreditEntry.getLastDepositDetail (startDate);
		ArrayList<Object[]> tableValueList = new ArrayList <Object []>();
		int slNo=1;
		float total =0;
		for (CreditEntry trSales : depositList) {
			Object[] obj = new Object[SIZE_OF_CASH_DEPOSIT_TABLE_COLUMN];
		 
			obj[INDEX_OF__CASHDEPOSIT_SL_NO_COLUMN] = slNo;
			slNo++;	 	 
			obj [INDEX_OF_BILL_NUMBER] =trSales.getBillNumber();
			obj [INDEX_OF_CUSTOMER_NAME] = trSales.getCustomer().toString();			 
			obj[INDEX_OF_CASH_DEPOSIT] = Utility.getValue(trSales.getCashDeposit());
			total+=Utility.getValue(trSales.getCashDeposit());
			tableValueList.add(obj);
		}
		cashDepositTotal.setText("Total Cash Deposit: " + total);
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
		creditEntryModel.setColumnIdentifiers(customerCashDEpoistColumn);		 
		for (Object s : tableValueList) {

			Object [] objArr =  (Object []) s;
			creditEntryModel.addRow(objArr);
		}
 
		customerCAshDepositTable.setModel(creditEntryModel);
	//	customerBillTable.getColumnModel().getColumn(INDEX_OF_SL_NO_COLUMN).setWidth(18);
	//	customerBillTable.getColumnModel().getColumn(INDEX_OF_ITEM_COLUMN ).setPreferredWidth(50);
	//	customerBillTable.getColumnModel().getColumn(INDEX_OF_AMOUNT_COLUMN).setWidth(18);

	}
 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			CashDepositAndCashSellReport dialog = new CashDepositAndCashSellReport (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(870, 560);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}





}
