
public class TweetCommand implements PluginInterface {

	private TEPluginListener listener;
	public static String commandName = "tweet";
	
	public TweetCommand(TEPluginListener listener) {
		this.listener = listener;
	}
	
	@Override
	public String getName() {
		return commandName;
	}

	@Override
	public int getNumParameters() {
		return 1;
	}

	@Override
	public String checkParameters(Object[] parameters) {
		if(parameters.length != 1) {
			return "Expected one string as a parameter.";
		}
		if(!(parameters[0] instanceof String)) {
			return "Expected a string as the first parameter.";
		}
		return null;
	}

	@Override
	public Object run(Object[] parameters) {
		listener.tweet((String)parameters[0]);
		return true;
	}

}
