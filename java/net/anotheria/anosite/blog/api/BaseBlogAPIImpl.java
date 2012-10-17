package net.anotheria.anosite.blog.api;

import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoplass.api.AbstractAPIImpl;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.blog.api.exception.BlogAPIException;
import net.anotheria.anosite.blog.api.exception.CommentNotFoundException;
import net.anotheria.anosite.blog.api.exception.PostNotFoundException;
import net.anotheria.anosite.blog.api.exception.TagNotFoundException;
import net.anotheria.anosite.gen.asblogdata.data.Comment;
import net.anotheria.anosite.gen.asblogdata.data.Post;
import net.anotheria.anosite.gen.asblogdata.data.Tag;
import net.anotheria.anosite.gen.asblogdata.service.*;
import net.anotheria.util.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Basic implementation of BlogAPI.
 * @author vbezuhlyi
 */
public class BaseBlogAPIImpl extends AbstractAPIImpl implements BlogAPI {
    /**
     * {@code Log4j} logger instance.
     */
    private static final Logger LOG = Logger.getLogger(BaseBlogAPIImpl.class);

    /**
     * Service: {@link net.anotheria.anosite.gen.asblogdata.service.IASBlogDataService}
     */
    private IASBlogDataService blogDataService;

    /**
     * Creates {@link BlogAO} based on {@link PostAO} list of all the posts, tags and comments to blog.
     *
     * @return {@link BlogAO}
     */
    private BlogAO toBlogAO() throws BlogAPIException {
        return new BlogAO(getPosts());
    }


    /**
     * Creates {@link BlogAO} based on {@link PostAO} list of all the posts, tags and comments to blog.
     * Sorting is by specific parameters.
     *
     * @param postComparator comparator for sorting posts by specific parameter
     * @param commentComparator {@link CommentComparators} comparator for sorting comments by specific parameter
     * @return {@link BlogAO}
     */
    private BlogAO toBlogAO(Comparator<PostAO> postComparator, Comparator<CommentAO> commentComparator) throws BlogAPIException {
        return new BlogAO(getPosts(postComparator, commentComparator));
    }


    /**
     * Creates {@link PostAO} based on {@link Post}.
     *
     * @param post {@link Post}
     * @return {@link PostAO}
     * @throws BlogAPIException on exception inside getTags()/getComments()
     */
    private PostAO toPostAO(Post post) throws BlogAPIException {
        PostAO result = new PostAO(post.getId());
        result.setCreated(post.getCreated());
        result.setUpdated(post.getUpdated());
        result.setBlogger(post.getBlogger());
        result.setName(post.getName());
        result.setContent(post.getContent());
        result.setTags(getTags(post.getId()));
        result.setComments(getComments(post.getId()));

        return result;
    }


    /**
     * Creates {@link TagAO} based on {@link Tag}.
     *
     * @param tag {@link Tag}
     * @return {@link TagAO}
     */
    private TagAO toTagAO(Tag tag) {
        TagAO result = new TagAO(tag.getId());
        result.setName(tag.getName());

        return result;
    }


    /**
     * Creates {@link CommentAO} based on {@link Comment}.
     *
     * @param comment {@link Comment}
     * @return {@link CommentAO}
     */
    private CommentAO toCommentAO(Comment comment) {
        CommentAO result = new CommentAO(comment.getId());
        result.setCreated(comment.getCreated());
        result.setCommentator(comment.getCommentator());
        result.setContent(comment.getContent());

        return result;
    }


    @Override
    public void init() throws APIInitException {
        super.init();
        try {
            /* Initializing service */
            blogDataService = MetaFactory.get(IASBlogDataService.class);
        } catch (MetaFactoryException e) {
            LOG.error("MetaFactory for BlogAPI init failed", e);
            throw new APIInitException("BlogAPI init failed", e);
        }
    }


    @Override
    public BlogAO getBlog() throws BlogAPIException {
        return toBlogAO();
    }


    @Override
    public BlogAO getBlog(Comparator<PostAO> postComparator, Comparator<CommentAO> commentComparator) throws BlogAPIException {
        return toBlogAO(postComparator, commentComparator);
    }


