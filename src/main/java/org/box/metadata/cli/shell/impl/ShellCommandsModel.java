package org.box.metadata.cli.shell.impl;

import org.box.metadata.cli.impl.CommandCompile;
import org.box.metadata.cli.impl.DefaultConfigurationModel;
import org.box.metadata.cli.util.CLIUtil;

public class ShellCommandsModel extends DefaultConfigurationModel {

	@SuppressWarnings("rawtypes")
	public ShellCommandsModel(Class... cfgClasses) {
		for (Class c : cfgClasses) {
			CLIUtil.compileCommandsModel(this, c);
		}
	}

	@Override
	public void addCommand(CommandCompile commandCompile) {
		if (!(commandCompile instanceof ShellCommandCompile))
			throw new IllegalStateException();
		
		super.addCommand(commandCompile);
	}
	
	@Override
	public ShellCommandCompile findCommandForInput(String[] args) {
		return (ShellCommandCompile) super.findCommandForInput(args);
	}

}
