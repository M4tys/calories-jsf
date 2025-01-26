package com.jsfcourse.dish;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.enterprise.context.RequestScoped;
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
	private List<Dish> userDishes;

	@Inject
	private ExternalContext extContext;

	@Inject
	DishDAO dishDAO;

	@Inject
	Flash flash;

	@Inject
	FacesContext context;

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public List<Dish> getUserDishes() {

		HttpSession session = (HttpSession) extContext.getSession(false);

		if (session != null) {
			RemoteClient<?> remoteClient = (RemoteClient<?>) session.getAttribute("remoteClient");

			if (remoteClient != null) {
				User loggedInUser = (User) remoteClient.getDetails();

				if (loggedInUser != null) {
					userDishes = dishDAO.getDishesByUser(loggedInUser);
				}

			}
		}
		return userDishes;
	}

	public String deleteDish(Dish dish) {
		dishDAO.delete(dish);
		return PAGE_STAY_AT_THE_SAME;
	}

	public String addDish() {

		HttpSession session = (HttpSession) extContext.getSession(false);

		if (session != null) {
			RemoteClient<?> remoteClient = (RemoteClient<?>) session.getAttribute("remoteClient");

			if (remoteClient != null) {
				User loggedInUser = (User) remoteClient.getDetails();

				if (loggedInUser != null && dishName != null && !dishName.isEmpty()) {

					try {

						if (dishDAO.dishExistsForUser(dishName, loggedInUser)) {
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Danie o takiej nazwie juz istnieje", null));
							return PAGE_STAY_AT_THE_SAME;
						}

						Dish newDish = new Dish();
						newDish.setDishName(dishName);
						newDish.setUser(loggedInUser);
						dishDAO.insert(newDish);
					} catch (Exception e) {
						e.printStackTrace();
						context.addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
						return PAGE_STAY_AT_THE_SAME;
					}

				}
			}
		}
		return PAGE_STAY_AT_THE_SAME;
	}

	public String showDishDetails(Dish dish) {
		flash.put("dish", dish);

		return PAGE_DISH_DETAILS;
	}

}
