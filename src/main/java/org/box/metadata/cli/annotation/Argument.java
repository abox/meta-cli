package org.box.metadata.cli.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.box.metadata.cli.CommandLineParser;
import org.box.metadata.cli.impl.MetaCommandLineParser;

/**
 * <p>
 * Marks fields as a Enumerated Command Line Argument for
 * {@link MetaCommandLineParser}. E.g.:
 * <p>
 * 
 * <pre>
 * shell>app.exe infile [outfile]
 * </pre>
 * 
 * <p>
 * where <code>infile</code> and <code>outfile</code> are enumerated Command Line Arguments
 * with <code>index</code>s equal 1 and 2 
 * 
 * @see CommandLineParser
 * @see MetaCommandLineParser
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Argument {

	int index();

	/**
	 * an Argument label that prints out during 'help' command or
	 * 'missing argument' warning if the argument is required
	 * 
	 * e.g.: 'infile' or 'outfile'
	 * 
	 * @return a label string.
	 */
	String name();

	boolean required() default false;

}
