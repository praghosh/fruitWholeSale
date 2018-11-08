package nmfc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXDatePicker;

import entities.Truck;
import entities.TruckEntry;
import entities.TruckSales;

import nmfc.helper.CloseButton;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import pdftables.CreateTableUtitlity;
import pdftables.CreateTruckSalePDFReport;

public class TruckWiseSaleReport    extends JFrame{


	private JPanel leftPanel= new JPanel();
	private JPanel rightPanel= new JPanel();
	private JXDatePicker fromReceiveDatePicker;
	private JXDatePicker toReceiveDatePicker;
	private JScrollPane listScrollPane = new JScrollPane();
	private List truckList;
	private JList rowList;
	private JTable truckEntryTable = new JTable();
	private JTable truckSaleTable = new JTable();
	private JScrollPane scrollForTruckEntryTable;
	private DefaultTableModel model;
	String [] truckSaleColumns;
	String[] columns;
	// Constants.
	private static final int  INDEX_OF_ID_COLUMN = 0;
	private static final int  INDEX_OF_SELLING_DATE_COLUMN = 1;
	private static final int  INDEX_OF_CUSTOMER_COLUMN = 2;
	private static final int  INDEX_OF_FRUIT_COLUMN = 3;
	private static final int  INDEX_OF_QUALITY_COLUMN = 4;
	private static final int  INDEX_OF_LOT_NUMBER_COLUMN = 5;
	
	private static final int  INDEX_OF_QUANTITY_IN_PURCHASE_COLUMN = 6;
	private static final int  INDEX_OF_PURCHASE_UNIT_COLUMN = 7;
	
	private static final int  INDEX_OF_QUANTITY_COLUMN = 8;
	private static final int  INDEX_OF_SALES_UNIT_COLUMN = 9;
	private static final int  INDEX_OF_NORMAL_RATE_COLUMN = 10;
	private static final int  INDEX_OF_NORMAL_AMOUNT_COLUMN = 11;
//	private static final int  INDEX_OF_DISCOUNTED_AMOUNT_COLUMN = 10;
//	private static final int  INDEX_OF_TOTAL_COLUMN = 11;
	private static final int  SIZE_OF_ALL_COLUMN = 12;

	JScrollPane scrollForTruckSaleTable = new JScrollPane();
	private Truck selectedTruck;
	private DefaultTableModel trckSaleModel;
	JFileChooser fc = new JFileChooser();
	private JLabel totalSell;



	public TruckWiseSaleReport(Frame homePage, String string, boolean b) {
	//	super (homePage,  string,  b);
		super ( string );
		getContentPane().setLayout( null );
		setBounds( 50, 50, 860, 720 );
		getContentPane().setBackground(new Color(230, 245, 240) );

		leftPanel = new JPanel();
		leftPanel.setLayout( null );
		leftPanel.setBounds( 5, 20, 130, 600 );
		leftPanel.setBackground(new Color(200, 230, 210) );
		leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createLeftPanel();
		add(leftPanel);


		rightPanel.setLayout( null );
		rightPanel.setBounds( 138, 20, 850, 600 );
		rightPanel.setBackground(new Color(200, 230, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);
		
		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 445, 640, 160, 35 );
		add(closeButton);

	}

	private void createRightPanel() {
		createTruckEntryTable();
		createTotalSellLabel();
		createTruckSaleTable();
		createReportButtons ();
	}

	private void createTotalSellLabel() {
		totalSell = new JLabel (" ");
		totalSell.setBounds( 490,500,200,20);
		StyleItems.styleLabel (totalSell);
		rightPanel.add (totalSell);		

	}

	private void createReportButtons() {
		JButton genarateReport = new JButton ("Generate Report");
		genarateReport.setBounds( 320,550,180,20);
		StyleItems.styleLabel (genarateReport);
		genarateReport.addActionListener(new GenerateTruckWiseReportListener ());
		rightPanel.add(genarateReport);
		
	}

