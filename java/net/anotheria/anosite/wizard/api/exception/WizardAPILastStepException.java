package net.anotheria.anosite.wizard.api.exception;

/**
 * WizardAPILastStepException  is  thrown when on next step  adjust, when current page is LAST.
 *
 * @author h3ll
 */
public class WizardAPILastStepException extends WizardAPIException {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 2409090109389034911L;

	/**
	 * Constructor.
	 *
	 * @param wizardId wizard id
	 * @param stepId   wizard step id
	 * @param index	wizard step index
	 */

	public WizardAPILastStepException(String wizardId, String stepId, int index) {
		super("WizardStep [" + stepId + "] with index [" + index + "], is LastStep for wizard[" + wizardId + "]");
	}
}
