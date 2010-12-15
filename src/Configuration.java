
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
	
	// Twitter access tokens
	public final String pleaseRegister = "See README to connect to Twitter";
	
	public String getAccessToken() {
		return config.getString("accessToken", pleaseRegister);
	}
	
	public String getAccessTokenSecret() {
		return config.getString("accessTokenSecret", pleaseRegister);
	}
}
