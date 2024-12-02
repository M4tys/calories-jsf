package com.jsfcourse.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ejb.EJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.Flash;

import com.jsf.dao.ProductDAO;
import com.jsf.entities.Product;

@Named
@RequestScoped
public class ProductListBB {
	private static final String PAGE_PRODUCT_EDIT = "productEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String productName;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	ProductDAO productDAO;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<Product> getFullList() {
		return productDAO.getFullList();
	}

	public List<Product> getList() {
		List<Product> list = null;

		// 1. Prepare search params
		Map<String, Object> searchParams = new HashMap<String, Object>();

		if (productName != null && productName.length() > 0) {
			searchParams.put("productName", productName);
		}

		// 2. Get list
		list = productDAO.getList(searchParams);

		return list;
	}

	public String newProduct() {
		Product product = new Product();

		flash.put("product", product);

		return PAGE_PRODUCT_EDIT;
	}

	public String editProduct(Product product) {

		flash.put("product", product);

		return PAGE_PRODUCT_EDIT;
	}

	public String deleteProduct(Product product) {	
		productDAO.delete(product);
		return PAGE_STAY_AT_THE_SAME;
	}
}
