package net.anotheria.anosite.access.conf;

import net.anotheria.anoplass.api.APIException;

/**
 * {@link AccessConfigurator} main exception.
 * 
 */
public class AccessConfiguratorException extends APIException {
	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 6183813023999549032L;


	/**
	 * Default constructor.
	 */
	public AccessConfiguratorException() {
	}

	/**
	 * Public constructor.
	 *
	 * @param message
	 *            - exception message
	 */
	public AccessConfiguratorException(final String message) {
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
	public AccessConfiguratorException(final String message, final Exception cause) {
		super(message, cause);
	}

}
