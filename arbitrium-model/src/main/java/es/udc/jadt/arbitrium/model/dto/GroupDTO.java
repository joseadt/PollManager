package es.udc.jadt.arbitrium.model.dto;

public class GroupDTO {

	private long id;

	private String name;

	private int numMember;

	/**
	 * @param id
	 * @param name
	 * @param numMember
	 */
	public GroupDTO(long id, String name, int numMember) {
		this.id = id;
		this.name = name;
		this.numMember = numMember;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the numMember
	 */
	public int getNumMember() {
		return this.numMember;
	}

}
