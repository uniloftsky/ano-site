package net.anotheria.anosite.action;

import net.anotheria.anosite.action.servlet.cms.ActionHelper;
import net.anotheria.anosite.gen.ascustomaction.data.ActionMappingDef;
import net.anotheria.anosite.gen.shared.data.ActionCommandDefUtils;

public class ActionCommand {
	private String url;
	private String page;
	private CommandType type;
	private String parameters;
	
	public ActionCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public ActionCommand(ActionMappingDef def) {
		url = def.getUrl();
		page = ActionHelper.getPageNameForAction(def);
		parameters = def.getParameters();
		type = cms2enum(def.getCommand());
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public CommandType getType() {
		return type;
	}
	public void setType(CommandType type) {
		this.type = type;
	}
	
	public String getTarget(){
		String targetUrl =  url == null || url.trim().length() == 0 ? 
				getPageUrl() : url;
		if (parameters!=null && parameters.length()>0){
			targetUrl += targetUrl.indexOf('?') == -1 ? '?' : '&';
			targetUrl += parameters;
		}
		return targetUrl;
	}
	
	//fix this someday
	private String getPageUrl(){
		return getPage()+".html";
	}
	
	public static final CommandType cms2enum(int value){
		switch(value){
		case ActionCommandDefUtils.Forward:
			return CommandType.Forward;
		case ActionCommandDefUtils.Redirect:
			return CommandType.Redirect;
		}
		return CommandType.None;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	public String toString(){
		return getType()+" "+getTarget();
	}
	
	
}
