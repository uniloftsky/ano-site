package net.anotheria.anosite.api.common;

/**
 * Base interface for all api-class interfaces.
 * @author lrosenberg
 */
public interface API {
	
	/**
	 * Called when an api instance is first created.
	 */
	void init();
	
	/**
	 * Called immediately before shutdown.
	 */
	void deInit();
}
