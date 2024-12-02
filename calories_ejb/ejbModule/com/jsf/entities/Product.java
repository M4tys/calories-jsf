package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idProduct;

	private float calories;

	private float carbohydrates;

	private float fats;

	private String productName;

	private float proteins;

	//bi-directional many-to-one association to Dishproduct
	@OneToMany(mappedBy="product")
	private List<Dishproduct> dishproducts;

	public Product() {
	}

	public Integer getIdProduct() {
		return this.idProduct;
	}

	public void setIdProduct(Integer idProduct) {
		this.idProduct = idProduct;
	}

	public float getCalories() {
		return this.calories;
	}

	public void setCalories(float calories) {
		this.calories = calories;
	}

	public float getCarbohydrates() {
		return this.carbohydrates;
	}

	public void setCarbohydrates(float carbohydrates) {
		this.carbohydrates = carbohydrates;
	}

	public float getFats() {
		return this.fats;
	}

	public void setFats(float fats) {
		this.fats = fats;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public float getProteins() {
		return this.proteins;
	}

	public void setProteins(float proteins) {
		this.proteins = proteins;
	}

	public List<Dishproduct> getDishproducts() {
		return this.dishproducts;
	}

	public void setDishproducts(List<Dishproduct> dishproducts) {
		this.dishproducts = dishproducts;
	}

	public Dishproduct addDishproduct(Dishproduct dishproduct) {
		getDishproducts().add(dishproduct);
		dishproduct.setProduct(this);

		return dishproduct;
	}

	public Dishproduct removeDishproduct(Dishproduct dishproduct) {
		getDishproducts().remove(dishproduct);
		dishproduct.setProduct(null);

		return dishproduct;
	}

}