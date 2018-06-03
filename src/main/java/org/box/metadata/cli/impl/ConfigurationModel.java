package org.box.metadata.cli.impl;

import java.util.List;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.box.metadata.cli.annotation.Argument;

/**
 * <p>
 * A common representation of Application Configuration. 
 * A list of methods used by {@link CommandLineParser} implementations
 * to access to Application Configuration meta-data whatever the source of
 * meta-data is.
 * </p>
 * 
 * @see DefaultConfigurationModel
 * 
 * @author alexk
 *
 */
public interface ConfigurationModel {

	List<CommandCompile> getCommands();

	CommandCompile findCommandForInput(String[] args);

	Options getRootCLIOptions();
	
	Argument[] getRootArguments();

	String getRootArgumentsSyntax();


}
