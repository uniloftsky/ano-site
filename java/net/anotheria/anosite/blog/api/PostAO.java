package net.anotheria.anosite.blog.api;

import java.io.Serializable;
import java.util.List;

/**
 * Post API object.
 * @author vbezuhyli
 */
public class PostAO implements Serializable {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -4749027732756833368L;

    private String id;
    private long created;
    private long updated;
    private String name;
    private String blogger;
    private String content;
    private List<CommentAO> comments;
    private List<TagAO> tags;


    public PostAO(String id) {
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

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlogger() {
        return blogger;
    }

    public void setBlogger(String blogger) {
        this.blogger = blogger;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CommentAO> getComments() {
        return comments;
    }

    public void setComments(List<CommentAO> comments) {
        this.comments = comments;
    }

    public List<TagAO> getTags() {
        return tags;
    }

    public void setTags(List<TagAO> tags) {
        this.tags = tags;
    }

}
