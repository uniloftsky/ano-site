<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%><logic:iterate name="__boxlist" type="net.anotheria.anosite.content.bean.BoxBean" id="box"
	><logic:equal name="anosite.verbose" value="true"><!-- AS ListIterator: generating box, box id: <bean:write name="box" property="id"/>, name: <bean:write name="box" property="name"/> , renderer: <bean:write name="box" property="renderer"/>, type: <bean:write name="box" property="typeName"/> --></logic:equal
   	><bean:define id="__box" toScope="request"  scope="page" name="box" type="net.anotheria.anosite.content.bean.BoxBean"/>
	<% String renderer = box.getRenderer(); %>
	<logic:present name="anosite.sa..editModeFlag" scope="session">
	<a style="padding-left:3px;padding-right:3px; color:white; background-color: red; font-variant: small-caps; font-size:tiny" href="cms/boxEdit?pId=<bean:write name="box" property="id"/>" target="_blank"><bean:write name="box" property="name"/></a>
	<div style="border-top: solid red; border-left: solid red; border-right: solid red; border-bottom: solid red; border-width: 1px; ">
	</logic:present
	><jsp:include page="<%=renderer%>" flush="false"/><logic:present name="anosite.sa..editModeFlag" scope="session"
	></div></logic:present>
</logic:iterate>
