<%@ page
	contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
%>		
<%
	String pageName = (String)request.getAttribute("page");
%>
<html>
<head>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
</head>

<body >
<table width="790" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top" >
		<table width=100% border=0 cellpadding=3 cellspacing=0>
 		  <tr width=100% class="taborange">
		    <td valign="middle" colspan=2>
				<strong>ERROR</strong>
		    </td>
		  </tr>  
		  <tr width=100% class="logintext">
		   <td colspan=2></td>
		  </tr>
		  <tr class="logintextq">
		  	<td colspan="2" align="center"><br>
		      Die von Ihnen ausgef&uuml;hrte Aktion hat folgenden Fehler verursacht:<br> <br>
		      <font color=red><ano:write name="error" property="message"/></font>
		      <br><br>
		      Falls Sie meinen, dass der Fehler nicht aus einer falschen Eingabe resultiert,
		      benachrichtigen Sie uns bitte &uuml;ber diesen Fehler unter <a href="mailto:support@anotheria.net?subject=ERROR IK:<ano:write name="error" property="message"/>">support@anotheria.net</a>.
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
		      <a href="#" class="more" onClick="history.back(); return false">&raquo;&nbsp;Zur&uuml;ck&nbsp;</a>
		  	</td>
		  </tr>
		  <tr width=100% class="logintextq">
		    <td colspan=2></td>
		  </tr>
		  </form>
		</table>
		<p><font color="#FFFFFF"><ano:write name="error" property="stackTrace"/></font></p>
		  
    </td>
    <td width="21" valign="top"></td>
  </tr>
  <tr>
    <td colspan="3">&nbsp;</td>
  </tr>
</table>
<jsp:include page="Footer.jsp"/>
</body>
</html>