package org.box.metadata.cli.shell.impl;

import org.box.metadata.cli.shell.CommandHandler;
import org.box.metadata.cli.shell.Shell;
import org.box.metadata.cli.shell.exception.TerminatedException;

/**
 * Performs exit from the {@link Shell}
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */
public class ExitCommand implements CommandHandler<DefaultConsoleConfiguration> {


	@Override
	public void validate(DefaultConsoleConfiguration cfg, Shell shell) {}

	@Override
	public void process(DefaultConsoleConfiguration cfg, Shell shell) throws TerminatedException {
		shell.exit();
	}

}