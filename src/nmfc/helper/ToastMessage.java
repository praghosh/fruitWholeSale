package nmfc.helper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ToastMessage extends JDialog {
	 
	int miliseconds;
	
	public ToastMessage(String toastString, int time){
		this (toastString, time, Color.BLUE);
	}
    public ToastMessage(String toastString, int time, Color color) {
        this.miliseconds = time;
        setUndecorated(true);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(246, 243, 220));
        panel.setBorder(new LineBorder(Color.BLACK, 2));
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel toastLabel = new JLabel("");
        toastLabel.setText(toastString);
        toastLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        toastLabel.setForeground(color);

        setBounds(100, 100, toastLabel.getPreferredSize().width+20, 31);


        setAlwaysOnTop(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int y = dim.height/2-getSize().height/2;
        int half = y/2;
      //  setLocation(dim.width/2-getSize().width/2, y+half);
        setLocation(dim.width/2-getSize().width/2, y);
        panel.add(toastLabel);
        setVisible(false);

        new Thread(){
            public void run() {
                try {
                    Thread.sleep(miliseconds);
                    dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
