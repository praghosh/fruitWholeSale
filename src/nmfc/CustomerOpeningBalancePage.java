package nmfc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.NumberFormatter;

import org.hibernate.Session;
import org.jdesktop.swingx.JXDatePicker;

import entities.CommitData;
import entities.CreditEntry;
import entities.Customer;
import entities.CustomerOpeningBalance;
import entities.CustomerTransaction;
import entities.Truck;
import entities.TruckEntry;
import entities.TruckSales;

import nmfc.helper.CloseButton;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import persistence.HibernateUtil;

public class CustomerOpeningBalancePage extends JDialog {

	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JPanel rightTopInfoPanel = new JPanel();

	private JScrollPane customerListScrollPane =  new JScrollPane();
	private List<Customer> customerList ;
	public  JList rowListOfCustomer;
	private String[] customerCreditColumns;
	private JTable customerOpeningTable =  new JTable();
	private JScrollPane scrollForCreditEntryTable;
	private Customer selectedCustomer;
	private DefaultTableModel creditEntryModel;
	private JXDatePicker creditEntryDatePicker = new JXDatePicker();
	private JFormattedTextField smallCrateDue = new JFormattedTextField();
	private JFormattedTextField bigCrateDue = new JFormattedTextField();
	private Session session;
	private JXDatePicker fromEntryDatePicker = new JXDatePicker();
	private JXDatePicker toEntryDatePicker = new JXDatePicker();
	private static final int INDEX_OF_ID =0;
	private static final int INDEX_OF__OPENING_DATE =1;
	private static final int INDEX_OF_SMALL_CRATE_DUE =2;
	private static final int INDEX_OF_BIG_CRATE_DUE=3;
	private static final int INDEX_OF_CASH_DUE =4; 
	private static final int INDEX_OF_DATA_COMMITTED =5;
	private static final int SIZE_OF_TABLE_COLUMN =6;
	private Class columnClasses[];
	private Date startDate;
	private Date endDate;
	private List <Object []> tableValueList = new ArrayList <Object []> ();
	private JFormattedTextField cashDueText = new JFormattedTextField ();


	public CustomerOpeningBalancePage (Frame homePage, String string, boolean b) {
		super (homePage,  string,  b);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 800, 420 );
		getContentPane().setBackground(new Color(230, 245, 240) );
		
		UIManager.put("ToolTip.font",
				new FontUIResource("Siyamrupali_1_01", Font.BOLD, 14));

		initializeColumnClass ();
		initializeColumnHeaders ();

		leftPanel.setLayout( null );
		leftPanel.setBounds( 10, 10, 170, 460 );
		leftPanel.setBackground(new Color(200, 230, 210) );
		leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createLeftPanel();
		add(leftPanel);


		rightTopInfoPanel.setLayout( null );
		rightTopInfoPanel.setBounds( 190, 10, 600, 165 );
		rightTopInfoPanel.setBackground(new Color(200, 230, 210) );
		rightTopInfoPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightTopInfoPanel();
		add(rightTopInfoPanel);

