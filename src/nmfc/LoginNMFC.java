package nmfc;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class LoginNMFC extends JFrame  implements ActionListener{

	private JPanel contentPane;
	JButton SUBMIT;
	JPanel panel;
	JLabel label1,label2;
	final JTextField  text1,text2;
	static Logger log = Logger.getLogger(LoginNMFC.class.getName());
	
	private final JLabel lblUserName = new JLabel("User Name");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginNMFC frame = new LoginNMFC();
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
	public LoginNMFC() throws IOException, ParseException {
		setResizable(false);
		log.debug("Hello this is Start");
		
		Date currentDate = new Date ();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date license= sdf.parse("2037-04-17");
		if (license.compareTo(currentDate) <0) {
			System.exit(DISPOSE_ON_CLOSE);
		}
	// As window closing event is handled there is no need to take
		// any other action during closing window
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);		 
		getContentPane().setLayout( null );
		setBounds( 210, 140, 600, 650 );
		getContentPane().setBackground(new Color(120, 189,  60) );


		//		contentPane.setLayout(new BorderLayout(0, 0));
		//	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		//	contentPane = new JPanel();
		// 	contentPane.setBackground(SystemColor.desktop);
		//   	contentPane.setBorder(new LineBorder(new Color(0, 80, 70), 2));	
		//	setContentPane(new JLabel(new ImageIcon("bin/image/background3.JPG")));

		BufferedImage myPicture = ImageIO.read(new File("bin/image/mango1.png"));
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		picLabel.setBounds(190, 30, 150, 150);
		add (picLabel);

		JLabel labelWelcome = new JLabel();
		labelWelcome.setForeground(Color.BLUE);
		//labelWelcome.setText("Welcome to NMFC");
	//	labelWelcome.setFont(new Font("Consolas", Font.BOLD, 18));
		labelWelcome.setFont(new Font("Siyamrupali_1_01", Font.BOLD, 18));	
		labelWelcome.setText("<html> Welcome to NMFC <br> নিউ মদিনা ফ্রুট কোম্পানি ");  
		labelWelcome.setBounds(195, 195, 220, 45);
		getContentPane().add(labelWelcome);	

 

		panel=new JPanel();
		panel.setLayout( null );
		panel.setBounds(150, 255, 261, 160);
		panel.setBackground(new Color(135, 205, 185));
		panel.setBorder(new LineBorder(new Color(145, 140, 122), 4));
		//panel.setBackground(SystemColor.desktop);
 

		label1 = new JLabel();
		panel.add(label1);
		label1.setForeground(Color.BLUE);
		label1.setText("Username:");
		label1.setBounds(15, 10, 100, 20);

		text1 = new JTextField(15);
		text1.setBounds(130, 10, 100, 20);
		panel.add(text1);
 
		label2 = new JLabel();
		label2.setForeground(Color.BLUE);
		label2.setBounds(15, 50, 100, 20);
		panel.add(label2);

		text2 = new JPasswordField(15);
		text2.setBounds(130, 50, 100, 20);
		label2.setText("Password:");

		//text1.setForeground(UIManager.getColor("Tree.selectionBackground"));
		panel.add(text2);

		SUBMIT=new JButton("SUBMIT");
		SUBMIT.setBounds(90, 100, 102, 40);	    
		SUBMIT.setBackground(new Color(255, 200, 182));
		SUBMIT.addActionListener(this);
		getRootPane().setDefaultButton(SUBMIT);

		panel.add(SUBMIT);	
		add (panel); 

		setTitle("LOGIN NMFC");


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
		String value1=text1.getText();
		String value2=text2.getText();
		if ( (value1.equals("admin") && value2.equals("nmfc123"))||
				 (value1.equals("78692110") && value2.equals("78692110"))
				|| (value1.equals("user") && value2.equals("nmfc"))
				)
		{
			text1.setText(""); 
			text2.setText(""); 
			setVisible(false); //you can't see me!	
			HomePage page;
			try {
				page = new HomePage(this, value1);
				page.setVisible(true);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//JLabel label = new JLabel("Welcome:"+value1);
			//page.getContentPane().add(label);
			//dispose(); 
		} 
		else{
			System.out.println("enter the valid username and password");
			JOptionPane.showMessageDialog(this,"Incorrect login name or password",
					"Error",JOptionPane.ERROR_MESSAGE);
		}
	}

}
