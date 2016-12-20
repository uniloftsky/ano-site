package net.anotheria.anosite.util.staticutil;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;

/**
 * Util for configuring jersey client.
 *
 * @author ykalapusha
 */
public final class JerseyClientUtil {

    /**
     * Connection timeout.
     */
    private static final int CONNECT_TIMEOUT = 5000;

    /**
     * Read timeout.
     */
    private static final int READ_TIMEOUT = 90000;

    /**
     * Jersey {@link Client} client instance.
     */
    private static final Client CLIENT;

    /**
     * Configure jersey client
     */
    static {
        CLIENT = Client.create();
        CLIENT.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        CLIENT.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, READ_TIMEOUT);
    }

    /**
     * Default constructor.
     */
    private JerseyClientUtil() { }

    /**
     * Getter for jersey client
     *
     * @return
     *      {@link Client} configured jersey client
     */
    public static Client getClientInstance() {
        return CLIENT;
    }
}

