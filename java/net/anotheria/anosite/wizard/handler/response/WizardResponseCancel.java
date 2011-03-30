package net.anotheria.anosite.wizard.handler.response;

import net.anotheria.anosite.shared.InternalResponseCode;

/**
 * Wizard internal response code - which signalize that wizard should be canceled!
 *
 * @author h3ll
 */

public class WizardResponseCancel extends WizardHandlerResponse {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 795941573072338609L;
	/**
	 * WizardResponseCancel 'redirectUrl'.
	 */
	private String redirectUrl;

	/**
	 * Constructor.
	 *
	 * @param aRedirectUrl redirect url
	 */
	public WizardResponseCancel(String aRedirectUrl) {
		this.redirectUrl = aRedirectUrl;
	}

	@Override
	public InternalResponseCode getResponseCode() {
		return InternalResponseCode.CANCEL_AND_REDIRECT;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}
