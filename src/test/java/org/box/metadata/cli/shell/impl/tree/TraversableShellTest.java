package org.box.metadata.cli.shell.impl.tree;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import org.box.metadata.cli.DummyConfigurationCommands;
import org.box.metadata.cli.impl.MetaCommandLineParser.ParserType;
import org.box.metadata.cli.shell.impl.ShellCommandsModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TraversableShellTest {

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
	public void test() {
		HashMap<String, Object[]> map = new HashMap<>();
		
		ShellCommandsModel m1 = new ShellCommandsModel(
				DummyConfigurationCommands.class) { 
					@Override public String toString() { return "m1";}
				};
		
		map.put("/", new Object[] { "bin", "usr"});
		map.put("/bin/", new Object[]{m1, "file1", "file2"});
		map.put("/usr/", new Object[]{"bin"});
		
		map.put("/usr/bin/", new Object[] {m1, "file3", "file4"});

		TraversableTestMapModel model = new TraversableTestMapModel(map);
		
		TraversableShell shell = new TraversableShell(
				ParserType.GNU_PARSER, 
				new ShellCommandsModel(TraversableShellConfiguration.class), 
				model);
		
		shell.runAndExit(new String[]{ "/bin/dummy", "create"});
		shell.runAndExit(new String[]{ "ls"});
		shell.runAndExit(new String[]{ "cd", "/bin/"});
		shell.runAndExit(new String[]{ "ls"});
		
		String expected = "validation: key = false; file = null\n"
 						+ "process: key = false; file = null\n"
						+ " bin usr\n"
						+ " m1 file1 file2\n";
		
		String[] splite = expected.split("\n");
		String[] splita = assertOut.toString().split("\n");
		assertEquals(splite.length, splita.length);
		assertEquals(splite[0].trim(), splita[0].trim());
		assertEquals(splite[1].trim(), splita[1].trim());
		assertEquals(splite[2].trim(), splita[2].trim());
		assertEquals(splite[3].trim(), splita[3].trim());

	}

}
