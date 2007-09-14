package net.anotheria.anosite.decorator;

import net.anotheria.anodoc.data.NoSuchDocumentException;
import net.anotheria.anosite.gen.data.Box;
import net.anotheria.anosite.gen.data.BoxType;
import net.anotheria.anosite.gen.data.PageTemplate;
import net.anotheria.anosite.gen.data.Pagex;
import net.anotheria.anosite.gen.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.service.ASMetaDataServiceFactory;
import net.anotheria.anosite.gen.service.IASFederatedDataService;
import net.anotheria.anosite.gen.service.IASMetaDataService;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.decorators.IAttributeDecorator;

public class LinkTargetNameDecorator implements IAttributeDecorator{
	private IASFederatedDataService federatedDataService = ASFederatedDataServiceFactory.createASFederatedDataService();
	private IASMetaDataService metaDataService = ASMetaDataServiceFactory.createASMetaDataService();

	/* (non-Javadoc)
	 * @see net.anotheria.asg.util.decorators.IAttributeDecorator#decorate(net.anotheria.anodoc.data.Document, java.lang.String, java.lang.String)
	 */
	public String decorate(DataObject doc, String attributeName, String rule) {
		String name = "Unknown";
		String linkValue = ""+ doc.getPropertyValue(attributeName);

		try{
			if (doc instanceof PageTemplate){
				name = getTargetNameForTemplate((PageTemplate)doc, attributeName);
			}
			if (doc instanceof Box){
				name = getTargetNameForBox((Box)doc, attributeName);
			}
			if (doc instanceof Pagex){
				name = getTargetNameForPage((Pagex)doc, attributeName);
			}
		}catch(NoSuchDocumentException e){
			name = "*** DELETED ***";
		}catch(RuntimeException e){
			name = "*** ERR: "+e.getMessage()+" ***";
		}

		return name + " ["+linkValue+"]";
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
