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

import java.util.ArrayList;
import java.util.List;

public class TEPlugin extends SuperPlugin {
	
	private List<PluginRegisteredListener> prls;
	private Configuration config;
	ShutdownHook sdHook;
	
	public TEPlugin() {
		super("TwitterEvents");
		config = new Configuration(baseConfig);
		prls = new ArrayList<PluginRegisteredListener>();
	}

	@Override
	public void enableExtra() {
		
		log.info("Starting TwitterEvents");
		
		// Write updatr file
		UpdatrWriter.writeUpdatrFile("TwitterEvents.updatr");

		if(config.getTwitterConfigured()) {
			TEPluginListener listener = new TEPluginListener(config, log);
			
			// Register hMod hooks
			if(config.getTweetLogin()) {
				prls.add(etc.getLoader().addListener(PluginLoader.Hook.LOGIN, listener, this, PluginListener.Priority.LOW));
				log.info("* Tweeting logins");
			}
			if(config.getTweetDisconnect()) {
				prls.add(etc.getLoader().addListener(PluginLoader.Hook.DISCONNECT, listener, this, PluginListener.Priority.LOW));
				log.info("* Tweeting disconnects");
			}
			if(config.getTweetBan()) {
				prls.add(etc.getLoader().addListener(PluginLoader.Hook.BAN, listener, this, PluginListener.Priority.LOW));
				log.info("* Tweeting bans");
			}
			if(config.getTweetKick()) {
				prls.add(etc.getLoader().addListener(PluginLoader.Hook.KICK, listener, this, PluginListener.Priority.LOW));
				log.info("* Tweeting kicks");
			}
			if(config.getTweetDeath()) {
				prls.add(etc.getLoader().addListener(PluginLoader.Hook.HEALTH_CHANGE, listener, this, PluginListener.Priority.LOW));
				log.info("* Tweeting deaths");
			}
		
			if(config.getTweetServerStatus()) {
				listener.tweet(config.getTweetPluginStartMessage());
				// Set the shutdown hook
				sdHook = new ShutdownHook(listener);
				Runtime.getRuntime().addShutdownHook(sdHook);
			}
			
			initMessagesConfig();
		} else {
			log.info("*** TwitterEvents is not yet registered to send tweets to Twitter! Consult the README.");
		}
	}

	@Override
	public void disableExtra() {
		log.warning("Stopping TwitterEvents");
		
		// Unregister hMod hooks
		for(PluginRegisteredListener psr : prls) {
			etc.getLoader().removeListener(psr);
		}
		prls = new ArrayList<PluginRegisteredListener>();
		
		// Clear the shutdown hook
		if(sdHook != null) {
			Runtime.getRuntime().removeShutdownHook(sdHook);
			sdHook = null;
		}
	}
	
	private void initMessagesConfig() {
		config.getTweetLoginMessage();
		config.getTweetDisconnectMessage();
		config.getTweetBanMessage();
		config.getTweetKickMessage();
		config.getTweetDeathMessage();	
	}
	
	private class ShutdownHook extends Thread {
		
		private TEPluginListener listener;
		
		public ShutdownHook(TEPluginListener l) {
			listener = l;
		}
		
		public void run() { 
			listener.forceTweet(config.getTweetPluginStopMessage());
		}
	}
}
