package net.anotheria.anosite.shared.presentation.listener;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.ascustomdata.data.*;
import net.anotheria.anosite.gen.ascustomdata.service.ASCustomDataServiceFactory;
import net.anotheria.anosite.gen.ascustomdata.service.IASCustomDataService;
import net.anotheria.anosite.gen.asgenericdata.data.*;
import net.anotheria.anosite.gen.asgenericdata.service.IASGenericDataService;
import net.anotheria.asg.exception.ASGRuntimeException;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * This utility class scans the data on start and ensures that all generic data is created.
 * @author lrosenberg
 *
 */
public final class CMSSelfTest {
	
	private static  IASGenericDataService genericDataService ;
	private static  IASCustomDataService  customDataService ;
	private static Logger log = Logger.getLogger(CMSSelfTest.class);

	static {
		try {
			genericDataService = MetaFactory.get(IASGenericDataService.class);
			customDataService = MetaFactory.get(IASCustomDataService.class);
		} catch (MetaFactoryException e) {
			log.fatal("ASG services init failure",e);
		}
	}
	
	public static final void performSelfTest(){
		log.info("%%% CMS SELF TEST STARTED %%%");
		selfTestGuards();
		selfTestBoxTypes();
		selfTestBoxHandlers();
		log.info("%%% CMS SELF TEST FINISHED %%%");
	}
	
	private static void selfTestBoxTypes(){
		ensureBoxTypeExists("GoogleAnalytics", "GoogleAnalytics");
		ensureBoxTypeExists("GoogleAnalyticsGA", "GoogleAnalyticsGA");
		ensureBoxTypeExists("IfSet", "IfSet");
		ensureBoxTypeExists("Plain", "Plain");
		ensureBoxTypeExists("Styled", "Styled");
		ensureBoxTypeExists("TextBox", "TextBox");
		ensureBoxTypeExists("CSSLink", "CSSLink");
		ensureBoxTypeExists("JSLink", "JSLink");
	}

	private static void selfTestBoxHandlers(){
		ensureBoxHandlerExists("RedirectImmediatelyHandler", "net.anotheria.anosite.handler.def.RedirectImmediatelyHandler");
	}

	private static void selfTestGuards(){
		ensureGuardExists("CMSLogedInGuard", "net.anotheria.anosite.guard.CMSLogedInGuard");
		ensureGuardExists("CMSLoggedOut", "net.anotheria.anosite.guard.CMSLoggedOutGuard");
		ensureGuardExists("InEditModeGuard", "net.anotheria.anosite.guard.InEditModeGuard");
		ensureGuardExists("NotInEditModeGuard", "net.anotheria.anosite.guard.NotInEditModeGuard");
		ensureGuardExists("System is Develop", "net.anotheria.anosite.guard.SystemIsDevelopGuard");
		ensureGuardExists("System is Production", "net.anotheria.anosite.guard.SystemIsProductionGuard");
		ensureGuardExists("System is Test", "net.anotheria.anosite.guard.SystemIsTestGuard");
		
		//context language guards
		ensureGuardExists("RussianContextLanguage", "net.anotheria.anosite.guard.ContextLanguageIsRussianGuard");
		ensureGuardExists("GermanContextLanguage", "net.anotheria.anosite.guard.ContextLanguageIsGermanGuard");
		ensureGuardExists("EnglishContextLanguage", "net.anotheria.anosite.guard.ContextLanguageIsEnglishGuard");
		
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
			log.error("ensureGuardExists("+name+", "+clazz+")", e);
			
		}
	}

	private static void ensureBoxHandlerExists(String name, String clazz){
		try{
			List<GenericBoxHandlerDef> defs = genericDataService.getGenericBoxHandlerDefsByProperty(GenericBoxHandlerDef.PROP_NAME, name);
			if (defs.size()>0)
				return;
			log.info("%%% Creating GenericBoxHandlerDef "+name+", clazz: "+clazz);
			GenericBoxHandlerDef newDef = GenericBoxHandlerDefFactory.createGenericBoxHandlerDef();
			newDef.setName(name);
			newDef.setClazz(clazz);
			genericDataService.createGenericBoxHandlerDef(newDef);
			log.info("%%% --> done, created "+newDef);
			
		}catch(ASGRuntimeException e){
			log.error("ensureBoxHandlerExists("+name+", "+clazz+")", e);
		}
	}

	private static void ensureBoxTypeExists(String name, String rendererpage){
		try{
			List<GenericBoxType> types = genericDataService.getGenericBoxTypesByProperty(GenericBoxType.PROP_NAME, name);
			if (types.size()>0)
				return;
			log.info("%%% Creating GenericBoxType "+name+", rendererpage: "+rendererpage);
			GenericBoxType newType = GenericBoxTypeFactory.createGenericBoxType();
			newType.setName(name);
			newType.setRendererpage(rendererpage);
			genericDataService.createGenericBoxType(newType);
			log.info("%%% --> done, created "+newType);
			
		}catch(ASGRuntimeException e){
			log.error("ensureBoxTypeExists("+name+", "+rendererpage+")", e);
		}
	}

	public static void ensureCustomGuardExists(String name, String clazz){
		try{
			List<CustomGuardDef> defs = customDataService.getCustomGuardDefsByProperty(CustomGuardDef.PROP_NAME, name);
			if (defs.size()>0)
				return;
			log.info("%%% Creating CustomDataDef "+name+", clazz: "+clazz);
			CustomGuardDef newDef = CustomGuardDefFactory.createCustomGuardDef();
			newDef.setName(name);
			newDef.setClazz(clazz);
			customDataService.createCustomGuardDef(newDef);
			log.info("%%% --> done, created "+newDef);
			
		}catch(ASGRuntimeException e){
			log.error("ensureGuardExists("+name+", "+clazz+")", e);
			
		}
	}

	public static void ensureCustomBoxHandlerExists(String name, String clazz){
		try{
			List<CustomBoxHandlerDef> defs = customDataService.getCustomBoxHandlerDefsByProperty(CustomBoxHandlerDef.PROP_NAME, name);
			if (defs.size()>0)
				return;
			log.info("%%% Creating CustomBoxHandlerDef "+name+", clazz: "+clazz);
			CustomBoxHandlerDef newDef = CustomBoxHandlerDefFactory.createCustomBoxHandlerDef();
			newDef.setName(name);
			newDef.setClazz(clazz);
			customDataService.createCustomBoxHandlerDef(newDef);
			log.info("%%% --> done, created "+newDef);
			
		}catch(ASGRuntimeException e){
			log.error("ensureBoxHandlerExists("+name+", "+clazz+")", e);
		}
	}

	public static void ensureCustomBoxTypeExists(String name, String rendererpage){
		try{
			List<CustomBoxType> types = customDataService.getCustomBoxTypesByProperty(CustomBoxType.PROP_NAME, name);
			if (types.size()>0)
				return;
			log.info("%%% Creating CustomBoxType "+name+", rendererpage: "+rendererpage);
			CustomBoxType newType = CustomBoxTypeFactory.createCustomBoxType();
			newType.setName(name);
			newType.setRendererpage(rendererpage);
			customDataService.createCustomBoxType(newType);
			log.info("%%% --> done, created "+newType);
			
		}catch(ASGRuntimeException e){
			log.error("ensureBoxTypeExists("+name+", "+rendererpage+")", e);
		}
	}
}
