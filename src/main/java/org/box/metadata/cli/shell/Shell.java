package org.box.metadata.cli.shell;

import org.box.metadata.cli.impl.ConfigurationModel;

/**
 * <p>
 * Describes a basic API for Shell's implementation in terms of meta-CLI
 * framework.
 * </p>
 * 
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */
public interface Shell {
	

	/**
	 * Runs a shell with the given list of Command Line arguments and wait
	 * until user call the 'exit' command or 'exit' method is called in the
	 * separated thread.
	 * 
	 * @param initArgs
	 *            - the list of arguments.
	 */
	void run(String[] initArgs);

	/**
	 * Runs a shell with the given list of Command Line arguments and closed 
	 * immediately after the specified command end
	 * 
	 * @param initArgs
	 *            - the list of arguments.
	 */
	void runAndExit(String[] args);

	/**
	 * Appends a message to Shell log. In the most cases those messages are
	 * from command handlers. 
	 * 
	 * @param msg - a message to print out
	 */
	void appendMessage(String msg);
	
	/**
	 * <p>
	 * Appends a message to Shell log. In the most cases those messages are
	 * from command handlers.
	 * </p>
	 * 
	 * <p>
	 * Makes no break line.
	 * <p> 
	 * 
	 * @param msg - a message to print out
	 */
	void appendMessageNoBr(String msg);


	/**
	 * Returns compiled commands model. 
	 * 
	 * @see ConfigurationModel
	 * 
	 * @return a current instance of model used by Shell.
	 */
	ConfigurationModel getModel();

	/**
	 * Returns from the Shell if no command active or waits it completion.
	 * 
	 * The Shell's Thread must be interrupted to terminate the active 
	 * command and exit shell immediately.
	 * 
	 */
	void exit();

	void addListener(CommandHandlerListener listener);
	
	void removeListener(CommandHandlerListener listener);
	

}
