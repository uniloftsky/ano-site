package net.anotheria.anosite.blog.api.exception;

/**
 * TagNotFoundException is thrown when specified tag not found by service.
 * @author vbezuhlyi
 */
public class TagNotFoundException extends BlogAPIException {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = 6939245097953918847L;

    /**
     * Constructor.
     *
     * @param data additional info (e.g. tag ID)
     */
    public TagNotFoundException(String data) {
        super("Tag not found: " + data);
    }


}
