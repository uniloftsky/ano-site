<%@ page
	contentType="text/html;charset=iso-8859-15" session="true"
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
          <ul>

            <li><a href="#" title="Pagina home in Italiano"><img src="simg/flag_italy.gif" alt="Image description" /></a></li>
            <li><a href="#" title="Homepage auf Deutsch"><img src="simg/flag_germany.gif" alt="Image description" /></a></li>
            <li><a href="#" title="Hemsidan p&aring; svenska"><img src="simg/flag_sweden.gif" alt="Image description" /></a></li>
          </ul>
        </div>			

        <!-- Navigation Level 1 -->
        <div class="nav1">

          <ul>
            <li><a href="#" title="Go to Start page">Home</a></li>
            <li><a href="#" title="Get to know who we are">About</a></li>
            <li><a href="#" title="Get in touch with us">Contact</a></li>																		
            <li><a href="#" title="Get an overview of website">Sitemap</a></li>
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
          <li><a href="#">Home</a></li>

          <li><a href="#">Webdesign</a></li>
          <li><a href="#">Templates</a></li>
          <li>Multiflex-3</li>
        </ul>

        <!-- Search form -->                  
        <div class="searchform">
          <form action="index.html" method="get">

            <fieldset>
              <input name="field" class="field"  value=" Search..." />
              <input type="submit" name="button" class="button" value="GO!" />
            </fieldset>
          </form>
        </div>
      </div>
    </div>
