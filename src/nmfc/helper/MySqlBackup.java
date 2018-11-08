package nmfc.helper;


import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import persistence.HibernateUtil;

/**
 *
 * @author Prabir Kumar Ghosh
 */
public class MySqlBackup {

	private int STREAM_BUFFER = 512000;
	private boolean status = true;
	//initializing the logger  
	static Logger log = Logger.getLogger(MySqlBackup.class.getName());
	private static String dbUser="root";
	private static String dbPass="admin";
	private static String dbName;
	private static String hostName;
	public static String getDbUser() {
		return dbUser;
	}

	public static void setDbUser(String dbUser) {
		MySqlBackup.dbUser = dbUser;
	}

	public static String getDbPass() {
		return dbPass;
	}

	public static void setDbPass(String dbPass) {
		MySqlBackup.dbPass = dbPass;
	}

	public static String getDbName() {
		return dbName;
	}

	public static void setDbName(String dbName) {
		MySqlBackup.dbName = dbName;
	}

	public static String getHostName() {
		return hostName;
	}

	public static void setHostName(String hostName) {
		MySqlBackup.hostName = hostName;
	}
	

	/**
	 * This method is used for backup the mysql database
	 *
	 * @param host - hostname - localhost/127.0.0.1
	 * @param port - 3306
	 * @param user - username
	 * @param password - password
	 * @param db - database name
	 * @param backupfile - file path to backup from the current location ex/
	 * "/backup/"
	 * @param mysqlDumpExePath - file path to mysqldump.exe from the current
	 * location ex/ copy the mysqldump.exe from mysql bin in to backup folder
	 * and set the path as backup ex/ "/backup/mysqldump.exe"
	 * @return the status of the backup true/false
	 */
	public boolean backupDatabase(String host, String port, String user, String password, String db, String backupfile, String mysqlDumpExePath) {
		System.out.println("Started");
		try {
			// Get MySQL DUMP data
			String dump = getServerDumpData(host, port, user, password, db, mysqlDumpExePath);
			//check the backup dump
			if (status) {
				byte[] data = dump.getBytes();
				// Set backup folder
				String rootpath = System.getProperty("user.dir") + backupfile;

				// See if backup folder exists
				File file = new File(rootpath);
				if (!file.isDirectory()) {
					// Create backup folder when missing. Write access is needed.
					file.mkdir();
				}
				// Compose full path, create a filename as you wish
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = new Date();
				String filepath = backupfile + "backup(With_DB)-" + db + "-" + host + "-(" + dateFormat.format(date) + ").sql";
				
				//String filepath = rootpath + "backup(With_DB)-" + db + "-" + host + "-(" + dateFormat.format(date) + ").sql";
				// Write SQL DUMP file
				File filedst = new File(filepath);
				FileOutputStream dest = new FileOutputStream(filedst);
				dest.write(data);
				dest.close();
				System.out.println("Backup created successfully for - " + db + " " + host);
				log.info("Backup created successfully for - " + db + " " + host);
			} else {
				//when status false  
				System.out.println("Could not create the backup for - " + db + " " + host);
				log.error("Could not create the backup for - " + db + " " + host);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex, ex.getCause());
		}

		return status;
	}

