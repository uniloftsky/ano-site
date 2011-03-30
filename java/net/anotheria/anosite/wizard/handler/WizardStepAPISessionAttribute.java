package net.anotheria.anosite.wizard.handler;

/**
 * Wrapper for Wizard step attribute.
 * This bean  will store info about current Wizard step in APISession, as attribute  named
 *  [wizard.name]+ BaseWizardHandler.CURRENT_STEP_SUFFIX.
 *
 * @author h3ll
 */

import java.io.Serializable;


public class WizardStepAPISessionAttribute implements Serializable {

	/**
	 * Basic serial version uid.
	 */
	private static final long serialVersionUID = 6980588082760153919L;
	/**
	 * WizardStepAPISessionAttribute 'stepIndex'.
	 */
	private int stepIndex;
	/**
	 * WizardStepAPISessionAttribute 'wizardStepId'.
	 */
	private String wizardStepId;

	/**
	 * Constructor.
	 *
	 * @param aNumber int number
	 * @param aId	 string id
	 */
	public WizardStepAPISessionAttribute(int aNumber, String aId) {
		this.stepIndex = aNumber;
		this.wizardStepId = aId;
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(int stepIndex) {
		this.stepIndex = stepIndex;
	}

	public String getWizardStepId() {
		return wizardStepId;
	}

	public void setWizardStepId(String wizardStepId) {
		this.wizardStepId = wizardStepId;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("WizardStep");
		sb.append("{stepIndex=").append(stepIndex);
		sb.append(", wizardStepId='").append(wizardStepId).append('\'');
		sb.append('}');
		return sb.toString();
	}
}