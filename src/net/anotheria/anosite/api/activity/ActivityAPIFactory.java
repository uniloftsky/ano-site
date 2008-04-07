package net.anotheria.anosite.api.activity;

import net.anotheria.anosite.api.common.APIFactory;

public class ActivityAPIFactory implements APIFactory<ActivityAPI>{

	public ActivityAPI createAPI() {
		return new ActivityAPIImpl();
	}

}
 