package org.box.metadata.cli;

import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.annotation.Command;
import org.box.metadata.cli.annotation.Option;
import org.box.metadata.cli.shell.impl.DymmyCreateHandler;


/**
 * 
 * An advanced application configuration that covers commands and 
 * commands' handlers functionality.
 * 
 * @author alex
 *
 */

@CLIConfiguration(version="0", name= "dummy")
public class DummyConfigurationCommands {
	
	public static class DummyCommand {

		@Option(description = "Simple yes/no option without value", 
				fullName = "key2", 
				shortName = "k2")
		public boolean key2;
		
		@Argument(index = 1, name = "filename", required = true)
		public String infile;
		
	}
	
	@Command(description = "create a new dummy command", name = "create", 
			handlers = DymmyCreateHandler.class)
	public DummyCommand create;
	
	@Command(description = "delete a active dummy command", name = "delete")
	public DummyCommand delete;

	@Option(description = "The option to show help info", 
			fullName = "help", 
			shortName = "h")
	public boolean help;

	@Option(description = "Exit from the shell after completion", 
			fullName = "exitOnFinish", 
			shortName = "eof")
	public boolean exitOnFinifh;

}