    @Override
    public PostAO getPost(String postId) throws BlogAPIException {
        if (StringUtils.isEmpty(postId)) {
            throw new IllegalArgumentException("postId is illegal");
        }
        try {
            return toPostAO(blogDataService.getPost(postId));
        } catch (PostNotFoundInASBlogDataServiceException e) {
            throw new PostNotFoundException(postId);
        } catch (ASBlogDataServiceException e) {
            log.error("getPost(" + postId + ")", e);
            throw new BlogAPIException("Blog backend failure", e);
        }
    }


    @Override
    public PostAO getPost(String postId, Comparator<CommentAO> commentComparator) throws BlogAPIException {
        PostAO result = getPost(postId);
        Collections.sort(result.getComments(), commentComparator);
        return result;
    }


    @Override
    public List<PostAO> getPosts() throws BlogAPIException {
        try {
            List<PostAO> result = new ArrayList<PostAO>();
            List<Post> posts = blogDataService.getPosts();

            for(Post post : posts) {
                result.add(toPostAO(post));
            }

            return result;
        } catch (PostNotFoundInASBlogDataServiceException e) {
            throw new PostNotFoundException(e.toString());
        } catch (ASBlogDataServiceException e) {
            log.error("getPosts()", e);
            throw new BlogAPIException("Blog backend failure", e);
        }
    }


    @Override
    public List<PostAO> getPosts(Comparator<PostAO> postComparator, Comparator<CommentAO> commentComparator) throws BlogAPIException {
        List<PostAO> result = getPosts();
        Collections.sort(result, postComparator);
        for (PostAO postAo : result) {
            Collections.sort(postAo.getComments(), commentComparator);
        }
        return result;
    }


    @Override
    public CommentAO getComment(String commentId) throws BlogAPIException {
        if (StringUtils.isEmpty(commentId)) {
            throw new IllegalArgumentException("commentId is illegal");
        }
        try {
            return toCommentAO(blogDataService.getComment(commentId));
        } catch (CommentNotFoundInASBlogDataServiceException e) {
            throw new CommentNotFoundException(commentId);
        } catch (ASBlogDataServiceException e) {
            log.error("getComment(" + commentId + ")", e);
            throw new BlogAPIException("Blog backend failure", e);
        }
    }


    @Override
    public List<CommentAO> getComments(String postId) throws BlogAPIException {
        if (StringUtils.isEmpty(postId)) {
            throw new IllegalArgumentException("postId is illegal");
        }
        try {
            List<CommentAO> result = new ArrayList<CommentAO>();
            Post post = blogDataService.getPost(postId);

            for(String commentId : post.getComments()) {
                result.add(getComment(commentId));
            }

            return result;
        } catch (PostNotFoundInASBlogDataServiceException e) {
            throw new PostNotFoundException(postId);
        } catch (ASBlogDataServiceException e) {
            log.error("getComments(" + postId + ")", e);
            throw new BlogAPIException("Blog backend failure", e);
        }
    }


    @Override
    public List<CommentAO> getComments(String postId, Comparator<CommentAO> commentComparator) throws BlogAPIException {
        List<CommentAO> result = getComments(postId);
        Collections.sort(result, commentComparator);
        return result;
    }


    @Override
    public TagAO getTag(String tagId) throws BlogAPIException {
        if (StringUtils.isEmpty(tagId)) {
            throw new IllegalArgumentException("tagId is illegal");
        }
        try {
            return toTagAO(blogDataService.getTag(tagId));
        } catch (TagNotFoundInASBlogDataServiceException e) {
            throw new TagNotFoundException(tagId);
        } catch (ASBlogDataServiceException e) {
            log.error("getTag(" + tagId + ")", e);
            throw new BlogAPIException("Blog backend failure", e);
        }
    }


    @Override
    public List<TagAO> getTags(String postId) throws BlogAPIException {
        if (StringUtils.isEmpty(postId)) {
            throw new IllegalArgumentException("postId is illegal");
        }
        try {
            List<TagAO> result = new ArrayList<TagAO>();
            Post post = blogDataService.getPost(postId);

            for(String tagId : post.getTags()) {
                result.add(getTag(tagId));
            }

            return result;
        } catch (PostNotFoundInASBlogDataServiceException e) {
            throw new PostNotFoundException(postId);
        } catch (ASBlogDataServiceException e) {
            log.error("getTags(" + postId + ")", e);
            throw new BlogAPIException("Blog backend failure", e);
        }
    }

}
