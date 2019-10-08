package net.anotheria.anosite.util.staticutil;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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
     * {@link TrustManager} config.
     */
    private static final TrustManager[] certs = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            }
    };

    /**
     * Configure jersey client
     */
    static {

        HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
        ClientConfig config = new DefaultClientConfig();
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to get SSL context. " + e.getMessage());
        }
        try {
            ctx.init(null, certs, null);
        } catch (KeyManagementException e) {
            throw new RuntimeException("Unable to init SSL context. " + e.getMessage());
        }

        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, ctx));
        config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        config.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, READ_TIMEOUT);

        CLIENT = Client.create(config);
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

