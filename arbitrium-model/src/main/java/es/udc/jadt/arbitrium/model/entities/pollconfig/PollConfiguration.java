package es.udc.jadt.arbitrium.model.entities.pollconfig;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import es.udc.jadt.arbitrium.model.entities.poll.Poll;

/**
 * The Class PollConfiguration.
 *
 * Class That maps the configuration of a single poll. It's not refereed to what
 * an user can configure of a poll based in it's poll type, but the actual
 * values of the different configurations
 *
 * User checkAttributes function to obtain a clear PollConfiguration with the
 * attributes validated and formated
 *
 * @author JADT
 */
@Entity
public class PollConfiguration implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 3540847492155219788L;

	@Id
	@Column(name = "poll_config_id")
	private Long pollId;

	@MapsId
	@OneToOne(mappedBy = "configuration")
	@JoinColumn(name = "poll_config_id")
	private Poll poll;


	private Integer maxVotesPerUser;

	private Integer minVotesPerUser;

	private Integer maxVotesPerOption;

	private Integer maxSelectableOptions;


	public PollConfiguration() {
		checkAttributes();
	}

	@Transient
	public boolean isMultiSelection() {
		return (this.maxSelectableOptions == null) ? true : this.maxSelectableOptions.intValue() > 1;
	}

	/**
	 * @return the maxSelectableOptions
	 */
	public Integer getMaxSelectableOptions() {
		return this.maxSelectableOptions;
	}

	/**
	 * @return the maxVotesPerOption
	 */
	public Integer getMaxVotesPerOption() {
		return this.maxVotesPerOption;
	}

	/**
	 * @return the maxVotesPerUser
	 */
	public Integer getMaxVotesPerUser() {
		return this.maxVotesPerUser;
	}

	/**
	 * @return the minVotesPerUser
	 */
	public Integer getMinVotesPerUser() {
		return this.minVotesPerUser;
	}

	/**
	 * @return the poll
	 */
	public Poll getPoll() {
		return this.poll;
	}

	/**
	 * @return the pollId
	 */
	public Long getPollId() {
		return this.pollId;
	}

	/**
	 * @param maxSelectableOptions
	 *            the maxSelectableOptions to set
	 */
	public void setMaxSelectableOptions(Integer maxSelectableOptions) {
		this.maxSelectableOptions = maxSelectableOptions;
	}

	/**
	 * @param maxVotesPerOption
	 *            the maxVotesPerOption to set
	 */
	public void setMaxVotesPerOption(Integer maxVotesPerOption) {
		this.maxVotesPerOption = maxVotesPerOption;
	}

	/**
	 * @param maxVotesPerUser
	 *            the maxVotesPerUser to set
	 */
	public void setMaxVotesPerUser(Integer maxVotesPerUser) {
		this.maxVotesPerUser = maxVotesPerUser;
	}

	/**
	 * @param minVotesPerUser
	 *            the minVotesPerUser to set
	 */
	public void setMinVotesPerUser(Integer minVotesPerUser) {
		this.minVotesPerUser = minVotesPerUser;
	}

	/**
	 * @param poll
	 *            the poll to set
	 */
	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	/**
	 * @param pollId
	 *            the pollId to set
	 */
	public void setPollId(Long pollId) {
		this.pollId = pollId;
	}

	/**
	 * Check the attributes of this {@link PollConfiguration}, validates it and
	 * adapts them
	 */
	public PollConfiguration checkAttributes() {
		if (this.maxVotesPerUser == null) {
			this.maxVotesPerUser = Integer.valueOf(1);
		}

		if (this.maxSelectableOptions == null) {
			this.maxSelectableOptions = this.maxVotesPerUser;
		}

		if (this.minVotesPerUser == null) {
			this.minVotesPerUser = Integer.valueOf(1);
		}

		if (this.minVotesPerUser.compareTo(this.maxVotesPerUser) > 0) {
			this.minVotesPerUser = this.maxVotesPerUser;
		}

		if(this.maxVotesPerOption == null) {
			this.maxVotesPerOption = Integer.valueOf(1);
		}

		if (this.maxVotesPerOption.compareTo(this.maxVotesPerUser) > 0) {
			this.maxVotesPerOption = this.maxVotesPerUser;
		}

		return this;
	}

}
