<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%>
        <!-- Login -->

<h1><bean:write name="res.login.title"/></h1>
<div class="loginform">
  <form method="post" action="<bean:write name="login.action" ignore="true"/>"> 
    <fieldset>
      <p><label for="username" class="top"><bean:write name="res.login.username"/>:</label><br />
        <input type="text" name="username" id="username" tabindex="1" class="field" /></p>
   <p><label for="password" class="top"><bean:write name="res.login.password"/>:</label><br />
        <input type="password" name="password" id="password" tabindex="2" class="field" onkeypress="return webLoginEnter(document.loginfrm.cmdweblogin);" value="" /></p>
   <p><input type="checkbox" name="rememberme" id="rememberme" class="checkbox" tabindex="3" size="1" /><label for="rememberme" class="right"><bean:write name="res.login.rememberme"/></label></p>
   <p><input type="submit" name="login" class="button" value="<bean:write name="res.login.submit"/>"  /></p>
   <p><bean:write name="login.invalid" ignore="true" /></p>
   <p><a href="#" id="password_forgotten"><bean:write name="res.login.password_forgotten"/></a></p>
 </fieldset>
  </form>
</div>

