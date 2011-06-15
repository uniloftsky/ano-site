package net.anotheria.anosite.wizard.api;

import net.anotheria.anoplass.api.API;
import net.anotheria.anosite.wizard.api.exception.WizardAPIException;

import java.util.List;

/**
 * WizardAPI interface.
 *
 * @author h3ll
 */
public interface WizardAPI extends API {

	/**
	 * Returns wizard with selected name.
	 *
	 * @param wizardName name of wizard
	 * @return {@link WizardAO}
	 * @throws WizardAPIException on errors  WizardNotFoundException - if wizard not found
	 */
	WizardAO getWizardByName(String wizardName) throws WizardAPIException;

	/**
	 * Returns wizard with selected id.
	 *
	 * @param wizardId wizard id
	 * @return {@link WizardAO}
	 * @throws WizardAPIException on errors , WizardNotFoundException - if wizard not found
	 */
	WizardAO getWizard(String wizardId) throws WizardAPIException;


	/**
	 * Return id of current step page.
	 *
	 * @param wizardId id of wizard
	 * @return string id of step
	 * @throws WizardAPIException on errors
	 */
	String getCurrentStepPageId(String wizardId) throws WizardAPIException;

	/**
	 * Returns current step for wizard. If step does not exist, then new one will be created.
	 *
	 * @param wizardId id of wizard
	 * @return {@link WizardStepAO }
	 * @throws WizardAPIException on errors
	 */
	WizardStepAO getCurrentStep(String wizardId) throws WizardAPIException;

	/**
	 * Returns collection off steps which were already completed (passed).
	 *
	 * @param wizardId id of wizard
	 * @return {@link WizardStepAO} collection
	 * @throws WizardAPIException on errors
	 */
	List<WizardStepAO> getCompletedSteps(String wizardId) throws WizardAPIException;

	/**
	 * Adjusting to next step.
	 *
	 * @param wizardId id
	 * @return {@link WizardStepAO}
	 * @throws WizardAPIException on errors
	 */
	WizardStepAO adjustToNextStep(String wizardId) throws WizardAPIException;

	/**
	 * Adjusting to previous step.
	 *
	 * @param wizardId id
	 * @return {@link WizardStepAO}
	 * @throws WizardAPIException on errors
	 */
	WizardStepAO adjustToPreviousStep(String wizardId) throws WizardAPIException;


	/**
	 * Adjusting to step.
	 *
	 * @param wizardId  id of wizard
	 * @param stepIndex step of step
	 * @return {@link WizardStepAO}
	 * @throws WizardAPIException on errors
	 */
	WizardStepAO adjustToStep(String wizardId, int stepIndex) throws WizardAPIException;

	/**
	 * Finish wizard.
	 *
	 * @param wizard {@link WizardAO}
	 * @return true if wizard finished, false otherwise
	 * @throws WizardAPIException on errors
	 */
	boolean finishWizard(WizardAO wizard) throws WizardAPIException;

	/**
	 * Cancel wizard.
	 *
	 * @param wizard {@link WizardAO}
	 * @return true if wizard canceled, false otherwise
	 * @throws WizardAPIException
	 */
	boolean cancelWizard(WizardAO wizard) throws WizardAPIException;

	/**
	 * Allow next step.
	 *
	 * @param wizard	 {@link WizardAO}
	 * @param wizardStep {@link WizardStepAO}
	 */
	void allowNextStepNavigation(WizardAO wizard, WizardStepAO wizardStep);

	/**
	 * Allow previous step.
	 *
	 * @param wizard	 {@link WizardAO}
	 * @param wizardStep {@link WizardStepAO}
	 */
	void allowPreviousStepNavigation(WizardAO wizard, WizardStepAO wizardStep);

	/**
	 * Allow wizard cancel step.
	 *
	 * @param wizard	 {@link WizardAO}
	 * @param wizardStep {@link WizardStepAO}
	 */
	void allowWizardCancel(WizardAO wizard, WizardStepAO wizardStep);

	/**
	 * Allow wizard finish step.
	 *
	 * @param wizard	 {@link WizardAO}
	 * @param wizardStep {@link WizardStepAO}
	 */
	void allowWizardFinish(WizardAO wizard, WizardStepAO wizardStep);


}
