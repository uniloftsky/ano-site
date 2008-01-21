package net.anotheria.anosite.content.servlet;

import java.util.ArrayList;
import java.util.List;

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

public class BlueprintProducer implements IStatsProducer{
	private String producerId;
	private String category;
	private String subsystem;
	
	private ActionStats stats;
	
	private List<IStats> statsList;
	
	public BlueprintProducer(String aProducerId, String aCategory, String aSubsystem){
		producerId = aProducerId;
		category = aCategory;
		subsystem = aSubsystem;
		
		stats = new ActionStats("execute", getMonitoringIntervals());
		statsList = new ArrayList<IStats>();
		statsList.add(stats);
		
		ProducerRegistryFactory.getProducerRegistryInstance().registerProducer(this);

	}
	
	public Object execute(BlueprintCallExecutor executor, Object... parameters) throws Exception{
		stats.addRequest();
		long startTime = System.nanoTime();
		RunningUseCase aRunningUseCase = RunningUseCaseContainer.getCurrentRunningUseCase();
		PathElement currentElement = null;
		ExistingRunningUseCase runningUseCase = aRunningUseCase.useCaseRunning() ? 
				(ExistingRunningUseCase)aRunningUseCase : null; 
		if (runningUseCase !=null)
			currentElement = runningUseCase.startPathElement(new StringBuilder(getProducerId()).append('.').append("execute").toString());
		try {
			return executor.execute(parameters);
		}  catch (Exception e) {
			stats.notifyError();
			throw e;
		} finally {
			long duration = System.nanoTime() - startTime;
			stats.addExecutionTime(duration);
			stats.notifyRequestFinished();
			if (currentElement!=null)
				currentElement.setDuration(duration);
			if (runningUseCase !=null)
				runningUseCase.endPathElement();
		}
		
	}
	
	
	public String getCategory() {
		return category;
	}

	public String getProducerId() {
		return producerId;
	}

	public List<IStats> getStats() {
		return statsList;
	}

	public String getSubsystem() {
		return subsystem;
	}
	
	protected Interval[] getMonitoringIntervals(){
		return Constants.DEFAULT_INTERVALS;
	}

	
}
