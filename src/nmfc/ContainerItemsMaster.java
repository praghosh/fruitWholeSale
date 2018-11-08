package nmfc;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
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

import entities.ContainerItems;
import entities.Franchise;
import entities.Fruit;
import entities.Route;

import nmfc.helper.CloseButton;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import persistence.HibernateUtil;



public class ContainerItemsMaster  extends JFrame{

	private JTextField enterContainerItems = new JTextField ();
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel(); 
	private JScrollPane listScrollPane = new JScrollPane();
	private JList rowList;
	private List  containerItems;
	private Session session;
	private ContainerItems selectedContainerItems;

	public class DeleteContainerItemsActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {


			int idx = rowList.getSelectedIndex();
			if (idx == -1)  {
				System.out.println("Please choose a  selling or purchase unit.");
				ToastMessage toastMessage = new ToastMessage("Please choose a   selling or purchase unit.",3000);
				toastMessage.setVisible(true);
				return;

			}
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){


				selectedContainerItems = (ContainerItems) rowList.getSelectedValue();
				Serializable id =  selectedContainerItems.getId();
				if (ContainerItems.deleteSelectedContainerItems(id))
				{				
					ToastMessage toastMessage = new ToastMessage("  selling or purchase unit deleted Successfully",3000);
					toastMessage.setVisible(true);
					enterContainerItems.setText("");
					refreshContainerItemsList ();

				}
				else {
					ToastMessage toastMessage = new ToastMessage("selling or purchase unit  Not Deleted",3000);
					toastMessage.setVisible(true);
				}
			}

		} 



	}

	public class ModifyContainerItemsActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (!isNameValid ()) return;
			int idx = rowList.getSelectedIndex();
			if (idx == -1)  {
				System.out.println("Please choose a selling or purchase unit .");
				ToastMessage toastMessage = new ToastMessage("Please choose a selling or purchase unit ",3000);
				toastMessage.setVisible(true);
				return;

			}

			if (selctedItemExits(true))
			{
				ToastMessage toastMessage = new ToastMessage("Selling or purchase unit   Exists",3000);
				toastMessage.setVisible(true);
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();
				System.out.println (((ContainerItems)rowList.getSelectedValue()).getId());
				System.out.println("Current selection: " + containerItems.get(idx));				 
				Serializable id = ((ContainerItems)rowList.getSelectedValue()).getId();
				Object persistentInstance = session.load(ContainerItems.class, id);
				ContainerItems containerItem = (ContainerItems)persistentInstance;
				containerItem.setName(enterContainerItems.getText().trim());
				//boolean isreturnable = returnableItems.isSelected();
				//containerItem.setReturnable(isreturnable);
				session.save(containerItem);
				session.getTransaction().commit();
				enterContainerItems.setText("");
				enterContainerItems.repaint();
				ToastMessage toastMessage = new ToastMessage("Selling or purchase unit  Modified Successfully",3000);
				toastMessage.setVisible(true);

			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("Selling or purchase unit  Not Modified ",3000);
				toastMessage.setVisible(true);
				return;
			}

			finally {
				if(session!=null){
					session.close();
				}
			}
			refreshContainerItemsList();

		}	

	}

	public class SaveContainerItemsActionListener implements ActionListener {


		@Override
		public void actionPerformed(ActionEvent e) {


			// Save a persistent entity
			// Create a new Container Item
			if (!isNameValid ()) return;

			if (selctedItemExits(false)) 
			{
				ToastMessage toastMessage = new ToastMessage("Container Item Already Exists",3000);
				toastMessage.setVisible(true);			
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();				 
				ContainerItems item = new ContainerItems();
				item.setName(enterContainerItems.getText().trim());	
				//	boolean isreturnable = returnableItems.isSelected();
				//		item.setReturnable(isreturnable);
				session.save(item);
				session.getTransaction().commit();
				ToastMessage toastMessage = new ToastMessage("Container Item Saved Successfully",3000);
				toastMessage.setVisible(true);
			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("Container Item could not be Saved",3000);
				toastMessage.setVisible(true);
				System.out.println(ex.getMessage());				
				return;
			}

			finally {
				if(session!=null){
					session.close();
				}
			}
			refreshContainerItemsList();			
		}

	}


	private void refreshContainerItemsList() {

		containerItems = ContainerItems.getContainerItemsNames();
		System.out.println("containerItems size " + containerItems.size() );
		rowList.setListData(containerItems.toArray());	 	  
		listScrollPane.repaint();

	}

	public ContainerItemsMaster (Frame homePage, String string, boolean b) {
		// TODO Auto-generated constructor stub
	//	super (homePage,  string,  b);
		super ( string );
		getContentPane().setLayout( null );
		setBounds( 160, 140, 545, 220 );
		getContentPane().setBackground(new Color(230, 245, 180) );

		createLeftPane();
		createRightPane();


		JButton clsoeButton = new CloseButton(this);
		clsoeButton.setBounds( 210, 350, 150, 30 );
		add(clsoeButton);
	}

	private void createLeftPane() {

		// Create LeftPanel, Entry of ContainerItems 

		leftPanel.setLayout( null );
		leftPanel.setBounds( 10, 10, 220, 310 );
		leftPanel.setBackground(new Color(200, 230, 210) );
		leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));

		JLabel enterContainerItemsLabel = new JLabel (
				"<html> Enter Selling or <br> Purchase Unit Name <br>" 
						+
				"(For example, Big Crate, Small Crate etc. )");
		enterContainerItemsLabel.setBounds( 10, 20, 200, 120);
		StyleItems.styleComponent (enterContainerItemsLabel);
		leftPanel.add (enterContainerItemsLabel);

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

		enterContainerItems.setBounds( 10,210,145,20);
		StyleItems.styleComponent (enterContainerItems);
		leftPanel.add(enterContainerItems);





		JButton save = new JButton ("ADD");
		save.setBounds( 10,250,90,20);
		StyleItems.styleComponent (save);
		save.addActionListener(new SaveContainerItemsActionListener ());
		leftPanel.add(save);


		add(leftPanel);


	}

	private void createRightPane() {
		rightPanel = new JPanel(); 
		rightPanel.setLayout( null );
		rightPanel.setBounds( 240, 10, 260, 310 );
		rightPanel.setBackground(new Color(200, 230, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));

		JLabel labelListOfContainerItems = new JLabel ("<html>List of <br> Selling or Purchase Unit**");
		labelListOfContainerItems.setBounds( 10, 10, 220, 40);
		StyleItems.styleComponent (labelListOfContainerItems);
		rightPanel.add (labelListOfContainerItems);


		listScrollPane.setBounds( 30, 55, 170, 150 );
		containerItems = ContainerItems.getContainerItemsNames();
		rowList = new JList(containerItems.toArray());
		rowList.setVisibleRowCount(12);
		listScrollPane.setViewportView(rowList);
		rowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		rowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = rowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " + containerItems.get(idx));
					ContainerItems item =  (ContainerItems) rowList.getSelectedValue();
					enterContainerItems.setText(item.toString());
				//	returnableItems.setSelected(item.isReturnable());

				}
				else
					System.out.println("Please choose a Container Items.");
			}
		});

		rightPanel.add(listScrollPane);

		JButton delete = new JButton ("Delete");
		delete.setBounds( 40,250,90,20);
		StyleItems.styleComponent (delete);
		delete.addActionListener(new DeleteContainerItemsActionListener ());
		rightPanel.add(delete);

		JButton modify = new JButton ("Modify");
		modify.setBounds( 150,250,90,20);
		StyleItems.styleComponent (modify);
		modify.addActionListener(new ModifyContainerItemsActionListener ());
		rightPanel.add(modify);

		add(rightPanel);

	}

	private boolean isNameValid() {

		String routeName =  enterContainerItems.getText();
		if (routeName.trim().isEmpty())
		{
			ToastMessage toastMessage = new ToastMessage("Container Name not valid",3000);
			toastMessage.setVisible(true);
			return false;
		}
		else return true;

	}

	private boolean selctedItemExits(boolean modify) {
		String itemName =  enterContainerItems.getText().trim();

		// In case of modifying a Franchise we need to make sure name
		// does not clash with other  names. But it can be same with current name.
		if (modify)
		{			 
			String existingName = ((ContainerItems)rowList.getSelectedValue()).getName();
			if (itemName.equals(existingName)) return false;
		}

		return ContainerItems.containerItemsExistWithSameName (itemName);
	}

	// Deletes Selected Fruit
	// Return true if success
	public boolean deleteSelectedFruit ()

	{   selectedContainerItems = (ContainerItems) rowList.getSelectedValue();
	try {
		session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		selectedContainerItems = (ContainerItems) rowList.getSelectedValue();
		Serializable id =  selectedContainerItems.getId();
		Object persistentInstance = session.load(ContainerItems.class, id);
		if (persistentInstance != null)  {
			session.delete(persistentInstance);
			session.getTransaction().commit();
			selectedContainerItems=null;
			return true;
		}
		else {
			System.out.println ("Did not find the ContainerItems Object in persistance");
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
			ContainerItemsMaster dialog = new ContainerItemsMaster (null, "Container Items", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(520, 420);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
