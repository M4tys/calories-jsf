package com.jsfcourse.user;

import java.io.IOException;
import java.io.Serializable;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.jsf.dao.UserDAO;
import com.jsf.entities.User;

@Named
@ViewScoped
public class UserEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_USER_LIST = "userList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user = new User();
	private User loaded = null;

	@Inject
	UserDAO userDAO;

	@Inject
	FacesContext ctx;
	
	@Inject
	private ExternalContext extContext;
	
	@Inject
	Flash flash;

	public User getUser() {
		return user;
	}

	public void onLoad() throws IOException {
		loaded = (User) flash.get("user");

		if (loaded != null) {
			user = loaded;
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
		}

	}


	public String saveData() {
		// no Person object passed
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			RemoteClient<User> client = (RemoteClient<User>) extContext.getSessionMap().get("remoteClient");
            
            if (client != null) {
                user.setUser(client.getDetails());
            }

			if (user.getIdUser() == null) {
				// new record
				userDAO.insert(user);
			} else {
				// existing record
				userDAO.update(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bład podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_USER_LIST;
	}
}
