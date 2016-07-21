package build;

import java.util.HashMap;

import ui.ConsoleWindow;
import data.Item;
import data.Parser;
import data.Range;
import data.Token;
import exceptions.BindNameException;
import exceptions.EmptyListException;
import exceptions.InvalidRangeException;
import exceptions.InvalidUtilVarException;
import exceptions.ListNotFoundException;
import exceptions.NonExistingTagException;

public class UtilVars {
	
	private static HashMap<String, String> bindings = new HashMap<String, String>();	
	private static HashMap<String, Integer> utilIDs;
	
	public static final String[] BASE_VAR_NAMES = {"Abc", "ABC", "abc", "x", "s", "An", "an", "let", "replace"};
	
	//TODO set up modifiers
	public static void init() {
		utilIDs = new HashMap<String, Integer>();
		
		//Valid Util Vars
		utilIDs.put("Abc", 0);
		utilIDs.put("ABC", 1);
		
	}
	
	
	
	/** Evaluates a utility given the utility name 
	 * @throws InvalidUtilVarException 
	 * @throws EmptyListException 
	 * @throws BindNameException 
	 * @throws Exception var name does not exist */
	public static String evalUtil(Token var) throws ListNotFoundException, InvalidUtilVarException, EmptyListException, BindNameException {
		String output = "";
		
		//A simple variable
		if (!var.hasArgs()) { 
			output = evalVar(var.getName());
		}
		
		//Title Case
		else if (var.getName().equals("Abc")) {
			return toTitleCase(Parser.eval(var.getArgs()[0]));
		}
		
		//Lowercase
		else if (var.getName().equals("abc")) {
			return toLowerCase(Parser.eval(var.getArgs()[0]));
		}
		
		//Upper Case
		else if (var.getName().equals("ABC")) {
			return toUpperCase(Parser.eval(var.getArgs()[0]));
		}
		
		//Multiply
		else if (var.getName().equals("x")) {
			return multiply(var.getArgs());
		}
		
		//Plural
		else if (var.getName().equals("s")) {
			if (var.getArgs().length != 2) {
				throw new InvalidUtilVarException("!s must have two arguments. An amount and the name of a list (without brackets)");
			}
			
			String amount = Parser.eval(var.getArgs()[0]);
			if (amount.equalsIgnoreCase("one")
					|| amount.equalsIgnoreCase("a")
					|| amount.equalsIgnoreCase("an")
					|| amount.equalsIgnoreCase("1")) {
				return amount + " " + Parser.eval(RandomWords.OPEN_LIST_NAME + var.getArgs()[1] + RandomWords.CLOSE_LIST_NAME);
			} else {
				return amount + " " + RandomWords.getList(RandomWords.getListID(var.getArgs()[1])).getItem().getPlural();
			}
		}
		
		//An or a?
		//Uppercase
		else if (var.getName().equals("An")) {
			String thing = Parser.eval(var.getArgs()[0]);
			if (shouldUseAn(thing)) {
				return "An " + thing;
			} else {
				return "A " + thing;
			}
		}
		//Lowercase
		else if (var.getName().equals("an")) {
			String thing = Parser.eval(var.getArgs()[0]);
			if (shouldUseAn(thing)) {
				return "an " + thing;
			} else {
				return "a " + thing;
			}
		}
		
		//Assignment
		else if (var.getName().equals("let")) {
			String bindname = var.getArgs()[0].trim();
			
			String bind = "";
			try {
				bind = Parser.eval(var.getArgs()[1].trim());
			} catch (IndexOutOfBoundsException e) {
				throw new InvalidUtilVarException("!let must have two arguments; only recieved one");
			}
			
			//Bind name must be alphanumeric
			if(!RandomWords.isAlphaNumeric(bindname)) {
				throw new InvalidUtilVarException("Binding must be aplhanumeric. Recieved '" + bindname + "'");
			}

			RandomWords.addBinding(bindname, bind);
			
			return bind;
		}
		
		else if (var.getName().equals("replace")) {
			if (var.getArgs().length != 3) {
				throw new InvalidUtilVarException("replace must have 3 arguments");
			}
			
			return var.getArgs()[2].replace(var.getArgs()[0], var.getArgs()[1]);
		}
		
		else {
			
			//Check item bindings
			Item item = RandomWords.getItemBind(var.getName());
			if(item != null) {
				String tagName = var.getArgs()[0];
				try {
					return item.getTagByTagname(tagName);
				} catch (NonExistingTagException e) {
					//Check if there is a default value
					if (var.getArgs().length >= 2) {
						return var.getArgs()[1];
					} else {
						ConsoleWindow.printQuiet("No tag name '" + tagName + "' for item binding '" + var.getName() + "'. You can use a default by adding an additional argument like !list(tag,default)");
						return item.getName();
					}
				} 
			}
				
			else if(var.equals("")) {
					throw new InvalidUtilVarException("Use '!' to denote utilities. If you want to use it normally, escape it with a backslash \"\\!\"" + var);
			}
			else {
				throw new InvalidUtilVarException("Invalid util var: " + var.getName());
			}
		}
		
		return output;
	}

