package res;

import data.ConnectFactory;
import data.ConnectType;
import data.IConnect;
import wrapper.History;
import wrapper.User;

public class HistoryLoader {
	
	private IConnect connect;
	
	public HistoryLoader (){
		connect = new ConnectFactory().getConnector(ConnectType.MongoDB);
	}
	
	private void getHistory(User user){
		History[] histories = connect.getHistory(user);
		
		for (int i = 0; i < histories.length; i++) {
			System.out.println(histories[i]);
		}
	}
	
	private boolean addHistory (String query, User user){
		connect.addHistory(new History(query, user));
		return true;
	}
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		HistoryLoader histL = new HistoryLoader();
		UserLoader userL = new UserLoader();

		User user = userL.getUser("mroge037@fiu.edu", "123");
		System.out.println(user);
		
	//	histL.addHistory("123testing", user);
		histL.getHistory(user);
		
	}
	
}
