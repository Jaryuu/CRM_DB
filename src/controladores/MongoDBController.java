/*
 *	Universidad del Valle de Guatemala 
 * 	Bases de Datos
 * 	Proyecto 2
 * 	Julio Ayala, Diego Perez, Ricardo Zepeda
 * 	Controlador de mongo
 */

package controladores;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;


public class MongoDBController {
	
	private DB db;
	private DBCollection collection;
	MongoClient mongoClient;
	
	//COnstructor recibe nombre de la db a la que se va a conectar.
	public MongoDBController(String databaseName){
		try{
			MongoClient mongoClient = new MongoClient();
			db = mongoClient.getDB(databaseName);
		}
		catch(Exception e){
			
		}
	}
	
	public DB getDb() {
		return db;
	}


	public void setDb(String database) {
		db = mongoClient.getDB(database);
	}


	public DBCollection getCollection() {
		return collection;
	}


	public void setCollection(String coll) {
		collection = db.getCollection(coll);
	}	


	
	//Inserta en la coleccion todos los tweets para un nit
	public void insert(int nit, ArrayList<String> tweets){
		for (String tweet:tweets){
			BasicDBObject doc = new BasicDBObject("nit", nit)
	        .append("tweet", tweet);
			collection.insert(doc);
		}
		find(nit);
		
	}
	
	public ArrayList<String> find(int nit){
		System.out.println(nit);
		ArrayList<String> result = new ArrayList<String>();
		BasicDBObject query = new BasicDBObject("nit",nit);
		DBCursor cursor = collection.find(query);
		try {
		   while(cursor.hasNext()) {
			   DBObject obj = cursor.next();
		       result.add((String) obj.get("tweet"));
//			   System.out.println(nit+" : "+(String) obj.get("tweet"));
		   }
		} 
		finally {
		   cursor.close();
		}
		return result;
	}
	
	//Actualiza en la coleccion todos los tweets para un nit
	public void update(int oldNit, int newNit){
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.append("$set",
			new BasicDBObject().append("nit", newNit));
	 
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.append("nit", oldNit);
	 
		collection.updateMulti(searchQuery, updateQuery);			
	}
	
	
	//borra en la coleccion todos los tweets para un nit
	public void delete(int nit){
		
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.append("nit", nit);
	 
		collection.remove(searchQuery);
		find(nit);
	}
	
	
}
