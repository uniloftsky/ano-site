package net.anotheria.anosite.guard;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.asg.data.DataObject;

/**
 * A guard which only returns true if the desired attribute is in session.
 * @author another
 *
 */
public abstract class SessionFlagPresentGuard implements ConditionalGuard{

	public boolean isConditionFullfilled(DataObject object, HttpServletRequest req) {
		return (req.getSession().getAttribute(getFlagName())!=null) == getDesiredResult();
	}
	
	protected boolean getDesiredResult(){
		return true;
	}
	
	protected abstract String getFlagName();
	
}
