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
	 * WizardStepAO 'pagexId'.
	 * Actually PagexId.
	 */
	private String pagexId;

	/**
	 * Constructor.
	 *
	 * @param aWizardId  id of wizard
	 * @param aStepIndex step index
	 * @param aPagexId   id of Ano-Pagex  which is  linked to current step
	 */
	public WizardStepAO(String aWizardId, int aStepIndex, String aPagexId) {
		this.stepIndex = aStepIndex;
		this.wizardId = aWizardId;
		this.pagexId = aPagexId;
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(int aStepIndex) {
		this.stepIndex = aStepIndex;
	}

	public String getPagexId() {
		return pagexId;
	}

	public void setPagexId(String aPagexId) {
		this.pagexId = aPagexId;
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

		if (pagexId != null ? !pagexId.equals(that.pagexId) : that.pagexId != null) return false;
		if (wizardId != null ? !wizardId.equals(that.wizardId) : that.wizardId != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = wizardId != null ? wizardId.hashCode() : 0;
		final int mult = 31;
		result = mult * result + (pagexId != null ? pagexId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "WizardStepAO{" +
				"wizardId='" + wizardId +
				", stepIndex=" + stepIndex +
				", pagexId='" + pagexId +
				'}';
	}
}
