package net.anotheria.anosite.decorator;

import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.anotheria.anosite.gen.asresourcedata.data.Image;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.decorators.IAttributeDecorator;
import net.anotheria.webutils.filehandling.actions.FileStorage;

public class ImageSizeDecorator implements IAttributeDecorator {

	@Override
	public String decorate(DataObject doc, String attributeName, String rule) {
		if (doc instanceof Image)
			return processImage((Image)doc, attributeName, rule);
		return "";
	}

	private String processImage(Image img, String attributeName, String rule){
		String fileName = img.getImage();
		if (fileName==null || fileName.length()==0)
			return "No file";
		String message = null;
	
		try{
			File f = new File(FileStorage.fileStorageDir+File.separatorChar+fileName);
			if (!f.exists()){
				return "Missing "+fileName;
			}
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(fileName.substring(fileName.length()-3, fileName.length()));
			ImageReader reader = readers.next();
			ImageInputStream iis = ImageIO.createImageInputStream(f);
			reader.setInput(iis, false);
			int nImageCount = reader.getNumImages(true);
			if(nImageCount<1){
				return "Error: ImageReader found no images";
			}
			int h = reader.getHeight(0);
			int w = reader.getWidth(0);

			message = w+" x "+h+" pixel";
		}catch(Exception e){
			message = "Error: "+e.getMessage();
		}
		return message;
	}

}
