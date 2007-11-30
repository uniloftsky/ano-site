<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%> <meta http-equiv="content-type" content="text/html; charset=iso-8859-15" />
  <meta http-equiv="cache-control" content="no-cache" />
  <meta http-equiv="expires" content="3600" />
  <meta name="revisit-after" content="2 days" />
  <meta name="robots" content="index,follow" />
  <meta name="publisher" content="Your publisher infos here ..." />
  <meta name="copyright" content="Your copyright infos here ..." />
  <meta name="author" content="Design: Wolfgang (www.1-2-3-4.info) / Modified: Your Name" />
  <meta name="distribution" content="global" />
  <meta name="description" content="Your page description here ..." />
  <meta name="keywords" content="Your keywords, keywords, keywords, here ..." />
  <link rel="stylesheet" type="text/css" media="screen,projection,print" href="<bean:write name="stylesheet" property="link"/>" />
  <link rel="icon" type="image/x-icon" href="simg/favicon.ico" />
  <title><bean:write name="page" scope="request" property="title"/></title>
  <bean:define id="__boxlist" toScope="request" name="page" property="headerBoxes"/>
  <jsp:include page="ListIterator.jsp" flush="false"/>
  