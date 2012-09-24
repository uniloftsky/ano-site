package net.anotheria.anosite.acess;

import net.anotheria.anoplass.api.APIException;

/**
 * {@link AccessAPI} main exception.
 * 
 * @author Alexandr Bolbat
 */
public class AccessAPIException extends APIException {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 6781697641130775376L;

	/**
	 * Default constructor.
	 */
	public AccessAPIException() {
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            - exception message
	 */
	public AccessAPIException(final String message) {
		super(message);
	}

	/**
	 * Public constructor.
	 * 
	 * @param message
	 *            - exception message
	 * @param cause
	 *            - exception cause
	 */
	public AccessAPIException(final String message, final Exception cause) {
		super(message, cause);
	}

}
