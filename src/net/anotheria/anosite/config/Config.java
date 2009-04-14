package net.anotheria.anosite.config;

import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;


@ConfigureMe (name="testconfig")
public class Config{
	@Configure private String value;
	
	public static final String DEF_VALUE = "default";
	
	@Configure private String systemName;

	private static Config instance;
	
	private static Object lock = new Object();
	public static Config getInstance(){
		if(instance != null)
			return instance;
		synchronized(lock){
			if(instance != null)
				return instance;
			instance = new Config();
			ConfigurationManager.INSTANCE.configure(instance);
			return instance;
		}
	}
	
	private Config(){
		value = DEF_VALUE;
	}
	
	public void setValue(String aValue){
		value = aValue;
	}
	
	public void setSystemName(String aValue){
		systemName = aValue;
	}
	
	public String getValue(){
		return value;
	}
	

	public String getSystemName(){
		return systemName;
	}
	
	public boolean isSystemTest(){
		return systemName.startsWith(ConfigConst.MODE_TEST);
	}
	
	public boolean isSystemDevelop(){
		return systemName.startsWith(ConfigConst.MODE_DEVELOP);
	}
	
	public boolean isSystemProduction(){
		return systemName.startsWith(ConfigConst.MODE_PRODUCTION);
	}
}
