package net.anotheria.anosite.wizard.api;

import net.anotheria.anoplass.api.APIFactory;

/**
 * WizardAPI factory.
 *
 * @author h3ll
 */
public class WizardAPIFactory implements APIFactory<WizardAPI> {
	@Override
	public WizardAPI createAPI() {
		return new BaseWizardAPIImpl();
	}
}
