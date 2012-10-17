<%@ page contentType="text/html;charset=utf-8" session="true" %>
<%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>

<h1>[Demo] Posts to blog:</h1>
<hr>
<ano:iterate name="blog" property="posts" id="post">
    <h2>${post.name}</h2><br>
    <b>Posted on:</b> ${post.created} <b>Updated on:</b> ${post.updated}<br><br>
    ${post.content}<br><br>

    <b>Tags:</b> <ano:iterate name="post" property="tags" id="tag">
        <a href="#">${tag.name}</a>
    </ano:iterate>
    <br><br>

    <b>Comments to this post:</b><br><br>
    <ano:iterate name="post" property="comments" id="comment">
        Leaved on: ${comment.created} by ${comment.commentator}<br><br>
        &nbsp&nbsp<i>${comment.content}</i><br><br>
    </ano:iterate>
    <hr>
</ano:iterate>

