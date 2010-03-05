<%@ page contentType="text/html;charset=utf-8" session="true"%><%@ taglib
	uri="/tags/struts-bean" prefix="bean"%><%@ taglib
	uri="/tags/struts-html" prefix="html"%><%@ taglib
	uri="/tags/struts-logic" prefix="logic"%><%@ taglib
	uri="/WEB-INF/tld/anoweb.tld" prefix="ano"%>

<html>
<head>
<title>Edit Preferences</title>
<META http-equiv="pragma" content="no-cache">
<META http-equiv="Cache-Control" content="no-cache, must-revalidate">
<META name="Expires" content="0">
<META http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css"
	href="/blackstone/yui/core/build/reset-fonts-grids/reset-fonts-grids.css" />
<link rel="stylesheet" type="text/css"
	href="/blackstone/yui/core/build/fonts/fonts-min.css" />
<link rel="stylesheet" type="text/css"
	href="/blackstone/yui/core/build/assets/skins/sam/skin.css" />
<link rel="stylesheet" type="text/css"
	href="/blackstone/yui/core/build/container/assets/skins/sam/container.css" />
<link rel="stylesheet" type="text/css"
	href="/blackstone/yui/core/build/tabview/assets/skins/sam/tabview.css" />
<link href="/blackstone/css/admin.css" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="/blackstone/yui/core/build/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript"
	src="/blackstone/yui/core/build/element/element-debug.js"></script>
<script type="text/javascript"
	src="/blackstone/yui/core/build/datasource/datasource-min.js"></script>
<script type="text/javascript"
	src="/blackstone/yui/core/build/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript"
	src="/blackstone/yui/core/build/dragdrop/dragdrop-min.js"></script>
<script type="text/javascript"
	src="/blackstone/yui/core/build/container/container-min.js"></script>
<script type="text/javascript"
	src="/blackstone/yui/anoweb/widget/ComboBox.js"></script>
<script type="text/javascript" src="/blackstone/js/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript"
	src="/blackstone/js/jquery/plugins/jquery.cookie.js"></script>
</head>
<body class="editDialog yui-skin-sam">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="menuTitleSelected">Edit User Settings</td>
	</tr>
</table>

<br/><br/><br/>

<div class="yui-content">
<html:form action="userSettingsUpdate">		
	<input id="referrer" type="hidden" name="referrer" value="<bean:write name="EditUserSettingsForm" property="referrer"/>"/>
	<input id="userId" type="hidden" name="userId" value="<bean:write name="EditUserSettingsForm" property="userId"/>"/>
	<div class="yui-gd lineDark" id="languageFilteringSettings">
		<div class="yui-u first">Enable language filtering</div>		
		<div class="yui-u">
		<input id="languageFilteringEnabled"
		type="checkbox" name="languageFilteringEnabled"
		<logic:equal name="EditUserSettingsForm" property="languageFilteringEnabled" value="true"> checked="checked"</logic:equal> />
		</div>
	</div>
	
	<div class="yui-gd lineLight" id="displayedLanguages">
		<div class="yui-u first">Displayed languages</div>
		<div class="yui-u" id="displayedLanguagesInputs">
			<logic:iterate id='lang'
				name="EditUserSettingsForm" property="supportedLanguages">
				<html:multibox property="displayedLanguages">
					<bean:write name="lang" />
				</html:multibox>
					<bean:write name="lang" />
			</logic:iterate>	
		</div>
	</div>

	<div id="Functions" class="functions">		
		&nbsp;&raquo&nbsp;<a href="#" onClick="document.EditUserSettingsForm.submit(); return false">SaveAndClose</a>		
		&nbsp;&raquo&nbsp;<a href="<bean:write name="EditUserSettingsForm" property="referrer"/>">Close</a>		
	<div/>
	
</html:form>
</div>



</body>


<script type="text/javascript">

	enableDisableDisplayedLanguages = function() {
		$("div#displayedLanguagesInputs").find("input").each(function() {
		       $(this).attr( "disabled", !$("#languageFilteringEnabled").attr("checked") );		       
		 });
		 var fadeTo = $("#languageFilteringEnabled").attr("checked") ? 1 : 0.5;		 
		 $("div#displayedLanguages").fadeTo('slow', fadeTo);	
	}

	$(document).ready(function(){

		enableDisableDisplayedLanguages();
		
		$("#languageFilteringEnabled").click( function(event) {				 
			enableDisableDisplayedLanguages();						 							   				  
		});
	});
</script>

</html>
