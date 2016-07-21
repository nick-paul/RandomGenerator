package exceptions;

@SuppressWarnings("serial")
public class InvalidTagException extends Exception {
	public InvalidTagException(String s) {
		super(s);
	}
}
