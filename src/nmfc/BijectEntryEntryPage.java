package nmfc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jdesktop.swingx.JXDatePicker;

import entities.CommitData;
import entities.ContainerItems;
import entities.CreditEntry;
import entities.Customer;
import entities.CustomerTransaction;
import entities.Franchise;
import entities.Fruit;
import entities.FruitQuality;
import entities.Route;
import entities.Truck;
import entities.TruckEntry;
import entities.TruckSales;

import nmfc.CreditEntryPage.CustomTableCellRenderer;
import nmfc.TruckPage.AddTruckEntryActionListener;
import nmfc.BijectEntryEntryPage.AddCustomerEntryActionListener;
import nmfc.BijectEntryEntryPage.GetTruckByDateActionListener;
import nmfc.BijectEntryEntryPage.GetUnsoldTruckActionListener;
import nmfc.helper.CloseButton;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import persistence.HibernateUtil;

public class BijectEntryEntryPage extends JFrame{




	//private final JPanel contentPanel = new JPanel();
	private Font buttonFont = new Font("Courier", Font.BOLD,13);
	private Color buttonText = new Color(90, 10, 20);
	private Color buttonBorder = new Color(180, 100, 160);
	//private JXDatePicker receiveDatePicker;
	private JPanel itemListPanel;
	private JPanel secondinfoPanel;
	private JPanel itemEntryPanel;
	private JPanel leftPanel;
	private JScrollPane truckListScrollPane = new JScrollPane();
	private List truckList;
	private JList truckRowList;
	private JXDatePicker fromReceiveDatePicker;
	private String[] columns;
	private JTable truckEntryTable = new JTable();
	private JTable truckSaleTable = new JTable();
	private JScrollPane scrollForTruckEntryTable ;
	private JPanel ItemSellPanel=new JPanel();
	private JPanel topinfoPanel = new JPanel();
	private JPanel DiscountEntryPanel = new JPanel();
	private DefaultTableModel model;
	private Truck selectedTruck;
	private JTextField fruitQualityText = new JTextField();
	private JTextField fruitText = new JTextField();
	private JTextField purchaseUnit = new JTextField ();
	private JComboBox salesUnit = new JComboBox ();
	private JFormattedTextField quantity = new  JFormattedTextField ();
	private JFormattedTextField discountedQuantity =new  JFormattedTextField ();
	private JFormattedTextField normalRate = new  JFormattedTextField ();
	private JFormattedTextField discountedRate = new  JFormattedTextField ();
	private JFormattedTextField normalAmount = new  JFormattedTextField ();
	private JFormattedTextField discountedAmount = new  JFormattedTextField ();
	private JFormattedTextField purchaseQuantity = new  JFormattedTextField ();
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	private JComboBox saleToCutsomer;
	private Session session;
	JTextField lotNumber = new JTextField ();
	private DefaultTableModel trckSaleModel;
	private String[] truckSaleColumns;
	private JScrollPane scrollForTruckSaleTable;
	private JLabel itemFromTruckNumberLabel = new JLabel("");
	private JXDatePicker itemSaleDatePicker;

	// Constants.
	private static final int  INDEX_OF_ID_COLUMN = 0;
	private static final int  INDEX_OF_SELLING_DATE_COLUMN = 1;
	private static final int  INDEX_OF_CUSTOMER_COLUMN = 2;
	private static final int  INDEX_OF_FRUIT_COLUMN = 3;
	private static final int  INDEX_OF_QUALITY_COLUMN = 4;
	private static final int  INDEX_OF_LOT_NUMBER_COLUMN = 5;
	private static final int  INDEX_OF_PURCHASE_UNIT_COLUMN = 6;
	private static final int  INDEX_OF_QUANTITY_IN_PURCHASE_UNIT_COLUMN = 7;
	private static final int  INDEX_OF_SALES_UNIT_COLUMN = 8;
	private static final int  INDEX_OF_QUANTITY_COLUMN = 9;
	private static final int  INDEX_OF_NORMAL_RATE_COLUMN = 10;
	private static final int  INDEX_OF_NORMAL_AMOUNT_COLUMN = 11;
	private static final int  INDEX_OF_DEBIT_CREDIT_COLUMN = 12;
	private static final int  INDEX_OF_DISCOUNT_COLUMN = 13;	 	 
	private static final int  SIZE_OF_ALL_COLUMN = 14;
	private ArrayList<Object[]> tableValueList;
	private JComboBox debitCredit;
	private JLabel discountLabel;
	private JComboBox discountNonDiscount;
	private JLabel customerLabel;
	private JCheckBox isEnterbyAmount;
	private JXDatePicker toEeceiveDatePicker;





