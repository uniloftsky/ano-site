package net.anotheria.anosite.content.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.anotheria.anodoc.data.StringProperty;
import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.content.bean.BoxTypeBean;
import net.anotheria.anosite.content.bean.NaviItemBean;
import net.anotheria.anosite.content.bean.PageBean;
import net.anotheria.anosite.content.bean.SiteBean;
import net.anotheria.anosite.content.bean.StylesheetBean;
import net.anotheria.anosite.gen.data.Box;
import net.anotheria.anosite.gen.data.BoxType;
import net.anotheria.anosite.gen.data.NaviItem;
import net.anotheria.anosite.gen.data.PageTemplate;
import net.anotheria.anosite.gen.data.Pagex;
import net.anotheria.anosite.gen.data.PagexDocument;
import net.anotheria.anosite.gen.data.Site;
import net.anotheria.anosite.gen.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.service.ASMetaDataServiceFactory;
import net.anotheria.anosite.gen.service.ASWebDataServiceFactory;
import net.anotheria.anosite.gen.service.IASFederatedDataService;
import net.anotheria.anosite.gen.service.IASMetaDataService;
import net.anotheria.anosite.gen.service.IASWebDataService;
import net.anotheria.anosite.handler.BoxHandler;
import net.anotheria.anosite.handler.BoxHandlerFactory;
import net.java.dev.moskito.web.MoskitoHttpServlet;


public class ContentPageServlet extends MoskitoHttpServlet {

	private static Logger log = Logger.getLogger(ContentPageServlet.class);
	
