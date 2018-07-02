package es.udc.jadt.arbitrium.model.entities.pollconfig;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;

@Entity
public class ConfigurationParameters {

	@Id
	@GeneratedValue
	private Long Id;

	private Integer maxOptions;

	private Boolean isMultiSelection;
	private Boolean isUserDefinedOptions;

	@Transient
	private List<PollOption> defaultOptions;

	public Long getId() {
		return this.Id;
	}

	public void setId(Long id) {
		this.Id = id;
	}

	public Integer getMaxOptions() {
		return this.maxOptions;
	}

	public void setMaxOptions(Integer maxOptions) {
		if (maxOptions != null) {
			this.maxOptions = maxOptions;
		}
	}

	public Boolean getIsMultiSelection() {
		return this.isMultiSelection;
	}

	public void setIsMultiSelection(Boolean isMultiSelection) {
		if (isMultiSelection != null) {
			this.isMultiSelection = isMultiSelection;
		}
	}

	public Boolean getIsUserDefinedOptions() {
		return this.isUserDefinedOptions;
	}

	public void setIsUserDefinedOptions(Boolean isUserDefinedOptions) {
		if (isUserDefinedOptions != null) {
			this.isUserDefinedOptions = isUserDefinedOptions;
		}
	}

	public List<PollOption> getDefaultOptions() {
		return this.defaultOptions;
	}

	public void setDefaultOptions(List<PollOption> defaultOptions) {
		if (defaultOptions != null) {
			this.defaultOptions = defaultOptions;
		}
	}

	public ConfigurationParameters applyConfiguration(ConfigurationParameters newParameters) {
		this.setIsMultiSelection(newParameters.getIsMultiSelection());
		this.setIsUserDefinedOptions(newParameters.getIsUserDefinedOptions());
		this.setMaxOptions(newParameters.getMaxOptions());

		return this;
	}

}
