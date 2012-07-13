<%@ page contentType="text/html;charset=UTF-8" session="true"%>
<%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"%>
<%@ taglib uri="/WEB-INF/tlds/anosite.tld" prefix="as"%>
<%@ page isELIgnored ="false" %>
<html>
<head>
<title>Change password</title>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
</head>
<body bgcolor="#FFFFFF" leftMargin="0" topMargin="0" marginwidth="0" marginheight="0">
<center>
<br><br><br>
<form name="ChangePass" action="changePass" method="POST">
 <table border="0" style="width: 500px; align: center;">
  <tr>
   <td valign="middle" colspan=2>
   	<strong>CHANGE PASSWORD</strong>
   </td>
  <tr>
   <td colspan=2 align="center"><br><ano:write name="Message"/><br><br>
  <tr>
   <td align="right">Your login:<td align="left"><input type="text" name="Login" disabled="disabled" value="<ano:write name="currentUserId"/>">
  <tr>
   <td align="right">Current password:<td align="left"><input type="password" name="OldPass" value="">
  <tr>
   <td align="right">New password:<td align="left"><input type="password" name="NewPass1" value="">
  <tr>
   <td align="right">New password (confirm):<td align="left"><input type="password" name="NewPass2" value="">
  <tr>
   <td colspan=2 align="center"><br><input type="Submit" value="Change password">
 </table>
 <input type="hidden" name="isSubmit" value="true">
</form>
</center>
</body>
</html>