	public static String evalVar(String var) throws InvalidUtilVarException {
		//Check bindings
		String bind = RandomWords.getBind(var);
		if(bind != null) {
			return bind;
		}
		
		else if(RandomWords.getItemBind(var) != null) {
			return RandomWords.getItemBind(var).getName();
		}
		
		else if(var.equals("")) {
			throw new InvalidUtilVarException("Use '!' to denote utilities. If you want to use it normally, escape it with a backslash \"\\!\"" + var);
		}
		throw new InvalidUtilVarException("Invalid util var: " + var);
	}
		
	public static String toTitleCase(String input) {
		//Empty String
		if(input.equals("")) return "";
		
	    StringBuilder titleCase = new StringBuilder();
	    boolean nextTitleCase = true;

	    for (char c : input.toCharArray()) {
	        if (Character.isSpaceChar(c)) {
	            nextTitleCase = true;
	        } else if (nextTitleCase) {
	            c = Character.toTitleCase(c);
	            nextTitleCase = false;
	        }

	        titleCase.append(c);
	    }

	    return titleCase.toString();
	}
	
	public static String toLowerCase(String input) {
	    StringBuilder lower = new StringBuilder();

	    for (char c : input.toCharArray()) {
	    	lower.append(Character.toLowerCase(c));
	    }

	    return lower.toString();
	}
	
	
	private static String toUpperCase(String input) {
	    StringBuilder lower = new StringBuilder();

	    for (char c : input.toCharArray()) {
	    	lower.append(Character.toUpperCase(c));
	    }

	    return lower.toString();
	}
	
	public static String multiply(String[] args) throws InvalidUtilVarException {
		//Check num of args
		if (args.length < 2) {
			throw new InvalidUtilVarException("function x has too few args");
		}
		
		//First arg must always be the range
		Range range;
		try {
			range = new Range(args[0]);
		} catch (InvalidRangeException e) {
			throw new InvalidUtilVarException("Invalid range '" + args[0] +"' for util 'x'");
		}
		
		if(!range.isIntRange()) {
			throw new InvalidUtilVarException("Invalid range '" + args[0] +"' for util 'x'");
		}
		
		int repeats = range.getRandInt();
		
		//Second arg is always the repeated string
		String repeatedString = args[1];
		
		//If there is a 3rd arg, include it as a spacer between all entries
		if (args.length == 3) {
			repeatedString += args[2];
		}
		
		StringBuilder output = new StringBuilder();
		
		//Don't add the last element (repeats-1)
		for (int i = 0; i < repeats-1; i++) {
			output.append(repeatedString);
		}
		
		//Okay, now add the last element
		//this prevents the spacer being included at the end of 
		//the string like do-de-do-da-
		if(repeats != 0) {
			output.append(args[1]);
		}
		
		return output.toString();
	}
	
	/** Returns true if An should be used in front of the word, false if A should
	 * 	All rules mentioned taken from https://owl.english.purdue.edu/owl/resource/591/01/
	 */
	public static boolean shouldUseAn(String inStr) {
		String str = toLowerCase(inStr);
		//A normal non-vowel
		if(!isVowel(str.charAt(0)) && str.charAt(0) != 'h') {
			return false;
		}
		
		/* Use "an" before unsounded "h." Because the "h" hasn't any phonetic representation
		 * and has no audible sound, the sound that follows the article is a vowel;
		 * consequently,"an" is used.
		 */
		else if (str.indexOf("hon") == 0 && isVowel(str.charAt(3))) {
			return true;
		}
		
		//Normal h
		else if(str.charAt(0) == 'h') {
			return false;
		}
		
		/* When "u" makes the same sound as the "y" in "you," or "o" makes the same sound
		 * as "w" in "won," then a is used. The word-initial "y" sound ("unicorn") is actually
		 * a glide [j] phonetically, which has consonantal properties; consequently, it is
		 * treated as a consonant, requiring "a."
		 */
		else if (str.indexOf("uni") == 0 || str.indexOf("one") == 0 || str.indexOf("u.") == 0) {
			return false;
		}
		
		//A normal vowel
		else {
			return true;
		}
	}
	
	
	/** Tests if char c is equal to a,e,i,o,u or y (ignores case) */
	public static boolean isVowel(char c) {
		c = Character.toLowerCase(c);
		return c == 'a' || c == 'e'|| c == 'i'|| c == 'o' || c == 'u' || c == 'y';
	}
}
