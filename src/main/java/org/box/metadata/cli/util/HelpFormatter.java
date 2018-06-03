package org.box.metadata.cli.util;

import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.cli.Options;
import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.exception.InvalidConfigurationException;
import org.box.metadata.cli.impl.CommandCompile;
import org.box.metadata.cli.impl.ConfigurationModel;
import org.box.metadata.cli.impl.DefaultConfigurationModel;
import org.box.metadata.cli.shell.impl.ShellCommandCompile;
import org.box.metadata.cli.shell.impl.ShellCommandsModel;

/**
 * A formatter of help messages for the given command line configuration.
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 */
public class HelpFormatter {

	private final org.apache.commons.cli.HelpFormatter delegate = new org.apache.commons.cli.HelpFormatter();
	private final ConfigurationModel model;
	private final CommandCompile command;

	/**
	 * Constructs a new instance of {@link HelpFormatter}
	 * 
	 */
	public HelpFormatter(ConfigurationModel model) {
		this.model = model;
		this.command = null;
	}	
	
	/**
	 * Constructs a new instance of {@link HelpFormatter}
	 * 
	 */
	public HelpFormatter(ConfigurationModel model, CommandCompile command) {
		this.model = model;
		this.command = command;
	}	


	/**
	 * Generates usage section for help created from several parts: 
	 * <li>Commands - generates a list of commands, if those are present 
	 * in the Configuration class(es). Or it is a list of sub-command, if
	 * particular {@link CommandCompile} is specified for HelpFormater 
	 * during construction.
	 * 
	 * <li>Options - adds a string [options] to the resulted string, if at least
	 * one options are present in the Configuration.
	 * 
	 * <li>Arguments - If non-Enumerated Arguments field is present in the
	 * Configuration and it has non-empty syntax parameter, it is being returned
	 * otherwise an aggregated string from Enumerated Argument fields
	 * 
	 * @return usage section
	 */
	public String generateUsageSyntax() {

		StringBuilder sb = new StringBuilder();
		List<CommandCompile> commands = model.getCommands();
		if (model instanceof ShellCommandsModel && command == null) {
			if (commands.size() > 0) {
				for (CommandCompile cc : commands) {
					ShellCommandCompile scc = (ShellCommandCompile) cc;
					boolean embeddedCmd = "".equalsIgnoreCase(scc.getAppName());
					boolean defaultCmd = "".equalsIgnoreCase(scc.getFullAndShortName());
					if (embeddedCmd || defaultCmd) {
						if (sb.length() > 0) {
							sb.append("|");
						}
						if (defaultCmd)
							sb.append(scc.getAppName());
						else	
							sb.append(cc.getFullAndShortName());
					}
				}
			}
			sb.append(" " + (hasRootOptions() ? "[options] " : ""));

			String syntax = model.getRootArgumentsSyntax();
			if (syntax == null || "".equals(syntax.trim())) {
				sb.append(getEnumeratedArgsSyntax(model.getRootArguments()));
			} else {
				sb.append(syntax);
			}

		} else if (model instanceof DefaultConfigurationModel && command == null) {
			if (commands.size() > 0) {
				for (CommandCompile cmd : commands) {
					if (sb.length() > 0) {
						sb.append("|");
					}
					sb.append(cmd.getFullAndShortName());
				}
			}
			sb.append(" " + (hasRootOptions() ? "[options] " : ""));
			
			String syntax = model.getRootArgumentsSyntax();
			if (syntax == null || "".equals(syntax.trim())) {
				sb.append(getEnumeratedArgsSyntax(model.getRootArguments()));
			} else {
				sb.append(syntax);
			}

		} else if (command != null) {
			throw new UnsupportedOperationException("TODO");
		}


		return sb.toString();
	};


	public String genereteHeader() {
		if (model instanceof ShellCommandsModel && command == null) {
			return "These shell commands are defined internally. Type `help' to see this list.\n" 
				 + "Type `help name' to find out more about the function `name'\n";
		} else if (command != null) {
			return command.getOrig().name() + ": " + command.getOrig().description();
		}
		return null;
	}

