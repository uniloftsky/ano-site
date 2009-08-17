<%@ page
	contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%>
<bean:define id="__boxlist" toScope="request" name="page" property="column1"/>
<jsp:include page="ListIterator.jsp" flush="false"/>
<bean:define id="__boxlist" toScope="request" name="page" property="column2"/>
<jsp:include page="ListIterator.jsp" flush="false"/>