	public BijectEntryEntryPage(Frame homePage, String string, boolean b) {
	//	super (homePage,  string,  b);
		super ( string );
		getContentPane().setLayout( null );
		setBounds( 50, 50, 960, 800 );
		getContentPane().setBackground(new Color(230, 245, 240) );
		// This is required to provide help in bengali
		// Do not forget to add fonts to the <jdkORJre>/lib/fonts/fallback 
		// directory.
		UIManager.put("ToolTip.font",
				new FontUIResource("Siyamrupali_1_01", Font.BOLD, 14));

		leftPanel = new JPanel();
		leftPanel.setLayout( null );
		leftPanel.setBounds( 5, 10, 130, 725 );
		leftPanel.setBackground(new Color(200, 230, 210) );
		leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createLeftPanel();
		add(leftPanel);


		topinfoPanel.setLayout( null );
		topinfoPanel.setBounds( 136, 10, 1035, 140 );
		topinfoPanel.setBackground(new Color(200, 230, 210) );
		topinfoPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createTruckEntryTable();
		add(topinfoPanel);

		DiscountEntryPanel.setLayout( null );
		DiscountEntryPanel.setBounds( 136, 155, 335, 540 );
		DiscountEntryPanel.setBackground(new Color(200, 230, 210) );
		DiscountEntryPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createCustomerEntryList();
		add(DiscountEntryPanel);


		ItemSellPanel.setLayout( null );
		ItemSellPanel.setBounds( 510, 155, 305, 540 );
		ItemSellPanel.setBackground(new Color(200, 230, 180) );
		ItemSellPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createTruckSaleTable() ;
  

		add(ItemSellPanel);

		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 395, 745, 160, 35 );
		add(closeButton);
	}

	private void createLeftPanel() {
		JLabel selectTruckNumberLabel = new JLabel ("Select Truck");
		selectTruckNumberLabel.setBounds( 8,90,120,20);
		StyleItems.styleLabel (selectTruckNumberLabel);
		leftPanel.add (selectTruckNumberLabel);
		createUnsoldTruckList ();

		createReceiveDateEntry();

		//		createTruckList (receiveDatePicker.getDate());




	}

	private void createReceiveDateEntry() {

		JLabel selectFromEntryDateLabel = new JLabel ("From Date");
		selectFromEntryDateLabel.setBounds( 10,480,115,20);
		StyleItems.styleLabel (selectFromEntryDateLabel); 
		leftPanel.add (selectFromEntryDateLabel);

		fromReceiveDatePicker = new JXDatePicker();
		fromReceiveDatePicker.setDate(new Date());
		//StyleItems.styleButton (entryDatePicker);
		fromReceiveDatePicker.setBounds( 10,510,110,20);
		fromReceiveDatePicker.setFormats("dd/MM/yyyy");
		PropertyChangeListener listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**receiveDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("receiveDatePicker.isEditValid()  " +fromReceiveDatePicker.isEditValid());
				if ((fromReceiveDatePicker.getDate()!=null) && fromReceiveDatePicker.isEditValid()) {
					System.out.println(fromReceiveDatePicker.getDate());
					// refreshTruckList();
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		fromReceiveDatePicker.addPropertyChangeListener(listener);
		leftPanel.add (fromReceiveDatePicker);
		
		
		JLabel selectToEntryDateLabel = new JLabel ("To  Date");
		selectToEntryDateLabel.setBounds( 10,540,115,20);
		StyleItems.styleLabel (selectToEntryDateLabel); 
		leftPanel.add (selectToEntryDateLabel);

		toEeceiveDatePicker = new JXDatePicker();
		toEeceiveDatePicker.setDate(new Date());
		//StyleItems.styleButton (entryDatePicker);
		toEeceiveDatePicker.setBounds( 10,570,110,20);
		toEeceiveDatePicker.setFormats("dd/MM/yyyy");
		PropertyChangeListener todatelistener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**toEeceiveDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("toEeceiveDatePicker.isEditValid()  " +fromReceiveDatePicker.isEditValid());
				if ((toEeceiveDatePicker.getDate()!=null) && toEeceiveDatePicker.isEditValid()) {
					System.out.println(toEeceiveDatePicker.getDate());
					// refreshTruckList();
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		toEeceiveDatePicker.addPropertyChangeListener(todatelistener);
		leftPanel.add (toEeceiveDatePicker);

		JButton retrieve = new JButton ("Retrieve");
		retrieve.setBounds( 12,600,105,35);
		retrieve.setToolTipText("তারিখ অনুযায়ী ট্রাক সার্চ");   
		StyleItems.styleButton2(retrieve);
		retrieve.addActionListener(new GetTruckByDateActionListener ());
		leftPanel.add(retrieve);		



	}

	private void createUnsoldTruckList() {

		JButton getUnsoldTruck = new JButton ("Unsold Truck");
		getUnsoldTruck.setBounds( 8,15,115,35);
		getUnsoldTruck.setToolTipText("যে ট্রাকের সব মাল বিক্রি হয় নি");  
		StyleItems.styleButton2(getUnsoldTruck);
		getUnsoldTruck.addActionListener(new GetUnsoldTruckActionListener ());
		leftPanel.add(getUnsoldTruck);		

		truckListScrollPane.setBounds(6,130,115,330);
		truckList = Truck.getUnsoldTruckNames();
		truckRowList = new JList(truckList.toArray());
		truckRowList.setVisibleRowCount(8);
		truckRowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = truckRowList.getSelectedIndex();
				if (idx != -1)
				{

					if (!le.getValueIsAdjusting()) {
						System.out.println("Current selection: " + truckList.get(idx));
						populateTruckEntryTable();
						populateTruckSaleTable();
					}


				}
				else
					System.out.println("Please choose a Truck.");
			}


		}); 

		truckListScrollPane.setViewportView(truckRowList);
		truckRowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleList (truckRowList);
		leftPanel.add(truckListScrollPane);



	}

	public void createTruckEntryTable()  {

		String slectedTruckInfo ="";
		selectedTruck = (Truck)truckRowList.getSelectedValue();
		if (selectedTruck != null) {
			slectedTruckInfo =", Truck Receive Date = " + selectedTruck.getReceiveDate();
		}
		
		itemFromTruckNumberLabel.setText("Items from the selected Truck: " + slectedTruckInfo);
		itemFromTruckNumberLabel.setBounds( 120, 10, 430, 20);
		 
		
		StyleItems.styleLabel (itemFromTruckNumberLabel);
		topinfoPanel.add (itemFromTruckNumberLabel);		
		columns = new String [TruckEntryTableData.TRUCK_TOTAL_SIZE_OF_ALL_COLUMN ];     
		columns [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_ID_COLUMN] ="ID";         
		columns [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_FRUIT_COLUMN] ="<html>Fruit<br>Purchased ";      
		columns [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_QUALITY_COLUMN ]="<html>Fruit <br>Quality";    
		columns [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_LOT_NUMBER_COLUMN ]="Lot No."; 
		columns [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_PURCHASE_UNIT_COLUMN]="<html>Purchase<br>Unit";
		columns [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_SALES_UNIT_COLUMN]="Sales Unit"; 
		columns [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_QUANTITY_COLUMN] ="Quantity";   
		columns [TruckEntryTableData.TRUCK_ENTRY_INDEX_REMAINING_QUANTITY_COLUMN] ="Balance";

		truckEntryTable.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		truckEntryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleTableMediumFont(truckEntryTable);
		scrollForTruckEntryTable  = new JScrollPane(truckEntryTable); 
		populateTruckEntryTable();
		scrollForTruckEntryTable.setBounds( 10, 30, 1010, 100 );
		truckEntryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (truckEntryTable.getSelectedRow() > -1) {
					// print first column value from selected row
					//     System.out.println("Value = " + truckEntryTable.getValueAt(truckEntryTable.getSelectedRow(), 0).toString());
					if (!event.getValueIsAdjusting()) {
						fillCustomerEntryItems();
					}

				}
			}
		});
		topinfoPanel.add(scrollForTruckEntryTable);


	}

	public void createTruckSaleTable() 

	{   
		truckSaleColumns = new String[SIZE_OF_ALL_COLUMN ];
		truckSaleColumns [INDEX_OF_ID_COLUMN ]= "ID";                
		truckSaleColumns [INDEX_OF_SELLING_DATE_COLUMN] ="<html>Selling<br> Date";      
		truckSaleColumns [INDEX_OF_CUSTOMER_COLUMN ]=  "Customer";          
		truckSaleColumns [INDEX_OF_FRUIT_COLUMN] = "Fruit";             
		truckSaleColumns [INDEX_OF_QUALITY_COLUMN ]= "Quality";           
		truckSaleColumns [INDEX_OF_LOT_NUMBER_COLUMN] = "Lot No."; 				
		truckSaleColumns [ INDEX_OF_PURCHASE_UNIT_COLUMN]= "<html>Purchase<br> Unit";                
		truckSaleColumns [ INDEX_OF_QUANTITY_IN_PURCHASE_UNIT_COLUMN] = "<html>Quantity in Purchase<br> Unit";                 
		truckSaleColumns [  INDEX_OF_DEBIT_CREDIT_COLUMN] = "<html>Debit/<br>Credit";              
		truckSaleColumns [  INDEX_OF_DISCOUNT_COLUMN] = "<html>Discount/<br>Non-Discount"; 	             	 				
		truckSaleColumns [INDEX_OF_SALES_UNIT_COLUMN ]= "<html>Sales<br> Unit";        
		truckSaleColumns [INDEX_OF_QUANTITY_COLUMN] = "Quantity";          
		truckSaleColumns [INDEX_OF_NORMAL_RATE_COLUMN] = "<html>Normal<br> Rate";       
		truckSaleColumns [INDEX_OF_NORMAL_AMOUNT_COLUMN] = "<html>Normal<br> Amount";
		  
		//truckSaleColumns [INDEX_OF_TOTAL_COLUMN] = "Total" ;            

		SimpleHeaderRenderer headerRenderForTRuckSale = new SimpleHeaderRenderer();
		headerRenderForTRuckSale.setFont(new Font("Consolas", Font.BOLD, 10)); 
		truckSaleTable.getTableHeader().setDefaultRenderer(headerRenderForTRuckSale);
		truckSaleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleTableMediumFont2(truckSaleTable);
		scrollForTruckSaleTable  = new JScrollPane(truckSaleTable); 
		populateTruckSaleTable();
		scrollForTruckSaleTable.setBounds( 4, 10, 970, 370 );
		truckSaleTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (truckSaleTable.getSelectedRow() > -1) {
					// print first column value from selected row
					//  System.out.println("Value = " + truckEntryTable.getValueAt(truckSaleTable.getSelectedRow(), 0).toString());
					//   fillCustomerEntryItems();
				}
			}
		});
		ItemSellPanel.add(scrollForTruckSaleTable);

	}
	/**
	 * Fills the data entry row of customer based on product selected for a truck
	 */
	protected void fillCustomerEntryItems() {

		Long trckEntryId = (Long) model.getValueAt(truckEntryTable.getSelectedRow(), 0);
		//Integer trckEntryId = (Integer) truckEntryTable.getValueAt(truckEntryTable.getSelectedRow(), 0);
		TruckEntry tr = TruckEntry.getTruckEntry(trckEntryId);
		fruitQualityText.setText(tr.getFruitQuality().toString());
		fruitText.setText(tr.getFruit().toString());
		lotNumber.setText(tr.getLotNumber());
		purchaseUnit.setText( tr.getPurchaseUnit());
		markSalesUnit (tr.getSalesUnit().toString());
		//	reapaintCustomerEntryPanel();

	}


	private void reapaintCustomerEntryPanel() {
		fruitQualityText.repaint();
		fruitText.repaint();
		lotNumber.repaint();		
	}


	/** Create Truck List based on receive date 
	 * 
	 * @param truckReciveDate
	 */
	public void createTruckList (Date fromTruckReciveDate, Date toTruckReciveDate) {

		truckListScrollPane.setBounds(10,120,105,230);
		truckList = Truck.getTruckNames(fromTruckReciveDate, toTruckReciveDate);
		truckRowList = new JList(truckList.toArray());
		truckRowList.setVisibleRowCount(8);
		truckRowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = truckRowList.getSelectedIndex();
				if (idx != -1)
				{

					if (!le.getValueIsAdjusting()) {
						System.out.println("Current selection: " + truckList.get(idx));
						populateTruckEntryTable();
						populateTruckSaleTable();
					}


				}
				else
					System.out.println("Please choose a Truck.");
			}


		}); 		
		truckListScrollPane.setViewportView(truckRowList);
		truckRowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleList (truckRowList);
		leftPanel.add(truckListScrollPane);
	}

	private void createCustomerEntryList() {
		JLabel dateLabel = new JLabel ("Date");
		dateLabel.setBounds( 10,10,90,20);
		StyleItems.styleComponentSmall2 (dateLabel);
		DiscountEntryPanel.add(dateLabel);

		itemSaleDatePicker = new JXDatePicker();
		itemSaleDatePicker.setDate(new Date());
		StyleItems.styleComponentSmall2(itemSaleDatePicker);
		itemSaleDatePicker.setBounds( 10,35,105,20);
		itemSaleDatePicker.setFormats("dd/MM/yyyy");		
		DiscountEntryPanel.add (itemSaleDatePicker);


		JLabel fruitLabel = new JLabel ("Fruit");
		fruitLabel.setBounds( 130,10,90,20);
		StyleItems.styleComponentSmall2 (fruitLabel);
		DiscountEntryPanel.add(fruitLabel);

		StyleItems.styleComponentSmall2 (fruitText);
		fruitText.setBounds( 130,35,85,20);
		fruitText.setEditable(false);
		DiscountEntryPanel.add(fruitText);


		JLabel qualityLabel = new JLabel ("Quality");
		qualityLabel.setBounds( 230,10,95,20);
		StyleItems.styleComponentSmall2 (qualityLabel);
		DiscountEntryPanel.add(qualityLabel);


		fruitQualityText.setBounds( 230,35,95,20);
		StyleItems.styleComponentSmall2 (fruitQualityText);
		DiscountEntryPanel.add(fruitQualityText);
		fruitQualityText.setEditable(false);

		JLabel lotLabel = new JLabel ("Lot Number");
		lotLabel.setBounds( 340,10,105,20);
		StyleItems.styleComponentSmall2 (lotLabel);
		DiscountEntryPanel.add(lotLabel);

		lotNumber.setBounds( 340,35,75,20);
		StyleItems.styleComponentSmall2 (lotNumber);
		lotNumber.setEditable(false);
		DiscountEntryPanel.add(lotNumber);

		JLabel purchaseUnitLabel = new JLabel ("Purchase Unit");
		purchaseUnitLabel.setBounds( 460,10,125,20);
		StyleItems.styleComponentSmall2 (purchaseUnitLabel);
		purchaseUnitLabel.setToolTipText("যেভাবে কেনা হয়েছে");  
		DiscountEntryPanel.add(purchaseUnitLabel);

		purchaseUnit.setBounds( 460,35,125,20);
		StyleItems.styleComponentSmall2 (purchaseUnit);
		purchaseUnit.setToolTipText("যেভাবে কেনা হয়েছে");   
		purchaseUnit.setEditable(false);
		DiscountEntryPanel.add(purchaseUnit);



		JLabel purchaseQuantityLabel = new JLabel ("<html>Quantity in <br> Purchase Unit");
		purchaseQuantityLabel.setBounds( 650,03,125,30);
		StyleItems.styleComponentSmall (purchaseQuantityLabel);
		purchaseQuantityLabel.setToolTipText("যাতে কেনা হয়েছে");  
		DiscountEntryPanel.add(purchaseQuantityLabel);

		purchaseQuantity.setBounds( 650,35,65,20);
		StyleItems.styleComponentSmall (purchaseQuantity);
		purchaseQuantity.setValue(0L);
		purchaseQuantity.getDocument().addDocumentListener(new RateOrAmountChangeListener());   
		DiscountEntryPanel.add(purchaseQuantity);


		JLabel salesUnitLabel = new JLabel ("Sales Unit");
		salesUnitLabel.setBounds( 10,80,85,20);
		salesUnitLabel.setToolTipText("যেভাবে বিক্রি হচ্ছে");  
		StyleItems.styleComponentSmall (salesUnitLabel);
		DiscountEntryPanel.add(salesUnitLabel);

		List <ContainerItems> totalList = ContainerItems.getContainerItemsNames(); // item from DB 
		salesUnit = new JComboBox (totalList.toArray());
		StyleItems.styleComponentSmall (salesUnit);
		salesUnit.setToolTipText("যেভাবে বিক্রি হচ্ছে");  
		salesUnit.setBounds( 10,105,85,20);
		DiscountEntryPanel.add(salesUnit);

		JLabel quantityLabel = new JLabel ("<html> Quantity in <br> Sales Unit");
		quantityLabel.setBounds( 130,70,125,30);
		StyleItems.styleComponentSmall (quantityLabel);
		DiscountEntryPanel.add(quantityLabel);

		quantity.setBounds( 130,105,65,20);
		StyleItems.styleComponentSmall (quantity);
		quantity.setValue(0.0f);
		quantity.getDocument().addDocumentListener(new RateOrAmountChangeListener());   
		DiscountEntryPanel.add(quantity);


		JLabel normalRateLablel = new JLabel ("Rate");
		normalRateLablel.setBounds( 240,80,105,20);
		StyleItems.styleComponentSmall ( normalRateLablel);
		DiscountEntryPanel.add( normalRateLablel);

		normalRate.setBounds( 240,105,65,20);
		StyleItems.styleComponentSmall (normalRate);
		normalRate.setValue(0.0f);
		normalRate.getDocument().addDocumentListener(new RateOrAmountChangeListener());   
		DiscountEntryPanel.add(normalRate);

		JLabel normalAmountLabel = new JLabel ("Amount");
		normalAmountLabel.setBounds( 360,85,85,20);
		StyleItems.styleComponentSmall (normalAmountLabel); 
		normalAmountLabel.setToolTipText("কত টাকার মাল"); 
		DiscountEntryPanel.add(normalAmountLabel);


		JLabel isSOldByAmount = new JLabel ("Is sold by amount");
		isSOldByAmount.setBounds( 270,60,105,20);
		StyleItems.styleComponentSmall (isSOldByAmount); 
		DiscountEntryPanel.add(isSOldByAmount);

		isEnterbyAmount = new JCheckBox ();
		isEnterbyAmount.setBounds( 380,65,20,15);	
		ActionListener actionListener = new ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
		        boolean selected = abstractButton.getModel().isSelected();
		       if (selected) {
		    	   normalAmount.setEditable(true); 
		    	   normalRate.setEditable(false); 
		       } 
		       else {
		    	   normalAmount.setEditable(false); 
		    	   normalRate.setEditable(true); 
		       }
		         
		      }
		    };
		isEnterbyAmount.addActionListener(actionListener);
		StyleItems.styleComponentSmall (isEnterbyAmount);
		isEnterbyAmount.setToolTipText("টোটাল টাকায় কেনা মাল");  
		DiscountEntryPanel.add(isEnterbyAmount);


		normalAmount.setBounds( 360,105,65,20);
		StyleItems.styleComponentSmall (normalAmount);
		normalAmount.setToolTipText("কত টাকার মাল");  
		normalAmount.setEditable(false);
		normalAmount.setValue(0.0f);
		normalAmount.getDocument().addDocumentListener(new RateOrAmountChangeListener());   
		DiscountEntryPanel.add(normalAmount);


		JLabel debitCreditLabel = new JLabel ("Debit/Credit");
		debitCreditLabel.setBounds( 455,80,85,20);
		debitCreditLabel.setToolTipText("নগদে না ধারে ");
		StyleItems.styleComponentSmall (debitCreditLabel);
		DiscountEntryPanel.add(debitCreditLabel);

		debitCredit = new JComboBox (new String [] {"Credit", "Debit"});
		StyleItems.styleComponentSmall (debitCredit);
		debitCredit.setToolTipText("নগদে না ধারে "); 
		debitCredit.addItemListener (new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED) {
					String Item = (String) itemEvent.getItem();
					if ("Credit".equals(Item)) {
						discountLabel.setVisible( true);
						discountNonDiscount.setVisible( true);
					}
					else {
						discountLabel.setVisible( false);
						discountNonDiscount.setVisible( false);	  
					}
				}	          
			}
		});
		debitCredit.setBounds( 455,105,85,20);
		DiscountEntryPanel.add(debitCredit);

		discountLabel = new JLabel ("Discount/Non-Discount");
		discountLabel.setBounds( 590,80,135,20);
		discountLabel.setToolTipText("কাশের মাল না ছাড়ের মাল?");
		StyleItems.styleComponentSmall (discountLabel);
		DiscountEntryPanel.add(discountLabel);

		discountNonDiscount = new JComboBox (new String [] {"Discount", "Non-Discount"});
		StyleItems.styleComponentSmall (discountNonDiscount);
		discountNonDiscount.setToolTipText("কাশের মাল না ছাড়ের মাল?"); 
		discountNonDiscount.setBounds( 590,105,135,20);
		DiscountEntryPanel.add(discountNonDiscount);


		customerLabel = new JLabel ("Customer");
		customerLabel.setBounds( 785,80,110,20);
		StyleItems.styleComponentSmall (customerLabel);
		DiscountEntryPanel.add(customerLabel);

		List <Customer> custrList = Customer.getCustomerLists();
		saleToCutsomer = new JComboBox (custrList.toArray());
		//JTextField franchise = new JTextField ();
		saleToCutsomer.setBounds( 785,105,140,20);
		StyleItems.styleComponentSmall (saleToCutsomer);		 
		DiscountEntryPanel.add(saleToCutsomer);

		JButton saveTruckEntry = new JButton ("+");
		saveTruckEntry.setBounds( 890,35,45,35);
		saveTruckEntry.setFont(StyleItems.veryBigButtonFont);
		saveTruckEntry.setBackground(StyleItems.lightYellowBackGround);
		saveTruckEntry.setToolTipText("নতুন আইটেম যোগ করুন");  
		//StyleItems.styleLabel (saveTruckEntry);
		saveTruckEntry.addActionListener(new AddTruckSaleActionListener ());
		DiscountEntryPanel.add(saveTruckEntry);


	}
	
	/**
	 * Select the correct SalesUnit from the dropdown.
	 */
 
	private void markSalesUnit (String selectedSalesUnit){
		
		int sizeOfSalesUnit = salesUnit.getModel().getSize();
		salesUnit.setSelectedIndex(-1); // First de-select the route	
		 
		for (int i = 0; i <sizeOfSalesUnit ; i++) {

			String salesUnitString =  salesUnit.getModel().getElementAt(i).toString();
			
			if (selectedSalesUnit.equals(salesUnitString))
			{
				salesUnit.setSelectedIndex(i);
				salesUnit.repaint();
				return;
			}

		}
 
	}
	 