	/**
	 * Returns true, if cfgClasses has at least one option annotation
	 * 
	 * @return true, if configuration class has options
	 */
	protected boolean hasRootOptions() {
		return model.getRootCLIOptions() != null 
				&& model.getRootCLIOptions().getOptions().size() > 0;
	}

	/**
	 * Generates a syntax string from Enumerated Arguments for help section
	 * 
	 * @return a usage syntax as string
	 */
	protected String getEnumeratedArgsSyntax(Argument[] args) {
		if (args == null)
			return "";

		StringBuilder sb = new StringBuilder();
		for (Argument a : args) {
			sb.append(a.required() ? a.name() + " " : "[" + a.name() + "] ");
		}

		return sb.toString();
	}

	/**
	 * Print the help for <code>configuration</code> with auto generated command
	 * line syntax. This method prints help information to System.out.
	 * 
	 */
	public void printHelp() {
		PrintWriter pw = new PrintWriter(System.out);
		printHelp(pw, org.apache.commons.cli.HelpFormatter.DEFAULT_WIDTH,
				generateUsageSyntax(), genereteHeader(), null);
	}

	/**
	 * <p>
	 * Print the help for {@link ConfigurationModel} and/or {@link CommandCompile} into the 
	 * {@link PrintWriter}. 
	 * </p>
	 * 
	 * @param pw
	 *            - the output help print writer
	 */
	public void printHelp(PrintWriter pw) {
		printHelp(pw, org.apache.commons.cli.HelpFormatter.DEFAULT_WIDTH,
				generateUsageSyntax(), genereteHeader(), null);
	}

	/**
	 * <p>
	 * Print the help for {@link ConfigurationModel} with the specified command
	 * line syntax. 
	 * </p>
	 * <p>
	 * This method prints help information to the given writer.
	 * 
	 * @param cmdLineSyntax
	 *            the syntax for this application
	 * @throws InvalidConfigurationException
	 *             if the given class is not compatible or valid as an
	 *             Application's configuration
	 */
	public void printHelp(PrintWriter pw, String cmdLineSyntax)
			throws InvalidConfigurationException {
		printHelp(pw, org.apache.commons.cli.HelpFormatter.DEFAULT_WIDTH,
				cmdLineSyntax, genereteHeader(), null);
	}

	/**
	 * Print the help for <code>configuration</code> with the specified command
	 * line syntax. This method prints help information to System.out.
	 * 
	 * @param width
	 *            the number of characters to be displayed on each line
	 * @param cmdLineSyntax
	 *            the syntax for this application
	 * @param header
	 *            the banner to display at the begining of the help
	 * @param options
	 *            the Options instance
	 * @param footer
	 *            the banner to display at the end of the help
	 * @param autoUsage
	 *            whether to print an automatically generated usage statement
	 * @throws InvalidConfigurationException
	 *             if the given class is not compatible or valid as an
	 *             Application's configuration
	 */
	public void printHelp(PrintWriter pw, int width, String cmdLineSyntax, String header,
			String footer)
			throws InvalidConfigurationException {

		printHelp(pw, width, cmdLineSyntax, header,
				org.apache.commons.cli.HelpFormatter.DEFAULT_LEFT_PAD,
				org.apache.commons.cli.HelpFormatter.DEFAULT_DESC_PAD, footer);
		pw.flush();
	}

