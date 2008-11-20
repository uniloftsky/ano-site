package net.anotheria.anosite.action.servlet.cms;

import net.anotheria.anosite.action.Action;
import net.anotheria.anosite.gen.asaction.data.ActionDef;
import net.anotheria.anosite.gen.asaction.service.ASActionServiceFactory;
import net.anotheria.anosite.gen.asaction.service.IASActionService;
import net.anotheria.anosite.gen.ascustomaction.data.ActionMappingDef;

public class ActionFactory {
	
	private static IASActionService service = ASActionServiceFactory.createASActionService();
	
	public static final Action createAction(ActionMappingDef def){
		String actionId = def.getAction();
		System.out.println("ActionID: "+actionId);
		
		
		try{
			ActionDef ad = service.getActionDef(actionId);
			String clazz = ad.getClazz();
			Action ret = (Action)Class.forName(clazz).newInstance();
			return ret;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;

	}
}
