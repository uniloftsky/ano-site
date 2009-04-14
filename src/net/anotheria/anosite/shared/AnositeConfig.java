package net.anotheria.anosite.shared;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

@ConfigureMe
public class AnositeConfig{
	private static AnositeConfig instance = new AnositeConfig();
	
	public static final String CFG_ENFORCE_HTTPS = "enforcehttps";
	
	@Configure private boolean enforceHttps = true;
	
	
	public static AnositeConfig getInstance(){ return instance; }
		
	private AnositeConfig(){
		ConfigurationManager.INSTANCE.configure(this);
	}
	
	public boolean enforceHttps(){ return enforceHttps; }
	
	public void setEnforceHttps(boolean aValue){
		enforceHttps = aValue;
	}
}
