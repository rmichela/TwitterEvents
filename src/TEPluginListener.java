import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TEPluginListener extends PluginListener {

	private Configuration config;
	private ExecutorService pool;
	
	public TEPluginListener(Configuration config) {
		this.config = config;
		pool = Executors.newSingleThreadExecutor();
	}
	
	@Override
	public void onLogin(Player player) {
		String message = config.getTweetLoginMessage()
		                       .replace("{player}", player.getName());
		pool.execute(new Tweet(message));
		super.onLogin(player);
	}

	@Override
	public void onDisconnect(Player player) {
		String message = config.getTweetDisconnectMessage()
        					   .replace("{player}", player.getName());
		pool.execute(new Tweet(message));
		super.onDisconnect(player);
	}

	@Override
	public void onBan(Player mod, Player player, String reason) {
		String message = config.getTweetBanMessage()
							   .replace("{player}", player.getName())
							   .replace("{moderator}", mod.getName())
							   .replace("{reason}", reason);
		pool.execute(new Tweet(message));
		super.onBan(mod, player, reason);
	}

	@Override
	public void onKick(Player mod, Player player, String reason) {
		String message = config.getTweetKickMessage()
							   .replace("{player}", player.getName())
							   .replace("{moderator}", mod.getName())
							   .replace("{reason}", reason);
		pool.execute(new Tweet(message));
		super.onKick(mod, player, reason);
	}

	@Override
	public boolean onHealthChange(Player player, int oldValue, int newValue) {
		if(newValue <= 0) {
			String message = config.getTweetDeathMessage()
			   					   .replace("{player}", player.getName());
			pool.execute(new Tweet(message));
		}
		return super.onHealthChange(player, oldValue, newValue);
	}

	@Override
	protected void finalize() throws Throwable {
		pool.shutdown();
		pool = null;
		super.finalize();
	}
	
		
}
