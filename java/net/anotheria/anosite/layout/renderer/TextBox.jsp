<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%><bean:define id="box" toScope="page" name="__box" scope="request" type="net.anotheria.anosite.content.bean.BoxBean"
/>
<logic:notEqual name="box" property="parameter1" value="">
	<h2><bean:write name="box" property="parameter1"/></h2>
</logic:notEqual>
<p><bean:write name="box" property="content" filter="false"/></p>
<p>&nbsp;</p>