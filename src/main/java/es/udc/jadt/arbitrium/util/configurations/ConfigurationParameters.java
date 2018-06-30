package es.udc.jadt.arbitrium.util.configurations;

import java.util.List;

import es.udc.jadt.arbitrium.model.entities.polloption.PollOption;

public class ConfigurationParameters {

	private Integer maxOptions;

	private Boolean isMultiSelection;
	private Boolean isUserDefinedOptions;
	
	private List<PollOption> defaultOptions;

	public Integer getMaxOptions() {
		return maxOptions;
	}

	public void setMaxOptions(Integer maxOptions) {
		this.maxOptions = maxOptions;
	}

	public Boolean getIsMultiSelection() {
		return isMultiSelection;
	}

	public void setIsMultiSelection(Boolean isMultiSelection) {
		this.isMultiSelection = isMultiSelection;
	}

	public Boolean getIsUserDefinedOptions() {
		return isUserDefinedOptions;
	}

	public void setIsUserDefinedOptions(Boolean isUserDefinedOptions) {
		this.isUserDefinedOptions = isUserDefinedOptions;
	}

	public List<PollOption> getDefaultOptions() {
		return defaultOptions;
	}

	public void setDefaultOptions(List<PollOption> defaultOptions) {
		this.defaultOptions = defaultOptions;
	}

}
