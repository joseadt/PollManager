package es.udc.jadt.arbitrium.util.generics;

/**
 * The Class Pair.
 *
 * @param <F>
 *            the generic type
 * @param <S>
 *            the generic type
 */
public class Pair<F, S> {

	/** The first. */
	private F first;

	/** The second. */
	private S second;

	/**
	 * Instantiates a new pair.
	 *
	 * @param first
	 *            the first
	 * @param second
	 *            the second
	 */
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Gets the first.
	 *
	 * @return the first
	 */
	public F getFirst() {
		return this.first;
	}

	/**
	 * Sets the first.
	 *
	 * @param first
	 *            the first to set
	 */
	public void setFirst(F first) {
		this.first = first;
	}

	/**
	 * Gets the second.
	 *
	 * @return the second
	 */
	public S getSecond() {
		return this.second;
	}

	/**
	 * Sets the second.
	 *
	 * @param second
	 *            the second to set
	 */
	public void setSecond(S second) {
		this.second = second;
	}

}
