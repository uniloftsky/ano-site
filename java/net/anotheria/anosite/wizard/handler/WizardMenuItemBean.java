package net.anotheria.anosite.wizard.handler;

import java.io.Serializable;

/**
 * Wizard Menu item.
 * For rendering Wizard step navigation menu on pages.
 *
 * @author h3ll
 */

public class WizardMenuItemBean implements Serializable {

	/**
	 * Basic serial version uid.
	 */
	private static final long serialVersionUID = 7447946856782918469L;
	/**
	 * MenuItemBean stepName.
	 */
	private String stepName;
	/**
	 * MenuItemBean stepUrl.
	 */
	private String stepUrl;
	/**
	 * MenuItemBean isCurrent.
	 */
	private boolean isCurrent;
	/**
	 * MenuItemBean isPassed.
	 */
	private boolean isPassed;

	/**
	 * Constructor.
	 *
	 * @param aStepName  name of step
	 * @param aStepUrl   url to step
	 * @param aIsCurrent is current step
	 * @param aIsPassed  is step passed
	 */
	public WizardMenuItemBean(String aStepName, String aStepUrl, boolean aIsCurrent, boolean aIsPassed) {
		this.stepName = aStepName;
		this.stepUrl = aStepUrl;
		this.isCurrent = aIsCurrent;
		this.isPassed = aIsPassed;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public String getStepName() {
		return stepName;
	}

	public String getStepUrl() {
		return stepUrl;
	}

	public boolean isPassed() {
		return isPassed;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("WizardMenuItemBean");
		sb.append("{isPassed=").append(isPassed);
		sb.append(", isCurrent=").append(isCurrent);
		sb.append(", stepUrl='").append(stepUrl).append('\'');
		sb.append(", stepName='").append(stepName).append('\'');
		sb.append('}');
		return sb.toString();
	}
}

