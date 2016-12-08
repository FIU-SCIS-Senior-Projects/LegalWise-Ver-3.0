package dispatch;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.sql.Connector;
import security.Authorization;
import service.CreateService;
import service.DownloadService;
import service.HistoryService;
import service.ListDocumentsService;
import service.ListUsersService;
import service.Password;
import service.PasswordReset;
import service.QaAdditionService;
import service.SearchService;
import service.Service;
import service.UpdateUserService;
import service.UploadService;

/**
 * 
 * @author Fernando
 *
 */
public class ServiceDispatcher implements Dispatchable {
	private String service;

	/**
	 * 
	 * @param service
	 */
	public ServiceDispatcher(String service) {
		this.service = service;
	}

	@Override
	public void dispatchGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Authorization auth = new Authorization(request, response);
		Service service = null;
		boolean validRequest = true;
		boolean isAuth = true;
		
		// reset password dont check user registration
		switch (this.service) {
			case "password":
				service = new PasswordReset(request, response);
				break;
	
			default:
				validRequest = false;
				break;
		}
		
		//not a previous services, check the one with auth
		if(!validRequest){
			if (auth.isValidToken()) {
				validRequest = true;
				
				switch (this.service) {
				case "create":
					service = new CreateService(request, response);
					break;
				case "users":
					service = new ListUsersService(request, response);
					break;
				case "search":
					service = new SearchService(auth.getUser(), request, response);
					break;
				case "documents":
					service = new ListDocumentsService(request, response);
					break;
				case "download":
					service = new DownloadService(request, response);
					break;
				case "history":
					service = new HistoryService(auth.getUser(), request, response);
					break;
				default:
					validRequest = false;
				}
			} else{
				isAuth = false;
			}
		}
		
		if (validRequest) {
			//valid request check is and authorized user or a service that do not need auth
			if(!isAuth){
				setResponse(response, 401, "Unauthorized");
			}else{
				// perform service
				service.execute();
			}
		} else {
			setResponse(response, 404, "{\"msg\":\"Service not found\"}");
			return;
		}
	}

	@Override
	public void dispatchPost(HttpServletRequest request, HttpServletResponse response) {
		Authorization auth = new Authorization(request, response);
		Service service = null;
		boolean validRequest = true;
		boolean isAuth = true;

		// reset password dont check user registration
		switch (this.service) {
			case "password":
				service = new Password(request, response);
				break;
	
			default:
				validRequest = false;
				break;
		}
		
		//not a previous services, check the one with auth
		if(!validRequest){
			if (auth.isValidToken()) {
				validRequest = true;
				
				switch (this.service) {
				case "upload":
					service = new UploadService(auth.getUser(), request, response);
					break;
				case "user/update":
					service = new UpdateUserService(request, response);
					break;
				case "qa":
					service = new QaAdditionService(request, response);
					break;
				default:
					validRequest = false;
				}
			} else {
				isAuth = false;
			}
		}
		
		if (validRequest) {
			//valid request check is and authorized user or a service that do not need auth
			if(!isAuth){
				setResponse(response, 401, "Unauthorized");
			}else{
				// perform service
				service.execute();
			}
		} else {
			setResponse(response, 404, "{\"msg\":\"Service not found\"}");
			return;
		}
	}

	/**
	 * Sets the status and body of a response.
	 * 
	 * @param response
	 * @param status
	 * @param content
	 * @throws IOException
	 */
	public static void setResponse(HttpServletResponse response, int status, Object content) {
		response.setStatus(status);
		try {
			response.getOutputStream().write((content != null ? content.toString() : "").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
