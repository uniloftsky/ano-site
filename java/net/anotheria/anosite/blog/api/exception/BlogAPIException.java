package net.anotheria.anosite.blog.api.exception;

import net.anotheria.anoplass.api.APIException;

/**
 * BlogAPI exception.
 * @author vbezuhlyi
 */
public class BlogAPIException extends APIException {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = 954719953542814048L;

    /**
     * Constructor.
     *
     * @param message exception message
     */
    public BlogAPIException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message exception message
     * @param cause exception cause
     */
    public BlogAPIException(String message, Exception cause) {
        super(message, cause);
    }
}
