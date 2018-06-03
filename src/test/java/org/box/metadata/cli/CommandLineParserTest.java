package org.box.metadata.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.box.metadata.cli.exception.InvalidConfigurationException;
import org.box.metadata.cli.exception.ParseException;
import org.box.metadata.cli.impl.MetaCommandLineParser;
import org.box.metadata.cli.impl.MetaCommandLineParser.ParserType;
import org.junit.Test;

/**
 * {@link MetaCommandLineParser} main functionality coverage tests. 
 * 
 * @see CommandLineParser
 * @see MetaCommandLineParser
 * @see CommandCompatible
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */
public class CommandLineParserTest {
	
	@Test
	public void noCommandTest() {

		String[] args = new String[] { "-k1", "-key2", "-kv1=test",
				"infile.txt", "outfile.txt", "keyword1" };
		try {
			
			DummyConfiguration cfg = MetaCli.parse(DummyConfiguration.class, args);

			assertTrue(cfg.key1);
			assertTrue(cfg.isKey2());
			assertEquals("test", cfg.keyValue1);
			assertEquals("infile.txt", cfg.infile);
			assertEquals("outfile.txt", cfg.oufile);
			assertArrayEquals(new String[] { "infile.txt", "outfile.txt",
					"keyword1" }, cfg.args);

		} catch (ParseException e) {
			fail(e.getMessage());
		} catch (InvalidConfigurationException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void commandsTest() {

		try {
			String[] args = new String[] { "create", "-k2", "-eof", "file.txt" };

			DummyConfigurationCommands cfg = MetaCli.parse(
					DummyConfigurationCommands.class, args, ParserType.GNU_PARSER);

			assertTrue(cfg.create.key2);
			assertTrue(cfg.exitOnFinifh);
			assertEquals("file.txt", cfg.create.infile);
			assertNull(cfg.delete);

		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void enumOptionTest() {
		// success
		try {
			String[] args = new String[] { "-kv1=value1"};

			DummyConfigurationEnum cfg = MetaCli.parse(
					DummyConfigurationEnum.class, args);

			assertNotNull(cfg.keyValue1);
			assertEquals(DummyConfigurationEnum.OptionEnum.value1, cfg.keyValue1);

		} catch (ParseException e) {
			fail(e.getMessage());
		}
		
		// failure
		try {
			String[] args = new String[] { "-kv1=value3"};

			MetaCli.parse(DummyConfigurationEnum.class, args);

			fail("No validation exeption");
		} catch (ParseException e) {
		}

	}
}
