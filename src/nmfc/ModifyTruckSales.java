package nmfc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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

import entities.ContainerItems;
import entities.CreditEntry;
import entities.Customer;
import entities.CustomerTransaction;
import entities.Route;
import entities.TruckSales;
import nmfc.BillPrint.GenerateBillPrintReportListener;
import nmfc.CreditEntryPage.CustomTableCellRenderer;
import nmfc.TruckSalePage.RateOrAmountChangeListener;
import nmfc.helper.CloseButton;
import nmfc.helper.SimpleHeaderRenderer;
import nmfc.helper.StandardItems;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import pdftables.AllCustomerDueReport;
import pdftables.BillPrintPDF;
import pdftables.RouteWiseReportPDF;

public class ModifyTruckSales extends JDialog{
	 
	private JPanel rightPanel;
 
	private String[] customerLedgerColumns;
	private  JTable customerLedgerTable = new JTable ();
	private JScrollPane scrollForCustomerLedgerTable;
	private DefaultTableModel customerLedgerModel;
	private JXDatePicker fromEntryDatePicker = new JXDatePicker();
	private JXDatePicker toEntryDatePicker = new JXDatePicker();
	private Customer selectedCustomer;
	private DefaultTableModel creditEntryModel;
	private Class[] columnClasses;
	TruckSales trckSale;

	private JXDatePicker itemSaleDatePicker;
	 
