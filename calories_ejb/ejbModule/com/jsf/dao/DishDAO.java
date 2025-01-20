package com.jsf.dao;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.jsf.entities.Dish;
import com.jsf.entities.User;

@Stateless
public class DishDAO {
	
    @PersistenceContext
    protected EntityManager em;

    public List<Dish> getDishesByUser(User user) {
        List<Dish> dishes = null;
        try {
            Query query = em.createQuery(
                "SELECT d FROM Dish d WHERE d.user = :user"
            );
            query.setParameter("user", user);
            dishes = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dishes;
    }
    
    public boolean dishExistsForUser(String dishName, User user) {
        try {
            Query query = em.createQuery(
                "SELECT COUNT(d) FROM Dish d WHERE d.dishName = :dishName AND d.user = :user"
            );
            query.setParameter("dishName", dishName);
            query.setParameter("user", user);
            Long count = (Long) query.getSingleResult();
            return count > 0; // Jeśli liczba > 0, danie już istnieje
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
	public void insert(Dish dish) {
		em.persist(dish);
	}
    
	public void delete(Dish dish) {
	     em.remove(em.merge(dish));
	}
	
	public void update(Dish dish) {
	    em.merge(dish);
	}
	
	public Dish find(Object id) {
		return em.find(Dish.class, id);
	}
}
