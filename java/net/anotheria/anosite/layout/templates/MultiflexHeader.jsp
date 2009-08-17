<%@ page
	contentType="text/html;charset=UTF-8" session="true"
%><%@ taglib uri="/tags/struts-bean" prefix="bean"
%><%@ taglib uri="/tags/struts-html" prefix="html"
%><%@ taglib uri="/tags/struts-logic" prefix="logic"
%>
    <!-- A. HEADER -->      
    <div class="header">
      
      <!-- A.1 HEADER MIDDLE -->
      <div class="header-middle">    
   
        <!-- Sitelogo and sitename -->

        <a class="sitelogo" href="<bean:write name="site" property="linkToStartPage"/>" title="Go to Start page"></a>
        <div class="sitename">

          <h1><a href="<bean:write name="site" property="linkToStartPage"/>" title="Go to Start page"><bean:write name="site" property="title"/></a></h1>
          <h2><bean:write name="site" property="subtitle"/></h2>
        </div>

        <!-- Navigation Level 0 -->

        <div class="nav0">
          <logic:equal name="site" property="languageSelector" value="true">
          <ul>

            <li><a href="<bean:write name="site" property="linkToStartPage"/>?lang=EN" title="English homepage"><img src="simg/flag_gb.gif" alt="English homepage" /></a></li>
            <li><a href="<bean:write name="site" property="linkToStartPage"/>?lang=DE" title="Homepage auf Deutsch"><img src="simg/flag_germany.gif" alt="German homepage" /></a></li>
          </ul>
          </logic:equal>
        </div>			

        <!-- Navigation Level 1 -->
        <div class="nav1">

          <ul>
          	<logic:iterate name="topNavi" id="item" type="net.anotheria.anosite.content.bean.NaviItemBean">
	            <li><a <logic:equal name="item" property="popup" value="true">target="_blank" </logic:equal>href="<bean:write name="item" property="link"/>" title="<bean:write name="item" property="title"/>"><bean:write name="item" property="name"/></a></li>
            </logic:iterate>
          </ul>
        </div>              
      </div>

      
      <!-- A.2 HEADER BOTTOM -->
      <div class="header-bottom">

      
        <!-- Navigation Level 2 (Drop-down menus) -->
        <div class="nav2">
	
	      <logic:iterate name="mainNavi" id="item" type="net.anotheria.anosite.content.bean.NaviItemBean">
          	  <!-- Navigation item -->
        	  <ul>
          	  <logic:equal name="item" property="hasChilds" value="false">
    	        <li><a <logic:equal name="item" property="popup" value="true">target="_blank" </logic:equal>href="<bean:write name="item" property="link"/>"><bean:write name="item" property="name"/></a></li>
	          </logic:equal>
          	  <logic:equal name="item" property="hasChilds" value="true">
	            <li><a <logic:equal name="item" property="popup" value="true">target="_blank" </logic:equal>href="<bean:write name="item" property="link"/>"><bean:write name="item" property="name"/><!--[if IE 7]><!--></a><!--<![endif]-->
	              <!--[if lte IE 6]><table><tr><td><![endif]-->
	                <ul>
				      <logic:iterate name="item" property="subNavi" id="subitem" type="net.anotheria.anosite.content.bean.NaviItemBean">
	                  <li><a <logic:equal name="subitem" property="popup" value="true">target="_blank" </logic:equal>href="<bean:write name="subitem" property="link"/>"><bean:write name="subitem" property="name"/></a></li>
	                  </logic:iterate>
	                </ul>
	              <!--[if lte IE 6]></td></tr></table></a><![endif]-->
	            </li>
	          </logic:equal>
	          </ul>
	      </logic:iterate>

        </div>
	  </div>

      <!-- A.3 HEADER BREADCRUMBS -->

      <!-- Breadcrumbs -->
      <div class="header-breadcrumbs">
        <ul>
      	<logic:iterate name="breadcrumbs" type="net.anotheria.anosite.content.bean.BreadCrumbItemBean" id="b">
      		<logic:equal name="b" property="clickable" value="true">
	          <li><a href="<bean:write name="b" property="link"/>"><bean:write name="b" property="title"/></a></li>
			</logic:equal>
      		<logic:notEqual name="b" property="clickable" value="true">
	          <li><bean:write name="b" property="title"/></li>
			</logic:notEqual>
        </logic:iterate>
        </ul>

		<logic:equal name="site" property="showSearchDialog" value="true">
        <!-- Search form -->                  
        <div class="searchform">
          <form action="<bean:write name="site" property="searchTarget"/>" method="get">

            <fieldset>
              <input name="criteria" class="field"  value=" Search..." />
              <input type="submit" name="button" class="button" value="GO!" />
            </fieldset>
          </form>
        </div>
        </logic:equal>
      </div>
    </div>
