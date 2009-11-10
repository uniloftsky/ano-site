<%@ page contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%><%@ page isELIgnored ="false" 
%><logic:iterate name="page" property="mediaLinks" type="net.anotheria.anosite.content.bean.MediaLinkBean" id="mediaLink"
	><logic:equal name="anosite.verbose" value="true"><!-- AS MediaLinks: MediaLink id: <bean:write name="mediaLink" property="id"/>, name: <bean:write name="mediaLink" property="name"/> --></logic:equal
   	><link<logic:notEmpty name="mediaLink" property="href"> href="${mediaLink.href}"</logic:notEmpty
   	><logic:notEmpty name="mediaLink" property="type"> type="${mediaLink.type}"</logic:notEmpty
   	><logic:notEmpty name="mediaLink" property="media"> media="${mediaLink.media}"</logic:notEmpty
   	><logic:notEmpty name="mediaLink" property="rel"> rel="${mediaLink.rel}"</logic:notEmpty
   	><logic:notEmpty name="mediaLink" property="rev"> rev="${mediaLink.rev}"</logic:notEmpty
   	><logic:notEmpty name="mediaLink" property="charset"> charset="${mediaLink.charset}"</logic:notEmpty
   	><logic:notEmpty name="mediaLink" property="hreflang"> hreflang="${mediaLink.hreflang}"</logic:notEmpty
   	>/>
</logic:iterate>
