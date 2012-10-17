package net.anotheria.anosite.decorator;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asblogdata.data.Comment;
import net.anotheria.anosite.gen.asblogdata.data.Post;
import net.anotheria.anosite.gen.asblogdata.service.IASBlogDataService;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.decorators.IAttributeDecorator;
import net.anotheria.util.StringUtils;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Decorator to show created/updated date of post/comment in usual format instead of long type number.
 *
 * @author vbezuhlyi
 */
public class DateDecorator implements IAttributeDecorator {

    /**
     * Default format of date/time for this decorator.
     */
    private static String DEFAULT_DATE_FORMAT = "dd.MM.yyyy HH:mm";


    @Override
    public String decorate(DataObject doc, String attributeName, String rule) {
        if (StringUtils.isEmpty(rule) || rule.equals("null")) { // yes, ShowPostAction sends parameter "null" as String by default
            rule = DEFAULT_DATE_FORMAT;
        }

        if (doc instanceof Post) {
            return getDateForPost((Post) doc, attributeName, rule);
        }
        if (doc instanceof Comment) {
            return getDateForComment((Comment) doc, attributeName, rule);
        }
        return "Incompatible element for decoration";
    }


    private String getDateForPost(Post post, String attributeName, String format) {
        if (attributeName.equals("created")) {
            return formatDate(post.getCreated(), format);
        }
        if (attributeName.equals("updated")) {
            long updated = post.getUpdated();
            return updated != 0 ? formatDate(updated, format) : "----";
        }
        return "Unknown attribute: " + attributeName;
    }


    private String getDateForComment(Comment comment, String attributeName, String format) {
        if (attributeName.equals("created")) {
            return formatDate(comment.getCreated(), format);
        }
        return "Unknown attribute: " + attributeName;
    }

    private String formatDate(long time, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date(time);
        return dateFormat.format(date);
    }
}
