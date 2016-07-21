package ui;
import io.ListFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import menuitems.InsertPathNameMenuItem;
import menuitems.OpenMenuItem;
import menuitems.SaveAsMenuItem;
import menuitems.SaveMenuItem;
import build.RandomWords;


@SuppressWarnings("serial")
public class RandomGeneratorGUI extends JFrame
{	
	public RandomGeneratorGUI randomGerneratorInstance = this;
	
	//Layout
	private JPanel all = new JPanel();
	private MyConsole sc = new MyConsole();
	public static ScriptBox scriptBox = new ScriptBox();
	private JMenu menu = new JMenu();
	private JMenuBar menuBar = new JMenuBar();

	//Theme colors
	public static final Color MONOKAI_BG = new Color(39 , 40 , 34);
	public static final Color MONOKAI_BLUE = new Color(102, 217, 239);
	public static final Color MONOKAI_PINK = new Color(249, 38 , 114);
	public static final Color MONOKAI_YELLOW = new Color(230, 219, 116);
	public static final Color MONOKAI_COMMENT = new Color(117, 113, 94);
	public static final Color MONOKAI_WHITE = new Color(248, 248, 242);
	public static final Color MONOKAI_PURPLE = new Color(174, 129, 255);
    
	//Scriptbox
	private static File scriptBoxFile = new File("scriptbox_autosave.txt");
	
	
	public static File getScriptBoxFile() {
		return scriptBoxFile;
	}

	public static void setScriptBoxFile(File scriptBoxFile) {
		RandomGeneratorGUI.scriptBoxFile = scriptBoxFile;
	}

	public RandomGeneratorGUI() {
		super("Random Generator");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				//Keyboard Listener
				KeyboardFocusManager.getCurrentKeyboardFocusManager()
				  .addKeyEventDispatcher(new KeyEventDispatcher() {
				      public boolean dispatchKeyEvent(KeyEvent e) {
				    	 //System.out.println(e.getID() + " - " + e.getKeyCode());
				    	  
				    	  
				    	  //Enter Key = 10
				    	  //Shift = 16
				    	  //alt = 18
				    	  if(e.getID() == 401 && e.getKeyCode() == 10){// && e.isShiftDown()) {
				    		  String input = "INPUT NOT RECIEVED";
				    		  try {
					    		  if(sc.getInputLine().inFocus) {
					    			  input = sc.getInputLine().getText();
					    			  sc.eval();
					    		  }
				    		  } catch (Exception e2) {
				    				System.out.println("EXCEPTION: RandomGeneratorGUI() - Keyboard Listener");
				    				ConsoleWindow.printEx(input);
				    				StringWriter sw = new StringWriter();
				    				PrintWriter pw = new PrintWriter(sw);
				    				e2.printStackTrace(pw);
				    				ConsoleWindow.printEx(sw.toString());
				    		  }
				    	  }
				    	  
				    	  //Up Arrow Key
				    	  if(e.getID() == 401 && e.getKeyCode() == 38 && e.isControlDown()) {
				    		  sc.getInputLine().loadLastText();
				    	  }
				    	  
				    	  //Down Arrow Key
				    	  if(e.getID() == 401 && e.getKeyCode() == 40 && e.isControlDown()) {
				    		  sc.getInputLine().loadPrevText();
				    	  }
				    	  
				    	  //Ctrl - R
				    	  if(e.getID() == 401 && e.getKeyCode() == 82 && e.isControlDown()) {
				    		  if(!scriptBox.getText().equals("")) {
				    			 runScriptPane();
				    		  }
				    	  }
				    	  
				    	  if(sc.getInputLine().getText().equals("\n")) {
				    		  sc.getInputLine().makeEmpty();
				    	  }
				        
				        return false;
				      }
				});
				
				menuBar.setPreferredSize(new Dimension(100, 20));
				
				//File
				menu = new JMenu("File");
		        menu.setMnemonic(KeyEvent.VK_A);
		        menu.getAccessibleContext().setAccessibleDescription("");
		        JMenuItem save = new SaveMenuItem();
		        menu.add(save);
		        JMenuItem saveas = new SaveAsMenuItem();
		        menu.add(saveas);
		        JMenuItem open = new OpenMenuItem();
		        menu.add(open);
		        menuBar.add(menu);
		        
		        //Script
				menu = new JMenu("Script");
		        menu.setMnemonic(KeyEvent.VK_A);
		        menu.getAccessibleContext().setAccessibleDescription("");
		        JMenuItem mi =new JMenuItem(new Action() {
		        	public void actionPerformed(ActionEvent e) {
		        		if(!scriptBox.getText().equals("")) {
		        			runScriptPane();
		        		}
		        	}
					public void addPropertyChangeListener(PropertyChangeListener listener) {}
					public Object getValue(String key) {return null;}
					public boolean isEnabled() {return true;}
					public void putValue(String key, Object value) {}
					public void removePropertyChangeListener(PropertyChangeListener listener) {}
					public void setEnabled(boolean b) {}
				});
		        mi.setText("ctrl+R    Run");
		        menu.add(mi);
		        JMenuItem insert = new InsertPathNameMenuItem();
		        menu.add(insert);
		        menuBar.add(menu);
		        
		        
		        
		        //Console
//				menu = new JMenu("Console");
//		        menu.setMnemonic(KeyEvent.VK_A);
//		        menu.getAccessibleContext().setAccessibleDescription("");
		        
		        //Toggle Debug Printing Item
//		        mi = new JMenuItem(new Action() {
//		        	public void actionPerformed(ActionEvent e) {
//		        		sc.getConsole().allowDebug(!sc.getConsole().getAllowDebugPrinting());
//		        		
//		        	}
//					public void addPropertyChangeListener(PropertyChangeListener listener) {}
//					public Object getValue(String key) {return null;}
//					public boolean isEnabled() {return true;}
//					public void putValue(String key, Object value) {}
//					public void removePropertyChangeListener(PropertyChangeListener listener) {}
//					public void setEnabled(boolean b) {}
//				});
//		        mi.setText("Toggle Debug Printing");
//		        menu.add(mi);
//		        menuBar.add(menu);
		        
//		        //Preferences
//				menu = new JMenu("Preferences");
//		        menu.setMnemonic(KeyEvent.VK_A);
//		        menu.getAccessibleContext().setAccessibleDescription("");
//		        
//		        //Toggle Dark Theme
//		        mi = new JMenuItem(new Action() {
//		        	public void actionPerformed(ActionEvent e) {
//		        		sc.setDarkTheme(!sc.getDarkTheme());
//		        		scriptBox.setDarkTheme(!scriptBox.getDarkTheme());
//		        	}
//					public void addPropertyChangeListener(PropertyChangeListener listener) {}
//					public Object getValue(String key) {return null;}
//					public boolean isEnabled() {return true;}
//					public void putValue(String key, Object value) {}
//					public void removePropertyChangeListener(PropertyChangeListener listener) {}
//					public void setEnabled(boolean b) {}
//				});
//		        mi.setText("Toggle Dark Theme");
//		        menu.add(mi);
//		        menuBar.add(menu);
				
				all.setLayout(new BorderLayout(0,1));
				scriptBox.setPreferredSize(new Dimension(350,320));
				scriptBox.setMaximumSize(new Dimension(1000,1000));
				
				all.add(scriptBox, BorderLayout.CENTER);
				all.add(sc, BorderLayout.SOUTH);
				
				all.setPreferredSize(new Dimension(500, 500));
				
				setJMenuBar(menuBar);
				add(all);
				
				setLocationRelativeTo(null);
				pack();
				setVisible(true);
				
				sc.getInputLine().grabFocus();
				
				
				
//				inputBar.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
//                        java.awt.event.InputEvent.CTRL_DOWN_MASK),
//                "actionMapKey");
//				inputBar.getActionMap().put("actionMapKey",
//                 eval());
			}
		});
	}
	
