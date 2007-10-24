package net.anotheria.anosite.content.variables;

import net.anotheria.anosite.gen.asresourcedata.data.FileLink;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceFactory;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;

public class FileLinkProcessor extends XLinkProcessor{
	private static IASResourceDataService service = ASResourceDataServiceFactory.createASResourceDataService();

	@Override
	protected String getFileName(String variable) {
		try{
			return service.getFileLinksByProperty(FileLink.PROP_NAME, variable).get(0).getFile();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
}
