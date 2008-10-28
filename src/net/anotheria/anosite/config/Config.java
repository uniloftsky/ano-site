package net.anotheria.anosite.config;

import net.java.dev.moskito.core.configuration.ConfigurationServiceFactory;
import net.java.dev.moskito.core.configuration.IConfigurable;

public class Config implements IConfigurable{
	private String value;
	
	public static final String DEF_VALUE = "default";
	public static final String KEY_SYSTEM_NAME = "system.name"; 
	
	private String systemName;

	private static Config instance;
	
	private static Object lock = new Object();
	public static Config getInstance(){
		if(instance != null)
			return instance;
		synchronized(lock){
			if(instance != null)
				return instance;
			instance = new Config();
			return instance;
		}
	}
	
	private Config(){
		value = DEF_VALUE;
		ConfigurationServiceFactory.getConfigurationService().addConfigurable(this);
	}
	
	public String getConfigurationName() {
		return "testconfig";
	}

	public void notifyConfigurationFinished() {
		// TODO Auto-generated method stub
		
	}

	public void notifyConfigurationStarted() {
		// TODO Auto-generated method stub
		
	}

	public void setProperty(String key, String aValue) {
		//System.out.println("Set: "+key+", value:"+aValue);
		if ("value".equals(key))
			this.value = aValue;
		
		if (KEY_SYSTEM_NAME.equals(key))
			systemName = aValue;
		
	}
	
	public String getValue(){
		return value;
	}
	

	public String getSystemName(){
		return systemName;
	}
	
	public boolean isSystemTest(){
		return ConfigConst.MODE_TEST.equals(systemName);
	}
	
	public boolean isSystemDevelop(){
		return ConfigConst.MODE_DEVELOP.equals(systemName);
	}
	
	public boolean isSystemProduction(){
		return ConfigConst.MODE_PRODUCTION.equals(systemName);
	}
}
