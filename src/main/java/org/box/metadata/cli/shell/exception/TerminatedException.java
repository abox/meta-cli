package org.box.metadata.cli.shell.exception;

import org.box.metadata.cli.annotation.Command;
import org.box.metadata.cli.shell.CommandHandler;
import org.box.metadata.cli.shell.Shell;
import org.box.metadata.cli.shell.impl.ConsoleShell;

/**
 * <p>
 * Checkable exceptions that occurs on a {@link Command} execution phase
 * during {@link CommandHandler#process(Object, Shell)} method.
 * </p>
 * 
 * <p>
 * An instance of {@link TerminatedException} informs {@link ConsoleShell}
 * about abnormal {@link CommandHandler} execution.
 * </p>
 * 
 * @see CommandHandler
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 *
 */
public class TerminatedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TerminatedException(Throwable e) {
		super(e);
	}

}
