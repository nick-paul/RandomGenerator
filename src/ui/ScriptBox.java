package ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class ScriptBox extends JScrollPane {

	static JTextPane textpane = new JTextPane();
	static StyledDocument doc = textpane.getStyledDocument();

	private static boolean darkTheme = true;
	
	StyleContext context = new StyleContext();
	Style blueStyle = context.addStyle("blue", null);
	
	//Colors
	static MutableAttributeSet black = textpane.addStyle("black", null);

	
	public ScriptBox() {
		super(textpane);
		textpane.setEditable(true);
		doc = textpane.getStyledDocument();
		textpane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		
        setDarkTheme(true);
		
		//Set up styles
		StyleConstants.setForeground(blueStyle, Color.BLUE);
		
		setBorder(BorderFactory.createEmptyBorder());
	}
	
	public void setDarkTheme(boolean b) {
		darkTheme = b;
		if(b) {
	        //StyleConstants.setForeground(black, Color.WHITE);
	        textpane.setForeground(new Color(248, 248, 242));
			textpane.setBackground(new Color(39 , 40 , 34));
			textpane.setCaretColor(new Color(248, 248, 242)); 
		} else {
	        //StyleConstants.setForeground(black, Color.BLACK);
	        textpane.setForeground(Color.BLACK);
			textpane.setBackground(Color.WHITE);
			textpane.setCaretColor(Color.BLACK); 
		}
	}
	
	public boolean getDarkTheme() {
		return darkTheme;
	}
	
	public String getText() {
		return textpane.getText();
	}

	public void setText(String rawText) {
		textpane.setText(rawText);
		
	}
	
	public void insertAtCaret(String str) {
		try {
			textpane.getDocument().insertString(textpane.getCaretPosition(), str, null);
		} catch (BadLocationException e) {
			ConsoleWindow.printEx("Cannot insert there");
		}
	}
	
	//This may help
	//http://stackoverflow.com/questions/14400946/how-to-change-the-color-of-specific-words-in-a-jtextpane
	
//	public void recolor() {
//		try {
//			doc.insertString(0, "", blueStyle);
//		} catch (BadLocationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
