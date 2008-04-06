/* ------------------------------------------------------------------------- *
$Source$
$Author$
$Date$
$Revision$


Copyright 2004-2005 by FriendScout24 GmbH, Munich, Germany.
All rights reserved.

This software is the confidential and proprietary information
of FriendScout24 GmbH. ("Confidential Information").  You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with FriendScout24 GmbH.
See www.friendscout24.de for details.
** ------------------------------------------------------------------------- */
package net.anotheria.anosite.api.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anotheria.anosite.api.content.ContentAPI;
import net.anotheria.anosite.api.content.ContentAPIFactory;
import net.java.dev.moskito.core.dynamic.MoskitoInvokationProxy;
import net.java.dev.moskito.core.predefined.ServiceStatsCallHandler;
import net.java.dev.moskito.core.predefined.ServiceStatsCallHandlerWithCallSysout;
import net.java.dev.moskito.core.predefined.ServiceStatsFactory;

import org.apache.log4j.Logger;

public class APIFinder {
	
	private static Map<Class<? extends API>, API> apis;
	private static Map<Class<? extends API>, APIFactory> factories;
	
	private static Logger log;
	
	static{
		log = Logger.getLogger(APIFinder.class);
		//BasicConfigurator.configure();
		init();
	}
	
	@SuppressWarnings("unchecked")
	public static API findAPI(String identifier){
		try{
			return findAPI((Class<? extends API>)Class.forName(identifier));
		}catch(ClassNotFoundException e){
			throw new RuntimeException("Class not found: "+identifier+", exception: "+e.getMessage());
		}
	}
	
	public static API findAPI(Class<? extends API> identifier){
		log.debug("find api: "+identifier);
		API loaded = apis.get(identifier);
		log.debug(" loaded: "+loaded);
		if (loaded != null)
			return loaded;
		synchronized (apis) {
			API doubleChecked = apis.get(identifier);
			log.debug("\t doubleChecked: "+doubleChecked);
			if (doubleChecked!=null)
				return doubleChecked;
			API created = createAPI(identifier);
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
	
	private synchronized static API createAPI(Class<? extends API> identifier){
		APIFactory factory = factories.get(identifier);
		if (factory==null)
			throw new NoSuchAPIException(identifier.getName());
		
		log.debug("------ START creation API "+identifier);
		
		API newAPI = factory.createAPI();
		
		log.debug("\tcreated new instance: "+newAPI);
		
		if (APIConfig.isInTestMode()){
			if (newAPI instanceof TestModeAware)
				((TestModeAware)newAPI).setTestMode(true);
		}
		
		//newAPI.init();
		
		//log.debug("\tinited "+newAPI);
		
		try{
			Class<? extends API>[] interfaces;
			List<Class<? extends API>> aliases = APIConfig.getAliases(identifier);
			if (aliases!=null && aliases.size()>0){
				interfaces = new Class[aliases.size()+2];
				int i = 2;
				for (Class<? extends API> a : aliases)
					interfaces[i++] = a;
			}else{
				interfaces = new Class[2];
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
			API ret = (API) proxy.createProxy(); 
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
		
		factories.put(ContentAPI.class, new ContentAPIFactory());
	}
	
	public static void addAPIFactory(Class<? extends API> apiClass, APIFactory factoryObject){
		APIConfig.getFactories().put(apiClass, factoryObject);
	}
	
	static Collection<API> getAPIs(){
		return apis.values(); 
	}
}
