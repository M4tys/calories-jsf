package com.jsfcourse.login;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.jsf.dao.UserDAO;
import com.jsf.entities.User;

@Named
@RequestScoped
public class LoginBB {
	private static final String PAGE_ADMIN = "/pages/admin/userList?faces-redirect=true";
	private static final String PAGE_DIETICIAN = "/pages/dietician/productList?faces-redirect=true";
	private static final String PAGE_LOGIN = "/pages/login/login?faces-redirect=true";
	private static final String PAGE_USER = "/pages/user/dishList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String login;
	private String pass;

	@Inject
	UserDAO userDAO;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String doLogin() {
		FacesContext ctx = FacesContext.getCurrentInstance();

		try {
			// 1. verify login and password - get User from "database"
			User user = userDAO.getUserFromDatabase(login, pass);

			// 2. if bad login or password - stay with error info
			if (user == null) {
				ctx.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Niepoprawny login lub hasło", null));
				setLogin("");
				setPass("");

				return PAGE_STAY_AT_THE_SAME;
			}

			if (user.getIsActive() == (byte) 0) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Konto jest nieaktywne", null));
				return PAGE_STAY_AT_THE_SAME;
			}

			// 3. if logged in: get User roles, save in RemoteClient and store it in session
			RemoteClient<User> client = new RemoteClient<>(); // create new RemoteClient
			client.setDetails(user);

			List<String> roles = userDAO.getUserRolesFromDatabase(user); // get User roles

			if (roles != null) { // save roles in RemoteClient
				for (String role : roles) {
					client.getRoles().add(role);
				}
			}

			// store RemoteClient with request info in session (needed for SecurityFilter)
			HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
			client.store(request);

			// and enter the system (now SecurityFilter will pass the request)
			if (roles.contains("admin")) {
				return PAGE_ADMIN;
			}
			if (roles.contains("dietician")) {
				return PAGE_DIETICIAN;
			} else {
				return PAGE_USER;
			}

		} catch (Exception e) {

			// Add an error message for the user
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Wystąpił błąd podczas logowania. Spróbuj ponownie później.", null));

			// Stay on the same page
			return PAGE_STAY_AT_THE_SAME;
		}
	}

	public String doLogout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		session.invalidate();
		return PAGE_LOGIN;
	}

}
