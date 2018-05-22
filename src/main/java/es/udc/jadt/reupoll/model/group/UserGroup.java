package es.udc.jadt.reupoll.model.group;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import es.udc.jadt.reupoll.model.poll.Poll;
import es.udc.jadt.reupoll.model.userprofile.UserProfile;

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

	public UserGroup() {
	}

	public UserGroup(String name, List<UserProfile> members, List<Poll> polls) {
		this.name = name;
		this.members = members;
		this.polls = polls;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserProfile> getMembers() {
		return members;
	}

	public void setMembers(List<UserProfile> members) {
		this.members = members;
	}

	public List<Poll> getPolls() {
		return polls;
	}

	public void setPolls(List<Poll> polls) {
		this.polls = polls;
	}

}
