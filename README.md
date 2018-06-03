# meta-cli
 
 Annotation's based {@link CommandLineParser} implementation for 
 Command Line Interface. MetaCommandLineParser collects meta information
 represented via java annotations in an application's configuration class.
 
 Simple application configuration class can be demonstrated by the code
 snippet below:
 
    @CLIConfiguration(version="0", name="myApp")
    public class AppConfiguration {
    
	    @Option(description = "Simple private yes/no option without agruments", fullName = "key1", shortName = "k1")
	    private boolean key1;
    
	    @Option(description = "Simple key/value option with agruments", fullName = "keyValue1", shortName = "kv1", hasArguments = true)
	    public String keyValue1;
    
	    @Argument(index = 1, name = "infile", required = true)
	    public String infile;
    
    }
 
 
 Parser analyzes User's input and creates a new AppConfiguration instance with
 injected values into corresponding fields.
 
    CommandLineParser<AppConfiguration> cliParser = new MetaCommandLineParser<>();
 
    AppConfiguration cfg = cliParser.parse(args);
 
 or you can simply call:
 
    AppConfiguration cfg = MetaCli.parse(args, AppConfiguration.class);
 
 The project includes few addition tools such as Console mode initial support and well-formatted help print.