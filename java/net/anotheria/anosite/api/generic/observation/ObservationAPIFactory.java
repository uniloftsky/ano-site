package net.anotheria.anosite.api.generic.observation;

import net.anotheria.anosite.api.common.APIFactory;

public class ObservationAPIFactory implements APIFactory<ObservationAPI>{
	public ObservationAPI createAPI() {
		return new ObservationAPIImpl();
	}
}
