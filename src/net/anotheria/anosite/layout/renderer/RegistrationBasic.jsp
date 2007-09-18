<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/WEB-INF/tlds/struts-bean.tld" prefix="bean"
%><%@ taglib uri="/WEB-INF/tlds/struts-html.tld" prefix="html"
%><%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic"
%><div class="contactform">
<form method="post" action="Register.html">
<fieldset><legend>&nbsp;Personal Data&nbsp;</legend>
<p><label for="name" class="left">Name:</label> <input name="name"
	id="name" class="field" value="" tabindex="1" type="text"></p>
<p><label for="email" class="left">Email:</label> <input
	name="email" id="email" class="field" value="" tabindex="2" type="text"></p>
<p><label for="password" class="left">Password:</label> <input
	name="password" id="password" class="field" value="" tabindex="3"
	type="password"></p>
<p><label for="passwordConf" class="left">Password Conformation:</label> <input
	name="passwordConf" id="passwordConf" class="field" value="" tabindex="3"
	type="password"></p>
<p><input name="submit" id="submit" class="button"
	value="Next" tabindex="4" type="submit"></p>
</fieldset>
</form>
</div>
