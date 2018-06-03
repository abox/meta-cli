package org.box.metadata.cli.shell.impl;

import org.box.metadata.cli.DummyConfigurationCommands;
import org.box.metadata.cli.shell.CommandHandler;
import org.box.metadata.cli.shell.Shell;
import org.box.metadata.cli.shell.exception.TerminatedException;

public class DymmyCreateHandler implements CommandHandler<DummyConfigurationCommands> {

	public void validate(DummyConfigurationCommands cfg, Shell shell) {
		System.out.println("validation: key = " + cfg.create.key2 + "; file = " + cfg.create.infile);
	}

	public void process(DummyConfigurationCommands cfg, Shell shell) throws TerminatedException {
		System.out.println("process: key = " + cfg.create.key2 + "; file = " + cfg.create.infile);
	}

}
