package ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


@SuppressWarnings("serial")
public class ConsoleWindow extends JScrollPane {
	
	static JTextPane textpane = new JTextPane();
	
	private static boolean allowExceptionPrinting = true;
	private static boolean allowDebugPrinting = false;
	private static boolean darkTheme = true;
	
	
	//Font Colors
	static MutableAttributeSet red = textpane.addStyle("red", null);
	static MutableAttributeSet gray = textpane.addStyle("gray", null);
	static MutableAttributeSet black = textpane.addStyle("black", null);
	static MutableAttributeSet yellow = textpane.addStyle("yellow", null);
	static MutableAttributeSet comment = textpane.addStyle("comment", null);


	
	static StyledDocument doc = textpane.getStyledDocument();
	
	public ConsoleWindow() {
	    super(textpane);
	    textpane.setEditable(false);
		doc = textpane.getStyledDocument();
		textpane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		
		//Set up colors
        StyleConstants.setForeground(red, new Color(230, 30, 30));	 //A de-saturated red   
        StyleConstants.setForeground(yellow, new Color(230, 230, 0));	    //A de-saturated yellow
        StyleConstants.setForeground(black, Color.BLACK);	   
        StyleConstants.setForeground(gray, Color.GRAY);	   
        StyleConstants.setForeground(comment, RandomGeneratorGUI.MONOKAI_COMMENT);	    

        
        textpane.setBackground(RandomGeneratorGUI.MONOKAI_BG);
        StyleConstants.setForeground(black, RandomGeneratorGUI.MONOKAI_WHITE);
        
        //No border
        setBorder(BorderFactory.createEmptyBorder());

	}
	
	public static void setDarkTheme(boolean b) {
		darkTheme = b;
		if(b) {
	        StyleConstants.setForeground(black, RandomGeneratorGUI.MONOKAI_WHITE);
	        textpane.setBackground(RandomGeneratorGUI.MONOKAI_BG);
		} else {
	        StyleConstants.setForeground(black, Color.BLACK);
	        textpane.setBackground(Color.WHITE);
		}
	}
	
	public static boolean getDarkTheme() {
		return darkTheme;
	}
	
	/**
	 * Prints normal black text to the console
	 * @param text
	 */
	public static void print(String text) {
		try {
			doc.insertString(doc.getLength(), text, black);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints text with the given style to the console
	 * @param text
	 * @param style
	 */
	private static void print(String text, MutableAttributeSet style) {
		try {
			doc.insertString(doc.getLength(), text, style);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void println(String text) {
		print(text + "\n");
	}

	public static void printEx(String string) {
		if(allowExceptionPrinting)
			print(string + "\n", red);
	}
	
	public static void printQuiet(String s) {
		print(s + "\n", comment);		
	}	
	
	public void printWarn(String s) {
		print(s + "\n", yellow);
	}
	
	public void printTime(String s) {
		print(s + "\n", gray);
	}
	
	public void printDebug(String s) {
		if(allowDebugPrinting)
			print(s + "\n", gray);
	}

	public void allowEX(boolean b) {
		allowExceptionPrinting = b;
	}
	
	public void allowDebug(boolean b) {
		print("Debug printing set to: " + b + "\n", gray);
		allowDebugPrinting = b;
	}
	
	public boolean getAllowDebugPrinting() {
		return allowDebugPrinting;
	}

	public static void goToEnd() {
		textpane.setCaretPosition(textpane.getDocument().getLength());		
	}
}
