package data;

import java.util.Random;

import exceptions.InvalidRangeException;

public class Range {
	private int lower;
	private int upper;
	private boolean isIntRange = true;
	
	/** String should be in the format "char-char" or "int-int" 
	 * @throws InvalidRangeException */
	public Range(String str) throws InvalidRangeException {
		String[] bounds = str.split("-");
		if (bounds.length == 2) {
			//Is it an alphabetic bound?
			if(bounds[0].length() == 1
					&& bounds[1].length() == 1
					&& Character.isAlphabetic(bounds[0].charAt(0))
					&& Character.isAlphabetic(bounds[0].charAt(0))) {
				this.isIntRange = false;
				this.lower = Character.toLowerCase(bounds[0].charAt(0));
				this.upper = Character.toLowerCase(bounds[1].charAt(0));
			} else {
				try {
					this.lower = Integer.parseInt(bounds[0]);
					this.upper = Integer.parseInt(bounds[1]);
				} catch (NumberFormatException e) {
					throw new InvalidRangeException("Invalid range: '" + str + "'");
				}
			}
		} else if (bounds.length == 1) {
			//Is it an alphabetic bound?
			if(bounds[0].length() == 1
					&& Character.isAlphabetic(bounds[0].charAt(0))) {
				this.isIntRange = false;
				this.lower = Character.toLowerCase(bounds[0].charAt(0));
				this.upper = this.lower;
			} else {
				try {
					this.lower = Integer.parseInt(bounds[0]);
					this.upper = this.lower;
				} catch (NumberFormatException e) {
					throw new InvalidRangeException("Invalid range: '" + str + "'");
				}
			}
		}
		if (upper < lower) {
			//Swap the numbers
			upper += lower;
			lower = upper-lower;
			upper = upper-lower;
		}
	}
	
	public int getRandInt() {
		Random rand = new Random();
		return rand.nextInt(this.upper-this.lower+1) + this.lower;
	}
	
	public char getRandChar() {
		Random rand = new Random();
		return (char)(rand.nextInt(this.upper-this.lower+1) + this.lower);
	}
	
	public String getRandom() {
		if(isIntRange) {
			return Integer.toString(getRandInt());
		}
		return "" + getRandChar();
	}

	public boolean isIntRange() {
		return isIntRange;
	}
}
