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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import entities.Truck;
import entities.TruckEntry;
import entities.TruckSales;
import nmfc.helper.CloseButton;
import nmfc.helper.DateUtility;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import pdftables.AllCustomerDueOnDatePDF;
import pdftables.AllCustomerDueReport;
import pdftables.BillPrintPDF;
import pdftables.RouteWiseReportPDF;

public class TruckSaleReportPage extends JFrame{
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

 	 
	private static final int  INDEX_OF__NAME_OF_TRUCK =0;
	private static final int  INDEX_OF_TRUCK_RECEIVE_DATE =1;
	private static final int  INDEX_OF_ITEMS_RECEIVED =2;
	private static final int  INDEX_OF_TOTAL_SELL =3;
	private static final int  INDEX_OF_TOTAL_DEPOSIT=4;
	private static final int  INDEX_OF_FINAL_BALANCE=5;
	private static final int  SIZE_OF_ALL_COLUMN = 6;
	JFileChooser fc = new JFileChooser();
	public TruckSaleReportPage (Frame homePage, String string, boolean b) {
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
		customerLedgerColumns [INDEX_OF__NAME_OF_TRUCK ] = "<html>Truck<br>Number";            		            
		customerLedgerColumns [INDEX_OF_TRUCK_RECEIVE_DATE] = "<html>Receive <br>Date";
		customerLedgerColumns [INDEX_OF_ITEMS_RECEIVED ] = "<html>Items<br>Received";
		customerLedgerColumns [INDEX_OF_TOTAL_DEPOSIT] = "<html>Total<br>Deposit";
		customerLedgerColumns [INDEX_OF_TOTAL_SELL] = "<html>Total<br>Sell";
		customerLedgerColumns [INDEX_OF_FINAL_BALANCE] = "<html>Final<br>Balance";  
	 	

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
		 
		Date startDate = fromEntryDatePicker.getDate();
		Date endDate = toEntryDatePicker.getDate();
		List <Truck> truckList = Truck.getSoldOutTruckNames(startDate, endDate);
		
		ArrayList<Object[]> tableValueList = new ArrayList <Object []>();
		Date truckReciveDate = null;
		for (Truck truck : truckList) {
			Object[] obj = new Object[SIZE_OF_ALL_COLUMN ];
			
			obj [INDEX_OF__NAME_OF_TRUCK] = truck.getName();		 
			obj[INDEX_OF_TRUCK_RECEIVE_DATE]  = truck.getReceiveDate();			
			List <TruckEntry> trnList = TruckEntry.getTruckEntryList (truck);
			
			Map <String, Long > fruitMap = new HashMap () ;
			
			for (TruckEntry trn : trnList) {
				
				String item = trn.getFruit().toString();
				long number = Utility.getValue( trn.getQuantity());
				
				if (fruitMap.get(item) ==null) {
					
					fruitMap.put( item, number);
				}
				else {
					
					long quantity = fruitMap.get(item);
					long total = quantity + number;
					fruitMap.put( item, total);
				}				
				
			}
			String allItems="";
			for (Map.Entry<String, Long> entry : fruitMap.entrySet())
			{
			    allItems+= entry.getKey() + "-" + entry.getValue() + " ";
			};
			obj[INDEX_OF_ITEMS_RECEIVED] =allItems;	 
			obj[INDEX_OF_TOTAL_SELL] =  (long) (Utility .getValue( 
					TruckSales.recalculateTruckSales (truck)));  			
			 
			tableValueList.add(obj);
		}



		creditEntryModel = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {	

				return false ;
			}
			public Class getColumnClass(int c) {

				Class requiredClass = TruckSaleReportPage.this.columnClasses [c];
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
				if (fc.showSaveDialog(TruckSaleReportPage.this) == JFileChooser.APPROVE_OPTION) {

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
				if (fc.showSaveDialog(TruckSaleReportPage.this) == JFileChooser.APPROVE_OPTION) {

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
		columnClasses [INDEX_OF__NAME_OF_TRUCK ] = String.class;  
		columnClasses [INDEX_OF_TRUCK_RECEIVE_DATE] = Date.class;  
		columnClasses [INDEX_OF_ITEMS_RECEIVED] = String.class;  		
		columnClasses [INDEX_OF_TOTAL_SELL  ] = Long.class;
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
			TruckSaleReportPage dialog = new TruckSaleReportPage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(770, 720);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
