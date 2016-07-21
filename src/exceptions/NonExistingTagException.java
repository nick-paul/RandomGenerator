package exceptions;

@SuppressWarnings("serial")
public class NonExistingTagException extends Exception {
	public NonExistingTagException(String s) {
		super(s);
	}
}
