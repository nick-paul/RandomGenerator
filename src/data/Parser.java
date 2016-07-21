package data;

import java.util.ArrayList;
import java.util.Random;

import ui.ConsoleWindow;
import build.RandomWords;
import build.UtilVars;
import exceptions.BindNameException;
import exceptions.EmptyListException;
import exceptions.InvalidRangeException;
import exceptions.InvalidUtilVarException;
import exceptions.ListNotFoundException;
import exceptions.NonExistingTagException;

public class Parser {	
	
	public static String parse(Token[] tokens) {		
		StringBuilder output = new StringBuilder();
		for (Token t : tokens) {
			
			//Normal String
			if (t.getType() == Token.STRING) {
				output.append(t.getName());
			}
			
			//List Name
			else if(t.getType() == Token.LISTNAME) {				
				if(t.hasTag()) {
					try {
						Item item = RandomWords.getList(t.getListID()).getItem();
						String str;
						try {
							str = Parser.eval(item.getTagByTagname(t.getTagName()));
						} catch (NonExistingTagException e) {
							//Get default value
							str = Parser.eval(t.getTagDefault());
						}
						if(t.shouldCapitalize()) {
							output.append(UtilVars.toTitleCase(str));
						} else {
							output.append(str);
						}
					} catch (ListNotFoundException e) {
						ConsoleWindow.printEx(e.getMessage());
					} catch (EmptyListException e) {
						ConsoleWindow.printEx(e.getMessage());
					}
				}
				else {
					String str = "";
					Item item;
					
					try {
						item = RandomWords.getList(t.getListID()).getItem();
						RandomWords.addItemBinding(t.getBindingName(), item);
						
						str = parse(tokenize(item.getName()));
						
						if(t.shouldCapitalize()) {
							output.append(UtilVars.toTitleCase(str));
						} else {
							output.append(str);
						}
					} catch (EmptyListException e) {
						ConsoleWindow.printEx(e.getMessage());
					} catch (BindNameException e) {
						ConsoleWindow.printEx(e.getMessage());
					}
					
					
				}
				
					
			}
			
			//Anon List
			else if(t.getType() == Token.ANONLIST) {
				output.append(parse(tokenize(t.getAnonItem())));
			}
			
			//Util Var
			else if(t.getType() == Token.UTILVAR) {
				try {
					output.append(parse(tokenize(UtilVars.evalUtil(t))));
				} catch (ListNotFoundException e) {
					ConsoleWindow.printEx(e.getMessage());
				} catch (InvalidUtilVarException e) {
					ConsoleWindow.printEx(e.getMessage());
				} catch (EmptyListException e) {
					ConsoleWindow.printEx(e.getMessage());
				} catch (BindNameException e) {
					ConsoleWindow.printEx(e.getMessage());
				}
			}
			
			else if(t.getType() == Token.RANGE) {
				output.append(t.getRange().getRandom());
			}
			
			//Type not set up
			else {
				output.append("(SET UP TYPE IN Parser.parse)");
			}
			
		}
		
		return output.toString();
	}
	
