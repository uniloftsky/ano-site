<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%><bean:define id="box" toScope="page" name="__box" scope="request" type="net.anotheria.anosite.content.bean.BoxBean"/>
<link id="gallery" rel="alternate" href="<bean:write name="box" property="parameter1"/>.rss" type="application/rss+xml">
<script type="text/javascript" src="http://lite.piclens.com/current/piclens.js"> </script>
