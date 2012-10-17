package net.anotheria.anosite.blog.api;

import java.io.Serializable;
import java.util.List;

/**
 * BLog API object.
 * @author vbezuhlyi
 */
public class BlogAO implements Serializable {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -704571748142347691L;

    private List<PostAO> posts;


    public BlogAO(List<PostAO> posts) {
        this.posts = posts;
    }


    public List<PostAO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostAO> postBeans) {
        this.posts = postBeans;
    }
}
