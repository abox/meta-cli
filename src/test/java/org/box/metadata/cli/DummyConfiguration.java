package org.box.metadata.cli;

import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.annotation.Arguments;
import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.annotation.Option;

/**
 * A dummy implementation of the configuration for tests' purpose.
 * 
 * @see DummyConfigurationCommands for a more complex example.
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */

@CLIConfiguration(version="0", name = "dummy")
public class DummyConfiguration {

	@Option(description = "Simple yes/no option without value", 
			fullName = "key1", 
			shortName = "k1")
	public boolean key1;
	
	@Option(description = "Simple private yes/no option without agruments", 
			fullName = "key2", 
			shortName = "k2")
	private boolean key2;

	@Option(description = "Simple key/value option with agruments", 
			fullName = "keyValue1", 
			shortName = "kv1",
			hasArguments = true)
	public String keyValue1;
	
	@Argument(index = 1, name = "infile", required = true)
	public String infile;

	@Argument(index = 2, name = "oufile", required = false)
	public String oufile;
	
	@Arguments(syntax = "infile oufile keyword1 keyword2 ...")
	public String[] args;
	
	/**
	 * Getter for private Key2 option
	 * 
	 * @return a value of Key2 option 
	 */
	public boolean isKey2() {
		return key2;
	}

	/**
	 * Setter for private Key2 option
	 * 
	 * @param key2 - the private option value
	 */
	public void setKey2(boolean key2) {
		this.key2 = key2;
	}
	
}
