package net.anotheria.anosite.api.content;

import net.anotheria.anosite.api.common.API;
import net.anotheria.anosite.api.common.APIFactory;

public class ContentAPIFactory implements APIFactory{

	public API createAPI() {
		return new ContentAPIImpl();
	}

}
