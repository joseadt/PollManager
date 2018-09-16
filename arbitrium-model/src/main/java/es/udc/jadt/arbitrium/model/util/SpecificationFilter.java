package es.udc.jadt.arbitrium.model.util;

import org.springframework.data.jpa.domain.Specification;

public abstract class SpecificationFilter<T> implements Specification<T> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1264589561237147866L;

	/**
	 * @param args
	 */
	public SpecificationFilter(Object[] args) {
		this.args = args;
	}

	public SpecificationFilter() {

	}

	private Object[] args;

	public Object[] getArgs() {
		return this.args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

}
