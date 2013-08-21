package net.anotheria.anosite.blog.presentation.handler;

import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.anosite.blog.api.BlogAO;
import net.anotheria.anosite.blog.api.BlogAPI;
import net.anotheria.anosite.blog.api.exception.BlogAPIException;
import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.handler.AbstractBoxHandler;
import net.anotheria.anosite.handler.BoxHandlerResponse;
import net.anotheria.anosite.handler.ResponseContinue;
import net.anotheria.anosite.handler.exception.BoxProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler for BlogListView renderer.
 * @author vbezuhlyi
 */
public class BlogListViewHandler extends AbstractBoxHandler {

    /**
     * {@code Log4j} logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogListViewHandler.class);

    /**
     * {@link BlogAPI} instance.
     */
    protected BlogAPI blogAPI;

    /**
     * Attribute name for object encapsulating all blog data
     */
    protected static final String ATTR_NAME_BLOG_BEAN = "blog";

    /**
     * Constructor.
     */
    public BlogListViewHandler() {
        blogAPI = APIFinder.findAPI(BlogAPI.class);
    }


    @Override
    public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean) throws BoxProcessException {
        try {
            BlogAO blogAO = blogAPI.getBlog();
            req.setAttribute(ATTR_NAME_BLOG_BEAN, blogAO);
        } catch (BlogAPIException e) {
            LOGGER.error("BlogAPI failed", e);
            throw new BoxProcessException("process failed", e);
        }

        return ResponseContinue.INSTANCE;
    }

}
