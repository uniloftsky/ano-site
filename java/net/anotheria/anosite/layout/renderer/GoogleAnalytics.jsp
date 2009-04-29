<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%><bean:define id="box" toScope="page" name="__box" scope="request" type="net.anotheria.anosite.content.bean.BoxBean"
/><%
	String url = request.isSecure() ? 
		"https://ssl.google-analytics.com/urchin.js" : 
		"http://www.google-analytics.com/urchin.js";
%><!-- google analytics -->
<script src="<%=url%>" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "<bean:write name="box" property="parameter1"/>";
urchinTracker();
</script>
<!-- /google analytics -->
