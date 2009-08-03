package net.anotheria.anosite.guard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.anotheria.anosite.gen.asfederateddata.data.GuardDef;
import net.anotheria.anosite.gen.asfederateddata.service.ASFederatedDataServiceFactory;
import net.anotheria.anosite.gen.asfederateddata.service.IASFederatedDataService;

/**
 * A utility for guard instantiation.
 * @author another
 *
 */
public final class GuardFactory {
	/**
	 * Guard cache.
	 */
	private static Map<String, ConditionalGuard> guards = new ConcurrentHashMap<String, ConditionalGuard>();
	/**
	 * Federated data service for guard lookup.
	 */
	private static IASFederatedDataService service = ASFederatedDataServiceFactory.createASFederatedDataService();

	/**
	 * Creates a new conditional guards (or returns an existing one) by guards id.
	 * @param id
	 * @return
	 */
	public static ConditionalGuard getConditionalGuard(String id){
		ConditionalGuard g = null;
		g = guards.get(id);
		if (g==null){
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
		}
		return g;
	}
	
	/**
	 * Prevent instantiation.
	 */
	private GuardFactory(){
		
	}
}
