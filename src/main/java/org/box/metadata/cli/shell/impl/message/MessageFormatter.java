package org.box.metadata.cli.shell.impl.message;

import org.box.metadata.cli.impl.CommandCompile;
import org.box.metadata.cli.impl.ConfigurationModel;

/**
 * Describes API for message formating of outgoing messages from the Shell.
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 *
 */
public interface MessageFormatter {

	/**
	 * A unique identifier of the assigned formatter.
	 * 
	 * @return a identifier as a string
	 */
	String getId();
	
	/**
	 * Format the given parameters from Shell to a string 
	 * 
	 * @param command 
	 * 			- the message producer application
	 * 
	 * @param model
	 * 			- a commands model that containing the command 
	 * 
	 * @param e
	 * 			- an error instance if any
	 * 
	 * @param args 
	 * 			- a list of addition arguments such as an exception etc.
	 * 
	 * @return a message as a string
	 */
	String format(CommandCompile command, ConfigurationModel model, Throwable e, Object... args);
	
}
