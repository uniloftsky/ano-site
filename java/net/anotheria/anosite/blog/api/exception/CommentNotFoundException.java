package net.anotheria.anosite.blog.api.exception;

/**
 * CommentNotFoundException is thrown when specified comment not found by service.
 * @author vbezuhlyi
 */
public class CommentNotFoundException extends BlogAPIException {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = 8615612401785445486L;

    /**
     * Constructor.
     *
     * @param data additional info (e.g. comment ID)
     */
    public CommentNotFoundException(String data) {
        super("Comment not found: " + data);
    }
}
