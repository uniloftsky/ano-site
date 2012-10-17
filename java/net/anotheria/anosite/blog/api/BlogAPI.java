package net.anotheria.anosite.blog.api;

import net.anotheria.anoplass.api.API;
import net.anotheria.anosite.blog.api.exception.BlogAPIException;

import java.util.Comparator;
import java.util.List;

/**
 * BlogAPI interface.
 * @author vbezuhlyi
 */
public interface BlogAPI extends API {

    /**
     * Returns blog with all posts, comments and tags data inside.
     * For default implementation.
     *
     * @return  {@link BlogAO}
     * @throws BlogAPIException on errors Post/Comment/TagNotFoundException - if element not found
     */
    BlogAO getBlog() throws BlogAPIException;

    /**
     * Returns blog with all posts, comments and tags data inside.
     *
     * @return  {@link BlogAO}
     * @param postComparator comparator for sorting posts by specific parameter
     * @param commentComparator comparator for sorting comments by specific parameter
     * @throws BlogAPIException on errors Post/Comment/TagNotFoundException - if element not found
     */
    BlogAO getBlog(Comparator<PostAO> postComparator, Comparator<CommentAO> commentComparator) throws BlogAPIException;

    /**
     * Returns post with requested ID.
     * For default implementation.
     *
     * @param postId post ID
     * @return {@link PostAO} collection
     * @throws BlogAPIException on errors Post/Comment/TagNotFoundException - if element not found
     */
    PostAO getPost(String postId) throws BlogAPIException;

    /**
     * Returns post with requested ID, with comments sorted by their specific parameter.
     *
     *
     * @param postId post ID
     * @param commentComparator comparator for sorting comments by specific parameter
     * @return {@link PostAO} collection
     * @throws BlogAPIException on errors Post/Comment/TagNotFoundException - if element not found
     */
    PostAO getPost(String postId, Comparator<CommentAO> commentComparator) throws BlogAPIException;

    /**
     * Returns collection of all posts to blog.
     * For default implementation.
     *
     * @return {@link PostAO} collection
     * @throws BlogAPIException on errors Post/Comment/TagNotFoundException - if element not found
     */
    List<PostAO> getPosts() throws BlogAPIException;

    /**
     * Returns collection of all posts to blog, sorted by specific parameter,
     * with comments sorted by specific parameter.
     *
     * @param postComparator comparator for sorting posts by specific parameter
     * @param commentComparator comparator for sorting comments by specific parameter
     * @return {@link PostAO} collection
     * @throws BlogAPIException on errors Post/Comment/TagNotFoundException - if element not found
     */
    List<PostAO> getPosts(Comparator<PostAO> postComparator, Comparator<CommentAO> commentComparator) throws BlogAPIException;

    /**
     * Returns comment with requested ID.
     *
     * @param commentId comment ID
     * @return {@link CommentAO}
     * @throws BlogAPIException on errors CommentNotFoundException - if comment not found
     */
    CommentAO getComment(String commentId) throws BlogAPIException;

    /**
     * Returns collection of comments, related to post with requested ID.
     * For default implementation.
     *
     * @param postId post ID
     * @return {@link CommentAO} collection
     * @throws BlogAPIException on errors Post/CommentNotFoundException - if element not found
     */
    List<CommentAO> getComments(String postId) throws BlogAPIException;

    /**
     * Returns collection of comments, related to post with requested ID, sorted by specific parameter.
     *
     * @param postId post ID
     * @param commentComparator omparator for sorting comments by specific parameter
     * @return {@link CommentAO} collection
     * @throws BlogAPIException on errors Post/CommentNotFoundException - if element not found
     */
    List<CommentAO> getComments(String postId, Comparator<CommentAO> commentComparator) throws BlogAPIException;

    /**
     * Returns tag with requested ID.
     *
     * @param tagId tag ID
     * @return {@link TagAO}
     * @throws BlogAPIException on errors TagNotFoundException - if tag not found
     */
    TagAO getTag(String tagId) throws BlogAPIException;

    /**
     * Returns collections of tags, related to post with requested ID.
     *
     * @param postId post ID
     * @return {@link TagAO} collection
     * @throws BlogAPIException on errors TagNotFoundException - if tag not found
     */
    List<TagAO> getTags(String postId) throws BlogAPIException;

}
