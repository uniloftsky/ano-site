package net.anotheria.anosite.guard;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.exception.ASGRuntimeException;

/**
 * A conditional guard guards any object and returns true only if the predefined condition is true in the objects context.
 * @author another
 *
 */
public interface ConditionalGuard {
	/**
	 * Returns true if the condition is fullfilled.
	 * @param object
	 * @param req
	 * @return
	 * @throws ASGRuntimeException
	 */
	boolean isConditionFullfilled(DataObject object, HttpServletRequest req) throws ASGRuntimeException;
}
