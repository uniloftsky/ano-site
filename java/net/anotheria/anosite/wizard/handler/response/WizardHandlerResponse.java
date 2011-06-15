package net.anotheria.anosite.wizard.handler.response;

import net.anotheria.anosite.shared.InternalResponseCode;

import java.io.Serializable;

/**
 * WizardHandlerResponse to be used in steps and wizard handlers.
 *
 * @author h3ll
 */
public abstract class WizardHandlerResponse implements Serializable {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 7289281348326360389L;

	/**
	 * Returns the response code of the response. Classes react on the response accordingly to the response code.
	 *
	 * @return {@link InternalResponseCode}
	 */
	public abstract InternalResponseCode getResponseCode();

	@Override
	public String toString() {
		return getResponseCode().toString();
	}

}
