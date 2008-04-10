package net.anotheria.anosite.api.activity;

import net.anotheria.anosite.api.common.AbstractAPIImpl;

public class ActivityAPIImpl extends AbstractAPIImpl implements ActivityAPI{

	//this is just a test impl of a test api
	
	public void notifyMyActivity(String url) {
//		String lastUrl = (String)getAttributeFromSession("LAST_URL");
//		if (lastUrl!=null)
//			System.out.print(lastUrl +" -> ");
//		System.out.println(url+ "("+getCallContext()+")");
		setAttributeInSession("LAST_URL", url);
	}
	
}
