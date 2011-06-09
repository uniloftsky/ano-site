package net.anotheria.anosite.content.servlet;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asresourcedata.data.Image;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceException;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.util.StringUtils;
import net.anotheria.webutils.filehandling.servlet.FileDeliveryServlet;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet that delivers file by alias.
 *
 * @author dsilenko
 */
public class AliasFileDeliveryServlet extends FileDeliveryServlet {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -9048929437969384125L;

	/**
	 * Logger instance.
	 */
	private static Logger log = Logger.getLogger(AliasFileDeliveryServlet.class);

	/**
	 * Instance of IASResourceDataService.
	 */
	private IASResourceDataService service;

	public AliasFileDeliveryServlet() {
		super();

		try {
			service = MetaFactory.get(IASResourceDataService.class);
		} catch (MetaFactoryException e) {
			log.error("AliasFileDeliveryServlet()", e);
			throw new RuntimeException("AliasFileDeliveryServlet()", e);
		}
	}


	@Override
	protected String getRequestedResourceName(HttpServletRequest req) throws ServletException {
		String name = super.getRequestedResourceName(req);
		if (StringUtils.isEmpty(name))
			return null;

		try {

			for (Image image : service.getImages()) {
				if (image.getAlias().equals(name))
					return image.getImage();
			}

		} catch (ASResourceDataServiceException e) {
			log.error("getRequestedResourceName()", e);
			throw new ServletException("getRequestedResourceName()", e);
		}

		return null;
	}

}
