package net.anotheria.anosite.wizard.handler.exceptions;

/**
 * PreProcess exception for WizardHandler.
 *
 * @author h3ll
 */

public class WizardHandlerPreProcessException extends WizardHandlerException {

	/**
	 * Basic serial version uid.
	 */
	private static final long serialVersionUID = 310069633151397756L;

	/**
	 * Constructor.
	 *
	 * @param wizardId string id
	 */
	public WizardHandlerPreProcessException(String wizardId) {
		super("preProcess exceptions wizard{" + wizardId + "}");
	}

	/**
	 * Constructor.
	 *
	 * @param wizardId	 string id
	 * @param messageCause string message
	 */
	public WizardHandlerPreProcessException(String wizardId, String messageCause) {
		super("preProcess exceptions wizard{" + wizardId + "}, cause " + messageCause);
	}

	/**
	 * Constructor.
	 *
	 * @param wizardId string id
	 * @param cause	exception
	 */
	public WizardHandlerPreProcessException(String wizardId, Throwable cause) {
		super("preProcess exceptions wizard{" + wizardId + "}", cause);
	}
}
