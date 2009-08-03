package net.anotheria.anosite.guard;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.anosite.config.Config;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.exception.ASGRuntimeException;

/**
 * Only fulfilled if the system is in production mode (conigured as in production).
 * @author lrosenberg
 *
 */
public class SystemIsProductionGuard implements ConditionalGuard{

	@Override
	public boolean isConditionFullfilled(DataObject object, HttpServletRequest req) throws ASGRuntimeException {
		return Config.getInstance().isSystemProduction();
	}
	
}
