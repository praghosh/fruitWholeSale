package nmfc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.hibernate.Session;
import org.jdesktop.swingx.JXDatePicker;

import entities.ContainerItems;
import entities.CreditEntry;
import entities.Customer;
import entities.CustomerHalkhataEntry;
import entities.CustomerTransaction;
import entities.HalKhata;
import entities.Route;
import entities.Truck;
import entities.TruckSales; 
import nmfc.helper.CloseButton;
import nmfc.helper.DateUtility;
import nmfc.helper.PrintPdf;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import pdftables.AllCustomerDueOnDatePDF;
import pdftables.AllCustomerDueReport;
import pdftables.PrintHalkhataBalance;
import pdftables.PrintHalkhataBalanceForAllCustomers;
import persistence.HibernateUtil;


public class HalkhataEntryPage extends JFrame{

	private JPanel leftPanel;
	private JPanel rightPanel;
	private JScrollPane routeListScrollPane;
	private Route selectedRoute;
	private List<Customer> customerList;
	private JScrollPane customerListScrollPane;
	private JList routeRowList;
	private JList customerRowList;
	private String[] customerLedgerColumns;
	private JTable customerLedgerTable = new JTable ();
	private JScrollPane scrollForCustomerLedgerTable;
	private DefaultTableModel customerLedgerModel;
	private JXDatePicker fromEntryDatePicker = new JXDatePicker();
	private JXDatePicker toEntryDatePicker = new JXDatePicker();
	private Customer selectedCustomer;
	private DefaultTableModel creditEntryModel;
	private Class[] columnClasses;

	// Constants. 


	private static final int  INDEX_OF__NAME_OF_CUSTOMER =0;
	private static final int  INDEX_OF_TOTAL_DISCOUNTED_SELL =1;
	private static final int  INDEX_OF_TOTAL_NON_DISCOUNTED_SELL =2;
	private static final int  INDEX_OF_TOTAL_SELL =3;	
	private static final int  INDEX_OF_TOTAL_CASH_DEPOSITED =4;
	private static final int  INDEX_OF_TOTAL_CASH_DUE_BEFORE_HALKHATA =5;
	private static final int  INDEX_OF_TOTAL_BIG_CRATE_DUE =6;
	private static final int  INDEX_OF_TOTAL_SMALL_CRATE_DUE =7;
	private static final int  INDEX_OF_TOTAL_DISCOUNT_ELLIGIBLE =8;
	private static final int  INDEX_OF_SEPCIAL_DISCOUNT =9;
	private static final int  INDEX_OF_AMOUNT_TO_BE_PAID_AFTER_DISCOUNT =10;
	private static final int  INDEX_OF_HALKHATA_DEPOSIT_PAID =11;
	private static final int  INDEX_OF_ACUTAL_DISCOUNT_GIVEN =12;
	private static final int  INDEX_OF_FINAL_DUE_AFTER_ALL_DISCOUNT_AND_DEPOSIT =13;
	private static final int  INDEX_OF_ACUTALBALANCE_FROM_LEDGER_AFTER_HALKHATA =14;
	private static final int  INDEX_OF_IS_DEFAULTER =15;
	private static final int  INDEX_OF_HALKHATA_CLOSED =16;
	private static final int  INDEX_OF_CUSTOMER_ID_COLUMN=17;	
	private static final int  SIZE_OF_ALL_COLUMN = 18;
	JFileChooser fc = new JFileChooser();
	private JComboBox halKhataItems;
	public HalkhataEntryPage (Frame homePage, String string, boolean b) {
		//super (homePage,  string,  b);
		super (string);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 820, 550 );
		getContentPane().setBackground(new Color(230, 245, 240) );
		initializeColumnClass ();

