package org.box.metadata.cli.impl;

import java.lang.reflect.Field;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Parser;
import org.box.metadata.cli.CommandLineParser;
import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.annotation.Arguments;
import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.annotation.Option;
import org.box.metadata.cli.exception.InvalidConfigurationException;
import org.box.metadata.cli.exception.ParseException;
import org.box.metadata.cli.shell.impl.AbstractShell;
import org.box.metadata.cli.util.CLIUtil;

/**
 * 
 * Annotation's based {@link CommandLineParser} implementation for Application
 * Command Line Interface. MetaCommandLineParser uses meta information
 * represented via java annotations in an application's configuration class.
 * 
 * Simple application configuration class can be demonstrated by the code
 * snippet below:
 * 
 * <pre>
 * &#064;CLIConfiguration(version="0", name="myApp")
 * public class AppConfiguration {
 * 
 * 	&#064;Option(description = &quot;Simple private yes/no option without agruments&quot;, fullName = &quot;key1&quot;, shortName = &quot;k1&quot;)
 * 	private boolean key1;
 * 
 * 	&#064;Option(description = &quot;Simple key/value option with agruments&quot;, fullName = &quot;keyValue1&quot;, shortName = &quot;kv1&quot;, hasArguments = true)
 * 	public String keyValue1;
 * 
 * 	&#064;Argument(index = 1, name = &quot;infile&quot;, required = true)
 * 	public String infile;
 * 
 * }
 * </pre>
 * 
 * Parser analyzes User's input and creates a new AppConfiguration instance with
 * injected values into corresponding fields.
 * 
 * <pre>
 * CommandLineParser&lt;AppConfiguration&gt; cliParser = new MetaCommandLineParser&lt;&gt;();
 * 
 * AppConfiguration cfg = cliParser.parse(args);
 * </pre>
 * 
 * or you can simply call:
 * 
 * <pre>
 * AppConfiguration cfg = MetaCli.parse(args, AppConfiguration.class);
 * </pre>
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */
public class MetaCommandLineParser<T> implements CommandLineParser<T> {

	/**
	 * <p>
	 * Enum of all out of box parsers supported by the Apache's commons-cli
	 * library.
	 * </p>
	 * 
	 * <p>
	 * See Apache documentation about parser differences.
	 * </p>
	 */
	public enum ParserType {
		BASIC_PARSER {
			@Override
			public Parser createParser() {
				return new org.apache.commons.cli.BasicParser();
			}
		}, //
		GNU_PARSER {
			@Override
			public Parser createParser() {
				return new org.apache.commons.cli.GnuParser();
			}
		}, //
		POSIX_PARSER() {
			@Override
			public Parser createParser() {
				return new org.apache.commons.cli.PosixParser();
			}
		};//
		
		public abstract org.apache.commons.cli.Parser createParser();
	}

	protected final org.apache.commons.cli.CommandLineParser delegate;
	protected final ConfigurationModel model;
	private final Class<?> cfgClass;

	/**
	 * Constructors a new instance with nested GnuParser inside.
	 * 
	 * @param cfgClass
	 * 				- a class annotated by {@link CLIConfiguration}
	 * 
	 */
	public MetaCommandLineParser(Class<T> cfgClass) {
		this.model = new DefaultConfigurationModel(cfgClass);
		this.cfgClass = cfgClass;
		delegate = ParserType.GNU_PARSER.createParser();
	}

	/**
	 * Constructors a new instance of the parser with the given type.
	 * 
	 * @param cfgClass
	 * 				-a class annotated by {@link CLIConfiguration}
	 * 
	 * @param parserType
	 * 				- one of the following {@link ParserType}
	 */
	public MetaCommandLineParser(Class<T> cfgClass, ParserType parserType) {
		this.model = new DefaultConfigurationModel(cfgClass);
		this.cfgClass = cfgClass;
		delegate = parserType.createParser();
	}

