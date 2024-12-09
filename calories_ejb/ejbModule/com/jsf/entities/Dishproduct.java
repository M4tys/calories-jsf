package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the dishproducts database table.
 * 
 */
@Entity
@Table(name="dishproducts")
@NamedQuery(name="Dishproduct.findAll", query="SELECT d FROM Dishproduct d")
public class Dishproduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idDishproduct;

	private double quantity;

	//bi-directional many-to-one association to Dish
	@ManyToOne
	@JoinColumn(name="idDish")
	private Dish dish;

	//bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name="idProduct")
	private Product product;

	public Dishproduct() {
	}

	public Integer getIdDishproduct() {
		return this.idDishproduct;
	}

	public void setIdDishproduct(Integer idDishproduct) {
		this.idDishproduct = idDishproduct;
	}

	public double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Dish getDish() {
		return this.dish;
	}

	public void setDish(Dish dish) {
		this.dish = dish;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}