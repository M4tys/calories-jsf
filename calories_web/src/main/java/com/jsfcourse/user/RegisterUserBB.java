package com.jsfcourse.user;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import com.jsf.dao.UserDAO;
import com.jsf.dao.UserRoleDAO;
import com.jsf.entities.User;
import com.jsf.entities.Userrole;
import com.jsf.entities.Role;

@Named
@RequestScoped
public class RegisterUserBB {
    private String login;
    private String userName;
    private String password;
    private String confirmPassword;
    private String role; // wartość: "USER" lub "DIETICIAN"
    
	@Inject
	FacesContext context;

    @EJB
    private UserDAO userDAO;

    @EJB
    private UserRoleDAO userRoleDAO;

    public String register() {
        // Sprawdzenie, czy hasła się zgadzają
        if (!password.equals(confirmPassword)) {
        	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne hasło", null)); 
        	return null;
        }

        try {
            // Tworzenie nowego użytkownika
            User user = new User();
            user.setLogin(login);
            user.setUserName(userName);                   
            user.setPassword(password); // Zakładając, że hasło jest już zahaszowane

            userDAO.insert(user); // Zapis użytkownika do bazy
            
            Role selectedRole = userRoleDAO.findByName(role);
            
            // Tworzenie roli użytkownika
            Userrole userrole = new Userrole();
            userrole.setUser(user);
            userrole.setRole(selectedRole);
            userRoleDAO.create(userrole);          
            // Zapis roli do bazy

            return "/pages/login/login?faces-redirect=true"; // Przekierowanie po sukcesie
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Gettery i Settery
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
