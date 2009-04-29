package net.anotheria.anosite.cms.helper;

import java.util.HashMap;

import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;
import net.anotheria.asg.util.helper.cmsview.CMSViewHelper;

public abstract class BoxHelper implements CMSViewHelper{
	private static IASFederatedDataService federatedDataService = ASFederatedDataServiceFactory.createASFederatedDataService();
	
	private HashMap<String, BoxSubHelper> subHelpers = new HashMap<String, BoxSubHelper>();
	
	protected static IASFederatedDataService getFederatedDataService(){
		return federatedDataService;
	}

	public void addSubHelper(String name, BoxSubHelper subHelper){
		subHelpers.put(name, subHelper);
	}
	
	protected BoxSubHelper getSubHelper(String name){
		return subHelpers.get(name);
	}

}
