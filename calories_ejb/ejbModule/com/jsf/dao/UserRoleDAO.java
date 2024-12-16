package com.jsf.dao;

import com.jsf.entities.Userrole;
import com.jsf.entities.Role;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class UserRoleDAO {
    @PersistenceContext
    private EntityManager em;

    public void create(Userrole userRole) {
        em.persist(userRole);
    }
    
    public Role findByName(String roleName) {
        TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class);
        query.setParameter("roleName", roleName);
        return query.getSingleResult();
    }
}
