package org.box.metadata.cli.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.cli.Options;
import org.box.metadata.cli.annotation.Argument;
import org.box.metadata.cli.annotation.Arguments;
import org.box.metadata.cli.annotation.CLIConfiguration;
import org.box.metadata.cli.annotation.Command;
import org.box.metadata.cli.annotation.Option;
import org.box.metadata.cli.exception.InvalidConfigurationException;
import org.box.metadata.cli.exception.ParseException;
import org.box.metadata.cli.impl.CommandCompile;
import org.box.metadata.cli.impl.DefaultConfigurationModel;
import org.box.metadata.cli.shell.impl.ShellCommandCompile;
import org.box.metadata.cli.shell.impl.ShellCommandsModel;

/**
 * A utility class
 * 
 * @author <a href="mailto:alexander.box@gmail.com">Alexander Korobka</a>
 *
 */
public class CLIUtil {

	private static final String ARGUMENT_FIELD_TYPE_INVALID = "@Argument Field '%s' has an incompatible field type. Valid is one of [%s] or enum.";
	private static final String ARGUMENTS_FIELD_TYPE_INVALID = "@Arguments Field '%s' has an incompatible field type. Valid 'String[]' is allowed.";
	private static final String OPTION_FIELD_TYPE_INVALID = "@Option Field '%s' has an incompatible field type. Valid is one of [%s] or enum.";
	private static final String OPTION_FIELD_TYPE_INVALID_BOOL = "@Option Field '%s' has an incompatible field type. Valid is one of [boolean, Boolean].";
	
	private static final Pattern COMMAND_VALIDTION_PATTERN = Pattern.compile("^[a-z\\_\\.][a-z0-9\\_\\-\\.]*(\\s+[a-z_\\.][a-z0-9\\_\\-\\.]*)*$", 0x2);
	private static final Pattern OPTION_SHORNAME_VALIDTION_PATTERN = Pattern.compile("^[a-z\\_\\.][a-z0-9]*$", 0x2);
	private static final Pattern OPTION_LONGNAME_VALIDTION_PATTERN = Pattern.compile("^[a-z\\_\\.][a-z0-9\\_\\-\\.]+$", 0x2);
	
	@SuppressWarnings("rawtypes")
	private static final Class[] ARG_TYPES_ARRAY = {
		String.class, int.class, float.class, boolean.class,
		Integer.class, Float.class, Boolean.class};
	
	@SuppressWarnings("rawtypes")
	private static final List<Class> ARG_TYPES = Arrays.asList(ARG_TYPES_ARRAY);

	/**
	 * Creates a Options list from the given configuration classes;
	 * 
	 * @param cfgClass
	 *            - a class that represent a Application of Command configuration;
	 *
	 * @param options
	 * 			  - an Apache's options list to put in
	 * 
	 * @throws InvalidConfigurationException
	 *             if the given class is not compatible or valid as an
	 *             Application or {@link Command} configuration
	 */
	@SuppressWarnings("rawtypes")
	public static void createCLIOptions(org.apache.commons.cli.Options options, Class cfgClass) 
			throws InvalidConfigurationException {

		for (Field f : cfgClass.getDeclaredFields()) {
			// add options
			if (f.isAnnotationPresent(Option.class)) {
				Option o = f.getAnnotation(Option.class);

				if (!OPTION_SHORNAME_VALIDTION_PATTERN.matcher(o.shortName().trim()).matches())
					throw new InvalidConfigurationException(
							String.format("invalid shortName of options: %s; pattern: %s", 
									o.shortName(), OPTION_SHORNAME_VALIDTION_PATTERN.pattern()));
				
				if (!OPTION_LONGNAME_VALIDTION_PATTERN.matcher(o.fullName().trim()).matches())
					throw new InvalidConfigurationException(
							String.format("invalid fullName of options: %s; pattern: %s", 
									o.fullName(), OPTION_LONGNAME_VALIDTION_PATTERN.pattern()));
				
				if (!o.hasArguments()
						&& boolean.class != f.getType() 
						&& Boolean.class != f.getType()) {
					throw new InvalidConfigurationException(
							String.format(OPTION_FIELD_TYPE_INVALID_BOOL, 
									f.getName()));
				} else if (o.hasArguments() 
						&& ARG_TYPES.indexOf(f.getType()) == -1
						&& !f.getType().isEnum())
					throw new InvalidConfigurationException(
							String.format(OPTION_FIELD_TYPE_INVALID, 
									f.getName(), Arrays.toString(ARG_TYPES_ARRAY)));

				options.addOption(new org.apache.commons.cli.Option(o
						.shortName(), o.fullName(), o.hasArguments(), o
						.description()));
			}
		}
	}
	
