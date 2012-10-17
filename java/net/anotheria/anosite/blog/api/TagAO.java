package net.anotheria.anosite.blog.api;

import java.io.Serializable;

/**
 * Tag API object.
 * @author vbezuhlyi
 */
public class TagAO implements Serializable {

    /**
     * Basic serial version UID.
     */
    private static final long serialVersionUID = -2919197320203681650L;

    private String id;
    private String name;


    public TagAO(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
