package dispatch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import security.Authorization;
import security.CreateUser;
import service.PasswordReset;
import wrapper.User;

/**
 * 
 * @author Fernando
 *
 */
public class PageDispatcher implements Dispatchable {
	private String page;

	/**
	 * 
	 */
	public PageDispatcher(String page) {
		this.page = page;
	}

	@Override
	public void dispatchGet(HttpServletRequest request,
			HttpServletResponse response) 
					throws ServletException, IOException {
		RequestDispatcher view;
		User user;
		Authorization auth = new Authorization(request, response);
		
		if (page.equals("/logout")) {
			auth.logout();
			response.sendRedirect("login.jsp");
			return;
		}// check is a password reset 
		else if (page.equals("/passwordreset")){
			if(auth.isValidSessionPSW()){
				request.setAttribute("success", "Plase enter a new password");
				view = request.getRequestDispatcher("passwordReset.jsp");
			}else{
				// not a valid cookie, failed!!!
				request.setAttribute("error", "Invalid Link to reset Password,"
						+ " it could has expired. Otherwise, Please register!!!");
				view = request.getRequestDispatcher("login.jsp");
			}
		}//check is valid session
		else if (!auth.isValidSession()) {
			try {
				request.setAttribute("retUrl", 
					URLEncoder.encode(request.getRequestURI(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// disregard attr
			}	
			request.removeAttribute("userFirstName");
			request.removeAttribute("userLastName");
			request.removeAttribute("userEmail");
			request.removeAttribute("userType");
			request.removeAttribute("userIsTrial");
			request.removeAttribute("userTrialDuration");
			view = request.getRequestDispatcher("login.jsp");
		} else {
			user = auth.getUser();
			// put user info in request
			request.setAttribute("userFirstName", user.getFirstName());
			request.setAttribute("userLastName", user.getLastName());
			request.setAttribute("userEmail", user.getEmail());
			request.setAttribute("userType", user.getType().toString());
			request.setAttribute("userIsTrial", user.isTrial());
			request.setAttribute("userTrialDuration", user.getTrialDuration());
			// send to requested
			view = request.getRequestDispatcher(page + ".jsp");
		}
			
		view.forward(request, response);
	}

	@Override
	public void dispatchPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher view;
		Authorization auth = new Authorization(request, response);
		// attribute that says where to go after login
		String retUrl = request.getParameter("retUrl");

		switch (page) {
		case "login":
			if (auth.login()) {
				// login succeeded
				if (retUrl == null || retUrl.isEmpty())
					retUrl = "index";
				// we use sendRedirect to force GET,
				// the RequestDispatcher will retain the POST
				response.sendRedirect(retUrl);
			} else {
				// login failed
				if (retUrl != null)
					request.setAttribute("retUrl", retUrl);

				request.setAttribute("error", "Invalid credentials");
				view = request.getRequestDispatcher("login.jsp");
				view.forward(request, response);
			}
			break;
		case "register":
			CreateUser user = new CreateUser(request, response);

			try {
				User CreatedUser = user.create();
				if (CreatedUser != null && auth.loginAfterRegister(CreatedUser)) {
					retUrl = "index";
					// we use sendRedirect to force GET,
					// the RequestDispatcher will retain the POST
					response.sendRedirect(retUrl);
				} else {
					// register failed
					if (retUrl != null)
						request.setAttribute("retUrl", retUrl);

					request.setAttribute("error", "Please contact the administrator, error register user");
					view = request.getRequestDispatcher("login.jsp");
					view.forward(request, response);
				}

			} catch (Exception ex) {
				if (retUrl != null)
					request.setAttribute("retUrl", retUrl);

				request.setAttribute("error", "Error on register a User, " + ex.getMessage());
				view = request.getRequestDispatcher("login.jsp");
				view.forward(request, response);
			}
			break;
		case "passwordreset":
			//check cookie for passwd
			if(auth.isValidSessionPSW()){
				//set the new password
				User userPsw = auth.setPasswd();
				 if( userPsw != null && auth.loginAfterRegister(userPsw)){
					 //clean passwd cookie
					auth.cleanPasswdCookie();
					retUrl = "index";
					// we use sendRedirect to force GET,
					// the RequestDispatcher will retain the POST
					response.sendRedirect(retUrl);
				 }else{
					// se new password failed
					if (retUrl != null)
						request.setAttribute("retUrl", retUrl);

					request.setAttribute("error", "Please contact the administrator, error setting new password");
					view = request.getRequestDispatcher("login.jsp");
					view.forward(request, response);
				 }
				/*
				request.setAttribute("success", "Plase enter a new password");
				view = request.getRequestDispatcher("passwordReset.jsp");
				 */
			}else{
				// not a valid cookie, failed!!!
				request.setAttribute("error", "Invalid Link to reset Password,"
						+ " it could has expired. Otherwise, Please register!!!");
				view = request.getRequestDispatcher("login.jsp");
			}
			
			break;
		}
	}
}
