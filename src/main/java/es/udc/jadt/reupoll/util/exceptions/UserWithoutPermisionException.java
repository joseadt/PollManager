package es.udc.jadt.reupoll.util.exceptions;

import es.udc.jadt.reupoll.model.userprofile.UserProfile;

public class UserWithoutPermisionException extends Exception {

	private static final String DEFAULT_MESSAGE = "User %s doesn't have permmisions to edit this entity : %s";
	
	private Object entity;
	
	public UserWithoutPermisionException(UserProfile user, Object object) {
		super(String.format(DEFAULT_MESSAGE, user.getEmail(), object.toString()));
		this.entity = object;
	}

	public Object entity() {
		return entity;
	}
}
