package net.anotheria.anosite.api.activity;

import net.anotheria.anosite.api.common.API;
import net.anotheria.anosite.api.common.APIFactory;

public class ActivityAPIFactory implements APIFactory{

	public API createAPI() {
		return new ActivityAPIImpl();
	}

}
 