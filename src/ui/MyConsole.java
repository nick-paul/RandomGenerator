package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import data.Parser;
import exceptions.ListNotFoundException;

@SuppressWarnings("serial")
public class MyConsole extends JPanel {
	
	public ConsoleWindow cw = new ConsoleWindow();
	public static InputLine il = new InputLine();
	
	private static boolean darkTheme = true;
	private int width = 500;
	private int height = 200;
	
	public MyConsole() {
		this.setLayout(new BorderLayout(0,0));
		
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(10,10));
		
		cw.setPreferredSize(new Dimension(width, height));
		il.setPreferredSize(new Dimension(width, 20));
		il.setMaximumSize(new Dimension(width,20));
		cw.setMaximumSize(new Dimension(width, 0));
		
		cw.setMinimumSize(new Dimension(10,10));
		il.setMinimumSize(new Dimension(10,10));
		
		setDarkTheme(true);
		
		this.add(cw, BorderLayout.CENTER);
		this.add(il, BorderLayout.SOUTH);
	}
	
	public void setDarkTheme(boolean b) {
		darkTheme = b;
		ConsoleWindow.setDarkTheme(b);
		if(b) {
			il.setForeground(new Color(240, 255, 235));
			il.setBackground(new Color(30 , 31 , 25));
			il.setCaretColor(RandomGeneratorGUI.MONOKAI_WHITE); 
		} else {
			il.setForeground(Color.BLACK);
			il.setBackground(Color.WHITE);
			il.setCaretColor(Color.BLACK); 
		}
	}
	
	public boolean getDarkTheme() {
		return darkTheme;
	}
	
	public InputLine getInputLine() {
		return il;
	}
	
	public void eval() {
		if(!il.getText().equals("")) {
			//this.cw.println("\n> " + il.getText());
			
			try {
				ConsoleWindow.println(Parser.removeExtraEscapes(Parser.eval(Parser.formatEscapes(il.getText()))));
			} catch (ListNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			il.clear();
	
			il.grabFocus();
			ConsoleWindow.goToEnd();
		}
	}
	
	public ConsoleWindow getConsole() {
		return this.cw;
	}
}
