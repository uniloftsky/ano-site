package net.anotheria.anosite.action.servlet.cms;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.action.Action;
import net.anotheria.anosite.gen.asaction.data.ActionDef;
import net.anotheria.anosite.gen.asaction.service.IASActionService;
import net.anotheria.anosite.gen.ascustomaction.data.ActionMappingDef;
import org.apache.log4j.Logger;

public class ActionFactory {
	
	private static IASActionService service ;
	
	private static Logger log = Logger.getLogger(ActionFactory.class);
	/**
	 * Init.
	 */
	static {
		try {
			service = MetaFactory.get(IASActionService.class);
		} catch (MetaFactoryException e) {
			log.fatal("IASActionService init failure",e);
		}
	}
	
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
