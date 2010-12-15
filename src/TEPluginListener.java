import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TEPluginListener extends PluginListener {

	private Configuration config;
	private Logger log;
	private ExecutorService pool;
	
	public TEPluginListener(Configuration config, Logger log) {
		this.config = config;
		this.log = log;
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
	
	private class Tweet implements Runnable {

		private String message;
		
		public Tweet(String message) {
			// Trim the tweet to 140 characters or less
			if(message.length() > 140) {
				this.message = message.substring(0, 139);
			} else {
				this.message = message;
			}
		}
		
		
		@Override
		public void run() {
			try {
				ConfigurationBuilder cb = new ConfigurationBuilder();
		    	cb.setDebugEnabled(true)
		    	  .setOAuthAccessToken(config.getAccessToken())
		    	  .setOAuthAccessTokenSecret(config.getAccessTokenSecret())
		    	  .setOAuthConsumerKey("wIb1qVNc0CNXQJxduYIXw")
		    	  .setOAuthConsumerSecret("vTES3U9862wYaxFRdMyD1LRatkq2R42mDyOjXLHIdk");
		    	Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		    	twitter.updateStatus(message);
			} catch(TwitterException e) {
				log.warning("TwitterEvents: Error sending tweet - " + e.getMessage());
			}
		}

	}	
}
