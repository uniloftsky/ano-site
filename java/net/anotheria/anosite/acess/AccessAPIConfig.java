package net.anotheria.anosite.acess;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.DontConfigure;

@ConfigureMe(name = "ano-site-access-api-config")
public final class AccessAPIConfig implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	@DontConfigure
	private static final long serialVersionUID = 6091949471599059300L;

	/**
	 * {@link Logger} instance.
	 */
	@DontConfigure
	private static final Logger LOGGER = Logger.getLogger(AccessAPIConfig.class.getName());

	/**
	 * {@link AccessAPIConfig} instance.
	 */
	@DontConfigure
	private static final AccessAPIConfig INSTANCE = new AccessAPIConfig();

	@Configure
	private boolean enabled;

	private AccessAPIConfig() {
		try {
			ConfigurationManager.INSTANCE.configure(this);
			LOGGER.info("AccessAPIConfig() Configured. Configuration[" + this.toString() + "].");
		} catch (Exception e) {
			this.enabled = false;
			LOGGER.warn("AccessAPIConfig() Configuration failed. Configuring with defaults[" + this.toString() + "].");
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static AccessAPIConfig getInstance() {
		return INSTANCE;
	}

	@Override
	public String toString() {
		return "AccessAPIConfig [enabled=" + enabled + "]";
	}

}
