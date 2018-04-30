package es.udc.jadt.reupoll.model.request;

import java.sql.Date;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import es.udc.jadt.reupoll.model.group.UserGroup;
import es.udc.jadt.reupoll.model.userprofile.UserProfile;

@Entity
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private UserGroup userGroup;

	@ManyToOne
	private UserProfile requestor;

	@ManyToOne
	private UserProfile admin;


	private Date createdDate;

	public Request() {

	}

	public Request(UserGroup group, UserProfile admin) {
		this.userGroup = group;
		this.admin = admin;
		this.createdDate = new Date(Calendar.getInstance().getTimeInMillis());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserGroup getGroup() {
		return userGroup;
	}

	public void setGroup(UserGroup group) {
		this.userGroup = group;
	}

	public UserProfile getAdmin() {
		return admin;
	}

	public void setAdmin(UserProfile admin) {
		this.admin = admin;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
