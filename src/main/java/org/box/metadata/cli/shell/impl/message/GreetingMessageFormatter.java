package org.box.metadata.cli.shell.impl.message;

import org.box.metadata.cli.impl.CommandCompile;
import org.box.metadata.cli.impl.ConfigurationModel;

public class GreetingMessageFormatter implements MessageFormatter {

	public static final String GREETING_MESSAGE_FORMATTER = "GreetingMessageFormatter";

	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.message.MessageFormatter#format(org.box.metadata.shell.Application, java.lang.Object[])
	 */
	public String format(CommandCompile command, ConfigurationModel model, Throwable e, Object... args) {
		//TODO:
		return "Walcome to Mata-CLI Shell (1.0.0)\n\n"
				+ "Please type 'help' to see all commands.";
	}

	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.message.MessageFormatter#getId()
	 */
	public String getId() {
		return GREETING_MESSAGE_FORMATTER;
	}

}
