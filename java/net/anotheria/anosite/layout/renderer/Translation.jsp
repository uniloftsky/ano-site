<%@ page
	contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%><%@taglib uri="/WEB-INF/tld/anosite.tld" prefix="as" %>

<bean:define id="box" toScope="page" name="__box" scope="request" type="net.anotheria.anosite.content.bean.BoxBean"/>
<logic:notEmpty name="box" property="cssClass">
<div id="box<bean:write name="box" property="id"/>" class="<bean:write name="box" property="cssClass"/>">
</logic:notEmpty>
<bean:define id="__boxlist" toScope="request" name="box" property="subboxes"/>
<jsp:include page="../templates/ListIterator.jsp" flush="false"/>
<logic:notEmpty name="box" property="cssClass">
</div>
</logic:notEmpty>