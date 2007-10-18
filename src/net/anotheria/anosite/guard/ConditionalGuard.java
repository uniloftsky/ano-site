package net.anotheria.anosite.guard;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.asg.data.DataObject;

public interface ConditionalGuard {
	public boolean isConditionFullfilled(DataObject object, HttpServletRequest req);
}
