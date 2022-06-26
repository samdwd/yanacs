package net.javaguides.springboot.springsecurity.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Profile {

    public Long getId() {
		return id;
	}


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;
    private String direccion;
    
    @ManyToOne
    @JoinColumn
    private User users;

	public Profile(String phone, String direccion, User users) {
		this.phone = phone;
		this.direccion = direccion;
		this.users = users;
	}

	public Profile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Profile(Long id2, String phone2, String direccion2, User u) {
		// TODO Auto-generated constructor stub
		this.id = id2;
		this.phone = phone2;
		this.direccion = direccion2;
		this.users = u;
	}

	public String getPhone() {
		return phone;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

}
