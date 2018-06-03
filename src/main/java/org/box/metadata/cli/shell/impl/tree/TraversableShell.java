package org.box.metadata.cli.shell.impl.tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.box.metadata.cli.impl.ConfigurationModel;
import org.box.metadata.cli.impl.DefaultConfigurationModel;
import org.box.metadata.cli.impl.MetaCommandLineParser.ParserType;
import org.box.metadata.cli.shell.impl.AbstractShell;
import org.box.metadata.cli.shell.impl.ShellCommandsModel;
import org.box.metadata.cli.util.StrTokenizer;

public class TraversableShell extends AbstractShell {
	
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	private final TraversableTreeModel treeModel;

	private final ConfigurationModel embeddedCommandsModel;

	public TraversableShell(ParserType parserType, 
			ConfigurationModel embeddedCommandsModel, 
			TraversableTreeModel treeModel) {
		super(parserType, new DefaultConfigurationModel(embeddedCommandsModel));
		this.treeModel = treeModel;
		this.embeddedCommandsModel = embeddedCommandsModel;
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.cli.shell.Shell#appendMessage(java.lang.String)
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

	/*
	 * (non-Javadoc)
	 * @see org.box.metadata.cli.shell.Shell#exit()
	 */
	@Override
	public void exit() {
		this.doExit = true;
		// TODO: interrupt 'System.in' waiting
	}
	
	@Override
	public void runAndExit(String[] args) {
		super.runAndExit(processCommandPath(args));
	}

	@Override
	protected String[] getUserInput() {
		System.out.print(treeModel.currentFolder() + ">");
		try {
			StrTokenizer tokenizer;
			String lines = "";
			ArrayList<String> args = new ArrayList<>(); 
			do {
				lines = lines.length() > 0 ? lines + "\n" + br.readLine() : br.readLine();
				tokenizer = new StrTokenizer(lines.replace('\t', ' '), ' ');
				tokenizer.setUnescaping(StrTokenizer.UNESCAPING_ALL); // slash ('/') cannot be escaped
				 													  // so, remove all escapes
				args.clear();
				String element = null;
				while ((element = tokenizer.nextToken()) != null) 
					args.add(element);
			
				if (tokenizer.isStringIncomplete()) 
					System.out.print(">");
				if (args.size() == 0)
					System.out.print(treeModel.currentFolder() + ">");
			} while (args.size() == 0 || tokenizer.isStringIncomplete());
			
			return processCommandPath(args.toArray(new String[args.size()]));
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	protected String[] processCommandPath(String[] args) {
		DefaultConfigurationModel model = (DefaultConfigurationModel) getModel();
		String first = args[0];
		
		if (first.indexOf('/') != -1) {
			// has a path
			StrTokenizer subtokenizer = new StrTokenizer(first, '/');
			subtokenizer.setUnescaping(StrTokenizer.UNESCAPING_OFF); 

			ArrayList<String> pathpart = new ArrayList<>();
			String element = null;
			while ((element = subtokenizer.nextToken()) != null) 
				pathpart.add(element);
			

			String path = first.charAt(0) == '/' ? "/" : " ";
			for (int i = 0; i < pathpart.size() - 1; i++) {
				path += pathpart.get(i) + "/";
			}
			
			model.clearCommands();
			Object[] list = treeModel.list(path);
			for (Object o : list)
				if (o instanceof ConfigurationModel) {
					// could be improved to check the last pathpart element
					// is equal to ConfigurationModel's name
					if (o instanceof ShellCommandsModel) {
						model.addCommands(((ConfigurationModel) o).getCommands());
					} else
						throw new UnsupportedOperationException(
								"ConfigurationModel is not ShellCommandsModel. Only ShellCommandsModel is currently supported");
				}
			args[0] = pathpart.get(pathpart.size() -1);
			
		} else {
			model.clearCommands();
			model.addCommands(embeddedCommandsModel.getCommands());
		}
		return args;
	}
	
	public TraversableTreeModel getTreeModel() {
		return treeModel;
	}
	
}
