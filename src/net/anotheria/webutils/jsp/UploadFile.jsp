<%@ page
	contentType="text/html;charset=windows-1251" session="true"
%><%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"
%><%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"
%><%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"
%>
<HTML>
<HEAD>
<META http-equiv="pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache, must-revalidate">
<META name="Expires" content="0">
<META http-equiv="Content-Type" content="text/html; charset=windows-1251">
<bean:message key="styles.common.link"/>
<head>
<title>@Upload file</title>
<script language="JavaScript">
	var j=0;
	var tmp;
	var loading = false;
	function uploadSubmit(){
		document.upload.submit();
		j=0;
		loading = true;
		parent.focusUpload();
		//statebarReload();
	}
	
	function statebarReload(){
/*		if(loading){
			j++;
			var status = "";
			for(t=0;t<=j;t++){
				status = status + ".";
			}
			statebar.innerHTML = "Loading" + status;
			tmp=window.setTimeout('statebarReload()',100);	
		}*/
	}

	function stopState(){
/*		loading = false;
		if(tmp){
			window.clearTimeout(tmp);
		}
		statebar.innerHTML = "";*/
	}

	
</script>
<bean:message key="styles.common.link"/>
<bean:message key="javascript.common.link"/>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="hellerblau">
  <table border="0" width=100% cellspacing=0 cellpadding=0>
  	<form id="upload" name="upload" target="answer" method="post" enctype="multipart/form-data" action="/aoncms/cms/fileUpload">
   <tr>
    <td class="hellerblau">
	Datei<br> 
	<input type="file" id="file" name="file" size="20">&nbsp;
	<input type="submit" value="Speichern" size="20"><br><br>
<iframe 
	id="answer"
	name="answer"
	src="<bean:message key="application.path"/>net/anotheria/webutils/jsp/UploadFileResult.jsp" 
	width="100%" 
	height="20" 
	scrolling="No" 
	frameborder="0" 
	marginwidth="0" 
	marginheight="0" >
</iframe>
    </td>
   </tr>
	</form>
  </table>
</body>
</html>