package net.anotheria.anosite.api.common;

/**
 * Base interface for all api-class interfaces.
 * @author another
 *
 */
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
