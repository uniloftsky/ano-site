package net.anotheria.anosite.action;

import net.anotheria.anosite.api.common.APICallContext;
import net.anotheria.anosite.api.session.APISessionImpl;

public abstract class AbstractAction implements Action{
	protected void sendAttributeToPage(String name, Object attribute){
		((APISessionImpl)APICallContext.getCallContext().getCurrentSession()).addAttributeToActionScope(name, attribute);
	}
}
