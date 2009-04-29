package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.anosite.gen.asresourcedata.data.Image;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceFactory;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;

public class ImageProcessor implements VariablesProcessor{
	private static IASResourceDataService service = ASResourceDataServiceFactory.createASResourceDataService();

	
	
	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		
		try{
			Image img = service.getImagesByProperty(Image.PROP_NAME, variable).get(0);
			String filepath = req.getContextPath()+"/file/"+img.getImage();
			String title = img.getTitle();
			
			return "<img src=\""+filepath+"\" alt=\""+title+"\" border=\"0\"/>";
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}



	
	
}
