package net.anotheria.anosite.api.feature;

import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.api.configuration.SystemConfigurationAPI;
import net.anotheria.anosite.gen.asfeature.data.Feature;
import net.anotheria.anosite.gen.asfeature.service.ASFeatureServiceException;
import net.anotheria.anosite.gen.asfeature.service.IASFeatureService;
import net.anotheria.anosite.guard.ConditionalGuard;
import net.anotheria.anosite.guard.GuardFactory;

import java.util.List;

/**
 * {@link FeatureAPI} implementation.
 *
 * @author lrosenberg
 * @since 12.07.14 20:40
 */
public class FeatureAPIImpl extends AbstractAPIImpl implements FeatureAPI{
	/**
	 * {@link IASFeatureService} instance.
	 */
	private IASFeatureService featureService;
	/**
	 * API: {@link SystemConfigurationAPI}.
	 */
	private SystemConfigurationAPI systemConfigurationAPI;

	@Override
	public boolean isFeatureActive(String name) throws APIException {
		try {
			List<Feature> featuresList = featureService.getFeaturesByProperty(Feature.PROP_NAME, name);
			if (featuresList == null || featuresList.size() == 0)
				throw new APIException("Feature "+name+" not found.");

			if (featuresList.size() > 1)
				throw new APIException("Found more than 1 feature with name "+name);

			Feature f = featuresList.get(0);
			if (!f.getEnabled())
				return false;

			/* check if this feature must be worked on production */
			if (systemConfigurationAPI.getCurrentSystem().startsWith("CMS") && !f.getActiveInProduction())
				return false;

			/* if feature is marked as 'obsolete' and it is being used */
			if (f.getObsolete()) {
				log.warn("Feature " + f.getName() + " is being used, though it is marked as 'OBSOLETE'");
			}

			List<String> gIds = f.getGuards();
			if (gIds==null || gIds.size()==0)
				return true;
			for (String gid : gIds) {
				ConditionalGuard g = null;
				try {
					g = GuardFactory.getConditionalGuard(gid);
					if (!g.isConditionFullfilled(null, null)) {
						return false;
					}

				} catch (Exception e) {
					log.warn("Caught error in guard processing( Feature: "+name+", guard: " + g + ", gid: " + gid + ")", e);
				}
			}

			return true;
		}catch(ASFeatureServiceException e){
			throw new APIException("Feature "+name+" not found, due ", e);
		}
	}

	@Override
	public void init() throws APIInitException {
		try{
			featureService = MetaFactory.get(IASFeatureService.class);
		}catch(MetaFactoryException e){
			throw new APIInitException("Feature service not found.");
		}
		systemConfigurationAPI = APIFinder.findAPI(SystemConfigurationAPI.class);
	}

	@Override
	public void deInit() {
		featureService = null;
		systemConfigurationAPI = null;
	}
}
