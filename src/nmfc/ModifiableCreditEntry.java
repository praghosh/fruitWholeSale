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
import javax.swing.JComboBox;
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
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.JXDatePicker;

import entities.CommitData;
import entities.CreditEntry;
import entities.Customer;
import nmfc.helper.CloseButton;
import nmfc.helper.DateUtility;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;


public class ModifiableCreditEntry extends JDialog {


	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JPanel rightTopInfoPanel = new JPanel();

	private JScrollPane customerListScrollPane =  new JScrollPane();
	private List<Customer> customerList ;
	public  JList rowListOfCustomer;
	private String[] customerCreditColumns;
	private JTable creaditEntryTable =  new JTable();
	private JScrollPane scrollForCreditEntryTable;
	private Customer selectedCustomer;
	private DefaultTableModel creditEntryModel;
	private JXDatePicker creditEntryDatePicker = new JXDatePicker();
	private JFormattedTextField returnedQuantity = new JFormattedTextField();
	private JFormattedTextField bigCrateDepositText = new JFormattedTextField();
	private JFormattedTextField bigCrateIssuedText = new JFormattedTextField(); 
	private JXDatePicker fromEntryDatePicker = new JXDatePicker();
	private JXDatePicker toEntryDatePicker = new JXDatePicker();
	private static final int INDEX_OF_ID =0;
	private static final int INDEX_OF_DATE =1;
	private static final int INDEX_OF_BIG_CRATE_DEPOSIT =2;
	private static final int INDEX_OF_CRATE_DEPOSIT =3;
	private static final int INDEX_OF_CASH_DEPOSIT =4;
	private static final int INDEX_OF_BILL_NUMBER =5;	
	private static final int INDEX_OF_DATA_COMMITTED =6;
	private static final int SIZE_OF_TABLE_COLUMN =7;
	private Class columnClasses[];
	private Date startDate;
	private Date endDate;
	private List <Object []> tableValueList = new ArrayList <Object []> ();
	private NumberFormat cashFormat =  NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
	private JFormattedTextField crateDepositText = new JFormattedTextField();
	private JFormattedTextField cashDepositText = new JFormattedTextField ();
	private JFormattedTextField billNumberText = new JFormattedTextField ();
	private JComboBox itemCombo = new JComboBox();


	public ModifiableCreditEntry (Frame homePage, String string, boolean b) {
		super (homePage,  string,  b);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 800, 420 );
		getContentPane().setBackground(new Color(230, 245, 240) );

		initializeColumnClass ();
		initializeColumnHeaders ();

		leftPanel.setLayout( null );
		leftPanel.setBounds( 10, 10, 180, 460 );
		leftPanel.setBackground(new Color(200, 230, 210) );
		leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createLeftPanel();
		add(leftPanel);


		rightTopInfoPanel.setLayout( null );
		rightTopInfoPanel.setBounds( 200, 10, 770, 165 );
		rightTopInfoPanel.setBackground(new Color(200, 230, 210) );
		rightTopInfoPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightTopInfoPanel();
		add(rightTopInfoPanel);

