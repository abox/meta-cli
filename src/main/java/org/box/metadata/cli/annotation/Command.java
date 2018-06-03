package org.box.metadata.cli.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.box.metadata.cli.shell.CommandHandler;
import org.box.metadata.cli.shell.Shell;

/**
 * <p>
 * Marks a field as a command in the Command Line Configuration.
 * </p>
 * 
 * <p>
 * While a {@link CLIConfiguration} annotated class is a configuration 
 * for a whole Application, the {@link Command} annotated type of field is 
 * a sub-configuration for a particular application command and may 
 * include {@link Option}, {@link Argument} or {@link Arguments} 
 * annotated field members inside.
 * </p>
 * 
 * <p>
 * If, Application command has no arguments or options, 
 * the <b>java.lang.Object</b> can be used as a member type.
 * </p>
 * 
 * <p> 
 * As a main configuration, a command configuration class also requires
 * <b>public default constructor</b> for Reflection initialization. 
 * </p>
 * 
 *  
 * @see Option
 * @see Argument
 * @see Arguments
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Command {

	/**
	 * A word(s) that uses in the {@link Shell}'s prompt line to execute the command.
	 * <br>
	 * <br>
	 * The space charters can be used inside. e.g.: "create db".
	 * <br>
	 * <br>
	 * 
	 * @return a name of the {@link Command}.
	 */
	String name();


	/**
	 * A brief description of the {@link Command}
	 * 
	 * @return a string
	 */
	String description();
	
	/**
	 * {@link Shell} specific only. 
	 * 
	 * @return a list of {@link CommandHandler}s
	 */
	Class<? extends CommandHandler<?>>[] handlers() default {};
	
}
