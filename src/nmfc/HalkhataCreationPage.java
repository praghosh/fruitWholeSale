package nmfc;

 
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdesktop.swingx.JXDatePicker;

import entities.HalKhata;
import entities.Franchise;
import entities.Fruit;
import entities.Route;

import nmfc.helper.CloseButton;
import nmfc.helper.DateUtility;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import persistence.HibernateUtil;



public class HalkhataCreationPage  extends JDialog{

	private JTextField enterHalkhataName = new JTextField ();
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel(); 
	private JScrollPane listScrollPane = new JScrollPane();
	private JList rowList;
	private List  halkhataItems;
	private Session session;
	private HalKhata selectedHalKhata;
	private JXDatePicker fromEntryDatePicker=new JXDatePicker();;
	private JXDatePicker toEntryDatePicker=new JXDatePicker();;

	public class DeleteHalkhataItemsActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {


			int idx = rowList.getSelectedIndex();
			if (idx == -1)  {
				System.out.println("Please choose a  HalKhata.");
				ToastMessage toastMessage = new ToastMessage("Please choose a Halkhata.",3000);
				toastMessage.setVisible(true);
				return;

			}
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){


				selectedHalKhata = (HalKhata) rowList.getSelectedValue();
				Serializable id =  selectedHalKhata.getId();
				if (HalKhata.deleteSelectedHalkhataItems(id))
				{				
					ToastMessage toastMessage = new ToastMessage("Halkhata deleted Successfully",3000);
					toastMessage.setVisible(true);
					enterHalkhataName.setText("");
					refreshHalkhataItemsList ();

				}
				else {
					ToastMessage toastMessage = new ToastMessage("Halkhata Not Deleted",3000);
					toastMessage.setVisible(true);
				}
			}

		} 
 
	}

	public class ModifyHalkhataItemsActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (!isNameValid ()) return;
			int idx = rowList.getSelectedIndex();
			if (idx == -1)  {
				System.out.println("Please choose a Halkhata .");
				ToastMessage toastMessage = new ToastMessage("Please choose a Halkhata ",3000);
				toastMessage.setVisible(true);
				return;

			}

			if (selctedItemExits(true))
			{
				ToastMessage toastMessage = new ToastMessage("Halkhata  Exists",3000);
				toastMessage.setVisible(true);
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();
				System.out.println (((HalKhata)rowList.getSelectedValue()).getId());
				System.out.println("Current selection: " + halkhataItems.get(idx));				 
				Serializable id = ((HalKhata)rowList.getSelectedValue()).getId();
				Object persistentInstance = session.load(HalKhata.class, id);
				HalKhata halkhataItem = (HalKhata)persistentInstance;
				udateHalkhataData (halkhataItem);
				session.save(halkhataItem);
				session.getTransaction().commit();
				enterHalkhataName.setText("");
				enterHalkhataName.repaint();
				ToastMessage toastMessage = new ToastMessage("Hakhata  Modified Successfully",3000);
				toastMessage.setVisible(true);

			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("HalKhata Not Modified ",3000);
				toastMessage.setVisible(true);
				return;
			}

			finally {
				if(session!=null){
					session.close();
				}
			}
			refreshHalkhataItemsList();

		}	

	}
	
	
	/**
	 * 
	 * @return if the fromDate is previous to toDate.
	 */
	 
	private boolean isDateValid () {
		
		Date fromDate = fromEntryDatePicker.getDate();
		Date toDate = toEntryDatePicker.getDate();
	 			
		System.out.println(fromDate.compareTo(toDate) );
		System.out.println(fromDate.compareTo(toDate) <=0);
		return  true;
	}
	

	public class SaveHalkhataItemsActionListener implements ActionListener {


		@Override
		public void actionPerformed(ActionEvent e) {


			// Save a persistent entity
			// Create a new Halkhata Item
			if (!isNameValid ()) return;

			if (selctedItemExits(false)) 
			{
				ToastMessage toastMessage = new ToastMessage("Halkhata Item Already Exists",3000);
				toastMessage.setVisible(true);			
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();				 
				HalKhata item = new HalKhata();
				udateHalkhataData (item);				 
				session.save(item);
				session.getTransaction().commit();
				ToastMessage toastMessage = new ToastMessage("Halkhata Item Saved Successfully",3000);
				toastMessage.setVisible(true);
			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("Halkhata Item could not be Saved",3000);
				toastMessage.setVisible(true);
				System.out.println(ex.getMessage());				
				return;
			}

			finally {
				if(session!=null){
					session.flush();
					session.close();
				}
			}
			refreshHalkhataItemsList();			
		}

	}


	private void refreshHalkhataItemsList() {

		halkhataItems = HalKhata.getHalkhataItems();
		System.out.println("HalkhataItems size " + halkhataItems.size() );
		rowList.setListData(halkhataItems.toArray());	 	  
		listScrollPane.repaint();

	}

	public void udateHalkhataData(HalKhata item) {
		item.setName(enterHalkhataName.getText().trim());	
		item.setStartDate(fromEntryDatePicker.getDate());
		item.setEndDate(toEntryDatePicker.getDate());
 	}

	public HalkhataCreationPage (Frame homePage, String string ) {
		// TODO Auto-generated constructor stub
	//	super (homePage,  string,  b);
		super ( homePage, string, false );
		getContentPane().setLayout( null );
		setBounds( 160, 140, 545, 220 );
		getContentPane().setBackground(new Color(230, 245, 180) );

		createLeftPane();
		createRightPane();


		JButton clsoeButton = new CloseButton(this);
		clsoeButton.setBounds( 210, 450, 150, 30 );
		add(clsoeButton);
	}

	private void createLeftPane() {

		// Create LeftPanel, Entry of HalKhata 

		leftPanel.setLayout( null );
		leftPanel.setBounds( 10, 10, 300, 430 );
		leftPanel.setBackground(new Color(200, 230, 210) );
		leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));

		JLabel enterHalKhataLabel = new JLabel (
				"<html> Enter Halkhta  Name <br>") ;					 
		enterHalKhataLabel.setBounds( 10, 20, 200, 30);
		StyleItems.styleComponent (enterHalKhataLabel);
		leftPanel.add (enterHalKhataLabel);

		/*
		JLabel returnableItemsLabel = new JLabel("Returnable Item?");
		returnableItemsLabel.setBounds( 10, 170, 140, 20);
		StyleItems.styleComponent (returnableItemsLabel);
		leftPanel.add (returnableItemsLabel);

		Icon ic = null;
		returnableItems = new JCheckBox(ic, true);
		returnableItems.setBounds( 150, 170, 20, 20);
		StyleItems.styleComponent (returnableItems);
		leftPanel.add (returnableItems);
		 */
		
        // Name of Halkhata
		enterHalkhataName.setBounds( 10,50,145,20);
		StyleItems.styleComponent (enterHalkhataName);
		leftPanel.add(enterHalkhataName);
		
		JLabel periiobLabel = new JLabel ("HalKhata Perid:");
		periiobLabel.setBounds( 10, 120,170,25);
		StyleItems.styleComponent  (periiobLabel);
		leftPanel.add(periiobLabel);
		
		JLabel fromDateLabel = new JLabel ("From:");
		fromDateLabel.setBounds( 10, 150,60,25);
		StyleItems.styleComponent (fromDateLabel);
		leftPanel.add(fromDateLabel);

		fromEntryDatePicker.setDate(DateUtility.getFirstDateOfMonth());
		StyleItems.styleDatePickerSmall(fromEntryDatePicker);
		fromEntryDatePicker.setBounds( 120, 150,108,20);
		fromEntryDatePicker.setFormats("dd/MM/yyyy");

		PropertyChangeListener fromDateListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {

				// When page opens and closes these event is called with 
				// name ancestor. This is quite unnecesary.
				if ("ancestor".equals(e.getPropertyName())) { 
					return;
				}

				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("fromEntryDatePicker.isEditValid()  " +fromEntryDatePicker.isEditValid());
				if ((fromEntryDatePicker.getDate()!=null) && fromEntryDatePicker.isEditValid()) {
					System.out.println(fromEntryDatePicker.getDate());					 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		fromEntryDatePicker.addPropertyChangeListener(fromDateListener);
		leftPanel.add (fromEntryDatePicker);

		JLabel toDateLabel = new JLabel ("To:");
		toDateLabel.setBounds( 10, 180,60,25);
		StyleItems.styleComponent (toDateLabel);
		leftPanel.add(toDateLabel);

		toEntryDatePicker.setDate(DateUtility.getLastDateOfMonth());
		StyleItems.styleDatePickerSmall(toEntryDatePicker);
		toEntryDatePicker.setBounds( 120, 180,108,25);
		toEntryDatePicker.setFormats("dd/MM/yyyy");		
		leftPanel.add (toEntryDatePicker);

		PropertyChangeListener toDateListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("**receiveDatePicker -- Property change");
				//	System.out.println("**receiveDatePicker.getDate()  " + receiveDatePicker.getDate());
				System.out.println("toEntryDatePicker.isEditValid()  " +toEntryDatePicker.isEditValid());
				if ((toEntryDatePicker.getDate()!=null) && toEntryDatePicker.isEditValid()) {
					System.out.println(toEntryDatePicker.getDate());
					 
				}
				else {				
					System.out.println("Date Edit Not Valid");
				}
			}
		};
		toEntryDatePicker.addPropertyChangeListener(toDateListener);
 


		JButton save = new JButton ("ADD");
		save.setBounds( 10,330,90,20);
		StyleItems.styleComponent (save);
		save.addActionListener(new SaveHalkhataItemsActionListener ());
		leftPanel.add(save);


		add(leftPanel);


	}

	private void createRightPane() {
		rightPanel = new JPanel(); 
		rightPanel.setLayout( null );
		rightPanel.setBounds( 315, 10, 260, 430 );
		rightPanel.setBackground(new Color(200, 230, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));

		JLabel labelListOfHalKhata = new JLabel ("<html>List of Halkhata Created");
		labelListOfHalKhata.setBounds( 20, 10, 220, 40);
		StyleItems.styleComponent (labelListOfHalKhata);
		rightPanel.add (labelListOfHalKhata);


		listScrollPane.setBounds( 30, 55, 170, 150 );
		halkhataItems = HalKhata.getHalkhataItems();
		rowList = new JList(halkhataItems.toArray());
		rowList.setVisibleRowCount(12);
		listScrollPane.setViewportView(rowList);
		rowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		rowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = rowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " + halkhataItems.get(idx));					 
					displayHalkhatInfo ();
		 
				}
				else
					System.out.println("Please choose a Container Items.");
			}

			 
		});

		rightPanel.add(listScrollPane);

		JButton delete = new JButton ("Delete");
		delete.setBounds( 30,250,90,20);
		StyleItems.styleComponent (delete);
		delete.addActionListener(new DeleteHalkhataItemsActionListener ());
		rightPanel.add(delete);

		JButton modify = new JButton ("Modify");
		modify.setBounds( 140,250,90,20);
		StyleItems.styleComponent (modify);
		modify.addActionListener(new ModifyHalkhataItemsActionListener ());
		rightPanel.add(modify);

		add(rightPanel);

	}

	protected void displayHalkhatInfo() {
		HalKhata item =  (HalKhata) rowList.getSelectedValue();		 
		enterHalkhataName.setText(item.toString());
		fromEntryDatePicker.setDate(item.getStartDate() );
		toEntryDatePicker.setDate(item.getEndDate() );
		
	}

	private boolean isNameValid() {

		String routeName =  enterHalkhataName.getText();
		if (routeName.trim().isEmpty())
		{
			ToastMessage toastMessage = new ToastMessage("Halkhata Name not valid",3000);
			toastMessage.setVisible(true);
			return false;
		}
		else return true;

	}

	private boolean selctedItemExits(boolean modify) {
		String itemName =  enterHalkhataName.getText().trim();

		// In case of modifying a Franchise we need to make sure name
		// does not clash with other  names. But it can be same with current name.
		if (modify)
		{			 
			String existingName = ((HalKhata)rowList.getSelectedValue()).getName();
			if (itemName.equals(existingName)) return false;
		}

		return HalKhata.halkhataItemsExistWithSameName (itemName);
	}

	// Deletes Selected Halkhata
	// Return true if success
	public boolean deleteSelectedHalkhata ()

	{   selectedHalKhata = (HalKhata) rowList.getSelectedValue();
	try {
		session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		selectedHalKhata = (HalKhata) rowList.getSelectedValue();
		Serializable id =  selectedHalKhata.getId();
		Object persistentInstance = session.load(HalKhata.class, id);
		if (persistentInstance != null)  {
			session.delete(persistentInstance);
			session.getTransaction().commit();
			selectedHalKhata=null;
			return true;
		}
		else {
			System.out.println ("Did not find the Halkhata Object in persistance");
			return false;
		}

	} 
	catch (HibernateException e) {
		e.printStackTrace();
		return false;
	}

	finally {
		if(session!=null && session.isOpen()){
			session.close();
		}
	}
	}

	/**
	 * Launch the application 
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			HalkhataCreationPage dialog = new HalkhataCreationPage (null, "Container Items");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(680, 520);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