		rightPanel.setLayout( null );
		rightPanel.setBounds( 190, 180, 600, 290 );
		rightPanel.setBackground(new Color(200, 230, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);

		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 345, 490, 160, 35 );
		add(closeButton);
	}

	private void createRightTopInfoPanel() {
		JLabel dateLabel = new JLabel ("Opening Date");
		dateLabel.setBounds( 10,10,90,20);
		StyleItems.styleComponentSmall (dateLabel);
		rightTopInfoPanel.add(dateLabel);

		creditEntryDatePicker.setDate(new Date());
		StyleItems.styleDatePickerSmall(creditEntryDatePicker);
		creditEntryDatePicker.setBounds( 10,30,108,20);
		creditEntryDatePicker.setFormats("dd/MM/yyyy");		
		rightTopInfoPanel.add (creditEntryDatePicker);


		JLabel smallCrateDueLabel = new JLabel ("Small Crate Due");
		smallCrateDueLabel.setBounds( 140,10,130,20);
		StyleItems.styleComponentSmall (smallCrateDueLabel);
		rightTopInfoPanel.add(smallCrateDueLabel);

		StyleItems.styleComponentSmall (smallCrateDue);
		smallCrateDue.setValue(new Long(0));
		smallCrateDue.setBounds( 140,30,85,20);
		rightTopInfoPanel.add(smallCrateDue);

		JLabel bigCrateDueLabel = new JLabel ("Big Crate Due");
		bigCrateDueLabel.setBounds ( 290,10,130,20);
		StyleItems.styleComponentSmall (bigCrateDueLabel);
		rightTopInfoPanel.add(bigCrateDueLabel);

		StyleItems.styleComponentSmall (bigCrateDue);
		bigCrateDue.setValue(new Long(0));
		bigCrateDue.setBounds( 290,30,85,20);
		rightTopInfoPanel.add(bigCrateDue);


		JLabel cashDueLebel = new JLabel ("Cash Due");
		cashDueLebel.setBounds( 440,10,100,20);
		StyleItems.styleComponentSmall (cashDueLebel);
		cashDueLebel.setForeground(Color.BLACK);
		rightTopInfoPanel.add(cashDueLebel);

		StyleItems.styleComponentSmall (cashDueText);
		cashDueText.setBounds( 440,30,125,20);
		cashDueText.setValue(new Float(0.0));
		rightTopInfoPanel.add(cashDueText);

		JButton saveCreditEntry = new JButton ("Update");
		saveCreditEntry.setBounds( 250,100,125,25);
		saveCreditEntry.setFont(StyleItems.buttonFont);
		//StyleItems.styleLabel (saveTruckEntry);
		saveCreditEntry.addActionListener(new AddCustomerOpeingBalance ());
		saveCreditEntry.setToolTipText("Click to add");
		rightTopInfoPanel.add(saveCreditEntry);



	}

	private void createRightPanel() {

		createCustomerOpeningTable();

		createModifyEntyButton();

	}

	private void createLeftPanel() {
		JLabel selectCustLabel = new JLabel ("Select Customer");
		selectCustLabel.setBounds( 20,10,135,20);		
		StyleItems.styleLabel (selectCustLabel);
		leftPanel.add (selectCustLabel);	
		createCustomerList();
		leftPanel.add(customerListScrollPane);

	}

	private void createCustomerList() {

		customerListScrollPane.setBounds( 15, 50, 150, 310 );
		customerList = Customer.getCustomerLists();
		rowListOfCustomer = new JList(customerList.toArray());
		rowListOfCustomer.setVisibleRowCount(12);
		rowListOfCustomer.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = rowListOfCustomer.getSelectedIndex();
				if (idx != -1)
				{
					if (!le.getValueIsAdjusting()) {
						System.out.println("Current selection: " + customerList.get(idx));
						resetOpeningValues();
						populateOpeningBalanceTable();
					}

				}
				else
					System.out.println("Please choose a Customer.");
			}

			 

		}); 
		customerListScrollPane.setViewportView(rowListOfCustomer);
		customerListScrollPane.revalidate();
		customerListScrollPane.repaint();
		rowListOfCustomer.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

		//	listScrollPane.add(rowList);
		System.out.println("Size **= " +  customerList.size());

	}





	protected void resetOpeningValues() {
		cashDueText.setValue(new Float(0.0));
		bigCrateDue.setValue(new Long(0));
		smallCrateDue.setValue(new Long(0));
	}

	public void createCustomerOpeningTable() 

	{  
		SimpleHeaderRenderer headerRenderForCreditEntry = new SimpleHeaderRenderer();
		headerRenderForCreditEntry.setFont(new Font("Consolas", Font.BOLD, 11)); 
		customerOpeningTable.getTableHeader().setDefaultRenderer(headerRenderForCreditEntry);
		customerOpeningTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleComponent(customerOpeningTable);
		scrollForCreditEntryTable  = new JScrollPane(customerOpeningTable); 
		populateOpeningBalanceTable();
		scrollForCreditEntryTable.setBounds( 10, 10, 585, 230 );
		customerOpeningTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (customerOpeningTable.getSelectedRow() > -1) {
					// print first column value from selected row
					//  System.out.println("Value = " + truckEntryTable.getValueAt(truckSaleTable.getSelectedRow(), 0).toString());
					//   fillCustomerEntryItems();
				}
			}
		});
		rightPanel.add(scrollForCreditEntryTable);

	}

	private void createModifyEntyButton() {
		JButton modifyEntryButton = new JButton("Commit Opening Balalce");
		modifyEntryButton.setToolTipText( "লেজার আপদেট করুন ");
		modifyEntryButton.setBounds( 210, 260, 160, 20 );
		modifyEntryButton.addActionListener (new CommitOpeningBalanceListener ());
		rightPanel.add(modifyEntryButton);
	}






	private void initializeColumnHeaders () {

		customerCreditColumns = new String[SIZE_OF_TABLE_COLUMN];
		customerCreditColumns [INDEX_OF_ID] ="ID" ;//0 (first column will be hidden)
		customerCreditColumns [INDEX_OF__OPENING_DATE] ="<html>Entry<br> Date";
		customerCreditColumns [INDEX_OF_SMALL_CRATE_DUE] ="<html> Small Crate <br>Due";
		customerCreditColumns [INDEX_OF_CASH_DUE] ="Cash Due";
		customerCreditColumns [INDEX_OF_BIG_CRATE_DUE] ="<html>Big Crate <br> Due";
		//	customerCreditColumns [INDEX_OF_SELECT_ROW] = "Select Item";
		customerCreditColumns [ INDEX_OF_DATA_COMMITTED] = "<html>Data <br> Committed";
	}

	private void initializeColumnClass () {

		columnClasses = new Class [SIZE_OF_TABLE_COLUMN];
		columnClasses [ INDEX_OF_ID ]= Long.class;               
		columnClasses [ INDEX_OF__OPENING_DATE] = Date.class;     
		columnClasses [ INDEX_OF_SMALL_CRATE_DUE] = Long.class;         
		columnClasses [ INDEX_OF_BIG_CRATE_DUE] =Long.class;            
		columnClasses [ INDEX_OF_CASH_DUE]  = Float.class;          
		columnClasses [ INDEX_OF_DATA_COMMITTED]  = Boolean.class;
	}

	/**
	 * Populates Credit Entry  Data into  table
	 * Each record is added as array to  
	 * TableModel and displayed in separate row 
	 * of the table.
	 */


	private void populateOpeningBalanceTable() {

		if (rowListOfCustomer.getSelectedIndex()==-1)
			return;
		selectedCustomer = (Customer)rowListOfCustomer.getSelectedValue();
		startDate = fromEntryDatePicker.getDate();
		endDate = toEntryDatePicker.getDate();
		List  customerCreditEntryList = CustomerOpeningBalance.getOpeningBalance(selectedCustomer);		
		tableValueList = new ArrayList <Object []>();
		for (Object s : customerCreditEntryList) {
			Object[] obj = new Object[SIZE_OF_TABLE_COLUMN];
			CustomerOpeningBalance credtEntry = (CustomerOpeningBalance) s;
			obj[INDEX_OF_ID] = credtEntry.getId();
			obj[INDEX_OF__OPENING_DATE] = credtEntry.getEntryDate();
			obj[INDEX_OF_SMALL_CRATE_DUE] = credtEntry.getSmallCrateDue();
			obj[INDEX_OF_BIG_CRATE_DUE] = credtEntry.getBigCrateDue();
			obj[INDEX_OF_CASH_DUE] = credtEntry.getCashDue();
			Boolean val = credtEntry.istDataCommited();
			boolean dataCommitted = (val!=null)? (boolean) val: false;;			 
			obj[INDEX_OF_DATA_COMMITTED] = dataCommitted;			
			tableValueList.add(obj);
		}



		creditEntryModel = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {	
				Object [] obj  =  (Object []) tableValueList.get(row);
				Object value = obj [INDEX_OF_DATA_COMMITTED ];
				boolean editable=true;
				if (value!=null && value instanceof Boolean) {
					editable = ! (boolean) value;

				}
				return ( (column >1) && (column<INDEX_OF_DATA_COMMITTED) && ( editable)) ;
			}
			public Class getColumnClass(int c) {
				return CustomerOpeningBalancePage.this.columnClasses [c];
			}
		};

		//	List  customerCreditEntryList = CreditEntry.getCreditEntryList(selectedCustomer,startDate , endDate);		
		creditEntryModel.setColumnIdentifiers(customerCreditColumns);		 
		for (Object s : tableValueList) {

			Object [] objArr =  (Object []) s;
			creditEntryModel.addRow(objArr);
		}

		customerOpeningTable.setModel(creditEntryModel);

		// First column is removed to hide ID of the entry.
		// This id will be used to update or delete the entry
		customerOpeningTable.removeColumn( customerOpeningTable.getColumnModel().getColumn(INDEX_OF_ID)); 

		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer();
		customerOpeningTable.setDefaultRenderer( Boolean.class, customTableCellRenderer );



	}

	//  Heilight the color of rows those are committed

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

				checkBox.setBackground(Color.green);
				checkBox.setBackground(Color.green);
			}
			else {

				checkBox.setBackground(StyleItems.darkRed);

			}

			checkBox.setSelected(val);
			return checkBox;
		}
	}

	class AddCustomerOpeingBalance implements ActionListener {


		@Override
		public void actionPerformed(ActionEvent e) {

			if (rowListOfCustomer.getSelectedIndex()==-1){
				ToastMessage toastMessage = new ToastMessage("Please select a customer",3000);
				toastMessage.setVisible(true);
				return;				
			}
			
			
			CustomerOpeningBalance customerOpening = new CustomerOpeningBalance();
			populateCustomerOpeningBalanceData(customerOpening);
			if (CustomerOpeningBalance.saveCustomerOpeningBalance (customerOpening)) {
				ToastMessage toastMessage = new ToastMessage("Opening Balance Added Successfully",3000);
				toastMessage.setVisible(true);					
			}
			else 

			{   
				ToastMessage toastMessage = new ToastMessage("Opening Balance Not Added",3000);
				toastMessage.setVisible(true);
				return;
			}

			populateOpeningBalanceTable();
			
		}


	}
	


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			CustomerOpeningBalancePage dialog = new CustomerOpeningBalancePage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(810, 580);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public void populateCustomerOpeningBalanceData(CustomerOpeningBalance customerOpening) {
		customerOpening.setCustomer(  (Customer) rowListOfCustomer.getSelectedValue());
		customerOpening.setEntryDate(creditEntryDatePicker.getDate());

		long crateDueVal = Utility.getValue( (Long) smallCrateDue.getValue()); 
		customerOpening.setSmallCrateDue(crateDueVal) ;	

		long bigCrateDueVal = Utility.getValue( (Long) bigCrateDue.getValue()); 
		customerOpening.setBigCrateDue(bigCrateDueVal) ;

		float cashDueVal = Utility.getValue( (Float) cashDueText.getValue()); 
		customerOpening.setCashDue(cashDueVal) ;

		customerOpening.setDataCommited(false);

	}
	


	public class CommitOpeningBalanceListener implements ActionListener {
 			public void actionPerformed(ActionEvent evt)
			{ 
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Commit All Selected?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					if (rowListOfCustomer.getSelectedIndex()==-1){
						ToastMessage toastMessage = new ToastMessage("Please select a customer",3000);
						toastMessage.setVisible(true);
						return;				
					}
					
					if (creditEntryModel.getRowCount() > 0 && customerOpeningTable.getSelectedRow() != -1 )
					{
						System.out.println ( " Selcted Row ==  " + customerOpeningTable.getSelectedRow() );
						Object dataCommitted = creditEntryModel.getValueAt
								(customerOpeningTable.getSelectedRow(), INDEX_OF_DATA_COMMITTED); 
						boolean isDataCommitted = Utility.getValue(((Boolean ) dataCommitted).booleanValue());						
						if (isDataCommitted) {
							System.out.println("Data already committed");
							ToastMessage toastMessage = new ToastMessage("Data already committed",3000);
							toastMessage.setVisible(true);
							return;
						}   
						Customer customer = (Customer) rowListOfCustomer.getSelectedValue();
						commitOpeningBalance (customer);
					}
					else {

						System.out.println("Please choose an item.");
						ToastMessage toastMessage = new ToastMessage("Please choose an item",3000);
						toastMessage.setVisible(true);
						return;

					}
					
					
				}
			}

		}
	public void commitOpeningBalance(Customer customer) {
		 if (CommitData.commitOpeningBalanceNewMadina (customer)) {
			 System.out.println("Data  committed");
				ToastMessage toastMessage = new ToastMessage("Data  committed successfuly",3000);
				toastMessage.setVisible(true);
		 }
		 else {
			 System.out.println("Data not committed.");
				ToastMessage toastMessage = new ToastMessage("Data not committed ",3000);
				toastMessage.setVisible(true);
				return; 
		 }
		 populateOpeningBalanceTable();
		
	} 
 

}


