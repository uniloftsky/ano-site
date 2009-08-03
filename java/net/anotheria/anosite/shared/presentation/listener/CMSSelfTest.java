package net.anotheria.anosite.shared.presentation.listener;

import java.util.List;

import org.apache.log4j.Logger;

import net.anotheria.anosite.gen.asgenericdata.data.GenericBoxHandlerDef;
import net.anotheria.anosite.gen.asgenericdata.data.GenericBoxHandlerDefFactory;
import net.anotheria.anosite.gen.asgenericdata.data.GenericBoxType;
import net.anotheria.anosite.gen.asgenericdata.data.GenericBoxTypeFactory;
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

}
