package com.jsf.dao;

import com.jsf.entities.Userrole;

import java.util.List;

import com.jsf.entities.Role;
import com.jsf.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Stateless
public class UserRoleDAO {
	@PersistenceContext
	private EntityManager em;

	public void insert(Userrole userRole) {
		em.persist(userRole);
	}

	public void update(Userrole userRole) {
		em.merge(userRole);
	}

	public Role findByName(String roleName) {
		try {
			TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class);
			query.setParameter("roleName", roleName);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public Userrole findActiveRoleByUser(User user) {
		try {
			Query query = em.createQuery("SELECT ur FROM Userrole ur WHERE ur.user = :user AND ur.removeDate IS NULL");
			query.setParameter("user", user);
			return (Userrole) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<Role> getAllRoles() {
		try {
			TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.isActive = 1", Role.class);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
}
