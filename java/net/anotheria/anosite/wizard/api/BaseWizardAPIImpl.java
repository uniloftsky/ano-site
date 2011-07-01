package net.anotheria.anosite.wizard.api;

import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.anoplass.api.session.APISession;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.aswizarddata.data.WizardDef;
import net.anotheria.anosite.gen.aswizarddata.service.ASWizardDataServiceException;
import net.anotheria.anosite.gen.aswizarddata.service.IASWizardDataService;
import net.anotheria.anosite.gen.aswizarddata.service.WizardDefNotFoundInASWizardDataServiceException;
import net.anotheria.anosite.wizard.WizardCommand;
import net.anotheria.anosite.wizard.api.exception.WizardAPIException;
import net.anotheria.anosite.wizard.api.exception.WizardAPIFirstStepException;
import net.anotheria.anosite.wizard.api.exception.WizardAPILastStepException;
import net.anotheria.anosite.wizard.api.exception.WizardNotFoundException;
import net.anotheria.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static net.anotheria.anosite.wizard.WizardCommand.*;

/**
 * Base implementation of WizardAPI.
 *
 * @author h3ll
 */
public class BaseWizardAPIImpl extends AbstractAPIImpl implements WizardAPI {

	/**
	 * BaseWizardAPIImpl  WIZARD_ATTRIBUTE_SUFFIX.
	 */
	public static final String WIZARD_ATTRIBUTE_SUFFIX = "_wizardData";

	/**
	 * Attribute which holds allowed wizard step commands.
	 */
	public static final String ALLOWED_WIZARD_STEP_COMMANDS_ATTRIBUTE = "allowedWizardStepCommands";
	/**
	 * Log4j logger instance.
	 */
	private static final Logger LOG = Logger.getLogger(BaseWizardAPIImpl.class);
	/**
	 * Wizard service.
	 */
	private IASWizardDataService wizardDataService;


	@Override
	public void init() throws APIInitException {
		super.init();
		try {
			this.wizardDataService = MetaFactory.get(IASWizardDataService.class);
		} catch (MetaFactoryException e) {
			throw new APIInitException("WizardAPI init failed", e);
		}
	}

	@Override
	public WizardAO getWizardByName(String wizardName) throws WizardAPIException {
		if (StringUtils.isEmpty(wizardName))
			throw new IllegalArgumentException("Wizard name is illegal");

		try {
			List<WizardDef> wizards = wizardDataService.getWizardDefsByProperty(WizardDef.PROP_NAME, wizardName);
			if (wizards == null || wizards.isEmpty())
				throw new WizardNotFoundException(wizardName);
			return toWizardAO(wizards.get(0));
		} catch (ASWizardDataServiceException e) {
			LOG.error("getWizardByName(" + wizardName + ")", e);
			throw new WizardAPIException("Backend failure", e);
		}
	}


	@Override
	public WizardAO getWizard(String wizardId) throws WizardAPIException {
		if (StringUtils.isEmpty(wizardId))
			throw new IllegalArgumentException("WizardId is illegal");
		try {
			return toWizardAO(wizardDataService.getWizardDef(wizardId));
		} catch (WizardDefNotFoundInASWizardDataServiceException wDnfe) {
			throw new WizardNotFoundException(wizardId);
		} catch (ASWizardDataServiceException e) {
			LOG.error("getWizard(" + wizardId + ")", e);
			throw new WizardAPIException("Backend failure", e);
		}

	}

	@Override
	public String getCurrentStepPageId(String wizardId) throws WizardAPIException {
		return getCurrentStep(wizardId).getPagexId();
	}

	@Override
	public WizardStepAO getCurrentStep(String wizardId) throws WizardAPIException {
		WizardAO wizard = getWizard(wizardId);

		if (wizard.getWizardSteps().isEmpty()) {
			LOG.warn(" getCurrentStep(" + wizardId + ") - Steps are not properly configured");
			throw new WizardAPIException(" getCurrentStep(" + wizardId + ") - Steps are not properly configured");
		}
		WizardData data = getDataFromSession(wizardId);
		if (data == null) {
			data = createWizardData(wizard);
		}
		return data.getCurrentStep();
	}


