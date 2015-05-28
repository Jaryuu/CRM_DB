/*
 *	Universidad del Valle de Guatemala 
 * 	Bases de Datos
 * 	Proyecto 2
 * 	Julio Ayala, Diego Perez, Ricardo Zepeda
 * 	Controlador de twitter
 */

package controladores;

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
	
	//Constructor que recibe los keys
	public TwitterController(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(consumerKey)
		  .setOAuthConsumerSecret(consumerSecret)
		  .setOAuthAccessToken(accessToken)
		  .setOAuthAccessTokenSecret(accessTokenSecret);
		
	}

	//Metodo para buscar tweets que recibe el criterio de busqueda y la cantidad de tweets a retornar.
	public ArrayList<String> searchTweets(String searchQuery, int amount){
		ArrayList<String> tweets = new ArrayList<String>();
		
		TwitterFactory tf = new TwitterFactory(cb.build());
	    Twitter twitter = tf.getInstance();
	    
	    Query query = new Query(searchQuery);
	    query.setCount(amount);
	    QueryResult result;
		try {
			result = twitter.search(query);
			for (Status status : result.getTweets()) {
		        tweets.add(status.getText());
		    }
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return tweets;
	}
	
}

