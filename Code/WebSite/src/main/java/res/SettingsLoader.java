package res;
import data.ConnectFactory;
import data.ConnectType;
import data.IConnect;
import wrapper.Settings;

public class SettingsLoader {
	
	private Settings settings;
	
	public SettingsLoader (){
		settings = Settings.getInstance();
	}
	
	private void loadSettings (){
		settings.setSetting("solrClusterId", "scc701cf1e_60b6_46c7_a96c_fbc4d75274ee");
		settings.setSetting("solrConfigurationId", "lw3.0_debug_config");
		settings.setSetting("solrCollectionId", "lw3.0");
		settings.setSetting("solrRankerId", "c852c8x19-rank-2711");
		
		settings.save();
	}
	
	private void readSettings (){
		
		System.out.println(settings.getSetting("solrClusterId"));
		System.out.println(settings.getSetting("solrConfigurationId"));
		System.out.println(settings.getSetting("solrCollectionId"));
		System.out.println(settings.getSetting("solrRankerId"));
	}

	public static void main(String[] args){
		SettingsLoader setts = new SettingsLoader();
		setts.loadSettings();
		setts.readSettings();
		
	}
	
}
