package net.anotheria.anosite.shared.presentation.listener;

import java.util.List;

import org.apache.log4j.Logger;

import net.anotheria.anosite.gen.asgenericdata.data.GenericGuardDef;
import net.anotheria.anosite.gen.asgenericdata.data.GenericGuardDefFactory;
import net.anotheria.anosite.gen.asgenericdata.service.ASGenericDataServiceFactory;
import net.anotheria.anosite.gen.asgenericdata.service.IASGenericDataService;
import net.anotheria.asg.exception.ASGRuntimeException;

/**
 * This utility class scans the data on start and ensures that all generic data is created.
 * @author lrosenberg
 *
 */
public final class CMSSelfTest {
	
	private static final IASGenericDataService genericDataService = ASGenericDataServiceFactory.createASGenericDataService();
	private static Logger log = Logger.getLogger(CMSSelfTest.class);
	
	public static final void performSelfTest(){
		log.info("%%% CMS SELF TEST STARTED %%%");
		selfTestGuards();
		log.info("%%% CMS SELF TEST FINISHED %%%");
	}
	
	private static void selfTestGuards(){
		ensureGuardExists("CMSLogedInGuard", "net.anotheria.anosite.guard.CMSLogedInGuard");
		ensureGuardExists("CMSLoggedOut", "net.anotheria.anosite.guard.CMSLoggedOutGuard");
		ensureGuardExists("InEditModeGuard", "net.anotheria.anosite.guard.InEditModeGuard");
		ensureGuardExists("NotInEditModeGuard", "net.anotheria.anosite.guard.NotInEditModeGuard");
		ensureGuardExists("System is Develop", "net.anotheria.anosite.guard.SystemIsDevelopGuard");
		ensureGuardExists("System is Production", "net.anotheria.anosite.guard.SystemIsProductionGuard");
		ensureGuardExists("System is Test", "net.anotheria.anosite.guard.SystemIsTestGuard");
	}
	
	private static void ensureGuardExists(String name, String clazz){
		try{
			List<GenericGuardDef> defs = genericDataService.getGenericGuardDefsByProperty(GenericGuardDef.PROP_NAME, name);
			if (defs.size()>0)
				return;
			log.info("%%% Creating GenericDataDef "+name+", clazz: "+clazz);
			GenericGuardDef newDef = GenericGuardDefFactory.createGenericGuardDef();
			newDef.setName(name);
			newDef.setClazz(clazz);
			genericDataService.createGenericGuardDef(newDef);
			log.info("%%% --> done, created "+newDef);
			
		}catch(ASGRuntimeException e){
			
		}
	}
}
