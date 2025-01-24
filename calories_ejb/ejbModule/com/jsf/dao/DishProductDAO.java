package com.jsf.dao;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.jsf.entities.Dish;
import com.jsf.entities.Dishproduct;
import com.jsf.entities.Product;

@Stateless
public class DishProductDAO {
	
	@PersistenceContext
    protected EntityManager em;
	
    public void insert(Dishproduct dishProduct) {
        if (dishProduct != null) {
            em.persist(dishProduct);
        }
    }
    
	public Dishproduct update(Dishproduct dishproduct) {
		return em.merge(dishproduct);
	}
	
	public void delete(Dishproduct dishproduct) {
	     em.remove(em.merge(dishproduct));
	}
	
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
    
    public Dishproduct findByDishAndProduct(Dish dish, Product product) {
        try {
            Query query = em.createQuery(
                "SELECT dp FROM Dishproduct dp WHERE dp.dish = :dish AND dp.product = :product"
            );
            query.setParameter("dish", dish);
            query.setParameter("product", product);
            
            // Pobierz pojedynczy wynik
            return (Dishproduct) query.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            // Jeśli nie znaleziono wpisu, zwróć null
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}