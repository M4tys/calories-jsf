package com.jsfcourse.user;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.simplesecurity.RemoteClient;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import com.jsf.dao.UserDAO;
import com.jsf.dao.UserRoleDAO;
import com.jsf.entities.Role;
import com.jsf.entities.User;
import com.jsf.entities.Userrole;

@Named
@ViewScoped
public class UserEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_USER_LIST = "userList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user = new User();
	private User loaded = null;
	private Userrole activeRole = new Userrole();
	private String selectedRole;
	private List<Role> availableRoles;

	@Inject
	UserDAO userDAO;

	@Inject
	UserRoleDAO userRoleDAO;

	@Inject
	FacesContext ctx;

	@Inject
	ExternalContext extContext;

	@Inject
	Flash flash;

	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}

	public List<Role> getAvailableRoles() {
		if (availableRoles == null) {
			availableRoles = userRoleDAO.getAllRoles();
		}
		return availableRoles;
	}

	public User getUser() {
		return user;
	}

	public void onLoad() throws IOException {
		loaded = (User) flash.get("user");

		if (loaded != null) {
			user = loaded;

			if (user.getUserroles() != null && !user.getUserroles().isEmpty()) {
				activeRole = userRoleDAO.findActiveRoleByUser(user);
				if (activeRole != null) {
					selectedRole = activeRole.getRole().getRoleName();
				}
			}
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
		}

	}

	public String saveData() {
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		HttpSession session = (HttpSession) extContext.getSession(false);

		if (session != null) {
			RemoteClient<?> remoteClient = (RemoteClient<?>) session.getAttribute("remoteClient");

			if (remoteClient != null) {
				User client = (User) remoteClient.getDetails();

				try {

					if (client != null) {
						user.setUser(client);
						user.setEditDate(new Date());
					}

					if (activeRole == null || !activeRole.getRole().getRoleName().equals(selectedRole)) {
						user.getUserroles().forEach(userRole -> {
							if (userRole.getRemoveDate() == null) {
								userRole.setRemoveDate(new Date());
								userRoleDAO.update(userRole);
							}
						});

						Role newRole = userRoleDAO.findByName(selectedRole);

						Userrole newUserRole = new Userrole();
						newUserRole.setUser(user);
						newUserRole.setRole(newRole);
						newUserRole.setAssignDate(new Date());
						userRoleDAO.insert(newUserRole);
					}

					userDAO.update(user);

				} catch (Exception e) {
					e.printStackTrace();
					ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bład podczas zapisu", null));
					return PAGE_STAY_AT_THE_SAME;
				}
			}
		}

		return PAGE_USER_LIST;
	}
}