		rightPanel.setLayout( null );
		rightPanel.setBounds( 200, 180, 770, 290 );
		rightPanel.setBackground(new Color(200, 230, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);

		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 345, 490, 160, 35 );
		add(closeButton);
	}

	private void createRightTopInfoPanel() {
		JLabel dateLabel = new JLabel ("Date");
		dateLabel.setBounds( 10,10,90,20);
		StyleItems.styleComponentSmall (dateLabel);
		rightTopInfoPanel.add(dateLabel);

		creditEntryDatePicker.setDate(new Date());
		StyleItems.styleDatePickerSmall(creditEntryDatePicker);
		creditEntryDatePicker.setBounds( 10,30,108,20);
		creditEntryDatePicker.setFormats("dd/MM/yyyy");		
		rightTopInfoPanel.add (creditEntryDatePicker);


		// Add small crate return entry

		JLabel returnedQuantityLebel = new JLabel ("Small Crate Returned");
		returnedQuantityLebel.setBounds( 140,10,140,20);
		StyleItems.styleComponentSmall (returnedQuantityLebel);
		rightTopInfoPanel.add(returnedQuantityLebel);

		StyleItems.styleComponentSmall (crateDepositText);
		crateDepositText.setBounds (140,30,135,20);
		crateDepositText.setValue(new Long(0));
		crateDepositText.setToolTipText ("Negative values will be considered as crate issue");
		rightTopInfoPanel.add(crateDepositText);

		// Add big crate return entry
		JLabel itemComboLebel = new JLabel ("Big Crate Returned");
		itemComboLebel.setBounds( 310,10,145,20);
		StyleItems.styleComponentSmall (itemComboLebel);
		rightTopInfoPanel.add(itemComboLebel);

		StyleItems.styleComponentSmall (bigCrateDepositText);
		bigCrateDepositText.setBounds(310,30,135,20) ; 
		bigCrateDepositText.setValue(new Long(0));
		bigCrateDepositText.setToolTipText ("Negative values will be considered as big crate issue");
		rightTopInfoPanel.add(bigCrateDepositText);

		// Add cash deposit
		JLabel cashDepositLebel = new JLabel ("Cash Deposit");
		cashDepositLebel.setBounds( 460,10,130,20);
		StyleItems.styleComponentSmall (cashDepositLebel);
		cashDepositLebel.setForeground(Color.BLACK);
		rightTopInfoPanel.add(cashDepositLebel);

		StyleItems.styleComponentSmall (cashDepositText);
		cashDepositText.setBounds( 460,30,125,20);
		cashDepositText.setValue(new Float(0.0));
		cashDepositText.setToolTipText ("Negative values will be considered as cash issue");
		rightTopInfoPanel.add(cashDepositText);


		// Add cash deposit
		JLabel billNumberLebel = new JLabel ("Bill Number");
		billNumberLebel.setBounds( 10,80,130,20);
		StyleItems.styleComponentSmall (billNumberLebel);
		billNumberLebel.setForeground(Color.BLACK);
		rightTopInfoPanel.add(billNumberLebel);

		StyleItems.styleComponentSmall (billNumberText);
		billNumberText.setBounds( 140,80,125,20);
		billNumberText.setValue(new Long(0));
		rightTopInfoPanel.add(billNumberText);
		 
		/*

		StyleItems.styleComponentSmall (bigCrateDepositText);
		bigCrateDepositText.setBounds( 10,80,85,20);
		bigCrateDepositText.setValue(new Long(0));
		bigCrateDepositText.setToolTipText ("Negative values will be ignored");
		rightTopInfoPanel.add(bigCrateDepositText);

		//Big Crate
		JLabel bigCrateIssuedLebel = new JLabel ("Big Crate Issued");
		bigCrateIssuedLebel.setBounds( 160,60,130,20);
		StyleItems.styleComponentSmall (bigCrateIssuedLebel);
		rightTopInfoPanel.add(bigCrateIssuedLebel);

		StyleItems.styleComponentSmall (bigCrateIssuedText );
		bigCrateIssuedText.setBounds( 160,80,85,20);
		bigCrateIssuedText.setValue(new Long(0)); 
		bigCrateIssuedText.setToolTipText ("Negative values will be ignored");
		rightTopInfoPanel.add(bigCrateIssuedText);
		 */
		JButton saveCreditEntry = new JButton ("+");
		saveCreditEntry.setFont(StyleItems.veryBigButtonFont);
		saveCreditEntry.setBackground(StyleItems.lightYellowBackGround);
		saveCreditEntry.setBounds( 590,60,55,25);
		saveCreditEntry.setFont(StyleItems.buttonFont);
		//StyleItems.styleLabel (saveTruckEntry);
		saveCreditEntry.addActionListener(new AddCreditEntryActionListener ());
		saveCreditEntry.setToolTipText("Click to add");
		rightTopInfoPanel.add(saveCreditEntry);


		JLabel filterBy = new JLabel ("Filter By:");
		filterBy.setBounds( 10,120,90,25);
		StyleItems.styleComponentSmall (filterBy);
		rightTopInfoPanel.add(filterBy);

		JLabel fromDateLabel = new JLabel ("From:");
		fromDateLabel.setBounds( 120,120,70,25);
		StyleItems.styleComponentSmall (fromDateLabel);
		rightTopInfoPanel.add(fromDateLabel);

		fromEntryDatePicker.setDate(DateUtility.getFirstDateOfMonth());
		StyleItems.styleDatePickerSmall(fromEntryDatePicker);
		fromEntryDatePicker.setBounds( 170,120,108,20);
		fromEntryDatePicker.setFormats("dd/MM/yyyy");

		PropertyChangeListener fromDateListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**fromEntryDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("fromEntryDatePicker.isEditValid()  " +fromEntryDatePicker.isEditValid());
				if ((fromEntryDatePicker.getDate()!=null) && fromEntryDatePicker.isEditValid()) {
					System.out.println(fromEntryDatePicker.getDate());
					populateCreditEntryTable(); 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		fromEntryDatePicker.addPropertyChangeListener(fromDateListener);


		rightTopInfoPanel.add (fromEntryDatePicker);

		JLabel toDateLabel = new JLabel ("To:");
		toDateLabel.setBounds( 320,120,60,25);
		StyleItems.styleComponentSmall (toDateLabel);
		rightTopInfoPanel.add(toDateLabel);

		toEntryDatePicker.setDate(DateUtility.getLastDateOfMonth());
		StyleItems.styleDatePickerSmall(toEntryDatePicker);
		toEntryDatePicker.setBounds( 390,120,108,20);
		toEntryDatePicker.setFormats("dd/MM/yyyy");		
		rightTopInfoPanel.add (toEntryDatePicker);

		PropertyChangeListener toDateListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**receiveDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("toEntryDatePicker.isEditValid()  " +toEntryDatePicker.isEditValid());
				if ((toEntryDatePicker.getDate()!=null) && toEntryDatePicker.isEditValid()) {
					System.out.println(toEntryDatePicker.getDate());
					populateCreditEntryTable(); 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		toEntryDatePicker.addPropertyChangeListener(toDateListener);



	}

	private void createRightPanel() {

		createCustomerCreditTable();
		createDeleteCreditEntryButton ();
		createModifyEntyButton();
		createCommitAllEntresButton();
		createUnCommitEntresButton();
	}

	private void createUnCommitEntresButton() {
		JButton commitEntryButton = new JButton("Un Commit Entry");
		commitEntryButton.setBounds( 580, 260, 170, 20 );
		commitEntryButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{ 
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to UnCommit Selected item?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){	
					if (creditEntryModel!=null && creditEntryModel.getRowCount() > 0 && creaditEntryTable.getSelectedRow() != -1 )
					{
						System.out.println ( " Selcted Row ==  " + creaditEntryTable.getSelectedRow() );
						Object dataCommitted = creditEntryModel.getValueAt
								(creaditEntryTable.getSelectedRow(), INDEX_OF_DATA_COMMITTED); 
						boolean isDataCommitted = false;
						if (dataCommitted!=null) {
							isDataCommitted = ((Boolean ) dataCommitted).booleanValue();
						}

						if (!isDataCommitted) {

							System.out.println("Data already un committed");
							ToastMessage toastMessage = new ToastMessage("Data already un committed",3000);
							toastMessage.setVisible(true);
							return;
						}

						Object selctedTableObject = creditEntryModel.getValueAt
								(creaditEntryTable.getSelectedRow(), INDEX_OF_ID);
						System.out.println ( "selctedTableObject=" + selctedTableObject);
						Long itemEntryId  = (Long) selctedTableObject;
						unCommitCreditEntryRow  (itemEntryId); 
					}
					else {

						System.out.println("Please choose an item.");
						ToastMessage toastMessage = new ToastMessage("Please choose an item to un commit",3000);
						toastMessage.setVisible(true);
						return;

					}

				}
			}


		});
		rightPanel.add(commitEntryButton);
		
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
						populateCreditEntryTable();
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

	private void populateCustomerData() {

	}



	public void createCustomerCreditTable() 

	{  
		SimpleHeaderRenderer headerRenderForCreditEntry = new SimpleHeaderRenderer();
		headerRenderForCreditEntry.setFont(new Font("Consolas", Font.BOLD, 11)); 
		creaditEntryTable.getTableHeader().setDefaultRenderer(headerRenderForCreditEntry);
		creaditEntryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleComponent(creaditEntryTable);
		scrollForCreditEntryTable  = new JScrollPane(creaditEntryTable); 
		populateCreditEntryTable();
		scrollForCreditEntryTable.setBounds( 10, 10, 720, 230 );
		creaditEntryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (creaditEntryTable.getSelectedRow() > -1) {
					// print first column value from selected row
					//  System.out.println("Value = " + truckEntryTable.getValueAt(truckSaleTable.getSelectedRow(), 0).toString());
					//   fillCustomerEntryItems();
				}
			}
		});
		rightPanel.add(scrollForCreditEntryTable);

	}

	private void createModifyEntyButton() {
		JButton modifyEntryButton = new JButton("Modify Selected Entry");
		modifyEntryButton.setBounds( 210, 260, 160, 20 );
		modifyEntryButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{ 
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Commit All Selected?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){				 
				}
			}


		});
		rightPanel.add(modifyEntryButton);

	}

	private void createCommitAllEntresButton() {
		JButton commitEntryButton = new JButton("Commit Sellected Entry");
		commitEntryButton.setBounds( 400, 260, 170, 20 );
		commitEntryButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{ 
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Commit All Selected?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){	
					if (creditEntryModel.getRowCount() > 0 && creaditEntryTable.getSelectedRow() != -1 )
					{
						System.out.println ( " Selcted Row ==  " + creaditEntryTable.getSelectedRow() );
						Object dataCommitted = creditEntryModel.getValueAt
								(creaditEntryTable.getSelectedRow(), INDEX_OF_DATA_COMMITTED); 
						boolean isDataCommitted = false;
						if (dataCommitted!=null) {
							isDataCommitted = ((Boolean ) dataCommitted).booleanValue();
						}

						if (isDataCommitted) {

							System.out.println("Data already committed");
							ToastMessage toastMessage = new ToastMessage("Data already committed",3000);
							toastMessage.setVisible(true);
							return;
						}

						Object selctedTableObject = creditEntryModel.getValueAt
								(creaditEntryTable.getSelectedRow(), INDEX_OF_ID);
						System.out.println ( "selctedTableObject=" + selctedTableObject);
						Long itemEntryId  = (Long) selctedTableObject;
						commitCreditEntryRow  (itemEntryId); 
					}
					else {

						System.out.println("Please choose an item.");
						ToastMessage toastMessage = new ToastMessage("Please choose an item to commit",3000);
						toastMessage.setVisible(true);
						return;

					}

				}
			}


		});
		rightPanel.add(commitEntryButton);

	}

	private void createDeleteCreditEntryButton() {
		JButton deleteEntryButton = new JButton("Delete Item Entry");
		deleteEntryButton.setBounds( 20, 260, 160, 20 );
		deleteEntryButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{ 
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){

					if (creditEntryModel.getRowCount() > 0 && creaditEntryTable.getSelectedRow() != -1 )
					{
						System.out.println ( " Selcted Roww ==  " + creaditEntryTable.getSelectedRow() );
						Object selctedTableObject = creditEntryModel.getValueAt
								(creaditEntryTable.getSelectedRow(), INDEX_OF_ID);
						System.out.println ( "selctedTableObject=" + selctedTableObject);
						Long itemEntryId  = (Long) selctedTableObject;
						deleteCreditEntryRow (itemEntryId); 
					}
					else {

						System.out.println("Please choose an item.");
						ToastMessage toastMessage = new ToastMessage("Please choose an item",3000);
						toastMessage.setVisible(true);
						return;

					}
				}
			}


		});
		rightPanel.add(deleteEntryButton);

	}

	private void initializeColumnHeaders () {

		customerCreditColumns = new String[SIZE_OF_TABLE_COLUMN];
		customerCreditColumns [INDEX_OF_ID] ="ID" ;//0 (first column will be hidden)
		customerCreditColumns [INDEX_OF_DATE] ="<html>Entry<br> Date";
		customerCreditColumns [INDEX_OF_CRATE_DEPOSIT] ="Small Crate Reurned";
		customerCreditColumns [INDEX_OF_CASH_DEPOSIT] ="Cash Deposit";
		customerCreditColumns [INDEX_OF_BILL_NUMBER] ="Bill Number";		
		customerCreditColumns [INDEX_OF_BIG_CRATE_DEPOSIT] ="<html>Big Crate <br> Reurned";
		customerCreditColumns [ INDEX_OF_DATA_COMMITTED] = "<html>Data <br> Committed";
	}

	private void initializeColumnClass () {

		columnClasses = new Class [SIZE_OF_TABLE_COLUMN];
		columnClasses [ INDEX_OF_ID ]= Long.class;               
		columnClasses [ INDEX_OF_DATE ] = Date.class;     
		columnClasses [ INDEX_OF_CRATE_DEPOSIT] = Integer.class;                   
		columnClasses [ INDEX_OF_CASH_DEPOSIT]  = Float.class;          
		columnClasses [ INDEX_OF_BIG_CRATE_DEPOSIT] = Integer.class;             
		 columnClasses [ INDEX_OF_BILL_NUMBER]  = Long.class;
		columnClasses [ INDEX_OF_DATA_COMMITTED]  = Boolean.class;


	}

	/**
	 * Populates Credit Entry  Data into  table
	 * Each record is added as array to  
	 * TableModel and displayed in separate row 
	 * of the table.
	 */


	private void populateCreditEntryTable() {

		if (rowListOfCustomer.getSelectedIndex()==-1)
			return;
		selectedCustomer = (Customer)rowListOfCustomer.getSelectedValue();
		startDate = fromEntryDatePicker.getDate();
		endDate = toEntryDatePicker.getDate();
		List  customerCreditEntryList = CreditEntry.getCreditEntryList(selectedCustomer,startDate , endDate);		
		tableValueList = new ArrayList <Object []>();
		for (Object s : customerCreditEntryList) {
			Object[] obj = new Object[SIZE_OF_TABLE_COLUMN];
			CreditEntry credtEntry = (CreditEntry) s;
			obj[INDEX_OF_ID] = credtEntry.getId();
			obj[INDEX_OF_DATE ] = credtEntry.getEntryDate();
			obj[INDEX_OF_CRATE_DEPOSIT] = credtEntry.getCrateDeposit();
			obj[INDEX_OF_CASH_DEPOSIT] = credtEntry.getCashDeposit();
			obj[INDEX_OF_BIG_CRATE_DEPOSIT] = credtEntry.getBigCrateDeposit();
			obj[INDEX_OF_BILL_NUMBER] = credtEntry.getBillNumber();
	 			Boolean val = credtEntry.getDataCommited();
			boolean dataCommitted = (val!=null)? (boolean) val: false;;			 
			obj[INDEX_OF_DATA_COMMITTED] = dataCommitted;
			//	o[4] = credtEntry.getCrateIssue();			
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
				return ModifiableCreditEntry.this.columnClasses [c];
			}
		};

		//	List  customerCreditEntryList = CreditEntry.getCreditEntryList(selectedCustomer,startDate , endDate);		
		creditEntryModel.setColumnIdentifiers(customerCreditColumns);		 
		for (Object s : tableValueList) {

			Object [] objArr =  (Object []) s;
			creditEntryModel.addRow(objArr);
		}

		creaditEntryTable.setModel(creditEntryModel);

		// First column is removed to hide ID of the entry.
		// This id will be used to update or delete the entry
		creaditEntryTable.removeColumn( creaditEntryTable.getColumnModel().getColumn(INDEX_OF_ID)); 

		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer();
		creaditEntryTable.setDefaultRenderer( Boolean.class, customTableCellRenderer );



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

	class AddCreditEntryActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to add entry and update ledger?","Warning",dialogButton);
			if(! (dialogResult == JOptionPane.YES_OPTION)){
				return;
			}

			if (rowListOfCustomer.getSelectedIndex()==-1){
				ToastMessage toastMessage = new ToastMessage("Please select a customer",3000);
				toastMessage.setVisible(true);
				return;				
			}
			// This part is commented as negative value is allowed.
