package net.anotheria.anosite.wizard.handler.response;

import net.anotheria.anosite.shared.InternalResponseCode;

/**
 * WizardResponseAbort usually happens if some exception occurred during execution.
 *
 * @author h3ll
 */

public class WizardResponseAbort extends WizardHandlerResponse {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 1525345918533128347L;
	/**
	 * WizardResponseAbort 'cause'.
	 */
	private Exception cause;


	/**
	 * Public constructor.
	 *
	 * @param aException {@link Exception}
	 */
	public WizardResponseAbort(Exception aException) {
		cause = aException;
	}


	@Override
	public InternalResponseCode getResponseCode() {
		return InternalResponseCode.ABORT;
	}

	public Exception getCause() {
		return cause;
	}

	/**
	 * Return exception message if possible.
	 *
	 * @return String message or null
	 */
	public String getCauseMessage() {
		return cause != null ? cause.getMessage() : null;
	}

}
