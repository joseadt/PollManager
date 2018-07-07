package es.udc.jadt.arbitrium.controller.poll;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;
import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;
import es.udc.jadt.arbitrium.util.polltype.PollType;

public class PollForm {

	@NotBlank
	private String title;

	private String description;

	private List<PollOption> options;

	private PollType pollType;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	public PollForm() {
		this.options = new ArrayList<PollOption>();
	}

	public PollForm(Poll poll) {
		this.title = poll.getName();
		this.description = poll.getDescription();

		this.options = poll.getOptions();

		this.pollType = poll.getPollType();
		this.endDate = Date.from(poll.getEndDate());

	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<PollOption> getOptions() {
		return this.options;
	}

	public void setOptions(List<PollOption> options) {
		this.options = options;
	}


	public PollType getPollType() {
		return this.pollType;
	}

	public void setPollType(PollType pollType) {
		this.pollType = pollType;
	}

	public Date getEndDate() {
		return this.endDate;
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
		poll.setConfiguration(this.pollType.getConfiguration().getConfigurationParameters());

		if (!Boolean.TRUE.equals(poll.getConfiguration().getIsUserDefinedOptions())) {
			this.options = poll.getConfiguration().getDefaultOptions();
		}
		for (PollOption option : this.options) {
			option.setPoll(poll);
		}
		poll.setOptions(this.options);
		return poll;
	}

}
