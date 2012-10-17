package net.anotheria.anosite.blog.api.exception;

/**
 * PostNotFoundException is thrown when specified post not found by service.
 * @author vbezuhlyi
 */
public class PostNotFoundException extends BlogAPIException {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -4462252838558835537L;

    /**
     * Constructor.
     *
     * @param data additional info (e.g. post ID)
     */
    public PostNotFoundException(String data) {
        super("Post not found: " + data);
    }

}
