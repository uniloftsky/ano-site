package net.anotheria.anosite.usersettings.bean;

import net.anotheria.webutils.bean.BaseActionForm;


public class EditUserSettingsForm extends BaseActionForm{

		
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private boolean languageFilteringEnabled;	
	private String[] supportedLanguages;
	private String[] displayedLanguages;
	private String referrer;
	
	
	
	public EditUserSettingsForm() {
		//super();
		//userId = null;
		languageFilteringEnabled = false;
		supportedLanguages = new String [] {};
		displayedLanguages = new String [] {};
		//referrerUrl = null;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean getLanguageFilteringEnabled() {
		return languageFilteringEnabled;
	}
	public void setLanguageFilteringEnabled(boolean languageFilteringEnabled) {
		this.languageFilteringEnabled = languageFilteringEnabled;
	}
	public String[] getSupportedLanguages() {
		return supportedLanguages;
	}
	public void setSupportedLanguages(String[] supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}
	public String[] getDisplayedLanguages() {
		return displayedLanguages;
	}
	public void setDisplayedLanguages(String[] displayedLanguages) {
		this.displayedLanguages = displayedLanguages;
	}
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	public String getReferrer() {
		return referrer;
	}
	
	
	
	

	
}
