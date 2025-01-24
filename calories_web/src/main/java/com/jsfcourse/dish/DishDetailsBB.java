package com.jsfcourse.dish;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;

import com.jsf.entities.Dish;
import com.jsf.entities.Dishproduct;
import com.jsf.entities.Product;
import com.jsf.dao.DishDAO;
import com.jsf.dao.DishProductDAO;
import com.jsf.dao.ProductDAO;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Named
@ViewScoped
public class DishDetailsBB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_ADD_PRODUCT = "/pages/user/addProduct?faces-redirect=true";
	private Dish dish = new Dish();
	private Dish loaded = null;

	@Inject
	FacesContext ctx;

	@Inject
	DishProductDAO dishProductDAO;

	@Inject
	DishDAO dishDAO;

	@Inject
	ProductDAO productDAO;

	@Inject
	Flash flash;

	public Dish getDish() {
		return dish;
	}
	
	public void updateCalories() {
		List<Dishproduct> dishProducts = dishProductDAO.getDishProductsByDish(dish);
        double totalCalories = 0.0;
        double totalProteins = 0.0;
        double totalCarbohydrates = 0.0;
        double totalFats = 0.0;
        
        for (Dishproduct dp : dishProducts) {
            double quantity = dp.getQuantity();
            Product product = dp.getProduct();

            totalCalories += product.getCalories() * quantity/100;
            totalProteins += product.getProteins() * quantity/100;
            totalCarbohydrates += product.getCarbohydrates() * quantity/100;
            totalFats += product.getFats() * quantity/100;
        }
        
        totalCalories = new BigDecimal(totalCalories).setScale(2, RoundingMode.HALF_UP).doubleValue();
        totalProteins = new BigDecimal(totalProteins).setScale(2, RoundingMode.HALF_UP).doubleValue();
        totalCarbohydrates = new BigDecimal(totalCarbohydrates).setScale(2, RoundingMode.HALF_UP).doubleValue();
        totalFats = new BigDecimal(totalFats).setScale(2, RoundingMode.HALF_UP).doubleValue();
        
        dish.setTotalCalories(totalCalories);
        dish.setTotalProteins(totalProteins);
        dish.setTotalCarbohydrates(totalCarbohydrates);
        dish.setTotalFats(totalFats);

        // Save the updated totals to the database
        dishDAO.update(dish);
	}

	public void onLoad() throws IOException {
		
		loaded = (Dish) flash.get("dish");

		if (loaded != null) {
			dish = loaded;
			
			updateCalories();
	        
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
		}

	}

	public List<Dishproduct> getList() {
		return dishProductDAO.getDishProductsByDish(dish);
	}

	public String removeProduct(Dishproduct dishProduct) {
		try {
			
			dishProductDAO.delete(dishProduct);
			updateCalories();
			
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Produkt usunięty", null));
		} catch (Exception e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nie udało się usunąć produktu", null));
			e.printStackTrace();
		}

		return PAGE_STAY_AT_THE_SAME;
	}

	public String addProduct(Dish dish) {
		flash.put("dish", dish);

		return PAGE_ADD_PRODUCT;
	}
}
