package com.jsfcourse.dish;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;

import com.jsf.entities.Dish;
import com.jsf.entities.Dishproduct;
import com.jsf.dao.DishProductDAO;
import com.jsf.dao.DishDAO;

import java.io.IOException;
import java.util.List;

@Named
@RequestScoped
public class DishDetailsBB {
	private Dish dish = new Dish();
	private Dish loaded = null;

	@Inject
	FacesContext context;

	@Inject
	private DishProductDAO dishProductDAO;

	@Inject
	private DishDAO dishDAO;
	
	@Inject
	Flash flash;

	public Dish getDish() {
		return dish;
	}

	public void onLoad() throws IOException {

		// 2. load person passed through flash
		loaded = (Dish) flash.get("dish");

		// cleaning: attribute received => delete it from session
		if (loaded != null) {
			dish = loaded;
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			// if (!context.isPostback()) { //possible redirect
			// context.getExternalContext().redirect("personList.xhtml");
			// context.responseComplete();
			// }
		}

	}

	public List<Dishproduct> getList() {
		return dishProductDAO.getDishProductsByDish(dish);
	}

}
