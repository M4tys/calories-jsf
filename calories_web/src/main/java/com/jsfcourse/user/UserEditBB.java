package com.jsfcourse.user;

import java.io.IOException;
import java.io.Serializable;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

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

	@EJB
	UserDAO userDAO;

	@Inject
	FacesContext context;

	public User getUser() {
		return user;
	}

	public void onLoad() throws IOException {
		if (!context.isPostback()) {
			if (!context.isValidationFailed() && user.getIdUser() != null) {
				loaded = userDAO.find(user.getIdUser());
			}
			if (loaded != null) {
				user = loaded;
			} else {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
				// if (!context.isPostback()) { // possible redirect
				// context.getExternalContext().redirect("personList.xhtml");
				// context.responseComplete();
				// }
			}
		}
	}


	public String saveData() {
		// no Person object passed
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {

			if (user.getIdUser() == null) {
				// new record
				userDAO.insert(user);
			} else {
				// existing record
				userDAO.update(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_USER_LIST;
	}
}
