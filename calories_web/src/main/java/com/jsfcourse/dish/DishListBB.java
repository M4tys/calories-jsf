package com.jsfcourse.dish;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.simplesecurity.RemoteClient;

import com.jsf.dao.DishDAO;
import com.jsf.entities.Dish;
import com.jsf.entities.User;


@Named
@RequestScoped
public class DishListBB {
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_DISH_DETAILS = "/pages/user/dishDetails?faces-redirect=true";
	private String dishName;

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	@Inject
	private ExternalContext extContext;

	@EJB
	private DishDAO dishDAO;

	@Inject
	Flash flash;
	
	@Inject
	FacesContext context;

	private List<Dish> userDishes;

	public List<Dish> getUserDishes() {
		if (userDishes == null) {
			// Pobierz RemoteClient z sesji
			RemoteClient<User> remoteClient = (RemoteClient<User>) extContext.getSessionMap().get("remoteClient");
			if (remoteClient != null) {
				User loggedInUser = remoteClient.getDetails(); // Pobierz szczegóły użytkownika
				if (loggedInUser != null) {
					userDishes = dishDAO.getDishesByUser(loggedInUser);
				}
			}
		}
		return userDishes;
	}

	public String deleteDish(Dish dish) {
		dishDAO.delete(dish);
		userDishes = null;
		return PAGE_STAY_AT_THE_SAME;
	}

	public String addDish() {
		RemoteClient<User> remoteClient = (RemoteClient<User>) extContext.getSessionMap().get("remoteClient");
		
		try {
		if (remoteClient != null) {
			User loggedInUser = remoteClient.getDetails(); // Pobierz szczegóły użytkownika
			if (loggedInUser != null && dishName != null && !dishName.isEmpty()) {
				
				if(dishDAO.dishExistsForUser(dishName, loggedInUser)) {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Danie o takiej nazwie juz istnieje", null)); 
					return PAGE_STAY_AT_THE_SAME;
				}

				Dish newDish = new Dish();
				newDish.setDishName(dishName);
				newDish.setUser(loggedInUser); // Ustaw użytkownika
				newDish.setTotalCalories(0); // Możesz ustawić domyślną wartość kalorii lub dodać pole w formularzu
				dishDAO.insert(newDish); // Zapisz nowe danie
				userDishes = null; // Odśwież listę dań
			}
		}
		return PAGE_STAY_AT_THE_SAME;
		} catch (Exception e) {
			e.printStackTrace();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}
	}

	public String showDishDetails(Dish dish) {
		flash.put("dish", dish);

		return PAGE_DISH_DETAILS;
	}

}