	private IASWebDataService webDataService;
	private IASMetaDataService metaDataService;
	private IASFederatedDataService federatedDataService;

	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		
		webDataService = ASWebDataServiceFactory.createASWebDataService();
		metaDataService = ASMetaDataServiceFactory.createASMetaDataService();
		federatedDataService = ASFederatedDataServiceFactory.createASFederatedDataService();
	}

	
	private BoxBean createBoxBean(HttpServletRequest req,
			HttpServletResponse res, Box box) {
		System.out.println("creating box bean for box: " + box);
		BoxBean ret = new BoxBean();

		ret.setName(box.getName());
		ret.setId(box.getId());

		ret.setContent(box.getContent());
		ret.setParameter1(box.getParameter1());
		ret.setParameter2(box.getParameter2());
		ret.setParameter3(box.getParameter3());
		ret.setParameter4(box.getParameter4());
		ret.setParameter5(box.getParameter5());
		ret.setParameter6(box.getParameter6());
		ret.setParameter7(box.getParameter7());
		ret.setParameter8(box.getParameter8());
		ret.setParameter9(box.getParameter9());
		ret.setParameter10(box.getParameter10());

		ret.setType(createBoxTypeBean(box.getType()));

		if (box.getSubboxes() != null && box.getSubboxes().size() > 0) {
			ret.setSubboxes(createBoxBeanList(req, res, box.getSubboxes()));
		}

		if (box.getHandler() != null && box.getHandler().length() > 0) {
			BoxHandler handler = BoxHandlerFactory.createHandler(box
					.getHandler());
			handler.process(req, res, box, ret);
		}

		return ret;
	}

	private List<BoxBean> createBoxBeanList(HttpServletRequest req,
			HttpServletResponse res, List<StringProperty> boxIds) {
		ArrayList<BoxBean> ret = new ArrayList<BoxBean>();

		for (StringProperty p : boxIds) {
			ret.add(createBoxBean(req, res, webDataService
					.getBox(p.getString())));
		}

		return ret;
	}

	private BoxTypeBean createBoxTypeBean(String boxTypeId) {
		BoxType type = federatedDataService.getBoxType(boxTypeId);
		BoxTypeBean bean = new BoxTypeBean();
		bean.setName(type.getName());
		bean.setRenderer(type.getRendererpage());
		return bean;
	}

	private List<NaviItemBean> createNaviItemList(List<StringProperty> idList) {
		List<NaviItemBean> ret = new ArrayList<NaviItemBean>(idList.size());
		for (StringProperty p : idList) {
			String id = p.getString();
			NaviItemBean bean = new NaviItemBean();
			NaviItem item = webDataService.getNaviItem(id);
			bean.setPopup(item.getPopup());
			bean.setName(item.getName());
			bean.setTitle(item.getTitle());
			if (item.getExternalLink().length() > 0) {
				bean.setLink(item.getExternalLink());
			} else {
				if (item.getInternalLink().length() > 0) {
					String pageId = item.getInternalLink();
					bean.setLink(webDataService.getPagex(pageId).getName()
							+ ".html");
				} else {
					bean.setLink("#");
				}
			}
			ret.add(bean);
			List<StringProperty> subNaviIds = item.getSubNavi();
			if (subNaviIds.size() > 0)
				bean.setSubNavi(createNaviItemList(subNaviIds));
		}

		return ret;
	}

	private PageBean createPageBean(HttpServletRequest req,
			HttpServletResponse res, Pagex page) {
		PageBean ret = new PageBean();

		ret.setTitle(page.getTitle());
		ret.setName(page.getName());

		List<StringProperty> c1 = page.getC1();
		ret.setColumn1(createBoxBeanList(req, res, c1));
		List<StringProperty> c2 = page.getC2();
		ret.setColumn2(createBoxBeanList(req, res, c2));
		List<StringProperty> c3 = page.getC3();
		ret.setColumn3(createBoxBeanList(req, res, c3));
		System.out.println("\t c1: " + c1 + ", c2: " + c2 + ", c3: " + c3);

		return ret;
	}

	private SiteBean createSiteBean(PageTemplate template) {
		SiteBean ret = new SiteBean();

		try {
			Site site = metaDataService.getSite(template.getSite());
			ret.setSubtitle(site.getSubtitle());
			ret.setTitle(site.getTitle());
		} catch (Exception e) {
			log.warn("createSiteBean(" + template + ")", e);
		}

		return ret;
	}

	private String extractPageName(HttpServletRequest req) {
		String servletPath = req.getServletPath();
		if (servletPath.length() > 0 && servletPath.charAt(0) == '/')
			servletPath = servletPath.substring(1);
		int indexOfDot = servletPath.indexOf('.');
		if (indexOfDot != -1)
			servletPath = servletPath.substring(0, indexOfDot);
		return servletPath;
	}

	private Pagex getPageByName(String pageName) throws ServletException {
		try {
			return webDataService.getPagexsByProperty(PagexDocument.PROP_NAME,
					pageName).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new ServletException("Page " + pageName + " not found.");
	}


	@Override
	protected void moskitoDoGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String pageName = extractPageName(req);
		System.out.println("Page: " + pageName);
		Pagex page = getPageByName(pageName);
		System.out.println("Found page: " + page);

		PageTemplate template = metaDataService.getPageTemplate(page
				.getTemplate());
		System.out.println("Found template: " + template);

		req.setAttribute("stylesheet", new StylesheetBean(template.getStyle()));

		SiteBean siteBean = createSiteBean(template);
		req.setAttribute("site", siteBean);

		PageBean pageBean = createPageBean(req, res, page);
		if (pageBean.getTitle() == null || pageBean.getTitle().length() == 0)
			pageBean.setTitle(siteBean.getTitle());
		req.setAttribute("page", pageBean);

		// prepare navi
		Site site = metaDataService.getSite(template.getSite());
		List<NaviItemBean> topNavi = createNaviItemList(site.getTopNavi());
		// System.out.println("TOPNavi: "+topNavi);
		req.setAttribute("topNavi", topNavi);

		List<NaviItemBean> mainNavi = createNaviItemList(site.getMainNavi());
		// System.out.println("MainNavi: "+mainNavi);
		req.setAttribute("mainNavi", mainNavi);

		// navi end

		String layoutPage = template.getLayoutpage();
		if (!layoutPage.startsWith("/"))
			layoutPage = "/net/anotheria/anosite/layout/templates/"
					+ layoutPage;
		if (!layoutPage.endsWith(".jsp"))
			layoutPage += ".jsp";

		RequestDispatcher dispatcher = req.getRequestDispatcher(layoutPage);
		dispatcher.forward(req, res);

	}

}
