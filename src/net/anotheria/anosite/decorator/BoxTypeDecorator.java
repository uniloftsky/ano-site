package net.anotheria.anosite.decorator;

import net.anotheria.anodoc.data.NoSuchDocumentException;
import net.anotheria.anosite.gen.data.Box;
import net.anotheria.anosite.gen.data.BoxType;
import net.anotheria.anosite.gen.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.service.IASFederatedDataService;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.decorators.IAttributeDecorator;

public class BoxTypeDecorator implements IAttributeDecorator{
	
	private IASFederatedDataService federatedDataService = ASFederatedDataServiceFactory.createASFederatedDataService();

	/* (non-Javadoc)
	 * @see net.anotheria.asg.util.decorators.IAttributeDecorator#decorate(net.anotheria.anodoc.data.Document, java.lang.String, java.lang.String)
	 */
	public String decorate(DataObject doc, String attributeName, String rule) {
		String linkValue = ((Box)doc).getType();
		String name = null;
		try{
			BoxType targetType = federatedDataService.getBoxType(linkValue);
			name = targetType.getName();
		}catch(NoSuchDocumentException e){
			name = "*** DELETED ***";
		}catch(RuntimeException e){
			name = "*** ERR: "+e.getMessage()+ " ***";
		}
		return name + " ["+linkValue+"]";
	}

}