	@Override
	public List<WizardStepAO> getCompletedSteps(String wizardId) throws WizardAPIException {
		WizardData data = getDataFromSession(wizardId);
		if (data == null) {
			LOG.warn("getCompletedSteps(" + wizardId + ") . There is no data found. Suppose it was just removed!");
			throw new WizardAPIException("getCompletedSteps(" + wizardId + ") . There is no data found. Suppose it was just removed!");
		}
		return data.getCompletedSteps();
	}

	@Override
	public WizardStepAO adjustToNextStep(String wizardId) throws WizardAPIException {
		WizardData data = getDataFromSession(wizardId);
		if (data == null) {
			LOG.warn("adjustToNextStep(" + wizardId + ") . There is no data found.");
			throw new WizardAPIException("adjustToNextStep(" + wizardId + ") . There is no data found.");
		}

		WizardAO wizard = getWizard(wizardId);
		WizardStepAO currentStep = getCurrentStep(wizardId);
		try {

			if (isLastStep(wizard, currentStep))
				throw new WizardAPILastStepException(wizardId, currentStep.getPagexId(), currentStep.getStepIndex());


			//check if next is allowed!
			if (!isCommandAllowed(wizard.getId(), currentStep.getPagexId(), NEXT)) {
				LOG.debug("Next step is not allowed! Stay on current!");
				return currentStep;
			}

			final int nextStepIndex = currentStep.getStepIndex() + 1;
			//mark step completed
			data.addCompletedStep(data.getCurrentStep());
			WizardStepAO nextStep = wizard.getWizardSteps().get(nextStepIndex);
			data.setCurrentStep(nextStep);
			//returning nextStep data
			return nextStep;
		} finally {
			cleanAllowedCommands(wizard.getId(), currentStep.getPagexId());
		}
	}


	@Override
	public WizardStepAO adjustToPreviousStep(String wizardId) throws WizardAPIException {
		WizardData data = getDataFromSession(wizardId);
		if (data == null) {
			LOG.warn("adjustToPreviousStep(" + wizardId + ") . There is no data found.");
			throw new WizardAPIException("adjustToPreviousStep(" + wizardId + ") . There is no data found.");
		}
		WizardStepAO currentStep = getCurrentStep(wizardId);
		WizardAO wizard = getWizard(wizardId);
		try {
			if (isFirstStep(wizard, currentStep))
				throw new WizardAPIFirstStepException(wizardId, currentStep.getPagexId(), currentStep.getStepIndex());

			if (!isCommandAllowed(wizard.getId(), currentStep.getPagexId(), PREVIOUS)) {
				LOG.debug("Previous step is not allowed! Stay on current!");
				return currentStep;
			}

			final int prevStepIndex = currentStep.getStepIndex() - 1;

			//we should not mark step as completed
			WizardStepAO prevStep = wizard.getWizardSteps().get(prevStepIndex);
			data.setCurrentStep(prevStep);
			//returning prevStep data
			return prevStep;
		} finally {
			cleanAllowedCommands(wizard.getId(), currentStep.getPagexId());
		}
	}


	@Override
	public WizardStepAO adjustToStep(String wizardId, int stepIndex) throws WizardAPIException {

		WizardAO wizard = getWizard(wizardId);
		WizardStepAO currentStep = getCurrentStep(wizardId);

		if (!navigateToAllowed(wizard, stepIndex)) {
			LOG.debug("Navigate to step is not allowed! Stay on current!");
			return currentStep;
		}

		if (currentStep.getStepIndex() == stepIndex)
			return currentStep;

		WizardData data = getDataFromSession(wizardId);
		if (data == null) {
			LOG.warn("adjustToStep(" + wizardId + ", " + stepIndex + ") . There is no data found.");
			throw new WizardAPIException("adjustToStep(" + wizardId + ", " + stepIndex + ") . There is no data found.");
		}

		//we should not mark step as completed
		WizardStepAO step = wizard.getWizardSteps().get(stepIndex);
		data.setCurrentStep(step);


		//returning step data
		return step;
	}

