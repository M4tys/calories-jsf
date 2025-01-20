package com.jsf.dao;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import com.jsf.entities.Dish;
import com.jsf.entities.Dishproduct;

@Stateless
public class DishProductDAO {
	
	@PersistenceContext
    protected EntityManager em;
	
	public List<Dishproduct> getDishProductsByDish(Dish dish) {
        List<Dishproduct> dishProducts = null;
        try {
            Query query = em.createQuery(
                "SELECT dp FROM Dishproduct dp WHERE dp.dish = :dish"
            );
            query.setParameter("dish", dish);
            dishProducts = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dishProducts;
    }

}