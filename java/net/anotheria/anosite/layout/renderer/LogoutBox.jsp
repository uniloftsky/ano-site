<%@ page
	contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%>
        <!-- Login -->
<h1><bean:write name="res.logout.title"/></h1>
<div class="loginform">
  <form method="post" action="<bean:write name="logout.action" ignore="true"/>"> 
   	<fieldset>
   		<p><input type="submit" name="logout" class="button" value="<bean:write name="res.logout.submit"/>"  /></p>
 	</fieldset>
  </form>
</div>

