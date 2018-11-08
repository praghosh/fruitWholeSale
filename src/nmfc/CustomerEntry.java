package nmfc;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
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
import entities.Customer;
import entities.Franchise;
import entities.Route;

import nmfc.helper.CloseButton;
import nmfc.helper.Result;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import nmfc.helper.Utility;
import persistence.HibernateUtil;

public class CustomerEntry  extends JDialog {	 
	private JPanel leftPanel;
	private JPanel infoPanel;
	private JScrollPane listScrollPane;
	private List <Customer> customerList;
	private JList rowList;
	private Session session;
	private JTextField customerName;
	private JTextField customerAddressLine1;
	private JTextField customerAddressLine2;
	private JTextField customerCity;
	private JTextField pinCode;
	private JTextField state;
	private JScrollPane routeListScrollPane;
	private List routes;
	private JList routeRowList;
	private JTextField parentName;
	private JTextField mobileNumber;
	private JTextField adharNumber;
	private JTextField voterNumber;
	private JTextField customerCode;	
	private JFormattedTextField creditLimit =new JFormattedTextField();
	private JFormattedTextField discountPercent =new JFormattedTextField();
	private JCheckBox isDefaulterCheckBox;

	public CustomerEntry (Frame homePage, String string, boolean b) {
		super (homePage,  string,  b);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 750, 520 );
		getContentPane().setBackground(new Color(230, 245, 240) );

		leftPanel = new JPanel();
		leftPanel.setLayout( null );
		leftPanel.setBounds( 10, 10, 250, 530 );
		leftPanel.setBackground(new Color(200, 230, 210) );
		leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createLeftPanel();
		add(leftPanel);


