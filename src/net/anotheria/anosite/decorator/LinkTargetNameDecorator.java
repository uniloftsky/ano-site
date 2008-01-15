package net.anotheria.anosite.decorator;

import net.anotheria.anodoc.data.NoSuchDocumentException;
import net.anotheria.anodoc.data.NoSuchPropertyException;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;
import net.anotheria.anosite.gen.aslayoutdata.service.ASLayoutDataServiceFactory;
import net.anotheria.anosite.gen.aslayoutdata.service.IASLayoutDataService;
import net.anotheria.anosite.gen.assitedata.data.EntryPoint;
import net.anotheria.anosite.gen.assitedata.data.NaviItem;
import net.anotheria.anosite.gen.assitedata.data.PageTemplate;
import net.anotheria.anosite.gen.assitedata.data.Site;
import net.anotheria.anosite.gen.assitedata.service.ASSiteDataServiceFactory;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.gen.aswebdata.data.Pagex;
import net.anotheria.anosite.gen.aswebdata.service.ASWebDataServiceFactory;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.exception.ASGRuntimeException;
import net.anotheria.asg.util.decorators.IAttributeDecorator;

public class LinkTargetNameDecorator implements IAttributeDecorator{
	private static IASFederatedDataService federatedDataService = ASFederatedDataServiceFactory.createASFederatedDataService();
	private static IASSiteDataService siteDataService = ASSiteDataServiceFactory.createASSiteDataService();
	private static IASWebDataService webDataService = ASWebDataServiceFactory.createASWebDataService();
	private static IASLayoutDataService layoutDataService = ASLayoutDataServiceFactory.createASLayoutDataService();

	/* (non-Javadoc)
	 * @see net.anotheria.asg.util.decorators.IAttributeDecorator#decorate(net.anotheria.anodoc.data.Document, java.lang.String, java.lang.String)
	 */
	public String decorate(DataObject doc, String attributeName, String rule) {
		String name = "Unknown";
		String linkValue = "?";
		try{
			Object link = doc.getPropertyValue(attributeName);
			
			linkValue = link == null ? null : ""+link;
			if (linkValue==null || linkValue.length()==0)
				return "----";
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
			if (doc instanceof EntryPoint){
				name = getTargetNameForEntryPoint((EntryPoint)doc, attributeName);
			}
		}catch(NoSuchDocumentException e){
			name = "*** DELETED ***";
		}catch(NoSuchPropertyException e){
			name = "*** NoProp ***";
		}catch(RuntimeException e){
			name = "*** ERR: "+e.getMessage()+" ***";
		}catch(ASGRuntimeException e){
			name = "*** ASG-ERR: "+e.getMessage()+" ***";
		}

		return name + " ["+linkValue+"]";
	}
	
	private String getTargetNameForNaviItem(NaviItem item, String attributeName) throws ASGRuntimeException{
		if (attributeName.equals("internalLink")){
			return webDataService.getPagex(item.getInternalLink()).getName();
		}
		return "Unknown attribute: "+attributeName;
		
	}
	
	private String getTargetNameForTemplate(PageTemplate template, String attributeName) throws ASGRuntimeException{
		if (attributeName.equals("site")){
			return siteDataService.getSite(template.getSite()).getName();
		}
		if (attributeName.equals("layout")){
			return layoutDataService.getPageLayout(template.getLayout()).getName();
		}
		return "Unknown attribute: "+attributeName;
	}
	
	private String getTargetNameForSite(Site site, String attributeName) throws ASGRuntimeException{
		if (attributeName.equals("startpage")){
			return webDataService.getPagex(site.getStartpage()).getName();
		}
		return "Unknown attribute: "+attributeName;
	}

	private String getTargetNameForEntryPoint(EntryPoint entry, String attributeName)throws ASGRuntimeException{
		if (attributeName.equals("startPage")){
			return webDataService.getPagex(entry.getStartPage()).getName();
		}
		
		if (attributeName.equals("startSite")){
			return siteDataService.getSite(entry.getStartSite()).getName();
		}
		
		return "Unknown attribute: "+attributeName;
	}

	private String getTargetNameForBox(Box box, String attributeName)throws ASGRuntimeException{
		if (attributeName.equals("handler")){
			return federatedDataService.getBoxHandlerDef(box.getHandler()).getName();
		}
		if (attributeName.equals("type")){
			return federatedDataService.getBoxType(box.getType()).getName();
		}
		return "UnknownAttr: "+attributeName;
	}

	private String getTargetNameForPage(Pagex page, String attributeName)throws ASGRuntimeException{
		if (attributeName.equals("template")){
			return siteDataService.getPageTemplate(page.getTemplate()).getName();
		}
		return "UnknownAttr: "+attributeName;
	}
	
}
