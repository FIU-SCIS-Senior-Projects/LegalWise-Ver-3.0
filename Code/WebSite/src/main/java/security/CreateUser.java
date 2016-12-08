package security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.handler.component.FacetComponent;

import com.ctc.wstx.util.StringUtil;

import data.ConnectFactory;
import data.ConnectType;
import data.IConnect;
import res.StringUtils;
import wrapper.User;

public class CreateUser {

	IConnect connect;
	User user;
	
	public CreateUser(HttpServletRequest request,
			HttpServletResponse response){
		String name = request.getParameter("name");
		String lname = request.getParameter("lname");
		String email = request.getParameter("email");
		String company = request.getParameter("company");
		String password = request.getParameter("password");		
		this.user = new User(name,lname,company,email,password);
		connect = new ConnectFactory().getConnector(ConnectType.MongoDB);
	}
	
	public User create() throws Exception{
		
		//verified that the email is not already on the database
		User existingUser = connect.getUserByEmail(user.getEmail());
		if (existingUser != null){
			throw new Exception("Another user already exists with that email address or invalid email address");
		}		
		//get hash password
		String hash;
		try {
			hash = StringUtils.getHash(user.getPassword());
		}catch(Exception ex){
			throw new Exception (ex.getMessage());
		}
		user.setPassword(hash);
		
		if (connect.createUser(user)){
			return user;
		}
		return null;
	}
}
