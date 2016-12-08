package res;

import java.util.Date;

import data.ConnectFactory;
import data.ConnectType;
import data.IConnect;
import wrapper.History;
import wrapper.User;
import wrapper.UserType;

public class UserLoader {

	private User user;
	private IConnect connect;
	
	public UserLoader (){
		connect = new ConnectFactory().getConnector(ConnectType.MongoDB);
	}
	
	private void createUser(int id, String name, String lname, String cname, 
			String email, UserType utype, Date createdOn, boolean isTrial, int trialDuration,
			String password, boolean isLocked, Date modifiedOn, boolean isActive) throws Exception{
		user = new User();
		user.setUserId(id);
		user.setFirstName(name);
		user.setLastName(lname);
		user.setCompanyName(cname);
		user.setEmail(email);
		user.setType(utype);
		user.setCreatedOn(createdOn);
		user.setTrial(isTrial);
		user.setTrialDuration(trialDuration);
		String pass = StringUtils.getHash(password);
		user.setPassword(pass);
		user.setLocked(isLocked);
		user.setModifiedOn(modifiedOn);
		user.setActive(isActive);
		connect.createUser(user);
	}
	
	public User getUser(String username, String pwd) throws Exception{
		String pass = StringUtils.getHash(pwd);
		return connect.getUserByCredentials(username, pass);
	}
	
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		UserLoader userL = new UserLoader();
		/*
		userL.createUser(5, "Michel", "Roger", "LegalWise3.0", "mroge037@fiu.edu"
				, UserType.ADMIN, new Date(), false, 0, "123", false, new Date(), true);
			*/	
		userL.createUser(6, "Renato", "Almeida", "LegalWise3.0", "ralme006@fiu.edu"
				, UserType.ADMIN, new Date(), false, 0, "123", false, new Date(), true);
		
		User user = userL.getUser("ralme006@fiu.edu", "123");
		System.out.println(user);

	}

}
