package net.anotheria.anosite.decorator;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.anodoc.data.Document;
import net.anotheria.anodoc.data.NoSuchDocumentException;
import net.anotheria.anodoc.data.NoSuchPropertyException;
import net.anotheria.anosite.gen.assitedata.data.NaviItem;
import net.anotheria.anosite.gen.assitedata.data.Site;
import net.anotheria.anosite.gen.aswebdata.service.ASWebDataServiceFactory;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.exception.ASGRuntimeException;
import net.anotheria.asg.util.decorators.IAttributeDecorator;
import net.anotheria.util.StringUtils;

public class NaviItemDecorator implements IAttributeDecorator{
	
	private static IASWebDataService service = ASWebDataServiceFactory.createASWebDataService();
	
	public String decorate(DataObject doc, String attributeName, String rule) {
		try{
			
			//tmp hack
			List<Object> links = null;
			try{
				links = ((Document)doc).getListProperty(attributeName).getListData();
			}catch(NoSuchPropertyException e){
				links = new ArrayList<Object>(0);
			}
			List<String> ids = new ArrayList<String>(links.size());
			String value = ""+links.size()+" [";
			String linksValue = "";
			for (Object l : links){
				if (linksValue.length()>0)
					linksValue+=", ";
				linksValue+=l;
				ids.add(""+l);
			}
			value = value + linksValue+"]";
			
			String href = "#";
			if (doc instanceof Site){
				href = "site"+StringUtils.capitalize(attributeName)+"Show?pId="+doc.getId()+"&ts="+System.currentTimeMillis();
			}
			
			if (doc instanceof NaviItem){
				href = "naviitem"+StringUtils.capitalize(attributeName)+"Show?pId="+doc.getId()+"&ts="+System.currentTimeMillis();
			}
			
			String title = "";
			for (String id : ids){
				String name ;
				try{
					name = service.getPagex(id).getName();
				}catch(NoSuchDocumentException e){
					name = "*DELETED*";
				}catch(ASGRuntimeException e){
					name = "*ERR-"+e.getMessage()+"*";
				}
				if (title.length()>0)
					title += ", ";
				title += name;
					
			}
			if (title.length()>0)
				title = " title=\""+title+"\"";
			
			String hrefTarget = href.length()>1 ? " target=\"_blank\"" : "";
			
			return "<a href=\""+href+"\""+hrefTarget+title+">"+value+"</a>";
				
		}catch(NoSuchPropertyException e){
			return "none";
		}
	}

}
