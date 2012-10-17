package net.anotheria.anosite.blog.api;

import net.anotheria.anosite.blog.api.CommentAO;

import java.util.Comparator;

/**
 * Comparators for sorting comments in different orders.
 * @author vbezuhlyi
 */
public enum CommentComparators implements Comparator<CommentAO> {
    BY_CREATED {
        @Override
        public int compare(CommentAO comment, CommentAO anotherComment) {
            if (comment.getCreated() > anotherComment.getCreated())
                return 1;
            if (comment.getCreated() < anotherComment.getCreated())
                return -1;
            return 0;
        }
    }
}
