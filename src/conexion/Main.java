package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.UIManager;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class Main {
 
	public static void main(String[] argv) {
		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		try {
			GUI frame = new GUI();
			frame.setVisible(true);
			frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
			//frame.getTextPane().requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		Connection connection = Conexion.getConexion();
		Conexion.close();
		connection=Conexion.getConexion();
		Conexion.close();
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("cf5fLMuGEIW8vZmHzhsXJLocx")
		  .setOAuthConsumerSecret("Rk9Wgc8wJRcNAjBdDe02V5EpePOctZbXlTgIczlTNgmqgp8702")
		  .setOAuthAccessToken("783415302-y3DwvNRfWIBM0AtjTuv93QpvhulDjlbmy37pX0b0")
		  .setOAuthAccessTokenSecret("suvmZ44xdgfJrUkRCWn7VvhwerEuUCvjPXrSaWqvbuWsk");
		TwitterFactory tf = new TwitterFactory(cb.build());
		 // The factory instance is re-useable and thread safe.
	    Twitter twitter = tf.getInstance();
	    
	    Query query = new Query("");
	    QueryResult result;
		try {
			result = twitter.search(query);
			for (Status status : result.getTweets()) {
		        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
		    }
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		*/
	}
 
}