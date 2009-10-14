package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asresourcedata.data.Image;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;

import org.apache.log4j.Logger;

public class ImageProcessor implements VariablesProcessor{
	private static IASResourceDataService service;
	static{
		try{
			service = MetaFactory.get(IASResourceDataService.class);
		}catch(MetaFactoryException e){
			Logger.getLogger(ImageLinkProcessor.class).fatal("Not properly initialized, can't find resource data service.");
		}
		
	}


	
	
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
