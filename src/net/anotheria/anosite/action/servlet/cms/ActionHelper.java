package net.anotheria.anosite.action.servlet.cms;

import java.util.List;

import org.apache.log4j.Logger;

import net.anotheria.anodoc.query2.QueryProperty;
import net.anotheria.anosite.gen.ascustomaction.data.ActionMappingDef;
import net.anotheria.anosite.gen.ascustomaction.service.ASCustomActionServiceException;
import net.anotheria.anosite.gen.ascustomaction.service.ASCustomActionServiceFactory;
import net.anotheria.anosite.gen.ascustomaction.service.IASCustomActionService;
import net.anotheria.anosite.gen.aswebdata.data.Pagex;
import net.anotheria.anosite.gen.aswebdata.service.ASWebDataServiceFactory;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import net.anotheria.anosite.gen.aswebdata.service.rmi.ASWebDataServer;

public class ActionHelper {
	
	private static IASCustomActionService actionService = ASCustomActionServiceFactory.createASCustomActionService();
	private static IASWebDataService webdataService = ASWebDataServiceFactory.createASWebDataService();
	private static Logger log = Logger.getLogger(ActionHelper.class);
	
	public static ActionMappingDef lookupActionMapping(String name){
		try{
			List<ActionMappingDef> defs = actionService.getActionMappingDefsByProperty(ActionMappingDef.PROP_NAME, name);
			if (defs.size()>1){
				log.warn("Multiple mappings for name: "+name+", returning first, ("+defs+")");
				return defs.get(0);
			}
			return defs.size()>0 ? defs.get(0) : null;
		}catch(ASCustomActionServiceException e){
			log.error("lookupActionMapping("+name+")", e);
		}
		return null;
	}
	
	public static String getPageNameForAction(ActionMappingDef def){
		try{
			Pagex page = webdataService.getPagex(def.getPage());
			return page.getName();
		}catch(Exception e){
			log.error("getPageNameForAction("+def+")", e);
		}
		return null;
	}
}
