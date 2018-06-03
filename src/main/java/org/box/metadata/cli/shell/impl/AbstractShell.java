package org.box.metadata.cli.shell.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.box.metadata.cli.CommandLineParser;
import org.box.metadata.cli.annotation.Command;
import org.box.metadata.cli.exception.InvalidConfigurationException;
import org.box.metadata.cli.exception.ParseException;
import org.box.metadata.cli.impl.CommandCompile;
import org.box.metadata.cli.impl.ConfigurationModel;
import org.box.metadata.cli.impl.MetaCommandLineParser;
import org.box.metadata.cli.impl.MetaCommandLineParser.ParserType;
import org.box.metadata.cli.shell.CommandHandler;
import org.box.metadata.cli.shell.CommandHandlerListener;
import org.box.metadata.cli.shell.Shell;
import org.box.metadata.cli.shell.exception.TerminatedException;
import org.box.metadata.cli.shell.impl.message.ErrorMessageFormatter;
import org.box.metadata.cli.shell.impl.message.GreetingMessageFormatter;
import org.box.metadata.cli.shell.impl.message.HelpMessageFormatter;
import org.box.metadata.cli.shell.impl.message.InfoMessageFormatter;
import org.box.metadata.cli.shell.impl.message.MessageFormatters;
import org.box.metadata.cli.util.CLIUtil;

