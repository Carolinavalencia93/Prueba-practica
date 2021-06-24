package co.com.ceiba.mobile.pruebadeingreso.models;

import java.io.Serializable;

public class Users implements Serializable {

	int id;
	String name;
	String email;
	String phone;

	public Users(int id, String name, String email, String phone) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getphone() {
		return phone;
	}

	public void setphone(String phone) {
		this.phone = phone;
	}
}
