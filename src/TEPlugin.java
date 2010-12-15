import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TEPlugin extends SuperPlugin {
	
	private List<PluginRegisteredListener> prls;
	private Configuration config;
	
	public TEPlugin() {
		super("TwitterEvents");
		config = new Configuration(baseConfig);
		
	}

	@Override
	public void enableExtra() {
		TEPluginListener listener = new TEPluginListener(config);
		
		// Register hMod hooks
		if(config.getTweetLogin()) {
			prls.add(etc.getLoader().addListener(PluginLoader.Hook.LOGIN, listener, this, PluginListener.Priority.LOW));
		}
		if(config.getTweetDisconnect()) {
			prls.add(etc.getLoader().addListener(PluginLoader.Hook.DISCONNECT, listener, this, PluginListener.Priority.LOW));
		}
		if(config.getTweetBan()) {
			prls.add(etc.getLoader().addListener(PluginLoader.Hook.BAN, listener, this, PluginListener.Priority.LOW));
		}
		if(config.getTweetKick()) {
			prls.add(etc.getLoader().addListener(PluginLoader.Hook.KICK, listener, this, PluginListener.Priority.LOW));
		}
		if(config.getTweetDeath()) {
			prls.add(etc.getLoader().addListener(PluginLoader.Hook.HEALTH_CHANGE, listener, this, PluginListener.Priority.LOW));
		}
	}

	@Override
	public void disableExtra() {
		// Unregister hMod hooks
		for(PluginRegisteredListener psr : prls) {
			etc.getLoader().removeListener(psr);
		}
		prls = new ArrayList<PluginRegisteredListener>();
	}
	
	
}
