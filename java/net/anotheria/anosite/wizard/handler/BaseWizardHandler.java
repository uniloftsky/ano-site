package net.anotheria.anosite.wizard.handler;

import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.anosite.gen.aswizarddata.data.WizardDef;
import net.anotheria.anosite.gen.aswizarddata.data.WizardNavigation;
import net.anotheria.anosite.wizard.api.WizardAO;
import net.anotheria.anosite.wizard.api.WizardAPI;
import net.anotheria.anosite.wizard.api.WizardStepAO;
import net.anotheria.anosite.wizard.api.exception.WizardAPIException;
import net.anotheria.anosite.wizard.api.exception.WizardAPIFirstStepException;
import net.anotheria.anosite.wizard.api.exception.WizardAPILastStepException;
import net.anotheria.anosite.wizard.handler.exceptions.WizardHandlerException;
import net.anotheria.anosite.wizard.handler.exceptions.WizardHandlerPreProcessException;
import net.anotheria.anosite.wizard.handler.exceptions.WizardHandlerProcessException;
import net.anotheria.anosite.wizard.handler.exceptions.WizardHandlerSubmitException;
import net.anotheria.anosite.wizard.handler.response.WizardHandlerResponse;
import net.anotheria.anosite.wizard.handler.response.WizardResponseCancel;
import net.anotheria.anosite.wizard.handler.response.WizardResponseChangeStep;
import net.anotheria.anosite.wizard.handler.response.WizardResponseContinue;
import net.anotheria.anosite.wizard.handler.response.WizardResponseFinish;
import net.anotheria.util.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * BaseWizardHandler as main class for Wizard handlers.
 * Used as Default WizardHandler for wizards where handler is not specified.
 * Can be simply extended.
 *
 * @author h3ll
 */

//TODO : Review me Please!  - and decide  which methods should be declared private!
//TODO : Decide and  document 1 way  of  error notifications! (to predict current  tricky way with ERROR_NAMES).
public class BaseWizardHandler implements WizardHandler {

	/**
	 * Step menu request attribute name.
	 */
	public static final String STEP_MENU_REQUEST_DEFAULT_ATTRIBUTE_NAME = "stepMenu";
	/**
	 * Step navigation attribute name.
	 */
	public static final String STEP_NAVIGATION_REQUEST_DEFAULT_ATTRIBUTE_NAME = "stepNavigation";
	/**
	 * Error names array.
	 */
	private static final String[] ERROR_NAMES = new String[]{"error", "failure"};
	/**
	 * WizardNavigation attribute.
	 */
	protected static final String WIZARD_NAVIGATION_ATTRIBUTE = "wizard_navigation";

	/**
	 * Wizard menu attribute name.
	 */
	protected static final String WIZARD_MENU_ATTRIBUTE = "wizard_menu";
	/**
	 * Log4j logger.
	 */
	private Logger log;

	/**
	 * WizardAPI instance.
	 */
	private WizardAPI wizardAPI;

	/**
	 * Constructor.
	 */
	protected BaseWizardHandler() {
		log = Logger.getLogger(this.getClass());
		wizardAPI = APIFinder.findAPI(WizardAPI.class);
	}

	@Override
	public WizardHandlerResponse preProcess(HttpServletRequest req, HttpServletResponse res, WizardDef wizard) throws WizardHandlerPreProcessException {
		// adding cache control headers
		addCacheControlHeadersToResponse(res);
		return WizardResponseContinue.INSTANCE;
	}


	@Override
	public WizardHandlerResponse process(HttpServletRequest req, HttpServletResponse res, WizardDef wizard) throws WizardHandlerProcessException {

		if (wizard.getWizardStepsSize() == 0)
			throw new WizardHandlerProcessException(wizard.getId(), " wizard Steps are not configured!");
		// adding cache control headers
		addCacheControlHeadersToResponse(res);
		try {
			WizardAO wizardAO = wizardAPI.getWizard(wizard.getId());
			WizardStepAO currentStep = wizardAPI.getCurrentStep(wizard.getId());

			prepareMenu(req, wizard, currentStep);
			//prepare allowed navy!
			prepareNavigation(req, wizard, wizardAO.getNavigation(), currentStep);
			return handleIncomingCommand(req, wizard, currentStep, wizardAO.getNavigation(), false);

		} catch (WizardAPIException e) {
			throw new WizardHandlerProcessException(e.getMessage(), e);
		} catch (WizardHandlerException e) {
			throw new WizardHandlerProcessException(e.getMessage(), e);
		}
	}