	/**
	 * Collects a list of {@link Argument}s from the given configuration classes;
	 * 
	 * @param cfgClazz
	 *            - an array of application's configuration classes;
	 * 
	 * @return a array of {@link Argument}s
	 * 
	 */
	public static Argument[] collectArguments(Class<?> cfgClass)
			throws InvalidConfigurationException {

		ArrayList<Argument> args = new ArrayList<>();

		for (Field f : cfgClass.getDeclaredFields()) {
			if (f.isAnnotationPresent(Argument.class)) {
				Argument a = f.getAnnotation(Argument.class);
				if (ARG_TYPES.indexOf(f.getType()) == -1
						&& !f.getType().isEnum())
					throw new InvalidConfigurationException(
							String.format(ARGUMENT_FIELD_TYPE_INVALID, 
									f.getName(), Arrays.toString(ARG_TYPES_ARRAY)));
				args.add(a);
			}
		}

		Collections.sort(args, new Comparator<Argument>() {

			@Override
			public int compare(Argument o1, Argument o2) {
				return new Integer(o1.index()).compareTo(o2.index());
			}
		});

		return args.toArray(new Argument[args.size()]);
	}

	
	public static String findArgumentsSyntax(Class<?> cfgClass) {
		for (Field f : cfgClass.getDeclaredFields()) {
			if (f.isAnnotationPresent(Arguments.class)) {
				if (!f.getType().isArray() ||
						f.getType().getComponentType() != String.class)
					throw new InvalidConfigurationException(
							String.format(ARGUMENTS_FIELD_TYPE_INVALID, f.getName()));
				return  f.getAnnotation(Arguments.class).syntax();
			}
		}
		return null;
	}

	
	/**
	 * Creates a list of compiled commands from the given class and
	 * puts them into the model.
	 * 
	 * @param model
	 * 			- a model to compile in
	 * 
	 * @param cfgClazz 
	 * 			- the class to process
	 * 
	 * @throws InvalidConfigurationException 
	 * 			- if cfgClass is not a {@link CLIConfiguration} class;
	 * 			- if an option name or type is not meet the internal standard;
	 * 			- if an argument type is not meet the internal standard;
	 * 			- if a command name is not meet the internal standard;
	 * 
	 * @return a set of compiled commands in the given class
	 */
	public static void compileCommandsModel(DefaultConfigurationModel model, Class<?> cfgClass) 
			throws InvalidConfigurationException {

		
		if (!cfgClass.isAnnotationPresent(CLIConfiguration.class)) {
			throw new InvalidConfigurationException(
					"Invalid configuration class ("+cfgClass+"). Configuration class shell be annotated by 'CLIConfiguration'");
		}
		
		CLIConfiguration a = cfgClass.getAnnotation(CLIConfiguration.class);
		
		boolean shell = model instanceof ShellCommandsModel;

		for (Field f : cfgClass.getDeclaredFields()) {
			if (f.isAnnotationPresent(Command.class)) {
				Command cmd = f.getAnnotation(Command.class);
				
				String cmdName = cmd.name().trim();
				boolean defaultCommand = "".equals(cmdName);
				if (defaultCommand && "".equals(a.name().trim()))
					throw new InvalidConfigurationException("default command (command name == \"\") cannot be embedded (app name ==\"\")");
				if (defaultCommand || COMMAND_VALIDTION_PATTERN.matcher(cmdName).matches()) {
					org.apache.commons.cli.Options options = new Options();
					createCLIOptions(options, cfgClass);
					createCLIOptions(options, f.getType()); // command options overrides app. options
					if (shell) {
						ShellCommandCompile scc = new ShellCommandCompile(cmd, f, 
								options, 
								collectArguments(f.getType()),
								findArgumentsSyntax(f.getType()), 
								a.name(), cfgClass);
						model.addCommand(scc);
					} else {
						CommandCompile cc = new CommandCompile(cmd, f, 
								options, 
								collectArguments(f.getType()),
								findArgumentsSyntax(f.getType()));
						model.addCommand(cc);
					}
				} else
					throw new InvalidConfigurationException(
							"inappropriate command format: " + cmdName 
							+ "; allowed is: " + COMMAND_VALIDTION_PATTERN.pattern());
			}
		}

		org.apache.commons.cli.Options options = new Options();
		createCLIOptions(options, cfgClass);
		
		model.setRootCLIOptions(options);
		model.setRootArgumentsSyntax(findArgumentsSyntax(cfgClass));
		model.setRootArguments(collectArguments(cfgClass));
	}
	
