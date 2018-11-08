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

import entities.Fruit;
import entities.FruitQuality;

import nmfc.FruitMasterPage.DeleteFruitQualityActionListener;
import nmfc.FruitMasterPage.SaveFruitQualityActionListener;
import nmfc.FruitMasterPage.modifyFruitQualityActionListener;
import nmfc.helper.StyleItems;
import nmfc.helper.ToastMessage;
import persistence.HibernateUtil;

public class FruitMasterPage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Font buttonFont = new Font("Courier", Font.BOLD,13);
	private Color buttonText = new Color(90, 10, 20);
	private Color buttonBorder = new Color(180, 100, 160);
	private JXDatePicker receiveDatePicker;
	private JTextField enterFruit;
	private JPanel rightPanel;
	private JList rowList;
	private List fruits;
	private Session session;
	private JScrollPane listScrollPane;
	private JPanel leftPanel;
	private JTextField enterFruitQuality;
	private JScrollPane listScrollPaneForQuality;
	protected Fruit selectedFruit;
	private List fruitsQuality;
	private JList rowListOfQuality;
	private FruitQuality selectedFruitQuality;
	private JTextField shortName;


	public class DeleteFruitQualityActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){

				int idx = rowListOfQuality.getSelectedIndex();
				if (idx == -1)  {
					System.out.println("Please choose a fruit quality first.");
					ToastMessage toastMessage = new ToastMessage("Please choose a fruit quality",3000);
					toastMessage.setVisible(true);
					return;
				}

				System.out.println ("Id=" +((FruitQuality)rowListOfQuality.getSelectedValue()).getId());
				if (deleteSelectedFruitQuality())
				{				
					ToastMessage toastMessage = new ToastMessage("Fruit Quality Deleted Successfully",3000);
					toastMessage.setVisible(true);
					enterFruit.setText("");
				}
				else {
					ToastMessage toastMessage = new ToastMessage("Fruit Quality Not Deleted",3000);
					toastMessage.setVisible(true);
				}

				//	recreateFruitList ();
				recreateQualityList();
			} 
		}
	}






	public class modifyFruitQualityActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (!isFruitQualityNameValid ()) return;

			if (selctedFruitQualityExits())
			{
				ToastMessage toastMessage = new ToastMessage("Fruit Quality Already exists",3000);
				toastMessage.setVisible(true);
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();

				int idx = rowList.getSelectedIndex();
				System.out.println (((FruitQuality)rowListOfQuality.getSelectedValue()).getId());
				System.out.println("Current selection: " + fruits.get(idx));
				Serializable id = ((FruitQuality)rowListOfQuality.getSelectedValue()).getId();
				Object persistentInstance = session.load(FruitQuality.class, id);
				FruitQuality fruitQuality = (FruitQuality)persistentInstance;
				fruitQuality.setName(enterFruitQuality.getText());			 
				int fruitId = (Integer) session.save(fruitQuality);
				session.getTransaction().commit();
				enterFruitQuality.setText("");
				enterFruitQuality.repaint();
				ToastMessage toastMessage = new ToastMessage("Fruit Quality Saved Successfully",3000);
				toastMessage.setVisible(true);

			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("Fruit Quality Not Saved",3000);
				toastMessage.setVisible(true);
			}

			finally {
				if(session!=null){
					session.close();
				}
			}
			enterFruitQuality.setText("");
			enterFruitQuality.repaint();
			recreateQualityList();
		}

	}

	public class SaveFruitQualityActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// Save a persistent entity
			// Create a new Fruit Quality
			System.out.println(" **** selectedFruit" + selectedFruit);
			selectedFruit = (Fruit) rowList.getSelectedValue();
			if (!isQualityNameValid()) return;

			int idx = rowList.getSelectedIndex();
			System.out.println(" idx = " + idx + "selectedFruit" + selectedFruit);
			if (selectedFruit == null || idx == -1){
				System.out.println("Please choose a fruit.");
				ToastMessage toastMessage = new ToastMessage("Please choose a fruit first",3000);
				toastMessage.setVisible(true);
				return;
			}
			if (selctedFruitQualityExits())
			{
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();				 
				FruitQuality fqr = new  FruitQuality();
				fqr.setName(enterFruitQuality.getText());
				fqr.setFruit(selectedFruit);
				session.save(fqr);
				session.getTransaction().commit();
				//	enterFruit.repaint();
				ToastMessage toastMessage = new ToastMessage("Fruit Quality Saved Successfully",3000);
				toastMessage.setVisible(true);								

			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("Fruit could not be Saved",3000);
				toastMessage.setVisible(true);
				System.out.println(ex.getMessage());
			}

			finally {
				if(session!=null&& session.isOpen()){
					session.close();
				}
			}
			enterFruitQuality.setText("");
			enterFruitQuality.repaint();
			recreateQualityList();	
		}



	}
	private boolean selctedFruitQualityExits() {
		session = HibernateUtil.getSessionFactory().openSession();
		try {
			String fruitQualityName =  enterFruitQuality.getText();
			selectedFruit = (Fruit) rowList.getSelectedValue();
			String hql = "from FruitQuality where  name =" + "'"+fruitQualityName +"' and fruit =:e"  ;
			Query query = session.createQuery(hql).setParameter("e", selectedFruit);
			List<Fruit> listProducts = query.list();			
			if ((listProducts==null) || listProducts.size()==0){
				System.out.println("Fruit Quality does not Exist");
				return false;
			}
			else {	
				return true;
			}
		}
		finally   {
			if(session!=null&& session.isOpen()){
				session.close();
			}
		}
	}


	public boolean deleteSelectedFruitQuality() {
		selectedFruitQuality = (FruitQuality) rowListOfQuality.getSelectedValue();
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			selectedFruitQuality = (FruitQuality) rowListOfQuality.getSelectedValue();
			Serializable id =  selectedFruitQuality.getId();
			Object persistentInstance = session.load(FruitQuality.class, id);
			if (persistentInstance != null)  {
				session.delete(persistentInstance);
				session.getTransaction().commit();
				selectedFruitQuality=null;
				return true;
			}
			else {
				System.out.println ("Did not find the Fruit Object in persistance");
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


	public boolean isFruitQualityNameValid() {
		String fruitQualityName =  enterFruitQuality.getText().trim();
		if (fruitQualityName.isEmpty())
		{
			ToastMessage toastMessage = new ToastMessage("Fruit Qualty Name not valid",3000);
			toastMessage.setVisible(true);
			return false;
		}
		else return true;
	}


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//Truck dialog = new Truck();
			FruitMasterPage dialog = new FruitMasterPage (null, "aaa", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//dialog.pack();
			dialog.setSize(540, 430);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public FruitMasterPage(Frame homePage, String string, boolean b) {
		// TODO Auto-generated constructor stub
		super (homePage,  string,  b);
		getContentPane().setLayout( null );
		setBounds( 160, 140, 545, 220 );
		getContentPane().setBackground(new Color(230, 205, 210) );



		createLeftPane ();
		add(leftPanel);
		createRightPane();


		JButton button = new JButton("Close Window");
		button.setBounds( 210, 370, 150, 30 );
		button.setBorder(new LineBorder(new Color(200, 80, 70), 2));
		add(button);
		// set action listener on the button
		button.addActionListener(new CloseActionListener());


	}


	private void createLeftPane () {

		leftPanel = new JPanel();
		leftPanel.setLayout( null );
		leftPanel.setBounds( 10, 10, 230,350 );
		leftPanel.setBackground(new Color(200, 230, 210) );
		leftPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));

		JLabel enterFruitLabel = new JLabel ("Enter Fruit Name");
		enterFruitLabel.setBounds( 10, 5, 150, 30);
		styleComponent (enterFruitLabel);
		leftPanel.add (enterFruitLabel);

		enterFruit = new JTextField ();
		enterFruit.setBounds( 10,30,155,20);
		styleComponent (enterFruit);
		leftPanel.add(enterFruit);


		JLabel shortNameLabel = new JLabel ("Short Name");
		shortNameLabel.setBounds( 10, 55, 80, 20);
		styleComponent (shortNameLabel);
		leftPanel.add (shortNameLabel);

		shortName = new JTextField ();
		shortName.setBounds( 120,55,60,20);
		styleComponent (shortName);
		leftPanel.add(shortName);



		JButton save = new JButton ("ADD");
		save.setBounds( 10,90,90,20);
		styleComponent (save);
		save.addActionListener(new SaveFruitActionListener ());
		leftPanel.add(save);

		JButton modify = new JButton ("Modify");
		modify.setBounds( 120,90,90,20);
		styleComponent (modify);
		modify.addActionListener(new modifyFruitActionListener ());
		leftPanel.add(modify);

		JLabel labelListOfFruit = new JLabel ("List of Fruits");
		labelListOfFruit.setBounds( 50, 120, 150, 20);
		styleComponent (labelListOfFruit);
		leftPanel.add (labelListOfFruit);

		listScrollPane = new JScrollPane();
		listScrollPane.setBounds( 30, 140, 170, 150 );
		fruits = Fruit.getFruitNames();
		rowList = new JList(fruits.toArray());
		rowList.setVisibleRowCount(12);
		rowList.addListSelectionListener(new ListSelectionListener() {


			public void valueChanged(ListSelectionEvent le) {
				int idx = rowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " + fruits.get(idx));
					selectedFruit = (Fruit) fruits.get(idx);					 
					enterFruit.setText(rowList.getSelectedValue().toString());
					shortName.setText(selectedFruit.getShortName());
					recreateQualityList ();

				}
				else
					System.out.println("Please choose a Fruit.");
				selectedFruit=null;
			}
		});

		listScrollPane.setViewportView(rowList);
		rowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

		//	listScrollPane.add(rowList);
		leftPanel.add(listScrollPane);

		JButton delete = new JButton ("Delete");
		delete.setBounds( 40,310,90,20);
		styleComponent (delete);
		delete.addActionListener(new DeleteFruitActionListener ());
		leftPanel.add(delete);
	}

	private void createRightPane() {
		rightPanel = new JPanel(); 
		rightPanel.setLayout( null );
		rightPanel.setBounds( 260, 10, 230, 350 );
		rightPanel.setBackground(new Color(200, 230, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));

		JLabel enterFruitQualityLabel = new JLabel ("Enter Fruit Quality");
		enterFruitQualityLabel.setBounds( 10, 10, 200, 30);
		styleComponent (enterFruitQualityLabel);
		rightPanel.add (enterFruitQualityLabel);

		enterFruitQuality = new JTextField ();
		enterFruitQuality.setBounds( 10,40,155,20);
		styleComponent (enterFruitQuality);
		rightPanel.add(enterFruitQuality);

		JButton saveQuality = new JButton ("ADD");
		saveQuality.setBounds( 10,80,90,20);
		styleComponent (saveQuality);
		saveQuality.addActionListener(new SaveFruitQualityActionListener ());
		rightPanel.add(saveQuality);

		JButton modifyQuality = new JButton ("Modify");
		modifyQuality.setBounds( 120,80,90,20);
		styleComponent (modifyQuality);
		modifyQuality.addActionListener(new modifyFruitQualityActionListener ());
		rightPanel.add(modifyQuality);

		JLabel labelListOfFruitQuality = new JLabel ("List of Fruit Quality");
		labelListOfFruitQuality.setBounds( 30, 110, 200, 20);
		styleComponent (labelListOfFruitQuality);
		rightPanel.add (labelListOfFruitQuality);

		listScrollPaneForQuality = new JScrollPane();
		listScrollPaneForQuality.setBounds( 30, 130, 170, 150 );

		System.out.println("Selected Fruit=----------" + (Fruit)selectedFruit);
		fruitsQuality = FruitQuality.getFruitQualityNames((Fruit)selectedFruit);

		System.out.println("Selected Fruit  Quality size="  + fruitsQuality.size());
		rowListOfQuality = new JList(fruitsQuality.toArray());
		rowListOfQuality.setBackground( StyleItems.lightYellowBackGround);  
		rowListOfQuality.setVisibleRowCount(12);
		rowListOfQuality.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = rowList.getSelectedIndex();
				if (idx != -1)
				{
					if (rowListOfQuality.getSelectedValue()!=null){
						enterFruitQuality.setText(rowListOfQuality.getSelectedValue().toString());
					}
					else{
						enterFruitQuality.setText ("");
					}
					enterFruitQuality.repaint();
				}
				else
					System.out.println("Please choose a Fruit.");
			}
		});
		listScrollPaneForQuality.setViewportView(rowListOfQuality);
		rowListOfQuality.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		rightPanel.add(listScrollPaneForQuality); 

		JButton deleteQuality = new JButton ("Delete");
		deleteQuality.setBounds( 40,310,90,20);
		styleComponent (deleteQuality);
		deleteQuality.addActionListener(new DeleteFruitQualityActionListener ());
		rightPanel.add(deleteQuality);

		add(rightPanel);


	}

	private void recreateQualityList () {
		System.out.println(" recreateQualityList -- Selected Fruit=" + selectedFruit);
		selectedFruit = (Fruit) rowList.getSelectedValue();
		fruitsQuality = FruitQuality.getFruitQualityNames(selectedFruit);	 	
		System.out.println("Selected Fruit  Quality size="  + fruitsQuality.size());
		rowListOfQuality.setListData(fruitsQuality.toArray());		
		listScrollPaneForQuality.repaint();
		//    rowListOfQuality.repaint();
		System.out.println(" NOW ### recreateQualityList -- Selected Fruit=" + selectedFruit);
	}

	private void recreateFruitList () { 
		fruits  = Fruit.getFruitNames();	 	
		System.out.println("Selected Fruit  size = "  + fruits.size());
		rowList.setListData(fruits.toArray());
		listScrollPane.repaint();
		//	rowList.repaint();

	}

	private boolean selctedFruitExits() {

		session = HibernateUtil.getSessionFactory().openSession();
		try {
			String fruitName =  enterFruit.getText();
			String hql = "from Fruit where  name =" + "'"+fruitName +"'"  ;
			Query query = session.createQuery(hql);
			List<Fruit> listProducts = query.list();			
			if ((listProducts==null) || listProducts.size()==0){
				System.out.println("Fruit does not Exist");
				return false;
			}
			else {	
				return true;
			}
		}
		finally   {
			if(session!=null&& session.isOpen()){
				session.close();
			}
		}
	}

	// An action listener to be used when an action is performed
	// In this case when close button is clicked this event occurred
	class CloseActionListener implements ActionListener {

		//close and dispose of the window.
		public void actionPerformed(ActionEvent e) {
			System.out.println("disposing the window..");
			setVisible(false);
			dispose();
		}
	}


	class SaveFruitActionListener implements ActionListener {


		public void actionPerformed(ActionEvent e) {
			// Save a persistent entity
			// Create a new Fruit
			if (!isNameValid ()) return;
			if (selctedFruitExits())
			{
				ToastMessage toastMessage = new ToastMessage("Fruit Already Exits ",3000);
				toastMessage.setVisible(true);
				return;
			}			

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();				 
				Fruit fruit = new Fruit();
				fruit.setName(enterFruit.getText());
				fruit.setShortName(shortName.getText());
				//	int fruitId = (Integer) session.save(fruit);
				session.save(fruit);
				// Create a Fruit Quality "N/A" along
				// with creating a fruit
				FruitQuality fruitQuality = new FruitQuality();
				fruitQuality.setName("N/A");
				fruitQuality.setFruit(fruit);
				session.save(fruitQuality);

				session.getTransaction().commit();
				enterFruit.setText("");
				enterFruit.repaint();
				ToastMessage toastMessage = new ToastMessage("Fruit Saved Successfully",3000);
				toastMessage.setVisible(true);

			}
			catch (Exception ex)
			{
				session.getTransaction().rollback();	
				ToastMessage toastMessage = new ToastMessage("Fruit could not be Saved",3000);
				toastMessage.setVisible(true);			
				System.out.println(ex.getMessage());
			}

			finally {
				if(session!=null&& session.isOpen()){
					session.close();
				}
			}
			recreateFruitList (); 
		}


	}

	public class DeleteFruitActionListener implements ActionListener {



		@Override
		public void actionPerformed(ActionEvent e) {
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){

				int idx = rowList.getSelectedIndex();
				if (idx == -1)  {
					System.out.println("Please choose a fruit.");
					ToastMessage toastMessage = new ToastMessage("Please choose a fruit",3000);
					toastMessage.setVisible(true);
					return;
				}

				System.out.println (((Fruit)rowList.getSelectedValue()).getId());
				System.out.println("Current selection: " + fruits.get(idx));
				if (deleteSelectedFruit())
				{				
					ToastMessage toastMessage = new ToastMessage("Fruit Deleted Successfully",3000);
					toastMessage.setVisible(true);
					enterFruit.setText("");
				}
				else {
					ToastMessage toastMessage = new ToastMessage("Fruit Not Deleted",3000);
					toastMessage.setVisible(true);
				}




				recreateFruitList ();
				recreateQualityList();
			} 
		}

	}
	public class modifyFruitActionListener implements ActionListener {


		public void actionPerformed(ActionEvent e) {
			if (!isNameValid ()) return;
			if (selctedFruitExits())
			{
				String fruitName =  enterFruit.getText();
				String existingFrutName = ((Fruit)rowList.getSelectedValue()).getName();
				if (fruitName==null) {
					fruitName ="";
				}
					
				if (!fruitName.equals((existingFrutName))) {
					ToastMessage toastMessage = new ToastMessage("Fruit Already Exists",3000);
					toastMessage.setVisible(true);
					return;
				}
				
			}


			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();
				int idx = rowList.getSelectedIndex();
				System.out.println (((Fruit)rowList.getSelectedValue()).getId());
				System.out.println("Current selection: " + fruits.get(idx));
				Serializable id = ((Fruit)rowList.getSelectedValue()).getId();
				Object persistentInstance = session.load(Fruit.class, id);
				Fruit fruit = (Fruit)persistentInstance;
				fruit.setName(enterFruit.getText());
				fruit.setShortName(shortName.getText());
				session.save(fruit);
				session.getTransaction().commit();
				enterFruit.setText("");
				enterFruit.repaint();
				ToastMessage toastMessage = new ToastMessage("Fruit Saved Successfully",3000);
				toastMessage.setVisible(true);				

			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("Fruit Not Saved ",3000);
				toastMessage.setVisible(true);
			}

			finally {
				if(session!=null){
					session.close();
				}
			}
			recreateFruitList ();
		}		

	}

	private boolean isQualityNameValid() {

		String fruitQualityName =  enterFruitQuality.getText();
		if (fruitQualityName.trim().isEmpty())
		{
			ToastMessage toastMessage = new ToastMessage("Fruit Name not valid",3000);
			toastMessage.setVisible(true);
			return false;
		}
		else return true;

	}

	private boolean isNameValid() {

		String fruitName =  enterFruit.getText().trim();
		if (fruitName.isEmpty())
		{
			ToastMessage toastMessage = new ToastMessage("Fruit Name not valid",3000);
			toastMessage.setVisible(true);
			return false;
		}
		else return true;

	}

	// Deletes Selected Fruit
	// Return true if success
	public boolean deleteSelectedFruit ()

	{   selectedFruit = (Fruit) rowList.getSelectedValue();
	try {
		session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		selectedFruit = (Fruit) rowList.getSelectedValue();
		Serializable id =  selectedFruit.getId();
		Object persistentInstance = session.load(Fruit.class, id);
		if (persistentInstance != null)  {
			session.delete(persistentInstance);
			session.getTransaction().commit();
			selectedFruit=null;
			return true;
		}
		else {
			System.out.println ("Did not find the Fruit Object in persistance");
			return false;
		}

	} 
	catch (HibernateException e) {
		session.getTransaction().rollback();
		e.printStackTrace();
		return false;
	}

	finally {
		if(session!=null && session.isOpen()){
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