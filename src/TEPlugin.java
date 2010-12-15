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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TEPlugin extends SuperPlugin {
	
	private List<PluginRegisteredListener> prls;
	private Configuration config;
	
	public TEPlugin() {
		super("TwitterEvents");
		config = new Configuration(baseConfig);
		prls = new ArrayList<PluginRegisteredListener>();
	}

	@Override
	public void enableExtra() {
		
		log.info("Starting TwitterEvents");
		
		try {
			ClassPathHack.addFile("twitter4j-core-2.1.9-SNAPSHOT.jar");
			
			String accessToken = config.getAccessToken();
			String accessTokenSecret = config.getAccessTokenSecret();
			
			if(accessToken != config.pleaseRegister && accessTokenSecret != config.pleaseRegister) {
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
				
				initMessagesConfig();
			} else {
				log.info("*** TwitterEvents is not yet registered to send tweets to Twitter! Consult the README.");
			}
		} catch(IOException e) {
			log.warning(e.getMessage());
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
	}
	
	private void initMessagesConfig() {
		config.getTweetLoginMessage();
		config.getTweetDisconnectMessage();
		config.getTweetBanMessage();
		config.getTweetKickMessage();
		config.getTweetDeathMessage();
		baseConfig.save();		
	}
}
