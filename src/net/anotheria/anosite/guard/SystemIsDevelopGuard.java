package net.anotheria.anosite.guard;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.anosite.config.Config;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.exception.ASGRuntimeException;

public class SystemIsDevelopGuard implements ConditionalGuard{

	@Override
	public boolean isConditionFullfilled(DataObject object, HttpServletRequest req) throws ASGRuntimeException {
		return Config.getInstance().isSystemDevelop();
	}
	
}