	/**
	 * Checking possibility of navigation to selected step.
	 *
	 * @param wizard	{@link WizardAO}
	 * @param stepIndex index
	 * @return boolean value
	 * @throws net.anotheria.anosite.wizard.api.exception.WizardAPIException
	 *          on errors
	 */
	private boolean navigateToAllowed(WizardAO wizard, int stepIndex) throws WizardAPIException {
		if (stepIndex < 0 || stepIndex >= wizard.getWizardSteps().size())
			throw new IllegalArgumentException("Step index is illegal " + stepIndex);

		WizardStepAO stepAO = wizard.getWizardSteps().get(stepIndex);

		List<WizardStepAO> passed = getCompletedSteps(wizard.getId());
		if (!passed.contains(stepAO) && !isCommandAllowed(wizard.getId(), stepAO.getPagexId(), NAVIGATE_TO)) {
			log.debug(stepAO + "is not passed! Navigation is not allowed!");
			return false;
		}
		log.debug("Navigating to allowed " + stepAO);
		return true;
	}

	@Override
	public boolean finishWizard(WizardAO wizard) throws WizardAPIException {
		if (wizard == null)
			throw new IllegalArgumentException("Incorrect incoming param");

		WizardStepAO current = getCurrentStep(wizard.getId());
		try {
			if (isLastStep(wizard, current) && isCommandAllowed(wizard.getId(), current.getPagexId(), FINISH)) {
				removeWizardData(wizard.getId());
				LOG.debug(wizard + " finished");
				return true;
			}
			LOG.debug("Wizard Finish is not allowed " + wizard + "isLastStep [" + isLastStep(wizard, current) +
					"]  commandAllowed[" + isCommandAllowed(wizard.getId(), current.getPagexId(), FINISH) + "]");
			return false;
		} finally {
			cleanAllowedCommands(wizard.getId(), current.getPagexId());
		}
	}


	@Override
	public boolean cancelWizard(WizardAO wizard) throws WizardAPIException {
		if (wizard == null)
			throw new IllegalArgumentException("Incorrect incoming param");
		WizardStepAO current = getCurrentStep(wizard.getId());
		try {
			if (isCommandAllowed(wizard.getId(), current.getPagexId(), CANCEL)) {
				removeWizardData(wizard.getId());
				LOG.debug(wizard + " canceled on step :[" + current + "]");
				return true;
			}
			LOG.debug(wizard + " cancel not allowed on step :[" + current + "]");
			return false;
		} finally {
			cleanAllowedCommands(wizard.getId(), current.getWizardId());
		}

	}


	@Override
	public void allowNextStepNavigation(WizardAO wizard, WizardStepAO wizardStep) {
		allowCommand(wizard.getId(), wizardStep.getPagexId(), NEXT);
	}

	@Override
	public void allowPreviousStepNavigation(WizardAO wizard, WizardStepAO wizardStep) {
		allowCommand(wizard.getId(), wizardStep.getPagexId(), PREVIOUS);
	}

	@Override
	public void allowWizardCancel(WizardAO wizard, WizardStepAO wizardStep) {
		allowCommand(wizard.getId(), wizardStep.getPagexId(), CANCEL);
	}

	@Override
	public void allowWizardFinish(WizardAO wizard, WizardStepAO wizardStep) {
		allowCommand(wizard.getId(), wizardStep.getPagexId(), FINISH);
	}

	public void allowWizardNavigateTo(WizardAO wizard, WizardStepAO wizardStep) {
		allowCommand(wizard.getId(), wizardStep.getPagexId(), NAVIGATE_TO);
	}

