<%@ page
	contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%><bean:define id="box" toScope="page" name="__box" scope="request" type="net.anotheria.anosite.content.bean.BoxBean"
/><div class="column1-unit">
<logic:notEqual name="box" property="content" value="">
	<bean:write name="box" property="content" filter="false"/>
</logic:notEqual>
</div>          
<hr class="clear-contentunit" />