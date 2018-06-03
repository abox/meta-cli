package org.box.metadata.cli.shell.impl;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.box.metadata.cli.DummyConfigurationCommands;
import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.annotation.Command;
import org.box.metadata.cli.shell.CommandHandler;
import org.box.metadata.cli.shell.Shell;
import org.box.metadata.cli.shell.impl.ConsoleShell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * {@link ConsoleShell} main functionality coverage tests. 
 * 
 * @see Shell
 * @see CLIConfiguration
 * @see Command
 * @see CommandHandler
 * 
 * @author alexk
 *
 */
public class ConsoleShellTest {
	
	private PrintStream origOut;
	private ByteArrayOutputStream assertOut;

	@Before
	public void before() {
		origOut = System.out;
		assertOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(assertOut));
	}
	
	@After
	public void after() {
		System.setOut(origOut);
	}

	@Test
	public void handlerTest1() {
		
		ConsoleShell shell = new ConsoleShell(DummyConfigurationCommands.class);
		
		String[] args = new String[]{"dummy", "create", "-k2", "file.txt"};
		shell.runAndExit(args);
		
		String[] splite = "validation: key = true; file = file.txt\nprocess: key = true; file = file.txt\n".split("\n");
		String[] splita = assertOut.toString().split("\n");
		
		assertEquals(splite.length, splita.length);
		assertEquals(splite[0].trim(), splita[0].trim());
		assertEquals(splite[1].trim(), splita[1].trim());

	}
	
	@Test
	public void handlerTest2() {

		ConsoleShell shell = new ConsoleShell(ShellTestCfgs2.class);
		
		String[] args = new String[]{"set", "a=b"};
		shell.runAndExit(args);
		
		String[] splite = "validation: name = a; value = b\nvalidation: name = a; value = b\n".split("\n");
		String[] splita = assertOut.toString().split("\n");
		assertEquals(splite.length, splita.length);
		assertEquals(splite[0].trim(), splita[0].trim());
		assertEquals(splite[1].trim(), splita[1].trim());
		
	}
}
