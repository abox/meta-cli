package org.box.metadata.cli.shell.impl;

import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.annotation.Command;

@CLIConfiguration(version="0", name="")
public class DefaultConsoleConfiguration {

	/**
	 * it's ok to use simple Object class here while exit command 
	 * has no options or arguments.
	 */
	@Command(name = "exit", description = "exit from the shell", 
			handlers = ExitCommand.class)
	public Object exitCommand;
	
	@Command(name = "help", description = "print this help or help for <command>", 
			handlers = HelpCommand.class)
	public HelpCommand helpCommand;

}
