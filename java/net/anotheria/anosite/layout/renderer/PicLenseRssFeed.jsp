<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%> <channel>
<logic:iterate name="piclenserssitems" type="net.anotheria.anosite.content.bean.RssFeedItem" id="item">
        <item>
            <title><bean:write name="item" property="title"/></title>
            <link><bean:write name="item" property="link"/></link>
            <media:thumbnail url="<bean:write name="item" property="thumbnailUrl"/>"/>
            <media:content url="<bean:write name="item" property="contentUrl"/>"/>
            <%--guid isPermaLink="<bean:write name="item" property="permalink"/>"><bean:write name="item" property="guid"/></guid--%>
        </item>
</logic:iterate> 
</channel>
