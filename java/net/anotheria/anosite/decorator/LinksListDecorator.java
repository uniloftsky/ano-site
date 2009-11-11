package net.anotheria.anosite.decorator;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.anodoc.data.Document;
import net.anotheria.anodoc.data.NoSuchDocumentException;
import net.anotheria.anodoc.data.NoSuchPropertyException;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.exception.ASGRuntimeException;
import net.anotheria.asg.util.decorators.IAttributeDecorator;
import net.anotheria.util.StringUtils;



/**
 * Basic Decorator for list of links
 * @author denis
 */
abstract public class LinksListDecorator implements IAttributeDecorator{
	
	@Override public String decorate(DataObject doc, String attributeName, String rule) {
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
			
			String href = doc.getDefinedParentName().toLowerCase() + StringUtils.capitalize(doc.getDefinedName())
			+ StringUtils.capitalize(attributeName) + "Show?pId=" + doc.getId() + "&ts=" + System.currentTimeMillis();
			
			String title = "";
			for (String id : ids){
				String name ;
				try{
					name = getLinkTargetName(id);
				}catch(NoSuchDocumentException e){
					name = "*DELETED*";
				}catch(ASGRuntimeException e){
					name = "*ASG-ERR: "+e.getMessage()+"*";
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
	
	abstract protected String getLinkTargetName(String targetId) throws ASGRuntimeException;

}
