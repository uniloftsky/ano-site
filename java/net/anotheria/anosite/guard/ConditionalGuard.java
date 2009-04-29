package net.anotheria.anosite.guard;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.exception.ASGRuntimeException;

public interface ConditionalGuard {
	public boolean isConditionFullfilled(DataObject object, HttpServletRequest req) throws ASGRuntimeException;
}
