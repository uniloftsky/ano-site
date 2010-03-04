

package net.anotheria.anosite.usersettings.action;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anodoc.data.NoSuchDocumentException;
import net.anotheria.anosite.gen.shared.action.BaseActionsAction;
import net.anotheria.anosite.gen.usersettings.data.LanguageFilteringSettings;
import net.anotheria.anosite.gen.usersettings.data.LanguageFilteringSettingsFactory;
import net.anotheria.anosite.gen.usersettings.service.UserSettingsServiceException;
import net.anotheria.anosite.usersettings.bean.EditUserSettingsForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Edit user settings dialog action
 * @author vkazhdan
 */
public class EditUserSettingsDialogAction extends BaseActionsAction{

	
	public ActionForward anoDocExecute(ActionMapping mapping, ActionForm af,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		EditUserSettingsForm form = (EditUserSettingsForm) af;
		String forwardUrl;
		
		if (form != null) {
			
			String userId = (form.getUserId() != null) ? form.getUserId() : getUserId(req); 
			boolean create = false;
			
			LanguageFilteringSettings languageFilteringSettings = getLanguageFilteringSettingsByUserId(userId);
			if(languageFilteringSettings == null) {
				languageFilteringSettings = LanguageFilteringSettingsFactory.createLanguageFilteringSettings();
				languageFilteringSettings.setUserId(userId);
				create = true;
			}
			
			// Fill new data			
			languageFilteringSettings.setLanguageFilteringEnabled(form.getLanguageFilteringEnabled());
			languageFilteringSettings.setDisplayedLanguages(Arrays.asList(form.getDisplayedLanguages()));
			
			// Update record in database
			LanguageFilteringSettings updatedCopy;
			if (create){
				updatedCopy = getUserSettingsService().createLanguageFilteringSettings(languageFilteringSettings);
			}else{				
				updatedCopy = getUserSettingsService().updateLanguageFilteringSettings(languageFilteringSettings);
			}
						
			log.debug("Create/Update user settings. id: " + updatedCopy.getId() + " userId: " + userId );
			
			if( form.getReferrer() != null) {
				log.debug("Redirect to referrer: " + form.getReferrer());
				forwardUrl = form.getReferrer(); 			
			} else	{			
				log.debug("Referrer URL wasn't setted correctly. Redirect to site root.");
				forwardUrl = "";		
			}
			
		} else {
			log.warn("Can't update user settings. EditUserSettingsForm bean must be not null.");
			forwardUrl = "";
		}
		
		res.sendRedirect(forwardUrl);					
		
		return null;

	}
	
	@Override
	protected String getTitle() {		
		return "Edit User Settings Dialog Action";
	}

	
}
