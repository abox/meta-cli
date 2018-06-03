package org.box.metadata.cli;

import org.box.metadata.cli.exception.InvalidConfigurationException;
import org.box.metadata.cli.exception.ParseException;
import org.box.metadata.cli.impl.MetaCommandLineParser;

/**
 * CommandLineParser interface describes API of instantiating 
 * and populating of an Application configuration based on java annotations.
 * 
 * @see MetaCommandLineParser
 * 
 * @param <T>
 *            - the application's configuration class
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 */
public interface CommandLineParser<T> {

	/**
	 * Parse the arguments according to the matadata informations and returns a
	 * new instance of application's configuration class with injected values
	 * 
	 * @param arguments
	 *            the command line arguments
	 * @return the instance of the given Application's configuration
	 * 
	 * @throws ParseException
	 *             if there are any problems encountered while parsing the
	 *             command line tokens.
	 * @throws InvalidConfigurationException
	 *             if the given class <code>T<code> is not compatible or valid as
	 *             an Application's configuration
	 */
	T parse(String[] arguments) throws ParseException;

}
