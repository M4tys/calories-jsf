package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;


/**
 * The persistent class for the userroles database table.
 * 
 */
@Entity
@Table(name="userroles")
@NamedQuery(name="Userrole.findAll", query="SELECT u FROM Userrole u")
public class Userrole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idUserrole;

	@Temporal(TemporalType.TIMESTAMP)
	private Date assignDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date removeDate;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="idRole")
	private Role role;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="idUser")
	private User user;

	public Userrole() {
	}

	public Integer getIdUserrole() {
		return this.idUserrole;
	}

	public void setIdUserrole(Integer idUserrole) {
		this.idUserrole = idUserrole;
	}

	public Date getAssignDate() {
		return this.assignDate;
	}

	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}

	public Date getRemoveDate() {
		return this.removeDate;
	}

	public void setRemoveDate(Date removeDate) {
		this.removeDate = removeDate;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}