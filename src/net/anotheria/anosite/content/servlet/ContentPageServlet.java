package net.anotheria.anosite.content.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anodoc.data.NoSuchDocumentException;
import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.content.bean.BoxTypeBean;
import net.anotheria.anosite.content.bean.BreadCrumbItemBean;
import net.anotheria.anosite.content.bean.NaviItemBean;
import net.anotheria.anosite.content.bean.PageBean;
import net.anotheria.anosite.content.bean.SiteBean;
import net.anotheria.anosite.content.bean.StylesheetBean;
import net.anotheria.anosite.content.variables.VariablesUtility;
import net.anotheria.anosite.gen.asfederateddata.data.BoxType;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceException;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;
import net.anotheria.anosite.gen.aslayoutdata.service.ASLayoutDataServiceFactory;
import net.anotheria.anosite.gen.aslayoutdata.service.IASLayoutDataService;
import net.anotheria.anosite.gen.asresourcedata.data.TextResource;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceException;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceFactory;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.gen.assitedata.data.NaviItem;
import net.anotheria.anosite.gen.assitedata.data.PageTemplate;
import net.anotheria.anosite.gen.assitedata.data.Site;
import net.anotheria.anosite.gen.assitedata.service.ASSiteDataServiceException;
import net.anotheria.anosite.gen.assitedata.service.ASSiteDataServiceFactory;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.gen.aswebdata.data.Pagex;
import net.anotheria.anosite.gen.aswebdata.data.PagexDocument;
import net.anotheria.anosite.gen.aswebdata.service.ASWebDataServiceException;
import net.anotheria.anosite.gen.aswebdata.service.ASWebDataServiceFactory;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import net.anotheria.anosite.guard.ConditionalGuard;
import net.anotheria.anosite.guard.GuardFactory;
import net.anotheria.anosite.handler.AbstractRedirectResponse;
import net.anotheria.anosite.handler.BoxHandler;
import net.anotheria.anosite.handler.BoxHandlerFactory;
import net.anotheria.anosite.handler.BoxHandlerResponse;
import net.anotheria.anosite.handler.ResponseAbort;
import net.anotheria.anosite.handler.ResponseContinue;
import net.anotheria.anosite.handler.ResponseRedirectAfterProcessing;
import net.anotheria.anosite.handler.ResponseRedirectImmediately;
import net.anotheria.anosite.handler.ResponseStop;
import net.anotheria.anosite.shared.InternalResponseCode;
import net.anotheria.anosite.util.AnositeConstants;
import net.anotheria.asg.exception.ASGRuntimeException;
import net.java.dev.moskito.core.blueprint.BlueprintCallExecutor;
import net.java.dev.moskito.core.blueprint.BlueprintProducer;
import net.java.dev.moskito.core.blueprint.BlueprintProducersFactory;
import net.java.dev.moskito.web.MoskitoHttpServlet;

import org.apache.log4j.Logger;


public class ContentPageServlet extends MoskitoHttpServlet {

	private static Logger log = Logger.getLogger(ContentPageServlet.class);

	private IASWebDataService webDataService;
	private IASSiteDataService siteDataService;
	private IASFederatedDataService federatedDataService;
	private IASLayoutDataService layoutDataService;
	private IASResourceDataService resourceDataService;

	private BlueprintCallExecutor pageExecutor;
	private BlueprintCallExecutor boxExecutor;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		webDataService = ASWebDataServiceFactory.createASWebDataService();
		siteDataService = ASSiteDataServiceFactory.createASSiteDataService();
		federatedDataService = ASFederatedDataServiceFactory.createASFederatedDataService();
		layoutDataService = ASLayoutDataServiceFactory.createASLayoutDataService();
		resourceDataService = ASResourceDataServiceFactory.createASResourceDataService();
		