	public static Token[] tokenize(String string) {
		//string = replaceEscapes(string);
		
		char[] chars = string.toCharArray();
		ArrayList<Token> tokens = new ArrayList<Token>();
		int charIndex = 0; 
		
		while(charIndex < chars.length) {
			
			
			//List or List Name
			if (chars[charIndex] == RandomWords.OPEN_LIST_NAME) {
				boolean anonList = false, isRange = false, requiresTag = false, hasBinding = false, hasMods = false;
				String listname = "";
				int brackets = 0;
				charIndex++; //Skip the opening bracket
				while (charIndex < chars.length && !(chars[charIndex] == RandomWords.CLOSE_LIST_NAME && brackets == 0)) {
					//Anon
					if (chars[charIndex] == RandomWords.SEPERATOR && brackets == 0) {
						anonList = true;
					} 
					//Range
					else if (chars[charIndex] == '-' && brackets == 0) {
						isRange = true;
					} 
					//Tag
					else if (chars[charIndex] == '.' && brackets == 0) {
						requiresTag = true;
					}
					//Binding
					else if (chars[charIndex] == '=' && brackets == 0) {
						hasBinding = true;
					}
					//Modifiers
					else if(chars[charIndex] == RandomWords.COMMA_SEPERATOR && brackets == 0) {
						hasMods = true;
					}
					//Entering a nested bracket
					else if (chars[charIndex] == RandomWords.OPEN_LIST_NAME) {
						brackets++;
					}
					else if (chars[charIndex] == RandomWords.CLOSE_LIST_NAME) {
						brackets--;
					}
					
					listname += chars[charIndex];
					charIndex++;
				}
				
				Token token = null;
							
				//Are we at the end of the line?
				//If so there was no closing bracket, return the listname as a string
				if (charIndex == chars.length && chars[chars.length-1] != RandomWords.CLOSE_LIST_NAME) {
					token = new Token(listname, Token.STRING);
				}
				
				//Make sure it isn't empty brackets
				else if (listname.length() == 1) {
					token = new Token("[]", Token.STRING);
				}

				
				//It was not an empty bracket
				else  {
					
					//Cannot use bindings with anonymous lists or ranges
					if( (hasBinding && anonList) || (hasBinding && isRange) || (hasBinding && requiresTag) ) {
						ConsoleWindow.printQuiet("Cannot use bindings with anonymous lists, ranges, or tags: '" + listname + "'");
						token = new Token('[' + listname + ']', Token.STRING);
					}
					
					else if (hasMods) {
						String[] mods = splitStringBy(listname, RandomWords.COMMA_SEPERATOR, '[', ']');
						listname = mods[0];
						mods = RandomWords.dropFromFront(mods, 1);

						//Trim all mods
						for(int i = 0; i < mods.length; i++) {
							mods[i] = mods[i].trim();
						}
					}

					//Range
					else if (isRange && !anonList) {
						try {
							Range rng = new Range(listname);
							token = new Token("range", Token.RANGE);
							token.setRange(rng);
						} catch (InvalidRangeException e) {
							ConsoleWindow.printQuiet(e.getMessage());
							token = new Token('[' + listname + ']', Token.STRING);
						}
					
					}

					
					//Anonymous List
					else if(anonList) {
						token = new Token("ANON", Token.ANONLIST);
						token.setList(splitStringBy(listname, RandomWords.SEPERATOR, RandomWords.OPEN_LIST_NAME, RandomWords.CLOSE_LIST_NAME));
					} 
					
					//List reference	
					else {
						
						//Set up binding
						String bindName = null;
						if(hasBinding) {
							//remove the opening bracket after getting the bind name
							bindName = RandomWords.splitAtfirstOccuranceOf(listname, " = ")[0];
							listname = RandomWords.splitAtfirstOccuranceOf(listname, " = ")[1];
						}
						
						//Verify binding name (if it exists)
						if (bindName != null && !RandomWords.isAlphaNumeric(bindName)) {
							//remove bind
							hasBinding = false;
							ConsoleWindow.printQuiet("Binding '" + Parser.removeAllSpecialChars(bindName) + "' can only contain alphanumeric characters");
							bindName = null;
						}
						
						//Split the tag name and the list name
						String tagName = "";
						if(requiresTag) {
							String[] splitter = listname.split("\\.");
							listname = splitter[0];
							tagName = splitter[1];
						}
						
						token = new Token(UtilVars.toLowerCase(listname), Token.LISTNAME);
						
						//If bindName == null, the token will determine that there is no binding
						token.setBinding(bindName);
						
						//If the first letter of the list name is capital, title case the input
						token.setTitleCaseFlag(Character.isUpperCase(listname.charAt(0)));
						
						//Lowercase the listname
						listname = UtilVars.toLowerCase(listname);
						
						//Check list validity
						int listID = RandomWords.getListID(listname);
						
						//Is it a valid list?
						if(listID == -1) {							
							//If it is not a valid list, return it as a normal string
							token = new Token('[' + listname + ']', Token.STRING);
							

						} else {
							token.setListID(listID);
							
							//Add the tag
							if(requiresTag) {
								token.setTag(tagName);
							}
						}
					}
				}
				
				tokens.add(token);
				charIndex++; //Skip the closing bracket
			}
			
			//Util Var
			else if(chars[charIndex] == RandomWords.FUNCTION) {
				String str = "";
				charIndex++;
				int brackets = 0;
				boolean includesArgs = false;
				
				while (charIndex < chars.length) {
					//Entering a nested bracket
					if (chars[charIndex] == '(') {
						includesArgs = true;
						brackets++;
					} else if (chars[charIndex] == ')') {
						//found match
						if(brackets == 1)
							break;
						brackets--;
					} else if (!(Character.isLetterOrDigit(chars[charIndex]) || chars[charIndex] == '_') && brackets == 0) {
						break;
					}
					str += chars[charIndex];
					charIndex++;
				}
				
				Token t;
				if(includesArgs) {
					t = new Token(str.substring(0, str.indexOf('(')), Token.UTILVAR);
					str = str.substring(str.indexOf('(')+1, str.length());
					t.setUtilArguments(splitStringBy(str, RandomWords.COMMA_SEPERATOR, '(', ')'));
					charIndex++; //Skip the closing parenthesis
				} else {
					t = new Token(str, Token.UTILVAR);
				}
				tokens.add(t);
			}
						
			//Normal String
			else {
				String str = "";
				while (charIndex < chars.length && !isSpecial(chars[charIndex])) {
					str += chars[charIndex];
					charIndex++;
				}
				tokens.add(new Token(str, Token.STRING));
			}
		}
		
		return tokens.toArray(new Token[tokens.size()]);
	}
	
