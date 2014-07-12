package net.anotheria.anosite.api.feature;

import net.anotheria.anoplass.api.API;
import net.anotheria.anoplass.api.APIException;

/**
 * FeatureAPI for working with features.
 *
 * @author lrosenberg
 * @since 12.07.14 20:40
 */
public interface FeatureAPI extends API{
	boolean isFeatureActive(String name) throws APIException;
}
