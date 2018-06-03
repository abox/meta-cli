package org.box.metadata.cli.shell.impl.message;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.box.metadata.cli.impl.CommandCompile;
import org.box.metadata.cli.impl.ConfigurationModel;
import org.box.metadata.cli.util.HelpFormatter;

public class HelpMessageFormatter implements MessageFormatter {

	public static final String HELP_MESSAGE_FORMATTER = "HelpMessageFormatter";

	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.message.ShellMessage#getMessage()
	 */
	public String format(CommandCompile command, ConfigurationModel model, Throwable e, Object... args) {
		StringWriter writer = new StringWriter();

		if (e != null)
			writer.append(e.getMessage() + "\n");
		HelpFormatter formatter = new HelpFormatter(model, command);
		formatter.printHelp(new PrintWriter(writer));

		return writer.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.message.MessageFormatter#getId()
	 */
	public String getId() {
		return HELP_MESSAGE_FORMATTER;
	}
}