		pageExecutor = new PageBeanCreator();
		boxExecutor  = new BoxBeanCreator();
	}

	@Override
	protected void moskitoDoGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try{
			processRequest(req, res, false);
		}catch(ASGRuntimeException e){
			log.error("moskitoDoGet", e);
			throw new ServletException("ASG Runtime Exception: "+e.getMessage());
		}
	}

	@Override
	protected void moskitoDoPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try{
			processRequest(req, res, true);
		}catch(ASGRuntimeException e){
			log.error("moskitoDoPut", e);
			throw new ServletException("ASG Runtime Exception: "+e.getMessage());
		}
	}

	protected void processRequest(HttpServletRequest req, HttpServletResponse res, boolean submit) throws ServletException, IOException, ASGRuntimeException {

		prepareTextResources(req);

		String requestURI = req.getRequestURI();
		String queryString = req.getQueryString();
		if (queryString==null || queryString.length()==0)
			requestURI+="?dummy=dummy";
		else
			requestURI+="?"+queryString;
		req.setAttribute(AnositeConstants.RA_CURRENT_URI, requestURI);
		
		String pageName = extractPageName(req);
		Pagex page = getPageByName(pageName);
		
		//new check for https.
		boolean secure = req.isSecure();
		boolean secureRequired = page.getHttpsonly();
		
		if (!(secure==secureRequired)){
			String redirectTarget = secureRequired ? 
					"https://" : "http://";
			redirectTarget+= req.getServerName();
			redirectTarget+= requestURI;
			log.debug("making secure switch to "+redirectTarget);
			res.sendRedirect(redirectTarget);
			return;
		}
		
		// end check for https --->
		
		PageTemplate template = siteDataService.getPageTemplate(page.getTemplate());
		
		HashMap<String, BoxHandler> handlerCache = new HashMap<String, BoxHandler>();
		if (!submit){
			if ("true".equals(req.getParameter("submitFlag")))
				submit = true;
				
		}
		if (submit) {
			InternalResponse response = processSubmit(req, res, page, template, handlerCache);
			if (response.getCode()==InternalResponseCode.CONTINUE_AND_REDIRECT){
				res.sendRedirect(((InternalRedirectResponse)response).getUrl());
				return;
			}
			
			//TODO any further actions needed?
			if (!response.canContinue())
				return;
		}
		
		//ok, if we got sofar, we have at least continue and error or continue responses.
		
		
		//set the proper stylesheet
		req.setAttribute("stylesheet", new StylesheetBean(layoutDataService.getPageLayout(template.getLayout()).getStyle()));
		
		SiteBean siteBean = createSiteBean(template);
		req.setAttribute("site", siteBean);
		
		
		///////////////// PAGE START ////////////////////
		//OLD - InternalResponse pageResponse = createPageBean(req, res, page, template);
		InternalResponse pageResponse = null;
		BlueprintProducer pageProducer = BlueprintProducersFactory.getBlueprintProducer("Page-"+page.getId()+"-"+page.getName(), "page", AnositeConstants.AS_MOSKITO_SUBSYSTEM);
		try{
			pageResponse = (InternalResponse) pageProducer.execute(pageExecutor, req, res, page, template);
		}catch(Exception e){
			log.error(e);
			throw new ServletException(e);
		}
		///////////////// PAGE END //////////////////////
		if (!pageResponse.canContinue()){
			//todo log?
			return;
		}
		PageBean pageBean = ((InternalPageBeanResponse)pageResponse).getPageBean();
		if (pageBean.getTitle() == null || pageBean.getTitle().length() == 0)
			pageBean.setTitle(siteBean.getTitle());
		req.setAttribute("page", pageBean);
		
		// prepare navi
		Site site = siteDataService.getSite(template.getSite());
		List<NaviItemBean> topNavi = createNaviItemList(site.getTopNavi(), req);
		req.setAttribute("topNavi", topNavi);
		req.setAttribute("topNaviSize", topNavi.size());
		
		List<NaviItemBean> mainNavi = createNaviItemList(site.getMainNavi(), req);		
		req.setAttribute("mainNavi", mainNavi);
		req.setAttribute("mainNaviSize", mainNavi.size());
		// navi end
		
		//prepare breadcrumb
		List<BreadCrumbItemBean> breadcrumb = prepareBreadcrumb(page, site);
		req.setAttribute("breadcrumbs", breadcrumb);
		
		//execute the final redirect
		if (pageResponse.getCode()==InternalResponseCode.CONTINUE_AND_REDIRECT){
			String redirectUrl = ((InternalPageBeanWithRedirectResponse)pageResponse).getRedirectUrl();
			res.sendRedirect(redirectUrl);
			return;
		}
		
		String pageLayout = template.getLayout();
		String layoutPage = layoutDataService.getPageLayout(pageLayout).getLayoutpage();
		if (!layoutPage.startsWith("/"))
			layoutPage = "/net/anotheria/anosite/layout/templates/" + layoutPage;
		if (!layoutPage.endsWith(".jsp"))
			layoutPage += ".jsp";
		if (!"true".equals(req.getParameter(AnositeConstants.FLAG_XML_REQUEST)) && !res.isCommitted()) {
			RequestDispatcher dispatcher = req.getRequestDispatcher(layoutPage);
			dispatcher.forward(req, res);
		}
		
		
	}
	
	private List<String> getBoxIdsForRenderingStep(Pagex page, PageTemplate template, int step){
		switch(step){
		case 0:
			return template.getHeader();
		case 1: 
			return page.getHeader();
		case 2:
			return template.getC1first();
		case 3:
			return template.getC2first();
		case 4:
			return template.getC3first();
		case 5:
			return page.getC1();
		case 6:
			return page.getC2();
		case 7:
			return page.getC3();
		case 8:
			return template.getC1last();
		case 9:
			return template.getC2last();
		case 10:
			return template.getC3last();
		case 11:
			return template.getFooter();
		case 12: 
			return page.getFooter();
		}
		throw new RuntimeException("Error, step "+step+" is unknown!");
	}

	private InternalResponse processSubmit(HttpServletRequest req, HttpServletResponse res, Pagex page, PageTemplate template, HashMap<String, BoxHandler> handlerCache) throws ASGRuntimeException{
		
		InternalResponse progress = new InternalResponseContinue();
		int step = 0;
		while(progress.canContinue() && step<13){
			progress = processSubmit(req, res, getBoxIdsForRenderingStep(page, template, step), handlerCache, progress);
			step++;
		}
		log.debug("Returning at step: "+step+" : "+progress);
		return progress;
		
	
	}

	private InternalResponse processSubmit(HttpServletRequest req, HttpServletResponse res, List<String> boxIds, HashMap<String, BoxHandler> handlerCache, InternalResponse previous) throws ASGRuntimeException{
		if (boxIds == null || boxIds.size() == 0)
			return previous;
		boolean doRedirect = false;
		String redirectTarget = null;
		for (String id : boxIds) {
			Box box = webDataService.getBox(id);
			String handlerId = box.getHandler();
			
			
			if (handlerId != null && handlerId.length() > 0) {
				BoxHandler handler = BoxHandlerFactory.createHandler(handlerId);
				BoxHandlerResponse response = handler.submit(req, res, box);
				switch(response.getResponseCode()){
				case CONTINUE:
					break;
				case CONTINUE_AND_REDIRECT:
					if (!(previous instanceof InternalRedirectResponse)){
						doRedirect = true;
						redirectTarget = ((AbstractRedirectResponse)response).getRedirectTarget();
					}else{
						log.warn("box "+box+" trying to rewrite redirect, denied");
					}
					break;
				case CANCEL_AND_REDIRECT:
					redirectTarget = ((AbstractRedirectResponse)response).getRedirectTarget();
					try{
						res.sendRedirect(redirectTarget);
					}catch(IOException e){
						log.error("Redirect failed, target: "+redirectTarget, e);
					}
					//abort execution
					return new InternalResponse(InternalResponseCode.STOP);
				case STOP:
					return new InternalResponse(InternalResponseCode.STOP);
				case ABORT:
					Exception e = ((ResponseAbort)response).getCause();
					if (e == null)
						throw new RuntimeException("No exception given");
					if (e instanceof RuntimeException)
						throw (RuntimeException)e;
					throw new RuntimeException("Execution aborted: "+e.getMessage()+" ("+e.getClass());
						
				}
				handlerCache.put(box.getId(), handler);
			}
			List<String> subboxesIds = box.getSubboxes();
			InternalResponse subResponse = processSubmit(req, res, subboxesIds, handlerCache, previous);
			//a redirect from subbox can override a continue from upper box
			if (subResponse.getCode()==InternalResponseCode.CONTINUE_AND_REDIRECT && previous.getCode()==InternalResponseCode.CONTINUE)
				previous = subResponse;
			if(!subResponse.canContinue())
				return new InternalResponse(InternalResponseCode.STOP);
			
		}
		if (doRedirect){
			return new InternalRedirectResponse(redirectTarget);
		}
		return previous;
	}

	 
	private List<BreadCrumbItemBean> prepareBreadcrumb(Pagex page, Site site){
		List<BreadCrumbItemBean> ret = new ArrayList<BreadCrumbItemBean>();
		try{
			//first find navi item
			List<NaviItem> linkingItems = siteDataService.getNaviItemsByProperty(NaviItem.LINK_PROP_INTERNAL_LINK, page.getId());
			if (linkingItems.size()==0)
				return ret;
			NaviItem linkingItem = linkingItems.get(0);
			
			if (site.getStartpage().length()>0){
				linkingItems = siteDataService.getNaviItemsByProperty(NaviItem.LINK_PROP_INTERNAL_LINK, site.getStartpage());
				NaviItem linkToStartPage = linkingItems.get(0);
				
				BreadCrumbItemBean startpageBean = new BreadCrumbItemBean();
				startpageBean.setTitle(linkToStartPage.getName());
				startpageBean.setLink(webDataService.getPagex(linkToStartPage.getInternalLink()).getName()+".html");
				ret.add(startpageBean);
				if (linkToStartPage.equals(linkingItem)){
					startpageBean.setClickable(false);
					return ret;
				}else{
					startpageBean.setClickable(true);
				}
				
				//ok start page is found and we are not the startpage
				//now find other... this is now hardcored for two level navigation.
				List<BreadCrumbItemBean> items = new ArrayList<BreadCrumbItemBean>();
				while (linkingItem!=null){
					BreadCrumbItemBean b = new BreadCrumbItemBean();
					b.setClickable(items.size()>0);
					b.setTitle(linkingItem.getName());
					try{
						b.setLink(webDataService.getPagex(linkingItem.getInternalLink()).getName()+".html");
					}catch(NoSuchDocumentException e){
						b.setLink("");
						b.setClickable(false);
					}
					items.add(b);
					String searchId = linkingItem.getId();
					linkingItem = null;
					List<NaviItem> tosearch = siteDataService.getNaviItems();
					for (NaviItem i : tosearch){
						if (i.getSubNavi().contains(searchId)){
							linkingItem = i;
							break;
						}
					}
				}
				
				Collections.reverse(items);
				ret.addAll(items);
				
				
			}
		}catch(Exception e){
			BreadCrumbItemBean b = new BreadCrumbItemBean();
			b.setTitle("Error: "+e.getMessage());
			b.setClickable(false);
			ret.add(b);
		}
		
		return ret;
	}

	private InternalResponse createBoxBean(HttpServletRequest req, HttpServletResponse res, Box box) throws ASGRuntimeException{
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

		BoxHandlerResponse handlerResponse = null;
		
		// Firsts notify handler
		if (box.getHandler() != null && box.getHandler().length() > 0) {
			BoxHandler handler = BoxHandlerFactory.createHandler(box.getHandler());
			handlerResponse = handler.process(req, res, box, ret);
		}
		
		if (handlerResponse == null)
			handlerResponse = new ResponseContinue();
		
		if (handlerResponse.getResponseCode()==InternalResponseCode.CANCEL_AND_REDIRECT){
			try{
				res.sendRedirect(((ResponseRedirectImmediately)handlerResponse).getRedirectTarget());
			}catch(IOException ignored){
				ignored.printStackTrace();
			}
			handlerResponse = new ResponseStop();
		}

		InternalResponse response = null;
		
		switch(handlerResponse.getResponseCode()){
		case ERROR_AND_CONTINUE://TODO make an error bean later
		case CONTINUE:
			response = new InternalBoxBeanResponse(InternalResponseCode.CONTINUE, ret);
			break;
		case CONTINUE_AND_REDIRECT:
			response = new InternalBoxBeanWithRedirectResponse(ret, ((ResponseRedirectAfterProcessing)handlerResponse).getRedirectTarget());
			break;
		case STOP:
			response = new InternalResponse(handlerResponse);
			break;
		case ABORT:
			response = new InternalResponse(handlerResponse);
		}
		
		if (response==null){
			throw new RuntimeException("Unhandled handler response: "+handlerResponse);
		}
		
		if (!response.canContinue())
			return response;

		// Then create subboxes
		if (box.getSubboxes() != null && box.getSubboxes().size() > 0) {
			InternalResponse subBoxResponse = createBoxBeanList(req, res, box.getSubboxes());
			switch(subBoxResponse.getCode()){
			case ERROR_AND_CONTINUE:
			case CONTINUE:
				ret.setSubboxes(((InternalBoxBeanListResponse)subBoxResponse).getBeans());
				break;
			case STOP:
				return subBoxResponse;
			case ABORT:
				return subBoxResponse;
			case CONTINUE_AND_REDIRECT:
				ret.setSubboxes(((InternalBoxBeanListResponse)subBoxResponse).getBeans());
				if (response.getCode()!=InternalResponseCode.CONTINUE_AND_REDIRECT)
					response = new InternalBoxBeanWithRedirectResponse(ret, ((InternalBoxBeanListWithRedirectResponse)subBoxResponse).getRedirectUrl());
				break;
			}
		}
		
		return response;
	}

	private InternalResponse createBoxBeanList(HttpServletRequest req, HttpServletResponse res, List<String> boxIds) throws ASGRuntimeException{
		ArrayList<BoxBean> ret = new ArrayList<BoxBean>();
		String redirectUrl = null;
		for (String boxId : boxIds) {
			
			/// ADDED GUARDS ///
			Box box = webDataService.getBox(boxId);
			boolean do_break = false;
			//check the guards
			try{
				List<String> gIds = box.getGuards();
				for (String gid : gIds){
					ConditionalGuard g = GuardFactory.getConditionalGuard(gid);
					if (!g.isConditionFullfilled(box, req)){
						do_break = true;
						break;
					}
						
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if (do_break){
				continue;
			}
			
			
			/// END GUARDS HANDLING ///
			
			InternalResponse response = null;
			BlueprintProducer boxProducer = BlueprintProducersFactory.getBlueprintProducer("Box-"+box.getId()+"-"+box.getName(), "box", AnositeConstants.AS_MOSKITO_SUBSYSTEM);
			try{
				response = (InternalResponse)boxProducer.execute(boxExecutor, req, res, box);
			}catch(Exception e){
				log.error(e);
				throw new ASGRuntimeException(e);
			}

			
			
			
			if (!response.canContinue())
				return response;
			ret.add(((InternalBoxBeanResponse)response).getBean());
			if (response.getCode()==InternalResponseCode.CONTINUE_AND_REDIRECT && redirectUrl==null)
				redirectUrl = ((InternalBoxBeanListWithRedirectResponse)response).getRedirectUrl();
		}

		return redirectUrl == null ? 
				new InternalBoxBeanListResponse(ret) :
				new InternalBoxBeanListWithRedirectResponse(ret, redirectUrl);
	}

	private BoxTypeBean createBoxTypeBean(String boxTypeId) throws ASFederatedDataServiceException{
		BoxType type = federatedDataService.getBoxType(boxTypeId);
		BoxTypeBean bean = new BoxTypeBean();
		bean.setName(type.getName());
		bean.setRenderer(type.getRendererpage());
		return bean;
	}

	private List<NaviItemBean> createNaviItemList(List<String> idList, HttpServletRequest req) throws ASSiteDataServiceException, ASWebDataServiceException{
		List<NaviItemBean> ret = new ArrayList<NaviItemBean>(idList.size());
		for (String id : idList) {
			NaviItemBean bean = new NaviItemBean();
			NaviItem item = siteDataService.getNaviItem(id);
			
			boolean do_break = false;
			//check the guards
			try{
				List<String> gIds = item.getGuards();
				for (String gid : gIds){
					ConditionalGuard g = GuardFactory.getConditionalGuard(gid);
					if (!g.isConditionFullfilled(item, req)){
						do_break = true;
						break;
					}
						
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if (do_break){
				continue;
			}
			
			
			bean.setPopup(item.getPopup());
			bean.setName(item.getName());
			bean.setTitle(item.getTitle());
			if (item.getExternalLink().length() > 0) {
				bean.setLink(item.getExternalLink());
			} else {
				if (item.getInternalLink().length() > 0) {
					String pageId = item.getInternalLink();
					String pageName = webDataService.getPagex(pageId).getName();
					bean.setLink(pageName + ".html");
					if(extractPageName(req).equals(pageName))
						bean.setSelected(true);
				} else {
					bean.setLink("#");
				}
			}
			ret.add(bean);
			List<String> subNaviIds = item.getSubNavi();
			if (subNaviIds.size() > 0){
				List<NaviItemBean> subNavi = createNaviItemList(subNaviIds, req);
				if(!bean.isSelected())
					for(NaviItemBean subBean: subNavi)
						if(subBean.isSelected()){
							bean.setSelected(true);
							break;
						}
				bean.setSubNavi(subNavi);
			}
		}

		return ret;
	}

	private String getNewPageRedirectTargetIfApplies(InternalResponse response, String previousRedirectTarget){
		if (previousRedirectTarget!=null)
			return previousRedirectTarget;
		if (response.getCode()==InternalResponseCode.CONTINUE_AND_REDIRECT)
			return ((InternalBoxBeanListWithRedirectResponse)response).getRedirectUrl();
		return null;
	}
	
	private InternalResponse getNewPageResponse(InternalResponse current, InternalResponse previous){
		if (previous.getCode()==InternalResponseCode.CONTINUE_AND_REDIRECT)
			return previous;
		return current;
	}

	private InternalResponse createPageBean(HttpServletRequest req, HttpServletResponse res, Pagex page, PageTemplate template) throws ASGRuntimeException{
		PageBean ret = new PageBean();

		ret.setTitle(page.getTitle());
		ret.setName(page.getName());

		InternalResponse response = new InternalResponseContinue();
		
		InternalResponse call = null;
		String redirectTarget = null;
		
		//c1
		call = createBoxBeanList(req, res, template.getC1first());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addColumn1(((InternalBoxBeanListResponse)call).getBeans());
		
		call = createBoxBeanList(req, res, page.getC1());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addColumn1(((InternalBoxBeanListResponse)call).getBeans());
		
		call = createBoxBeanList(req, res, template.getC1last());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addColumn1(((InternalBoxBeanListResponse)call).getBeans());

		//c2
		call = createBoxBeanList(req, res, template.getC2first());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addColumn2(((InternalBoxBeanListResponse)call).getBeans());
		
		call = createBoxBeanList(req, res, page.getC2());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addColumn2(((InternalBoxBeanListResponse)call).getBeans());
		
		call = createBoxBeanList(req, res, template.getC2last());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addColumn2(((InternalBoxBeanListResponse)call).getBeans());
		
		//c3
		call = createBoxBeanList(req, res, template.getC3first());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addColumn3(((InternalBoxBeanListResponse)call).getBeans());
		
		call = createBoxBeanList(req, res, page.getC3());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addColumn3(((InternalBoxBeanListResponse)call).getBeans());
		
		call = createBoxBeanList(req, res, template.getC3last());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addColumn3(((InternalBoxBeanListResponse)call).getBeans());

		
		//header
		call = createBoxBeanList(req, res, template.getHeader());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addHeaderBoxes(((InternalBoxBeanListResponse)call).getBeans());
		
		call = createBoxBeanList(req, res, page.getHeader());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addHeaderBoxes(((InternalBoxBeanListResponse)call).getBeans());
		
		//footer
		call = createBoxBeanList(req, res, template.getFooter());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addFooterBoxes(((InternalBoxBeanListResponse)call).getBeans());
		
		call = createBoxBeanList(req, res, page.getFooter());
		if (!call.canContinue())
			return new InternalResponse(call.getCode());
		redirectTarget = getNewPageRedirectTargetIfApplies(call, redirectTarget);
		response = getNewPageResponse(call, response);
		ret.addFooterBoxes(((InternalBoxBeanListResponse)call).getBeans());

		
		return redirectTarget == null ? 
				new InternalPageBeanResponse(ret) :
				new InternalPageBeanWithRedirectResponse(ret, redirectTarget);

	}

	private SiteBean createSiteBean(PageTemplate template) {
		SiteBean ret = new SiteBean();

		try {
			Site site = siteDataService.getSite(template.getSite());
			ret.setSubtitle(site.getSubtitle());
			ret.setTitle(site.getTitle());
			ret.setLanguageSelector(site.getLanguageselector());
			if (site.getStartpage() != null && site.getStartpage().length() > 0)
				ret.setLinkToStartPage(webDataService.getPagex(site.getStartpage()).getName() + ".html");
			if (site.getSearchpage() != null && site.getSearchpage().length() > 0)
				ret.setSearchTarget(webDataService.getPagex(site.getSearchpage()).getName() + ".html");
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
			log.error("getPageByName("+pageName+")", e);
		}
		throw new ServletException("Page " + pageName + " not found.");
	}


	private void prepareTextResources(HttpServletRequest req) throws ASResourceDataServiceException {
		List<TextResource> resources = resourceDataService.getTextResources();
		for (TextResource r : resources)
			req.setAttribute("res." + r.getName(), VariablesUtility.replaceVariables(req, r.getValue()));
	}
	
	class PageBeanCreator implements BlueprintCallExecutor{
		public Object execute(Object... parameters)  throws ASGRuntimeException{
			return createPageBean( 
					(HttpServletRequest)parameters[0], 
					(HttpServletResponse)parameters[1], 
					(Pagex)parameters[2], 
					(PageTemplate)parameters[3]
			);
		}
	}
	class BoxBeanCreator implements BlueprintCallExecutor{
		public Object execute(Object... parameters)  throws ASGRuntimeException{
			return createBoxBean( 
					(HttpServletRequest)parameters[0], 
					(HttpServletResponse)parameters[1], 
					(Box)parameters[2] 
			);
		}
	}

}
