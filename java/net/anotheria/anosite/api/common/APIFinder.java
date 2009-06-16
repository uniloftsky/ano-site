package net.anotheria.anosite.api.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anotheria.anosite.api.mock.APIMaskImpl;
import net.anotheria.anosite.api.mock.APIMockImpl;
import net.anotheria.anosite.api.mock.MockMethodRegistry;
import net.anotheria.anosite.api.mock.MaskMethodRegistry;
import net.java.dev.moskito.core.dynamic.MoskitoInvokationProxy;
import net.java.dev.moskito.core.predefined.ServiceStatsCallHandler;
import net.java.dev.moskito.core.predefined.ServiceStatsCallHandlerWithCallSysout;
import net.java.dev.moskito.core.predefined.ServiceStatsFactory;

import org.apache.log4j.Logger;

/**
 * The utility for API resolution.
 * @author lrosenberg
 *
 */
public class APIFinder {
	
	/**
	 * Storage for API instances.
	 */
	private static Map<Class<? extends API>, API> apis;
	/**
	 * Inernal storage for API factories.
	 */
	private static Map<Class<? extends API>, APIFactory<? extends API>> factories;
	
	/**
	 * Logger.
	 */
	private static Logger log;
	
	static{
		log = Logger.getLogger(APIFinder.class);
		//BasicConfigurator.configure();
		init();
	}
	
	/**
	 * True if masking is enabled. If masking is enabled any created API will be proxied by a masking api which allows to overwrite any single method in the API
	 * by putting it in the MaskMethodRegistry. Masking must be enabled before usage (in @BeforeClass method of your unittest).
	 */
	private static boolean maskingEnabled = false;
	/**
	 * True if mocking is enabled. If mocking is enabled any non existing, not registered API will be created on the fly and proxied by a mocking api,
	 * which allows to call single API methods by putting them in the MockMethodRegistry. Mocking must be enabled before usage (in @BeforeClass method of your unittest).
	 */
	private static boolean mockingEnabled = false;
	
	
	@SuppressWarnings("unchecked")
	public static API findAPI(String identifier){
		try{
			return findAPI((Class<? extends API>)Class.forName(identifier));
		}catch(ClassNotFoundException e){
			throw new RuntimeException("Class not found: "+identifier+", exception: "+e.getMessage());
		}
	}
	
	/**
	 * Returns the implementation for the given API interface clazz.
	 * @param <T> the API class.
	 * @param identifier T.class.
	 * @return an implementation of T.
	 */
	public static<T  extends API> T findAPI(Class<T> identifier){
		log.debug("find api: "+identifier);
		@SuppressWarnings("unchecked")
		T loaded = (T) apis.get(identifier);
		log.debug(" loaded: "+loaded);
		if (loaded != null)
			return loaded;
		synchronized (apis) {
			@SuppressWarnings("unchecked")
			T doubleChecked = (T)apis.get(identifier);
			log.debug("\t doubleChecked: "+doubleChecked);
			if (doubleChecked!=null)
				return doubleChecked;
			T created = createAPI(identifier);
			log.info(" created api: "+created+" for "+identifier);
			apis.put(identifier, created);
			log.debug(" calling init "+identifier);
			try{
				created.init();
			}catch(Exception e){
				log.error("findAPI.init in API "+identifier, e);
			}
			log.debug("ready");
			return created;
		}
	}
	
	/**
	 * Creates a new instance of T.
	 * @param <T> the API class to create.
	 * @param identifier a pattern.
	 * @return
	 */
	private synchronized static<T extends API> T createAPI(Class<T> identifier){
		@SuppressWarnings("unchecked")
		APIFactory<T> factory = (APIFactory<T>)factories.get(identifier);
		if (factory==null){
			//if no factory is configured but mocking is enabled we create a mock-api on the fly.
			if (isMockingEnabled())
				return createMockAPI(identifier);
			throw new NoSuchAPIException(identifier.getName());
		}
		
		log.debug("------ START creation API "+identifier);
		
		T newAPI = factory.createAPI();
		
		log.debug("\tcreated new instance: "+newAPI);
		
		if (APIConfig.isInTestMode()){
			if (newAPI instanceof TestModeAware)
				((TestModeAware)newAPI).setTestMode(true);
		}
		
		//masking
		if (isMaskingEnabled()){
			APIMaskImpl<T> maskedAPI = new APIMaskImpl<T>(newAPI, identifier);
			newAPI = maskedAPI.createAPIProxy();
		}//masking end
		
		
		//newAPI.init();
		
		//log.debug("\tinited "+newAPI);
		
		try{
			Class<? extends API>[] interfaces;
			List<Class<? extends API>> aliases = APIConfig.getAliases(identifier);
			if (aliases!=null && aliases.size()>0){
				@SuppressWarnings("unchecked") 
				Class<? extends API>[] _interfaces = (Class<? extends API>[]) new Class[aliases.size()+2];
				interfaces = _interfaces;
				int i = 2;
				for (Class<? extends API> a : aliases)
					interfaces[i++] = a;
			}else{
				@SuppressWarnings("unchecked") 
				Class<? extends API>[] _interfaces = (Class<? extends API>[]) new Class[2];
				interfaces = _interfaces;
			}
			
			interfaces[0] = identifier;
			interfaces[1] = API.class;

			MoskitoInvokationProxy proxy = new MoskitoInvokationProxy(
				newAPI,
				APIConfig.verboseMethodCalls() ? new ServiceStatsCallHandlerWithCallSysout() : new ServiceStatsCallHandler(),
				new ServiceStatsFactory(),
				identifier.getName().substring(identifier.getName().lastIndexOf('.')+1),
				"api",
				"default",
				interfaces
			);
			@SuppressWarnings("unchecked")T ret = (T) proxy.createProxy(); 
			//log.debug("\t created proxy, returning proxy:"+proxy+", ret: "+ret);
			return ret;
			//return newAPI;
		}catch(Throwable t){
			log.debug("THROWABLE creating "+identifier,t);			
			return newAPI;
		}finally{
			log.debug("------ END creation API "+identifier+"\n");
		}
	}
	
	private static void init(){
		apis = new HashMap<Class<? extends API>, API>();
		
		//das ist die stelle zum customizen
		factories = APIConfig.getFactories();
		
	}
	
	public static<T extends API> void addAPIFactory(Class<T> apiClass, APIFactory<T> factoryObject){
		factories.put(apiClass, factoryObject);
	}
	
	static Collection<API> getAPIs(){
		return apis.values(); 
	}
	
	private static<T extends API> T createMockAPI(Class<T> identifier){
		APIMockImpl<T> mock = new APIMockImpl<T>(identifier);
		return mock.createAPIProxy();
	
	}
	

	public static boolean isMaskingEnabled() {
		return maskingEnabled;
	}

	public static void setMaskingEnabled(boolean aMaskingEnabled) {
		maskingEnabled = aMaskingEnabled;
	}

	public static boolean isMockingEnabled() {
		return mockingEnabled;
	}

	public static void setMockingEnabled(boolean aMockingEnabled) {
		mockingEnabled = aMockingEnabled;
	}
	
	/**
	 * Returns true if either mocking or masking is enabled.
	 * @return
	 */
	public static boolean isInTestingMode(){
		return isMockingEnabled() || isMaskingEnabled();
	}

    /**
     * Only for cleaaning  collections in jUnits!!! 
     */
    public static void cleanUp(){
        if(apis!=null)
            apis.clear();
        if(factories!=null)
            factories.clear();
    }


}
