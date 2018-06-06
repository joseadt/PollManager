package es.udc.jadt.arbitrium.model.entities.poll;

public enum PollType {

	PROPOSAL("PROPOSAL", false);

	private String name;

	private boolean multiselection;

	PollType(String name, boolean isMultiSelection) {
		this.multiselection = isMultiSelection;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public String longName() {
		return "PollType.".concat(name);
	}

	public boolean isMultiselection() {
		return multiselection;
	}

}
