package net.anotheria.anosite.handler;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.util.AnositeConstants;
import net.anotheria.moskito.core.calltrace.CurrentlyTracedCall;
import net.anotheria.moskito.core.calltrace.RunningTraceContainer;
import net.anotheria.moskito.core.calltrace.TraceStep;
import net.anotheria.moskito.core.calltrace.TracedCall;
import net.anotheria.moskito.core.predefined.ActionStats;
import net.anotheria.moskito.core.predefined.Constants;
import net.anotheria.moskito.core.producers.IStats;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.anotheria.moskito.core.stats.Interval;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * A stats producer for boxhandler for embedding into moskito.
 * @author another
 *
 */
public class BoxHandlerProducer implements IStatsProducer {
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
		TracedCall aRunningUseCase = RunningTraceContainer.getCurrentlyTracedCall();
		TraceStep currentElement = null;
		CurrentlyTracedCall runningUseCase = aRunningUseCase.callTraced() ? 
				(CurrentlyTracedCall)aRunningUseCase : null;
		if (runningUseCase !=null)
			currentElement = runningUseCase.startStep(new StringBuilder(getProducerId()).append('.').append("process").toString(), this);
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
				runningUseCase.endStep();
		}
		
	}
	
	BoxHandlerResponse submit(HttpServletRequest req, HttpServletResponse res, Box box, BoxHandler target){
		submitStats.addRequest();
		long startTime = System.nanoTime();
		TracedCall aRunningUseCase = RunningTraceContainer.getCurrentlyTracedCall();
		TraceStep currentElement = null;
		CurrentlyTracedCall runningUseCase = aRunningUseCase.callTraced() ? 
				(CurrentlyTracedCall)aRunningUseCase : null; 
		if (runningUseCase !=null)
			currentElement = runningUseCase.startStep(new StringBuilder(getProducerId()).append('.').append("submit").toString(), this);
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
				runningUseCase.endStep();
		}
		
	}

}
