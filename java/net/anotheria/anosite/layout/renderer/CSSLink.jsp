<%@ page
	contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%>

<bean:define id="box" toScope="page" name="__box" scope="request" type="net.anotheria.anosite.content.bean.BoxBean"/>
<script type="text/javascript" src="<bean:write name="box" property="parameter1" filter="false"/>" >
<logic:notEmpty name="box" property="content">
	<script type="text/javascript">
	<bean:write name="box" property="content" filter="false"/>
	</script>
</logic:notEmpty>

<%--
<bean:define id="__boxlist" toScope="request" name="box" property="subboxes"/>
<jsp:include page="../templates/ListIterator.jsp" flush="false"/>
--%>