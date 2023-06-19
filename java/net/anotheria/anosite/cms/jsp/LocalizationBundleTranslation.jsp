<%@ page
        contentType="text/html;charset=UTF-8" session="true"
%>
<%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
%>
<%@ taglib uri="/WEB-INF/tlds/anosite.tld" prefix="as"
%>
<%@ page isELIgnored="false" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>LocalizationBundleTranslation</title>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
    <meta name="Expires" content="0"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript" src="/cms_static/js/jquery-1.4.min.js"></script>
    <script type="text/javascript" src="/cms_static/js/anofunctions.js"></script>
    <link href="/cms_static/css/newadmin.css" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="MenuMaf.jsp" flush="true"/>
<div class="right">
    <div class="r_w">
        <div class="main_area">
            <div class="c_l"><!-- --></div>
            <div class="c_r"><!-- --></div>
            <div class="c_b_l"><!-- --></div>
            <div class="c_b_r"><!-- --></div>
            <div>
                <label>From:
                    <select name="languageTo" class="locale">
                        <ano:iterate name="languages" type="java.lang.String" id="option">
                            <option value="<ano:write name="option"/>"><ano:write name="option"/></option>
                        </ano:iterate>
                    </select>
                </label>
            </div>
            <div>
                <label> To:
                    <select name="languageFrom" class="locale">
                        <ano:iterate name="languages" type="java.lang.String" id="option">
                            <option value="<ano:write name="option"/>"><ano:write name="option"/></option>
                        </ano:iterate>
                    </select>
                </label>
            </div>
            <a href="#" class="button export-txt"><span>Translate</span></a>
            <div class="clear"><!-- --></div>
        </div>
    </div>
</div>
</body>
</html>
