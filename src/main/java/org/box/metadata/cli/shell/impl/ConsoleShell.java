package org.box.metadata.cli.shell.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.box.metadata.cli.impl.MetaCommandLineParser.ParserType;
import org.box.metadata.cli.shell.Shell;
import org.box.metadata.cli.util.CLIUtil;

/**
 * <br>Provides Shell implementation for std. IO.
 * <br>Two simple commands embedded in the {@link DefaultConsoleConfiguration}:
 * 
 * <li> {@link HelpCommand} - Prints a list of available commands
 * <li> {@link ExitCommand} - Performs an exit from the shell
 *
 * @see Shell
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */
public class ConsoleShell extends AbstractShell {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public ConsoleShell(Class<?>... cfgClass) {
		super(ParserType.GNU_PARSER, 
				CLIUtil.appendCfg(cfgClass, DefaultConsoleConfiguration.class));
	}

	public ConsoleShell(ParserType parserType, Class<?>... cfgClass) {
		super(parserType, 
				CLIUtil.appendCfg(cfgClass, DefaultConsoleConfiguration.class));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.box.metadata.shell.AbstractShell#getUserInput()
	 */
	@Override
	protected String[] getUserInput() {
		System.out.print(">");
		try {
			return CLIUtil.smartSplit(br.readLine());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void exit() {
		doExit = true;
		//TODO: interrupt 'System.in' waiting
	}

	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.shell.Shell#displayMessage(org.box.metadata.shell.message.ShellMessage)
	 */
	@Override
	public void appendMessage(String msg) {
		System.out.println(msg);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.cli.shell.Shell#appendMessageNoBr(java.lang.String)
	 */
	@Override
	public void appendMessageNoBr(String msg) {
		System.out.print(msg);
	}
}
