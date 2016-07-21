package build;

import io.ListFile;

import java.util.ArrayList;
import java.util.HashMap;

import data.Item;
import data.ItemList;
import exceptions.BindNameException;


public class RandomWords {
	
	public static ArrayList<ItemList> lists = new ArrayList<ItemList>();
	public static HashMap<String, String> bindings = new HashMap<String, String>();
	public static HashMap<String, Item> itemBindings = new HashMap<String, Item>();

	public static final char MAX = 65535;
	
	public static final char CLOSE_LIST_NAME = MAX-2;
	public static final char SEPERATOR = MAX-3;
	public static final char ESCAPE = MAX-4;
	public static final char FUNCTION = MAX-5;
	public static final char ENDL = MAX-6;
	public static final char TAB = MAX-7;
	public static final char COMMA_SEPERATOR = MAX-8;
	public static final char OPEN_UTIL_ARGS = MAX-9;
	public static final char CLOSE_UTIL_ARGS = MAX-10;
	public static final char LISTNAME = MAX-11;
	public static final char COMMENT = MAX-12;
	public static final char OPEN_TAG = MAX-13;
	public static final char CLOSE_TAG = MAX-14;
	public static final char TAGASSIGN = MAX-15;
	public static final char SPACE = MAX-16;
	public static final char OPEN_LIST_NAME = MAX-17;

	
//	public static void main(String[] args) {
//		System.out.println(dropFromBack("hello", -1));
//		System.out.println(dropFromBack("hello", 0));
//		System.out.println(dropFromBack("hello", 3));
//		System.out.println(dropFromBack("hello", 10));
//
//
//	}
	
	public static int getListID(String name) {
		for (int i = 0; i < lists.size(); i++) {
			if (name.equals(lists.get(i).getName())) {
				return i;
			}
		}
		return -1;
	}
	
	public static void clearAll() {
		lists.clear();
		clearAllBindings();
	}
	
	public static void clearAllBindings() {
		bindings.clear();
		itemBindings.clear();
	}
	
	
	/** Splits a string at the first occurrence of another string. If the splitter
	 * is not found, the first element will be str and the secong will be an empty string.
	 * @param str
	 * @param splitter
	 * @return
	 */
	public static String[] splitAtfirstOccuranceOf(String str, String splitter) {
		String[] out = {"",""};
		int splitIndex = str.indexOf(splitter);
		if (splitIndex == -1) {
			out[0] = str;
			out[1] = "";
			return out;
		}
		if (splitter.length() > 1) {
			out[0] = str.substring(0, splitIndex).trim();
			out[1] = str.substring(splitIndex+splitter.length()-1, str.length()).trim();
		} else {
			out[0] = str.substring(0, splitIndex).trim();
			out[1] = str.substring(splitIndex+1, str.length()).trim();
		}
		return out;
	}
	
	/** Drops a given number of characters from the back of the string <br />
	 * <br />
	 * if n is less than or equal to 0, return the whole string<br />
	 * if n is greater than str.len, return empty string */
	public static String dropFromBackStr(String str, int n) {
		//Drop the whole string
		if (n >= str.length()) {
			return "";
		}
		//Drop nothing
		else if (n <= 0) {
			return str;
		}
		return str.substring(0, str.length()-n);
	}
	
	/** Drops a given number of characters from the front of the string <br />
	 * <br />
	 * if n is less than or equal to 0, return the whole string<br />
	 * if n is greater than str.len, return empty string */
	public static String dropFromFrontStr(String str, int n) {
		//Drop the whole string
		if (n >= str.length()) {
			return "";
		}
		//Drop nothing
		else if (n <= 0) {
			return str;
		}
		return str.substring(n, str.length());
	}
	
	public static void addLists(ListFile file) {
		ArrayList<ItemList> fileLists = file.getLists();
		for (ItemList list : fileLists) {
			//Is list in lists already
			for (int i = 0; i < lists.size(); i++) {
				if (lists.get(i).getName().equals(list.getName())) {
					//Overwrite the list
					lists.remove(i);
					lists.add(list);
					break;
				}
			}
			lists.add(list);
		}
	}
	
	public static ItemList getList(int id) {
		return lists.get(id);
	}
	
	/** Tests if a string consists of only alphanumeric characters */
	public static boolean isAlphaNumeric(String string) {
		for (char c : string.toCharArray()) {
			if ( !(Character.isLetterOrDigit(c) || c == '_') ) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isAlphaNumeric(char c) {
		return (Character.isLetterOrDigit(c) || c == '_');
	}
	
	public static boolean isBaseVarName(String str) {
		for (String s : UtilVars.BASE_VAR_NAMES) {
			if (s.equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static void addBinding(String key, String value) throws BindNameException {
		if (isBaseVarName(key)) {
			throw new BindNameException("Cannot bind '" + value + "' to '" + key + "'. '" + key + "' is the name of an existing base variable.");
		}
		bindings.put(key, value);
	}

	public static void addItemBinding(String key, Item value) throws BindNameException {
		if (isBaseVarName(key)) {
			throw new BindNameException("Cannot bind '" + value + "' to '" + key + "'. '" + key + "' is the name of an existing base variable.");
		}
		itemBindings.put(key, value);
		
	}

	public static String getBind(String bindName) {
		return bindings.get(bindName);
	}
	
	public static Item getItemBind(String bindName) {
		return itemBindings.get(bindName);
	}

	/** Drops a given number of elements from the front of the array <br />
	 * <br />
	 * if n is less than or equal to 0, return the whole array<br />
	 * if n is greater than arr.len, return empty array */
	public static String[] dropFromFront(String[] arr, int n) {
		//Drop the whole string
		if (n >= arr.length) {
			return new String[0];
		}
		//Drop nothing
		else if (n <= 0) {
			return arr;
		}
				
		//Create array with proper length
		String[] out = new String[arr.length-n];
		
		//Move data into output array
		for(int i = n; i < arr.length; i ++) {
			out[i-n] = arr[i];
		}
		return out;
	}



	
	
}
