package net.anotheria.anosite.wizard.api.exception;

import net.anotheria.anoplass.api.APIException;

/**
 * WizardAPI exception.
 *
 * @author h3ll
 */

public class WizardAPIException extends APIException {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 1019869075416778846L;

	/**
	 * Constructor.
	 *
	 * @param message string message
	 */
	public WizardAPIException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 *
	 * @param message string message
	 * @param cause   exception cause
	 */
	public WizardAPIException(String message, Exception cause) {
		super(message, cause);
	}
}
