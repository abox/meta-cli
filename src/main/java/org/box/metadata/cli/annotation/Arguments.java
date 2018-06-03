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
 * Marks field as the non-Enumerated Command Line Arguments for
 * {@link MetaCommandLineParser} and can be used for an array of string fields .
 * E.g.:
 * <p>
 * 
 * <pre>
 * shell>app.exe keyword1 keyword2 ...
 * </pre>
 * 
 * <p>
 * This annotation typically uses for non-defined number of arguments. The code
 * snippet of Application's configuration class:
 * </p>
 * 
 * <pre>
 * &#064;Arguments(syntax = &quot;keyword1 keyword2 ...&quot;)
 * public String[] args;
 * 
 * </pre>
 * 
 * @see CommandLineParser
 * @see MetaCommandLineParser
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Arguments {

	String syntax();
	
	int minoccurs() default 0;

}
