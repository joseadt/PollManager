package es.udc.jadt.arbitrium.model.entities.pollconfig;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

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

	private boolean isMultiSelection;

	private boolean isDotPoll;

	private Integer maxSelectableOptions;


	public PollConfiguration() {
		this.isMultiSelection = false;
		this.isDotPoll = false;
	}

	/**
	 * @return the pollId
	 */
	public Long getPollId() {
		return this.pollId;
	}



	/**
	 * @param pollId
	 *            the pollId to set
	 */
	public void setPollId(Long pollId) {
		this.pollId = pollId;
	}



	/**
	 * @return the poll
	 */
	public Poll getPoll() {
		return this.poll;
	}



	/**
	 * @param poll
	 *            the poll to set
	 */
	public void setPoll(Poll poll) {
		this.poll = poll;
	}



	/**
	 * @return the isMultiSelection
	 */
	public boolean isMultiSelection() {
		return this.isMultiSelection;
	}



	/**
	 * Sets if the poll is MultiSelection. Also checks and corrects the other
	 * attributes.
	 *
	 * @param isMultiSelection
	 *            the isMultiSelection to set. If true, then isDotPoll will be set
	 *            to false and maxSelectableOptions to {@code Integer.valueOf(1)}.
	 *            <p>
	 *            If false, maxSelectableOptions will be set to
	 *            {@code Integer.MAX_VALUE} if it's null or equals to
	 *            {@code Integer.valueOf(1)}
	 */
	public void setMultiSelection(boolean isMultiSelection) {
		this.isMultiSelection = isMultiSelection;
		if (!this.isMultiSelection) {
			this.isDotPoll = false;
			this.maxSelectableOptions = Integer.valueOf(1);
		} else {
			if (this.maxSelectableOptions == null || Integer.valueOf(1).equals(this.maxSelectableOptions)) {
				this.maxSelectableOptions = Integer.MAX_VALUE;
			}
		}


	}

	/**
	 * @return the isDotPoll
	 */
	public boolean isDotPoll() {
		return this.isDotPoll;
	}

	/**
	 * @param isDotPoll
	 *            the isDotPoll to set
	 */
	public void setDotPoll(boolean isDotPoll) {
		this.isDotPoll = isDotPoll;
	}

	/**
	 * @return the maxSelectableOptions
	 */
	public Integer getMaxSelectableOptions() {
		return this.maxSelectableOptions;
	}


	/**
	 * Sets if the poll is MultiSelection. Also checks and corrects the other
	 * attributes.
	 *
	 * @param maxSelectableOptions
	 *            the maxSelectableOptions to set
	 *            <p>
	 *            If it's an integer greater than 1 , then isMultiselection will be
	 *            set to true. If it's 0 isMultiselection will be set to false.
	 *            <p>
	 *            if maxSelectableOptions is null or less than 0, then the value
	 *            that will be set will depend on the current value of
	 *            isMultiselection
	 */
	public void setMaxSelectableOptions(Integer maxSelectableOptions) {
		if (maxSelectableOptions == null || maxSelectableOptions.intValue() < 0) {
			if (this.isMultiSelection) {
				this.maxSelectableOptions = Integer.MAX_VALUE;
			}else {
				this.maxSelectableOptions = Integer.valueOf(1);
			}
		}else {
			this.maxSelectableOptions = maxSelectableOptions;

			this.isMultiSelection = maxSelectableOptions.intValue() > 0;
		}
	}


}
