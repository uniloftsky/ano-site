<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%><?xml version="1.0" encoding="iso-8859-15" standalone="yes"?>
<rss xmlns:media="http://search.yahoo.com/mrss" version="2.0">
<bean:define id="__boxlist" toScope="request" name="page" property="column1"/>
<jsp:include page="ListIterator.jsp" flush="false"/>
</rss>