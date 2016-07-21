package data;

import java.util.HashMap;
import java.util.Random;

import build.RandomWords;
import exceptions.InvalidTagException;
import exceptions.NonExistingTagException;

public class Item {
	private String name;
	private int percent = 100;
	private HashMap<String, String> tags = new HashMap<String, String>();
	
	/** Parses a string including all tags
	 * ex: quick fox (15%, s:quick foxes)
	 */
	Item(String str) throws InvalidTagException {
		//Is there a possibility it includes tags?
		if (str.length() > 2 && Item.lastChar(str.trim()) == RandomWords.CLOSE_TAG) {
			
			//Attempt to separate the item and its tags
			char[] chars = str.toCharArray();
			int brackets = 0, tagIndex = -1;
			for(int i = 0; i < chars.length; i++) {
				if(chars[i] == RandomWords.OPEN_LIST_NAME) {
					brackets++;
				} else if (chars[i] == RandomWords.CLOSE_LIST_NAME) {
					brackets--;
				} else if (chars[i] == RandomWords.OPEN_TAG && brackets == 0) {
					tagIndex = i;
					break;
				}
			}

			
			//Does not include tags
			if (tagIndex < 0) {
				this.name = str;
			}
			
			else {

				this.name = str.substring(0,tagIndex).trim();
				String tagStr = str.substring(tagIndex+1, str.length()-1); //Remove open and close tags
				
				//Empty tag list
				if(tagStr.equals("")) 
					throw new InvalidTagException("Empty tag list at '" + this.name + "'");
				
				//Tag operator escape
				tagStr = tagStr.replaceAll("(?<!\\\\):", ""+RandomWords.TAGASSIGN).replace("\\:", ":");
				String[] tags = tagStr.split(""+RandomWords.COMMA_SEPERATOR);
				
				//Parse the tags
				for (String tag : tags) {
					tag = tag.trim();
					String[] nameAndValue = Parser.splitStringBy(tag, RandomWords.TAGASSIGN, RandomWords.OPEN_TAG, RandomWords.CLOSE_TAG);//tag.split(""+RandomWords.TAGASSIGN); // Use the splitter I wrote for the parser here
					
					if(nameAndValue.length < 2) {
						throw new InvalidTagException("Invalid Tag '" + nameAndValue[0] + "' on item '" + this.name + "' please include name and disc");
					}
					
					//Trim the tags
					String tagname = nameAndValue[0].trim();
					String tagvalue = nameAndValue[1].trim();
					
					//Special Tags
					//Percentage
					if (tagname.equals("%")) {
						try {
							this.percent = Integer.parseInt(tagvalue);
						} catch (NumberFormatException e) {
							//Ignore tag
							e.printStackTrace();
							continue;
						}
					} 
					
					//Is the name completely alphanumeric?
					else if (RandomWords.isAlphaNumeric(tagname)) {
						this.tags.put(tagname, tagvalue);
					}
					
					//Invalid tag
					else {
						throw new InvalidTagException("Tag '" + this.name + "." + tagname + "' can not contain special characters. Only use 0-9, a-z, A-Z, or _");
					}
				}
			}
		} 
		
		//No tags
		else {
			this.name = str;
		}
	}
	
	private static char lastChar(String str) {
		return str.charAt(str.length()-1);
	}

	public String getName() {
		return this.name;
	}
	
	public String getTagByTagname(String tagname) throws NonExistingTagException {
		String out = tags.get(tagname);
		if (out == null) {
			throw new NonExistingTagException("Tag '" + tagname + "' does not exist for the item '" + this.name + "'");
		}
		return out;
	}
	
	public String getPlural() {
		String plural = this.tags.get("s");
		//Does it have a specified plural?
		if (plural == null) {
			plural = this.name+"s";
		} 
		return plural;
	}
	
	/** Returns true if the percentage is less than 100 (100 is default) */
	public boolean hasPercentTag() {
		return this.percent < 100;
	}
	
	/** There is a this.percent% chance that this will return true */
	public boolean shouldAdd() {
		Random rand = new Random();
		return rand.nextInt(100)+1 <= this.percent;		
	}
	
	public String toString() {
		return name;
	}
}
