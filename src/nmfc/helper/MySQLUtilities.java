package nmfc.helper;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class MySQLUtilities {

	//initializing the logger  
	static Logger log = Logger.getLogger(MySqlBackup.class.getName());
	private  String dbUser="root";
	private  String dbPass="admin";
	private  String dbName="test";;
	private  String hostName="localhost";
	private  String databaseDriver = "com.mysql.jdbc.Driver";
	private  String mysqlPath="C:/Program Files/MySQL/MySQL Server 5.5/bin/";



	public  void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}


	public  void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}

	public   void setDbName(String dbName) {
		this.dbName = dbName;
	}


	public   void setHostName(String hostName) {
		this.hostName = hostName;
	}






	public static void main(String[] args) throws SQLException {
		MySQLUtilities b = new MySQLUtilities();
		// b.backupDatabase("10.100.3.108", "3306", "root", "root", "payroll", "/backup/", "/backup/mysqldump.exe");
		//b.backupDatabase("localhost", "3306", "root", "", "payroll", "/backup/", "/backup/mysqldump.exe");
		//  b.restoreDatabase("root", "", "backup/backup-payroll-10.100.3.108-27-09-2012.sql");
		// b.test();
		//  b.backupDataWithDatabase("C:\\wamp\\bin\\mysql\\mysql5.5.16\\bin\\mysqldump.exe", "localhost", "3306", "root", "", "payroll", "C:/Users/dinuka/Desktop/test/");
		//b.backupAllDatabases("C:\\wamp\\bin\\mysql\\mysql5.5.16\\bin\\mysqldump.exe", "localhost", "3306", "root", "admin", "C:/Users/dinuka/Desktop/test/");
		//b.showAllDatabase();

		//	b.restoreDB(  "F:\\data\\backup\\sqlserver\\backup1.sql");
		try {
			b.createDatabase("puretest1");
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		b.restoreDB ("J:\\data\\tmp\\nmfc.sql", "puretest");
		//	 	System.out.println(b.backupDataBase("F:\\data\\backup\\sqlserver\\backup2.sql",  "test1"   ));
		//	 	System.out.println(b.listNMFCDatabases());
		//		b.backupAllDatabases("C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump.exe", "localhost", "3306", "root", "admin", "F:\\data\\backup\\sqlserver");

	}



	/**
	 * Restore the backup into a local database
	 *
	 * @param dbUserName - user name
	 * @param dbPassword - password
	 * @param backupPath - backup file
	 * @return the status true/false
	 */

	public void restoreDB (String backupPath, String dbName){

		//	String   executeCmd = "\"C:/Program Files/MySQL/MySQL Server 5.5/bin/mysql\" -u root -padmin testnmfc <" + backupPath;

		String   executeCmd = "\"" + mysqlPath + "mysql" + "\"" + " -u " + dbUser + " -p" + dbPass + " " + dbName + "<" + backupPath;
		System.out.println(executeCmd);

		try
		{
			Process runtimeProcess = Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", executeCmd });
			int processComplete = runtimeProcess.waitFor();
			System.out.println(processComplete);
			if(processComplete == 0)
			{
				System.out.println("Restored the Backup !");
			}
			else
			{
				System.out.println("Couldn't Restore the DB !");
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * List all the database starts with "nmfc" 
	 *
	 *  
	 * @return the list of Strings with matched database name
	 */


	public   List <String>  listNMFCDatabases() {
		List <String> nmfcDBlist = new  ArrayList <String>();
		try {
			Class.forName(databaseDriver).newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + hostName + "/", dbUser, dbPass);
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet ctlgs = dbmd.getCatalogs();

			while (ctlgs.next())
			{
				System.out.println("ctlgs="+ctlgs.getString(1));
				if (ctlgs.getString(1).toUpperCase().startsWith("NMFC"))
				{
					nmfcDBlist.add (ctlgs.getString(1));
				}
			}

		} 
		catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		 
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nmfcDBlist;
	}


	/**
	 * Print all database names
	 *
	 *  
	 */

	private void showAllDatabase() {
		try {
			Class.forName(databaseDriver).newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + hostName + "/", dbUser, dbPass);
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet ctlgs = dbmd.getCatalogs();
			while (ctlgs.next())
			{
				System.out.println("ctlgs="+ctlgs.getString(1));
			}
		} 
		catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * This method is used to backup a single  mysql database
	 *
	 * Credential like user name and Password will be set through setter method
	 * @param dbName - database name
	 * @param backupPath - file path to backup  up
	 *  
	 * @return the status of the backup true/false
	 */

	public boolean  backupDataBase (String backupPath, String dbName){
		dbUser = "root";
		dbPass = "admin";
		String executeCmd =  "";
		executeCmd ="\"" + mysqlPath + "mysqldump.exe" + "\""   + " -u " 
				+dbUser+ " -p" +dbPass + " " + dbName+ "   -r " +  backupPath;


		//	executeCmd =  "C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump.exe -u " 
		//	+dbUser+ " -p" +dbPass + " " + dbName+ "   -r " +  "F:\\data\\backup\\sqlserver\\backup.sql";

		Process runtimeProcess;
		try {
			runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int processComplete = runtimeProcess.waitFor();
			if(processComplete == 0){
				System.out.println("Backup taken successfully");
				return true;
			} else {
				System.out.println("Could not take mysql backup");
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public   void createDatabase (String dbName ) throws Exception {
		try 
		{   
			Class.forName(databaseDriver);
			Connection conn = DriverManager.getConnection("jdbc:mysql://" + hostName + "/", dbUser, dbPass);
			Statement s = conn.createStatement();
			int Result = s.executeUpdate("CREATE DATABASE IF NOT EXISTS  " +  dbName);
			System.out.println ("Create Database output : " + Result);

		} 
		catch (ClassNotFoundException | SQLException e) {			 
			e.printStackTrace();
			throw new Exception ();
		}

	}





}