	/**
	 * <p>
	 * Protected parser initialization. Used by {@link AbstractShell} for 
	 * surrogate User Input processing.
	 * </p>
	 * 
	 * @param model
	 * 				- a compiled model from cfgClass
	 * 
	 * @param parserType
	 * 				- one of the following {@link ParserType}
	 */
	protected MetaCommandLineParser(ConfigurationModel model, ParserType parserType) {
		this.model = model;
		delegate = parserType.createParser();
		this.cfgClass = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.box.metadate.cli.CommandLineParser#parse(java.lang.String[])
	 */
	@Override
	public T parse(String[] args) throws ParseException {

		org.apache.commons.cli.CommandLine cl;

		try {
			Field cField = null;
			CommandCompile cmd = model.findCommandForInput(args);
			if (cmd != null) {
				
				cField = cmd.getFeild();
				cl = delegate.parse(cmd.getCLIOptions(), 
						CLIUtil.excludeCommandName(cmd, args));
			} else
				cl = delegate.parse(model.getRootCLIOptions(), args);

			checkArgumentsPopulation(cl, model);
			
			T obj = doCreateConfigurationObject(cmd, cField, cl);
			doPopulateConfigurationObject(obj, cmd, cField, cl);
			
			return obj;
		} catch (org.apache.commons.cli.ParseException e) {
			throw new ParseException(e.getMessage());
		} catch (InstantiationException e) {
			throw new InvalidConfigurationException(e);
		} catch (IllegalAccessException e) {
			throw new InvalidConfigurationException(e);
		}
	}

	protected void doPopulateConfigurationObject(Object cfg, CommandCompile cmd,
			Field cField, CommandLine cl) throws ParseException {
		try {
			
			if (cField != null) {
				Object command = cField.getType().newInstance();
				cField.setAccessible(true);
				cField.set(cfg, command);

				injectValues(cfg, cl, false);
				injectValues(command, cl, true);
			} else {
				injectValues(cfg, cl, true);
			}

		} catch (InstantiationException e) {
			throw new InvalidConfigurationException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new InvalidConfigurationException(e.getMessage());
		}
		
	}

	@SuppressWarnings("unchecked")
	protected T doCreateConfigurationObject(CommandCompile cmd, Field cField,
			org.apache.commons.cli.CommandLine cl) throws ParseException, 
			InstantiationException, IllegalAccessException {

		return (T) cfgClass.newInstance();
	}

	/**
	 * Injects Command Line options and arguments into the Application's
	 * configuration object
	 * 
	 * @param cfg
	 *            - the instance of Application's configuration or a
	 *            configuration's command
	 * @param cmd
	 *            - CommandLine from delegated parser
	 * @param populateArgs
	 *            - populate arguments fields too, or just options
	 *            
	 * @throws ParseException
	 *             occurs if an options's value can't be casted to assigned enum.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected void injectValues(Object cfg,
			org.apache.commons.cli.CommandLine cmd, boolean populateArgs)
			throws ParseException {

		try {
			for (Field f : cfg.getClass().getDeclaredFields()) {
				// populate options
				if (f.isAnnotationPresent(Option.class)) {
					Option o = f.getAnnotation(Option.class);
					if (cmd.hasOption(o.shortName())) {
						f.setAccessible(true);
						if (o.hasArguments()) {
							f.set(cfg,  CLIUtil.autoCast(f, cmd.getOptionValue(o.shortName()),
								o.shortName(), null));
						} else {
							f.setBoolean(cfg, true);
						}
					} else if (cmd.hasOption(o.fullName())) {
						f.setAccessible(true);
						if (o.hasArguments()) {
							f.set(cfg, CLIUtil.autoCast(f, cmd.getOptionValue(o.fullName()),
								o.fullName(), null));
						} else {
							f.setBoolean(cfg, true);
						}
					}
				} 

				if (populateArgs) {
					// populate enumerated arguments
					String[] args = cmd.getArgs();
					if (f.isAnnotationPresent(Argument.class)) {
						Argument a = f.getAnnotation(Argument.class);
						if (a.index() <= args.length) {
							String val = args[a.index() - 1];
							f.setAccessible(true);
							f.set(cfg, CLIUtil.autoCast(f, val, null, a.name()));
						}
					}

					// non-enumerated args ( including enumerated arguments)
					if (f.isAnnotationPresent(Arguments.class)) {
						f.setAccessible(true);
						f.set(cfg, args);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			throw new InvalidConfigurationException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new InvalidConfigurationException(e.getMessage());
		}

	}

	/**
	 * Check CLI arguments related to {@link Argument} and {@link Arguments}
	 * annotations.
	 * 
	 * @param cmd
	 *            	- a result of delegated parser processing
	 * @param model
	 * 				- a compiled model from cfgClass
	 * 
	 * @throws ParseException
	 */
	protected void checkArgumentsPopulation(org.apache.commons.cli.CommandLine cmd,
			ConfigurationModel model) throws ParseException {

		String[] args = cmd.getArgs();
		for (Argument arg : model.getRootArguments()) {
			if (arg.required() && arg.index() > args.length) {
				throw new ParseException(String.format(
						"Argument '%s' is not specified", arg.name()));
			}
		}
	}

	/**
	 * Compiled model from cfgClass
	 * 
	 * @return a model of Application Configuration
	 */
	public ConfigurationModel getModel() {
		return model;
	}
}
