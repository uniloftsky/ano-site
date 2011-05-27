<%@ page
	contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="http://www.anotheria.net/ano-tags" prefix="ano"
%>
<HTML>
<HEAD>
<META http-equiv="pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache, must-revalidate">
<META name="Expires" content="0">
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/css/admin.css" rel="stylesheet" type="text/css">
<title>@Upload file</title>
</head>
<body topmargin="0" marginwidth="0" marginheight="0" onLoad="parent.stopState();">
<%--<ano:messagesPresent>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#e2e2e2">
	<tr><td colspan="3" height="16">&nbsp;</td></tr>
	<tr>
		<td colspan="3" bgcolor="#FF0000"></td>
	</tr>
	<tr>
		<td bgcolor="#FF0000" width="1"></td>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="6">
				<html:messages id="msg" bundle="ds">
					<tr>
				  	<td><font color="#FF0000">### <%=msg%> ###</font></td>
				   </tr>
				 </html:messages>
			</table>
		</td>
		<td bgcolor="#FF0000" width="1"></td>
	</tr>
	<tr>
		<td colspan="3" bgcolor="#FF0000"></td>	
	</tr>
</table>
</ano:messagesPresent>
<ano:messagesNotPresent>--%>
<ano:equal name="ano-web.file.fileBean" property="valid" value="true">
<table  cellspacing="0" cellpadding="0" width=100% >
	<tr class="hellerblau">
		<td>
		Filename:&nbsp;
		<ano:equal name="ano-web.file.fileBean" property="filePresent" value="false">
		<ano:write name="ano-web.file.fileBean" property="name"/>
		</ano:equal>
		<ano:equal name="ano-web.file.fileBean" property="filePresent" value="true">
		<a target="_blank" href="<%=request.getContextPath()%>/cms/showTmpFile"><ano:write name="ano-web.file.fileBean" property="name"/></a>
		</ano:equal>
		&nbsp;|&nbsp;
		Filesize:&nbsp;<ano:write name="ano-web.file.fileBean" property="size"/>
		&nbsp;|&nbsp;
		<ano:write name="ano-web.file.fileBean" property="message"/>
		</td>
    </tr>
	<tr class="hellerblau">
		<td colspan=5>&nbsp;</td>
    </tr>
</table>
</ano:equal>
<%--</ano:messagesNotPresent>--%>
</body>
</html>