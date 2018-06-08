package es.udc.jadt.arbitrium.controller.poll;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.jadt.arbitrium.model.entities.poll.PollType;

public class PollForm {

	@NotBlank
	private String title;

	private String description;

	private List<String> options;

	private PollType pollType;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	public PollForm() {
		this.options = new ArrayList<String>();
		options.add("Opcion1");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public PollType getPollType() {
		return pollType;
	}

	public void setPollType(PollType pollType) {
		this.pollType = pollType;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public PollType[] getPollTypes() {

		return PollType.values();
	}

}
