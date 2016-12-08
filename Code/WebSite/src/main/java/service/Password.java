package service;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jdi.Value;

import data.ConnectFactory;
import data.ConnectType;
import data.IConnect;
import res.Email;
import res.StringUtils;
import security.PasswordUser;
import wrapper.User;

/**
 *
 * @author Michel Roger
 *
 */

public class Password extends Service {
	
	private IConnect connect;
	
	public Password(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		connect = new ConnectFactory().getConnector(ConnectType.MongoDB);
	}

	@Override
	public void execute() {
		//response message
		String response = null;
		int status = 200;
		String hash = null;
		
		//get the json object
		String userEmail = getUser().trim();
		
		if (userEmail != null){
			//check is the user is register
			User user = connect.getUserByEmail(userEmail);
			
			if (user != null){
				//generate the hash to be sent
				hash = PasswordUser.getHash(user.getEmail().trim());
				//save the tmphash on the db
				user.setHashPass(hash);
				boolean succSave = connect.updateUser(user);
				//sent the email
				if(succSave){
					String body = getBodyEmail(user, hash);
					ArrayList<String> emailTo = new ArrayList<>();
					emailTo.add(userEmail);
					Email email = new Email(emailTo, "LegalWise reset Password request!!", body);
					email.sentEmail();
				}else{
					response = "Please contact the admin, something bad happen!!";
					status = 500;
				}
			}else{
				//do not show that the user is not register
				response = "If your user is register, an email is on the way";
			}
			//sent response back
			setResponse(status, response);
		}
	}
	
	private String getUser (){
		String user = null;
		JSONParser parser;
		JSONObject obj;
		
		try {
			parser = new JSONParser();
			obj = (JSONObject)parser.parse(request.getReader());			
			user = obj.get("body").toString();
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			setResponse(400, getBasicResponseJson(400, e.getMessage()));
		}
		return user;
	}
	
	private String getBodyEmail (User user, String hash){
		String body = null;
		
		try{
		StringBuffer uri = request.getRequestURL();
		uri.append("?name=");
		body = "Hello "+user.getFirstName()+
				", \n"+"Lets do it, " +
				// we need to encode in order to get it right in the way back from the server request
				uri+URLEncoder.encode(hash, "UTF-8");
		
		}catch (Exception e) {
			System.out.println("ERROR: getting the email body!!");
			e.printStackTrace();
		}
		
		return body;
	}
}
