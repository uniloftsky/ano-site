package net.anotheria.anosite.guard;

import java.util.HashMap;
import java.util.Map;

import net.anotheria.anosite.gen.asfederateddata.data.GuardDef;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;

public class GuardFactory {
	private static Map<String, ConditionalGuard> guards = new HashMap<String, ConditionalGuard>();
	private static IASFederatedDataService service = ASFederatedDataServiceFactory.createASFederatedDataService();

	public static ConditionalGuard getConditionalGuard(String id){
		ConditionalGuard g = null;
		synchronized(guards){
			g = guards.get(id);
			if (g==null){
				try{
					GuardDef def = service.getGuardDef(id);
					g = (ConditionalGuard)Class.forName(def.getClazz()).newInstance();
					guards.put(id, g);
				}catch(Exception e){
					throw new RuntimeException("Couldn't retrieve guard definition: "+e.getMessage()+", gid: "+id);
				}
			}
		}//end synch
		return g;
	}
}
