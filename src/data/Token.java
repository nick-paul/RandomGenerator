package data;

import build.RandomWords;
import ui.ConsoleWindow;
import exceptions.EmptyListException;

public class Token {
	public static final byte STRING = 0;
	public static final byte LISTNAME = 1;
	public static final byte ANONLIST = 2;
	public static final byte UTILVAR = 3;
	public static final byte RANGE = 4;
	public static final byte LIST_WITH_TAG = 5;
	
	
	private String name;
	private int listID = -1;
	private byte type = STRING;
	private ItemList anonList;
	private boolean titleCaseFlag;
	private String[] utilArguments;
	private Range range;
	private boolean hasTag;
	private String tagName;
	private String tagDefault;
	private String bindName;
	private boolean hasBinding;
	
		
	public Token(String name, byte type) {
		if (type == STRING) {
			//Remove any special characters
			name = Parser.removeAllSpecialChars(name);
		}
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public byte getType() {
		return this.type;
	}
	
	public void setListID(int id) {
		this.listID = id;
	}
	
	public int getListID() {
		return this.listID;
	}
	
	public void setList(String[] list) {
		anonList = new ItemList("ANON");
		for (String s : list) {
			this.anonList.add(s);
		}
	}
	
	public void setUtilArguments(String[] args) {
		this.utilArguments = args;
	}
	
	public String getAnonItem() {
		try {
			return this.anonList.getItemStr();
		} catch (EmptyListException e) {
			// An anon list will only be empty if all its items were invalid
			ConsoleWindow.printQuiet("All items of anon list invalid. Please fix list.");
			return "";
		}
	}
	
	public String toString() {
		String str = "\"" + this.name + "\": {";
		switch (this.type) {
		case STRING:
			str += "string";
			break;
		case LISTNAME:
			str += "listname";
			break;
		case ANONLIST:
			str += "anonlist";
			break;
		case UTILVAR:
			str += "variable";
			break;
		default:
			str += "NO TOKEN TYPE";
			break;
		}
		return str + "}";
	}

	public void setTitleCaseFlag(boolean flag) {
		this.titleCaseFlag = flag;		
	}
	
	public boolean shouldCapitalize() {
		return this.titleCaseFlag;
	}

	/** Returns true if the token has arguments stored in utilArguments */
	public boolean hasArgs() {
		return this.utilArguments != null;
	}
	
	/** Returns the arguments stored in utilArguments */
	public String[] getArgs() {
		return this.utilArguments;
	}

	public void setRange(Range rng) {
		this.range = rng;
	}
	
	public Range getRange() {
		return this.range;
	}

	public void setTag(String tag) {
		String[] arr = RandomWords.splitAtfirstOccuranceOf(tag, " or ");
		this.tagName = arr[0];
		this.tagDefault = arr[1];
		this.hasTag = true;
	}
	
	public boolean hasTag() {
		return this.hasTag;
	}

	public String getTagName() {
		return this.tagName;
	}
	
	public String getTagDefault() {
		return this.tagDefault;
	}

	/** Sets the bind name for the list item. If bindName == null,
	 * it will set hasBinding to false, otherwise it will set it to true */
	public void setBinding(String bindName) {
		this.bindName = bindName;
		if(bindName == null)
			this.hasBinding = true;
		else
			this.hasBinding = false;
	}
	
	/** returns true if the token is a list type with binding */
	public boolean hasBinding() {
		return this.hasBinding;
	}
	
	/** returns the binding name */
	public String getBindingName() {
		return this.bindName;
	}
}
