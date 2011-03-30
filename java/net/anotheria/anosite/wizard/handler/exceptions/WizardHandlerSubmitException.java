package net.anotheria.anosite.wizard.handler.exceptions;

/**
 * WizardHandlerSubmitException may happen on wizard submit.
 *
 * @author h3ll
 */

public class WizardHandlerSubmitException extends WizardHandlerException {

	/**
	 * Basic, serial version uid.
	 */
	private static final long serialVersionUID = -9097899526572007207L;

	/**
	 * Constructor.
	 *
	 * @param wizardId id of current wizard
	 */
	public WizardHandlerSubmitException(String wizardId) {
		super("submit exceptions wizard{" + wizardId + "}");
	}

	/**
	 * Constructor.
	 *
	 * @param wizardId	 string id
	 * @param messageCause string message
	 */
	public WizardHandlerSubmitException(String wizardId, String messageCause) {
		super("submit exceptions wizard{" + wizardId + "}, cause " + messageCause);
	}

	/**
	 * Constructor.
	 *
	 * @param wizardId id of current wizard
	 * @param cause	exception cause
	 */
	public WizardHandlerSubmitException(String wizardId, Throwable cause) {
		super("submit exceptions wizard{" + wizardId + "}", cause);
	}

}
