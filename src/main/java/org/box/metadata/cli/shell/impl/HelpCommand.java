package org.box.metadata.cli.shell.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.shell.CommandHandler;
import org.box.metadata.cli.shell.Shell;
import org.box.metadata.cli.shell.exception.TerminatedException;
import org.box.metadata.cli.util.HelpFormatter;

/**
 * Prints out a list of available applications and commands.
 * 
 * A command description and implementation here.
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */
public class HelpCommand implements CommandHandler<DefaultConsoleConfiguration> {

	@Argument(index = 1, name = "<command>", required = false)
	public String command;
	
	@Override
	public void validate(DefaultConsoleConfiguration cfg, Shell shell) {}

	@Override
	public void process(DefaultConsoleConfiguration cfg, Shell shell) throws TerminatedException {
		HelpFormatter formatter = new HelpFormatter(shell.getModel());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(baos);
		formatter.printHelp(writer);
		writer.flush();
		
		shell.appendMessage(baos.toString());
	}

}