		rightPanel = new JPanel();
		rightPanel.setLayout( null );
		rightPanel.setBounds( 10, 10, 1080, 740 );
		rightPanel.setBackground(new Color(200, 250, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);



		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 430, 760, 160, 35 );
		add(closeButton);
	}

	private void createRightPanel() {
		createHalKhataTopViewButton () ;
		createDateRangeButtons();
		createTotalSellTable();
	 
	}

	private void	createHalKhataTopViewButton () {
		JButton addHalkhataEntry = new JButton ("Add/Modify Halkhata");
		addHalkhataEntry.setBounds( 50,85,195,35);
		addHalkhataEntry.setFont(StyleItems.buttonFont);
		addHalkhataEntry.setBackground(StyleItems.componentBackgroundColor2);
		addHalkhataEntry.setToolTipText("নতুন আইটেম যোগ করুন");  
		//StyleItems.styleLabel (saveTruckEntry);
		addHalkhataEntry.addActionListener(new AddHalKhataActionListener ());
		rightPanel.add(addHalkhataEntry);


		JLabel halKhatasLabel = new JLabel ("Select a HalKhata");
		halKhatasLabel.setBounds( 50,130,145,30);		 
		StyleItems.styleComponent  (halKhatasLabel);
		rightPanel.add(halKhatasLabel);

		List <HalKhata> halkhataList = HalKhata.getHalkhataItems(); // i 
		halKhataItems = new JComboBox (halkhataList.toArray());
		StyleItems.styleComponentSmall (halKhataItems);  
		halKhataItems.setBounds( 50,165,125,30);
		halKhataItems.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				System.out.println( "********" +  halKhataItems.getSelectedItem()  );
				refreshDateRange();
				populateHalKhataTable(); 
			}
		});	 	
		rightPanel.add(halKhataItems);

		JButton openHalKhataBalnceOfCustomer = new JButton ("Open Customer Balance");
		openHalKhataBalnceOfCustomer.setBounds( 50,230,225,35);
		openHalKhataBalnceOfCustomer.setFont(StyleItems.buttonFont);
		openHalKhataBalnceOfCustomer.setBackground(StyleItems.componentBackgroundColor2);	 
		openHalKhataBalnceOfCustomer.addActionListener(new CreateHalKhataBalanceForCustomer ());
		rightPanel.add(openHalKhataBalnceOfCustomer);

		JButton printHalkhata = new JButton ("Print Halkhata Detail");
		printHalkhata.setBounds( 350,230,225,35);
		printHalkhata.setFont(StyleItems.buttonFont);
		printHalkhata.setBackground(StyleItems.componentBackgroundColor2);	 
		printHalkhata.addActionListener(new PrintHalKhataForAllCustomers ());
		rightPanel.add(printHalkhata);

		JButton saveHalkhata = new JButton ("Save Halkhata Detail");
		saveHalkhata.setBounds( 650,230,225,35);
		saveHalkhata.setFont(StyleItems.buttonFont);
		saveHalkhata.setBackground(StyleItems.componentBackgroundColor2);	 
		saveHalkhata.addActionListener(new SaveHalKhataForAllCustomers ());
		rightPanel.add(saveHalkhata);
		
		JButton updateDefaulterList = new JButton ("Update Defaulter List of Customers");
		updateDefaulterList.setBounds( 150,690,285,30);
		updateDefaulterList.setFont(StyleItems.buttonFont);
		updateDefaulterList.setBackground(StyleItems.componentBackgroundColor2);	 
		updateDefaulterList.addActionListener(new UpadateCustomerDefaulterList ());
		rightPanel.add(updateDefaulterList);

	}

	protected void refreshDateRange() {
		HalKhata item = (HalKhata) halKhataItems.getSelectedItem();
		fromEntryDatePicker.setDate(item.getStartDate());
		toEntryDatePicker.setDate(item.getEndDate());

	}

	private void refreshHalkhataItemsList() {

		List <HalKhata> halkhataItemsLists = HalKhata.getHalkhataItems();
		DefaultComboBoxModel model = new DefaultComboBoxModel( halkhataItemsLists.toArray() );
		halKhataItems.setModel( model );	 	  
		halKhataItems.repaint();
		refreshDateRange();
	}
  	 
	public void createTotalSellTable() 

	{   
		JLabel title = new JLabel ("HalKhata Report");
		title.setBounds(490, 15, 310, 45 );
		title.setFont(StyleItems.bigButtonFont); 
		title.setForeground(StyleItems.componentForegroundColor3);
		rightPanel.add(title);
		customerLedgerColumns = new String[SIZE_OF_ALL_COLUMN ]; 
		customerLedgerColumns [INDEX_OF__NAME_OF_CUSTOMER]  = "<html><br>_______<br>_________<br>Customer<br>Name<br>_________<br>_________";  ;                
		customerLedgerColumns [INDEX_OF_TOTAL_DISCOUNTED_SELL] = "<html>Total<br>Discounted <br>Sell <br>(TDS)";              
		customerLedgerColumns [INDEX_OF_TOTAL_NON_DISCOUNTED_SELL] = "<html>Total<br>Non Discntd<br>Sell ";         
		customerLedgerColumns [INDEX_OF_TOTAL_SELL] = "<html>Total<br> Sell ";
		customerLedgerColumns [INDEX_OF_TOTAL_CASH_DEPOSITED] = "<html>Total<br> Cash <br>Deposited <br> ";
		customerLedgerColumns [INDEX_OF_TOTAL_CASH_DUE_BEFORE_HALKHATA ]= "<html>Total<br>Cash Due <br>   <b>(TCD)";  ;                   
		customerLedgerColumns [INDEX_OF_TOTAL_BIG_CRATE_DUE ]= "<html>Total <br>Big <br> Crate <br> Due";             
		customerLedgerColumns [INDEX_OF_TOTAL_SMALL_CRATE_DUE ]= "<html>Total <br>Small <br> Crate <br> Due";            
		customerLedgerColumns [INDEX_OF_TOTAL_DISCOUNT_ELLIGIBLE] = "<html>Discount<br>Elligible <br>TDSXrate<br>(DE)";
		customerLedgerColumns [INDEX_OF_SEPCIAL_DISCOUNT] = "<html>Special<br>Discount <br>(SPD) ";
		customerLedgerColumns [INDEX_OF_AMOUNT_TO_BE_PAID_AFTER_DISCOUNT] = "<html>Amount<br>Payable <br>after Discount<br>TCD-DE-SPD<br>ATBP";  ; 
		customerLedgerColumns [INDEX_OF_HALKHATA_DEPOSIT_PAID] ="<html>Halkhata<br> Cash <br>Deposited <br>(HCD)";
		customerLedgerColumns [INDEX_OF_IS_DEFAULTER] = "<html>Is<br>  Defaulter <br>";
		customerLedgerColumns [INDEX_OF_FINAL_DUE_AFTER_ALL_DISCOUNT_AND_DEPOSIT] = "<html>Final<br>Balance<br>After HalKhata";                   
		customerLedgerColumns [INDEX_OF_HALKHATA_CLOSED] = "<html>Halkhata<br>Closed,br>";
		customerLedgerColumns [INDEX_OF_ACUTAL_DISCOUNT_GIVEN] = "<html>Actual Discount <br> Given <br>" ;
		customerLedgerColumns [INDEX_OF_ACUTALBALANCE_FROM_LEDGER_AFTER_HALKHATA ] = "<html><After> Balance <br> From Halkhata <br>" ;

		customerLedgerColumns [INDEX_OF_CUSTOMER_ID_COLUMN]= "<html>ID<br> ";  ;	                        


		SimpleHeaderRenderer headerRenderForTRuckSale = new SimpleHeaderRenderer();
		headerRenderForTRuckSale.setFont(new Font("Consolas", Font.BOLD, 12)); 
		customerLedgerTable.getTableHeader().setDefaultRenderer(headerRenderForTRuckSale);
		customerLedgerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleTableMediumFont(customerLedgerTable);
		scrollForCustomerLedgerTable  = new JScrollPane(customerLedgerTable); 
		populateHalKhataTable();
		scrollForCustomerLedgerTable.setBounds( 4, 280, 1055, 370 );
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



	private void populateHalKhataTable() {

		Date startDate = fromEntryDatePicker.getDate();
		Date endDate = toEntryDatePicker.getDate();
		List <Customer> customerList = Customer.getCustomerLists() ;
		HalKhata halkhata = (HalKhata) halKhataItems.getSelectedItem();
		if (halkhata==null) return;

		ArrayList<Object[]> tableValueList = new ArrayList <Object []>();
		long totalBigCrateForAll=0;
		long totalSmallCrateForAll=0;
		long totalCashForAll =0;
		long totalCashAfterDiscountForAll =0;
		long totalElligibleDiscountForAll =0;
		long totalActualDiscountForAll =0;
		long totalDiscountSellForAll =0;
		long totalSellForAll =0;
		long totalDepositInHalkhata =0;
		long totalDepositMadeForAll=0;
		long specialDiscountForAll=0;
		long actualDiscount=0;;
		long totalNonDiscountSellForAll =0;
		long totalDepositForAll =0;
		long totalFinalBalanceFromLedgerForAll=0;
		long totalFinalDueAfterHalkhataForAll=0;
		for (Customer customer: customerList) {
			Object[] obj = new Object[SIZE_OF_ALL_COLUMN ]; // Last One for Total

			obj [INDEX_OF_CUSTOMER_ID_COLUMN] = customer.getId();
			obj [INDEX_OF__NAME_OF_CUSTOMER] =  customer.getName();
			CustomerHalkhataEntry  custHalkhata = 
					CustomerHalkhataEntry.getCustomerHalkhataEntry(customer, halkhata);	
			float discountPercentGiven;
			long discountedSell, nonDiscountedSell,totalDepositMade;
			long eligibleDiscount, totalCashDue, smallCrateDue, bigCrateDue;
			long specialDiscount, cashDepositInHalkhata, bigCrateDepositInHalkhata;
			long smallCrateDepositInHalkhata, cashDueAfterDiscount, finalDueAfterHalkhata;
			String halkhataClosed = "No";
			if (custHalkhata==null) {
				halkhataClosed = "No";
				discountedSell = (long) TruckSales.getTotalDiscountedSales 
						(customer, startDate, endDate );
				nonDiscountedSell = (long) TruckSales.getTotalNonDiscountedSales 
						(customer, startDate, endDate );
				totalDepositMade = (long) CreditEntry.getTotalDepositForCustomerBeforeDate 
						(customer, startDate, endDate);
				discountPercentGiven = Utility.getValue (customer.getDiscountEligible());
				if (discountPercentGiven==0) discountPercentGiven = 1; // If not defined then take as 1 %
				actualDiscount=0;
				eligibleDiscount = (long)  (discountedSell * discountPercentGiven *0.01);
				totalCashDue =0;
				smallCrateDue=0;
				bigCrateDue =0;
				specialDiscount = 0;	
				cashDepositInHalkhata = 0;
				java.util.List <CreditEntry> depositList = CreditEntry.getCreditEntryListOnDate
						(customer, endDate);
				if (depositList!=null && depositList.size() >0) {
					cashDepositInHalkhata = (long) Utility.getValue (depositList.get(0).getCashDeposit()) ;
				}


				bigCrateDepositInHalkhata=0;
				smallCrateDepositInHalkhata =0;
				java.util.List lastBlance = CustomerTransaction.getBalanceBeforeDate(customer, endDate);
				if (lastBlance!=null && lastBlance.size()!=0) {		
					totalCashDue =(long) Utility.getValue( (Float) lastBlance.get(2)); 							
					bigCrateDue = (long)lastBlance.get(1);
					smallCrateDue = (long) lastBlance.get(0);

				}
				cashDueAfterDiscount = totalCashDue - eligibleDiscount ;
				finalDueAfterHalkhata = totalCashDue - cashDepositInHalkhata - actualDiscount;	

			}

			else {
				halkhataClosed = "Yes"; 
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
				if (custHalkhata.getActualTotalDiscount()!=null) {
					actualDiscount =  custHalkhata.getActualTotalDiscount();

				}
				else {

					// This is done because acutal Discount was not entered for all customer.
					actualDiscount =  eligibleDiscount + specialDiscount;
				}


				cashDueAfterDiscount = totalCashDue - eligibleDiscount ;
				finalDueAfterHalkhata = totalCashDue - cashDepositInHalkhata - actualDiscount;	
			}

			long finalBalanceFromLedger = (long) (Utility.getValue ( CustomerTransaction.getLasCashtBalanceOnDate (  customer,endDate)));
			totalFinalBalanceFromLedgerForAll+=finalBalanceFromLedger;
			totalFinalDueAfterHalkhataForAll+=finalDueAfterHalkhata;
			obj[INDEX_OF_TOTAL_DISCOUNTED_SELL]  = discountedSell ;
			obj[INDEX_OF_TOTAL_NON_DISCOUNTED_SELL] =nonDiscountedSell;	
			obj[INDEX_OF_TOTAL_CASH_DEPOSITED] =totalDepositMade;
			obj [INDEX_OF_TOTAL_DISCOUNT_ELLIGIBLE ] =  eligibleDiscount; 
			obj[INDEX_OF_TOTAL_SELL] = discountedSell + nonDiscountedSell;  

			obj[INDEX_OF_TOTAL_SMALL_CRATE_DUE] = smallCrateDue;
			obj[INDEX_OF_TOTAL_BIG_CRATE_DUE] = bigCrateDue;

			obj[INDEX_OF_TOTAL_CASH_DUE_BEFORE_HALKHATA] = totalCashDue; 
			obj[INDEX_OF_AMOUNT_TO_BE_PAID_AFTER_DISCOUNT] = totalCashDue - eligibleDiscount;
			obj[INDEX_OF_HALKHATA_DEPOSIT_PAID] = cashDepositInHalkhata; 
			obj[INDEX_OF_FINAL_DUE_AFTER_ALL_DISCOUNT_AND_DEPOSIT] = finalDueAfterHalkhata;
			obj [INDEX_OF_IS_DEFAULTER] = finalDueAfterHalkhata > 10;
			obj [INDEX_OF_SEPCIAL_DISCOUNT] = specialDiscount;
			obj [INDEX_OF_HALKHATA_CLOSED] = halkhataClosed;
			obj [INDEX_OF_ACUTAL_DISCOUNT_GIVEN] = actualDiscount;
			obj [INDEX_OF_ACUTALBALANCE_FROM_LEDGER_AFTER_HALKHATA] = finalBalanceFromLedger;



			tableValueList.add(obj);

			// Create grand Total
			totalDiscountSellForAll+=discountedSell;
			totalNonDiscountSellForAll+=nonDiscountedSell;
			totalSellForAll+=discountedSell + nonDiscountedSell;
			totalElligibleDiscountForAll+=eligibleDiscount;
			totalCashForAll+=totalCashDue;
			totalBigCrateForAll+=bigCrateDue;
			totalSmallCrateForAll+=smallCrateDue;
			totalDepositInHalkhata+=cashDepositInHalkhata;
			totalDepositMadeForAll+=totalDepositMade;
			specialDiscountForAll+=specialDiscount;
			totalActualDiscountForAll+=actualDiscount;
		}

		// Add Total;

		// Add Total;
		Object[] obj = new Object[SIZE_OF_ALL_COLUMN ]; // Last One for Total 
		obj [INDEX_OF__NAME_OF_CUSTOMER] =  "Total";
		obj[INDEX_OF_TOTAL_DISCOUNTED_SELL]  = totalDiscountSellForAll ;
		obj[INDEX_OF_TOTAL_NON_DISCOUNTED_SELL] =totalNonDiscountSellForAll;	
		//	obj[INDEX_OF_TOTAL_CASH_DEPOSITED] =totalDepositMade;
		obj [INDEX_OF_TOTAL_DISCOUNT_ELLIGIBLE ] =  totalElligibleDiscountForAll; 
		obj[INDEX_OF_TOTAL_SELL] = totalSellForAll;  

		obj[INDEX_OF_TOTAL_SMALL_CRATE_DUE] = totalSmallCrateForAll;
		obj[INDEX_OF_TOTAL_BIG_CRATE_DUE] = totalBigCrateForAll;

		obj[INDEX_OF_TOTAL_CASH_DUE_BEFORE_HALKHATA] = totalCashForAll; 
		obj[INDEX_OF_HALKHATA_DEPOSIT_PAID] = totalDepositInHalkhata; 
		obj[INDEX_OF_TOTAL_CASH_DEPOSITED] = totalDepositMadeForAll;
		obj[INDEX_OF_SEPCIAL_DISCOUNT] =  specialDiscountForAll;
		obj[INDEX_OF_ACUTAL_DISCOUNT_GIVEN] =  totalActualDiscountForAll;
		obj [INDEX_OF_ACUTALBALANCE_FROM_LEDGER_AFTER_HALKHATA] = totalFinalBalanceFromLedgerForAll;				 
		obj[INDEX_OF_FINAL_DUE_AFTER_ALL_DISCOUNT_AND_DEPOSIT]=totalFinalDueAfterHalkhataForAll;

		/*
				obj[INDEX_OF_AMOUNT_TO_BE_PAID_AFTER_DISCOUNT] = totalCashDue - eligibleDiscount;

				obj[INDEX_OF_FINAL_DUE_AFTER_ALL_DISCOUNT_AND_DEPOSIT] = finalDueAfterHalkhata;
				obj [INDEX_OF_IS_DEFAULTER] = finalDueAfterHalkhata > 10;
				obj [INDEX_OF_SEPCIAL_DISCOUNT] = specialDiscount;
		 */


		tableValueList.add(obj);

		creditEntryModel = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {	

				//	return column==INDEX_OF_OK_FOR_DISCOUNT ;

				return false;
			}
			public Class getColumnClass(int c) {

				Class requiredClass = HalkhataEntryPage.this.columnClasses [c];
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
		customerLedgerTable.removeColumn( customerLedgerTable.getColumnModel().getColumn(INDEX_OF_CUSTOMER_ID_COLUMN)); //hiding
		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer();
		// Color the comiited data as green and otherwise red
		customerLedgerTable.setDefaultRenderer( Boolean.class, customTableCellRenderer );

	}

	//  Highlight the color of rows those are checked

	public class CustomTableCellRenderer extends JCheckBox implements   TableCellRenderer 
	{
		private JCheckBox checkBox = this;

		/**
		 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent
		 * (javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
		public Component getTableCellRendererComponent(
				JTable tbl,
				Object value,
				boolean isSelected,
				boolean hasFocus,
				int row,
				int column) 
		{
			checkBox.setHorizontalAlignment( JCheckBox.CENTER );
			JCheckBox b = new JCheckBox();
			//	    b.setPressedIcon(pressedIcon)   // To be used later
			//	    b.setIcon(defaultIcon);
			//	    b.setDisabledIcon(disabledIcon)

			boolean val = false;

			if ( value ==null) {
				checkBox.setSelected(false);
			} else {

				try {
					val = ((Boolean) value).booleanValue();
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
				checkBox.setSelected(val);
			}
			if  ( val) {

				checkBox.setBackground(StyleItems.darkRed);
			}
			else {

				checkBox.setBackground(Color.green);

			}

			checkBox.setSelected(val);
			return checkBox;
		}
	}


	public class ApplyHalkhataDiscount   implements ActionListener {


		public void actionPerformed(ActionEvent evt) {


			Date startDate = fromEntryDatePicker.getDate();


			ToastMessage toastMessage = new ToastMessage("Halkhata To be Implemented",3000);
			toastMessage.setVisible(true);

		}

	}

	private void initializeColumnClass () {

		columnClasses = new Class [SIZE_OF_ALL_COLUMN ];  
		columnClasses [INDEX_OF__NAME_OF_CUSTOMER ] = String.class;  ;                
		columnClasses [INDEX_OF_TOTAL_DISCOUNTED_SELL ] = Long.class;;            
		columnClasses [INDEX_OF_TOTAL_NON_DISCOUNTED_SELL ] = Long.class;        
		columnClasses [INDEX_OF_TOTAL_SELL ] = Long.class;	                    
		columnClasses [INDEX_OF_TOTAL_CASH_DUE_BEFORE_HALKHATA  ] = Long.class;
		columnClasses [INDEX_OF_SEPCIAL_DISCOUNT]  = Long.class;
		columnClasses [INDEX_OF_TOTAL_BIG_CRATE_DUE ] = Long.class;             
		columnClasses [INDEX_OF_TOTAL_SMALL_CRATE_DUE ] = Long.class;           
		columnClasses [INDEX_OF_TOTAL_DISCOUNT_ELLIGIBLE ] = Long.class;        
		columnClasses [INDEX_OF_AMOUNT_TO_BE_PAID_AFTER_DISCOUNT] = Long.class; 
		columnClasses [INDEX_OF_TOTAL_CASH_DEPOSITED] = Long.class; 
		columnClasses [INDEX_OF_HALKHATA_DEPOSIT_PAID] = Long.class; 
		columnClasses [INDEX_OF_FINAL_DUE_AFTER_ALL_DISCOUNT_AND_DEPOSIT] = Long.class; 
		columnClasses [INDEX_OF_ACUTALBALANCE_FROM_LEDGER_AFTER_HALKHATA ] = Long.class; 

		columnClasses [INDEX_OF_ACUTAL_DISCOUNT_GIVEN] = Long.class; 		
		columnClasses [INDEX_OF_IS_DEFAULTER] =Boolean.class;                  


	}
	private void createDateRangeButtons() {
		JLabel fromDateLabel = new JLabel ("HalKhata Perid --   From:");
		fromDateLabel.setBounds( 200, 165,220,25);
		StyleItems.styleComponentSmall (fromDateLabel);
		rightPanel.add(fromDateLabel);


		if (halKhataItems.getSelectedItem()!=null) {
			HalKhata item = (HalKhata) halKhataItems.getSelectedItem();
			fromEntryDatePicker.setDate(item.getStartDate());
		}

		StyleItems.styleDatePickerSmall(fromEntryDatePicker);
		fromEntryDatePicker.setBounds( 430, 165,108,20);
		fromEntryDatePicker.setFormats("dd/MM/yyyy");
		fromEntryDatePicker.setEditable(false); 
		/*
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
		 */
		rightPanel.add (fromEntryDatePicker);

		JLabel toDateLabel = new JLabel ("To:");
		toDateLabel.setBounds( 600, 165,60,25);
		StyleItems.styleComponentSmall (toDateLabel);
		rightPanel.add(toDateLabel);

		if (halKhataItems.getSelectedItem()!=null) {
			HalKhata item = (HalKhata) halKhataItems.getSelectedItem();
			toEntryDatePicker.setDate(item.getEndDate());
		}
		StyleItems.styleDatePickerSmall(toEntryDatePicker);
		toEntryDatePicker.setBounds( 710, 165,108,20);
		toEntryDatePicker.setFormats("dd/MM/yyyy");	
		toEntryDatePicker.setEditable(false); 
		rightPanel.add (toEntryDatePicker);
		/*
		PropertyChangeListener toDateListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**receiveDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("toEntryDatePicker.isEditValid()  " +toEntryDatePicker.isEditValid());
				if ((toEntryDatePicker.getDate()!=null) && toEntryDatePicker.isEditValid()) {
					System.out.println(toEntryDatePicker.getDate());
					populateHalKhataTable(); 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		toEntryDatePicker.addPropertyChangeListener(toDateListener);
		 */

	}

	public class AddHalKhataActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog page=new HalkhataCreationPage( HalkhataEntryPage.this, "Halkhata Balance For a Customer");
			page.setSize(590, 555);
			page.setVisible(true);
			refreshHalkhataItemsList();
			populateHalKhataTable() ;

			//	populateTruckEntryTable();
			//	populateTruckSaleTable();
			//	resetValues (); // Not required
		}
	}
	/**
	 * 
	 * @author  prabir
	 *
	 */
	public class UpadateCustomerDefaulterList implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to update Defaulter List?","Warning",dialogButton);
			if(dialogResult == JOptionPane.NO_OPTION){
				return;
			}
			HalKhata halkhata = (HalKhata) halKhataItems.getSelectedItem();
			if (halkhata==null) {
				ToastMessage toastMessage = new ToastMessage("No Halkhata Selected",3000, Color.RED);
				toastMessage.setVisible(true);
				return;
			}
			Date endDate = toEntryDatePicker.getDate();
			List <Customer> customerList = Customer.getCustomerLists() ;
			int totalDefaulter =0;
			for (Customer customer: customerList )  {
				CustomerHalkhataEntry  custHalkhata = 
						CustomerHalkhataEntry.getCustomerHalkhataEntry(customer, halkhata);	
				long actualDiscount, totalCashDue, cashDepositInHalkhata,finalDueAfterHalkhata; 
				if (custHalkhata==null) {
					actualDiscount=0;				 
					totalCashDue =0;					 					 
					cashDepositInHalkhata = 0;
					java.util.List <CreditEntry> depositList = CreditEntry.getCreditEntryListOnDate
							(customer, endDate);
					if (depositList!=null && depositList.size() >0) {
						cashDepositInHalkhata = (long) Utility.getValue (depositList.get(0).getCashDeposit()) ;
					}

					java.util.List lastBlance = CustomerTransaction.getBalanceBeforeDate(customer, endDate);
					if (lastBlance!=null && lastBlance.size()!=0) {								totalCashDue =(long) Utility.getValue( (Float) lastBlance.get(2)); 							

					}
				}

				else {

					long eligibleDiscount = Utility.getValue(custHalkhata.getEligibleDiscount());
					totalCashDue =Utility.getValue(custHalkhata.getCashDue());				 
					long specialDiscount = Utility.getValue(custHalkhata.getSpecialDiscount());	
					cashDepositInHalkhata = Utility.getValue(custHalkhata.getCashDepositInHalkhata());					 				 			 
					if (custHalkhata.getActualTotalDiscount()!=null) {
						actualDiscount =  custHalkhata.getActualTotalDiscount();

					}
					else {

						// This is done because acutal Discount was not entered for all customer.
						actualDiscount =  eligibleDiscount + specialDiscount;
					}

				}	

				finalDueAfterHalkhata = totalCashDue - cashDepositInHalkhata - actualDiscount;	
 
				Session session =null;
				try {
					session = HibernateUtil.getSessionFactory().openSession();
					session.beginTransaction();
					Serializable id =  customer.getId();
					Object persistentInstance = session.load(Customer.class, id);
					customer = (Customer)persistentInstance; // loading from persistence to modify;
					if (finalDueAfterHalkhata>10) {
						totalDefaulter++;
						customer.setIsDefaulter(true);
					}
					else {
						customer.setIsDefaulter(false);
					}
					
 					session.save(customer);
					session.getTransaction().commit();					 
				}
				catch (Exception ex)
				{
					 ex.printStackTrace();
				}

				finally {
					if(session!=null && session.isOpen()){
						session.close();
					}
				}
				
			}
		 
			ToastMessage toastMessage = new ToastMessage(totalDefaulter + " Customers Are set as Defaulter",3000);
			toastMessage.setVisible(true);

			  
			
		}
	}


	public class CreateHalKhataBalanceForCustomer implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			HalKhata item = (HalKhata) halKhataItems.getSelectedItem();
			JDialog page=new CustomerHalkhataBalancePage ( HalkhataEntryPage.this, 
					"Halkhata Balance For a Customer",  true,
					item);
			page.setSize(580, 700);
			page.setVisible(true);
			refreshHalkhataItemsList();
			populateHalKhataTable() ;
		}
	}

	public class SaveHalKhataForAllCustomers implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			HalKhata halkhata = (HalKhata) halKhataItems.getSelectedItem();
			try {
				if (fc.showSaveDialog(HalkhataEntryPage.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}					 
					PrintHalkhataBalanceForAllCustomers.createAllCustomerHalkhataPDF  ( finalBackupPath, halkhata);
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

			HalKhata halkhata = (HalKhata) halKhataItems.getSelectedItem();
			try {
				if (fc.showSaveDialog(HalkhataEntryPage.this) == JFileChooser.APPROVE_OPTION) {

					String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
					String finalBackupPath = pdfReportPath;

					if (!pdfReportPath.endsWith(".pdf") )
					{
						finalBackupPath = pdfReportPath + 	 ".pdf";
					}					 
					PrintHalkhataBalanceForAllCustomers.createAllCustomerHalkhataPDF  ( finalBackupPath, halkhata);

					PrintPdf printJob = new PrintPdf(finalBackupPath, "HalKhata Job", false);
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
	 * Launch the application            
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			HalkhataEntryPage dialog = new HalkhataEntryPage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(770, 720);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
