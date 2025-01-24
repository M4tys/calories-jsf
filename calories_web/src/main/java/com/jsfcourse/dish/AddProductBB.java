package com.jsfcourse.dish;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.jsf.dao.DishProductDAO;
import com.jsf.dao.ProductDAO;
import com.jsf.entities.Dish;
import com.jsf.entities.Dishproduct;
import com.jsf.entities.Product;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;

@Named
@ViewScoped
public class AddProductBB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String PAGE_SHOW_DETAILS = "/pages/user/dishDetails?faces-redirect=true";
	private Dish dish = new Dish();
	private Dish loaded = null;
	private Product selectedProduct;
	private double quantity;
	
	@Inject
	ProductDAO productDAO;
	
	@Inject
	DishProductDAO dishProductDAO;

	@Inject
	Flash flash;

	@Inject
	FacesContext ctx;
	
	public Dish getDish() {
		return dish;
	}
	
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
    
    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public List<Product> getAllProducts() {
        return productDAO.getFullList();
    }
    
    public String dishDetails() {
    	flash.put("dish", dish);
    	return PAGE_SHOW_DETAILS;
    }

	public void onLoad() throws IOException {

		loaded = (Dish) flash.get("dish");

		if (loaded != null) {
			dish = loaded;
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
		}
	}
	
    
    public String addProductToDish() {
        Dishproduct existingDishProduct = dishProductDAO.findByDishAndProduct(dish, selectedProduct);

        if (existingDishProduct != null) {
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Produkt już został dodany do dania", null));
            return null;
        }

        // Produkt nie istnieje, dodajemy go
        Dishproduct newDishProduct = new Dishproduct();
        newDishProduct.setDish(dish);
        newDishProduct.setProduct(selectedProduct);
        newDishProduct.setQuantity(quantity);
        dishProductDAO.insert(newDishProduct);

        flash.put("dish", newDishProduct.getDish());
        return PAGE_SHOW_DETAILS;
    }

    
    

}
