package org.box.metadata.cli.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.box.metadata.cli.exception.ParseException;
import org.box.metadata.cli.impl.MetaCommandLineParser;

/** 
 * Annotation representation of {@link org.apache.commons.cli.Option}
 * 
 * <p>
 * Describes a single command-line option.  It maintains
 * information regarding to the short-name of the option, the long-name,
 * if any exists, a flag indicating an argument is required for
 * this option and a self-documenting description of the option.</p>
 * </p>
 * 
 * <p>
 * The target of annotation is class field. The following list of field 
 * types are supported for such fields:
 * <li> <code>boolean</code>/<code>{@link Boolean}</code> - valid for both modes
 * 		(<code>hasArguments</code> == <code>false</code> or <code>true</code>)
 * <li> <code>{@link String}</code> - the option has common argument
 * 		(<code>hasArguments</code> == <code>true</code>).
 * 		No auto casting is applied for String type arguments. 
 * <li> <code>int/{@link Integer}</code> - the option has integer argument
 * 		(<code>hasArguments</code> == <code>true</code>)
 * <li> <code>float/{@link Float}</code> - the option has float argument
 * 		(<code>hasArguments</code> == <code>true</code>)
 * </p>
 * <br>
 * <p>
 * Auto casting is applied for integer, boolean (<code>hasArguments</code> == true) 
 * and float type arguments. The {@link ParseException} throws to User if
 * a value cannot be casted to the appropriate type.
 * </p>
 * 
 * @see org.apache.commons.cli.Option
 * @see MetaCommandLineParser
 *
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Option {

	String shortName();
	
	String fullName();
	
	boolean hasArguments() default false;
	
	String description();
}
