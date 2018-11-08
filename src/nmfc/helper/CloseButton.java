package nmfc.helper;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.LineBorder;


public class CloseButton extends JButton {
	Window parent;
	
	public CloseButton (Window parent, String label)
	{
		super ( label );
		this.parent = parent;
		setForeground(StyleItems.buttonTextColor);
		setFont(StyleItems.buttonFont);
		setBorder(new LineBorder(new Color(200, 80, 70), 2));
		addActionListener(new CloseActionListener());
	}
	
	public CloseButton (Window parent)
	{
		super ("Close");
		this.parent = parent;
		setForeground(StyleItems.buttonTextColor);
		setFont(StyleItems.buttonFont);
		setBorder(new LineBorder(new Color(200, 80, 70), 2));
		addActionListener(new CloseActionListener());
	}
	
	
	// An action listener to be used when an action is performed
		// In this case when close button is clicked this event occurred
			class CloseActionListener implements ActionListener {

				//close and dispose of the window.
				public void actionPerformed(ActionEvent e) {
					System.out.println("disposing the window..");
					setVisible(false);
					parent.dispose(); 
				}
			}

}
