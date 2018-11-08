package nmfc.helper;
//search for this import on the net

import java.awt.Color;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;

import nmfc.FruitMasterPage;


public class dbasebackupdetailpanel extends javax.swing.JDialog {

	JFileChooser fc = new JFileChooser();
	JCheckBox chkTables = new JCheckBox();   
	JTextField tfHost = new JTextField("localhost");
	JTextField  tfPort  = new  JTextField("3306");
	JTextField tfPassword = new JTextField("admin");
	JTextField tfUser = new JTextField("root");
	JButton btnConnect = new JButton("Connect");
	JButton btnBackup = new JButton("Backup");
	JComboBox  cmbTables = new JComboBox ();
	
	
	
	
	
	private boolean isConnected = false;

	Connection conn = null;

	List tables = null;

	String url, password, uname;

	
	public dbasebackupdetailpanel(Frame homePage, String string, boolean b) {
 		super (homePage,  string,  b);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 545, 220 );
		getContentPane().setBackground(new Color(230, 245, 180) );
		
		createPane ();
	}
	
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			dbasebackupdetailpanel dialog = new dbasebackupdetailpanel (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(540, 430);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void createPane() {
		
		//fc.setBounds( 160,10 ,150  , 60 );
	//	add (fc);
		JLabel tfHostLabel = new JLabel("Host");
		tfHostLabel.setBounds( 50,70 ,115  , 20 );
		add (tfHostLabel);
		
		tfHost.setBounds( 160,70 ,115  , 20 );
		add (tfHost);
		
		
		JLabel tfPortLabel = new JLabel("Port");
		tfPortLabel.setBounds( 50,110 ,115  , 20 );
		add (tfPortLabel);
		
		tfPort.setBounds( 160,110 ,115  , 20 );
		add (tfPort);
		
		JLabel tfUserLabel = new JLabel("User");
		tfUserLabel.setBounds(50,150 ,115  , 20 );
		add (tfUserLabel);
		
		tfUser.setBounds( 160,150 ,115  , 20 );
		add (tfUser);
		
		JLabel tfPasswdLabel = new JLabel("Password");
		tfPasswdLabel.setBounds( 50,190 ,115  , 20 );
		add (tfPasswdLabel);
		
		tfPassword.setBounds( 160,190 ,115  , 20 );
		add (tfPassword);
		
		btnConnect.setBounds( 160,220 ,115  , 20 );
		add (btnConnect);
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
		
		chkTables.setBounds( 160,250 ,115  , 20 );
		add (chkTables);
		
		cmbTables.setBounds( 160,290 ,115  , 20 );
		add (cmbTables);
		
		btnBackup.setBounds( 160,320 ,115  , 20 );
		add (btnBackup);
		
		btnBackup.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(java.awt.event.ActionEvent evt) {
				  btnBackupActionPerformed(evt);
			  }
			});
			
			
		btnBackup.setBounds( 160,350 ,115  , 20 );
		add (btnBackup);
		
		
		
		
		
	}
	
	
	//handles the backup button  click

	private void btnBackupActionPerformed(java.awt.event.ActionEvent evt) {

		try {

			fc.setDialogTitle("Create a file to BackUp database");

			fc.setCurrentDirectory(fc.getCurrentDirectory());

			if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

				File file = new File(fc.getSelectedFile().getAbsolutePath() + ".sql");

				String data = null;

				if (chkTables.isSelected() == false) {

					data = this.getData(tfHost.getText(), tfPort.getText()  , tfUser.getText(),
							tfPassword.getText(), "dbFStation");

				} else {

					data = this.getData(tfHost.getText(),  tfPort.getText(), tfUser.getText(), 
							tfPassword.getText(),    "dbFStation",
							cmbTables.getSelectedItem().toString());

				}

				FileUtils.writeStringToFile(file, data);//avaible when import org.apache.commons.io.FileUtils;
				System.out.println( "Backup Successfull");
			//	xputils.showMessage("Backup Successfull");

			}

		} catch (Exception e) {

			e.printStackTrace();
			//xputils.showErrorMessage(e.toString());

		}}

	//method that handles the connect button

	private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		//validate controls

		url = "jdbc:mysql://" + tfHost.getText() 
		//+ ":"  
	//			+  tfPort.getText() 
		+ "/";

		uname = tfUser.getText();

		password = tfPassword.getText();

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, uname, password);

			if (conn != null) {

				isConnected = true;

				this.btnBackup.setEnabled(true);

				getTableNames(conn);

				this.cmbTables.setModel(tablesModel());
				System.out.println( "Connection was  Succesfull");

			//	xputils.showMessage("Connection was  Succesfull");

			} else {

				this.btnBackup.setEnabled(false);

				isConnected = false;
				System.out.println( "Connection was not Succesfull");
			//	xputils.showMessage("Connection was not Succesfull");

			}

		} catch (SQLException ex) {

			Logger.getLogger(dbasebackupdetailpanel.class.getName()).log(Level.SEVERE, null, ex);

		}}

	private int BUFFER = 10485760;

	//method to backup mysql database all tables

	private String getData(String host, String port, String user,

			String password, String db) throws Exception {
		
		System.out.println (

				"C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump.exe –host=" + host + " –port=" + port 
				+" –user=" + user + " –password=" + password +    " " +                db);
		

		Process run = Runtime.getRuntime().exec(

				"C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump.exe –host=" + host + " –port=" + port 
				+" –user=" + user + " –password=" + password +    " " 
				//		+                db
						);

		InputStream in = run.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		StringBuffer content = new StringBuffer();

		int count;

		char[] cbuf = new char[BUFFER];

		while ((count = br.read(cbuf, 0, BUFFER)) != -1) {

			content.append(cbuf, 0, count);

		}

		br.close();

		in.close();

		return content.toString();

	}

	//method to backup mysql database –one  table

	private String getData(String host, String port, String user,

			String password, String db, String table) throws Exception {

		Process run = Runtime.getRuntime().exec(

				"mysqldump –host=" + host + " –port=" + port +  " –user=" + user + " –password=" + password + " " +                db + "  " + table);

		InputStream in = run.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		StringBuffer content = new StringBuffer();

		int count;

		char[] cbuf = new char[BUFFER];

		while ((count = br.read(cbuf, 0, BUFFER)) != -1) {

			content.append(cbuf, 0, count);

		}

		br.close();

		in.close();

		return content.toString();

	}

	//method to retrive table names in mysql

	private void getTableNames(Connection con) {

		String[] DB_TABLE_TYPES = {"TABLE"};

		String COLUMN_NAME_TABLE_NAME = "TABLE_NAME";

		ResultSet rs = null;

		try {

			DatabaseMetaData meta = conn.getMetaData();

			rs = meta.getTables(null, null, null, DB_TABLE_TYPES);

			if (rs != null) {

				tables = new ArrayList();

				while (rs.next()) {

					String tableName = rs.getString(COLUMN_NAME_TABLE_NAME);

					if (tableName != null) {

						tables.add(tableName);

					}}}

			con.close();

		} catch (Exception e) {

			e.printStackTrace();

		}}

	//binding a list of tablenames to a comboboxmodel

	public ComboBoxModel tablesModel() {

		ComboBoxModel model;

		if (tables == null || tables.size() <= 0) {

			Object[] d = new Object[1];

			ComboBoxModel mo = new DefaultComboBoxModel(d);

			return mo;

		}

		Object[] days = new Object[tables.size()];

		int i = 0;

		try {

			ListIterator lg = tables.listIterator();

			while (lg.hasNext()) {

				days[i] = tables.get(i);

				i = i + 1;

			}

		} catch (Exception ex) {

			System.out.println("errorred " + ex.toString());

		}

		model = new DefaultComboBoxModel(days);

		return model;
	}
	}
