package es.udc.jadt.arbitrium.model.entities.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.userprofile.UserProfile;

@SuppressWarnings("serial")
@Entity
public class UserGroup implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true)
	private String name;

	@ManyToOne
	private UserProfile creator;

	@ManyToMany
	private List<UserProfile> members;

	@ManyToMany
	private List<Poll> polls;

	private Boolean isPrivate;

	public UserGroup() {
	}

	public UserGroup(String name, List<UserProfile> members, List<Poll> polls) {
		this.name = name;
		this.members = members;
		this.polls = polls;
		this.isPrivate = false;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserProfile> getMembers() {
		return this.members;
	}

	public void setMembers(List<UserProfile> members) {
		this.members = members;
	}

	public List<Poll> getPolls() {
		return this.polls;
	}

	public void setPolls(List<Poll> polls) {
		this.polls = polls;
	}

	public boolean addMember(UserProfile user) {
		if (this.members == null) {
			this.members = new ArrayList<>();
		}

		return this.members.add(user);
	}

	/**
	 * @return the creator
	 */
	public UserProfile getCreator() {
		return this.creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(UserProfile creator) {
		this.creator = creator;
	}

	/**
	 * @return the isPrivate
	 */
	public Boolean getIsPrivate() {
		return this.isPrivate;
	}

	/**
	 * @param isPrivate
	 *            the isPrivate to set
	 */
	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

}
