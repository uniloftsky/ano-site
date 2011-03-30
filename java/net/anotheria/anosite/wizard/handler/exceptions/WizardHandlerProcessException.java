package net.anotheria.anosite.wizard.handler.exceptions;

/**
 * WizardHandlerProcessException, exception may happen on wizard process.
 *
 * @author h3ll
 */

public class WizardHandlerProcessException extends WizardHandlerException {

	/**
	 * Basic, serial version uid.
	 */
	private static final long serialVersionUID = -9097899526572007207L;

	/**
	 * Constructor.
	 *
	 * @param wizardId id of current wizard
	 */
	public WizardHandlerProcessException(String wizardId) {
		super("process exceptions wizard{" + wizardId + "}");
	}

	/**
	 * Constructor.
	 *
	 * @param wizardId id of current wizard
	 * @param cause	exception cause
	 */
	public WizardHandlerProcessException(String wizardId, Throwable cause) {
		super("process exceptions wizard{" + wizardId + "}", cause);
	}

	/**
	 * Constructor.
	 *
	 * @param wizardId	 string id
	 * @param messageCause string message
	 */
	public WizardHandlerProcessException(String wizardId, String messageCause) {
		super("submit exceptions wizard{" + wizardId + "}, cause " + messageCause);
	}
}
