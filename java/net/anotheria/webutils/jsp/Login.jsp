<%@ page
	contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"
%><%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"
%><%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"
%><%@ taglib uri="/WEB-INF/tld/anoweb.tld" prefix="ano"
%><html>
<head>
<title>please login</title>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<bean:message key="styles.common.link"/>
<bean:message key="styles.links.link"/>
<!--bean:message key="javascript.common.link"/-->
</head>
<body bgcolor="#FFFFFF" leftMargin="0" topMargin="0" marginwidth="0" marginheight="0">
<center>
<br><br><br>
<table width=500 border=0 cellpadding=1 cellspacing=0>
 <tr class="aktiv">
   <td width=100%>
		<table width=100% border=0 cellpadding=3 cellspacing=0>
		 <form name="Login" method="POST" action="login">
		  <tr width=100% class="taborange">
		    <td valign="middle" colspan=2>
				<strong>LOGIN</strong>
		    </td> 
		  </tr>  
		  <tr width=100% class="logintext">
		   <td colspan=2><img src="<bean:message key="emptyimage"/>" height=10 width=1></td>
		  </tr>
		  <tr class="qsinfo">
		  	<td colspan="2" align="center"><br>
		      This page is not public. Please login to access this page.
		      <br><br>
		  	</td>
		  </tr>
		  <tr class="logintextq">
		  	<td width=35% align="right">
		      Username:&nbsp;
		  	</td>
		  	<td width="65%" align="left">
		      <input class="myinput" type="text" size="20" name="pUserId">
		  	</td>
		  </tr>
		  <tr class="logintextq">
		  	<td width=35% align="right">
		      Password:&nbsp;
		  	</td>
		  	<td width="65%" align="left">
		      <input class="myinput" type="password" size="20" name="pPassword">
		  	</td>
		  </tr>
		  <tr class="logintext">
		  	<td colspan="2">&nbsp;</td>
		  </tr>
		  <tr class="logintextq">
		  	<td width=35% align="right">
		      &nbsp;
		  	</td>
		  	<td width="65%" align="left" class="logintextq">
		            <input type="submit" value="Login">
		  	</td>
		  </tr>
		  <tr width=100% class="logintextq">
		    <td colspan=2><img src="<bean:message key="emptyimage"/>" height=10 width=1></td>
		  </tr>
		  </form>
		</table>
	</td>
 </tr>
</table>
</center>
</BODY>
</HTML>