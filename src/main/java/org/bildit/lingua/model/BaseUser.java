package org.bildit.lingua.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.bildit.lingua.common.BaseEntity;
import org.hibernate.validator.constraints.Email;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "base_users")
public class BaseUser extends BaseEntity {
	
private static final long serialVersionUID = 1L;
	
	@Size(min = 2, max = 25)
	private String firstName;
	@Size(min = 2, max = 25)
	private String lastName;
	@Size(min = 4, max = 25)
	private String username;
	@Size(min = 6, max = 80)
	@NotNull
	private String password;
	@Email
	@Size(min = 8, max=40)
	private String email;
	
	@Column(columnDefinition="TINYINT(1)")
	private boolean enabled;
	private String authority;
	
	public BaseUser() {
		/** Empty default constructor */
	}
	
	/** Constructor with params */
	public BaseUser(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getAuthority() {
		return authority;
	}
	
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
}