	/**
	 * Allow selected {@link WizardCommand} execution for wizard with selected id, on selected step.
	 *
	 * @param wizardId	 id of wizard
	 * @param wizardStepId id of wizard step
	 * @param command	  {@link WizardCommand}
	 */
	private void allowCommand(String wizardId, String wizardStepId, WizardCommand command) {
		Map<String, Set<WizardCommand>> allowedWizardCommands = getAllowedCommands();
		Set<WizardCommand> stepCommands = allowedWizardCommands.get(wizardId + "_" + wizardStepId);
		if (stepCommands == null) {
			stepCommands = new HashSet<WizardCommand>();
			allowedWizardCommands.put(wizardId + "_" + wizardStepId, stepCommands);
		}
		stepCommands.add(command);
	}


	/**
	 * Return true - if selected {@link WizardCommand} allowed for  selected wizard with wizardId, on  step  with wizardStepId.
	 *
	 * @param wizardId	 wizard id
	 * @param wizardStepId page id / step id
	 * @param command	  {@link WizardCommand}
	 * @return boolean value
	 */
	private boolean isCommandAllowed(String wizardId, String wizardStepId, WizardCommand command) {
		Map<String, Set<WizardCommand>> allowedWizardCommands = getAllowedCommands();
		Set<WizardCommand> stepCommands = allowedWizardCommands.get(wizardId + "_" + wizardStepId);
		return stepCommands != null && stepCommands.contains(command);
	}

	/**
	 * Remove all commands.
	 *
	 * @param wizardId	 id of wizard
	 * @param wizardStepId page id/ step id
	 */
	private void cleanAllowedCommands(String wizardId, String wizardStepId) {
		Map<String, Set<WizardCommand>> allowedWizardCommands = getAllowedCommands();
		if (allowedWizardCommands.isEmpty())
			return;
		allowedWizardCommands.remove(wizardId + "_" + wizardStepId);
	}

	/**
	 * Get allowed commands from APISession.
	 * Command  map  contains all allowed {@link WizardCommand} which can be performed on current step.
	 *
	 * @return {@link Map<String,Set<WizardCommand>>}
	 */
	private Map<String, Set<WizardCommand>> getAllowedCommands() {
		Object cached = getAttributeFromSession(ALLOWED_WIZARD_STEP_COMMANDS_ATTRIBUTE);
		if (cached instanceof Map)
			//noinspection unchecked
			return (Map<String, Set<WizardCommand>>) cached;

		Map<String, Set<WizardCommand>> allowedWizardCommands = new ConcurrentHashMap<String, Set<WizardCommand>>();
		setAttributeInSession(ALLOWED_WIZARD_STEP_COMMANDS_ATTRIBUTE, allowedWizardCommands);
		return allowedWizardCommands;
	}


	private boolean isFirstStep(WizardAO wizard, WizardStepAO wizardStep) throws WizardAPIException {
		if (wizard.getWizardSteps().isEmpty()) {
			LOG.warn(" isFirstStep(" + wizard + "," + wizardStep + ") - Steps are not properly configured");
			throw new WizardAPIException(" isFirstStep(" + wizard + "," + wizardStep + ")- Steps are not properly configured");
		}
		return wizard.getWizardSteps().get(0).equals(wizardStep);
	}


	private boolean isLastStep(WizardAO wizard, WizardStepAO wizardStep) throws WizardAPIException {
		if (wizard.getWizardSteps().isEmpty()) {
			LOG.warn(" isFirstStep(" + wizard + "," + wizardStep + ") - Steps are not properly configured");
			throw new WizardAPIException(" isFirstStep(" + wizard + "," + wizardStep + ")- Steps are not properly configured");
		}
		int lastStepIndex = wizard.getWizardSteps().size() - 1;
		return wizard.getWizardSteps().get(lastStepIndex).equals(wizardStep);
	}


	/**
	 * Return {@link WizardData} from {@link APISession} if such exists. Null otherwise.
	 *
	 * @param wizardId id of wizard.
	 * @return {@link WizardData} or null
	 */
	private WizardData getDataFromSession(String wizardId) {
		Object attribute = getAttributeFromSession(wizardId + WIZARD_ATTRIBUTE_SUFFIX);
		if (attribute instanceof WizardData)
			return WizardData.class.cast(attribute);
		return null;
	}

