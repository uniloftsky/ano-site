package net.anotheria.anosite.cms.listener;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.blog.api.exception.BlogAPIException;
import net.anotheria.anosite.gen.asblogdata.data.CommentDocument;
import net.anotheria.anosite.gen.asblogdata.data.PostDocument;
import net.anotheria.anosite.gen.asblogdata.service.ASBlogDataServiceException;
import net.anotheria.anosite.gen.asblogdata.service.IASBlogDataService;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.listener.IServiceListener;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Listener for checking blog posts and comments creations/updates.
 * @author vbezuhlyi
 * @see IASBlogDataService
 */
public class BlogListener implements IServiceListener {

    /**
     * Parameter name to indicate whether the listener's method was invoked by listener itself
     */
    private static final String CALLED_BY_LISTENER = "calledByListener";

    private static final Logger log;

    private static IASBlogDataService blogDataService;


    static {
        log = Logger.getLogger(BlogListener.class);

        try {
            blogDataService = MetaFactory.get(IASBlogDataService.class);
        } catch (MetaFactoryException e) {
            log.error("MetaFactory for BlogListener failed", e);
            throw new RuntimeException("MetaFactory for BlogListener failed", e);
        }
    }


    @Override
    public void documentUpdated(DataObject old, DataObject updated) {
        if (old instanceof PostDocument && updated instanceof PostDocument) {
            PostDocument oldPostDocument = (PostDocument) old;
            PostDocument updatedPostDocument = (PostDocument) updated;
            if (oldPostDocument.getBoolean(CALLED_BY_LISTENER)) {
                updatedPostDocument.setBoolean(CALLED_BY_LISTENER, false);
                return;
            }
            updatedPostDocument.setUpdated(new Date().getTime());
            updatedPostDocument.setBoolean(CALLED_BY_LISTENER, true);
            try {
                blogDataService.updatePost(updatedPostDocument);
            } catch (ASBlogDataServiceException e) {
                log.error("updatePost(" + updatedPostDocument.toString() + ")", e);
                throw new RuntimeException("BlogListener failed", e);
            }
        }
    }

    @Override
    public void documentDeleted(DataObject doc) {
    }

    @Override
    public void documentCreated(DataObject dataObject) {
        if (dataObject instanceof PostDocument) {
            PostDocument postDocument = (PostDocument) dataObject;
            postDocument.setCreated(new Date().getTime());
            postDocument.setBlogger(postDocument.getAuthor());
            postDocument.setBoolean(CALLED_BY_LISTENER, true);
            try {
                blogDataService.updatePost(postDocument);
            } catch (ASBlogDataServiceException e) {
                log.error("updatePost(" + postDocument.toString() + ")", e);
                throw new RuntimeException("BlogListener failed", e);
            }
        }
        if (dataObject instanceof CommentDocument) {
            CommentDocument commentDocument = (CommentDocument) dataObject;
            commentDocument.setCreated(new Date().getTime());
            commentDocument.setCommentator(commentDocument.getAuthor());
            try {
                blogDataService.updateComment(commentDocument);
            } catch (ASBlogDataServiceException e) {
                log.error("updateComment(" + commentDocument.toString() + ")", e);
                throw new RuntimeException("BlogListener failed", e);
            }
        }
    }

    @Override
    public void documentImported(DataObject doc) {
    }

    @Override
    public void persistenceChanged() {
    }

}
