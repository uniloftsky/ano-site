package net.anotheria.anosite.blog.api;

import java.io.Serializable;

/**
 * Comment API object.
 * @author vbezuhlyi
 */
public class CommentAO implements Serializable {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -9120649988726722788L;

    private String id;
    private long created;
    private String commentator;
    private String content;


    public CommentAO(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getCommentator() {
        return commentator;
    }

    public void setCommentator(String commentator) {
        this.commentator = commentator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