	/**
	 * Checks that <code>array</code> starts with <code>subArray</code>. 
	 * 
	 * @param array 
	 * 			- an array to check in
	 * @param subArray 
	 * 			- an array to check as sub array of <code>array</code>
	 * 
	 * @return true, if <code>subArray</code> starts with <code>array</code>
	 */
	public static boolean arrayStartsWith(String[] array, String[] subArray) {
		if (subArray.length > array.length) {
			return false;
		}

		for (int i = 0; i < subArray.length; i++) {
			if (!subArray[i].equalsIgnoreCase(array[i])) {
				return false;
			}
		}

		return true;
	}

	public static Object findEnumConst(Class<?> type, String optionValue) {
		for (Object c : type.getEnumConstants()){
			Enum<?> e = (Enum<?>) c;
			if(e.name().equalsIgnoreCase(optionValue)){
				return c;
			};
		}
		return null;
	}
	
	/**
	 * A hack for the delegated parser. Excludes string(s) that represents
	 * commands from the list of arguments.
	 * 
	 * @param command
	 *            - a command that must be excluded from the args
	 * @param args
	 *            - the given args to an exclude.
	 * 
	 * @return a new list of args with no command within
	 */
	public static String[] excludeCommandName(CommandCompile command, String[] args) {
		if (command == null) {
			return args;
		}
		
		int length = command.complexity();
		
		String[] result = new String[args.length - length];
		System.arraycopy(args, length, result, 0, result.length);
		return result;
	}

	
	public static Object autoCast(
			Field f, String value, String option, String argument) 
					throws ParseException {
		
		if (f.getType().isEnum()) {
			Object val = CLIUtil.findEnumConst(f.getType(), value);
			if (val == null) {
				if (option != null)
					throw new ParseException(
							String.format("Option '%s' expected one of the: [%s] but was: '%s'", 
									option, 
									Arrays.toString(f.getType().getEnumConstants()), 
									value));
				else if (argument != null)
					throw new ParseException(
							String.format("Argument '%s' expected one of the: [%s] but was: '%s'", 
									argument, 
									Arrays.toString(f.getType().getEnumConstants()), 
									value));
				else
					throw new IllegalStateException();
			} else
				return val;
		} else {

				if (f.getType() == int.class 
						|| f.getType() == Integer.class ) {
					try {
						return Integer.parseInt(value);
					} catch (NumberFormatException e) {
						if (option != null)
							throw new ParseException(
								String.format("Option '%s' expected integer value but was: '%s'", 
										option, 
										value));
						else if (argument != null)
							throw new ParseException(
								String.format("Argument '%s' expected integer value but was: '%s'", 
										argument, 
										value));
						else
							throw new IllegalStateException();
					}
				} else if (f.getType() == float.class 
						|| f.getType() == Float.class) {
					try {
						return Float.parseFloat(value);
					} catch (NumberFormatException e) {
						if (option != null)
							throw new ParseException(
								String.format("Option '%s' expected float value but was: '%s'", 
										option, 
										value));
						else if (argument != null)
							throw new ParseException(
								String.format("Argument '%s' expected float value but was: '%s'", 
										argument, 
										value));
						else
							throw new IllegalStateException();
					}
				} else if (f.getType() == boolean.class
						|| f.getType() == Boolean.class) {
					if ("true".equalsIgnoreCase(value) 
							|| "1".equalsIgnoreCase(value))
						return true;
					else if ("false".equalsIgnoreCase(value) 
							|| "0".equalsIgnoreCase(value))
						return false;
					
					if (option != null)
						throw new ParseException(
							String.format("Option '%s' expected boolean value but was: '%s'", 
									option, 
									value));
					else if (argument != null)
						throw new ParseException(
							String.format("Argument '%s' expected boolean value but was: '%s'", 
									argument, 
									value));
					else
						throw new IllegalStateException();
				} else
					return value;
			
		}
	}

	public static Class<?>[] appendCfg(Class<?>[] cfgClass, Class<?> append) {
		Class<?>[] newArray = new Class<?>[cfgClass.length + 1];
		System.arraycopy(cfgClass, 0, newArray, 0, cfgClass.length);
		newArray[cfgClass.length] = append;
		return newArray;
	}

	public static String[] smartSplit(String line) {
		List<String> res = new ArrayList<>();

		StrTokenizer tokenizer = new StrTokenizer(line.replace('\t', ' '), ' ');
		String token = null;
		while ((token = tokenizer.nextTokenUnquoted()) != null) {
			res.add(token);
		}
		return res.toArray(new String[res.size()]);
	}

}
