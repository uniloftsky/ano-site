package net.anotheria.anosite.content.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anodoc.data.StringProperty;
import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.content.bean.BoxTypeBean;
import net.anotheria.anosite.content.bean.NaviItemBean;
import net.anotheria.anosite.content.bean.PageBean;
import net.anotheria.anosite.content.bean.SiteBean;
import net.anotheria.anosite.content.bean.StylesheetBean;
import net.anotheria.anosite.content.variables.VariablesUtility;
import net.anotheria.anosite.gen.asfederateddata.data.BoxType;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;
import net.anotheria.anosite.gen.aslayoutdata.service.ASLayoutDataServiceFactory;
import net.anotheria.anosite.gen.aslayoutdata.service.IASLayoutDataService;
import net.anotheria.anosite.gen.asresourcedata.data.TextResource;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceFactory;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.gen.assitedata.data.NaviItem;
import net.anotheria.anosite.gen.assitedata.data.PageTemplate;
import net.anotheria.anosite.gen.assitedata.data.Site;
import net.anotheria.anosite.gen.assitedata.service.ASSiteDataServiceFactory;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.gen.aswebdata.data.Pagex;
import net.anotheria.anosite.gen.aswebdata.data.PagexDocument;
import net.anotheria.anosite.gen.aswebdata.service.ASWebDataServiceFactory;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import net.anotheria.anosite.handler.BoxHandler;
import net.anotheria.anosite.handler.BoxHandlerFactory;
import net.java.dev.moskito.web.MoskitoHttpServlet;

import org.apache.log4j.Logger;

public class ContentPageServlet extends MoskitoHttpServlet {

	private static Logger log = Logger.getLogger(ContentPageServlet.class);

