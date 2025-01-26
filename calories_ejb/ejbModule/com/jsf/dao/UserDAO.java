package com.jsf.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import com.jsf.entities.User;

@Stateless
public class UserDAO {

	@PersistenceContext
	protected EntityManager em;

	public void insert(User user) {
		em.persist(user);
	}

	public User update(User user) {
		return em.merge(user);
	}

	public void delete(User user) {
		em.remove(em.merge(user));
	}

	public User find(Object id) {
		return em.find(User.class, id);
	}

	public List<User> getFullList() {
		List<User> list = null;

		Query query = em.createQuery("select u from User u");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<User> getPagedList(int offset, int pageSize, Map<String, Object> searchParams) {
        List<User> list = null;

        String select = "SELECT u ";
        String from = "FROM User u JOIN u.userroles ur JOIN ur.role r ";
        String where = "WHERE ur.removeDate IS NULL ";
        String orderby = "ORDER BY u.login ASC";

        if (searchParams.containsKey("login")) {
            where += "AND u.login LIKE :login ";
        }

        Query query = em.createQuery(select + from + where + orderby);
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);

        if (searchParams.containsKey("login")) {
            query.setParameter("login", searchParams.get("login") + "%");
        }

        try {
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Metoda do zliczania rekordów
    public int countUsers(Map<String, Object> searchParams) {
        String select = "SELECT COUNT(u) ";
        String from = "FROM User u JOIN u.userroles ur JOIN ur.role r ";
        String where = "WHERE ur.removeDate IS NULL ";

        if (searchParams.containsKey("login")) {
            where += "AND u.login LIKE :login ";
        }

        Query query = em.createQuery(select + from + where);

        if (searchParams.containsKey("login")) {
            query.setParameter("login", searchParams.get("login") + "%");
        }

        try {
            Long count = (Long) query.getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

	public User getUserFromDatabase(String login, String pass) {

		User u = null;

		try {
			Query query = em.createQuery("SELECT u FROM User u WHERE u.login = :login AND u.password = :password");
			query.setParameter("login", login);
			query.setParameter("password", pass);
			u = (User) query.getSingleResult();
		} catch (Exception e) {
			System.err.println("Błąd podczas wyszukiwania użytkownika w bazie danych: " + e.getMessage());
		}

		return u;
	}

	public List<String> getUserRolesFromDatabase(User user) {

		ArrayList<String> roles = new ArrayList<String>();

		try {
	        Query query = em.createQuery(
	                "SELECT r.roleName " +
	                "FROM Role r " +
	                "JOIN r.userroles ur " +
	                "WHERE ur.user = :user AND ur.removeDate IS NULL"
	            );
	        query.setParameter("user", user);
	        
	        roles.addAll(query.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return roles;
	}
	
	public User findByLogin(String login) {
	    try {
	        Query query = em.createQuery("SELECT u FROM User u WHERE u.login = :login");
	        query.setParameter("login", login);
	        return (User) query.getSingleResult();
	    } catch (Exception e) {
	        return null;
	    }
	}
}
