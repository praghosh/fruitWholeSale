package nmfc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXDatePicker;

import nmfc.TruckPage.retrieveTruckActionListener;
import nmfc.helper.StyleItems;

public class SearchTruckPage extends JDialog {
	
	private JPanel topPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JXDatePicker fromReceiveDatePicker;
	private JXDatePicker fromEntryDatePicker;
	JTextField truckNumber = new JTextField ();
	private JXDatePicker toReceiveDatePicker;
	private JXDatePicker toEntryDatePicker;
	
	
	public SearchTruckPage (Frame homePage, String string, boolean b) {
		super (homePage,  string,  b);
		getContentPane().setLayout( null );
		setBounds( 120, 100, 820, 450 );
		getContentPane().setBackground(new Color(230, 245, 240) );


		topPanel.setLayout( null );
		topPanel.setBounds( 10, 10, 610, 140 );
		topPanel.setBackground(new Color(200, 230, 210) );
		topPanel.setBorder(new LineBorder(new Color(10, 90, 70), 2));
		createTopPanel();
		
		bottomPanel.setLayout( null );
		bottomPanel.setBounds( 10, 155, 610, 400 );
		bottomPanel.setBackground(new Color(180, 230, 230) );
		bottomPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createBottomPanel();
	}


	private void createBottomPanel() {
		 add(bottomPanel);
		
	}


	private void createTopPanel() {
		
		JLabel receiveDateLabel = new JLabel ("Receive Date");
		receiveDateLabel.setBounds( 10,10,105,20);
		StyleItems.styleComponent (receiveDateLabel);
		topPanel.add(receiveDateLabel);
		
		JLabel fromDateLabel = new JLabel ("From");
		fromDateLabel.setBounds( 165,10,85,20);
		StyleItems.styleComponent (fromDateLabel);
		topPanel.add(fromDateLabel);
		
		fromReceiveDatePicker = new JXDatePicker();
		fromReceiveDatePicker.setDate(new Date());
		// styleComponent (receiveDatePicker);
		fromReceiveDatePicker.setBounds( 215,10,120,30);
		fromReceiveDatePicker.setFormats("dd/MM/yyyy");
		StyleItems.styleDatePicker(fromReceiveDatePicker);
		topPanel.add (fromReceiveDatePicker);
		
		
		JLabel toDateLabel = new JLabel ("To");
		toDateLabel.setBounds( 395,10,65,20);
		StyleItems.styleComponent (toDateLabel);
		topPanel.add(toDateLabel);
		
		toReceiveDatePicker = new JXDatePicker();
		toReceiveDatePicker.setDate(new Date());
		// styleComponent (receiveDatePicker);
		toReceiveDatePicker.setBounds( 425,10,120,30);
		toReceiveDatePicker.setFormats("dd/MM/yyyy");
		StyleItems.styleDatePicker(toReceiveDatePicker);
		topPanel.add (toReceiveDatePicker);
		
		


		JLabel entryDateLabel = new JLabel ("Entry Date");
		entryDateLabel.setBounds( 10,50,95,20);
		StyleItems.styleComponent (entryDateLabel);
		topPanel.add (entryDateLabel);

		
		
		JLabel fromReceiveDateLabel = new JLabel ("From");
		fromReceiveDateLabel.setBounds( 165,50,85,20);
		StyleItems.styleComponent (fromReceiveDateLabel);
		topPanel.add(fromReceiveDateLabel);
		
		fromEntryDatePicker = new JXDatePicker();
		fromEntryDatePicker.setDate(new Date());
		StyleItems.styleDatePicker (fromEntryDatePicker);
		fromEntryDatePicker.setBounds( 215,50,120,30);
		fromEntryDatePicker.setFormats("dd/MM/yyyy");
		topPanel.add (fromEntryDatePicker);	 

		JLabel toEntryDateLabel = new JLabel ("To");
		toEntryDateLabel.setBounds( 395,50,65,20);
		StyleItems.styleComponent (toEntryDateLabel);
		topPanel.add(toEntryDateLabel);
		
		toEntryDatePicker = new JXDatePicker();
		toEntryDatePicker.setDate(new Date());
		// styleComponent (receiveDatePicker);
		toEntryDatePicker.setBounds( 425,50,120,30);
		toEntryDatePicker.setFormats("dd/MM/yyyy");
		StyleItems.styleDatePicker(toEntryDatePicker);
		topPanel.add (toEntryDatePicker);
		
	 

		JLabel truckNumberLabel = new JLabel ("Truck Number");
		truckNumberLabel.setBounds( 10,95,115,20);
		StyleItems.styleComponent (truckNumberLabel);
		topPanel.add(truckNumberLabel);

	//	truckNumber = new JTextField ();
		truckNumber.setBounds( 125,95,115,20);
		StyleItems.styleComponent (truckNumber);
		truckNumber.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		topPanel.add(truckNumber);

		JButton retrieve = new JButton ("Retrive");
		retrieve.setBounds( 300,95,90,20);
		StyleItems.styleLabel (retrieve);
		retrieve.addActionListener(new retrieveTruckDataActionListener ());
		topPanel.add(retrieve);	
		add(topPanel); 
		
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			SearchTruckPage dialog = new SearchTruckPage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(660, 620);
			dialog.setVisible(true);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	class retrieveTruckDataActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("calling retrieve");

		}
		
	}
}
