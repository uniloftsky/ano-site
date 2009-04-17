package net.anotheria.anosite.api.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anotheria.anosite.api.activity.ActivityAPI;
import net.anotheria.anosite.api.activity.ActivityAPIFactory;
import net.anotheria.anosite.api.content.ContentAPI;
import net.anotheria.anosite.api.content.ContentAPIFactory;
import net.anotheria.anosite.api.generic.login.LoginAPI;
import net.anotheria.anosite.api.generic.login.LoginAPIFactory;
import net.anotheria.anosite.api.generic.observation.ObservationAPI;
import net.anotheria.anosite.api.generic.observation.ObservationAPIFactory;

import org.configureme.ConfigurationManager;

public class APIConfig {

	public static final int SERVICE_POLICY_SINGLE_INSTANCE = 1;
	public static final int SERVICE_POLICY_MULTI_INSTANCE = 2;

	public static final int SERVICE_POLICY_DEFAULT = SERVICE_POLICY_MULTI_INSTANCE;

	private static int servicePolicy;
	private static Map<Class<? extends API>, List<Class<? extends API>>> aliasMap;

	private static APIConfigurable configurable;

	static {
		servicePolicy = SERVICE_POLICY_DEFAULT;

		aliasMap = new HashMap<Class<? extends API>, List<Class<? extends API>>>();
		// addAlias(ITargetingAPI.class, ITargetingTestingAPI.class);

		configurable = new APIConfigurable();
		ConfigurationManager.INSTANCE.configure(configurable);

	}

	@SuppressWarnings("unused")
	private static void addAlias(Class<? extends API> interfaceClass,
			Class<? extends API> aliasClass) {
		List<Class<? extends API>> aliases = aliasMap.get(interfaceClass);
		if (aliases == null) {
			aliases = new ArrayList<Class<? extends API>>();
			aliasMap.put(interfaceClass, aliases);
		}
		aliases.add(aliasClass);
	}

	public static List<Class<? extends API>> getAliases(
			Class<? extends API> source) {
		return aliasMap.get(source);
	}

	public static final Map<Class<? extends API>, APIFactory<? extends API>> getFactories() {

		Map<Class<? extends API>, APIFactory<? extends API>> ret = new HashMap<Class<? extends API>, APIFactory<? extends API>>();

		ret.put(ContentAPI.class, new ContentAPIFactory());
		ret.put(ActivityAPI.class, new ActivityAPIFactory());
		ret.put(LoginAPI.class, new LoginAPIFactory());
		ret.put(ObservationAPI.class, new ObservationAPIFactory());

		// ret.put(ISystemMailAPI.class, new MailAPIFactory());
		return ret;
	}

	public static final Map<Class<? extends API>, List<Class<? extends API>>> getInterfaceAliases() {
		return aliasMap;
	}

	public static int getServicePolicy() {
		return servicePolicy;
	}

	public static boolean verboseMethodCalls() {
		return configurable.isVerboseMethodCalls();
	}

	public static boolean isInTestMode() {
		return configurable.isTestMode();
	}


}