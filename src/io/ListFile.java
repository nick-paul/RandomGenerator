package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ui.ConsoleWindow;
import build.RandomWords;
import data.ItemList;
import data.Parser;

/**
 * A Class used for managing comma separated data text files
 * @author Nicholas Paul
 */
public class ListFile {
	private String rawText;
	private String parsedText;
	private ArrayList<ItemList> itemLists;
	File file;

	/** New list file using a file 
	 * @throws FileNotFoundException */
	public ListFile(File file) throws FileNotFoundException {
		String str = new String();
		this.file = file;

		try {
			str = new Scanner( this.file ).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("File '" + file.getPath() + "' cannot be found");
		} catch (NoSuchElementException e2) {
			//File exists, but is empty
			str = "";
		}

		this.rawText = str;
	}

	/** Returns true if the file is empty */
	public boolean isEmpty() {
		return this.rawText.trim().equals("");
	}

	/** Writes a string to the data file */
	public void print(String str) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(this.file);
		this.rawText += str;
		writer.print(this.rawText);
		writer.close();
	}

	/** Returns the data from the file as a list of string arrays */
	public ArrayList<ItemList> getLists() {
		if(this.itemLists == null) {
			System.out.println(this.file.getPath() + " has not been parsed and the getLists function has been called");
			parse();
		}
		return this.itemLists;
	}

	/** Parses the data file */
	public void parse() {
		this.parsedText = Parser.formatEscapes(this.rawText);
		itemLists = new ArrayList<ItemList>();
		String[] lists = this.parsedText.replace("\n\t", "").split(""+RandomWords.LISTNAME);//replace("\\#", ""+POUND_ESCAPE).replace("\\/", ""+COMMENT_ESCAPE).replace("//", ""+COMMENT).split("#");

		for(String list : lists) {
			
			//replace the # escape
			//list = list.replace(POUND_ESCAPE, '#');
			
			//Is the list valid
			if(list.trim().equals("")) {
				continue;
			} 
			
			//Comment
			else if (list.charAt(0) == RandomWords.COMMENT) {
				continue;
			}
			
			
			String[] items = list.split("\n");
			
			items[0] = items[0].trim();
			
			if(!isAlphaNumeric(items[0])) {
				ConsoleWindow.printEx("Invalid list name '" + items[0] + "'; ignoring list.");
			}
			
			//Is it an INCLUDE?
			if (items[0].trim().equals("INCLUDE")) {
				for(int i = 1; i < items.length; i++) {
					if (!items[i].contains(".txt")) {
						//TODO Notify user if not "\r" or "\n"
						continue;
					}
					
					String path = this.file.getParent();
					//If items[0] contains the path name, don't add it again
					//We want to avoid C:\Users\Name\Desktop\C:\Users\Name\Desktop\data.txt
					if(path == null || items[i].contains(path)) {
						path = items[i].trim();
					} else {
						path += "\\" +items[i].trim();
					}
					
					//Importing this file from within this file
					//should be prevented
					if(path.equals(this.file.getPath())) {
						continue;
					}
					
					try {
						ListFile lf = new ListFile(new File(path));
						lf.parse();
						RandomWords.addLists(lf);
					} catch (FileNotFoundException e) {
						ConsoleWindow.printEx("Cannot find file: " + path);
					}
					
				}
				continue;
			}
			
			//Is list in lists already?
			for (int i = 0; i < this.itemLists.size(); i++) {
				if (this.itemLists.get(i).getName().equals(items[0])) {
					//It is already in the list, remove it
					this.itemLists.remove(i);
				}
			}
			
			ItemList itemList = new ItemList(items[0]);
			
			for(int i = 1; i < items.length; i++) {
				if(items[i].equals("\r"))
					continue;
				
				//Comment at start of line
				else if (items[i].charAt(0) == RandomWords.COMMENT)
					continue;
				
				//comment elsewhere in the line
				else if (items[i].contains(""+RandomWords.COMMENT)) {
					//Let it equal the string before the first comment marker
					items[i] = items[i].split(""+RandomWords.COMMENT)[0].trim();
				}
				
				itemList.add(items[i]);
			}
			
			this.itemLists.add(itemList);
		}
	}
	
	
	/** Tests if a string consists of only alphanumeric characters */
	private boolean isAlphaNumeric(String string) {
		for (char c : string.toCharArray()) {
			if ( !(Character.isLetterOrDigit(c) || c == '_') ) {
				return false;
			}
		}
		return true;
	}

	/** returns the rawtest of the file */
	public String toString() {
		return this.rawText;
	}

	/** prints the input string over the current file. Erases all previous file contents */
	public void printOver(String text) throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(this.file);
		writer.print(text);
		this.rawText = text;
		writer.close();
		parse();
	}

	/** returns the rawTest for the file */
	public String getRawText() {
		return rawText;
	}

	/** returns false if the file does not exist */
	public boolean exists() {
		return this.rawText != null;
	}
}
