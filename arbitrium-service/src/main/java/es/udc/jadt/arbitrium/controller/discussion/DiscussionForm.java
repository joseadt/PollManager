package es.udc.jadt.arbitrium.controller.discussion;

public class DiscussionForm {

	private String title;

	private String description;

	private Long groupId;

	/**
	 * @param title
	 * @param description
	 * @param groupId
	 */
	public DiscussionForm(String title, String description, Long groupId) {
		this.title = title;
		this.description = description;
		this.groupId = groupId;
	}

	/**
	 *
	 */
	public DiscussionForm() {
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
		return this.groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
