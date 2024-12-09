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

	public Product find(Object id) {
		return em.find(Product.class, id);
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
	
	public List<Product> getList(Map<String, Object> searchParams) {
		List<Product> list = null;

		// 1. Build query string with parameters
		String select = "select p ";
		String from = "from Product p ";
		String where = "";
		String orderby = "order by p.productName asc, p.productName";

		// search for surname
		String productName = (String) searchParams.get("productName");
		if (productName != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.productName like :productName ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (productName != null) {
			query.setParameter("productName", productName+"%");
		}

		// ... other parameters ... 

		// 4. Execute query and retrieve list of Person objects
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
