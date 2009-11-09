package net.anotheria.anosite.handler.def;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.content.bean.ImageResourceBean;
import net.anotheria.anosite.gen.asresourcedata.data.Image;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.handler.AbstractBoxHandler;
import net.anotheria.anosite.handler.BoxHandlerResponse;
import net.anotheria.asg.exception.ASGRuntimeException;

public class ImageBrowserHandler extends AbstractBoxHandler{

	public static final String ATTR_AS_IMAGE_RESOURCES = "asImageResources";
	
	@Override
	public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean) throws ASGRuntimeException {
		
		List<Image> imageResources = getResourceDataService().getImages();
		List<ImageResourceBean> imageResourceBeans = new ArrayList<ImageResourceBean>(imageResources.size());
		
		for(Image img: imageResources)
			imageResourceBeans.add(toImageResourceBean(req, img));
		
		req.setAttribute(ATTR_AS_IMAGE_RESOURCES, imageResourceBeans);
		return super.process(req, res, box, bean);
	}
	
	private ImageResourceBean toImageResourceBean(HttpServletRequest req, Image image){
		ImageResourceBean ret = new ImageResourceBean();
		ret.setId(image.getId());
		ret.setName(image.getName());
		ret.setTitle(image.getTitle());
		ret.setLink(req.getContextPath()+ "/file/" + image.getImage());
		return ret;
	}
	
}
