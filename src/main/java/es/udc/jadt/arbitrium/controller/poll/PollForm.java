package es.udc.jadt.arbitrium.controller.poll;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.poll.PollType;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;

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
	}

	public PollForm(Poll poll) {
		this.title = poll.getName();
		this.description = poll.getDescription();
		this.options = new ArrayList<String>();
		for (PollOption option : poll.getOptions()) {
			options.add(option.getDescription());
		}

		this.pollType = poll.getPollType();
		this.endDate = Date.from(poll.getEndDate());

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

	public Poll getPoll() {
		Poll poll = new Poll();
		poll.setDescription(this.description);
		poll.setPollType(this.pollType);
		poll.setEndDate(Instant.ofEpochMilli(this.endDate.getTime()));
		poll.setName(this.title);

		return poll;
	}

}
