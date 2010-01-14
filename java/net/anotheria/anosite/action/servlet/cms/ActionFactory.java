package net.anotheria.anosite.action.servlet.cms;

import org.apache.log4j.Logger;

import net.anotheria.anosite.action.Action;
import net.anotheria.anosite.gen.asaction.data.ActionDef;
import net.anotheria.anosite.gen.asaction.service.ASActionServiceFactory;
import net.anotheria.anosite.gen.asaction.service.IASActionService;
import net.anotheria.anosite.gen.ascustomaction.data.ActionMappingDef;

public class ActionFactory {
	
	private static IASActionService service = ASActionServiceFactory.createASActionService();
	
	private static Logger log = Logger.getLogger(ActionFactory.class);
	
	public static Action createAction(ActionMappingDef def){
		String actionId = def.getAction();
		
		try{
			ActionDef ad = service.getActionDef(actionId);
			String clazz = ad.getClazz();
			return (Action)Class.forName(clazz).newInstance();
		}catch(Exception e){
			log.error("createAction("+def+")", e);
		}
		
		return null;

	}
}
