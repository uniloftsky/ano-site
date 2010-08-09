package net.anotheria.anosite.handler;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.util.AnositeConstants;
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
/**
 * A stats producer for boxhandler for embedding into moskito.
 * @author another
 *
 */
public class BoxHandlerProducer implements IStatsProducer{
	/**
	 * The id of the producer.
	 */
	private String producerId;
	/**
	 * Stats for process method.
	 */
	private ActionStats processStats;
	/**
	 * Stats for submit method.
	 */
	private ActionStats submitStats;
	/**
	 * Cached list with stats.
	 */
	private final List<IStats> myStats;
	
	public BoxHandlerProducer (String aProducerId){
		producerId = aProducerId;
		
		processStats = new ActionStats("process", getMonitoringIntervals());
		submitStats = new ActionStats("submit", getMonitoringIntervals());
		
		myStats = new ArrayList<IStats>();
		myStats.add(processStats);
		myStats.add(submitStats);
		
		ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(this);
	}
	
	protected Interval[] getMonitoringIntervals(){
		return Constants.getDefaultIntervals();
	}
	
	@Override public String getCategory() {
		return "box-handler";
	}

	@Override public String getProducerId() {
		return producerId;
	}

	@Override public List<IStats> getStats() {
		return myStats;
	}

	@Override public String getSubsystem() {
		return AnositeConstants.AS_MOSKITO_SUBSYSTEM;
	}
	
	BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean, BoxHandler target){
		processStats.addRequest();
		long startTime = System.nanoTime();
		RunningUseCase aRunningUseCase = RunningUseCaseContainer.getCurrentRunningUseCase();
		PathElement currentElement = null;
		ExistingRunningUseCase runningUseCase = aRunningUseCase.useCaseRunning() ? 
				(ExistingRunningUseCase)aRunningUseCase : null; 
		if (runningUseCase !=null)
			currentElement = runningUseCase.startPathElement(new StringBuilder(getProducerId()).append('.').append("process").toString());
		try {
			return target.process(req, res, box, bean);
		}  catch (Exception e) {
			processStats.notifyError();
			return new ResponseAbort(e);
		} finally {
			long duration = System.nanoTime() - startTime;
			processStats.addExecutionTime(duration);
			processStats.notifyRequestFinished();
			if (currentElement!=null)
				currentElement.setDuration(duration);
			if (runningUseCase !=null)
				runningUseCase.endPathElement();
		}
		
	}
	
	BoxHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, Box box, BoxHandler target){
		submitStats.addRequest();
		long startTime = System.nanoTime();
		RunningUseCase aRunningUseCase = RunningUseCaseContainer.getCurrentRunningUseCase();
		PathElement currentElement = null;
		ExistingRunningUseCase runningUseCase = aRunningUseCase.useCaseRunning() ? 
				(ExistingRunningUseCase)aRunningUseCase : null; 
		if (runningUseCase !=null)
			currentElement = runningUseCase.startPathElement(new StringBuilder(getProducerId()).append('.').append("submit").toString());
		try {
			return target.submit(req, res, box);
		}  catch (Exception e) {
			processStats.notifyError();
			return new ResponseAbort(e);
		} finally {
			long duration = System.nanoTime() - startTime;
			submitStats.addExecutionTime(duration);
			submitStats.notifyRequestFinished();
			if (currentElement!=null)
				currentElement.setDuration(duration);
			if (runningUseCase !=null)
				runningUseCase.endPathElement();
		}
		
	}

}
