package net.anotheria.anosite.wizard.handler.response;

import net.anotheria.anosite.shared.InternalResponseCode;

/**
 * Wizard internal response code - which signalize that wizard should continue rendering.
 *
 * @author h3ll
 */

public class WizardResponseContinue extends WizardHandlerResponse {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 6175736858472476916L;
	/**
	 * WizardResponseContinue INSTANCE constant.
	 */
	public static final WizardResponseContinue INSTANCE = new WizardResponseContinue();


	@Override
	public InternalResponseCode getResponseCode() {
		return InternalResponseCode.CONTINUE;
	}
}
