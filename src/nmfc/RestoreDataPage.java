package nmfc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.apache.commons.io.FileUtils;

import entities.Fruit;

import nmfc.helper.CloseButton;
import nmfc.helper.MySQLUtilities;
import nmfc.helper.MySqlBackup;
import nmfc.helper.ToastMessage;
import nmfc.helper.dbasebackupdetailpanel;

public class RestoreDataPage extends JDialog {
	
	JFileChooser fc = new JFileChooser();
	JCheckBox chkTables = new JCheckBox();   
	JTextField tfHost = new JTextField("localhost");
	JTextField  tfPort  = new  JTextField("3306");
	JTextField tfPassword = new JTextField("admin");
	JTextField tfUser = new JTextField("root");
	JButton btnConnect = new JButton("Connect");
	JButton btnRestore = new JButton("Restore Database");
	JComboBox  cmbNMFCDBTables = new JComboBox ();
	JPanel topPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	private boolean isConnected = false;
	Connection conn = null;
	List tables = null;
	String url, password, uname;
	private MySQLUtilities sqlUtil;
	
	
	public RestoreDataPage(Frame homePage, String title, boolean b) {
 		super (homePage,  title,  b);
 		getContentPane().setLayout( null );
		setBounds( 160, 140, 390, 500 );
		getContentPane().setBackground(new Color(230, 245, 180) );
		
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
		add(closeButton);
	}

	private void createBottomPane() {
		
		
		JLabel tfNMFCDBLabel = new JLabel("List of NMFC Database");
		tfNMFCDBLabel.setBounds( 10,10 ,130  , 20 );
		bottomPanel.add (tfNMFCDBLabel);
	 
		
		cmbNMFCDBTables.setBounds( 160,10 ,115  , 20 );
		bottomPanel.add  (cmbNMFCDBTables);
		
	 	btnRestore.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(java.awt.event.ActionEvent evt) {
				  btnBackupActionPerformed(evt);
			  }
			});
			
			
		btnRestore.setBounds( 160,80 ,145  , 20 );
		btnRestore.setEnabled(false);
		bottomPanel.add  (btnRestore);
		
		
		
	}

	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			RestoreDataPage dialog = new RestoreDataPage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(420, 500);
			dialog.setVisible(true);
		} catch (Exception e) {
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
		
		btnConnect.setBounds( 160,160 ,145  , 20 );
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

//handles the backup button  click

	private void btnBackupActionPerformed(java.awt.event.ActionEvent evt) {
		
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Overwrite the existing Database?","Warning",dialogButton);
		if(dialogResult != JOptionPane.YES_OPTION){
			return;
		}

		try {

			fc.setDialogTitle("Create a file to BackUp database");

			fc.setCurrentDirectory(fc.getCurrentDirectory());

			System.out.println("File=" + fc.getSelectedFile());
			if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

				String backupPath = fc.getSelectedFile().getAbsolutePath();				 
				sqlUtil = new MySQLUtilities ();
				String dbName = cmbNMFCDBTables.getSelectedItem().toString();
				System.out.println( "dbname=" + dbName);
				sqlUtil.restoreDB ( backupPath,   dbName);
				
				ToastMessage toastMessage = new ToastMessage("Restoration was Successful",3000);
				toastMessage.setVisible(true);
				System.out.println( "DB Restore Successfull");
			//	xputils.showMessage("Backup Successfull");

			}

		} catch (Exception e) {

			ToastMessage toastMessage = new ToastMessage("Could not Restore the DB",3000);
			toastMessage.setVisible(true);
			e.printStackTrace();
			//xputils.showErrorMessage(e.toString());

		}}

	//method that handles the connect button

	private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
	
	 
			 List <String> nmfcList = MySqlBackup.listNMFCDatabases();
	
			if (nmfcList.size()!= 0) {
				DefaultComboBoxModel model = new DefaultComboBoxModel( nmfcList.toArray());
				cmbNMFCDBTables.setModel( model );
 				this.btnRestore.setEnabled(true);
 				isConnected = true;
				System.out.println( "Connection was  Succesfull");
 	 
			} 
			else {
				this.btnRestore.setEnabled(false);
				isConnected = false;
				System.out.println( "Connection was not Succesfull");
	 
			}

		}  
	
	
	
	 
	
}
