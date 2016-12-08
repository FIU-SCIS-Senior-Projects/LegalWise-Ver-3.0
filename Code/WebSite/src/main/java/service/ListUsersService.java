package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import data.ConnectFactory;
import data.ConnectType;
import data.IConnect;
import data.sql.Connector;
import wrapper.User;

/**
 * 
 * @author Fernando
 *
 */
public class ListUsersService extends Service {
	/**
	 * Main constructor
	 * 
	 * @param request
	 * @param response
	 */
	public ListUsersService(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}
	
	@Override
	public void execute() {
		JSONArray obj = new JSONArray();
	//	Connector conn = new Connector();
		IConnect conn = new ConnectFactory().getConnector(ConnectType.MongoDB);
		User[] users;
		int offset = 0;
		String textFilter = request.getParameter("textFilter");
		
		try {
			offset = Integer.valueOf(request.getParameter("offset"));
		} catch (NumberFormatException e) {}
		
		if ((users = conn.getUsers(offset, textFilter)) != null) {
			for (User user : users)
				obj.add(user.getAsJson());
			setResponse(200, obj.toString());
		} else
			setResponse(500, getBasicResponseJson(500, conn.getLastError()));
	}

}
