package org.box.metadata.cli.shell;

import org.box.metadata.cli.CommandLineParser;
import org.box.metadata.cli.exception.ParseException;
import org.box.metadata.cli.shell.exception.TerminatedException;

/**
 * Describes API for User's applications to support the Shell facilities.
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 * @param <T>
 *            - a class of the application's configuration.
 * 
 * @see Shell
 * @see CommandLineParser
 */
public interface CommandHandler<T> {

	/**
	 * A request of the application to validate instantiated configuration
	 * before execution
	 * 
	 * @param cfg
	 *            - an instance of configuration.
	 * @param shell TODO
	 * 
	 */
	void validate(T cfg, Shell shell) throws ParseException ;

	/**
	 * Run the application with the given configuration
	 * 
	 * @param cfg - an instance of configuration.
	 * @param shell TODO
	 * 
	 * @throws TerminatedException
	 *             if application was stopped
	 */
	void process(T cfg, Shell shell) throws TerminatedException;
	
}
