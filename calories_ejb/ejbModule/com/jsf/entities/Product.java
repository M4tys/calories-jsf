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

	private double calories;

	private double carbohydrates;

	private double fats;

	private String productName;

	private double proteins;

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

	public double getCalories() {
		return this.calories;
	}

	public void setCalories(double calories) {
		this.calories = calories;
	}

	public double getCarbohydrates() {
		return this.carbohydrates;
	}

	public void setCarbohydrates(double carbohydrates) {
		this.carbohydrates = carbohydrates;
	}

	public double getFats() {
		return this.fats;
	}

	public void setFats(double fats) {
		this.fats = fats;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProteins() {
		return this.proteins;
	}

	public void setProteins(double proteins) {
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