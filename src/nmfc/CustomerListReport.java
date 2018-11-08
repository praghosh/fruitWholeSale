package nmfc;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import entities.Truck;
import nmfc.TruckWiseSaleReport.GenerateTruckWiseReportListener;
import nmfc.helper.CloseButton;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import pdftables.CreateTruckSalePDFReport;
import pdftables.CustomerListPDFTable;

public class CustomerListReport extends JDialog {
	
	private JPanel rightPanel;
	JFileChooser fc = new JFileChooser();
	public CustomerListReport (Frame homePage, String string, boolean b) {
		super (homePage,  string,  b);
		getContentPane().setLayout( null );
		setBounds( 50, 10, 720, 450 );
		getContentPane().setBackground(new Color(230, 245, 240) );
  
		rightPanel = new JPanel();
		rightPanel.setLayout( null );
		rightPanel.setBounds( 10, 10, 400, 300 );
		rightPanel.setBackground(new Color(200, 250, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));	 
		add(rightPanel);
		createReportButtons();
		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 150, 350, 160, 35 );
		add(closeButton);
	}
	
	
	private void createReportButtons() {
		JButton genarateReport = new JButton ("Generate Report");
		genarateReport.setBounds( 120,200,180,20);
		StyleItems.styleLabel (genarateReport);
		genarateReport.addActionListener(new GenerateCustomerReportListener ());
		rightPanel.add(genarateReport);
		
	}
	
	public class GenerateCustomerReportListener   implements ActionListener {
		
		
		public void actionPerformed(ActionEvent evt) {
		 	
				try {

					fc.setDialogTitle("Create a file to create report");

					fc.setCurrentDirectory(fc.getCurrentDirectory());

					System.out.println("File=" + fc.getSelectedFile());
					if (fc.showSaveDialog(CustomerListReport.this) == JFileChooser.APPROVE_OPTION) {

						String pdfReportPath = fc.getSelectedFile().getAbsolutePath();
						String finalBackupPath = pdfReportPath;
			
						if (!pdfReportPath.endsWith(".pdf") )
						{
							finalBackupPath = pdfReportPath + 	 ".pdf";
						}
						 
					     CustomerListPDFTable.createCustomerListPDF (finalBackupPath);
						
						
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

	
	

}
