package net.anotheria.anosite.content.bean;

public class SiteBean {
	private String title;
	private String subtitle;
	private String keywords;
	private String description;
	private String linkToStartPage;
	private boolean languageSelector;
	private String searchTarget;
	
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLinkToStartPage() {
		return linkToStartPage;
	}
	public void setLinkToStartPage(String linkToStartPage) {
		this.linkToStartPage = linkToStartPage;
	}
	public boolean isLanguageSelector() {
		return languageSelector;
	}
	public void setLanguageSelector(boolean languageSelector) {
		this.languageSelector = languageSelector;
	}
	public boolean isShowSearchDialog() {
		return searchTarget!=null && searchTarget.length()>0;
	}
	public String getSearchTarget() {
		return searchTarget;
	}
	public void setSearchTarget(String searchTarget) {
		this.searchTarget = searchTarget;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
