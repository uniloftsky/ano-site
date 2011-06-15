package net.anotheria.anosite.wizard.api.exception;

/**
 * WizardNotFoundException as WizardAPIException.
 *
 * @author h3ll
 */
public class WizardNotFoundException extends WizardAPIException {

	/**
	 * Basic serial versionUid.
	 */
	private static final long serialVersionUID = -4831431741887119781L;

	/**
	 * Constructor.
	 *
	 * @param data id or name
	 */
	public WizardNotFoundException(String data) {
		super("Wizard not found " + data);
	}
}
