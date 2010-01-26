package net.anotheria.anosite.action.servlet.cms;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.ascustomaction.data.ActionMappingDef;
import net.anotheria.anosite.gen.ascustomaction.service.ASCustomActionServiceException;
import net.anotheria.anosite.gen.ascustomaction.service.IASCustomActionService;
import net.anotheria.anosite.gen.aswebdata.data.Pagex;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import org.apache.log4j.Logger;

import java.util.List;

public class ActionHelper {
	
	private static IASCustomActionService actionService;
	private static IASWebDataService webDataService;
	private static Logger log = Logger.getLogger(ActionHelper.class);

	static {
		try{
			actionService = MetaFactory.get(IASCustomActionService.class);
			webDataService = MetaFactory.get(IASWebDataService.class);
		} catch (MetaFactoryException e) {
			log.fatal("Services init failure", e);
		}
	}

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
			Pagex page = webDataService.getPagex(def.getPage());
			return page.getName();
		}catch(Exception e){
			log.error("getPageNameForAction("+def+")", e);
		}
		return null;
	}
}
