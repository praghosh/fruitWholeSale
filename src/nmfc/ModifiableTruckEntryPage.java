package nmfc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdesktop.swingx.JXDatePicker;

import entities.Address;
import entities.ContainerItems;
import entities.Franchise;
import entities.Fruit;
import entities.FruitQuality;
import entities.Truck;
import entities.TruckEntry;

import nmfc.ModifiableTruckEntryPage.AddTruckEntryActionListener;
import nmfc.ModifiableTruckEntryPage.retrieveTruckActionListener;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StandardItems;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import persistence.HibernateUtil;



public class ModifiableTruckEntryPage extends JDialog {

	private Font buttonFont = new Font("Courier", Font.BOLD,13);
	private Color buttonText = new Color(90, 10, 20);
	private Color buttonBorder = new Color(180, 100, 160);
	private JXDatePicker receiveDatePicker;
	private JXDatePicker entryDatePicker;
	private JPanel itemListPanel = new JPanel();
	private JPanel secondinfoPanel = new JPanel();
	private JPanel itemEntryPanel = new JPanel();
	private JScrollPane listScrollPane = new JScrollPane();
	private List truckList;
	private JList rowList;
	private Session session;
	private JTextField truckNumber = new JTextField ();
	private JTextArea addressEntry = new JTextArea();
	private List <Franchise> frList;
	private Truck selectedTruck;
	private JComboBox < Franchise> franchise;
	private List fruitList;
	private JComboBox fruitCombo;
	private DefaultTableModel model;
	private Fruit selectedFruit;
	private JComboBox fruitQualityCombo;
	private JFormattedTextField quantity;
	//	private JTextField purchaseUnit;
	//	private JTextField salesUnit;
	private JTable table =  new JTable();
	private JScrollPane scroll;
	private String[] columns;
	private JComboBox purchaseUnitCombo;
	private JComboBox salesUnitCombo;
	JTextField lotNumber = new JTextField ();
	private JTextArea franchiseAddressEntry;
	private JCheckBox isSoldOut;

	private static final int  INDEX_OF_ID_COLUMN = 0; 
	private static final int  INDEX_OF_FRUIT_COLUMN = 1;
	private static final int  INDEX_OF_QUALITY_COLUMN = 2;
	private static final int  INDEX_OF_LOT_NUMBER_COLUMN = 3;
	private static final int  INDEX_OF_PURCHASE_UNIT_COLUMN = 4;
	private static final int  INDEX_OF_SALES_UNIT_COLUMN = 5;
	private static final int  INDEX_OF_QUANTITY_COLUMN = 6;
	private static final int  INDEX_OF_REMAINING_QUANTITY_COLUMN = 7;
	private static final int  TOTAL_SIZE_OF_ALL_COLUMN = 8;
	private static final  String[]  purschaseOrSellingUnit = new String[] {
		"Small Crate", "Big Crate", "Petty",  "Box", "Busket" , "Bag", "Kg", "Pieces", "Kandi"};


	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			//Truck dialog = new Truck();
			ModifiableTruckEntryPage dialog = new ModifiableTruckEntryPage (null, "Truck Page", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(820, 600);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public ModifiableTruckEntryPage(Frame homePage, String string, boolean b) {
		// TODO Auto-generated constructor stub
		super (homePage,  string,  b);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 920, 640 );
		getContentPane().setBackground(new Color(230, 245, 240) );
		UIManager.put("ToolTip.font",
				new FontUIResource("Siyamrupali_1_01", Font.BOLD, 14));

		//	secondinfoPanel = new JPanel();
		secondinfoPanel.setLayout( null );
		secondinfoPanel.setBounds( 10, 10, 1000, 190 );
		secondinfoPanel.setBackground(new Color(200, 230, 210) );
		secondinfoPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createSecndInfo();
		add(secondinfoPanel);

		//	itemEntryPanel = new JPanel();
		itemEntryPanel.setLayout( null );
		itemEntryPanel.setBounds( 10, 205, 1000, 85 );
		itemEntryPanel.setBackground(new Color(210, 230, 200) );
		itemEntryPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createItemEntryPanel () ;
		add(itemEntryPanel);

		//	itemListPanel = new JPanel();
		itemListPanel.setLayout( null );
		itemListPanel.setBounds( 10, 300, 1000, 270 );
		itemListPanel.setBackground(new Color(200, 230, 210) );
		itemListPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createTruckListPanel () ;
		add(itemListPanel);	

		JButton button = new JButton("Close Window");
		button.setBounds( 490, 580, 150, 30 );
		button.setBorder(new LineBorder(new Color(200, 80, 70), 2));
		add(button);
		// set action listener on the button
		button.addActionListener(new CloseActionListener());


	}