		infoPanel = new JPanel();
		infoPanel.setLayout( null );
		infoPanel.setBounds( 265, 10, 560, 530 );
		infoPanel.setBackground(new Color(200, 230, 210) );
		infoPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));
		createRightPanel();
		add(infoPanel);



		JButton closeButton = new CloseButton (this);
		closeButton.setBounds( 395, 550, 160, 35 );
		add(closeButton);
	}

	private void createLeftPanel() {

		JLabel selectomerNameLabel = new JLabel ("List of Customer");
		selectomerNameLabel.setBounds( 10,10,135,20);
		StyleItems.styleLabel (selectomerNameLabel); 
		leftPanel.add (selectomerNameLabel);


		listScrollPane = new JScrollPane();
		listScrollPane.setBounds( 30, 40, 170, 400 );
		customerList = Customer.getCustomerLists();
		rowList = new JList(customerList.toArray());
		rowList.setVisibleRowCount(12);
		rowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = rowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " + customerList.get(idx));
					populateCustomerData();

				}
				else
					System.out.println("Please choose a Customer.");
			}
		}); 		
		listScrollPane.setViewportView(rowList);
		listScrollPane.revalidate();
		listScrollPane.repaint();
		rowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		System.out.println("Size **= " +  customerList.size());
		leftPanel.add(listScrollPane);

		JButton delete = new JButton ("Delete");
		delete.setBounds( 40,480,90,20);
		StyleItems.styleLabel  (delete);
		delete.addActionListener(new DeleteCustomerActionListener ());
		leftPanel.add(delete);
		add(leftPanel);

	}

	private void createRightPanel() {

		JLabel customerNameLabel = new JLabel ("Customer Name");
		customerNameLabel.setBounds( 10,10,135,20);
		StyleItems.styleLabel (customerNameLabel); 
		infoPanel.add (customerNameLabel);	

		customerName = new JTextField();
		customerName.setBounds( 145,10,230,20);
		StyleItems.styleLabel (customerName); 
		infoPanel.add (customerName);


		JLabel customerCodeLabel = new JLabel ("Customer Code");
		customerCodeLabel.setBounds( 10,40,135,20);
		StyleItems.styleLabel (customerCodeLabel); 
		infoPanel.add (customerCodeLabel);	

		customerCode = new JTextField();
		customerCode.setBounds( 145,40,125,20);
		StyleItems.styleLabel (customerCode); 
		infoPanel.add (customerCode);

		JLabel customerAddressLabel = new JLabel ("Address");
		customerAddressLabel.setBounds( 10,70,135,20);
		StyleItems.styleLabel (customerAddressLabel); 
		infoPanel.add (customerAddressLabel);



		JLabel customerAddressLine1Label = new JLabel ("Line 1");
		customerAddressLine1Label.setBounds( 150,70,85,20);
		StyleItems.styleLabel (customerAddressLine1Label); 
		infoPanel.add (customerAddressLine1Label);

		customerAddressLine1 = new JTextField();
		customerAddressLine1.setBounds( 205,70,185,20);
		StyleItems.styleLabel (customerAddressLine1); 
		infoPanel.add (customerAddressLine1);

		JLabel customerAddressLine2Label = new JLabel ("Line 2");
		customerAddressLine2Label.setBounds( 150,100,85,20);
		StyleItems.styleLabel (customerAddressLine2Label); 
		infoPanel.add (customerAddressLine2Label);

		customerAddressLine2 = new JTextField();
		customerAddressLine2.setBounds( 205,100,185,20);
		StyleItems.styleLabel (customerAddressLine2); 
		infoPanel.add (customerAddressLine2); 

		JLabel customerCityLabel = new JLabel ("City/Village");
		customerCityLabel.setBounds( 150,135,105,20);
		StyleItems.styleLabel (customerCityLabel); 
		infoPanel.add (customerCityLabel);

		customerCity = new JTextField();
		customerCity.setBounds(270,135,120,20);
		StyleItems.styleLabel (customerCity); 
		infoPanel.add (customerCity); 

		JLabel pinCodeLabel = new JLabel ("Pin Code");
		pinCodeLabel.setBounds( 150,165,105,20);
		StyleItems.styleLabel (pinCodeLabel); 
		infoPanel.add (pinCodeLabel);

		pinCode = new JTextField();
		pinCode.setBounds(270,165,120,20);
		StyleItems.styleLabel (pinCode); 
		infoPanel.add (pinCode);

		JLabel stateLabel = new JLabel ("State");
		stateLabel.setBounds( 150,195,105,20);
		StyleItems.styleLabel (stateLabel); 
		infoPanel.add (stateLabel);

		state = new JTextField();
		state.setBounds(270,195,120,20);
		StyleItems.styleLabel (state); 
		infoPanel.add (state); 

		JLabel customerRouteLabel = new JLabel ("Select Route");
		customerRouteLabel.setBounds( 420,60,135,20);
		StyleItems.styleLabel (customerRouteLabel); 
		infoPanel.add (customerRouteLabel);	

		routeListScrollPane = new JScrollPane();
		routeListScrollPane.setBounds( 405, 80, 130, 180 );
		routes = Route.getRouteNames();
		routeRowList = new JList(routes.toArray());
		routeRowList.setVisibleRowCount(12);		 
		routeListScrollPane.setViewportView(routeRowList);
		routeRowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);	 
		//	listScrollPane.add(rowList);
		infoPanel.add(routeListScrollPane);


		JLabel parentNameLabel = new JLabel ("Parent's Name");
		parentNameLabel.setBounds( 10,250,135,20);
		StyleItems.styleLabel (parentNameLabel); 
		infoPanel.add (parentNameLabel);	

		parentName = new JTextField();
		parentName.setBounds( 145,250,230,20);
		StyleItems.styleLabel (parentName); 
		infoPanel.add (parentName);

		JLabel mobileNumberLabel = new JLabel ("Mobile Number");
		mobileNumberLabel.setBounds( 10,280,115,20);
		StyleItems.styleLabel (mobileNumberLabel); 
		infoPanel.add (mobileNumberLabel);	

		mobileNumber = new JTextField();
		mobileNumber.setBounds( 145,280,125,20);
		StyleItems.styleLabel (mobileNumber); 
		infoPanel.add (mobileNumber);

		JLabel adharNumberLabel = new JLabel ("Adhar Number");
		adharNumberLabel.setBounds( 10,310,115,20);
		StyleItems.styleLabel (adharNumberLabel); 
		infoPanel.add (adharNumberLabel);	

		adharNumber = new JTextField();
		adharNumber.setBounds( 145,310,185,20);
		StyleItems.styleLabel (adharNumber); 
		infoPanel.add (adharNumber);

		JLabel voterNumberLabel = new JLabel ("Voter ID Number");
		voterNumberLabel.setBounds( 10,340,155,20);
		StyleItems.styleLabel (voterNumberLabel); 
		infoPanel.add (voterNumberLabel);	

		voterNumber = new JTextField();
		voterNumber.setBounds( 145,340,185,20);
		StyleItems.styleLabel (voterNumber); 
		infoPanel.add (voterNumber);
		
		JLabel creditLimitLabel = new JLabel ("Credit Limit");
		creditLimitLabel.setBounds( 10,370,155,20);
		StyleItems.styleLabel (creditLimitLabel); 
		infoPanel.add (creditLimitLabel);	

		 
		creditLimit.setBounds( 145,370,185,20);
		StyleItems.styleLabel (creditLimit); 
		creditLimit.setValue(new Float(0)); 
		infoPanel.add (creditLimit);
		
		
		JLabel dicountLabel = new JLabel ("Discount in %");
		dicountLabel.setBounds( 10,400,155,20);
		StyleItems.styleLabel (dicountLabel); 
		infoPanel.add (dicountLabel);	

		 
		discountPercent.setBounds( 145,400,185,20);
		StyleItems.styleLabel (discountPercent); 
		discountPercent.setValue(new Float(0)); 
		infoPanel.add (discountPercent);

		JLabel isDefaulterLabel = new JLabel("Is Defaulter?");
		isDefaulterLabel.setBounds( 10, 430, 140, 20);
		StyleItems.styleComponent (isDefaulterLabel);
		infoPanel.add (isDefaulterLabel);

		Icon ic = null;
		isDefaulterCheckBox = new JCheckBox(ic, true);
		isDefaulterCheckBox.setBounds( 160, 430, 20, 20);
		StyleItems.styleComponent (isDefaulterCheckBox);
		infoPanel.add (isDefaulterCheckBox);
			
		
		JButton save = new JButton ("ADD");
		save.setBounds( 10,470,120,20);
		StyleItems.styleLabel (save);
		save.addActionListener(new addCustomerActionListener ());
		infoPanel.add(save);

		JButton modify = new JButton ("Modify");
		modify.setBounds( 140,470,120,20);
		StyleItems.styleLabel (modify);
		modify.addActionListener(new modifyCustomerActionListener ());
		infoPanel.add(modify);


		JButton reset= new JButton ("Clear All Values");
		reset.setBounds( 270,470,180,20);
		StyleItems.styleLabel (reset);
		reset.addActionListener(new ResetCustomerActionListener ());
		infoPanel.add(reset);

		add(infoPanel);
	}

	public class ResetCustomerActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			clearCustomerData ();

		}
	}
	private void recreateRouteList () { 
		routes = Route.getRouteNames();	 	
		routeRowList.setListData(routes.toArray());
		routeListScrollPane.repaint();
	}


	private void recreateCustomerList () {  
		customerList= Customer.getCustomerLists();	 	
		rowList.setListData(customerList.toArray());
		listScrollPane.repaint();
	}

	public class DeleteCustomerActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int dialogButton = JOptionPane.YES_NO_OPTION;
		  	int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){

				int idx = rowList.getSelectedIndex();
				if (idx == -1){

					System.out.println("Please choose a Customer.");
					ToastMessage toastMessage = new ToastMessage("Please choose a Customer",3000, Color.RED);
					toastMessage.setVisible(true);
					return;

				}
				System.out.println (((Customer)rowList.getSelectedValue()).getId());
				Result result = deleteSelectedCustomer();
				if (result.isSuccess())
				{				
					ToastMessage toastMessage = new ToastMessage(result.getMessage(),3000);
					toastMessage.setVisible(true);
					recreateCustomerList ();
					clearCustomerData();
				}
				else {
					ToastMessage toastMessage = new ToastMessage(result.getMessage(),3000, Color.RED);
					toastMessage.setVisible(true);
				}

			}



		}
	} 



