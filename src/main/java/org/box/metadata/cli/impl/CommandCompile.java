package org.box.metadata.cli.impl;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.cli.Options;
import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.annotation.Command;

public class CommandCompile{
	
	private final Command orig;
	private final Field field;
	
	private final Options cliOptions;
	private final Argument[] arguments;
	private final String argumentsSyntax;
	
	protected String[] split;

	public CommandCompile(
			Command orig, 
			Field field, 
			Options cliOptions, 
			Argument[] arguments, 
			String argumentsSyntax) {
		
		this.orig = orig;
		this.field = field;
		this.cliOptions = cliOptions;
		this.arguments = arguments;
		this.argumentsSyntax = argumentsSyntax;
		
		split = orig.name().trim().split("\\s+");
	}

	public Field getFeild() {
		return field;
	}

	public int complexity() {
		return split.length;
	}
	
	public Command getOrig() {
		return orig;
	}

	public Options getCLIOptions() {
		return cliOptions;
	}
	
	public Argument[] getArguments() {
		return arguments;
	}
	
	public String getArgumentsSyntax() {
		return argumentsSyntax;
	}
	
	public String getFullAndShortName() {
		return orig.name();
	}

	@Override
	public String toString() {
		return "CommandCompile [orig=" + getFullAndShortName() + ", cliOptions=" + cliOptions
				+ ", arguments=" + Arrays.toString(arguments)
				+ ", argumentsSyntax=" + argumentsSyntax + "]";
	}

}