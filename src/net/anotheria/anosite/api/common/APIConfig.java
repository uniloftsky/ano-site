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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anotheria.util.StringUtils;

public class APIConfig {
	
	public static final int SERVICE_POLICY_SINGLE_INSTANCE = 1;
	public static final int SERVICE_POLICY_MULTI_INSTANCE = 2;
	
	public static final int SERVICE_POLICY_DEFAULT = SERVICE_POLICY_MULTI_INSTANCE;
	
	private static int servicePolicy;
	private static Map<Class<? extends API>,List<Class<? extends API>>> aliasMap;
	
	private static APIConfigurable configurable;
	
	static{
		servicePolicy = SERVICE_POLICY_DEFAULT;
		
		aliasMap = new HashMap<Class<? extends API>, List<Class<? extends API>>>();
		//addAlias(ITargetingAPI.class, ITargetingTestingAPI.class);
		
		configurable = APIConfigurable.getInstance();
	
	}
	
	private static void addAlias(Class<? extends API> interfaceClass, Class<? extends API> aliasClass){
		List<Class<? extends API>> aliases = aliasMap.get(interfaceClass);
		if (aliases==null){
			aliases = new ArrayList<Class<? extends API>>();
			aliasMap.put(interfaceClass, aliases);
		}
		aliases.add(aliasClass);
	}
	
	public static List<Class<? extends API>> getAliases(Class<? extends API> source){
		return aliasMap.get(source);
	}
	
	public static final Map<Class<? extends API>,APIFactory> getFactories(){
		
		Map<Class<? extends API>,APIFactory> ret = new HashMap<Class<? extends API>,APIFactory>();
		
		//ret.put(ISystemMailAPI.class, new MailAPIFactory());
		return ret;
	}
	
	public static final Map<Class<? extends API>, List<Class<? extends API>>> getInterfaceAliases(){
		return aliasMap;
	}
	
	public static int getServicePolicy(){
		return servicePolicy;
	}
	
	public static boolean verboseMethodCalls(){
		return configurable.verboseMethodCalls();
	}
	
	public static boolean isInTestMode(){
		return configurable.isTestMode();
	}

	public static String getProperty(String key){
		return getProperty(key, "");
	}
	
	public static String getProperty(String key, String defaultValue){
		return configurable.getProperty(key, defaultValue);
	}

	public static int getPropertyAsInt(String key){
		return getPropertyAsInt(key, 0);
	}

	public static int getPropertyAsInt(String key, int defaultValue){
		try{
			return Integer.parseInt(configurable.getProperty(key, defaultValue+""));
		}catch(NumberFormatException e){
			return defaultValue;
		}
	}

	public static long getPropertyAsLong(String key){
		return getPropertyAsLong(key, 0L);
	}

	public static long getPropertyAsLong(String key, long defaultValue){
		String val = configurable.getProperty(key, defaultValue+"");
		try{
			return Long.parseLong(val);
		}catch(NumberFormatException e){
			try{
				String tokens[] = StringUtils.tokenize(val, '*');
				long ret = Long.parseLong(tokens[0]);
				if(tokens.length>1){
					for(int i=1; i<tokens.length; i++){
						ret *= Long.parseLong(tokens[i]);
					}
				}
				return ret;
			}catch(NumberFormatException e1){
				return defaultValue;
			}
		}
	}

	
}