package nmfc.helper;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXDatePicker;


public class StyleItems {
	
	final static public Font  buttonFont = new Font("Courier", Font.BOLD,14); 
	final static public Font  smallFont = new Font("Courier", Font.BOLD,12);
	final static public Font  verySmallFont = new Font("Courier", Font.BOLD,8);
	final static public Font  mediumFont = new Font("Arial", Font.BOLD,11);
	
	final static public Font  bigTableFont =new Font("Consolas", Font.BOLD, 14);
	final static public Font  mediumTableFont =new Font("Consolas", Font.BOLD, 12);
	final static public Font  smallTableFont =new Font("Consolas", Font.BOLD, 10);
	
	final static public Font  bigButtonFont = new Font("Courier", Font.BOLD,16);
	final static public Font  veryBigButtonFont = new Font("Courier", Font.BOLD, 18);
	final static public Color buttonTextColor = new Color(90, 10, 20);
	final static public Color buttonTextColor2 = new Color(20, 40, 30);
	final static public Color componentForegroundColor = new Color(90, 50, 20);
	final static public Color componentBackgroundColor = new Color(248, 243, 255);
	 
	
	final static public Color buttonBorder = new Color(180, 100, 160);
	final static public Font  listFont = new Font("Courier", Font.BOLD, 13);
	final static public Color listTextColor = new Color(90, 80, 20);
	final static public Color lightYellowBackGround = new Color(255, 243, 215);
	final static public Color lightPinkBackGround = new Color(255, 215, 225);
	final static public Color veryLightYellowBackGround = new Color(255, 249, 223);
	
	final static public Color listBorderColor = new Color(100, 110, 55);
	final static public Color lightRed = new Color(240, 110, 85);
	final static public Color darkRed = new Color(220, 10, 15);
	final static public Color darkBrown = new Color(80,40, 35);
	 
	
	final static public Color componentForegroundColor2 = new Color(130, 40, 110);
	final static public Color componentForegroundColor3 = new Color(78, 29, 10);
	final static public Color componentBackgroundColor2 = new Color(208, 249, 255);
	final static public Color componentBackgroundColor3 = new Color(198, 229, 255);
	
	
	public static void styleComponent(JComponent comp)
	{
		comp.setForeground(componentForegroundColor);
		comp.setFont(buttonFont);
	}
	
	public static void styleComponentBig (JComponent comp)
	{
		comp.setForeground(componentForegroundColor2);
		comp.setFont(bigButtonFont);
	}
	
	public static void styleComponent2 (JComponent comp)
	{
		comp.setForeground(componentForegroundColor2);
		comp.setFont(bigButtonFont);
	}
	
	public static void styleComponentSmall(JComponent comp)
	{
		comp.setForeground(componentForegroundColor);
		comp.setFont(smallFont);
	}
	
	public static void styleComponentSmall2 (JComponent comp)
	{
		comp.setForeground(componentForegroundColor2);
		comp.setFont(smallFont);
	}
	
	public static void styleDatePickerSmall(JXDatePicker date)
	{
		date.setForeground(buttonTextColor);
		date.setFont(smallFont);
	}
	public static void styleDatePicker(JXDatePicker date)
	{
		date.setForeground(buttonTextColor);
		date.setFont(buttonFont);
	}

	public static void styleButtont(JButton button)
	{
		button.setForeground(buttonTextColor);
		button.setFont(buttonFont);
		button.setBorder(new LineBorder(buttonBorder, 2));
	}
	
	public static void styleButton2(JButton button)
	{
		button.setForeground(buttonTextColor2);
	//	button.setForeground(componentBackgroundColor2);
		button.setFont(buttonFont);
		button.setBorder(new LineBorder(buttonBorder, 2));
	}
	
	public static void styleLabel(JComponent item) {
		item.setForeground(buttonTextColor);
		item.setFont(buttonFont);
		
	} 
	
	public static void styleList(JList item)
	{
		item.setForeground(listTextColor);
		item.setFont(listFont);
		item.setBorder(new LineBorder(buttonBorder, 1));
		item.setBackground(lightYellowBackGround);
	}
	public static void styleList2 (JList item)
	{
		item.setForeground(listTextColor);
		item.setFont(listFont);
		item.setBorder(new LineBorder(buttonBorder, 1));
		item.setBackground(lightPinkBackGround);
	}
	
	public static void styleTableSmallFont(JTable table) {
		table.setFont(smallTableFont);
		table.setForeground(darkBrown);
		table.setBackground(lightYellowBackGround);	
		
	}

	public static void styleTableMediumFont(JTable table) {
		table.setFont(mediumTableFont);
		table.setForeground(darkBrown); 
		table.setBackground(lightYellowBackGround);	
		
	}
	
	public static void styleTableMediumFont2(JTable table) {
		table.setFont(mediumTableFont);
		table.setForeground(componentForegroundColor3); 
		table.setBackground(veryLightYellowBackGround);	
		
	}

	public static void styleTableBigFont(JTable table) {
		table.setFont(bigTableFont);
		table.setForeground(darkBrown); 
	}

}
