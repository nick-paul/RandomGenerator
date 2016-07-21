package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class InputLine extends JTextArea{
	
	Vector<String> lastText = new Vector<String>();	
	int lastTextID = 0;
	Boolean hasUsedLastText = false;
	public boolean inFocus = false;
	
	public InputLine() {
		
        this.setBackground(Color.DARK_GRAY);
        this.setForeground(Color.WHITE);
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

		this.addFocusListener(new FocusListener() {
	        public void focusGained(FocusEvent e) {
	            inFocus = true;
	        }
	
	        public void focusLost(FocusEvent e) {
	            inFocus = false;
	        }
    });
	}
	
	public void loadLastText() {
		//ConsoleWindow.printDebug("id:" + lastTextID + "lastTextArr: " + lastText);
		
		hasUsedLastText = true;
		if(lastText.size()==0 || lastTextID<0)
			return;
		else{
			setText(lastText.elementAt(lastTextID));
			lastTextID--;
		}
	}
	
	public void loadPrevText() {
		//ConsoleWindow.printDebug("id:" + lastTextID + "lastTextArr: " + lastText);

		if(hasUsedLastText)
		{
			lastTextID++;
			hasUsedLastText = false;
		}		
		if(lastTextID+1>lastText.size()-1 || lastText.size()==0)
			setText("");
		else{
			lastTextID++;
			setText(lastText.elementAt(lastTextID));
		}
	}

	public void clear() {
		lastText.add(getText());
		lastTextID = lastText.size()-1;
		setText("");
	}
	
	public void makeEmpty() {
		setText("");
	}

}
