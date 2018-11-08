package nmfc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdesktop.swingx.JXDatePicker;

import entities.Address;
import entities.Franchise;
import entities.Fruit;

import nmfc.FranchicePage.DeleteFranchiseActionListener;
import nmfc.FranchicePage.addFranchiseActionListener;
import nmfc.FranchicePage.modifyFranchiseActionListener;
import nmfc.helper.CloseButton;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import persistence.HibernateUtil;

public class FranchicePage extends JDialog {
	private Session session;
	private JPanel leftPanel = new JPanel();
	private JPanel infoPanel = new JPanel();
	private JList rowList;
	private JTextField customerName;
	private List <Franchise> franchiseList;
	private JTextField customerCode;
	private JTextField customerAddressLine1 = new JTextField();;
	private JTextField customerAddressLine2 = new JTextField();
	private JTextField franchiseCity = new JTextField();
	private JTextField pinCode  = new JTextField();
	private JTextField state;
	private JTextField mobileNumber = new JTextField();
	private JTextField phoneNumber  = new JTextField();
	private JScrollPane listScrollPane = new JScrollPane();;

	public FranchicePage (Frame homePage, String string, boolean b) {
		super (homePage,  string,  b);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 720, 420 );
		getContentPane().setBackground(new Color(230, 245, 240) );


		leftPanel.setLayout( null );
		leftPanel.setBounds( 10, 10, 250, 400 );
		leftPanel.setBackground(new Color(200, 230, 210) );
		leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createLeftPanel();


