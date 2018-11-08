package nmfc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;



import javax.swing.DefaultListModel;
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

import entities.Country;
import entities.Fruit;
import entities.Route;

import nmfc.RoutePage.DeleteRouteActionListener;
import nmfc.RoutePage.modifyRouteActionListener;
import nmfc.TruckPage.CloseActionListener;
import nmfc.helper.CloseButton;
import nmfc.helper.ToastMessage;
import persistence.HibernateUtil;

public class RoutePage extends JDialog {

	private Font buttonFont = new Font("Courier", Font.BOLD,13);
	private Color buttonText = new Color(90, 10, 20);
	private JTextField enterRoute;
	private JPanel rightPanel = new JPanel();
	private JList rowList;
	private List routes;
	private Session session;
	private JScrollPane listScrollPane;
	JPanel leftPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RoutePage dialog = new RoutePage (null, "Route Master Page", false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
			dialog.setSize(520, 370);
			dialog.setVisible(true);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public RoutePage(Frame homePage, String string, boolean b) {
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

			JLabel enterRouteLabel = new JLabel ("Enter Route Name");
			enterRouteLabel.setBounds( 10, 30, 150, 30);
			styleComponent (enterRouteLabel);
			leftPanel.add (enterRouteLabel);

			enterRoute = new JTextField ();
			enterRoute.setBounds( 10,70,155,20);
			styleComponent (enterRoute);
			leftPanel.add(enterRoute);



			JButton save = new JButton ("ADD");
			save.setBounds( 10,140,90,20);
			styleComponent (save);
			save.addActionListener(new SaveRouteActionListener ());
			leftPanel.add(save);

			JButton modify = new JButton ("Modify");
			modify.setBounds( 120,140,90,20);
			styleComponent (modify);
			modify.addActionListener(new modifyRouteActionListener ());
			leftPanel.add(modify);
			add(leftPanel);
			}

	private void createRightPane() {
		rightPanel.setLayout( null );
		rightPanel.setBounds( 240, 10, 260, 270 );
		rightPanel.setBackground(new Color(200, 230, 210) );
		rightPanel.setBorder(new LineBorder(new Color(0, 80, 70), 2));

		JLabel labelListOfRoute = new JLabel ("List of Routes");
		labelListOfRoute.setBounds( 10, 10, 150, 20);
		styleComponent (labelListOfRoute);
		rightPanel.add (labelListOfRoute);

		listScrollPane = new JScrollPane();
		listScrollPane.setBounds( 30, 40, 170, 150 );
		routes = Route.getRouteNames();
		rowList = new JList(routes.toArray());
		rowList.setVisibleRowCount(12);
		rowList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent le) {
				int idx = rowList.getSelectedIndex();
				if (idx != -1)
				{
					System.out.println("Current selection: " + routes.get(idx));

					enterRoute.setText(rowList.getSelectedValue().toString());
				}
				else
					System.out.println("Please choose a Route.");
			}
		});
		listScrollPane.setViewportView(rowList);
		rowList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

		//	listScrollPane.add(rowList);
		rightPanel.add(listScrollPane);

		JButton delete = new JButton ("Delete");
		delete.setBounds( 40,220,90,20);
		styleComponent (delete);
		delete.addActionListener(new DeleteRouteActionListener ());
		rightPanel.add(delete);

		add(rightPanel);


	}

	// Return true if a route already exists with same name.
	private boolean selctedRouteExits() {
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();	
			String routeName =  enterRoute.getText();
			String hql = "from Route where  name =" + "'"+routeName +"'"  ;
			Query query = session.createQuery(hql);
			List<Route> listProducts = query.list();			
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
	// Create a new Route
	 
	class SaveRouteActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (!isNameValid ()) return;

			if (selctedRouteExits())
			{
				ToastMessage toastMessage = new ToastMessage("Route Already Exists",3000);
				toastMessage.setVisible(true);
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();				 
				Route route = new Route();
				route.setName(enterRoute.getText());			 
			    session.save(route);
				session.getTransaction().commit(); 
				ToastMessage toastMessage = new ToastMessage("Route Saved Successfully",3000);
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
			recreateRouteList ();
		}


	}

	public class DeleteRouteActionListener implements ActionListener {

 	@Override
		public void actionPerformed(ActionEvent e) {
 		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Delete?","Warning",dialogButton);
		if(dialogResult == JOptionPane.YES_OPTION){
			try {
				int idx = rowList.getSelectedIndex();
				if (idx != -1){
					System.out.println("Current selection: " + routes.get(idx));
					if (deleteSelectedRoute())
					{				
						ToastMessage toastMessage = new ToastMessage("Route Deleted Successfully",3000);
						toastMessage.setVisible(true);	
					}
					else {
						ToastMessage toastMessage = new ToastMessage("Route Not Deleted",3000);
						toastMessage.setVisible(true);
					}					 
				}
				else {
					System.out.println("Please choose a route.");
					ToastMessage toastMessage = new ToastMessage("Please choose a route",3000);
					toastMessage.setVisible(true);

				}

			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("Could not delete route",3000);
				toastMessage.setVisible(true);
			}			 
			recreateRouteList ();
		}
	}

	}
	public class modifyRouteActionListener implements ActionListener {


		public void actionPerformed(ActionEvent e) {
			if (!isNameValid ()) return;
			int idx = rowList.getSelectedIndex();
			if (idx == -1)  {
				System.out.println("Please choose a  Route.");
				ToastMessage toastMessage = new ToastMessage("Please choose a Route",3000);
				toastMessage.setVisible(true);
				return;

			}
			
			if (selctedRouteExits())
			{
				ToastMessage toastMessage = new ToastMessage("Route Already Exists",3000);
				toastMessage.setVisible(true);
				return;
			}

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();

			

				System.out.println (((Route)rowList.getSelectedValue()).getId());
				System.out.println("Current selection: " + routes.get(idx));
				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();
				Serializable id = ((Route)rowList.getSelectedValue()).getId();
				Object persistentInstance = session.load(Route.class, id);
				Route route = (Route)persistentInstance;
				route.setName(enterRoute.getText());			 
				int routeId = (Integer) session.save(route);
				session.getTransaction().commit();
				enterRoute.setText("");
				enterRoute.repaint();
				ToastMessage toastMessage = new ToastMessage("Route Saved Successfully",3000);
				toastMessage.setVisible(true);				 

			}
			catch (Exception ex)
			{
				ToastMessage toastMessage = new ToastMessage("Route Not Saved ",3000);
				toastMessage.setVisible(true);		
			}

			finally {
				if(session!=null){
					session.close();
				}
			}
			recreateRouteList ();
		}		

	}

	private void recreateRouteList () { 
		routes = Route.getRouteNames();	 	
		System.out.println("Selected Route  size = "  + routes.size());
		rowList.setListData(routes.toArray());
		listScrollPane.repaint();
	}

	private boolean isNameValid() {

		String routeName =  enterRoute.getText();
		if (routeName.isEmpty())
		{
			ToastMessage toastMessage = new ToastMessage("Route Name not valid",3000);
			toastMessage.setVisible(true);
			return false;
		}
		else return true;

	}

	// Deletes Selected Route
	// Return true if success
	public boolean deleteSelectedRoute ()
	{
		try {
			System.out.println (((Route)rowList.getSelectedValue()).getId());
			//	System.out.println("Current selection: " + routes.get(idx));
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Serializable id = ((Route)rowList.getSelectedValue()).getId();
			Object persistentInstance = session.load(Route.class, id);
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
			if(session!=null&&session.isOpen()){
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