	@Override
	public WizardHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, WizardDef wizard) throws WizardHandlerSubmitException {

		try {
			WizardAO wizardAO = wizardAPI.getWizard(wizard.getId());
			WizardStepAO currentStep = wizardAPI.getCurrentStep(wizard.getId());

			return handleIncomingCommand(req, wizard, currentStep, wizardAO.getNavigation(), true);
		} catch (WizardHandlerException e) {
			throw new WizardHandlerSubmitException(e.getMessage(), e);
		} catch (WizardAPIException e) {
			throw new WizardHandlerSubmitException(e.getMessage(), e);
		}

	}


	/**
	 * Prepare menu method. Creates and adds to request {@link List< WizardMenuItemBean >}, for further rendering on jsp.
	 * {@code BaseWizardHandler.WIZARD_MENU} - is request attribute name for it.
	 * <NOTE : in current implementation - only passed steps will contains step-links >
	 *
	 * @param req		 {@link HttpServletRequest}
	 * @param wizard	  {@link WizardDef}
	 * @param currentStep {@link WizardStepAO}
	 * @throws net.anotheria.anosite.wizard.handler.exceptions.WizardHandlerProcessException
	 *          on errors
	 */
	protected void prepareMenu(HttpServletRequest req, WizardDef wizard, WizardStepAO currentStep) throws WizardHandlerProcessException {
		if (!wizard.getMenuRenderingEnabled()) {
			log.debug("Menu rendering disabled");
			return;
		}
		try {
			List<WizardStepAO> completedSteps = wizardAPI.getCompletedSteps(wizard.getId());
			List<String> passedSteps = new ArrayList<String>();
			for (WizardStepAO ao : completedSteps)
				passedSteps.add(ao.getStepId());


			List<WizardMenuItemBean> wizardMenuBean = new ArrayList<WizardMenuItemBean>();
			//building menu
			for (String stepId : wizard.getWizardSteps()) {
				int stepPos = wizard.getWizardSteps().indexOf(stepId);
				boolean isCurrent = currentStep.getStepId().equals(stepId);
				boolean isPassed = passedSteps.contains(stepId);
				String stepUrl = isPassed ? WizardCommand.NAVIGATE_TO.getCommandTitle() + "=" + stepPos : "";

				wizardMenuBean.add(new WizardMenuItemBean(wizard.getName(), stepUrl, isCurrent, isPassed));
			}

			req.setAttribute(WIZARD_MENU_ATTRIBUTE, wizardMenuBean);


		} catch (WizardAPIException e) {
			log.error("prepareMenu failed", e);
			throw new WizardHandlerProcessException("prepare menu failed", e);
		}

	}


	/**
	 * Prepare allowed for each step navigation.
	 * Current method should check and add to request all required and allowed WizardCommands ....
	 * Commands  by default can be found under  {@code BaseWizardHandler.WIZARD_NAVIGATION_ATTRIBUTE}  in request.
	 *
	 * @param req		{@link HttpServletRequest}
	 * @param wizard	 {@link WizardDef}
	 * @param navigation {@link WizardNavigation}
	 * @param step	   {@link WizardStepAO}
	 */
	protected void prepareNavigation(HttpServletRequest req, WizardDef wizard, WizardNavigation navigation, WizardStepAO step) {
		if (!wizard.getNavigationRenderingEnabled()) {
			log.debug("Navigation rendering is disabled");
			return;
		}

		List<String> commands = new ArrayList<String>();
		// CANCEL
		if (navigation.getAllowCancellation())
			commands.add(WizardCommand.CANCEL.getCommandTitle());
		//BACK
		if (navigation.getAllowBackStep() && !isFirstStep(step, wizard))
			commands.add(WizardCommand.PREVIOUS.getCommandTitle());
		// NEXT
		if (!isLastStep(step, wizard))
			commands.add(WizardCommand.NEXT.getCommandTitle());
		// FINISH
		if (isLastStep(step, wizard))
			commands.add(WizardCommand.FINISH.getCommandTitle());

		req.setAttribute(WIZARD_NAVIGATION_ATTRIBUTE, commands);
	}


	/**
	 * Returns true if we can be forwarded to next step. False otherwise.
	 *
	 * @param req	  {@link HttpServletRequest}
	 * @param wizard   {@link WizardDef}
	 * @param step	 {@link WizardStepAO}
	 * @param isSubmit boolean value - true if called from submit
	 * @return boolean value
	 */
	protected boolean isNextStepAllowed(HttpServletRequest req, WizardDef wizard, WizardStepAO step, boolean isSubmit) {
		@SuppressWarnings({"unchecked"}) Enumeration<String> attributeNames = req.getAttributeNames();
		//TODO :  "isAjaxValidation"  change  to  validationHandler  constant after it  replace!
		return req.getParameter("isAjaxValidation") == null && !isErrorPresent(attributeNames) && isSubmit;
	}


	/**
	 * Returns true if we can be forwarded to previous step. False otherwise.
	 *
	 * @param req	  {@link HttpServletRequest}
	 * @param wizard   {@link WizardDef}
	 * @param step	 {@link WizardStepAO}
	 * @param isSubmit boolean value - true if called from submit
	 * @return boolean value
	 */
	protected boolean isPreviousStepAllowed(HttpServletRequest req, WizardDef wizard, WizardStepAO step, boolean isSubmit) {
		return !isSubmit && !isFirstStep(step, wizard);
	}


	/**
	 * Returns true if we can finish wizard.
	 *
	 * @param req	  {@link HttpServletRequest}
	 * @param wizard   {@link WizardDef}
	 * @param step	 {@link WizardStepAO}
	 * @param isSubmit boolean value - true if called from submit
	 * @return boolean value
	 */
	protected boolean isFinishAllowed(HttpServletRequest req, WizardDef wizard, WizardStepAO step, boolean isSubmit) {
		@SuppressWarnings({"unchecked"}) Enumeration<String> attributeNames = req.getAttributeNames();
		//TODO :  "isAjaxValidation"  change  to  validationHandler  constant after it  replace!
		return req.getParameter("isAjaxValidation") == null && isSubmit && isLastStep(step, wizard) && !isErrorPresent(attributeNames);
	}

	/**
	 * Returns true if we can cancel wizard.
	 *
	 * @param req	  {@link HttpServletRequest}
	 * @param wizard   {@link WizardDef}
	 * @param step	 {@link WizardStepAO}
	 * @param isSubmit boolean value - true if called from submit
	 * @return boolean value
	 */
	protected boolean isCancellationAllowed(HttpServletRequest req, WizardDef wizard, WizardStepAO step, boolean isSubmit) {
		return !isSubmit;
	}

	/**
	 * Current Method checks if some error  present! In this  base  implementation - it  simply search request attribute
	 * with some name  like "error"  or  attribute  which  contains  something  like "error" in name.
	 * <Note : in child  classes  override   current  method to extend  functionality >
	 *
	 * @param attributeNames request attributes names enumeration
	 * @return true if error exists, false otherwise
	 */
	protected boolean isErrorPresent(Enumeration<String> attributeNames) {
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			for (String possibleError : getPossibleNames())
				if (attributeName.contains(possibleError) || attributeName.equalsIgnoreCase(possibleError))
					return true;

		}
		return false;
	}

	/**
	 * Current method returns array of different error - names (possible error names or  errors names part).
	 * If You need - You can  simply override it.
	 *
	 * @return {@link String[]}} error names
	 */
	protected String[] getPossibleNames() {
		return ERROR_NAMES;
	}

	/**
	 * Handle incoming command method.
	 * Checks which command should be executed, and execute it.
	 *
	 * @param req		 {@link HttpServletRequest}
	 * @param wizard	  {@link WizardDef}
	 * @param currentStep {@link WizardStepAO}
	 * @param navigation  {@link WizardNavigation}
	 * @param isSubmit	boolean value - true if called from submit
	 * @return {@link WizardHandlerResponse}
	 * @throws net.anotheria.anosite.wizard.handler.exceptions.WizardHandlerException
	 *          on errors
	 */
	protected WizardHandlerResponse handleIncomingCommand(HttpServletRequest req, WizardDef wizard, WizardStepAO currentStep, WizardNavigation navigation,
														  boolean isSubmit) throws WizardHandlerException {
		// handle logic!


		if (!wizard.getNavigationRenderingEnabled()) {
			//only NEXT is allowed now!! means default one
			return processNextStep(req, wizard, currentStep, isSubmit);
		}
		@SuppressWarnings({"unchecked"})
		WizardCommand command = WizardCommand.getCommandByValue(req.getParameterMap());

		switch (command) {
			case FINISH:
				if (!isFinishAllowed(req, wizard, currentStep, isSubmit)) {
					log.debug("Wizard finish {" + wizard.getId() + "} not performed");
					return WizardResponseContinue.INSTANCE;
				}
				log.debug("Wizard finished {" + wizard.getId() + "}");
				removeWizardData(wizard);
				return new WizardResponseFinish(wizard.getWizardFinishRedirectUrl());
			case CANCEL:
				if (!navigation.getAllowCancellation() || !isCancellationAllowed(req, wizard, currentStep, isSubmit)) {
					log.debug("Wizard cancel {" + wizard.getId() + "} not performed");
					return WizardResponseContinue.INSTANCE;
				}
				log.debug("Wizard canceled {" + wizard.getId() + "}");
				removeWizardData(wizard);
				return new WizardResponseCancel(wizard.getWizardCancelRedirectUrl());
			case PREVIOUS:
				if (!navigation.getAllowBackStep() || !isPreviousStepAllowed(req, wizard, currentStep, isSubmit)) {
					log.debug("Wizard previous step {" + wizard.getId() + "} not performed");
					return WizardResponseContinue.INSTANCE;
				}
				log.debug("Wizard previous step {" + wizard.getId() + "}");

				try {
					WizardStepAO nextStep = wizardAPI.adjustToPreviousStep(wizard.getId());
				} catch (WizardAPIFirstStepException e) {
					return WizardResponseContinue.INSTANCE;
				} catch (WizardAPIException e) {
					throw new WizardHandlerException(e.getMessage(), e);
				}
				return WizardResponseChangeStep.INSTANCE;

			case NEXT:
				return processNextStep(req, wizard, currentStep, isSubmit);
			case NAVIGATE_TO:
				return processNavigateToStep(req, wizard, currentStep, isSubmit);

			default:
				throw new AssertionError("Unknown command" + command);
		}
	}

	/**
	 * Handle navigate to command.
	 * Current method will allow navigation only to passed steps!!!
	 *
	 * @param req		 {@link HttpServletRequest}
	 * @param wizard	  {@link HttpServletResponse}
	 * @param currentStep {@link WizardStepAO}
	 * @param isSubmit	is submit operation
	 * @return {@link WizardHandlerResponse}
	 * @throws WizardHandlerException on errors
	 */
	private WizardHandlerResponse processNavigateToStep(HttpServletRequest req, WizardDef wizard, WizardStepAO currentStep, boolean isSubmit) throws WizardHandlerException {
		if (!wizard.getMenuRenderingEnabled() || isSubmit) {
			log.warn("Menu rendering is disabled. NAVIGATE_TO can't be executed.");
			return WizardResponseContinue.INSTANCE;
		}

		String stepIndex = req.getParameter(WizardCommand.NAVIGATE_TO.getCommandTitle());
		if (StringUtils.isEmpty(stepIndex)) {
			log.warn("Required parameter not found.");
			return WizardResponseContinue.INSTANCE;
		}
		int stepNumber = Integer.valueOf(stepIndex);
		if (stepNumber < 0 || stepNumber >= wizard.getWizardStepsSize()) {
			log.debug("Illegal step " + stepNumber);
			return WizardResponseContinue.INSTANCE;
		}

		if (stepNumber == currentStep.getStepIndex() && wizard.getWizardStepsElement(stepNumber).equals(currentStep.getStepId())) {
			log.debug("step number is current");
			return WizardResponseContinue.INSTANCE;
		}
		try {
			List<WizardStepAO> passedSteps = wizardAPI.getCompletedSteps(wizard.getId());
			for (WizardStepAO passed : passedSteps)
				if (passed.getStepIndex() == stepNumber && passed.getStepId().equals(wizard.getWizardStepsElement(stepNumber))) {
					WizardStepAO nextStep = wizardAPI.adjustToStep(wizard.getId(), stepNumber);
					return WizardResponseChangeStep.INSTANCE;
				}
			log.debug("Can't navigate to not passed step!!!");
			return WizardResponseContinue.INSTANCE;

		} catch (WizardAPIException e) {
			throw new WizardHandlerException("process navigateToStep failed  step:{" + stepIndex + "}", e);
		}

	}

	/**
	 * Process next step command.
	 * Current method can work with enabled and disabled navigation.
	 * If navi rendering is disabled, we can simply finish current wizard!
	 *
	 * @param req		 {@link HttpServletRequest}
	 * @param wizard	  {@link WizardDef}
	 * @param currentStep {@link WizardStepAO}
	 * @param isSubmit	is submit
	 * @return {@link WizardHandlerResponse}
	 * @throws WizardHandlerException on errors
	 */
	private WizardHandlerResponse processNextStep(HttpServletRequest req, WizardDef wizard, WizardStepAO currentStep, boolean isSubmit)
			throws WizardHandlerException {
		if (!isNextStepAllowed(req, wizard, currentStep, isSubmit)) {
			log.debug("Wizard next step {" + wizard.getId() + "} not performed");
			return WizardResponseContinue.INSTANCE;
		}
		try {
			WizardStepAO nextStep = wizardAPI.adjustToNextStep(wizard.getId());
		} catch (WizardAPILastStepException e) {
			//handle FINSH!!! if navi is disabled
			if (!wizard.getNavigationRenderingEnabled() && isFinishAllowed(req, wizard, currentStep, isSubmit)) {

				log.debug("Wizard finished {" + wizard.getId() + "}");
				removeWizardData(wizard);
				return new WizardResponseFinish(wizard.getWizardFinishRedirectUrl());
			}
			return WizardResponseContinue.INSTANCE;
		} catch (WizardAPIException e) {
			throw new WizardHandlerException(e.getMessage(), e);
		}

		return WizardResponseChangeStep.INSTANCE;
	}

	/**
	 * Current method add browser cache-control headers to response.
	 * Current will disable browser cache for all  wizard pages.
	 * <p/>
	 * Headers "Cache-Control", "Pragma", "Expires" with values. etc.
	 *
	 * @param response {@link HttpServletResponse}
	 */
	private void addCacheControlHeadersToResponse(HttpServletResponse response) {
		// "Cache-Control" header.
		response.setHeader("Cache-Control", "no-cache, no-store, post-check=0, pre-check=0");
		// "Pragma" header .
		response.setHeader("Pragma", "no-cache");
		// "Expires" header.
		response.setHeader("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");

	}


	/**
	 * Returns logger.
	 *
	 * @return {@link Logger}
	 */
	protected Logger getLogger() {
		return log;
	}


	/**
	 * Return step menu request attribute name.
	 *
	 * @return string
	 */
	protected String getStepMenuAttributeName() {
		return STEP_MENU_REQUEST_DEFAULT_ATTRIBUTE_NAME;
	}

	/**
	 * Return step navigation request attribute name.
	 *
	 * @return string
	 */
	protected String getStepNavigationAttributeName() {
		return STEP_NAVIGATION_REQUEST_DEFAULT_ATTRIBUTE_NAME;
	}

	/**
	 * Return {@link WizardAPI}.
	 *
	 * @return {@link WizardAPI}
	 */
	public WizardAPI getWizardAPI() {
		return wizardAPI;
	}

	/**
	 * Remove attribute from session.
	 *
	 * @param wizard {@link WizardDef}
	 */
	private void removeWizardData(WizardDef wizard) {
		try {
			wizardAPI.removeWizardData(wizard.getId());
		} catch (WizardAPIException e) {
			log.warn("failure removing wizardData wizard{" + wizard.getId() + "}");
		}
	}


	/**
	 * Returns true if currently wizard stays on first step.
	 *
	 * @param step   {@link WizardStepAO}
	 * @param wizard {@link WizardDef}
	 * @return boolean value
	 */
	private boolean isFirstStep(WizardStepAO step, WizardDef wizard) {
		final int firstStepIndex = 0;
		return step.getStepIndex() == firstStepIndex && step.getWizardId().equals(wizard.getId()) && step.getStepId().equals(wizard.getWizardStepsElement(firstStepIndex));
	}

	/**
	 * Returns true if currently wizard stays on last step.
	 *
	 * @param step   {@link WizardStepAO}
	 * @param wizard {@link WizardDef}
	 * @return boolean value
	 */
	private boolean isLastStep(WizardStepAO step, WizardDef wizard) {
		final int lastStepIndex = wizard.getWizardStepsSize() - 1;
		return step.getStepIndex() == lastStepIndex && step.getStepId().equals(wizard.getWizardStepsElement
				(lastStepIndex)) || step.getStepIndex() > lastStepIndex;
	}


}