	private IASWebDataService webDataService;
	private IASSiteDataService siteDataService;
	private IASFederatedDataService federatedDataService;
	private IASLayoutDataService layoutDataService;
	private IASResourceDataService resourceDataService;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		webDataService = ASWebDataServiceFactory.createASWebDataService();
		siteDataService = ASSiteDataServiceFactory.createASSiteDataService();
		federatedDataService = ASFederatedDataServiceFactory.createASFederatedDataService();
		layoutDataService = ASLayoutDataServiceFactory.createASLayoutDataService();
		resourceDataService = ASResourceDataServiceFactory.createASResourceDataService();
	}

	private BoxBean createBoxBean(HttpServletRequest req, HttpServletResponse res, Box box) {
		BoxBean ret = new BoxBean();

		ret.setName(box.getName());
		ret.setId(box.getId());

		ret.setContent(VariablesUtility.replaceVariables(req, box.getContent()));
		ret.setParameter1(VariablesUtility.replaceVariables(req, box.getParameter1()));
		ret.setParameter2(VariablesUtility.replaceVariables(req, box.getParameter2()));
		ret.setParameter3(VariablesUtility.replaceVariables(req, box.getParameter3()));
		ret.setParameter4(VariablesUtility.replaceVariables(req, box.getParameter4()));
		ret.setParameter5(VariablesUtility.replaceVariables(req, box.getParameter5()));
		ret.setParameter6(VariablesUtility.replaceVariables(req, box.getParameter6()));
		ret.setParameter7(VariablesUtility.replaceVariables(req, box.getParameter7()));
		ret.setParameter8(VariablesUtility.replaceVariables(req, box.getParameter8()));
		ret.setParameter9(VariablesUtility.replaceVariables(req, box.getParameter9()));
		ret.setParameter10(VariablesUtility.replaceVariables(req, box.getParameter10()));

		ret.setType(createBoxTypeBean(box.getType()));

		// Firsts notify handler
		if (box.getHandler() != null && box.getHandler().length() > 0) {
			BoxHandler handler = BoxHandlerFactory.createHandler(box.getHandler());
			handler.process(req, res, box, ret);
		}
		// Then create subboxes
		if (box.getSubboxes() != null && box.getSubboxes().size() > 0) {
			ret.setSubboxes(createBoxBeanList(req, res, box.getSubboxes()));
		}

		return ret;
	}

	private List<BoxBean> createBoxBeanList(HttpServletRequest req, HttpServletResponse res, List<String> boxIds) {
		ArrayList<BoxBean> ret = new ArrayList<BoxBean>();

		for (String boxId : boxIds) {
			ret.add(createBoxBean(req, res, webDataService.getBox(boxId)));
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

	private List<NaviItemBean> createNaviItemList(List<String> idList) {
		List<NaviItemBean> ret = new ArrayList<NaviItemBean>(idList.size());
		for (String id : idList) {
			NaviItemBean bean = new NaviItemBean();
			NaviItem item = siteDataService.getNaviItem(id);
			bean.setPopup(item.getPopup());
			bean.setName(item.getName());
			bean.setTitle(item.getTitle());
			if (item.getExternalLink().length() > 0) {
				bean.setLink(item.getExternalLink());
			} else {
				if (item.getInternalLink().length() > 0) {
					String pageId = item.getInternalLink();
					bean.setLink(webDataService.getPagex(pageId).getName() + ".html");
				} else {
					bean.setLink("#");
				}
			}
			ret.add(bean);
			List<String> subNaviIds = item.getSubNavi();
			if (subNaviIds.size() > 0)
				bean.setSubNavi(createNaviItemList(subNaviIds));
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	private PageBean createPageBean(HttpServletRequest req, HttpServletResponse res, Pagex page, PageTemplate template) {
		PageBean ret = new PageBean();

		ret.setTitle(page.getTitle());
		ret.setName(page.getName());

		ret.addColumn1(createBoxBeanList(req, res, template.getC1first()));
		ret.addColumn1(createBoxBeanList(req, res, page.getC1()));
		ret.addColumn1(createBoxBeanList(req, res, template.getC1last()));
		
		ret.addColumn2(createBoxBeanList(req, res, template.getC2first()));
		ret.addColumn2(createBoxBeanList(req, res, page.getC2()));
		ret.addColumn2(createBoxBeanList(req, res, template.getC2last()));

		ret.addColumn3(createBoxBeanList(req, res, template.getC3first()));
		ret.addColumn3(createBoxBeanList(req, res, page.getC3()));
		ret.addColumn3(createBoxBeanList(req, res, template.getC3last()));

		

		return ret;
	}

	private SiteBean createSiteBean(PageTemplate template) {
		SiteBean ret = new SiteBean();

		try {
			Site site = siteDataService.getSite(template.getSite());
			ret.setSubtitle(site.getSubtitle());
			ret.setTitle(site.getTitle());
			if (site.getStartpage() != null && site.getStartpage().length() > 0)
				ret.setLinkToStartPage(webDataService.getPagex(site.getStartpage()).getName() + ".html");
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
			return webDataService.getPagexsByProperty(PagexDocument.PROP_NAME, pageName).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new ServletException("Page " + pageName + " not found.");
	}

	@Override
	protected void moskitoDoGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		processRequest(req, res, false);
	}

	@Override
	protected void moskitoDoPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		processRequest(req, res, true);
	}

	private void processSubmit(HttpServletRequest req, HttpServletResponse res, Pagex page,
			HashMap<String, BoxHandler> handlerCache) {
		processSubmit(req, res, page.getC1(), handlerCache);
		processSubmit(req, res, page.getC2(), handlerCache);
		processSubmit(req, res, page.getC3(), handlerCache);

	}

	private void processSubmit(HttpServletRequest req, HttpServletResponse res, List<String> boxIds, HashMap<String, BoxHandler> handlerCache) {
		if (boxIds == null || boxIds.size() == 0)
			return;
		for (String id : boxIds) {
			Box box = webDataService.getBox(id);
			String handlerId = box.getHandler();
			if (handlerId != null && handlerId.length() > 0) {
				BoxHandler handler = BoxHandlerFactory.createHandler(handlerId);
				handler.submit(req, res, box);
				handlerCache.put(box.getId(), handler);
			}
			List<String> subboxesIds = box.getSubboxes();
			processSubmit(req, res, subboxesIds, handlerCache);
		}
	}

	protected void processRequest(HttpServletRequest req, HttpServletResponse res, boolean submit)
			throws ServletException, IOException {

		prepareTextResources(req);

		String pageName = extractPageName(req);
		System.out.println("Page: " + pageName);
		Pagex page = getPageByName(pageName);

		HashMap<String, BoxHandler> handlerCache = new HashMap<String, BoxHandler>();
		if (submit) {
			processSubmit(req, res, page, handlerCache);
		}

		PageTemplate template = siteDataService.getPageTemplate(page.getTemplate());

		req.setAttribute("stylesheet", new StylesheetBean("1"));// template.getLayout();getStyle()));

		SiteBean siteBean = createSiteBean(template);
		req.setAttribute("site", siteBean);

		PageBean pageBean = createPageBean(req, res, page, template);
		if (pageBean.getTitle() == null || pageBean.getTitle().length() == 0)
			pageBean.setTitle(siteBean.getTitle());
		req.setAttribute("page", pageBean);

		// prepare navi
		Site site = siteDataService.getSite(template.getSite());
		List<NaviItemBean> topNavi = createNaviItemList(site.getTopNavi());
		// System.out.println("TOPNavi: "+topNavi);
		req.setAttribute("topNavi", topNavi);

		List<NaviItemBean> mainNavi = createNaviItemList(site.getMainNavi());
		// System.out.println("MainNavi: "+mainNavi);
		req.setAttribute("mainNavi", mainNavi);

		// navi end

		String pageLayout = template.getLayout();
		String layoutPage = layoutDataService.getPageLayout(pageLayout).getLayoutpage();
		if (!layoutPage.startsWith("/"))
			layoutPage = "/net/anotheria/anosite/layout/templates/" + layoutPage;
		if (!layoutPage.endsWith(".jsp"))
			layoutPage += ".jsp";
		if (!res.isCommitted()) {
			RequestDispatcher dispatcher = req.getRequestDispatcher(layoutPage);
			dispatcher.forward(req, res);
		}
	}

	private void prepareTextResources(HttpServletRequest req) {
		List<TextResource> resources = resourceDataService.getTextResources();
		for (TextResource r : resources)
			req.setAttribute("res." + r.getName(), r.getValue());
	}

}
