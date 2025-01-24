package com.jsfcourse.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;

import com.jsf.dao.UserDAO;
import com.jsf.entities.User;

@Named
@ViewScoped
public class UserListBB implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final String PAGE_USER_EDIT = "/pages/admin/userEdit?faces-redirect=true";

	private String login;
		
	@Inject
	ExternalContext extcontext;
	
	@Inject
	FacesContext ctx;
	
	@Inject
	Flash flash;
	
	@EJB
	UserDAO userDAO;
		
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public List<User> getFullList(){
		return userDAO.getFullList();
	}
	
	public List<User> getList(){
		List<User> list = null;
		
		//1. Prepare search params
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		if (login != null && login.length() > 0){
			searchParams.put("login", login);
		}
		
		//2. Get list
		list = userDAO.getList(searchParams);
		
		return list;
	}

	public String editUser(User user){

		flash.put("user", user);
		
		return PAGE_USER_EDIT;
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
	        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas zmiany statusu użytkownika!", null));
	        e.printStackTrace();
	    }
	}



}
