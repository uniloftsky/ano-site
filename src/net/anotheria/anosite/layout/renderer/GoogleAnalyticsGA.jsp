<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%><bean:define id="box" toScope="page" name="__box" scope="request" type="net.anotheria.anosite.content.bean.BoxBean"
/><!-- /google analytics -->
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
var pageTracker = _gat._getTracker("<%=box.getAttributes().getAttribute("trackerId").getValue()%>");
pageTracker._initData();
pageTracker._setDomainName(none);
pageTracker._setAllowLinker(true);
pageTracker._trackPageview();
<bean:define id="__boxlist" toScope="request" name="box" property="subboxes"/>
<jsp:include page="../templates/ListIterator.jsp" flush="false"/>
</script>