private void clearCustomerData() {

	customerName.setText("");
	customerCode.setText("");
	customerAddressLine1.setText("");
	customerAddressLine2.setText("");
	customerCity.setText("");
	pinCode.setText("");
	state.setText("");
	parentName.setText("");
	mobileNumber.setText(""); 
	routeRowList.clearSelection();
	routeListScrollPane.repaint();
	adharNumber.setText("");
	voterNumber.setText("");
	isDefaulterCheckBox.setSelected(false); 
	creditLimit.setValue(new Float(0));
	discountPercent.setValue(new Float(1));

} 

public Result deleteSelectedCustomer() {

	System.out.println (((Customer)rowList.getSelectedValue()).getId());
	Serializable id = ((Customer)rowList.getSelectedValue()).getId();
	return Customer.deleteSelectedRCustomer (id);

}
private void populateCustomerData() {
	System.out.println ( "calling populateCustomerData ");
	Customer selectedCustomer =  (Customer)rowList.getSelectedValue();
	System.out.println ( "selectedCustomer =" + selectedCustomer);
	customerName.setText(selectedCustomer.toString());
	customerCode.setText(selectedCustomer.getCode());
	Address selectedAddress = selectedCustomer.getAddress();
	if (selectedAddress!=null) {
		customerAddressLine1.setText(selectedAddress.getAddressLine1());
		customerAddressLine2.setText(selectedAddress.getAddressLine2());
		customerCity.setText(selectedAddress.getCity());
		pinCode.setText(selectedAddress.getZipCode());
		state.setText(selectedAddress.getState());
	}
	else {
		customerAddressLine1.setText("");
		customerAddressLine2.setText("");
		customerCity.setText("");
		pinCode.setText("");
		state.setText("");
	}
	adharNumber.setText(selectedCustomer.getAdharNo());  
	parentName.setText(selectedCustomer.getParentName()); 
	mobileNumber.setText(selectedCustomer.getMobile());
	voterNumber.setText(selectedCustomer.getVoterIDNumber());
	creditLimit.setValue(selectedCustomer.getCreditLImit());
	discountPercent.setValue(selectedCustomer.getDiscountEligible());
	isDefaulterCheckBox.setSelected(Utility.getValue(selectedCustomer.getIsDefaulter()));
	 
	if (selectedCustomer.getRoute()!=null)
	{
		markCustomerRoute(selectedCustomer.getRoute().getId());
	}
	else
	{   System.out.println ("UNMARKING SELECTION**********");
	routeRowList.clearSelection();
	routeListScrollPane.repaint();
	}

}

