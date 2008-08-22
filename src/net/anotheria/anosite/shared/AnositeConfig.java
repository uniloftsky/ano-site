package net.anotheria.anosite.shared;

import net.java.dev.moskito.core.configuration.ConfigurationServiceFactory;
import net.java.dev.moskito.core.configuration.IConfigurable;

public class AnositeConfig implements IConfigurable{
	private static AnositeConfig instance = new AnositeConfig();
	
	public static final String CFG_ENFORCE_HTTPS = "enforcehttps";
	
	private boolean enforceHttps = true;
	
	
	public static AnositeConfig getInstance(){ return instance; }
		
	private AnositeConfig(){
		ConfigurationServiceFactory.getConfigurationService().addConfigurable(this);
	}


	@Override
	public String getConfigurationName() {
		return "anosite";
	}

	@Override
	public void notifyConfigurationFinished() {
	}

	@Override
	public void notifyConfigurationStarted() {
	}

	@Override
	public void setProperty(String name, String value) {
		if (CFG_ENFORCE_HTTPS.equals(name))
			enforceHttps = value.equals("true");
	}

	
	public boolean enforceHttps(){ return enforceHttps; }
}
