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

public class Configuration {
	private PropertiesFile config;
	
	// Tweet enable configs
	public Configuration (PropertiesFile config) {
		this.config = config;
	}
	
	public boolean getTweetLogin() {
		return config.getBoolean("tweetLogin", true);
	}
	
	public boolean getTweetDisconnect() {
		return config.getBoolean("tweetDisconnect", false);
	}
	
	public boolean getTweetBan() {
		return config.getBoolean("tweetBan", false);
	}
	
	public boolean getTweetKick() {
		return config.getBoolean("tweetKick", false);
	}
	
	public boolean getTweetDeath() {
		return config.getBoolean("tweetDeath", false);
	}
	
	public boolean getTweetServerStatus() {
		return config.getBoolean("tweetServerStatus", true);
	}
	
	// Tweet message configs
	public String getTweetLoginMessage() {
		return config.getString("tweetLoginMessage", "{player} has logged into Minecraft");
	}
	
	public String getTweetDisconnectMessage() {
		return config.getString("tweetDisconnectMessage", "{player} has disconnected from Minecraft");
	}
	
	public String getTweetBanMessage() {
		return config.getString("tweetBanMessage", "{player} was banned from Minecraft by {moderator}! {reason}");
	}
	
	public String getTweetKickMessage() {
		return config.getString("tweetKickMessage", "{player} was kicked from Minecraft by {moderator}! {reason}");
	}
	
	public String getTweetDeathMessage() {
		return config.getString("tweetDeathMessage", "{player} died in Minecraft");
	}
	
	public String getTweetPluginStartMessage() {
		return config.getString("tweetPluginStartMessage", "The Minecraft server has started");
	}
	
	public String getTweetPluginStopMessage() {
		return config.getString("tweetPluginStopMessage", "The Minecraft server has stopped");
	}
	
	// Twitter access tokens
	public final String pleaseRegister = "See README to connect to Twitter";
	
	public String getAccessToken() {
		return config.getString("accessToken", pleaseRegister);
	}
	
	public String getAccessTokenSecret() {
		return config.getString("accessTokenSecret", pleaseRegister);
	}
	
	public boolean getTwitterConfigured() {
		return getAccessToken() != pleaseRegister && getAccessTokenSecret() != pleaseRegister;
	}
}
