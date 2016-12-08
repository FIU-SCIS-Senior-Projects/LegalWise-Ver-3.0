package service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.ConnectFactory;
import data.ConnectType;
import data.IConnect;
import security.Authorization;
import security.PasswordUser;
import wrapper.User;

public class PasswordReset extends Service {

	private IConnect connect;
	private Authorization auth;

	public PasswordReset(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		connect = new ConnectFactory().getConnector(ConnectType.MongoDB);
		auth = new Authorization(request, response);
	}

	@Override
	public void execute() {
		// get the usermail
		String tmpHash = null;
		//view
		RequestDispatcher view;
		//threshold
		long createdOn, now = (new Date()).getTime(), threshold = 1000 * 60 * 60;	
		//url back
		String retUrl = "/passwordReset";
		//clean cookie
		auth.cleanPasswdCookie();
		
		try {
			request.setCharacterEncoding("utf-8");
			tmpHash = request.getParameter("name");
		} catch (Exception e) {
			System.out.println("ERROR, encoding section");
			e.printStackTrace();
			return;
		}
		
		String[] emailHash = PasswordUser.getEmailHash(tmpHash);

		if (emailHash.length > 2) {
			String userEmail = emailHash[0].trim();
			createdOn = Long.valueOf(emailHash[2]);
			
			if ((now - createdOn) <= threshold) {
				// check the user is registerd
				User user = connect.getUserByEmail(userEmail);
	
				if (user != null) {
					// check the tmpHash
					if (user.getHashPass().equals(tmpHash)) {
						// give back the view
						System.out.println("Valid hashLink!!!! user: " + user.getFirstName());
						//retUrl = "/passwordReset";
						auth.getPasswdCookie(user);						
					} else {
						// trying to fool, fraud hash is not valid
					}
				} else {
					// user not found. please register
				}
			} else {
				//link expired 
			}
		} else {
			// problem with the hash link, report fraud!!!!
		}
		try {
			response.sendRedirect(retUrl);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
