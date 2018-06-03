package org.box.metadata.cli.exception;

public class ParseException extends Exception {

	private static final long serialVersionUID = 1L;

	
	/**
	 * Construct a new <code>ParseException</code> with the specified detail
	 * message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public ParseException(String message) {
		super(message);
	}

	/**
	 * Construct a new <code>ParseException</code> with the specified detail
	 * message.
	 * 
	 * @param message
	 *          the detail message
	 * @param args
	 * 			a list of the message arguments 
	 */
	public ParseException(String message, Object... args) {
		super(String.format(message, args));
	}
}
