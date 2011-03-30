package net.anotheria.anosite.wizard.api;

import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.anoplass.api.session.APISession;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.aswizarddata.data.WizardDef;
import net.anotheria.anosite.gen.aswizarddata.data.WizardNavigation;
import net.anotheria.anosite.gen.aswizarddata.data.WizardNavigationDocument;
import net.anotheria.anosite.gen.aswizarddata.service.ASWizardDataServiceException;
import net.anotheria.anosite.gen.aswizarddata.service.IASWizardDataService;
import net.anotheria.anosite.gen.aswizarddata.service.WizardDefNotFoundInASWizardDataServiceException;
import net.anotheria.anosite.wizard.api.exception.WizardAPIException;
import net.anotheria.anosite.wizard.api.exception.WizardAPIFirstStepException;
import net.anotheria.anosite.wizard.api.exception.WizardAPILastStepException;
import net.anotheria.anosite.wizard.api.exception.WizardNotFoundException;
import net.anotheria.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	 * Log4j logger instance.
	 */
	private static final Logger LOG = Logger.getLogger(BaseWizardAPIImpl.class);
	/**
	 * DEFAULT_NAVIGATION_NAME.
	 */
	private static final String DEFAULT_NAVIGATION_NAME = "defaultNavigation";
	/**
	 * DEFAULT_NAVIGATION_ID.
	 */
	private static final String DEFAULT_NAVIGATION_ID = "0";

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
			log.error("getWizardByName(" + wizardName + ")", e);
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
			log.error("getWizard(" + wizardId + ")", e);
			throw new WizardAPIException("Backend failure", e);
		}

	}

	@Override
	public String getCurrentStepId(String wizardId) throws WizardAPIException {
		return getCurrentStep(wizardId).getStepId();
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
		if (isLastStep(currentStep, wizard.getWizardSteps()))
			throw new WizardAPILastStepException(wizardId, currentStep.getStepId(), currentStep.getStepIndex());

		final int nextStepIndex = currentStep.getStepIndex() + 1;
		//mark step completed
		data.addCompletedStep(data.getCurrentStep());
		WizardStepAO nextStep = wizard.getWizardSteps().get(nextStepIndex);
		data.setCurrentStep(nextStep);
		//returning nextStep data
		return nextStep;
	}

	/**
	 * Return true if current step is last.
	 *
	 * @param currentStep {@link WizardStepAO}
	 * @param wizardSteps {@link List<WizardStepAO> wizardSteps}
	 * @return true if current step is last, false otherwise
	 */
	private boolean isLastStep(WizardStepAO currentStep, List<WizardStepAO> wizardSteps) {
		final int lastStepIndex = wizardSteps.size() - 1;
		return currentStep.getStepIndex() == lastStepIndex && currentStep.getStepId().equals(wizardSteps.get(lastStepIndex).getStepId());

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
		if (isFirstStep(currentStep, wizard.getWizardSteps()))
			throw new WizardAPIFirstStepException(wizardId, currentStep.getStepId(), currentStep.getStepIndex());
		final int prevStepIndex = currentStep.getStepIndex() - 1;

		//we should not mark step as completed
		WizardStepAO prevStep = wizard.getWizardSteps().get(prevStepIndex);
		data.setCurrentStep(prevStep);
		//returning prevStep data
		return prevStep;
	}

	@Override
	public WizardStepAO adjustToStep(String wizardId, int stepIndex) throws WizardAPIException {

		WizardAO wizard = getWizard(wizardId);

		if (stepIndex < 0 || stepIndex >= wizard.getWizardSteps().size())
			throw new IllegalArgumentException("Step index is illegal " + stepIndex);

		WizardStepAO currentStep = getCurrentStep(wizardId);
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
	 * Return true if current step is first.
	 *
	 * @param currentStep {@link WizardStepAO}
	 * @param wizardSteps {@link List<String> wizardSteps}
	 * @return true if current step is first, false otherwise
	 */
	private boolean isFirstStep(WizardStepAO currentStep, List<WizardStepAO> wizardSteps) {
		final int firstStepIndex = 0;
		return currentStep.getStepIndex() == firstStepIndex && currentStep.getStepId().equals(wizardSteps.get(firstStepIndex).getStepId());

	}


	@Override
	public void removeWizardData(String wizardId) throws WizardAPIException {
		removeAttributeFromSession(wizardId + WIZARD_ATTRIBUTE_SUFFIX);
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
		//TODO : think - maybe  we need it  distributed :)
		setAttributeInSession(wizard.getId() + WIZARD_ATTRIBUTE_SUFFIX, data);
		return data;
	}


	/**
	 * Creates {@link WizardAO} based on {@link WizardDef}.
	 *
	 * @param wizardDef {@link WizardDef}
	 * @return {@link WizardAO}
	 */
	private WizardAO toWizardAO(WizardDef wizardDef) {
		WizardAO result = new WizardAO(wizardDef.getId());
		result.setName(wizardDef.getName());

		result.setCancelRedirectUrl(wizardDef.getWizardCancelRedirectUrl());
		result.setFinishRedirectUrl(wizardDef.getWizardFinishRedirectUrl());

		result.setHandlerId(wizardDef.getHandler());
		result.setMenuEnabled(wizardDef.getMenuRenderingEnabled());
		result.setNavigationEnabled(wizardDef.getMenuRenderingEnabled());

		result.setNavigation(getNavigation(wizardDef.getNavigation()));
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
		List<WizardStepAO> steps = new ArrayList<WizardStepAO>(wizardSteps.size());
		for (String stepId : wizardSteps) {
			int position = wizardSteps.indexOf(stepId);
			steps.add(position, new WizardStepAO(wizardId, stepId, position));
		}
		return steps;
	}

	/**
	 * Retrieves wizard navigation.
	 * If navigation was not attached to wizard via cms, or some error happens, default navy will be used.
	 *
	 * @param navigation navi id
	 * @return {@link WizardNavigation}
	 */
	private WizardNavigation getNavigation(String navigation) {

		WizardNavigation navi = null;
		if (!StringUtils.isEmpty(navigation))
			try {
				navi = wizardDataService.getWizardNavigation(navigation);
			} catch (ASWizardDataServiceException e) {
				log.warn(e.getMessage(), e);
			}

		if (navi == null) {
			navi = new WizardNavigationDocument(DEFAULT_NAVIGATION_ID);
			navi.setName(DEFAULT_NAVIGATION_NAME);
			navi.setAllowCancellation(false);
			navi.setAllowBackStep(false);
		}
		return navi;
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
