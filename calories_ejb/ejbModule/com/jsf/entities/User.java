package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idUser;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deactivateDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date editDate;

	private byte isActive;

	private String login;

	@Lob
	private String password;

	private String userName;

	//bi-directional many-to-one association to Dish
	@OneToMany(mappedBy="user")
	private List<Dish> dishs;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="editedBy")
	private User user;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="user")
	private List<User> users;

	//bi-directional many-to-one association to Userrole
	@OneToMany(mappedBy="user", fetch= FetchType.EAGER)
	private List<Userrole> userroles;

	public User() {
	}

	public Integer getIdUser() {
		return this.idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDeactivateDate() {
		return this.deactivateDate;
	}

	public void setDeactivateDate(Date deactivateDate) {
		this.deactivateDate = deactivateDate;
	}

	public Date getEditDate() {
		return this.editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Dish> getDishs() {
		return this.dishs;
	}

	public void setDishs(List<Dish> dishs) {
		this.dishs = dishs;
	}

	public Dish addDish(Dish dish) {
		getDishs().add(dish);
		dish.setUser(this);

		return dish;
	}

	public Dish removeDish(Dish dish) {
		getDishs().remove(dish);
		dish.setUser(null);

		return dish;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setUser(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setUser(null);

		return user;
	}

	public List<Userrole> getUserroles() {
		return this.userroles;
	}

	public void setUserroles(List<Userrole> userroles) {
		this.userroles = userroles;
	}

	public Userrole addUserrole(Userrole userrole) {
		getUserroles().add(userrole);
		userrole.setUser(this);

		return userrole;
	}

	public Userrole removeUserrole(Userrole userrole) {
		getUserroles().remove(userrole);
		userrole.setUser(null);

		return userrole;
	}

}