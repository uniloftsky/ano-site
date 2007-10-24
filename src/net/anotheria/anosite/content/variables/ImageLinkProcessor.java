package net.anotheria.anosite.content.variables;

import net.anotheria.anosite.gen.asresourcedata.data.Image;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceFactory;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;

public class ImageLinkProcessor extends XLinkProcessor{
	private static IASResourceDataService service = ASResourceDataServiceFactory.createASResourceDataService();

	@Override
	protected String getFileName(String variable) {
		try{
			return service.getImagesByProperty(Image.PROP_NAME, variable).get(0).getImage();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
}
