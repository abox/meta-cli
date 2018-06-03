package org.box.metadata.cli.shell;

import org.box.metadata.cli.annotation.Command;

public interface CommandHandlerListener {

	void onHandlerStart(CommandHandler<?> handler, Command c, Object cfg);

	void onHandlerSuccess(CommandHandler<?> handler, Command c, Object cfg);

	void onHandlerFailure(CommandHandler<?> handler, Command c, Object cfg);

}
