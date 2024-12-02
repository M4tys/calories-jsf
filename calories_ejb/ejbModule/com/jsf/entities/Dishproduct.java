package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the dishproducts database table.
 * 
 */
@Entity
@Table(name="dishproducts")
@IdClass(DishproductPK.class)
@NamedQuery(name="Dishproduct.findAll", query="SELECT d FROM Dishproduct d")
public class Dishproduct implements Serializable {
	private static final long serialVersionUID = 1L;

	private float quantity;

    @Id
    @Column(name = "idDish")
    private Integer idDish;

    @Id
    @Column(name = "idProduct")
    private Integer idProduct;
	
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

	public float getQuantity() {
		return this.quantity;
	}

	public void setQuantity(float quantity) {
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