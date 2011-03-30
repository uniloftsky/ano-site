package net.anotheria.anosite.wizard.handler.response;

import net.anotheria.anosite.shared.InternalResponseCode;

/**
 * WizardResponseChangeStep - response which means that step should be changed.
 *
 * @author h3ll
 */

public class WizardResponseChangeStep extends WizardHandlerResponse {
	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 4649126014349238387L;

	/**
	 * WizardResponseChangeStep INSTANCE.
	 */
	public static final WizardResponseChangeStep INSTANCE = new WizardResponseChangeStep();


	@Override
	public InternalResponseCode getResponseCode() {
		return InternalResponseCode.CONTINUE_AND_REDIRECT;
	}
}
