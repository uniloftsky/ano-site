package net.anotheria.anosite.blog.api;

import net.anotheria.anoplass.api.APIFactory;

/**
 * BlogAPI factory.
 * @author vbezuhlyi
 */
public class BlogAPIFactory implements APIFactory<BlogAPI> {

    @Override
    public BlogAPI createAPI() {
        return new BaseBlogAPIImpl();
    }
}