	public ModifyTruckSales (JFrame parentPage, String string, boolean b, Long trckSaleId) {
		super (parentPage,  string,  b);
		TruckSales truckSale  = TruckSales.getTrckSaleEntries (trckSaleId);	
		this.trckSale=truckSale;
		getContentPane().setLayout( null );
		setBounds( 160, 140, 650, 450 );
		getContentPane().setBackground(new Color(230, 245, 240) );
	 	 
		rightPanel = new JPanel();
		rightPanel.setLayout( null );
		rightPanel.setBounds( 10, 10, 610, 420 );
		rightPanel.setBackground(new Color(200, 250, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(rightPanel);
 
	}

	private void createRightPanel() {
 	 
		createItemDetail ();
		createRouteReportButton ();
		createCancelButton ();

	}
	
	private void createItemDetail() {
		
		JLabel dateLabel = new JLabel ("Date of Sale: ");
		dateLabel.setBounds( 10,10,120,20);
		StyleItems.styleComponentSmall2 (dateLabel);
		rightPanel.add(dateLabel);
 
		itemSaleDatePicker = new JXDatePicker();
		itemSaleDatePicker.setDate(new Date());
		StyleItems.styleComponentSmall2(itemSaleDatePicker);
		itemSaleDatePicker.setBounds( 120,10,105,20);
		itemSaleDatePicker.setFormats("dd/MM/yyyy");
	//	itemSaleDatePicker.setDate(trckSale.getSellingDate() );
		rightPanel.add (itemSaleDatePicker);
		
		JLabel customerLabel = new JLabel ("Customer: " + trckSale.getCustomer());
		customerLabel.setBounds( 290,10,110,20);
		StyleItems.styleComponentSmall (customerLabel);
		rightPanel.add(customerLabel);
 

		JLabel fruitLabel = new JLabel ("Fruit:                       " 
				+ trckSale.getFruit());
		fruitLabel.setBounds( 10,50,200,20);
		StyleItems.styleComponentSmall2 (fruitLabel);
		rightPanel.add(fruitLabel);

		 

		JLabel qualityLabel = new JLabel ("Quality:                      " 
				+ trckSale.getFruitQuality());
		qualityLabel.setBounds( 290,50,95,20);
		StyleItems.styleComponentSmall2 (qualityLabel);
		rightPanel.add(qualityLabel);

    
		JLabel purchaseUnitLabel = new JLabel ("Purchase Unit:    " + trckSale.getPurchaseUnit());
		purchaseUnitLabel.setBounds( 10,80,200,20);
		StyleItems.styleComponentSmall2 (purchaseUnitLabel);
		purchaseUnitLabel.setToolTipText("যেভাবে কেনা হয়েছে");  
		rightPanel.add(purchaseUnitLabel);
 
		JLabel purchaseQuantityLabel = new JLabel ( "Quantity in purchase unit: " +trckSale.getQuantityInPurchaseUnit() );
		purchaseQuantityLabel.setBounds( 290,80,200,30);
		StyleItems.styleComponentSmall (purchaseQuantityLabel);
		purchaseQuantityLabel.setToolTipText("যাতে কেনা হয়েছে");  
		rightPanel.add(purchaseQuantityLabel);

	 

		JLabel salesUnitLabel = new JLabel ("Sales Unit: " + trckSale.getSalesUnit() );
		salesUnitLabel.setBounds( 10,110,145,20);
		salesUnitLabel.setToolTipText("যেভাবে বিক্রি হচ্ছে");  
		StyleItems.styleComponentSmall (salesUnitLabel);
		rightPanel.add(salesUnitLabel);

	  
		JLabel quantityLabel = new JLabel ("Quantity in Sales Unit: " + trckSale.getQuantity());
		quantityLabel.setBounds( 290,110,225,30);
		StyleItems.styleComponentSmall (quantityLabel);
		rightPanel.add(quantityLabel);
 

		JLabel normalRateLablel = new JLabel ("Rate: " + trckSale.getRate());
		normalRateLablel.setBounds( 10,140,205,20);
		StyleItems.styleComponentSmall ( normalRateLablel);
		rightPanel.add( normalRateLablel);

		 

		JLabel normalAmountLabel = new JLabel ("Amount: " + trckSale.getAmount());
		normalAmountLabel.setBounds( 290,140,185,20);
		StyleItems.styleComponentSmall (normalAmountLabel); 
		normalAmountLabel.setToolTipText("কত টাকার মাল"); 
		rightPanel.add(normalAmountLabel);
   	 
		String debitCreditString = "Credit";
		if (trckSale.getIsDebitPurchase()) {
			debitCreditString = "Debit"; 
		}

		JLabel debitCreditLabel = new JLabel ("Type of Sale: " + debitCreditString);
		debitCreditLabel.setBounds( 10,170,185,20);
		debitCreditLabel.setToolTipText("নগদে না ধারে ");
		StyleItems.styleComponentSmall (debitCreditLabel);
		rightPanel.add(debitCreditLabel);
 
		String discountString = "Not Applicable";
		if (! Utility.getValue( trckSale.getIsDebitPurchase())) {
			discountString =  Utility.getValue(trckSale.getIsDiscounted())?"Discounted":"Non Discounted"; 
		}
		JLabel discountLabel = new JLabel ("Discount/Non-Discount: " + discountString );
		discountLabel.setBounds( 10,200,235,20);
		discountLabel.setToolTipText("কাশের মাল না ছাড়ের মাল?");
		StyleItems.styleComponentSmall (discountLabel);
		rightPanel.add(discountLabel);

	 

		
	}

	private void createCancelButton () {

		JButton cancelButton = new CloseButton (this);
		cancelButton.setBounds( 280, 340, 100, 25 );
		rightPanel.add(cancelButton);
	}

	
	private void	createRouteReportButton () {
		
		JButton updateData = new JButton ("Update");
		updateData.setBounds( 10,340,100,25);
		updateData.setForeground(StyleItems.buttonTextColor);
		updateData.setFont(StyleItems.buttonFont);
		updateData.setBorder(new LineBorder(new Color(20, 180, 70), 2));
		 
		updateData.addActionListener(new upadteTruckSaleData ());
		rightPanel.add(updateData);
	}

 

	public class upadteTruckSaleData   implements ActionListener {

		public void actionPerformed(ActionEvent evt) {
 		 
			if (TruckSales.addNewEntry (trckSale)) {				
				ToastMessage toastMessage = new ToastMessage("Truck Entry Added Successfully",3000);
				toastMessage.setVisible(true);
				System.out.println("disposing the window..");
				setVisible(false);
				dispose(); 
			}
			else {
				ToastMessage toastMessage = new ToastMessage("Truck Sale Entry Not Added",3000);
				toastMessage.setVisible(true);

			}
			 
 
		}
	}

 

	/**                                                       
	 * Launch the applicSIZE_OF_ALL_COLUMN = 14;              
	 */
	public static void main(String[] args) {
		 
	}



}