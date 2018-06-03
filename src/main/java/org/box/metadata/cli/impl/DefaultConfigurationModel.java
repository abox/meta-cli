package org.box.metadata.cli.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.Options;
import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.shell.impl.ShellCommandsModel;
import org.box.metadata.cli.util.CLIUtil;

/**
 * <p>
 * This is a base implementation of {@link ConfigurationModel}.
 * It include mechanism to collect and compile meta-data information
 * from classes annotated by {@link CLIConfiguration} but also can 
 * be used as a raw model with meta-data obtained from a differ type
 * of source, e.g.: xml, properties, composite configurations
 * (see AbstractShell)
 * 
 * @see ShellCommandsModel
 * 
 * @author alexk
 *
 */
public class DefaultConfigurationModel implements ConfigurationModel {
	
	private static final Comparator<CommandCompile> COMMAND_COMPARATOR = 
			new Comparator<CommandCompile>(){

				@Override
				public int compare(CommandCompile o1, CommandCompile o2) {
					return o2.split.length - o1.split.length ;
				}
			};
	
	private final ArrayList<CommandCompile> commands = new ArrayList<CommandCompile>();
	private transient boolean _needResort = true;

	private Argument[] argsArray;

	private String rootArgumentsSyntax;

	private Options rootCLIOptions;

	public DefaultConfigurationModel() {
	}

	public DefaultConfigurationModel(Class<?> cfgClass) {
		CLIUtil.compileCommandsModel(this, cfgClass);
	}
	
	public DefaultConfigurationModel(ConfigurationModel copyFrom) {
		commands.addAll(copyFrom.getCommands());
		argsArray = copyFrom.getRootArguments();
		rootArgumentsSyntax = copyFrom.getRootArgumentsSyntax();
		rootCLIOptions = copyFrom.getRootCLIOptions();
	}

	@Override
	public List<CommandCompile> getCommands() {
		return Collections.unmodifiableList(commands);
	}

	public void addCommand(CommandCompile commandCompile) {
		commands.add(commandCompile);
		_needResort = true;
	}
	
	public void addCommands(List<? extends CommandCompile> append) {
		commands.addAll(append);
		_needResort = true;
	}

	@Override
	public CommandCompile findCommandForInput(String[] args) {
		
		if (_needResort)
			Collections.sort(commands, COMMAND_COMPARATOR);
		
		for (CommandCompile _cc : commands) {
//			if (_cc.split.length > args.length)
//				break;
			
			if (CLIUtil.arrayStartsWith(args, _cc.split)) {
				return _cc;
			}
		}
		
		return null;
	}

	@Override
	public Options getRootCLIOptions() {
		return rootCLIOptions;
	}
	
	public void setRootCLIOptions(Options rootCLIOptions) {
		this.rootCLIOptions = rootCLIOptions;
	}

	@Override
	public Argument[] getRootArguments() {
		return argsArray;
	}

	public void setRootArguments(Argument[] argsArray) {
		this.argsArray = argsArray;
	}
	
	@Override
	public String getRootArgumentsSyntax() {
		return this.rootArgumentsSyntax;
	}
	
	public void setRootArgumentsSyntax(String rootArgumentsSyntax) {
		this.rootArgumentsSyntax = rootArgumentsSyntax;
	}

	public void clearCommands() {
		commands.clear();
	}

}