	/**
	 * Prints the help for {@link ConfigurationModel} and/or {@link CommandCompile} 
	 * with the specified command line width, command syntax, header, footer into 
	 * the given {@link PrintWriter}
	 * 
	 * @param pw
	 *            the writer to which the help will be written
	 * @param width
	 *            the number of characters to be displayed on each line
	 * @param cmdLineSyntax
	 *            the syntax for this application
	 * @param header
	 *            the banner to display at the begining of the help
	 * @param leftPad
	 *            the number of characters of padding to be prefixed to each
	 *            line
	 * @param descPad
	 *            the number of characters of padding to be prefixed to each
	 *            description line
	 * @param footer
	 *            the banner to display at the end of the help
	 * @throws InvalidConfigurationException
	 *             if the given class is not compatible or valid as an
	 *             Application's configuration
	 * @throws IllegalStateException
	 *             if there is no room to print a line
	 */
	public void printHelp(PrintWriter pw, int width, String cmdLineSyntax,
			String header, int leftPad, int descPad, String footer) throws InvalidConfigurationException {
		

		if (cmdLineSyntax != null && !"".equals(cmdLineSyntax.trim())) {
			delegate.printUsage(pw, width, cmdLineSyntax);
		}

		if ((header != null) && (header.trim().length() > 0)) {
			delegate.printWrapped(pw, width, header);
		}

		if (model instanceof ShellCommandsModel && command == null) {
			List<CommandCompile> commands = model.getCommands();
			if (commands.size() > 0) {
				for (CommandCompile command : commands) {
					ShellCommandCompile scc = (ShellCommandCompile) command;
					boolean embeddedCmd = "".equalsIgnoreCase(scc.getAppName());
					boolean defaultCmd = "".equalsIgnoreCase(scc.getFullAndShortName());
					if (embeddedCmd || defaultCmd) 
						printCommand(pw, width, command, leftPad, descPad, false);
				}
			}

			Options options = model.getRootCLIOptions();
			if (options.getOptions().size() > 0) {

				delegate.printWrapped(pw, width, "\noptions:");
				delegate.printWrapped(pw, width, "");
				delegate.printOptions(pw, width, options, leftPad, descPad);
			}

		} else if (model instanceof DefaultConfigurationModel && command == null) {
			List<CommandCompile> commands = model.getCommands();
			if (commands.size() > 0) {
				for (CommandCompile command : commands) {
					printCommand(pw, width, command, leftPad, descPad, true);
				}
			}

			Options options = model.getRootCLIOptions();
			if (options.getOptions().size() > 0) {

				delegate.printWrapped(pw, width, "\noptions:");
				delegate.printWrapped(pw, width, "");
				delegate.printOptions(pw, width, options, leftPad, descPad);
			}

		} else if (command != null) {
			throw new UnsupportedOperationException("TODO");
		}

		if ((footer != null) && (footer.trim().length() > 0)) {
			delegate.printWrapped(pw, width, footer);
		}

	}

	/**
	 * Prints help for the given <code>command</code> that includes 
	 * command's options and arguments.
	 * 
	 * @param pw
	 *            the writer to which the help will be written
	 * @param width
	 *            the number of characters to be displayed on each line
	 * @param command
	 *            the command to print help for
	 * @param leftPad
	 *            the number of characters of padding to be prefixed to each
	 *            line
	 * @param descPad
	 *            the number of characters of padding to be prefixed to each
	 *            description line
	 */
	public void printCommand(PrintWriter pw, int width, CommandCompile command,
			int leftPad, int descPad, boolean showOptions) {
		
		String name = command.getFullAndShortName();
		if ("".equals(name) && command instanceof ShellCommandCompile)
			name = ((ShellCommandCompile) command).getAppName();
		
		String syntax = command.getArgumentsSyntax();
		if (syntax == null) 
			syntax = getEnumeratedArgsSyntax(command.getArguments());
		
		String cmdSyntax = String.format(createPadding(leftPad) + " %-"+leftPad + descPad+"s%s",
				name, syntax);

		delegate.printWrapped(pw, width, cmdSyntax);
		if (showOptions) {
			Options options = command.getCLIOptions();
			if (options.getOptions().size() > 0) {
				delegate.printOptions(pw, width, options, leftPad * 2 + 1, descPad);
				delegate.printWrapped(pw, 0, "");
			}
		}
	}

	/**
	 * Return a String of padding of length <code>len</code>.
	 * 
	 * @param len
	 *            The length of the String of padding to create.
	 * 
	 * @return The String of padding
	 */
	public static String createPadding(int len) {
		StringBuffer sb = new StringBuffer(len);

		for (int i = 0; i < len; ++i) {
			sb.append(' ');
		}

		return sb.toString();
	}
}
