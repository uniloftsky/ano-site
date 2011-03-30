package net.anotheria.anosite.wizard.api.exception;

/**
 * WizardAPIFirstStepException  is  thrown when on previous step  adjust, if current page is FIRST.
 *
 * @author h3ll
 */

public class WizardAPIFirstStepException extends WizardAPIException {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 1636424291889974911L;

	/**
	 * Constructor.
	 *
	 * @param wizardId wizard id
	 * @param stepId   wizard step id
	 * @param index	wizard step index
	 */
	public WizardAPIFirstStepException(String wizardId, String stepId, int index) {
		super("WizardStep [" + stepId + "] with index [" + index + "], is FIRST for wizard[" + wizardId + "]");
	}
}