/**
 * <p>
 * Provides second-level abstraction of Sell API and implements fundamental
 * aspects for Shell functionality such as handling User requests and
 * processing commands.
 * </p>
 * 
 * 
 * @see Shell
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractShell implements Shell {
	
	private static final class BasicSellCLIParser extends
			MetaCommandLineParser<Object> {
		
		private BasicSellCLIParser(ConfigurationModel model, ParserType parserType) {
			super(model, parserType);
		}

		public Object processCmd(ShellCommandCompile cmd, String[] args) throws ParseException {
			org.apache.commons.cli.CommandLine cl;

			try {
				Field cField = null;
					
				cField = cmd.getFeild();
				cl = delegate.parse(cmd.getCLIOptions(), 
						CLIUtil.excludeCommandName(cmd, args));

				checkArgumentsPopulation(cl, model);
				Object obj = cmd.getCfgClass().newInstance();

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
	}

	private MessageFormatters messageFormatters = new MessageFormatters()
			.setMessageFormatter(new GreetingMessageFormatter())
			.setMessageFormatter(new ErrorMessageFormatter())
			.setMessageFormatter(new HelpMessageFormatter())
			.setMessageFormatter(new InfoMessageFormatter());
	
	private final transient Set<CommandHandlerListener> listeners = new CopyOnWriteArraySet<CommandHandlerListener>();
	
	private final ConfigurationModel model;

	protected volatile boolean doExit;

	private final CommandLineParser parser;

	protected AbstractShell(ParserType parserType, ConfigurationModel model) {
		this.model = model;
		parser = createParser(model, parserType);
	}
	
	protected AbstractShell(ParserType parserType, Class... cfgClasses) {
		model = new ShellCommandsModel(cfgClasses);
		parser = createParser(model, parserType);
	}
	
	protected CommandLineParser createParser(ConfigurationModel model, ParserType parserType) {
		return new BasicSellCLIParser(model, parserType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.box.metadata.shell.Shell#run(java.util.List, java.lang.String[])
	 */
	@Override
	public void run(String[] args) {

		displayGreeting();

		handleUserInput(args);

		mainLoop();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.box.metadata.shell.Shell#runAndExit(org.box.metadata.shell.Application
	 * , java.lang.String[])
	 */
	@Override
	public void runAndExit(String[] args) {
		if (args.length > 0) 
			executeCommand(args);
		else
			displayHelp(null, new ParseException("no command specified"));
	}

	@SuppressWarnings("unchecked")
	protected void runHandlers(Command c, Object cfg) throws TerminatedException, ParseException {
		
		for (Class<? extends CommandHandler<?>> chc : c.handlers()) {
			CommandHandler handler = null;
			try {
				handler = chc.newInstance();
				handler.validate(cfg, this);
				fireHandlerStart(c, cfg, handler); // fire about start
				handler.process(cfg, this);
				fireHandlerSuccess(c, cfg, handler);
			} catch (InstantiationException e) {
				throw new InvalidConfigurationException(e.getMessage());
			} catch (IllegalAccessException e) {
				throw new InvalidConfigurationException(e.getMessage());
			} 
		}
	}

	private void fireHandlerSuccess(Command c, Object cfg, CommandHandler handler) {
		for (CommandHandlerListener l : listeners) {
			l.onHandlerSuccess(handler, c, cfg);
		}
		
	}

	private void fireHandlerFailure(Command c, Object cfg, CommandHandler handler) {
		for (CommandHandlerListener l : listeners) {
			l.onHandlerFailure(handler, c, cfg);
		}
	}

	private void fireHandlerStart(Command c, Object cfg, CommandHandler handler) {
		for (CommandHandlerListener l : listeners) {
			l.onHandlerStart(handler, c, cfg);
		}
	}

	/**
	 * Method responsible to provide User's input
	 * 
	 * @return a string 
	 */
	protected abstract String[] getUserInput();

	/**
	 * Handles User's input and executes corresponding Shell's and User's
	 * application commands
	 * 
	 * @param handlersMap
	 * 
	 * @param args
	 *            - user's input as a list of arguments from the command line
	 */
	protected void handleUserInput(String[] args) {

		if (args.length > 0) 
			executeCommand(args);
		
	}

	protected void executeCommand(String[] args) {
		CommandCompile cc = model.findCommandForInput(args);
		if (cc != null) {
			Command orig = cc.getOrig();
			Object cfg = null;
			try {
				ShellCommandCompile scc = ((ShellCommandCompile) cc);
				cfg = ((BasicSellCLIParser)parser).processCmd(scc, args);
				runHandlers(orig, cfg);
			} catch (ParseException e) {
				// wrong parameters specified for the command.
				// the actual command processing is not started yet.
				displayHelp(cc, e);
			} catch (Throwable e) {
				fireHandlerFailure(orig, cfg, null);
				displayError(cc, e);
			}
		} else
			displayInfo(String.format(
					"No appropriate command has been found for '%s'",
					args[0]));
	}

	/**
	 * Runs the main loop
	 * 
	 * @param wrappers
	 * @throws IOException
	 */
	protected void mainLoop() {
		while (!doExit) {
			handleUserInput(getUserInput());
		}
	}

	/**
	 * Prints a greeting message to the user on Shell's run
	 * @param cfgClass 
	 */
	protected void displayGreeting() {
		String msg = getFormatters().get(
				GreetingMessageFormatter.GREETING_MESSAGE_FORMATTER).format(null, model, null);
		
		appendMessage(msg);
	}

	/**
	 * Prints out a help message about the given <code>application</code> usage
	 * @param e - an error from the application
	 * @param cfgClass 
	 * @param app - an application which usage help must be displayed
	 */
	protected void displayHelp(CommandCompile c, ParseException e) {
		String msg = getFormatters().get(
				HelpMessageFormatter.HELP_MESSAGE_FORMATTER).format(c, model, e);
		
		appendMessage(msg);
	}
	
	/**
	 * Prints out an error message from the given <code>application</code>
	 * @param e - the error from the application
	 */
	protected void displayError(CommandCompile c, Throwable e) {
		String msg = getFormatters().get(
				ErrorMessageFormatter.ERROR_MESSAGE_FORMATTER).format(c, model, e);
		
		appendMessage(msg);
	}

	/**
	 * Prints out an info message for user
	 * 
	 * @param message - a message to print to
	 */
	protected void displayInfo(String message) {
		String msg = getFormatters().get(
				InfoMessageFormatter.INFO_MESSAGE_FORMATTER).format( null, null, null, message);
		
		appendMessage(msg);

	}
	
	/**
	 * The getter for message formatters' collection
	 * 
	 * @return a MessageFormatters object
	 */
	public MessageFormatters getFormatters() {
		return messageFormatters;
	}

	/**
	 * The setter for message formatters' collection
	 * 
	 * @param msgFormatters a MessageFormatters object
	 */
	public void setFormatters(MessageFormatters msgFormatters) {
		this.messageFormatters = msgFormatters;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.Shell#addListener(org.box.metadata.shell.CommandHandlerListener)
	 */
	@Override
	public void addListener(CommandHandlerListener listener) {
		listeners.add(listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.Shell#removeListener(org.box.metadata.shell.CommandHandlerListener)
	 */
	@Override
	public void removeListener(CommandHandlerListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public ConfigurationModel getModel() {
		return model;
	}
}