	/**
	 * Creates new {@link WizardData} and store it to {@link APISession}.
	 *
	 * @param wizard {@link WizardAO}
	 * @return {@link WizardData}
	 */
	private WizardData createWizardData(WizardAO wizard) {
		final int firstStepIndex = 0;
		WizardData data = new WizardData(wizard.getId());
		data.setCurrentStep(wizard.getWizardSteps().get(firstStepIndex));
		//storing to APISession
		setAttributeInSession(wizard.getId() + WIZARD_ATTRIBUTE_SUFFIX, data);
		return data;
	}


	/**
	 * Creates {@link WizardAO} based on {@link WizardDef}.
	 *
	 * @param wizardDef {@link WizardDef}
	 * @return {@link WizardAO}
	 * @throws net.anotheria.anosite.wizard.api.exception.WizardAPIException
	 *          on errors
	 */
	private WizardAO toWizardAO(WizardDef wizardDef) throws WizardAPIException {
		WizardAO result = new WizardAO(wizardDef.getId());
		result.setName(wizardDef.getName());

		result.setCancelRedirectUrl(wizardDef.getWizardCancelRedirectUrl());
		result.setFinishRedirectUrl(wizardDef.getWizardFinishRedirectUrl());

		result.setHandlerId(wizardDef.getHandler());

		result.setWizardSteps(toWizardSteps(wizardDef.getId(), wizardDef.getWizardSteps()));
		return result;
	}

	/**
	 * Create wizard steps collection based on configured via cms steps ids.
	 *
	 * @param wizardId	id of wizard
	 * @param wizardSteps {@link List<String>}
	 * @return {@link WizardStepAO}
	 */
	private List<WizardStepAO> toWizardSteps(String wizardId, List<String> wizardSteps) {
		List<WizardStepAO> resultSteps = new ArrayList<WizardStepAO>(wizardSteps.size());
		for (String pagexId : wizardSteps) {
			int position = wizardSteps.indexOf(pagexId);
			resultSteps.add(position, new WizardStepAO(wizardId, position, pagexId));
		}
		return resultSteps;
	}

	/**
	 * Remove WizardData session attribute.
	 *
	 * @param wizardId id of wizard
	 */
	private void removeWizardData(String wizardId) {
		removeAttributeFromSession(wizardId + WIZARD_ATTRIBUTE_SUFFIX);
	}

	/**
	 * WizardData object which represents APISessionAttribute for storing WizardStepAO.
	 */
	protected class WizardData implements Serializable {
		/**
		 * Basic serial version UID.
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * WizardData 'wizardId'.
		 */
		private String wizardId;
		/**
		 * WizardData 'currentStep'.
		 */
		private WizardStepAO currentStep;
		/**
		 * WizardData 'completedSteps'.
		 */
		private List<WizardStepAO> completedSteps = new ArrayList<WizardStepAO>();

		/**
		 * Constructor.
		 *
		 * @param aWizardId id
		 */
		public WizardData(String aWizardId) {
			this.wizardId = aWizardId;
		}

		public String getWizardId() {
			return wizardId;
		}

		public WizardStepAO getCurrentStep() {
			return currentStep;
		}

		public void setCurrentStep(WizardStepAO currentStep) {
			this.currentStep = currentStep;
		}

		public List<WizardStepAO> getCompletedSteps() {
			return completedSteps;
		}

		/**
		 * Add {@link WizardStepAO} to completed step collection if such still does not exists there.
		 *
		 * @param completed {@link WizardStepAO}
		 */
		public void addCompletedStep(WizardStepAO completed) {
			if (!completedSteps.contains(completed))
				completedSteps.add(completed);
		}


		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("WizardData");
			sb.append("{completedSteps=").append(completedSteps.size());
			sb.append(", wizardId='").append(wizardId).append('\'');
			sb.append(", currentStep=").append(currentStep);
			sb.append('}');
			return sb.toString();
		}
	}


}


