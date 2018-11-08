package nmfc;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import nmfc.helper.CloseButton;
import nmfc.helper.MySQLUtilities;
import nmfc.helper.MySqlBackup;
import nmfc.helper.ToastMessage;
import persistence.HibernateUtil;

public class SwitchDataBasePage  extends JDialog {
	
	JFileChooser fc = new JFileChooser();
	JCheckBox chkTables = new JCheckBox();   
	JTextField tfHost = new JTextField("localhost");
	JTextField  tfPort  = new  JTextField("3306");
	JTextField tfPassword = new JTextField("admin");
	JTextField tfUser = new JTextField("root");
	JButton btnConnect = new JButton("Connect");
	JButton btnSwitch = new JButton("Switch to New DB");
	JComboBox  cmbNMFCDBTables = new JComboBox ();
	JPanel topPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	private boolean isConnected = false;
	Connection conn = null;
	List tables = null;
	String url, password, uname;
	private MySQLUtilities sqlUtil; 
	
 
	
	
	public SwitchDataBasePage (Frame homepage, String title, boolean isModal ) {
		super (homepage, title, isModal);
		getContentPane(). setLayout(null);
		setBounds( 160, 140, 430, 500 );
	    getContentPane(). setBackground(new Color(230, 245, 180) );
	    
	    topPanel.setLayout( null );
		topPanel.setBounds( 10, 10, 350, 200 );
		topPanel.setBackground(new Color(200, 230, 210) );
		topPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		 
		bottomPanel.setLayout( null );
		bottomPanel.setBounds( 10, 230, 350, 200 );
		bottomPanel.setBackground(new Color(200, 230, 210) );
		bottomPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		
		createTopPane ();
		createBottomPane ();
		add(topPanel);
		add(bottomPanel);
		
		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 100, 440, 160, 35 );
		add(closeButton);;
	    
	}
	
private void createBottomPane() {
		
		
		JLabel tfNMFCDBLabel = new JLabel("List of NMFC Database");
		tfNMFCDBLabel.setBounds( 10,10 ,130  , 20 );
		bottomPanel.add (tfNMFCDBLabel);
	 
		
		cmbNMFCDBTables.setBounds( 160,10 ,115  , 20 );
		bottomPanel.add  (cmbNMFCDBTables);
		
	 	btnSwitch.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(java.awt.event.ActionEvent evt) {
				  btnSwitchActionPerformed(evt);
			  }
			});
			
			
		btnSwitch.setBounds( 160,80 ,145  , 20 );
		btnSwitch.setEnabled(false);
		bottomPanel.add  (btnSwitch);
		
		
		
	}

protected void btnSwitchActionPerformed(ActionEvent evt) {
	
 		try{
 			String url = "jdbc:mysql://" + tfHost.getText()+  "/" + cmbNMFCDBTables.getSelectedItem() ;

	    System.out.println("url = " + url);
	    HibernateUtil.buildSessionFactory (url, tfUser.getText(), tfPassword.getText());
	    ToastMessage toastMessage = new ToastMessage("Success",3000);
		toastMessage.setVisible(true);
 		}
 		catch (Exception e){
 			ToastMessage toastMessage = new ToastMessage("Failed",3000);
			toastMessage.setVisible(true);
			e.printStackTrace();
 		}
	 
}

private void createTopPane() {
	
	JLabel tfHostLabel = new JLabel("Host");
	tfHostLabel.setBounds( 50,40 ,115  , 20 );
	topPanel.add (tfHostLabel);
	
	tfHost.setBounds( 160,40 ,115  , 20 );
	topPanel.add  (tfHost);
	
	
	JLabel tfPortLabel = new JLabel("Port");
	tfPortLabel.setBounds( 50,70 ,115  , 20 );
	topPanel.add  (tfPortLabel);
	
	tfPort.setBounds( 160,70 ,115  , 20 );
	topPanel.add  (tfPort);
	
	JLabel tfUserLabel = new JLabel("User");
	tfUserLabel.setBounds(50,100 ,115  , 20 );
	topPanel.add  (tfUserLabel);
	
	tfUser.setBounds( 160,100 ,115  , 20 );
	topPanel.add  (tfUser);
	
	JLabel tfPasswdLabel = new JLabel("Password");
	tfPasswdLabel.setBounds( 50,130 ,115  , 20 );
	topPanel.add  (tfPasswdLabel);
	
	tfPassword.setBounds( 160,130 ,115  , 20 );
	topPanel.add  (tfPassword);
	
	btnConnect.setBounds( 160,160 ,115  , 20 );
	topPanel.add  (btnConnect);
	btnConnect.addActionListener(new java.awt.event.ActionListener() {
		  public void actionPerformed(java.awt.event.ActionEvent evt) {
			  try {
				btnConnectActionPerformed(evt);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		});
	
}
	
private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
	
	 
	 List <String> nmfcList = MySqlBackup.listNMFCDatabases();

	if (nmfcList.size()!= 0) {
		DefaultComboBoxModel model = new DefaultComboBoxModel( nmfcList.toArray());
		cmbNMFCDBTables.setModel( model );
		this.btnSwitch.setEnabled(true);
		isConnected = true;
		System.out.println( "Connection was  Succesfull");

	} 
	else {
		this.btnSwitch.setEnabled(false);
		isConnected = false;
		System.out.println( "Connection was not Succesfull");

	}

}  


	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			SwitchDataBasePage dialog = new SwitchDataBasePage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(380, 460);
			dialog.setVisible(true);
		}
		 catch (Exception e) {
			e.printStackTrace();
		}
	}

}
