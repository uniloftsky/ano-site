package net.anotheria.anosite.api.generic.observation;

import net.anotheria.anosite.api.common.APIFactory;

/**
 * The factory for the ObservationAPI implementation.
 * @author lrosenberg
 *
 */
public class ObservationAPIFactory implements APIFactory<ObservationAPI>{
	@Override public ObservationAPI createAPI() {
		return new ObservationAPIImpl();
	}
}