	public static boolean isSpecial(char c) {
		char[] specials = {RandomWords.OPEN_LIST_NAME, RandomWords.FUNCTION};
		for (char s : specials) {
			if (s == c) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isAlpha(char c) {
		if (Character.isAlphabetic(c) || c=='.')
			return true;
		return false;
	}
	
	/**
	 * Splits a string into an array of strings using the delimiter. Ignores nested
	 * delimiters contained within open and close. Assumed to NOT include open and
	 * close brackets at the beginning and end of the string. open and close
	 * will be used as the open and close for nesting
	 * 
	 * @param str The string to be split
	 * @param delim The delimiter
	 * @param open
	 * @param close
	 * @return An array of strings split using the delimiter
	 */
	public static String[] splitStringBy(String str, char delim, char open, char close) {
		ArrayList<String> arr = new ArrayList<String>();
		char[] chars = str.toCharArray();
		int charIndex = 0;
		int brackets = 0;
		StringBuilder currentSplit = new StringBuilder();
		
		//-1 to ignore close bracket
		while (charIndex < chars.length) {
			
			if(chars[charIndex] == open) {
				brackets++;
				currentSplit.append(chars[charIndex]);
			} else if (chars[charIndex] == close) {
				brackets--;
				currentSplit.append(chars[charIndex]);
			} else if (chars[charIndex] == delim && brackets == 0) {
				arr.add(currentSplit.toString());
				currentSplit = new StringBuilder();
			} else {
				currentSplit.append(chars[charIndex]);
			}
			
			charIndex++;
			
		}
		
		arr.add(currentSplit.toString());
		
		return arr.toArray(new String[arr.size()]);
	}

	public static String formatEscapes(String str) {
		return str
				//Escape Escape
					.replace("\\\\", ""+RandomWords.ESCAPE)
				//List names
					.replaceAll("(?<!\\\\)#", ""+RandomWords.LISTNAME)
				  	.replace("\\#", "#")
				//Comments
				  	.replaceAll("(?<!\\\\)//", ""+RandomWords.COMMENT)
				  	.replace("\\//", "//")
				//Item tags
				  	.replaceAll("(?<!\\\\)\\{", ""+RandomWords.OPEN_TAG)
					.replace("\\{", "{")
					.replaceAll("(?<!\\\\)\\}", ""+RandomWords.CLOSE_TAG)
					.replace("\\}", "}")
				//Functions
				  	.replaceAll("(?<!\\\\)!", ""+RandomWords.FUNCTION)
				  	.replace("\\!", "!")
				//Opening and closing list identifiers
					.replaceAll("(?<!\\\\)\\[", ""+RandomWords.OPEN_LIST_NAME)
					.replace("\\[", "[")
					.replaceAll("(?<!\\\\)\\]", ""+RandomWords.CLOSE_LIST_NAME)
					.replace("\\]", "]")
				//Separators
					.replaceAll("(?<!\\\\)\\|", ""+RandomWords.SEPERATOR)
					.replace("\\|", "|")
					.replaceAll("(?<!\\\\),", ""+RandomWords.COMMA_SEPERATOR)
					.replace("\\,", ",")
				//Formatting
					.replace("\\n", ""+RandomWords.ENDL)
					.replace("\\t", ""+RandomWords.TAB)
					.replace("\\_", ""+RandomWords.SPACE);
		
	}
	
	public static String removeExtraEscapes(String str) {
		return str.replace(RandomWords.SEPERATOR, '|')
				  .replace(RandomWords.COMMA_SEPERATOR, ',')
				  .replace(RandomWords.ENDL, '\n')
				  .replace(RandomWords.TAB, '\n')
				  .replace(RandomWords.SPACE, ' ')
				  .replace(""+RandomWords.ESCAPE, "\\\\");
	}
	
	public static String eval(String string) throws ListNotFoundException {
		return parse(tokenize(string));
	}
	
	public static char convertChar(char c) {
		switch (c) {
		case RandomWords.CLOSE_TAG:
			return '}';
		case RandomWords.COMMA_SEPERATOR:
			return ',';
		case RandomWords.ENDL:
			return '\n';
		case RandomWords.CLOSE_LIST_NAME:
			return ']';
		case RandomWords.ESCAPE:
			return '\\';
		case RandomWords.FUNCTION:
			return '!';
		case RandomWords.LISTNAME:
			return '#';
		case RandomWords.OPEN_TAG:
			return '{';
		case RandomWords.SEPERATOR:
			return '|';
		case RandomWords.OPEN_LIST_NAME:
			return '[';
		case RandomWords.TAB:
			return '\t';
		case RandomWords.TAGASSIGN:
			return ':';
		default:
			return c;
		}
	}

	public static String removeAllSpecialChars(String name) {
		char[] chars = name.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			chars[i] = convertChar(chars[i]);
		}
		return new String(chars);
	}
}

