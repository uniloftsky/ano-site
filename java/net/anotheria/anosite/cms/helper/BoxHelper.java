package net.anotheria.anosite.cms.helper;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;
import net.anotheria.asg.util.helper.cmsview.CMSViewHelper;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * BoxHelper abstract class.
 */
public abstract class BoxHelper implements CMSViewHelper{
	/**
	 * IASFederatedDataService ASG.
	 */
	private static IASFederatedDataService federatedDataService;
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(BoxHelper.class);
	/**
	 * Init.
	 */
	static {
		try {
			federatedDataService= MetaFactory.get(IASFederatedDataService.class);
		} catch (MetaFactoryException e) {
			log.fatal("IASFederatedDataService ASG init failure",e);
		}
	}

	/**
	 * SubHelpers map.
	 */
	private HashMap<String, BoxSubHelper> subHelpers = new HashMap<String, BoxSubHelper>();

	/**
	 * Returns IASFederatedDataService ASG service.
	 * @return ASG service
	 */
	protected static IASFederatedDataService getFederatedDataService(){
		return federatedDataService;
	}

	/**
	 * Allow add BoxSubHelper.
	 * @param name name of subHelper
	 * @param subHelper subHelper to add
	 */
	public void addSubHelper(String name, BoxSubHelper subHelper){
		subHelpers.put(name, subHelper);
	}

	/**
	 * Returns subHelper by given name.
 	 * @param name actually subHelper name
	 * @return BoxSubHelper
	 */
	protected BoxSubHelper getSubHelper(String name){
		return subHelpers.get(name);
	}

}
