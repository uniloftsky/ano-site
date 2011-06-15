package net.anotheria.anosite.wizard.handler;

import net.anotheria.anosite.gen.aswizarddata.data.WizardDef;
import net.anotheria.anosite.util.AnositeConstants;
import net.anotheria.anosite.wizard.handler.response.WizardHandlerResponse;
import net.anotheria.anosite.wizard.handler.response.WizardResponseAbort;
import net.java.dev.moskito.core.predefined.ActionStats;
import net.java.dev.moskito.core.predefined.Constants;
import net.java.dev.moskito.core.producers.IStats;
import net.java.dev.moskito.core.producers.IStatsProducer;
import net.java.dev.moskito.core.registry.ProducerRegistryFactory;
import net.java.dev.moskito.core.stats.Interval;
import net.java.dev.moskito.core.usecase.running.ExistingRunningUseCase;
import net.java.dev.moskito.core.usecase.running.PathElement;
import net.java.dev.moskito.core.usecase.running.RunningUseCase;
import net.java.dev.moskito.core.usecase.running.RunningUseCaseContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Wizard handler producer, as Moskito IStatsProducer.
 *
 * @author h3ll
 */
public final class WizardHandlerProducer implements IStatsProducer {
	/**
	 * WizardHandlerProducer 'producerId'.
	 */
	private String producerId;
	/**
	 * WizardHandlerProducer 'preProcessStats'.
	 */
	private ActionStats preProcessStats;

	/**
	 * WizardHandlerProducer 'process'.
	 */
	private ActionStats process;
	/**
	 * WizardHandlerProducer 'submit'.
	 */
	private ActionStats submit;

	/**
	 * Cached list with stats.
	 */
	private final List<IStats> myStats;

	/**
	 * Handler Constructor.
	 *
	 * @param aProducerId id
	 */
	public WizardHandlerProducer(String aProducerId) {
		producerId = aProducerId;

		preProcessStats = new ActionStats("preProcess", getMonitoringIntervals());
		process = new ActionStats("process", getMonitoringIntervals());
		submit = new ActionStats("submit", getMonitoringIntervals());

		myStats = new ArrayList<IStats>();
		myStats.add(preProcessStats);
		myStats.add(process);
		myStats.add(submit);

		ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(this);
	}

	protected Interval[] getMonitoringIntervals() {
		return Constants.getDefaultIntervals();
	}

	@Override
	public String getCategory() {
		return "wizard-handler";
	}

	@Override
	public String getProducerId() {
		return producerId;
	}

	@Override
	public List<IStats> getStats() {
		return myStats;
	}

	@Override
	public String getSubsystem() {
		return AnositeConstants.AS_MOSKITO_SUBSYSTEM;
	}

	/**
	 * PreProcess for Wizard.
	 *
	 * @param req	{@link HttpServletRequest}
	 * @param res	{@link HttpServletResponse}
	 * @param wizard {@link WizardDef}
	 * @param target {@link WizardHandler}
	 * @return {@link WizardHandlerResponse}
	 */
	WizardHandlerResponse preProcess(HttpServletRequest req, HttpServletResponse res, WizardDef wizard, WizardHandler target) {
		preProcessStats.addRequest();
		long startTime = System.nanoTime();
		RunningUseCase aRunningUseCase = RunningUseCaseContainer.getCurrentRunningUseCase();
		PathElement currentElement = null;
		ExistingRunningUseCase runningUseCase = aRunningUseCase.useCaseRunning() ?
				(ExistingRunningUseCase) aRunningUseCase : null;
		if (runningUseCase != null)
			currentElement = runningUseCase.startPathElement(new StringBuilder(getProducerId()).append('.').append("preProcess").toString());
		try {
			return target.preProcess(req, res, wizard);
		} catch (Exception e) {
			preProcessStats.notifyError();
			return new WizardResponseAbort(e);
		} finally {
			long duration = System.nanoTime() - startTime;
			preProcessStats.addExecutionTime(duration);
			preProcessStats.notifyRequestFinished();
			if (currentElement != null)
				currentElement.setDuration(duration);
			if (runningUseCase != null)
				runningUseCase.endPathElement();
		}

	}


	/**
	 * Process for Wizard.
	 *
	 * @param req	{@link HttpServletRequest}
	 * @param res	{@link HttpServletResponse}
	 * @param wizard {@link WizardDef}
	 * @param target {@link WizardHandler}
	 * @return {@link WizardHandlerResponse}
	 */
	WizardHandlerResponse process(HttpServletRequest req, HttpServletResponse res, WizardDef wizard, WizardHandler target) {
		process.addRequest();
		long startTime = System.nanoTime();
		RunningUseCase aRunningUseCase = RunningUseCaseContainer.getCurrentRunningUseCase();
		PathElement currentElement = null;
		ExistingRunningUseCase runningUseCase = aRunningUseCase.useCaseRunning() ?
				(ExistingRunningUseCase) aRunningUseCase : null;
		if (runningUseCase != null)
			currentElement = runningUseCase.startPathElement(new StringBuilder(getProducerId()).append('.').append("process").toString());
		try {
			return target.process(req, res, wizard);
		} catch (Exception e) {
			process.notifyError();
			return new WizardResponseAbort(e);
		} finally {
			long duration = System.nanoTime() - startTime;
			process.addExecutionTime(duration);
			process.notifyRequestFinished();
			if (currentElement != null)
				currentElement.setDuration(duration);
			if (runningUseCase != null)
				runningUseCase.endPathElement();
		}

	}

	/**
	 * Submit for Wizard.
	 *
	 * @param req	{@link HttpServletRequest}
	 * @param res	{@link HttpServletResponse}
	 * @param wizard {@link WizardDef}
	 * @param target {@link WizardHandler}
	 * @return {@link WizardHandlerResponse}
	 */
	WizardHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, WizardDef wizard, WizardHandler target) {
		submit.addRequest();
		long startTime = System.nanoTime();
		RunningUseCase aRunningUseCase = RunningUseCaseContainer.getCurrentRunningUseCase();
		PathElement currentElement = null;
		ExistingRunningUseCase runningUseCase = aRunningUseCase.useCaseRunning() ?
				(ExistingRunningUseCase) aRunningUseCase : null;
		if (runningUseCase != null)
			currentElement = runningUseCase.startPathElement(new StringBuilder(getProducerId()).append('.').append("submit").toString());
		try {
			return target.submit(req, res, wizard);
		} catch (Exception e) {
			submit.notifyError();
			return new WizardResponseAbort(e);
		} finally {
			long duration = System.nanoTime() - startTime;
			submit.addExecutionTime(duration);
			submit.notifyRequestFinished();
			if (currentElement != null)
				currentElement.setDuration(duration);
			if (runningUseCase != null)
				runningUseCase.endPathElement();
		}

	}


}