	private String getServerDumpData(String host, String port, String user, String password, String db, String mysqlDumpExePath) {
		StringBuilder dumpdata = new StringBuilder();
		String execlient = "";
		try {
			if (host != null && user != null && password != null && db != null) {
				// Set path. Set location of mysqldump 
				//  For example: current user folder and lib subfolder          

				execlient = System.getProperty("user.dir") + mysqlDumpExePath;

				// Usage: mysqldump [OPTIONS] database [tables]
				// OR     mysqldump [OPTIONS] --databases [OPTIONS] DB1 [DB2 DB3...]
				// OR     mysqldump [OPTIONS] --all-databases [OPTIONS]
				String command[] = new String[]{execlient,
						"--host=" + host,
						"--port=" + port,
						"--user=" + user,
						"--password=" + password,
						"--skip-comments",
						"--databases",
						db};

				// Run mysqldump
				ProcessBuilder pb = new ProcessBuilder(command);
				Process process = pb.start();

				InputStream in = process.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in));

				int count;
				char[] cbuf = new char[STREAM_BUFFER];

				// Read datastream
				while ((count = br.read(cbuf, 0, STREAM_BUFFER)) != -1) {
					dumpdata.append(cbuf, 0, count);
				}

				//set the status
				int processComplete = process.waitFor();
				if (processComplete == 0) {                   
					status = true;
				} else {
					status = false;
				}
				// Close
				br.close();
				in.close();
			}

		} catch (Exception ex) {
			log.error(ex, ex.getCause());
			return "";
		}
		return dumpdata.toString();
	}


	public static void main(String[] args) throws SQLException {
		MySqlBackup b = new MySqlBackup();
		// b.backupDatabase("10.100.3.108", "3306", "root", "root", "payroll", "/backup/", "/backup/mysqldump.exe");
		//b.backupDatabase("localhost", "3306", "root", "", "payroll", "/backup/", "/backup/mysqldump.exe");
		//  b.restoreDatabase("root", "", "backup/backup-payroll-10.100.3.108-27-09-2012.sql");
		// b.test();
		//  b.backupDataWithDatabase("C:\\wamp\\bin\\mysql\\mysql5.5.16\\bin\\mysqldump.exe", "localhost", "3306", "root", "", "payroll", "C:/Users/dinuka/Desktop/test/");
		//b.backupAllDatabases("C:\\wamp\\bin\\mysql\\mysql5.5.16\\bin\\mysqldump.exe", "localhost", "3306", "root", "admin", "C:/Users/dinuka/Desktop/test/");
		b.showAllDatabase();
		b.takeBackup1();
		b.restoreDB(  "F:\\data\\backup\\sqlserver\\backup1.sql");
	//	b.restoreDatabase ();
	//	b.backupDatabase("localhost", "3306", "root", "admin", "test1", "F:\\data\\backup\\sqlserver\\", "C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump.exe");
		System.out.println(b.listNMFCDatabases());
 //		b.backupAllDatabases("C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump.exe", "localhost", "3306", "root", "admin", "F:\\data\\backup\\sqlserver");
		
	}

	public boolean backupDataWithOutDatabase(String dumpExePath, String host, String port, String user, String password, String database, String backupPath) {
		boolean status = false;
		try {
			Process p = null;

			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String filepath = "backup(without_DB)-" + database + "-" + host + "-(" + dateFormat.format(date) + ").sql";

			String batchCommand = "";
			if (password != "") {
				//only backup the data not included create database
				batchCommand = dumpExePath + " -h " + host + " --port " + port + " -u " + user + " --password=" + password + " " + database + " -r \"" + backupPath + "" + filepath + "\"";
			} else {
				batchCommand = dumpExePath + " -h " + host + " --port " + port + " -u " + user + " " + database + " -r \"" + backupPath + "" + filepath + "\"";
			}

			Runtime runtime = Runtime.getRuntime();
			p = runtime.exec(batchCommand);
			int processComplete = p.waitFor();

			if (processComplete == 0) {
				status = true;
				log.info("Backup created successfully for without DB " + database + " in " + host + ":" + port);
			} else {
				status = false;
				log.info("Could not create the backup for without DB " + database + " in " + host + ":" + port);
			}

		} catch (IOException ioe) {
			log.error(ioe, ioe.getCause());
		} catch (Exception e) {
			log.error(e, e.getCause());
		}
		return status;
	}

	public boolean backupDataWithDatabase(String dumpExePath, String host, String port, String user, String password, String database, String backupPath) {
		boolean status = false;
		try {
			Process p = null;

			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String filepath = "backup(with_DB)-" + database + "-" + host + "-(" + dateFormat.format(date) + ").sql";

			String batchCommand = "";
			if (password != "") {
				//Backup with database
				batchCommand = dumpExePath + " -h " + host + " --port " + port + " -u " + user + " --password=" + password + " --add-drop-database -B " + database + " -r \"" + backupPath + "" + filepath + "\"";
			} else {
				batchCommand = dumpExePath + " -h " + host + " --port " + port + " -u " + user + " --add-drop-database -B " + database + " -r \"" + backupPath + "" + filepath + "\"";
			}

			Runtime runtime = Runtime.getRuntime();
			p = runtime.exec(batchCommand);
			int processComplete = p.waitFor();


			if (processComplete == 0) {
				status = true;
				log.info("Backup created successfully for with DB " + database + " in " + host + ":" + port);
			} else {
				status = false;
				log.info("Could not create the backup for with DB " + database + " in " + host + ":" + port);
			}

		} catch (IOException ioe) {
			log.error(ioe, ioe.getCause());
		} catch (Exception e) {
			log.error(e, e.getCause());
		}
		return status;
	}

	public boolean backupAllDatabases(String dumpExePath, String host, String port, String user, String password, String backupPath) {
		boolean status = false;
		System.out.println ("Satrted");
		try {
			Process p = null;

			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			String filepath = "backup(with_DB)-All-" + host + "-(" + dateFormat.format(date) + ").sql";

			String batchCommand = "";
			if (password != "") {
				//Backup with database
				batchCommand = dumpExePath + " -h " + host + " --port " + port + " -u " + user + " --password=" + password + " --add-drop-database -A  -r \"" + backupPath + "" + filepath + "\"";
			} else {
				batchCommand = dumpExePath + " -h " + host + " --port " + port + " -u " + user + " --add-drop-database -A  -r \"" + backupPath + "" + filepath + "\"";
			}

			Runtime runtime = Runtime.getRuntime();
			p = runtime.exec(batchCommand);
			int processComplete = p.waitFor();


			if (processComplete == 0) {
				status = true;
				System.out.println ("Success");
				log.info("Backup created successfully with All DBs in " + host + ":" + port);
			} else {
				status = false;
				System.out.println ("Failure");
				log.info("Could not create the backup for All DBs in " + host + ":" + port);
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
			log.error(ioe, ioe.getCause());
		} catch (Exception e) {
			log.error(e, e.getCause());
		}
		System.out.println ("Ended");
		return status;
	}

	/**
	 * Restore the backup into a local database
	 *
	 * @param dbUserName - user name
	 * @param dbPassword - password
	 * @param source - backup file
	 * @return the status true/false
	 */
	public boolean restoreDatabase(String dbUserName, String dbPassword, String source) {

		String[] executeCmd = new String[]{"mysql", "--user=" + dbUserName, "--password=" + dbPassword, "-e", "source " + source};

		Process runtimeProcess;
		try {
			runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int processComplete = runtimeProcess.waitFor();

			if (processComplete == 0) {
				log.info("Backup restored successfully with " + source);
				return true;
			} else {
				log.info("Could not restore the backup " + source);
			}
		} catch (Exception ex) {
			log.error(ex, ex.getCause());
		}

		return false;

	}
	
	public  static List <String>  listNMFCDatabases() {
		List <String> nmfcDBlist = new  ArrayList <String>();
		hostName = "localhost";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/", dbUser, dbPass);
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


	
	
	private void showAllDatabase() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "admin");
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
	
	public void restoreDatabase () {

		String executeCmd = "";
		executeCmd = "\"C:/Program Files/MySQL/MySQL Server 5.5/bin/mysql\"  -u root -padmin testnmfc < F:\\data\\backup\\sqlserver\\backup.sql" ;
		try {
			Process runtimeProcess =Runtime.getRuntime().exec(executeCmd);
			System.out.println("Started");
			int processComplete = runtimeProcess.waitFor();
			if(processComplete == 0){
				System.out.println("success");
			} else {
				System.out.println("restore failure");
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
	
	public void takeBackup1 (){
		dbUser = "root";
		dbPass = "admin";
		dbName = "test1";
		String executeCmd =  "";
		executeCmd =  "C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump.exe -u " 
				+dbUser+ " -p" +dbPass + " " + dbName+ "   -r " +  "F:\\data\\backup\\sqlserver\\backup1.sql";

		
	//	executeCmd =  "C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump.exe -u " 
	//	+dbUser+ " -p" +dbPass + " " + dbName+ "   -r " +  "F:\\data\\backup\\sqlserver\\backup.sql";

		Process runtimeProcess;
		try {
			runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int processComplete = runtimeProcess.waitFor();
			if(processComplete == 0){
				System.out.println("Backup taken successfully");
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
	}
		public void restoreDB (String backupPath){
			 
			  String   executeCmd = "\"C:/Program Files/MySQL/MySQL Server 5.5/bin/mysql\" -u root -padmin testnmfc <" + backupPath;
			 
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
			    System.out.println("Couldn't Restore the backup !");
			   }
			  }
			  catch(Exception ex)
			  {
			   ex.printStackTrace();
			  }
			 
			 }
	
	
}

