package com.jsf.dao;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.jsf.entities.Product;

@Stateless
public class ProductDAO {

	@PersistenceContext
	protected EntityManager em;

	public void insert(Product product) {
		em.persist(product);
	}

	public Product update(Product product) {
		return em.merge(product);
	}

	public void delete(Product product) {
	     em.remove(em.merge(product));
	}

	public Product find(Integer id) {
		return em.find(Product.class, id);
	}
	
	public List<Product> getPagedList(int offset, int pageSize, Map<String, Object> searchParams) {
	    List<Product> list = null;

	    String queryString = "SELECT p FROM Product p ";
	    String where = "";
	    String orderBy = " ORDER BY p.productName ASC";

	    if (searchParams.containsKey("productName")) {
	        where += "WHERE p.productName LIKE :productName ";
	    }

	    Query query = em.createQuery(queryString + where + orderBy);
	    query.setFirstResult(offset);
	    query.setMaxResults(pageSize);
	    
	    if (searchParams.containsKey("productName")) {
	        query.setParameter("productName", "%" + searchParams.get("productName") + "%");
	    }

	    try {
	        list = query.getResultList();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}

	
	public int countProducts(Map<String, Object> searchParams) {
	    String queryString = "SELECT COUNT(p) FROM Product p ";
	    String where = "";

	    if (searchParams.containsKey("productName")) {
	        where += "WHERE p.productName LIKE :productName ";
	    }

	    Query query = em.createQuery(queryString + where);

	    if (searchParams.containsKey("productName")) {
	        query.setParameter("productName", "%" + searchParams.get("productName") + "%");
	    }

	    try {
	        Long count = (Long) query.getSingleResult();
	        return count.intValue();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}


	public List<Product> getFullList() {
		List<Product> list = null;

		Query query = em.createQuery("select p from Product p");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
    public boolean existsByName(String productName) {
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(p) FROM Product p WHERE p.productName = :name", Long.class)
                    .setParameter("name", productName)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
