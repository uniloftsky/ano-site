<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%>
        <!-- Login -->

        <h1>Login</h1>
        <div class="loginform">
          <form method="post" action="MyProfile.html"> 
            <p><input type="hidden" name="rememberme" value="0" /></p>
            <fieldset>
              <p><label for="username_1" class="top">User:</label><br />
                <input type="text" name="username" id="username" tabindex="1" class="field" onkeypress="return webLoginEnter(document.loginfrm.password);" value="" /></p>
    	      <p><label for="password_1" class="top">Password:</label><br />

                <input type="password" name="password" id="password" tabindex="2" class="field" onkeypress="return webLoginEnter(document.loginfrm.cmdweblogin);" value="" /></p>
    	      <p><input type="checkbox" name="checkbox_1" id="checkbox_1" class="checkbox" tabindex="3" size="1" value="" onclick="webLoginCheckRemember()" /><label for="checkbox_1" class="right">Remember me</label></p>
    	      <p><input type="submit" name="cmdweblogin" class="button" value="LOGIN"  /></p>
	          <p><a href="#" onclick="webLoginShowForm(2);return false;" id="forgotpsswd_1">Password forgotten?</a></p>
	        </fieldset>
          </form>
        </div>

