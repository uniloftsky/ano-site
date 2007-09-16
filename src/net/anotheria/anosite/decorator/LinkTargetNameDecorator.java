package net.anotheria.anosite.decorator;

import net.anotheria.anodoc.data.NoSuchDocumentException;
import net.anotheria.anodoc.data.NoSuchPropertyException;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;
import net.anotheria.anosite.gen.asmetadata.data.PageTemplate;
import net.anotheria.anosite.gen.asmetadata.data.Site;
import net.anotheria.anosite.gen.asmetadata.service.ASMetaDataServiceFactory;
import net.anotheria.anosite.gen.asmetadata.service.IASMetaDataService;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.gen.aswebdata.data.NaviItem;
import net.anotheria.anosite.gen.aswebdata.data.Pagex;
import net.anotheria.anosite.gen.aswebdata.service.ASWebDataServiceFactory;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.decorators.IAttributeDecorator;

public class LinkTargetNameDecorator implements IAttributeDecorator{
	private static IASFederatedDataService federatedDataService = ASFederatedDataServiceFactory.createASFederatedDataService();
	private static IASMetaDataService metaDataService = ASMetaDataServiceFactory.createASMetaDataService();
	private static IASWebDataService webDataService = ASWebDataServiceFactory.createASWebDataService();

	/* (non-Javadoc)
	 * @see net.anotheria.asg.util.decorators.IAttributeDecorator#decorate(net.anotheria.anodoc.data.Document, java.lang.String, java.lang.String)
	 */
	public String decorate(DataObject doc, String attributeName, String rule) {
		String name = "Unknown";
		String linkValue = "?";
		try{
			linkValue = ""+ doc.getPropertyValue(attributeName);
			if (doc instanceof PageTemplate){
				name = getTargetNameForTemplate((PageTemplate)doc, attributeName);
			}
			if (doc instanceof NaviItem){
				name = getTargetNameForNaviItem((NaviItem)doc, attributeName);
			}
			if (doc instanceof Box){
				name = getTargetNameForBox((Box)doc, attributeName);
			}
			if (doc instanceof Pagex){
				name = getTargetNameForPage((Pagex)doc, attributeName);
			}
			if (doc instanceof Site){
				name = getTargetNameForSite((Site)doc, attributeName);
			}
		}catch(NoSuchDocumentException e){
			name = "*** DELETED ***";
		}catch(NoSuchPropertyException e){
			name = "*** NoProp ***";
		}catch(RuntimeException e){
			name = "*** ERR: "+e.getMessage()+" ***";
		}

		return name + " ["+linkValue+"]";
	}
	
	private String getTargetNameForNaviItem(NaviItem item, String attributeName){
		if (attributeName.equals("internalLink")){
			return webDataService.getPagex(item.getInternalLink()).getName();
		}
		return "Unknown attribute: "+attributeName;
		
	}
	
	private String getTargetNameForTemplate(PageTemplate template, String attributeName){
		if (attributeName.equals("style")){
			return metaDataService.getPageStyle(template.getStyle()).getName();
		}
		if (attributeName.equals("site")){
			return metaDataService.getSite(template.getSite()).getName();
		}
		return "Unknown attribute: "+attributeName;
	}
	
	private String getTargetNameForSite(Site site, String attributeName){
		if (attributeName.equals("startpage")){
			return webDataService.getPagex(site.getStartpage()).getName();
		}
		return "Unknown attribute: "+attributeName;
	}

	private String getTargetNameForBox(Box box, String attributeName){
		if (attributeName.equals("handler")){
			return federatedDataService.getBoxHandlerDef(box.getHandler()).getName();
		}
		if (attributeName.equals("type")){
			return federatedDataService.getBoxType(box.getType()).getName();
		}
		return "UnknownAttr: "+attributeName;
	}

	private String getTargetNameForPage(Pagex page, String attributeName){
		if (attributeName.equals("template")){
			return metaDataService.getPageTemplate(page.getTemplate()).getName();
		}
		return "UnknownAttr: "+attributeName;
	}
	
}