	private void createItemEntryPanel() {
		JLabel fruitLabel = new JLabel ("Fruit");
		fruitLabel.setBounds( 30,10,115,20);
		styleComponent (fruitLabel);

		fruitList = Fruit.getFruitNames();
		fruitCombo = new JComboBox (fruitList.toArray());
		//JTextField franchise = new JTextField ();
		fruitCombo.setBounds( 10,45,100,20);
		fruitCombo.setBorder(new LineBorder(new Color(0, 80, 70), 1));
		fruitCombo.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				recreateFruitQualityCombo();
			}
		});
		styleComponent (fruitCombo);
		itemEntryPanel.add(fruitCombo);


		JLabel qualityLabel = new JLabel ("Quality");
		qualityLabel.setBounds( 150,10,95,20);
		styleComponent (qualityLabel);

		selectedFruit = (Fruit) fruitCombo.getSelectedItem();
		fruitList = FruitQuality.getFruitQualityNames(selectedFruit);
		fruitQualityCombo = new JComboBox (fruitList.toArray());
		//JTextField franchise = new JTextField ();
		fruitQualityCombo.setBounds( 140,45,110,20);
		fruitQualityCombo.setBorder(new LineBorder(new Color(0, 80, 70), 1));
		styleComponent (fruitQualityCombo);
		itemEntryPanel.add(fruitQualityCombo);


		JLabel lotLabel = new JLabel ("<html> Lot <br> Number");
		lotLabel.setBounds( 300,10,105,30);
		styleComponent (lotLabel);


		lotNumber.setBounds( 280,45,75,20);
		styleComponent (lotNumber);
		itemEntryPanel.add(lotNumber);

		JLabel purchaseUnitLabel = new JLabel ("<html> Purchase <br>Unit");
		purchaseUnitLabel.setBounds( 400,10,145,30);
		styleComponent (purchaseUnitLabel);

		List <ContainerItems> totalList = ContainerItems.getContainerItemsNames();  // item from DB 
		purchaseUnitCombo = new JComboBox ( totalList.toArray());
		//JTextField franchise = new JTextField ();
		purchaseUnitCombo.setBounds( 400,45,125,20);
		purchaseUnitCombo.setBorder(new LineBorder(new Color(0, 80, 70), 1));
		styleComponent (purchaseUnitCombo);
		itemEntryPanel.add(purchaseUnitCombo);

		/*		purchaseUnit = new JTextField ();
		purchaseUnit.setBounds( 350,45,85,20);
		styleComponent (purchaseUnit);
		itemEntryPanel.add(purchaseUnit);
		 */

		JLabel salesUnitLabel = new JLabel ("<html> Sales <br> Unit");
		salesUnitLabel.setBounds( 560,10,85,30);
		styleComponent (salesUnitLabel);

		/*	salesUnit = new JTextField ();
		salesUnit.setBounds( 490,45,75,20);
		styleComponent (salesUnit);
		itemEntryPanel.add(salesUnit); */
 	 
		salesUnitCombo = new JComboBox (totalList.toArray());
		//JTextField franchise = new JTextField ();
		salesUnitCombo.setBounds( 560,45,125,20);
		salesUnitCombo.setBorder(new LineBorder(new Color(0, 80, 70), 1));
		styleComponent (salesUnitCombo);
		itemEntryPanel.add(salesUnitCombo);

		JLabel quantityLabel = new JLabel ("Quantity");
		quantityLabel.setBounds( 730,10,85,20);
		styleComponent (quantityLabel);

		quantity = new JFormattedTextField();
		quantity.setBounds( 730,45,75,20);		
		quantity.setValue(new Long (0));
		styleComponent (quantity);
		itemEntryPanel.add(quantity);

		JButton saveTruckEntry = new JButton ("+");
		saveTruckEntry.setBounds( 840,40,50,25);
		saveTruckEntry.setFont(StyleItems.bigButtonFont);
		StyleItems.styleLabel (saveTruckEntry);
		saveTruckEntry.addActionListener(new AddTruckEntryActionListener ());
		UIManager.put("ToolTip.font",
				new FontUIResource("Siyamrupali_1_01", Font.BOLD, 14));
		saveTruckEntry.setToolTipText("নতুন আইটেম যোগ করুন ");

		itemEntryPanel.add(saveTruckEntry);

		itemEntryPanel.add(fruitLabel);
		itemEntryPanel.add(qualityLabel);
		itemEntryPanel.add(lotLabel);
		itemEntryPanel.add(purchaseUnitLabel);
		itemEntryPanel.add(salesUnitLabel);
		itemEntryPanel.add(quantityLabel);
	}


	private void createSecndInfo() {

		JLabel receiveDateLabel = new JLabel ("Receive Date");
		receiveDateLabel.setBounds( 10,10,105,20);
		styleComponent (receiveDateLabel);
		secondinfoPanel.add(receiveDateLabel);

		receiveDatePicker = new JXDatePicker();
		receiveDatePicker.setDate(new Date());
		// styleComponent (receiveDatePicker);
		receiveDatePicker.setBounds( 115,10,120,30);
		receiveDatePicker.setFormats("dd/MM/yyyy");
		PropertyChangeListener listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**receiveDatePicker -- Property change");
	 			if ((receiveDatePicker.getDate()!=null) &&  receiveDatePicker.isEditValid()) {
					recreateTruckList();
				   
				}
			}
		};
       /*
		ActionListener alistener= new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{
				System.out.println("----------receiveDatePicker " + receiveDatePicker.getDate());

				if  ( (receiveDatePicker.getDate()==null) || !receiveDatePicker.isEditValid()) {
					receiveDatePicker.setDate(new Date() );
					System.out.println("Acction " + receiveDatePicker.getDate());
				}
			}
		};  
		*/
	    receiveDatePicker.addPropertyChangeListener(listener);
		//	 receiveDatePicker.addActionListener(alistener);
		secondinfoPanel.add (receiveDatePicker);




		JLabel entryDateLabel = new JLabel ("Entry Date");
		entryDateLabel.setBounds( 250,10,85,20);
		styleComponent (entryDateLabel);
		secondinfoPanel.add (entryDateLabel);

		entryDatePicker = new JXDatePicker();
		entryDatePicker.setDate(new Date());
		styleComponent (entryDatePicker);
		entryDatePicker.setBounds( 340,10,120,30);
		entryDatePicker.setFormats("dd/MM/yyyy");
		secondinfoPanel.add (entryDatePicker);

		createTruckList(new Date());
		secondinfoPanel.add(listScrollPane);

		JLabel truckNumberLabel = new JLabel ("Truck Number");
		truckNumberLabel.setBounds( 10,55,115,20);
		styleComponent (truckNumberLabel);
		secondinfoPanel.add(truckNumberLabel);

		//	truckNumber = new JTextField ();
		truckNumber.setBounds( 120,55,115,20);
		styleComponent (truckNumber);
		truckNumber.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		secondinfoPanel.add(truckNumber);
	/*	
		JButton retrieve = new JButton ("Retrive");
		retrieve.setBounds( 480,10,90,20);
		StyleItems.styleLabel (retrieve);
		retrieve.addActionListener(new retrieveTruckActionListener ());
		secondinfoPanel.add(retrieve);
 */
		JButton save = new JButton ("ADD");
		save.setBounds( 730,25,90,20);
		StyleItems.styleLabel (save);
		save.addActionListener(new AddTruckActionListener ());
		secondinfoPanel.add(save);

		JButton modify = new JButton ("Modify");
		modify.setBounds( 730,65,90,20);
		StyleItems.styleLabel (modify);
		modify.addActionListener(new ModifyTruckActionListener ());
		secondinfoPanel.add(modify);

		JButton delete = new JButton ("Delete");
		delete.setBounds( 730,105,90,20);
		StyleItems.styleLabel (delete);
		delete.addActionListener(new DeleteTruckActionListener ());
		secondinfoPanel.add(delete);


		JLabel franchiseLabel = new JLabel ("Franchise");
		franchiseLabel.setBounds( 10,95,105,20);
		styleComponent (franchiseLabel);
		secondinfoPanel.add(franchiseLabel);

		frList = Franchise.getFranchiseNames();
		if (frList!=null) {
			frList.add(null);
		}		 
		franchise = new JComboBox (frList.toArray());
		//JTextField franchise = new JTextField ();
		franchise.setBounds( 120,95,115,20);
		franchise.setBorder(new LineBorder(new Color(0, 80, 70), 1));
		franchise.addItemListener (new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED || state == ItemEvent.DESELECTED) {
					setFranchiseAddressText();		        	  
				}
								
			}
			
		});
		styleComponent (franchise);
		secondinfoPanel.add(franchise);

		JLabel franchiseAddressLabel = new JLabel ("Franchise Address");
		franchiseAddressLabel.setBounds( 305,55,155,20);
		styleComponent (franchiseAddressLabel);
		secondinfoPanel.add(franchiseAddressLabel);

		franchiseAddressEntry = new JTextArea ();
		franchiseAddressEntry.setBounds( 270,75,180,60);
		setFranchiseAddressText();
		StyleItems.styleComponent  (franchiseAddressEntry);
		franchiseAddressEntry.setEditable(false);
		franchiseAddressEntry.setBorder(new LineBorder(buttonBorder, 2));
		secondinfoPanel.add(franchiseAddressEntry);

		JLabel truckAddressLabel = new JLabel ("<html>Other Truck Address <br> If no Frnchise");
		truckAddressLabel.setBounds( 495,35,155,35);
		styleComponent (truckAddressLabel);
		truckAddressLabel.setToolTipText("যদি আলাদা ঠিকানা হয়"); 
		secondinfoPanel.add(truckAddressLabel);

		addressEntry = new JTextArea ();
		addressEntry.setBounds( 490,75,180,60);		 
		StyleItems.styleComponent2 (addressEntry);
		truckAddressLabel.setToolTipText("যদি আলাদা ঠিকানা হয় "); 
		addressEntry.setBorder(new LineBorder(buttonBorder, 2));
		secondinfoPanel.add(addressEntry); 
		
		JLabel isSolOutLabel = new JLabel("Items Sold Out?");
		isSolOutLabel.setBounds( 10, 165, 140, 20);
		StyleItems.styleComponent (isSolOutLabel);
		secondinfoPanel.add (isSolOutLabel);

		Icon ic = null;
		isSoldOut = new JCheckBox(ic, false);
		isSoldOut.setBounds( 135, 165, 20, 20);
		StyleItems.styleComponent (isSoldOut);
		secondinfoPanel.add (isSoldOut);

	}

	private void setFranchiseAddressText() {
		Franchise selectedFranchise = (Franchise) franchise.getSelectedItem();
		String addressText ="";
		if (selectedFranchise!=null) {
			Address address = selectedFranchise.getAddress();
			addressText = addressText + address.getAddressLine1() +
					"\n" + address.getAddressLine2() +				
					"\n"+ address.getCity()
					+ "\n"+ address.getState();
		}
		else {
			addressText="";
		}
		franchiseAddressEntry.setText(addressText);

	}


	/**
	 * Select the correct frnchise from the dropdown.
	 */

	private void selectRelatedFranchise() {

		int index = 0;
		Franchise frSlected = selectedTruck.getFranchise();

		Integer frId=-1;

		if (frSlected==null) {
			franchise.setSelectedItem(null);
			return;
		}


		frId= selectedTruck.getFranchise().getId(); 


		for (Iterator iterator = frList.iterator(); iterator.hasNext();) {
			Franchise 	fr = (Franchise) iterator.next();
			if (fr==null) {
				continue;
			}
			Integer id = fr.getId();
			if ( id!=null && id.equals(frId)) 
			{
				System.out.println("Macthing Franchise Found" );
				franchise.setSelectedIndex(index);
			}
			index++;
		}


	}


	/** Create Truck List based on receive date 
	 *  It just displays the names of the truck on Jlist.
	 *  On select of each truck, truck-data is rtrieved.
	 * 
	 * @param truckReciveDate
	 */
	public void createTruckList (Date truckReciveDate) {

		listScrollPane.setBounds( 850,10,135,160);
		truckList = Truck.getTruckNames(truckReciveDate);
		rowList = new JList(truckList.toArray());
		rowList.setVisibleRowCount(8);
		StyleItems.styleList(rowList);
		rowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = rowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " + truckList.get(idx));
					if (!le.getValueIsAdjusting()) {
						populateTruckData();
						populateTruckEntryTable();
					}
					

				}
				else
					System.out.println("Please choose a Truck.");
			}
		}); 		
		listScrollPane.setViewportView(rowList);
		listScrollPane.revalidate();
		listScrollPane.repaint();
		rowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		secondinfoPanel.add(listScrollPane);
	}



	// An action listener to be used when an action is performed
	// In this case when close button is clicked this event occurred
	class CloseActionListener implements ActionListener {

		//close and dispose of the window.
		public void actionPerformed(ActionEvent e) {
			System.out.println("disposing the window..");
			setVisible(false);
			dispose();
		}
	}
	/**
	 * 
	 * This class creates list truck items recived for each truck.
	 * List is refreshed when click on another truck or when truck data
	 * changed.
	 * 
	 */

	public void createTruckListPanel () 
	{

		createTruckEntryTableStructure();
		populateTruckEntryTable();
		itemListPanel.add(scroll);

		/*
		 *  Delete button at the bottom of truck itemlist to delete
		 *  selected item.
		 */
		JButton deleteEntryButton = new JButton("Delete Item Entry");
		deleteEntryButton.setBounds( 120, 210, 160, 20 );
		deleteEntryButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					if (model.getRowCount() > 0 && table.getSelectedRow() != -1 )
					{
						System.out.println ( " Selcted Roww ==  " + table.getSelectedRow() );
						Object selctedTableObject = model.getValueAt(table.getSelectedRow(), INDEX_OF_ID_COLUMN);
						System.out.println ( "selctedTableObject=" + selctedTableObject);
						Long itemEntryId  = (Long) selctedTableObject;
						if (TruckEntry.deleteItemEntryRow (itemEntryId)) {
							ToastMessage toastMessage = new ToastMessage("Truck Item deleted Successfully",3000);
							toastMessage.setVisible(true);						
							populateTruckEntryTable();
						}
						else {
							ToastMessage toastMessage = new ToastMessage("Truck Item not deleted",3000);
							toastMessage.setVisible(true);		
						}
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
		itemListPanel.add(deleteEntryButton);
		
		JButton modifyButton = new JButton("Modify Item Entry");
		modifyButton.setBounds( 340, 210, 160, 20 );
		modifyButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Modify?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					if (model.getRowCount() > 0 && table.getSelectedRow() != -1 )
					{
						System.out.println ( " Selcted Roww ==  " + table.getSelectedRow() );
						Object selctedTableObject = model.getValueAt(table.getSelectedRow(), INDEX_OF_ID_COLUMN);
						System.out.println ( "selctedTableObject=" + selctedTableObject);
						Long itemEntryId  = (Long) selctedTableObject;
						
						String s = (String) JOptionPane.showInputDialog("Enter the new Quantity");
						System.out.println(s);
						
						long newQty = 0;
						//If a string was returned, say so.
						if ((s != null) && (s.length() > 0)) {
						   
							try {
								newQty =Long.parseLong(s);
							}
							catch (Exception e) {
								e.printStackTrace();
								ToastMessage toastMessage = new ToastMessage("Not a valid Qty",3000);
								toastMessage.setVisible(true);
								return;
							}
							
						    
						}
						
				 		
						if (TruckEntry.modifyTruckEntryItemQuantity (itemEntryId, newQty)) {
							ToastMessage toastMessage = new ToastMessage("Truck Item modified Successfully",3000);
							toastMessage.setVisible(true);						
							populateTruckEntryTable();
						}
						else {
							ToastMessage toastMessage = new ToastMessage("Truck Item not modified",3000);
							toastMessage.setVisible(true);		
						}
						 
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
		itemListPanel.add(modifyButton);
	}

	/*
	 * This method creates the static structure of the 
	 * truck entry list table. But does not populate 
	 * table with data.
	 */
	private void createTruckEntryTableStructure() {
		columns = new String[TOTAL_SIZE_OF_ALL_COLUMN] ;	
		columns [INDEX_OF_ID_COLUMN] = "ID"  ;           
		columns [INDEX_OF_FRUIT_COLUMN ]= "Fruit";        
		columns [INDEX_OF_QUALITY_COLUMN ] ="Quality" ;      
		columns [INDEX_OF_LOT_NUMBER_COLUMN ]=  "Lot No.";   
		columns [INDEX_OF_PURCHASE_UNIT_COLUMN ]= "Purchase Unit";
		columns [INDEX_OF_SALES_UNIT_COLUMN ]= "Sales Unit";   
		columns [INDEX_OF_QUANTITY_COLUMN] = "Quantity"; 
		columns [INDEX_OF_REMAINING_QUANTITY_COLUMN] = "Remaining Qty";

		table.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleTableBigFont(table);
		scroll = new JScrollPane(table); 
		scroll.setBounds( 30, 10, 935, 190 );		
	}




	private void populateTruckEntryTable() {

		selectedTruck = (Truck)rowList.getSelectedValue();
		int truckId = 0;
		if (selectedTruck!=null) {
			truckId = selectedTruck.getId();
		} else {
			return;
		}
		List <TruckEntry> truckEntryList = TruckEntry.getTruckEntryList(truckId);

		model =	new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {					 
				//		return ( (column >INDEX_OF_LOT_NUMBER_COLUMN) && (column <INDEX_OF_REMAINING_QUANTITY_COLUMN) ) ;
				return false;
			}

			// Right now no restriction on data type check in entry
			/*	public Class getColumnClass(int c) {
				return columnClass [c];
			}
			 */ 


		};


		model.setColumnIdentifiers(columns);
		for (Object truckEntryItem : truckEntryList) {
			Object[] obj = new Object[TOTAL_SIZE_OF_ALL_COLUMN];
			TruckEntry truckEntry = (TruckEntry) truckEntryItem;
			obj[INDEX_OF_FRUIT_COLUMN ] = truckEntry.getFruit();
			obj[INDEX_OF_QUALITY_COLUMN ] = truckEntry.getFruitQuality();
			obj[INDEX_OF_LOT_NUMBER_COLUMN ] = truckEntry.getLotNumber();
			obj[INDEX_OF_PURCHASE_UNIT_COLUMN ] = truckEntry.getPurchaseUnit();
			obj[INDEX_OF_SALES_UNIT_COLUMN ] = truckEntry.getSalesUnit();
			obj[INDEX_OF_QUANTITY_COLUMN]  = truckEntry.getQuantity();
			obj[INDEX_OF_REMAINING_QUANTITY_COLUMN]  = truckEntry.getRemainingQuantity();
			obj[INDEX_OF_ID_COLUMN] = truckEntry.getId();	
			model.addRow(obj);
		}		
		table.setModel(model);
		//Hiding ID colum. It is required to select from database only
		table.removeColumn(table.getColumnModel().getColumn(INDEX_OF_ID_COLUMN)); //hiding		
		scroll.repaint();
	}


	public void styleComponent(JComponent button)
	{
		button.setForeground(buttonText);
		button.setFont(buttonFont);
		//	button.setBorder(new LineBorder(buttonBorder, 2));
	}

	public class DeleteTruckActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){
				try {
					int idx = rowList.getSelectedIndex();
					if (idx != -1){
						Serializable id = ((Truck)rowList.getSelectedValue()).getId();  
						if (Truck.deleteSelectedTruck(id))
						{				
							ToastMessage toastMessage = new ToastMessage("Truck Deleted Successfully",3000);
							toastMessage.setVisible(true);	
						}
						else {
							ToastMessage toastMessage = new ToastMessage("Truck Not Deleted",3000);
							toastMessage.setVisible(true);
						}
						 
						recreateTruckList ();
						clearTruckData();
					}
					else {
						System.out.println("Please choose a Truck.");
						ToastMessage toastMessage = new ToastMessage("Please choose a Truck",3000);
						toastMessage.setVisible(true);

					}

				}
				catch (Exception ex)
				{
					ToastMessage toastMessage = new ToastMessage("Could not delete route",3000);
					toastMessage.setVisible(true);
				}
				/*	finally {

					if(session!=null){
						session.close();
					}
				}*/
			}
		}


	}

	private void clearTruckData() {

		/*	truckName.setText("");
		truckCode.setText("");
		truckAddressLine1.setText("");
		truckAddressLine2.setText("");
		truckCity.setText("");
		pinCode.setText("");
		state.setText("");
		parentName.setText("");
		mobileNumber.setText(""); 
		//phoneNumber.setText("");
		adharNumber.setText("");
		voterNumber.setText(""); 	
		 */ 

	} 

	/*	public boolean deleteSelectedTruck(int x) {
		try {
			System.out.println (((Truck)rowList.getSelectedValue()).getId());
			//	System.out.println("Current selection: " + routes.get(idx));
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Serializable id = ((Truck)rowList.getSelectedValue()).getId();
			Object persistentInstance = session.load(Truck.class, id);
			if (persistentInstance != null)  {
				session.delete(persistentInstance);
				session.getTransaction().commit();
				return true;
			}
			else {
				System.out.println ("Did not find the Truck Object in persistance");
				return false;
			}

		} 
		catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}

		finally {
			if(session!=null){
				session.close();
			}
		}
	} */
	private void populateTruckData() {
		System.out.println ( "calling populateTruckData ");
		selectedTruck = (Truck)rowList.getSelectedValue();
		System.out.println ( "selectedTruck =" + selectedTruck);
		franchise.setSelectedItem( (Object) selectedTruck.getFranchise());
		truckNumber.setText(selectedTruck.toString());		
		entryDatePicker.setDate(selectedTruck.getEntryDate());
		isSoldOut.setSelected(Utility.getValue( selectedTruck.isSoldOut()));	 
		receiveDatePicker.setDate(selectedTruck.getReceiveDate() );
		// 	truckCode.setText(selectedTruck.getCode());
		Address selectedFranchiseAddress = null;
		if (selectedTruck.getFranchise()!=null) {
			selectedFranchiseAddress = selectedTruck.getFranchise().getAddress();
		}
		String addressText ="";
		if (selectedFranchiseAddress!=null) {
			addressText = addressText + selectedFranchiseAddress.getAddressLine1() +
					"\n" + selectedFranchiseAddress.getAddressLine2() +				
					"\n"+ selectedFranchiseAddress.getCity()
					+ "\n"+ selectedFranchiseAddress.getState();

			franchiseAddressEntry.setText(addressText);	  
		}
		else {
			franchiseAddressEntry.setText("");			 
		}
		  
		addressEntry.setText(selectedTruck.getTruckAddress());
		selectRelatedFranchise();
		reapaintTruckData();

	}

	private void reapaintTruckData() {
		truckNumber.repaint();
		entryDatePicker.repaint();
		receiveDatePicker.repaint();
		franchise.repaint();	
	}

	public class ModifyTruckActionListener implements ActionListener {


		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isTruckNameValid ()) return;

			int idx = rowList.getSelectedIndex();
			if (idx == -1) {
				System.out.println("Please choose a Truck.");
				ToastMessage toastMessage = new ToastMessage("Please choose a Truck",3000);
				toastMessage.setVisible(true);
				return;
			}
			if (truckExistWithSameName(true))
			{
				return;
			}

			try {				 
				System.out.println (((Truck)rowList.getSelectedValue()).getId());
				System.out.println("Current selection: " + truckList.get(idx));
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();
				Serializable id = ((Truck)rowList.getSelectedValue()).getId();
				Object persistentInstance = session.load(Truck.class, id);
				Truck truck = (Truck)persistentInstance;
				enterTruckData (truck);			 
				session.save(truck);
				session.getTransaction().commit();					
			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("Truck not modified",3000);
				toastMessage.setVisible(true);
			}

			finally {
				if(session!=null && session.isOpen()){
					session.close();
				}
			}
			ToastMessage toastMessage = new ToastMessage("Truck modified Successfully",3000);
			toastMessage.setVisible(true);
			recreateTruckList ();
		}

	}

	public void recreateFruitQualityCombo () {
		selectedFruit = (Fruit) fruitCombo.getSelectedItem();	
		fruitList = FruitQuality.getFruitQualityNames(selectedFruit);
		DefaultComboBoxModel model = new DefaultComboBoxModel( fruitList.toArray() );
		fruitQualityCombo.setModel( model );
		fruitQualityCombo.repaint();
	}

	public void recreateTruckList () {
	//	secondinfoPanel.setVisible (false);
	//	secondinfoPanel.remove(listScrollPane);
	//	createTruckList(receiveDatePicker.getDate());
		truckList = Truck.getTruckNames(receiveDatePicker.getDate());
		rowList.setListData(truckList.toArray());		 
	//	rowList.repaint();
		listScrollPane.setViewportView(rowList);
		listScrollPane.repaint();	
		listScrollPane.setVisible (true);
//		secondinfoPanel.repaint();
//		secondinfoPanel.setVisible (true);

	}

	public class AddTruckActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (!isTruckNameValid ()) return;

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();				 
				if (!truckExistWithSameName(false))
				{
					Truck truck = new Truck();
					enterTruckData( truck);
					int truckId = (Integer) session.save(truck);
					session.getTransaction().commit();					
				}
				else return;

			}
			catch (Exception ex)
			{   
				ToastMessage toastMessage = new ToastMessage("Truck was not Saved",3000);
				toastMessage.setVisible(true);
				System.out.println(ex.getMessage());
				ex.printStackTrace();
				return;
			}
			finally {
				if(session!=null&&session.isOpen()){
					session.close();
				}
			}
			ToastMessage toastMessage = new ToastMessage("Truck Saved Successfully",3000);
			toastMessage.setVisible(true);
			recreateTruckList();
		}
	}

	// Populate Truck data from user input. 	
	private  void  enterTruckData(Truck truck) {

		truck.setName(truckNumber.getText());
		truck.setReceiveDate(receiveDatePicker.getDate());
		truck.setEntryDate(entryDatePicker.getDate());
		truck.setTruckAddress(addressEntry.getText());
		boolean isSold = Utility.getValue(isSoldOut.isSelected());
		truck.setSoldOut(isSold);

		if (franchise.getSelectedItem()!=null)
		{
			Serializable id = ((Franchise)franchise.getSelectedItem()).getId();
			Object persistentInstance = session.load(Franchise.class, id);
			Franchise  selectedFranchise = (Franchise)persistentInstance;
			truck.setFranchise(selectedFranchise);
		}
		else {
			truck.setFranchise(null);
		}
	}


	/*

	public boolean deleteSelectedRTruck() {
		try {
			System.out.println ("Calling delete");
			System.out.println (((Truck)rowList.getSelectedValue()).getId());
			//	System.out.println("Current selection: " + routes.get(idx));
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Serializable id = ((Truck)rowList.getSelectedValue()).getId();
			Object persistentInstance = session.load(Truck.class, id);
			if (persistentInstance != null)  {
				session.delete(persistentInstance);
				session.getTransaction().commit();
				return true;
			}
			else {
				System.out.println ("Did not find the Truck Object in persistance");
				return false;
			}

		} 
		catch (HibernateException e) {
			e.printStackTrace();
			return false;
		}

		finally {
			if(session!=null){
				session.close();
			}
		}
	}
	 */
	private boolean isTruckNameValid() {

		String enteredTruckName =  truckNumber.getText();
		if (enteredTruckName.isEmpty())
		{
			ToastMessage toastMessage = new ToastMessage("Truck name not valid",3000);
			toastMessage.setVisible(true);
			return false;
		}
		else return true;

	}

	// Checks if a Truck Name already exists.	
	// It is called within a hibernate transaction
	private boolean truckExistWithSameName(boolean modify) {

		String selectedTruckName = truckNumber.getText();


		// In case of modifying a Truck we need to make sure name
		// does not clash with other  names. But it can be same with current name.
		if (modify) {
			String existingName = ((Truck)rowList.getSelectedValue()).getName();
			if (selectedTruckName.equals(existingName))
			{			
				return false;
			}
		}

		Date receiveDate = receiveDatePicker.getDate();
		return Truck.truckExistisWithSameName ( selectedTruckName,   receiveDate); 

	}

	private void populateTruckEntryData(TruckEntry truckEntry) {		
		truckEntry.setTruck( (Truck)rowList.getSelectedValue());
		truckEntry.setFruit( (Fruit)fruitCombo.getSelectedItem());
		truckEntry.setFruitQuality( (FruitQuality)fruitQualityCombo.getSelectedItem());
		truckEntry.setPurchaseUnit( purchaseUnitCombo.getSelectedItem().toString());
		truckEntry.setLotNumber( lotNumber.getText());
		truckEntry.setSalesUnit( salesUnitCombo.getSelectedItem().toString()); 
		long quantityVal = Utility.getValue((Long)quantity.getValue());
		truckEntry.setQuantity  (quantityVal);		 
		truckEntry.setRemainingQuantity(quantityVal);
	}

	public class AddTruckEntryActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			if (rowList.getSelectedIndex()==-1){
				ToastMessage toastMessage = new ToastMessage("Please select a truck",3000);
				toastMessage.setVisible(true);
				return;				
			}
			if ( Utility.getValue((Long)quantity.getValue()) <=0){
				ToastMessage toastMessage = new ToastMessage("Please enter a valid quantity",3000);
				toastMessage.setVisible(true);
				return;				
			}

			TruckEntry truckEntry = new TruckEntry();
			populateTruckEntryData( truckEntry);
			if ( TruckEntry.saveTruckEntry (truckEntry) ) {
				ToastMessage toastMessage = new ToastMessage("Truck Entry Added Successfully",3000);
				toastMessage.setVisible(true);				 				
			}
			else  {
				ToastMessage toastMessage = new ToastMessage("Truck Entry Not Added",3000);
				toastMessage.setVisible(true);

			}
			/*
			try {
		//		session = HibernateUtil.getSessionFactory().openSession();
		//		session.beginTransaction();				 
				TruckEntry truckEntry = new TruckEntry();
				populateTruckEntryData( truckEntry);
			//	session.save(truckEntry);
			//	session.getTransaction().commit();					

			}
			catch (Exception ex)
			{   
				ToastMessage toastMessage = new ToastMessage("Truck Entry Not Added",3000);
				toastMessage.setVisible(true);
				System.out.println(ex.getMessage());
				ex.printStackTrace();
				return;
			}
			finally {
				if(session!=null&&session.isOpen()){
					session.close();
				}
			}
			 */
			populateTruckEntryTable();

		}

	}

	public class retrieveTruckActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Calling retrieve");
			recreateTruckList();

		}
	}

}


