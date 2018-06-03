package org.box.metadata.cli.shell.impl.message;

import org.box.metadata.cli.impl.CommandCompile;
import org.box.metadata.cli.impl.ConfigurationModel;

public class ErrorMessageFormatter implements MessageFormatter {

	public static final String ERROR_MESSAGE_FORMATTER = "ErrorMessageFormatter";

	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.message.ShellMessage#getMessage()
	 */
	@Override
	public String format(CommandCompile command, ConfigurationModel model, Throwable e, Object... args) {
		return (command != null 
				? command.getFullAndShortName() : "<unknown>")
						+ ": command faliled. error: " 
						+ (e != null ? e.getMessage() : "<unknown>")
						+ "; type: " 
						+ (e != null ? e.getClass() : "<unknown>")
				;
	}

	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.message.MessageFormatter#getId()
	 */
	@Override
	public String getId() {
		return ERROR_MESSAGE_FORMATTER;
	}

}
