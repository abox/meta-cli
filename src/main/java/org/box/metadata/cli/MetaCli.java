package org.box.metadata.cli;

import java.io.PrintWriter;

import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.exception.ParseException;
import org.box.metadata.cli.impl.DefaultConfigurationModel;
import org.box.metadata.cli.impl.MetaCommandLineParser;
import org.box.metadata.cli.impl.MetaCommandLineParser.ParserType;
import org.box.metadata.cli.shell.exception.TerminatedException;
import org.box.metadata.cli.shell.impl.ConsoleShell;
import org.box.metadata.cli.util.HelpFormatter;

/**
 * <p>
 * Represents a facade API of the library and provides simplified methods to
 * working with the flowing modules:
 * 
 * <li> {@link MetaCommandLineParser}
 * <li> {@link HelpFormatter}
 * <li> {@link ConsoleShell}
 * 
 * </p>
 * 
 * @see CLIConfiguration
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */
public final class MetaCli {

	/**
	 * Parses a list of arguments and instantiates an instance of 
	 * the application's configuration with injected values in the 
	 * corresponding to fields.
	 * 
	 * @param args
	 *            - a list of arguments from the command line
	 * 
	 * @throws ParseException
	 *             if command line parsing errors (wrong user input)
	 *             
	 * @return a new instance of the configuration
	 */
	public static <T> T parse(Class<T> cfgCalss, String[] args)
			throws ParseException {
		return new MetaCommandLineParser<T>(cfgCalss).parse(args);
	}

	/**
	 * Parses a list of arguments and instantiates an instance of 
	 * the application's configuration with injected values in the 
	 * corresponding to fields.
	 * 
	 * @param args
	 *            - a list of arguments from the command line
	 * @param parserType
	 *            - the type of the parser to use for
	 * 
	 * @throws ParseException
	 *             if command line parsing errors
	 *             
	 * @return a new instance of the configuration
	 */
	public static <T> T parse(Class<T> cfgCalss, String[] args,  ParserType parserType) 
			throws ParseException {
		return new MetaCommandLineParser<T>(cfgCalss, parserType).parse(args);
	}

	/**
	 * Prints out help for the given configuration
	 * 
	 * @param cfgClass
	 *            - the class of the configuration
	 */
	public static void printHelp(Class<?> cfgClass) {
		new HelpFormatter(new DefaultConfigurationModel(cfgClass)).printHelp();
	}

	public static void printHelp(ParseException e, Class<?> cfgClass) {
		printHelp(e, cfgClass, new PrintWriter(System.out));
	}
	
	public static void printHelp(ParseException e, Class<?> cfgClass, PrintWriter out) {
		HelpFormatter formatter = new HelpFormatter(new DefaultConfigurationModel(cfgClass));

		if (e != null) {
			String s = e.getMessage();
			if (s != null && !s.trim().isEmpty())
				out.write(s + "\n");
			
			formatter.printHelp(out);
		}
		
		out.flush();
	}

	
	/**
	 * Runs the Shell with a list of arguments and perform exit after completion
	 * 
	 * @param args
	 *            - a list of arguments for the application
	 * 
	 * @throws TerminatedException
	 *             if the application terminates
	 */
	public static void runAndExit(String[] args, Class<?> cfgClass) throws TerminatedException {
		new ConsoleShell(cfgClass).runAndExit(args);
	}

	/**
	 * Runs the Shell with a list of arguments.
	 * 
	 * @param args
	 *            - a list of arguments for the application
	 *            
	 * @throws TerminatedException
	 *             if the application terminates
	 */
	public static void runShell(String[] args, Class<?> cfgClass)
			throws TerminatedException {
		new ConsoleShell(cfgClass).run(args);
	}
}
