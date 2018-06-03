package org.box.metadata.cli.exception;

import org.apache.commons.cli.Options;
import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.annotation.Command;

/**
 * Runtime exception occurred:
 * 
 * <li> if a class representing Application Configuration 
 * 		is not a {@link CLIConfiguration}
 * 
 * <li> if it has invalid {@link Command}, {@link Options} ,{@link Argument} 
 * 		name(s) or field types.
 * 
 * <li> if a configuration or command class cannot be instantiated 
 *  	via default constructor.
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */
public class InvalidConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new <code>Exception</code> with the specified
	 * detail message.
	 * 
	 * @param message
	 *         - the detail message
	 */
	public InvalidConfigurationException(String message) {
		super(message);
	}
	
	/**
	 * Wraps an error into InvalidConfigurationException
	 * 
	 * @param e 
	 * 		- an error happened during Configuration operating 
	 */
	public InvalidConfigurationException(Throwable e) {
		super(e);
	}

}
