//Copyright (C) 2010  Ryan Michela
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import java.util.Calendar;
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
	
	public void tweet(String message) {
		pool.execute(new Tweet(message));
	}
	
	public void forceTweet(String message) {
		Tweet t = new Tweet(message);
		t.run();
	}
	
	private class Tweet implements Runnable {

		private String tweetMessage;
		
		public Tweet(String message) {
			// Trim the tweet to 134 characters or less
			if(message.length() > 134) {
				tweetMessage = message.substring(0, 133);
			} else {
				tweetMessage = message;
			}
			
			// Add anti-dup suffix
			Calendar c = Calendar.getInstance();
			int s = c.get(Calendar.HOUR_OF_DAY) * 60 * 60 + 
			        c.get(Calendar.MINUTE) * 60 + 
			        c.get(Calendar.SECOND);
			tweetMessage += " " + s;
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
		    	twitter.updateStatus(tweetMessage);
			} catch(TwitterException e) {
				log.warning("TwitterEvents: Error sending tweet - " + e.getMessage());
			}
		}

	}	
}
