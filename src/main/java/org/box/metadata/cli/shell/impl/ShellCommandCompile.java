package org.box.metadata.cli.shell.impl;

import java.lang.reflect.Field;

import org.apache.commons.cli.Options;
import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.annotation.Command;
import org.box.metadata.cli.impl.CommandCompile;


public class ShellCommandCompile extends CommandCompile {

	private final String appName;
	private final Class<?> cfgClass;

	public ShellCommandCompile(
			Command orig, 
			Field field, 
			Options cliOptions, 
			Argument[] arguments, 
			String argumentsSyntax,
			String appName,
			Class<?> cfgClass) {
		
		super(orig, field, cliOptions, arguments, argumentsSyntax);
		this.appName = appName;
		this.cfgClass = cfgClass;
		
		if (appName != null && !appName.trim().isEmpty())
			split = (appName + " " + orig.name()).trim().split("\\s+");
	}
	
	public Class<?> getCfgClass() { 
		return cfgClass; 
	}

	public String getAppName() {
		return appName;
	}
}
