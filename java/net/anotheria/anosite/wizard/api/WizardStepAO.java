package net.anotheria.anosite.wizard.api;

import java.io.Serializable;

/**
 * WizardStep API object.
 *
 * @author h3ll
 */

public class WizardStepAO implements Serializable {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 2793846910830660545L;

	/**
	 * WizardStepAO 'wizardId'.
	 */
	private String wizardId;
	/**
	 * WizardStepAO 'stepIndex'.
	 */
	private int stepIndex;
	/**
	 * WizardStepAO 'stepId'.
	 */
	private String stepId;


	/**
	 * Constructor.
	 *
	 * @param aWizardId  id of wizard
	 * @param aStepId	id of step
	 * @param aStepIndex step index
	 */
	public WizardStepAO(String aWizardId, String aStepId, int aStepIndex) {
		this.stepId = aStepId;
		this.stepIndex = aStepIndex;
		this.wizardId = aWizardId;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(int stepIndex) {
		this.stepIndex = stepIndex;
	}

	public String getWizardId() {
		return wizardId;
	}

	public void setWizardId(String aWizardId) {
		this.wizardId = aWizardId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		WizardStepAO that = (WizardStepAO) o;

		if (stepIndex != that.stepIndex) return false;
		if (stepId != null ? !stepId.equals(that.stepId) : that.stepId != null) return false;
		if (wizardId != null ? !wizardId.equals(that.wizardId) : that.wizardId != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = wizardId != null ? wizardId.hashCode() : 0;
		final int mult = 31;
		result = mult * result + stepIndex;
		result = mult * result + (stepId != null ? stepId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("WizardStepAO");
		sb.append("{stepId='").append(stepId).append('\'');
		sb.append(", wizardId='").append(wizardId).append('\'');
		sb.append(", stepIndex=").append(stepIndex);
		sb.append('}');
		return sb.toString();
	}
}
