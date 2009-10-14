package net.anotheria.anosite.content.variables;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asresourcedata.data.FileLink;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;

import org.apache.log4j.Logger;

/**
 * Processor for links to files (for example PDF) which are uploaded via IASResourceDataService.
 * @author lrosenberg
 *
 */
public class FileLinkProcessor extends XLinkProcessor{
	/**
	 * Service to lookup files.
	 */
	private static IASResourceDataService service;
	static{
		try{
			service = MetaFactory.get(IASResourceDataService.class);
		}catch(MetaFactoryException e){
			Logger.getLogger(ImageLinkProcessor.class).fatal("Not properly initialized, can't find resource data service.");
		}
		
	}


	@Override
	protected String getFileName(String variable) {
		try{
			String ret = service.getFileLinksByProperty(FileLink.PROP_NAME, variable).get(0).getFile();
			if(ret == null)
				throw new RuntimeException("FileName is null!");
			return ret;
		}catch(Exception e){
			getLog().error("getFileName(" + variable + ") failure: ", e);
			return null;
		}
	}
}