/*
	private void createCommitTruckSaleItemButton (){
		JButton commitEntryButton = new JButton("Update Ledger");
		commitEntryButton.setBounds( 520, 385, 160, 20 );
		commitEntryButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Commit?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					if (trckSaleModel.getRowCount() > 0 && truckSaleTable.getSelectedRow() != -1 )
					{
						System.out.println ( " Selcted Row ==  " + truckSaleTable.getSelectedRow() );
						Object selctedTableObject = trckSaleModel.getValueAt(truckSaleTable.getSelectedRow(), 0);

						Object dataCommitted = trckSaleModel.getValueAt
								(truckSaleTable.getSelectedRow(), INDEX_OF_DATA_COMMITTED); 
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
						System.out.println ( "selctedTableObject=" + selctedTableObject);
						Long itemEntryId  = (Long) selctedTableObject;
						commitTrckSaleEntryRow (itemEntryId);
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
		bottominfoPanel.add(commitEntryButton);
	}
*/

/*
	protected void commitTrckSaleEntryRow(Long itemTrucKSaleId) {
		if (CommitData.commitTruckSaleItem(itemTrucKSaleId))  {			 
			System.out.println ("Committed Sucessfully");
			ToastMessage toastMessage = new ToastMessage("Committed Successfully",3000);
			toastMessage.setVisible(true); 
			populateTruckSaleTable();
		}
		else {
			System.out.println (" Truck Sale Could Not be Committed");
			ToastMessage toastMessage = new ToastMessage("Could Not be Committed",3000);
			toastMessage.setVisible(true); 
		}

	}
*/

