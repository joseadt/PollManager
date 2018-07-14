package es.udc.jadt.arbitrium.controller.group;

public class GroupForm {

	private String groupName;

	private boolean isPrivate;

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return this.groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the isPrivate
	 */
	public boolean isPrivate() {
		return this.isPrivate;
	}

	/**
	 * @param isPrivate
	 *            the isPrivate to set
	 */
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

}
