package nmfc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import nmfc.helper.CloseButton;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;


public class LicensingAgreement extends JFrame  implements ActionListener{

	private JPanel contentPane;
	JButton SUBMIT;
	JPanel panel;
	JLabel label1,label2;

	static Logger log = Logger.getLogger(LicensingAgreement.class.getName());


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LicensingAgreement frame = new LicensingAgreement();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public LicensingAgreement() throws IOException, ParseException {
		setResizable(false);
		Date currentDate = new Date ();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date license= sdf.parse("2057-2-17");
		if (license.compareTo(currentDate) <0) {
			ToastMessage toastMessage = new ToastMessage("License Expired",3000);
			toastMessage.setVisible(true);
			
			System.exit(DISPOSE_ON_CLOSE);
		}
		log.debug("Hello this is Start");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// As window closing event is handled there is no need to take
		// any other action during closing window
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout( null );
		setBounds( 280,  10, 600, 760 );
		getContentPane().setBackground(new Color(120, 189,  60) );

 

		panel=new JPanel();
		panel.setLayout( null );
		panel.setBounds(10, 15, 560, 740);
		panel.setBackground(new Color(135, 205, 185));
		panel.setBorder(new LineBorder(new Color(145, 240, 122), 4));
		//panel.setBackground(SystemColor.desktop);

		JLabel labelWelcome = new JLabel();
		labelWelcome.setForeground(Color.BLUE);
		//labelWelcome.setText("Welcome to NMFC");
		//	labelWelcome.setFont(new Font("Consolas", Font.BOLD, 18));
		labelWelcome.setFont(new Font("Siyamrupali_1_01", Font.BOLD, 18));	
		labelWelcome.setText("<html> NMFC Customer Agreement <br> ");  
		labelWelcome.setBounds(135, 5, 290, 45);
		panel.add(labelWelcome);	

		String licensing =  
				"This NMFC Customer Agreement   contains the terms and conditions that govern your access \n" +
						"to use NMFC application. This Agreement takes  effect when you click an “I Accept” button\n" +
						"provided at the start of application.  This software agreement is applicable to the customers\n" +
						"who are lawfully  authorized to use this application. You are not authorized to \n" +						 
						"use this application in case:\n" +
						"1. You are a minor. \n" +
						"2. You do not have a legal right to use this application.\n" +
						"3. You are using the application even after expiration license.\n\n"+
						" Use of this application:\n" +
						"1.1 Generally. You may access and use the software in accordance with this Agreement.  \n" + 
						"However, You will adhere to all laws, rules, and regulations applicable  in the\n" +
						"country you are residing.\n" +
						"1.2 Your Account. To access the application you will be provided with username and password.\n" +
						"You must  keep this credential safe  to avoid illegal access. \n" +
						"1.3 Support : Developer of this software may  provide support to application if required provided\n" +
						"charges/fees required for the support is paid in advance.\n" +

						"1.4 Third Party Content:  Third Party Content, such as software applications\n" +
						"used in this application are all free and open sources.\n" +
						"1.5 Developer's liabilities: Developer  of this software in no case will be responsible for\n" + 
						"any damage, loss of business or any other  financial losses incorporated due to\n" + 
						"legitimate or illegal use of this applications.\n\n" +
						"2. Changes.\n" +
						"2.1    Depending on the requirement of the customer,  this application developer can deliver\n" +
						"the changes which will be available in the newer version of this software. However,\n" +
						"detail of changes incorporated will be decided according to  agreements between software\n" +
						"supplier and customers. Fee or charges for the changes will decided during that agreement.\n\n" +
					 
						"3. Security and Data Privacy.\n" +
						"3.1  NMFC  Security.  Customer must keep his data safe by keeping \n" +
						" the password and data safe and also by not allowing the external persons to \n"+
						"access the computer where application is running. \n\n" +
						 
						"4.   Termination\n" +
						"   You may terminate this Agreement for any reason by: \n" +
						"(i) providing the developer notice and \n" +
						"(ii) Un installing and closing your  application\n" +
						 "No refund  will be made by the  developer for the termination of agreement." 
					   ;
						   

		JTextArea tarea = new JTextArea(licensing);
		tarea.setEditable(false);
		tarea.setBounds(20, 50, 520, 590);	    
		tarea.setBackground(new Color(185, 210, 212));		 		 
	 
		JScrollPane scrollPane = new JScrollPane(tarea  );
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(15, 55, 535, 560);
        
        scrollPane.setPreferredSize(new Dimension(300, 400));
        panel.add(scrollPane);
        
        
		SUBMIT=new JButton("I Accept");
		SUBMIT.setBounds(100, 662, 140, 30);
		SUBMIT.setForeground(StyleItems.buttonTextColor);
		SUBMIT.setFont(StyleItems.buttonFont);
		SUBMIT.setBorder(new LineBorder(new Color(200, 80, 70), 2));
		SUBMIT.setBackground(new Color(185, 220, 182));
		SUBMIT.addActionListener(this);		 
		panel.add(SUBMIT);	


		CloseButton noAccept =new CloseButton(this, "I do not accept");
		noAccept.setBounds(300, 662, 140, 30);	    
		noAccept.setBackground(new Color(255, 190, 182));		 
		panel.add(noAccept);

		add (panel); 

		setTitle("Accept Licensing Agrrement of  NMFC");


		// Logs out after confirmation while closing window
		WindowListener exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Close NMFC?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					setVisible (false);
					dispose();
					System.exit(0);
				}
			}
		};
		addWindowListener(exitListener);

	}

	public void actionPerformed(ActionEvent ae)
	{

		setVisible(false); //you can't see me!	
		String value1="";
		LoginNMFC page;
		try {
			try {
				page = new LoginNMFC( );
				page.setVisible(true);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

