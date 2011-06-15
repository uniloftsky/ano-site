package net.anotheria.anosite.wizard.api;

import java.io.Serializable;
import java.util.List;

/**
 * Wizard API object.
 *
 * @author h3ll
 */
public class WizardAO implements Serializable {

	/**
	 * Basic serial version UID.
	 */
	private static final long serialVersionUID = 708835991924875879L;

	/**
	 * WizardBean 'id'.
	 */
	private String id;
	/**
	 * WizardBean 'name'.
	 */
	private String name;
	/**
	 * WizardBean 'wizardSteps'. Actually Page ids collection.
	 */
	private List<WizardStepAO> wizardSteps;
	/**
	 * WizardBean 'handlerId'.
	 */
	private String handlerId;
	/**
	 * WizardBean 'handlerId'.
	 */
	private String cancelRedirectUrl;
	/**
	 * WizardBean 'finishRedirectUrl'.
	 */
	private String finishRedirectUrl;


	public WizardAO(String aId) {
		this.id = aId;
	}

	public String getCancelRedirectUrl() {
		return cancelRedirectUrl;
	}

	public void setCancelRedirectUrl(String cancelRedirectUrl) {
		this.cancelRedirectUrl = cancelRedirectUrl;
	}

	public String getFinishRedirectUrl() {
		return finishRedirectUrl;
	}

	public void setFinishRedirectUrl(String finishRedirectUrl) {
		this.finishRedirectUrl = finishRedirectUrl;
	}

	public String getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(String handlerId) {
		this.handlerId = handlerId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<WizardStepAO> getWizardSteps() {
		return wizardSteps;
	}

	public void setWizardSteps(List<WizardStepAO> wizardSteps) {
		this.wizardSteps = wizardSteps;
	}

	@Override
	public boolean equals(Object anotherObj) {
		return anotherObj instanceof WizardAO && id.equals(WizardAO.class.cast(anotherObj).getId());
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("WizardAO");
		sb.append("{id='").append(id).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
