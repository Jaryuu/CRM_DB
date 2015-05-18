package conexion;

import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterController{
	
	ConfigurationBuilder cb;
	
	public TwitterController(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(consumerKey)
		  .setOAuthConsumerSecret(consumerSecret)
		  .setOAuthAccessToken(accessToken)
		  .setOAuthAccessTokenSecret(accessTokenSecret);
		
	}

	public ArrayList<String> searchTweets(String searchQuery, int amount){
		ArrayList<String> tweets = new ArrayList<String>();
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		 // The factory instance is re-useable and thread safe.
	    Twitter twitter = tf.getInstance();
	    
	    Query query = new Query(searchQuery);
	    query.setCount(amount);
	    QueryResult result;
		try {
			result = twitter.search(query);
			for (Status status : result.getTweets()) {
//		        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
		        tweets.add(status.getText());
		        System.out.println(status.getText());
		    }
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweets;
	}
	
}

