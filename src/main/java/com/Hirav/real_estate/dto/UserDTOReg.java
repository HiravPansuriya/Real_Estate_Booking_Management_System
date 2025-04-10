package com.Hirav.real_estate.dto;

import com.Hirav.real_estate.entity.enums.Role;

public class UserDTOReg {
	private String username;
	private String password;
	private String email;
	private Role role;
	
	public UserDTOReg() {
		super();
	}

	public UserDTOReg(String username, String password, String email, Role role) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserDTOReg [username=" + username + ", password=" + password + ", email=" + email + ", role=" + role
				+ "]";
	}
	
}
