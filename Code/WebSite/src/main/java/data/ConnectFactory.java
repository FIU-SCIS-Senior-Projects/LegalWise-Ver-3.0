package data;

import data.mongo.MongoDAO;
import data.sql.Connector;

public class ConnectFactory {

	public IConnect getConnector(ConnectType contype ){
		IConnect connector = null;
		
		switch(contype){
		case SQL:
			connector = new Connector();
			break;
		case MongoDB:
			connector = new MongoDAO();
			break;
		default:
			//do nothing
			break;
		}
		
		return connector;
	}
}
