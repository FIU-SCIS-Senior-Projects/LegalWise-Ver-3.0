package wrapper;

import java.util.Map;

import data.ConnectFactory;
import data.ConnectType;
import data.IConnect;
import data.sql.Connector;

/**
 * Class containing settings of the application
 * @author 	Fernando Gomez
 * @date	3/10/2016
 */
public class Settings {	
	// singleton instance
	private static Settings instance;
		
	// map with all settings	
	private Map<String, String> map;
	
	//Connector
	private IConnect conn;
	
	/**
	 * Constructor
	 * @author 	Fernando Gomez
	 * @date	3/10/2016
	 */
	private Settings() {
		conn = new ConnectFactory().getConnector(ConnectType.MongoDB);
		map = conn.getSettings();
		
		if (map == null)
			throw new RuntimeException(conn.getLastError());
	}
	
	/**
	 * Returns the only active instance of Settings
	 * @author 	Fernando Gomez
	 * @date	3/10/2016
	 */
	public static Settings getInstance() {
		// initialize if it has not been
		if (instance == null)
			instance = new Settings();

		return instance;
	}
	
	/**
	 * Return the value of the specified setting
	 * @param settingName
	 * @return
	 */
	public String getSetting(String settingName) {
		return map.get(settingName);
	}
	
	/**
	 * Sets the value of the specified setting
	 * @param settingName
	 * @param value
	 */
	public void setSetting(String settingName, String value) {
		map.put(settingName, value);
	}
	
	/**
	 * Saves the current values of the settings
	 * @param settingName
	 * @param value
	 */
	public void save() {
		if (!conn.saveSettings(map))
			throw new RuntimeException(conn.getLastError()); 
	}
}
