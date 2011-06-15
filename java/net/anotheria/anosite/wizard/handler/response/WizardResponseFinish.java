package net.anotheria.anosite.wizard.handler.response;

import net.anotheria.anosite.shared.InternalResponseCode;

/**
 * Wizard internal response code - which signalize that wizard should be finished!
 *
 * @author h3ll
 */
public class WizardResponseFinish extends WizardHandlerResponse {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = -3385217694167173409L;
	/**
	 * WizardResponseCancel 'redirectUrl'.
	 */
	private String redirectUrl;

	/**
	 * Constructor.
	 *
	 * @param aRedirectUrl redirect url
	 */
	public WizardResponseFinish(String aRedirectUrl) {
		this.redirectUrl = aRedirectUrl;
	}

	@Override
	public InternalResponseCode getResponseCode() {
		return InternalResponseCode.CONTINUE_AND_REDIRECT;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}
