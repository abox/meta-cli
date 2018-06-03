package org.box.metadata.cli.shell.impl.message;

import org.box.metadata.cli.impl.CommandCompile;
import org.box.metadata.cli.impl.ConfigurationModel;

public class InfoMessageFormatter implements MessageFormatter {

	public static final String INFO_MESSAGE_FORMATTER = "InfoMessageFormatter";

	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.message.MessageFormatter#format(org.box.metadata.shell.Application, java.lang.Object[])
	 */
	public String format(CommandCompile command, ConfigurationModel model, Throwable e, Object... args) {
		
		StringBuilder sb = new StringBuilder();
		for(Object msg : args) {
			sb.append(msg);
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.message.MessageFormatter#getId()
	 */
	public String getId() {
		return INFO_MESSAGE_FORMATTER;
	}

}
