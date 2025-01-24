package com.jsfcourse.product;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.view.ViewScoped;

import com.jsf.dao.ProductDAO;
import com.jsf.entities.Product;

@Named
@ViewScoped
public class ProductListBB implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String PAGE_PRODUCT_EDIT = "productEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String productName;
	private LazyDataModel<Product> lazyModel;

	@Inject
	ExternalContext extcontext;
	
	@Inject
	FacesContext ctx;

	@Inject
	Flash flash;

	@Inject
	ProductDAO productDAO;
	
    @PostConstruct
    public void init() {
    	
        lazyModel = new LazyDataModel<Product>() {
			private static final long serialVersionUID = 1L;

			@Override
			public int count(Map<String, FilterMeta> arg0) {
				Map<String, Object> searchParams = new HashMap<String, Object>();

				if (productName != null && productName.length() > 0) {
					searchParams.put("productName", productName);
				}
				return productDAO.countProducts(searchParams);
			}

			@Override
			public List<Product> load(int offset, int pageSize, Map<String, SortMeta> arg2, Map<String, FilterMeta> arg3) {
				Map<String, Object> searchParams = new HashMap<String, Object>();

				if (productName != null && productName.length() > 0) {
					searchParams.put("productName", productName);
				}
				return productDAO.getPagedList(offset, pageSize, searchParams);
			}
        	
        };
        
    }

    public LazyDataModel<Product> getLazyModel() {
        return lazyModel;
    }

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<Product> getFullList() {
		return productDAO.getFullList();
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
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usunięto produkt: " + product.getProductName(), null));
		return PAGE_STAY_AT_THE_SAME;
	}
}
