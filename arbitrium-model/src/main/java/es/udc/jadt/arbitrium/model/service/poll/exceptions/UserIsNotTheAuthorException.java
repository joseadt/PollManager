package es.udc.jadt.arbitrium.model.service.poll.exceptions;

public class UserIsNotTheAuthorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3793098099565980756L;

	private Long userId;

	private Long pollId;

	public static final String DEFAULT_MESSAGE_FORMAT = "User with ID %s is not the author of the poll with ID %s";

	public UserIsNotTheAuthorException(Long userId, Long pollId) {
		super(String.format(DEFAULT_MESSAGE_FORMAT, userId.toString(), pollId.toString()));
		this.userId = userId;
		this.pollId = pollId;

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPollId() {
		return pollId;
	}

	public void setPollId(Long pollId) {
		this.pollId = pollId;
	}

}
