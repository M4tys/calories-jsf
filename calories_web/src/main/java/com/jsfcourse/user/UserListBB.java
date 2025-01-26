package com.jsfcourse.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;

import com.jsf.dao.UserDAO;
import com.jsf.entities.User;
import com.jsf.entities.Userrole;

@Named
@ViewScoped
public class UserListBB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String PAGE_USER_EDIT = "/pages/admin/userEdit?faces-redirect=true";
	private LazyDataModel<User> lazyModel;
	private String login;
	private User selectedUser;

	@Inject
	ExternalContext extcontext;

	@Inject
	FacesContext ctx;

	@Inject
	Flash flash;

	@Inject
	UserDAO userDAO;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	public LazyDataModel<User> getLazyModel() {
		return lazyModel;
	}

	public List<User> getFullList() {
		return userDAO.getFullList();
	}

	public String editUser(User user) {

		flash.put("user", user);

		return PAGE_USER_EDIT;
	}

	@PostConstruct
	public void init() {

		lazyModel = new LazyDataModel<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			public int count(Map<String, FilterMeta> filterBy) {
				Map<String, Object> searchParams = new HashMap<>();

				if (login != null && login.length() > 0) {
					searchParams.put("login", login);
				}

				return userDAO.countUsers(searchParams);
			}

			@Override
			public List<User> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				Map<String, Object> searchParams = new HashMap<>();

				if (login != null && login.length() > 0) {
					searchParams.put("login", login);
				}

				return userDAO.getPagedList(offset, pageSize, searchParams);
			}
			
	        @Override
	        public User getRowData(String rowKey) {
	            // Konwertuj rowKey (idUser) na obiekt User
	            for (User user : this) {
	                if (user.getIdUser().toString().equals(rowKey)) {
	                    return user;
	                }
	            }
	            return null;
	        }

	        @Override
	        public String getRowKey(User user) {
	            // Zwróć unikalny identyfikator użytkownika (idUser)
	            return user.getIdUser().toString();
	        }
		};
	}

	public void toggleUserStatus(User user) {
		try {
			user.setIsActive(user.getIsActive() == (byte) 0 ? (byte) 1 : (byte) 0);

			if (user.getIsActive() == (byte) 1) {
				user.setDeactivateDate(null);
			}

			userDAO.update(user);

			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Status użytkownika zmieniony!", null));
		} catch (Exception e) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas zmiany statusu użytkownika!", null));
			e.printStackTrace();
		}
	}

	public String getRolesAsString(User user) {
		if (user.getUserroles() == null || user.getUserroles().isEmpty()) {
			return "Brak roli";
		}

		StringBuilder roles = new StringBuilder();
		for (Userrole userrole : user.getUserroles()) {
			if (userrole.getRemoveDate() == null) { // Tylko aktywne role
				if (roles.length() > 0) {
					roles.append(", ");
				}
				roles.append(userrole.getRole().getRoleName());
			}
		}

		return roles.length() > 0 ? roles.toString() : "Brak roli";
	}

	public void onRowSelect(SelectEvent<User> event) {
		selectedUser = event.getObject();
	}

}
