package org.box.metadata.cli.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.box.metadata.cli.impl.MetaCommandLineParser;
import org.box.metadata.cli.shell.Shell;
import org.box.metadata.cli.shell.impl.ConsoleShell;

/**
 * <p>
 * Marks classes as a Command Line Configuration for
 * {@link MetaCommandLineParser} or {@link ConsoleShell}.
 * </p>
 * 
 * <p>
 * Annotation's target is a class. Java Reflection is used 
 * to create a new instance of the application configuration 
 * so <b> public default constructor</b> is required.
 * </p>
 * 
 * <p> 
 * After creation of the configuration instance, the framework
 * injects it member fields annotated with {@link Option}, 
 * {@link Argument}, {@link Arguments} or {@link Command} to 
 * values obtained from the Application's Command Line arguments.
 * </p>
 * 
 * <pre>
 * &#064;CLIConfiguration(version="0", name="myApp")
 * public class AppConfiguration {
 * 
 * 	&#064;Option(description = &quot;Simple private yes/no option without agruments&quot;, fullName = &quot;key1&quot;, shortName = &quot;k1&quot;)
 * 	private boolean key1;
 * 
 * 	&#064;Option(description = &quot;Simple key/value option with agruments&quot;, fullName = &quot;keyValue1&quot;, shortName = &quot;kv1&quot;, hasArguments = true)
 * 	public String keyValue1;
 * 
 * 	&#064;Argument(index = 1, name = &quot;infile&quot;, required = true)
 * 	public String infile;
 * 
 * }
 * </pre>
 * 
 * @see MetaCommandLineParser
 * @see ConsoleShell
 * 
 * @see Option
 * @see Argument
 * @see Arguments
 * @see Command
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CLIConfiguration {
	
	/**
	 * Predefined help {@link Option} short name.
	 * 
	 * @return the short name of the help {@link Option}.
	 */
	String helpShortName() default "h";
	
	/**
	 * Predefined help {@link Option} regular name.
	 * 
	 * @return the name of the help {@link Option}.
	 */
	String helpFullName() default "help";
	
	/**
	 * Predefined help {@link Option} description.
	 * 
	 * @return the description of the help {@link Option}.
	 */
	String helpDescription() default "print this help";
	
	/**
	 * Predefined version {@link Option} name.
	 * 
	 * @return the short name of the version {@link Option}.
	 */
	String versionShortName() default "v";
	
	/**
	 * Predefined version {@link Option} regular name.
	 * 
	 * @return the name of the version {@link Option}.
	 */
	String versionFullName() default "version";

	/**
	 * The version of application to printout.
	 * 
	 * @return the version as a string.
	 */
	String version();
	
	/**
	 * The name of an application to print out. 
	 * 
	 * <br><br>Set the name to the empty or null, if you want to 
	 * make all {@link Command}s are being embedded into 
	 * the {@link Shell}. (like 'exit' or 'help')
	 * 
	 * @return the name as a string.
	 */
	String name();

}
