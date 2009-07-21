package net.anotheria.anosite.content.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anosite.gen.aslayoutdata.data.PageStyle;
import net.anotheria.anosite.gen.aslayoutdata.service.ASLayoutDataServiceException;
import net.anotheria.anosite.gen.aslayoutdata.service.ASLayoutDataServiceFactory;
import net.anotheria.anosite.gen.aslayoutdata.service.IASLayoutDataService;
import net.java.dev.moskito.web.MoskitoHttpServlet;
/**
 * This servlet is responsible for delivering the styles to the users browser.
 * @author lrosenberg
 */
public class StyleSheetServlet extends MoskitoHttpServlet{
	
	private IASLayoutDataService layoutDataService;
	
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		layoutDataService = ASLayoutDataServiceFactory.createASLayoutDataService();
	}

	@Override
	protected void moskitoDoGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String path = req.getPathInfo();
		if (path.length()>0 && path.charAt(0)=='/')
			path = path.substring(1);
		int indexOfDot = path.indexOf('.');
		if (indexOfDot!=-1)
			path = path.substring(0, indexOfDot);
		
		try{
			PageStyle style = layoutDataService.getPageStyle(path);
			OutputStream out = null;
			try{
				out = res.getOutputStream();
				out.write(style.getCss().getBytes());
				out.flush();
				out.close();
				out = null;
			}finally{
				if (out!=null){
					try{
						out.close();
					}catch(IOException ignored){}
				}
			}
		}catch(ASLayoutDataServiceException e){
			throw new ServletException(e);
		}
	}
}
