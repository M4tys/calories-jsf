package com.jsf.entities;

import java.io.Serializable;
import java.util.Objects;

public class UserrolePK implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer idUser;
    private Integer idRole;

    public UserrolePK() {
    }

    public UserrolePK(Integer idUser, Integer idRole) {
        this.idUser = idUser;
        this.idRole = idRole;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserrolePK that = (UserrolePK) o;
        return idUser == that.idUser && idRole == that.idRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idRole);
    }
}
