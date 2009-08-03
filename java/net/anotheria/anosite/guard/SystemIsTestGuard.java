package net.anotheria.anosite.guard;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.anosite.config.Config;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.exception.ASGRuntimeException;

/**
 * Only fulfilled if the system is in test mode (conigured as in test).
 * @author lrosenberg
 *
 */
public class SystemIsTestGuard implements ConditionalGuard{

	@Override
	public boolean isConditionFullfilled(DataObject object, HttpServletRequest req) throws ASGRuntimeException {
		return Config.getInstance().isSystemTest();
	}
	
}
