package org.box.metadata.cli.shell.impl;

import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.annotation.Command;
import org.box.metadata.cli.impl.MetaCommandLineParser;
import org.box.metadata.cli.shell.CommandHandler;
import org.box.metadata.cli.shell.Shell;
import org.box.metadata.cli.shell.exception.TerminatedException;

/**
 * TODO:
 * 
 * @see Shell
 * @see MetaCommandLineParser
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 *
 */
@CLIConfiguration(version="0", name = "") // 'set' command is embedded since cfg name is empty
public class ShellTestCfgs2 {

	/**
	 * Represents Shell command 'set' which pre-sets Command Line options into
	 * a User specified value allowing to include those options implicitly
	 * 
	 */
	public static class SetCommand {

		@Argument(index = 1, name = "<option>=(<value>|on|off)", required = true)
		private String property;

		/**
		 * Returns a property name to set.
		 * 
		 * @return a name of the property as string
		 */
		public String getPropertyName() {
			String[] keyVal = property.split("=");
			return keyVal.length > 0 ? keyVal[0] : null;
		}

		/**
		 * Returns a property value to set. It returns a Boolean value, if
		 * property's value one of 'on' or 'off' and a String object otherwise.
		 * 
		 * @return a value of the property as Object
		 */
		public Object getPropertyValue() {
			String[] keyVal = property.split("=");
			if (keyVal.length > 1) {
				if ("on".equalsIgnoreCase(keyVal[1])) {
					return true;
				} else if ("off".equalsIgnoreCase(keyVal[1])) {
					return false;
				} else {
					return keyVal[1];
				}
			}

			return null;
		}
	}
	
	public static class SetCommandTestHandler implements CommandHandler<ShellTestCfgs2> {

		@Override
		public void validate(ShellTestCfgs2 cfg, Shell shell) {
			System.out.println("validation: name = " + cfg.set.getPropertyName() + "; value = " + cfg.set.getPropertyValue());
		}

		@Override
		public void process(ShellTestCfgs2 cfg, Shell shell) throws TerminatedException {
			System.out.println("validation: name = " + cfg.set.getPropertyName() + "; value = " + cfg.set.getPropertyValue());
		}
	}

	@Command(name = "set", 
		description = "set a property to the value.\n"
					+ "warning: no specing before and after equal sumbol('=')\n"
					+ "e.g.: 'a=b' or 'a=\"b1 b2\"'", 
			handlers = SetCommandTestHandler.class)
	SetCommand set;
}
