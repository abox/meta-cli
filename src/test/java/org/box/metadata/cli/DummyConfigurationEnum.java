package org.box.metadata.cli;

import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.annotation.Option;

/**
 * A dummy configuration focused on Value Enumerated Options testing.
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */

@CLIConfiguration(version="0", name="dummy")
public class DummyConfigurationEnum {
	
	public static enum OptionEnum {
		value1,
		value2
	}

	@Option(description = "Enum key/value option. Possible values are: value1, value2", 
			fullName = "keyValue1", 
			shortName = "kv1",
			hasArguments = true)
	public OptionEnum keyValue1;
	
}