		infoPanel.setLayout( null );
		infoPanel.setBounds( 265, 10, 440, 400 );
		infoPanel.setBackground(new Color(200, 230, 210) );
		infoPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();




		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 395, 460, 160, 35 );
		add(closeButton);
	}

	private void createLeftPanel() {
		leftPanel.removeAll();
		JLabel selectomerNameLabel = new JLabel ("List of Franchise");
		selectomerNameLabel.setBounds( 10,10,165,20);
		StyleItems.styleLabel (selectomerNameLabel); 
		leftPanel.add (selectomerNameLabel);

		listScrollPane.setBounds( 30, 40, 170, 150 );
		franchiseList = Franchise.getFranchiseNames();
		rowList = new JList(franchiseList.toArray());
		rowList.setVisibleRowCount(12);
		rowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = rowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " + franchiseList.get(idx));
					populateFranchiseData();

				}
				else
					System.out.println("Please choose a Franchise.");
			}
		}); 		
		listScrollPane.setViewportView(rowList);
		listScrollPane.revalidate();
		listScrollPane.repaint();
		rowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

		//	listScrollPane.add(rowList);
		System.out.println("Size **= " +  franchiseList.size());
		leftPanel.add(listScrollPane);


		JButton delete = new JButton ("Delete");
		delete.setBounds( 40,280,90,20);
		StyleItems.styleLabel  (delete);
		delete.addActionListener(new DeleteFranchiseActionListener ());
		leftPanel.add(delete);
		add(leftPanel);

	}

	private void populateFranchiseData() {
		Franchise selectedFranchise =  (Franchise)rowList.getSelectedValue();
		customerName.setText(selectedFranchise.toString());
		customerCode.setText(selectedFranchise.getCode());
		Address selectedAddress = selectedFranchise.getAddress();
		if (selectedAddress!=null) {
			customerAddressLine1.setText(selectedAddress.getAddressLine1());
			customerAddressLine2.setText(selectedAddress.getAddressLine2());
			franchiseCity.setText(selectedAddress.getCity());
			pinCode.setText(selectedAddress.getZipCode());
			state.setText(selectedAddress.getState());
		}
		else {
			customerAddressLine1.setText("");
			customerAddressLine2.setText("");
			franchiseCity.setText("");
			pinCode.setText("");
			state.setText("");
		}
		mobileNumber.setText(selectedFranchise.getMobile()); 
		phoneNumber.setText(selectedFranchise.getPhone()); 

	}
	// Remove all the entry
	private void clearFranchiseData() {

		customerName.setText("");
		customerCode.setText("");
		customerAddressLine1.setText("");
		customerAddressLine2.setText("");
		franchiseCity.setText("");
		pinCode.setText("");
		state.setText("");
		mobileNumber.setText(""); 
		phoneNumber.setText(""); 

	} 

	private void createRightPanel() {

		JLabel customerNameLabel = new JLabel ("Franchise Name");
		customerNameLabel.setBounds( 10,10,135,20);
		StyleItems.styleLabel (customerNameLabel); 
		infoPanel.add (customerNameLabel);	

		customerName = new JTextField();
		customerName.setBounds( 175,10,255,20);
		StyleItems.styleLabel (customerName); 
		infoPanel.add (customerName);


		JLabel customerCodeLabel = new JLabel ("Franchise Code");
		customerCodeLabel.setBounds( 10,40,135,20);
		StyleItems.styleLabel (customerCodeLabel); 
		infoPanel.add (customerCodeLabel);	

		customerCode = new JTextField();
		customerCode.setBounds( 175,40,125,20);
		StyleItems.styleLabel (customerCode); 
		infoPanel.add (customerCode);

		JLabel customerAddressLabel = new JLabel ("Address");
		customerAddressLabel.setBounds( 10,70,135,20);
		StyleItems.styleLabel (customerAddressLabel); 
		infoPanel.add (customerAddressLabel);


		JLabel customerAddressLine1Label = new JLabel ("Line 1");
		customerAddressLine1Label.setBounds( 180,70,85,20);
		StyleItems.styleLabel (customerAddressLine1Label); 
		infoPanel.add (customerAddressLine1Label);

		customerAddressLine1.setBounds( 235,70,185,20);
		StyleItems.styleLabel (customerAddressLine1); 
		infoPanel.add (customerAddressLine1);

		JLabel customerAddressLine2Label = new JLabel ("Line 2");
		customerAddressLine2Label.setBounds( 180,100,85,20);
		StyleItems.styleLabel (customerAddressLine2Label); 
		infoPanel.add (customerAddressLine2Label);

		customerAddressLine2.setBounds( 235,100,185,20);
		StyleItems.styleLabel (customerAddressLine2); 
		infoPanel.add (customerAddressLine2); 

		JLabel franchiseCityLabel = new JLabel ("City/Village");
		franchiseCityLabel.setBounds( 180,135,105,20);
		StyleItems.styleLabel (franchiseCityLabel); 
		infoPanel.add (franchiseCityLabel);

		franchiseCity.setBounds(300,135,120,20);
		StyleItems.styleLabel (franchiseCity); 
		infoPanel.add (franchiseCity); 

		JLabel pinCodeLabel = new JLabel ("Pin Code");
		pinCodeLabel.setBounds( 180,165,105,20);
		StyleItems.styleLabel (pinCodeLabel); 
		infoPanel.add (pinCodeLabel);


		pinCode.setBounds(300,165,120,20);
		StyleItems.styleLabel (pinCode); 
		infoPanel.add (pinCode);

		JLabel stateLabel = new JLabel ("State");
		stateLabel.setBounds( 180,195,105,20);
		StyleItems.styleLabel (stateLabel); 
		infoPanel.add (stateLabel);

		state = new JTextField();
		state.setBounds(300,195,120,20);
		StyleItems.styleLabel (state); 
		infoPanel.add (state); 


		JLabel mobileNumberLabel = new JLabel ("Mobile Number");
		mobileNumberLabel.setBounds( 10,230,115,20);
		StyleItems.styleLabel (mobileNumberLabel); 
		infoPanel.add (mobileNumberLabel);	

		mobileNumber.setBounds( 175,230,125,20);
		StyleItems.styleLabel (mobileNumber); 
		infoPanel.add (mobileNumber);

		JLabel phoneNumberLabel = new JLabel ("Phone Number");
		phoneNumberLabel.setBounds( 10,260,115,20);
		StyleItems.styleLabel (phoneNumberLabel); 
		infoPanel.add (phoneNumberLabel);	

		phoneNumber.setBounds( 175,260,125,20);
		StyleItems.styleLabel (phoneNumber); 
		infoPanel.add (phoneNumber);

		JButton save = new JButton ("ADD");
		save.setBounds( 10,320,90,20);
		StyleItems.styleLabel (save);
		save.addActionListener(new addFranchiseActionListener ());
		infoPanel.add(save);

		JButton modify = new JButton ("Modify");
		modify.setBounds( 140,320,90,20);
		StyleItems.styleLabel (modify);
		modify.addActionListener(new modifyFranchiseActionListener ());
		infoPanel.add(modify);
		add(infoPanel);

	}

	public class DeleteFranchiseActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){

				int idx = rowList.getSelectedIndex();
				if (idx == -1){
					System.out.println("Please choose a Franchise.");
					ToastMessage toastMessage = new ToastMessage("Please choose a Franchise",3000);
					toastMessage.setVisible(true);
					return;
				}

				if (deleteSelectedFranchise())
				{				
					ToastMessage toastMessage = new ToastMessage("Franchise Deleted Successfully",3000);
					toastMessage.setVisible(true);
					recreateFranchiseList ();
					clearFranchiseData();
				}
				else {
					ToastMessage toastMessage = new ToastMessage("Franchise Not Deleted",3000);
					toastMessage.setVisible(true);
				}
			}
		}

	}

	public class modifyFranchiseActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isFranchiseNameValid ()) return;
			int idx = rowList.getSelectedIndex();
			if (idx == -1) {
				System.out.println("Please choose a Franchise.");
				ToastMessage toastMessage = new ToastMessage("Please choose a Franchise",3000);
				toastMessage.setVisible(true);
				return;
			}
			if (franchiseExistWithSameName(true))
			{
				ToastMessage toastMessage = new ToastMessage("Franchise Already Exists with same name", 3000);
				toastMessage.setVisible(true);
				return;

			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();
				System.out.println (((Franchise)rowList.getSelectedValue()).getId());
				System.out.println("Current selection: " + franchiseList.get(idx));
				Serializable id = ((Franchise)rowList.getSelectedValue()).getId();
				Object persistentInstance = session.load(Franchise.class, id);
				Franchise franchise = (Franchise)persistentInstance;
				enterFrnachiseData (franchise);			 
				session.save(franchise);
				session.getTransaction().commit();					
			}
			catch (Exception ex)
			{
				session.getTransaction().rollback();
				ToastMessage toastMessage = new ToastMessage("Franchise not modified",3000);
				toastMessage.setVisible(true);
				return;
			}

			finally {
				if(session!=null){
					session.close();
				}
			}
			ToastMessage toastMessage = new ToastMessage("Franchise modified Successfully",3000);
			toastMessage.setVisible(true);
			recreateFranchiseList();
		}

	}

	public class addFranchiseActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (!isFranchiseNameValid ()) return;

			if (franchiseExistWithSameName(false))
			{
				ToastMessage toastMessage = new ToastMessage("Franchise Already Exists with same name", 3000);
				toastMessage.setVisible(true);
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();				 
				Franchise franchise = new Franchise();
				enterFrnachiseData( franchise);
				int franchiseId = (Integer) session.save(franchise);
				session.getTransaction().commit();
				ToastMessage toastMessage = new ToastMessage("Franchise Saved Successfully",3000);
				toastMessage.setVisible(true);
			}
			catch (Exception ex)
			{   
				session.getTransaction().rollback();
				ToastMessage toastMessage = new ToastMessage("Franchise was not Saved",3000);
				toastMessage.setVisible(true);
				System.out.println(ex.getMessage());
				ex.printStackTrace();
				return;
			}
			finally {
				if(session!=null&&session.isOpen()){
					session.close();
				}
			}

			recreateFranchiseList();

		}



	}



	// Populate Franchise data from user input. 	
	private  void  enterFrnachiseData(Franchise franchise) {

		franchise.setName(customerName.getText());
		franchise.setCode(customerCode.getText());
		Address address = new Address();
		address.setAddressLine1(customerAddressLine1.getText());
		address.setAddressLine2(customerAddressLine2.getText());
		address.setCity(franchiseCity.getText());
		address.setZipCode(pinCode.getText());
		address.setState(state.getText());
		franchise.setMobile(mobileNumber.getText());
		franchise.setAddress(address);
		franchise.setPhone(phoneNumber.getText());		

	}




	public boolean deleteSelectedFranchise() {
		try {
			System.out.println (((Franchise)rowList.getSelectedValue()).getId());
			//	System.out.println("Current selection: " + routes.get(idx));
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Serializable id = ((Franchise)rowList.getSelectedValue()).getId();
			Object persistentInstance = session.load(Franchise.class, id);
			if (persistentInstance != null)  {
				session.delete(persistentInstance);
				session.getTransaction().commit();
				return true;
			}
			else {
				System.out.println ("Did not find the Franchise Object in persistance");
				return false;
			}

		} 
		catch (HibernateException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			return false;
		}

		finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
	}

	private boolean isFranchiseNameValid() {

		String franchiseName =  customerName.getText();
		if (franchiseName.trim().isEmpty())
		{
			ToastMessage toastMessage = new ToastMessage("Franchise name not valid",3000);
			toastMessage.setVisible(true);
			return false;
		}
		else return true;

	}

	private void recreateFranchiseList () { 
		franchiseList = Franchise.getFranchiseNames(); 	
		rowList.setListData(franchiseList.toArray());
		listScrollPane.repaint();
	}

	// Checks if a Franchise Name already exists.	
	// It is called within a hibernate transaction
	private boolean franchiseExistWithSameName(boolean modify) {
	 
			String franchiseName = customerName.getText();
			// In case of modifying a Franchise we need to make sure name
			// does not clash with other  names. But it can be same with current name.
			if (modify)
			{
				String existingName = ((Franchise)rowList.getSelectedValue()).getName();
				if (franchiseName.equals(existingName)) return false;
			}
			return Franchise.franchiseExistWithSameName (franchiseName); 			 
		 
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			FranchicePage dialog = new FranchicePage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(760, 560);
			dialog.setVisible(true);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}



}