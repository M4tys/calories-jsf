package com.jsfcourse.dish;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import com.jsf.dao.ProductDAO;
import com.jsf.entities.Product;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
@FacesConverter(value="productConverter", managed = true)
public class ProductConverter implements Converter<Product> {

    @Inject
    private ProductDAO productDAO;

    @Override
    public Product getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0){
            int id = Integer.parseInt(value);
            return productDAO.find(id);
        } else {
        	return null;
        }
       
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Product product) {
        if (product != null) {
        	return product.getIdProduct().toString();
        } else {
        	return null;
        }
        
    }
}
