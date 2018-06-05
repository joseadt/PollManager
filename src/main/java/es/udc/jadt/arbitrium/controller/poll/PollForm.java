package es.udc.jadt.arbitrium.controller.poll;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

public class PollForm {

    @NotBlank
    private String title;

    private String description;

    private List<String> options;

    public PollForm() {
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

}
