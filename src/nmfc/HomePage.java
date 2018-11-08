package nmfc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;

public class HomePage extends JFrame {

	private JTabbedPane tabbedPane;
	private JPanel logOnInfoPane;
	private		JPanel		entryPanel;
	private		JPanel		reportPanel;
	private		JPanel		masterPanel;
	private		JPanel		adminPanel;
	private		JPanel	    searchPanel;
	private		JPanel	    rootPanel;
	private Font buttonFont = new Font("Courier", Font.BOLD,16);
	private Color buttonText = new Color(60, 10, 20);
	private Color buttonBorder = new Color(160, 100, 130);
	/**
	 * @wbp.nonvisual location=73,151
	 */
	private final JPanel panel = new JPanel();
	private JPanel logOutPane;
 	static Logger log = Logger.getLogger(HomePage.class.getName());
 	private Component parent;
 	private String logOnUserName="";


	public String getLogOnUserName() {
		return logOnUserName;
	}



	public void setLogOnUserName(String logOnUserName) {
		this.logOnUserName = logOnUserName;
	}



	/**
	 * Create the frame.
	 * @throws ParseException 
	 */
	public HomePage(Component parent, String logOnUserName) throws ParseException {
		super("Home Page");
		this.parent = parent;
		this.logOnUserName=logOnUserName;
		
		// Licensing part
		Date currentDate = new Date ();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date license= sdf.parse("2037-04-17");
		if (license.compareTo(currentDate) <0) {
			System.exit(DISPOSE_ON_CLOSE);
		}
		/// 
		setResizable(false);
	//	log.debug("Opening  HomePage");
	//	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setBounds(0, 20, 1040, 600);

		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(new JLabel(new ImageIcon("bin/image/background3.JPG")));


		createEntryPanel();
		createReportPanel();
		createMasterPanel();
		if ("admin".equals(logOnUserName)) {
			createAdminPanel();
		}
		if ("78692110".equals(logOnUserName)) {
			createAdminPanel();
			createSuperPanel();
		}
		
		//createSearchPanel();

		// Create a tabbed pane
		tabbedPane = new JTabbedPane();
		tabbedPane.setForeground(new Color(100, 30, 60 ));
		tabbedPane.addTab( "Entry", entryPanel );
		tabbedPane.addTab( "Report", reportPanel );
		tabbedPane.addTab( "Master", masterPanel );
		
		if ("admin".equals(logOnUserName)) {
			tabbedPane.addTab( "Admin", adminPanel );
		}
		
		if ("78692110".equals(logOnUserName)) {
			tabbedPane.addTab( "Admin", adminPanel );
			tabbedPane.addTab( "Super", rootPanel );
		}
		
	//	tabbedPane.addTab( "Search", searchPanel );
		//	tabbedPane.setLayout(new BorderLayout(0, 0));
		tabbedPane.setBackground(new Color(230, 230, 160) );
		tabbedPane.setBorder(new LineBorder(new Color(40, 80, 70), 3));
		if ("admin".equals(logOnUserName)) {
			tabbedPane.setBounds(40, 110, 385, 380);
		}
		else if ("78692110".equals(logOnUserName)) {
			tabbedPane.setBounds(40, 110, 455, 380);
			
		}
		else {
			tabbedPane.setBounds(40, 110, 340, 380);
		}
		
		tabbedPane.setFont(buttonFont);




		logOnInfoPane = new JPanel();
		logOnInfoPane.setBackground(new Color(230, 230, 210) );
		logOnInfoPane.setBorder(new LineBorder(new Color(100, 180, 170), 2));
		logOnInfoPane.setBounds(580,20, 250, 55);

		JLabel loginName = new JLabel();
		loginName.setForeground(new Color(80, 60, 70));
		loginName.setText("Logged in as: ");

		JLabel name = new JLabel();
		name.setForeground(new Color(120, 90, 70));
		name.setText(logOnUserName);


		JLabel date = new JLabel();
		date.setForeground(new Color(120, 90, 70));
		date.setText("Log on Time:" + (new java.util.Date()));

		 
		
		JButton logOut = new JButton("Log Out");
		logOut.setForeground(new Color(220, 90, 70));
		logOut.setBackground(new Color(230, 210, 210) );
		logOut.setBounds(730,90, 100, 25);
		logOut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Log Out?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					System.exit(0);
				// setVisible (false);
				// dispose();
				// HomePage.this.parent.setVisible (true);
				
			}
			}
		});
		add(logOut);
		

		logOnInfoPane.add(loginName);
		logOnInfoPane.add(name);
		logOnInfoPane.add(date);

		add(tabbedPane);
		add(logOnInfoPane);
		
		// Logs out after confirmation while closing window
		 WindowListener exitListener = new WindowAdapter() {

	            @Override
	            public void windowClosing(WindowEvent e) {
	            	int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Log Out?","Warning",dialogButton);
					if(dialogResult == JOptionPane.YES_OPTION){
						  System.exit(0);
	                  
	                }
	            }
	        };
	     addWindowListener(exitListener);

	}
	
	
	//
	//  Only root user can access this panel

	private void createSuperPanel() {
		rootPanel = new JPanel();
		rootPanel.setLayout( null );


		JButton  truck = new   JButton ("Modifiable Truck Sales");
		styleButton (truck);
		truck.setBounds( 10, 25, 280, 20 );
		truck.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{
		 		log.debug("Opening  Truck Page");
				JFrame page=new ModifiableTruckSalePage( HomePage.this, "Modifiable Truck Sales", true);
				page.setSize(1320, 960);
				page.setVisible(true);
			}});
		rootPanel.add( truck );	
		
		JButton  modifiableLedger = new   JButton ("Modifiable Customer Ledger");
		styleButton (modifiableLedger);
		modifiableLedger.setBounds( 10, 55, 280, 20 );
		modifiableLedger.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{
		 		log.debug("Modifiable Customer Ledger");
				JDialog page=new CustomerLedgerWithFullAccess( HomePage.this, "Modifiable Customer Ledger", true);
				page.setSize(1120, 880);
				page.setVisible(true);
			}});
		rootPanel.add( modifiableLedger );	
		
		JButton  modifiableTruck = new   JButton ("Modifiable Truck Entry");
		styleButton (modifiableTruck);
		modifiableTruck.setBounds( 10, 85, 280, 20 );
		modifiableTruck.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{
		 		log.debug("Modifiable Customer Ledger");
				JDialog page=new ModifiableTruckEntryPage( HomePage.this, "Modifiable Customer Ledger", true);
				page.setSize(1120, 880);
				page.setVisible(true);
			}});
		rootPanel.add( modifiableTruck );
		
		JButton  modifiableCommit= new   JButton ("Modifiable Credit Entry");
		styleButton (modifiableCommit);
		modifiableCommit.setBounds( 10, 115, 280, 20 );
		modifiableCommit.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{
		 		log.debug("Modifiable Customer Ledger");
				JDialog page=new ModifiableCreditEntry( HomePage.this, "Modifiable Credit Entry", true);
				page.setSize(1000, 630);
				page.setVisible(true);
			}});
		rootPanel.add( modifiableCommit );
		
  		log.debug("rootPanel  created");
		
	}


	
	
	
	private void createSearchPanel() {
		searchPanel = new JPanel();
		searchPanel.setLayout( null );


		JButton  search = new   JButton ("Search Truck");
		styleButton (search);
		search.setBounds( 10, 25, 220, 20 );
		search.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JDialog page=new SearchTruckPage( HomePage.this, "Search Truck", true);
				page.setSize(780, 600);
				page.setVisible(true);
			}});
		searchPanel.add( search );

		JButton  searchCustomer = new   JButton ("Search Customer");
		styleButton (searchCustomer);
		searchCustomer.setBounds( 10, 60, 220, 20 );
		searchCustomer.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{
				
				JFrame page=new TruckSalePage( HomePage.this, "Truck Sale", true);
				page.setSize(1040, 935);
				page.setVisible(true);
			}});

		searchPanel.add( searchCustomer );
		
	}

	public void styleButton(JButton button)
	{
		button.setForeground(buttonText);
		button.setFont(buttonFont);
		button.setBorder(new LineBorder(buttonBorder, 2));
	}


	public void createEntryPanel()
	{
		entryPanel = new JPanel();
		entryPanel.setLayout( null );


		JButton  truck = new   JButton ("Truck");
		styleButton (truck);
		truck.setBounds( 10, 25, 220, 20 );
		truck.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{
		 		log.debug("Opening  Truck Page");
		 		JFrame page=new TruckPage( HomePage.this, "Truck Entry", true);
				page.setSize(1120, 660);
				page.setVisible(true);
			}});
		entryPanel.add( truck );

		JButton  truckSale = new   JButton ("Truck Sale");
		styleButton (truckSale);
		truckSale.setBounds( 10, 60, 220, 20 );
		truckSale.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new TruckSalePage( HomePage.this, "Truck Sale", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(1200, 825);
				page.setVisible(true);
			}});

		entryPanel.add( truckSale );



		JButton  credit = new   JButton ("Credit Entry");
		styleButton (credit);
		credit.setBounds( 10, 95, 220, 20 );
		credit.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new CreditEntryPage( HomePage.this, "Credit Entry", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(960, 580);
				page.setVisible(true);
			}});
		entryPanel.add( credit );
 	 
 		
 		JButton  halKhata = new   JButton ("Hal Khata Entry");
		styleButton (halKhata);
		halKhata.setBounds( 10, 130, 220, 20 );
		halKhata.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new HalkhataEntryPage( HomePage.this, "Hal Khata Entry", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(1100, 835);
				page.setVisible(true);
			}});
		entryPanel.add( halKhata );
		
		JButton  bijectEntry = new   JButton ("Biject Entry");
		styleButton (bijectEntry);
		bijectEntry.setBounds( 10, 165, 220, 20 );
		bijectEntry.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new BijectEntryEntryPage( HomePage.this, "Biject Entry", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(1100, 835);
				page.setVisible(true);
			}});
		entryPanel.add( bijectEntry );
 		log.debug("Entry Panel created");

	}

	public void createReportPanel()
	{
		reportPanel = new JPanel();
		reportPanel.setLayout( null );

		JButton  routeWiseDetail = new   JButton ("Routewise Due List");
		styleButton (routeWiseDetail);
		routeWiseDetail.setBounds( 10, 25, 220, 20 );
		routeWiseDetail.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new RouteWiseDetail( HomePage.this, "Routewise Detail", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(900, 580);
				page.setVisible(true);
			}});

		reportPanel.add( routeWiseDetail );

		JButton  billPrint = new   JButton ("Bill Print");
		styleButton (billPrint);
		billPrint.setBounds( 10, 60, 220, 20 );
		billPrint.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				//JDialog page=new BillPrint( HomePage.this, "Bill Print", false);
				JFrame page = new BillPrint (HomePage.this, "Bill Print", false);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(1010, 730);
				page.setVisible(true);
			}});

		reportPanel.add( billPrint );



		JButton  trucwiseSale = new   JButton ("Truckwise Sale");
		styleButton (trucwiseSale);
		trucwiseSale.setBounds( 10, 95, 220, 20 );
		trucwiseSale.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new TruckWiseSaleReport( HomePage.this, "Truckwise Sale Detail", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(1020, 750);
				page.setVisible(true);
			}});

		reportPanel.add( trucwiseSale );

		JButton  customerLedger = new   JButton ("Customer Ledger");
		styleButton (customerLedger);
		customerLedger.setBounds( 10, 130, 220, 20 );
		customerLedger.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new CustomerLedgerPage( HomePage.this, "Customer Ledger", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(1340, 780);
				page.setVisible(true);
			}});
		reportPanel.add( customerLedger );
		
		JButton  cashSaleReport = new   JButton ("Cash Sell And Deposit");
		styleButton (cashSaleReport);
		cashSaleReport.setBounds( 10, 165, 220, 20 );
		cashSaleReport.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new CashDepositAndCashSellReport( HomePage.this, "Cash Sell And Deposit", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(980, 810);
				page.setVisible(true);
			}});
		reportPanel.add( cashSaleReport );
		
		
		JButton  customerListReport = new   JButton ("Customer Detail");
		styleButton (customerListReport);
		customerListReport.setBounds( 10, 200, 220, 20 );
		customerListReport.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JDialog page=new CustomerListReport( HomePage.this, "Customer List", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(700, 500);
				page.setVisible(true);
			}});
		reportPanel.add( customerListReport );
		
		JButton  totalDueReport = new   JButton ("Total Due Report");
		styleButton (totalDueReport);
		totalDueReport.setBounds( 10, 235, 220, 20 );
		totalDueReport.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new AllCustomersDue( HomePage.this, "Total Due", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(720, 660);
				page.setVisible(true);
			}});
		reportPanel.add( totalDueReport );
		
		JButton  totalSellReport = new   JButton ("Total Sell Report");
		styleButton (totalSellReport);
		totalSellReport.setBounds( 10, 270, 220, 20 );
		totalSellReport.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new CustomerTotalSell( HomePage.this, "Total Sell", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(720, 780);
				page.setVisible(true);
			}});
		reportPanel.add( totalSellReport );
		
		
		JButton  totalTruckSellReport = new   JButton ("Total Truck Sell Report");
		styleButton (totalTruckSellReport);
		totalTruckSellReport.setBounds( 10, 300, 220, 20 );
		totalTruckSellReport.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new TruckSaleReportPage( HomePage.this, "Total Truck Sell", true);
				page.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				page.setSize(720, 780);
				page.setVisible(true);
			}});
		reportPanel.add( totalTruckSellReport );
		

	}

	public void createMasterPanel()
	{

		masterPanel = new JPanel();
		masterPanel.setLayout( null );

		JButton  customer = new   JButton ("Customer");
		styleButton (customer);
		customer.setBounds( 10, 25, 220, 20 );
		customer.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JDialog page=new CustomerEntry( HomePage.this, "Customer", true);
				page.setSize(850, 650);
				page.setVisible(true);
			}});
		masterPanel.add( customer );

		JButton  route = new   JButton ("Route");
		styleButton (route);
		route.setBounds( 10, 60, 220, 20 );
		route.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JDialog page=new RoutePage( HomePage.this, "Route", true);
				page.setSize(550, 400);
				page.setVisible(true);
			}});

		masterPanel.add( route );



		JButton  fruit = new   JButton ("Fruit");
		styleButton (fruit);
		fruit.setBounds( 10, 95, 220, 20 );
		fruit.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JDialog page=new FruitMasterPage( HomePage.this, "Fruit Master", true);
				page.setSize(550, 440);
				page.setVisible(true);
			}});

		masterPanel.add( fruit );

		JButton  franchise = new   JButton ("Franchise");
		styleButton (franchise);
		franchise.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JDialog page=new FranchicePage( HomePage.this, "Franchice", true);
				page.setSize(740, 550);
				page.setVisible(true);
			}});
		franchise.setBounds( 10, 130, 220, 20 );
		masterPanel.add( franchise );
		
		JButton  sellingOrPurchaseUnit = new   JButton ("Sell/Purchase Unit");
		styleButton (sellingOrPurchaseUnit);
		sellingOrPurchaseUnit.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JFrame page=new ContainerItemsMaster( HomePage.this, "Sell/Purchase Unit", true);
				page.setSize(620, 550);
				page.setVisible(true);
			}});
		sellingOrPurchaseUnit.setBounds( 10, 165, 220, 20 );
		masterPanel.add( sellingOrPurchaseUnit );

	}

	public void createAdminPanel()
	{

		adminPanel = new JPanel();
		adminPanel.setLayout( null );

		JButton  opening = new   JButton ("Opening");
		styleButton (opening);
		opening.setBounds( 10, 25, 220, 20 );
		opening.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JDialog page=new CustomerOpeningBalancePage( HomePage.this, "Add Opening Balance", true);
				page.setSize(820, 580);
				page.setVisible(true);
			}});
		
		adminPanel.add( opening );
		
		
		
		JButton  createDatabse = new   JButton ("Create New Database");
		styleButton (createDatabse);
		createDatabse.setBounds( 10, 60, 220, 20 );
		createDatabse.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JDialog page=new CreateNewDatabasePage( HomePage.this, "Create New Database", true);
				page.setSize(380, 520);
				page.setVisible(true);
			}});
		adminPanel.add( createDatabse );
		

		JButton  backup = new   JButton ("Back Up");
		styleButton (backup);
		backup.setBounds( 10, 95, 220, 20 );
		backup.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{

				JDialog page=new DBBackupPage( HomePage.this, "Backup the data", true);
				page.setSize(380, 520);
				page.setVisible(true);
			}});
		adminPanel.add( backup );
		
		


		JButton  restore = new   JButton ("Restore");
		styleButton (restore);
		restore.setBounds( 10, 130, 220, 20 );
		restore.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{
				JDialog page=new RestoreDataPage( HomePage.this, "Restore the Database", true);
				page.setSize(480, 520);
				page.setVisible(true);
			}});
		adminPanel.add( restore );
		
		JButton  switchDB = new   JButton ("Switch Database");
		styleButton (switchDB);
		switchDB.setBounds( 10, 165, 220, 20 );
		switchDB.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{
				JDialog page=new SwitchDataBasePage( HomePage.this, "Switch the Database", true);
				page.setSize(380, 520);
				page.setVisible(true);
			}});
		adminPanel.add( switchDB );
		
		JButton  showDefaultCustomer = new   JButton ("Show Defaulter Customers");
		styleButton (showDefaultCustomer);
		showDefaultCustomer.setBounds( 10, 210, 220, 20 );
		showDefaultCustomer.addActionListener( new ActionListener()
		{

			public void actionPerformed(ActionEvent ae)
			{
				JFrame page=new DefaultCustomerListPage( HomePage.this, "Show Defaulter Customers", true);
				page.setSize(550, 780);
				page.setVisible(true);
			}});
		adminPanel.add( showDefaultCustomer );
		
		
		
		// DeleteCustomer is not an Admin job. 
		// If no record is attached then customer can
		// be deleted by any user
/*
		JButton  deleteCustomer = new   JButton ("Delete Customer");
		styleButton (deleteCustomer);
		deleteCustomer.setBounds( 10, 200, 220, 20 );
		adminPanel.add( deleteCustomer );
		
		*/

	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			HomePage dialog = new HomePage (null, "admin");
		//	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//	//dialog.pack();
		//	dialog.setSize(850, 560);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
