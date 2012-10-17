package net.anotheria.anosite.blog.api;

import net.anotheria.anosite.blog.api.PostAO;

import java.util.Comparator;

/**
 * Comparators for sorting posts in different orders.
 * @author vbezuhlyi
 */
public enum PostComparators implements Comparator<PostAO> {
    BY_CREATED {
        @Override
        public int compare(PostAO post, PostAO anotherPost) {
            if (post.getCreated() > anotherPost.getCreated())
                return 1;
            if (post.getCreated() < anotherPost.getCreated())
                return -1;
            return 0;
        }
    },

    BY_UPDATED {
        @Override
        public int compare(PostAO post, PostAO anotherPost) {
            if (post.getUpdated() > anotherPost.getUpdated())
                return 1;
            if (post.getUpdated() < anotherPost.getUpdated())
                return -1;
            return 0;
        }
    },

    BY_BLOGGER {
        @Override
        public int compare(PostAO post, PostAO anotherPost) {
            return post.getBlogger().compareTo(anotherPost.getBlogger());
        }
    },

    BY_NAME {
        @Override
        public int compare(PostAO post, PostAO anotherPost) {
            return post.getName().compareTo(anotherPost.getName());
        }
    }

    /*
     *  Possibly BY_RANK or BY_LIKES comparator will be added in future.
     */
}
