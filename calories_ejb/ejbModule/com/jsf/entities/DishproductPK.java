package com.jsf.entities;

import java.io.Serializable;
import java.util.Objects;


public class DishproductPK implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer idDish;
    private Integer idProduct;

    public DishproductPK() {
    }

    public DishproductPK(Integer idDish, Integer idProduct) {
        this.idDish = idDish;
        this.idProduct = idProduct;
    }

    public Integer getIdDish() {
        return idDish;
    }

    public void setIdDish(int idDish) {
        this.idDish = idDish;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishproductPK that = (DishproductPK) o;
        return idDish == that.idDish && idProduct == that.idProduct;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDish, idProduct);
    }
}
