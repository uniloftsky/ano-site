package net.anotheria.anosite.api.common;

import java.util.HashMap;
import java.util.Map;

import net.java.dev.moskito.core.configuration.ConfigurationServiceFactory;
import net.java.dev.moskito.core.configuration.IConfigurable;


public class APIConfigurable implements IConfigurable{
	
	public static final String CFG_VERBOSE_METHOD_CALLS = "verbose_method_calls";
	public static final String CFG_TEST_MODE            = "test_mode";
	
	public static final boolean DEF_VERBOSE_METHOD_CALLS = false;
	public static final boolean DEF_TEST_MODE            = false;
	
	private static APIConfigurable instance;

	private boolean verboseMethodCalls = DEF_VERBOSE_METHOD_CALLS;
	private boolean testMode = DEF_TEST_MODE;
	private Map<String, String> properties;
	
	public APIConfigurable(){
		ConfigurationServiceFactory.getConfigurationService().addConfigurable(this);
	}
	
	public String getConfigurationName() {
		return "apiconfig";
	}

	public void notifyConfigurationStarted() {
		properties = new HashMap<String, String>();
	}

	public void notifyConfigurationFinished() {
	}

	public void setProperty(String name, String value) {
		if (name==null || name.length()==0)
			return;
		properties.put(name, value);
		if (name.equals(CFG_VERBOSE_METHOD_CALLS)){
			verboseMethodCalls = value.equalsIgnoreCase("true");
		}
		if (name.equals(CFG_TEST_MODE)){
			testMode = value.equalsIgnoreCase("true");
		}
	}
	
	public boolean verboseMethodCalls(){
		return verboseMethodCalls;
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean isTestMode) {
		testMode = isTestMode;
	}

	public String getProperty(String key, String defaultValue) {
		String value = properties.get(key);
		return value == null ? defaultValue : value;
	}
	
	public static synchronized APIConfigurable getInstance() {
		if(instance == null) {
			instance = new APIConfigurable();
		}
		return instance;
	}

}
