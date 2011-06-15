package net.anotheria.anosite.wizard.handler.exceptions;

/**
 * WizardHandlerException class.
 *
 * @author h3ll
 */
public class WizardHandlerException extends Exception {

	/**
	 * Basic serial version uid.
	 */
	private static final long serialVersionUID = -8670792529679115845L;

	/**
	 * Constructor.
	 *
	 * @param message string message
	 */
	public WizardHandlerException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 *
	 * @param message string message
	 * @param cause   exception cause
	 */
	public WizardHandlerException(String message, Throwable cause) {
		super(message, cause);
	}
}