//	private void eval() {
//		if(!inputBar.getText().equals("")) {
//			try{
//				Parser.evaluate(inputBar.getText());
//			} catch(Exception ex) {
//				ex.printStackTrace();
//			}
//			inputBar.clear();
//			inputBar.grabFocus();
//		}
//	}
	
	public static void main(String[] args) {	
		//Open the application
		new RandomGeneratorGUI();

		//Catch all exceptions thrown
		try {
		//Load the script autosave
			try {
				//This will be called if and only if the file does not already exist
				if(!scriptBoxFile.exists()) {
					scriptBoxFile.createNewFile();
				}
			} catch (IOException e) {
				System.out.println("CANNOT LOAD SCRIPTBOX AUTOSAVE");
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				ConsoleWindow.printEx(sw.toString());
			}
			
			ListFile file2 = new ListFile(scriptBoxFile);
			file2.parse();
			RandomWords.addLists(file2);
			scriptBox.setText(file2.getRawText());
		} catch (Exception e) {
			System.out.println("EXCEPTION: RandomGeneratorGUI.main()");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			ConsoleWindow.printEx(sw.toString());
		}
	}
	
	public static void runScriptPane() {
		RandomWords.clearAll();
		try {
			try {
				ListFile file = new ListFile(scriptBoxFile);
				file.printOver(scriptBox.getText());
				RandomWords.addLists(file);
			} catch (FileNotFoundException e) {
				System.out.println("EXCEPTION: Cannot find " + scriptBoxFile.getPath());
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				ConsoleWindow.printEx(sw.toString());
			}
			ConsoleWindow.printQuiet("Script Updated");
		} catch (Exception e) {
			System.out.println("EXCEPTION: RandomGUI.runScriptPane()");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			ConsoleWindow.printEx(sw.toString());
		}
	}
	
	public static void save() {
		ConsoleWindow.printQuiet("Script saved to " + scriptBoxFile.getPath());
		runScriptPane();
	}
	
	public static void saveAs(File file) {
		//Verify File Extension
		if(!file.getPath().contains(".txt")) {
			file = new File(file.getPath()+".txt");
		}
		
		try {
			PrintWriter writer = new PrintWriter(file);
			writer.print(scriptBox.getText());
			writer.close();
		} catch (FileNotFoundException e) {
			ConsoleWindow.printEx("Cannot find file: " + file.getPath());
		}
		
		scriptBoxFile = file;
		runScriptPane();
	}

	public static void open(File file) {
		//New File
		scriptBoxFile = file;
		
		//Clear all lists
		RandomWords.clearAll();
		
		//Update the script box
		try {
			try {
				ListFile listFile = new ListFile(scriptBoxFile);
				scriptBox.setText(listFile.getRawText());
				listFile.parse();
				RandomWords.addLists(listFile);
			} catch (FileNotFoundException e) {
				System.out.println("EXCEPTION: Cannot find " + scriptBoxFile.getPath());
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				ConsoleWindow.printEx(sw.toString());
			}
			ConsoleWindow.printQuiet("Script Updated");
		} catch (Exception e) {
			System.out.println("EXCEPTION: RandomGUI.runScriptPane()");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			ConsoleWindow.printEx(sw.toString());
		}
	}

	public static void insertAtCaret(String str) {
		scriptBox.insertAtCaret(str);
	}

}