/* 	private void createDeleteTruckSaleItemButton (){
		JButton deleteEntryButton = new JButton("Delete Item Entry");
		deleteEntryButton.setBounds( 50, 385, 160, 20 );
		deleteEntryButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					if (trckSaleModel.getRowCount() > 0 && truckSaleTable.getSelectedRow() != -1 )
					{
						System.out.println ( " Selcted Row ==  " + truckSaleTable.getSelectedRow() );
						Object selctedTableObject = trckSaleModel.getValueAt(truckSaleTable.getSelectedRow(),
								INDEX_OF_ID_COLUMN );
						System.out.println ( "selcted Truck Sale TableObject=" + selctedTableObject);

						Object dataCommitted = trckSaleModel.getValueAt
								(truckSaleTable.getSelectedRow(), INDEX_OF_DATA_COMMITTED); 
						boolean isDataCommitted =  Utility.getValue( (Boolean) dataCommitted  );
						if (isDataCommitted) {
							ToastMessage toastMessage = new ToastMessage("Can not delete committed data",3000);
							toastMessage.setVisible(true);
							return;							
						}

						Long itemEntryId  = (Long) selctedTableObject;
						deleteTrckSaleEntryRow (itemEntryId);
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
		bottominfoPanel.add(deleteEntryButton);
	}
*/

	/*   // Commented as any modification will be automatically upadted and saved
	private void createModifyTruckSaleItemButton (){
		JButton saveEntryButton = new JButton("Modify Item Entries");
		saveEntryButton.setBounds( 280, 220, 160, 20 );
		saveEntryButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				if (trckSaleModel.getRowCount() > 0 && truckSaleTable.getSelectedRow() != -1 )
				{
					System.out.println ( " Selcted Roww ==  " + truckSaleTable.getSelectedRow() );
					Object selctedTableObject = trckSaleModel.getValueAt(truckSaleTable.getSelectedRow(), 0);
					//      System.out.println ( "selctedTableObject=" + selctedTableObject);
					Long itemEntryId  = (Long) selctedTableObject;
					modifyTrckSaleEntries (itemEntryId);
				}
				else {

					System.out.println("Please choose an item.");
					ToastMessage toastMessage = new ToastMessage("Please choose an item",3000);
					toastMessage.setVisible(true);
					return;

				}
			}}
				);


		bottominfoPanel.add(saveEntryButton);
	}
	 */
	/*
	private void modifyTrckSaleEntries(Long itemTrucKSaleId) {

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction(); 
			Serializable id = itemTrucKSaleId;
			Object persistentInstance = session.load(TruckSales.class, itemTrucKSaleId);
			TruckSales truckSaleItems = (TruckSales)persistentInstance;
			populateTruckSaleEntryFromTable (truckSaleItems);			 
			session.save(truckSaleItems);
			session.getTransaction().commit();
			ToastMessage toastMessage = new ToastMessage("Truck Sale Entry modified Successfully",3000);
			toastMessage.setVisible(true);

		}
		catch (Exception ex)
		{
			ToastMessage toastMessage = new ToastMessage("Truck Sale Entry not modified",3000);
			toastMessage.setVisible(true);
		}

		finally {
			if(session!=null){
				session.close();
			}
		}

		//	recreateTruckList ();


	}

	 */

	/*
	 * 
	 * This method takes the changes in TruckSaleEntryTable and upadates
	 * the slected truck object accordingly. However this method does 
	 * not persist the object. After this trucksales items should 
	 * be saved.
	 */
	/*
	private void populateTruckSaleEntryFromTable(TruckSales truckSaleItems) {
		int row = truckSaleTable.getSelectedRow();
		// As one column (ID) is hidden, therefore index -1 is used;
		float rate =  (Float) (truckSaleTable.getValueAt(row,INDEX_OF_NORMAL_RATE_COLUMN-1));

		float amount = (Float) truckSaleTable.getValueAt(row, INDEX_OF_NORMAL_AMOUNT_COLUMN-1);

		float quantity =  (Float) truckSaleTable.getValueAt(row, INDEX_OF_QUANTITY_COLUMN -1);

		//		String discString =  truckSaleTable.getValueAt(row, 9) + "";
		//		float discntPrice = Float.parseFloat(discString);

		truckSaleItems.setAmount(amount);
		truckSaleItems.setRate(rate);
		truckSaleItems.setQuantity(quantity);;
		//		truckSaleItems.setDiscountPrice(discntPrice);	
	}

	 */

	protected void deleteTrckSaleEntryRow(Long itemTrucKSaleId) {
		if (TruckSales.deleteTruckSaleItem(itemTrucKSaleId))  {			 
			System.out.println ("Deleted Sucessfully");
			ToastMessage toastMessage = new ToastMessage("Deleted Successfully",3000);
			toastMessage.setVisible(true); 
			populateTruckEntryTable();
			populateTruckSaleTable();
		}
		else {
			System.out.println ("Did not find the Truck Object in persistance");
			ToastMessage toastMessage = new ToastMessage("Could Not Delete",3000);
			toastMessage.setVisible(true); 
		}

	}


	/**
	 * Populates Truck Sale Data into a table
	 * Each record is added as array to Table
	 * Model and displayed in separate row 
	 * of the table.
	 */
	private void populateTruckSaleTable() {
		selectedTruck = (Truck)truckRowList.getSelectedValue();
		// Always ensure all columns are taken care of. 
		// Otherwise getColumnClass will throw null pointer exception
		final Class columnClass [] = new Class [SIZE_OF_ALL_COLUMN];
		columnClass [ INDEX_OF_ID_COLUMN ]= Long.class;               
		columnClass [ INDEX_OF_SELLING_DATE_COLUMN ] = Date.class;     
		columnClass [ INDEX_OF_CUSTOMER_COLUMN ] = String.class;         
		columnClass [ INDEX_OF_FRUIT_COLUMN ] = String.class;            
		columnClass [ INDEX_OF_QUALITY_COLUMN]  = String.class;          
		columnClass [ INDEX_OF_LOT_NUMBER_COLUMN] = String.class; 		
		columnClass [ INDEX_OF_PURCHASE_UNIT_COLUMN ] = String.class;       
		columnClass [ INDEX_OF_QUANTITY_IN_PURCHASE_UNIT_COLUMN ]  = Float.class; 
		columnClass [ INDEX_OF_SALES_UNIT_COLUMN] = String.class;  
		columnClass [ INDEX_OF_QUANTITY_COLUMN]  = Float.class; 		
		columnClass [ INDEX_OF_NORMAL_RATE_COLUMN]  = Float.class ; 
		columnClass [ INDEX_OF_NORMAL_AMOUNT_COLUMN ] = Float.class;  
		columnClass [INDEX_OF_DEBIT_CREDIT_COLUMN  ] = String.class;
		columnClass [ INDEX_OF_DISCOUNT_COLUMN]  = String.class ;      
		 


		List  truckSaleEntryList = TruckSales.getTruckSalesList(selectedTruck);
		tableValueList = new ArrayList <Object []>();
		for (Object s : truckSaleEntryList) {
			Object[] obj = new Object[SIZE_OF_ALL_COLUMN];
			TruckSales truckSaleEntry = (TruckSales) s; 
			obj[INDEX_OF_ID_COLUMN] = truckSaleEntry.getId();  // Id will be hidden to user,
			// used for accessing database
			obj[INDEX_OF_SELLING_DATE_COLUMN] = truckSaleEntry.getSellingDate();
			obj[INDEX_OF_CUSTOMER_COLUMN] = truckSaleEntry.getCustomer();
			obj[INDEX_OF_FRUIT_COLUMN] = truckSaleEntry.getFruit();
			obj[INDEX_OF_QUALITY_COLUMN ] = truckSaleEntry.getFruitQuality();
			obj[INDEX_OF_LOT_NUMBER_COLUMN] = truckSaleEntry.getLotNumber();

			obj[INDEX_OF_PURCHASE_UNIT_COLUMN ] =truckSaleEntry.getPurchaseUnit();       
			obj[ INDEX_OF_QUANTITY_IN_PURCHASE_UNIT_COLUMN ] = truckSaleEntry.getQuantityInPurchaseUnit();

			obj[INDEX_OF_SALES_UNIT_COLUMN] = truckSaleEntry.getSalesUnit();
			obj[INDEX_OF_QUANTITY_COLUMN] = truckSaleEntry.getQuantity();
			obj[INDEX_OF_NORMAL_RATE_COLUMN] = truckSaleEntry.getRate() ;
			obj[INDEX_OF_NORMAL_AMOUNT_COLUMN] = truckSaleEntry.getAmount();

			boolean isCashPuchase = Utility.getValue((Boolean) truckSaleEntry.getIsDebitPurchase());
			obj[INDEX_OF_DEBIT_CREDIT_COLUMN  ]  =  isCashPuchase ? "Cash":"Credit"; 
			if (!isCashPuchase) {
				obj[ INDEX_OF_DISCOUNT_COLUMN]  =   Utility.getValue((Boolean)
						truckSaleEntry.getIsDiscounted())?"Discounted":"Non-Discounted"; 
			}
			else {
				obj[ INDEX_OF_DISCOUNT_COLUMN] ="";
			}
 		tableValueList.add(obj);

		}

		trckSaleModel = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {	
				  return false;
			}

			public Class getColumnClass(int c) {
				return columnClass [c];
			}


		};
		trckSaleModel.setColumnIdentifiers(truckSaleColumns);

		for (Object s : tableValueList) {
			Object [] objArr =  (Object []) s;			  	
			trckSaleModel.addRow(objArr);
		}

		trckSaleModel.addTableModelListener (new TableModelListener  () {

			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				System.out.println(row + "-- " + column);
				TableModel model = (TableModel)e.getSource();
				String columnName = model.getColumnName(column);		       				
				switch (column) {
				case  INDEX_OF_QUANTITY_COLUMN : {
					// Falls through
				}
				case  INDEX_OF_NORMAL_RATE_COLUMN: {
					Object quantityObj = model.getValueAt(row, INDEX_OF_QUANTITY_COLUMN);
					float quanityValue =  Utility.getValue( (Float)quantityObj);

					Object rateValObj = model.getValueAt(row, INDEX_OF_NORMAL_RATE_COLUMN);
					float rate =  Utility.getValue( (Float)rateValObj);

					float normalAmountVal = quanityValue*rate;
					model.setValueAt(normalAmountVal, row, INDEX_OF_NORMAL_AMOUNT_COLUMN);					
					long truckSaleId = Utility.getValue(  
							(Long) model.getValueAt(row, INDEX_OF_ID_COLUMN));

					TruckSales.saveModifiedQuantityRateAmount(truckSaleId, quanityValue, rate, normalAmountVal);
					populateTruckSaleTable();
				}

				}

			}});


		truckSaleTable.setModel(trckSaleModel);
		truckSaleTable.removeColumn( truckSaleTable.getColumnModel().getColumn(INDEX_OF_ID_COLUMN)); //hiding
		CustomTableCellRenderer customTableCellRenderer = new CustomTableCellRenderer();
		// Color the comiited data as green and otherwise red
		truckSaleTable.setDefaultRenderer( Boolean.class, customTableCellRenderer );
	}

	private void populateTruckEntryTable() {
		selectedTruck = (Truck)truckRowList.getSelectedValue();	
		String slectedTruckInfo ="";		 
		if (selectedTruck != null) {
			slectedTruckInfo =" (Truck number=" + selectedTruck.getName() + ", Receive Date= " 
		+ formatter.format (selectedTruck.getReceiveDate()) +")";
		}
		
		itemFromTruckNumberLabel.setText("Items from the selected Truck: " + slectedTruckInfo);
		itemFromTruckNumberLabel.setBounds( 120, 10, 690, 20);
		
		
		List  truckEntryList = TruckEntry.getTruckEntryList(selectedTruck);
		model = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {
				//all cells false
				return false;
			}
		};
		model.setColumnIdentifiers(columns);
		for (Object trckList : truckEntryList) {
			Object[] obj = new Object[ TruckEntryTableData.TRUCK_TOTAL_SIZE_OF_ALL_COLUMN];
			TruckEntry truckEntry = (TruckEntry) trckList;
			obj [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_ID_COLUMN] = truckEntry.getId();;       
			obj [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_FRUIT_COLUMN] =truckEntry.getFruit(); 
			obj [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_QUALITY_COLUMN ]=truckEntry.getFruitQuality();
			obj [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_LOT_NUMBER_COLUMN ]=truckEntry.getLotNumber();
			obj [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_PURCHASE_UNIT_COLUMN]=truckEntry.getPurchaseUnit();
			obj [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_SALES_UNIT_COLUMN]=truckEntry.getSalesUnit();
			obj [TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_QUANTITY_COLUMN] =truckEntry.getQuantity();
			obj [TruckEntryTableData.TRUCK_ENTRY_INDEX_REMAINING_QUANTITY_COLUMN]= truckEntry.getRemainingQuantity();

			model.addRow(obj);

		}
		truckEntryTable.setModel(model);
		truckEntryTable.removeColumn( truckEntryTable.getColumnModel().getColumn(
				TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_ID_COLUMN));

	}


	protected void refreshTruckList() {
		System.out.println("Calling refreshTruckList");
		truckList = Truck.getUnsoldTruckNames(); 
		System.out.println("Size = " + truckList.size());
		truckRowList.setListData(truckList.toArray());
		truckListScrollPane.repaint();
	}

	protected void refreshTruckList(Date fromDate, Date toDate) {
		System.out.println("Calling refreshTruckList");
		truckList = Truck.getTruckNames(fromDate, toDate); 
		System.out.println("Size = " + truckList.size());
		truckRowList.setListData(truckList.toArray());
		truckListScrollPane.repaint();
	}

	protected void refreshTruckEntryTables() {


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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			BijectEntryEntryPage dialog = new BijectEntryEntryPage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(1050, 655);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class AddTruckSaleActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			if (truckEntryTable==null || truckEntryTable.getSelectedRow()==-1) {
				ToastMessage toastMessage = new ToastMessage("Select a truck sale entry first",3000);
				toastMessage.setVisible(true);
				return;

			}

			if ( ( Utility.getValue ((Float)quantity.getValue() ) <=0) ||
					( Utility.getValue ((Long)purchaseQuantity.getValue() )<=0)
					){ 
				ToastMessage toastMessage = new ToastMessage("Please enter valid quantity",3000);
				toastMessage.setVisible(true);
				return;
			}

			if ( ( Utility.getValue ((Float)normalRate.getValue() )<=0)  ){ 
				ToastMessage toastMessage = new ToastMessage("Please enter valid rate",3000);
				toastMessage.setVisible(true);
				return;
			}
			
			// If Customer Credit Balance Exceeded Then Do not allow entry----
			Customer custEntry =  (Customer)saleToCutsomer.getSelectedItem();
			float lastBalance = CustomerTransaction.getLastBalance (custEntry);		
			float creditLimit = Utility.getValue(custEntry.getCreditLImit());
			float finalBalance = lastBalance +   (float) normalAmount.getValue(); 		
			if  (creditLimit!=0  && finalBalance >creditLimit) {			
				ToastMessage toastMessage = new ToastMessage("Customer Credit Limit Exceeded",3000);
				toastMessage.setVisible(true);	
				return;
			}
			// ------------
			
			TruckSales truckSale = new TruckSales();
			enterTruckSaleData( truckSale);
			
			JDialog page=new UpdateTruckSaleEntry( BijectEntryEntryPage.this, "Truck Sale Datea Update", true, truckSale);
			page.setSize(580, 450);
			page.setVisible(true);
	
			populateTruckEntryTable();
			populateTruckSaleTable();
		//	resetValues (); // Not required
		}
	}

	private void enterTruckSaleData(TruckSales truckSaleItem) {		
		truckSaleItem.setItemsFromTruck( (Truck)truckRowList.getSelectedValue());
		Long trckEntryId =(Long) model.getValueAt
				(truckEntryTable.getSelectedRow(), TruckEntryTableData.TRUCK_ENTRY_INDEX_OF_ID_COLUMN);
		
		TruckEntry tr = TruckEntry.getTruckEntry(trckEntryId);
		truckSaleItem.setTruckEntry(tr);
		truckSaleItem.setFruit ( tr.getFruit());
		truckSaleItem.setSellingDate(itemSaleDatePicker.getDate()); 

		truckSaleItem.setCustomer((Customer)saleToCutsomer.getSelectedItem());
		truckSaleItem.setFruitQuality ( tr.getFruitQuality());
		truckSaleItem.setPurchaseUnit( purchaseUnit.getText());
		truckSaleItem.setLotNumber(tr.getLotNumber());
		truckSaleItem.setSalesUnit( salesUnit.getSelectedItem().toString()); 

		truckSaleItem.setQuantity( (Float)(quantity.getValue()));		
		truckSaleItem.setRate( (Float) normalRate.getValue());
		truckSaleItem.setAmount( (Float) normalAmount.getValue());
		truckSaleItem.setQuantityInPurchaseUnit((Long)purchaseQuantity.getValue());

		if (  "Debit".equalsIgnoreCase((String) debitCredit.getSelectedItem()) ) {
			truckSaleItem.setIsDebitPurchase(true); 
		}
		else {
			truckSaleItem.setIsDebitPurchase(false); 
		}

		if (  "Discount".equalsIgnoreCase((String) discountNonDiscount.getSelectedItem()) ) {
			truckSaleItem.setIsDiscounted(true);
		}
		else {
			truckSaleItem.setIsDiscounted(false);
		}

	}
	
	// Not to be used right now
	
	public void resetValues () {
		
		quantity.setValue(0.0f);
		purchaseQuantity.setValue(0l);
		normalRate.setValue(0.0f);
		normalAmount.setValue(0.0f);
	}
	
	/**
	 * This listener is attached with quantity and rate text field. If fields
	 * are updated then total amount is also changed
	 * @author prabir
	 *
	 */
	public class RateOrAmountChangeListener implements DocumentListener  {

		public void changedUpdate(DocumentEvent e) {			  
		 	setAmount(e);
		}
		public void removeUpdate(DocumentEvent e) {
			setAmount(e);
		}
		public void insertUpdate(DocumentEvent e) {
			setAmount(e);
		}

		public void setAmount(DocumentEvent e) {

			boolean isSoldInAmount = Utility.getValue(isEnterbyAmount.isSelected());

			// If quantity or rate is changed then
			// amount is changed accordingly
			if (!isSoldInAmount) {
 				if (e.getDocument()== quantity.getDocument()
						|| e.getDocument()== normalRate.getDocument()){
					float quantityVal = Utility.getValue((float)quantity.getValue());
					float rateVal = Utility.getValue((float)normalRate.getValue());
					float totalVal = quantityVal * rateVal;
					normalAmount.setValue(totalVal);			      
				}	
			}
			// Similarly change  the rate if amount is entered 
			else {
				
				if (e.getDocument()== quantity.getDocument()
						|| e.getDocument()== normalAmount.getDocument()){
					float quantityVal = Utility.getValue((float)quantity.getValue());
					float amountval = Utility.getValue((float)normalAmount.getValue());
					float rateVal =0;
					if (amountval!=0) 
					{
						rateVal = (float) Math.round(100* amountval/quantityVal ) /100 ;						 
						normalRate.setValue(rateVal);	
					}
							      
				}
				
			}
	 
		}
	}

	public class TruckEntryTableData {	 
		final static int TRUCK_ENTRY_INDEX_OF_ID_COLUMN =0;
		final static int TRUCK_ENTRY_INDEX_OF_FRUIT_COLUMN =1;
		final static int TRUCK_ENTRY_INDEX_OF_QUALITY_COLUMN =2;
		final static int TRUCK_ENTRY_INDEX_OF_LOT_NUMBER_COLUMN =3;
		final static int TRUCK_ENTRY_INDEX_OF_PURCHASE_UNIT_COLUMN =4;		
		final static int TRUCK_ENTRY_INDEX_OF_SALES_UNIT_COLUMN =5;
		final static int TRUCK_ENTRY_INDEX_OF_QUANTITY_COLUMN =6;
		final static int TRUCK_ENTRY_INDEX_REMAINING_QUANTITY_COLUMN =7;
		final static int TRUCK_TOTAL_SIZE_OF_ALL_COLUMN =8;
	}

	public class GetUnsoldTruckActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			refreshTruckList ();

		}

	}


	public class GetTruckByDateActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			refreshTruckList (fromReceiveDatePicker.getDate(), toEeceiveDatePicker.getDate());

		}

	}

	public class AddCustomerEntryActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
