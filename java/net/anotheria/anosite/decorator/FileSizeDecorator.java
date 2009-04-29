package net.anotheria.anosite.decorator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.anotheria.anosite.gen.asresourcedata.data.Image;
import net.anotheria.anosite.gen.asresourcedata.data.FileLink;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.decorators.IAttributeDecorator;
import net.anotheria.util.NumberUtils;
import net.anotheria.webutils.filehandling.actions.FileStorage;


/**
 * This decorator returns the size of the underlying ImageDocument and the contained image file name.
 * @author another
 *
 */
public class FileSizeDecorator implements IAttributeDecorator{
	
	public String decorate(DataObject doc, String attributeName, String rule) {
		if (doc instanceof Image)
			return processImage((Image)doc, attributeName, rule);
		if (doc instanceof FileLink)
			return processFile((FileLink)doc, attributeName, rule);
		return "";
	}
	
	private String processImage(Image img, String attributeName, String rule){
		String fileName = img.getImage();
		return processFile(fileName);
	}
	
	private String processFile(FileLink f, String attributeName, String rule){
		String fileName = f.getFile();
		return processFile(fileName);
	}

	private String processFile(String fileName){
		
		if (fileName==null || fileName.length()==0)
			return "No file";
		String message = null;
		FileInputStream fIn  = null;
		try{
			File f = new File(FileStorage.fileStorageDir+File.separatorChar+fileName);
			if (!f.exists()){
				message = "Missing "+fileName;
			}else{
				fIn = new FileInputStream(f);
				message = ""+NumberUtils.makeSizeString(fIn.available())+" "+fileName;
				fIn.close();
			}
		}catch(Exception e){
			message = "Error: "+e.getMessage();
			if (fIn!=null){
				try {
					fIn.close();
				}catch(IOException ignored){}
			}
		}
		return message;
	}
}

