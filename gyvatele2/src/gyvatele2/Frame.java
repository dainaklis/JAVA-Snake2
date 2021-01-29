package gyvatele2;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gyvatele2.graphics.Screen;

public class Frame extends JFrame{
	
	public Frame () {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Gyvatele");
		setResizable(false);

		init();
	}
	
	public void init() {
		
		setLayout(new GridLayout(1,1,0,0));
		Screen s = new Screen();
		
		add(s);	
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		
		
	}
	
	public static void main(String[] args) {
		
		new Frame();
		

	}
	

}
