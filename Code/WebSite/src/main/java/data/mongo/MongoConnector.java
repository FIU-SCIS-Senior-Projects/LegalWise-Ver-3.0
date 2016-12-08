package data.mongo;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import res.ResourceManager;

public class MongoConnector {

	private static final String ENV_NAME = "ENVIROMENT";
	private static final String MONGODB_KEY = "mongo_db";
	private String dbUrl;
	private Datastore datastore;
	private MongoClient mongoDB;
	private static MongoConnector connector;

	private MongoConnector() {
		obtainDbUrl();
	}

	/**
	 * Create a static method to get instance.
	 */
	public static MongoConnector getConnector() {
		if (connector == null) {
			connector = new MongoConnector();
		}
		if (connector.getDataStore() == null) {
			connector.connect();
		}
		return connector;
	}

	public Datastore getDataStore() {
		return datastore;
	}

	private boolean connect() {
		URI uri;
		String userInfo;
		String[] credentials;
		String db;

		if (dbUrl == null)
			return false;

		try {
			uri = new URI(dbUrl);
			db = uri.getPath().replace('/', ' ').trim();

			MongoClientURI connectionString = new MongoClientURI(uri.toString());
			// specified connection setting on this object
			MongoClient mongoDB = new MongoClient(connectionString);

			final Morphia morphia = new Morphia();

			// tell Morphia where to find your classes
			// can be called multiple times with different packages or classes
			morphia.mapPackage("wrapper.*");

			// create the Datastore connecting to the default port on the local
			// host
			datastore = morphia.createDatastore(mongoDB, db);
			datastore.ensureIndexes();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void disconnect() {
		if (mongoDB != null)
			try {
				mongoDB.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		// release resources anyway
		mongoDB = null;
	}

	public static void main(String[] args) {
		MongoConnector mc = new MongoConnector();
		mc.connect();
	}

	/**
	 * Private helper that obtains the url of the database linked to this app.
	 * Should work both remotely and locally as the ResourceManager.getEnv
	 * method will use the local version of the environment variables if the
	 * System returns a null value. This local version contains the URL
	 * specified in the xml file "env.xml", which should contain a tag named
	 * VCAP_SERVICES with the local url to be used in case the app is running
	 * locally. Change this value to your local database (using the proper JSON
	 * formatting):<br/>
	 * {"postgresql-9.1":[{"credentials":{uri": "[URL]"}}]}
	 * 
	 * @author Fernando
	 * @date 02/08/2015
	 * @return
	 */
	private void obtainDbUrl() {
		JSONParser parser;
		JSONObject obj1, obj2;
		JSONArray arr;
		String envValue;

		envValue = ResourceManager.getEnv(ENV_NAME);
		// env information should come as a JSON string, which needs
		// parsing...
		parser = new JSONParser();
		try {
			obj1 = (JSONObject) parser.parse(envValue);
			// the structure of the JSON is expected to be
			// {"postgresql-9.1":[{"credentials":{uri": "[URL]"}}]}
			if (obj1.containsKey(MONGODB_KEY)) {
				arr = (JSONArray) obj1.get(MONGODB_KEY);
				if (!arr.isEmpty()) {
					obj2 = (JSONObject) arr.get(0);
					if (obj2.containsKey("credentials"))
						dbUrl = (String) ((JSONObject) obj2.get("credentials")).get("uri");
				}
			}
		} catch (ParseException | RuntimeException e) {
			// null at this point;
			dbUrl = null;
			e.printStackTrace();
		}
	}

	private void test() {

		MongoDatabase database = mongoDB.getDatabase("scoop-dev");
		MongoCollection<Document> collection = database.getCollection("test");

		Document doc = new Document("name", "MongoDB").append("type", "database").append("count", 1).append("info",
				new Document("x", 203).append("y", 102));
		collection.insertOne(doc);

		System.out.println(collection.count());

	}
}
