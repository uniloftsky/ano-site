package net.anotheria.anosite.api.common;

public interface API {
	
	/**
	 * Called when an api instance is first created.
	 */
	public void init();
	
	/**
	 * Called immediately before shutdown.
	 */
	public void deInit();
}
