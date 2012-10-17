<%@ page contentType="text/html;charset=utf-8" session="true" %>
<%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano" %>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html>

<html>

<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="cache-control" content="no-cache" />

    <title>Blog</title>

</head>

<body>
    <p>
        <ano:define id="__boxlist" toScope="request" name="page" property="column1" />
        <jsp:include page="/net/anotheria/anosite/layout/templates/ListIterator.jsp" flush="false"/>
    </p>
    <p>
        <ano:define id="__boxlist" toScope="request" name="page" property="column2" />
        <jsp:include page="/net/anotheria/anosite/layout/templates/ListIterator.jsp" flush="false"/>
    </p>
    <p>
        <ano:define id="__boxlist" toScope="request" name="page" property="column3" />
        <jsp:include page="/net/anotheria/anosite/layout/templates/ListIterator.jsp" flush="false"/>
    </p>
</body>

</html>