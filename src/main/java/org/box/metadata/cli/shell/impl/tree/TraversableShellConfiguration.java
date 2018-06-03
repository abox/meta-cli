package org.box.metadata.cli.shell.impl.tree;

import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.annotation.Arguments;
import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.annotation.Command;
import org.box.metadata.cli.exception.ParseException;
import org.box.metadata.cli.shell.CommandHandler;
import org.box.metadata.cli.shell.Shell;
import org.box.metadata.cli.shell.exception.TerminatedException;
import org.box.metadata.cli.shell.impl.ExitCommand;
import org.box.metadata.cli.shell.impl.HelpCommand;

@CLIConfiguration(version="1", name="") // empty name - embedded commands
public class TraversableShellConfiguration {
	
	public static class ListCommand implements CommandHandler<TraversableShellConfiguration> {

		@Arguments(syntax = " [FILE]...")
		String[] files;

		@Override
		public void validate(TraversableShellConfiguration cfg, Shell shell) 
				throws ParseException {
			if (cfg.ls == null)
				throw new IllegalStateException("cfg.ls == null");
			
			TraversableShell tshell = (TraversableShell) shell;
			TraversableTreeModel tmodel = tshell.getTreeModel();
			if (cfg.ls.files != null && cfg.ls.files.length > 0) {
				for (int i = 0; i < cfg.ls.files.length; i++) {
					if (!tmodel.fileExists(cfg.ls.files[i]))
						throw new ParseException(
								String.format("ls: cannot access %s: No such file or directory", cfg.ls.files[i]));
				}
			}
		}

		@Override
		public void process(TraversableShellConfiguration cfg, Shell shell)
				throws TerminatedException {

			TraversableShell tshell = (TraversableShell) shell;
			TraversableTreeModel tmodel = tshell.getTreeModel();
			if (cfg.ls.files != null) {
				if (cfg.ls.files.length > 1) {
					for (int fi = 0; fi < cfg.ls.files.length; fi++) {
						tshell.appendMessage(cfg.ls.files[fi] + ":");
						Object[] list = tmodel.list(cfg.ls.files[0]);
						for (int oi = 0; oi < list.length; oi++) {
							tshell.appendMessageNoBr(" " + list[oi]);
						}
						tshell.appendMessage("");
					}
				} else {
					String path = cfg.ls.files.length > 0
							? cfg.ls.files[0] : "./";
					Object[] list = tmodel.list(path);
					for (int oi = 0; oi < list.length; oi++) {
						tshell.appendMessageNoBr(" " + list[oi]);
					}
					tshell.appendMessage("");
				}
			}

		}
	}
	
	public static class ChangeDirCommand implements CommandHandler<TraversableShellConfiguration> {

		@Argument(name = "dir", index = 1, required = true)
		String dir;

		@Override
		public void validate(TraversableShellConfiguration cfg, Shell shell) 
				throws ParseException {
			if (cfg.cd == null || cfg.cd.dir == null)
				throw new IllegalStateException("cfg.cd == null || cfg.cd.dir == null");
			
			TraversableShell tshell = (TraversableShell) shell;
			TraversableTreeModel tmodel = tshell.getTreeModel();
			if (!tmodel.isFolder(cfg.cd.dir))
				throw new ParseException(
						String.format("cd: %s: No such directory", cfg.cd.dir));
		}

		@Override
		public void process(TraversableShellConfiguration cfg, Shell shell)
				throws TerminatedException {

			if (cfg.cd == null || cfg.cd.dir == null)
				throw new IllegalStateException("cfg.cd == null || cfg.cd.dir == null");

			TraversableShell tshell = (TraversableShell) shell;
			TraversableTreeModel tmodel = tshell.getTreeModel();
			
			tmodel.setCurrentFolder(cfg.cd.dir);
		}
	}

	@Command(name = "ls", 
			description = "List information about the FILEs (the current directory by default).",
			handlers = {ListCommand.class})
	ListCommand ls;
	
	@Command(name = "cd", 
			description = "Change the shell working directory.",
			handlers = {ChangeDirCommand.class})
	ChangeDirCommand cd;
	
	@Command(name = "exit", description = "Exit from the shell", 
			handlers = ExitCommand.class)
	ExitCommand exitCommand;
	
	@Command(name = "help", description = "Print this help or help for <command>", 
			handlers = HelpCommand.class)
	HelpCommand helpCommand;

}