	private void createLeftPanel() {
		JLabel selectEntryDateLabel = new JLabel ("Receive Date");
		selectEntryDateLabel.setBounds( 10,10,115,20);
		StyleItems.styleLabel (selectEntryDateLabel); 
		leftPanel.add (selectEntryDateLabel);

	/*	JLabel fromDateLabel = new JLabel ("On Date");
		fromDateLabel.setBounds( 10,40,115,20);
		StyleItems.styleLabel (fromDateLabel); 
		leftPanel.add (fromDateLabel);
		*/


		fromReceiveDatePicker = new JXDatePicker();
		fromReceiveDatePicker.setDate(new Date());
		//StyleItems.styleButton (entryDatePicker);
		fromReceiveDatePicker.setBounds( 10,60,110,20);
		fromReceiveDatePicker.setFormats("dd/MM/yyyy");	
		PropertyChangeListener listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**receiveDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("receiveDatePicker.isEditValid()  " +fromReceiveDatePicker.isEditValid());
				if ((fromReceiveDatePicker.getDate()!=null) && fromReceiveDatePicker.isEditValid()) {
					System.out.println(fromReceiveDatePicker.getDate());
					refreshTruckList();
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		fromReceiveDatePicker.addPropertyChangeListener(listener); 		
		leftPanel.add (fromReceiveDatePicker);
 
 
// This part is commmentetd as date range does not work fine in hibernate.
	/*
		JLabel toDateLabel = new JLabel ("To");
		toDateLabel.setBounds( 10,100,115,20);
		StyleItems.styleLabel (toDateLabel); 
		leftPanel.add (toDateLabel);
// Currently to date is not working as between does not work fine in hiberanate

		toReceiveDatePicker = new JXDatePicker();
		toReceiveDatePicker.setDate(new Date());
		//StyleItems.styleButton (entryDatePicker);
		toReceiveDatePicker.setBounds( 10,120,110,20);
		toReceiveDatePicker.setFormats("dd/MM/yyyy");
 		leftPanel.add (toReceiveDatePicker);


		JButton retrieve = new JButton ("Retrive");
		retrieve.setBounds( 10,180,90,20);
		StyleItems.styleLabel (retrieve);
		retrieve.addActionListener(new RetrieveTruckWiseReportListener ());
		leftPanel.add(retrieve);
*/

		JLabel selectTruckNumberLabel = new JLabel ("Select Truck");
		selectTruckNumberLabel.setBounds( 10,60,115,20);
		StyleItems.styleLabel (selectTruckNumberLabel);
		leftPanel.add (selectTruckNumberLabel);		
		// 	createTruckList (fromReceiveDatePicker.getDate());

		createTruckList (fromReceiveDatePicker.getDate());


	}

	/** Create Truck List based on receive date range
	 * 
	 * @param formTruckReciveDate 
	 *  ( @param toTruckReciveDate --- to be added later, not working now)
	 *  
	 * 
	 */
	public void createTruckList (Date formTruckReciveDate) {

		listScrollPane.setBounds( 10,90,105,250);
		truckList = Truck.getTruckNames(formTruckReciveDate);
		rowList = new JList(truckList.toArray());
		rowList.setVisibleRowCount(8);
		rowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = rowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " + truckList.get(idx));
					refreshTruckEntryData();
					populateTruckSaleTable();

				}
				else
					System.out.println("Please choose a Truck.");
			}


		}); 		
		listScrollPane.setViewportView(rowList);
		//listScrollPane.revalidate();
		//listScrollPane.repaint();
		rowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		leftPanel.add(listScrollPane);
	}

	protected void refreshTruckList() {
		System.out.println("Calling refreshTruckList");
		truckList = Truck.getTruckNames(fromReceiveDatePicker.getDate());
		System.out.println("Size = " + truckList.size());
		rowList.setListData(truckList.toArray());
		listScrollPane.repaint();
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
		
		truckSaleColumns [  INDEX_OF_QUANTITY_IN_PURCHASE_COLUMN] =  "<html>Qty in<br>Purchase Unit";
		truckSaleColumns [  INDEX_OF_PURCHASE_UNIT_COLUMN] =  "Purchase Unit";
		
		truckSaleColumns [INDEX_OF_SALES_UNIT_COLUMN ]= "<html>Sales<br> Unit";        
		truckSaleColumns [INDEX_OF_QUANTITY_COLUMN] = "Quantity";          
		truckSaleColumns [INDEX_OF_NORMAL_RATE_COLUMN] = "<html>Normal<br> Rate";       
		truckSaleColumns [INDEX_OF_NORMAL_AMOUNT_COLUMN] = "<html>Normal<br> Amount";     
	//	truckSaleColumns [INDEX_OF_DISCOUNTED_AMOUNT_COLUMN] = "<html>Discounted<br> Amount";
//		truckSaleColumns [INDEX_OF_TOTAL_COLUMN] = "Total" ;            

		SimpleHeaderRenderer headerRenderForTRuckSale = new SimpleHeaderRenderer();
		headerRenderForTRuckSale.setFont(new Font("Consolas", Font.BOLD, 11)); 
		truckSaleTable.getTableHeader().setDefaultRenderer(headerRenderForTRuckSale);
		truckSaleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleComponent(truckSaleTable); 
		scrollForTruckSaleTable  = new JScrollPane(truckSaleTable); 
		populateTruckSaleTable();
		scrollForTruckSaleTable.setBounds( 10, 170, 800, 300 );
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
		rightPanel.add(scrollForTruckSaleTable);
	}

	protected void populateTruckSaleTable() {
		JLabel truckwiseSaleDetailLabel =new  JLabel ();
		truckwiseSaleDetailLabel .setText("Truckwise Sale Detail");

		truckwiseSaleDetailLabel.setBounds( 180, 140, 250, 20);
		StyleItems.styleLabel (truckwiseSaleDetailLabel);
		rightPanel.add (truckwiseSaleDetailLabel);		
		
		
		selectedTruck = (Truck)rowList.getSelectedValue();
		List  truckSaleEntryList = TruckSales.getTruckSalesList(selectedTruck);
		final Class columnClass [] = new Class [SIZE_OF_ALL_COLUMN];
		columnClass [ INDEX_OF_ID_COLUMN ]= Integer.class;               
		columnClass [ INDEX_OF_SELLING_DATE_COLUMN ] = Date.class;     
		columnClass [ INDEX_OF_CUSTOMER_COLUMN ] = String.class;         
		columnClass [ INDEX_OF_FRUIT_COLUMN ] = String.class;            
		columnClass [ INDEX_OF_QUALITY_COLUMN]  = String.class;          
		columnClass [ INDEX_OF_LOT_NUMBER_COLUMN] = String.class;       
		columnClass [ INDEX_OF_SALES_UNIT_COLUMN] = String.class;       
		columnClass [ INDEX_OF_QUANTITY_COLUMN]  = String.class;         
		columnClass [ INDEX_OF_NORMAL_RATE_COLUMN]  = String.class ;      
		columnClass [ INDEX_OF_NORMAL_AMOUNT_COLUMN ] = String.class;    
//		columnClass [ INDEX_OF_DISCOUNTED_AMOUNT_COLUMN ] = Float.class;
	//	columnClass [ INDEX_OF_TOTAL_COLUMN ] = Float.class;   

		trckSaleModel = new DefaultTableModel(){

			@Override
			public boolean isCellEditable(int row, int column) {		    
				return false;
			}

			public Class getColumnClass(int c) {
				
				if (columnClass [c]!=null) {
					return columnClass [c];
				}
				else {
					return String.class;
				}
				
			}


		};
		trckSaleModel.setColumnIdentifiers(truckSaleColumns);
		float totalSellAmount =0L;
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
			
			obj[INDEX_OF_QUANTITY_IN_PURCHASE_COLUMN] =  truckSaleEntry.getQuantityInPurchaseUnit();     
			obj[INDEX_OF_PURCHASE_UNIT_COLUMN] =truckSaleEntry.getPurchaseUnit()  ;            
			
			
			obj[INDEX_OF_SALES_UNIT_COLUMN] = truckSaleEntry.getSalesUnit();
			obj[INDEX_OF_QUANTITY_COLUMN] = truckSaleEntry.getQuantity();
			obj[INDEX_OF_NORMAL_RATE_COLUMN] = truckSaleEntry.getRate() ;
			obj[INDEX_OF_NORMAL_AMOUNT_COLUMN] = truckSaleEntry.getAmount();
			totalSellAmount+= Utility.getValue( truckSaleEntry.getAmount());
	//		obj[INDEX_OF_DISCOUNTED_AMOUNT_COLUMN] = truckSaleEntry.getDiscountPrice();			  	
			trckSaleModel.addRow(obj);
		}
		totalSell.setText("Total Sell = " + (long) totalSellAmount );
		trckSaleModel.addTableModelListener (new TableModelListener  () {

			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				System.out.println(row + "-- " + column);
				TableModel model = (TableModel)e.getSource();
				String columnName = model.getColumnName(column);		       
				Object data = model.getValueAt(row, column);
				switch (column) {
				case  INDEX_OF_QUANTITY_COLUMN : {
					data = model.getValueAt(row, column);
					System.out.println();
					float value = ((Float)data).floatValue();
					Object rateVal = model.getValueAt(row, INDEX_OF_NORMAL_RATE_COLUMN);
					float rate = (rateVal ==null)? 0:  (float) rateVal  ;

					float aValue = value*rate;
					model.setValueAt(aValue, row, INDEX_OF_NORMAL_AMOUNT_COLUMN);
				}

				}

				System.out.println(data + "  --" + columnName);

			}});

		truckSaleTable.setModel(trckSaleModel);
		truckSaleTable.removeColumn( truckSaleTable.getColumnModel().getColumn(0)); //hiding
	}

		
	

	protected void refreshTruckEntryData() { 
		System.out.println("Calling refreshTruckEntryData");
		populateTruckEntryTable();
		scrollForTruckEntryTable.repaint();

	}

	public class RetrieveTruckWiseReportListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			refreshTruckList ();

		}

	}

	public void createTruckEntryTable()  {

		JLabel itemFromTruckNumberLabel =new  JLabel ();
		itemFromTruckNumberLabel  .setText("Items from the selected Truck");

		itemFromTruckNumberLabel.setBounds( 180, 10, 250, 20);
		StyleItems.styleLabel (itemFromTruckNumberLabel);
		rightPanel.add (itemFromTruckNumberLabel);		

		columns = new String[] {"ID",
				"Fruit", "Quality"
				, "Lot No.", 
				"Purchase \n Unit", "Sales Unit", "Quantity" 
		};

		truckEntryTable.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		truckEntryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		StyleItems.styleComponent(truckEntryTable);
		scrollForTruckEntryTable  = new JScrollPane(truckEntryTable); 
		populateTruckEntryTable();
		scrollForTruckEntryTable.setBounds( 10, 30, 770, 100 );
		/*	truckEntryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (truckEntryTable.getSelectedRow() > -1) {
					// print first column value from selected row
					//     System.out.println("Value = " + truckEntryTable.getValueAt(truckEntryTable.getSelectedRow(), 0).toString());
					fillCustomerEntryItems();
				}
			}
		});
		 */
		rightPanel.add(scrollForTruckEntryTable);
	}



	private void populateTruckEntryTable() {
		Truck selectedTruck = (Truck)rowList.getSelectedValue();
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
			Object[] obj = new Object[7];
			TruckEntry truckEntry = (TruckEntry) trckList;

			obj[0] = truckEntry.getId();	
			obj[1] = truckEntry.getFruit();
			obj[2] = truckEntry.getFruitQuality();
			obj[3] = truckEntry.getLotNumber();
			obj[4] = truckEntry.getPurchaseUnit();
			obj[5] = truckEntry.getSalesUnit();
			obj[6] = truckEntry.getQuantity();

			model.addRow(obj);

		}
		truckEntryTable.setModel(model);
		truckEntryTable.removeColumn( truckEntryTable.getColumnModel().getColumn(0));

	}
	public class GenerateTruckWiseReportListener   implements ActionListener {
		
		
		public void actionPerformed(ActionEvent evt) {
			int idx = rowList.getSelectedIndex();
			if (idx == -1){
				System.out.println ("Please select a truck");
				ToastMessage toastMessage = new ToastMessage("Please choose a Truck",3000);
				toastMessage.setVisible(true);
				return;
 			
			}
	 		
				try {

					fc.setDialogTitle("Create a file to create report");

					fc.setCurrentDirectory(fc.getCurrentDirectory());

					System.out.println("File=" + fc.getSelectedFile());
					if (fc.showSaveDialog(TruckWiseSaleReport.this) == JFileChooser.APPROVE_OPTION) {

						String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
						String finalBackupPath = pdfReportPath;
			
						if (!pdfReportPath.endsWith(".pdf") )
						{
							finalBackupPath = pdfReportPath + 	 ".pdf";
						}
						Truck selectedTruck = (Truck)rowList.getSelectedValue();
						new CreateTruckSalePDFReport().createTruckWiseSaleReport (selectedTruck, finalBackupPath);
						
						
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
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {

			TruckWiseSaleReport dialog = new TruckWiseSaleReport (null, 
					"Truck Wise Sale Report", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(900, 605);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
