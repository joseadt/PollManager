package es.udc.jadt.arbitrium.model.util;

import org.springframework.data.jpa.domain.Specification;

public abstract class SpecificationFilter<T> implements Specification<T> {

	private Object[] args;


	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

}