// In   list of all routes mark the customer's route as selected

private void markCustomerRoute (int selectedCustomerRouteId){
	int sizeOfRouteList = routeRowList.getModel().getSize();
	routeRowList.setSelectedIndex(-1); // First de-select the route	
	for (int i = 0; i <sizeOfRouteList ; i++) {

		int routeID = ((Route)routeRowList.getModel().getElementAt(i)).getId();
		if (routeID==selectedCustomerRouteId)
		{
			routeRowList.setSelectedIndex(i);
			routeRowList.repaint();
			routeListScrollPane.repaint();
			return;
		}

	}


}

/**
 * Launch the application.
 */
public static void main(String[] args) {
	try {
		//Truck dialog = new Truck();
		CustomerEntry dialog = new CustomerEntry (null, "aaa", false);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//dialog.pack();
		dialog.setSize(850, 560);
		dialog.setVisible(true);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
public class modifyCustomerActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isCustomerNameValid ()) return;

		int idx = rowList.getSelectedIndex();
		if (idx == -1) {
			System.out.println("Please choose a Customer.");
			ToastMessage toastMessage = new ToastMessage("Please choose a Customer",3000);
			toastMessage.setVisible(true);
			return;
		}


		if (customerExistWithSameName(true))
		{
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Add customer with same name?","Warning",dialogButton);
			if(dialogResult == JOptionPane.NO_OPTION){
				return;
			}
		}

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			System.out.println (((Customer)rowList.getSelectedValue()).getId());
			System.out.println("Current selection: " + customerList.get(idx));
			Serializable id = ((Customer)rowList.getSelectedValue()).getId();
			Object persistentInstance = session.load(Customer.class, id);
			Customer customer = (Customer)persistentInstance;
			enterCustomerData (customer);			 
			session.save(customer);
			session.getTransaction().commit();
			ToastMessage toastMessage = new ToastMessage("Customer modified Successfully",3000);
			toastMessage.setVisible(true);
		}
		catch (Exception ex)
		{
			ToastMessage toastMessage = new ToastMessage("Customer not modified",3000);
			toastMessage.setVisible(true);
		}

		finally {
			if(session!=null){
				session.close();
			}
		}
		recreateCustomerList();	
	}

}

