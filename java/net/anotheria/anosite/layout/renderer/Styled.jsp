<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%>

<bean:define id="box" toScope="page" name="__box" scope="request" type="net.anotheria.anosite.content.bean.BoxBean"/>
<div <logic:notEmpty name="box" property="parameter1">id="<bean:write name="box" property="parameter1">"</logic:notEmpty> <logic:notEmpty name="box" property="parameter2">class="<bean:write name="box" property="parameter2">"</logic:notEmpty>>
<logic:notEqual name="box" property="content" value="">
	<bean:write name="box" property="content" filter="false"/>
</logic:notEqual>
<bean:define id="__boxlist" toScope="request" name="box" property="subboxes"/>
<jsp:include page="../templates/ListIterator.jsp" flush="false"/>
</div>