package es.udc.jadt.reupoll.model.userprofile;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import es.udc.jadt.reupoll.model.group.UserGroup;

@Entity
public class UserProfile implements Serializable {

	@Id
	private Long id;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String userName;

	private String encryptedPassword;

	@ManyToMany(fetch = FetchType.LAZY)
	private List<UserGroup> userGroups;


	public UserProfile() {
	}

	public UserProfile(String email, String userName, String encryptedPassword) {
		this.email = email;
		this.userName = userName;
		this.encryptedPassword = encryptedPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

}