public class addCustomerActionListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		if (!isCustomerNameValid ()) return;

		// As such there can be customer with same name


		if (customerExistWithSameName(false))
		{
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Add customer with same name?","Warning",dialogButton);
			if(dialogResult == JOptionPane.NO_OPTION){
				return;
			}
		}


		int idx = routeRowList.getSelectedIndex();
		if (idx == -1) {
			System.out.println("Please choose a Route First.");
			ToastMessage toastMessage = new ToastMessage("Please choose a Route of Customer",3000);
			toastMessage.setVisible(true);
			return;
		}


		Customer customer = new Customer();
		enterCustomerData( customer);
		if (Customer.addCustomer(customer)) {
			ToastMessage toastMessage = new ToastMessage("Customer Saved Successfully",3000);
			toastMessage.setVisible(true);
		}
		else {
			ToastMessage toastMessage = new ToastMessage("Customer Not Saved",3000, Color.RED);
			toastMessage.setVisible(true);

		}
 
		recreateCustomerList();
	}
}

// Populate Customer data from user input. 	
private  void  enterCustomerData(Customer customer) {

	customer.setName(customerName.getText());
	customer.setCode(customerCode.getText());
	Address address = new Address();
	address.setAddressLine1(customerAddressLine1.getText());
	address.setAddressLine2(customerAddressLine2.getText());
	address.setCity(customerCity.getText());
	address.setZipCode(pinCode.getText());
	address.setState(state.getText());
	customer.setMobile(mobileNumber.getText());
	customer.setAddress(address);
	customer.setParentName(parentName.getText());
	customer.setAdharNo(adharNumber.getText());
	customer.setVoterIDNumber(voterNumber.getText());
	customer.setCreditLImit( (Float) creditLimit.getValue());	 
	customer.setDiscountEligible ( (Float) discountPercent.getValue());
	customer.setIsDefaulter( isDefaulterCheckBox.isSelected());
	if (routeRowList.getSelectedIndex()!=-1)
	{
		customer.setRoute((Route) routeRowList.getSelectedValue());
	}

}




public boolean deleteSelectedRCustomer() {
	try {
		System.out.println (((Customer)rowList.getSelectedValue()).getId());
		//	System.out.println("Current selection: " + routes.get(idx));
		session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Serializable id = ((Customer)rowList.getSelectedValue()).getId();
		Object persistentInstance = session.load(Customer.class, id);
		if (persistentInstance != null)  {
			session.delete(persistentInstance);
			session.getTransaction().commit();
			return true;
		}
		else {
			System.out.println ("Did not find the Route Object in persistance");
			return false;
		}

	} 
	catch (HibernateException e) {
		e.printStackTrace();
		return false;
	}

	finally {
		if(session!=null){
			session.close();
		}
	}
}

private boolean isCustomerNameValid() {

	String enteredCustomerName =  customerName.getText();
	if (enteredCustomerName.trim().isEmpty())
	{
		ToastMessage toastMessage = new ToastMessage("Customer name not valid",3000);
		toastMessage.setVisible(true);
		return false;
	}
	else return true;

}

// Checks if a Customer Name already exists.	
// It is called within a hibernate transaction
// Currently this is not being used
private boolean customerExistWithSameName(boolean modify) {

	String selectedCustomerName = customerName.getText();


	// In case of modifying a Customer we need to make sure name
	// does not clash with other  names. But it can be same with current name.
	if (modify) {
		String existingName = ((Customer)rowList.getSelectedValue()).getName();
		if (selectedCustomerName.equals(existingName))
		{			
			return false;
		}
	}
	try{
		session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		String hql = "from Customer where  name =:cust";
		Query query = session.createQuery(hql);
		query.setParameter("cust", selectedCustomerName);
		List<Customer> listProducts = query.list();
		if ((listProducts == null) || listProducts.size() == 0) {
			System.out.println("Customer does not Exists");
			return false;
		} 
		else {
			ToastMessage toastMessage = new ToastMessage("Customer Already Exists with same name", 3000);
			toastMessage.setVisible(true);
			return true;
		} 
	}
	catch (HibernateException e) {
		e.printStackTrace();
		return false;
	}

	finally {
		if(session!=null){
			session.close();
		}
	}
}





}
