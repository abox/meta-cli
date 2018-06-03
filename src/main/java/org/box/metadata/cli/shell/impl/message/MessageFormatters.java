package org.box.metadata.cli.shell.impl.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a collection of message formatters 
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 *
 */
public class MessageFormatters {

	private Map<String, MessageFormatter> map = new HashMap<String, MessageFormatter>();

	/**
	 * Sets the given formatter by ID to a list of registered formatters
	 * 
	 * @param formatter - a formatter instance to set
	 * 
	 * @return MessageFormatters collection itself
	 */
	public MessageFormatters setMessageFormatter(MessageFormatter formatter) {

		map.put(formatter.getId(), formatter);
		return this;
	}

	/**
	 * Returns registered formatter from the map of formatters by the formatter's Id
	 * 
	 * @param formatterId - unique identifier of the formatter
	 *  
	 * @return an instance of the registered fromatter
	 */
	public MessageFormatter get(String formatterId) {
		return map.get(formatterId);
	}

}
