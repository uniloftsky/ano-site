package net.anotheria.anosite.action.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.anosite.acess.AnoSiteAccessAPI;
import net.anotheria.anosite.action.Action;
import net.anotheria.anosite.action.ActionCommand;
import net.anotheria.anosite.action.ActionMapping;
import net.anotheria.anosite.action.servlet.cms.ActionFactory;
import net.anotheria.anosite.action.servlet.cms.ActionHelper;
import net.anotheria.anosite.gen.ascustomaction.data.ActionMappingDef;
import net.anotheria.anosite.shared.presentation.servlet.BaseAnoSiteServlet;
import net.anotheria.anosite.util.ModelObjectMapper;
import net.anotheria.util.StringUtils;

import org.apache.log4j.Logger;

public class ActionServlet extends BaseAnoSiteServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ActionServlet.class);
	
	/**
	 * {@link AnoSiteAccessAPI} instance.
	 */
	private AnoSiteAccessAPI accessAPI;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		log.info("Init ActionServlet");
		accessAPI = APIFinder.findAPI(AnoSiteAccessAPI.class);
	}

	@Override
	protected void moskitoDoGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		execute(req, res);
	}

	@Override
	protected void moskitoDoPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		execute(req, res);
	}
	
	private void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String actionMappingName = getActionMappingName(req);
		out("Calling actionmapping: "+actionMappingName);
		
		ActionMappingDef def = ActionHelper.lookupActionMapping(actionMappingName);
		out("Found def: "+def);
		
		if (def==null){
			log.warn("ActionMapping not found: "+actionMappingName);
			return;
		}
		
		// checking access
		try {
			if (!StringUtils.isEmpty(def.getAction()) && def.getAction().toLowerCase().startsWith("c-")) // applicable only for custom actions
				if (!accessAPI.isAllowedForAction(def.getAction().substring(2))) {
					res.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
		} catch (Exception e) {
			log.warn("Error in AccessAPI. ActionMappingDef: " + def + ", actionMappingName: " + actionMappingName + ")", e);
		}
		                                                                                                             
		//create mapping:
		ActionMapping mapping = new ActionMapping(def);
		
		
		Action action = ActionFactory.createAction(def);
		if (action==null){
			log.warn("Couldn't create an action instance...");
			return;
		}		
		
		ModelObjectMapper.map(req, action);
		try{
			ActionCommand ret = action.execute(req, res, mapping);
			out("Action returned: "+ret);
			if (ret==null)
				return;
			
			switch(ret.getType()){
			case Forward:
				return;
			case Redirect:
				res.sendRedirect(ret.getTarget());
				return;
			case None:

			}
			
		} catch(Exception e) {
			log.error("execute", e);
			//send to error page
		}
			
		
	}
	
	private String getActionMappingName(HttpServletRequest req){
		return extractArtifactName(req);
	}
	
	private void out(Object o){
		log.debug("[ActionServlet] "+o);
	}

}
