package com.jsfcourse.product;

import java.io.IOException;
import java.io.Serializable;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import com.jsf.dao.ProductDAO;
import com.jsf.entities.Product;

@Named
@ViewScoped
public class ProductEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_PRODUCT_LIST = "productList?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private Product product = new Product();
	private Product loaded = null;

	@EJB
	ProductDAO productDAO;

	@Inject
	FacesContext ctx;

	@Inject
	Flash flash;

	public Product getProduct() {
		return product;
	}

	public void onLoad() throws IOException {
		loaded = (Product) flash.get("product");

		if (loaded != null) {
			product = loaded;
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
		}

	}

	public String saveData() {
		// no Person object passed
		if (loaded == null) {
			return PAGE_STAY_AT_THE_SAME;
		}

		try {
			boolean isDuplicate = productDAO.existsByName(product.getProductName())
					&& (product.getIdProduct() == null || !product.getIdProduct().equals(loaded.getIdProduct()));

			if (isDuplicate) {
				ctx.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Produkt o tej nazwie już istnieje", null));
				return PAGE_STAY_AT_THE_SAME;
			}

			if (product.getProteins() <= 0 && product.getCarbohydrates() <= 0 && product.getFats() <= 0) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Nie można dodać produktu, który ma 0 składników odżywczych (białka, węglowodany, tłuszcze).",
						null));
				return PAGE_STAY_AT_THE_SAME;
			}

			double Calories = (product.getProteins() * 4) + (product.getCarbohydrates() * 4) + (product.getFats() * 4);

			Calories = Math.round(Calories * 100.0) / 100.0;
			product.setCalories(Calories);

			if (product.getIdProduct() == null) {
				// new record
				productDAO.insert(product);
			} else {
				// existing record
				productDAO.update(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "wystąpił błąd podczas zapisu", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		return PAGE_PRODUCT_LIST;
	}

}
