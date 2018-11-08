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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import entities.SalesUnit;

import nmfc.helper.CloseButton;
import nmfc.helper.ToastMessage;
import persistence.HibernateUtil;

public class SalesUnitPage extends JDialog {
	private Font buttonFont = new Font("Courier", Font.BOLD,13);
	private Color buttonText = new Color(90, 10, 20);
	private JTextField enterSalesUnit;
	private JPanel rightPanel = new JPanel();
	private JList rowList;
	private List salesItems;
	private Session session;
	private JScrollPane listScrollPane;
	JPanel leftPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SalesUnitPage dialog = new SalesUnitPage (null, "SalesUnit Master Page", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
			dialog.setSize(520, 370);
			dialog.setVisible(true);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public SalesUnitPage(Frame homePage, String string, boolean b) {
 		super (homePage,  string,  b);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 545, 220 );
		getContentPane().setBackground(new Color(230, 245, 180) );
		
		createLeftPane ();

		createRightPane();

		JButton button = new CloseButton(this);
		button.setBounds( 210, 300, 150, 30 );
		button.setBorder(new LineBorder(new Color(200, 80, 70), 2));
		add(button);
 
	}
	// Create LeftPanel, Entry of Ruote
	private void createLeftPane () {
			
			leftPanel.setLayout( null );
			leftPanel.setBounds( 10, 10, 220, 270 );
			leftPanel.setBackground(new Color(200, 230, 210) );
			leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));

			JLabel enterSalesUnitLabel = new JLabel ("Enter Sales Unit");
			enterSalesUnitLabel.setBounds( 10, 30, 170, 30);
			styleComponent (enterSalesUnitLabel);
			leftPanel.add (enterSalesUnitLabel);

			enterSalesUnit = new JTextField ();
			enterSalesUnit.setBounds( 10,70,155,20);
			styleComponent (enterSalesUnit);
			leftPanel.add(enterSalesUnit);



			JButton save = new JButton ("ADD");
			save.setBounds( 10,140,90,20);
			styleComponent (save);
			save.addActionListener(new SaveSalesUnitActionListener ());
			leftPanel.add(save);

			JButton modify = new JButton ("Modify");
			modify.setBounds( 120,140,90,20);
			styleComponent (modify);
			modify.addActionListener(new modifySalesUnitActionListener ());
			leftPanel.add(modify);
			add(leftPanel);
			}

	private void createRightPane() {
		rightPanel.setLayout( null );
		rightPanel.setBounds( 240, 10, 260, 270 );
		rightPanel.setBackground(new Color(200, 230, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));

		JLabel labelListOfSalesUnit = new JLabel ("List of SalesUnits");
		labelListOfSalesUnit.setBounds( 10, 10, 150, 20);
		styleComponent (labelListOfSalesUnit);
		rightPanel.add (labelListOfSalesUnit);

		listScrollPane = new JScrollPane();
		listScrollPane.setBounds( 30, 40, 170, 150 );
		salesItems = SalesUnit.getSalesUnitNames();
		rowList = new JList(salesItems.toArray());
		rowList.setVisibleRowCount(12);
		rowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = rowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " + salesItems.get(idx));

					enterSalesUnit.setText(rowList.getSelectedValue().toString());
				}
				else
					System.out.println("Please choose a SalesUnit.");
			}
		});
		listScrollPane.setViewportView(rowList);
		rowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

		//	listScrollPane.add(rowList);
		rightPanel.add(listScrollPane);

		JButton delete = new JButton ("Delete");
		delete.setBounds( 40,220,90,20);
		styleComponent (delete);
		delete.addActionListener(new DeleteSalesUnitActionListener ());
		rightPanel.add(delete);

		add(rightPanel);


	}

	// Return true if a salesItem already exists with same name.
	private boolean selctedSalesUnitExits() {
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();	
			String salesItemName =  enterSalesUnit.getText();
			String hql = "from SalesUnit where  name =" + "'"+salesItemName +"'"  ;
			Query query = session.createQuery(hql);
			List<SalesUnit> listProducts = query.list();			
			if ((listProducts==null) || listProducts.size()==0){
				System.out.println("Roue Exists");
				return false;
			}
			else return true;
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			return false;
		}

		finally {
			if(session!=null){
				session.close();
			}
		}
	}
	
	// Save a persistent entity
	// Create a new SalesUnit
	 
	class SaveSalesUnitActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (!isNameValid ()) return;

			if (selctedSalesUnitExits())
			{
				ToastMessage toastMessage = new ToastMessage("SalesUnit Already Exists",3000);
				toastMessage.setVisible(true);
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();				 
				SalesUnit salesItem = new SalesUnit();
				salesItem.setName(enterSalesUnit.getText());			 
			    session.save(salesItem);
				session.getTransaction().commit(); 
				ToastMessage toastMessage = new ToastMessage("SalesUnit Saved Successfully",3000);
				toastMessage.setVisible(true);
			}
			catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}

			finally {
				if(session!=null){
					session.close();
				}
			}
			recreateSalesUnitList ();
		}


	}

	public class DeleteSalesUnitActionListener implements ActionListener {

 	@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int idx = rowList.getSelectedIndex();
				if (idx != -1){
					System.out.println("Current selection: " + salesItems.get(idx));
					if (deleteSelectedSalesUnit())
					{				
						ToastMessage toastMessage = new ToastMessage("SalesUnit Deleted Successfully",3000);
						toastMessage.setVisible(true);	
					}
					else {
						ToastMessage toastMessage = new ToastMessage("SalesUnit Not Deleted",3000);
						toastMessage.setVisible(true);
					}					 
				}
				else {
					System.out.println("Please choose a salesItem.");
					ToastMessage toastMessage = new ToastMessage("Please choose a salesItem",3000);
					toastMessage.setVisible(true);

				}

			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("Could not delete salesItem",3000);
				toastMessage.setVisible(true);
			}			 
			recreateSalesUnitList ();
		} 

	}
	public class modifySalesUnitActionListener implements ActionListener {


		public void actionPerformed(ActionEvent e) {
			if (!isNameValid ()) return;
			int idx = rowList.getSelectedIndex();
			if (idx == -1)  {
				System.out.println("Please choose a  SalesUnit.");
				ToastMessage toastMessage = new ToastMessage("Please choose a SalesUnit",3000);
				toastMessage.setVisible(true);
				return;

			}
			
			if (selctedSalesUnitExits())
			{
				ToastMessage toastMessage = new ToastMessage("SalesUnit Already Exists",3000);
				toastMessage.setVisible(true);
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();

			

				System.out.println (((SalesUnit)rowList.getSelectedValue()).getId());
				System.out.println("Current selection: " + salesItems.get(idx));
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();
				Serializable id = ((SalesUnit)rowList.getSelectedValue()).getId();
				Object persistentInstance = session.load(SalesUnit.class, id);
				SalesUnit salesItem = (SalesUnit)persistentInstance;
				salesItem.setName(enterSalesUnit.getText());			 
				int salesItemId = (Integer) session.save(salesItem);
				session.getTransaction().commit();
				enterSalesUnit.setText("");
				enterSalesUnit.repaint();
				ToastMessage toastMessage = new ToastMessage("SalesUnit Saved Successfully",3000);
				toastMessage.setVisible(true);				 

			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("SalesUnit Not Saved ",3000);
				toastMessage.setVisible(true);		
			}

			finally {
				if(session!=null){
					session.close();
				}
			}
			recreateSalesUnitList ();
		}		

	}

	private void recreateSalesUnitList () { 
		salesItems = SalesUnit.getSalesUnitNames();	 	
		System.out.println("Selected Fruit  size = "  + salesItems.size());
		rowList.setListData(salesItems.toArray());
		listScrollPane.repaint();
	}

	private boolean isNameValid() {

		String salesItemName =  enterSalesUnit.getText();
		if (salesItemName.isEmpty())
		{
			ToastMessage toastMessage = new ToastMessage("SalesUnit Name not valid",3000);
			toastMessage.setVisible(true);
			return false;
		}
		else return true;

	}

	// Deletes Selected SalesUnit
	// Return true if success
	public boolean deleteSelectedSalesUnit ()
	{
		try {
			System.out.println (((SalesUnit)rowList.getSelectedValue()).getId());
			//	System.out.println("Current selection: " + salesItems.get(idx));
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Serializable id = ((SalesUnit)rowList.getSelectedValue()).getId();
			Object persistentInstance = session.load(SalesUnit.class, id);
			if (persistentInstance != null)  {
				session.delete(persistentInstance);
				session.getTransaction().commit();
				return true;
			}
			else {
				System.out.println ("Did not find the SalesUnit Object in persistance");
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

	public void styleComponent(JComponent button)
	{
		button.setForeground(buttonText);
		button.setFont(buttonFont);
		//		button.setBorder(new LineBorder(buttonBorder, 2));
	}

}

