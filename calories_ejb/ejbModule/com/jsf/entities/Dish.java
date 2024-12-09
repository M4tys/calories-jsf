package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;


/**
 * The persistent class for the dish database table.
 * 
 */
@Entity
@NamedQuery(name="Dish.findAll", query="SELECT d FROM Dish d")
public class Dish implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idDish;

	private String dishName;

	private double totalCalories;

	private double totalCarbohydrates;

	private double totalFats;

	private double totalProteins;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="idUser")
	private User user;

	//bi-directional many-to-one association to Dishproduct
	@OneToMany(mappedBy="dish")
	private List<Dishproduct> dishproducts;

	public Dish() {
	}

	public Integer getIdDish() {
		return this.idDish;
	}

	public void setIdDish(Integer idDish) {
		this.idDish = idDish;
	}

	public String getDishName() {
		return this.dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public double getTotalCalories() {
		return this.totalCalories;
	}

	public void setTotalCalories(double totalCalories) {
		this.totalCalories = totalCalories;
	}

	public double getTotalCarbohydrates() {
		return this.totalCarbohydrates;
	}

	public void setTotalCarbohydrates(double totalCarbohydrates) {
		this.totalCarbohydrates = totalCarbohydrates;
	}

	public double getTotalFats() {
		return this.totalFats;
	}

	public void setTotalFats(double totalFats) {
		this.totalFats = totalFats;
	}

	public double getTotalProteins() {
		return this.totalProteins;
	}

	public void setTotalProteins(double totalProteins) {
		this.totalProteins = totalProteins;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Dishproduct> getDishproducts() {
		return this.dishproducts;
	}

	public void setDishproducts(List<Dishproduct> dishproducts) {
		this.dishproducts = dishproducts;
	}

	public Dishproduct addDishproduct(Dishproduct dishproduct) {
		getDishproducts().add(dishproduct);
		dishproduct.setDish(this);

		return dishproduct;
	}

	public Dishproduct removeDishproduct(Dishproduct dishproduct) {
		getDishproducts().remove(dishproduct);
		dishproduct.setDish(null);

		return dishproduct;
	}

}