/*
			if ( ( Utility.getValue ((Float)cashDepositText.getValue() ) <0) ||
					( Utility.getValue ((Long)crateDepositText.getValue() )<0) ||
					( Utility.getValue ((Long)bigCrateDepositText.getValue()) <0 )
					){ 
				ToastMessage toastMessage = new ToastMessage("Please enter valid quantity",3000, Color.RED);
				toastMessage.setVisible(true);
				return;
			}
			
			*/

			if ( ( Utility.getValue ((Float)cashDepositText.getValue() ) ==0) &&
					( Utility.getValue ((Long)crateDepositText.getValue() )==0) &&
					( Utility.getValue ((Long)bigCrateDepositText.getValue()) ==0 )
					){ 
				ToastMessage toastMessage = new ToastMessage("Please enter valid quantity",3000, Color.RED);
				toastMessage.setVisible(true);
				return;
			}
			CreditEntry truckEntry = new CreditEntry();
			populateCustomerCreditEntryData( truckEntry);
			if (CreditEntry.saveCreditEntryItem (truckEntry)) {
				ToastMessage toastMessage = new ToastMessage("Credit Entry Added Successfully",3000);
				toastMessage.setVisible(true);
				populateCreditEntryTable();
			}
			else 
			{   
				ToastMessage toastMessage = new ToastMessage("Credit Entry Not Added",3000, Color.RED);
				toastMessage.setVisible(true);
			}

		}


	}
	private void commitCreditEntryRow(Long itemEntryId) {

		if ( CommitData.commitCreditEntrySingleEntry(itemEntryId)) {			 
			System.out.println ("Committed  Sucessfully");
			ToastMessage toastMessage = new ToastMessage("Committed Successfully",3000);
			toastMessage.setVisible(true); 
			populateCreditEntryTable();
		}
		else {
			System.out.println ("Could not Commit  the CreditEntry");
			ToastMessage toastMessage = new ToastMessage("Could Not Commit",3000);
			toastMessage.setVisible(true); 
		}
	}
	
	private void unCommitCreditEntryRow(Long itemEntryId) {

		if ( CommitData.unCommitCreditEntrySingleEntry(itemEntryId)) {			 
			System.out.println ("Committed  Sucessfully");
			ToastMessage toastMessage = new ToastMessage("Un Committed Successfully",3000);
			toastMessage.setVisible(true); 
			populateCreditEntryTable();
		}
		else {
			System.out.println ("Could not Un Commit  the CreditEntry");
			ToastMessage toastMessage = new ToastMessage("Could Not Un Commit",3000);
			toastMessage.setVisible(true); 
		}
	}




	private void deleteCreditEntryRow(Long itemEntryId) {
		if ( CreditEntry.deleteCreditEntryItem(itemEntryId))  {			 
			System.out.println ("Deleted Sucessfully");
			ToastMessage toastMessage = new ToastMessage("Deleted Successfully",3000);
			toastMessage.setVisible(true); 
			populateCreditEntryTable();
		}
		else {
			System.out.println ("Could not delete the CreditEntry");
			ToastMessage toastMessage = new ToastMessage("Could Not Delete",3000);
			toastMessage.setVisible(true); 
		}

	}


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			ModifiableCreditEntry dialog = new ModifiableCreditEntry (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(950, 580);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public void populateCustomerCreditEntryData(CreditEntry creditEntry) {
		creditEntry.setCustomer(  (Customer) rowListOfCustomer.getSelectedValue());
		creditEntry.setEntryDate(creditEntryDatePicker.getDate());

		Object valueOfCash = cashDepositText.getValue();
		creditEntry.setCashDeposit(Utility.getValue( (Float)valueOfCash )); 

		Object valueOfSmallCrateReturned = crateDepositText.getValue();
		creditEntry.setCrateDeposit(Utility.getValue( (Long)valueOfSmallCrateReturned ));
		
		Object valueOfBillNumber = billNumberText.getValue();
		creditEntry.setBillNumber(Utility.getValue( (Long)valueOfBillNumber )); 
	 
		Object valueOfBigCrateReturned = bigCrateDepositText.getValue();
		creditEntry.setBigCrateDeposit(Utility.getValue( (Long)valueOfBigCrateReturned )); 

		creditEntry.setDataCommited(false);
 
	}


}


