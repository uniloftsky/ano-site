package net.anotheria.anosite.action;

import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anoplass.api.session.APISessionImpl;

public abstract class AbstractAction implements Action{
	protected void sendAttributeToPage(String name, Object attribute){
		((APISessionImpl)APICallContext.getCallContext().getCurrentSession()).addAttributeToActionScope(name, attribute);
	}
}
