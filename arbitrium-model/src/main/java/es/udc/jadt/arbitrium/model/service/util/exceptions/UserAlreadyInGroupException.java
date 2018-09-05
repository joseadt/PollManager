package es.udc.jadt.arbitrium.model.service.util.exceptions;

public class UserAlreadyInGroupException extends Exception {


	/**
	 *
	 */
	private static final long serialVersionUID = -1924570059979673220L;

	private static final String MESSAGE_FORMAT = "User with email %s is already in the group %s";

	private final String email;

	private final Long groupId;

	public UserAlreadyInGroupException(String email, Long groupId) {
		super(String.format(MESSAGE_FORMAT, email, groupId.toString()));
		this.email = email;
		this.groupId = groupId;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
		return this.groupId;
	}



	public static String messageFormatExample(String email, Long groupId) {
		return String.format(MESSAGE_FORMAT, email, groupId.toString());
	}
}
