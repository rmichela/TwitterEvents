
public class Tweet implements Runnable {

	private String message;
	
	public Tweet(String message) {
		// Trim the tweet to 140 characters or less
		this.message = message.substring(0, 139);
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
